package halo.util.validator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ValidateFileParser {

    private static final Map<String, Map<String, String>> vmap = new HashMap<String, Map<String, String>>();

    public static synchronized Map<String, String> parseFile(String filePath) {
        Map<String, String> map = vmap.get(filePath);
        if (map == null) {
            map = readFile(filePath);
            vmap.put(filePath, map);
        }
        return map;
    }

    private static Map<String, String> readFile(String filePath) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            String key = null;
            String json = null;
            while (line != null) {
                int idx = line.indexOf("=");
                key = line.substring(0, idx);
                json = line.substring(idx + 1);
                map.put(key, json);
                line = reader.readLine();
            }
            return map;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
