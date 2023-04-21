package org.lnicholls.galleon.winamp;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.io.InputStream;
import org.lnicholls.galleon.util.Tools;

/**
 * A decoder for Windows bitmap (.BMP) files.
 * Compression not supported.
 */
public class BMPLoader {

    private InputStream is;

    private int curPos = 0;

    private int bitmapOffset;

    private int width;

    private int height;

    private short bitsPerPixel;

    private int compression;

    private int actualSizeOfBitmap;

    private int scanLineSize;

    private int actualColorsUsed;

    private byte r[], g[], b[];

    private int noOfEntries;

    private byte[] byteData;

    private int[] intData;

    public BMPLoader() {
    }

    public Image getBMPImage(InputStream stream) throws Exception {
        read(stream);
        return Tools.createImage(getImageSource());
    }

    private int readInt() throws IOException {
        int b1 = is.read();
        int b2 = is.read();
        int b3 = is.read();
        int b4 = is.read();
        curPos += 4;
        return ((b4 << 24) + (b3 << 16) + (b2 << 8) + (b1 << 0));
    }

    private short readShort() throws IOException {
        int b1 = is.read();
        int b2 = is.read();
        curPos += 4;
        return (short) ((b2 << 8) + b1);
    }

    void getFileHeader() throws IOException, Exception {
        short fileType = 0x4d42;
        int fileSize;
        short reserved1 = 0;
        short reserved2 = 0;
        fileType = readShort();
        if (fileType != 0x4d42) throw new Exception("Not a BMP file");
        fileSize = readInt();
        reserved1 = readShort();
        reserved2 = readShort();
        bitmapOffset = readInt();
    }

    void getBitmapHeader() throws IOException {
        int size;
        short planes;
        int sizeOfBitmap;
        int horzResolution;
        int vertResolution;
        int colorsUsed;
        int colorsImportant;
        boolean topDown;
        int noOfPixels;
        size = readInt();
        width = readInt();
        height = readInt();
        planes = readShort();
        bitsPerPixel = readShort();
        compression = readInt();
        sizeOfBitmap = readInt();
        horzResolution = readInt();
        vertResolution = readInt();
        colorsUsed = readInt();
        colorsImportant = readInt();
        topDown = (height < 0);
        noOfPixels = width * height;
        scanLineSize = ((width * bitsPerPixel + 31) / 32) * 4;
        if (sizeOfBitmap != 0) actualSizeOfBitmap = sizeOfBitmap; else actualSizeOfBitmap = scanLineSize * height;
        if (colorsUsed != 0) actualColorsUsed = colorsUsed; else if (bitsPerPixel < 16) actualColorsUsed = 1 << bitsPerPixel; else actualColorsUsed = 0;
    }

    void getPalette() throws IOException {
        noOfEntries = actualColorsUsed;
        if (noOfEntries > 0) {
            r = new byte[noOfEntries];
            g = new byte[noOfEntries];
            b = new byte[noOfEntries];
            int reserved;
            for (int i = 0; i < noOfEntries; i++) {
                b[i] = (byte) is.read();
                g[i] = (byte) is.read();
                r[i] = (byte) is.read();
                reserved = is.read();
                curPos += 4;
            }
        }
    }

    void unpack(byte[] rawData, int rawOffset, int[] intData, int intOffset, int w) {
        int j = intOffset;
        int k = rawOffset;
        int mask = 0xff;
        for (int i = 0; i < w; i++) {
            int b0 = (((int) (rawData[k++])) & mask);
            int b1 = (((int) (rawData[k++])) & mask) << 8;
            int b2 = (((int) (rawData[k++])) & mask) << 16;
            intData[j] = 0xff000000 | b0 | b1 | b2;
            j++;
        }
    }

    void unpack(byte[] rawData, int rawOffset, int bpp, byte[] byteData, int byteOffset, int w) throws Exception {
        int j = byteOffset;
        int k = rawOffset;
        byte mask;
        int pixPerByte;
        switch(bpp) {
            case 1:
                mask = (byte) 0x01;
                pixPerByte = 8;
                break;
            case 4:
                mask = (byte) 0x0f;
                pixPerByte = 2;
                break;
            case 8:
                mask = (byte) 0xff;
                pixPerByte = 1;
                break;
            default:
                throw new Exception("Unsupported bits-per-pixel value");
        }
        for (int i = 0; ; ) {
            int shift = 8 - bpp;
            for (int ii = 0; ii < pixPerByte; ii++) {
                byte br = rawData[k];
                br >>= shift;
                byteData[j] = (byte) (br & mask);
                j++;
                i++;
                if (i == w) return;
                shift -= bpp;
            }
            k++;
        }
    }

    void getPixelData() throws IOException, Exception {
        byte[] rawData;
        long skip = bitmapOffset - curPos;
        if (skip > 0) {
            is.skip(skip);
            curPos += skip;
        }
        int len = scanLineSize;
        if (bitsPerPixel > 8) intData = new int[width * height]; else byteData = new byte[width * height];
        rawData = new byte[actualSizeOfBitmap];
        int rawOffset = 0;
        int offset = (height - 1) * width;
        for (int i = height - 1; i >= 0; i--) {
            int n = is.read(rawData, rawOffset, len);
            if (n < len) throw new Exception("Scan line ended prematurely after " + n + " bytes");
            if (bitsPerPixel > 8) {
                unpack(rawData, rawOffset, intData, offset, width);
            } else {
                unpack(rawData, rawOffset, bitsPerPixel, byteData, offset, width);
            }
            rawOffset += len;
            offset -= width;
        }
    }

    public void read(InputStream is) throws IOException, Exception {
        this.is = is;
        getFileHeader();
        getBitmapHeader();
        if (compression != 0) throw new Exception(" BMP Compression not supported");
        getPalette();
        getPixelData();
    }

    public MemoryImageSource getImageSource() {
        ColorModel cm;
        MemoryImageSource mis;
        if (noOfEntries > 0) {
            cm = new IndexColorModel(bitsPerPixel, noOfEntries, r, g, b);
        } else {
            cm = ColorModel.getRGBdefault();
        }
        if (bitsPerPixel > 8) {
            mis = new MemoryImageSource(width, height, cm, intData, 0, width);
        } else {
            mis = new MemoryImageSource(width, height, cm, byteData, 0, width);
        }
        return mis;
    }
}
