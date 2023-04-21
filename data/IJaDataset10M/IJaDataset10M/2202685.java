package org.openthinclient.console;

import java.awt.BorderLayout;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;
import org.openide.ErrorManager;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

public final class DetailViewTopComponent extends TopComponent {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private class MyNodeListener implements NodeListener {

        public void childrenAdded(NodeMemberEvent arg0) {
            updateDetailView(currentDetailViewProvider, lastTopComponent.getActivatedNodes(), lastTopComponent);
        }

        public void childrenRemoved(NodeMemberEvent arg0) {
            updateDetailView(currentDetailViewProvider, lastTopComponent.getActivatedNodes(), lastTopComponent);
        }

        public void childrenReordered(NodeReorderEvent arg0) {
            updateDetailView(currentDetailViewProvider, lastTopComponent.getActivatedNodes(), lastTopComponent);
        }

        public void nodeDestroyed(NodeEvent arg0) {
            detachDetailView();
            revalidate();
            repaint();
        }

        public void propertyChange(PropertyChangeEvent evt) {
            updateDetailView(currentDetailViewProvider, lastTopComponent.getActivatedNodes(), lastTopComponent);
        }
    }

    /**
	 * 
	 */
    private static final int MAX_TITLE_LENGTH = 60;

    private static DetailViewTopComponent instance;

    private final PropertyChangeListener propertyChangeListener;

    private final MyNodeListener listener = new MyNodeListener();

    private TopComponent lastTopComponent;

    private DetailViewProvider currentDetailViewProvider;

    private DetailViewTopComponent() {
        setName(Messages.getString("DetailViewTopComponent.name"));
        final Image loadImage = Utilities.loadImage("org/openthinclient/console/rss16.gif", true);
        setIcon(loadImage);
        setLayout(new BorderLayout());
        setBorder(null);
        propertyChangeListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(Registry.PROP_CURRENT_NODES)) {
                    final Object src = evt.getSource();
                    nodeSelectionChanged(evt.getNewValue(), src instanceof Registry ? ((Registry) src).getActivated() : null);
                }
            }
        };
    }

    @Override
    protected void componentOpened() {
        getRegistry().addPropertyChangeListener(propertyChangeListener);
    }

    @Override
    protected void componentClosed() {
        super.componentClosed();
        if (null != propertyChangeListener) getRegistry().removePropertyChangeListener(propertyChangeListener);
    }

    /**
	 * @param newValue
	 * @param topComponent
	 */
    protected void nodeSelectionChanged(Object newValue, TopComponent topComponent) {
        this.lastTopComponent = topComponent;
        if (newValue == null || topComponent != null) newValue = topComponent.getActivatedNodes();
        if (newValue != null && newValue instanceof Node[] && ((Node[]) newValue).length > 0) {
            final Node[] selection = (Node[]) newValue;
            DetailViewProvider dvp = null;
            for (final Node node : selection) if (node instanceof DetailViewProvider) {
                dvp = (DetailViewProvider) node;
                break;
            }
            if (null != dvp) {
                setTitle((Node) dvp);
                updateDetailView(dvp, selection, topComponent);
            }
        }
    }

    /**
	 * @param node
	 */
    private void setTitle(Node node) {
        final StringBuffer sb = new StringBuffer(node.getDisplayName());
        Node parent = node.getParentNode();
        while (null != parent) {
            sb.insert(0, " > ");
            sb.insert(0, parent.getDisplayName());
            parent = parent.getParentNode();
        }
        if (sb.length() > MAX_TITLE_LENGTH) {
            int idx = -1, nextIdx = sb.length();
            while (sb.length() - nextIdx < MAX_TITLE_LENGTH && nextIdx > 0) {
                idx = nextIdx;
                nextIdx = sb.lastIndexOf(">", idx - 1);
            }
            if (idx < 0) idx = sb.length() - MAX_TITLE_LENGTH;
            sb.replace(0, idx + 1, "...");
        }
        setName(sb.toString());
    }

    /**
	 * @param dvp
	 * @param selection
	 * @param topComponent
	 */
    private void updateDetailView(DetailViewProvider dvp, Node[] selection, TopComponent topComponent) {
        ConsoleFrame.getINSTANCE().hideObjectDetails();
        detachDetailView();
        if (null == dvp || 0 == selection.length) return;
        this.currentDetailViewProvider = dvp;
        try {
            final DetailView detailView = dvp.getDetailView();
            detailView.init(selection, topComponent);
            if (dvp instanceof Node) ((Node) dvp).addNodeListener(listener);
            reloadDetailView(detailView);
        } catch (final RuntimeException e) {
            ErrorManager.getDefault().notify(e);
        }
    }

    private void detachDetailView() {
        if (null != currentDetailViewProvider && currentDetailViewProvider instanceof Node) ((Node) currentDetailViewProvider).removeNodeListener(listener);
        currentDetailViewProvider = null;
        removeAll();
    }

    /**
	 * Reloads the given DetailView (revalidate() and repaint())
	 * 
	 * @param detailView
	 */
    private void reloadDetailView(DetailView detailView) {
        removeAll();
        final JComponent headerComponent = detailView.getHeaderComponent();
        if (null != headerComponent) {
            final JXPanel p = new JXPanel(new BorderLayout());
            final BasicGradientPainter gradient = new BasicGradientPainter(BasicGradientPainter.GRAY);
            p.setBackgroundPainter(gradient);
            p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, getBackground().darker()));
            headerComponent.setOpaque(false);
            p.add(headerComponent, BorderLayout.CENTER);
            add(p, BorderLayout.NORTH);
        }
        final JComponent mainComponent = detailView.getMainComponent();
        if (null != mainComponent) add(mainComponent, BorderLayout.CENTER);
        final JComponent footerComponent = detailView.getFooterComponent();
        if (footerComponent != null) add(footerComponent, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    public static synchronized DetailViewTopComponent getDefault() {
        if (instance == null) instance = new DetailViewTopComponent();
        return instance;
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    protected String preferredID() {
        return "MainTreeTopComponent";
    }

    @Override
    protected Object writeReplace() {
        return new ResolvableHelper();
    }

    private static final class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return DetailViewTopComponent.getDefault();
        }
    }

    @Override
    public boolean canClose() {
        return false;
    }
}
