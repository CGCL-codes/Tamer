package net.rptools.lib;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;

public class TaskBarFlasher {

    private static final int FLASH_DELAY = 500;

    private BufferedImage flashImage;

    private Image originalImage;

    private Frame frame;

    private FlashThread flashThread;

    public TaskBarFlasher(Frame frame) {
        this.frame = frame;
        originalImage = frame.getIconImage();
        flashImage = new BufferedImage(originalImage.getWidth(null), originalImage.getHeight(null), BufferedImage.OPAQUE);
        Graphics g = flashImage.getGraphics();
        g.setColor(Color.blue);
        g.fillRect(0, 0, flashImage.getWidth(), flashImage.getHeight());
        g.drawImage(originalImage, 0, 0, null);
        g.dispose();
    }

    public synchronized void flash() {
        if (flashThread != null) {
            return;
        }
        flashThread = new FlashThread();
        flashThread.start();
    }

    private class FlashThread extends Thread {

        public void run() {
            while (!frame.isFocused()) {
                try {
                    Thread.sleep(FLASH_DELAY);
                    frame.setIconImage(flashImage);
                    Thread.sleep(FLASH_DELAY);
                    frame.setIconImage(originalImage);
                } catch (InterruptedException ie) {
                    break;
                }
            }
            flashThread = null;
        }
    }
}
