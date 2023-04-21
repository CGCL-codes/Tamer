package de.fu_berlin.inf.gmanda.gui;

import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTreeFilterTextField.FilterKind;
import de.fu_berlin.inf.gmanda.gui.misc.PrimaryDocumentCellRenderer;
import de.fu_berlin.inf.gmanda.imports.GmaneFacade;
import de.fu_berlin.inf.gmanda.proxies.Filter;
import de.fu_berlin.inf.gmanda.proxies.FilterKindProxy;
import de.fu_berlin.inf.gmanda.proxies.FilterProxy;
import de.fu_berlin.inf.gmanda.proxies.FilterTextProxy;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.HashMapUtils;
import de.fu_berlin.inf.gmanda.util.StateChangeListener;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.tree.TreeMaker;
import de.fu_berlin.inf.gmanda.util.tree.TreeStructure;
import de.fu_berlin.inf.gmanda.util.tree.TreeWalker;

/**
 * A JTree component that displays a project and all associated primary
 * documents as a tree.
 * 
 * Supports filtering and searching.
 * 
 */
public class PrimaryDocumentTree extends JTree {

    public class TreeModelMaker implements TreeMaker<PrimaryDocument> {

        protected TreeModelImpl treeModel;

        public TreeModelMaker(TreeModelImpl treeModel) {
            this.treeModel = treeModel;
        }

        @Override
        public TreeStructure<PrimaryDocument> toStructure(final PrimaryDocument t) {
            return new TreeStructure<PrimaryDocument>() {

                @Override
                public Iterable<PrimaryDocument> getChildren() {
                    return treeModel.getChildren(t);
                }

                @Override
                public PrimaryDocument get() {
                    return t;
                }
            };
        }
    }

    /**
	 * Scenario 1: * If the user clicks, this will be called and must update the
	 * selectionproxy value.
	 * 
	 * Scenario 2: * If somewhere in the program the selection is udpated, we
	 * need to set our treepath accordingly and do not want to set the selection
	 * (because we are receiving the change). *
	 * 
	 */
    public class TreeSelectionImpl implements TreeSelectionListener {

        public boolean receiving = true;

        public void valueChanged(TreeSelectionEvent arg0) {
            if (!receiving) return;
            TreePath path = arg0.getPath();
            if (path != null) {
                Object[] turns = path.getPath();
                Object toShow = turns[turns.length - 1];
                if (toShow instanceof PrimaryDocument) {
                    selectionProxy.setVariable(toShow);
                }
            } else {
                selectionProxy.setVariable(null);
            }
        }
    }

    public static List<PrimaryDocument> filterChildren(List<PrimaryDocument> list) {
        List<PrimaryDocument> result = new LinkedList<PrimaryDocument>();
        codeable: for (PrimaryDocument c : list) {
            PrimaryDocument pd = c.getParent();
            while (pd != null) {
                if (list.contains(pd)) continue codeable;
                pd = pd.getParent();
            }
            result.add(c);
        }
        return result;
    }

    public class TreeModelImpl implements TreeModel {

        List<PrimaryDocument> rootFilterList = null;

        int rootFilterPosition = -1;

        Set<TreeModelListener> listeners = new HashSet<TreeModelListener>();

        FilterKind filterKind;

        List<PrimaryDocument> lists;

        HashMap<PrimaryDocument, List<PrimaryDocument>> threads;

