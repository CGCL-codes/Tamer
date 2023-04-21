package com.ixora.common.ui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.net.URL;
import javax.swing.JWindow;

/**
 * @author Daniel Moraru - initial internet source
 */
public final class AppSplashScreen extends Frame {

    private static final long serialVersionUID = -927585921379362665L;

    private final String fImageId;

    private MediaTracker fMediaTracker;

    private Image fImage;

    /**
  * @param aImageId
  */
    public AppSplashScreen(String aImageId) {
        if (aImageId == null || aImageId.trim().length() == 0) {
            throw new IllegalArgumentException("Image Id does not have content.");
        }
        fImageId = aImageId;
    }

    /**
  * Show the splash screen to the end user.
  */
    public void splash() {
        initImageAndTracker();
        setSize(fImage.getWidth(null), fImage.getHeight(null));
        center();
        fMediaTracker.addImage(fImage, 0);
        try {
            fMediaTracker.waitForID(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new SplashWindow(this, fImage);
    }

    /**
   *
   */
    private void initImageAndTracker() {
        fMediaTracker = new MediaTracker(this);
        URL imageURL = AppSplashScreen.class.getResource(fImageId);
        fImage = Toolkit.getDefaultToolkit().getImage(imageURL);
    }

    /**
  * Centers the frame on the screen.
  */
    private void center() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle frame = getBounds();
        setLocation((screen.width - frame.width) / 2, (screen.height - frame.height) / 2);
    }

    /**
	*/
    private class SplashWindow extends JWindow {

        private static final long serialVersionUID = -3152704968677454464L;

        private Image fImage;

        private BufferedImage fBackgroundImage;

        private static final int SHADOW_WIDTH = 10;

        SplashWindow(Frame aParent, Image aImage) {
            super(aParent);
            fImage = aImage;
            setSize(fImage.getWidth(null), fImage.getHeight(null));
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle window = getBounds();
            setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
            setVisible(true);
        }

        public void paint(Graphics g) {
            g.drawImage(fBackgroundImage, 0, 0, this);
        }

        public void setVisible(boolean b) {
            createShadowBorder();
            super.setVisible(b);
            setSize(getWidth() + SHADOW_WIDTH, getHeight() + SHADOW_WIDTH);
        }

        private void createShadowBorder() {
            fBackgroundImage = new BufferedImage(getWidth() + SHADOW_WIDTH, getHeight() + SHADOW_WIDTH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) fBackgroundImage.getGraphics();
            try {
                Robot robot = new Robot(getGraphicsConfiguration().getDevice());
                BufferedImage capture = robot.createScreenCapture(new Rectangle(getX(), getY(), getWidth() + SHADOW_WIDTH, getHeight() + SHADOW_WIDTH));
                g2.drawImage(capture, null, 0, 0);
            } catch (AWTException e) {
                e.printStackTrace();
            }
            BufferedImage shadow = new BufferedImage(getWidth() + SHADOW_WIDTH, getHeight() + SHADOW_WIDTH, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = shadow.getGraphics();
            graphics.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
            graphics.fillRect(SHADOW_WIDTH / 2, SHADOW_WIDTH / 2, getWidth(), getHeight());
            g2.drawImage(shadow, getBlurOp(SHADOW_WIDTH / 2), 0, 0);
            if (fImage != null) {
                g2.drawImage(fImage, 0, 0, null);
            }
        }

        private ConvolveOp getBlurOp(int size) {
            float[] data = new float[size * size];
            float value = 1 / (float) (size * size);
            for (int i = 0; i < data.length; i++) {
                data[i] = value;
            }
            return new ConvolveOp(new Kernel(size, size, data));
        }
    }
}
