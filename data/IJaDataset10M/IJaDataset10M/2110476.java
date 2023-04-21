package ch.intertec.storybook.view.tree;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import ch.intertec.storybook.model.hbn.entity.AbstractEntity;
import ch.intertec.storybook.model.hbn.entity.Person;
import ch.intertec.storybook.model.hbn.entity.Scene;
import ch.intertec.storybook.toolkit.EntityUtil;

@SuppressWarnings("serial")
class EntityTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object userObject = node.getUserObject();
        if (leaf) {
            if (userObject instanceof Person) {
                setLeafIcon(((Person) userObject).getGender().getIcon());
            } else if (userObject instanceof Scene) {
                Scene scene = (Scene) userObject;
                setLeafIcon(scene.getSceneState().getIcon());
            } else if (userObject instanceof AbstractEntity) {
                Icon icon = EntityUtil.getEntityIcon((AbstractEntity) userObject);
                setLeafIcon(icon);
            } else {
                setLeafIcon(null);
            }
        }
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (!leaf && userObject instanceof AbstractEntity) {
            Icon icon = EntityUtil.getEntityIcon((AbstractEntity) userObject);
            setIcon(icon);
        }
        return this;
    }

    ;
}
