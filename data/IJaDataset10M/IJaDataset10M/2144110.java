package visad.data.tiff;

import java.lang.reflect.Field;
import java.io.*;
import java.util.*;
import visad.*;
import visad.data.BadFormException;

/**
 * A utility class for manipulating TIFF files.
 *
 * @author Curtis Rueden ctrueden at wisc.edu
 * @author Eric Kjellman egkjellman at wisc.edu
 * @author Melissa Linkert linkert at cs.wisc.edu
 */
public abstract class TiffTools {

    public static final boolean DEBUG = false;

    public static final int LITTLE_ENDIAN = 0;

    public static final int NEW_SUBFILE_TYPE = 254;

    public static final int SUBFILE_TYPE = 255;

    public static final int IMAGE_WIDTH = 256;

    public static final int IMAGE_LENGTH = 257;

    public static final int BITS_PER_SAMPLE = 258;

    public static final int COMPRESSION = 259;

    public static final int PHOTOMETRIC_INTERPRETATION = 262;

    public static final int THRESHHOLDING = 263;

    public static final int CELL_WIDTH = 264;

    public static final int CELL_LENGTH = 265;

    public static final int FILL_ORDER = 266;

    public static final int DOCUMENT_NAME = 269;

    public static final int IMAGE_DESCRIPTION = 270;

    public static final int MAKE = 271;

    public static final int MODEL = 272;

    public static final int STRIP_OFFSETS = 273;

    public static final int ORIENTATION = 274;

    public static final int SAMPLES_PER_PIXEL = 277;

    public static final int ROWS_PER_STRIP = 278;

    public static final int STRIP_BYTE_COUNTS = 279;

    public static final int MIN_SAMPLE_VALUE = 280;

    public static final int MAX_SAMPLE_VALUE = 281;

    public static final int X_RESOLUTION = 282;

    public static final int Y_RESOLUTION = 283;

    public static final int PLANAR_CONFIGURATION = 284;

    public static final int PAGE_NAME = 285;

    public static final int X_POSITION = 286;

    public static final int Y_POSITION = 287;

    public static final int FREE_OFFSETS = 288;

    public static final int FREE_BYTE_COUNTS = 289;

    public static final int GRAY_RESPONSE_UNIT = 290;

    public static final int GRAY_RESPONSE_CURVE = 291;

    public static final int T4_OPTIONS = 292;

    public static final int T6_OPTIONS = 293;

    public static final int RESOLUTION_UNIT = 296;

    public static final int PAGE_NUMBER = 297;

    public static final int TRANSFER_FUNCTION = 301;

    public static final int SOFTWARE = 305;

    public static final int DATE_TIME = 306;

    public static final int ARTIST = 315;

    public static final int HOST_COMPUTER = 316;

    public static final int PREDICTOR = 317;

    public static final int WHITE_POINT = 318;

    public static final int PRIMARY_CHROMATICITIES = 319;

    public static final int COLOR_MAP = 320;

    public static final int HALFTONE_HINTS = 321;

    public static final int TILE_WIDTH = 322;

    public static final int TILE_LENGTH = 323;

    public static final int TILE_OFFSETS = 324;

    public static final int TILE_BYTE_COUNTS = 325;

    public static final int INK_SET = 332;

    public static final int INK_NAMES = 333;

    public static final int NUMBER_OF_INKS = 334;

    public static final int DOT_RANGE = 336;

    public static final int TARGET_PRINTER = 337;

    public static final int EXTRA_SAMPLES = 338;

    public static final int SAMPLE_FORMAT = 339;

    public static final int S_MIN_SAMPLE_VALUE = 340;

    public static final int S_MAX_SAMPLE_VALUE = 341;

    public static final int TRANSFER_RANGE = 342;

    public static final int JPEG_PROC = 512;

    public static final int JPEG_INTERCHANGE_FORMAT = 513;

    public static final int JPEG_INTERCHANGE_FORMAT_LENGTH = 514;

    public static final int JPEG_RESTART_INTERVAL = 515;

    public static final int JPEG_LOSSLESS_PREDICTORS = 517;

    public static final int JPEG_POINT_TRANSFORMS = 518;

    public static final int JPEG_Q_TABLES = 519;

    public static final int JPEG_DC_TABLES = 520;

    public static final int JPEG_AC_TABLES = 521;

    public static final int Y_CB_CR_COEFFICIENTS = 529;

    public static final int Y_CB_CR_SUB_SAMPLING = 530;

    public static final int Y_CB_CR_POSITIONING = 531;

    public static final int REFERENCE_BLACK_WHITE = 532;

    public static final int COPYRIGHT = 33432;

    public static final int UNCOMPRESSED = 1;

    public static final int CCITT_1D = 2;

    public static final int GROUP_3_FAX = 3;

    public static final int GROUP_4_FAX = 4;

    public static final int LZW = 5;

    public static final int JPEG = 6;

    public static final int PACK_BITS = 32773;

    public static final int WHITE_IS_ZERO = 0;

    public static final int BLACK_IS_ZERO = 1;

    public static final int RGB = 2;

    public static final int RGB_PALETTE = 3;

    public static final int TRANSPARENCY_MASK = 4;

    public static final int CMYK = 5;

    public static final int Y_CB_CR = 6;

    public static final int CIE_LAB = 8;

    public static final int BYTE = 1;

    public static final int ASCII = 2;

    public static final int SHORT = 3;

    public static final int LONG = 4;

    public static final int RATIONAL = 5;

    public static final int SBYTE = 6;

    public static final int UNDEFINED = 7;

    public static final int SSHORT = 8;

    public static final int SLONG = 9;

    public static final int SRATIONAL = 10;

    public static final int FLOAT = 11;

    public static final int DOUBLE = 12;

    protected static final int CLEAR_CODE = 256;

    protected static final int EOI_CODE = 257;

    public static final int MAGIC_NUMBER = 42;

    public static final int LITTLE = 0x49;

    public static final int BIG = 0x4d;

    /**
   * Tests the given data block to see if it represents
   * the first few bytes of a TIFF file.
   */
    public static boolean isValidHeader(byte[] block) {
        if (block.length < 4) {
            return false;
        }
        boolean little = block[0] == LITTLE && block[1] == LITTLE;
        boolean big = block[0] == BIG && block[1] == BIG;
        if (!little && !big) return false;
        short magic = bytesToShort(block, 2, little);
        return magic == MAGIC_NUMBER;
    }

    /** Gets whether the TIFF information in the given IFD is little endian. */
    public static boolean isLittleEndian(Hashtable ifd) throws BadFormException {
        return ((Boolean) getIFDValue(ifd, LITTLE_ENDIAN, true, Boolean.class)).booleanValue();
    }

    /**
   * Gets all IFDs within the given TIFF file, or null
   * if the given file is not a valid TIFF file.
   */
    public static Hashtable[] getIFDs(RandomAccessFile in) throws IOException {
        return getIFDs(in, 0);
    }

