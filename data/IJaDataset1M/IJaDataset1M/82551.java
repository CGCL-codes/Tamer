package cn.easyact.tdl.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import cn.easyact.tdl.sprite.DefaultSprite;
import com.sonyericsson.junit.framework.TestCase;

public class DefaultSpriteTest extends TestCase {

    private static final int BLUE = 0x0000ff;

    private static final int GREEN = 0x00ff00;

    private DefaultSprite sprite;

    private Image test;

    private int[] actuals;

    private Image image;

    private Image testRange;

    public void setUp() throws Throwable {
        super.setUp();
        test = Image.createImage(1, 1);
        actuals = new int[1];
        testRange = Image.createImage(1, 2);
        image = Image.createImage(2, 2);
        Graphics g = image.getGraphics();
        g.setColor(GREEN);
        g.fillRect(0, 0, 1, 1);
        g.setColor(BLUE);
        g.fillRect(0, 1, 1, 1);
        sprite = new DefaultSprite();
        sprite.setImage(image, 1, 1);
    }

    public void tearDown() throws Throwable {
        super.tearDown();
    }

    public void testPaint() throws Exception {
        Image image = Image.createImage(1, 1);
        Graphics g = image.getGraphics();
        g.setColor(GREEN);
        g.fillRect(0, 0, 1, 1);
        sprite.setImage(image, 1, 1);
        sprite.paint(test.getGraphics());
        test.getRGB(actuals, 0, 1, 0, 0, 1, 1);
        int paintActual = actuals[0];
        assertEquals(0xff00ff00, paintActual);
    }

    public void testFrame() throws Exception {
        Image image = Image.createImage(2, 1);
        Graphics g = image.getGraphics();
        g.setColor(GREEN);
        g.fillRect(0, 0, 1, 1);
        sprite.setImage(image, 1, 1);
        assertEquals(0xff00ff00, paintActual());
        sprite.nextFrame();
        assertEquals(-1, paintActual());
        sprite.nextFrame();
        assertEquals(0xff00ff00, paintActual());
    }

    public void testFrameInColumn() throws Exception {
        assertEquals(0xff00ff00, paintActual());
        sprite.nextFrame();
        assertEquals(-1, paintActual());
        sprite.nextFrame();
        assertEquals(0xff0000ff, paintActual());
    }

    public void testRange() throws Exception {
        Image image = Image.createImage(1, 2);
        Graphics g = image.getGraphics();
        g.setColor(GREEN);
        g.fillRect(0, 0, 1, 2);
        sprite.setImage(image, 1, 1);
        sprite.paint(testRange.getGraphics());
        testRange.getRGB(actuals, 0, 1, 0, 1, 1, 1);
        assertEquals(-1, actuals[0]);
    }

    private int paintActual() {
        sprite.paint(test.getGraphics());
        test.getRGB(actuals, 0, 1, 0, 0, 1, 1);
        return actuals[0];
    }

    public void testZoomOut() throws Exception {
        image = Image.createImage(4, 4);
        Graphics g = image.getGraphics();
        g.setColor(GREEN);
        g.fillRect(2, 2, 2, 2);
        sprite.setImage(image, 2, 2);
        sprite.zoomOut(1, 1);
        assertEquals(1, sprite.getHeight());
        assertEquals(1, sprite.getWidth());
        sprite.nextFrame();
        sprite.nextFrame();
        sprite.nextFrame();
        sprite.paint(test.getGraphics());
        test.getRGB(actuals, 0, 1, 0, 0, 1, 1);
        assertEquals(GREEN | 0xff000000, actuals[0]);
    }
}
