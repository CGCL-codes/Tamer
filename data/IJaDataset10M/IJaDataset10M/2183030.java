package com.sdi.pws.gui.compo.db.table;

import com.sdi.pws.gui.RecordSelector;
import com.sdi.pws.gui.compo.db.change.ChangeViewField;
import com.sdi.pws.db.PwsRecord;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

/** Keeps track of the current selected password entry in the table view.
 *
 */
public class TableViewSelector implements RecordSelector, ListSelectionListener {

    private PwsRecord selectedRecord;

    private TableModel db;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public TableViewSelector(TableModel aDb, JTable aTable) {
        if (aDb == null) throw new IllegalArgumentException("Db should not be null.");
        if (aTable == null) throw new IllegalArgumentException("JTable should not be null.");
        db = aDb;
        selectedRecord = null;
        aTable.getSelectionModel().addListSelectionListener(this);
    }

    public boolean isInfoAvailable() {
        return selectedRecord != null;
    }

    public PwsRecord getSelectedRecord() {
        return selectedRecord;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        final PwsRecord lOldValue = selectedRecord;
        final ListSelectionModel lSelectionModel = (ListSelectionModel) e.getSource();
        if (lSelectionModel.isSelectionEmpty()) {
            selectedRecord = null;
        } else {
            int index = lSelectionModel.getMinSelectionIndex();
            Object lField = db.getValueAt(index, 0);
            if (lField instanceof ChangeViewField) {
                selectedRecord = ((ChangeViewField) lField).getRecord();
            }
        }
        final PwsRecord lNewValue = selectedRecord;
        support.firePropertyChange("selectedRecord", lOldValue, lNewValue);
    }

    public void addPropertyChangeListener(PropertyChangeListener aListener) {
        support.addPropertyChangeListener(aListener);
    }

    public void removePropertyChangeListener(PropertyChangeListener aListener) {
        support.removePropertyChangeListener(aListener);
    }
}
