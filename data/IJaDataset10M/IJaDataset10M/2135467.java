package com.iv.flash.util;

import com.iv.flash.parser.DataMarker;
import java.io.*;
import java.util.zip.*;

/**
 * Helper class to decode PNG images.
 * Does read but not support gAMA chunk (yet :-)
 * @author Patrick Talbot
 */
public class PNGHelper {

    /** header chunk */
    private static final int CHUNK_IHDR = 0x49484452;

    /** palette chunk */
    private static final int CHUNK_PLTE = 0x504c5445;

    /** data chunk */
    private static final int CHUNK_IDAT = 0x49444154;

    /** end of file chunk */
    private static final int CHUNK_IEND = 0x49454e44;

    /** transparency chunk */
    private static final int CHUNK_tRNS = 0x74524e53;

    /** background (ignored) chunk */
    private static final int CHUNK_bKGD = 0x624b4744;

    /** chromaticities (ignored) chunk */
    private static final int CHUNK_cHRM = 0x6348524d;

    /** fractal image parameters (ignored) chunk */
    private static final int CHUNK_fRAc = 0x66524163;

    /** gamma (ignored) chunk */
    private static final int CHUNK_gAMA = 0x67414d41;

    /** GIF graphic control (ignored) chunk */
    private static final int CHUNK_gIFg = 0x67494667;

    /** GIF plain text (ignored) chunk */
    private static final int CHUNK_gIFt = 0x67494674;

    /** GIF application (ignored) chunk */
    private static final int CHUNK_gIFx = 0x67494678;

    /** histogram (ignored) chunk */
    private static final int CHUNK_hIST = 0x68495354;

    /** embedded ICC profile (ignored) chunk */
    private static final int CHUNK_iCCP = 0x69434350;

    /** unicode UTF-8 text (ignored) chunk */
    private static final int CHUNK_iTXt = 0x69545874;

    /** image offset (ignored) chunk */
    private static final int CHUNK_oFFs = 0x6f464673;

    /** calibration (ignored) chunk */
    private static final int CHUNK_pCAL = 0x7043414c;

    /** physical pixel dimension (ignored) chunk */
    private static final int CHUNK_pHYs = 0x70485973;

    /** significant bits (ignored) chunk */
    private static final int CHUNK_sBIT = 0x73424954;

    /** physical scale (ignored) chunk */
    private static final int CHUNK_sCAL = 0x7343414c;

    /** suggested palette (ignored) chunk */
    private static final int CHUNK_sPLT = 0x73504c54;

    /** standard RGB colour space (ignored) chunk */
    private static final int CHUNK_sRGB = 0x73524742;

    /** normal text (ignored) chunk */
    private static final int CHUNK_tEXt = 0x74455874;

    /** time stamp (ignored) chunk */
    private static final int CHUNK_tIME = 0x74494d45;

    /** compressed text (ignored) chunk */
    private static final int CHUNK_zTXt = 0x7a545874;

    /** PNG Signature */
    private static final int[] PNG_SIGN = { 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a };

    /** a flag to test if the header chunk has been encountered */
    private boolean flagIHDR = false;

    /** a flag to test if data chunks have been encountered */
    private boolean flagIDAT = false;

    /** a flag to test if the palette chunk has been encountered */
    private boolean flagPLTE = false;

    /** flag used to indicate that further computing is need to manage transparency */
    private boolean tRNSPassRequired = false;

    /** transparency color for colorType 0 and 2 */
    private RGB tRNSKeyColor = null;

    /** used to determine if transparency chunk exists */
    private boolean flagtRNS = false;

    /** number of bytes used per pixel */
    private int colorType_components = -1;

    /** compression method should always be 0 = deflate/inflate, */
    private int compressionMethod = -1;

    /** filter method can be
     * 0 = none
     * 1 = sub
     * 2 = up
     * 3 = average
     * 4 = paeth
     */
    private int filterMethod = -1;

    /** interlace method can be
     * 0 = not interlace
     * 1 = Adam7 interlacing
     */
    private int interlaceMethod = -1;

    /** width of image*/
    private int width = -1;

    /** height of image */
    private int height = -1;

    /** pixels bit depth can be 1,2,4,8,16 */
    private int bitDepth = -1;

    /** gamma value of image, set to default value */
    private long gamma = 45455;

    /** color type can be
     * 0 = gray
     * 2 = RGB
     * 3 = paletted
     * 4 = gray + alpha
     * 6 = RGB + alpha
     */
    private int colorType = -1;

    /** flag set if palette is used */
    private boolean colorType_hasPalette = false;

    /** flag set if alpha channel exists for colorType = 4 or 6 */
    private boolean colorType_hasAlpha = false;

    /** flag set if not gray */
    private boolean colorType_hasColor = false;

    /** RGB Palette */
    private RGB[] palette;

    /** size of the palette */
    private int colorsUsed = 0;

    /** main IDAT buffer */
    private byte[] buffer;

    /** Stream to read data from */
    private CRCInputStream source;

    public static class CRCInputStream extends DataInputStream {

        /** crc checker */
        private CRC32 crc = new CRC32();

        /**
         * Constructor
         * @param inputStream java.io.InputStream
         */
        public CRCInputStream(InputStream inputStream) {
            super(inputStream);
        }

        /**
         * Return the crc value
         * @return long
         */
        public long getCRC() {
            return crc.getValue();
        }

        /**
         * Reset the crc checker
         */
        public void reset() {
            crc.reset();
        }

        /**
         * Read one int and ensure the crc is updated
         * @return int
         */
        public int read() throws IOException {
            int x = super.read();
            crc.update(x);
            return x;
        }

        /**
         * Read two ints
         * @return int
         */
        public int read2() throws IOException {
            int a = read();
            int b = read();
            return a | b << 8;
        }

        /**
         * Read two ints in reverse order
         * @return int
         */
        public int read2n() throws IOException {
            int b = read();
            int a = read();
            return a | b << 8;
        }

        /**
         * Read a long in reverse order
         * @return long
         */
        public long read4n() throws IOException, EOFException {
            long b = (long) read2n();
            long a = (long) read2n();
            return a | b << 16;
        }
    }

    public static class RGB {

        /** represents 15 bits format */
        private static final int FORMAT_RGB5 = 0x8050;

        /** represents 24 bits format */
        private static final int FORMAT_RGB8 = 0x8051;

