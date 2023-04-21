package cz.cube.mtheory.helper;

import com.acarter.scenemonitor.SceneMonitor;
import com.jme.app.SimpleGame;
import com.jme.scene.Node;
import com.sceneworker.SceneWorker;

public abstract class SimpleGameWithMonitor extends SimpleGame {

    @Override
    protected void initGame() {
        super.initGame();
        SceneWorker.inst().initialiseSceneWorkerAndMonitor(rootNode);
    }

    @Override
    protected void simpleUpdate() {
        SceneWorker.inst().updateSceneWorker(tpf);
        super.simpleUpdate();
    }

    @Override
    protected void simpleRender() {
        SceneWorker.inst().renderSceneWorker(display.getRenderer());
        super.simpleRender();
    }

    @Override
    protected void cleanup() {
        super.cleanup();
        SceneMonitor.getMonitor().unregisterNode(rootNode);
    }

    public static void register2Monitor(Node node) {
        SceneMonitor.getMonitor().registerNode(node);
    }
}
