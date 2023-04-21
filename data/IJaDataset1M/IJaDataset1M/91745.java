package com.ivis.xprocess.ui.diagram.print;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

public class ImagePrintableFigure implements PrintableFigure {

    private final Image myImage;

    public ImagePrintableFigure(final Image image) {
        ImageData d = image.getImageData();
        d.transparentPixel = -1;
        myImage = new Image(image.getDevice(), d);
    }

    public Dimension getSize() {
        ImageData imageData = myImage.getImageData();
        return new Dimension(imageData.width, imageData.height);
    }

    public void paint(Graphics graphics) {
        graphics.drawImage(myImage, new Point(0, 0));
    }

    public void dispose() {
    }
}
