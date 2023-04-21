package verjinxer.subcommands;

import static verjinxer.Globals.programname;
import java.io.File;
import java.io.IOException;
import com.spinn3r.log5j.Logger;
import verjinxer.Globals;
import verjinxer.Project;
import verjinxer.QGramChecker;
import verjinxer.QGramIndexer;
import verjinxer.util.FileTypes;
import verjinxer.util.IllegalOptionException;
import verjinxer.util.Options;
import verjinxer.util.StringUtils;

/**
 * This class provides a q-gram indexer.
 * @author Sven Rahmann
 * @author Marcel Martin
 */
public final class QGramIndexerSubcommand implements Subcommand {

    private static final Logger log = Globals.getLogger();

    /** only store q-grams whose positions are divisible by stride */
    private int stride = 1;

    private Globals g;

    /** Creates a new instance of QgramIndexer
    * @param gl  the Globals object to use
    */
    public QGramIndexerSubcommand(Globals gl) {
        g = gl;
    }

    /**
    * print help on usage
    */
    public void help() {
        log.info("Usage:");
        log.info("%s qgram [options] Indexnames...", programname);
        log.info("Builds a q-gram index of .seq files; filters out low-complexity q-grams;");
        log.info("writes %s, %s, %s, %s.", FileTypes.QBUCKETS, FileTypes.QPOSITIONS, FileTypes.QFREQ, FileTypes.QSEQFREQ);
        log.info("Options:");
        log.info("  -q  <q>                 q-gram length [0=reasonable]");
        log.info("  -s, --stride <stride>   only store q-grams whose positions are divisible by stride (default: %d)", stride);
        log.info("  -b, --bisulfite         simulate bisulfite treatment");
        log.info("  -f, --allfreq           write (unfiltered) frequency files (--freq, --sfreq)");
        log.info("  --freq                  write (unfiltered) q-gram frequencies (%s)", FileTypes.QFREQ);
        log.info("  --seqfreq, --sfreq      write in how many sequences each qgram appears (%s)", FileTypes.QSEQFREQ);
        log.info("  -c, --check             additionally check index integrity");
        log.info("  -C, --onlycheck         ONLY check index integrity");
        log.info("  -F, --filter <cplx:occ> PERMANENTLY apply low-complexity filter to %s", FileTypes.QBUCKETS);
        log.info("  -X, --notexternal       DON'T save memory at the cost of lower speed");
        log.info("  -r, --runs              build the index of the run-compressed sequence (%s) (unsupported)", FileTypes.RUNSEQ);
    }

    /** if run independently, call main
    * @param args  the command line arguments
    */
    public static void main(String[] args) {
        System.exit(new QGramIndexerSubcommand(new Globals()).run(args));
    }

    /**
    * @param args the command line arguments
    * @return zero on success, nonzero if there is a problem
    */
    public int run(String[] args) {
        g.cmdname = "qgram";
        int returnvalue = 0;
        String action = "qgram \"" + StringUtils.join("\" \"", args) + "\"";
        Options opt = new Options("q:,F=filter:,c=check,C=onlycheck,X=notexternal=nox=noexternal,b=bisulfite,s=stride:,freq=fr,sfreq=seqfreq=sf,f=allfreq,r=runs");
        try {
            args = opt.parse(args);
        } catch (IllegalOptionException ex) {
            log.error("qgram: " + ex);
            return 1;
        }
        if (args.length == 0) {
            help();
            log.error("qgram: no index given");
            return 0;
        }
        final boolean external = !opt.isGiven("X");
        final boolean freq = opt.isGiven("f") || opt.isGiven("freq");
        final boolean sfreq = opt.isGiven("f") || opt.isGiven("sfreq");
        final boolean check = opt.isGiven("c");
        final boolean checkonly = opt.isGiven("C");
        final boolean bisulfite = opt.isGiven("b");
        final boolean runs = opt.isGiven("r") || opt.isGiven("runs");
        final int q = (opt.isGiven("q")) ? Integer.parseInt(opt.get("q")) : 0;
        stride = opt.isGiven("s") ? Integer.parseInt(opt.get("s")) : 1;
        log.info("qgram: stride length is %d", stride);
        for (int i = 0; i < args.length; i++) {
            Project project;
            try {
                project = Project.createFromFile(new File(args[i]));
            } catch (IOException ex) {
                log.error("qgram: cannot read project file.");
                return 1;
            }
            g.startProjectLogging(project);
            if (checkonly) {
                if (QGramChecker.docheck(project) >= 0) {
                    returnvalue = 1;
                }
                continue;
            }
            QGramIndexer qgramindexer = new QGramIndexer(g, project, q, external, bisulfite, stride, opt.get("F"));
            project.setProperty("QGramAction", action);
            File sequenceFile = project.makeFile(FileTypes.SEQ);
            if (runs) {
                throw new UnsupportedOperationException("Processing of runs is currently not supported.");
            }
            try {
                final File freqfile = (freq ? project.makeFile(FileTypes.QFREQ) : null);
                final File sfreqfile = (sfreq ? project.makeFile(FileTypes.QSEQFREQ) : null);
                qgramindexer.generateAndWriteIndex(sequenceFile, project.makeFile(FileTypes.QBUCKETS), project.makeFile(FileTypes.QPOSITIONS), freqfile, sfreqfile);
            } catch (IOException ex) {
                ex.printStackTrace();
                log.error("qgram: failed on %s: %s; continuing with remainder...", project.getName(), ex);
                g.stopplog();
                continue;
            }
            final double[] times = qgramindexer.getLastTimes();
            log.info("qgram: time for %s: %.1f sec or %.2f min", project.getName(), times[0], times[0] / 60.0);
            if (check && QGramChecker.docheck(project) >= 0) returnvalue = 1;
            g.stopplog();
        }
        return returnvalue;
    }
}
