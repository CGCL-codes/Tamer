package src.game;

import src.gui.editor.EditorWindow;
import src.gui.editor.InitEditorFrame;

/**
 * Runnable class that runs the editor.
 * @author Darren Watts
 * date 1/21/08
 */
public class TheEditor implements Runnable {

    private boolean running;

    private EditorWindow window;

    private boolean canDraw;

    /**
	 * Constructor.  Sets up the various variables.
	 */
    public TheEditor() {
        running = true;
        new InitEditorFrame(this);
        canDraw = true;
    }

    /**
	 * Sets the EditorWindow of the editor.
	 * @param win EditorWidow : editor window.
	 */
    public void setWindow(EditorWindow win) {
        window = win;
    }

    /**
	 * Run loop.  Draws the editor window if it can, and waits for the
	 * next tick.
	 */
    public void run() {
        running = true;
        long gameStartTime;
        long beforeTime, afterTime, timeDiff, sleepTime, overSleepTime = 0L, excess = 0L, period = (long) 1000000000 / 60;
        int noDelays = 0;
        gameStartTime = System.nanoTime();
        beforeTime = gameStartTime;
        while (running) {
            if (window != null && window.isVisible() && canDraw) window.draw();
            afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            sleepTime = (period - timeDiff) - overSleepTime;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1000000L);
                } catch (InterruptedException ex) {
                }
                overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
            } else {
                excess -= sleepTime;
                overSleepTime = 0L;
                if (++noDelays >= 16) {
                    Thread.yield();
                    noDelays = 0;
                }
            }
            beforeTime = System.nanoTime();
        }
    }

    /**
	 * Accessor for whether or not the editor window can be drawn at
	 * this time.
	 * @return boolean : can draw.
	 */
    public boolean getCanDraw() {
        return canDraw;
    }

    /**
	 * Sets whether or not the editor window can be drawn at this time.
	 * @param bool boolean : can draw
	 */
    public void setCanDraw(boolean bool) {
        canDraw = bool;
    }
}
