package org.objectstyle.cayenne.modeler.dialog.codegen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

/**
 * @author Andrus Adamchik
 */
public class EntitiesTabPanel extends JPanel {

    protected JTable table;

    protected JCheckBox checkAll;

    protected JLabel checkAllLabel;

    public EntitiesTabPanel() {
        this.table = new JTable();
        this.checkAll = new JCheckBox();
        this.checkAllLabel = new JLabel("Check All Entities");
        checkAll.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent event) {
                if (checkAll.isSelected()) {
                    checkAllLabel.setText("Uncheck All Entities");
                } else {
                    checkAllLabel.setText("Check All Entities");
                }
            }
        });
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        topPanel.add(checkAll);
        topPanel.add(checkAllLabel);
        JScrollPane tablePanel = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        tablePanel.setPreferredSize(new Dimension(300, 200));
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    public JTable getTable() {
        return table;
    }

    public JCheckBox getCheckAll() {
        return checkAll;
    }
}
