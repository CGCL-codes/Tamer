package org.wilmascope.view;

import java.util.*;
import java.io.*;
import javax.swing.ImageIcon;

/**
 * This class manages a 'registry' of view object prototypes for
 * nodes and edges.  It implements the 'Singleton' pattern to ensure that
 * only one instance is available to the System, a reference to which may
 * be obtained using the {@link #getInstance()} method.
 */
public class ViewManager {

    /**
   * @return a reference to the Singleton instance of this class.
   */
    public static ViewManager getInstance() {
        return instance;
    }

    private ViewManager() {
        nodeReg = new Registry();
        edgeReg = new Registry();
        clusterReg = new Registry();
    }

    public class UnknownViewTypeException extends Exception {

        /**
	 * 
	 */
        private static final long serialVersionUID = 4528164925193220886L;

        public UnknownViewTypeException(String viewType) {
            super("No known view type: " + viewType);
        }
    }

    public class Registry {

        @SuppressWarnings("unchecked")
        public String[] getViewNames() {
            return (String[]) views.keySet().toArray(new String[0]);
        }

        public GraphElementView create(String type) throws UnknownViewTypeException {
            GraphElementView view;
            if ((view = (GraphElementView) views.get(type)) == null) {
                throw new UnknownViewTypeException(type);
            }
            return (GraphElementView) view.clone();
        }

        public GraphElementView getDefaultViewPrototype() throws UnknownViewTypeException {
            if (defaultView == null) {
                throw new UnknownViewTypeException("Default view not set!");
            }
            return (GraphElementView) defaultView;
        }

        public GraphElementView create() throws UnknownViewTypeException {
            if (defaultView == null) {
                throw new UnknownViewTypeException("Default view not set!");
            }
            return (GraphElementView) defaultView.clone();
        }

        public void setDefaultView(String type) throws UnknownViewTypeException {
            if ((defaultView = (GraphElementView) views.get(type)) == null) {
                throw new UnknownViewTypeException(type);
            }
        }

        public ImageIcon getIcon(String type) throws UnknownViewTypeException {
            GraphElementView view;
            if ((view = (GraphElementView) views.get(type)) == null) {
                throw new UnknownViewTypeException(type);
            }
            return view.getIcon();
        }

        public ImageIcon getIcon() throws UnknownViewTypeException {
            if (defaultView == null) {
                throw new UnknownViewTypeException("Default view not set!");
            }
            return defaultView.getIcon();
        }

        @SuppressWarnings("unchecked")
        public void addPrototypeView(GraphElementView prototype) {
            views.put(prototype.getTypeName(), prototype);
        }

        @SuppressWarnings({ "rawtypes" })
        private Hashtable views = new Hashtable();

        private GraphElementView defaultView;
    }

    @SuppressWarnings("rawtypes")
    public void loadViews() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        try {
            String plugins = ViewConstants.getInstance().getProperty("ViewPlugins");
            List<String> classNames = new ArrayList<String>();
            StringTokenizer st = new StringTokenizer(plugins, ",");
            while (st.hasMoreElements()) {
                String el = (String) st.nextElement();
                classNames.add(el);
            }
            String[] list = new String[classNames.size()];
            list = (String[]) classNames.toArray(list);
            for (int i = 0; i < list.length; i++) {
                String name = list[i];
                Class c = Class.forName(name);
                GraphElementView view = (GraphElementView) c.newInstance();
                addPrototypeView(view);
            }
        } catch (NullPointerException e) {
            System.err.println("WARNING: Couldn't load plugins... ");
            System.err.println("check that you've listed the plugin classnames in the properties file");
        }
    }

    public void addPrototypeView(GraphElementView view) {
        if (view instanceof ClusterView) {
            clusterReg.addPrototypeView(view);
        } else if (view instanceof NodeView) {
            nodeReg.addPrototypeView(view);
        } else if (view instanceof EdgeView) {
            edgeReg.addPrototypeView(view);
        }
    }

    public Registry getNodeViewRegistry() {
        return nodeReg;
    }

    public Registry getEdgeViewRegistry() {
        return edgeReg;
    }

    public Registry getClusterViewRegistry() {
        return clusterReg;
    }

    public ClusterView createClusterView(String type) throws UnknownViewTypeException {
        return (ClusterView) clusterReg.create(type);
    }

    public NodeView createNodeView(String type) throws UnknownViewTypeException {
        return (NodeView) nodeReg.create(type);
    }

    public EdgeView createEdgeView(String type) throws UnknownViewTypeException {
        return (EdgeView) edgeReg.create(type);
    }

    public ClusterView createClusterView() throws UnknownViewTypeException {
        return (ClusterView) clusterReg.create();
    }

    public NodeView createNodeView() throws UnknownViewTypeException {
        return (NodeView) nodeReg.create();
    }

    public EdgeView createEdgeView() throws UnknownViewTypeException {
        return (EdgeView) edgeReg.create();
    }

    public String getDefaultEdgeViewType() {
        try {
            return edgeReg.getDefaultViewPrototype().getTypeName();
        } catch (UnknownViewTypeException e) {
            System.out.println(e);
            return null;
        }
    }

    public String getDefaultNodeViewType() {
        try {
            return nodeReg.getDefaultViewPrototype().getTypeName();
        } catch (UnknownViewTypeException e) {
            System.out.println(e);
            return null;
        }
    }

    public String getDefaultClusterViewType() {
        try {
            return clusterReg.getDefaultViewPrototype().getTypeName();
        } catch (UnknownViewTypeException e) {
            System.out.println(e);
            return null;
        }
    }

    private static final ViewManager instance = new ViewManager();

    private Registry nodeReg;

    private Registry edgeReg;

    private Registry clusterReg;
}
