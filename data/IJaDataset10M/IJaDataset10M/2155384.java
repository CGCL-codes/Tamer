package it.infodea.tapestrydea.support;

import it.infodea.tapestrydea.support.interfaces.Identifiable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DeaUtils {

    public static String capitalize(String value) {
        if (value.length() == 0) return value;
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }

    public static boolean isEmpty(Identifiable identifiable) {
        return identifiable == null || identifiable.getIdValue() == null;
    }

    public static boolean isEmpty(String value) {
        return value == null || value.equals("");
    }

    public static String fileToString(String filePath) throws IOException {
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (IOException e) {
            throw e;
        } finally {
            close(is);
        }
    }

    private static void close(InputStream is) throws IOException {
        if (is != null) {
            is.close();
        }
    }
}
