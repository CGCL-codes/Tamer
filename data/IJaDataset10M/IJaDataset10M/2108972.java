package com.jme3.gde.core.scene;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.awt.image.BufferedImage;

/**
 *
 * @author normenhansen
 */
public class PreviewRequest {

    private Object requester;

    private Spatial spatial;

    private BufferedImage image;

    private CameraRequest cameraRequest;

    public PreviewRequest(Object requester, Spatial spatial) {
        this(requester, spatial, 120, 120);
    }

    public PreviewRequest(Object requester, Spatial spatial, int width, int height) {
        this.requester = requester;
        this.spatial = spatial;
        cameraRequest = new CameraRequest();
        cameraRequest.width = width;
        cameraRequest.height = height;
    }

    /**
     * @return the requester
     */
    public Object getRequester() {
        return requester;
    }

    /**
     * @return the spatial
     */
    public Spatial getSpatial() {
        return spatial;
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public CameraRequest getCameraRequest() {
        return cameraRequest;
    }

    public class CameraRequest {

        Vector3f location = null;

        Quaternion rotation = null;

        Vector3f lookAt = null;

        Vector3f up = null;

        int width, height;

        public void setLocation(Vector3f location) {
            this.location = location;
        }

        public void setLookAt(Vector3f lookAt, Vector3f up) {
            this.lookAt = lookAt;
            this.up = up;
        }

        public void setRotation(Quaternion rotation) {
            this.rotation = rotation;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
