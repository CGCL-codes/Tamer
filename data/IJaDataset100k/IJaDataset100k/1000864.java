package org.ontospread.process.run;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;
import org.ontospread.constraints.OntoSpreadRelationWeight;
import org.ontospread.constraints.OntoSpreadRelationWeightImpl;
import org.ontospread.dao.DAOUtils;
import org.ontospread.exceptions.ConceptNotFoundException;
import org.ontospread.process.OntoSpreadTestUtils;
import org.ontospread.state.OntoSpreadCommonUtils;
import org.ontospread.state.OntoSpreadState;
import org.ontospread.state.UriDepthPair;
import org.ontospread.to.PathTO;
import org.ontospread.to.ScoredConceptTO;

public class OntoSpreadRunTest extends TestCase {

    public void testOntoRun() throws ConceptNotFoundException {
        String[] conceptUris = new String[] { "http://websemantica.fundacionctic.org/ontologias/bopa/piscina.owl#Piscina" };
        int min = 1;
        int max = 3;
        double minScore = 1.0;
        OntoSpreadRun runOntoSpread = new OntoSpreadRunImpl(DAOUtils.createOntologyDAO(), OntoSpreadTestUtils.createStopStrategy(min, max, minScore), OntoSpreadTestUtils.createSelectStrategy(), new OntoSpreadRelationWeightImpl());
        OntoSpreadState ontoSpreadState = new OntoSpreadState();
        ontoSpreadState.setInitialConcepts(OntoSpreadTestUtils.createScoredConcepts(conceptUris, 1.0));
        List<UriDepthPair> sortedList = new LinkedList<UriDepthPair>();
        for (int i = 0; i < conceptUris.length; i++) {
            sortedList.add(new UriDepthPair(conceptUris[i], 1));
        }
        ontoSpreadState.setSortedList(sortedList);
        while (runOntoSpread.hasIteration(ontoSpreadState)) {
            runOntoSpread.applyIteration(ontoSpreadState);
        }
        ontoSpreadState.setCurrentScore(minScore + 1);
    }

    public void testHasIteration() throws ConceptNotFoundException {
        String[] conceptUris = new String[] { "http://websemantica.fundacionctic.org/ontologias/bopa/piscina.owl#Piscina" };
        int min = 1;
        int max = 5;
        OntoSpreadRun runOntoSpread = new OntoSpreadRunImpl(DAOUtils.createOntologyDAO(), OntoSpreadTestUtils.createStopStrategy(min, max, 1.0), OntoSpreadTestUtils.createSelectStrategy(), new OntoSpreadRelationWeightImpl());
        OntoSpreadState ontoSpreadState = new OntoSpreadState();
        ontoSpreadState.setInitialConcepts(OntoSpreadTestUtils.createScoredConcepts(conceptUris, 1.0));
        List<UriDepthPair> sortedList = new LinkedList<UriDepthPair>();
        for (int i = 0; i < conceptUris.length; i++) {
            sortedList.add(new UriDepthPair(conceptUris[i], 1));
        }
        ontoSpreadState.setCurrentScore(2.0);
        ontoSpreadState.setSortedList(sortedList);
        assertTrue(runOntoSpread.hasIteration(ontoSpreadState));
        assertEquals(0, ontoSpreadState.getSpreadedConcepts().size());
    }

    public void testOntoRun2() throws ConceptNotFoundException {
        String[] conceptUris = new String[] { "http://websemantica.fundacionctic.org/ontologias/bopa/piscina.owl#Piscina" };
        int min = 1;
        int max = 3;
        OntoSpreadRun runOntoSpread = new OntoSpreadRunImpl(DAOUtils.createOntologyDAO(), OntoSpreadTestUtils.createStopStrategy(min, max, 1.0), OntoSpreadTestUtils.createSelectStrategy(), new OntoSpreadRelationWeightImpl());
        OntoSpreadState ontoSpreadState = new OntoSpreadState();
        ontoSpreadState.setInitialConcepts(OntoSpreadTestUtils.createScoredConcepts(conceptUris, 1.0));
        setupInitialConcepts(ontoSpreadState, 1.0);
        List<UriDepthPair> sortedList = new LinkedList<UriDepthPair>();
        for (int i = 0; i < conceptUris.length; i++) {
            sortedList.add(new UriDepthPair(conceptUris[i], 1));
        }
        ontoSpreadState.setCurrentScore(2.0);
        ontoSpreadState.setSortedList(sortedList);
        while (runOntoSpread.hasIteration(ontoSpreadState)) {
            runOntoSpread.applyIteration(ontoSpreadState);
        }
        assertEquals(1, ontoSpreadState.getSpreadedConcepts().size());
    }

    private double setupInitialConcepts(OntoSpreadState ontoSpreadState, double initialScore) throws ConceptNotFoundException {
        ScoredConceptTO[] initialConcepts = ontoSpreadState.getInitialConcepts();
        List<UriDepthPair> sortedList = new LinkedList<UriDepthPair>();
        for (int i = 0; i < initialConcepts.length; i++) {
            String initialConcept = initialConcepts[i].getConceptUri();
            OntoSpreadCommonUtils.registerSpreadPath(ontoSpreadState, initialConcept, null, new PathTO[0]);
            OntoSpreadCommonUtils.addToConceptStack(ontoSpreadState, initialConcept, 1);
            OntoSpreadCommonUtils.addScore(ontoSpreadState, initialConcepts[i].getScore(), initialConcept);
            initialScore = Math.max(initialScore, initialConcepts[i].getScore());
            sortedList.add(new UriDepthPair(initialConcepts[i].getConceptUri(), 1));
        }
        ontoSpreadState.setSortedList(sortedList);
        return initialScore;
    }
}
