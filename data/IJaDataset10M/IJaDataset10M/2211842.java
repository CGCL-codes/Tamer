package mx.unam.ecologia.gye.coalescence.app;

import mx.unam.ecologia.gye.coalescence.model.CoalescentGenealogy;
import mx.unam.ecologia.gye.coalescence.model.UniParentalGene;
import mx.unam.ecologia.gye.coalescence.util.SimulationParameters;
import mx.unam.ecologia.gye.coalescence.visitors.MicrosatelliteMutationVisitor;
import mx.unam.ecologia.gye.model.CompoundSequence;
import mx.unam.ecologia.gye.model.Sequence;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Provides a small application for generating Micsat input
 * files.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class CreateMicsatInput {

    private static final Log log = LogFactory.getLog(CreateMicsatInput.class);

    protected static final void writeMicsat(List leaves, String fname) {
        try {
            FileOutputStream fout = new FileOutputStream(fname);
            PrintWriter pw = new PrintWriter(fout);
            for (int i = 0; i < leaves.size(); i++) {
                UniParentalGene upgene = (UniParentalGene) leaves.get(i);
                CompoundSequence h = upgene.getCompoundSequence();
                int numloc = h.getSequenceCount();
                for (int n = 0; n < numloc; n++) {
                    Sequence s = h.get(n);
                    pw.print(s.getSize());
                    pw.print(" ");
                }
                pw.println();
            }
            pw.flush();
            pw.close();
            fout.close();
        } catch (IOException ex) {
            log.error("writeMicsat()", ex);
        }
    }

    public static void main(String[] args) throws Exception {
        String fname = "micsat_data.dat";
        BasicConfigurator.configure();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-output=")) {
                fname = args[i].substring(8);
            }
        }
        SimulationParameters params = new SimulationParameters(args);
        log.info(params.toString());
        CoalescentGenealogy genea = new CoalescentGenealogy(params);
        MicrosatelliteMutationVisitor vis = new MicrosatelliteMutationVisitor(params);
        log.info("Generating Genealogy");
        genea.generate();
        log.info("Applying mutations");
        genea.getMRCA().setCompoundSequence(params.CompoundSequence());
        UniParentalGene.traverse(genea.getMRCA(), vis);
        log.info("Writing Micsat Input file");
        List leaves = genea.getLeaves();
        writeMicsat(leaves, fname);
        log.info("Done.");
    }
}
