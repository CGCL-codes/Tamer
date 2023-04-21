package components.features.cam;

import components.features.Feature;

/**
 * Esta clase representa una c�mara. Debe ser extendida.
 */
public abstract class Camera extends Feature {

    public Camera() {
        super();
    }

    public Camera(String name, String description, Object initValue) {
        super(name, description, initValue);
    }
}
