package net.sourceforge.ondex.algorithm.ndfsm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import junit.framework.TestCase;
import net.sourceforge.ondex.algorithm.ndfsm.exceptions.StateMachineInvalidException;
import net.sourceforge.ondex.core.CV;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.EvidenceType;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.core.base.ONDEXViewImpl;
import net.sourceforge.ondex.core.memory.MemoryONDEXGraph;
import net.sourceforge.ondex.core.security.Session;
import net.sourceforge.ondex.core.util.SparseBitSet;

/**
 * Tests the Traversal of Graph using a state machine
 * @author hindlem
 *
 */
public class GraphTraverserTest extends TestCase {

    private MemoryONDEXGraph aog;

    private ConceptClass ccProtein;

    private ConceptClass ccEnzyme;

    private ConceptClass ccReaction;

    private RelationType rts_is_a;

    private RelationType rts_interacts;

    private RelationType rts_cats;

    private EvidenceType et;

    private CV cv;

    public void setUp() {
        aog = new MemoryONDEXGraph("testGraph");
        ccProtein = aog.getMetaData().getFactory().createConceptClass("Protein");
        ccEnzyme = aog.getMetaData().getFactory().createConceptClass("Enzyme");
        ccReaction = aog.getMetaData().getFactory().createConceptClass("Reaction");
        rts_is_a = aog.getMetaData().getFactory().createRelationType("is_a");
        rts_interacts = aog.getMetaData().getFactory().createRelationType("interacts");
        rts_cats = aog.getMetaData().getFactory().createRelationType("cats");
        et = aog.getMetaData().getFactory().createEvidenceType("I made it up");
        cv = aog.getMetaData().getFactory().createCV("matts db");
        Session.setSessionForThread(Session.NONE);
    }

    public void tearDown() {
        aog = null;
    }

    public void testSimpleGraphTraversal() throws StateMachineInvalidException {
        StateMachine sm = new StateMachine();
        State p = new State(ccProtein);
        State e = new State(ccEnzyme);
        State r = new State(ccReaction);
        Transition t = new Transition(rts_is_a);
        Transition c = new Transition(rts_cats);
        sm.setStartingState(p);
        sm.addFinalState(r);
        sm.addStep(p, t, e);
        sm.addStep(e, c, r);
        GraphTraverser gt = new GraphTraverser(sm);
        ONDEXConcept protein1 = aog.createConcept("Protein 1", cv, ccProtein, et);
        ONDEXConcept protein2 = aog.createConcept("Protein 2", cv, ccProtein, et);
        ONDEXConcept enzyme1 = aog.createConcept("Enzyme 1", cv, ccEnzyme, et);
        ONDEXConcept reaction1 = aog.createConcept("Reaction 1", cv, ccReaction, et);
        ONDEXRelation is_a1 = aog.createRelation(protein1, enzyme1, rts_is_a, et);
        ONDEXRelation is_a2 = aog.createRelation(protein2, enzyme1, rts_is_a, et);
        aog.createRelation(enzyme1, reaction1, rts_cats, et);
        List<StateMachineDerivedPath> routes = gt.traverseGraph(aog, aog.getConcepts());
        assertEquals(2, routes.size());
        for (StateMachineDerivedPath route : routes) {
            assertEquals(2, route.getTransitionLengthOfRoute());
            assertEquals(3, route.getStateLengthOfRoute());
            for (int i = 0; i < route.getStateLengthOfRoute(); i++) {
                int concept = route.getConceptAtPostion(i);
                Integer relation = null;
                if (i < route.getTransitionLengthOfRoute() - 1) {
                    relation = route.getRelationAtPosition(i);
                }
                if (i == 0) {
                    if (concept == protein1.getId().intValue()) {
                        assertEquals(is_a1.getId().intValue(), relation.intValue());
                    } else if (concept == protein2.getId().intValue()) {
                        assertEquals(is_a2.getId().intValue(), relation.intValue());
                    } else {
                        fail("unknown concept at position 0 in route :" + concept + " :" + aog.getConcept(concept).getPID());
                    }
                }
            }
        }
    }

