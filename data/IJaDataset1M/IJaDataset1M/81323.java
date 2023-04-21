package com.qasystems.qstudio.java.gui.observation;

import com.qasystems.qstudio.java.QStudioGlobal;
import com.qasystems.qstudio.java.gui.QualityAttributes;
import com.qasystems.qstudio.java.gui.SelectorMenuEntry;
import com.qasystems.util.Utilities;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.event.TableModelEvent;

/**
 * This class implements the attribute selection panel.
 */
public class AttributeSelectorPanel extends SelectorPanel {

    private final Vector menuEntries = new Vector();

    /**
   * Creates a new AttributeSelectorPanel object.
   *
   * @param obsSelector DOCUMENT ME!
   */
    public AttributeSelectorPanel(ViewSelection obsSelector) {
        super();
        setSelector(obsSelector);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Called when the selector table data changed.
   *
   * @param event the event
   */
    public void onTableChanged(TableModelEvent event) {
        final int lastRow = event.getLastRow() + 1;
        for (int i = event.getFirstRow(); i < lastRow; i++) {
            final boolean selected = ((Boolean) getSelectorModel().getValueAt(i, 0)).booleanValue();
            getSelector().selectAttribute(i, selected);
            rebuildSelectViewMenu(i, selected);
        }
    }

    /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
    public Vector getAttributeEntries() {
        return (menuEntries);
    }

    private void jbInit() throws Exception {
        getSelectorModel().setData(createTableData());
    }

    protected Object[][] createTableData() {
        final QualityAttributes attributes = new QStudioGlobal().getQualityAttributes();
        final int count = attributes.getMainAttributeCount();
        final int[] mainAttributes = attributes.getMainAttributes();
        final Object[][] result = new Object[count][2];
        for (int i = 0; i < count; i++) {
            result[i][0] = new Boolean(getSelector().isSelectedAttribute(i));
            result[i][1] = new String(attributes.toLongString(mainAttributes[i]));
            addMenuEntry(result[i], i);
        }
        return (result);
    }

    protected void addMenuEntry(final Object[] items, int id) {
        final SelectorMenuEntry entry = new SelectorMenuEntry((String) items[1], true, id) {

            public void updateAction() {
                final boolean selected = isSelected();
                final int id = getID();
                getSelector().selectAttribute(id, selected);
                updateTable(id, selected);
            }
        };
        Utilities.discardBooleanResult(menuEntries.add(entry));
    }

    void rebuildSelectViewMenu() {
        final Object[][] data = getSelectorModel().getData();
        for (int i = 0; i < data.length; i++) {
            rebuildSelectViewMenu(i, ((Boolean) getSelectorModel().getValueAt(i, 0)).booleanValue());
        }
    }

    private void rebuildSelectViewMenu(int id, boolean selected) {
        final Enumeration e = menuEntries.elements();
        while (e.hasMoreElements()) {
            final SelectorMenuEntry sme = (SelectorMenuEntry) e.nextElement();
            if (getSelector().getAttributeSelectionMode() == ViewSelection.SELECTION_MODE_ALL) {
                sme.setSelected(true);
            } else if (sme.getID() == id) {
                sme.setSelected(selected);
            } else {
            }
        }
    }

    void updateTable() {
        final Object[][] data = getSelectorModel().getData();
        for (int i = 0; i < data.length; i++) {
            data[i][0] = Boolean.TRUE;
        }
        selectAll();
    }

    /**
   * Synchronize the table, user clicked a menu item
   *
   * @param id the matching id to identify the table row
   * @param selected
   */
    void updateTable(int id, boolean selected) {
        final Object[][] data = getSelectorModel().getData();
        for (int i = 0; i < data.length; i++) {
            if (i == id) {
                data[i][0] = selected ? Boolean.TRUE : Boolean.FALSE;
            }
        }
        getSelectorModel().setData(data);
        selectUserDefined();
    }

    protected void selectAll() {
        super.selectAll();
        getSelector().setAttributeSelectionMode(ViewSelection.SELECTION_MODE_ALL);
    }

    protected void selectUserDefined() {
        super.selectUserDefined();
        getSelector().setAttributeSelectionMode(ViewSelection.SELECTION_MODE_USER);
    }
}
