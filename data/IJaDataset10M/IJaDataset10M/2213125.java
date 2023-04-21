package net.assimilator.utilities.console.tree;

import net.assimilator.utilities.console.ParameterPane;
import net.assimilator.utilities.console.graph.ServiceGraphView;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Contains the service tree views in a tabbed pane.
 *
 * @author Kevin Hartig
 * @version $Id: TreePanel.java 210 2007-08-24 18:36:42Z khartig $
 */
public class TreePanel extends JPanel implements ChangeListener {

    private final JTabbedPane tabbedPane;

    private final java.util.List<ServiceTree> serviceTrees = Collections.synchronizedList(new ArrayList<ServiceTree>());

    public TreePanel() {
        super();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 400));
        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(this);
    }

    public void addServiceTree(ServiceTree serviceTree, String name) {
        serviceTrees.add(serviceTree);
        tabbedPane.addTab(name, null, createTreeTab(serviceTree), name + " Tree");
        add(tabbedPane);
    }

    private JSplitPane createTreeTab(ServiceTree serviceTree) {
        JScrollPane scrollPane = new JScrollPane(serviceTree);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(250, 400));
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, serviceTree.getParameterPane());
        splitPane.setOneTouchExpandable(false);
        splitPane.setDividerLocation(400);
        return splitPane;
    }

    public void stateChanged(ChangeEvent e) {
        switch(tabbedPane.getSelectedIndex()) {
            default:
                for (ServiceTree serviceTree : serviceTrees) {
                    serviceTree.clearSelection();
                }
                break;
        }
    }
}
