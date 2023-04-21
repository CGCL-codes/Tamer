package dr.evolution.io;

import dr.evolution.tree.Tree;

/**
 * @author Andrew Rambaut
 * @author Alexei Drummond
 * @version $Id: TreeExporter.java,v 1.1 2006/01/05 17:55:47 rambaut Exp $
 */
public interface TreeExporter {

    void exportTree(Tree tree);

    void exportTrees(Tree[] trees);
}