    public void testNPathLengthGraph() throws StateMachineInvalidException {
        StateMachine sm = new StateMachine();
        State p = new State(ccProtein);
        State e = new State(ccEnzyme);
        State r = new State(ccReaction);
        Transition t = new Transition(rts_interacts);
        Transition i = new Transition(rts_is_a);
        Transition c = new Transition(rts_cats);
        sm.setStartingState(p);
        sm.addFinalState(r);
        sm.addStep(p, t, p);
        sm.addStep(p, i, e);
        sm.addStep(e, c, r);
        GraphTraverser gt = new GraphTraverser(sm);
        ONDEXConcept protein1 = aog.createConcept("Protein 1a", cv, ccProtein, et);
        ONDEXConcept protein2 = aog.createConcept("Protein 2a", cv, ccProtein, et);
        ONDEXConcept protein3 = aog.createConcept("Protein 3a", cv, ccProtein, et);
        ONDEXConcept protein4 = aog.createConcept("Protein 4a", cv, ccProtein, et);
        ONDEXConcept enzyme1 = aog.createConcept("Enzyme 1", cv, ccEnzyme, et);
        ONDEXConcept reaction1 = aog.createConcept("Reaction 1", cv, ccReaction, et);
        aog.createRelation(protein1, protein2, rts_interacts, et);
        aog.createRelation(protein2, protein3, rts_interacts, et);
        aog.createRelation(protein3, protein4, rts_interacts, et);
        aog.createRelation(protein4, enzyme1, rts_is_a, et);
        aog.createRelation(enzyme1, reaction1, rts_cats, et);
        List<StateMachineDerivedPath> routes = gt.traverseGraph(aog, aog.getConcepts());
        assertEquals(4, routes.size());
    }

    public void testLoopedGraphTraversal() throws StateMachineInvalidException {
        StateMachine sm = new StateMachine();
        State p = new State(ccProtein);
        State e = new State(ccEnzyme);
        State r = new State(ccReaction);
        Transition t = new Transition(rts_interacts);
        Transition i = new Transition(rts_is_a);
        Transition c = new Transition(rts_cats);
        sm.setStartingState(p);
        sm.addFinalState(r);
        sm.addStep(p, t, p);
        sm.addStep(p, i, e);
        sm.addStep(e, c, r);
        GraphTraverser gt = new GraphTraverser(sm);
        ONDEXConcept protein1 = aog.createConcept("Protein 1a", cv, ccProtein, et);
        ONDEXConcept protein2 = aog.createConcept("Protein 2a", cv, ccProtein, et);
        ONDEXConcept protein3 = aog.createConcept("Protein 3a", cv, ccProtein, et);
        ONDEXConcept protein4 = aog.createConcept("Protein 4a", cv, ccProtein, et);
        ONDEXConcept enzyme1 = aog.createConcept("Enzyme 1", cv, ccEnzyme, et);
        ONDEXConcept reaction1 = aog.createConcept("Reaction 1", cv, ccReaction, et);
        aog.createRelation(protein1, protein2, rts_interacts, et);
        aog.createRelation(protein2, protein3, rts_interacts, et);
        aog.createRelation(protein3, protein4, rts_interacts, et);
        aog.createRelation(protein3, protein4, rts_interacts, et);
        aog.createRelation(protein3, protein1, rts_interacts, et);
        aog.createRelation(protein4, enzyme1, rts_is_a, et);
        aog.createRelation(enzyme1, reaction1, rts_cats, et);
        SparseBitSet sbs = new SparseBitSet();
        sbs.set(protein1.getId().intValue());
        List<StateMachineDerivedPath> routes = gt.traverseGraph(aog, new ONDEXViewImpl<ONDEXConcept>(aog, ONDEXConcept.class, sbs));
        assertEquals(2, routes.size());
    }

