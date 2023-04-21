package org.waveprotocol.wave.client.common.util;

/**
 * Base View interface.
 *
 * @param <L> listener interface for this view.
 */
public interface View<L> {

    interface Factory<V extends View<?>> extends org.waveprotocol.wave.client.common.util.Factory<V> {
    }

    /**
   * Initializes this view. The view is considered to be used until
   * {@link #reset()}.
   *
   * @param listener listener for events broadcast by this view
   */
    void init(L listener);

    /**
   * Releases this view from being used. It is up to each implementation type to
   * define if this view is reusable after this method is called.
   */
    void reset();
}
