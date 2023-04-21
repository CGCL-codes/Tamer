package edu.dimacs.mms.borj;

import java.util.*;
import java.io.*;
import java.text.DateFormat;
import org.w3c.dom.Element;
import edu.dimacs.mms.boxer.*;
import edu.dimacs.mms.boxer.util.*;

/**  The main class for a standalone command-line driven Java program
 *  that runs machine learning experiments using BOXER.
`
    <p> This class implements the "main" function of BORJ (Bayesian
    Online Regression in Java), a standalone Java application that
    invokes most classes of the BOXER toolkit, and allows one to make
    use of most of its functionality. The operation of BORJ is controlled by
    the parameters supplied on the command line. The main operations
    include reading a suite description from an XML file, reading a
    training set (a set of data points) from an XML file and training
    the learning model on it, reading a test set and applying the
    learning model to it. Various other operations, such as reading a
    file of labels (separate from the data file), or writing out the
    learning model, are supported as well.

    <h3>Organization</h3>

    Commands appearing on borj.Driver's command line correspond to the
    operations BORJ will perform. Order matters. It is convenient to
    think of operations that BORJ can carry out as falling into
    several groups, which normally should be carried out in the following 
    sequence. Each command group is optional.

    <ol> 

    <li><strong>Command Group 1: Deserializing a suite or a "learner
    complex".</strong> There are two possiblities here: <ul>

    <li> You can read in just the suite file (list of discriminations,
    and classes in each dscrimination), using the <tt>read-suite:</tt>
    command.  This suite description file is a pre-created XML file
    (which you can create by hand, or with some program analyzing your
    data), describing a {@link edu.dimacs.mms.boxer.Suite suite} (list of {@link
    edu.dimacs.mms.boxer.Discrimination discriminations}, each one consisting of
    classes into which examples may be categorized).<br>
    
    The <tt>read-suite:</tt> command can be followed by an optional
    <tt>read-priors:</tt> command, which will cause BOXER read an XML
    file containing descriptions of individual priors (see a sample
    file in <a
    href="../../../../../sample-priors.xml">sample-priors.xml</a>); they
    will be used for all TG learners created later.
    <br>

    Once a suite has been created, a learner (or several learners)
    using it can be created later on (see Group 2, below).

    <li>Instead of just reading a suite, you can read an entire
    "learner complex" (a ready-made classifier), using the
    <tt>read:</tt> command. The learner complex file contains the
    description of a suite, list of features (e.g., words from which
    document examples consist), any existing individual priors, and
    one or several learners using this suite.<br>

    Typically, a learner complex file may be created by serializing BOXER's
    data from an earlier BORJ run, but you can create one by hand as well.<br>

    If you have read a "learner complex", you can add more learners later
    on, and you can continue training learners on more training data.
    </ul>

    If neither a suite nor a learner complex has been read at this
    stage, BOXER will create an empty suite (which it will later
    expand when it finds class labels in the first training set file).
    At least one learner will be added later on as well, as explained below.

    <li><strong>Command Group 2: Adding learners.</strong> You can use
    any number of <tt>read-learner:</tt> commands to add more learners
    using an existing suite (which at this point may still be an empty
    suite, if there has been no Group 1 command). Each learner is
    described in an XML file that describes the learner's parameters,
    and, optionally, the initial learner state as well.

    If no learner complex has been read in Command Group 1, and no
    learner has been added in Command Group 2 either, BORJ will then
    create one "default" learner, based on the value of the
    -Dmodel=tg|eg|trivial option.

    <li><strong>Command Group 3: Reading label files.</strong> If
    example labels reside in seprate files from example features, you
    should read in the label files now, using the
    <tt>read-labels:</tt> command. This will ensure that these labels
    will be properly matched with the examples when the latter are read in.

    <li><strong>Command Group 4: Training, testing, and
    output.</strong> Any number of <tt>train:</tt>, <tt>test:</tt>,
    <tt>write:</tt>, and <tt>write-suite:</tt>
    commands may now be supplied to train the existing learners, apply
    them to test sets, and to deserialize the suite and/or the learners.
    <br>

    There are also some "miscellaneous" commands in this group, such
    as <tt>validate:</tt>, which can be used to read a data set XML
    file to check whether it can be added to the current suite.    
    </ol>

    <h3>Usage</h3>
    Usage:
    <pre>
java [-Dmodel=tg|eg|trivial] [-Dverbose=true | -Dverbosity={0...3}] [-Drunid=RUN_ID]  borj.Driver command:file [command:file ...]
    </pre>

    The command line can contain any number of commands. They are
    executed sequentially, in the order they appear in the command
    line. Each command is followed by the file name (or sometimes file
    names) it applies to <p>

    The type of the learning model, and certain other options or it, 
    can be specified as Java system properties, with the -D command-line
    flag.
    <p>

    The following command are presently supported:

    <h4>Group 1</h4>
    <ul>

    <li>read-suite: Read the description of a "{@link edu.dimacs.mms.boxer.Suite
    suite}" (basically, a list of discriminations, and the classes of
    each one) from an XML file. Such file can be created manually, or
    generated by a program. This command is useful if you want to
    pre-define your discriminations (instead of having BOXER infer
    their structure from the labels it finds in the first training
    file); if you use it, it makes sense to use it as the first
    command on the command line, so that the suite will be actually used
    in constructing the model.

    <li>read: Reads a "learner complex" from an XML file. The input
    file's root element must be <tt>learnercomplex</tt>. It encloses
    the description of the suite (<tt>suite</tt>) element, the {@link
    edu.dimacs.mms.boxer.FeatureDictionary feature dictionary} (which can be used to
    parse input examples, and to produce human readable output), and
    one or more <tt>learner</tt> elements. Each <tt>learner</tt>
    element contains algorithm parameters, and optionally also the
    current inner state of the learning algorithm (such as the
    coefficient matrix (or matrices) for {@link edu.dimacs.mms.boxer.PLRMLearner
    PLRM-based algorithms}. A BORJ command line should have only one
    "read" command, and if given, it must be the first command.

    <li>read-priors: Supplies a priors definition file. Can only
    appear after a read-suite command, so that the priors will be
    interpreted in the context of that particular suite, and
    associated with the suite.

    </ul>
    <h4>Group 2</h4>
    <ul>
    <li>read-learner: Reads an XML file with a single <tt>learner</tt>
    element. That creates a new learner in the context of the existing
    suite (if no suite has been read in, an empty one will be created
    to start you on). The <tt>learner</tt> may contain just the
    algorithm parameters, or also (optionally) the initial state of
    the learner.

    </ul>
    <h4>Group 3</h4>
    <ul>

    <li>read-labels: Pre-read examples' labels from a separate XML
    file (a "label file"). The labels will be stored, and whenever a
    document with a matching id is read from another XML file in the
    future (with a "train" or "test" command), the labels will be
    applied to it.  You can use any number of "read-labels" commands
    anywhere on the command line, but for them to be useful, each label
    file should be read before the labels from it are used by a
    "train" or "test" command.

    <br> The label files which the Driver reads with the
    "read-labels:" command don't need to be "aligned" with the
    document files supplied with the "train:" and "test:"
    commands. They may contain labels for more documents than are
    found in the train and test files; the unnecessary labels will be
    simply ignored, and the order of the documents does not matter
    either. One only needs to ensure that the union of the set of doc
    ids in all the train and test files in a BORJ run is a subset of
    the union of doc ids in all of the label files read with
    "read-labels" in this run.

    </ul>
    <h4>Group 4</h4>
    <ul>
    <li> train: Read the examples from the XML (or BMR) file specified, and
    train the classifier on it. Any number of the "train" commands may
    appear in any position on the command line, the model being
    incrementally trained on each one. The examples in the file may
    contain both features and labels, or only features; in the latter
    case, the labels should have been pre-loaded with an earlier
    "read-labels" command.

    <li>test: Apply the model to each example from the specified XML
    (or BMR) file.  Any number of the "test" commands may appear in
    any position on the command line, although of course you may not
    want to use one before you've trained the model (or have read in a
    pre-computed model). As with the "train" command, the examples in
    the file may contain both features and labels, or only features;
    in the latter case, the labels should have been pre-loaded with an
    earlier "read-labels" command.<br>

    The <tt>test:</tt> command can also be used with two arguments,
    <tt>test:<em>test-set.xml</em>:<em>score-output-file.txt</em></tt>,
    to make BORJ save the test examples' scores to the second-named
    file. For the format of that file, see {@link edu.dimacs.mms.boxer.DataPoint#reportScoresAsText(double [][], Learner, String, PrintWriter)}

    <li>write-suite: Writes out the suite (only the suite, i.e. the
    list of classes), as it currently stands, into the
    specified XML file.

    <li>write: Writes out the complete "learner complex" (suite,
    feature dictionary, individual priors [if any], and all learners
    with their parameters and inner states), as it currently stands,
    into the specified XML file.

    </ul>
    <h4>Miscellaneous commands</h4>
    <ul>
    <li>delete-discr: Deletes a named discrimnation from the suite and
    all learners.
    </ul>

<h3>Command line examples</h3>

<p>
 (A): Read a suite (ontology) file from suite.xml, read algo params
 from tg-param.xml, train on a train set, test on a test set, and dump
 the current learner state (complete with suite etc) into learner-blob.xml:

<p><tt>
read-suite:suite.xml read-learner:tg-param.xml train:train-set.xml test:test-set.xml write:learner-blob.xml
</tt>
 
<p>
(B): Read an algo description; start with an empty suite, learning on a training file (filling one's suite based on the labels in it); score a training set, saving scores to text file; save just the suite

<p><tt>
read-learner:tg-param.xml  train:train-set.xml test:test-set.xml:save-scores.txt write-suite:suite-out.xml 
</tt>

<h3>See also</h3>

<ul>
<li>
 <a href="../../../../../tags.html">Overview of the XML elements used by BOXER</a>
</ul>

 */
