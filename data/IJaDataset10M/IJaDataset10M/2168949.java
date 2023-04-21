package com.loribel.commons.swing.tree;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import com.loribel.commons.swing.tools.GB_ButtonBarTools;
import com.loribel.commons.swing.tools.GB_TreeTools;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_IconTools;
import com.loribel.commons.util.STools;

/**
 * Tools.
 *
 * @author Gregory Borelli
 */
public final class GB_TreeActionTools {

    private GB_TreeActionTools() {
    }

    public static JComponent decoreWithActions(JComponent a_comp, JTree a_tree) {
        Action[] l_actions = getActions(a_tree);
        List l_actions2 = CTools.toList(l_actions);
        return GB_ButtonBarTools.decoreWithIconBar(a_comp, l_actions2, BorderLayout.NORTH);
    }

    public static Action[] getActions(JTree a_tree) {
        Action[] retour = new Action[4];
        retour[0] = new ActionHierarchyExpand(a_tree);
        retour[1] = new ActionCollapse(a_tree);
        retour[2] = new ActionExpand(a_tree, 2);
        retour[3] = new ActionRefresh(a_tree);
        return retour;
    }

    public static Action getActionCollapseAll(JTree a_tree) {
        return new ActionCollapse(a_tree);
    }

    public static Action getActionExpand(JTree a_tree, int a_maxLevel) {
        return new ActionExpand(a_tree, a_maxLevel);
    }

    /**
     * Inner class Action.
     */
    private static class ActionCollapse extends AbstractAction {

        private JTree tree;

        public ActionCollapse(JTree a_tree) {
            tree = a_tree;
            ImageIcon l_icon = GB_IconTools.get(AA.ICON.X16_TREE_COLLAPSE_ALL);
            this.putValue(Action.SMALL_ICON, l_icon);
            this.putValue(Action.SHORT_DESCRIPTION, AA.BUTTON_TREE_COLLAPSE_DESC);
        }

        public void actionPerformed(ActionEvent a_event) {
            GB_TreeTools.collapseChildren(tree, (TreeNode) tree.getModel().getRoot());
        }
    }

    /**
     * Inner class Action.
     */
    private static class ActionExpand extends AbstractAction {

        private JTree tree;

        private int maxLevel;

        public ActionExpand(JTree a_tree, int a_maxLevel) {
            tree = a_tree;
            maxLevel = a_maxLevel;
            ImageIcon l_icon = GB_IconTools.get(AA.ICON.X16_TREE_EXPAND_ALL);
            this.putValue(Action.SMALL_ICON, l_icon);
            String l_desc = STools.replace(AA.BUTTON_TREE_EXPAND_DESC, "" + maxLevel);
            this.putValue(Action.SHORT_DESCRIPTION, l_desc);
        }

        public void actionPerformed(ActionEvent a_event) {
            GB_TreeTools.expandAll(tree, GB_TreeTools.getSelectedNode(tree), maxLevel);
        }
    }

    /**
     * Inner class Action.
     */
    private static class ActionRefresh extends AbstractAction {

        private JTree tree;

        public ActionRefresh(JTree a_tree) {
            tree = a_tree;
            ImageIcon l_icon = GB_IconTools.get(AA.ICON.X16_TREE_REFRESH);
            this.putValue(Action.SMALL_ICON, l_icon);
            this.putValue(Action.SHORT_DESCRIPTION, AA.BUTTON_TREE_REFRESH_DESC);
        }

        public void actionPerformed(ActionEvent a_event) {
            TreeNode l_node = GB_TreeTools.getSelectedNode(tree);
            GB_TreeTools.nodeStructureChanged(l_node);
            GB_TreeTools.select(tree, l_node);
        }
    }

    /**
     * Inner class Action.
     */
    private static class ActionHierarchyExpand extends AbstractAction {

        private JTree tree;

        public ActionHierarchyExpand(JTree a_tree) {
            tree = a_tree;
            ImageIcon l_icon = GB_IconTools.get(AA.ICON.X16_TREE_SELECT_ONE);
            this.putValue(Action.SMALL_ICON, l_icon);
            this.putValue(Action.SHORT_DESCRIPTION, AA.BUTTON_TREE_SELECT_ONE);
        }

        public void actionPerformed(ActionEvent a_event) {
            GB_TreeTools.expandHierachy(tree);
        }
    }
}
