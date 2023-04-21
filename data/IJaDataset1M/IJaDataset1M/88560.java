package org.argus.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.argus.gui.table.PropertiesTable;
import org.argus.jmxmodel.AbstractNode;
import org.argus.jmxmodel.JmxServerGraph;
import org.argus.jmxmodel.MBeanConstructor;
import org.argus.jmxmodel.MBeanOperation;

public class PropertiesPanel extends JPanel implements TreeSelectionListener {

    private static final long serialVersionUID = 1L;

    private JLabel label;

    public PropertiesPanel() {
        super();
        setLayout(new BorderLayout());
        label = new JLabel(" Properties");
        label.setFont(new Font("Arial", Font.BOLD, 12));
        add(label, BorderLayout.NORTH);
        PropertiesTable table = new PropertiesTable();
        table.adjustColumnWidth();
        add(new JScrollPane(table), BorderLayout.CENTER);
        Access.getBrowserTree().addTreeSelectionListener(this);
    }

    public void valueChanged(TreeSelectionEvent event) {
        TreePath path = event.getPath();
        String temp = " Properties";
        if (path != null && path.getPathCount() > 1) {
            Object[] o1 = path.getPath();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) o1[o1.length - 1];
            Object o = node.getUserObject();
            if (o instanceof JmxServerGraph) {
                JmxServerGraph server = (JmxServerGraph) o;
                temp = temp + " :: " + server.getName();
            } else if (o instanceof MBeanOperation) {
                MBeanOperation oper = (MBeanOperation) o;
                temp = temp + " :: " + oper.getName() + "()";
            } else if (o instanceof MBeanConstructor) {
                temp = temp + " :: " + o.toString();
            } else if (o instanceof AbstractNode) {
                AbstractNode item = (AbstractNode) o;
                temp = temp + " :: " + item.getName();
            } else {
                temp = temp + " :: " + o.toString();
            }
        }
        label.setText(temp);
    }
}
