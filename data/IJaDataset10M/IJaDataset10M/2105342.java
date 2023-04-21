package com.android.framework.permission.tests;

import junit.framework.TestCase;
import android.os.Binder;
import android.os.IHardwareService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Verify that Hardware apis cannot be called without required permissions.
 */
@SmallTest
public class HardwareServicePermissionTest extends TestCase {

    private IHardwareService mHardwareService;

    @Override
    protected void setUp() throws Exception {
        mHardwareService = IHardwareService.Stub.asInterface(ServiceManager.getService("hardware"));
    }

    /**
     * Test that calling {@link android.os.IHardwareService#vibrate(long)} requires permissions.
     * <p>Tests permission:
     *   {@link android.Manifest.permission#VIBRATE}
     * @throws RemoteException
     */
    public void testVibrate() throws RemoteException {
        try {
            mHardwareService.vibrate(2000, new Binder());
            fail("vibrate did not throw SecurityException as expected");
        } catch (SecurityException e) {
        }
    }

    /**
     * Test that calling {@link android.os.IHardwareService#vibratePattern(long[],
     * int, android.os.IBinder)} requires permissions.
     * <p>Tests permission:
     *   {@link android.Manifest.permission#VIBRATE}
     * @throws RemoteException
     */
    public void testVibratePattern() throws RemoteException {
        try {
            mHardwareService.vibratePattern(new long[] { 0 }, 0, new Binder());
            fail("vibratePattern did not throw SecurityException as expected");
        } catch (SecurityException e) {
        }
    }

    /**
     * Test that calling {@link android.os.IHardwareService#cancelVibrate()} requires permissions.
     * <p>Tests permission:
     *   {@link android.Manifest.permission#VIBRATE}
     * @throws RemoteException
     */
    public void testCancelVibrate() throws RemoteException {
        try {
            mHardwareService.cancelVibrate(new Binder());
            fail("cancelVibrate did not throw SecurityException as expected");
        } catch (SecurityException e) {
        }
    }

    /**
     * Test that calling {@link android.os.IHardwareService#setFlashlightEnabled(boolean)}
     * requires permissions.
     * <p>Tests permissions:
     *   {@link android.Manifest.permission#HARDWARE_TEST}
     *   {@link android.Manifest.permission#FLASHLIGHT}
     * @throws RemoteException
     */
    public void testSetFlashlightEnabled() throws RemoteException {
        try {
            mHardwareService.setFlashlightEnabled(true);
            fail("setFlashlightEnabled did not throw SecurityException as expected");
        } catch (SecurityException e) {
        }
    }

    /**
     * Test that calling {@link android.os.IHardwareService#enableCameraFlash(int)} requires
     * permissions.
     * <p>Tests permission:
     *   {@link android.Manifest.permission#HARDWARE_TEST}
     *   {@link android.Manifest.permission#CAMERA}
     * @throws RemoteException
     */
    public void testEnableCameraFlash() throws RemoteException {
        try {
            mHardwareService.enableCameraFlash(100);
            fail("enableCameraFlash did not throw SecurityException as expected");
        } catch (SecurityException e) {
        }
    }
}
