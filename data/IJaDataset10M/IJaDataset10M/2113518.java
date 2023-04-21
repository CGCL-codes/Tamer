package pcgen.gui2.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import pcgen.gui2.util.treeview.DataView;
import pcgen.gui2.util.treeview.TreeView;
import pcgen.gui2.util.treeview.TreeViewPath;
import pcgen.gui2.util.treeview.TreeViewTableModel;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public class JCheckBoxTreeViewPane extends JTreeViewPane {

    @Override
    protected <T> TreeViewTableModel<T> createDefaultTreeViewTableModel(DataView<T> dataView) {
        return super.createDefaultTreeViewTableModel(dataView);
    }

    private class CheckBoxTreeViewTableModel<T> extends TreeViewTableModel<T> {

        private final Map<TreeView<? super T>, List<TreeViewPath<? super T>>> viewMap;

        private List<T> selectedData;

        public CheckBoxTreeViewTableModel(DataView<T> dataView) {
            super(dataView);
            this.viewMap = new HashMap<TreeView<? super T>, List<TreeViewPath<? super T>>>();
            this.selectedData = new ArrayList<T>();
        }

        public boolean isSelected(Object treeNode) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeNode;
            for (TreeViewPath<? super T> path : getSelectedPaths()) {
                int level = node.getLevel();
                if (path.getPathCount() < level) {
                    continue;
                }
                if (path.getPathComponent(level - 1).equals(node.getUserObject())) {
                    return true;
                }
            }
            return false;
        }

        private List<TreeViewPath<? super T>> getSelectedPaths() {
            TreeView<? super T> treeView = getSelectedTreeView();
            if (!viewMap.containsKey(treeView)) {
                List<TreeViewPath<? super T>> selectedPaths = new ArrayList<TreeViewPath<? super T>>();
                for (T data : selectedData) {
                    selectedPaths.addAll(treeView.getPaths(data));
                }
                viewMap.put(treeView, selectedPaths);
            }
            return viewMap.get(treeView);
        }

        public void setSelected(DefaultMutableTreeNode node, boolean selected) {
        }
    }
}
