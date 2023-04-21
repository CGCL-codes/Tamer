package org.one.stone.soup.grfxML;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.one.stone.soup.browser.Browser;
import org.one.stone.soup.grfx.ImageFactory;
import org.one.stone.soup.grfx.ImageLoadException;
import org.one.stone.soup.grfx.Pixel;
import org.one.stone.soup.math.IntMath;
import org.one.stone.soup.mjdb.data.field.BoolField;
import org.one.stone.soup.mjdb.data.field.FloatField;
import org.one.stone.soup.mjdb.data.field.IntField;
import org.one.stone.soup.mjdb.data.field.LockException;
import org.one.stone.soup.mjdb.data.field.Point2DField;
import org.one.stone.soup.xml.XmlElement;

/**
	*
	* @author Nik Cross
*/
public class GeometryImage extends Geometry {

    DataPoint windowField;

    DataPoint boundsField;

    boolean ready = false;

    BoolField visibleField;

    boolean visible;

    private DataColor colorField;

    private Color color;

    BoolField filledField;

    boolean filled;

    FloatField transparencyField;

    double currentTransparency = -1;

    BoolField rotatableField;

    IntField rotationField;

    Point2DField offsetField;

    Point2DField positionField;

    int x;

    int y;

    DataString fileField;

    String fileName = "not set";

    Point2DField sizeField;

    Point2DField sizeViewField;

    int w;

    int h;

    boolean scaled = true;

    Image image;

    Image fImage;

    public GeometryImage(GrfxMLEngine engine, DataPoint windowF, DataPoint boundsF, BoolField visibleF, DataColor colorF, BoolField filledF, FloatField transparencyF, BoolField rotatableF, IntField rotationF, Point2DField positionF, Point2DField offsetF, DataString fileF, Point2DField sizeF, Point2DField sizeViewF) {
        super(engine);
        windowField = windowF;
        boundsField = boundsF;
        visibleField = visibleF;
        colorField = colorF;
        filledField = filledF;
        transparencyField = transparencyF;
        rotatableField = rotatableF;
        rotationField = rotationF;
        positionField = positionF;
        offsetField = offsetF;
        fileField = fileF;
        sizeField = sizeF;
        sizeViewField = sizeViewF;
        process(true);
    }

    /**
 * contains method comment.
 */
    public boolean contains(Point2DField point, Point2DField pointView) {
        int pX = pointView.getValueX();
        int pY = pointView.getValueY();
        if (pX <= x + w && pY <= y + h && pX >= x && pY >= y) return true; else return false;
    }

    public void draw(Graphics grfx) {
        if (ready == false) return;
        if (visibleField.getValue() == false || transparencyField.getValue() == 1) return;
        if (visibleField.hasChanged()) process(true); else process(false);
        grfx.setClip(windowField.getValueXMin(), windowField.getValueYMin(), windowField.getValueX(), windowField.getValueY());
        grfx.setColor(color);
        if (transparencyField.getValue() != 0) {
            if (transparencyField.getValue() != currentTransparency) {
                fImage = makeTransparent(image, transparencyField.getValue());
                currentTransparency = transparencyField.getValue();
            }
        } else {
            fImage = image;
        }
        ((Graphics2D) grfx).rotate(IntMath.A_TO_R * rotationField.getValue(), positionField.getValueX(), positionField.getValueY());
        if (scaled == true) {
            grfx.drawImage(fImage, x, y, w, h, ImageFactory.DUMMY);
        } else {
            grfx.drawImage(fImage, x, y, ImageFactory.DUMMY);
        }
        ((Graphics2D) grfx).rotate(-(IntMath.A_TO_R * rotationField.getValue()), positionField.getValueX(), positionField.getValueY());
    }

    /**
 *
 * @return java.awt.Image
 */
    public Image getImage() {
        return image;
    }

