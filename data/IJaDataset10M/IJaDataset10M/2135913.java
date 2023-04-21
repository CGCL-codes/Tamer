package org.base.apps.api.events;

/**
 * 
 *
 * @author Kevan Simpson
 */
public interface AppEventEmitter {

    public void addAppEventListener(AppEventListener lsnr);

    public void addAppEventListener(String appName, AppEventListener lsnr);

    public void fireAppEvent(AppEvent evt);

    public AppEventListener[] getAppEventListeners();

    public AppEventListener[] getAppEventListeners(String appName);

    public void removeAppEventListener(AppEventListener lsnr);

    public void removeAppEventListener(String appName, AppEventListener lsnr);
}
