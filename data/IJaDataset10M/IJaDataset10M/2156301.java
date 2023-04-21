package test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.xml.bind.JAXBException;
import sk.savba.ui.ie.results.Result;
import sk.savba.ui.ie.results.ResultSet;
import sk.savba.ui.ie.xmlregex.XMLRegexPattern;
import sk.savba.ui.ie.xmlregex.XMLRegexPatternParser;

public class XMLRegex {

    private static void usage() {
        System.out.println("Usage: " + XMLRegex.class.getName() + " <path to patterns xml file> <file to parse>");
        System.exit(1);
    }

    public static String getFileContent(String filename, String charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), charset));
        StringBuilder writer = new StringBuilder();
        String line = null;
        String newline = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            writer.append(line + newline);
        }
        reader.close();
        return writer.toString();
    }

    public static void example(XMLRegexPatternParser parser, String text) {
        ResultSet results = new ResultSet();
        for (XMLRegexPattern p : parser.getPatterns()) {
            results.getResult().addAll(p.annotate(text));
        }
        for (Result result : results.getSortedResults()) {
            System.out.println(result.getPosition().toString() + ": " + result.getKey() + " => " + result.getValue());
        }
    }

    /**
	 * @param args
	 * @throws JAXBException
	 * @throws IOException
	 */
    public static void main(String[] args) throws JAXBException, IOException {
        String patternFile = null;
        String inputFile = null;
        if (args.length != 2) {
            usage();
            return;
        }
        patternFile = args[0];
        inputFile = args[1];
        String text = getFileContent(inputFile, "UTF-8");
        XMLRegexPatternParser parser = new XMLRegexPatternParser();
        parser.parse(patternFile);
        parser.compile();
        example(parser, text);
    }
}
