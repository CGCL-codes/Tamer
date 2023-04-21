package gate.gui;

import gate.CreoleRegister;
import gate.DataStore;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.Resource;
import gate.VisualResource;
import gate.creole.AbstractResource;
import gate.creole.ResourceData;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.GuiType;
import gate.event.DatastoreEvent;
import gate.event.DatastoreListener;
import gate.persist.PersistenceException;
import gate.security.SecurityException;
import gate.util.Err;
import gate.util.GateRuntimeException;
import gate.util.Strings;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

@CreoleResource(name = "Serial Datastore Viewer", guiType = GuiType.LARGE, resourceDisplayed = "gate.persist.SerialDataStore", mainViewer = true)
public class SerialDatastoreViewer extends JScrollPane implements VisualResource, DatastoreListener {

    public SerialDatastoreViewer() {
    }

    public void cleanup() {
        datastore.removeDatastoreListener(this);
        myHandle = null;
        datastore = null;
    }

    /** Accessor for features. */
    public FeatureMap getFeatures() {
        return features;
    }

    /** Mutator for features */
    public void setFeatures(FeatureMap features) {
        this.features = features;
    }

    /**
   * Gets the value of a parameter of this resource.
   * 
   * @param paramaterName the name of the parameter
   * @return the current value of the parameter
   */
    public Object getParameterValue(String paramaterName) throws ResourceInstantiationException {
        return AbstractResource.getParameterValue(this, paramaterName);
    }

    /**
   * Sets the value for a specified parameter.
   * 
   * @param paramaterName the name for the parameteer
   * @param parameterValue the value the parameter will receive
   */
    public void setParameterValue(String paramaterName, Object parameterValue) throws ResourceInstantiationException {
        BeanInfo resBeanInf = null;
        try {
            resBeanInf = Introspector.getBeanInfo(this.getClass(), Object.class);
        } catch (Exception e) {
            throw new ResourceInstantiationException("Couldn't get bean info for resource " + this.getClass().getName() + Strings.getNl() + "Introspector exception was: " + e);
        }
        AbstractResource.setParameterValue(this, resBeanInf, paramaterName, parameterValue);
    }

    /**
   * Sets the values for more parameters in one step.
   * 
   * @param parameters a feature map that has paramete names as keys and
   *          parameter values as values.
   */
    public void setParameterValues(FeatureMap parameters) throws ResourceInstantiationException {
        AbstractResource.setParameterValues(this, parameters);
    }

    /** Initialise this resource, and return it. */
    public Resource init() throws ResourceInstantiationException {
        return this;
    }

    public void clear() {
    }

    public void setTarget(Object target) {
        if (target == null) {
            datastore = null;
            return;
        }
        if (target instanceof DataStore) {
            datastore = (DataStore) target;
            initLocalData();
            initGuiComponents();
            initListeners();
        } else {
            throw new IllegalArgumentException("SerialDatastoreViewers can only be used with GATE serial datastores!\n" + target.getClass().toString() + " is not a GATE serial datastore!");
        }
    }

    public void setHandle(Handle handle) {
        if (handle instanceof NameBearerHandle) {
            myHandle = (NameBearerHandle) handle;
        }
    }

    protected void fireProgressChanged(int e) {
        myHandle.fireProgressChanged(e);
    }

    protected void fireProcessFinished() {
        myHandle.fireProcessFinished();
    }

    protected void fireStatusChanged(String e) {
        myHandle.fireStatusChanged(e);
    }

    protected void initLocalData() {
    }