        /** represents 16 bits format */
        private static final int FORMAT_RGB5_A1 = 0x8057;

        /** represents 32 bits format */
        private static final int FORMAT_RGBA8 = 0x8058;

        /** used to define color without alpha information */
        private static final int NO_ALPHA = -1;

        /** default alpha value */
        private static final float DEFAULT_ALPHA = 1.0f;

        /** used to define color with default format */
        private static final float DEFAULT_FORMAT = 255.0f;

        /** red value */
        private float red;

        /** green value */
        private float green;

        /** blue value */
        private float blue;

        /** alpha value */
        private float alpha;

        /**
         * float gray constructor
         * @param gray float
         */
        public RGB(float gray) {
            set(gray, gray, gray, DEFAULT_ALPHA);
        }

        /**
         * int gray constructor
         * @param gray int
         */
        public RGB(int gray) {
            set(gray, gray, gray, NO_ALPHA, NO_ALPHA);
        }

        /**
         * int gray + alpha constructor
         * @param gray int
         * @param a int
         */
        public RGB(int gray, int a) {
            set(gray, gray, gray, a, DEFAULT_FORMAT);
        }

        /**
         * float gray + alpha constructor
         * @param gray float
         * @param a float
         */
        public RGB(float gray, float a) {
            set(gray, gray, gray, a);
        }

        /**
         * float r,g,b constructor
         * @param r float
         * @param g float
         * @param b float
         */
        public RGB(float r, float g, float b) {
            set(r, g, b, DEFAULT_ALPHA);
        }

        /**
         * int r,g,b constructor
         * @param r int
         * @param g int
         * @param b int
         */
        public RGB(int r, int g, int b) {
            set(r, g, b, NO_ALPHA, NO_ALPHA);
        }

        /**
         * int r,g,b + alpha constructor
         * @param r int
         * @param g int
         * @param b int
         * @param a int
         */
        public RGB(int r, int g, int b, int a) {
            set((float) r / DEFAULT_FORMAT, (float) g / DEFAULT_FORMAT, (float) b / DEFAULT_FORMAT, (float) a / DEFAULT_FORMAT);
        }

        /**
         * float r,g,b + alpha constructor
         * @param r float
         * @param g float
         * @param b float
         * @param a float
         */
        public RGB(float r, float g, float b, float a) {
            set(r, g, b, a);
        }

        /**
         * general float r,g,b + alpha setter
         * @param r float
         * @param g float
         * @param b float
         * @param a float
         */
        public void set(float r, float g, float b, float a) {
            red = r > 1.0f ? 1.0f : (r < 0.0f ? 0.0f : r);
            green = g > 1.0f ? 1.0f : (g < 0.0f ? 0.0f : g);
            blue = b > 1.0f ? 1.0f : (b < 0.0f ? 0.0f : b);
            alpha = a > 1.0f ? 1.0f : (a < 0.0f ? 0.0f : a);
        }

        /**
         * general int r,g,b + alpha setter
         * @param r int
         * @param g int
         * @param b int
         * @param a int
         * @param format float
         */
        public void set(int r, int g, int b, int a, float format) {
            set((float) r / DEFAULT_FORMAT, (float) g / DEFAULT_FORMAT, (float) b / DEFAULT_FORMAT, format == NO_ALPHA ? DEFAULT_ALPHA : (float) a / format);
        }

        /**
         * Return an int array of color packed in the requested format
         * @param format int
         * @return int[]
         */
        public int[] getPacked(int format) {
            int r;
            int g;
            int b;
            switch(format) {
                case FORMAT_RGB5:
                    r = (int) (red * 32.0f);
                    g = (int) (green * 32.0f);
                    b = (int) (blue * 32.0f);
                    return new int[] { r + (g & 0x07) << 5, (g & 0x18) >> 3 + (b & 0x1f) << 2 };
                case FORMAT_RGB8:
                    return new int[] { (int) (red * 255.0f), (int) (green * 255.0f), (int) (blue * 255.0f) };
                case FORMAT_RGB5_A1:
                    r = (int) (red * 32.0f);
                    g = (int) (green * 32.0f);
                    b = (int) (blue * 32.0f);
                    return new int[] { r + (g & 0x07) << 5, (g & 0x18) >> 3 + (b & 0x1f) << 2 + (alpha > 0.5f ? 128 : 0) };
                case FORMAT_RGBA8:
                    return new int[] { (int) (red * 255.0f), (int) (green * 255.0f), (int) (blue * 255.0f), (int) (alpha * 255.0f) };
                default:
                    throw new IllegalArgumentException("Unknown color format " + format);
            }
        }

        public float getRed() {
            return red;
        }

        public float getGreen() {
            return green;
        }

        public float getBlue() {
            return blue;
        }

        public float getAlpha() {
            return alpha;
        }

        /**
         * Set the alpha value as a float
         * @param alpha float
         */
        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }

