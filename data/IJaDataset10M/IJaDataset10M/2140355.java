package com.memoire.bu;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import com.memoire.fu.FuPreferences;

/**
 * A table model for the favorite user tools.
 * tool.#.exec
 */
public class BuToolsModel implements TableModel, PropertyChangeListener {

    private Vector listeners_;

    private FuPreferences options_;

    private Vector names_;

    private Vector execs_;

    private Vector icons_;

    private boolean valid_;

    public BuToolsModel(FuPreferences _options) {
        listeners_ = new Vector();
        options_ = _options;
        names_ = new Vector();
        execs_ = new Vector();
        icons_ = new Vector();
        valid_ = false;
        if (options_ != null) options_.addPropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent _evt) {
        valid_ = false;
    }

    protected void build() {
        if (valid_) return;
        synchronized (options_) {
            valid_ = true;
            names_.removeAllElements();
            execs_.removeAllElements();
            icons_.removeAllElements();
            int i = 1;
            while (true) {
                String name = options_.getStringProperty("tool." + i + ".name");
                String exec = options_.getStringProperty("tool." + i + ".exec");
                String icon = options_.getStringProperty("tool." + i + ".icon");
                if ("".equals(exec)) break;
                if ("-".equals(exec)) {
                    names_.addElement(name);
                    execs_.addElement(exec);
                    icons_.addElement(BuResource.BU.getMenuIcon(""));
                } else {
                    if ("".equals(name)) name = exec;
                    if ("".equals(icon)) name = "executer";
                    names_.addElement(name);
                    execs_.addElement(exec);
                    icons_.addElement(BuResource.BU.getMenuIcon(icon));
                }
                i++;
            }
        }
        fireTableChanged();
    }

    public Class getColumnClass(int column) {
        if (!valid_) build();
        Class r = null;
        switch(column) {
            case 0:
                r = BuIcon.class;
                break;
            case 1:
                r = String.class;
                break;
            case 2:
                r = String.class;
                break;
            default:
                r = Object.class;
                break;
        }
        return r;
    }

    public int getColumnCount() {
        if (!valid_) build();
        return 3;
    }

    public String getColumnName(int column) {
        if (!valid_) build();
        String r = "";
        switch(column) {
            case 0:
                r = "I";
                break;
            case 1:
                r = BuResource.BU.getString("Outil");
                break;
            case 2:
                r = BuResource.BU.getString("Commande");
                break;
            default:
                r = "undefined";
                break;
        }
        return r;
    }

    public int getRowCount() {
        if (!valid_) build();
        return execs_.size() + 1;
    }

    public Object getValueAt(int row, int column) {
        if (!valid_) build();
        Object r = null;
        if (row == execs_.size()) {
            switch(column) {
                case 0:
                    r = BuResource.BU.getIcon("");
                    break;
                case 1:
                    r = "";
                    break;
                case 2:
                    r = "";
                    break;
            }
        } else {
            switch(column) {
                case 0:
                    r = icons_.elementAt(row);
                    break;
                case 1:
                    r = names_.elementAt(row);
                    break;
                case 2:
                    r = execs_.elementAt(row);
                    break;
            }
        }
        return r;
    }

    public boolean isCellEditable(int row, int column) {
        if (!valid_) build();
        return (column > 0) && ((row < execs_.size()) || (column == 2));
    }

    public void setValueAt(Object _value, int row, int column) {
        if (!valid_) build();
        Object value = _value;
        if (!(value instanceof String)) value = "" + value;
        if (column == 1) {
            names_.setElementAt(value, row);
            options_.putStringProperty("tool." + (row + 1) + ".name", (String) value);
            fireRowChanged(row);
        } else if (column == 2) {
            if ("".equals(value)) value = "-";
            options_.putStringProperty("tool." + (row + 1) + ".exec", (String) value);
            if (row == execs_.size()) {
                options_.putStringProperty("tool." + (row + 1) + ".icon", "executer");
                build();
            } else {
                execs_.setElementAt(value, row);
                fireRowChanged(row);
            }
        }
    }

    public void fireRowChanged(int row) {
        TableModelEvent e = new TableModelEvent(this, row);
        Enumeration l = listeners_.elements();
        while (l.hasMoreElements()) ((TableModelListener) l.nextElement()).tableChanged(e);
    }

    public void fireTableChanged() {
        TableModelEvent e = new TableModelEvent(this);
        Enumeration l = listeners_.elements();
        while (l.hasMoreElements()) ((TableModelListener) l.nextElement()).tableChanged(e);
    }

    public void addTableModelListener(TableModelListener _l) {
        listeners_.addElement(_l);
    }

    public void removeTableModelListener(TableModelListener _l) {
        listeners_.removeElement(_l);
    }
}
