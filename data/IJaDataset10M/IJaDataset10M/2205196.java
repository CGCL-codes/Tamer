package org.ofbiz.base.splash;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

public final class SplashScreen extends Frame {

    private final String fImageId;

    private MediaTracker fMediaTracker;

    private Window splashWindow;

    private Image fImage;

    public SplashScreen(String aImageId) {
        if (aImageId == null || aImageId.trim().length() == 0) {
            throw new IllegalArgumentException("Image Id does not have content.");
        }
        fImageId = aImageId;
    }

    public void splash() {
        initImageAndTracker();
        setSize(fImage.getWidth(null), fImage.getHeight(null));
        center();
        fMediaTracker.addImage(fImage, 0);
        try {
            fMediaTracker.waitForID(0);
        } catch (InterruptedException ie) {
            System.out.println("Cannot track image load.");
        }
        splashWindow = new SplashWindow(this, fImage);
    }

    public void close() {
        this.dispose();
        splashWindow.dispose();
        splashWindow = null;
    }

    private void initImageAndTracker() {
        fMediaTracker = new MediaTracker(this);
        fImage = Toolkit.getDefaultToolkit().getImage(fImageId);
    }

    private void center() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle frame = getBounds();
        setLocation((screen.width - frame.width) / 2, (screen.height - frame.height) / 2);
    }

    private class SplashWindow extends Window {

        private Image fImage;

        public SplashWindow(Frame aParent, Image aImage) {
            super(aParent);
            fImage = aImage;
            setSize(fImage.getWidth(null), fImage.getHeight(null));
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle window = getBounds();
            setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
            setVisible(true);
        }

        public void paint(Graphics graphics) {
            if (fImage != null) {
                graphics.drawImage(fImage, 0, 0, this);
            }
        }
    }
}
