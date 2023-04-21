package org.gdi3d.xnavi.listeners;

/**
 * <p>
 * <b>Title: BusyListener</b>
 * </p>
 * <p>
 * <b>Description:</b> A BusyListener receives events if the target object to
 * which the BusyListener has been registered starts or finishes a certain
 * tasks. For instance the LODThread issues busy events if data is currently
 * loaded or has been finished loading.
 * </p>
 *

 *
 * @author Arne Schilling
 */
public interface BusyListener {

    public void setBusy(boolean state);
}
