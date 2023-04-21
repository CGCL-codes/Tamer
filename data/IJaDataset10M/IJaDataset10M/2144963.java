package medi.swing.expander;

import javatools.db.DbException;
import javatools.db.DbIterator;
import javatools.swing.tree.IndexedTypedTreeNode;
import medi.swing.tree.MediTreeNode;
import medi.swing.util.TreeUtils;

/** Expansion manager for DataFileTypeContainer node.
 * @author Antonio Petrelli
 * @version 0.2.0
 */
public class DataFTCNodeExpander extends TreeNodeExpander {

    /** Creates a new instance of DataFTCNodeExpander */
    public DataFTCNodeExpander() {
    }

    /** Expands a DataFileTypeContainer node.
     * @param node The node to expand.
     */
    public void expand(IndexedTypedTreeNode node) {
        Object[] tempIDs;
        DbIterator rowIt;
        try {
            tempIDs = node.getID();
            rowIt = prv.getDataByFileTypeReduced((Integer) tempIDs[0]).iterator();
            TreeUtils.fillTree((MediTreeNode) node, rowIt, dataIndexes, dataIDIndexes, "Data", ": ", true);
        } catch (DbException e) {
            System.out.println(e.getMessage());
        }
    }
}
