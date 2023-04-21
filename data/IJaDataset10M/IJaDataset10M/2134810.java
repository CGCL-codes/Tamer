package org.xmlvm.tutorial.ios.sensor.gyroscope;

import org.xmlvm.iphone.CGRect;
import org.xmlvm.iphone.CMGyroData;
import org.xmlvm.iphone.CMMotionManager;
import org.xmlvm.iphone.CMRotationRate;
import org.xmlvm.iphone.NSError;
import org.xmlvm.iphone.NSTimer;
import org.xmlvm.iphone.UIApplication;
import org.xmlvm.iphone.UIApplicationDelegate;
import org.xmlvm.iphone.UIColor;
import org.xmlvm.iphone.UILabel;
import org.xmlvm.iphone.UIScreen;
import org.xmlvm.iphone.UIWindow;
import org.xmlvm.iphone.NSTimer;
import org.xmlvm.iphone.NSTimerDelegate;
import org.xmlvm.iphone.NSOperationQueue;
import org.xmlvm.iphone.CMGyroHandler;

/**
 * This application demonstrates the use of gyroscope in iOS using the 'pull'
 * method. The gyroscope is available on iPhone4. To access the gyroscope data,
 * we use the Core Motion Framework. The rate of rotation along the 3 axes are
 * updated in the form of <code>CMGyroData</code> object. The GyroData has the
 * <code>rotationRate</code> which has the values of rotation rate along the 3
 * axes. The Gyro updates can be obtained using 'pull' or 'push' method. In this
 * example, we use the 'push' method where the updates are queued in the
 * operation queue everytime there is an update in the gyroscope data.
 */
public class Gyroscope extends UIApplicationDelegate {

    private UILabel labelX;

    private UILabel labelY;

    private UILabel labelZ;

    CMMotionManager motionManager;

    @Override
    public void applicationDidFinishLaunching(UIApplication app) {
        CGRect rect = UIScreen.mainScreen().getApplicationFrame();
        UIWindow window = new UIWindow(rect);
        window.setBackgroundColor(UIColor.whiteColor);
        UILabel label = new UILabel(new CGRect(20, 20, 30, 30));
        label.setText("x:");
        window.addSubview(label);
        labelX = new UILabel(new CGRect(50, 20, 250, 30));
        labelX.setText("-");
        window.addSubview(labelX);
        label = new UILabel(new CGRect(20, 50, 30, 30));
        label.setText("y:");
        window.addSubview(label);
        labelY = new UILabel(new CGRect(50, 50, 250, 30));
        labelY.setText("-");
        window.addSubview(labelY);
        label = new UILabel(new CGRect(20, 80, 30, 30));
        label.setText("z:");
        window.addSubview(label);
        labelZ = new UILabel(new CGRect(50, 80, 250, 30));
        labelZ.setText("-");
        window.addSubview(labelZ);
        motionManager = new CMMotionManager();
        motionManager.setGyroUpdateInterval(1.0 / 2.0);
        if (motionManager.isGyroAvailable()) {
            motionManager.startGyroUpdatesToQueue(NSOperationQueue.getCurrentQueue(), new CMGyroHandler() {

                @Override
                public void run(CMGyroData gyroData, NSError error) {
                    CMRotationRate rotationData = gyroData.rotationRate();
                    labelX.setText("" + rotationData.x);
                    labelY.setText("" + rotationData.y);
                    labelZ.setText("" + rotationData.z);
                }
            });
        }
        window.makeKeyAndVisible();
    }

    @Override
    public void applicationWillTerminate(UIApplication app) {
        motionManager.stopGyroUpdates();
        motionManager.release();
    }

    public static void main(String[] args) {
        UIApplication.main(args, null, Gyroscope.class);
    }
}
