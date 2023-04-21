package org.xmlvm.iphone.demo.gl.nehelesson4;

import org.xmlvm.iphone.UIApplication;
import org.xmlvm.iphone.UIApplicationDelegate;
import org.xmlvm.iphone.UIScreen;
import org.xmlvm.iphone.UIWindow;

public class NeHeLesson4 extends UIApplicationDelegate {

    UIWindow window;

    GLView mainView;

    @Override
    public void applicationDidFinishLaunching(UIApplication application) {
        application.setStatusBarHidden(true);
        UIScreen screen = UIScreen.mainScreen();
        window = new UIWindow(screen.getApplicationFrame());
        mainView = new NeHeLesson4View(screen.getApplicationFrame());
        window.addSubview(mainView);
        window.makeKeyAndVisible();
    }

    public static void main(String[] argv) {
        System.setProperty("xmlvm.gl", "true");
        UIApplication.main(argv, null, NeHeLesson4.class);
    }
}