        public boolean compareTo(RGB rgb, boolean compareAlpha) {
            if (rgb.getRed() == red && rgb.getGreen() == green && rgb.getBlue() == blue) if (!compareAlpha || rgb.getAlpha() == alpha) return true;
            return false;
        }
    }

    public PNGHelper() {
    }

    public PNGHelper(byte[] inputBuffer) {
        setInputBuffer(inputBuffer);
    }

    /**
     * Set the main inputStream and construct the CRCInputStream to read to
     *
     * @param inputBuffer buffer to read from
     */
    public void setInputBuffer(byte[] inputBuffer) {
        source = new CRCInputStream(new ByteArrayInputStream(inputBuffer));
    }

    /**
     * Set the main inputStream and construct the CRCInputStream to read to
     *
     * @param fob    buffer to read from
     */
    public void setInputBuffer(FlashBuffer fob) {
        source = new CRCInputStream(fob.getInputStream());
    }

    /**
     * Return the width of the image
     * @return width of the image
     */
    public int getWidth() {
        return width;
    }

    /**
     * Return the height of the image
     *
     * @return height of the image
     */
    public int getHeight() {
        return height;
    }

    public int getColorTableSize() {
        return (colorsUsed - 1);
    }

    public boolean hasTransparency() {
        return (colorType > 3 || flagtRNS);
    }

    public int getFormat() {
        int format;
        switch(colorType_components) {
            case 1:
                format = 3;
                if (colorType == 0) format = 5;
                if (bitDepth == 16) format = 5;
                break;
            default:
                format = 5;
                break;
        }
        return format;
    }

    public DataMarker getZlibData() throws IOException, EOFException, IVException {
        RGB[][] rgb = read();
        boolean transparency = hasTransparency();
        int format = getFormat();
        int mult = 1;
        switch(format) {
            case 4:
                mult = 2;
                break;
            case 5:
                mult = 4;
        }
        byte[] tempData;
        int plus = 3;
        int[] pixel;
        int idx = 0;
        int falsewidth = width;
        int added = 0;
        int maxpixels = width * height;
        if ((mult == 1) && (width % 4 > 0)) {
            while (falsewidth % 4 > 0) {
                falsewidth++;
                added++;
            }
            maxpixels = falsewidth * height;
        }
        if (colorType_hasPalette) {
            if (transparency) plus++;
            tempData = new byte[(maxpixels) + (colorsUsed * plus)];
            for (int i = 0; i < colorsUsed; i++) {
                if (transparency) pixel = palette[i].getPacked(RGB.FORMAT_RGBA8); else pixel = palette[i].getPacked(RGB.FORMAT_RGB8);
                if (transparency && pixel[3] == 0x00) {
                    tempData[idx++] = 0;
                    tempData[idx++] = 0;
                    tempData[idx++] = 0;
                } else {
                    tempData[idx++] = (byte) ((pixel[0]));
                    tempData[idx++] = (byte) ((pixel[1]));
                    tempData[idx++] = (byte) ((pixel[2]));
                }
                if (transparency) tempData[idx++] = (byte) ((pixel[3]));
            }
        } else {
            if (transparency) mult = 4;
            tempData = new byte[(maxpixels * mult)];
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (colorType_hasPalette) {
                    if (transparency) pixel = rgb[y][x].getPacked(RGB.FORMAT_RGBA8); else pixel = rgb[y][x].getPacked(RGB.FORMAT_RGB8);
                    int b = 0;
                    for (int i = 0; i < palette.length; i++) {
                        if (palette[i].compareTo(rgb[y][x], false)) {
                            b = i;
                            break;
                        }
                    }
                    tempData[idx++] = (byte) b;
                } else {
                    if (mult == 4) pixel = rgb[y][x].getPacked(RGB.FORMAT_RGBA8); else if (mult == 2) pixel = rgb[y][x].getPacked(RGB.FORMAT_RGB5_A1); else pixel = rgb[y][x].getPacked(RGB.FORMAT_RGB8);
                    if (pixel != null) {
                        if (mult == 4) tempData[idx++] = (byte) ((pixel[3]));
                        tempData[idx++] = (byte) ((pixel[0]));
                        if (mult > 1) tempData[idx++] = (byte) ((pixel[1]));
                        if (mult == 4) tempData[idx++] = (byte) ((pixel[2]));
                    }
                }
            }
            if (added > 0) {
                for (int i = 0; i < added; i++) {
                    tempData[idx++] = 0x00;
                }
            }
        }
        Deflater deflater = new Deflater(9);
        deflater.setInput(tempData);
        byte[] data = new byte[(maxpixels * mult) + (colorsUsed * plus)];
        deflater.finish();
        int defsize = deflater.deflate(data);
        byte[] buff = new byte[defsize];
        System.arraycopy(data, 0, buff, 0, defsize);
        DataMarker zlibDatas = new DataMarker(buff, 0, defsize);
        return zlibDatas;
    }

    private RGB[][] read() throws IOException, EOFException {
        if (source == null) throw new IOException("Null input stream");
        source.mark(Integer.MAX_VALUE);
        buffer = new byte[0];
        try {
            for (int i = 0; i < 8; i++) if (source.read() != PNG_SIGN[i]) throw new IOException("Not a valid PNG file");
            int length;
            int id;
            while (true) {
                length = (int) source.read4n();
                source.reset();
                id = (int) source.read4n();
                switch(id) {
                    case CHUNK_IHDR:
                        readChunk_IHDR(length);
                        break;
                    case CHUNK_PLTE:
                        readChunk_PLTE(length);
                        break;
                    case CHUNK_tRNS:
                        readChunk_tRNS(length);
                        break;
                    case CHUNK_IDAT:
                        readChunk_IDAT(length);
                        break;
                    case CHUNK_IEND:
                        if (!flagIHDR || !flagIDAT) throw new IOException("No header or data chunk found");
                        return decode(buffer);
                    case CHUNK_gAMA:
                        readChunk_gAMA(length);
                        break;
                    case CHUNK_bKGD:
                    case CHUNK_cHRM:
                    case CHUNK_fRAc:
                    case CHUNK_gIFg:
                    case CHUNK_gIFt:
                    case CHUNK_gIFx:
                    case CHUNK_hIST:
                    case CHUNK_iCCP:
                    case CHUNK_iTXt:
                    case CHUNK_oFFs:
                    case CHUNK_pCAL:
                    case CHUNK_pHYs:
                    case CHUNK_sBIT:
                    case CHUNK_sCAL:
                    case CHUNK_sPLT:
                    case CHUNK_sRGB:
                    case CHUNK_tEXt:
                    case CHUNK_tIME:
                    case CHUNK_zTXt:
                        source.skipBytes(length + 4);
                        break;
                    default:
                        if ((id & 0x20000000) == 0) throw new IOException("Unknown critical chunk " + (char) ((id >> 24) & 0xff) + (char) ((id >> 16) & 0xff) + (char) ((id >> 8) & 0xff) + (char) (id & 0xff));
                        source.skipBytes(length + 4);
                }
            }
        } catch (EOFException eofe) {
            throw new EOFException("Reached unexpected EOF");
        } catch (IOException ioe) {
            throw new IOException("Error while reading file");
        }
    }

    /**
     * Deal with the gamma chunk
     * @param length int the length of the chunk to read
     * @exception java.io.IOException
     */
    private void readChunk_gAMA(int length) throws IOException {
        if (flagIDAT || flagPLTE) throw new IOException("gAMA chunk must precede IDAT and PLTE chunks");
        if (length != 4) throw new IOException("Invalid length " + length + " for gAMA chunk");
        gamma = source.read4n();
        long okCRC = source.getCRC();
        if (source.read4n() != okCRC) throw new IOException("Invalid CRC for gAMA chunk " + length);
    }

    /**
     * Deal with the transparency chunk
     * @param length int the length of the chunk to read
     * @exception java.io.IOException
     */
    private void readChunk_tRNS(int length) throws IOException {
        if (!flagIHDR || flagIDAT) throw new IOException("Misplaced transparency chunk");
        switch(colorType) {
            case 0:
                int tmp1 = source.read();
                int tmp2 = source.read();
                if (bitDepth < 16) {
                    int max = 1;
                    switch(bitDepth) {
                        case 1:
                            max = 256;
                            break;
                        case 2:
                            max = 256 / 4;
                            break;
                        case 4:
                            max = 256 / 16;
                            break;
                    }
                    if (bitDepth == 1) tRNSKeyColor = new RGB(tmp2 * max); else {
                        tmp1 = ((tmp2 + 1) * max) - 1;
                        tmp1 = (tmp1 < 0) ? 0 : tmp1;
                        tRNSKeyColor = new RGB(tmp1);
                    }
                } else tRNSKeyColor = new RGB((float) (tmp1 << 8 | tmp2) / 65535.0f);
                tRNSKeyColor.setAlpha(0.0f);
                tRNSPassRequired = true;
                break;
            case 2:
                int tmp;
                int cr1 = source.read();
                int cr2 = source.read();
                int cg1 = source.read();
                int cg2 = source.read();
                int cb1 = source.read();
                int cb2 = source.read();
                if (bitDepth < 16) {
                    tRNSKeyColor = new RGB(cr2, cg2, cb2);
                } else tRNSKeyColor = new RGB((float) (cr1 << 8 | cr2) / 65535.0f, (float) (cg1 << 8 | cg2) / 65535.0f, (float) (cb1 << 8 | cb2) / 65535.0f);
                tRNSKeyColor.setAlpha(0.0f);
                tRNSPassRequired = true;
                break;
            case 3:
                for (int i = 0; i < length; i++) palette[i].setAlpha((float) source.read() / 255.0f);
                break;
            default:
                throw new IOException("Unexpected transparency chunk");
        }
        flagtRNS = true;
        long okCRC = source.getCRC();
        if (source.read4n() != okCRC) throw new IOException("Invalid CRC for tRNS chunk " + length);
    }

    /**
     * Deal with the palette chunk
     * @param length int the length of the chunk to read
     * @exception java.io.IOException
     */
    private void readChunk_PLTE(int length) throws IOException {
        if (!flagIHDR || flagIDAT) throw new IOException("Misplaced palette chunk");
        if (length % 3 != 0) throw new IOException("Invalid palette size: " + length + " bytes");
        if (colorType != 3) {
            int tmp;
            for (int i = 0; i < length; i++) tmp = source.read();
        } else {
            flagPLTE = true;
            int entries = length / 3;
            colorsUsed = entries;
            palette = new RGB[entries];
            for (int i = 0; i < entries; i++) palette[i] = new RGB(source.read(), source.read(), source.read());
        }
        long okCRC = source.getCRC();
        if (source.read4n() != okCRC) throw new IOException("Invalid CRC for PLTE chunk");
    }

    /**
     * Deal with the header chunk
     * @param length int the length of the chunk to read
     * @exception java.io.IOException
     */
    private void readChunk_IHDR(int length) throws IOException {
        if (flagIHDR) throw new IOException("Duplicate header chunks");
        flagIHDR = true;
        width = (int) source.read4n();
        height = (int) source.read4n();
        bitDepth = source.read();
        colorType = source.read();
        compressionMethod = source.read();
        filterMethod = source.read();
        interlaceMethod = source.read();
        if ((width == 0) || (height == 0)) throw new IOException("Invalid image size: " + width + "x" + height);
        switch(colorType) {
            case 0:
                if ((bitDepth != 1) && (bitDepth != 2) && (bitDepth != 4) && (bitDepth != 8) && (bitDepth != 16)) throw new IOException("Invalid bit depth: " + bitDepth + " for color type: grayscale");
                colorType_components = 1;
                break;
            case 3:
                if ((bitDepth != 1) && (bitDepth != 2) && (bitDepth != 4) && (bitDepth != 8)) throw new IOException("Invalid bit depth: " + bitDepth + " for color type: paletted");
                colorType_components = 1;
                colorType_hasPalette = true;
                colorType_hasColor = true;
                break;
            case 2:
                if ((bitDepth != 8) && (bitDepth != 16)) throw new IOException("Invalid bit depth: " + bitDepth + " for color type: RGB");
                colorType_components = 3;
                colorType_hasColor = true;
                break;
            case 4:
                if ((bitDepth != 8) && (bitDepth != 16)) throw new IOException("Invalid bit depth: " + bitDepth + " for color type: gray + alpha");
                colorType_components = 2;
                colorType_hasAlpha = true;
                break;
            case 6:
                if ((bitDepth != 8) && (bitDepth != 16)) throw new IOException("Invalid bit depth: " + bitDepth + " for color type: RGB + alpha");
                colorType_components = 4;
                colorType_hasColor = true;
                colorType_hasAlpha = true;
                break;
            default:
                throw new IOException("Invalid color type: " + colorType);
        }
        if (compressionMethod != 0) throw new IOException("Unsupported compression method: " + compressionMethod);
        if (filterMethod != 0) throw new IOException("Unsupported filter method: " + compressionMethod);
        if ((interlaceMethod != 0) && (interlaceMethod != 1)) throw new IOException("Unsupported interlace method: " + interlaceMethod);
        long okCRC = source.getCRC();
        if (source.read4n() != okCRC) throw new IOException("Invalid CRC for IHDR chunk");
    }

    /**
     * Deal with data chunks
     * @param length int the length of the chunk to read
     * @exception java.io.IOException
     */
    private void readChunk_IDAT(int length) throws IOException {
        if (!flagIHDR) throw new IOException("Data chunks before header");
        if ((colorType == 3) && (!flagPLTE)) throw new IOException("Missing palette");
        flagIDAT = true;
        int oldLength = buffer.length;
        int newLength = oldLength + length;
        byte[] zlibNew = new byte[newLength];
        System.arraycopy(buffer, 0, zlibNew, 0, oldLength);
        for (int i = oldLength; i < newLength; i++) zlibNew[i] = (byte) source.read();
        buffer = zlibNew;
        long okCRC = source.getCRC();
        if (source.read4n() != okCRC) throw new IOException("Invalid CRC for IDAT chunk");
    }

    private RGB[][] decode(byte[] zlibStream) throws IOException {
        InflaterInputStream inflater = new InflaterInputStream(new java.io.ByteArrayInputStream(zlibStream));
        int bytesPerScanline = (int) ((float) width * (float) bitDepth * (float) colorType_components / 8.0f);
        if (colorType == 3) {
            switch(bitDepth) {
                case 1:
                    if ((width % 8) > 0) bytesPerScanline++;
                    break;
                case 2:
                    if ((width % 4) > 0) bytesPerScanline++;
                    break;
                case 4:
                    if ((width % 2) > 0) bytesPerScanline++;
                    break;
            }
        }
        RGB[][] rgb = new RGB[height][width];
        if (interlaceMethod == 1 && bitDepth < 8) {
            rgb = deCompressSpecial(rgb, inflater);
        } else {
            int bytesPerPixel = bitDepth * colorType_components / 8;
            bytesPerPixel = (bytesPerPixel == 0) ? 1 : bytesPerPixel;
            int[][] scanlines = new int[height][bytesPerScanline];
            scanlines = deCompress(scanlines, inflater, bytesPerPixel);
            int b, bb;
            int cr, cg, cb, ca;
            switch(colorType) {
                case 0:
                    switch(bitDepth) {
                        case 1:
                            for (int y = 0; y < height; y++) {
                                int scanX = 0;
                                for (int x = 0; x < width; ) {
                                    b = scanlines[y][scanX++];
                                    for (int z = 7; ((x < width) && (z > -1)); z--) {
                                        bb = ((b >> z) & 0x01) * 256;
                                        RGB color = new RGB(bb, bb, bb);
                                        if (tRNSPassRequired) {
                                            if (color.compareTo(tRNSKeyColor, false)) {
                                                color = new RGB(0);
                                                color.setAlpha(0.0f);
                                            }
                                        }
                                        rgb[y][x++] = color;
                                    }
                                }
                            }
                            break;
                        case 2:
                            for (int y = 0; y < height; y++) {
                                int scanX = 0;
                                for (int x = 0; x < width; ) {
                                    b = scanlines[y][scanX++];
                                    for (int z = 6; ((x < width) && (z > -1)); z -= 2) {
                                        bb = ((((b >> z) & 0x03) + 1) * 64) - 1;
                                        bb = (bb < 0) ? 0 : bb;
                                        RGB color = new RGB(bb, bb, bb);
                                        if (tRNSPassRequired) {
                                            if (color.compareTo(tRNSKeyColor, false)) {
                                                color = new RGB(0);
                                                color.setAlpha(0.0f);
                                            }
                                        }
                                        rgb[y][x++] = color;
                                    }
                                }
                            }
                            break;
                        case 4:
                            for (int y = 0; y < height; y++) {
                                int scanX = 0;
                                for (int x = 0; x < width; ) {
                                    b = scanlines[y][scanX++];
                                    bb = ((((b >> 4) & 0x0f) + 1) * 16) - 1;
                                    bb = (bb < 0) ? 0 : bb;
                                    RGB color = new RGB(bb, bb, bb);
                                    if (tRNSPassRequired) {
                                        if (color.compareTo(tRNSKeyColor, false)) {
                                            color = new RGB(0);
                                            color.setAlpha(0.0f);
                                        }
                                    }
                                    rgb[y][x++] = color;
                                    if (x < width) {
                                        bb = (((b & 0x0f) + 1) * 16) - 1;
                                        bb = (bb < 0) ? 0 : bb;
                                        color = new RGB(bb, bb, bb);
                                        if (tRNSPassRequired) {
                                            if (color.compareTo(tRNSKeyColor, false)) {
                                                color = new RGB(0);
                                                color.setAlpha(0.0f);
                                            }
                                        }
                                        rgb[y][x++] = color;
                                    }
                                }
                            }
                            break;
                        case 8:
                            for (int y = 0; y < height; y++) {
                                for (int x = 0; x < width; x++) {
                                    RGB color = new RGB(scanlines[y][x]);
                                    if (tRNSPassRequired) {
                                        if (color.compareTo(tRNSKeyColor, false)) {
                                            color = new RGB(0);
                                            color.setAlpha(0.0f);
                                        }
                                    }
                                    rgb[y][x] = color;
                                }
                            }
                            break;
                        case 16:
                            for (int y = 0; y < height; y++) {
                                int scanX = 0;
                                for (int x = 0; x < width; x++) {
                                    RGB color = new RGB((float) (scanlines[y][scanX++] << 8 | scanlines[y][scanX++]) / 65535.0f);
                                    if (tRNSPassRequired) {
                                        if (color.compareTo(tRNSKeyColor, false)) {
                                            color = new RGB(0);
                                            color.setAlpha(0.0f);
                                        }
                                    }
                                    rgb[y][x] = color;
                                }
                            }
                            break;
                    }
                    break;
                case 2:
                    switch(bitDepth) {
                        case 8:
                            for (int y = 0; y < height; y++) {
                                int scanX = 0;
                                for (int x = 0; x < width; x++) {
                                    RGB color = new RGB(scanlines[y][scanX++], scanlines[y][scanX++], scanlines[y][scanX++]);
                                    if (tRNSPassRequired) {
                                        if (color.compareTo(tRNSKeyColor, false)) {
                                            color = new RGB(0);
                                            color.setAlpha(0.0f);
                                        }
                                    }
                                    rgb[y][x] = color;
                                }
                            }
                            break;
                        case 16:
                            for (int y = 0; y < height; y++) {
                                int scanX = 0;
                                for (int x = 0; x < width; x++) {
                                    float aa = (float) (scanlines[y][scanX++] << 8 | scanlines[y][scanX++]) / 65535.0f;
                                    float ab = (float) (scanlines[y][scanX++] << 8 | scanlines[y][scanX++]) / 65535.0f;
                                    float ac = (float) (scanlines[y][scanX++] << 8 | scanlines[y][scanX++]) / 65535.0f;
                                    RGB color = new RGB(aa, ab, ac);
                                    if (tRNSPassRequired) {
                                        if (color.compareTo(tRNSKeyColor, false)) {
                                            color = new RGB(0);
                                            color.setAlpha(0.0f);
                                        }
                                    }
                                    rgb[x][y] = color;
                                }
                            }
                            break;
                    }
                    break;
                case 3:
                    switch(bitDepth) {
                        case 1:
                            for (int y = 0; y < height; y++) {
                                int scanX = 0;
                                for (int x = 0; x < width; ) {
                                    if (scanX < bytesPerScanline) {
                                        b = scanlines[y][scanX++];
                                        for (int z = 7; ((x < width) && (z > -1)); z--) {
                                            bb = (b >> z) & 0x01;
                                            rgb[y][x++] = palette[bb];
                                        }
                                    } else break;
                                }
                            }
                            break;
                        case 2:
                            for (int y = 0; y < height; y++) {
                                int scanX = 0;
                                for (int x = 0; x < width; ) {
                                    if (scanX < bytesPerScanline) {
                                        b = scanlines[y][scanX++];
                                        for (int z = 6; ((x < width) && (z > -1)); z -= 2) {
                                            bb = (b >> z) & 0x03;
                                            rgb[y][x++] = palette[bb];
                                        }
                                    } else break;
                                }
                            }
                            break;
                        case 4:
                            for (int y = 0; y < height; y++) {
                                int scanX = 0;
                                for (int x = 0; x < width; ) {
                                    if (scanX <= bytesPerScanline) {
                                        b = scanlines[y][scanX++];
                                        bb = (b >> 4) & 0x0f;
                                        rgb[y][x++] = palette[bb];
                                        if (x < width) {
                                            bb = b & 0x0f;
                                            rgb[y][x++] = palette[bb];
                                        }
                                    } else break;
                                }
                            }
                            break;
                        case 8:
                            for (int y = 0; y < height; y++) {
                                for (int x = 0; x < width; x++) {
                                    b = scanlines[y][x];
                                    b = (b < palette.length) ? b : 0;
                                    rgb[y][x] = palette[b];
                                }
                            }
                            break;
                    }
                    break;
                case 4:
                    switch(bitDepth) {
                        case 8:
                            for (int y = 0; y < height; y++) {
                                int scanX = 0;
                                for (int x = 0; x < width; x++) {
                                    cr = scanlines[y][scanX++];
                                    ca = scanlines[y][scanX++];
                                    cr -= (255 - ca);
                                    rgb[y][x] = new RGB(cr, ca);
                                }
                            }
                            break;
                        case 16:
                            for (int y = 0; y < height; y++) {
                                int scanX = 0;
                                for (int x = 0; x < width; x++) {
                                    float aa = (float) (scanlines[y][scanX++] << 8 | scanlines[y][scanX++]) / 65535.0f;
                                    float ab = (float) (scanlines[y][scanX++] << 8 | scanlines[y][scanX++]) / 65535.0f;
                                    aa -= (1.0f - ab);
                                    rgb[y][x] = new RGB(aa, ab);
                                }
                            }
                            break;
                    }
                    break;
                case 6:
                    switch(bitDepth) {
                        case 8:
                            int idx = 0;
                            for (int y = 0; y < height; y++) {
                                int scanX = 0;
                                for (int x = 0; x < width; x++) {
                                    cr = scanlines[y][scanX++];
                                    cg = scanlines[y][scanX++];
                                    cb = scanlines[y][scanX++];
                                    ca = scanlines[y][scanX++];
                                    cr -= (255 - ca);
                                    cg -= (255 - ca);
                                    cb -= (255 - ca);
                                    rgb[y][x] = new RGB(cr, cg, cb, ca);
                                }
                            }
                            break;
                        case 16:
                            for (int y = 0; y < height; y++) {
                                int scanX = 0;
                                for (int x = 0; x < width; x++) {
                                    float aa = (float) (scanlines[y][scanX++] << 8 | scanlines[y][scanX++]) / 65535.0f;
                                    float ab = (float) (scanlines[y][scanX++] << 8 | scanlines[y][scanX++]) / 65535.0f;
                                    float ac = (float) (scanlines[y][scanX++] << 8 | scanlines[y][scanX++]) / 65535.0f;
                                    float ad = (float) (scanlines[y][scanX++] << 8 | scanlines[y][scanX++]) / 65535.0f;
                                    aa -= (1.0f - ad);
                                    ab -= (1.0f - ad);
                                    ac -= (1.0f - ad);
                                    rgb[y][x] = new RGB(aa, ab, ac, ad);
                                }
                            }
                            break;
                    }
            }
        }
        return rgb;
    }

    /**
     * get the previous row value of the pass
     * @param row int the current row
     * @param pass int the current pass
     * @return int
     */
    private int getPreviousRow(int row, int pass) {
        if (interlaceMethod == 1) {
            int[] starting_Row = { 0, 0, 4, 0, 2, 0, 1 };
            int[] row_Increment = { 8, 8, 8, 4, 4, 2, 2 };
            int currentPassMin = starting_Row[pass];
            if (row == currentPassMin) return 0; else return ((row > (row - row_Increment[pass])) ? (row - row_Increment[pass]) : 0);
        } else {
            return ((row > 0) ? (row - 1) : 0);
        }
    }

    /**
     * get the previous column value of the pass
     * @param col int the current column
     * @param pass int the current pass
     * @return int
     */
    private int getPreviousCol(int col, int pass) {
        if (interlaceMethod == 1) {
            int[] starting_Col = { 0, 4, 0, 2, 0, 1, 0 };
            int[] col_Increment = { 8, 8, 4, 4, 2, 2, 1 };
            int currentPassMin = starting_Col[pass];
            if (col == currentPassMin) return 0; else return ((col > (col - col_Increment[pass])) ? (col - col_Increment[pass]) : 0);
        } else {
            return ((col > 0) ? (col - 1) : 0);
        }
    }

    /**
     * Decompress, deinterlace, unfilter and set the pixels colors
     * Used for special interlaced images of less than 8 bit depth (padding involved)
     * @param rgb RGB[][] the RGB colors of pixels
     * @param inflater InputStream the inflater InputStream to read the zlib data from
     * @return RGB[][] the RGB colors of pixels set
     * @Exception java.io.IOException
     */
    private RGB[][] deCompressSpecial(RGB[][] rgb, InputStream inflater) throws IOException {
        int[] starting_Row = { 0, 0, 4, 0, 2, 0, 1 };
        int[] starting_Col = { 0, 4, 0, 2, 0, 1, 0 };
        int[] row_Increment = { 8, 8, 8, 4, 4, 2, 2 };
        int[] col_Increment = { 8, 8, 4, 4, 2, 2, 1 };
        int[] block_Height = { 8, 8, 4, 4, 2, 2, 1 };
        int[] block_Width = { 8, 4, 4, 2, 2, 1, 1 };
        int row, col, filter;
        int[][] pixels = new int[height][width];
        int a, b, c, pa, pb, pc, p;
        for (int pass = 0; pass < 7; pass++) {
            row = starting_Row[pass];
            while (row < height) {
                int minHeight = (block_Height[pass] < (height - row)) ? block_Height[pass] : (height - row);
                b = inflater.read();
                if (b > -1) {
                    filter = (b >> 4) & 0x03;
                    col = starting_Col[pass];
                    while (col < width) {
                        int minWidth = (block_Width[pass] < (width - col)) ? block_Width[pass] : (width - col);
                        b = inflater.read();
                        if (b > -1) {
                            switch(bitDepth) {
                                case 1:
                                    for (int i = 7; i > -1; i--) {
                                        if (col < width) {
                                            a = (b >> i) & 0x01;
                                            for (int x = 0; x < minHeight; x++) {
                                                int rowx = row + x;
                                                if (rowx < height) {
                                                    for (int y = 0; y < minWidth; y++) {
                                                        int coly = col + y;
                                                        if (coly < width) pixels[rowx][coly] = a; else break;
                                                    }
                                                } else break;
                                            }
                                        } else break;
                                        col += col_Increment[pass];
                                    }
                                    break;
                                case 2:
                                    for (int i = 6; i > -1; i -= 2) {
                                        if (col < width) {
                                            a = (b >> i) & 0x03;
                                            for (int x = 0; x < minHeight; x++) {
                                                int rowx = row + x;
                                                if (rowx < height) {
                                                    for (int y = 0; y < minWidth; y++) {
                                                        int coly = col + y;
                                                        if (coly < width) pixels[rowx][coly] = a; else break;
                                                    }
                                                } else break;
                                            }
                                        } else break;
                                        col += col_Increment[pass];
                                    }
                                    break;
                                case 4:
                                    for (int i = 4; i > -1; i -= 4) {
                                        if (col < width) {
                                            a = (b >> i) & 0x0f;
                                            for (int x = 0; x < minHeight; x++) {
                                                int rowx = row + x;
                                                if (rowx < height) {
                                                    for (int y = 0; y < minWidth; y++) {
                                                        int coly = col + y;
                                                        if (coly < width) pixels[rowx][coly] = a; else break;
                                                    }
                                                } else break;
                                            }
                                        } else break;
                                        col += col_Increment[pass];
                                    }
                                    break;
                            }
                        } else break;
                    }
                    int nbboucle = 8 / bitDepth;
                    switch(filter) {
                        case 0:
                            break;
                        case 1:
                            col = starting_Col[pass] + col_Increment[pass];
                            while (col < width) {
                                for (int i = 0; i < nbboucle; i++) {
                                    if (col < width) {
                                        pixels[row][col] = (byte) (pixels[row][col] + pixels[row][getPreviousCol(col, pass)]) & 0xff;
                                    }
                                    col += col_Increment[pass];
                                }
                            }
                            break;
                        case 2:
                            if (row == 0) break;
                            col = starting_Col[pass];
                            while (col < width) {
                                for (int i = 0; i < nbboucle; i++) {
                                    if (col < width) {
                                        pixels[row][col] = (byte) (pixels[row][col] + pixels[getPreviousRow(row, pass)][col]) & 0xff;
                                    }
                                    col += col_Increment[pass];
                                }
                            }
                            break;
                        case 3:
                            col = starting_Col[pass];
                            pixels[row][col] = (byte) (pixels[row][col] + (((row == 0) ? 0 : pixels[getPreviousRow(row, pass)][col]) / 2)) & 0xff;
                            col += col_Increment[pass];
                            while (col < width) {
                                pixels[row][col] = (byte) (pixels[row][col] + (pixels[row][getPreviousCol(col, pass)] + ((row == 0) ? 0 : pixels[getPreviousRow(row, pass)][col])) / 2) & 0xff;
                                col += col_Increment[pass];
                            }
                            break;
                        case 4:
                            if (row == 0) {
                                col = starting_Col[pass];
                                pixels[row][col] = 0xff & pixels[row][col];
                                col += col_Increment[pass];
                                while (col < width) {
                                    p = pixels[row][getPreviousCol(col, pass)];
                                    pixels[row][col] = 0xff & (byte) (p + pixels[row][col]);
                                    col += col_Increment[pass];
                                }
                            } else {
                                col = starting_Col[pass];
                                pixels[row][col] = 0xff & (byte) (pixels[getPreviousRow(row, pass)][col] + pixels[row][col]);
                                col += col_Increment[pass];
                                while (col < width) {
                                    a = pixels[row][getPreviousCol(col, pass)];
                                    b = pixels[getPreviousRow(row, pass)][col];
                                    c = pixels[getPreviousRow(row, pass)][getPreviousCol(col, pass)];
                                    p = (a + b) - c;
                                    pa = p > a ? p - a : a - p;
                                    pb = p > b ? p - b : b - p;
                                    pc = p > c ? p - c : c - p;
                                    if ((pa <= pb) && (pa <= pc)) p = a; else if (pb <= pc) p = b; else p = c;
                                    pixels[row][col] = 0xff & (byte) (p + pixels[row][col]);
                                    col += col_Increment[pass];
                                }
                            }
                            break;
                    }
                }
                row += row_Increment[pass];
            }
        }
        p = 256 / (bitDepth * bitDepth);
        row = 0;
        while (row < height) {
            col = 0;
            while (col < width) {
                if (colorType == 0) {
                    RGB color;
                    if (bitDepth == 1) color = new RGB(pixels[row][col] * p); else color = new RGB(((pixels[row][col] + 1) * p) - 1);
                    if (tRNSPassRequired) {
                        if (color.compareTo(tRNSKeyColor, false)) {
                            color = new RGB(0);
                            color.setAlpha(0.0f);
                        }
                    }
                    rgb[row][col] = color;
                } else rgb[row][col] = palette[pixels[row][col]];
                col++;
            }
            row++;
        }
        return rgb;
    }

    /**
     * Decompress, deinterlace, and unfilter the scanlines
     * Used for special interlaced images of less than 8 bit depth (padding involved)
     * @param scanlines int[][] the scanlines to decode into
     * @param inflater InputStream the inflater InputStream to read the zlib data from
     * @param bytesPerPixel int the number of bytes per pixel of scanlines
     * @return int[][] the scanlines decoded
     * @Exception java.io.IOException
     */
    private int[][] deCompress(int[][] scanlines, InputStream inflater, int bytesPerPixel) throws IOException {
        int[] starting_Row = { 0, 0, 4, 0, 2, 0, 1 };
        int[] starting_Col = { 0, 4, 0, 2, 0, 1, 0 };
        int[] row_Increment = { 8, 8, 8, 4, 4, 2, 2 };
        int[] col_Increment = { 8, 8, 4, 4, 2, 2, 1 };
        int a, b, c, pa, pb, pc, p;
        int maxPass = 7;
        if (interlaceMethod == 0) maxPass = 1;
        int row, col;
        for (int pass = 0; pass < maxPass; pass++) {
            if (interlaceMethod == 1) row = starting_Row[pass]; else row = 0;
            while (row < height) {
                int filter = inflater.read();
                if (interlaceMethod == 1) col = starting_Col[pass]; else col = 0;
                while (col < width) {
                    for (int i = 0; i < bytesPerPixel; i++) {
                        int bitcol = (col * bytesPerPixel) + i;
                        if (bitcol < scanlines[row].length) scanlines[row][bitcol] = inflater.read();
                    }
                    if (interlaceMethod == 1) col += col_Increment[pass]; else col++;
                }
                switch(filter) {
                    case 0:
                        break;
                    case 1:
                        if (interlaceMethod == 1) col = starting_Col[pass] + col_Increment[pass]; else col = 1;
                        while (col < width) {
                            for (int i = 0; i < bytesPerPixel; i++) {
                                int bitcol = (col * bytesPerPixel) + i;
                                if (bitcol < scanlines[row].length) {
                                    scanlines[row][bitcol] = (byte) (scanlines[row][bitcol] + scanlines[row][(getPreviousCol(col, pass) * bytesPerPixel) + i]) & 0xff;
                                }
                            }
                            if (interlaceMethod == 1) col += col_Increment[pass]; else col++;
                        }
                        break;
                    case 2:
                        if (row == 0) break;
                        if (interlaceMethod == 1) col = starting_Col[pass]; else col = 0;
                        while (col < width) {
                            for (int i = 0; i < bytesPerPixel; i++) {
                                int bitcol = (col * bytesPerPixel) + i;
                                if (bitcol < scanlines[row].length) {
                                    scanlines[row][bitcol] = (byte) (scanlines[row][bitcol] + scanlines[getPreviousRow(row, pass)][bitcol]) & 0xff;
                                }
                            }
                            if (interlaceMethod == 1) col += col_Increment[pass]; else col++;
                        }
                        break;
                    case 3:
                        if (interlaceMethod == 1) col = starting_Col[pass]; else col = 0;
                        if (row > 0) {
                            for (int i = 0; i < bytesPerPixel; i++) {
                                int bitcol = (col * bytesPerPixel) + i;
                                if (bitcol < scanlines[row].length) {
                                    scanlines[row][bitcol] = (byte) (scanlines[row][bitcol] + (scanlines[getPreviousRow(row, pass)][bitcol] / 2)) & 0xff;
                                }
                            }
                        }
                        if (interlaceMethod == 1) col += col_Increment[pass]; else col++;
                        while (col < width) {
                            for (int i = 0; i < bytesPerPixel; i++) {
                                int bitcol = (col * bytesPerPixel) + i;
                                if (bitcol < scanlines[row].length) {
                                    scanlines[row][bitcol] = (byte) (scanlines[row][bitcol] + (scanlines[row][(getPreviousCol(col, pass) * bytesPerPixel) + i] + ((row == 0) ? 0 : scanlines[getPreviousRow(row, pass)][bitcol])) / 2) & 0xff;
                                }
                            }
                            if (interlaceMethod == 1) col += col_Increment[pass]; else col++;
                        }
                        break;
                    case 4:
                        if (row == 0) {
                            if (interlaceMethod == 1) col = starting_Col[pass]; else col = 0;
                            for (int i = 0; i < bytesPerPixel; i++) {
                                int bitcol = (col * bytesPerPixel) + i;
                                if (bitcol < scanlines[row].length) scanlines[row][bitcol] = 0xff & scanlines[row][bitcol];
                            }
                            if (interlaceMethod == 1) col += col_Increment[pass]; else col++;
                            while (col < width) {
                                for (int i = 0; i < bytesPerPixel; i++) {
                                    int bitcol = (col * bytesPerPixel) + i;
                                    if (bitcol < scanlines[row].length) {
                                        p = scanlines[row][(getPreviousCol(col, pass) * bytesPerPixel) + i];
                                        scanlines[row][bitcol] = 0xff & (byte) (p + scanlines[row][bitcol]);
                                    }
                                }
                                if (interlaceMethod == 1) col += col_Increment[pass]; else col++;
                            }
                        } else {
                            if (interlaceMethod == 1) col = starting_Col[pass]; else col = 0;
                            for (int i = 0; i < bytesPerPixel; i++) {
                                int bitcol = (col * bytesPerPixel) + i;
                                if (bitcol < scanlines[row].length) scanlines[row][bitcol] = 0xff & (byte) (scanlines[getPreviousRow(row, pass)][bitcol] + scanlines[row][bitcol]);
                            }
                            if (interlaceMethod == 1) col += col_Increment[pass]; else col++;
                            while (col < width) {
                                for (int i = 0; i < bytesPerPixel; i++) {
                                    int bitcol = (col * bytesPerPixel) + i;
                                    if (bitcol < scanlines[row].length) {
                                        a = scanlines[row][(getPreviousCol(col, pass) * bytesPerPixel) + i];
                                        b = scanlines[getPreviousRow(row, pass)][bitcol];
                                        c = scanlines[getPreviousRow(row, pass)][(getPreviousCol(col, pass) * bytesPerPixel) + i];
                                        p = (a + b) - c;
                                        pa = p > a ? p - a : a - p;
                                        pb = p > b ? p - b : b - p;
                                        pc = p > c ? p - c : c - p;
                                        if ((pa <= pb) && (pa <= pc)) p = a; else if (pb <= pc) p = b; else p = c;
                                        scanlines[row][bitcol] = 0xff & (byte) (p + scanlines[row][bitcol]);
                                    }
                                }
                                if (interlaceMethod == 1) col += col_Increment[pass]; else col++;
                            }
                        }
                        break;
                }
                if (interlaceMethod == 1) row += row_Increment[pass]; else row++;
            }
        }
        return scanlines;
    }
}
