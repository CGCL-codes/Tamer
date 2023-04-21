package hermes.swing;

import java.util.HashSet;
import java.util.Set;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author colincrist@hermesjms.com
 * @version $Id: ProxyListSelectionModel.java,v 1.1 2006/05/26 10:08:21 colincrist Exp $
 */
public class ProxyListSelectionModel implements ListSelectionModel {

    private Set<ListSelectionModel> models = new HashSet<ListSelectionModel>();

    private ListSelectionModel selected;

    private Set<ListSelectionListener> listeners = new HashSet<ListSelectionListener>();

    public void remove(ListSelectionModel model) {
        models.remove(model);
    }

    public void forward(ListSelectionEvent e) {
        for (ListSelectionListener l : listeners) {
            l.valueChanged(e);
        }
    }

    public void add(final ListSelectionModel model) {
        models.add(model);
        if (models.size() == 1) {
            selected = model;
        }
        for (ListSelectionListener l : listeners) {
            model.addListSelectionListener(l);
        }
        model.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                selected = model;
            }
        });
    }

    public void addListSelectionListener(ListSelectionListener x) {
        listeners.add(x);
        for (ListSelectionModel m : models) {
            m.addListSelectionListener(x);
        }
    }

    public void removeListSelectionListener(ListSelectionListener x) {
        listeners.remove(x);
        for (ListSelectionModel m : models) {
            m.removeListSelectionListener(x);
        }
    }

    public void addSelectionInterval(int index0, int index1) {
        selected.addSelectionInterval(index0, index1);
    }

    public void clearSelection() {
        selected.clearSelection();
    }

    public int getAnchorSelectionIndex() {
        return selected.getAnchorSelectionIndex();
    }

    public int getLeadSelectionIndex() {
        return selected.getLeadSelectionIndex();
    }

    public int getMaxSelectionIndex() {
        return selected.getMaxSelectionIndex();
    }

    public int getMinSelectionIndex() {
        return selected.getMinSelectionIndex();
    }

    public int getSelectionMode() {
        return selected.getSelectionMode();
    }

    public boolean getValueIsAdjusting() {
        return selected.getValueIsAdjusting();
    }

    public void insertIndexInterval(int index, int length, boolean before) {
        selected.insertIndexInterval(index, length, before);
    }

    public boolean isSelectedIndex(int index) {
        return selected.isSelectedIndex(index);
    }

    public boolean isSelectionEmpty() {
        return selected.isSelectionEmpty();
    }

    public void removeIndexInterval(int index0, int index1) {
        selected.removeIndexInterval(index0, index1);
    }

    public void removeSelectionInterval(int index0, int index1) {
        selected.removeSelectionInterval(index0, index1);
    }

    public void setAnchorSelectionIndex(int index) {
        selected.setAnchorSelectionIndex(index);
    }

    public void setLeadSelectionIndex(int index) {
        selected.setLeadSelectionIndex(index);
    }

    public void setSelectionInterval(int index0, int index1) {
        selected.setSelectionInterval(index0, index1);
    }

    public void setSelectionMode(int selectionMode) {
        selected.setSelectionMode(selectionMode);
    }

    public void setValueIsAdjusting(boolean valueIsAdjusting) {
        selected.setValueIsAdjusting(valueIsAdjusting);
    }
}