    public void process(boolean forced) {
        if (forced == true) {
            visible = visibleField.getValue();
            color = GrfxMLProcessor.getColorFor(colorField);
            filled = filledField.getValue();
            x = positionField.getValueX() + offsetField.getValueX();
            y = positionField.getValueY() + offsetField.getValueY();
            boundsField.setValueXMin(x);
            boundsField.setValueYMin(y);
            if (!fileField.getValue().equals(fileName)) {
                try {
                    sizeField.setValueX(0, fileField);
                    sizeField.setValueY(0, fileField);
                } catch (LockException le) {
                }
                ready = false;
            }
            if (ready == false) {
                try {
                    try {
                        fileName = fileField.getValue();
                        if (fileName.length() == 0) {
                            return;
                        } else {
                            Object resource = Browser.getResource(fileField.getValue());
                            if (resource != null && resource instanceof Image) {
                                image = (Image) resource;
                            } else {
                                fileName = fileField.getValue();
                                image = ImageFactory.loadImage(Browser.getInputStream(fileField.getValue()));
                                Browser.storeResource(fileField.getValue(), (Object) image);
                                engine.registerResource(fileField.getValue());
                            }
                            ready = true;
                        }
                    } catch (IOException ue) {
                        System.out.println("Failed to load '" + fileField.getValue() + "' from file field " + fileField.getId() + ": " + ue.getMessage());
                    }
                } catch (ImageLoadException ie) {
                    System.out.println("Failed to load '" + fileField.getValue() + "' from file field " + fileField.getId() + ": " + ie.getMessage());
                }
            }
            try {
                if (sizeField.getValueX() == 0) sizeField.setValueX(image.getWidth(ImageFactory.DUMMY), this);
                if (sizeField.getValueY() == 0) sizeField.setValueY(image.getHeight(ImageFactory.DUMMY), this);
            } catch (LockException le) {
            }
            w = sizeViewField.getValueX();
            h = sizeViewField.getValueY();
            boundsField.setValueXMax(x + w);
            boundsField.setValueYMax(y + h);
            boundsField.update();
        } else {
            if (visibleField.hasChanged()) {
                visible = visibleField.getValue();
            }
            if (colorField.hasChanged()) {
                color = GrfxMLProcessor.getColorFor(colorField);
            }
            if (filledField.hasChanged()) {
                filled = filledField.getValue();
            }
            if (!fileField.getValue().equals(fileName)) {
                try {
                    try {
                        fileName = fileField.getValue();
                        image = ImageFactory.loadImage(Browser.getInputStream(fileField.getValue()));
                        ready = true;
                    } catch (IOException ue) {
                        ue.printStackTrace();
                    }
                } catch (ImageLoadException ie) {
                    ie.printStackTrace();
                }
                try {
                    sizeField.setValueX(image.getWidth(ImageFactory.DUMMY), this);
                    sizeField.setValueY(image.getHeight(ImageFactory.DUMMY), this);
                } catch (LockException le) {
                }
            }
            if (sizeViewField.hasChanged()) {
                w = sizeViewField.getValueX();
                h = sizeViewField.getValueY();
                if (image.getWidth(ImageFactory.DUMMY) == w && image.getHeight(ImageFactory.DUMMY) == h) scaled = false; else scaled = true;
                boundsField.setValueXMax(x + w);
                boundsField.setValueYMax(y + h);
                boundsField.update();
            }
            if (rotatableField.getValue() == true && rotationField.hasChanged() && rotationField.getValue() != 0) {
            }
            if (offsetField.hasChanged() || positionField.hasChanged()) {
                x = positionField.getValueX() + offsetField.getValueX();
                y = positionField.getValueY() + offsetField.getValueY();
                boundsField.setValueXMin(x);
                boundsField.setValueYMin(y);
                boundsField.setValueXMax(x + w);
                boundsField.setValueYMax(y + h);
                boundsField.update();
            }
        }
    }

    /**
 *
 * @param newImage java.awt.Image
 */
    public void setImage(Image newImage) {
        image = newImage;
        ready = true;
        try {
            sizeField.setValueX(image.getWidth(ImageFactory.DUMMY), this);
            sizeField.setValueY(image.getHeight(ImageFactory.DUMMY), this);
        } catch (LockException le) {
        }
    }

