package org.tacticalTroopers.jme.client;

import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

/**
 *
 * @author tim
 */
public class CameraNode extends Node {

    private final Camera cam;

    private boolean enabled = true;

    public CameraNode(String name, Camera cam) {
        super(name);
        this.cam = cam;
    }

    public CameraNode(Camera cam) {
        this("defName", cam);
    }

    @Override
    public void updateGeometricState() {
        super.updateGeometricState();
        if (enabled) {
            cam.setLocation(getWorldTranslation());
            cam.setRotation(getWorldRotation());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final CameraNode other = (CameraNode) obj;
        if (this.cam != other.cam && (this.cam == null || !this.cam.equals(other.cam))) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.cam != null ? this.cam.hashCode() : 0);
        return hash;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
