package org.vikamine.swing.subgroup.editors.zoomtable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.tree.TreePath;
import org.vikamine.app.Resources;
import org.vikamine.kernel.data.Attribute;
import org.vikamine.swing.subgroup.editors.zoomtable.compare.InstancesComparator;
import org.vikamine.swing.subgroup.event.SortChangedEvent;
import org.vikamine.swing.subgroup.event.listener.SortChangeListener;

/**
 * @author Tobias Vogele, atzmueller
 */
public class SortedZoomTableModel extends ZoomTableModel {

    private List listener = new LinkedList();

    /**
     * @param delegate
     */
    public SortedZoomTableModel(CommonZoomTreesModel delegate) {
        super(delegate);
    }

    @Override
    public Class getColumnClass(int column) {
        if (column == getSortOrderColumnIndex()) {
            return Comparator.class;
        }
        return super.getColumnClass(column);
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        if (column == getSortOrderColumnIndex()) {
            return Resources.I18N.getString("vikamine.zoomtable.columnName.sorting");
        }
        return super.getColumnName(column);
    }

    @Override
    public Object getValueAt(Object node, int column) {
        if (column == getSortOrderColumnIndex()) {
            AttributeNode attnode = (AttributeNode) node;
            Attribute att = attnode.getAttribute();
            return att;
        } else {
            return super.getValueAt(node, column);
        }
    }

    /**
     * @param att
     * @return
     */
    public boolean isSortedBy(Attribute att) {
        InstancesComparator comp = getTreeModel().getData().getInstancesComparator();
        return comp.isComparedBy(att);
    }

    @Override
    public int getZoomValuesColumnIndex() {
        return 3;
    }

    public int getSortOrderColumnIndex() {
        return 2;
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        if (column == getSortOrderColumnIndex()) {
            return ((AttributeNode) node).getAttribute() != null;
        }
        return super.isCellEditable(node, column);
    }

    @Override
    public void setValueAt(Object aValue, Object node, int column) {
        if (column == getSortOrderColumnIndex()) {
            AttributeNode attNode = (AttributeNode) node;
            Attribute att = attNode.getAttribute();
            if (att == null) {
                return;
            }
            sortBy(att);
            fireSortChangedEvent(att);
        } else {
            super.setValueAt(aValue, node, column);
        }
    }

    /**
     * 
     */
    private void sortBy(Attribute att) {
        InstancesComparator comp;
        List<Attribute> sortingAttributes;
        if (isSortedBy(att)) {
            getTreeModel().getData().removeSortingAttribute(att);
            expandTreeNode(att);
            comp = getTreeModel().getData().getInstancesComparator();
            sortingAttributes = comp.getAttributes();
            for (Attribute attribute : sortingAttributes) {
                ((SortedZoomTreesModel) getTreeModel()).move(attribute.getId(), new TreePath(getTreeModel().getRoot()), comp.getSortRank(attribute), true);
                collapseTreeNode(attribute);
            }
        } else {
            comp = getTreeModel().getData().getInstancesComparator();
            getTreeModel().getData().sortBy(att, comp.getAttributes().size());
            sortingAttributes = comp.getAttributes();
            ((SortedZoomTreesModel) getTreeModel()).move(att.getId(), new TreePath(getTreeModel().getRoot()), sortingAttributes.size() - 1, true);
            collapseTreeNode(att);
        }
    }

    private void collapseTreeNode(Attribute attribute) {
        getTreeModel().getController().getSortedZoomController().getTreeTable().getTree().collapsePath(getTreeModel().getPath(attribute.getId()));
    }

    private void expandTreeNode(Attribute attribute) {
        getTreeModel().getController().getSortedZoomController().getTreeTable().getTree().expandPath(getTreeModel().getPath(attribute.getId()));
    }

    public void addSortChangeListener(SortChangeListener liss) {
        listener.add(liss);
    }

    public void removeSortChangeListener(SortChangeListener liss) {
        listener.remove(liss);
    }

    protected void fireSortChangedEvent(Attribute sortAtt) {
        List listenersCopy = new LinkedList(listener);
        SortChangedEvent eve = new SortChangedEvent(this, sortAtt);
        for (Iterator iter = listenersCopy.iterator(); iter.hasNext(); ) {
            SortChangeListener liss = (SortChangeListener) iter.next();
            liss.sortingChanged(eve);
        }
    }
}
