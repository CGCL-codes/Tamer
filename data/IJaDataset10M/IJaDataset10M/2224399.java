package pl.edu.agh.ssm.monitor.gui;

import java.util.HashMap;
import javax.swing.Icon;
import javax.swing.JPanel;
import pl.edu.agh.ssm.monitor.SessionMonitorView;
import pl.edu.agh.ssm.monitor.data.SessionConnection;
import pl.edu.agh.ssm.monitor.data.SessionNode;
import pl.edu.agh.ssm.monitor.utils.IconUtil;
import pl.edu.agh.ssm.monitor.visualization.GraphEdgeDisplayer;
import pl.edu.agh.ssm.monitor.visualization.GraphNodeDisplayer;

/**
 *
 * @author aneezka
 */
public class GUIItemDisplayer implements GraphNodeDisplayer, GraphEdgeDisplayer, UpdatePanelListener {

    public GUIItemDisplayer(SessionMonitorView view) {
        this.view = view;
        this.iconUtil = new IconUtil();
    }

    public void displayNodeItem(SessionNode node) {
        if (node != null) {
            if (!panels.containsKey(node)) {
                Icon icon = iconUtil.getNodeTypeIcon(node);
                UpdatePanel panel = new GraphNodeInfoPanel(node);
                panel.addListener(this);
                panels.put(node, panel);
                view.addBottomTab("Node " + node.getAddress().getHostAddress(), icon, (JPanel) panel);
            } else {
                view.selectBottomTab((JPanel) panels.get(node));
            }
        }
    }

    public void displayEdgeItem(SessionConnection connection) {
        if (connection != null) {
            if (!panels.containsKey(connection)) {
                System.out.println(panels);
                Icon icon = iconUtil.getConnectionIcon(connection);
                UpdatePanel panel = new GraphConnectionInfoPanel(connection);
                panel.addListener(this);
                panels.put(connection, panel);
                view.addBottomTab("Connection " + connection.getConnectionAddress().getHostAddress() + ":" + connection.getConnectionPort(), icon, (JPanel) panel);
            } else {
                view.selectBottomTab((JPanel) panels.get(connection));
            }
        }
    }

    public void panelUpdated(UpdatePanel panel) {
    }

    public void panelRemoved(UpdatePanel panel) {
        if (panels.containsValue(panel)) {
            for (Object o : panels.keySet()) {
                if (panels.get(o).equals(panel)) {
                    panels.remove(o);
                    return;
                }
            }
        }
    }

    private SessionMonitorView view;

    private IconUtil iconUtil;

    private HashMap<Object, UpdatePanel> panels = new HashMap<Object, UpdatePanel>();
}