    protected void initGuiComponents() {
        treeRoot = new DefaultMutableTreeNode(datastore.getName(), true);
        treeModel = new DefaultTreeModel(treeRoot, true);
        mainTree = new JTree();
        mainTree.setModel(treeModel);
        mainTree.setExpandsSelectedPaths(true);
        mainTree.expandPath(new TreePath(treeRoot));
        mainTree.addTreeWillExpandListener(new TreeWillExpandListener() {

            @Override
            public void treeWillCollapse(TreeExpansionEvent e) {
            }

            @Override
            public void treeWillExpand(TreeExpansionEvent e) {
                TreePath path = e.getPath();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                if (node.getChildCount() == 0 && node.getUserObject() instanceof DSType) {
                    DSType dsType = (DSType) node.getUserObject();
                    if (dsType.expanded) return;
                    node.removeAllChildren();
                    try {
                        Iterator lrIDsIter = datastore.getLrIds(dsType.type).iterator();
                        while (lrIDsIter.hasNext()) {
                            String id = (String) lrIDsIter.next();
                            DSEntry entry = new DSEntry(datastore.getLrName(id), id, dsType.type);
                            DefaultMutableTreeNode lrNode = new DefaultMutableTreeNode(entry, false);
                            treeModel.insertNodeInto(lrNode, node, node.getChildCount());
                            node.add(lrNode);
                        }
                        dsType.expanded = true;
                    } catch (PersistenceException pe) {
                        throw new GateRuntimeException(pe.toString());
                    }
                }
            }
        });
        try {
            Iterator lrTypesIter = datastore.getLrTypes().iterator();
            CreoleRegister cReg = Gate.getCreoleRegister();
            while (lrTypesIter.hasNext()) {
                String type = (String) lrTypesIter.next();
                ResourceData rData = (ResourceData) cReg.get(type);
                DSType dsType = new DSType(rData.getName(), type);
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(dsType);
                treeModel.insertNodeInto(node, treeRoot, treeRoot.getChildCount());
            }
        } catch (PersistenceException pe) {
            throw new GateRuntimeException(pe.toString());
        }
        DefaultTreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
        selectionModel.setSelectionMode(DefaultTreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        mainTree.setSelectionModel(selectionModel);
        getViewport().setView(mainTree);
        popup = new JPopupMenu();
        deleteAction = new DeleteAction();
        loadAction = new LoadAction();
        popup.add(deleteAction);
        popup.add(loadAction);
    }

    protected void initListeners() {
        datastore.addDatastoreListener(this);
        mainTree.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    TreePath path = mainTree.getPathForLocation(e.getX(), e.getY());
                    Object value = null;
                    if (path != null) value = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
                    if (value != null && value instanceof DSEntry) {
                        loadAction.ignoreSelection = true;
                        loadAction.setLocation(path);
                        loadAction.actionPerformed(null);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    TreePath path = mainTree.getPathForLocation(e.getX(), e.getY());
                    deleteAction.setLocation(path);
                    loadAction.setLocation(path);
                    popup.show(SerialDatastoreViewer.this, e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    TreePath path = mainTree.getPathForLocation(e.getX(), e.getY());
                    deleteAction.setLocation(path);
                    loadAction.setLocation(path);
                    popup.show(SerialDatastoreViewer.this, e.getX(), e.getY());
                }
            }
        });
    }

    /**
   * ACtion to delete all selected resources.
   */
    class DeleteAction extends AbstractAction {

        public DeleteAction() {
            super("Delete");
        }

        public void actionPerformed(ActionEvent e) {
            TreePath[] selectedPaths = mainTree.getSelectionPaths();
            if (selectedPaths == null && location != null) {
                selectedPaths = new TreePath[] { location };
                location = null;
            }
            if (selectedPaths != null) {
                for (TreePath aPath : selectedPaths) {
                    Object value = ((DefaultMutableTreeNode) aPath.getLastPathComponent()).getUserObject();
                    if (value instanceof DSEntry) {
                        DSEntry entry = (DSEntry) value;
                        try {
                            datastore.delete(entry.type, entry.id);
                        } catch (gate.persist.PersistenceException pe) {
                            JOptionPane.showMessageDialog(SerialDatastoreViewer.this, "Error!\n" + pe.toString(), "GATE", JOptionPane.ERROR_MESSAGE);
                            pe.printStackTrace(Err.getPrintWriter());
                        } catch (SecurityException se) {
                            JOptionPane.showMessageDialog(SerialDatastoreViewer.this, "Error!\n" + se.toString(), "GATE", JOptionPane.ERROR_MESSAGE);
                            se.printStackTrace(Err.getPrintWriter());
                        }
                    }
                }
            }
        }

        /**
     * The path where the mouse click occurred.
     */
        TreePath location;

        public TreePath getLocation() {
            return location;
        }

        public void setLocation(TreePath location) {
            this.location = location;
        }
    }

    /**
   * Action to load all selected resources.
   */
    class LoadAction extends AbstractAction {

        public LoadAction() {
            super("Load");
        }

        public void actionPerformed(ActionEvent e) {
            Runnable runner = new Runnable() {

                public void run() {
                    TreePath[] selectedPaths = mainTree.getSelectionPaths();
                    if (ignoreSelection) {
                        ignoreSelection = false;
                        selectedPaths = null;
                    }
                    if (selectedPaths == null && location != null) {
                        selectedPaths = new TreePath[] { location };
                        location = null;
                    }
                    if (selectedPaths != null) {
                        for (TreePath aPath : selectedPaths) {
                            Object value = ((DefaultMutableTreeNode) aPath.getLastPathComponent()).getUserObject();
                            if (value instanceof DSEntry) {
                                DSEntry entry = (DSEntry) value;
                                try {
                                    MainFrame.lockGUI("Loading " + entry.name);
                                    long start = System.currentTimeMillis();
                                    fireStatusChanged("Loading " + entry.name);
                                    fireProgressChanged(0);
                                    FeatureMap params = Factory.newFeatureMap();
                                    params.put(DataStore.DATASTORE_FEATURE_NAME, datastore);
                                    params.put(DataStore.LR_ID_FEATURE_NAME, entry.id);
                                    FeatureMap features = Factory.newFeatureMap();
                                    Resource res = Factory.createResource(entry.type, params, features, entry.name);
                                    fireProgressChanged(0);
                                    fireProcessFinished();
                                    long end = System.currentTimeMillis();
                                    fireStatusChanged(entry.name + " loaded in " + NumberFormat.getInstance().format((double) (end - start) / 1000) + " seconds");
                                } catch (ResourceInstantiationException rie) {
                                    MainFrame.unlockGUI();
                                    JOptionPane.showMessageDialog(SerialDatastoreViewer.this, "Error!\n" + rie.toString(), "GATE", JOptionPane.ERROR_MESSAGE);
                                    rie.printStackTrace(Err.getPrintWriter());
                                    fireProgressChanged(0);
                                    fireProcessFinished();
                                } finally {
                                    MainFrame.unlockGUI();
                                }
                            }
                        }
                    }
                }
            };
            Thread thread = new Thread(runner, SerialDatastoreViewer.this.getClass().getCanonicalName() + " DS Loader");
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }

        /**
     * The path where the mouse click occurred.
     */
        protected TreePath location;

        protected boolean ignoreSelection = false;

        public TreePath getLocation() {
            return location;
        }

        public void setLocation(TreePath location) {
            this.location = location;
        }
    }

    static class DSEntry {

        DSEntry(String name, String id, String type) {
            this.name = name;
            this.type = type;
            this.id = id;
        }

        public String toString() {
            return name;
        }

        String name;

        String type;

        String id;
    }

    static class DSType {

        String name, type;

        boolean expanded = false;

        DSType(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String toString() {
            return name;
        }
    }

    DefaultMutableTreeNode treeRoot;

    DefaultTreeModel treeModel;

    JTree mainTree;

    DataStore datastore;

    NameBearerHandle myHandle;

    /**
   * Action used to delete selected resources
   */
    protected DeleteAction deleteAction;

    /**
   * Action object for loading resources.
   */
    protected LoadAction loadAction;

    /**
   * The popup used for actions.
   */
    protected JPopupMenu popup;

    protected FeatureMap features;

    private transient Vector progressListeners;

    private transient Vector statusListeners;

    public void resourceAdopted(DatastoreEvent e) {
    }

    public void resourceDeleted(DatastoreEvent e) {
        String resID = (String) e.getResourceID();
        DefaultMutableTreeNode node = null;
        Enumeration nodesEnum = treeRoot.depthFirstEnumeration();
        boolean found = false;
        while (nodesEnum.hasMoreElements() && !found) {
            node = (DefaultMutableTreeNode) nodesEnum.nextElement();
            Object userObject = node.getUserObject();
            found = userObject instanceof DSEntry && ((DSEntry) userObject).id.equals(resID);
        }
        if (found) {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
            treeModel.removeNodeFromParent(node);
            if (parent.getChildCount() == 0) treeModel.removeNodeFromParent(parent);
        }
    }

    public void resourceWritten(DatastoreEvent e) {
        Resource res = e.getResource();
        String resID = (String) e.getResourceID();
        String resType = ((ResourceData) Gate.getCreoleRegister().get(res.getClass().getName())).getName();
        DefaultMutableTreeNode parent = treeRoot;
        DefaultMutableTreeNode node = null;
        Enumeration childrenEnum = parent.children();
        boolean found = false;
        while (childrenEnum.hasMoreElements() && !found) {
            node = (DefaultMutableTreeNode) childrenEnum.nextElement();
            if (node.getUserObject() instanceof DSType) {
                found = ((DSType) node.getUserObject()).name.equals(resType);
            }
        }
        if (!found) {
            node = new DefaultMutableTreeNode(resType);
            treeModel.insertNodeInto(node, parent, parent.getChildCount());
        }
        if (node.getUserObject() instanceof DSType) {
            if (!((DSType) node.getUserObject()).expanded) return;
        }
        parent = node;
        childrenEnum = parent.children();
        found = false;
        while (childrenEnum.hasMoreElements() && !found) {
            node = (DefaultMutableTreeNode) childrenEnum.nextElement();
            found = ((DSEntry) node.getUserObject()).id.equals(resID);
        }
        if (!found) {
            try {
                DSEntry entry = new DSEntry(datastore.getLrName(resID), resID, res.getClass().getName());
                node = new DefaultMutableTreeNode(entry, false);
                treeModel.insertNodeInto(node, parent, parent.getChildCount());
            } catch (PersistenceException pe) {
                pe.printStackTrace(Err.getPrintWriter());
            }
        }
    }
}
