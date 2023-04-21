package org.wltea.analyzer.help;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {

    public static InputStream loadFile(String path) {
        InputStream input = null;
        try {
            input = new FileInputStream(path);
        } catch (Exception e) {
        }
        if (input != null) {
            return input;
        }
        input = Utils.class.getResourceAsStream(path);
        return input;
    }

    public static String readFile(final String filePath) {
        final StringBuilder sBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                final FileReader fileReader = new FileReader(filePath);
                bufferedReader = new BufferedReader(fileReader);
            } else {
                InputStream stream = Utils.class.getResourceAsStream(filePath);
                InputStreamReader reader = new InputStreamReader(stream);
                bufferedReader = new BufferedReader(reader);
            }
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sBuilder.append(line);
                sBuilder.append("\n");
            }
            return sBuilder.toString();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