        public void updateFilter() {
            if (project == null) return;
            Filter filter = filterProxy.getVariable();
            filterKind = filterKindProxy.getVariable();
            if (filter == null) rootFilterList = null; else rootFilterList = new ArrayList<PrimaryDocument>(filter.filterResult);
            rootFilterPosition = -1;
            TreePath currentSelection = getSelectionPath();
            if (rootFilterList != null) {
                switch(filterKind) {
                    case SINGLE:
                        break;
                    case ROOT:
                        rootFilterList = filterChildren(rootFilterList);
                        break;
                    case THREAD:
                        rootFilterList = filterChildren(rootFilterList);
                        lists = new LinkedList<PrimaryDocument>();
                        threads = new HashMap<PrimaryDocument, List<PrimaryDocument>>();
                        for (PrimaryDocument pd : rootFilterList) {
                            if (pd.getParent() == null) lists.add(pd); else {
                                PrimaryDocument child = pd;
                                PrimaryDocument parent = pd.getParent();
                                while (parent.getParent() != null) {
                                    child = parent;
                                    parent = parent.getParent();
                                }
                                if (!lists.contains(parent)) lists.add(parent);
                                HashMapUtils.putList(threads, parent, child, true);
                            }
                        }
                        Collections.sort(lists, PrimaryDocument.getProjectSensitiveComparator(project));
                        for (List<PrimaryDocument> list : threads.values()) {
                            Collections.sort(list);
                        }
                        break;
                }
            }
            if (rootFilterList != null) Collections.sort(rootFilterList, PrimaryDocument.getProjectSensitiveComparator(project));
            TreeModelEvent event = new TreeModelEvent(project, buildTreePath(project));
            for (TreeModelListener listener : listeners) {
                listener.treeStructureChanged(event);
            }
            if (rootFilterList != null && rootFilterList.size() > 0) {
                for (PrimaryDocument pd : filterProxy.getVariable().filterResult) {
                    expand(pd);
                }
                if (currentSelection != null) {
                    Object o = currentSelection.getLastPathComponent();
                    if (o instanceof PrimaryDocument) {
                        PrimaryDocument pd = (PrimaryDocument) o;
                        switch(filterKind) {
                            case SINGLE:
                                if (rootFilterList.contains(pd)) {
                                    selectionProxy.setVariable(pd);
                                    return;
                                }
                                break;
                            case ROOT:
                                while (pd != null) {
                                    if (rootFilterList.contains(pd)) {
                                        selectionProxy.setVariable(o);
                                        return;
                                    }
                                    pd = pd.getParent();
                                }
                                break;
                            case THREAD:
                                if (pd.getParent() == null) {
                                    if (lists.contains(pd)) {
                                        selectionProxy.setVariable(pd);
                                        return;
                                    }
                                } else {
                                    PrimaryDocument child = pd;
                                    PrimaryDocument parent = pd.getParent();
                                    while (parent.getParent() != null) {
                                        child = parent;
                                        parent = parent.getParent();
                                    }
                                    if (lists.contains(parent)) {
                                        if (threads.containsKey(parent) && threads.get(parent).contains(child)) {
                                            selectionProxy.setVariable(pd);
                                            return;
                                        }
                                    }
                                }
                        }
                    }
                }
                rootFilterPosition = -1;
                nextFilterItem();
            } else {
                if (currentSelection != null && (rootFilterList == null || rootFilterList.size() > 0)) selectionProxy.setVariable(currentSelection.getLastPathComponent());
            }
        }

        public void nextFilterItem() {
            if (rootFilterList == null) return;
            if (rootFilterPosition < 0 || rootFilterPosition + 1 >= rootFilterList.size()) rootFilterPosition = 0; else rootFilterPosition++;
            selectionProxy.setVariable(rootFilterList.get(rootFilterPosition));
        }

        public void previousFilterItem() {
            if (rootFilterList == null) return;
            if (rootFilterPosition <= 0 || rootFilterPosition - 1 >= rootFilterList.size()) rootFilterPosition = rootFilterList.size() - 1; else rootFilterPosition--;
            selectionProxy.setVariable(rootFilterList.get(rootFilterPosition));
        }

        public TreeModelImpl() {
            filterKindProxy.add(new VariableProxyListener<FilterKind>() {

                public void setVariable(FilterKind newValue) {
                    updateFilter();
                }
            });
            filterProxy.add(new VariableProxyListener<Filter>() {

                public void setVariable(Filter newValue) {
                    updateFilter();
                }
            });
        }

        public Object getRoot() {
            if (project != null) {
                return project;
            }
            return null;
        }

