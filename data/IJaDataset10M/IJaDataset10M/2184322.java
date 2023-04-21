package android.permission.cts;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.PowerManager;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Verify the Wake Lock related operations require specific permissions.
 */
public class NoWakeLockPermissionTest extends AndroidTestCase {

    private PowerManager mPowerManager;

    private PowerManager.WakeLock mWakeLock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mPowerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "tag");
    }

    /**
     * Verify that WifiManager.WifiLock.acquire() requires permissions.
     * <p>Requires Permission:
     *   {@link android.Manifest.permission#WAKE_LOCK}.
     */
    @SmallTest
    public void testWifiLockAcquire() {
        final WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        final WifiLock wifiLock = wifiManager.createWifiLock("WakeLockPermissionTest");
        try {
            wifiLock.acquire();
            fail("WifiManager.WifiLock.acquire() didn't throw SecurityException as expected");
        } catch (SecurityException e) {
        }
    }

    /**
     * Verify that MediaPlayer.start() requires permissions.
     * <p>Requires Permission:
     *   {@link android.Manifest.permission#WAKE_LOCK}.
     */
    @SmallTest
    public void testMediaPlayerWakeLock() {
        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(mContext, PowerManager.FULL_WAKE_LOCK);
        try {
            mediaPlayer.start();
            fail("MediaPlayer.setWakeMode() did not throw SecurityException as expected");
        } catch (SecurityException e) {
        }
        mediaPlayer.stop();
    }

    /**
     * Verify that PowerManager.WakeLock.acquire() requires permissions.
     * <p>Requires Permission:
     *   {@link android.Manifest.permission#WAKE_LOCK}.
     */
    @SmallTest
    public void testPowerManagerWakeLockAcquire() {
        try {
            mWakeLock.acquire();
            fail("MediaPlayer.setWakeMode() did not throw SecurityException as expected");
        } catch (SecurityException e) {
        }
    }

    /**
     * Verify that PowerManager.WakeLock.acquire(long) requires permissions.
     * <p>Requires Permission:
     *   {@link android.Manifest.permission#WAKE_LOCK}.
     */
    @SmallTest
    public void testPowerManagerWakeLockAcquire2() {
        try {
            mWakeLock.acquire(1);
            fail("MediaPlayer.setWakeMode(long) did not throw SecurityException as expected");
        } catch (SecurityException e) {
        }
    }

    /**
     * Verify that PowerManager.WakeLock.release() requires permissions.
     * <p>Requires Permission:
     *   {@link android.Manifest.permission#WAKE_LOCK}.
     */
    @SmallTest
    public void testPowerManagerWakeLockRelease() {
        mWakeLock.setReferenceCounted(false);
        try {
            mWakeLock.release();
            fail("MediaPlayer.setWakeMode(long) did not throw SecurityException as expected");
        } catch (SecurityException e) {
        }
    }
}
