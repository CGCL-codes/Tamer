package capitulo4.lowlevel;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

public class BufferedStopWatch extends MIDlet {

    public void startApp() {
        Display display = Display.getDisplay(this);
        display.setCurrent(new BufferedStopWatchCanvas(display, 10));
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean forced) {
    }

    class BufferedStopWatchCanvas extends Canvas implements Runnable {

        int degree = 360;

        long startTime;

        int seconds;

        Display display;

        Image offscreen;

        BufferedStopWatchCanvas(Display display, int seconds) {
            this.display = display;
            this.seconds = seconds;
            if (!isDoubleBuffered() && false) offscreen = Image.createImage(getWidth(), getHeight());
            startTime = System.currentTimeMillis();
        }

        public void paint(Graphics g) {
            Graphics g2 = offscreen == null ? g : offscreen.getGraphics();
            g2.setGrayScale(255);
            g2.fillRect(0, 0, getWidth(), getHeight());
            if (degree > 0) {
                g2.setColor(255, 0, 0);
                g2.fillArc(0, 0, getWidth(), getHeight(), 90, degree);
                display.callSerially(this);
            }
            g2.setGrayScale(0);
            g2.drawArc(0, 0, getWidth() - 1, getHeight() - 1, 0, 360);
            if (offscreen != null) g.drawImage(offscreen, 0, 0, Graphics.TOP | Graphics.RIGHT);
        }

        public void run() {
            int permille = (int) ((System.currentTimeMillis() - startTime) / seconds);
            degree = 360 - (permille * 360) / 1000;
            repaint();
        }
    }
}