        public List<PrimaryDocument> getChildren(Object node) {
            if (node instanceof Project) {
                if (rootFilterList == null) return ((Project) node).getPrimaryDocuments();
                if (filterKind != FilterKind.THREAD) return rootFilterList; else return lists;
            }
            if (node instanceof PrimaryDocument) {
                PrimaryDocument pd = (PrimaryDocument) node;
                if (rootFilterList == null) return pd.getChildren();
                switch(filterKind) {
                    case SINGLE:
                        throw new RuntimeException();
                    case ROOT:
                        return pd.getChildren();
                    case THREAD:
                        if (threads.containsKey(pd)) return threads.get(pd);
                        return pd.getChildren();
                }
            }
            throw new RuntimeException();
        }

        public Object getChild(Object node, int i) {
            return getChildren(node).get(i);
        }

        public int getChildCount(Object node) {
            if (node instanceof Project) {
                if (rootFilterList == null) return ((Project) node).getPrimaryDocuments().size();
                if (filterKind != FilterKind.THREAD) return rootFilterList.size(); else return lists.size();
            }
            if (node instanceof PrimaryDocument) {
                PrimaryDocument pd = (PrimaryDocument) node;
                if (rootFilterList == null) return pd.getChildren().size();
                switch(filterKind) {
                    case SINGLE:
                        return 0;
                    case ROOT:
                        return pd.getChildren().size();
                    case THREAD:
                        if (threads.containsKey(pd)) return threads.get(pd).size();
                        return pd.getChildren().size();
                }
            }
            throw new RuntimeException("Only primary documents and projects can be tree nodes.");
        }

        public boolean isLeaf(Object node) {
            return getChildCount(node) == 0;
        }

        public int getIndexOfChild(Object node, Object i) {
            if (node instanceof Project) {
                if (rootFilterList == null) return ((Project) node).getPrimaryDocuments().indexOf(i);
                if (filterKind != FilterKind.THREAD) return rootFilterList.indexOf(i); else return lists.indexOf(i);
            }
            if (node instanceof PrimaryDocument) {
                PrimaryDocument pd = (PrimaryDocument) node;
                if (rootFilterList == null) return pd.getChildren().indexOf(i);
                switch(filterKind) {
                    case SINGLE:
                        throw new RuntimeException();
                    case ROOT:
                        return pd.getChildren().indexOf(i);
                    case THREAD:
                        if (threads.containsKey(pd)) return threads.get(pd).indexOf(i);
                        return pd.getChildren().indexOf(i);
                }
            }
            throw new RuntimeException("Only project and primary documents can be nodes.");
        }

        public TreePath buildTreePath(Object node) {
            if (project == null || node == null) return null;
            List<Object> nodes = new LinkedList<Object>();
            if (node instanceof Project) {
                nodes.add(node);
            }
            if (node instanceof PrimaryDocument) {
                PrimaryDocument pd = (PrimaryDocument) node;
                while (pd != null) {
                    nodes.add(0, pd);
                    if (rootFilterList != null && filterKind != FilterKind.THREAD && rootFilterList.contains(pd)) break;
                    pd = pd.getParent();
                }
                if (!assertTreePath(getRoot(), nodes, getModel(), node)) return null;
                nodes.add(0, project);
            }
            return new TreePath(nodes.toArray());
        }

        private boolean assertTreePath(Object root, List<Object> nodes, TreeModel model, Object node) {
            Object parent = root;
            for (Object o : nodes) {
                int i = model.getIndexOfChild(parent, o);
                if (i == -1) return false;
                if (model.getChild(parent, i) != o) return false;
                parent = o;
            }
            return parent == node;
        }

        public void updateNode(Object node, boolean structure) {
            final TreePath path = buildTreePath(node);
            TreeModelEvent event = new TreeModelEvent(node, path);
            for (TreeModelListener listener : listeners) {
                if (structure) {
                    listener.treeStructureChanged(event);
                } else {
                    listener.treeNodesChanged(event);
                }
            }
        }

        public void addTreeModelListener(TreeModelListener arg0) {
            listeners.add(arg0);
        }

        public void removeTreeModelListener(TreeModelListener arg0) {
            listeners.remove(arg0);
        }

