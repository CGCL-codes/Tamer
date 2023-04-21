package org.apache.harmony.awt.tests.image;

import java.net.URL;
import javax.swing.ImageIcon;
import com.google.code.appengine.awt.Frame;
import com.google.code.appengine.awt.Image;
import com.google.code.appengine.awt.MediaTracker;
import com.google.code.appengine.awt.Toolkit;
import com.google.code.appengine.awt.image.BufferedImage;
import junit.framework.TestCase;

public class BufferedImageTest extends TestCase {

    private final int EXP_WIDTH = 320;

    private final int EXP_HEIGHT = 182;

    public void testJpg() throws InterruptedException {
        decodeImage("utest.jpg");
    }

    public void testGif() throws InterruptedException {
        decodeImage("utest.gif");
    }

    public void testPng() throws InterruptedException {
        decodeImage("utest.png");
    }

    private final ClassLoader c = ClassLoader.getSystemClassLoader();

    private Image createImage(String name) {
        final URL path = c.getResource("../resources/images/" + name);
        assertNotNull("Resource not found: " + name, path);
        return Toolkit.getDefaultToolkit().createImage(path);
    }

    private void decodeImage(String name) throws InterruptedException {
        final Image im = createImage(name);
        final BufferedImage bim = new BufferedImage(EXP_WIDTH, EXP_HEIGHT, BufferedImage.TYPE_INT_RGB);
        final Frame f = new Frame();
        final MediaTracker t = new MediaTracker(f);
        t.addImage(im, 0);
        t.waitForAll();
        assertEquals(EXP_WIDTH, im.getWidth(null));
        assertEquals(EXP_HEIGHT, im.getHeight(null));
        bim.getGraphics().drawImage(im, 0, 0, null);
        int rgbVal = bim.getRGB(0, 0);
        assertEquals(0xFFFFFFFF, rgbVal);
    }

    /**
	 * Regression test for HARMONY-3602
	 */
    public void testTerminate() {
        final Image img = createImage("test.gif");
        img.flush();
        assertFalse("Current thread is interrupted", Thread.currentThread().isInterrupted());
    }
}
