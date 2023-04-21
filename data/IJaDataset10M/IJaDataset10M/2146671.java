package net.sf.amemailchecker.gui.settings;

import net.sf.amemailchecker.app.extension.ExtensionSettingsChangeStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ExtensionModelChangeListener implements PropertyChangeListener {

    private SettingsMediator mediator;

    public ExtensionModelChangeListener(SettingsMediator mediator) {
        this.mediator = mediator;
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(ExtensionSettingsChangeStateListener.PROPERTY_STATE_CHANGED)) {
            mediator.changeApplyState(true);
        }
    }
}
