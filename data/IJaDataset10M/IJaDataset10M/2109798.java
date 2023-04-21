package org.personalsmartspace.lm.bayesian.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.Services;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.personalsmartspace.cm.model.api.pss3p.ICtxHistoricAttribute;
import org.personalsmartspace.lm.api.ILearner;
import org.personalsmartspace.lm.api.ondemand.ILearningConsumer;
import org.personalsmartspace.lm.bayesian.fakeRule.DAGtoBall;
import org.personalsmartspace.lm.bayesian.fakeRule.structures.DAG;
import org.personalsmartspace.lm.bayesian.fakeRule.structures.Edge;
import org.personalsmartspace.lm.bayesian.fakeRule.structures.Node;
import org.personalsmartspace.lm.bayesian.impl.bayesianLibrary.bayesianLearner.exceptions.CountsNotCompleteException;
import org.personalsmartspace.lm.bayesian.impl.bayesianLibrary.bayesianLearner.impl.BNSegment;
import org.personalsmartspace.lm.bayesian.impl.bayesianLibrary.bayesianLearner.impl.BayesianNetworkCandidate;
import org.personalsmartspace.lm.bayesian.impl.bayesianLibrary.bayesianLearner.impl.BayesianNetworkCandidatesGenerator;
import org.personalsmartspace.lm.bayesian.impl.bayesianLibrary.bayesianLearner.impl.SimpleJointMeasurement;
import org.personalsmartspace.lm.bayesian.impl.bayesianLibrary.bayesianLearner.interfaces.BayesianProbabilitiesEstimator;
import org.personalsmartspace.lm.bayesian.impl.bayesianLibrary.bayesianLearner.interfaces.ConditionalProbabilityTable;
import org.personalsmartspace.lm.bayesian.impl.bayesianLibrary.bayesianLearner.interfaces.CountTable;
import org.personalsmartspace.lm.bayesian.impl.bayesianLibrary.bayesianLearner.interfaces.RandomVariable;
import org.personalsmartspace.lm.bayesian.impl.conversion.BNConverter;
import org.personalsmartspace.lm.bayesian.impl.conversion.HistoryConverter;
import org.personalsmartspace.lm.bayesian.impl.searchLibrary.greedySearch.impl.BasicGreedyHillClimber;
import org.personalsmartspace.lm.bayesian.impl.searchLibrary.greedySearch.interfaces.Candidate;
import org.personalsmartspace.lm.bayesian.impl.searchLibrary.greedySearch.interfaces.SearchConsumer;
import org.personalsmartspace.lm.bayesian.rule.BayesianRule;
import org.personalsmartspace.lm.tools.impl.DPIRetriever;
import org.personalsmartspace.lm.tools.impl.HistoryRetriever;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.pm.prefmodel.api.pss3p.Action;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;

@Component(name = "Bayesian Learning", immediate = true)
@Services(value = { @Service(value = ILearner.class) })
public class BayesianLearning implements SearchConsumer, ILearner<BayesianRule> {

    private PSSLog logging = new PSSLog(this);

    boolean debug = false;

    private HistoryRetriever historyRetriever;

    private PreProcessor preProcessor = new PreProcessor();

    private BayesianProbabilitiesEstimator learningEngine;

    private BayesianNetworkCandidate bestCandidate;

    private BNConverter converter = new BNConverter();

    private HistoryConverter inputProvider = new HistoryConverter();

    private int numberParents = 5;

    private boolean naiveBayes = false;

    private String naiveBayesianCause = "activity";

    private DPIRetriever dpiRetriever;

    protected void activate(ComponentContext cc) {
        BundleContext bc = cc.getBundleContext();
        historyRetriever = new HistoryRetriever(bc);
        dpiRetriever = new DPIRetriever(bc);
    }

