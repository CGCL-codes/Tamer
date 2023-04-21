package org.objectwiz.fxclient.panel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.objectwiz.core.facet.display.DisplayFacet;
import org.objectwiz.core.facet.display.DisplayFacetProxy;
import org.objectwiz.core.model.ObjectClass;
import org.objectwiz.core.model.Property;
import org.objectwiz.core.ui.rendering.table.TableView;
import org.objectwiz.core.ui.rendering.table.TableViewEventListener;
import org.objectwiz.fxclient.panel.ChooseColumnsCheckBoxTreeModel.TreeRoot;
import org.objectwiz.javafx.checkboxtree.CheckBoxTreeModel;

/**
 * CheckBoxTreeModel for the {@link ChooseColumnsPanel} that is build upon
 * a {@link TableView}.
 *
 * One <emp>root column</emp> in the {@link TableView} may correspond to <emp>multiple
 * roots</emp> in the tree model: one per class in the hierarchy when the root
 * column corresponds to an entity.
 *
 * Upon status modification, the {@link TableView#hideShowColumn(String,boolean)}
 * method is called to apply the status to the column in the table.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public class ChooseColumnsCheckBoxTreeModel extends CheckBoxTreeModel<TreeRoot, String> implements TableViewEventListener {

    /**
     * A root in the tree.
     *
     * Corresponds to a <emp>root column</emp> in the {@link TableView}
     * ({@link TableView#getRootColumnsIds()}). If this column corresponds
     * to a mapped class, then there will be several {@link TreeRoot}s for
     * this root column, each associated to one specific class of the hierarchy.
     */
    public class TreeRoot {

        private String rootColumnId;

        private ObjectClass specificClass;

        public TreeRoot(String rootColumnId, ObjectClass specificClass) {
            this.rootColumnId = rootColumnId;
            this.specificClass = specificClass;
        }

        public String getRootColumnId() {
            return rootColumnId;
        }

        public ObjectClass getSpecificClass() {
            return specificClass;
        }

        @Override
        public int hashCode() {
            if (specificClass == null) return rootColumnId.hashCode();
            return (rootColumnId + specificClass.getName()).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof TreeRoot)) return false;
            TreeRoot r = (TreeRoot) obj;
            return r.rootColumnId.equals(this.rootColumnId) && r.specificClass == this.specificClass;
        }
    }

    private TableView tableView;

    private List<TreeRoot> roots;

    private Map<TreeRoot, List<String>> children;

    public ChooseColumnsCheckBoxTreeModel(TableView<?> tableView) {
        if (tableView == null) throw new NullPointerException("tableView");
        this.tableView = tableView;
        this.roots = new ArrayList();
        this.children = new HashMap();
        for (String rootColumn : tableView.getRootColumnsIds()) {
            Map<String, Property> subColumns = tableView.getSubColumns(rootColumn);
            if (subColumns.isEmpty()) {
                this.roots.add(new TreeRoot(rootColumn, null));
            } else {
                this.roots.addAll(buildRoots(rootColumn, subColumns));
            }
        }
        for (TreeRoot root : roots) {
            if (root.getSpecificClass() == null) {
                setRootStatus(root, tableView.isColumnVisible(root.getRootColumnId()));
            } else {
                for (String child : children.get(root)) {
                    setChildStatus(root, child, tableView.isColumnVisible(child));
                }
            }
        }
        tableView.registerEventListener(this);
    }

    private List<TreeRoot> buildRoots(String rootColumnId, Map<String, Property> subColumns) {
        Map<ObjectClass, List<String>> columnsByClass = new HashMap();
        List<String> columnsWithoutClass = new ArrayList();
        for (Map.Entry<String, Property> entry : subColumns.entrySet()) {
            if (entry.getValue() == null) {
                columnsWithoutClass.add(entry.getKey());
                continue;
            }
            ObjectClass cl = entry.getValue().getOwnerClass();
            List<String> columns = columnsByClass.get(cl);
            if (columns == null) {
                columns = new ArrayList();
                columnsByClass.put(cl, columns);
            }
            columns.add(entry.getKey());
        }
        List<ObjectClass> sortedClasses = new ArrayList(columnsByClass.keySet());
        Collections.sort(sortedClasses, new Comparator<ObjectClass>() {

            @Override
            public int compare(ObjectClass cl1, ObjectClass cl2) {
                if (cl1.equals(cl2)) return 0;
                if (cl1.isAssignableFrom(cl2)) return -1;
                if (cl2.isAssignableFrom(cl1)) return 1;
                return cl1.getName().compareTo(cl2.getName());
            }
        });
        List<TreeRoot> partialRoots = new ArrayList();
        boolean first = true;
        for (ObjectClass mc : sortedClasses) {
            TreeRoot root = new TreeRoot(rootColumnId, mc);
            partialRoots.add(root);
            children.put(root, columnsByClass.get(mc));
            if (first) {
                children.get(root).addAll(0, columnsWithoutClass);
                first = false;
            }
        }
        return partialRoots;
    }

    /**
     * {@inheritDoc}
     *
     * Not to be mistaken for the root columns of the {@link TableView} (may differ
     * in case the root column corresponds to an entity the class of which is
     * contained within a hierarchy of classes).
     */
    @Override
    public List<TreeRoot> getRoots() {
        return roots;
    }

    @Override
    public List<String> getChildren(TreeRoot root) {
        if (root.getSpecificClass() == null) {
            return new ArrayList();
        } else {
            return children.get(root);
        }
    }

    @Override
    public String getRootLabel(TreeRoot root) {
        String rootColumnLabel = tableView.getColumnLabel(root.getRootColumnId(), false);
        if (root.getSpecificClass() == null) {
            return rootColumnLabel;
        } else {
            DisplayFacet display = DisplayFacetProxy.get(tableView.getApplication());
            return rootColumnLabel + " (" + display.getClassLabel(root.getSpecificClass()) + ")";
        }
    }

    @Override
    public String getChildLabel(String child) {
        return tableView.getColumnLabel(child, false);
    }

    @Override
    public void setRootStatus(TreeRoot root, boolean status) {
        super.setRootStatus(root, status);
        if (root.getSpecificClass() == null) {
            tableView.hideShowColumn(root.getRootColumnId(), status);
        }
    }

    @Override
    public void setChildStatus(TreeRoot root, String child, Boolean status) {
        super.setChildStatus(root, child, status);
        tableView.hideShowColumn(child, status);
    }

    /**
     * When the visibility of a column is changed, have to update the status of
     * the corresponding checkbox.
     */
    @Override
    public void columnVisibilityChanged(String columnId, boolean visible) {
        String rootColumnId = tableView.getRootColumn(columnId);
        Property property = tableView.getMappedProperty(columnId);
        if (rootColumnId.equals(columnId)) {
            super.setRootStatus(new TreeRoot(rootColumnId, null), visible);
        } else {
            TreeRoot root = new TreeRoot(rootColumnId, property == null ? null : property.getOwnerClass());
            super.setChildStatus(root, columnId, visible);
        }
    }
}
