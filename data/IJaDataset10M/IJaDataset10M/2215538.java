package tm.displayEngine.tmHigraph;

import tm.interfaces.Datum;

/**
 * An interface for building Higraph models within the TeachingMachine
 * 
 * @author mpbl
 *
 */
public interface HigraphModellingInterface {

    /** Makes a top level node in the wholeGraph associated with d.
	 * Has no effect if a node for d already exists
	 * otherwise node for d  is created.
	 * 
	 * @param d the Datum associated with the node
	 */
    public void makeNode(Datum d);

    /** Deletes a node in the wholeGraph associated with d.
	 * 
	 * @param d the Datum associated with the node
	 */
    public void deleteNode(Datum d);

    /** A node is added as a child to another node of the wholeGraph. If either
	 *	node does not exist it is created. If the child is already linked to a
	 *  different parent, it will be moved.
	 * 
	 * @param p the datum associated with the parent node 
	 * @param c the datum associated with the child node
	 */
    public void addChild(Datum p, Datum c);

    /**
	 * an edge is created between ANY two nodes in the higraph. If either
	 *	node does not exist it is created. 
	 * 
	 * @param source the datum associated with the node where the edge begins 
	 * @param target the datum associated with the node where the edge ends
	 */
    public void makeEdge(Datum source, Datum target);

    /** <p><b>A scripting function for manipulating higraph model</b></p>
	 * <p>make a new, empty subGraph of the wholeGraph (which, as a convenience
	 * the tm automatically builds using the reserved id "wholeGraph").</p>
	 * 
	 * @param id a unique id for the subGraph, may not be "wholeGraph"
	 */
    public void makeSubGraph(String id);

    /**
	 * add the node from the wholeGraph associated with t as a top node
	 * of the subGraph id. All descendants are added by definition, as are
	 * edges which both start and end on the subGraph
	 * 
	 * @param id - the name of an existing subGraph. Throws an Assert error
     *                  if no such graph exists
	 * @param t - the datum associated with the top node to be added
	 *            throws an Assert error if no such node exists
	 */
    public void addTopNode(String id, Datum t);

    public void makeNode(Datum d, boolean deref);

    public void deleteNode(Datum d, boolean deref);

    public void addChild(Datum p, boolean derefP, Datum c);

    public void addChild(Datum p, Datum c, boolean derefC);

    public void addChild(Datum p, boolean derefP, Datum c, boolean derefC);

    public void makeEdge(Datum source, boolean derefS, Datum target);

    public void makeEdge(Datum source, Datum target, boolean derefT);

    public void makeEdge(Datum source, boolean derefS, Datum target, boolean derefT);

    public void addTopNode(String hgid, Datum t, boolean deref);
}
