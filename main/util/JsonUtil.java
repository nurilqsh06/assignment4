package main.util;

import java.io.*;
import main.model.GraphData;

public class JsonUtil {

    public static GraphData readGraphData(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line).append("\n");
        }
        reader.close();

        String json = jsonBuilder.toString();
        return parseJsonToGraphData(json);
    }

    private static GraphData parseJsonToGraphData(String json) {
        boolean directed = json.contains("\"directed\": true");
        int vertices = Integer.parseInt(extractValue(json, "\"n\":"));
        int source = Integer.parseInt(extractValue(json, "\"source\":"));
        String weightModel = extractValue(json, "\"weight_model\":").replaceAll("\"", "");

        GraphData graphData = new GraphData();

        String[] edgesData = json.split("\"edges\": \\[")[1].split("\\]")[0].split("\\}, \\{");
        for (String edge : edgesData) {
            int u = Integer.parseInt(extractValue(edge, "\"u\":"));
            int v = Integer.parseInt(extractValue(edge, "\"v\":"));
            int w = Integer.parseInt(extractValue(edge, "\"w\":"));
            graphData.addEdge(u, v, w);
        }

        return graphData;
    }

    private static String extractValue(String json, String key) {
        int start = json.indexOf(key) + key.length() + 1;
        int end = json.indexOf(",", start);
        if (end == -1) {
            end = json.indexOf("}", start);
        }
        return json.substring(start, end).trim();
    }
}
