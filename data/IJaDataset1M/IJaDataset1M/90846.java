package main.naiveBayes.tests;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import y.base.Edge;
import y.base.EdgeCursor;
import y.base.EdgeList;
import y.base.EdgeMap;
import y.base.Node;
import y.base.NodeCursor;
import main.graph.FormTree;
import main.graph.FormTreeWithMapping;
import main.graph.GetCMGraph;
import main.graph.GetFormTreeWithMapping;
import main.graph.GetFormTree;
import main.graph.GraphAlgo;
import main.graph.ValuedGraph;
import main.logger.LoggerSetup;
import main.model.EdgeGivenPath;
import main.model.EdgePriors;
import main.model.Entity;
import main.model.MappingCandidate;
import main.model.OntoEle;
import main.model.OntoNodePair;
import main.model.OntoPath;
import main.model.Relationship;
import main.model.SteinerTree;
import main.naiveBayes.FindCorrespondence;
import main.naiveBayes.GetCandidateOntoTree;
import main.naiveBayes.LearnParameter;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

public class GetCandidateOntoTests extends TestCase {

    static {
        LoggerSetup.enable();
    }

    static Logger log = Logger.getLogger(GetCandidateOntoTests.class);

    /**
	 * Constructor
	 * 
	 * @param name
	 */
    public GetCandidateOntoTests(String name) {
        super(name);
    }

