package com.javaeedev.midp.gtalk;

import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * Welcome screen that display a logo.
 * 
 * @author Michael Liao (askxuefeng@gmail.com)
 */
public class SplashCanvas extends Canvas implements Runnable {

    private Image logo = null;

    private Thread waitingThread;

    public SplashCanvas() {
        try {
            logo = Image.createImage("/logo.png");
        } catch (IOException e) {
            throw new Error("Failed loading resource.");
        }
        setFullScreenMode(true);
        waitingThread = new Thread(this);
        waitingThread.start();
    }

    protected void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        g.setColor(0xffffff);
        g.fillRect(0, 0, w, h);
        g.drawImage(logo, w / 2, h / 2, Graphics.HCENTER | Graphics.VCENTER);
    }

    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        GTalkMIDlet.switchUI(new LoginForm());
    }

    protected void keyPressed(int keyCode) {
        waitingThread.interrupt();
    }

    protected void pointerPressed(int x, int y) {
        waitingThread.interrupt();
    }
}
