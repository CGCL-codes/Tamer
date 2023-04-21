package org.web3d.x3d.xj3d.viewer;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import org.openide.ErrorManager;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.web3d.x3d.X3DDataObject;

/**
 * Xj3dViewerTopComponent.java
 * Created on Sep 14, 2007, 9:57 AM
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey
 * @version $Id$
 */
public final class Xj3dViewerTopComponent extends TopComponent {

    private static Xj3dViewerTopComponent instance;

    private static final String PREFERRED_ID = "Xj3dViewerTopComponent";

    private Xj3dViewerPanel xj3dPanel;

    private Xj3dViewerTopComponent() {
        setName(NbBundle.getMessage(Xj3dViewerTopComponent.class, "CTL_Xj3dViewerTopComponent"));
        setToolTipText(NbBundle.getMessage(Xj3dViewerTopComponent.class, "HINT_Xj3dViewerTopComponent"));
        setLayout(new BorderLayout());
        Xj3dViewerPanel xj = new Xj3dViewerPanel();
        xj.initialize();
        add(xj3dPanel = xj, BorderLayout.CENTER);
    }

    /**
   * Gets default instance. Do not use directly: reserved for *.settings files only,
   * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
   * To obtain the singleton instance, use {@link findInstance}.
   */
    public static synchronized Xj3dViewerTopComponent getDefault() {
        if (instance == null) {
            instance = new Xj3dViewerTopComponent();
        }
        return instance;
    }

    /**
   * Obtain the Xj3dViewerTopComponent instance. Never call {@link #getDefault} directly!
   */
    public static synchronized Xj3dViewerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            ErrorManager.getDefault().log(ErrorManager.WARNING, "' ID. That is a potential source of errors and unexpected behavior.");
            return getDefault();
        }
        if (win instanceof Xj3dViewerTopComponent) {
            return (Xj3dViewerTopComponent) win;
        }
        ErrorManager.getDefault().log(ErrorManager.WARNING, "There seem to be multiple components with the \'" + PREFERRED_ID + "\' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public void open() {
        Mode m = WindowManager.getDefault().findMode("Xj3dViewerMode");
        if (m != null) m.dockInto(this);
        super.open();
    }

    public Xj3dViewerPanel getViewerPanel() {
        return xj3dPanel;
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    private Lookup.Result result = null;

    private topComponentPropListener propListener = null;

    @SuppressWarnings("unchecked")
    @Override
    public void componentOpened() {
        Lookup.Template tpl = new Lookup.Template(X3DDataObject.class);
        result = Utilities.actionsGlobalContext().lookup(tpl);
        TopComponent.getRegistry().addPropertyChangeListener(propListener = new topComponentPropListener());
    }

    class topComponentPropListener implements PropertyChangeListener {

        @SuppressWarnings("unchecked")
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
        }
    }

    @Override
    public void componentClosed() {
        result = null;
        TopComponent.getRegistry().removePropertyChangeListener(propListener);
        propListener = null;
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
            return Xj3dViewerTopComponent.getDefault();
        }
    }
}
