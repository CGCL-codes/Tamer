package org.jhotdraw.app.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.jhotdraw.app.Application;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * ClearRecentFilesAction.
 *
 * @author Werner Randelshofer.
 * @version 1.0 June 15, 2006 Created.
 */
public class ClearRecentFilesAction extends AbstractApplicationAction {

    public static final String ID = "clearRecentFiles";

    private PropertyChangeListener applicationListener;

    /** Creates a new instance. */
    public ClearRecentFilesAction(Application app) {
        super(app);
        ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, "clearMenu");
        updateEnabled();
    }

    /**
     * Installs listeners on the application object.
     */
    @Override
    protected void installApplicationListeners(Application app) {
        super.installApplicationListeners(app);
        if (applicationListener == null) {
            applicationListener = createApplicationListener();
        }
        app.addPropertyChangeListener(applicationListener);
    }

    private PropertyChangeListener createApplicationListener() {
        return new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName() == "recentFiles") {
                    updateEnabled();
                }
            }
        };
    }

    /**
     * Installs listeners on the application object.
     */
    @Override
    protected void uninstallApplicationListeners(Application app) {
        super.uninstallApplicationListeners(app);
        app.removePropertyChangeListener(applicationListener);
    }

    public void actionPerformed(ActionEvent e) {
        getApplication().clearRecentFiles();
    }

    private void updateEnabled() {
        setEnabled(getApplication().recentFiles().size() > 0);
    }
}
