package ogap.blast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import ogap.engine.genome.Gene;
import ogap.engine.genome.Genome;
import ogap.gui.statusWindow.StatusAction;

/**
 *
 * @author aaronmckenna
 */
public class BlastStatusAction implements StatusAction {

    private int geneCount = 0;

    private int currentGene = 0;

    private PrintWriter out = null;

    private Genome g = null;

    private Iterator<Gene> i = null;

    private BlastWrapper wrap = null;

    private String dataBase = "";

    public BlastStatusAction(String fileName, Genome g, String database) {
        this.g = g;
        this.dataBase = database;
        File st = new File(fileName);
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        } catch (IOException ex) {
            System.err.println("Failed to open" + fileName);
            return;
        }
        this.geneCount = g.size();
        this.currentGene = 0;
        i = g.iterator();
    }

    public int totalItems() {
        return geneCount;
    }

    public int itemsProcessed() {
        return currentGene;
    }

    public boolean processNextItem() {
        if (i.hasNext()) {
            Gene gene = (Gene) i.next();
            wrap = new BlastWrapper(gene.getSequence());
            wrap.setDatabase(dataBase);
            BlastParser parse = wrap.executeTool();
            HashMap<Double, String> map = parse.getScoreMap();
            Iterator iter = map.keySet().iterator();
            Double bestScore = -1.0;
            while (iter.hasNext()) {
                Double n = (Double) iter.next();
                if (n > bestScore) {
                    bestScore = n;
                }
            }
            out.print(gene.getLocusID() + ", " + bestScore.toString() + ", ");
            out.print(gene.getSeqLength() + ", ");
            if (gene.getAvailableRecords().contains("Total Entropy Value")) {
                out.print(gene.getAssociatedData("Total Entropy Value") + "\n");
            }
            out.print("\n");
            this.currentGene++;
            return true;
        } else {
            return false;
        }
    }

    public String generateStatusString() {
        return "Processing " + currentGene + " of " + geneCount + " genes.\n";
    }
}
