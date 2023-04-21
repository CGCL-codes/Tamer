package root.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Localizador {

    private static final String urlBase = "http://maps.google.com/maps/api/geocode/json?";

    public static double[] getCoordenadas(String endereco) {
        double[] coordenadas = { 0.0, 0.0 };
        StringBuilder searchThisString = new StringBuilder();
        searchThisString.append(urlBase);
        searchThisString.append("address=");
        endereco = endereco.replaceAll(" ", "+");
        searchThisString.append(endereco);
        searchThisString.append("&sensor=false");
        InputStream input;
        try {
            input = getStreamOfConnection(searchThisString.toString());
            String data = getData(input);
            String latitude = data.split("\"location\"")[1].split(",")[0].split(":")[2].replace("}", "").trim();
            String longitude = data.split("\"location\"")[1].split(",")[1].split(":")[1].replace("}", "").trim();
            coordenadas[0] = Double.parseDouble(latitude);
            coordenadas[1] = Double.parseDouble(longitude);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return coordenadas;
    }

    private static InputStream getStreamOfConnection(String requestUrl) throws MalformedURLException, IOException {
        URL url = new URL(requestUrl.toString());
        URLConnection connection = url.openConnection();
        return connection.getInputStream();
    }

    private static String getData(InputStream input) throws IOException {
        StringBuilder stringJSON = new StringBuilder();
        try {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
            String line = "";
            while ((line = buffer.readLine()) != null) {
                stringJSON.append(line);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return stringJSON.toString();
    }

    public static double calculaDistancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2)) + (Math.pow(y2 - y1, 2));
    }
}
