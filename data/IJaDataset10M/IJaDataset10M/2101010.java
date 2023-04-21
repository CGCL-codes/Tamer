package edu.ua.j3dengine.core;

import org.xith3d.scenegraph.View;
import org.xith3d.scenegraph.Node;
import edu.ua.j3dengine.core.state.DynamicObjectState;
import edu.ua.j3dengine.core.behavior.Behavior;
import edu.ua.j3dengine.core.behavior.InertBehavior;
import edu.ua.j3dengine.core.movement.MovementController;
import edu.ua.j3dengine.core.movement.CameraMovementController;
import edu.ua.j3dengine.core.movement.impl.CameraMovementControllerImpl;
import edu.ua.j3dengine.core.geometry.impl.GeometryXithImpl;

public class GameObjectFactory {

    private static GameObjectFactory instance;

    private GameObjectFactory() {
    }

    public static synchronized GameObjectFactory getInstance() {
        if (instance == null) {
            instance = new GameObjectFactory();
        }
        return instance;
    }

    public DynamicGameObject createDynamicObject(String name) {
        DynamicGameObject object = new DynamicGameObject(name);
        DynamicObjectState state = new DynamicObjectState("defaultState", null, null, new InertBehavior());
        object.addState(state);
        object.setInitialState(state.getName());
        return object;
    }

    public DynamicGameObject createDynamicObject(String name, Node xithGeometry) {
        DynamicGameObject object = createDynamicObject(name);
        object.setGeometry(new GeometryXithImpl(name + "_xith_geometry", xithGeometry));
        return object;
    }

    public Camera createCamera(String cameraName, View view) {
        Camera camera = new Camera(cameraName, view);
        Behavior behavior = new InertBehavior();
        DynamicObjectState normalCameraState = new DynamicObjectState("NormalCameraState", null, null, behavior);
        camera.addState(normalCameraState);
        camera.setInitialState("NormalCameraState");
        MovementController controller = new CameraMovementControllerImpl(camera);
        camera.changeMovementController(controller);
        return camera;
    }
}
