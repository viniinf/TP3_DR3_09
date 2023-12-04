package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            // Faz a requisição para a API questao 09
            String apiUrl = "http://universities.hipolabs.com/search?country=Brazil";
            String jsonResponse = sendGetRequest(apiUrl);

            // Parseia a resposta JSON
            List<Universidade> universidades = parseJsonResponse(jsonResponse);

            // Imprime a lista de universidades
            for (Universidade universidade : universidades) {
                System.out.println("Nome: " + universidade.getNome());
                System.out.println("URL: " + universidade.getUrl());
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String sendGetRequest(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    private static List<Universidade> parseJsonResponse(String jsonResponse) {
        List<Universidade> universidades = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            Iterator<JsonNode> elements = root.elements();

            while (elements.hasNext()) {
                JsonNode node = elements.next();
                String nome = node.get("name").asText();

                // O campo "web_pages" é um array o primeiro elemento será pego se existir
                JsonNode webPagesArray = node.get("web_pages");
                String url = webPagesArray.isArray() && webPagesArray.size() > 0
                        ? webPagesArray.get(0).asText()
                        : "";

                Universidade universidade = new Universidade(nome, url);
                universidades.add(universidade);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return universidades;
    }
}

class Universidade {
    private String nome;
    private String url;

    public Universidade(String nome, String url) {
        this.nome = nome;
        this.url = url;
    }

    public String getNome() {
        return nome;
    }

    public String getUrl() {
        return url;
    }
}