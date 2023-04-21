package owl2prefuse.tree;

import owl2prefuse.Constants;

/**
 * Wrap the prefuse Tree into an OWL2Prefuse tree in order to register the 
 * type of tree as a instance variable of the tree. This is needed to create 
 * the proper visualization.
 * <p/>
 * Project OWL2Prefuse <br/>
 * Tree.java created 14 june 2007, 14:57
 * <p/>
 * Copyright &copy 2007 Jethro Borsje
 * @author <a href="mailto:info@jborsje.nl">Jethro Borsje</a>
 * @version $$Revision:$$, $$Date:$$
 */
public class Tree extends prefuse.data.Tree {

    /**
     * The type of the graph (being: RDF or OWL).
     * @param p_type A constant indicating the type of the Tree.
     */
    int m_type;

    /** Creates a new instance of Tree. */
    public Tree(int p_type) {
        m_type = p_type;
    }

    /**
     * Creates a new instance of the OWL2Prefuse Tree based on the given Prefuse 
     * Tree. Please note we assume an OWL tree in this case.
     * @param p_graph The Prefuse tree that is to be wrappend in an OWL2Prefuse 
     * tree.
     */
    public Tree(prefuse.data.Tree p_tree) {
        super(p_tree.getNodeTable(), p_tree.getEdgeTable());
        m_type = Constants.TREE_TYPE_OWL;
    }

    /**
     * Get the type of the tree.
     * @return The type of the tree as a Constant.
     */
    public int getType() {
        return m_type;
    }
}
