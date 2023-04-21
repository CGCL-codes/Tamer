package com.android.mediaframeworktest.unit;

import android.media.MediaRecorder;

/**
 * All MediaRecorder method unit test subclass must implement this interface. 
 */
interface MediaRecorderMethodUnderTest {

    public void checkStateErrors(MediaRecorderStateErrors stateErrors);

    public void invokeMethodUnderTest(MediaRecorder player);
}
