package capitulo3.highlevel.game.simplegame;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

public class SimpleGameCanvas extends GameCanvas implements Runnable {

    private volatile boolean mTrucking;

    private long mFrameDelay;

    private int mX, mY;

    private int mState;

    public SimpleGameCanvas() {
        super(true);
        mX = getWidth() / 2;
        mY = getHeight() / 2;
        mState = 0;
        mFrameDelay = 20;
    }

    public void start() {
        mTrucking = true;
        Thread t = new Thread(this);
        t.start();
    }

    public void stop() {
        mTrucking = false;
    }

    public void run() {
        Graphics g = getGraphics();
        while (mTrucking == true) {
            tick();
            input();
            render(g);
            try {
                Thread.sleep(mFrameDelay);
            } catch (InterruptedException ie) {
                stop();
            }
        }
    }

    private void tick() {
        mState = (mState + 1) % 20;
    }

    private void input() {
        int keyStates = getKeyStates();
        if ((keyStates & LEFT_PRESSED) != 0) mX = Math.max(0, mX - 1);
        if ((keyStates & RIGHT_PRESSED) != 0) mX = Math.min(getWidth(), mX + 1);
        if ((keyStates & UP_PRESSED) != 0) mY = Math.max(0, mY - 1);
        if ((keyStates & DOWN_PRESSED) != 0) mY = Math.min(getHeight(), mY + 1);
    }

    private void render(Graphics g) {
        g.setColor(0xffffff);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(0x0000ff);
        g.drawLine(mX, mY, mX - 10 + mState, mY - 10);
        g.drawLine(mX, mY, mX + 10, mY - 10 + mState);
        g.drawLine(mX, mY, mX + 10 - mState, mY + 10);
        g.drawLine(mX, mY, mX - 10, mY + 10 - mState);
        flushGraphics();
    }
}
