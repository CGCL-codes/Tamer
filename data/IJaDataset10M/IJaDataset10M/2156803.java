package net.quies.math.plot;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 @author Pascal S. de Kloe
 */
final class ToolBarFader implements MouseMotionListener, ActionListener {

    ToolBarFader(ToolBar target) {
        toolBar = target;
        timer = new Timer(FADE_INTERVAL, this);
        timer.setInitialDelay(IDDLE_TIMEOUT);
        setIddleState();
    }

    public void mouseDragged(MouseEvent e) {
        setIddleState();
    }

    public void mouseMoved(MouseEvent e) {
        setIddleState();
    }

    /**
 * Shows the tool bar until IDDLE_TIMEOUT.
 */
    private void setIddleState() {
        timer.restart();
        if (fadeTrack != 0) {
            fadeTrack = 0;
            toolBar.setAlphaComposite((AlphaComposite) null);
            toolBar.setVisible(true);
            toolBar.repaint();
        }
    }

    /**
 * Fades the tool bar.
 */
    public void actionPerformed(ActionEvent e) {
        ++fadeTrack;
        if (fadeTrack >= FADE_STEPS) {
            setVanishState();
            return;
        }
        float alpha = 1f - (float) fadeTrack / FADE_STEPS;
        toolBar.setAlphaComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        toolBar.repaint();
    }

    /**
 * Hides the tool bar.
 */
    private void setVanishState() {
        timer.stop();
        toolBar.setVisible(false);
    }

    private static final int IDDLE_TIMEOUT = 2000;

    private static final int FADE_INTERVAL = 30;

    private static final int FADE_STEPS = 10;

    private int fadeTrack = 0;

    private final ToolBar toolBar;

    private final Timer timer;
}