    /**
   * Gets all IFDs within the given TIFF file, or null
   * if the given file is not a valid TIFF file.
   */
    public static Hashtable[] getIFDs(RandomAccessFile in, int globalOffset) throws IOException {
        if (DEBUG) debug("getIFDs: reading IFD entries");
        in.seek(globalOffset);
        byte[] order = new byte[2];
        in.read(order);
        boolean littleEndian = order[0] == LITTLE && order[1] == LITTLE;
        boolean bigEndian = order[0] == BIG && order[1] == BIG;
        if (!littleEndian && !bigEndian) return null;
        int magic = read2UnsignedBytes(in, littleEndian);
        if (magic != MAGIC_NUMBER) return null;
        long offset = read4UnsignedBytes(in, littleEndian);
        long ifdMax = (in.length() - 8) / 18;
        Vector v = new Vector();
        for (long ifdNum = 0; ifdNum < ifdMax; ifdNum++) {
            Hashtable ifd = new Hashtable();
            v.add(ifd);
            ifd.put(new Integer(LITTLE_ENDIAN), new Boolean(littleEndian));
            if (DEBUG) {
                debug("getIFDs: seeking IFD #" + ifdNum + " at " + (globalOffset + offset));
            }
            in.seek(globalOffset + offset);
            int numEntries = read2UnsignedBytes(in, littleEndian);
            for (int i = 0; i < numEntries; i++) {
                int tag = read2UnsignedBytes(in, littleEndian);
                int type = read2UnsignedBytes(in, littleEndian);
                int count = (int) read4UnsignedBytes(in, littleEndian);
                if (DEBUG) {
                    debug("getIFDs: read " + getIFDTagName(tag) + " (type=" + type + "; count=" + count + ")");
                }
                if (count < 0) return null;
                Object value = null;
                long pos = in.getFilePointer() + 4;
                if (type == BYTE) {
                    short[] bytes = new short[count];
                    if (count > 4) {
                        in.seek(globalOffset + read4UnsignedBytes(in, littleEndian));
                    }
                    for (int j = 0; j < count; j++) bytes[j] = readUnsignedByte(in);
                    if (bytes.length == 1) value = new Short(bytes[0]); else value = bytes;
                } else if (type == ASCII) {
                    byte[] ascii = new byte[count];
                    if (count > 4) {
                        in.seek(globalOffset + read4UnsignedBytes(in, littleEndian));
                    }
                    readFully(in, ascii);
                    int nullCount = 0;
                    for (int j = 0; j < count; j++) if (ascii[j] == 0) nullCount++;
                    String[] strings = new String[nullCount];
                    int c = 0, ndx = -1;
                    for (int j = 0; j < count; j++) {
                        if (ascii[j] == 0) {
                            strings[c++] = new String(ascii, ndx + 1, j - ndx - 1);
                            ndx = j;
                        }
                    }
                    if (strings.length == 1) value = strings[0]; else value = strings;
                } else if (type == SHORT) {
                    int[] shorts = new int[count];
                    if (count > 2) {
                        in.seek(globalOffset + read4UnsignedBytes(in, littleEndian));
                    }
                    for (int j = 0; j < count; j++) {
                        shorts[j] = read2UnsignedBytes(in, littleEndian);
                    }
                    if (shorts.length == 1) value = new Integer(shorts[0]); else value = shorts;
                } else if (type == LONG) {
                    long[] longs = new long[count];
                    if (count > 1) {
                        in.seek(globalOffset + read4UnsignedBytes(in, littleEndian));
                    }
                    for (int j = 0; j < count; j++) {
                        longs[j] = read4UnsignedBytes(in, littleEndian);
                    }
                    if (longs.length == 1) value = new Long(longs[0]); else value = longs;
                } else if (type == RATIONAL) {
                    TiffRational[] rationals = new TiffRational[count];
                    in.seek(globalOffset + read4UnsignedBytes(in, littleEndian));
                    for (int j = 0; j < count; j++) {
                        long numer = read4UnsignedBytes(in, littleEndian);
                        long denom = read4UnsignedBytes(in, littleEndian);
                        rationals[j] = new TiffRational(numer, denom);
                    }
                    if (rationals.length == 1) value = rationals[0]; else value = rationals;
                } else if (type == SBYTE || type == UNDEFINED) {
                    byte[] sbytes = new byte[count];
                    if (count > 4) {
                        in.seek(globalOffset + read4UnsignedBytes(in, littleEndian));
                    }
                    readFully(in, sbytes);
                    if (sbytes.length == 1) value = new Byte(sbytes[0]); else value = sbytes;
                } else if (type == SSHORT) {
                    short[] sshorts = new short[count];
                    if (count > 2) {
                        in.seek(globalOffset + read4UnsignedBytes(in, littleEndian));
                    }
                    for (int j = 0; j < count; j++) {
                        sshorts[j] = read2SignedBytes(in, littleEndian);
                    }
                    if (sshorts.length == 1) value = new Short(sshorts[0]); else value = sshorts;
                } else if (type == SLONG) {
                    int[] slongs = new int[count];
                    if (count > 1) {
                        in.seek(globalOffset + read4UnsignedBytes(in, littleEndian));
                    }
                    for (int j = 0; j < count; j++) {
                        slongs[j] = read4SignedBytes(in, littleEndian);
                    }
                    if (slongs.length == 1) value = new Integer(slongs[0]); else value = slongs;
                } else if (type == SRATIONAL) {
                    TiffRational[] srationals = new TiffRational[count];
                    in.seek(globalOffset + read4UnsignedBytes(in, littleEndian));
                    for (int j = 0; j < count; j++) {
                        int numer = read4SignedBytes(in, littleEndian);
                        int denom = read4SignedBytes(in, littleEndian);
                        srationals[j] = new TiffRational(numer, denom);
                    }
                    if (srationals.length == 1) value = srationals[0]; else value = srationals;
                } else if (type == FLOAT) {
                    float[] floats = new float[count];
                    if (count > 1) {
                        in.seek(globalOffset + read4UnsignedBytes(in, littleEndian));
                    }
                    for (int j = 0; j < count; j++) floats[j] = readFloat(in, littleEndian);
                    if (floats.length == 1) value = new Float(floats[0]); else value = floats;
                } else if (type == DOUBLE) {
                    double[] doubles = new double[count];
                    in.seek(globalOffset + read4UnsignedBytes(in, littleEndian));
                    for (int j = 0; j < count; j++) {
                        doubles[j] = readDouble(in, littleEndian);
                    }
                    if (doubles.length == 1) value = new Double(doubles[0]); else value = doubles;
                }
                in.seek(globalOffset + pos);
                if (value != null) ifd.put(new Integer(tag), value);
            }
            offset = read4UnsignedBytes(in, littleEndian);
            if (offset == 0) break;
        }
        Hashtable[] ifds = new Hashtable[v.size()];
        v.copyInto(ifds);
        return ifds;
    }

