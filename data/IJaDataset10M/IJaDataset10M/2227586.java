package de.engehausen.mobile.puzzling;

import javax.microedition.lcdui.Graphics;

/**
 * A timer counting up and displaying minutes (max. 99) and seconds.
 */
public class Timer implements Runnable {

    private final GameCanvas canvas;

    private final Font font;

    private boolean running;

    private int seconds;

    private final int width;

    /**
	 * Creates the timer for the given game canvas, using the given font.
	 * @param game the game canvas showing the timer
	 * @param f the font to use
	 */
    public Timer(final GameCanvas game, final Font f) {
        canvas = game;
        font = f;
        seconds = 0;
        width = 5 * f.getCharWidth();
    }

    /**
	 * The timer functionality, to be run in a separate thread
	 * from the rest of the game.
	 */
    public void run() {
        running = true;
        while (running) {
            long rest = 998L;
            final long target = System.currentTimeMillis() + rest;
            while (rest > 0) {
                try {
                    Thread.sleep(rest);
                } catch (InterruptedException e) {
                    ;
                }
                rest = target - System.currentTimeMillis();
            }
            if (running) {
                if (seconds < 0) {
                    running = false;
                } else {
                    seconds++;
                }
                canvas.repaint(Constants.GAME_RENDER_MODE_TIME);
            }
        }
    }

    /**
	 * Stops the timer where it stands.
	 */
    public void stop() {
        running = false;
    }

    /**
	 * Cancels the timer (its value won't be displayed after).
	 */
    public void cancel() {
        seconds = Integer.MIN_VALUE;
    }

    /**
	 * Paints the current time
	 * @param textX the starting x position
	 * @param textY the starting y position
	 * @param g the graphics to paint on
	 */
    public void timePaint(final int textX, final int textY, final Graphics g) {
        final int now = seconds;
        final int w = font.getCharWidth();
        g.fillRect(textX, textY, width, font.getCharHeight());
        if (now >= 0) {
            final int s = now % 60;
            final int m = (now - s) / 60;
            digits(m, textX, textY, w, g);
            font.paint(':', textX + 2 * w, textY, g);
            digits(s, textX + 3 * w, textY, w, g);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return font.getCharHeight();
    }

    /**
	 * Returns the current time in seconds
	 * @return the current time in seconds
	 */
    public int getTime() {
        return seconds;
    }

    protected void digits(final int num, final int pos, final int textY, final int w, final Graphics g) {
        font.paint((char) ('0' + num / 10), pos, textY, g);
        font.paint((char) ('0' + num % 10), pos + w, textY, g);
    }
}