    /**
	 * Set-up is always called first by the JUnit test framework.
	 */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
	 * 
	 * test generating Steiner trees
	 * 
	 */
    public void testGetOntoSteiners() {
        try {
            File file = new File("ontologies/movieontology-1.owl");
            ValuedGraph cmg = GetCMGraph.getGraph(file);
            String fileName = "resources/test-FormTree-movie-1";
            ArrayList<FormTree> trees = GetFormTree.getFormTreesNodeToOnto(fileName, cmg);
            System.out.println(GetFormTree.displayFormTrees(trees, cmg));
            FormTree tree = trees.get(0);
            ArrayList<EdgeList> steiners = GetCandidateOntoTree.getOntoSteiners(tree, cmg);
            PrintWriter output = new PrintWriter(new File("ontologies/candidate-Steiners.output"));
            System.out.println("&&&&&&&&&&&&&&&&A Set of Steiner Trees&&&&&&&&&&&&&&&&&&&");
            output.println("&&&&&&&&&&&&&&&&A Set of Steiner Trees&&&&&&&&&&&&&&&&&&&");
            for (int i = 0; i < steiners.size(); i++) {
                System.out.println("<<<<<<<<<<<The " + i + "th Steiner:>>>>>>");
                output.println("<<<<<<<<<<<The " + i + "th Steiner:>>>>>>");
                EdgeList amst = steiners.get(i);
                for (EdgeCursor amstEc = amst.edges(); amstEc.ok(); amstEc.next()) {
                    Edge e = amstEc.edge();
                    OntoEle value = (OntoEle) cmg.getEdgeValue(e);
                    System.out.println(value.display());
                    output.println(value.display());
                }
                System.out.println("<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>");
                output.println("<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>");
                System.out.println("<<<<<<<<<<<<<<<<<<<<NodePair to Paths>>>>>>>>>>>>>>>>>>>");
                output.println("<<<<<<<<<<<<<<<<<<<<NodePair to Paths>>>>>>>>>>>>>>>>>>>");
                HashMap<ArrayList<String>, OntoPath> pairToPaths = GetCandidateOntoTree.getPathsFromSteiner(tree, amst, cmg);
                Iterator<ArrayList<String>> it = pairToPaths.keySet().iterator();
                while (it.hasNext()) {
                    ArrayList<String> pair = it.next();
                    OntoPath path = pairToPaths.get(pair);
                    String line = "<" + pair.get(0) + "," + pair.get(1) + "> :: ";
                    ArrayList<Edge> edges = path.getEdgePath();
                    for (Edge e : edges) {
                        OntoEle edgeValue = (OntoEle) cmg.getEdgeValue(e);
                        line += edgeValue.display() + "|";
                    }
                    line += "\n";
                    System.out.println(line);
                    output.println(line);
                }
                System.out.println("<<<<<<<<<<<<<<<<<<<<End of NodePair to Paths>>>>>>>>>>>>>>>>>>>");
                output.println("<<<<<<<<<<<<<<<<<<<<End of NodePair to Paths>>>>>>>>>>>>>>>>>>>");
            }
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            output.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * 
	 * mapping discovery and ranking
	 * 
	 */
    public void testGetOntoSteinersAndProbabilities() {
        try {
            File ontoFile = new File("ontologies/Bibliographic-Data-1.owl");
            ValuedGraph cmg = GetCMGraph.getGraph(ontoFile);
            String parametersFile = "resources/book.formTreesx5.graph.mapping";
            ArrayList<FormTreeWithMapping> trainingTrees = GetFormTreeWithMapping.getFormTrees(parametersFile, cmg);
            EdgePriors priors = LearnParameter.learnEdgePrior(trainingTrees, cmg);
            ArrayList<ArrayList<EdgeGivenPath>> likelihoods = LearnParameter.learnLikelihood(trainingTrees, cmg);
            String testFile = "resources/book.formTrees-1-20.mapping";
            ArrayList<FormTree> testTrees = GetFormTree.getFormTreesNodeToOnto(testFile, cmg);
            PrintWriter output = new PrintWriter(new File("ontologies/candidate-Steiners.output.book"));
            output.println("Total number of trees:" + testTrees.size());
            for (FormTree testTree : testTrees) {
                ArrayList<EdgeList> steiners = GetCandidateOntoTree.getOntoSteiners(testTree, cmg);
                TreeSet<MappingCandidate> orderedSteiners = new TreeSet<MappingCandidate>();
                for (int i = 0; i < steiners.size(); i++) {
                    EdgeList amst = steiners.get(i);
                    HashMap<ArrayList<String>, OntoPath> pairToPaths = GetCandidateOntoTree.getPathsFromSteiner(testTree, amst, cmg);
                    double prob = GetCandidateOntoTree.getConditionalProbability(pairToPaths, amst, testTree, cmg, priors, likelihoods);
                    MappingCandidate aCandidate = new MappingCandidate(amst);
                    aCandidate.setProbability(prob);
                    aCandidate.setPairToPaths(pairToPaths);
                    orderedSteiners.add(aCandidate);
                }
                System.out.println("&&&&&&&&&&&&&&&&A Set of Ordered Steiner Trees&&&&&&&&&&&&&&&&&&&");
                output.println("&&&&&&&&&&&&&&&&A Set of Ordered Steiner Trees&&&&&&&&&&&&&&&&&&&");
                System.out.println("Total unordered steiners: " + steiners.size() + ", " + "Total ordered steiners: " + orderedSteiners.size());
                output.println("Total unordered steiners: " + steiners.size() + ", " + "Total ordered steiners: " + orderedSteiners.size());
                Iterator<MappingCandidate> orderedIt = orderedSteiners.iterator();
                int i = 0;
                while (orderedIt.hasNext() && i < 11) {
                    i++;
                    System.out.println("<<<<<<<<<<<The " + i + "th Steiner:>>>>>>");
                    output.println("<<<<<<<<<<<The " + i + "th Steiner:>>>>>>");
                    MappingCandidate aCandidate = orderedIt.next();
                    System.out.println("Its conditional probability is: " + aCandidate.getProbability());
                    output.println("Its conditional probability is: " + aCandidate.getProbability());
                    EdgeList amst = aCandidate.getCandidateSteinerTree();
                    for (EdgeCursor amstEc = amst.edges(); amstEc.ok(); amstEc.next()) {
                        Edge e = amstEc.edge();
                        OntoEle value = (OntoEle) cmg.getEdgeValue(e);
                        System.out.println(value.display());
                        output.println(value.display());
                    }
                    System.out.println("<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>");
                    output.println("<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>");
                    System.out.println("<<<<<<<<<<<<<<<<<<<<NodePair to Paths>>>>>>>>>>>>>>>>>>>");
                    output.println("<<<<<<<<<<<<<<<<<<<<NodePair to Paths>>>>>>>>>>>>>>>>>>>");
                    HashMap<ArrayList<String>, OntoPath> pairToPaths = aCandidate.getPairToPaths();
                    Iterator<ArrayList<String>> it = pairToPaths.keySet().iterator();
                    while (it.hasNext()) {
                        ArrayList<String> pair = it.next();
                        OntoPath path = pairToPaths.get(pair);
                        String line = "<" + pair.get(0) + "," + pair.get(1) + "> :: ";
                        ArrayList<Edge> edges = path.getEdgePath();
                        for (Edge e : edges) {
                            OntoEle edgeValue = (OntoEle) cmg.getEdgeValue(e);
                            line += edgeValue.display() + "|";
                        }
                        line += "\n";
                        System.out.println(line);
                        output.println(line);
                    }
                    System.out.println("<<<<<<<<<<<<<<<<<<<<End of NodePair to Paths>>>>>>>>>>>>>>>>>>>");
                    output.println("<<<<<<<<<<<<<<<<<<<<End of NodePair to Paths>>>>>>>>>>>>>>>>>>>");
                }
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                output.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                output.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                output.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                output.println();
                output.println();
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * 
	 * mapping discovery and ranking using string matching for correspondences
	 * 
	 */
    public void testGetOntoSteinersAndProbabilitiesWithMatchingCorrespondences() {
        try {
            File ontoFile = new File("ontologies/Bibliographic-Data-1.owl");
            ValuedGraph cmg = GetCMGraph.getGraph(ontoFile);
            String parametersFile = "resources/book.formTreesx5.graph.mapping";
            ArrayList<FormTreeWithMapping> trainingTrees = GetFormTreeWithMapping.getFormTrees(parametersFile, cmg);
            EdgePriors priors = LearnParameter.learnEdgePrior(trainingTrees, cmg);
            ArrayList<ArrayList<EdgeGivenPath>> likelihoods = LearnParameter.learnLikelihood(trainingTrees, cmg);
            String testFile = "resources/book.formTrees-1-20.mapping";
            ArrayList<FormTree> testTrees = GetFormTree.getFormTreesNodeOwnLabels(testFile, cmg);
            PrintWriter output = new PrintWriter(new File("ontologies/candidate-Steiners.output.book"));
            output.println("Total number of trees:" + testTrees.size());
            for (FormTree aOrigTree : testTrees) {
                ArrayList<SteinerTree> steiners = GetCandidateOntoTree.getOntoSteinersWithMatchingCorrespondences(aOrigTree, cmg);
                TreeSet<MappingCandidate> orderedSteiners = new TreeSet<MappingCandidate>();
                for (int i = 0; i < steiners.size(); i++) {
                    SteinerTree aSteinerTree = steiners.get(i);
                    EdgeList amst = aSteinerTree.getSteiner();
                    FormTree testTree = aSteinerTree.getFormTree();
                    HashMap<ArrayList<String>, OntoPath> pairToPaths = GetCandidateOntoTree.getPathsFromSteiner(testTree, amst, cmg);
                    double prob = GetCandidateOntoTree.getConditionalProbability(pairToPaths, amst, testTree, cmg, priors, likelihoods);
                    MappingCandidate aCandidate = new MappingCandidate(amst);
                    aCandidate.setProbability(prob);
                    aCandidate.setPairToPaths(pairToPaths);
                    orderedSteiners.add(aCandidate);
                }
                System.out.println("&&&&&&&&&&&&&&&&A Set of Ordered Steiner Trees&&&&&&&&&&&&&&&&&&&");
                output.println("&&&&&&&&&&&&&&&&A Set of Ordered Steiner Trees&&&&&&&&&&&&&&&&&&&");
                System.out.println("Total unordered steiners: " + steiners.size() + ", " + "Total ordered steiners: " + orderedSteiners.size());
                output.println("Total unordered steiners: " + steiners.size() + ", " + "Total ordered steiners: " + orderedSteiners.size());
                Iterator<MappingCandidate> orderedIt = orderedSteiners.iterator();
                int i = 0;
                while (orderedIt.hasNext() && i < 11) {
                    i++;
                    System.out.println("<<<<<<<<<<<The " + i + "th Steiner:>>>>>>");
                    output.println("<<<<<<<<<<<The " + i + "th Steiner:>>>>>>");
                    MappingCandidate aCandidate = orderedIt.next();
                    System.out.println("Its conditional probability is: " + aCandidate.getProbability());
                    output.println("Its conditional probability is: " + aCandidate.getProbability());
                    EdgeList amst = aCandidate.getCandidateSteinerTree();
                    for (EdgeCursor amstEc = amst.edges(); amstEc.ok(); amstEc.next()) {
                        Edge e = amstEc.edge();
                        OntoEle value = (OntoEle) cmg.getEdgeValue(e);
                        System.out.println(value.display());
                        output.println(value.display());
                    }
                    System.out.println("<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>");
                    output.println("<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>");
                    System.out.println("<<<<<<<<<<<<<<<<<<<<NodePair to Paths>>>>>>>>>>>>>>>>>>>");
                    output.println("<<<<<<<<<<<<<<<<<<<<NodePair to Paths>>>>>>>>>>>>>>>>>>>");
                    HashMap<ArrayList<String>, OntoPath> pairToPaths = aCandidate.getPairToPaths();
                    Iterator<ArrayList<String>> it = pairToPaths.keySet().iterator();
                    while (it.hasNext()) {
                        ArrayList<String> pair = it.next();
                        OntoPath path = pairToPaths.get(pair);
                        String line = "<" + pair.get(0) + "," + pair.get(1) + "> :: ";
                        ArrayList<Edge> edges = path.getEdgePath();
                        for (Edge e : edges) {
                            OntoEle edgeValue = (OntoEle) cmg.getEdgeValue(e);
                            line += edgeValue.display() + "|";
                        }
                        line += "\n";
                        System.out.println(line);
                        output.println(line);
                    }
                    System.out.println("<<<<<<<<<<<<<<<<<<<<End of NodePair to Paths>>>>>>>>>>>>>>>>>>>");
                    output.println("<<<<<<<<<<<<<<<<<<<<End of NodePair to Paths>>>>>>>>>>>>>>>>>>>");
                }
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                output.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                output.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                output.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                output.println();
                output.println();
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new GetCandidateOntoTests("testGetOntoSteinersAndProbabilitiesWithMatchingCorrespondences"));
        return suite;
    }

    /**
	 * tearDown() (does nothing)
	 */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * The main routine
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
