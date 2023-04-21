package com.explosion.utilities.preferences.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.explosion.utilities.RowHeaderUtil;
import com.explosion.utilities.exception.ExceptionManagerFactory;
import com.explosion.utilities.preferences.Preference;
import com.explosion.utilities.preferences.PreferenceChangeEvent;
import com.explosion.utilities.preferences.PreferenceChangeListener;

/**
 * @author Stephen Cowx Date created:@25-Jan-2003
 * This JPanel is specifically for editing preferences of type Collection.
 * In other words, if a preference is of typoe collection and the user selects
 * to edit the prefrence, this JPanel will be used to allow
 * the user to add and remove items from their collection.
 */
public class EditCollectionPreferenceJPanel extends JPanel implements PreferenceChangeListener {

    private static Logger log = LogManager.getLogger(EditCollectionPreferenceJPanel.class);

    private Component owner;

    private JPanel buttonPanel = new JPanel();

    private CollectionPreferenceTable table;

    private JScrollPane scrollPane = new JScrollPane();

    private JButton removeButton = new JButton("Delete");

    private JButton addButton = new JButton("Add");

    private Preference preference;

    private String initialSelection = null;

    private Vector trackedButtons = new Vector();

    private Vector buttons = new Vector();

    /**
     * Constructor
     */
    public EditCollectionPreferenceJPanel(Preference preference) throws Exception {
        this.preference = preference;
        this.preference.addPreferenceChangeListener(this);
        init();
    }

    /**
     * This method sets out the look and feel for this component
     */
    public void init() throws Exception {
        this.setSize(new Dimension(300, 200));
        this.setLayout(new GridBagLayout());
        addButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addButton_actionPerformed(e);
            }
        });
        removeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeButton_actionPerformed(e);
            }
        });
        trackedButtons.addElement(removeButton);
        buttons.addElement(addButton);
        buttons.addElement(removeButton);
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        for (int i = 0; i < buttons.size(); i++) buttonPanel.add(((JButton) buttons.elementAt(i)), new GridBagConstraints(0, i, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        table = new CollectionPreferenceTable(preference, this);
        scrollPane.getViewport().add(table);
        RowHeaderUtil util = new RowHeaderUtil();
        util.setRowHeader(table, true);
        refresh();
        table.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) list_mouseClicked(e);
            }
        });
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        this.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 10, 10));
        this.add(buttonPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 10, 10));
        checkEnabled();
    }

    void addButton_actionPerformed(ActionEvent e) {
        try {
            preference.addValue(null);
        } catch (Exception ex) {
            com.explosion.utilities.exception.ExceptionManagerFactory.getExceptionManager().manageException(ex, null);
        }
    }

    void list_mouseClicked(MouseEvent e) {
        checkEnabled();
    }

    void removeButton_actionPerformed(ActionEvent e) {
        try {
            int selectedRows[] = table.getSelectedRows();
            Arrays.sort(selectedRows);
            for (int i = 0; i < selectedRows.length; i++) {
                this.preference.removeValue(table.getValueAt(selectedRows[i], 0));
            }
        } catch (Exception ex) {
            com.explosion.utilities.exception.ExceptionManagerFactory.getExceptionManager().manageException(ex, "Exception caught while removing value.");
        }
    }

    public void preferenceChanged(PreferenceChangeEvent preferenceChangeEvent) {
        try {
            refresh();
        } catch (Exception e) {
            ExceptionManagerFactory.getExceptionManager().manageException(e, "Exception caught while updating preference.");
        }
    }

    public void refresh() {
        log.debug("refreshing values");
        Vector rows = new Vector();
        Vector columns = new Vector();
        columns.add("Values");
        Vector row;
        for (Iterator it = preference.getValues().iterator(); it.hasNext(); ) {
            Object object = it.next();
            row = new Vector();
            row.add(object);
            rows.add(row);
        }
        TableModel model = new DefaultTableModel(rows, columns);
        table.setModel(model);
        checkEnabled();
        repaint();
    }

    private void checkEnabled() {
        if (table.getModel().getRowCount() < 1) {
            this.removeButton.setEnabled(false);
            for (int t = 0; t < trackedButtons.size(); t++) ((JButton) trackedButtons.elementAt(t)).setEnabled(false);
            return;
        }
        if (table.getSelectedRow() == -1) {
            this.removeButton.setEnabled(false);
            for (int t = 0; t < trackedButtons.size(); t++) ((JButton) trackedButtons.elementAt(t)).setEnabled(false);
        }
        if (preference.isEditable()) {
            this.removeButton.setEnabled(true);
            for (int t = 0; t < trackedButtons.size(); t++) ((JButton) trackedButtons.elementAt(t)).setEnabled(true);
        } else {
            this.removeButton.setEnabled(false);
            for (int t = 0; t < trackedButtons.size(); t++) ((JButton) trackedButtons.elementAt(t)).setEnabled(false);
        }
    }
}
