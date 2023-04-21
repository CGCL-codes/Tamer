package backend.tools.algorithms;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import backend.core.AbstractConcept;
import backend.core.AbstractONDEXGraph;
import backend.core.AbstractONDEXIterator;
import backend.core.AbstractRelation;
import backend.core.ONDEXView;
import backend.core.SparseBitSet;
import backend.core.security.Session;

/**
 * 
 * @author hindlem
 *
 */
public class ExtractSubGraphs {

    private Int2ObjectOpenHashMap<IntArrayList> componentToConceptId;

    private Int2IntOpenHashMap conceptIdToComponant;

    private Int2ObjectOpenHashMap<IntArrayList> componentToRelationId;

    private Int2IntOpenHashMap relationIdToComponant;

    private Session s;

    private AbstractONDEXGraph og;

    /**
	 * 
	 * @param og 
	 * @param concepts
	 * @param relations
	 */
    public ExtractSubGraphs(Session s, AbstractONDEXGraph og, AbstractONDEXIterator<AbstractConcept> concepts, AbstractONDEXIterator<AbstractRelation> relations) {
        this.s = s;
        this.og = og;
        extract(concepts, relations);
    }

    /**
	 * Conveniance constructor applies SubGraph to entire graph
	 * @param s
	 * @param og
	 */
    public ExtractSubGraphs(Session s, AbstractONDEXGraph og) {
        this(s, og, og.getConcepts(s), og.getRelations(s));
    }

    private void extract(AbstractONDEXIterator<AbstractConcept> concepts, AbstractONDEXIterator<AbstractRelation> relations) {
        IntOpenHashSet connectedNodes = new IntOpenHashSet((int) concepts.size());
        concepts.close();
        int m = (int) relations.size();
        int[] edges = new int[m];
        int[] nodeFrom = new int[m];
        int[] nodeTo = new int[m];
        int relNum = 0;
        while (relations.hasNext()) {
            AbstractRelation relation = relations.next();
            edges[relNum] = relation.getId(s).intValue();
            int from = relation.getKey(s).getFromID().intValue();
            int to = relation.getKey(s).getToID().intValue();
            nodeFrom[relNum] = from;
            nodeTo[relNum] = to;
            connectedNodes.add(from);
            connectedNodes.add(to);
            relNum++;
        }
        relations.close();
        int n = connectedNodes.size();
        int[] components = new int[n + 1];
        connectedNodes = null;
        GraphAlgo.connectedComponents(n, m - 1, nodeFrom, nodeTo, components);
        System.out.println("Total number of components = " + components[0]);
        componentToConceptId = new Int2ObjectOpenHashMap<IntArrayList>();
        componentToRelationId = new Int2ObjectOpenHashMap<IntArrayList>();
        conceptIdToComponant = new Int2IntOpenHashMap();
        relationIdToComponant = new Int2IntOpenHashMap();
        for (int i = 0; i < components.length; i++) {
            int componant = components[i];
            IntArrayList componantIndex = componentToConceptId.get(componant);
            if (componantIndex == null) {
                componantIndex = new IntArrayList(2);
                componentToConceptId.put(componant, componantIndex);
            }
            if (!componantIndex.contains(nodeFrom[i])) {
                componantIndex.add(nodeFrom[i]);
            }
            if (!componantIndex.contains(nodeTo[i])) {
                componantIndex.add(nodeTo[i]);
            }
            conceptIdToComponant.put(nodeFrom[i], componant);
            conceptIdToComponant.put(nodeTo[i], componant);
            componantIndex = componentToRelationId.get(componant);
            if (componantIndex == null) {
                componantIndex = new IntArrayList(2);
                componentToRelationId.put(componant, componantIndex);
            }
            if (!componantIndex.contains(edges[i])) {
                componantIndex.add(edges[i]);
            }
            relationIdToComponant.put(edges[i], componant);
        }
    }

    /**
	 * Returns all concept members of this subgraph
	 * @param cid conceptId
	 * @return all cids for Concept members of this subgraph
	 */
    public IntArrayList getSubGraphConceptList(int cid) {
        if (conceptIdToComponant.containsValue(cid)) {
            return componentToConceptId.get(conceptIdToComponant.get(cid));
        }
        IntArrayList defaultReturn = new IntArrayList(1);
        defaultReturn.add(cid);
        return defaultReturn;
    }

    /**
	 * Returns all concept members of this subgraph
	 * @param cid conceptId
	 * @return all Concept members of this subgraph
	 */
    public ONDEXView<AbstractConcept> getSubGraphConceptView(int cid) {
        SparseBitSet sbs = new SparseBitSet();
        if (conceptIdToComponant.containsValue(cid)) {
            IntIterator cidIt = componentToConceptId.get(conceptIdToComponant.get(cid)).iterator();
            while (cidIt.hasNext()) {
                sbs.set(cidIt.next());
            }
            return new ONDEXView<AbstractConcept>(og, s, AbstractConcept.class, sbs);
        }
        sbs.set(cid);
        return new ONDEXView<AbstractConcept>(og, s, AbstractConcept.class, sbs);
    }

    /**
	 * Returns all relational members of this subgraph
	 * @param rid relationId
	 * @return all relationIds for relational members of this subgraph
	 */
    public IntArrayList getSubGraphRelationList(int rid) {
        if (relationIdToComponant.containsValue(rid)) {
            return componentToConceptId.get(relationIdToComponant.get(rid));
        }
        IntArrayList defaultReturn = new IntArrayList(1);
        defaultReturn.add(rid);
        return defaultReturn;
    }

    /**
	 * Returns all relational members of this subgraph
	 * @param rid relationId
	 * @return all Realation members of this subgraph
	 */
    public ONDEXView<AbstractRelation> getSubGraphRelationView(int rid) {
        SparseBitSet sbs = new SparseBitSet();
        if (relationIdToComponant.containsValue(rid)) {
            IntIterator cidIt = componentToRelationId.get(relationIdToComponant.get(rid)).iterator();
            while (cidIt.hasNext()) {
                sbs.set(cidIt.next());
            }
            return new ONDEXView<AbstractRelation>(og, s, AbstractRelation.class, sbs);
        }
        sbs.set(rid);
        return new ONDEXView<AbstractRelation>(og, s, AbstractRelation.class, sbs);
    }

    public static void main(String args[]) {
        int n = 9, m = 10;
        int component[] = new int[n + 1];
        int nodei[] = { 0, 4, 9, 1, 5, 4, 3, 9, 7, 6, 1 };
        int nodej[] = { 0, 3, 5, 7, 2, 1, 7, 2, 4, 8, 3 };
        GraphAlgo.connectedComponents(n, m, nodei, nodej, component);
        System.out.println("Total number of components = " + component[0]);
        System.out.print("\n             Node: ");
        for (int i = 1; i <= n; i++) System.out.print("  " + i);
        System.out.print("\n Component Number: ");
        for (int i = 1; i <= n; i++) System.out.print("  " + component[i]);
        System.out.println();
    }
}
