package gnu.rhuelga.cirl.htmllib;

import javax.swing.table.*;
import java.util.*;

public class NodeTableModel extends AbstractTableModel {

    Node node;

    private String[] headings = new String[] { "Attributes", "Value" };

    public NodeTableModel(Node n) {
        node = n;
    }

    public int getRowCount() {
        return node.tag.attributes.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int row, int column) {
        Object obj = null;
        Set set = node.tag.attributes.keySet();
        Iterator iter = set.iterator();
        if (row < 0) return null;
        while (row-- >= 0) obj = iter.next();
        if (column == 0) return obj.toString(); else return (node.tag.attributes.get(obj)).toString();
    }

    public String getColumnName(int i) {
        return headings[i];
    }
}
