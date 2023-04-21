package net.sf.dpdesktop.gui.tray;

import net.sf.dpdesktop.gui.state.ApplicationStateListener;
import net.sf.dpdesktop.gui.state.ApplicationStateModel;
import net.sf.dpdesktop.service.TrackingListener;
import net.sf.dpdesktop.service.TrackingManager;

/**
 *
 * @author Heiner Reinhardt
 */
public interface Tray {

    public void setApplicationStateModel(ApplicationStateModel m);

    public void setApplicationStateListener(ApplicationStateListener l);

    public void addTrackingListener(TrackingListener l);

    public void setTrackingManager(TrackingManager trackingManager);
}
