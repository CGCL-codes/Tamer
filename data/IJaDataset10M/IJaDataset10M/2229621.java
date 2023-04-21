package org.yaoqiang.bpmn.editor.dialog.jsonpanels;

import java.awt.Container;
import org.yaoqiang.bpmn.editor.BPMNEditor;
import org.yaoqiang.bpmn.editor.dialog.tree.OrganizationTreeNode;
import org.yaoqiang.bpmn.editor.dialog.tree.OrganizationTreePanel;
import org.yaoqiang.bpmn.editor.util.BPMNEditorUtils;
import org.yaoqiang.graph.editor.dialog.PanelContainer;
import com.mxgraph.model.mxCell;

/**
 * SelectDepartmentPanel
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class SelectDepartmentPanel extends OrganizationTreePanel {

    private static final long serialVersionUID = -7257047844739551708L;

    public SelectDepartmentPanel(PanelContainer pc, BPMNEditor owner) {
        super(pc, owner, "department");
    }

    public void saveObjects() {
        Container parent = getParentPanel();
        if (selectedPath != null && parent instanceof MemberOfPanel) {
            ((MemberOfPanel) parent).setDepartmentPath(selectedPath);
            OrganizationTreeNode node = (OrganizationTreeNode) selectedPath.getLastPathComponent();
            mxCell cell = ((OrganizationTreeNode) node).getUserObject();
            String label = graph.convertValueToString(cell);
            ((MemberOfPanel) parent).getDepartmentPanel().setText(label);
            ((MemberOfPanel) parent).getRolePanel().refreshItem(BPMNEditorUtils.getChildRoles(graph, cell));
        }
    }
}