    @Override
    public Set<BayesianRule> runLearning(String ctxAttrType, Date fake, Date fake2) {
        String[] as = { "busy", "free" };
        String[] bs = { "DangerousActivity", "IntellectualActivity", "CommunicationActivity", "IdleActivity", "PassiveActivity" };
        String[] cs = { "none", "Family", "Boss", "unknown" };
        String[] ds = { "WordProcessor", "VoIP", "Newscast", "NoActivity" };
        String[] es = { "morning", "afternoon", "evening", "night" };
        String[] fs = { "yes", "no" };
        String[] gs = { "Walking", "Standing", "Sitting" };
        String[] hs = { "Home", "Office", "MeetingRoom", "Outdoor" };
        String[] is = { "Noisy", "Conversation", "Quiet" };
        String[] js = { "high", "medium", "low" };
        String[] ks = { "Home", "Office", "MeetingRoom", "Outdoor" };
        String[] ls = { "Loud", "Medium", "Silent" };
        Node a = new Node("Availability", as);
        Node b = new Node("Activity", bs);
        Node c = new Node("Caller", cs);
        Node d = new Node("Used_Services", ds);
        Node e = new Node("Time", es);
        Node f = new Node("Working_Day", fs);
        Node g = new Node("Movement", gs);
        Node h = new Node("Location", hs);
        Node i = new Node("Noise_Level", is);
        Node j = new Node("MovementSensor", js);
        Node k = new Node("LocationSensor", ks);
        Node l = new Node("Microphone", ls);
        Edge ca = new Edge(c, a);
        Edge ba = new Edge(b, a);
        Edge bd = new Edge(b, d);
        Edge fb = new Edge(f, b);
        Edge eb = new Edge(e, b);
        Edge bg = new Edge(b, g);
        Edge bh = new Edge(b, h);
        Edge bi = new Edge(b, i);
        Edge hj = new Edge(h, j);
        Edge gj = new Edge(g, j);
        Edge hk = new Edge(h, k);
        Edge il = new Edge(i, l);
        ArrayList nodes = new ArrayList();
        ArrayList edges = new ArrayList();
        nodes.add(a);
        nodes.add(b);
        nodes.add(c);
        nodes.add(d);
        nodes.add(e);
        nodes.add(f);
        nodes.add(g);
        nodes.add(h);
        nodes.add(i);
        nodes.add(j);
        nodes.add(k);
        nodes.add(l);
        edges.add(ca);
        edges.add(ba);
        edges.add(bd);
        edges.add(fb);
        edges.add(eb);
        edges.add(bg);
        edges.add(bh);
        edges.add(bi);
        edges.add(hj);
        edges.add(gj);
        edges.add(hk);
        edges.add(il);
        double[] tempa = { 1, 0.7, 0.8, 0, 0.4, 1, 0.3, 0.5, 0, 0.4, 1, 0, 0.1, 0, 0.2, 1, 0.7, 0.8, 0, 0.4, 0, 0.3, 0.2, 1, 0.6, 0, 0.7, 0.5, 1, 0.6, 0, 1, 0.9, 1, 0.8, 0, 0.3, 0.2, 1, 0.6 };
        a.setProbDistribution(tempa);
        double[] tempb = { 0, 0, 0.2, 0.1, 0.1, 0.3, 0.2, 0.2, 0.5, 0.5, 0.2, 0, 0, 0.1, 0.1, 0, 0.3, 0.3, 0.1, 0.1, 0.1, 0.3, 0.2, 0.2, 0, 0, 0.2, 0.7, 0.5, 0.1, 0.2, 0.5, 0.2, 0.2, 0.3, 0.1, 0.3, 0.2, 0.3, 0.1 };
        b.setProbDistribution(tempb);
        double[] tempc = { 0.98, 0.1, .05, .05 };
        c.setProbDistribution(tempc);
        double[] tempd = { 0, 0.6, 0.1, 0, 0, 0.2, 0.3, 0.5, 0, 0, 0, 0, 0.2, 0, 0.8, 0.8, 0.1, 0.2, 1, 0.2 };
        d.setProbDistribution(tempd);
        double[] tempe = { 0.25, 0.25, .25, .25 };
        e.setProbDistribution(tempe);
        double[] tempf = { 0.6, 0.4 };
        f.setProbDistribution(tempf);
        double[] tempg = { 0.8, 0.15, 0.2, 0, 0.2, 0.2, 0.15, 0.2, 0.1, 0.1, 0, 0.7, 0.6, 0.9, 0.7 };
        g.setProbDistribution(tempg);
        double[] temph = { 0.1, 0.2, 0.25, 0.6, 0.5, 0.1, 0.5, 0.1, 0.1, 0.1, 0.1, 0.2, 0.4, 0.1, 0.2, 0.7, 0.1, 0.25, 0.2, 0.2 };
        h.setProbDistribution(temph);
        double[] tempi = { 0.5, 0, 0.1, 0.1, 0.3, 0, 0.3, 0.8, 0.1, 0.6, 0.5, 0.7, 0.1, 0.8, 0.1 };
        i.setProbDistribution(tempi);
        double[] tempj = { 0.4, 0, 0, 0.4, 0, 0, 0.4, 0, 0, 0.4, 0, 0, 0.5, 0.3, 0.1, 0.5, 0.3, 0.1, 0.5, 0.3, 0.1, 0.5, 0.3, 0.1, 0.1, 0.7, 0.9, 0.1, 0.7, 0.9, 0.1, 0.7, 0.9, 0.1, 0.7, 0.9 };
        j.setProbDistribution(tempj);
        double[] tempk = { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 };
        k.setProbDistribution(tempk);
        double[] templ = { 0.9, 0.2, 0, 0.1, 0.7, 0.1, 0, 0.1, 0.9 };
        l.setProbDistribution(templ);
        String ruleString = DAGtoBall.bayesletToBalLString(new DAG(nodes, edges));
        BayesianRule result = new BayesianRule();
        result.setRule(ruleString);
        ArrayList<String> inputs = new ArrayList<String>();
        inputs.add("Availability");
        ArrayList<String> outputs = new ArrayList<String>();
        outputs.add("Activity");
        outputs.add("Caller");
        outputs.add("Used_Services");
        outputs.add("Time");
        outputs.add("Working_Day");
        outputs.add("Movement");
        outputs.add("Location");
        outputs.add("Noise_Level");
        outputs.add("MovementSensor");
        outputs.add("LocationSensor");
        outputs.add("Microphone");
        result.setInputs(inputs);
        result.setInputs(outputs);
        Set<BayesianRule> ruleSet = new HashSet<BayesianRule>();
        ruleSet.add(result);
        return ruleSet;
    }

