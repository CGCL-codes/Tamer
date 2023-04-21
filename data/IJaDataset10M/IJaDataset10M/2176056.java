package ru.mcfr.oxygen.util.ui.treetable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: ws
 * Date: 04.05.11
 * Time: 18:15
 * To change this template use File | Settings | File Templates.
 */
public class SchemaInfoModel extends AbstractTreeTableModel implements TreeTableModel {

    protected static String[] cNames = { "Name", "Id", "URL" };

    protected static Class[] cTypes = { TreeTableModel.class, String.class, String.class };

    public SchemaInfoModel(Document doc) {
        super(doc.getDocumentElement());
    }

    public int getColumnCount() {
        return cNames.length;
    }

    public String getColumnName(int column) {
        return cNames[column];
    }

    public Object getValueAt(Object node, int column) {
        if (node instanceof Node) {
            Node n = (Node) node;
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                switch(column) {
                    case 0:
                        return e.getAttribute("title");
                    case 1:
                        return e.getAttribute("id");
                    case 2:
                        return e.getAttribute("noNamespaceSchemaLocation");
                }
            }
        }
        return null;
    }

    private Object[] getChildren(Object node) {
        Element e = (Element) node;
        NodeList chlds = e.getChildNodes();
        Vector<Element> res = new Vector<Element>();
        for (int i = 0; i < chlds.getLength(); i++) if (chlds.item(i).getNodeType() == Node.ELEMENT_NODE) res.add((Element) chlds.item(i));
        Object[] ores = new Object[res.size()];
        res.copyInto(ores);
        return ores;
    }

    public Object getChild(Object o, int i) {
        String title = ((Element) o).getAttribute("title");
        Object newObj = getChildren(o)[i];
        String other_title = ((Element) newObj).getAttribute("title");
        return newObj;
    }

    public int getChildCount(Object o) {
        String title = ((Element) o).getAttribute("title");
        int len = getChildren(o).length;
        return len;
    }
}
