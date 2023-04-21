package com.jmex.game.state;

import com.jme.scene.Node;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;

/**
 * <code>BasicGameStateNode</code> this is identical to BasicGameState except it allows
 * you to add additional GameStates as children beneath it.
 * 
 * @author Matthew D. Hicks
 */
public class BasicGameStateNode<G extends GameState> extends GameStateNode<G> {

    /** The root of this GameStates scenegraph. */
    protected Node rootNode;

    /**
	 * Creates a new BasicGameStateNode with a given name.
	 * 
	 * @param name The name of this GameState.
	 */
    public BasicGameStateNode(String name) {
        super(name);
        rootNode = new Node(name + ": RootNode");
        ZBufferState buf = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
        buf.setEnabled(true);
        buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        rootNode.setRenderState(buf);
    }

    /**
	 * Updates the rootNode.
	 * 
	 * @see GameState#update(float)
	 */
    public void update(float tpf) {
        super.update(tpf);
        rootNode.updateGeometricState(tpf, true);
    }

    /**
	 * Draws the rootNode.
	 * 
	 * @see GameState#render(float)
	 */
    public void render(float tpf) {
        super.render(tpf);
        DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);
    }

    /**
	 * Empty.
	 * 
	 * @see GameState#cleanup()
	 */
    public void cleanup() {
        super.cleanup();
    }

    public Node getRootNode() {
        return rootNode;
    }
}
