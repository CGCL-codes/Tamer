package org.appspy.admin.client.scheduling;

import org.appspy.admin.client.AbstractTreePanel;
import org.appspy.admin.client.scheduling.actions.OpenListJobDeclarationPanelAction;
import org.appspy.admin.client.scheduling.actions.OpenListJobSchedulePanelAction;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public class ScheduleMenuTreePanel extends AbstractTreePanel {

    public ScheduleMenuTreePanel() {
        TreeNode root = new TreeNode("Scheduling");
        root.setExpanded(true);
        TreeNode jobDeclarationTreeNode = new TreeNode("Job Declarations");
        jobDeclarationTreeNode.setTooltip("Manage the job declarations");
        jobDeclarationTreeNode.setIconCls("reports-menu-icon");
        root.appendChild(jobDeclarationTreeNode);
        jobDeclarationTreeNode.addListener(new TreeNodeListenerAdapter() {

            @Override
            public void onClick(Node node, EventObject e) {
                new OpenListJobDeclarationPanelAction(getAdminModule()).execute();
            }
        });
        TreeNode jobScheduleTreeNode = new TreeNode("Job Schedules");
        jobScheduleTreeNode.setTooltip("Manage the job schedules");
        jobScheduleTreeNode.setIconCls("reports-menu-icon");
        root.appendChild(jobScheduleTreeNode);
        jobScheduleTreeNode.addListener(new TreeNodeListenerAdapter() {

            @Override
            public void onClick(Node node, EventObject e) {
                new OpenListJobSchedulePanelAction(getAdminModule()).execute();
            }
        });
        setTitle("Scheduling");
        setIconCls("email-icon");
        setWidth(200);
        setHeight(400);
        setEnableDD(true);
        setRootNode(root);
        setRootVisible(false);
    }
}
