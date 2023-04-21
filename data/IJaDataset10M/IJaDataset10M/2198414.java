package arme.core.artkp;

/**
 * Clase abstracta que representa un sistema de tracking o reconocimiento. Cada
 * tracker puede reconocer n marcadores de un unico tipo.
 */
public abstract class Tracker {

    protected Tracker(long trackerHandle) throws ARTKPException {
        if (trackerHandle == 0) throw new ARTKPException("Trying to initialize Tracker with an uninitialized ");
        _trackerHandle = trackerHandle;
        _initTracker();
    }

    public void finalize() throws ARTKPException {
        if (_trackerHandle != 0) {
            cleanup();
            _destroyTrackerHandle();
        }
    }

    protected native void _initTracker() throws ARTKPException;

    private long _trackerHandle = 0;

    private native void _destroyTrackerHandle() throws ARTKPException;

    public native void cleanup();

    /**
	 * Default format is RGB888 (ARToolkitPlus.PIXEL_FORMAT_RGB)
	 */
    public native boolean setPixelFormat(int nFormat) throws ARTKPException;

    /**
	 * Loads a camera calibration file and stores data internally<br>
	 * To prevent memory leaks, this method internally deletes an existing
	 * camera. If you want to use more than one camera, retrieve the existing
	 * camera using getCamera() and call setCamera(NULL); before loading another
	 * camera file. On destruction, ARToolKitPlus will only destroy the
	 * currently set camera. All other cameras have to be destroyed manually.
	 */
    public native boolean loadCameraFile(String nCamParamFile, float nNearClip, float nFarClip) throws ARTKPException;

    /**
	 * Set to true to try loading camera undistortion table from a cache file<br>
	 * On slow platforms (e.g. Smartphone) creation of the undistortion
	 * lookup-table can take quite a while. Consequently caching will speedup
	 * the start phase. If set to true and no cache file could be found a new
	 * one will be created. The cache file will get the same name as the camera
	 * file with the added extension '.LUT'
	 */
    public native void setLoadUndistLUT(boolean nSet) throws ARTKPException;

    /**
	 * Activates binary markers. Markers are converted to pure black/white
	 * during loading
	 *
	 */
    public native void activateBinaryMarker(int nThreshold) throws ARTKPException;

    /**
	 * Activate the usage of id-based markers rather than template based markers<br>
	 * Template markers are the classic marker type used in ARToolKit. Id-based
	 * markers directly encode the marker id in the image. Simple markers use
	 * 3-times redundancy to increase robustness, while BCH markers use an
	 * advanced CRC algorithm to detect and repair marker damages. See
	 * arBitFieldPattern.h for more information. In order to use id-based
	 * markers, the marker size has to be 6x6, 12x12 or 18x18.
	 *
	 * @param nMarkerMode
	 * @throws ARTKPException
	 */
    public native void setMarkerMode(int nMarkerMode) throws ARTKPException;

    public native int getMarkerMode() throws ARTKPException;

    public native void activateVignettingCompensation(boolean nEnable, int nCorners, int nLeftRight, int nTopBottom);

    /**
	 * Changes the resolution of the camera after the camerafile was already
	 * loaded
	 *
	 * @param nWidth
	 * @param nHeight
	 * @throws ARTKPException
	 */
    public native void changeCameraSize(int nWidth, int nHeight) throws ARTKPException;

    /**
	 * Changes the undistortion mode<br>
	 * Default value is UNDIST_STD which means that artoolkit's standard
	 * undistortion method is used.
	 *
	 * @param nMode
	 * @throws ARTKPException
	 */
    public native void setUndistortionMode(int nMode) throws ARTKPException;

    /**
	 * Changes the Pose Estimation Algorithm<br>
	 * POSE_ESTIMATOR_ORIGINAL (default): arGetTransMat() POSE_ESTIMATOR_RPP:
	 * "Robust Pose Estimation from a Planar Target"
	 *
	 * @param nMethod
	 * @return
	 * @throws ARTKPException
	 */
    public native boolean setPoseEstimator(int nMethod) throws ARTKPException;

