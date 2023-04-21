package at.rc.tacos.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts a CSV File into a list of maps
 * 
 * @author Harald Pittesser
 * @version 0.1 alpha
 */
public class CSVParser {

    private static CSVParser instance = null;

    /**
	 * Returns an instance of singleton CSVParser
	 * 
	 * @return instance of CSVParser
	 */
    public static CSVParser getInstance() {
        if (instance == null) {
            instance = new CSVParser();
        }
        return instance;
    }

    /**
	 * parses the csv file
	 * 
	 * @param file
	 * @return List of maps <element's name, element>
	 */
    public List<Map<String, Object>> parseCSV(File file) throws Exception {
        List<Map<String, Object>> elementList = new ArrayList<Map<String, Object>>();
        List<String> elementNames = null, elementsInLine = null;
        String tempLine;
        int lineCount = 0;
        BufferedReader in = new BufferedReader(new FileReader(file));
        while ((tempLine = in.readLine()) != null) {
            if (lineCount == 0) {
                elementNames = Arrays.asList(tempLine.split(";"));
            } else {
                Map<String, Object> elements = new HashMap<String, Object>();
                elementsInLine = Arrays.asList(tempLine.split(";"));
                for (int i = 0; i < elementsInLine.size(); i++) {
                    elements.put(elementNames.get(i), elementsInLine.get(i));
                }
                elementList.add(elements);
            }
            lineCount++;
        }
        return elementList;
    }
}
