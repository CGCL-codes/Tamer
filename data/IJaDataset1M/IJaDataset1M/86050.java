package genomemap.provider.impl.compldata;

import commons.provider.DefaultBaseProvider;
import commons.provider.ProviderException;
import commons.util.FreqDist;
import commons.util.Util;
import genomemap.data.provider.ComplDataProvider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @since
 * @author Susanta Tewari
 */
public class CSVComplDataProviderImpl extends DefaultBaseProvider<Map<String, Set<String>>> implements ComplDataProvider {

    private File dataFile;

    @Override
    public Map<String, Set<String>> create() throws ProviderException {
        List<String> lines = null;
        try {
            lines = Util.IO.readLines(dataFile);
        } catch (FileNotFoundException ex) {
            throw new ProviderException("", ex);
        } catch (IOException ex) {
            throw new ProviderException("", ex);
        }
        Map<String, Set<String>> map_local = new HashMap<String, Set<String>>();
        List<String> input_genes = new ArrayList<String>();
        for (String line : lines) {
            String[] tokens = line.split(",");
            String gene = tokens[0];
            input_genes.add(gene);
            Set<String> clones = new HashSet<String>();
            for (int i = 1; i < tokens.length; i++) clones.add(tokens[i]);
            map_local.put(gene, clones);
        }
        if (lines.size() > map_local.size()) {
            FreqDist<String> freqDist = new FreqDist<String>(input_genes);
            Set<String> duplicate_genes = freqDist.withFreqGreaterThan(Integer.valueOf("1"));
            StringBuilder builder = new StringBuilder(100);
            builder.append("\nDuplicate genes found: " + duplicate_genes + ".\nTheir frequencies: \n");
            for (String duplicate_gene : duplicate_genes) builder.append(duplicate_gene + ":" + freqDist.getFrequency(duplicate_gene) + "\n");
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, builder.toString());
        }
        return map_local;
    }

    @Override
    public void setDataFile(File dataFile) throws ProviderException {
        if (!dataFile.exists()) throw new ProviderException("File " + dataFile.getAbsolutePath() + " does not exist.");
        this.dataFile = dataFile;
    }
}
