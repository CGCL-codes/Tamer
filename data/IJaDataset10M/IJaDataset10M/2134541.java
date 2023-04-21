package org.dllearner.algorithms.el;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Represents a simulation relation between EL description trees.
 * 
 * @author Jens Lehmann
 *
 */
public class Simulation {

    private List<TreeTuple> relation;

    private Map<ELDescriptionNode, List<ELDescriptionNode>> in;

    private Map<ELDescriptionNode, List<ELDescriptionNode>> out;

    public Simulation() {
        relation = new LinkedList<TreeTuple>();
        in = new HashMap<ELDescriptionNode, List<ELDescriptionNode>>();
        out = new HashMap<ELDescriptionNode, List<ELDescriptionNode>>();
    }

    /**
	 * Adds a tuple to the simulation.
	 * 
	 * @param tuple The new tuple.
	 * @see java.util.List#add(java.lang.Object)
	 */
    public void addTuple(TreeTuple tuple) {
        relation.add(tuple);
        if (in.containsKey(tuple.getTree2())) {
            in.get(tuple.getTree2()).add(tuple.getTree1());
        } else {
            List<ELDescriptionNode> list = new LinkedList<ELDescriptionNode>();
            list.add(tuple.getTree1());
            in.put(tuple.getTree2(), list);
        }
        if (out.containsKey(tuple.getTree1())) {
            out.get(tuple.getTree1()).add(tuple.getTree2());
        } else {
            List<ELDescriptionNode> list = new LinkedList<ELDescriptionNode>();
            list.add(tuple.getTree2());
            out.put(tuple.getTree1(), list);
        }
    }

    /**
	 * Removes a tuple from the simulation.
	 * 
	 * @param tuple The new tuple.
	 * @see java.util.List#add(java.lang.Object)
	 */
    public void removeTuple(TreeTuple tuple) {
        relation.remove(tuple);
        in.get(tuple.getTree2()).remove(tuple.getTree1());
        if (in.get(tuple.getTree2()).isEmpty()) in.remove(tuple.getTree2());
        out.get(tuple.getTree1()).remove(tuple.getTree2());
        if (out.get(tuple.getTree1()).isEmpty()) out.remove(tuple.getTree1());
    }

    /**
	 * Gets the complete simulation relation.
	 * @return the relation
	 */
    public List<TreeTuple> getRelation() {
        return relation;
    }

    public List<ELDescriptionNode> in(ELDescriptionNode tree) {
        return in.get(tree);
    }

    public List<ELDescriptionNode> out(ELDescriptionNode tree) {
        return out.get(tree);
    }
}
