package com.agentfactory.afapl2.debugger.inspector;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import com.agentfactory.afapl2.debugger.AFAPL2Snapshot;
import com.agentfactory.afapl2.debugger.model.ActionDescription;
import com.agentfactory.visualiser.core.ISnapshot;
import com.agentfactory.visualiser.impl.InspectorPanel;

/**
 *
 * @author remcollier
 */
public class ActionsPanel extends JPanel implements InspectorPanel {

    private JTable table;

    private JTable config;

    private JTextField actuator;

    private JTextField cardinality;

    private List<ActionDescription> actions;

    public ActionsPanel() {
        this.setLayout(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Identifier");
        model.addColumn("Pre-Condition");
        model.addColumn("Post-Condition");
        table = new JTable(model);
        table.addMouseListener(new MouseListener() {

            public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
                int index = ((JTable) mouseEvent.getSource()).getSelectedRow();
                ActionDescription description = actions.get(index);
                DefaultTableModel model = (DefaultTableModel) config.getModel();
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
                if (description == null) {
                    actuator.setText("");
                    cardinality.setText("");
                } else {
                    actuator.setText(description.getActuator());
                    cardinality.setText("" + description.getCardinality());
                    Object[] rowData = null;
                    Map map = description.getConfiguration();
                    Iterator it = map.keySet().iterator();
                    while (it.hasNext()) {
                        rowData = new Object[2];
                        rowData[0] = it.next();
                        rowData[1] = map.get(rowData[0]);
                        model.addRow(rowData);
                    }
                }
            }

            public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
            }

            public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
            }

            public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
            }

            public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
            }
        });
        TableColumn col = table.getColumnModel().getColumn(0);
        col.setMinWidth(350);
        col = table.getColumnModel().getColumn(1);
        col.setMinWidth(150);
        col = table.getColumnModel().getColumn(2);
        col.setMinWidth(150);
        JPanel details = new JPanel(new BorderLayout());
        JPanel part1 = new JPanel(new GridLayout(2, 2));
        part1.add(new JLabel("Actuator:"));
        actuator = new JTextField();
        part1.add(actuator);
        part1.add(new JLabel("Cardinality:"));
        cardinality = new JTextField();
        part1.add(cardinality);
        details.add(BorderLayout.NORTH, part1);
        model = new DefaultTableModel();
        model.addColumn("Parameter");
        model.addColumn("Value");
        config = new JTable(model);
        details.add(BorderLayout.CENTER, new JScrollPane(config));
        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        pane.setTopComponent(new JScrollPane(table));
        pane.setBottomComponent(details);
        add(BorderLayout.CENTER, pane);
    }

    public synchronized void update(ISnapshot _snapshot) {
        AFAPL2Snapshot snapshot = (AFAPL2Snapshot) _snapshot;
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int i = model.getRowCount();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        actions = new ArrayList<ActionDescription>();
        Object[] rowData = null;
        for (ActionDescription desc : snapshot.getActions()) {
            actions.add(desc);
            rowData = new Object[5];
            rowData[0] = desc.getIdentifier();
            rowData[1] = desc.getPreCondition();
            rowData[2] = desc.getPostCondition();
            rowData[3] = desc.getActuator();
            rowData[4] = new Integer(desc.getCardinality());
            model.addRow(rowData);
        }
    }
}
