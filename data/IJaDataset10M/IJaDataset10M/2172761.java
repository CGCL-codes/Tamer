package com.android.ide.eclipse.adt.internal.launch;

import com.android.ddmlib.IDevice;
import com.android.ide.eclipse.adt.AdtPlugin;
import java.io.IOException;

/**
 * Launches the given activity
 */
public class ActivityLaunchAction implements IAndroidLaunchAction {

    private final String mActivity;

    private final ILaunchController mLaunchController;

    /**
     * Creates a ActivityLaunchAction
     *
     * @param activity fully qualified activity name to launch
     * @param controller the {@link ILaunchController} that performs launch
     */
    public ActivityLaunchAction(String activity, ILaunchController controller) {
        mActivity = activity;
        mLaunchController = controller;
    }

    /**
     * Launches the activity on targeted device
     *
     * @param info the {@link DelayedLaunchInfo} that contains launch details
     * @param device the Android device to perform action on
     *
     * @see IAndroidLaunchAction#doLaunchAction(DelayedLaunchInfo, IDevice)
     */
    public boolean doLaunchAction(DelayedLaunchInfo info, IDevice device) {
        try {
            String msg = String.format("Starting activity %1$s on device ", mActivity, device);
            AdtPlugin.printToConsole(info.getProject(), msg);
            info.incrementAttemptCount();
            device.executeShellCommand("am start" + (info.isDebugMode() ? " -D" : "") + " -n " + info.getPackageName() + "/" + mActivity.replaceAll("\\$", "\\\\\\$") + " -a android.intent.action.MAIN" + " -c android.intent.category.LAUNCHER", new AMReceiver(info, device, mLaunchController));
            if (info.isDebugMode() == false) {
                return false;
            }
        } catch (IOException e) {
            AdtPlugin.printErrorToConsole(info.getProject(), String.format("Launch error: %s", e.getMessage()));
            return false;
        }
        return true;
    }

    /**
     * Returns a description of the activity being launched
     *
     * @see IAndroidLaunchAction#getLaunchDescription()
     */
    public String getLaunchDescription() {
        return String.format("%1$s activity launch", mActivity);
    }
}