    /** Gets the name of the IFD tag encoded by the given number. */
    public static String getIFDTagName(int tag) {
        Field[] fields = TiffTools.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                if (fields[i].getInt(null) == tag) return fields[i].getName();
            } catch (Exception exc) {
            }
        }
        return "" + tag;
    }

    /** Gets the given directory entry value from the specified IFD. */
    public static Object getIFDValue(Hashtable ifd, int tag) {
        return ifd.get(new Integer(tag));
    }

    /**
   * Gets the given directory entry value from the specified IFD,
   * performing some error checking.
   */
    public static Object getIFDValue(Hashtable ifd, int tag, boolean checkNull, Class checkClass) throws BadFormException {
        Object value = ifd.get(new Integer(tag));
        if (checkNull && value == null) {
            throw new BadFormException(getIFDTagName(tag) + " directory entry not found");
        }
        if (checkClass != null && value != null && !checkClass.isInstance(value)) {
            throw new BadFormException(getIFDTagName(tag) + " directory entry is the wrong type (got " + value.getClass().getName() + ", expected " + checkClass.getName());
        }
        return value;
    }

    /**
   * Gets the given directory entry value in long format from the
   * specified IFD, performing some error checking.
   */
    public static long getIFDLongValue(Hashtable ifd, int tag, boolean checkNull, long defaultValue) throws BadFormException {
        long value = defaultValue;
        Number number = (Number) getIFDValue(ifd, tag, checkNull, Number.class);
        if (number != null) value = number.longValue();
        return value;
    }

    /**
   * Gets the given directory entry value in int format from the
   * specified IFD, or -1 if the given directory does not exist.
   */
    public static int getIFDIntValue(Hashtable ifd, int tag) {
        int value = -1;
        try {
            value = getIFDIntValue(ifd, tag, false, -1);
        } catch (BadFormException exc) {
        }
        return value;
    }

    /**
   * Gets the given directory entry value in int format from the
   * specified IFD, performing some error checking.
   */
    public static int getIFDIntValue(Hashtable ifd, int tag, boolean checkNull, int defaultValue) throws BadFormException {
        int value = defaultValue;
        Number number = (Number) getIFDValue(ifd, tag, checkNull, Number.class);
        if (number != null) value = number.intValue();
        return value;
    }

    /**
   * Gets the given directory entry value in rational format from the
   * specified IFD, performing some error checking.
   */
    public static TiffRational getIFDRationalValue(Hashtable ifd, int tag, boolean checkNull) throws BadFormException {
        return (TiffRational) getIFDValue(ifd, tag, checkNull, TiffRational.class);
    }

    /**
   * Gets the given directory entry values in long format
   * from the specified IFD, performing some error checking.
   */
    public static long[] getIFDLongArray(Hashtable ifd, int tag, boolean checkNull) throws BadFormException {
        Object value = getIFDValue(ifd, tag, checkNull, null);
        long[] results = null;
        if (value instanceof long[]) results = (long[]) value; else if (value instanceof Number) {
            results = new long[] { ((Number) value).longValue() };
        } else if (value instanceof Number[]) {
            Number[] numbers = (Number[]) value;
            results = new long[numbers.length];
            for (int i = 0; i < results.length; i++) results[i] = numbers[i].longValue();
        } else if (value instanceof int[]) {
            int[] integers = (int[]) value;
            results = new long[integers.length];
            for (int i = 0; i < integers.length; i++) results[i] = integers[i];
        } else if (value != null) {
            throw new BadFormException(getIFDTagName(tag) + " directory entry is the wrong type (got " + value.getClass().getName() + ", expected Number, long[], Number[] or int[])");
        }
        return results;
    }

    /**
   * Gets the given directory entry values in int format
   * from the specified IFD, performing some error checking.
   */
    public static int[] getIFDIntArray(Hashtable ifd, int tag, boolean checkNull) throws BadFormException {
        Object value = getIFDValue(ifd, tag, checkNull, null);
        int[] results = null;
        if (value instanceof int[]) results = (int[]) value; else if (value instanceof Number) {
            results = new int[] { ((Number) value).intValue() };
        } else if (value instanceof Number[]) {
            Number[] numbers = (Number[]) value;
            results = new int[numbers.length];
            for (int i = 0; i < results.length; i++) results[i] = numbers[i].intValue();
        } else if (value != null) {
            throw new BadFormException(getIFDTagName(tag) + " directory entry is the wrong type (got " + value.getClass().getName() + ", expected Number, int[] or Number[])");
        }
        return results;
    }

    /**
   * Gets the given directory entry values in short format
   * from the specified IFD, performing some error checking.
   */
    public static short[] getIFDShortArray(Hashtable ifd, int tag, boolean checkNull) throws BadFormException {
        Object value = getIFDValue(ifd, tag, checkNull, null);
        short[] results = null;
        if (value instanceof short[]) results = (short[]) value; else if (value instanceof Number) {
            results = new short[] { ((Number) value).shortValue() };
        } else if (value instanceof Number[]) {
            Number[] numbers = (Number[]) value;
            results = new short[numbers.length];
            for (int i = 0; i < results.length; i++) {
                results[i] = numbers[i].shortValue();
            }
        } else if (value != null) {
            throw new BadFormException(getIFDTagName(tag) + " directory entry is the wrong type (got " + value.getClass().getName() + ", expected Number, short[] or Number[])");
        }
        return results;
    }

    /** Reads the image defined in the given IFD from the specified file. */
    public static FlatField getImage(Hashtable ifd, RandomAccessFile in) throws BadFormException, IOException {
        return getImage(ifd, in, 0);
    }

    /** Reads the image defined in the given IFD from the specified file. */
    public static FlatField getImage(Hashtable ifd, RandomAccessFile in, int globalOffset) throws BadFormException, IOException {
        if (DEBUG) debug("parsing IFD entries");
        boolean littleEndian = ((Boolean) getIFDValue(ifd, LITTLE_ENDIAN, true, Boolean.class)).booleanValue();
        long imageWidth = getIFDLongValue(ifd, IMAGE_WIDTH, true, 0);
        long imageLength = getIFDLongValue(ifd, IMAGE_LENGTH, true, 0);
        int[] bitsPerSample = getIFDIntArray(ifd, BITS_PER_SAMPLE, false);
        if (bitsPerSample == null) bitsPerSample = new int[] { 1 };
        int samplesPerPixel = getIFDIntValue(ifd, SAMPLES_PER_PIXEL, false, 1);
        int compression = getIFDIntValue(ifd, COMPRESSION, false, UNCOMPRESSED);
        int photoInterp = getIFDIntValue(ifd, PHOTOMETRIC_INTERPRETATION, true, 0);
        long[] stripOffsets = getIFDLongArray(ifd, STRIP_OFFSETS, true);
        long[] stripByteCounts = getIFDLongArray(ifd, STRIP_BYTE_COUNTS, true);
        long[] rowsPerStripArray = getIFDLongArray(ifd, ROWS_PER_STRIP, false);
        boolean fakeRPS = rowsPerStripArray == null;
        if (fakeRPS) {
            rowsPerStripArray = new long[1];
            long temp = stripByteCounts[0];
            stripByteCounts = new long[1];
            stripByteCounts[0] = temp;
            temp = bitsPerSample[0];
            bitsPerSample = new int[1];
            bitsPerSample[0] = (int) temp;
            temp = stripOffsets[0];
            stripOffsets = new long[1];
            stripOffsets[0] = temp;
            for (int i = 0; i < stripByteCounts.length; i++) {
                if (i < bitsPerSample.length) {
                    if (bitsPerSample[i] != 0) {
                        rowsPerStripArray[i] = (long) stripByteCounts[i] / (imageWidth * (bitsPerSample[i] / 8));
                    } else if (bitsPerSample[i] == 0 && i > 0) {
                        rowsPerStripArray[i] = (long) stripByteCounts[i] / (imageWidth * (bitsPerSample[i - 1] / 8));
                    } else {
                        throw new BadFormException("BitsPerSample is 0");
                    }
                } else if (i >= bitsPerSample.length) {
                    rowsPerStripArray[i] = (long) stripByteCounts[i] / (imageLength * (bitsPerSample[bitsPerSample.length - 1] / 8));
                }
            }
            samplesPerPixel = 1;
        }
        TiffRational xResolution = getIFDRationalValue(ifd, X_RESOLUTION, false);
        TiffRational yResolution = getIFDRationalValue(ifd, Y_RESOLUTION, false);
        int planarConfig = getIFDIntValue(ifd, PLANAR_CONFIGURATION, false, 1);
        int resolutionUnit = getIFDIntValue(ifd, RESOLUTION_UNIT, false, 2);
        if (xResolution == null || yResolution == null) resolutionUnit = 0;
        int[] colorMap = getIFDIntArray(ifd, COLOR_MAP, false);
        int predictor = getIFDIntValue(ifd, PREDICTOR, false, 1);
        if (DEBUG) {
            StringBuffer sb = new StringBuffer();
            sb.append("IFD directory entry values:");
            sb.append("\n\tLittleEndian=");
            sb.append(littleEndian);
            sb.append("\n\tImageWidth=");
            sb.append(imageWidth);
            sb.append("\n\tImageLength=");
            sb.append(imageLength);
            sb.append("\n\tBitsPerSample=");
            sb.append(bitsPerSample[0]);
            for (int i = 1; i < bitsPerSample.length; i++) {
                sb.append(",");
                sb.append(bitsPerSample[i]);
            }
            sb.append("\n\tSamplesPerPixel=");
            sb.append(samplesPerPixel);
            sb.append("\n\tCompression=");
            sb.append(compression);
            sb.append("\n\tPhotometricInterpretation=");
            sb.append(photoInterp);
            sb.append("\n\tStripOffsets=");
            sb.append(stripOffsets[0]);
            for (int i = 1; i < stripOffsets.length; i++) {
                sb.append(",");
                sb.append(stripOffsets[i]);
            }
            sb.append("\n\tRowsPerStrip=");
            sb.append(rowsPerStripArray[0]);
            for (int i = 1; i < rowsPerStripArray.length; i++) {
                sb.append(",");
                sb.append(rowsPerStripArray[i]);
            }
            sb.append("\n\tStripByteCounts=");
            sb.append(stripByteCounts[0]);
            for (int i = 1; i < stripByteCounts.length; i++) {
                sb.append(",");
                sb.append(stripByteCounts[i]);
            }
            sb.append("\n\tXResolution=");
            sb.append(xResolution);
            sb.append("\n\tYResolution=");
            sb.append(yResolution);
            sb.append("\n\tPlanarConfiguration=");
            sb.append(planarConfig);
            sb.append("\n\tResolutionUnit=");
            sb.append(resolutionUnit);
            sb.append("\n\tColorMap=");
            if (colorMap == null) sb.append("null"); else {
                sb.append(colorMap[0]);
                for (int i = 1; i < colorMap.length; i++) {
                    sb.append(",");
                    sb.append(colorMap[i]);
                }
            }
            sb.append("\n\tPredictor=");
            sb.append(predictor);
            debug(sb.toString());
        }
        for (int i = 0; i < bitsPerSample.length; i++) {
            if (bitsPerSample[i] < 1) {
                throw new BadFormException("Illegal BitsPerSample (" + bitsPerSample[i] + ")");
            } else if (bitsPerSample[i] > 8 && bitsPerSample[i] % 8 != 0) {
                throw new BadFormException("Sorry, unsupported BitsPerSample (" + bitsPerSample[i] + ")");
            }
        }
        if (bitsPerSample.length != samplesPerPixel) {
            throw new BadFormException("BitsPerSample length (" + bitsPerSample.length + ") does not match SamplesPerPixel (" + samplesPerPixel + ")");
        }
        if (photoInterp == RGB_PALETTE) {
            throw new BadFormException("Sorry, Palette color PhotometricInterpretation is not supported");
        } else if (photoInterp == TRANSPARENCY_MASK) {
            throw new BadFormException("Sorry, Transparency Mask PhotometricInterpretation is not supported");
        } else if (photoInterp == CMYK) {
            throw new BadFormException("Sorry, CMYK PhotometricInterpretation is not supported");
        } else if (photoInterp == Y_CB_CR) {
            throw new BadFormException("Sorry, YCbCr PhotometricInterpretation is not supported");
        } else if (photoInterp == CIE_LAB) {
            throw new BadFormException("Sorry, CIELAB PhotometricInterpretation is not supported");
        } else if (photoInterp != WHITE_IS_ZERO && photoInterp != BLACK_IS_ZERO && photoInterp != RGB) {
            throw new BadFormException("Unknown PhotometricInterpretation (" + photoInterp + ")");
        }
        long rowsPerStrip = rowsPerStripArray[0];
        for (int i = 1; i < rowsPerStripArray.length; i++) {
            if (rowsPerStrip != rowsPerStripArray[i]) {
                throw new BadFormException("Sorry, non-uniform RowsPerStrip is not supported");
            }
        }
        long numStrips = (imageLength + rowsPerStrip - 1) / rowsPerStrip;
        if (planarConfig == 2) numStrips *= samplesPerPixel;
        if (stripOffsets.length != numStrips) {
            throw new BadFormException("StripOffsets length (" + stripOffsets.length + ") does not match expected " + "number of strips (" + numStrips + ")");
        }
        if (stripByteCounts.length != numStrips) {
            throw new BadFormException("StripByteCounts length (" + stripByteCounts.length + ") does not match expected " + "number of strips (" + numStrips + ")");
        }
        if (imageWidth > Integer.MAX_VALUE || imageLength > Integer.MAX_VALUE || imageWidth * imageLength > Integer.MAX_VALUE) {
            throw new BadFormException("Sorry, ImageWidth x ImageLength > " + Integer.MAX_VALUE + " is not supported (" + imageWidth + " x " + imageLength + ")");
        }
        int numSamples = (int) (imageWidth * imageLength);
        if (planarConfig != 1 && planarConfig != 2) {
            throw new BadFormException("Unknown PlanarConfiguration (" + planarConfig + ")");
        }
        if (DEBUG) {
            debug("reading image data (samplesPerPixel=" + samplesPerPixel + "; numSamples=" + numSamples + ")");
        }
        float[][] samples = new float[samplesPerPixel][numSamples];
        for (int strip = 0, row = 0; strip < numStrips; strip++, row += rowsPerStrip) {
            if (DEBUG) debug("reading image strip #" + strip);
            long actualRows = (row + rowsPerStrip > imageLength) ? imageLength - row : rowsPerStrip;
            in.seek(globalOffset + stripOffsets[strip]);
            if (stripByteCounts[strip] > Integer.MAX_VALUE) {
                throw new BadFormException("Sorry, StripByteCounts > " + Integer.MAX_VALUE + " is not supported");
            }
            byte[] bytes = new byte[(int) stripByteCounts[strip]];
            in.read(bytes);
            bytes = uncompress(bytes, compression);
            undifference(bytes, bitsPerSample, imageWidth, planarConfig, predictor);
            unpackBytes(samples, (int) (imageWidth * row), bytes, bitsPerSample, photoInterp, colorMap, littleEndian);
        }
        if (DEBUG) debug("constructing field");
        RealType x = RealType.getRealType("ImageElement");
        RealType y = RealType.getRealType("ImageLine");
        RealType[] v = new RealType[samplesPerPixel];
        for (int i = 0; i < samplesPerPixel; i++) {
            v[i] = RealType.getRealType("value" + i);
        }
        FlatField field = null;
        try {
            RealTupleType domain = new RealTupleType(x, y);
            RealTupleType range = new RealTupleType(v);
            FunctionType fieldType = new FunctionType(domain, range);
            Linear2DSet fieldSet = new Linear2DSet(domain, 0.0, imageWidth - 1.0, (int) imageWidth, imageLength - 1.0, 0.0, (int) imageLength);
            field = new FlatField(fieldType, fieldSet);
            field.setSamples(samples, false);
        } catch (VisADException exc) {
            exc.printStackTrace();
        }
        return field;
    }

    /**
   * Extracts pixel information from the given byte array according to the
   * bits per sample, photometric interpretation and color map IFD directory
   * entry values, and the specified byte ordering.
   * No error checking is performed.
   */
    public static void unpackBytes(float[][] samples, int startIndex, byte[] bytes, int[] bitsPerSample, int photoInterp, int[] colorMap, boolean littleEndian) throws BadFormException {
        int totalBits = 0;
        for (int i = 0; i < bitsPerSample.length; i++) totalBits += bitsPerSample[i];
        int sampleCount = 8 * bytes.length / totalBits;
        if (DEBUG) {
            debug("unpacking " + sampleCount + " samples (startIndex=" + startIndex + "; totalBits=" + totalBits + "; numBytes=" + bytes.length + ")");
        }
        if (startIndex + sampleCount > samples[0].length) {
            int trunc = startIndex + sampleCount - samples[0].length;
            if (DEBUG) debug("WARNING: truncated " + trunc + " extra samples");
            sampleCount -= trunc;
        }
        int index = 0;
        for (int j = 0; j < sampleCount; j++) {
            for (int i = 0; i < bitsPerSample.length; i++) {
                int numBytes = bitsPerSample[i] / 8;
                if (numBytes == 1) {
                    byte b = bytes[index++];
                    int ndx = startIndex + j;
                    samples[i][ndx] = b < 0 ? 256 + b : b;
                    if (photoInterp == WHITE_IS_ZERO) {
                        samples[i][ndx] = 256 - samples[i][ndx];
                    }
                } else {
                    byte[] b = new byte[numBytes];
                    System.arraycopy(bytes, index, b, 0, numBytes);
                    index += numBytes;
                    int ndx = startIndex + j;
                    samples[i][ndx] = bytesToLong(b, littleEndian);
                    if (photoInterp == WHITE_IS_ZERO) {
                        long maxValue = 1;
                        for (int q = 0; q < numBytes; q++) maxValue *= 8;
                        samples[i][ndx] = maxValue - samples[i][ndx];
                    }
                }
            }
        }
    }

    /** Decodes a strip of data compressed with the given compression scheme. */
    public static byte[] uncompress(byte[] input, int compression) throws BadFormException, IOException {
        if (compression == UNCOMPRESSED) return input; else if (compression == CCITT_1D) {
            throw new BadFormException("Sorry, CCITT Group 3 1-Dimensional Modified Huffman " + "run length encoding compression mode is not supported");
        } else if (compression == GROUP_3_FAX) {
            throw new BadFormException("Sorry, CCITT T.4 bi-level encoding " + "(Group 3 Fax) compression mode is not supported");
        } else if (compression == GROUP_4_FAX) {
            throw new BadFormException("Sorry, CCITT T.6 bi-level encoding " + "(Group 4 Fax) compression mode is not supported");
        } else if (compression == LZW) return lzwUncompress(input); else if (compression == JPEG) {
            throw new BadFormException("Sorry, JPEG compression mode is not supported");
        } else if (compression == PACK_BITS) {
            throw new BadFormException("Sorry, PackBits compression mode is not supported");
        } else {
            throw new BadFormException("Unknown Compression type (" + compression + ")");
        }
    }

    /** Undoes in-place differencing according to the given predictor value. */
    public static void undifference(byte[] input, int[] bitsPerSample, long width, int planarConfig, int predictor) throws BadFormException {
        if (predictor == 2) {
            if (DEBUG) debug("reversing horizontal differencing");
            for (int b = 0; b < input.length; b++) {
                if (b / bitsPerSample.length % width == 0) continue;
                input[b] += input[b - bitsPerSample.length];
            }
        } else if (predictor != 1) {
            throw new BadFormException("Unknown Predictor (" + predictor + ")");
        }
    }

    /**
   * Decodes an LZW-compressed image strip.
   * Adapted from the TIFF 6.0 Specification:
   * http://partners.adobe.com/asn/developer/pdfs/tn/TIFF6.pdf (page 61)
   * @author Eric Kjellman egkjellman at wisc.edu
   * @author Wayne Rasband wsr at nih.gov
   */
    public static byte[] lzwUncompress(byte[] input) {
        if (input == null || input.length == 0) return input;
        if (DEBUG) debug("decompressing " + input.length + " bytes of LZW data");
        byte[][] symbolTable = new byte[4096][1];
        int bitsToRead = 9;
        int nextSymbol = 258;
        int code;
        int oldCode = -1;
        ByteVector out = new ByteVector(8192);
        BitBuffer bb = new BitBuffer(input);
        byte[] byteBuffer1 = new byte[16];
        byte[] byteBuffer2 = new byte[16];
        while (true) {
            code = bb.getBits(bitsToRead);
            if (code == EOI_CODE || code == -1) break;
            if (code == CLEAR_CODE) {
                for (int i = 0; i < 256; i++) symbolTable[i][0] = (byte) i;
                nextSymbol = 258;
                bitsToRead = 9;
                code = bb.getBits(bitsToRead);
                if (code == EOI_CODE || code == -1) break;
                out.add(symbolTable[code]);
                oldCode = code;
            } else {
                if (code < nextSymbol) {
                    out.add(symbolTable[code]);
                    ByteVector symbol = new ByteVector(byteBuffer1);
                    symbol.add(symbolTable[oldCode]);
                    symbol.add(symbolTable[code][0]);
                    symbolTable[nextSymbol] = symbol.toByteArray();
                    oldCode = code;
                    nextSymbol++;
                } else {
                    ByteVector symbol = new ByteVector(byteBuffer2);
                    symbol.add(symbolTable[oldCode]);
                    symbol.add(symbolTable[oldCode][0]);
                    byte[] outString = symbol.toByteArray();
                    out.add(outString);
                    symbolTable[nextSymbol] = outString;
                    oldCode = code;
                    nextSymbol++;
                }
                if (nextSymbol == 511) bitsToRead = 10;
                if (nextSymbol == 1023) bitsToRead = 11;
                if (nextSymbol == 2047) bitsToRead = 12;
            }
        }
        return out.toByteArray();
    }

    /** Reads 1 signed byte [-128, 127]. */
    public static byte readSignedByte(RandomAccessFile in) throws IOException {
        byte[] b = new byte[1];
        readFully(in, b);
        return b[0];
    }

    /** Reads 1 unsigned byte [0, 255]. */
    public static short readUnsignedByte(RandomAccessFile in) throws IOException {
        short q = readSignedByte(in);
        if (q < 0) q += 256;
        return q;
    }

    /** Reads 2 signed bytes [-32768, 32767]. */
    public static short read2SignedBytes(RandomAccessFile in, boolean little) throws IOException {
        byte[] bytes = new byte[2];
        readFully(in, bytes);
        return bytesToShort(bytes, little);
    }

    /** Reads 2 unsigned bytes [0, 65535]. */
    public static int read2UnsignedBytes(RandomAccessFile in, boolean little) throws IOException {
        int q = read2SignedBytes(in, little);
        if (q < 0) q += 65536;
        return q;
    }

    /** Reads 4 signed bytes [-2147483648, 2147483647]. */
    public static int read4SignedBytes(RandomAccessFile in, boolean little) throws IOException {
        byte[] bytes = new byte[4];
        readFully(in, bytes);
        return bytesToInt(bytes, little);
    }

    /** Reads 4 unsigned bytes [0, 4294967296]. */
    public static long read4UnsignedBytes(RandomAccessFile in, boolean little) throws IOException {
        long q = read4SignedBytes(in, little);
        if (q < 0) q += 4294967296L;
        return q;
    }

    /** Reads 8 signed bytes [-9223372036854775808, 9223372036854775807]. */
    public static long read8SignedBytes(RandomAccessFile in, boolean little) throws IOException {
        byte[] bytes = new byte[8];
        readFully(in, bytes);
        return bytesToLong(bytes, little);
    }

    /** Reads 4 bytes in single precision IEEE format. */
    public static float readFloat(RandomAccessFile in, boolean little) throws IOException {
        return Float.intBitsToFloat(read4SignedBytes(in, little));
    }

    /** Reads 8 bytes in double precision IEEE format. */
    public static double readDouble(RandomAccessFile in, boolean little) throws IOException {
        return Double.longBitsToDouble(read8SignedBytes(in, little));
    }

    /**
   * Translates up to the first len bytes of a byte array beyond the given
   * offset to a short. If there are fewer than 2 bytes in the array,
   * the MSBs are all assumed to be zero (regardless of endianness).
   */
    public static short bytesToShort(byte[] bytes, int off, int len, boolean little) {
        if (bytes.length - off < len) len = bytes.length - off;
        short total = 0;
        for (int i = 0, ndx = off; i < len; i++, ndx++) {
            total |= (bytes[ndx] < 0 ? 256 + bytes[ndx] : (int) bytes[ndx]) << ((little ? i : len - i - 1) * 8);
        }
        return total;
    }

    /**
   * Translates up to the first 2 bytes of a byte array beyond the given
   * offset to a short. If there are fewer than 2 bytes in the array,
   * the MSBs are all assumed to be zero (regardless of endianness).
   */
    public static short bytesToShort(byte[] bytes, int off, boolean little) {
        return bytesToShort(bytes, off, 2, little);
    }

    /**
   * Translates up to the first 2 bytes of a byte array to a short.
   * If there are fewer than 2 bytes in the array, the MSBs are all
   * assumed to be zero (regardless of endianness).
   */
    public static short bytesToShort(byte[] bytes, boolean little) {
        return bytesToShort(bytes, 0, 2, little);
    }

    /**
   * Translates up to the first len bytes of a byte array byond the given
   * offset to a short. If there are fewer than 2 bytes in the array,
   * the MSBs are all assumed to be zero (regardless of endianness).
   */
    public static short bytesToShort(short[] bytes, int off, int len, boolean little) {
        if (bytes.length - off < len) len = bytes.length - off;
        short total = 0;
        for (int i = 0, ndx = off; i < len; i++, ndx++) {
            total |= ((int) bytes[ndx]) << ((little ? i : len - i - 1) * 8);
        }
        return total;
    }

    /**
   * Translates up to the first 2 bytes of a byte array byond the given
   * offset to a short. If there are fewer than 2 bytes in the array,
   * the MSBs are all assumed to be zero (regardless of endianness).
   */
    public static short bytesToShort(short[] bytes, int off, boolean little) {
        return bytesToShort(bytes, off, 2, little);
    }

    /**
   * Translates up to the first 2 bytes of a byte array to a short.
   * If there are fewer than 2 bytes in the array, the MSBs are all
   * assumed to be zero (regardless of endianness).
   */
    public static short bytesToShort(short[] bytes, boolean little) {
        return bytesToShort(bytes, 0, 2, little);
    }

    /**
   * Translates up to the first len bytes of a byte array beyond the given
   * offset to an int. If there are fewer than 4 bytes in the array,
   * the MSBs are all assumed to be zero (regardless of endianness).
   */
    public static int bytesToInt(byte[] bytes, int off, int len, boolean little) {
        if (bytes.length - off < len) len = bytes.length - off;
        int total = 0;
        for (int i = 0, ndx = off; i < len; i++, ndx++) {
            total |= (bytes[ndx] < 0 ? 256 + bytes[ndx] : (int) bytes[ndx]) << ((little ? i : len - i - 1) * 8);
        }
        return total;
    }

    /**
   * Translates up to the first 4 bytes of a byte array beyond the given
   * offset to an int. If there are fewer than 4 bytes in the array,
   * the MSBs are all assumed to be zero (regardless of endianness).
   */
    public static int bytesToInt(byte[] bytes, int off, boolean little) {
        return bytesToInt(bytes, off, 4, little);
    }

    /**
   * Translates up to the first 4 bytes of a byte array to an int.
   * If there are fewer than 4 bytes in the array, the MSBs are all
   * assumed to be zero (regardless of endianness).
   */
    public static int bytesToInt(byte[] bytes, boolean little) {
        return bytesToInt(bytes, 0, 4, little);
    }

    /**
   * Translates up to the first len bytes of a byte array beyond the given
   * offset to an int. If there are fewer than 4 bytes in the array,
   * the MSBs are all assumed to be zero (regardless of endianness).
   */
    public static int bytesToInt(short[] bytes, int off, int len, boolean little) {
        if (bytes.length - off < len) len = bytes.length - off;
        int total = 0;
        for (int i = 0, ndx = off; i < len; i++, ndx++) {
            total |= ((int) bytes[ndx]) << ((little ? i : len - i - 1) * 8);
        }
        return total;
    }

    /**
   * Translates up to the first 4 bytes of a byte array beyond the given
   * offset to an int. If there are fewer than 4 bytes in the array,
   * the MSBs are all assumed to be zero (regardless of endianness).
   */
    public static int bytesToInt(short[] bytes, int off, boolean little) {
        return bytesToInt(bytes, off, 4, little);
    }

    /**
   * Translates up to the first 4 bytes of a byte array to an int.
   * If there are fewer than 4 bytes in the array, the MSBs are all
   * assumed to be zero (regardless of endianness).
   */
    public static int bytesToInt(short[] bytes, boolean little) {
        return bytesToInt(bytes, 0, 4, little);
    }

    /**
   * Translates up to the first len bytes of a byte array beyond the given
   * offset to a long. If there are fewer than 8 bytes in the array,
   * the MSBs are all assumed to be zero (regardless of endianness).
   */
    public static long bytesToLong(byte[] bytes, int off, int len, boolean little) {
        if (bytes.length - off < len) len = bytes.length - off;
        long total = 0;
        for (int i = 0; i < len; i++) {
            total |= (bytes[i] < 0 ? 256L + bytes[i] : (long) bytes[i]) << ((little ? i : len - i - 1) * 8);
        }
        return total;
    }

    /**
   * Translates up to the first 8 bytes of a byte array beyond the given
   * offset to a long. If there are fewer than 8 bytes in the array,
   * the MSBs are all assumed to be zero (regardless of endianness).
   */
    public static long bytesToLong(byte[] bytes, int off, boolean little) {
        return bytesToLong(bytes, off, 8, little);
    }

    /**
   * Translates up to the first 8 bytes of a byte array to a long.
   * If there are fewer than 8 bytes in the array, the MSBs are all
   * assumed to be zero (regardless of endianness).
   */
    public static long bytesToLong(byte[] bytes, boolean little) {
        return bytesToLong(bytes, 0, 8, little);
    }

    /**
   * Translates up to the first len bytes of a byte array beyond the given
   * offset to a long. If there are fewer than 8 bytes to be translated,
   * the MSBs are all assumed to be zero (regardless of endianness).
   */
    public static long bytesToLong(short[] bytes, int off, int len, boolean little) {
        if (bytes.length - off < len) len = bytes.length - off;
        long total = 0;
        for (int i = 0, ndx = off; i < len; i++, ndx++) {
            total |= ((long) bytes[ndx]) << ((little ? i : len - i - 1) * 8);
        }
        return total;
    }

    /**
   * Translates up to the first 8 bytes of a byte array beyond the given
   * offset to a long. If there are fewer than 8 bytes to be translated,
   * the MSBs are all assumed to be zero (regardless of endianness).
   */
    public static long bytesToLong(short[] bytes, int off, boolean little) {
        return bytesToLong(bytes, off, 8, little);
    }

    /**
   * Translates up to the first 8 bytes of a byte array to a long.
   * If there are fewer than 8 bytes in the array, the MSBs are all
   * assumed to be zero (regardless of endianness).
   */
    public static long bytesToLong(short[] bytes, boolean little) {
        return bytesToLong(bytes, 0, 8, little);
    }

    /** Converts bytes from the given array into a string. */
    public static String bytesToString(short[] bytes, int off, int len) {
        if (bytes.length - off < len) len = bytes.length - off;
        for (int i = 0; i < len; i++) {
            if (bytes[off + i] == 0) {
                len = i;
                break;
            }
        }
        byte[] b = new byte[len];
        for (int i = 0; i < b.length; i++) b[i] = (byte) bytes[off + i];
        return new String(b);
    }

    /** Adds a directory entry to an IFD. */
    public static void putIFDValue(Hashtable ifd, int tag, Object value) {
        ifd.put(new Integer(tag), value);
    }

    /** Adds a directory entry of type BYTE to an IFD. */
    public static void putIFDValue(Hashtable ifd, int tag, short value) {
        putIFDValue(ifd, tag, new Short(value));
    }

    /** Adds a directory entry of type SHORT to an IFD. */
    public static void putIFDValue(Hashtable ifd, int tag, int value) {
        putIFDValue(ifd, tag, new Integer(value));
    }

    /** Adds a directory entry of type LONG to an IFD. */
    public static void putIFDValue(Hashtable ifd, int tag, long value) {
        putIFDValue(ifd, tag, new Long(value));
    }

    /**
   * Writes the given field to the specified output stream using the given
   * byte offset and IFD, in big-endian format.
   *
   * @param image The field to write
   * @param ifd Hashtable representing the TIFF IFD; can be null
   * @param out The output stream to which the TIFF data should be written
   * @param offset The value to use for specifying byte offsets
   * @param last Whether this image is the final IFD entry of the TIFF data
   * @return total number of bytes written
   */
    public static long writeImage(FlatField image, Hashtable ifd, OutputStream out, int offset, boolean last) throws IOException, VisADException {
        if (image == null) throw new BadFormException("Image is null");
        if (DEBUG) debug("writeImage (offset=" + offset + "; last=" + last + ")");
        visad.Set set = image.getDomainSet();
        if (!(set instanceof Gridded2DSet)) {
            throw new BadFormException("Image has an " + "incompatible domain set (" + set.getClass().getName() + ")");
        }
        Gridded2DSet gset = (Gridded2DSet) set;
        int width = gset.getLength(0);
        int height = gset.getLength(1);
        float[][] values = image.getFloats(false);
        if (values.length < 1 || values.length > 3) {
            throw new BadFormException("Image has an unsupported " + "number of range components (" + values.length + ")");
        }
        if (values.length == 2) {
            values = new float[][] { values[0], values[1], new float[values[0].length] };
        }
        if (ifd == null) ifd = new Hashtable();
        putIFDValue(ifd, IMAGE_WIDTH, width);
        putIFDValue(ifd, IMAGE_LENGTH, height);
        if (getIFDValue(ifd, BITS_PER_SAMPLE) == null) {
            int max = 0;
            for (int c = 0; c < values.length; c++) {
                for (int ndx = 0; ndx < values[c].length; ndx++) {
                    float v = values[c][ndx];
                    int iv = (int) v;
                    if (v < 0) {
                        throw new BadFormException("Sample #" + ndx + " of range component #" + c + " has negative value (" + v + ")");
                    } else if (v != iv) {
                        throw new BadFormException("Sample #" + ndx + " of range component #" + c + " is not an integer (" + v + ")");
                    }
                    if (iv > max) max = iv;
                }
            }
            int bps = max < 256 ? 8 : (max < 65536 ? 16 : 32);
            int[] bpsArray = new int[values.length];
            Arrays.fill(bpsArray, bps);
            putIFDValue(ifd, BITS_PER_SAMPLE, bpsArray);
        }
        if (getIFDValue(ifd, COMPRESSION) == null) {
            putIFDValue(ifd, COMPRESSION, UNCOMPRESSED);
        }
        if (getIFDValue(ifd, PHOTOMETRIC_INTERPRETATION) == null) {
            putIFDValue(ifd, PHOTOMETRIC_INTERPRETATION, values.length == 1 ? 1 : 2);
        }
        if (getIFDValue(ifd, SAMPLES_PER_PIXEL) == null) {
            putIFDValue(ifd, SAMPLES_PER_PIXEL, values.length);
        }
        if (getIFDValue(ifd, X_RESOLUTION) == null) {
            putIFDValue(ifd, X_RESOLUTION, new TiffRational(1, 1));
        }
        if (getIFDValue(ifd, Y_RESOLUTION) == null) {
            putIFDValue(ifd, Y_RESOLUTION, new TiffRational(1, 1));
        }
        if (getIFDValue(ifd, RESOLUTION_UNIT) == null) {
            putIFDValue(ifd, RESOLUTION_UNIT, 1);
        }
        if (getIFDValue(ifd, SOFTWARE) == null) {
            putIFDValue(ifd, SOFTWARE, "VisAD");
        }
        int stripSize = 8192;
        int rowsPerStrip = stripSize / width;
        int stripsPerImage = (height + rowsPerStrip - 1) / rowsPerStrip;
        int[] bps = (int[]) getIFDValue(ifd, BITS_PER_SAMPLE, true, int[].class);
        ByteArrayOutputStream[] stripBuf = new ByteArrayOutputStream[stripsPerImage];
        DataOutputStream[] stripOut = new DataOutputStream[stripsPerImage];
        for (int i = 0; i < stripsPerImage; i++) {
            stripBuf[i] = new ByteArrayOutputStream(stripSize);
            stripOut[i] = new DataOutputStream(stripBuf[i]);
        }
        for (int y = 0; y < height; y++) {
            int strip = y / rowsPerStrip;
            for (int x = 0; x < width; x++) {
                int ndx = y * width + x;
                for (int c = 0; c < values.length; c++) {
                    int q = (int) values[c][ndx];
                    if (bps[c] == 8) stripOut[strip].writeByte(q); else if (bps[c] == 16) stripOut[strip].writeShort(q); else if (bps[c] == 32) stripOut[strip].writeInt(q); else {
                        throw new BadFormException("Unsupported bits per sample value (" + bps[c] + ")");
                    }
                }
            }
        }
        int planarConfig = getIFDIntValue(ifd, PLANAR_CONFIGURATION, false, 1);
        int predictor = getIFDIntValue(ifd, PREDICTOR, false, 1);
        int compression = getIFDIntValue(ifd, COMPRESSION, false, UNCOMPRESSED);
        byte[][] strips = new byte[stripsPerImage][];
        for (int i = 0; i < stripsPerImage; i++) {
            strips[i] = stripBuf[i].toByteArray();
            difference(strips[i], bps, width, planarConfig, predictor);
            strips[i] = compress(strips[i], compression);
        }
        long[] stripByteCounts = new long[stripsPerImage];
        long[] stripOffsets = new long[stripsPerImage];
        putIFDValue(ifd, STRIP_OFFSETS, stripOffsets);
        putIFDValue(ifd, ROWS_PER_STRIP, rowsPerStrip);
        putIFDValue(ifd, STRIP_BYTE_COUNTS, stripByteCounts);
        Object[] keys = ifd.keySet().toArray();
        Arrays.sort(keys);
        int ifdBytes = 2 + 12 * keys.length + 4;
        long pixelBytes = 0;
        for (int i = 0; i < stripsPerImage; i++) {
            stripByteCounts[i] = strips[i].length;
            stripOffsets[i] = pixelBytes + offset + ifdBytes;
            pixelBytes += stripByteCounts[i];
        }
        ByteArrayOutputStream ifdBuf = new ByteArrayOutputStream(ifdBytes);
        DataOutputStream ifdOut = new DataOutputStream(ifdBuf);
        ByteArrayOutputStream extraBuf = new ByteArrayOutputStream();
        DataOutputStream extraOut = new DataOutputStream(extraBuf);
        offset += ifdBytes + pixelBytes;
        ifdOut.writeShort(keys.length);
        for (int k = 0; k < keys.length; k++) {
            Object key = keys[k];
            if (!(key instanceof Integer)) {
                throw new BadFormException("Malformed IFD tag (" + key + ")");
            }
            Object value = ifd.get(key);
            if (DEBUG) {
                String sk = getIFDTagName(((Integer) key).intValue());
                String sv = value instanceof int[] ? ("int[" + ((int[]) value).length + "]") : value.toString();
                debug("writeImage: writing " + sk + " (value=" + sv + ")");
            }
            if (value instanceof Short) {
                value = new short[] { ((Short) value).shortValue() };
            } else if (value instanceof Integer) {
                value = new int[] { ((Integer) value).intValue() };
            } else if (value instanceof Long) {
                value = new long[] { ((Long) value).longValue() };
            } else if (value instanceof TiffRational) {
                value = new TiffRational[] { (TiffRational) value };
            } else if (value instanceof Float) {
                value = new float[] { ((Float) value).floatValue() };
            } else if (value instanceof Double) {
                value = new double[] { ((Double) value).doubleValue() };
            }
            ifdOut.writeShort(((Integer) key).intValue());
            if (value instanceof short[]) {
                short[] q = (short[]) value;
                ifdOut.writeShort(BYTE);
                ifdOut.writeInt(q.length);
                if (q.length <= 4) {
                    for (int i = 0; i < q.length; i++) ifdOut.writeByte(q[i]);
                    for (int i = q.length; i < 4; i++) ifdOut.writeByte(0);
                } else {
                    ifdOut.writeInt(offset + extraBuf.size());
                    for (int i = 0; i < q.length; i++) extraOut.writeByte(q[i]);
                }
            } else if (value instanceof String) {
                char[] q = ((String) value).toCharArray();
                ifdOut.writeShort(ASCII);
                ifdOut.writeInt(q.length + 1);
                if (q.length < 4) {
                    for (int i = 0; i < q.length; i++) ifdOut.writeByte(q[i]);
                    for (int i = q.length; i < 4; i++) ifdOut.writeByte(0);
                } else {
                    ifdOut.writeInt(offset + extraBuf.size());
                    for (int i = 0; i < q.length; i++) extraOut.writeByte(q[i]);
                    extraOut.writeByte(0);
                }
            } else if (value instanceof int[]) {
                int[] q = (int[]) value;
                ifdOut.writeShort(SHORT);
                ifdOut.writeInt(q.length);
                if (q.length <= 2) {
                    for (int i = 0; i < q.length; i++) ifdOut.writeShort(q[i]);
                    for (int i = q.length; i < 2; i++) ifdOut.writeShort(0);
                } else {
                    ifdOut.writeInt(offset + extraBuf.size());
                    for (int i = 0; i < q.length; i++) extraOut.writeShort(q[i]);
                }
            } else if (value instanceof long[]) {
                long[] q = (long[]) value;
                ifdOut.writeShort(LONG);
                ifdOut.writeInt(q.length);
                if (q.length <= 1) {
                    if (q.length == 1) ifdOut.writeInt((int) q[0]); else ifdOut.writeInt(0);
                } else {
                    ifdOut.writeInt(offset + extraBuf.size());
                    for (int i = 0; i < q.length; i++) {
                        extraOut.writeInt((int) q[i]);
                    }
                }
            } else if (value instanceof TiffRational[]) {
                TiffRational[] q = (TiffRational[]) value;
                ifdOut.writeShort(RATIONAL);
                ifdOut.writeInt(q.length);
                ifdOut.writeInt(offset + extraBuf.size());
                for (int i = 0; i < q.length; i++) {
                    extraOut.writeInt((int) q[i].getNumerator());
                    extraOut.writeInt((int) q[i].getDenominator());
                }
            } else if (value instanceof float[]) {
                float[] q = (float[]) value;
                ifdOut.writeShort(FLOAT);
                ifdOut.writeInt(q.length);
                if (q.length <= 1) {
                    if (q.length == 1) ifdOut.writeFloat(q[0]); else ifdOut.writeInt(0);
                } else {
                    ifdOut.writeInt(offset + extraBuf.size());
                    for (int i = 0; i < q.length; i++) extraOut.writeFloat(q[i]);
                }
            } else if (value instanceof double[]) {
                double[] q = (double[]) value;
                ifdOut.writeShort(DOUBLE);
                ifdOut.writeInt(q.length);
                ifdOut.writeInt(offset + extraBuf.size());
                for (int i = 0; i < q.length; i++) extraOut.writeDouble(q[i]);
            } else {
                throw new BadFormException("Unknown IFD value type (" + value.getClass().getName() + ")");
            }
        }
        ifdOut.writeInt(last ? 0 : offset + extraBuf.size());
        byte[] ifdArray = ifdBuf.toByteArray();
        byte[] extraArray = extraBuf.toByteArray();
        long numBytes = ifdArray.length + extraArray.length;
        out.write(ifdArray);
        for (int i = 0; i < strips.length; i++) {
            out.write(strips[i]);
            numBytes += strips[i].length;
        }
        out.write(extraArray);
        return numBytes;
    }

    /** Encodes a strip of data with the given compression scheme. */
    public static byte[] compress(byte[] input, int compression) throws BadFormException, IOException {
        if (compression == UNCOMPRESSED) return input; else if (compression == CCITT_1D) {
            throw new BadFormException("Sorry, CCITT Group 3 1-Dimensional Modified Huffman " + "run length encoding compression mode is not supported");
        } else if (compression == GROUP_3_FAX) {
            throw new BadFormException("Sorry, CCITT T.4 bi-level encoding " + "(Group 3 Fax) compression mode is not supported");
        } else if (compression == GROUP_4_FAX) {
            throw new BadFormException("Sorry, CCITT T.6 bi-level encoding " + "(Group 4 Fax) compression mode is not supported");
        } else if (compression == LZW) return lzwCompress(input); else if (compression == JPEG) {
            throw new BadFormException("Sorry, JPEG compression mode is not supported");
        } else if (compression == PACK_BITS) {
            throw new BadFormException("Sorry, PackBits compression mode is not supported");
        } else {
            throw new BadFormException("Unknown Compression type (" + compression + ")");
        }
    }

    /** Performs in-place differencing according to the given predictor value. */
    public static void difference(byte[] input, int[] bitsPerSample, long width, int planarConfig, int predictor) throws BadFormException {
        if (predictor == 2) {
            if (DEBUG) debug("performing horizontal differencing");
            for (int b = input.length - 1; b >= 0; b--) {
                if (b / bitsPerSample.length % width == 0) continue;
                input[b] -= input[b - bitsPerSample.length];
            }
        } else if (predictor != 1) {
            throw new BadFormException("Unknown Predictor (" + predictor + ")");
        }
    }

    /**
   * Encodes an image strip using the LZW compression method.
   * Adapted from the TIFF 6.0 Specification:
   * http://partners.adobe.com/asn/developer/pdfs/tn/TIFF6.pdf (page 61)
   */
    public static byte[] lzwCompress(byte[] input) {
        if (input == null || input.length == 0) return input;
        if (DEBUG) debug("compressing " + input.length + " bytes of data to LZW");
        LZWTreeNode symbols = new LZWTreeNode(-1);
        symbols.initialize();
        int nextCode = 258;
        int numBits = 9;
        BitWriter out = new BitWriter();
        out.write(CLEAR_CODE, numBits);
        ByteVector omega = new ByteVector();
        for (int i = 0; i < input.length; i++) {
            byte k = input[i];
            LZWTreeNode omegaNode = symbols.nodeFromString(omega);
            LZWTreeNode omegaKNode = omegaNode.getChild(k);
            if (omegaKNode != null) {
                omega.add(k);
            } else {
                out.write(omegaNode.getCode(), numBits);
                omega.add(k);
                symbols.addTableEntry(omega, nextCode++);
                omega.clear();
                omega.add(k);
                if (nextCode == 512) numBits = 10; else if (nextCode == 1024) numBits = 11; else if (nextCode == 2048) numBits = 12; else if (nextCode == 4096) {
                    out.write(CLEAR_CODE, numBits);
                    symbols.initialize();
                    nextCode = 258;
                    numBits = 9;
                }
            }
        }
        out.write(symbols.codeFromString(omega), numBits);
        out.write(EOI_CODE, numBits);
        return out.toByteArray();
    }

    /** Prints a debugging message with current time. */
    public static void debug(String message) {
        System.out.println(System.currentTimeMillis() + ": " + message);
    }

    /** Reads bytes from the given random access file or array. */
    private static void readFully(RandomAccessFile in, byte[] bytes) throws IOException {
        if (in instanceof RandomAccessArray) {
            ((RandomAccessArray) in).copyArray(bytes);
        } else in.readFully(bytes);
    }
}
