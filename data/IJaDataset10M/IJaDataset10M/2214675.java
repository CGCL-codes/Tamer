package com.android.camera.power;

import com.android.camera.Camera;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.KeyEvent;
import android.content.Intent;

/**
 * Junit / Instrumentation test case for camera power measurement
 *
 * Running the test suite:
 *
 * adb shell am instrument \
 *    -e com.android.camera.power.ImageAndVideoCapture \
 *    -w com.android.camera.tests/android.test.InstrumentationTestRunner
 *
 */
public class ImageAndVideoCapture extends ActivityInstrumentationTestCase2<Camera> {

    private String TAG = "ImageAndVideoCapture";

    private static final int TOTAL_NUMBER_OF_IMAGECAPTURE = 5;

    private static final int TOTAL_NUMBER_OF_VIDEOCAPTURE = 5;

    private static final long WAIT_FOR_IMAGE_CAPTURE_TO_BE_TAKEN = 1500;

    private static final long WAIT_FOR_VIDEO_CAPTURE_TO_BE_TAKEN = 10000;

    private static final long WAIT_FOR_PREVIEW = 1500;

    private static final long WAIT_FOR_STABLE_STATE = 2000;

    public ImageAndVideoCapture() {
        super("com.android.camera", Camera.class);
    }

    @Override
    protected void setUp() throws Exception {
        getActivity();
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @LargeTest
    public void testLaunchCamera() {
        try {
            Thread.sleep(WAIT_FOR_STABLE_STATE);
        } catch (Exception e) {
            Log.v(TAG, e.toString());
            assertTrue("testImageCaptureDoNothing", false);
        }
        assertTrue("testImageCaptureDoNothing", true);
    }

    @LargeTest
    public void testCapture5Image() {
        Instrumentation inst = getInstrumentation();
        try {
            for (int i = 0; i < TOTAL_NUMBER_OF_IMAGECAPTURE; i++) {
                Thread.sleep(WAIT_FOR_IMAGE_CAPTURE_TO_BE_TAKEN);
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_UP);
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
                Thread.sleep(WAIT_FOR_IMAGE_CAPTURE_TO_BE_TAKEN);
            }
            Thread.sleep(WAIT_FOR_STABLE_STATE);
        } catch (Exception e) {
            Log.v(TAG, e.toString());
            assertTrue("testImageCapture", false);
        }
        assertTrue("testImageCapture", true);
    }

    @LargeTest
    public void testCapture5Videos() {
        Instrumentation inst = getInstrumentation();
        try {
            Intent intent = new Intent();
            intent.setClassName("com.android.camera", "com.android.camera.VideoCamera");
            getActivity().startActivity(intent);
            for (int i = 0; i < TOTAL_NUMBER_OF_VIDEOCAPTURE; i++) {
                Thread.sleep(WAIT_FOR_PREVIEW);
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
                Thread.sleep(WAIT_FOR_VIDEO_CAPTURE_TO_BE_TAKEN);
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
                Thread.sleep(WAIT_FOR_PREVIEW);
            }
            Thread.sleep(WAIT_FOR_STABLE_STATE);
        } catch (Exception e) {
            Log.v(TAG, e.toString());
            assertTrue("testVideoCapture", false);
        }
        assertTrue("testVideoCapture", true);
    }
}