    /**
 *
 * @return java.lang.String
 */
    public XmlElement toXML() {
        XmlElement element = new XmlElement("Image");
        if (getId() != null) element.addAttribute("id", getId());
        element.addAttribute("visible", "" + visibleField.getValue());
        element.addAttribute("x", "" + x);
        element.addAttribute("y", "" + y);
        element.addAttribute("width", "" + w);
        element.addAttribute("height", "" + h);
        XmlElement bounds = element.addChild("Clip");
        bounds.addAttribute("x", "" + windowField.getValueXMin());
        bounds.addAttribute("y", "" + windowField.getValueYMin());
        bounds.addAttribute("width", "" + windowField.getValueX());
        bounds.addAttribute("height", "" + windowField.getValueY());
        if (w <= 0 || h <= 0) return element;
        int scan = w;
        int size = w * h;
        int[] data = new int[size];
        PixelGrabber pGrab = new PixelGrabber(image, 0, 0, w, h, data, 0, scan);
        try {
            pGrab.grabPixels();
        } catch (Exception e) {
        }
        XmlElement rgb = element.addChild("Rgb");
        StringBuffer buffer = null;
        try {
            String hexFileName = File.createTempFile("data-for-" + fileField.getRef(), "hex").getAbsolutePath();
            String datFileName = File.createTempFile("data-for-" + fileField.getRef(), "dat").getAbsolutePath();
            FileOutputStream oStream = new FileOutputStream(hexFileName);
            FileOutputStream oStreamD = new FileOutputStream(datFileName);
            for (int loop = 0; loop < data.length; loop++) {
                Pixel pixel = new Pixel(data[loop]);
                oStreamD.write(pixel.red);
                oStreamD.write(pixel.green);
                oStreamD.write(pixel.blue);
                if (loop % scan == 0) {
                    if (buffer != null) {
                        buffer.append('\n');
                        oStream.write(buffer.toString().getBytes());
                        oStream.flush();
                    }
                    buffer = new StringBuffer();
                }
                buffer.append(Integer.toHexString(pixel.red));
                buffer.append(Integer.toHexString(pixel.green));
                buffer.append(Integer.toHexString(pixel.blue));
                buffer.append(" ");
            }
            if (buffer != null && buffer.length() > 0) {
                buffer.append('\n');
                oStream.write(buffer.toString().getBytes());
            }
            oStreamD.flush();
            oStream.flush();
            oStream.close();
            oStreamD.close();
            rgb.addChild("original").addAttribute("fileName", fileName);
            rgb.addChild("externalData").addAttribute("fileName", datFileName);
        } catch (IOException ie) {
            ie.printStackTrace();
            rgb.addValue("Failed to write data for image " + fileName + " Exception: " + ie);
        }
        return element;
    }

    private Image makeTransparent(Image image, double transparency) {
        Canvas canvas = ImageFactory.DUMMY;
        int alpha = (int) (255.0 * transparency);
        int w = image.getWidth(canvas);
        int h = image.getHeight(canvas);
        int scan = w;
        int size = w * h;
        int[] cData = new int[size];
        PixelGrabber cGrab = new PixelGrabber(image, 0, 0, w, h, cData, 0, scan);
        try {
            cGrab.grabPixels();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Pixel pixel = new Pixel();
        for (int loop = 0; loop < cData.length; loop++) {
            pixel.setRGB(cData[loop]);
            pixel.alpha = alpha;
            cData[loop] = pixel.getRGB();
        }
        MemoryImageSource mis = new MemoryImageSource(w, h, cData, 0, scan);
        return canvas.getToolkit().createImage(mis);
    }

    public boolean hasChanged() {
        if (windowField.hasChanged() == true || boundsField.hasChanged() == true || visibleField.hasChanged() == true || transparencyField.hasChanged() == true || colorField.hasChanged() == true || filledField.hasChanged() == true || positionField.hasChanged() == true || offsetField.hasChanged() == true || rotatableField.hasChanged() == true || rotationField.hasChanged() == true || positionField.hasChanged() == true || offsetField.hasChanged() == true || fileField.hasChanged() == true || sizeField.hasChanged() == true || sizeViewField.hasChanged() == true) {
            return true;
        }
        return false;
    }

    public void resetChanged() {
        windowField.resetChanged();
        boundsField.resetChanged();
        visibleField.resetChanged();
        transparencyField.resetChanged();
        colorField.resetChanged();
        filledField.resetChanged();
        positionField.resetChanged();
        offsetField.resetChanged();
        rotatableField.resetChanged();
        rotationField.resetChanged();
        positionField.resetChanged();
        offsetField.resetChanged();
        fileField.resetChanged();
        sizeField.resetChanged();
        sizeViewField.resetChanged();
    }
}
