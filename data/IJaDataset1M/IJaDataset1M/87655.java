package org.jmol.g3d;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.image.ImageProducer;
import java.awt.image.ImageConsumer;
import java.awt.image.ColorModel;

/**
 *<p>
 * Implementation of Platform3D when using AWT on 1.1 JVMs.
 *</p>
 *<p>
 * Uses the AWT imaging routines to convert an int[] of ARGB values
 * into an Image by implementing the ImageProducer interface.
 *</p>
 *<p>
 * This is used by MSFT Internet Explorer with the MSFT JVM,
 * and Netscape 4.* on both Win32 and MacOS 9.
 *</p>
 *
 * @author Miguel, miguel@jmol.org
 */
final class Awt3D extends Platform3D implements ImageProducer {

    Component component;

    ColorModel colorModelRGB;

    ImageConsumer ic;

    Awt3D(Component component) {
        this.component = component;
        colorModelRGB = Toolkit.getDefaultToolkit().getColorModel();
    }

    Image allocateImage() {
        return component.createImage(this);
    }

    void notifyEndOfRendering() {
        if (this.ic != null) startProduction(ic);
    }

    Image allocateOffscreenImage(int width, int height) {
        Image img = component.createImage(width, height);
        return img;
    }

    Graphics getGraphics(Image image) {
        return image.getGraphics();
    }

    public synchronized void addConsumer(ImageConsumer ic) {
        startProduction(ic);
    }

    public boolean isConsumer(ImageConsumer ic) {
        return (this.ic == ic);
    }

    public void removeConsumer(ImageConsumer ic) {
        if (this.ic == ic) this.ic = null;
    }

    public void requestTopDownLeftRightResend(ImageConsumer ic) {
    }

    public void startProduction(ImageConsumer ic) {
        if (this.ic != ic) {
            this.ic = ic;
            ic.setDimensions(windowWidth, windowHeight);
            ic.setHints(ImageConsumer.TOPDOWNLEFTRIGHT | ImageConsumer.COMPLETESCANLINES | ImageConsumer.SINGLEPASS);
        }
        ic.setPixels(0, 0, windowWidth, windowHeight, colorModelRGB, pBuffer, 0, windowWidth);
        ic.imageComplete(ImageConsumer.SINGLEFRAMEDONE);
    }
}
