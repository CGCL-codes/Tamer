package uk.ac.shef.wit.trex.experimental;

import no.uib.cipr.matrix.Vector;
import uk.ac.shef.wit.aleph.AlephException;
import uk.ac.shef.wit.aleph.SparseVector;
import uk.ac.shef.wit.aleph.algorithm.Classifier;
import uk.ac.shef.wit.aleph.algorithm.Learner;
import uk.ac.shef.wit.aleph.algorithm.svm.LearnerSVMStochasticGradientDescentPL;
import uk.ac.shef.wit.aleph.dataset.Dataset;
import uk.ac.shef.wit.aleph.dataset.io.DatasetReaderSVMLight;
import uk.ac.shef.wit.aleph.dataset.view.DatasetViewMemoryCached;
import uk.ac.shef.wit.aleph.dataset.view.instances.InstancesFilterTrainTest;
import uk.ac.shef.wit.aleph.dataset.view.instances.InstancesFilterTrainTestFromSplit;
import uk.ac.shef.wit.aleph.validation.SplitNRandom;
import uk.ac.shef.wit.aleph.validation.score.Scorer;
import uk.ac.shef.wit.aleph.validation.score.ScorerPrecisionAndRecallMulti;
import uk.ac.shef.wit.commons.ObjectIndex;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Experiments on applying PL-SVM to the ACE08 entity recognition task.
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public class ACE08PartialLabels {

    private static final Logger log = Logger.getLogger(ReutersRCV1PartialLabels.class.getName());

    private void run() throws AlephException, IOException {
        final ObjectIndex<String> ids = loadClassIds("ace08/classes.id");
        final String[] labels = ids.toArray(new String[ids.size() + 1]);
        final Map<Integer, Vector> labelSets = generateSpecificLabelSets(ids);
        final Dataset dataset = new DatasetViewMemoryCached().apply(new DatasetReaderSVMLight(".", "ace08", "data").read());
        for (int ratio = 100; ratio >= 0; --ratio) {
            if (ratio < 80 && ratio % 5 != 0) continue;
            log.fine("running ratio=" + ratio);
            final ScorerPrecisionAndRecallMulti scorer = new ScorerPrecisionAndRecallMulti();
            for (final Dataset split : new SplitNRandom(4, 50).split(dataset)) {
                final Dataset withPartials = new PartialLabelsGenerator(labelSets, ratio).apply(new InstancesFilterTrainTest(true).apply(split));
                final Learner learner = new LearnerSVMStochasticGradientDescentPL(3);
                final Classifier classifier = learner.learn(withPartials);
                final Dataset predicted = classifier.classify(new InstancesFilterTrainTest(false).apply(split));
                log.fine("finished.");
                scorer.add(new InstancesFilterTrainTestFromSplit(split, false).apply(dataset), predicted);
            }
            final PrintWriter writerCSV = new PrintWriter(new BufferedWriter(new FileWriter("ace08_" + ratio + ".csv")));
            for (int i = 1; i < labels.length; ++i) {
                final StringWriter sw = new StringWriter();
                final PrintWriter writer = new PrintWriter(sw);
                writer.printf("%15s, %3.0f, %3.0f, %3.0f, %3.2f, %3.2f, %3.2f", labels[i], scorer.getScore(i, Scorer.Measure.TRUE_POSITIVES), scorer.getScore(i, Scorer.Measure.FALSE_POSITIVES), scorer.getScore(i, Scorer.Measure.FALSE_NEGATIVES), scorer.getScore(i, Scorer.Measure.PRECISION), scorer.getScore(i, Scorer.Measure.RECALL), scorer.getScore(i, Scorer.Measure.FMEASURE));
                writer.println();
                writer.flush();
                log.fine(sw.toString());
                writerCSV.append(sw.toString());
            }
            writerCSV.close();
        }
    }

    private Map<Integer, Vector> generateSpecificLabelSets(final ObjectIndex<String> index) {
        final Map<String, Set<String>> labelsByPrefix = new HashMap<String, Set<String>>();
        for (final String label : index.keySet()) {
            final int i = label.lastIndexOf('_');
            final String prefix = i >= 0 ? label.substring(0, i) : label;
            Set<String> set = labelsByPrefix.get(prefix);
            if (set == null) labelsByPrefix.put(prefix, set = new HashSet<String>());
            set.add(label);
        }
        final Map<Integer, Vector> labelSets = new HashMap<Integer, Vector>();
        for (final Set<String> set : labelsByPrefix.values()) {
            final Vector ids = new SparseVector();
            for (final String label : set) {
                final int id = index.get(label);
                ids.add(id, 1.0);
                labelSets.put(id, ids);
            }
        }
        return labelSets;
    }

    private Map<Integer, Vector> generateGeneralLabelSets(final ObjectIndex<String> index) {
        final Map<String, Set<String>> labelsByPrefix = new HashMap<String, Set<String>>();
        for (final String label : index.keySet()) {
            String prefix;
            prefix = label.equals("O") ? label : label.substring(0, 5);
            Set<String> set = labelsByPrefix.get(prefix);
            if (set == null) labelsByPrefix.put(prefix, set = new HashSet<String>());
            set.add(label);
        }
        final Map<Integer, Vector> labelSets = new HashMap<Integer, Vector>();
        for (final Set<String> set : labelsByPrefix.values()) {
            final Vector ids = new SparseVector();
            for (final String label : set) {
                final int id = index.get(label);
                ids.add(id, 1.0);
                labelSets.put(id, ids);
            }
        }
        return labelSets;
    }

    public static ObjectIndex loadClassIds(final String filename) {
        final ObjectIndex<String> index = new ObjectIndex();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) index.add(line);
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException ignore) {
            }
        }
        return index;
    }

    public static void main(String[] args) throws AlephException, IOException {
        new ACE08PartialLabels().run();
    }
}
