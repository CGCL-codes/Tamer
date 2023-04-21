package glowaxes.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Test class
 */
public class Main {

    /**
	 * @param args
	 *            the command line arguments
	 */
    public static void main(String[] args) {
        try {
            String[] files = { "/Users/eddie/Downloads/JSONReader/Spec/2011-11-01-json.txt", "/Users/eddie/Downloads/JSONReader/Spec/2011-11-02-json.txt", "/Users/eddie/Downloads/JSONReader/Spec/2011-11-03-json.txt", "/Users/eddie/Downloads/JSONReader/Spec/2011-11-04-json.txt", "/Users/eddie/Downloads/JSONReader/Spec/2011-11-05-json.txt" };
            ArrayList<String> jsonStrings = new ArrayList<String>();
            for (String file : files) {
                jsonStrings.add(Parser.readFileAsString(file).trim().replace("�", "").replace("�", ""));
            }
            String[] summableFields = { "sessions ", "pvv", "(pv)" };
            Parser parser = new Parser("site", "No.", "sessions ", new HashSet(Arrays.asList(summableFields)));
            parser.parse(jsonStrings);
            System.out.println(parser.getSum());
            System.out.println(parser.getAvg());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
