package edu.arizona.cs.learn.timeseries;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;
import edu.arizona.cs.learn.algorithm.recognition.BPPNode;
import edu.arizona.cs.learn.algorithm.recognition.FSMConverter;
import edu.arizona.cs.learn.algorithm.recognition.FSMFactory;
import edu.arizona.cs.learn.algorithm.recognition.FSMRecognizer;
import edu.arizona.cs.learn.algorithm.recognition.FSMUtil;
import edu.arizona.cs.learn.timeseries.classification.Classifier;
import edu.arizona.cs.learn.timeseries.classification.Classify;
import edu.arizona.cs.learn.timeseries.classification.ClassifyCallable;
import edu.arizona.cs.learn.timeseries.classification.ClassifyParams;
import edu.arizona.cs.learn.timeseries.evaluation.BatchStatistics;
import edu.arizona.cs.learn.timeseries.model.Instance;
import edu.arizona.cs.learn.timeseries.model.Interval;
import edu.arizona.cs.learn.timeseries.model.SequenceType;
import edu.arizona.cs.learn.timeseries.model.signature.Signature;
import edu.arizona.cs.learn.timeseries.recognizer.RecognizeCallable;
import edu.arizona.cs.learn.timeseries.recognizer.Recognizer;
import edu.arizona.cs.learn.timeseries.recognizer.RecognizerStatistics;
import edu.arizona.cs.learn.util.DataMap;
import edu.arizona.cs.learn.util.Utils;
import edu.arizona.cs.learn.util.graph.Edge;
import edu.uci.ics.jung.graph.DirectedGraph;

public class Experiments {

    private static Logger logger = Logger.getLogger(Experiments.class);

    private String _sequenceType;

    private String _classify;

    private boolean _shuffle;

    private int _kFolds;

    private int _k;

    private int _cavePct;

    private boolean _fromFile;

    private ExecutorService _execute;

    public Experiments(int kFolds) {
        this._kFolds = kFolds;
    }

    public Experiments(String sequence, String classify, int kFolds, boolean shuffle, boolean fromFile) {
        this(kFolds);
        this._shuffle = shuffle;
        this._sequenceType = sequence;
        this._classify = classify;
        this._k = 10;
        this._cavePct = 50;
        this._fromFile = fromFile;
    }

    public void setCAVEPercent(int value) {
        this._cavePct = value;
    }

    public void setK(int value) {
        this._k = value;
    }

    public int getFolds() {
        return this._kFolds;
    }

