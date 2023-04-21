package org.dllearner.scripts;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.dllearner.algorithms.ocel.OCEL;
import org.dllearner.core.ComponentInitException;
import org.dllearner.core.ComponentManager;
import org.dllearner.core.EvaluatedDescription;
import org.dllearner.core.KnowledgeSource;
import org.dllearner.core.LearningProblemUnsupportedException;
import org.dllearner.kb.OWLFile;
import org.dllearner.learningproblems.EvaluatedDescriptionPosNeg;
import org.dllearner.learningproblems.PosNegLPStandard;
import org.dllearner.reasoning.FastInstanceChecker;
import org.dllearner.refinementoperators.RhoDRDown;
import org.dllearner.utilities.Files;
import org.dllearner.utilities.Helper;
import com.jamonapi.MonitorFactory;

/**
 * Sample script showing how to use DL-Learner. Provides an entry point for tool
 * developers.
 * 
 * @author Sebastian Hellmann
 * @author Jens Lehmann
 * 
 */
public class NewSample {

    private static Logger logger = Logger.getRootLogger();

    private static DecimalFormat df = new DecimalFormat();

    public static void main(String[] args) throws IOException, ComponentInitException, LearningProblemUnsupportedException {
        SimpleLayout layout = new SimpleLayout();
        FileAppender fileAppender = new FileAppender(layout, "log/sample_log.txt", false);
        ConsoleAppender consoleAppender = new ConsoleAppender(layout);
        logger.removeAllAppenders();
        logger.addAppender(consoleAppender);
        logger.addAppender(fileAppender);
        logger.setLevel(Level.DEBUG);
        String owlFile = "examples/trains/trains.owl";
        SortedSet<String> posExamples = new TreeSet<String>();
        posExamples.add("http://example.com/foo#east1");
        posExamples.add("http://example.com/foo#east2");
        posExamples.add("http://example.com/foo#east3");
        posExamples.add("http://example.com/foo#east4");
        posExamples.add("http://example.com/foo#east5");
        SortedSet<String> negExamples = new TreeSet<String>();
        negExamples.add("http://example.com/foo#west6");
        negExamples.add("http://example.com/foo#west7");
        negExamples.add("http://example.com/foo#west8");
        negExamples.add("http://example.com/foo#west9");
        negExamples.add("http://example.com/foo#west10");
        List<? extends EvaluatedDescription> results = learn(owlFile, posExamples, negExamples, 5);
        int x = 0;
        for (EvaluatedDescription ed : results) {
            System.out.println("solution: " + x);
            System.out.println("  description: \t" + ed.getDescription().toManchesterSyntaxString(null, null));
            System.out.println("  accuracy: \t" + df.format(((EvaluatedDescriptionPosNeg) ed).getAccuracy() * 100) + "%");
            System.out.println();
            x++;
        }
        Files.createFile(new File("log/jamon_sample.html"), MonitorFactory.getReport());
    }

    public static List<? extends EvaluatedDescription> learn(String owlFile, SortedSet<String> posExamples, SortedSet<String> negExamples, int maxNrOfResults) throws ComponentInitException, LearningProblemUnsupportedException {
        logger.info("Start Learning with");
        logger.info("positive examples: \t" + posExamples.size());
        logger.info("negative examples: \t" + negExamples.size());
        URL fileURL = null;
        try {
            fileURL = new File(owlFile).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        OWLFile ks = new OWLFile();
        ks.setUrl(fileURL);
        Set<KnowledgeSource> tmp = new HashSet<KnowledgeSource>();
        tmp.add(ks);
        FastInstanceChecker f = new FastInstanceChecker(tmp);
        PosNegLPStandard lp = new PosNegLPStandard(f, Helper.getIndividualSet(posExamples), Helper.getIndividualSet(negExamples));
        OCEL la = ComponentManager.getInstance().learningAlgorithm(OCEL.class, lp, f);
        RhoDRDown op = (RhoDRDown) la.getOperator();
        op.setUseAllConstructor(false);
        op.setUseExistsConstructor(true);
        op.setUseCardinalityRestrictions(false);
        op.setUseExistsConstructor(true);
        op.setUseNegation(false);
        la.setWriteSearchTree(false);
        la.setReplaceSearchTree(true);
        la.setNoisePercentage(0.0);
        ks.init();
        f.init();
        lp.init();
        la.init();
        logger.debug("start learning");
        la.start();
        return la.getCurrentlyBestEvaluatedDescriptions(maxNrOfResults);
    }
}
