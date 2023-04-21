package org.waveprotocol.wave.client.widget.menu;

/**
 * An item in a menu.
 *
 */
public interface MenuItem {

    /**
   * Sets the item to be enabled or disabled (greyed out).
   *
   * @param enabled Whether to set the item to be enabled or disabled.
   */
    void setEnabled(boolean enabled);

    /**
   * Sets the item's enabledness to the state it was in at creation.
   */
    void resetEnabled();

    /**
   * Sets whether this object is visible.
   *
   * @param visible {code true} to show the object, {@code false} to hide it
   */
    void setVisible(boolean visible);

    /**
   * Change the label.
   *
   * @param label {code String} label
   */
    void setText(String label);

    void setDebugClassTODORename(String debugClass);
}
