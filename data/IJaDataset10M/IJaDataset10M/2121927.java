package android.internal;

import android.app.Application;
import org.xmlvm.iphone.UIApplication;
import org.xmlvm.iphone.UIApplicationDelegate;

/**
 * Wiring code for launching an Android Activity inside the iPhone simulator
 */
public class AndroidAppLauncher extends UIApplicationDelegate {

    private static Application app = null;

    @Override
    public void applicationDidFinishLaunching(UIApplication iphone_app) {
        getApplication().onCreate();
    }

    @Override
    public void applicationWillTerminate(UIApplication iphone_app) {
        getApplication().onTerminate();
    }

    @Override
    public void applicationDidReceiveMemoryWarning(UIApplication iphone_app) {
        getApplication().onLowMemory();
    }

    @Override
    public void applicationDidBecomeActive(UIApplication iphone_app) {
        getApplication().onRestart();
    }

    @Override
    public void applicationWillResignActive(UIApplication iphone_app) {
        getApplication().onStop();
    }

    public static void main(String[] args) {
        UIApplication.main(null, null, AndroidAppLauncher.class);
    }

    public static Application getApplication() {
        if (app == null) {
            app = new Application();
        }
        return app;
    }
}
