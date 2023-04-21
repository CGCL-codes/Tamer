package org.waveprotocol.wave.client.widget.button;

/**
 * Display that acts as a composite, wrapping multiple child displays,
 *   and keeping their states in sync.
 * @author patcoleman@google.com (Pat Coleman)
 */
public class CompositeButtonDisplay implements ButtonDisplay {

    /** The child displays this is a composite for */
    private final ButtonDisplay[] children;

    /**
   * Initialises the composite, taking all displays it is to sync together
   * @param displays Displays to sync
   */
    public CompositeButtonDisplay(ButtonDisplay... displays) {
        children = displays;
    }

    @Override
    public void setState(ButtonState state) {
        for (ButtonDisplay display : children) {
            display.setState(state);
        }
    }

    @Override
    public void setUiListener(MouseListener mouseListener) {
        for (ButtonDisplay display : children) {
            display.setUiListener(mouseListener);
        }
    }

    @Override
    public void setTooltip(String tooltip) {
        for (ButtonDisplay display : children) {
            display.setTooltip(tooltip);
        }
    }

    @Override
    public void setText(String text) {
        for (ButtonDisplay display : children) {
            display.setText(text);
        }
    }
}