    private void writeLog(String activityName, String classifierName, List<SequenceType> types, List<List<BatchStatistics>> values) {
        try {
            File f = new File("logs/" + activityName + "-" + classifierName + ".csv");
            BufferedWriter out = new BufferedWriter(new FileWriter(f));
            StringBuffer buf = new StringBuffer();
            buf.append("activity,classifier,cavePct,sequence,fold," + BatchStatistics.csvHeader());
            out.write(buf.toString() + "\n");
            System.out.println(buf.toString());
            for (int i = 0; i < types.size(); i++) {
                SequenceType type = types.get(i);
                List<BatchStatistics> results = values.get(i);
                for (int j = 0; j < results.size(); ++j) {
                    BatchStatistics batch = results.get(j);
                    String pre = activityName + "," + classifierName + "," + _cavePct + "," + type + "," + j + ",";
                    buf = new StringBuffer();
                    buf.append(batch.toCSV(pre, ""));
                    out.write(buf.toString());
                    System.out.println(buf.toString());
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<BatchStatistics> crossValidation(String activityName, Classifier c, SequenceType type, List<String> classNames) {
        logger.debug("Beginning cross validation");
        _execute = Executors.newFixedThreadPool(Utils.numThreads);
        Map<String, List<Instance>> data = Utils.load(activityName, type);
        if (_shuffle) {
            for (List<Instance> list : data.values()) for (Instance instance : list) Collections.shuffle(instance.sequence());
        }
        List<BatchStatistics> foldStats = new ArrayList<BatchStatistics>();
        for (int i = 0; i < _kFolds; i++) {
            logger.debug("Fold --- " + i);
            BatchStatistics fs = new BatchStatistics(c.getName(), classNames);
            Map<String, List<Integer>> testMap = Utils.getTestSet(activityName, _kFolds, i);
            Map<String, List<Instance>> training = new HashMap<String, List<Instance>>();
            List<Instance> test = new ArrayList<Instance>();
            for (String key : data.keySet()) {
                List<Instance> list = data.get(key);
                List<Integer> ignore = testMap.get(key);
                training.put(key, new ArrayList<Instance>());
                for (Instance instance : list) {
                    if (!ignore.contains(instance.id())) {
                        training.get(key).add(instance);
                    } else if (Utils.testExcludeSet.size() > 0) {
                        test.add(instance.copy());
                    } else {
                        test.add(instance);
                    }
                }
            }
            Map<String, Long> timing = c.train(i, training);
            for (String key : classNames) fs.addTrainingDetail(key, training.get(key).size(), timing.get(key));
            List<Future<ClassifyCallable>> future = new ArrayList<Future<ClassifyCallable>>();
            for (Instance instance : test) {
                future.add(_execute.submit(new ClassifyCallable(c, instance)));
            }
            for (Future<ClassifyCallable> results : future) {
                try {
                    ClassifyCallable callable = results.get();
                    fs.addTestDetail(callable.actual(), callable.predicted(), callable.duration());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            foldStats.add(fs);
        }
        _execute.shutdown();
        return foldStats;
    }

    public Map<String, RecognizerStatistics> recognition(String dataset, Recognizer r, SequenceType type, int minPct, boolean prune, boolean onlyStart) {
        return recognition(dataset, r, type, minPct, prune, onlyStart, false);
    }

    public Map<String, RecognizerStatistics> recognition(String dataset, Recognizer r, SequenceType type, int minPct, boolean prune, boolean onlyStart, boolean outputRecognizers) {
        return recognition(dataset, r, type, minPct, prune, onlyStart, outputRecognizers, false);
    }

    public Map<String, RecognizerStatistics> recognition(String dataset, Recognizer r, SequenceType type, int minPct, boolean prune, boolean onlyStart, boolean outputRecognizers, boolean optimizeRecognizers) {
        _execute = Executors.newFixedThreadPool(1);
        List<String> classes = Utils.getActivityNames(dataset);
        Map<String, List<Instance>> data = new HashMap<String, List<Instance>>();
        for (String className : classes) {
            data.put(className, Instance.load(new File("data/input/" + className + ".lisp")));
        }
        Map<String, RecognizerStatistics> map = new HashMap<String, RecognizerStatistics>();
        for (String className : classes) map.put(className, new RecognizerStatistics(className));
        for (int i = 0; i < _kFolds; i++) {
            Map<String, List<Integer>> testMap = Utils.getTestSet(dataset, _kFolds, i);
            List<FSMRecognizer> recognizers = new ArrayList<FSMRecognizer>();
            double testSize = 0.0D;
            String dir = "data/cross-validation/k" + _kFolds + "/fold-" + i + "/" + type + "/";
            String suffix = ".xml";
            if (prune) suffix = "-prune.xml";
            for (String className : data.keySet()) {
                String signatureFile = dir + className + suffix;
                FSMRecognizer mr = r.build(className, signatureFile, data.get(className), testMap.get(className), minPct, onlyStart);
                if (outputRecognizers) {
                    FSMFactory.toDot(mr.getGraph(), signatureFile.replace(".xml", ".dot"));
                }
                if (optimizeRecognizers) {
                    System.out.println("Optimizing FSM for " + className);
                    DirectedGraph<BPPNode, Edge> dfa = FSMConverter.convertNFAtoDFA(mr.getGraph());
                    mr = new FSMRecognizer(className, dfa);
                    if (outputRecognizers) {
                        FSMFactory.toDot(mr.getGraph(), signatureFile.replace(".xml", "-dfa.dot"));
                    }
                }
                recognizers.add(mr);
            }
            List<Future<RecognizeCallable>> future = new ArrayList<Future<RecognizeCallable>>();
            for (String className : testMap.keySet()) {
                for (Integer id : testMap.get(className)) {
                    System.out.println("Test: " + className + " -- " + id);
                    Instance testInstance = null;
                    List<Instance> instances = data.get(className);
                    for (Instance instance : instances) {
                        if (instance.id() == id) {
                            testInstance = instance;
                            break;
                        }
                    }
                    List<Interval> testItem = testInstance.intervals();
                    RecognizeCallable rc = new RecognizeCallable(recognizers, id, className, testItem);
                    future.add(_execute.submit(rc));
                }
                testSize += testMap.get(className).size();
            }
            for (Future<RecognizeCallable> results : future) {
                RecognizeCallable rr = null;
                try {
                    rr = results.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (String className : classes) {
                    RecognizerStatistics rs = map.get(className);
                    if (className.equals(rr.name())) {
                        if (rr.recognized(className)) {
                            rs.truePositives.add(Integer.valueOf(rr.id()));
                        } else {
                            rs.falseNegative.add(Integer.valueOf(rr.id()));
                        }
                    } else if (rr.recognized(className)) {
                        rs.falsePositive(rr.name(), rr.id());
                    } else {
                        rs.trueNegative(rr.name(), rr.id());
                    }
                }
            }
        }
        for (String className : map.keySet()) {
            RecognizerStatistics rs = map.get(className);
            logger.debug("--- " + className);
            logger.debug("        -- " + rs.tp() + "\t" + rs.fp());
            logger.debug("        -- " + rs.fn() + "\t" + rs.tn());
            logger.debug("      precision: " + rs.precision());
            logger.debug("      recall: " + rs.recall());
            logger.debug("      f-measure: " + rs.fscore());
        }
        _execute.shutdown();
        return map;
    }

    public void runComparison(String prefix) {
        Utils.LIMIT_RELATIONS = true;
        Utils.WINDOW = 5;
        List<String> fileNames = Utils.getActivityNames(prefix);
        logger.debug("Comparison for : " + prefix);
        List<Classify> classifyList = Classify.get(_classify);
        for (Classify c : classifyList) {
            List<SequenceType> typesTested = new ArrayList<SequenceType>();
            List<List<BatchStatistics>> testResults = new ArrayList<List<BatchStatistics>>();
            StringBuffer buf = new StringBuffer();
            buf.append(prefix + " & ");
            for (SequenceType type : SequenceType.get(_sequenceType)) {
                buf.append(type + " & ");
            }
            buf.append("\n");
            buf.append(prefix + " & ");
            for (SequenceType type : SequenceType.get(this._sequenceType)) {
                ClassifyParams params = new ClassifyParams();
                params.k = _k;
                params.type = type;
                params.prunePct = _cavePct;
                params.fromFiles = _fromFile;
                params.folds = getFolds();
                Classifier classifier = c.getClassifier(params);
                List<BatchStatistics> values = new ArrayList<BatchStatistics>();
                values = crossValidation(prefix, classifier, type, fileNames);
                SummaryStatistics ss = new SummaryStatistics();
                for (BatchStatistics fd : values) {
                    ss.addValue(fd.accuracy());
                }
                buf.append(toString(ss) + " & ");
                typesTested.add(type);
                testResults.add(values);
            }
            buf.append(" \\\\ \\hline");
            writeLog(prefix, c.toString(), typesTested, testResults);
            System.out.println(buf.toString());
        }
    }

    public static void main(String[] args) {
    }

    private static String toString(SummaryStatistics ss) {
        double meanPct = 100.0D * ss.getMean();
        double meanStd = 100.0D * ss.getStandardDeviation();
        return Utils.nf.format(meanPct) + "\\% & " + Utils.nf.format(meanStd);
    }

    public static void init(String pre, String c, String s, int pct, int k, int folds, boolean shuffle, boolean load) {
        Experiments cv = new Experiments(s, c, folds, shuffle, load);
        cv.setCAVEPercent(pct);
        cv.setK(k);
        List<String> prefixes = Utils.getPrefixes(pre);
        for (String prefix : prefixes) cv.runComparison(prefix);
    }

    public static void selectCrossValidation(String prefix, int folds) {
        Random r = new Random(System.currentTimeMillis());
        File crossDir = new File("data/cross-validation/");
        if (!crossDir.exists()) crossDir.mkdir();
        File kDir = new File("data/cross-validation/k" + folds + "/");
        if (!kDir.exists()) kDir.mkdir();
        List<String> classNames = new ArrayList<String>();
        for (File f : new File("data/input/").listFiles()) {
            if ((f.getName().startsWith(prefix)) && (f.getName().endsWith("lisp"))) {
                String name = f.getName();
                classNames.add(name.substring(0, name.indexOf(".lisp")));
            }
        }
        int k = folds;
        List<Map<String, List<Integer>>> sets = new ArrayList<Map<String, List<Integer>>>();
        for (int i = 0; i < k; i++) {
            Map<String, List<Integer>> map = new TreeMap<String, List<Integer>>();
            for (String c : classNames) map.put(c, new ArrayList<Integer>());
            sets.add(map);
        }
        for (String className : classNames) {
            String f = "data/input/" + className + ".lisp";
            List<Instance> instances = Instance.load(new File(f));
            List<Integer> ids = new ArrayList<Integer>();
            for (Instance i : instances) ids.add(i.id());
            Collections.shuffle(ids, r);
            for (int i = 0; i < ids.size(); ++i) {
                sets.get(i % k).get(className).add(ids.get(i));
            }
        }
        writeTestFile(prefix, classNames, sets, k);
        List<SequenceType> types = SequenceType.get("all");
        types.add(SequenceType.cba);
        for (SequenceType type : SequenceType.get("all")) for (int i = 0; i < k; i++) {
            String f = "data/cross-validation/k" + k + "/fold-" + i + "/" + type + "/";
            File file = new File(f);
            if (!file.exists()) file.mkdir();
        }
    }

    /**
	 * select the test set for each of the different folds
	 * in the cross-validation
	 * @param prefix
	 * @param k
	 */
    public static void writeTestFile(String prefix, List<String> classNames, List<Map<String, List<Integer>>> sets, int k) {
        try {
            for (int i = 0; i < k; i++) {
                String dir = "data/cross-validation/k" + k + "/fold-" + i + "/";
                if (!new File(dir).exists()) {
                    new File(dir).mkdir();
                }
                String f = dir + prefix + "-test.txt";
                BufferedWriter out = new BufferedWriter(new FileWriter(f));
                for (String c : classNames) {
                    out.write(c);
                    for (Integer id : sets.get(i).get(c)) {
                        out.write(" " + id);
                    }
                    out.write("\n");
                }
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void signatures(final String prefix, final SequenceType type, final int folds, final boolean prune) {
        class SignatureCallable implements Callable<Object> {

            private int _fold;

            public SignatureCallable(int fold) {
                _fold = fold;
            }

            @Override
            public Object call() throws Exception {
                System.out.println("Fold " + (_fold + 1));
                List<String> activityNames = Utils.getActivityNames(prefix);
                Map<String, List<Integer>> testMap = Utils.getTestSet(prefix, folds, _fold);
                System.out.println("  activityNames: " + activityNames);
                for (String activity : activityNames) {
                    System.out.println("..Building signature for " + activity);
                    File dataFile = new File("data/input/" + activity + ".lisp");
                    List<Instance> instances = Instance.load(activity, dataFile, type);
                    Collections.shuffle(instances);
                    String f = "data/cross-validation/k" + folds + "/fold-" + _fold + "/" + type + "/";
                    File file = new File(f);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    Signature s = new Signature(activity);
                    List<Integer> testSet = testMap.get(activity);
                    for (int i = 1; i <= instances.size(); i++) {
                        Instance instance = instances.get(i - 1);
                        if (testSet.contains(instance.id())) continue;
                        System.out.println("\t" + activity + " " + _fold + " Training: " + instance.id());
                        s.update(instance.sequence());
                        if (prune && (i % 10 == 0)) s = s.prune(3);
                    }
                    int half = s.trainingSize() / 2;
                    s = s.prune(half);
                    if (prune) s.toXML(f + activity + "-prune.xml"); else s.toXML(f + activity + ".xml");
                }
                return new Object();
            }
        }
        ExecutorService execute = Executors.newFixedThreadPool(Utils.numThreads);
        List<Future<Object>> list = new ArrayList<Future<Object>>();
        for (int fold = 0; fold < folds; ++fold) {
            list.add(execute.submit(new SignatureCallable(fold)));
        }
        for (Future<Object> results : list) {
            try {
                results.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        execute.shutdown();
    }

    public static void signatureTiming(String prefix, SequenceType type, int folds, boolean prune) {
        try {
            String log = "logs/" + prefix + "-" + type + "-" + folds + "-" + prune + "-timing.csv";
            BufferedWriter out = new BufferedWriter(new FileWriter(log));
            out.write("prefix,activity,type,folds,prune,fold,time\n");
            for (int fold = 0; fold < folds; fold++) {
                System.out.println("Fold -- " + fold);
                Map<String, List<Instance>> map = Utils.load(prefix, type);
                Map<String, List<Integer>> testMap = Utils.getTestSet(prefix, folds, fold);
                for (String key : map.keySet()) {
                    long start = System.currentTimeMillis();
                    Signature s = new Signature(key);
                    List<Integer> testSet = testMap.get(key);
                    List<Instance> list = map.get(key);
                    for (int i = 1; i <= list.size(); i++) {
                        Instance instance = (Instance) list.get(i - 1);
                        if (testSet.indexOf(Integer.valueOf(instance.id())) != -1) {
                            continue;
                        }
                        s.update(((Instance) list.get(i - 1)).sequence());
                        if ((prune) && (i % 10 == 0)) {
                            s = s.prune(3);
                        }
                    }
                    long end = System.currentTimeMillis();
                    long elapsed = end - start;
                    out.write(prefix + "," + key + "," + type + "," + folds + "," + prune + "," + fold + "," + elapsed + "\n");
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Decomposition experiment.
	 * 
	 * Added by Anh T.
	 */
    @SuppressWarnings("unchecked")
    public void decomposition(final String dataset, final String mainActivity, final String subActivity, final Recognizer r, final SequenceType type, final int minPct, final boolean prune, final boolean onlyStart, final boolean optimizeRecognizers, final int experimentsCount, final boolean compose) {
        final Random rand = new Random(System.currentTimeMillis());
        final String decompdir = "data/decomposition/" + type + "/";
        final String compdir = "data/composition/" + type + "/";
        final String sigDir = "data/signatures/temp/";
        final String suffix = (prune) ? "-prune" : "";
        final String mainKey = dataset + "-" + mainActivity;
        final String subKey = dataset + "-" + subActivity;
        final Map<String, List<Instance>> data = new HashMap<String, List<Instance>>();
        data.put(mainKey, Instance.load(new File("data/input/" + mainKey + ".lisp")));
        data.put(subKey, Instance.load(new File("data/input/" + subKey + ".lisp")));
        final Map<String, List<Instance>> instancesMap = Utils.load(dataset, type);
        List<Instance> list = instancesMap.get(subKey);
        Signature s = new Signature(subKey);
        for (int k = 1; k <= list.size(); k++) {
            Instance instance = list.get(k - 1);
            s.update(instance.sequence());
            if (prune && (k % 10 == 0)) s = s.prune(3);
        }
        int half = s.trainingSize() / 2;
        s = s.prune(half);
        s.toXML(sigDir + subKey + "-" + type + suffix + ".xml");
        final String subSignatureFile = sigDir + subKey + "-" + type + suffix + ".xml";
        class DecompositionCallable implements Callable<Object> {

            private int id;

            public DecompositionCallable(int id) {
                this.id = id;
            }

            @Override
            public Object call() throws Exception {
                int partitions = 4;
                int decompPortions = (compose) ? 1 : 2;
                int testPortions = (compose) ? 1 : 0;
                Map<String, Set<Integer>> decompTestMap = new HashMap<String, Set<Integer>>();
                List<Integer> dataKeys = new ArrayList<Integer>();
                for (Instance instance : data.get(mainKey)) dataKeys.add(instance.id());
                int compTestSize = (int) (data.get(mainKey).size() * (decompPortions + testPortions) / partitions);
                Set<Integer> mainDecompTestSet = new HashSet<Integer>();
                for (int j = 0; j < compTestSize; j++) {
                    mainDecompTestSet.add(dataKeys.remove(rand.nextInt(dataKeys.size())));
                }
                decompTestMap.put(mainKey, mainDecompTestSet);
                List<Instance> list = instancesMap.get(mainKey);
                Signature s = new Signature(mainKey);
                for (int k = 1; k <= list.size(); k++) {
                    Instance instance = list.get(k - 1);
                    if (decompTestMap.get(mainKey).contains(instance.id())) continue;
                    s.update(instance.sequence());
                    if (prune && (k % 10 == 0)) s = s.prune(3);
                }
                int half = s.trainingSize() / 2;
                s = s.prune(half);
                String mainSignatureFile = sigDir + mainKey + "-" + type + suffix + "-" + id + ".xml";
                s.toXML(mainSignatureFile);
                FSMRecognizer decompMain = r.build(mainKey, mainSignatureFile, data.get(mainKey), new ArrayList<Integer>(decompTestMap.get(mainKey)), minPct, onlyStart);
                FSMRecognizer main = r.build(mainKey, mainSignatureFile, data.get(mainKey), new ArrayList<Integer>(decompTestMap.get(mainKey)), minPct, onlyStart);
                if (optimizeRecognizers) {
                    decompMain = new FSMRecognizer(mainKey, FSMConverter.convertNFAtoDFA(decompMain.getGraph()));
                    main = new FSMRecognizer(mainKey, FSMConverter.convertNFAtoDFA(main.getGraph()));
                }
                FSMRecognizer sub = r.build(subKey, subSignatureFile, data.get(subKey), new ArrayList<Integer>(), minPct, onlyStart);
                if (optimizeRecognizers) {
                    sub = new FSMRecognizer(subKey, FSMConverter.convertNFAtoDFA(sub.getGraph()));
                }
                Map<String, Set<Integer>> testMap = new HashMap<String, Set<Integer>>();
                if (compose) {
                    int testSize = (int) (data.get(mainKey).size() * (testPortions) / partitions);
                    Set<Integer> testSet = new HashSet<Integer>();
                    for (int j = 0; j < testSize; j++) {
                        int instanceID = decompTestMap.get(mainKey).iterator().next();
                        decompTestMap.get(mainKey).remove(instanceID);
                        testSet.add(instanceID);
                    }
                    testMap.put(mainKey, testSet);
                }
                Set<BPPNode> actives = new HashSet<BPPNode>();
                List<Instance> instances = data.get(mainKey);
                for (Integer testID : decompTestMap.get(mainKey)) {
                    Instance testInstance = null;
                    for (Instance instance : instances) {
                        if (instance.id() == testID) {
                            testInstance = instance;
                            break;
                        }
                    }
                    List<Interval> testItem = testInstance.intervals();
                    int start = Integer.MAX_VALUE;
                    int end = 0;
                    for (Interval interval : testItem) {
                        start = Math.min(start, interval.start);
                        end = Math.max(end, interval.end);
                    }
                    decompMain.reset();
                    sub.reset();
                    boolean isMain = false;
                    boolean isSub = false;
                    Set<BPPNode> mainActive = new HashSet<BPPNode>();
                    Set<BPPNode> subActive = new HashSet<BPPNode>();
                    for (int j = start; j < end; j++) {
                        Set<Integer> props = new HashSet<Integer>();
                        for (Interval interval : testItem) {
                            if (interval.on(j)) {
                                props.add(interval.keyId);
                            }
                        }
                        isMain = decompMain.update(props, false);
                        if (!isSub) {
                            isSub = sub.update(props);
                            if (isSub) {
                                subActive.addAll(decompMain.getActive());
                                subActive.remove(decompMain.getStartState());
                            }
                        }
                        if (isMain) {
                            mainActive.addAll(decompMain.getActive());
                            mainActive.remove(decompMain.getStartState());
                            break;
                        }
                    }
                    if (isMain) {
                        if (isSub) {
                            actives.addAll(subActive);
                            for (BPPNode n : subActive) {
                                if (n.getColor().equalsIgnoreCase("red")) n.setColor("yellow"); else if (n.getColor().equalsIgnoreCase("white")) n.setColor("blue");
                            }
                        }
                        for (BPPNode n : mainActive) {
                            if (!n.isFinal() && decompMain.getGraph().getOutEdges(n).size() != 0) continue;
                            if (n.getColor().equalsIgnoreCase("blue")) n.setColor("yellow"); else if (n.getColor().equalsIgnoreCase("white")) n.setColor("red");
                        }
                    }
                }
                String file = decompdir + dataset + "-" + mainActivity + "-" + subActivity + suffix + "-" + id + ".dot";
                FSMFactory.toDot(decompMain.getGraph(), file);
                if (compose) {
                    if (actives.size() > 0) actives.add(decompMain.getStartState());
                    DirectedGraph<BPPNode, Edge> compositeGraph = FSMUtil.composePrefix(decompMain.getGraph(), actives, sub.getGraph());
                    FSMRecognizer comp = new FSMRecognizer(mainKey, compositeGraph);
                    file = compdir + dataset + "-" + mainActivity + "-" + subActivity + suffix + "-" + id + ".dot";
                    FSMFactory.toDot(main.getGraph(), file.replace(".dot", "-orig.dot"));
                    FSMFactory.toDot(comp.getGraph(), file.replace(".dot", "-comp.dot"));
                    double mismatches = 0;
                    double mainPrecision = 0;
                    double compPrecision = 0;
                    double totalSize = testMap.get(mainKey).size();
                    instances = data.get(mainKey);
                    for (Integer testID : testMap.get(mainKey)) {
                        Instance testInstance = null;
                        for (Instance instance : instances) {
                            if (instance.id() == testID) {
                                testInstance = instance;
                                break;
                            }
                        }
                        List<Interval> testItem = testInstance.intervals();
                        int start = Integer.MAX_VALUE;
                        int end = 0;
                        for (Interval interval : testItem) {
                            start = Math.min(start, interval.start);
                            end = Math.max(end, interval.end);
                        }
                        comp.reset();
                        main.reset();
                        boolean compOut = comp.test(testItem, start, end);
                        boolean mainOut = main.test(testItem, start, end);
                        mainPrecision += (mainOut) ? 1 : 0;
                        compPrecision += (compOut) ? 1 : 0;
                        mismatches += (mainOut != compOut) ? 1 : 0;
                    }
                    System.out.println("Composition Experiment " + mainActivity + "-" + subActivity + "-" + id + " completed.");
                    List<Object> temp = new ArrayList<Object>();
                    temp.add(this.id);
                    temp.add(mainPrecision / totalSize);
                    temp.add(compPrecision / totalSize);
                    temp.add(mismatches);
                    return temp;
                }
                System.out.println("Decomposition Experiment " + mainActivity + "-" + subActivity + "-" + id + " completed.");
                return new Object();
            }
        }
        _execute = Executors.newFixedThreadPool(Utils.numThreads);
        List<Future<Object>> jobs = new ArrayList<Future<Object>>();
        for (int id = 0; id < experimentsCount; ++id) {
            jobs.add(_execute.submit(new DecompositionCallable(id)));
        }
        List<Object> resultStats = new ArrayList<Object>();
        for (Future<Object> results : jobs) {
            try {
                if (compose) resultStats.add(results.get()); else results.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (compose) {
            try {
                String file = compdir + dataset + "-" + mainActivity + "-" + subActivity + suffix + ".csv";
                BufferedWriter out;
                out = new BufferedWriter(new FileWriter(file));
                out.write("experimentID,originalPrecision,compositePrecision,mismatches\n");
                for (Object o : resultStats) {
                    List<Object> stats = (List<Object>) o;
                    out.write(((Integer) stats.get(0)) + "," + ((Double) stats.get(1)) + "," + ((Double) stats.get(2)) + "," + ((Double) stats.get(3)) + "\n");
                }
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        _execute.shutdown();
    }

    /**
	 * Information Depth experiment.
	 * 
	 * Added by Anh T.
	 */
    public void informationDepth(final String dataset, final String[] activities, final String testActivity, final Recognizer r, final SequenceType type, final int minPct, final boolean prune, final boolean onlyStart, final boolean optimizeRecognizers) {
        final String depthdir = "data/infodepth/" + type + "/";
        final String suffix = (prune) ? "-prune" : "";
        final Map<String, List<Instance>> data = new HashMap<String, List<Instance>>();
        for (String activity : activities) {
            String key = dataset + "-" + activity;
            data.put(key, Instance.load(new File("data/input/" + key + ".lisp")));
        }
        Random rand = new Random(System.currentTimeMillis());
        List<Instance> testData = data.get(dataset + "-" + testActivity);
        Instance instance = testData.get(rand.nextInt(testData.size()));
        final List<Interval> testInstance = instance.intervals();
        final Map<String, List<Instance>> instancesMap = Utils.load(dataset, type);
        class InfoDepthCallable implements Callable<Object> {

            private String _key;

            public InfoDepthCallable(String key) {
                _key = key;
            }

            public Object call() throws Exception {
                System.out.println("Info depth experiment for " + _key + " started...");
                List<Instance> list = instancesMap.get(_key);
                Signature s = new Signature(_key);
                for (int k = 1; k <= list.size(); k++) {
                    Instance instance = list.get(k - 1);
                    s.update(instance.sequence());
                    if (prune && (k % 10 == 0)) s = s.prune(3);
                }
                int half = s.trainingSize() / 2;
                s = s.prune(half);
                FSMRecognizer recognizer = r.build(_key, s, minPct, onlyStart);
                if (optimizeRecognizers) {
                    recognizer = new FSMRecognizer(_key, FSMConverter.convertNFAtoDFA(recognizer.getGraph()));
                }
                FSMFactory.toDot(recognizer.getGraph(), depthdir + _key + suffix + ".dot");
                int start = Integer.MAX_VALUE;
                int end = 0;
                for (Interval interval : testInstance) {
                    start = Math.min(start, interval.start);
                    end = Math.max(end, interval.end);
                }
                recognizer.reset();
                StringBuffer results = new StringBuffer();
                List<BPPNode> actives = new ArrayList<BPPNode>();
                boolean isAccepted = false;
                for (int j = start; j < end; j++) {
                    Set<Integer> props = new HashSet<Integer>();
                    for (Interval interval : testInstance) {
                        if (interval.on(j)) {
                            props.add(interval.keyId);
                        }
                    }
                    isAccepted = recognizer.update(props, false);
                    actives = recognizer.getActive();
                    int maxActiveDepth = 0;
                    float maxActiveDepthRatio = 0;
                    for (BPPNode n : actives) {
                        maxActiveDepth = Math.max(maxActiveDepth, n.getActiveDepth());
                        maxActiveDepthRatio = Math.max(maxActiveDepthRatio, ((float) n.getActiveDepth()) / ((float) n.getActiveDepth() + n.getDistanceToFinal()));
                    }
                    results.append(_key.replace(dataset + "-", "") + "," + j + "," + maxActiveDepth + "," + maxActiveDepthRatio + "," + ((isAccepted) ? "true" : "false") + "," + type + "," + r.name() + "," + ((optimizeRecognizers) ? "true" : "false") + "," + ((prune) ? "true" : "false") + "," + minPct + "\n");
                }
                System.out.println("Info depth experiment for " + _key + " completed.");
                return results;
            }
        }
        _execute = Executors.newFixedThreadPool(Utils.numThreads);
        List<Future<Object>> jobs = new ArrayList<Future<Object>>();
        for (String activity : activities) {
            String key = dataset + "-" + activity;
            jobs.add(_execute.submit(new InfoDepthCallable(key)));
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
            String fileName = depthdir + "infodepth-" + dataset + "-" + testActivity + "-" + type + "-" + dateFormat.format(new Date()) + ".csv";
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write("className,timestep,maxactivedepth,maxactivedepthratio,accepted,type,recognizer,optimized,prune,minPct\n");
            for (Future<Object> results : jobs) {
                try {
                    StringBuffer res = (StringBuffer) results.get();
                    out.write(res.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        _execute.shutdown();
    }
}
