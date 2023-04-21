package org.sodeja.swing.dataservice;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.sodeja.collections.ListUtils;
import org.sodeja.dataservice.DataService;
import org.sodeja.dataservice.DataServiceListener;

public class DataServiceTreeModel<T> implements TreeModel, DataServiceListener<T> {

    private List<TreeModelListener> listeners;

    private DataService<T> dataService;

    private DataPathExtractor<T> extractor;

    private Comparator<T> comparator;

    private DataTreeNode<T> root;

    private Map<T, List<String>> pathMappings;

    public DataServiceTreeModel(DataService<T> dataService, String rootNodeStr, DataPathExtractor<T> extractor, Comparator<T> comparator) {
        listeners = new ArrayList<TreeModelListener>();
        pathMappings = new HashMap<T, List<String>>();
        this.dataService = dataService;
        this.extractor = extractor;
        this.comparator = comparator;
        this.root = new DataTreeNode<T>(this, rootNodeStr);
        initNodes();
        dataService.addDataServiceListener(this);
    }

    private void initNodes() {
        for (T obj : dataService.findAll()) {
            created(dataService, obj);
        }
    }

    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(l);
    }

    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);
    }

    public Object getRoot() {
        return root;
    }

    public Object getChild(Object parent, int index) {
        if (!(parent instanceof DataTreeNode)) {
            throw new IllegalArgumentException();
        }
        return ((DataTreeNode) parent).getChild(index);
    }

    public int getChildCount(Object parent) {
        if (!(parent instanceof DataTreeNode)) {
            throw new IllegalArgumentException();
        }
        return ((DataTreeNode) parent).getChildCount();
    }

    public int getIndexOfChild(Object parent, Object child) {
        if (!(parent instanceof DataTreeNode)) {
            throw new IllegalArgumentException();
        }
        return ((DataTreeNode) parent).getIndexOfChild(child);
    }

    public boolean isLeaf(Object node) {
        return !(node instanceof DataTreeNode);
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        throw new UnsupportedOperationException();
    }

    public void created(DataService<T> service, T data) {
        List<String> path = ListUtils.asList(extractor.getPathFor(data));
        root.addData(path, data, new ArrayList<DataTreeNode>());
        pathMappings.put(data, path);
    }

    public void updated(DataService<T> service, T data) {
        List<String> oldPath = pathMappings.get(data);
        List<String> path = ListUtils.asList(extractor.getPathFor(data));
        if (path.equals(oldPath)) {
            root.changeData(path, data, new ArrayList<DataTreeNode>());
            return;
        }
        root.deleteData(oldPath, data, new ArrayList<DataTreeNode>());
        root.addData(path, data, new ArrayList<DataTreeNode>());
        pathMappings.put(data, path);
    }

    public void deleted(DataService<T> service, T data) {
        root.deleteData(pathMappings.get(data), data, new ArrayList<DataTreeNode>());
    }

    protected void fireInserted(List<DataTreeNode> path, int index, Object data) {
        TreeModelEvent event = new TreeModelEvent(this, ListUtils.asArray(path), new int[] { index }, new Object[] { data });
        for (TreeModelListener listener : listeners) {
            listener.treeNodesInserted(event);
        }
    }

    protected void fireChanged(List<DataTreeNode> path, int index, Object data) {
        TreeModelEvent event = new TreeModelEvent(this, ListUtils.asArray(path), new int[] { index }, new Object[] { data });
        for (TreeModelListener listener : listeners) {
            listener.treeNodesChanged(event);
        }
    }

    protected void fireRemoved(List<DataTreeNode> path, int index, Object data) {
        TreeModelEvent event = new TreeModelEvent(this, ListUtils.asArray(path), new int[] { index }, new Object[] { data });
        for (TreeModelListener listener : listeners) {
            listener.treeNodesRemoved(event);
        }
    }

    protected Comparator<T> getComparator() {
        return comparator;
    }

    protected TreePath findPathFor(T object) {
        List<DataTreeNode> nodes = new ArrayList<DataTreeNode>();
        root.findPath(pathMappings.get(object), object, nodes);
        TreePath tp = new TreePath(ListUtils.asArray(nodes));
        return tp.pathByAddingChild(object);
    }
}
