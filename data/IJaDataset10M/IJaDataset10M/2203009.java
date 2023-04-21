package com.homedepot.provisioning.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class will handle text files that are used for user input and output.
 */
public class Text {

    public ArrayList<String> readStoreList(String aFileName) {
        ArrayList<String> stores = new ArrayList<String>();
        File fFile = new File(aFileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fFile));
            String text = null;
            while ((text = reader.readLine()) != null) {
                text = lrtrim(text);
                if (Integer.parseInt(text) < 1000) stores.add("swpa0" + text); else stores.add("swpa" + text);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error generated" + e);
        } catch (IOException e) {
            System.out.println("Error generated" + e);
        }
        return stores;
    }

    public void writeOutputFile(String fileName) {
        return;
    }

    public static String ltrim(String source) {
        return source.replaceAll("^\\s+", "");
    }

    public static String rtrim(String source) {
        return source.replaceAll("\\s+$", "");
    }

    public static String lrtrim(String source) {
        return ltrim(rtrim(source));
    }
}
