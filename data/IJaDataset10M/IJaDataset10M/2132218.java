package org.appspy.admin.client.reports;

import org.appspy.admin.client.AbstractTreePanel;
import org.appspy.admin.client.reports.actions.OpenListDashboardPanelAction;
import org.appspy.admin.client.reports.actions.OpenListReportCategoryPanelAction;
import org.appspy.admin.client.reports.actions.OpenListReportPanelAction;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public class ReportsMenuTreePanel extends AbstractTreePanel {

    public ReportsMenuTreePanel() {
        TreeNode root = new TreeNode("Reports");
        root.setExpanded(true);
        TreeNode reportCatTreeNode = new TreeNode("Categories");
        reportCatTreeNode.setTooltip("Manage the report categories");
        reportCatTreeNode.setIconCls("reports-menu-icon");
        root.appendChild(reportCatTreeNode);
        reportCatTreeNode.addListener(new TreeNodeListenerAdapter() {

            @Override
            public void onClick(Node node, EventObject e) {
                new OpenListReportCategoryPanelAction(getAdminModule()).execute();
            }
        });
        TreeNode reportDescTreeNode = new TreeNode("Reports");
        reportDescTreeNode.setTooltip("Manage the reports");
        reportDescTreeNode.setIconCls("reports-menu-icon");
        root.appendChild(reportDescTreeNode);
        reportDescTreeNode.addListener(new TreeNodeListenerAdapter() {

            @Override
            public void onClick(Node node, EventObject e) {
                new OpenListReportPanelAction(getAdminModule()).execute();
            }
        });
        TreeNode dashboardTreeNode = new TreeNode("Dashboards");
        dashboardTreeNode.setTooltip("Manage the dashboards");
        dashboardTreeNode.setIconCls("reports-menu-icon");
        root.appendChild(dashboardTreeNode);
        dashboardTreeNode.addListener(new TreeNodeListenerAdapter() {

            @Override
            public void onClick(Node node, EventObject e) {
                new OpenListDashboardPanelAction(getAdminModule()).execute();
            }
        });
        setTitle("E-Mail");
        setIconCls("email-icon");
        setWidth(200);
        setHeight(400);
        setEnableDD(true);
        setRootNode(root);
        setRootVisible(false);
    }
}
