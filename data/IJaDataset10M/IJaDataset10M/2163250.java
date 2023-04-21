package org.waveprotocol.wave.client.widget.popup;

/**
 * Factory for creating PopupChrome objects.
 *
 */
public interface PopupChromeProvider {

    /**
   * Create a new PopupChrome object.
   */
    PopupChrome createPopupChrome();
}