        public void valueForPathChanged(TreePath arg0, Object arg1) {
            throw new RuntimeException();
        }
    }

    Project project;

    ProjectProxy projectProxy;

    SelectionProxy selectionProxy;

    FilterTextProxy filterTextProxy;

    FilterKindProxy filterKindProxy;

    FilterProxy filterProxy;

    TreeSelectionImpl selectionListener;

    TreeModelImpl treeModel;

    ForegroundWindowProxy windowProxy;

    GmaneFacade gmane;

    public PrimaryDocumentTree(ProjectProxy projectProxy, SelectionProxy selectionProxy, FilterTextProxy filterTextProxy, FilterProxy filterProxy, FilterKindProxy filterKindProxy, ForegroundWindowProxy windowProxy, GmaneFacade gmane, PrimaryDocumentCellRenderer cellRenderer) {
        super();
        this.projectProxy = projectProxy;
        this.selectionProxy = selectionProxy;
        this.filterTextProxy = filterTextProxy;
        this.filterProxy = filterProxy;
        this.windowProxy = windowProxy;
        this.filterKindProxy = filterKindProxy;
        this.gmane = gmane;
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setEditable(false);
        setRootVisible(false);
        setExpandsSelectedPaths(true);
        Set<AWTKeyStroke> newForwardKeys = new HashSet<AWTKeyStroke>();
        newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
        selectionListener = new TreeSelectionImpl();
        treeModel = new TreeModelImpl();
        addTreeSelectionListener(selectionListener);
        setCellRenderer(cellRenderer);
        projectProxy.addAndNotify(new VariableProxyListener<Project>() {

            StateChangeListener<PrimaryDocument> localListener = new StateChangeListener<PrimaryDocument>() {

                public void stateChangedNotification(PrimaryDocument t) {
                    TreeModel model = getModel();
                    if (model instanceof TreeModelImpl) {
                        TreeModelImpl impl = (TreeModelImpl) model;
                        impl.updateNode(t, false);
                    }
                }
            };

            StateChangeListener<Project> nonLocalListener = new StateChangeListener<Project>() {

                public void stateChangedNotification(Project t) {
                    TreeModel model = getModel();
                    if (model instanceof TreeModelImpl) {
                        TreeModelImpl impl = (TreeModelImpl) model;
                        impl.updateNode(t, true);
                    }
                }
            };

            public void setVariable(Project newGame) {
                if (project != null) {
                    project.getLocalChangeNotifier().remove(localListener);
                    project.getNonLocalChangeNotifier().remove(nonLocalListener);
                }
                project = newGame;
                if (project == null) {
                    setEnabled(false);
                    setBackground(Color.LIGHT_GRAY);
                    setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
                } else {
                    setEnabled(true);
                    setBackground(Color.WHITE);
                    setModel(treeModel);
                    project.getLocalChangeNotifier().add(localListener);
                    project.getNonLocalChangeNotifier().add(nonLocalListener);
                }
            }
        });
        selectionProxy.add(new VariableProxyListener<Object>() {

            public void setVariable(Object toFokus) {
                if (toFokus == null) return;
                TreePath path;
                if (!(toFokus instanceof TreePath)) path = treeModel.buildTreePath(toFokus); else path = (TreePath) toFokus;
                if (path == null) return;
                selectionListener.receiving = false;
                setSelectionPath(path);
                selectionListener.receiving = true;
                scrollPathToVisible(path);
            }
        });
    }

    public void expand(Object toFokus) {
        TreePath path;
        if (!(toFokus instanceof TreePath)) path = treeModel.buildTreePath(toFokus); else path = (TreePath) toFokus;
        if (getModel().isLeaf(path.getLastPathComponent())) path = path.getParentPath();
        expandPath(path);
    }

    public void nextFilterItem() {
        treeModel.nextFilterItem();
    }

    public void previousFilterItem() {
        treeModel.previousFilterItem();
    }

    public TreeWalker<PrimaryDocument> getTreeWalker(PrimaryDocument root) {
        return new TreeWalker<PrimaryDocument>(root, new TreeModelMaker(treeModel));
    }
}
