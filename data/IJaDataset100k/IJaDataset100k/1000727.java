package org.waveprotocol.wave.client.wavepanel.view;

/**
 * A view interface for an inline thread.
 *
 */
public interface InlineThreadView extends ThreadView, IntrinsicInlineThreadView {

    @Override
    ContinuationIndicatorView getReplyIndicator();
}
