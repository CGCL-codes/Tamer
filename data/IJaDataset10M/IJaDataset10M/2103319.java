package net.sourceforge.ondex.parser.psimi_lite;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * 
 * @author lysenkoa
 *
 */
public class DatabaseTranslator {

    private final HashMap<String, HashMap<String, String>> translation = new HashMap<String, HashMap<String, String>>();

    public void parseFile(String file) {
        HashMap<String, String> currentElement = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while (br.ready()) {
                String line = br.readLine().trim();
                if (line.length() == 0 || line.startsWith("#")) {
                    String catagory = line.substring(1, line.trim().length());
                    currentElement = new HashMap<String, String>();
                    translation.put(catagory, currentElement);
                    continue;
                }
                String[] elements = line.split("\t");
                if (elements.length == 2) {
                    currentElement.put(elements[0], elements[1]);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, HashMap<String, String>> getTranslation() {
        return translation;
    }
}
