package gnu.javax.imageio.bmp;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageWriteParam;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

public class EncodeRGB32 extends BMPEncoder {

    protected BMPInfoHeader infoHeader;

    protected BMPFileHeader fileHeader;

    protected long offset;

    /**
   * Constructs an instance of this class.
   * 
   * @param fh - the file header to use.
   * @param ih - the info header to use.
   */
    public EncodeRGB32(BMPFileHeader fh, BMPInfoHeader ih) {
        super();
        fileHeader = fh;
        infoHeader = ih;
        offset = BMPFileHeader.SIZE + BMPInfoHeader.SIZE;
    }

    /**
   * The image encoder.
   * 
   * @param o - the image output stream
   * @param streamMetadata - metadata associated with this stream, or null
   * @param image - an IIOImage containing image data.
   * @param param - image writing parameters, or null
   * @exception IOException if a write error occurs
   */
    public void encode(ImageOutputStream o, IIOMetadata streamMetadata, IIOImage image, ImageWriteParam param) throws IOException {
        int size;
        int value;
        int j;
        int rowCount;
        int rowIndex;
        int lastRowIndex;
        int[] bitmap;
        byte rgb[] = new byte[4];
        size = (infoHeader.biWidth * infoHeader.biHeight) - 1;
        rowCount = 1;
        rowIndex = size - infoHeader.biWidth;
        lastRowIndex = rowIndex;
        try {
            bitmap = new int[infoHeader.biWidth * infoHeader.biHeight];
            PixelGrabber pg = new PixelGrabber((BufferedImage) image.getRenderedImage(), 0, 0, infoHeader.biWidth, infoHeader.biHeight, bitmap, 0, infoHeader.biWidth);
            pg.grabPixels();
            for (j = 0; j < size; j++) {
                value = bitmap[rowIndex];
                rgb[0] = (byte) (value & 0xFF);
                rgb[1] = (byte) ((value >> 8) & 0xFF);
                rgb[2] = (byte) ((value >> 16) & 0xFF);
                rgb[3] = (byte) ((value >> 24) & 0xFF);
                o.write(rgb);
                if (rowCount == infoHeader.biWidth) {
                    rowCount = 1;
                    rowIndex = lastRowIndex - infoHeader.biWidth;
                    lastRowIndex = rowIndex;
                } else rowCount++;
                rowIndex++;
            }
        } catch (Exception wb) {
            wb.printStackTrace();
        }
    }
}
