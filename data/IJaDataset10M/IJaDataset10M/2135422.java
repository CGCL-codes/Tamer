package net.sf.jmp3renamer.gui;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * A animated version of ImageIcon. It can be used anywhere an ImageIcon can be.
 */
public class AnimatedIcon extends ImageIcon {

    private static Animator animator = new Animator();

    private static Image[] frames;

    private static int current;

    private static int delay;

    private static Component host;

    private static Image icon;

    private static AnimatedIcon instance;

    private static boolean running = false;

    /**
     * @param frames
     *            The frames to be used in the animation
     * @param rate
     *            The frame rate of the animation, in frames per second
     * @param host
     *            The container that the animation is used in
     */
    private AnimatedIcon(Image icon, Image[] frames, int rate, Component host) {
        super(icon);
        AnimatedIcon.icon = icon;
        AnimatedIcon.frames = frames;
        delay = 1000 / rate;
        AnimatedIcon.host = host;
    }

    public static AnimatedIcon getInstance(Image img, Image[] frames, int rate, Component host) {
        if (instance == null) {
            instance = new AnimatedIcon(img, frames, rate, host);
            animator.start();
        }
        return instance;
    }

    public static void setHost(Component host) {
        AnimatedIcon.host = host;
    }

    /**
     * Starts the animation rolling
     */
    public static void startAnimation() {
        animator.start();
    }

    /**
     * Stops the animation, and resets to frame 0
     */
    public static void stopAnimation() {
        animator.stopThread();
        current = 0;
        AnimatedIcon.instance.setImage(icon);
        host.repaint();
    }

    public boolean isRunning() {
        return running;
    }

    static class Animator extends Thread {

        public void stopThread() {
            running = false;
        }

        public void run() {
            running = true;
            while (running) {
                try {
                    sleep(delay);
                } catch (Exception e) {
                }
                current = (current + 1) % frames.length;
                AnimatedIcon.instance.setImage(frames[current]);
                host.repaint();
            }
        }
    }
}
