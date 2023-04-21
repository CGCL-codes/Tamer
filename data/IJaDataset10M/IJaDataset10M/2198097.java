package org.isakiev.wic.j2kfacade;

import jj2000.j2k.image.*;
import jj2000.j2k.image.output.ImgWriter;
import java.awt.image.BufferedImage;
import java.io.*;

class BufferedImageWriter extends ImgWriter {

    /** The number of bits that determine the nominal dynamic range */
    private static final int RANGE_BITS = 8;

    /** Number of the image components */
    private static final int COMPONENTS_NUMBER = 3;

    private int levelShift;

    private int fractionalBitsNumber;

    private BufferedImage image;

    private byte buf[];

    public BufferedImageWriter(BlkImgDataSrc imageSource) {
        if (imageSource.getNumComps() != COMPONENTS_NUMBER) {
            throw new IllegalArgumentException("Image source must have 3 components");
        }
        for (int i = 0; i < COMPONENTS_NUMBER; i++) {
            if (imageSource.getNomRangeBits(i) > RANGE_BITS) {
                throw new IllegalArgumentException("Wrong range bits number");
            }
        }
        w = imageSource.getImgWidth();
        h = imageSource.getImgHeight();
        for (int i = 0; i < COMPONENTS_NUMBER; i++) {
            if (w != imageSource.getCompImgWidth(i) || h != imageSource.getCompImgHeight(i)) {
                throw new IllegalArgumentException("All components must have the same dimensions");
            }
        }
        src = imageSource;
        fractionalBitsNumber = imageSource.getFixedPoint(0);
        levelShift = 1 << (imageSource.getNomRangeBits(0) - 1);
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }

    public void close() throws IOException {
    }

    public void flush() throws IOException {
    }

    /**
	 * Writes the data of the specified area to the file, coordinates are
	 * relative to the current tile of the source. Before writing, the
	 * coefficients are limited to the nominal range.
	 * 
	 * <p>
	 * This method may not be called concurrently from different threads.
	 * </p>
	 * 
	 * <p>
	 * If the data returned from the BlkImgDataSrc source is progressive, then
	 * it is requested over and over until it is not progressive anymore.
	 * </p>
	 * 
	 * @param ulx The horizontal coordinate of the upper-left corner of the area
	 *            to write, relative to the current tile.
	 * 
	 * @param uly The vertical coordinate of the upper-left corner of the area
	 *            to write, relative to the current tile.
	 * 
	 * @param width The width of the area to write.
	 * 
	 * @param height The height of the area to write.
	 * 
	 * @exception IOException If an I/O error occurs.
	 */
    public void write(int ulx, int uly, int w, int h) throws IOException {
        int tileOffsetX = src.getCompULX(0) - (int) Math.ceil(src.getImgULX() / (double) src.getCompSubsX(0));
        int tileOffsetY = src.getCompULY(0) - (int) Math.ceil(src.getImgULY() / (double) src.getCompSubsY(0));
        DataBlkInt dataBlock = new DataBlkInt();
        if (buf == null || buf.length < 3 * w) {
            buf = new byte[3 * w];
        }
        for (int y = 0; y < h; y++) {
            for (int componentIndex = 0; componentIndex < COMPONENTS_NUMBER; componentIndex++) {
                int maxValue = (1 << src.getNomRangeBits(componentIndex)) - 1;
                dataBlock.ulx = ulx;
                dataBlock.uly = uly + y;
                dataBlock.w = w;
                dataBlock.h = 1;
                do {
                    dataBlock = (DataBlkInt) src.getInternCompData(dataBlock, componentIndex);
                } while (dataBlock.progressive);
                if (fractionalBitsNumber == 0) {
                    for (int k = dataBlock.offset + w - 1, j = 3 * w - 1 + componentIndex - 2; j >= 0; k--) {
                        int value = dataBlock.data[k] + levelShift;
                        buf[j] = (byte) ((value < 0) ? 0 : ((value > maxValue) ? maxValue : value));
                        j -= 3;
                    }
                } else {
                    for (int k = dataBlock.offset + w - 1, j = 3 * w - 1 + componentIndex - 2; j >= 0; k--) {
                        int value = (dataBlock.data[k] >>> fractionalBitsNumber) + levelShift;
                        buf[j] = (byte) ((value < 0) ? 0 : ((value > maxValue) ? maxValue : value));
                        j -= 3;
                    }
                }
            }
            for (int xx = 0; xx < w; xx++) {
                int color = 0xFF000000 + (buf[3 * xx] << 16) + (buf[3 * xx + 1] << 8) + buf[3 * xx + 2];
                int cx = ulx + tileOffsetX + xx;
                int cy = uly + tileOffsetY + y;
                image.setRGB(cx, cy, color);
            }
        }
    }

    /**
	 * Writes the source's current tile to the output. The requests of data
	 * issued to the source BlkImgDataSrc object are done by strips, in order to
	 * reduce memory usage.
	 * 
	 * <P>
	 * If the data returned from the BlkImgDataSrc source is progressive, then
	 * it is requested over and over until it is not progressive any more.
	 * 
	 * @exception IOException If an I/O error occurs.
	 */
    public void write() throws IOException {
        int tileIndex = src.getTileIdx();
        int tileWidth = src.getTileCompWidth(tileIndex, 0);
        int tileHeight = src.getTileCompHeight(tileIndex, 0);
        for (int i = 0; i < tileHeight; i += DEF_STRIP_HEIGHT) {
            write(0, i, tileWidth, ((tileHeight - i) < DEF_STRIP_HEIGHT) ? tileHeight - i : DEF_STRIP_HEIGHT);
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
