package com.openbravo.pos.panels;

import java.util.Enumeration;
import javax.swing.tree.TreeNode;
import java.util.ArrayList;

/**
 *
 * @author adrianromero
 */
public class SQLTable implements TreeNode {

    private SQLDatabase m_db;

    private String m_sName;

    private ArrayList m_aColumns;

    /** Creates a new instance of SQLTable */
    public SQLTable(SQLDatabase db, String name) {
        m_db = db;
        m_sName = name;
        m_aColumns = new ArrayList();
    }

    public String getName() {
        return m_sName;
    }

    public void addColumn(String name) {
        SQLColumn c = new SQLColumn(this, name);
        m_aColumns.add(c);
    }

    public String toString() {
        return m_sName;
    }

    public Enumeration children() {
        return new EnumerationIter(m_aColumns.iterator());
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public TreeNode getChildAt(int childIndex) {
        return (TreeNode) m_aColumns.get(childIndex);
    }

    public int getChildCount() {
        return m_aColumns.size();
    }

    public int getIndex(TreeNode node) {
        return m_aColumns.indexOf(node);
    }

    public TreeNode getParent() {
        return m_db;
    }

    public boolean isLeaf() {
        return m_aColumns.size() == 0;
    }
}
