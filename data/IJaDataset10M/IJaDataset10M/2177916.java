package org.waveprotocol.wave.client.autohide;

/**
 * Something that can be hidden.
 *
 */
public interface Hideable {

    /**
   * @return {@code true} if the object is showing.
   */
    boolean isShowing();

    /**
   * Hides the object.
   */
    void hide();
}
