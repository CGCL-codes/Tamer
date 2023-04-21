package dr.evolution.tree;

/**
 * Interface for a phylogenetic or genealogical tree.
 *
 * @version $Id: NodeRef.java,v 1.5 2005/05/24 20:25:56 rambaut Exp $
 *
 * @author Andrew Rambaut
 * @author Alexei Drummond
 */
public interface NodeRef {

    int getNumber();

    void setNumber(int n);
}
