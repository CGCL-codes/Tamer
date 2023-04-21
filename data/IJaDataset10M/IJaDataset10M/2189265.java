package com.hifi.plugin.ui.core.modules.preferences.panes;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import javax.swing.JComponent;
import com.hifi.core.preferences.PreferenceObj;
import com.hifi.core.preferences.PreferenceStateManager;

public abstract class AbstractOptionPane implements OptionPane {

    private static final Logger logger = Logger.getLogger(AbstractOptionPane.class.getName());

    private TitledPaddedPanel pane;

    protected AbstractOptionPane(String title) {
        pane = new TitledPaddedPanel();
        pane.setTitle(title);
    }

    protected void add(JComponent item) {
        pane.add(item);
    }

    @Override
    public JComponent getComponent() {
        return pane;
    }

    protected void savePref(PreferenceObj obj) throws ClassNotFoundException, BackingStoreException, IOException {
        PreferenceStateManager.getInstance().storeObject(obj.getClass(), obj);
    }

    public abstract void save() throws ClassNotFoundException, BackingStoreException, IOException;

    public Object getState() {
        try {
            Object state = PreferenceStateManager.getInstance().getObject(this.getClass());
            return state;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not get preference state for: " + this.getClass().getCanonicalName());
            return null;
        }
    }
}
