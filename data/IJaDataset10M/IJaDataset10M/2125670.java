package Threads;

import java.awt.*;
import Map.ScreenManager;

public class Flash implements Runnable {

    private boolean running = true;

    private Graphics g = ScreenManager.getGraphics();

    public void stop() {
        running = false;
    }

    public void run() {
        while (running) {
            paint(g);
            IntroScreen.checkInput();
            stop();
        }
    }

    public static void paint(Graphics g) {
        try {
            g.setColor(Color.black);
            g.fillRect(0, 0, 800, 600);
            g.setFont(new Font("Times New Roman", Font.PLAIN, 72));
            g.setColor(Color.white);
            g.drawString("Secret of Java", 200, 100);
            g.setColor(Color.white);
            g.setFont(new Font("Dialog", Font.PLAIN, 24));
            g.drawString("Press Enter", 325, 400);
            ScreenManager.update();
            IntroScreen.checkInput();
            Thread.sleep(375);
            g.dispose();
        } catch (InterruptedException IE) {
        }
    }
}