    @Override
    public void runLearning(String ctxAttrType, Date start, Date end, ILearningConsumer requestor) {
        IDigitalPersonalIdentifier[] dpis = dpiRetriever.getDPIs();
        Map<ICtxHistoricAttribute, List<ICtxHistoricAttribute>> history = historyRetriever.getUserHistory(start);
        BufferedReader reader = inputProvider.convertHistory2Stream(history);
        String ruleString = learn(reader, 60000);
        BayesianRule result = new BayesianRule();
        result.setRule(ruleString);
        Set<BayesianRule> ruleSet = new HashSet<BayesianRule>();
        ruleSet.add(result);
        requestor.handleLearntRules(ruleSet);
    }

    public void runLearning(Date date, int millisecs, ILearningConsumer requestor) {
        Map<ICtxHistoricAttribute, List<ICtxHistoricAttribute>> history = historyRetriever.getUserHistory(date);
        BufferedReader reader = inputProvider.convertHistory2Stream(history);
        String ruleString = learn(reader, millisecs);
        BayesianRule result = new BayesianRule();
        result.setRule(ruleString);
        Set<BayesianRule> ruleSet = new HashSet<BayesianRule>();
        ruleSet.add(result);
        requestor.handleLearntRules(ruleSet);
    }

    public void runLearning(Date date, String prefName, int millisecs, ILearningConsumer requestor) {
        Map<ICtxHistoricAttribute, List<ICtxHistoricAttribute>> history = historyRetriever.getUserHistory(date);
        Map<ICtxHistoricAttribute, List<ICtxHistoricAttribute>> extractedHistory = preProcessor.extractActions(history, new Action(prefName, null));
        BufferedReader reader = inputProvider.convertHistory2Stream(extractedHistory);
        String ruleString = learn(reader, millisecs);
        BayesianRule result = new BayesianRule();
        result.setRule(ruleString);
        Set<BayesianRule> ruleSet = new HashSet<BayesianRule>();
        ruleSet.add(result);
        requestor.handleLearntRules(ruleSet);
    }

    public void runLearning(Date date, Action action, int millisecs, ILearningConsumer requestor) {
        Map<ICtxHistoricAttribute, List<ICtxHistoricAttribute>> history = historyRetriever.getUserHistory(date);
        Map<ICtxHistoricAttribute, List<ICtxHistoricAttribute>> extractedHistory = preProcessor.extractActions(history, action);
        BufferedReader reader = inputProvider.convertHistory2Stream(extractedHistory);
        String ruleString = learn(reader, millisecs);
        BayesianRule result = new BayesianRule();
        result.setRule(ruleString);
        Set<BayesianRule> ruleSet = new HashSet<BayesianRule>();
        ruleSet.add(result);
        requestor.handleLearntRules(ruleSet);
    }

