package cubem;

import java.util.Random;
import javax.microedition.lcdui.*;

/**
 * @author Ewan
 */
public class PllCanvas extends TimerCanvas {

    /**
     * constructor
     */
    public PllCanvas() {
        super();
        fnt = Font.getFont(Font.FONT_STATIC_TEXT, Font.STYLE_BOLD, Font.SIZE_LARGE);
        fontHeight = fnt.getHeight() + 2;
        fntLaps = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        lapFontHeight = fntLaps.getHeight();
        pll = -1;
        generator = new Random(System.currentTimeMillis());
    }

    /**
     * Variables
     */
    protected int pll;

    protected Random generator;

    protected Font fnt;

    protected Font fntLaps;

    protected int fontHeight;

    protected int lapFontHeight;

    /**
     * Starts the next pll showing with a random pll
     */
    protected void startPll() {
        pll = -1;
        repaint();
        pll = getRandomPll();
        startTimer();
        parent.updatePllCanvasCommands();
    }

    /**
     * Gets a random pll (0 - 20)
     * @return the index of the pll, 0 <= idx <= 20
     */
    protected int getRandomPll() {
        if (parent.confPllRealProbability) {
            int i = Math.abs(generator.nextInt() % 72);
            if (0 <= i && i < 4) return 0;
            if (4 <= i && i < 8) return 1;
            if (8 <= i && i < 10) return 2;
            if (10 <= i && i < 12) return 3;
            if (12 <= i && i < 13) return 4;
            if (13 <= i && i < 17) return 5;
            if (17 <= i && i < 21) return 6;
            if (21 <= i && i < 25) return 7;
            if (25 <= i && i < 29) return 8;
            if (29 <= i && i < 33) return 9;
            if (33 <= i && i < 37) return 10;
            if (37 <= i && i < 41) return 11;
            if (41 <= i && i < 45) return 12;
            if (45 <= i && i < 49) return 13;
            if (49 <= i && i < 53) return 14;
            if (53 <= i && i < 57) return 15;
            if (57 <= i && i < 61) return 16;
            if (61 <= i && i < 65) return 17;
            if (65 <= i && i < 66) return 18;
            if (66 <= i && i < 67) return 19;
            if (67 <= i && i < 71) return 20;
        }
        return Math.abs(generator.nextInt() % 21);
    }

    /**
     * Starts the timer
     * Override the regular timer start method as we want a
     * certain lead in time before counting
     */
    public void startTimer() {
        super.startTimer();
        timeStarted += parent.confPllWaitTime;
        parent.updatePllCanvasCommands();
    }

    /**
     * Stops the timer
     * Override the regular timer stop method as we want to update
     * pllCanvas commands
     */
    public void stopTimer() {
        super.stopTimer();
        parent.updatePllCanvasCommands();
    }

    /**
     * Paints the PllCanvas
     */
    public void paint(Graphics g) {
        String toPaint = "00:00.00";
        long currTime = 0;
        if (status != STATUS_NOT_STARTED) {
            switch(status) {
                case STATUS_STOPPED:
                    currTime = timeStopped;
                    break;
                case STATUS_RUNNING:
                    currTime = System.currentTimeMillis();
                    break;
            }
            toPaint = Solve.convertToTimeString(timeStarted, currTime);
        }
        g.setColor(COLOUR_BACKGROUND);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(COLOUR_FOREGROUND);
        g.setFont(fnt);
        if (status == STATUS_NOT_STARTED) {
            g.drawString("Press * to begin", 0, 2 * fontHeight, Graphics.TOP | Graphics.LEFT);
        } else if (status == STATUS_STOPPED) {
            if (parent.confPllShowName) g.drawString(pllNames[pll], 0, 2 * fontHeight, Graphics.TOP | Graphics.LEFT);
            g.drawString("Press * to continue", 0, 3 * fontHeight, Graphics.TOP | Graphics.LEFT);
            g.setFont(fntLaps);
            g.drawString("Show Names: " + (parent.confPllShowName ? "true" : "false"), 0, 5 * fontHeight + 0 * lapFontHeight, Graphics.TOP | Graphics.LEFT);
            g.drawString("Real Probabilities: " + (parent.confPllRealProbability ? "true" : "false"), 0, 5 * fontHeight + 1 * lapFontHeight, Graphics.TOP | Graphics.LEFT);
            g.drawString("Wait time: " + Long.toString(parent.confPllWaitTime) + "ms", 0, 5 * fontHeight + 2 * lapFontHeight, Graphics.TOP | Graphics.LEFT);
        } else if (currTime < timeStarted) {
            g.drawString("Wait " + Long.toString(parent.confPllWaitTime) + "ms", 0, 2 * fontHeight, Graphics.TOP | Graphics.LEFT);
        } else {
            if (parent.confPllShowName) g.drawString(pllNames[pll], 0, 2 * fontHeight, Graphics.TOP | Graphics.LEFT);
            g.drawImage(parent.pllImages[pll], getWidth() / 2, 3 * fontHeight, Graphics.TOP | Graphics.HCENTER);
        }
        paintCurrentTime(g, toPaint);
    }

    /**
     * Paints the given string to the current time region of the screen
     * @param g the Graphics object for the canvas
     * @param toPaint the string to paint
     */
    protected void paintCurrentTime(Graphics g, String toPaint) {
        g.setColor(COLOUR_BACKGROUND);
        g.fillRect(0, 0, getWidth(), fontHeight);
        g.setColor(COLOUR_FOREGROUND);
        g.setFont(fnt);
        g.drawString(toPaint, getWidth() / 2, 0, Graphics.TOP | Graphics.HCENTER);
    }

    /**
     * Called when a key is pressed.
     */
    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);
        boolean done = false;
        switch(action) {
            case FIRE:
                if (status == STATUS_RUNNING) stopTimer(); else if (status == STATUS_NOT_STARTED) startPll(); else if (status == STATUS_STOPPED) startPll();
                done = true;
                break;
            case GAME_A:
            case GAME_B:
            case GAME_C:
            case GAME_D:
                if (status == STATUS_RUNNING) {
                    stopTimer();
                    done = true;
                }
                break;
            case DOWN:
                if (status != STATUS_RUNNING) {
                    if (parent.confPllWaitTime > 200) parent.confPllWaitTime -= 200;
                    done = true;
                }
                break;
            case UP:
                if (status != STATUS_RUNNING) {
                    if (parent.confPllWaitTime < 3000) parent.confPllWaitTime += 200;
                    done = true;
                }
                break;
        }
        if (!done) {
            if ((keyCode == KEY_POUND) || (keyCode == KEY_STAR)) {
                if (status == STATUS_RUNNING) stopTimer(); else if (status == STATUS_NOT_STARTED) startPll(); else if (status == STATUS_STOPPED) startPll();
            }
        }
        parent.updatePllCanvasCommands();
        repaint();
    }

    /**
     * override superclass method so double events aren't triggered
     */
    protected void keyReleased(int keyCode) {
    }

    /**
    * Resets the timer
    */
    public void reset() {
        super.reset();
        pll = -1;
    }

    public static final String[] pllNames = { "A1", "A2", "E", "Z", "H", "U1", "U2", "J1", "J2", "T", "R1", "R2", "F", "G1", "G2", "G3", "G4", "V", "N1", "N2", "Y" };
}
