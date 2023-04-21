package com.limegroup.gnutella.gui.options.panes;

import java.io.IOException;
import javax.swing.JCheckBox;
import org.limewire.i18n.I18nMarker;
import com.limegroup.gnutella.gui.I18n;
import com.limegroup.gnutella.gui.LabeledComponent;
import com.limegroup.gnutella.settings.UISettings;

public final class NotificationsPaneItem extends AbstractPaneItem {

    public static final String TITLE = I18n.tr("Notifications");

    public static final String LABEL = I18n.tr("FrostWire can display popups to notify you when certain things happen, such as a download completing. ");

    private final String SHOW_NOTIFICATIONS_LABEL = I18nMarker.marktr("Show Notifications:");

    private final JCheckBox SHOW_NOTIFICATIONS_CHECK_BOX = new JCheckBox();

    public NotificationsPaneItem() {
        super(TITLE, LABEL);
        LabeledComponent comp = new LabeledComponent(SHOW_NOTIFICATIONS_LABEL, SHOW_NOTIFICATIONS_CHECK_BOX, LabeledComponent.LEFT_GLUE, LabeledComponent.LEFT);
        add(comp.getComponent());
    }

    public void initOptions() {
        SHOW_NOTIFICATIONS_CHECK_BOX.setSelected(UISettings.SHOW_NOTIFICATIONS.getValue());
    }

    public boolean applyOptions() throws IOException {
        UISettings.SHOW_NOTIFICATIONS.setValue(SHOW_NOTIFICATIONS_CHECK_BOX.isSelected());
        return false;
    }

    public boolean isDirty() {
        return SHOW_NOTIFICATIONS_CHECK_BOX.isSelected() != UISettings.SHOW_NOTIFICATIONS.getValue();
    }
}
