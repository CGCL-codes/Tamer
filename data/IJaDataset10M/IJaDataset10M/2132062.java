package net.sourceforge.ondex.algorithm.ndfsm;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.HashSet;
import java.util.Set;
import net.sourceforge.ondex.algorithm.ndfsm.exceptions.StateDoesNotExistException;
import net.sourceforge.ondex.algorithm.ndfsm.exceptions.TransitionDoesNotExistException;

/**
 * defines a non cyclical route through a graph
 * @author hindlem
 *
 */
public class Route {

    private int[] conceptIdsInPositionOrder;

    private int[] relationIdsInPositionOrder;

    private Int2ObjectOpenHashMap<int[]> state2ConceptId;

    private Int2ObjectOpenHashMap<int[]> transitionId2RelationId;

    /**
	 * 
	 * @param conceptId conceptId starting concept
	 * @param stateId the starting state for this concept
	 */
    public Route(int conceptId, int stateId) {
        conceptIdsInPositionOrder = new int[1];
        conceptIdsInPositionOrder[0] = conceptId;
        initState2ConceptId(null);
        addToMappedSet(stateId, conceptId, state2ConceptId);
    }

    private Route() {
    }

    private void initState2ConceptId(Int2ObjectOpenHashMap<int[]> existing) {
        if (existing == null) state2ConceptId = new Int2ObjectOpenHashMap<int[]>(5, Hash.VERY_FAST_LOAD_FACTOR); else state2ConceptId = new Int2ObjectOpenHashMap<int[]>(existing, Hash.VERY_FAST_LOAD_FACTOR);
        state2ConceptId.defaultReturnValue(null);
        state2ConceptId.growthFactor(3);
    }

    private void initTransition2RelationId(Int2ObjectOpenHashMap<int[]> existing) {
        if (existing == null) transitionId2RelationId = new Int2ObjectOpenHashMap<int[]>(5, Hash.VERY_FAST_LOAD_FACTOR); else transitionId2RelationId = new Int2ObjectOpenHashMap<int[]>(existing, Hash.VERY_FAST_LOAD_FACTOR);
        transitionId2RelationId.defaultReturnValue(null);
        transitionId2RelationId.growthFactor(3);
    }

    /**
	 * returns the concept at the specified position in the route (e.g. starting concept = 0, outgoing relation = 0, target concept = 1)
	 * @param i the position in the route (starting from 0)
	 * @return the id for the concept
	 */
    public int getConceptAtPostion(int i) {
        return conceptIdsInPositionOrder[i];
    }

    /**
	 * 
	 * @return all the concepts that are traversed in the route
	 */
    public Set<Integer> getAllConcepts() {
        Set<Integer> concepts = new HashSet<Integer>();
        for (int concept : conceptIdsInPositionOrder) {
            concepts.add(concept);
        }
        return concepts;
    }

    /**
	 * returns the relation at the specified position in the route
	 * @param i the position in the route (starting from 0)
	 * @return the id for the relation
	 */
    public int getRelationAtPosition(int i) {
        if (i > conceptIdsInPositionOrder.length - 2) {
            throw new IndexOutOfBoundsException("There is no relation associated with position " + i);
        }
        int value = relationIdsInPositionOrder[i];
        return value;
    }

    /**
	 * 
	 * @return all the concepts that are traversed in the route
	 */
    public Set<Integer> getAllRelations() {
        Set<Integer> relations = new HashSet<Integer>();
        for (int relation : relationIdsInPositionOrder) {
            relations.add(relation);
        }
        return relations;
    }

    /**
	 * 
	 * @return the number of concepts in the route
	 */
    public int getLengthOfRoute() {
        return conceptIdsInPositionOrder.length;
    }

