package com.colladaviewer.ardetector;

import java.io.InputStream;
import java.nio.ByteBuffer;
import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.core.NyARCode;
import jp.nyatla.nyartoolkit.core.raster.rgb.NyARRgbRaster;
import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;
import jp.nyatla.nyartoolkit.core.types.NyARBufferType;
import jp.nyatla.nyartoolkit.core.types.matrix.NyARDoubleMatrix44;
import jp.nyatla.nyartoolkit.detector.NyARSingleDetectMarker;
import android.content.res.Resources.NotFoundException;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;

/**
 * <code>ARDetector</code> class handles the detection of optical markers in a given picture
 * and the transfer of this 2D information into 3D space for use with the JME3 engine.
 * 
 * @author as
 *
 */
public class ARDetector {

    private final ARCam cam;

    private final NyARCode pattern = new NyARCode(16, 16);

    private final NyARTransMatResult transmat_result = new NyARTransMatResult();

    private final Matrix3f markerRotation = new Matrix3f();

    private final Vector3f markerTranslation = new Vector3f();

    public NyARRgbRaster raster = null;

    public NyARSingleDetectMarker marker;

    private boolean detected = false;

    private int iNotDetectedCount = 0;

    private int treshold = 100;

    private int width;

    private int height;

    private byte[] buf = null;

    /**
     * Constructor
     * @param pattern must be a stream to a *.patt file for use with NyARToolkit
     * @throws NotFoundException
     * @throws NyARException
     */
    public ARDetector(final InputStream pattern) throws Exception {
        markerRotation.loadIdentity();
        this.pattern.loadARPatt(pattern);
        cam = new ARCam();
    }

    /**
     * Detected gets set to true when a new image was scanned and detected successfully and
     * gets set to false after you fetch the coordinates with applyDetectedMatrix(s) or spatial != null in scan
     * @return true if a new detected marker is available, false else
     */
    public boolean getDetected() {
        return detected;
    }

    /**
     * <code>applyDetectedMatrix</code> sets the location and rotation of spatial 
     * to that of the last detected marker. Clears the detected flag
     * @param spatial
     * @return true if set, false else
     */
    public boolean applyDetectedMatrix(final Spatial spatial) {
        if (spatial != null) {
            if (detected) {
                detected = false;
                spatial.setLocalRotation(markerRotation);
                spatial.setLocalTranslation(markerTranslation);
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * <code>scan</code> tries to detect pattern inside image
     * The size of the image is not allowed to change after the first scan call!
     * 
     * @param image the jme3 image to scan
     * @param spatial jme3 output - the nodes rotation/scale/translation is set if detected, may be null
     * @return float confidence - zero is not detected, > 0 detected
     * @throws NyARException 
     */
    public float scan(final Image image, final Spatial spatial) throws Exception {
        return 0f;
    }

    /**
     * <code>scan</code> tries to detect pattern inside image
     * The size of the image is not allowed to change after the first scan call!
     * 
     * @param image the image to scan as byte array in format R8G8B8_24
     * @param spatial jme3 output - the nodes rotation/scale/translation is set if detected, may be null
     * @return float confidence - zero is not detected, > 0 detected
     * @throws NyARException 
     */
    public float scan(final short[] image, int width, int height, final Spatial spatial) throws Exception {
        if (raster == null) {
            this.width = width;
            this.height = height;
            cam.changeScreenSize(width, height);
            raster = new NyARRgbRaster(width, height, NyARBufferType.WORD1D_R5G6B5_16LE, false);
            marker = new NyARSingleDetectMarker(cam, pattern, 1d, raster.getBufferType(), NyARSingleDetectMarker.PF_NYARTOOLKIT);
        }
        raster.wrapBuffer(image);
        if (marker.detectMarkerLite(raster, treshold)) {
            marker.getTransmationMatrix(transmat_result);
            convertMatrixToJme(transmat_result, 1f);
            detected = true;
            applyDetectedMatrix(spatial);
            iNotDetectedCount = 0;
            marker.setContinueMode(true);
        } else {
            iNotDetectedCount++;
            if (iNotDetectedCount == 10) {
                marker.setContinueMode(false);
            }
            treshold += iNotDetectedCount * ((iNotDetectedCount % 2 == 0) ? -1 : 1);
            if (treshold < 70 || treshold > 130) {
                treshold = 100;
                iNotDetectedCount = 1;
            }
        }
        return (float) marker.getConfidence();
    }

    /**
     * <code>convertImage</code> converts a jme3 image to R8G8B8_24 
     * @param image
     * @param out byte[] in R8G8B8_24
     * @returns int the number of bytes decoded
     * @throws NyARException
     */
    private int convertImage(final Image image, byte[] out) throws NyARException {
        int o = 0;
        for (ByteBuffer bb : image.getData()) {
            if (image.getFormat().equals(Format.RGBA8)) {
                int p = bb.position();
                bb.rewind();
                while (bb.hasRemaining()) {
                    out[o * 3] = bb.get();
                    out[o * 3 + 1] = bb.get();
                    out[o * 3 + 2] = bb.get();
                    o++;
                    bb.get();
                }
                bb.position(p);
            } else if (image.getFormat().equals(Format.BGR8)) {
                int p = bb.position();
                bb.rewind();
                while (bb.hasRemaining()) {
                    out[o * 3 + 2] = bb.get();
                    out[o * 3 + 1] = bb.get();
                    out[o * 3] = bb.get();
                    o++;
                }
                bb.position(p);
            } else {
                throw new NyARException("Unsupported pixel format " + image.getFormat());
            }
        }
        return o;
    }

    /**
     * convert the detected marker location&rotation to JME3
     * @param mat
     * @param scale
     * @param resultMatrix
     */
    public void convertMatrixToJme(final NyARDoubleMatrix44 src, final float scale) {
        markerRotation.set(0, 0, (float) -src.m00);
        markerRotation.set(0, 1, (float) src.m01);
        markerRotation.set(0, 2, (float) src.m02);
        markerRotation.set(1, 0, (float) -src.m10);
        markerRotation.set(1, 1, (float) src.m11);
        markerRotation.set(1, 2, (float) src.m12);
        markerRotation.set(2, 0, (float) src.m20);
        markerRotation.set(2, 1, (float) -src.m21);
        markerRotation.set(2, 2, (float) -src.m22);
        markerTranslation.set((float) src.m03, (float) src.m13, (float) -src.m23);
        markerTranslation.multLocal(1 / scale);
    }
}
