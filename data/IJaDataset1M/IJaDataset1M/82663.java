package druid.data;

import java.util.Enumeration;
import java.util.Vector;
import org.dlib.gui.treeview.TreeViewNode;
import org.dlib.xml.XmlElement;
import druid.core.AttribSet;

public class AbstractNode extends TreeViewNode {

    public AttribSet attrSet = new AttribSet();

    public XmlElement xmlDoc = new XmlElement();

    public ModulesConfig modsConfig = new ModulesConfig();

    private DatabaseNode dbNode;

    private TableNode parentTable;

    public AbstractNode(DatabaseNode node) {
        this();
        dbNode = node;
    }

    public AbstractNode() {
        this("-UnNamed-");
    }

    public AbstractNode(String name) {
        attrSet.addAttrib("name", name);
        setText(name);
    }

    public String getName() {
        return attrSet.getString("name");
    }

    public void setName(String name) {
        attrSet.setString("name", name);
        setText(name);
    }

    public void copyTo(TreeViewNode node) {
        AbstractNode n = (AbstractNode) node;
        n.attrSet = attrSet.duplicate();
        n.xmlDoc = xmlDoc.duplicate();
        n.modsConfig = modsConfig.duplicate();
        n.dbNode = dbNode;
        n.parentTable = parentTable;
        super.copyTo(node);
    }

    public void textChanged() {
        String s = getText();
        if (s.equals("")) {
            s = "-UnNamed-";
            setText(s);
            refresh();
        }
        attrSet.setString("name", s);
    }

    /** The method scans all children and returns all objects of a given class
	  * (that is a vector of objects)
	  */
    public Vector getObjects(Class c) {
        Vector v = new Vector();
        for (Enumeration e = preorderEnumeration(); e.hasMoreElements(); ) {
            AbstractNode node = (AbstractNode) e.nextElement();
            if (node.getClass() == c) v.addElement(node);
        }
        return v;
    }

    public void setDatabase(DatabaseNode node) {
        dbNode = node;
    }

    public DatabaseNode getDatabase() {
        AbstractNode node = this;
        while (node.dbNode == null) {
            if (node.isRoot()) return getParentTable().getDatabase();
            node = (AbstractNode) node.getParent();
        }
        return node.dbNode;
    }

    /** Used by triggers and rules to obtain the table they refer to */
    public void setParentTable(TableNode node) {
        parentTable = node;
    }

    /** Used by triggers and rules to obtain the table they refer to */
    public TableNode getParentTable() {
        return ((AbstractNode) getRoot()).parentTable;
    }
}