    /**
	 * 
	 * @param conceptId the target concept where this traversal is to
	 * @param stateId the state that represents this target concept
	 * @param relationId the relationship that is the traversal
	 * @param transitionId the transition that directed this traversal
	 */
    public void addRouteStep(int conceptId, int stateId, int relationId, int transitionId) {
        int[] newConceptArray = new int[conceptIdsInPositionOrder.length + 1];
        System.arraycopy(conceptIdsInPositionOrder, 0, newConceptArray, 0, conceptIdsInPositionOrder.length);
        conceptIdsInPositionOrder = newConceptArray;
        if (relationIdsInPositionOrder != null) {
            int[] newRelationArray = new int[relationIdsInPositionOrder.length + 1];
            System.arraycopy(relationIdsInPositionOrder, 0, newRelationArray, 0, relationIdsInPositionOrder.length);
            relationIdsInPositionOrder = newRelationArray;
        } else {
            relationIdsInPositionOrder = new int[1];
        }
        conceptIdsInPositionOrder[conceptIdsInPositionOrder.length - 1] = conceptId;
        relationIdsInPositionOrder[relationIdsInPositionOrder.length - 1] = relationId;
        if (state2ConceptId == null) {
            initState2ConceptId(null);
        }
        if (transitionId2RelationId == null) {
            initTransition2RelationId(null);
        }
        addToMappedSet(stateId, conceptId, state2ConceptId);
        addToMappedSet(transitionId, relationId, transitionId2RelationId);
    }

    private String printArray(int[] conceptIdsInPositionOrder2) {
        StringBuffer sb = new StringBuffer();
        for (int i : conceptIdsInPositionOrder2) {
            sb.append(i + "'");
        }
        return sb.toString();
    }

    private void addToMappedSet(int key, int value, Int2ObjectOpenHashMap<int[]> map) {
        int[] list = map.get(key);
        if (list == null) {
            list = new int[1];
            map.put(key, list);
        } else {
            int[] newList = new int[list.length + 1];
            System.arraycopy(list, 0, newList, 0, list.length);
            map.put(key, newList);
            list = newList;
        }
        list[list.length - 1] = value;
    }

    @SuppressWarnings("unchecked")
    public Object clone() {
        Route r = new Route();
        r.conceptIdsInPositionOrder = new int[conceptIdsInPositionOrder.length];
        System.arraycopy(conceptIdsInPositionOrder, 0, r.conceptIdsInPositionOrder, 0, conceptIdsInPositionOrder.length);
        if (relationIdsInPositionOrder != null) {
            r.relationIdsInPositionOrder = new int[relationIdsInPositionOrder.length];
            System.arraycopy(relationIdsInPositionOrder, 0, r.relationIdsInPositionOrder, 0, relationIdsInPositionOrder.length);
        }
        r.initState2ConceptId(state2ConceptId);
        r.initTransition2RelationId(transitionId2RelationId);
        return r;
    }

    public boolean containsConcept(int cId) {
        for (int value : conceptIdsInPositionOrder) {
            if (value == cId) {
                return true;
            }
        }
        return false;
    }

    public boolean containsRelation(int rId) {
        if (relationIdsInPositionOrder == null) return false;
        for (int value : relationIdsInPositionOrder) {
            if (value == rId) {
                return true;
            }
        }
        return false;
    }

    /**
	 * 
	 * @param stateId acts as evidence for concepts
	 * @return a list where concept ids appear in the order they are in the route
	 * @throws StateDoesNotExistException 
	 */
    public int[] getConceptIdsForStateId(int stateId) throws StateDoesNotExistException {
        int[] concepts = state2ConceptId.get(stateId);
        if (concepts == null) {
            throw new StateDoesNotExistException("State id does not exist :" + stateId);
        }
        IntSort.sortArrayByFirstInstanceInArray(concepts, 0, concepts.length, relationIdsInPositionOrder);
        return concepts;
    }

    public boolean containsConceptsForStateId(int stateId) {
        return state2ConceptId.containsKey(stateId);
    }

    /**
	 * 
	 * @param transitionId acts as evidence for relations
	 * @return a list where relation ids appear in the order they appear in the route
	 * @throws TransitionDoesNotExistException 
	 */
    public int[] getRelationIdsForTransitionId(int transitionId) throws TransitionDoesNotExistException {
        int[] relations = transitionId2RelationId.get(transitionId);
        if (relations == null) {
            throw new TransitionDoesNotExistException("Transition id does not exist :" + transitionId);
        }
        IntSort.sortArrayByFirstInstanceInArray(relations, 0, relations.length, conceptIdsInPositionOrder);
        return relations;
    }

    public boolean containsRelationsForTransitionId(int transitionId) {
        return transitionId2RelationId.containsKey(transitionId);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < conceptIdsInPositionOrder.length; i++) {
            sb.append(conceptIdsInPositionOrder[i]);
            if (i < conceptIdsInPositionOrder.length - 1) sb.append("(" + relationIdsInPositionOrder[i] + ")");
        }
        return sb.toString();
    }
}
