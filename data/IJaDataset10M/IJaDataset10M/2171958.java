package shu.jai.jaistuff.display;

import javax.media.jai.iterator.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.media.jai.*;
import com.sun.media.jai.widget.*;

/**
 * This class shows how one can extend the DisplayJAI class. We'll override the
 * mouseMoved method of DisplayJAI so when the mouse is moved, some information
 * about the pixel beneath the mouse will be stored (but not displayed).
 */
public class DisplayJAIWithPixelInfo extends DisplayJAI implements MouseMotionListener {

    private StringBuffer pixelInfo;

    private double[] dpixel;

    private int[] ipixel;

    private boolean isDoubleType;

    private RandomIter readIterator;

    private boolean isIndexed;

    private short[][] lutData;

    protected int width, height;

    /**
   * The constructor of the class, which creates the arrays and instances needed
   * to obtain the image data and registers the class to listen to mouse motion
   * events.
   * @param image a RenderedImage for display
   */
    public DisplayJAIWithPixelInfo(RenderedImage image) {
        super(image);
        readIterator = RandomIterFactory.create(image, null);
        width = image.getWidth();
        height = image.getHeight();
        int dataType = image.getSampleModel().getDataType();
        switch(dataType) {
            case DataBuffer.TYPE_BYTE:
            case DataBuffer.TYPE_SHORT:
            case DataBuffer.TYPE_USHORT:
            case DataBuffer.TYPE_INT:
                isDoubleType = false;
                break;
            case DataBuffer.TYPE_FLOAT:
            case DataBuffer.TYPE_DOUBLE:
                isDoubleType = true;
                break;
        }
        if (isDoubleType) {
            dpixel = new double[image.getSampleModel().getNumBands()];
        } else {
            ipixel = new int[image.getSampleModel().getNumBands()];
        }
        isIndexed = (image.getColorModel() instanceof IndexColorModel);
        if (isIndexed) {
            IndexColorModel icm = (IndexColorModel) image.getColorModel();
            int mapSize = icm.getMapSize();
            byte[][] templutData = new byte[3][mapSize];
            icm.getReds(templutData[0]);
            icm.getGreens(templutData[1]);
            icm.getBlues(templutData[2]);
            lutData = new short[3][mapSize];
            for (int entry = 0; entry < mapSize; entry++) {
                lutData[0][entry] = templutData[0][entry] > 0 ? templutData[0][entry] : (short) (templutData[0][entry] + 256);
                lutData[1][entry] = templutData[1][entry] > 0 ? templutData[1][entry] : (short) (templutData[1][entry] + 256);
                lutData[2][entry] = templutData[2][entry] > 0 ? templutData[2][entry] : (short) (templutData[2][entry] + 256);
            }
        }
        addMouseMotionListener(this);
        pixelInfo = new StringBuffer(50);
    }

    /**
   * This method will be called when the mouse is moved over the image being
   * displayed.
   * @param me the mouse event that caused the execution of this method.
   */
    public void mouseMoved(MouseEvent me) {
        pixelInfo.setLength(0);
        int x = me.getX();
        int y = me.getY();
        if ((x >= width) || (y >= height)) {
            pixelInfo.append("No data!");
            return;
        }
        if (isDoubleType) {
            pixelInfo.append("(floating-point data) ");
            readIterator.getPixel(me.getX(), me.getY(), dpixel);
            for (int b = 0; b < dpixel.length; b++) {
                pixelInfo.append(dpixel[b] + ",");
            }
            pixelInfo = pixelInfo.deleteCharAt(pixelInfo.length() - 1);
        } else {
            if (isIndexed) {
                pixelInfo.append("(integer data with colormap) ");
                readIterator.getPixel(me.getX(), me.getY(), ipixel);
                pixelInfo.append("Index: " + ipixel[0]);
                pixelInfo.append(" RGB:" + lutData[0][ipixel[0]] + "," + lutData[1][ipixel[0]] + "," + lutData[2][ipixel[0]]);
            } else {
                pixelInfo.append("(integer data) ");
                readIterator.getPixel(me.getX(), me.getY(), ipixel);
                for (int b = 0; b < ipixel.length; b++) {
                    pixelInfo.append(ipixel[b] + ",");
                }
                pixelInfo = pixelInfo.deleteCharAt(pixelInfo.length() - 1);
            }
        }
    }

    /**
   * This method allows external classes access to the pixel info which was
   * obtained in the mouseMoved method.
   * @return the pixel information, formatted as a string
   */
    public String getPixelInfo() {
        return pixelInfo.toString();
    }

    public static void main(String[] args) {
        String filename = "Image/d200.jpg";
        PlanarImage planar = JAI.create("FileLoad", filename);
        DisplayJAIWithPixelInfo display = new DisplayJAIWithPixelInfo(planar);
        display.setVisible(true);
    }
}
