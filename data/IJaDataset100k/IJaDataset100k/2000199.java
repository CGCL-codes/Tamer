package medi.swing.expander;

import javatools.db.DbException;
import javatools.db.DbIterator;
import javatools.swing.tree.IndexedTypedTreeNode;
import javatools.util.NumberUtils;
import medi.swing.tree.MediTreeNode;
import medi.swing.util.TreeUtils;

/** Expansion manager for program node.
 * @author Antonio Petrelli
 * @version 0.2.0
 */
public class ProgramNodeExpander extends TreeNodeExpander {

    /** Creates a new instance of ProgramNodeExpander */
    public ProgramNodeExpander() {
    }

    /** Expands a program node.
     * @param node The node to expand.
     */
    public void expand(IndexedTypedTreeNode node) {
        Object[] tempIDs;
        DbIterator rowIt;
        try {
            tempIDs = node.getID();
            rowIt = prv.getFileTypesByProgram(NumberUtils.number2long(tempIDs[0])).iterator();
            TreeUtils.fillTree((MediTreeNode) node, rowIt, fileTypeIndexes, fileTypeIDIndexes, "FileType", ": ", false);
        } catch (DbException e) {
            System.out.println(e.getMessage());
        }
    }
}
