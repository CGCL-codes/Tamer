package org.waveprotocol.wave.client.wavepanel.view;

/**
 * A link info popup.
 *
 * @author vega113@gmail.com (Yuri Z.)
 */
public interface BlipLinkPopupView {

    /**
   * Observer of view events.
   */
    public interface Listener {

        void onHide();

        void onShow();
    }

    /**
   * Binds this view to a listener, until {@link #reset()}.
   */
    void init(Listener listener);

    /**
   * Releases this view from its listener, allowing it to be reused.
   */
    void reset();

    /**
   * Sets the link info url
   *
   * @param url link URL
   */
    void setLinkInfo(String url);

    /**
   * Shows the popup.
   */
    void show();

    /**
   * Hides the popup.
   */
    void hide();
}
