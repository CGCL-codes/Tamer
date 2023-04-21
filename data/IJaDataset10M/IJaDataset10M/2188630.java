package it.tukano.jps.engines.jme2.adders;

import com.jme.math.Quaternion;
import com.jme.math.TransformMatrix;
import com.jme.renderer.Camera;
import it.tukano.jps.engines.jme2.Adder;
import it.tukano.jps.engines.jme2.JME2Engine;
import it.tukano.jps.engines.jme2.MathConversions;
import it.tukano.jps.event.engine.EngineEvent;
import it.tukano.jps.event.engine.SceneElementAdd;
import it.tukano.jps.scene.CameraElement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An adder that adds a CameraElement to a jme 2 scene.
 */
public class CameraElementAdder extends Adder<CameraElement> {

    /**
     * Default no arg constructor
     */
    public CameraElementAdder(JME2Engine engine) {
        super(CameraElement.class, true, engine);
    }

    @Override
    public Iterable<EngineEvent> applyTo(CameraElement cameraElement) {
        List<CameraElement> cameras = getEngine().getSceneMap().listSceneElements(CameraElement.class);
        TransformMatrix transform = MathConversions.newTransformMatrix(cameraElement.getTransform());
        getEngine().getSceneMap().put(cameraElement, transform);
        if (cameras.size() == 0) {
            getEngine().setCurrentCamera(cameraElement);
            if (!getEngine().getCurrentCamera().getEnabled()) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Engine unique camera is not enabled, enabling it by default");
            }
            Camera jmeCamera = getEngine().getCanvas().getCamera();
            jmeCamera.setLocation(transform.getTranslation(null));
            jmeCamera.setAxes(transform.getRotation((Quaternion) null));
            jmeCamera.update();
        } else {
            if (cameraElement.getEnabled()) {
                getEngine().setCurrentCamera(cameraElement);
                Camera jmeCamera = getEngine().getCanvas().getCamera();
                jmeCamera.setLocation(transform.getTranslation(null));
                jmeCamera.setAxes(transform.getRotation((Quaternion) null));
                jmeCamera.update();
            }
        }
        return new SceneElementAdd(getEngine(), cameraElement);
    }
}
