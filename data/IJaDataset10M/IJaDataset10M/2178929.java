package homura.hde.app.core.game.state;

/**
 * A GameState is used to encapsulate a certain state of a game, e.g. "ingame" or
 * "main menu".
 * <p>
 * A GameState can be attached to a GameStateNode, forming a tree structure 
 * similar to jME's scenegraph.
 * <p>
 * It contains two important methods: update(float) and render(float),
 * which gets called by the parent GameStateNode, e.g. the GameStateManager.
 * 
 * @see GameStateManager
 * @see GameStateNode
 * @see BasicGameState
 * @see CameraGameState
 * 
 * @author Per Thulin
 */
public abstract class GameState {

    /** The name of this GameState. */
    protected String name;

    /** Flags whether or not this GameState should be processed. */
    protected boolean active;

    /** GameState's parent, or null if it has none (is the root node). */
    protected GameStateNode parent;

    /**
	 * Gets called every frame before render(float) by the parent 
	 * <code>GameStateNode</code>.
	 * 
	 * @param tpf The elapsed time since last frame.
	 */
    public abstract void update(float tpf);

    /**
	 * Gets called every frame after update(float) by the 
	 * <code>GameStateManager</code>.
	 * 
	 * @param tpf The elapsed time since last frame.
	 */
    public abstract void render(float tpf);

    /**
     * Gets performed when cleanup is called on a parent GameStateNode (e.g.
     * the GameStateManager).
     */
    public abstract void cleanup();

    /**
     * Sets whether or not you want this GameState to be updated and rendered.
     * 
     * @param active 
     *        Whether or not you want this GameState to be updated and rendered.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns whether or not this GameState is updated and rendered.
     * 
     * @return Whether or not this GameState is updated and rendered.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns the name of this GameState.
     * 
     * @return The name of this GameState.
     */
    public String getName() {
        return name;
    }

    /**
	 * Sets the name of this GameState.
	 * 
	 * @param name The new name of this GameState.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Sets the parent of this node. <b>The user should never touch this method,
	 * instead use the attachChild method of the wanted parent.</b>
	 * 
	 * @param parent The parent of this GameState.
	 */
    public void setParent(GameStateNode parent) {
        this.parent = parent;
    }

    /**
	 * Retrieves the parent of this GameState. If the parent is null, this is
	 * the root node.
	 * 
	 * @return The parent of this node.
	 */
    public GameStateNode getParent() {
        return parent;
    }
}
