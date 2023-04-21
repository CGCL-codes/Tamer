package kegg.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import kegg.KeggParser;

/**
 * @author taubertj
 *
 */
public class TaxidMapping {

    public static Hashtable<String, String> mapping;

    static {
        createTaxidMapping();
    }

    private static void createTaxidMapping() {
        mapping = new Hashtable<String, String>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(KeggParser.pathToGenome));
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        if (in != null) {
            String org = "";
            boolean inEntry = false;
            try {
                while (in.ready()) {
                    String line = in.readLine();
                    if (line.startsWith("ENTRY")) {
                        inEntry = true;
                        org = line.substring(5, line.length()).trim();
                    } else if (inEntry && line.startsWith("TAXONOMY")) {
                        String taxid = line.substring(8, line.length()).trim();
                        taxid = taxid.substring(4, taxid.length()).trim();
                        mapping.put(org, taxid);
                    } else if (inEntry && line.startsWith("///")) {
                        inEntry = false;
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
