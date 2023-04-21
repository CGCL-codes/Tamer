package backend.parser.coryne3.cGlutamicum_parsers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class HomologousReader {

    private final String TAB = new String("\t");

    Hashtable homologous = new Hashtable();

    public Hashtable start(String homologesDir) {
        try {
            read(homologesDir + "blasted_genes.gb");
        } catch (IOException e) {
            Log.println("No homology-file found for genes: " + homologesDir + "blasted_genes.gb");
        }
        try {
            read(homologesDir + "blasted_proteins.gb");
        } catch (IOException e) {
            Log.println("No homology-file found for proteins: " + homologesDir + "blasted_genes.gb");
        }
        return homologous;
    }

    private void read(String fileName) throws IOException {
        BufferedReader br = myBufferedReader(fileName);
        String line = new String();
        while ((line = br.readLine()) != null) {
            if (line.trim().equalsIgnoreCase("")) {
                break;
            }
            String[] tokens = line.split(TAB);
            Homology h = new Homology();
            h.sourceID = (String) tokens[0].trim();
            h.targetID = (String) tokens[1].trim();
            if (!h.sourceID.equalsIgnoreCase(h.targetID)) {
                String eValue = (String) tokens[10];
                h.eValue = Double.parseDouble(eValue);
                String key = h.sourceID + "#" + h.targetID;
                Homology existing = (Homology) this.homologous.get(key);
                if (existing != null) {
                    if (existing.eValue > h.eValue) {
                        existing.eValue = h.eValue;
                    }
                } else {
                    this.homologous.put(key, h);
                }
            }
        }
    }

    private BufferedReader myBufferedReader(String file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        return br;
    }
}
