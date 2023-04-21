package common.model;

/**
 * @author stefan
 * @since 2.0
 */
public interface GameObject {

    /**
   * Returns true if this JawGameObject is idle.
   */
    public boolean isIdle();

    /**
   * Returns true if this JawGameObject is active.
   */
    public boolean isActive();

    /**
   * Returns true if this JawGameObject is dying.
   */
    public boolean isDying();

    /**
   * Returns true if this JawGameObject is destroyed.
   */
    public boolean isDestroyed();

    GameObjectState getState();
}
