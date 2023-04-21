package org.dllearner.scripts.matching;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.zip.DataFormatException;
import org.dllearner.algorithms.celoe.CELOE;
import org.dllearner.core.ComponentInitException;
import org.dllearner.core.ComponentManager;
import org.dllearner.core.LearningProblemUnsupportedException;
import org.dllearner.core.AbstractReasonerComponent;
import org.dllearner.kb.manipulator.Manipulator;
import org.dllearner.kb.manipulator.StringToResource;
import org.dllearner.kb.manipulator.Rule.Months;
import org.dllearner.kb.sparql.Cache;
import org.dllearner.kb.sparql.SPARQLTasks;
import org.dllearner.kb.sparql.SparqlEndpoint;
import org.dllearner.kb.sparql.SparqlKnowledgeSource;
import org.dllearner.learningproblems.PosOnlyLP;
import org.dllearner.reasoning.FastInstanceChecker;
import org.dllearner.refinementoperators.RhoDRDown;
import org.dllearner.utilities.Helper;

/**
 * Uses learning algorithms to learn class definitions for DBpedia
 * classes in LinkedGeoData. As a final result, we can classify most
 * objects in LinkedGeoData using the DBpedia ontology. 
 * 
 * @author Jens Lehmann
 *
 */
public class LearnOSMClasses {

    private static SparqlEndpoint dbpediaEndpoint = SparqlEndpoint.getEndpointLOCALDBpedia();

    public static void main(String args[]) throws IOException, DataFormatException, LearningProblemUnsupportedException, ComponentInitException {
        File matchesFile = new File("log/geodata/owlsameas_en.dat");
        Map<URI, URI> matches = Utility.getMatches(matchesFile);
        SPARQLTasks dbpedia = new SPARQLTasks(new Cache("cache/matching"), dbpediaEndpoint);
        Set<String> positives = new TreeSet<String>();
        Set<String> negatives = new TreeSet<String>();
        for (Entry<URI, URI> match : matches.entrySet()) {
            URI dbpediaURI = match.getKey();
            URI lgdURI = match.getValue();
            String query = "ASK {<" + dbpediaURI + "> a <http://dbpedia.org/ontology/Organisation>}";
            boolean isInClass = dbpedia.ask(query);
            if (isInClass) {
                positives.add(lgdURI.toString());
                System.out.println("+\"" + lgdURI + "\"");
            } else {
                negatives.add(lgdURI.toString());
                System.out.println("-\"" + lgdURI + "\"");
            }
        }
        Set<String> instances = new TreeSet<String>();
        instances.addAll(positives);
        instances.addAll(negatives);
        System.out.println(instances.size() + " instances - " + positives.size() + " positive examples");
        ComponentManager cm = ComponentManager.getInstance();
        SparqlKnowledgeSource ks = cm.knowledgeSource(SparqlKnowledgeSource.class);
        ks.setInstances(instances);
        ks.setPredefinedEndpoint("LOCALGEODATA");
        ks.setSaveExtractedFragment(true);
        Manipulator m = Manipulator.getDefaultManipulator();
        m.addRule(new StringToResource(Months.DECEMBER, "http://linkedgeodata.org/vocabulary", 0));
        ks.setManipulator(m);
        ks.init();
        AbstractReasonerComponent reasoner = cm.reasoner(FastInstanceChecker.class, ks);
        reasoner.init();
        PosOnlyLP lp = cm.learningProblem(PosOnlyLP.class, reasoner);
        lp.setPositiveExamples(Helper.getIndividualSet(positives));
        lp.init();
        CELOE celoe = cm.learningAlgorithm(CELOE.class, lp, reasoner);
        RhoDRDown op = (RhoDRDown) celoe.getOperator();
        op.setUseAllConstructor(false);
        op.setUseCardinalityRestrictions(false);
        op.setUseBooleanDatatypes(false);
        op.setUseDoubleDatatypes(false);
        op.setUseNegation(false);
        op.setUseHasValueConstructor(true);
        op.setFrequencyThreshold(3);
        celoe.setMaxExecutionTimeInSeconds(100);
        celoe.setNoisePercentage(0.2);
        celoe.init();
        celoe.start();
    }
}
