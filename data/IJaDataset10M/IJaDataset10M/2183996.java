package com.siemens.mp.game;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import org.microemu.device.DeviceDisplay;
import org.microemu.device.DeviceFactory;

public class ExtendedImage extends com.siemens.mp.misc.NativeMem {

    Image image;

    public ExtendedImage(Image image) {
        this.image = image;
    }

    public void blitToScreen(int x, int y) {
        DeviceDisplay dd = DeviceFactory.getDevice().getDeviceDisplay();
        Image image = dd.getDisplayImage();
        Graphics g = image.getGraphics();
        g.drawImage(this.image, x, y, Graphics.LEFT | Graphics.TOP);
        dd.repaint(0, 0, image.getWidth(), image.getHeight());
    }

    public void clear(byte color) {
        Graphics g = image.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
    }

    public Image getImage() {
        return image;
    }

    public int getPixel(int x, int y) {
        System.out.println("int getPixel(int x, int y)");
        return 1;
    }

    public void getPixelBytes(byte[] pixels, int x, int y, int width, int height) {
        System.out.println("void getPixelBytes(byte[] pixels, int x, int y, int width, int height)");
    }

    public void setPixel(int x, int y, byte color) {
        System.out.println("void setPixel(int x, int y, byte color)");
    }

    public void setPixels(byte[] pixels, int x, int y, int width, int height) {
        Image img = com.siemens.mp.ui.Image.createImageFromBitmap(pixels, width, height);
        image.getGraphics().drawImage(img, x, y, 0);
    }
}