public class Driver {

    static void usage() {
        usage(null);
    }

    private static void usage(String m) {
        System.out.println("This is the BORJ driver for the BOXER toolkit (version " + Version.version + ")");
        System.out.println("Usage: java [options] borj.Driver command:file [command:file] ...");
        System.out.println("For example:");
        System.out.println("  java [options] borj.Driver [train:]train.xml [test:]test.xml");
        System.out.println("  java [options] borj.Driver [read-suite:suite-in.xml] train:train1.xml train:train2.xml test:test_a.xml train:train3.xml test:test_b.xml [write:model-out.xml]");
        System.out.println(" ... etc.");
        System.out.println("Optons:");
        System.out.println(" [-Dmodel=eg|tg|trivial] [-Drunid=RUN_ID] [-Dverbose=true|false | -Dverbosity=0|1|2|3]");
        System.out.println("See Javadoc for borj.Driver for the full list of commands.");
        if (m != null) {
            System.out.println(m);
        }
        System.exit(1);
    }

    /** Auxiliary class responsible for parsing command line */
    private static class CmdManager {

        private CMD[] cm;

        /** Pointer to the first not-yet parsed cmd line argument */
        private int h = 0;

        /** Initializes the command manager by parsing the entire command line */
        CmdManager(String[] argv) {
            cm = CMD.parse(argv);
        }

