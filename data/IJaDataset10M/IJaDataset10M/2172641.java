package java.awt;

import java.util.Vector;
import java.awt.image.ImageProducer;
import java.awt.image.ImageObserver;
import java.awt.image.ImageConsumer;
import java.awt.image.ColorModel;

/** An offscreen image created by Component.createImage(int width, int height). As it is not
 possible to create a graphics from an image produced
 through the image producer/consumer model this class exists to allow us to get the graphics
 for an offscreen image. */
class MWOffscreenImage extends MWImage implements ImageProducer {

    Color foreground, background;

    Font font;

    private int originX, originY;

    private ImageConsumer theConsumer;

    private java.util.Hashtable props = new java.util.Hashtable();

    MWOffscreenImage(Component component, int width, int height, MWGraphicsConfiguration gc) {
        super(width, height, gc);
        if (component != null) {
            foreground = component.getForeground();
            background = component.getBackground();
            font = component.getFont();
        }
        setProperties(props);
        Graphics g = getGraphics();
        g.clearRect(0, 0, width, height);
        g.dispose();
    }

    public void flush() {
    }

    ;

    public Graphics getGraphics() {
        return new MWGraphics(this);
    }

    public ImageProducer getSource() {
        return this;
    }

    public synchronized void addConsumer(ImageConsumer ic) {
        theConsumer = ic;
        startProduction(ic);
    }

    public synchronized boolean isConsumer(ImageConsumer ic) {
        return (ic == theConsumer);
    }

    public synchronized void removeConsumer(ImageConsumer ic) {
        if (theConsumer == ic) theConsumer = null;
    }

    public void requestTopDownLeftRightResend(ImageConsumer ic) {
    }

    public void startProduction(ImageConsumer ic) {
        if (ic == null) return;
        int[] rgbArray;
        ColorModel cm = ColorModel.getRGBdefault();
        ic.setDimensions(width, height);
        ic.setColorModel(cm);
        rgbArray = getRGB(originX, originY, width, height, null, 0, width);
        ic.setPixels(0, 0, width, height, cm, rgbArray, 0, width);
        ic.imageComplete(ImageConsumer.SINGLEFRAMEDONE);
    }
}
