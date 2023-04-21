package net.sf.lostclan.ai;

/**
 * Specifies the interface for AI components in the system.
 * <p/>
 * Note: All AI components must follow the Singleton pattern.
 *
 * @author Bart Cremers
 * @since 13-dec-2006
 */
public interface AI {

    /**
     * This method is called to initialize the AI component.
     */
    public void initialize();

    /**
     * This method is called for each frame update in the in the game. It is up to the AI component to determine if an
     * update should happen.
     */
    public void update();
}