    public void notifyNewSearchResult(Candidate newCandidate, double oldBestscore, boolean stoppedExternally, int counter, int genCounter, int randomRestartsCounter, boolean isAbsoluteBest, boolean foundSignificantlyBetter) {
        String filename = null;
        if (isAbsoluteBest) {
            filename = ".\\resources\\bestfound.txt";
            BasicGreedyHillClimber.updateResultFiles(filename, oldBestscore, newCandidate, counter, genCounter, randomRestartsCounter, true);
            this.bestCandidate = (BayesianNetworkCandidate) newCandidate.cloneCandidate();
        }
        filename = ".\\resources\\closefound.txt";
        BasicGreedyHillClimber.updateResultFiles(filename, oldBestscore, newCandidate, counter, genCounter, randomRestartsCounter, !foundSignificantlyBetter);
    }

    private String learn(BufferedReader input, int milliseconds) {
        Map<String, RandomVariable> rvMap = new HashMap<String, RandomVariable>();
        BayesianNetworkCandidate starterBN;
        starterBN = importData(rvMap, input);
        if (naiveBayes) {
            String cause = naiveBayesianCause;
            RandomVariable causeRV = rvMap.get(cause);
            for (RandomVariable effect : rvMap.values()) {
                if (effect != causeRV) starterBN.addArc(effect, causeRV);
            }
            starterBN.computeFitness();
            boolean foundAbsoluteBest = true;
            boolean foundSignificantBetter = true;
            boolean stoppedExternally = false;
            notifyNewSearchResult(starterBN, 0, stoppedExternally, 1, 1, 0, foundAbsoluteBest, foundSignificantBetter);
        } else {
            BasicGreedyHillClimber bghc = new BasicGreedyHillClimber(new BayesianNetworkCandidatesGenerator(this.learningEngine, starterBN.getNodes(), numberParents), this);
            bghc.startSearch();
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bghc.stopSearch();
            if (debug) System.out.println("BGHC STOPPED!");
        }
        Map<RandomVariable, ConditionalProbabilityTable> finalNW = null;
        if (this.bestCandidate != null) {
            Map segments = this.bestCandidate.getSegments();
            finalNW = new HashMap<RandomVariable, ConditionalProbabilityTable>();
            Iterator it = segments.keySet().iterator();
            while (it.hasNext()) {
                RandomVariable rv = (RandomVariable) it.next();
                BNSegment seg = (BNSegment) segments.get(rv);
                CountTable ct = this.learningEngine.getCounts(rv, seg.getOrderedParents());
                if (debug) System.out.println(ct);
                try {
                    ConditionalProbabilityTable cpt = this.learningEngine.getCPT(rv, seg.getOrderedParents(), this.learningEngine.getUniformPriors(this.bestCandidate.getN_equiv(), rv, seg.getOrderedParents()));
                    finalNW.put(rv, cpt);
                } catch (CountsNotCompleteException e) {
                    e.printStackTrace();
                }
            }
        }
        return converter.produceWriteOut(finalNW);
    }

    private BayesianNetworkCandidate importData(Map rvMap, BufferedReader reader) {
        String data = "";
        String firstLine;
        try {
            firstLine = reader.readLine() + "\n";
            data += firstLine;
            String temp;
            while ((temp = reader.readLine()) != null) {
                if (!temp.equals(firstLine)) data += temp + "\n";
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleJointMeasurement[] sjmFile = SimpleJointMeasurement.computeFromData(rvMap, data);
        Set<RandomVariable> allnodesset = new HashSet<RandomVariable>();
        allnodesset.clear();
        this.learningEngine.resetTables();
        this.learningEngine.clearMeasurements();
        allnodesset.addAll(rvMap.values());
        for (int r = 0; r < sjmFile.length; r++) {
            if (debug) System.out.println("Adding sjm to Learning engine: " + sjmFile[r]);
            this.learningEngine.addMeasurement(sjmFile[r]);
        }
        BayesianNetworkCandidate starterBN = new BayesianNetworkCandidate(this.learningEngine, allnodesset);
        return starterBN;
    }
}
