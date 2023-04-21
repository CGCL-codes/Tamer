package org.galaxy.gpf.ui;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * AnimationInvoker adds animation effect to heavy-weight components, as
 * subclasses of <code>Window</code>. The effect is showing a component
 * with animation.
 * 
 * @author Cheng Liang
 * @version 1.0.0
 */
public class Animation {

    /** The Constant ANIMATION_INTERVAL. */
    private static final int ANIMATION_INTERVAL = 10;

    /** The Constant ANIMATION_FRAMES. */
    private static final int ANIMATION_FRAMES = 10;

    /** The window to show. */
    private Window window;

    /** The final size after window shown. */
    private Dimension finalSize;

    /** The timer of effect. */
    private Timer timer;

    /** The current frame index. */
    private int frameIndex;

    /**
	 * Shows a component with animation, including modal windows.
	 * 
	 * @param w the window to show
	 */
    public static void show(Window w) {
        if (w.isVisible()) {
            return;
        }
        new Animation(w).invoke();
    }

    /**
	 * Instantiates a new animation invoker.
	 * 
	 * @param w the window to show
	 */
    private Animation(Window w) {
        window = w;
        finalSize = window.getSize();
        timer = new Timer(ANIMATION_INTERVAL, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int w = finalSize.width * frameIndex / ANIMATION_FRAMES;
                int h = finalSize.height * frameIndex / ANIMATION_FRAMES;
                window.setSize(w, h);
                if (frameIndex == ANIMATION_FRAMES) {
                    timer.stop();
                    timer = null;
                    window = null;
                    finalSize = null;
                } else {
                    frameIndex++;
                }
            }
        });
        frameIndex = 0;
        window.setSize(0, 0);
    }

    /**
	 * Starts animation.
	 */
    private void invoke() {
        if (!window.isVisible()) {
            timer.start();
            window.setVisible(true);
        }
    }
}
