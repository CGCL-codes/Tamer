package com.daveoxley.cnery.scenes;

import com.daveoxley.cnery.entities.Scene;
import java.util.HashMap;

/**
 *
 * @author Dave Oxley <dave@daveoxley.co.uk>
 */
public final class SceneRunnerFactory {

    private static final SceneRunnerFactory instance = new SceneRunnerFactory();

    private final HashMap<String, SceneRunner> sceneRunners = new HashMap<String, SceneRunner>();

    private SceneRunnerFactory() {
    }

    public static SceneRunnerFactory getInstance() {
        return instance;
    }

    public synchronized SceneRunner getSceneRunner(Scene scene) throws Exception {
        SceneRunner sceneRunner = sceneRunners.get(scene.getId());
        if (sceneRunner != null) return sceneRunner;
        sceneRunner = new SceneRunner(scene);
        sceneRunners.put(scene.getId(), sceneRunner);
        return sceneRunner;
    }
}
