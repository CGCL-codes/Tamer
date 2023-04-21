package frameextraction;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.media.Buffer;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;
import utilities.Utilities;

/**
 *
 * @author Dooma�
 */
public class mFrame {

    public static final int RED_MASK = 2;

    public static final int GREEN_MASK = 1;

    public static final int BLUE_MASK = 0;

    RGBFormat frameFormat;

    int height, width;

    public byte[] data;

    Buffer mBuffer;

    /** Creates a new instance of Frame */
    public mFrame(Buffer newData) {
        mBuffer = newData;
        frameFormat = (RGBFormat) newData.getFormat();
        height = (int) frameFormat.getSize().getHeight();
        width = (int) frameFormat.getSize().getWidth();
        data = (byte[]) newData.getData();
    }

    /** Creates a new empty instance of Frame */
    public mFrame() {
    }

    public void setData(byte[] data, int w, int h) {
        height = h;
        width = w;
        this.data = data;
    }

    public Buffer getBuffer() {
        return mBuffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getSequenceNumber() {
        return mBuffer.getSequenceNumber();
    }

    public void setSequenceNumber(long sequenceNumber) {
        mBuffer.setSequenceNumber(sequenceNumber);
    }

    public Image frameToImage() {
        VideoFormat vf = (VideoFormat) mBuffer.getFormat();
        BufferToImage bufferToImage = new BufferToImage(vf);
        Image im = bufferToImage.createImage(mBuffer);
        return im;
    }

    public void imageToFrame(BufferedImage image) {
        int dataIndex = 0;
        for (int i = getHeight() - 1; i >= 0; i--) for (int j = 0; j < getWidth(); j++) {
            Color c = new Color(image.getRGB(j, i));
            data[dataIndex++] = (byte) c.getBlue();
            data[dataIndex++] = (byte) c.getGreen();
            data[dataIndex++] = (byte) c.getRed();
        }
    }

    private Pixel tempPixel = new Pixel();

    public Pixel getPixel(int x, int y) {
        try {
            int index = (y * width + x) * 3;
            tempPixel.changeData(x, y, data[index + RED_MASK], data[index + GREEN_MASK], data[index + BLUE_MASK]);
            return tempPixel;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public Pixel getPixelFlipped(int x, int y) {
        return getPixel(x, height - y - 1);
    }

    private byte[] RGB;

    public void setPixel(Pixel newPixel) {
        int index = (newPixel.y * width + newPixel.x) * 3;
        RGB = newPixel.getRGB();
        data[index + frameFormat.getRedMask() - 1] = RGB[0];
        data[index + frameFormat.getGreenMask() - 1] = RGB[1];
        data[index + frameFormat.getBlueMask() - 1] = RGB[2];
    }

    public double[][] getLuminance() {
        return new double[1][1];
    }

    public void save(String fileName) {
        try {
            File f = new File(fileName);
            f.createNewFile();
            ImageIO.write((BufferedImage) frameToImage(), "jpg", f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        save(Utilities.dateString + "/" + getSequenceNumber() + ".jpg");
    }

    public boolean loadFromImage(String fileName) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File(fileName));
            imageToFrame(bufferedImage);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
