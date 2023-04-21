package edu.usc.epigenome.uecgatk.benWalkers.cytosineWalkers;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.broadinstitute.sting.commandline.Argument;
import org.broadinstitute.sting.commandline.Output;
import org.broadinstitute.sting.utils.wiggle.WiggleHeader;
import edu.usc.epigenome.genomeLibs.MethylDb.CpgSummarizers.CpgMethLevelSummarizer;
import edu.usc.epigenome.uecgatk.benWalkers.CpgBackedByGatk;
import edu.usc.epigenome.uecgatk.benWalkers.LocusWalkerToBisulfiteCytosineWalker;
import edu.usc.epigenome.uecgatk.benWalkers.WiggleHeaderCytosines;
import edu.usc.epigenome.uecgatk.WiggleWriterReducible;

/**
 * @author benb
 * 
 */
public class GnomeSeqToBareWigWalker extends LocusWalkerToBisulfiteCytosineWalker<Map<String, WiggleWriterReducible>, Map<String, WiggleWriterReducible>> {

    @Argument(fullName = "outPrefix", shortName = "pre", doc = "Output prefix for all output files", required = true)
    public String outPrefix = null;

    @Argument(fullName = "nonGnomeMode", shortName = "bs", doc = "Non-GNOMe mode (normal bisulfite). Outputs only CG and/or CH tracks", required = false)
    public boolean nonGnomeMode = false;

    @Argument(fullName = "outputAllContexts", shortName = "all", doc = "Output optional contexts HCH, GCG (default)", required = false)
    public boolean outputAllContexts = false;

    Map<String, WiggleWriterReducible> wigByContext = new HashMap<String, WiggleWriterReducible>();

    /**
	 * Provides an initial value for the reduce function.  Hello walker counts loci,
	 * so the base case for the inductive step is 0, indicating that the walker has seen 0 loci.
	 * @return 0.
	 */
    @Override
    public Map<String, WiggleWriterReducible> reduceInit() {
        Map<String, WiggleWriterReducible> out = new HashMap<String, WiggleWriterReducible>();
        return out;
    }

    @Override
    public void initialize() {
        super.initialize();
        if (this.getToolkit().getArguments().numberOfThreads > 1) {
            System.err.println("GnomeSeqToBareWigWalker does not yet implement multi-threaded mode. Use -nt 1");
            System.exit(1);
        }
    }

    @Override
    public Map<String, WiggleWriterReducible> treeReduce(Map<String, WiggleWriterReducible> a, Map<String, WiggleWriterReducible> b) {
        System.err.println("GnomeSeqToBareWigWalker does not yet implement multi-threaded mode. Use -nt 1");
        System.exit(1);
        return this.reduceCytosines(a, b);
    }

    /**
	 * Retrieves the final result of the traversal.
	 * @param result The ultimate value of the traversal, produced when map[n] is combined with reduce[n-1]
	 *               by the reduce function. 
	 */
    @Override
    public void onTraversalDone(Map<String, WiggleWriterReducible> result) {
    }

    /***************************************************
	 * cytosine walker overrides
	 ***************************************************/
    @Override
    protected void alertNewContig(String newContig) {
    }

    @Override
    protected Map<String, WiggleWriterReducible> processCytosine(CpgBackedByGatk thisC) {
        writeCoverage(thisC);
        String context = thisC.context();
        if (this.nonGnomeMode) {
            context = context.substring(1);
        }
        if (!this.outputAllContexts) {
            if (context.equalsIgnoreCase("HCH") || context.equalsIgnoreCase("GCG")) return this.wigByContext;
        }
        double meth = thisC.fracMeth(false);
        WiggleWriterReducible wig = null;
        if (this.wigByContext.containsKey(context)) {
            wig = this.wigByContext.get(context);
        } else {
            String name = String.format("%s.%s", this.outPrefix, context);
            String outfn = String.format("%s.wig", name, context);
            logger.info("NEW wig " + outfn);
            wig = new WiggleWriterReducible(new File(outfn));
            WiggleHeader head = new WiggleHeaderCytosines(name, name);
            wig.writeHeader(head);
            this.wigByContext.put(context, wig);
        }
        wig.writeData(thisC.getGenomeLoc(), Math.round(100.0 * meth));
        return this.wigByContext;
    }

    private void writeCoverage(CpgBackedByGatk thisC) {
        String context = "readcvg";
        WiggleWriterReducible wig = null;
        if (this.wigByContext.containsKey(context)) {
            wig = this.wigByContext.get(context);
        } else {
            String name = String.format("%s.%s", this.outPrefix, context);
            String outfn = String.format("%s.wig", name, context);
            logger.info("NEW wig " + outfn);
            wig = new WiggleWriterReducible(new File(outfn));
            WiggleHeader head = new WiggleHeaderCytosines(name, name);
            wig.writeHeader(head);
            this.wigByContext.put(context, wig);
        }
        wig.writeData(thisC.getGenomeLoc(), thisC.totalReads);
    }

    @Override
    protected Map<String, WiggleWriterReducible> reduceCytosines(Map<String, WiggleWriterReducible> newMap, Map<String, WiggleWriterReducible> oldMap) {
        Map<String, WiggleWriterReducible> outMap = null;
        if (newMap.hashCode() == oldMap.hashCode()) {
            outMap = oldMap;
        } else {
            logger.info(String.format("Actually combining maps: (%s,%s)\n", newMap.hashCode(), oldMap.hashCode()));
            outMap = this.actuallyReduceCytosines(newMap, oldMap);
        }
        return outMap;
    }

    private Map<String, WiggleWriterReducible> actuallyReduceCytosines(Map<String, WiggleWriterReducible> newMap, Map<String, WiggleWriterReducible> oldMap) {
        Map<String, WiggleWriterReducible> outmap = oldMap;
        Set<String> keys = new HashSet<String>();
        keys.addAll(newMap.keySet());
        keys.addAll(oldMap.keySet());
        for (String key : keys) {
            if (newMap.containsKey(key) && !oldMap.containsKey(key)) {
                outmap.put(key, newMap.get(key));
            } else if (!newMap.containsKey(key) && oldMap.containsKey(key)) {
                outmap.put(key, oldMap.get(key));
            } else if (!newMap.containsKey(key) && !oldMap.containsKey(key)) {
                logger.error(String.format("GnomeSeqToBareWigWalker::actuallyReduceCytosines() - How did we get key \"%s\" when neither lhs and rhs contain it??", key));
                System.exit(1);
            } else {
                String outfn = String.format("%s.%s.wig.%s", this.outPrefix, key, this.hashCode());
                WiggleWriterReducible combined = WiggleWriterReducible.merge(newMap.get(key), oldMap.get(key), new File(outfn));
                outmap.put(key, combined);
            }
        }
        return outmap;
    }
}
