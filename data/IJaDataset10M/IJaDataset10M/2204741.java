package org.waveprotocol.wave.client.widget.button;

/**
 * Something that can be disabled (eg, a button that is disabled when its action
 * cannot be performed).
 *
 */
public interface Disableable {

    /**
   * Sets whether or not this object is disabled.
   *
   * @param isDisabled Whether or not this object is disabled.
   */
    void setDisabled(boolean isDisabled);
}
