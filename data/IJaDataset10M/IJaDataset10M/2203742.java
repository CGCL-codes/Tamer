package org.semtinel.core.overview;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.swing.ActionMap;
import javax.swing.JButton;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 * Top component which displays something.
 */
final class CoreOverviewTopComponent extends TopComponent implements ExplorerManager.Provider {

    private static CoreOverviewTopComponent instance;

    private static final String PREFERRED_ID = "CoreOverviewTopComponent";

    private ExplorerManager explorerManager;

    private Lookup lookup;

    private CoreOverviewTopComponent() {
        initComponents();
        BeanTreeView view = new BeanTreeView();
        view.setRootVisible(false);
        add(view, BorderLayout.CENTER);
        JButton refreshButton = new JButton();
        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                explorerManager.setRootContext(new AbstractNode(new OverviewChildren()));
            }
        });
        add(refreshButton, BorderLayout.NORTH);
        setName(NbBundle.getMessage(CoreOverviewTopComponent.class, "CTL_CoreOverviewTopComponent"));
        setToolTipText(NbBundle.getMessage(CoreOverviewTopComponent.class, "HINT_CoreOverviewTopComponent"));
        this.explorerManager = new ExplorerManager();
        this.lookup = ExplorerUtils.createLookup(explorerManager, getActionMap());
        associateLookup(lookup);
        Node root = new AbstractNode(new OverviewChildren());
        root.setDisplayName("Semtinel");
        explorerManager.setRootContext(root);
    }

    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
    }

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized CoreOverviewTopComponent getDefault() {
        if (instance == null) {
            instance = new CoreOverviewTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the CoreOverviewTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized CoreOverviewTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(CoreOverviewTopComponent.class.getName()).warning("Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof CoreOverviewTopComponent) {
            return (CoreOverviewTopComponent) win;
        }
        Logger.getLogger(CoreOverviewTopComponent.class.getName()).warning("There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    static final class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return CoreOverviewTopComponent.getDefault();
        }
    }
}
