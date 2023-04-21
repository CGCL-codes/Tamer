package edu.isi.div2.metadesk.gui;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
import edu.isi.div2.metadesk.util.MetaDeskConstants;
import edu.isi.div2.metadesk.datamodel.*;
import edu.isi.div2.metadesk.event.InstanceChangeListener;

/**
 * ElementObjectTableModel class extends the <i>AbstractTableModel </i> and
 * provides the model for the nodes in the system.
 * 
 * @author Sameer Maggon (maggon@isi.edu)
 * @version $Id: ElementObjectTableModel.java,v 1.1 2005/05/24 16:34:18 maggon Exp $
 */
public class ElementObjectTableModel extends AbstractTableModel implements MetaDeskConstants, InstanceChangeListener {

    static Logger logger = Logger.getLogger(ElementObjectTableModel.class.getName());

    private transient List representingTerms = null;

    /**
     *  
     */
    public ElementObjectTableModel() {
        super();
    }

    public void setInstances(List instances) {
        this.representingTerms = instances;
        this.fireTableDataChanged();
    }

    public List getInstances() {
        return representingTerms;
    }

    public int getColumnCount() {
        return 1;
    }

    public int getRowCount() {
        if (representingTerms == null) return 0;
        InstanceWorld iw = InstanceWorld.getCurrentInstanceWorld();
        return ((Term) representingTerms.get(0)).getChildren(iw, false).size() + 1;
    }

    public String getColumnName(int column) {
        return "";
    }

    public Class getColumnClass(int c) {
        if (c == 0) return ("").getClass();
        return getValueAt(0, c).getClass();
    }

    public Object getValueAt(int rowN, int colN) {
        InstanceWorld iw = InstanceWorld.getCurrentInstanceWorld();
        java.util.List children = ((Term) representingTerms.get(0)).getChildren(iw, true);
        if (rowN < children.size()) {
            Term child = (Term) children.get(rowN);
            return iw.getFriendlyLabel(child, true);
        }
        if (rowN == children.size()) return "";
        return "error";
    }

    public void instanceChanged(List terms) {
        this.setInstances(terms);
    }

    public boolean isCellEditable(int rowN, int colN) {
        return true;
    }

    /**
     * Called when child node is added using the lower middle pane. 'rowN'
     * starts counting at zero. 
     * colN will always be zero as the table model returns 1 column
     * rowN starts with zero
     * 
     */
    public void setValueAt(Object aValue, int rowN, int colN) {
        InstanceWorld iw = InstanceWorld.getCurrentInstanceWorld();
        java.util.List children = ((Term) representingTerms.get(0)).getChildren(iw, true);
        if (rowN < children.size()) {
            Term term = (Term) children.get(rowN);
            term.renameTerm((String) aValue, iw);
        } else {
            rowN -= children.size();
            if (rowN == 0) {
                String value = ((String) aValue).trim();
                if ((value != null) && !value.equals("")) {
                    Term childInstance = iw.findOrCreateTermForLabel(value);
                    if (null == childInstance) return;
                    String sourceContext = null;
                    logger.warn("CAN'T FIGURE OUT SOURCE CONTEXT");
                    ((Term) representingTerms.get(0)).addChild(childInstance, iw, sourceContext);
                }
            }
        }
        fireTableDataChanged();
    }

    public static Object friendlyRendering(Object o) {
        if (o instanceof String) {
            String s = (String) o;
            if (s.length() > 70 && !s.toLowerCase().startsWith("<html")) {
                return "<html><table><td width=400>" + s + "</td></table></html>";
            }
            return s;
        } else {
            return o;
        }
    }
}
