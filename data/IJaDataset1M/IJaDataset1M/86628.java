package com.zerocool.app;

import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.SceneElement;
import com.jme.scene.Text;
import com.jme.util.LoggingSystem;
import com.zerocool.managers.InputManager;

public class SimpleApp extends SimpleGame {

    InputManager input = new InputManager();

    public final String GAME_TITLE = "Simple Game";

    public static void main(String[] args) {
        LoggingSystem.getLogger().setLevel(java.util.logging.Level.WARNING);
        SimpleApp app = new SimpleApp();
        app.setDialogBehaviour(ALWAYS_SHOW_PROPS_DIALOG);
        app.start();
    }

    @Override
    protected void simpleInitGame() {
        display.setTitle(GAME_TITLE);
        Text text = new Text("PQ label", "Welcome");
        text.setCullMode(SceneElement.CULL_NEVER);
        Text text2 = new Text("PQ label", "SimpleProject V:0.01");
        text2.setCullMode(SceneElement.CULL_NEVER);
        Node node = new Node();
        node.setLocalTranslation(new Vector3f(0, 20, 0));
        node.attachChild(text);
        Node node2 = new Node();
        node2.setLocalTranslation(new Vector3f(0, 20, 0));
        node2.attachChild(text2);
        node.attachChild(node2);
        fpsNode.attachChild(node);
    }

    protected void simpleUpdate() {
    }
}
