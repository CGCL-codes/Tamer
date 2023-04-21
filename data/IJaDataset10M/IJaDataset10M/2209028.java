package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import org.moyakarta.rest.PlaceGenerateUtils;

public class GPlacesPuter implements Callable<Long> {

    private final int num;

    private static final String URL_REST = "http://places-test-datastore.appspot.com/places";

    private final int numIterate;

    public GPlacesPuter(int num, int numIterate) {
        this.num = num;
        this.numIterate = numIterate;
    }

    @Override
    public Long call() throws Exception {
        System.out.println(">>> alexey: GPlacesPuter.call = entered = num = " + num);
        long sumTime = 0;
        for (int i = 0; i < numIterate; i++) {
            System.out.println(">>> alexey: GPlacesPuter.call num = " + num + " i = " + i);
            long spentTime = putPLace(i);
            sumTime += spentTime;
        }
        long avgTime = sumTime / numIterate;
        System.out.println(">>> alexey: GPlacesPuter.call avgTime num = " + num + " = " + avgTime);
        return avgTime;
    }

    private long putPLace(int i) throws IOException {
        long startTime = System.currentTimeMillis();
        URL url = new URL(URL_REST);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-type", "application/json");
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        String lng = Double.toString(PlaceGenerateUtils.randomDouble(31.9407876f, 32.1612008f));
        String lat = Double.toString(PlaceGenerateUtils.randomDouble(49.3694599f, 49.4657723f));
        String content = "{" + "\"name\": \"nameABC_" + i + "\"" + ", " + "\"mlng\": " + lng + ", \"mlat\": " + lat + ", " + "\"lng\": " + lng + ", \"lat\": " + lat + ", " + "\"zoom\": 15, " + "\"mapProvider\": \"google\", " + "\"mapType\": \"sat\", " + "\"mapW\": 854, " + "\"mapH\": 480, " + "\"createdBy\": \"createdBy\" " + "}";
        out.write(content);
        out.close();
        conn.getInputStream();
        long time = System.currentTimeMillis() - startTime;
        return time;
    }

    private String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }
}
