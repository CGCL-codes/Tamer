package org.j4me.examples.ui;

import java.util.*;
import javax.microedition.lcdui.*;
import org.j4me.ui.*;
import org.j4me.ui.components.*;

/**
 * The stopwatch can be started and stopped to time an event.
 * When restarted the watch resets back to zero seconds before
 * timing.
 * <p>
 * Demonstrates how to use an empty canvas.  It also demonstrates
 * manually laying out a component (specifically a <code>Label</code>)
 * on a canvas.
 */
public class Stopwatch extends DeviceScreen {

    /**
	 * The screen to return to once this one is canceled.
	 */
    private final DeviceScreen previous;

    /**
	 * The system time that the stopwatch started.  If the stopwatch
	 * isn't running, this will be -1.
	 */
    private long startTime = -1;

    /**
	 * A label showing how much time has elapsed.  This is painted onto
	 * the center of the canvas while the stopwatch is running.
	 */
    private Label elapsedTime;

    /**
	 * A timer to handle stopwatch timing.
	 */
    private Timer timer;

    /**
	 * Initializes the screen.
	 * 
	 * @param previous is the screen that this one will return to when
	 *  the user backs out of it.
	 */
    public Stopwatch(DeviceScreen previous) {
        this.previous = previous;
        setTitle("Stopwatch");
        setMenuText("Back", "Start");
        elapsedTime = new Label();
        elapsedTime.setLabel("Press Start to begin");
        elapsedTime.setHorizontalAlignment(Graphics.HCENTER);
        Font font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE);
        elapsedTime.setFont(font);
    }

    /**
	 * Called when the user presses the left side button which takes us
	 * back to the previous screen.
	 */
    protected void declineNotify() {
        if (previous != null) {
            previous.show();
        }
    }

    /**
	 * Called when the user presses the right side button which starts
	 * or stops the stopwatch.
	 */
    protected void acceptNotify() {
        if (startTime < 0) {
            setMenuText(getLeftMenuText(), "Stop");
            startTime = System.currentTimeMillis();
            if (timer != null) {
                timer.cancel();
            }
            timer = new Timer();
            timer.schedule(new StopwatchTimerTask(), 0, 15);
        } else {
            setMenuText(getLeftMenuText(), "Start");
            startTime = -1;
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
        repaint();
    }

    /**
	 * Called when any key is pressed.
	 * 
	 * @see org.j4me.ui.DeviceScreen#keyPressed(int)
	 */
    protected void keyPressed(int keyCode) {
        if (keyCode == FIRE) {
            acceptNotify();
        }
        super.keyPressed(keyCode);
    }

    /**
	 * Implements stopwatch functionality.
	 */
    private final class StopwatchTimerTask extends TimerTask {

        public void run() {
            synchronized (Stopwatch.this) {
                long duration = System.currentTimeMillis() - startTime;
                long milliseconds = duration % 1000;
                long seconds = duration / 1000;
                long minutes = seconds / 60;
                seconds %= 60;
                StringBuffer buffer = new StringBuffer();
                buffer.append(minutes);
                buffer.append(":");
                if (seconds < 10) buffer.append("0");
                buffer.append(seconds);
                buffer.append(".");
                if (milliseconds < 10) buffer.append("00"); else if (milliseconds < 100) buffer.append("0");
                buffer.append(milliseconds);
                String time = buffer.toString();
                elapsedTime.setLabel(time);
                repaint();
            }
        }
    }

    /**
	 * When the canvas becomes visible notify the component it is
	 * going to be displayed.  This lets it initialize any resources
	 * it requires.
	 * 
	 * @see DeviceScreen#showNotify()
	 */
    public void showNotify() {
        elapsedTime.visible(true);
    }

    /**
	 * When the canvas is removed notify the component it is
	 * no longer displayed.  This lets it clean up any resources it
	 * used.
	 * 
	 * @see DeviceScreen#hideNotify()
	 */
    public void hideNotify() {
        elapsedTime.visible(false);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
	 * Paints the blank area of the canvas.  This just shows how long
	 * the timer has been running.
	 */
    protected synchronized void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        Theme theme = UIManager.getTheme();
        int[] dimensions = elapsedTime.getPreferredSize(theme, width, height);
        int labelHeight = dimensions[1];
        int labelY = (height - labelHeight) / 2;
        elapsedTime.paint(g, theme, this, 0, labelY, width, height, true);
    }
}