        /** Are there more commands?*/
        boolean hasNext() {
            return h < cm.length;
        }

        /** Gets the next command (or null, if none is left) */
        CMD next() {
            return hasNext() ? cm[h++] : null;
        }
    }

    /** Produces default run id, if none is supplied */
    private static String mkRunId() {
        Calendar now = Calendar.getInstance();
        long time = now.getTimeInMillis();
        System.out.println("[TIME][START] " + DateFormat.getDateInstance().format(now.getTime()) + " (" + time + ")");
        return "" + time;
    }

    public static void main(String argv[]) throws IOException, org.xml.sax.SAXException, BoxerException {
        memory("BORJ startup");
        if (argv.length == 0) usage();
        ParseConfig ht = new ParseConfig();
        System.out.println("Welcome to the BOXER toolkit (version " + Version.version + ")");
        System.out.println("[VERSION] " + Version.version);
        Suite.verbosity = ht.getOption("verbosity", 1);
        boolean verbose = ht.getOption("verbose", (Suite.verbosity >= 3));
        String runid = ht.getOption("runid", mkRunId());
        DataPoint.setDefaultNameBase(runid);
        Suite suite = null;
        LabelStore qrelStore = new LabelStore();
        CmdManager cm = new CmdManager(argv);
        CMD q = cm.next();
        if (q.is(CMD.READ)) {
            System.out.println("Reading learner(s) from file: " + q.f);
            suite = Learner.deserializeLearnerComplex(new File(q.f));
            Vector<Learner> algos = suite.getAllLearners();
            Logging.info("Read " + algos.size() + " learners from the 'learner complex' file " + q.f);
            if (algos.size() == 0) {
                Logging.info("The 'learner complex' file " + q.f + " did not specify even a single learner. We expect that one will be added with a separate read-learner command");
            }
            q = cm.next();
        } else if (q.is(CMD.READ_SUITE)) {
            System.out.println("Reading discrimination suite from file: " + q.f);
            suite = new Suite(new File(q.f));
            q = cm.next();
        } else {
            suite = new Suite("Basic_BORJ_suite");
        }
        if (q != null && q.is(CMD.READ_PRIORS)) {
            System.out.println("Reading priors from file: " + q.f);
            Priors p = Priors.readPriorsFileMultiformat(new File(q.f), suite);
            suite.setPriors(p);
            q = cm.next();
        }
        for (; q != null && q.is(CMD.READ_LEARNER); q = cm.next()) {
            System.out.println("Adding a learner from file: " + q.f);
            suite.addLearner(ParseXML.readFileToElement(new File(q.f)));
        }
        if (suite.getLearnerCount() > 0) {
        } else {
            String model = ht.getOption("model", "tg");
            System.out.println("Default model name: " + model);
            if (model.equals("eg")) {
                Learner algo = new ExponentiatedGradient(suite);
            } else if (model.equals("tg")) {
                Learner algo = new TruncatedGradient(suite);
            } else if (model.equals("trivial")) {
                Learner algo = new TrivialLearner(suite);
            } else {
                usage("Unknown model `" + model + "'");
            }
        }
        int nLearners = suite.getLearnerCount();
        System.out.println("The suite is used by " + nLearners + " learners");
        int cnt = 0;
        for (Learner algo : suite.getAllLearners()) {
            if (Suite.verbosity >= 0) {
                System.out.println("Describing Learner No. " + (cnt++));
                algo.describe(System.out, false);
                System.out.println("-----------------------------------");
            }
        }
        int trainCnt = 0, testCnt = 0;
        Scores se[] = new Scores[nLearners];
        for (int i = 0; i < se.length; i++) se[i] = new Scores(suite);
        for (; q != null; q = cm.next()) {
            if (q.is(CMD.READ)) {
                usage("The " + CMD.READ + " command can only be used for the first argument");
            } else if (q.is(CMD.READ_SUITE)) {
                usage("The " + CMD.READ_SUITE + " command can only be used for the first argument");
            } else if (q.is(CMD.READ_LABELS)) {
                System.out.println("Reading labels from file: " + q.f);
                qrelStore.readXML(q.f);
            } else if (q.is(CMD.WRITE_PRIORS)) {
                System.out.println("Saving the priors (only) to file: " + q.f);
                suite.getPriors().saveAsXML(q.f);
            } else if (q.is(CMD.WRITE_SUITE)) {
                System.out.println("Saving the suite (only) to file: " + q.f);
                suite.saveAsXML(q.f);
            } else if (q.is(CMD.WRITE)) {
                System.out.println("Saving all " + nLearners + "  learner(s) to file: " + q.f);
                suite.serializeLearnerComplex(q.f);
            } else if (q.is(CMD.DELETE_DISCR)) {
                System.out.println("Deleting discrimination " + q.f);
                int delDid = suite.getDid(suite.getDisc(q.f));
                suite.deleteDiscrimination(q.f);
                System.out.println("Dropping saved scores for deleted discr");
                for (int i = 0; i < se.length; i++) se[i].deleteDiscr(delDid);
            } else if (q.is(CMD.VALIDATE)) {
                System.out.println("Validating a would-be training set (" + q.f + ")");
                Element e = ParseXML.readFileToElement(q.f);
                int n = ParseXML.validateDatasetElement(e, suite, true);
                if (n > 0) {
                    System.out.println("[VALIDATE] The data set from file " + q.f + " appears to be fully acceptable as a training set in the current suite. It contains " + n + " data points");
                } else {
                    System.out.println("[VALIDATE] It would not be possible to parse the data set from file " + q.f + " as a training set in the current suite. Please see a warning log message for detals");
                }
            } else if (q.is(CMD.TRAIN)) {
                trainCnt++;
                System.out.println("Reading training set no. " + trainCnt + " (" + q.f + ")");
                Vector<DataPoint> train = ParseXML.readDataFileMultiformat(q.f, suite, true);
                qrelStore.applyTo(train, suite, true);
                if (Suite.verbosity > 0) System.out.println(suite.describe());
                if (verbose) System.out.println(suite.getDic().describe());
                if (Suite.verbosity > 0) System.out.println("Training set no. " + trainCnt + " (" + q.f + ") contains " + train.size() + " points, memory use=" + Sizeof.sizeof(train) + " bytes");
                for (int i = 0; i < train.size(); i++) {
                    if (verbose) System.out.println(train.elementAt(i));
                }
                memory("Read train set; starting to train");
                cnt = 0;
                for (Learner algo : suite.getAllLearners()) {
                    algo.absorbExample(train);
                    if (Suite.verbosity >= 0) {
                        System.out.println("Describing Learner No. " + (cnt++));
                        algo.describe(System.out, false);
                        System.out.println("-----------------------------------");
                    } else {
                        System.out.println("[NET] Leaner no. " + (cnt++) + " net memory use=" + algo.memoryEstimate());
                    }
                    if (verbose) algo.saveAsXML(algo.algoName() + "-out" + trainCnt + ".xml");
                }
                int ts = train.size();
                train = null;
                memory("Absorbed " + ts + " examples from " + q.f);
            } else if (q.is(CMD.TEST)) {
                testCnt++;
                System.out.println("Reading test set (" + q.f + ")");
                Vector<DataPoint> test = ParseXML.readDataFileMultiformat(q.f, suite, false);
                qrelStore.applyTo(test, suite, false);
                if (Suite.verbosity > 0) System.out.println("Test set no. " + testCnt + " (" + q.f + ") contains " + test.size() + " points, memory use=" + Sizeof.sizeof(test) + " bytes");
                cnt = 0;
                PrintWriter sw = null;
                if (q.f2 != null) {
                    System.out.println("Scores will go to text file (" + q.f2 + ")");
                    sw = new PrintWriter(new FileWriter(q.f2));
                }
                for (Learner algo : suite.getAllLearners()) {
                    if (Suite.verbosity > 0) System.out.println("Scoring test set (" + q.f + ") using learner " + cnt);
                    Scores seLocal = new Scores(suite);
                    for (int i = 0; i < test.size(); i++) {
                        DataPoint x = test.elementAt(i);
                        double[][] probLog = algo.applyModelLog(x);
                        double[][] prob = expProb(probLog);
                        if (Suite.verbosity > 0) System.out.println("Scored test vector " + i + "; scores=" + x.describeScores(prob, suite));
                        if (sw != null) x.reportScoresAsText(prob, algo, runid, sw);
                        seLocal.evalScores(x, suite, prob);
                        se[cnt].evalScores(x, suite, prob);
                        x.addLogLik(probLog, suite, seLocal.likCnt, seLocal.logLik);
                        x.addLogLik(probLog, suite, se[cnt].likCnt, se[cnt].logLik);
                    }
                    if (Suite.verbosity >= 0) {
                        System.out.println("Scoring report (file " + q.f + "):");
                        System.out.println(seLocal.scoringReport(suite, "[SCORES][" + q.f + "]"));
                        System.out.println(seLocal.loglikReport(suite, "[LOGLIK][" + q.f + "]"));
                    }
                    cnt++;
                }
                if (sw != null) sw.close();
                int ts = test.size();
                test = null;
                memory("Scored " + ts + " examples from " + q.f);
            } else {
                usage("Unknown command: " + q.cmd);
            }
        }
        if (Suite.verbosity >= 0) {
            for (int i = 0; i < se.length; i++) {
                if (se[i].size() == 0) continue;
                System.out.println("Scoring report (GLOBAL) for learner no. " + i + ":");
                System.out.println(se[i].scoringReport(suite, "[SCORES][GLOBAL] "));
                System.out.println(se[i].loglikReport(suite, "[LOGLIK][GLOBAL]"));
            }
        }
        memory("Finished");
        Calendar now = Calendar.getInstance();
        System.out.println("[TIME][FINISH] " + DateFormat.getDateInstance().format(now.getTime()) + " (" + now.getTimeInMillis() + ")");
    }

    static void memory() {
        memory("");
    }

    static void memory(String title) {
        Runtime run = Runtime.getRuntime();
        String s = (title.length() > 0) ? " (" + title + ")" : "";
        run.gc();
        long mmem = run.maxMemory();
        long tmem = run.totalMemory();
        long fmem = run.freeMemory();
        long used = tmem - fmem;
        System.out.println("[MEMORY]" + s + " max=" + mmem + ", total=" + tmem + ", free=" + fmem + ", used=" + used);
    }

    /** Computes exponent of each array element */
    private static double[][] expProb(double[][] probLog) {
        double[][] prob = new double[probLog.length][];
        for (int j = 0; j < prob.length; j++) {
            double[] v = probLog[j];
            prob[j] = new double[v.length];
            for (int k = 0; k < v.length; k++) {
                prob[j][k] = Math.exp(v[k]);
            }
        }
        return prob;
    }
}
