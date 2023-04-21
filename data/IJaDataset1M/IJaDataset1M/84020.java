package org.opencarto.fusion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Test {

    public static void main(String[] args) {
        try {
            String att = "scale,geometry";
            int id = 1277845;
            String locationColumn = "geometry";
            double lonMin = 8;
            double lonMax = 9;
            double latMin = 45;
            double latMax = 46;
            String st = "https://www.google.com/fusiontables/api/query?sql=SELECT%20" + att + "%20FROM%20" + id + "%20WHERE%20ST_INTERSECTS(" + locationColumn + ",%20RECTANGLE(LATLNG(" + latMin + "," + lonMin + "),%20LATLNG(" + latMax + "," + lonMax + ")))";
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL(st).openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) System.out.println(inputLine);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
