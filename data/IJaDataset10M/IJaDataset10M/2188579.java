package org.sodeja.swing;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInteraction extends KeyAdapter {

    private static final int OCCURANCE_MARKER_TIMEOUT = 700;

    private Callback callback;

    private StringBuilder occuranceStringBuilder;

    private long occuranceTimeoutMarker;

    public KeyInteraction(Callback callback) {
        this.callback = callback;
        occuranceStringBuilder = new StringBuilder();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (KeyEvent.VK_ENTER == e.getKeyCode()) {
            callback.action();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        if (keyChar < 16) {
            return;
        }
        long currTime = System.currentTimeMillis();
        if (currTime - occuranceTimeoutMarker > OCCURANCE_MARKER_TIMEOUT) {
            occuranceStringBuilder.setLength(0);
        }
        occuranceStringBuilder.append(keyChar);
        callback.typed(occuranceStringBuilder.toString());
        occuranceTimeoutMarker = currTime;
    }

    public static interface Callback {

        void action();

        void typed(String text);
    }
}
