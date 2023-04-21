package org.scohen.juploadr.ui;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.scohen.juploadr.app.ImageAttributes;

/**
 * @author steve
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class MultiSelectThumbnail extends Canvas {

    private static final int BORDER_WIDTH = 5;

    private List<ImageAttributes> atts;

    private Point mySize = new Point(256, 192);

    private int totalArea = mySize.x * mySize.y;

    private Point thumbSize;

    private Point minSize = new Point(80, 60);

    private ReusableUIFactory uiFactory = ReusableUIFactory.getInstance();

    public MultiSelectThumbnail(Composite parent, List<ImageAttributes> atts) {
        super(parent, SWT.NONE);
        this.atts = new LinkedList<ImageAttributes>(atts);
        thumbSize = getThumbnailSize();
        addMouseTrackListener(new MouseTrackAdapter() {

            public void mouseEnter(MouseEvent event) {
                MultiSelectThumbnail.this.mouseEnter(event);
            }

            public void mouseExit(MouseEvent event) {
                MultiSelectThumbnail.this.mouseExit(event);
            }
        });
        addMouseMoveListener(new MouseMoveListener() {

            public void mouseMove(MouseEvent arg0) {
                MultiSelectThumbnail.this.mouseMove(arg0);
            }
        });
        addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                MultiSelectThumbnail.this.paintControl(e);
            }
        });
    }

    public Point computeSize(int w, int h, boolean changed) {
        return mySize;
    }

    public Point computeSize(int x, int y) {
        return computeSize(x, y, true);
    }

    /**
     * @param event
     */
    void paintControl(PaintEvent e) {
        GC gc = e.gc;
        int xBounds = mySize.x;
        int yBounds = mySize.y;
        int yFraction = yBounds;
        int xFraction = xBounds;
        if (atts.size() > 1) {
            yFraction = (int) Math.floor((yBounds - thumbSize.y) / (atts.size() - 1));
            xFraction = (xBounds - thumbSize.x) / (atts.size() - 1);
        }
        int xPos = xFraction * (atts.size() - 1);
        int yPos = yFraction * (atts.size() - 1);
        Iterator iter = atts.iterator();
        ImageData shadowData = new ImageData(thumbSize.x - 5, thumbSize.y - 5, 24, new PaletteData(255, 255, 255));
        shadowData.alpha = 90;
        Color white = uiFactory.getWhiteColor();
        Image shadow = new Image(e.display, shadowData);
        GC shadowGC = new GC(shadow);
        shadowGC.setForeground(white);
        while (iter.hasNext()) {
            ImageAttributes element = (ImageAttributes) iter.next();
            drawThumbnail(element, shadow, e.display, gc, xPos, yPos);
            xPos -= xFraction;
            yPos -= yFraction;
        }
        shadow.dispose();
        shadowGC.dispose();
        setSize(computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    private void drawThumbnail(ImageAttributes img, Image shadow, Display d, GC gc, int x, int y) {
        ImageData tnData;
        if (img.getRotation() != 0) {
            Image copy = copyImage(img.getThumbnail(), d);
            ImageData origData = copy.getImageData();
            byte[] data = new byte[origData.data.length];
            System.arraycopy(origData.data, 0, data, 0, origData.data.length);
            tnData = new ImageData(origData.width, origData.height, origData.depth, origData.palette, origData.bytesPerLine, data);
            tnData = Transforms.rotate(tnData, img.getRotation());
            copy.dispose();
        } else {
            tnData = img.getThumbnail().getImageData();
        }
        Image toDraw = new Image(d, tnData);
        Color borderColor = new Color(Display.getDefault(), 205, 205, 205);
        Color photoColor = new Color(Display.getDefault(), 250, 250, 250);
        if (shadow != null) {
            int offset = (int) (thumbSize.x * .07);
            gc.drawImage(shadow, x + offset, y + offset);
        }
        gc.setForeground(borderColor);
        gc.setBackground(photoColor);
        gc.fillRectangle(x, y, thumbSize.x - 1, thumbSize.y - 1);
        gc.drawRectangle(x, y, thumbSize.x - 1, thumbSize.y - 1);
        gc.drawImage(toDraw, 0, 0, tnData.width, tnData.height, x + BORDER_WIDTH, y + BORDER_WIDTH, thumbSize.x - (BORDER_WIDTH * 2), thumbSize.y - (BORDER_WIDTH * 2));
        borderColor.dispose();
        photoColor.dispose();
        toDraw.dispose();
    }

    private Image copyImage(Image src, Display disp) {
        return new Image(disp, src, SWT.IMAGE_COPY);
    }

    /**
     * @param atts2
     * @return
     */
    private Point getThumbnailSize() {
        int individualThumbArea = (int) Math.floor(totalArea / atts.size());
        int x = (int) Math.floor(Math.sqrt(individualThumbArea / 12));
        thumbSize = new Point(4 * x, 3 * x);
        if (thumbSize.x < minSize.x) {
            thumbSize = minSize;
        }
        return thumbSize;
    }

    private void mouseEnter(MouseEvent e) {
    }

    private void mouseExit(MouseEvent e) {
        redraw();
    }

    private void mouseMove(MouseEvent event) {
    }
}
