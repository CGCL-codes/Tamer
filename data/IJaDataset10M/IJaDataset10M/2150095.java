package mainview;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;

/**
 * Rendered replaces the default renderer for the JxList.
 * <p>
 * Renderer used for the JXTree to change the text colour of the nodes. Required
 * in the Group tab of the ArtifactDialog to reflect if an artifact belongs to a
 * group, by modifying the text colour (red if the artifact belongs to the group).
 *
 * @author cbride
 */
public class ListRenderer extends DefaultTreeCellRenderer {

    /**
	 * Text colour of a group that holds the Artifact
	 */
    private final Color inGroupColour = Color.red;

    /**
	 * Text colour of a group that does not hold the Artifact
	 */
    private final Color notInGroupColour = Color.black;

    /**
	 * Text colour of selected group
	 */
    private final Color selectedColour = Color.white;

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(mainview.MainviewApp.class).getContext().getResourceMap(GroupsDialog.class);
        ListObject node = (ListObject) value;
        if (!node.isRoot()) {
            if (node.getIsInGroup()) {
                this.setIcon(resourceMap.getIcon("groupsJxTree.inGroupIcon"));
                if (sel) {
                    setForeground(selectedColour);
                } else {
                    setForeground(inGroupColour);
                }
            } else {
                this.setIcon(resourceMap.getIcon("groupsJxTree.defaultIcon"));
                if (sel) {
                    setForeground(selectedColour);
                } else {
                    setForeground(notInGroupColour);
                }
            }
        } else {
            this.setIcon(null);
        }
        return this;
    }
}
