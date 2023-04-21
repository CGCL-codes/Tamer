package playground.andreas.bln.ana;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.matsim.core.utils.io.tabularFileParser.TabularFileHandler;
import org.matsim.core.utils.io.tabularFileParser.TabularFileParser;
import org.matsim.core.utils.io.tabularFileParser.TabularFileParserConfig;

/**
 * Compare two Linkstats files
 *
 * @author aneumann
 *
 */
public class EvaluateLinkstats implements TabularFileHandler {

    private static final Logger log = Logger.getLogger(EvaluateLinkstats.class);

    private TabularFileParserConfig tabFileParserConfig;

    private HashMap<String, ArrayList<Double>> linkMapWithCounts = new HashMap<String, ArrayList<Double>>();

    public void startRow(String[] row) throws IllegalArgumentException {
        if (row[0].contains("LINK")) {
            StringBuffer tempBuffer = new StringBuffer();
            for (String string : row) {
                tempBuffer.append(string);
                tempBuffer.append(", ");
            }
            log.info("Ignoring: " + tempBuffer);
        } else {
            try {
                if (this.linkMapWithCounts.get(row[0]) == null) {
                    this.linkMapWithCounts.put(row[0], new ArrayList<Double>());
                }
                ArrayList<Double> tempList = this.linkMapWithCounts.get(row[0]);
                for (int entry = 8; entry < 80; entry += 3) {
                    tempList.add(Double.valueOf(row[entry]));
                }
            } catch (Exception e) {
            }
        }
    }

    public static HashMap<String, ArrayList<Double>> readFile(String filename) throws IOException {
        EvaluateLinkstats personReader = new EvaluateLinkstats();
        personReader.tabFileParserConfig = new TabularFileParserConfig();
        personReader.tabFileParserConfig.setFileName(filename);
        personReader.tabFileParserConfig.setDelimiterTags(new String[] { "\t" });
        new TabularFileParser().parse(personReader.tabFileParserConfig, personReader);
        return personReader.linkMapWithCounts;
    }

    public static HashMap<String, ArrayList<Double>> compareLinkstatFiles(String filename1, String filename2) {
        HashMap<String, ArrayList<Double>> outputEvent1 = null;
        HashMap<String, ArrayList<Double>> outputEvent2 = null;
        HashMap<String, ArrayList<Double>> compareMap = new HashMap<String, ArrayList<Double>>();
        try {
            outputEvent1 = EvaluateLinkstats.readFile(filename1);
            outputEvent2 = EvaluateLinkstats.readFile(filename2);
            for (String string : outputEvent1.keySet()) {
                ArrayList<Double> tempList = new ArrayList<Double>();
                if (outputEvent2.containsKey(string)) {
                    for (int i = 0; i < outputEvent2.get(string).size(); i++) {
                        tempList.add(Double.valueOf((outputEvent1.get(string).get(i)).doubleValue() - (outputEvent2.get(string).get(i)).doubleValue()));
                    }
                    compareMap.put(string, tempList);
                } else {
                    for (int i = 0; i < outputEvent1.get(string).size(); i++) {
                        tempList.add(Double.valueOf((outputEvent1.get(string).get(i)).doubleValue()));
                    }
                    compareMap.put(string, tempList);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compareMap;
    }

    public static void main(String[] args) {
        System.out.print("Wait");
    }
}
