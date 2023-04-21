package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionScheduledSync extends AbstractAction {

    private int width;

    private int height;

    public ActionScheduledSync() {
        this(32, 32);
    }

    public ActionScheduledSync(int width, int height) {
        super(Translations.getString("action.scheduled_sync"));
        this.putValue(SHORT_DESCRIPTION, Translations.getString("action.scheduled_sync"));
        this.width = width;
        this.height = height;
        this.updateIcon();
        Main.getUserSettings().addPropertyChangeListener("synchronizer.scheduler_enabled", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                ActionScheduledSync.this.updateIcon();
            }
        });
    }

    private void updateIcon() {
        if (Main.getUserSettings().getBooleanProperty("synchronizer.scheduler_enabled")) this.putValue(SMALL_ICON, ImageUtils.getResourceImage("synchronize_play.png", this.width, this.height)); else this.putValue(SMALL_ICON, ImageUtils.getResourceImage("synchronize_pause.png", this.width, this.height));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        ActionScheduledSync.scheduledSync();
    }

    public static void scheduledSync() {
        Main.getUserSettings().setBooleanProperty("synchronizer.scheduler_enabled", !Main.getUserSettings().getBooleanProperty("synchronizer.scheduler_enabled"));
    }
}
