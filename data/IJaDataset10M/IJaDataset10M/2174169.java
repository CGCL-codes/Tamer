package net.sf.jlibdc1394.config;

import java.awt.Dimension;
import java.awt.Rectangle;
import net.sf.jlibdc1394.Camera;
import net.sf.jlibdc1394.CameraException;
import net.sf.jlibdc1394.event.VideoModeAdapter;
import net.sf.jlibdc1394.event.VideoModeListener;

/**
 * This proxy is used in an environment with multiple cameras and
 * possible multiple configurations on the same camera.
 * 
 * @author Alexander Bieber <fleque@users.sourceforge.net>
 *
 */
public class CameraProxy {

    private int camConfigID;

    private CameraRegistry registry;

    private Camera camera;

    private boolean allocated = false;

    private int[] camBuffer;

    private VideoModeListener modeListener = new VideoModeAdapter() {

        public void formatChanged(int newFormat) {
            resetCamBuffer();
        }

        public void modeChanged(int newMode) {
            resetCamBuffer();
        }

        public void frameRateChanged(int newFrameRate) {
            resetCamBuffer();
        }

        public void clipChanged(Rectangle newClip) {
            resetCamBuffer();
        }

        private void resetCamBuffer() {
            camBuffer = null;
        }
    };

    /**
	 * Instantiation of CameraProxy is only package visible.
	 * Obtain instances through {@link CameraRegistry}
	 */
    CameraProxy(int camConfigID, CameraRegistry regsitry, Camera camera) {
        super();
        this.camConfigID = camConfigID;
        this.registry = regsitry;
        this.camera = camera;
        this.camera.getVideoModes().addVideoModeListener(modeListener);
    }

    /**
	 * @return Wether this proxie has allocated the camera resource exclusively.
	 */
    public boolean isAllocated() {
        return allocated;
    }

    synchronized void setAllocated(boolean allocated) {
        this.allocated = allocated;
    }

    public void startImageAcquisition() throws CameraException {
        if (camera.isImageCaptureStarted()) camera.stopCapture();
        if (!camera.isImageAcquisitionStarted()) camera.startImageAcquisition();
    }

    public void startImageCapture() throws CameraException {
        if (camera.isImageAcquisitionStarted()) camera.stopCapture();
        if (!camera.isImageCaptureStarted()) camera.startImageCapture();
    }

    public void stopImageCapture() throws CameraException {
        camera.stopCapture();
    }

    public boolean isImageAcquisitionStarted() throws CameraException {
        return camera.isImageAcquisitionStarted();
    }

    public boolean isImageCaptureStarted() throws CameraException {
        return camera.isImageCaptureStarted();
    }

    private Dimension notAllocatedResolution = new Dimension(0, 0);

    public Dimension getResolution() throws CameraException {
        if (!isAllocated()) return notAllocatedResolution;
        return camera.getVideoModes().getResolutionDimension();
    }

    protected void makeNotAllocatedPixels(int[] pixels) {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }

    protected int[] getBuffer() throws CameraException {
        if (camBuffer == null) {
            camBuffer = camera.buildImageBuffer();
        }
        int requiredBufferLength = camera.getVideoModes().getResolutionDimension().width * camera.getVideoModes().getResolutionDimension().height;
        if (camBuffer.length != requiredBufferLength) camBuffer = camera.buildImageBuffer();
        return camBuffer;
    }

    public void acquireImage(int[] pixels) throws CameraException {
        if (!isAllocated()) makeNotAllocatedPixels(pixels); else {
            startImageAcquisition();
            camera.acquireImage(pixels);
        }
    }

    public int[] acquireImage() throws CameraException {
        int[] buffer = getBuffer();
        if (!isAllocated()) makeNotAllocatedPixels(buffer); else {
            startImageAcquisition();
            camera.acquireImage(buffer);
        }
        return buffer;
    }

    public void captureImage(int[] pixels) throws CameraException {
        if (!isAllocated()) makeNotAllocatedPixels(pixels); else {
            startImageCapture();
            camera.captureImage(pixels);
        }
    }

    public int[] captureImage() throws CameraException {
        int[] buffer = getBuffer();
        if (!isAllocated()) makeNotAllocatedPixels(buffer); else {
            startImageCapture();
            camera.captureImage(buffer);
        }
        return buffer;
    }

    public int getCamConfigID() {
        return camConfigID;
    }

    public CameraRegistry getRegistry() {
        return registry;
    }
}