    public void testLengthRestrictedTraversal() throws StateMachineInvalidException {
        System.out.println("length");
        StateMachine sm = new StateMachine();
        State p = new State(ccProtein);
        State e = new State(ccEnzyme);
        State r = new State(ccReaction);
        Transition t = new Transition(rts_interacts, 1, false);
        Transition i = new Transition(rts_is_a);
        Transition c = new Transition(rts_cats);
        sm.setStartingState(p);
        sm.addFinalState(r);
        sm.addStep(p, t, p);
        sm.addStep(p, i, e);
        sm.addStep(e, c, r);
        GraphTraverser gt = new GraphTraverser(sm);
        ONDEXConcept protein1 = aog.createConcept("Protein 1a", cv, ccProtein, et);
        ONDEXConcept protein2 = aog.createConcept("Protein 2a", cv, ccProtein, et);
        ONDEXConcept protein3 = aog.createConcept("Protein 3a", cv, ccProtein, et);
        ONDEXConcept protein4 = aog.createConcept("Protein 4a", cv, ccProtein, et);
        ONDEXConcept enzyme1 = aog.createConcept("Enzyme 1", cv, ccEnzyme, et);
        ONDEXConcept reaction1 = aog.createConcept("Reaction 1", cv, ccReaction, et);
        aog.createRelation(protein1, protein2, rts_interacts, et);
        aog.createRelation(protein2, protein3, rts_interacts, et);
        aog.createRelation(protein3, protein4, rts_interacts, et);
        aog.createRelation(protein3, protein4, rts_interacts, et);
        aog.createRelation(protein3, protein1, rts_interacts, et);
        aog.createRelation(protein4, enzyme1, rts_is_a, et);
        aog.createRelation(protein1, enzyme1, rts_is_a, et);
        aog.createRelation(enzyme1, reaction1, rts_cats, et);
        SparseBitSet sbs = new SparseBitSet();
        sbs.set(protein1.getId().intValue());
        List<StateMachineDerivedPath> routes = gt.traverseGraph(aog, new ONDEXViewImpl<ONDEXConcept>(aog, ONDEXConcept.class, sbs));
        assertEquals(1, routes.size());
    }

    public void testLargeGraph() throws StateMachineInvalidException {
        StateMachine sm = new StateMachine();
        State p = new State(ccProtein);
        State e = new State(ccEnzyme);
        State r = new State(ccReaction);
        Transition t = new Transition(rts_interacts);
        Transition i = new Transition(rts_is_a);
        Transition c = new Transition(rts_cats);
        sm.setStartingState(p);
        sm.addFinalState(r);
        sm.addStep(p, t, p);
        sm.addStep(p, i, e);
        sm.addStep(e, c, r);
        GraphTraverser gt = new GraphTraverser(sm);
        ArrayList<Integer> protiens = new ArrayList<Integer>();
        for (int j = 0; j < 20000; j++) {
            ONDEXConcept protein = aog.createConcept("Protein " + i, cv, ccProtein, et);
            ONDEXConcept enzyme = aog.createConcept("Enzyme " + i, cv, ccEnzyme, et);
            ONDEXConcept reaction = aog.createConcept("Reaction " + i, cv, ccReaction, et);
            aog.createRelation(protein, enzyme, rts_is_a, et);
            aog.createRelation(enzyme, reaction, rts_cats, et);
            protiens.add(protein.getId());
        }
        for (int j = 0; j < protiens.size(); j++) {
            aog.createRelation(aog.getConcept(protiens.get(j)), aog.getConcept(protiens.get((protiens.size() - 1) - j)), rts_interacts, et);
        }
        System.out.println("starting graph traversal");
        List<StateMachineDerivedPath> routes = gt.traverseGraph(aog, aog.getConceptsOfConceptClass(ccProtein));
        assertEquals(60000, routes.size());
        for (StateMachineDerivedPath route : routes) {
            HashSet<Integer> concepts = new HashSet<Integer>();
            for (int j = 0; j < route.getStateLengthOfRoute(); j++) {
                int conceptId = route.getConceptAtPostion(j);
                concepts.add(conceptId);
            }
            assertTrue(concepts.size() == route.getStateLengthOfRoute());
            assertTrue(concepts.size() == route.getAllConcepts().size());
            HashSet<Integer> relations = new HashSet<Integer>();
            for (int j = 0; j < route.getTransitionLengthOfRoute(); j++) {
                int relationId = route.getRelationAtPosition(j);
                relations.add(relationId);
            }
            assertTrue(relations.size() == route.getTransitionLengthOfRoute());
            assertTrue(relations.size() == route.getAllRelations().size());
        }
    }
}
