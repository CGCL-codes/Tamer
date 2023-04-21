package org.jgap.data;

/**
 * The IDataCreators interface represents an entity comparable to
 * org.w3c.dom.Document
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class DataElementsDocument implements IDataCreators {

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "$Revision: 1.6 $";

    private IDataElementList m_tree;

    public void setTree(final IDataElementList a_tree) {
        m_tree = a_tree;
    }

    /**
   * @return the tree (of elements) held by this class
   *
   * @author Klaus Meffert
   * @since 2.0
   */
    public IDataElementList getTree() {
        return m_tree;
    }

    public DataElementsDocument() throws Exception {
        m_tree = new DataElementList();
    }

    public IDataCreators newDocument() throws Exception {
        return new DataElementsDocument();
    }

    /**
   * Appends a child element to the tree
   * @param a_newChild the child to be added to the tree
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
    public void appendChild(final IDataElement a_newChild) throws Exception {
        m_tree.add(a_newChild);
    }
}
