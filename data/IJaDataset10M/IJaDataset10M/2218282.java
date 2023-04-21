package com.codename1.capture;

import com.codename1.ui.Display;
import com.codename1.ui.events.ActionListener;

/**
 * This is the main class for capturing media files from the device.
 * Use this class to invoke the native camera to capture images, audio or video
 * @author Chen
 */
public class Capture {

    /**
     * This method tries to invoke the device native camera to capture images.
     * The method returns immediately and the response will be sent asynchronously
     * to the given ActionListener Object
     * The image is saved as a jpeg to a file on the device.
     * 
     * use this in the actionPerformed to retrieve the file path
     * String path = (String) evt.getSource();
     * 
     * @param response a callback Object to retrieve the file path
     * @throws RuntimeException if this feature failed or unsupported on the platform
     */
    public static void capturePhoto(ActionListener response) {
        Display.getInstance().capturePhoto(response);
    }

    /**
     * This method tries to invoke the device native hardware to capture audio.
     * The method returns immediately and the response will be sent asynchronously
     * to the given ActionListener Object
     * The audio is saved to a file on the device.
     * 
     * use this in the actionPerformed to retrieve the file path
     * String path = (String) evt.getSource();
     * 
     * @param response a callback Object to retrieve the file path
     * @throws RuntimeException if this feature failed or unsupported on the platform
     */
    public static void captureAudio(ActionListener response) {
        Display.getInstance().captureAudio(response);
    }

    /**
     * This method tries to invoke the device native camera to capture video.
     * The method returns immediately and the response will be sent asynchronously
     * to the given ActionListener Object
     * The video is saved to a file on the device.
     * 
     * use this in the actionPerformed to retrieve the file path
     * String path = (String) evt.getSource();
     * 
     * @param response a callback Object to retrieve the file path
     * @throws RuntimeException if this feature failed or unsupported on the platform
     */
    public static void captureVideo(ActionListener response) {
        Display.getInstance().captureVideo(response);
    }
}