    /**
	 * Sets a new relative border width. ARToolKit's default value is 0.25<br>
	 * Take caution that the markers need of course really have thiner borders.
	 * Values other than 0.25 have not been tested for regular pattern-based
	 * matching, but only for id-encoded markers. It might be that the pattern
	 * creation process needs to be updated too.
	 *
	 * @param nFraction
	 * @throws ARTKPException
	 */
    public native void setBorderWidth(float nFraction) throws ARTKPException;

    /**
	 * Sets the threshold value that is used for black/white conversion
	 *
	 * @param nValue
	 * @throws ARTKPException
	 */
    public native void setThreshold(int nValue) throws ARTKPException;

    /**
	 * Returns the current threshold value.
	 *
	 * @return
	 * @throws ARTKPException
	 */
    public native int getThreshold() throws ARTKPException;

    /**
	 * Enables or disables automatic threshold calculation
	 *
	 * @param nEnable
	 * @throws ARTKPException
	 */
    public native void activateAutoThreshold(boolean nEnable) throws ARTKPException;

    /**
	 * Returns true if automatic threshold calculation is activated
	 *
	 * @return
	 * @throws ARTKPException
	 */
    public native boolean isAutoThresholdActivated() throws ARTKPException;

    /**
	 * Sets the number of times the threshold is randomized in case no marker
	 * was visible (Default: 2)<br>
	 * Autothreshold requires a visible marker to estime the optimal
	 * thresholding value. If no marker is visible ARToolKitPlus randomizes the
	 * thresholding value until a marker is found. This function sets the number
	 * of times ARToolKitPlus will randomize the threshold value and research
	 * for a marker per calc() invokation until it gives up. A value of 2 means
	 * that ARToolKitPlus will analyze the image a second time with an other
	 * treshold value if it does not find a marker the first time. Each
	 * unsuccessful try uses less processing power than a single full successful
	 * position estimation.
	 *
	 * @param nNumRetries
	 * @throws ARTKPException
	 */
    public native void setNumAutoThresholdRetries(int nNumRetries) throws ARTKPException;

    /**
	 * Sets an image processing mode (half or full resolution) Half resolution
	 * is faster but less accurate. When using full resolution smaller markers
	 * will be detected at a higher accuracy (or even detected at all).
	 *
	 * @param nMode
	 */
    public native void setImageProcessingMode(int nMode);

    /**
	 * Returns an opengl-style modelview transformation matrix
	 *
	 * @return
	 * @throws ARTKPException
	 */
    public native float[] getModelViewMatrix() throws ARTKPException;

    /**
	 * Returns an opengl-style projection transformation matrix
	 *
	 * @return
	 * @throws ARTKPException
	 */
    public native float[] getProjectionMatrix() throws ARTKPException;

    /**
	 * Returns a short description with compiled-in settings
	 *
	 * @return
	 * @throws ARTKPException
	 */
    public native String getDescription() throws ARTKPException;

    /**
	 * Returns the compiled-in pixel format
	 *
	 * @return
	 * @throws ARTKPException
	 */
    public native int getPixelFormat() throws ARTKPException;

    /**
	 * Returns the number of bits required to store a single pixel
	 *
	 * @return
	 */
    public native int getBitsPerPixel();

    /**
	 * Returns the maximum number of patterns that can be loaded<br>
	 * This maximum number of loadable patterns can be set via the
	 * __MAX_LOAD_PATTERNS template parameter
	 *
	 * @return
	 */
    public native int getNumLoadablePatterns();

    /**
	 * Calculates the OpenGL transformation matrix for a specific marker info
	 *
	 * @param nMarkerInfo
	 * @param nPatternCenterX
	 * @param nPatternCenterY
	 * @param nPatternSize
	 * @param nOpenGLMatrix
	 * @return
	 */
    public native float calcOpenGLMatrixFromMarker(long nMarkerInfo, float nPatternCenterX, float nPatternCenterY, float nPatternSize, float[] nOpenGLMatrix);

    static {
        System.loadLibrary("artkp_Tracker");
    }
}
