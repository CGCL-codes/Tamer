package com.jswiff.swfrecords;

import com.jswiff.io.InputBitStream;
import com.jswiff.io.OutputBitStream;
import java.io.IOException;

/**
 * This class is used within <code>DefineBitsLossless2</code> tags (with 32-bit
 * RGBA images). It contains an array of pixel colors and transparency
 * information. No scanline padding is needed.
 */
public final class AlphaBitmapData extends ZlibBitmapData {

    private RGBA[] bitmapPixelData;

    /**
   * Creates a new AlphaBitmapData instance. Supply an RGBA array of size
   * [width x height]. No scanline padding is needed.
   *
   * @param bitmapPixelData RGBA array
   */
    public AlphaBitmapData(RGBA[] bitmapPixelData) {
        this.bitmapPixelData = bitmapPixelData;
    }

    /**
   * Creates a new AlphaBitmapData instance, reading data from a bit stream.
   *
   * @param stream source bit stream
   * @param width image width
   * @param height image height
   *
   * @throws IOException if an I/O error occured
   */
    public AlphaBitmapData(InputBitStream stream, int width, int height) throws IOException {
        int imageDataSize = width * height;
        bitmapPixelData = new RGBA[imageDataSize];
        for (int i = 0; i < imageDataSize; i++) {
            bitmapPixelData[i] = RGBA.readARGB(stream);
        }
    }

    /**
   * Returns the bitmap data, i.e. an array of pixel colors and transparency
   * information.
   *
   * @return bitmap pixel data (one RGBA value for each pixel)
   */
    public RGBA[] getBitmapPixelData() {
        return bitmapPixelData;
    }

    /**
   * Writes this instance to a bit stream.
   *
   * @param stream target bit stream
   *
   * @throws IOException if an I/O error occured
   */
    public void write(OutputBitStream stream) throws IOException {
        for (int i = 0; i < bitmapPixelData.length; i++) {
            bitmapPixelData[i].writeARGB(stream);
        }
    }
}
