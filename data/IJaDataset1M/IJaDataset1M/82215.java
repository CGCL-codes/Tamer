package com.drewChanged.metadata.exif;

import com.drewChanged.imaging.jpeg.JpegProcessingException;
import com.drewChanged.imaging.jpeg.JpegSegmentData;
import com.drewChanged.imaging.jpeg.JpegSegmentReader;
import com.drewChanged.lang.Rational;
import com.drewChanged.metadata.Directory;
import com.drewChanged.metadata.Metadata;
import com.drewChanged.metadata.MetadataReader;
import java.io.InputStream;
import java.util.Hashtable;

/**
 * Extracts Exif data from a JPEG header segment, providing information about the
 * camera/scanner/capture device (if available).  Information is encapsulated in
 * an <code>Metadata</code> object.
 * @author  Drew Noakes http://drewnoakes.com
 */
public class ExifReader implements MetadataReader {

    /**
     * The JPEG segment as an array of bytes.
     */
    private final byte[] _data;

    /**
     * Represents the native byte ordering used in the JPEG segment.  If true,
     * then we're using Motorolla ordering (Big endian), else we're using Intel
     * ordering (Little endian).
     */
    private boolean _isMotorollaByteOrder;

    /**
     * Bean instance to store information about the image and camera/scanner/capture
     * device.
     */
    private Metadata _metadata;

    /**
     * The number of bytes used per format descriptor.
     */
    private static final int[] BYTES_PER_FORMAT = { 0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8 };

    /**
     * The number of formats known.
     */
    private static final int MAX_FORMAT_CODE = 12;

    private static final int FMT_BYTE = 1;

    private static final int FMT_STRING = 2;

    private static final int FMT_USHORT = 3;

    private static final int FMT_ULONG = 4;

    private static final int FMT_URATIONAL = 5;

    private static final int FMT_SBYTE = 6;

    private static final int FMT_UNDEFINED = 7;

    private static final int FMT_SSHORT = 8;

    private static final int FMT_SLONG = 9;

    private static final int FMT_SRATIONAL = 10;

    private static final int FMT_SINGLE = 11;

    private static final int FMT_DOUBLE = 12;

    public static final int TAG_EXIF_OFFSET = 0x8769;

    public static final int TAG_INTEROP_OFFSET = 0xA005;

    public static final int TAG_GPS_INFO_OFFSET = 0x8825;

    public static final int TAG_MAKER_NOTE = 0x927C;

    public static final int TIFF_HEADER_START_OFFSET = 6;

    /**
     * Creates an ExifReader for a JpegSegmentData object.
     * @param segmentData
     */
    public ExifReader(JpegSegmentData segmentData) {
        this(segmentData.getSegment(JpegSegmentReader.SEGMENT_APP1));
    }

    /**
     * Creates an ExifReader for a Jpeg stream.
     * @param is JPEG stream. Stream will be closed.
     */
    public ExifReader(InputStream is) throws JpegProcessingException {
        this(new JpegSegmentReader(is).readSegment(JpegSegmentReader.SEGMENT_APP1));
    }

    /**
     * Creates an ExifReader for the given JPEG header segment.
     */
    public ExifReader(byte[] data) {
        _data = data;
    }

    /**
     * Performs the Exif data extraction, returning a new instance of <code>Metadata</code>.
     */
    public Metadata extract() {
        return extract(new Metadata());
    }

    /**
     * Performs the Exif data extraction, adding found values to the specified
     * instance of <code>Metadata</code>.
     */
    public Metadata extract(Metadata metadata) {
        _metadata = metadata;
        if (_data == null) System.out.println("Bloody _data is null!");
        if (_data == null) return _metadata;
        ExifDirectory directory = (ExifDirectory) _metadata.getDirectory(ExifDirectory.class);
        if (_data.length <= 14) {
            System.out.println("Exif data segment must contain at least 14 bytes");
            return _metadata;
        }
        if (!"Exif\0\0".equals(new String(_data, 0, 6))) {
            System.out.println("Exif data segment doesn't begin with 'Exif'");
            return _metadata;
        }
        String byteOrderIdentifier = new String(_data, 6, 2);
        if (!setByteOrder(byteOrderIdentifier)) {
            System.out.println("Exif data segment doesn't begin with 'Exif'");
            return _metadata;
        }
        if (get16Bits(8) != 0x2a) {
            System.out.println("Invalid Exif start - should have 0x2A at offset 8 in Exif header");
            return _metadata;
        }
        int firstDirectoryOffset = get32Bits(10) + TIFF_HEADER_START_OFFSET;
        System.out.println("PP 1");
        if (firstDirectoryOffset >= _data.length - 1) {
            System.out.println("First exif directory offset is beyond end of Exif data segment");
            firstDirectoryOffset = 14;
        }
        System.out.println("PP 2");
        Hashtable processedDirectoryOffsets = new Hashtable();
        processDirectory(directory, processedDirectoryOffsets, firstDirectoryOffset, TIFF_HEADER_START_OFFSET);
        System.out.println("PP 3");
        storeThumbnailBytes(directory, TIFF_HEADER_START_OFFSET);
        System.out.println("PP 4");
        return _metadata;
    }

    private void storeThumbnailBytes(ExifDirectory exifDirectory, int tiffHeaderOffset) {
        if (!exifDirectory.containsTag(ExifDirectory.TAG_COMPRESSION)) return;
        if (!exifDirectory.containsTag(ExifDirectory.TAG_THUMBNAIL_LENGTH) || !exifDirectory.containsTag(ExifDirectory.TAG_THUMBNAIL_OFFSET)) return;
        try {
            int offset = exifDirectory.getInt(ExifDirectory.TAG_THUMBNAIL_OFFSET);
            int length = exifDirectory.getInt(ExifDirectory.TAG_THUMBNAIL_LENGTH);
            byte[] result = new byte[length];
            for (int i = 0; i < result.length; i++) {
                result[i] = _data[tiffHeaderOffset + offset + i];
            }
            exifDirectory.setByteArray(ExifDirectory.TAG_THUMBNAIL_DATA, result);
        } catch (Throwable e) {
            System.out.println("Unable to extract thumbnail: " + e.getMessage());
        }
    }

    private boolean setByteOrder(String byteOrderIdentifier) {
        if ("MM".equals(byteOrderIdentifier)) {
            _isMotorollaByteOrder = true;
        } else if ("II".equals(byteOrderIdentifier)) {
            _isMotorollaByteOrder = false;
        } else {
            return false;
        }
        return true;
    }

    /**
     * Process one of the nested Tiff IFD directories.
     * 2 bytes: number of tags
     * for each tag
     *   2 bytes: tag type
     *   2 bytes: format code
     *   4 bytes: component count
     */
    private void processDirectory(Directory directory, Hashtable processedDirectoryOffsets, int dirStartOffset, int tiffHeaderOffset) {
        if (processedDirectoryOffsets.containsKey(new Integer(dirStartOffset))) return;
        processedDirectoryOffsets.put(new Integer(dirStartOffset), "processed");
        if (dirStartOffset >= _data.length || dirStartOffset < 0) {
            System.out.println("Ignored directory marked to start outside data segement");
            return;
        }
        if (!isDirectoryLengthValid(dirStartOffset, tiffHeaderOffset)) {
            System.out.println("Illegally sized directory");
            return;
        }
        int dirTagCount = get16Bits(dirStartOffset);
        for (int tagNumber = 0; tagNumber < dirTagCount; tagNumber++) {
            final int tagOffset = calculateTagOffset(dirStartOffset, tagNumber);
            final int tagType = get16Bits(tagOffset);
            final int formatCode = get16Bits(tagOffset + 2);
            if (formatCode < 1 || formatCode > MAX_FORMAT_CODE) {
                System.out.println("Invalid format code: " + formatCode);
                continue;
            }
            final int componentCount = get32Bits(tagOffset + 4);
            if (componentCount < 0) {
                System.out.println("Negative component count in EXIF");
                continue;
            }
            final int byteCount = componentCount * BYTES_PER_FORMAT[formatCode];
            final int tagValueOffset = calculateTagValueOffset(byteCount, tagOffset, tiffHeaderOffset);
            if (tagValueOffset < 0 || tagValueOffset > _data.length) {
                System.out.println("Illegal pointer offset value in EXIF");
                continue;
            }
            if (byteCount < 0 || tagValueOffset + byteCount > _data.length) {
                System.out.println("Illegal number of bytes: " + byteCount);
                continue;
            }
            final int subdirOffset = tiffHeaderOffset + get32Bits(tagValueOffset);
            switch(tagType) {
                case TAG_EXIF_OFFSET:
                    processDirectory(_metadata.getDirectory(ExifDirectory.class), processedDirectoryOffsets, subdirOffset, tiffHeaderOffset);
                    continue;
                case TAG_INTEROP_OFFSET:
                    processDirectory(_metadata.getDirectory(ExifInteropDirectory.class), processedDirectoryOffsets, subdirOffset, tiffHeaderOffset);
                    continue;
                case TAG_GPS_INFO_OFFSET:
                    processDirectory(_metadata.getDirectory(GpsDirectory.class), processedDirectoryOffsets, subdirOffset, tiffHeaderOffset);
                    continue;
                case TAG_MAKER_NOTE:
                    processMakerNote(tagValueOffset, processedDirectoryOffsets, tiffHeaderOffset);
                    continue;
                default:
                    processTag(directory, tagType, tagValueOffset, componentCount, formatCode);
                    break;
            }
        }
        final int finalTagOffset = calculateTagOffset(dirStartOffset, dirTagCount);
        int nextDirectoryOffset = get32Bits(finalTagOffset);
        if (nextDirectoryOffset != 0) {
            nextDirectoryOffset += tiffHeaderOffset;
            if (nextDirectoryOffset >= _data.length) {
                return;
            } else if (nextDirectoryOffset < dirStartOffset) {
                return;
            }
            processDirectory(directory, processedDirectoryOffsets, nextDirectoryOffset, tiffHeaderOffset);
        }
    }

    private void processMakerNote(int subdirOffset, Hashtable processedDirectoryOffsets, int tiffHeaderOffset) {
        Directory exifDirectory = _metadata.getDirectory(ExifDirectory.class);
        if (exifDirectory == null) return;
        String cameraModel = exifDirectory.getString(ExifDirectory.TAG_MAKE);
        final String firstTwoChars = new String(_data, subdirOffset, 2);
        final String firstThreeChars = new String(_data, subdirOffset, 3);
        final String firstFourChars = new String(_data, subdirOffset, 4);
        final String firstFiveChars = new String(_data, subdirOffset, 5);
        final String firstSixChars = new String(_data, subdirOffset, 6);
        final String firstSevenChars = new String(_data, subdirOffset, 7);
        final String firstEightChars = new String(_data, subdirOffset, 8);
        if ("OLYMP".equals(firstFiveChars) || "EPSON".equals(firstFiveChars) || "AGFA".equals(firstFourChars)) {
            processDirectory(_metadata.getDirectory(OlympusMakernoteDirectory.class), processedDirectoryOffsets, subdirOffset + 8, tiffHeaderOffset);
        } else if (cameraModel != null && cameraModel.trim().toUpperCase().startsWith("NIKON")) {
            if ("Nikon".equals(firstFiveChars)) {
                if (_data[subdirOffset + 6] == 1) processDirectory(_metadata.getDirectory(NikonType1MakernoteDirectory.class), processedDirectoryOffsets, subdirOffset + 8, tiffHeaderOffset); else if (_data[subdirOffset + 6] == 2) processDirectory(_metadata.getDirectory(NikonType2MakernoteDirectory.class), processedDirectoryOffsets, subdirOffset + 18, subdirOffset + 10); else {
                    System.out.println("Unsupported makernote data ignored.");
                }
            } else {
                processDirectory(_metadata.getDirectory(NikonType2MakernoteDirectory.class), processedDirectoryOffsets, subdirOffset, tiffHeaderOffset);
            }
        } else if ("SONY CAM".equals(firstEightChars) || "SONY DSC".equals(firstEightChars)) {
            processDirectory(_metadata.getDirectory(SonyMakernoteDirectory.class), processedDirectoryOffsets, subdirOffset + 12, tiffHeaderOffset);
        } else if ("KDK".equals(firstThreeChars)) {
            processDirectory(_metadata.getDirectory(KodakMakernoteDirectory.class), processedDirectoryOffsets, subdirOffset + 20, tiffHeaderOffset);
        } else if ("Canon".equalsIgnoreCase(cameraModel)) {
            processDirectory(_metadata.getDirectory(CanonMakernoteDirectory.class), processedDirectoryOffsets, subdirOffset, tiffHeaderOffset);
        } else if (cameraModel != null && cameraModel.toUpperCase().startsWith("CASIO")) {
            if ("QVC   ".equals(firstSixChars)) processDirectory(_metadata.getDirectory(CasioType2MakernoteDirectory.class), processedDirectoryOffsets, subdirOffset + 6, tiffHeaderOffset); else processDirectory(_metadata.getDirectory(CasioType1MakernoteDirectory.class), processedDirectoryOffsets, subdirOffset, tiffHeaderOffset);
        } else if ("FUJIFILM".equals(firstEightChars) || "Fujifilm".equalsIgnoreCase(cameraModel)) {
            boolean byteOrderBefore = _isMotorollaByteOrder;
            _isMotorollaByteOrder = false;
            int ifdStart = subdirOffset + get32Bits(subdirOffset + 8);
            processDirectory(_metadata.getDirectory(FujifilmMakernoteDirectory.class), processedDirectoryOffsets, ifdStart, tiffHeaderOffset);
            _isMotorollaByteOrder = byteOrderBefore;
        } else if (cameraModel != null && cameraModel.toUpperCase().startsWith("MINOLTA")) {
            processDirectory(_metadata.getDirectory(OlympusMakernoteDirectory.class), processedDirectoryOffsets, subdirOffset, tiffHeaderOffset);
        } else if ("KC".equals(firstTwoChars) || "MINOL".equals(firstFiveChars) || "MLY".equals(firstThreeChars) || "+M+M+M+M".equals(firstEightChars)) {
            System.out.println("Unsupported Konica/Minolta data ignored.");
        } else if ("KYOCERA".equals(firstSevenChars)) {
            processDirectory(_metadata.getDirectory(KyoceraMakernoteDirectory.class), processedDirectoryOffsets, subdirOffset + 22, tiffHeaderOffset);
        } else if ("Panasonic   ".equals(new String(_data, subdirOffset, 12))) {
            processDirectory(_metadata.getDirectory(PanasonicMakernoteDirectory.class), processedDirectoryOffsets, subdirOffset + 12, tiffHeaderOffset);
        } else if ("AOC ".equals(firstFourChars)) {
            processDirectory(_metadata.getDirectory(CasioType2MakernoteDirectory.class), processedDirectoryOffsets, subdirOffset + 6, subdirOffset);
        } else if (cameraModel != null && (cameraModel.toUpperCase().startsWith("PENTAX") || cameraModel.toUpperCase().startsWith("ASAHI"))) {
            processDirectory(_metadata.getDirectory(PentaxMakernoteDirectory.class), processedDirectoryOffsets, subdirOffset, subdirOffset);
        } else {
            System.out.println("Unsupported makernote data ignored.");
        }
    }

    private boolean isDirectoryLengthValid(int dirStartOffset, int tiffHeaderOffset) {
        int dirTagCount = get16Bits(dirStartOffset);
        int dirLength = (2 + (12 * dirTagCount) + 4);
        if (dirLength + dirStartOffset + tiffHeaderOffset >= _data.length) {
            return false;
        }
        return true;
    }

    private void processTag(Directory directory, int tagType, int tagValueOffset, int componentCount, int formatCode) {
        System.out.println("Inside processTag");
        switch(formatCode) {
            case FMT_UNDEFINED:
                final byte[] tagBytes = new byte[componentCount];
                final int byteCount = componentCount * BYTES_PER_FORMAT[formatCode];
                for (int i = 0; i < byteCount; i++) tagBytes[i] = _data[tagValueOffset + i];
                directory.setByteArray(tagType, tagBytes);
                break;
            case FMT_STRING:
                directory.setString(tagType, readString(tagValueOffset, componentCount));
                break;
            case FMT_SRATIONAL:
            case FMT_URATIONAL:
                if (componentCount == 1) {
                    Rational rational = new Rational(get32Bits(tagValueOffset), get32Bits(tagValueOffset + 4));
                    directory.setRational(tagType, rational);
                } else {
                    Rational[] rationals = new Rational[componentCount];
                    for (int i = 0; i < componentCount; i++) rationals[i] = new Rational(get32Bits(tagValueOffset + (8 * i)), get32Bits(tagValueOffset + 4 + (8 * i)));
                    directory.setRationalArray(tagType, rationals);
                }
                break;
            case FMT_SBYTE:
            case FMT_BYTE:
                if (componentCount == 1) {
                    int b = _data[tagValueOffset];
                    directory.setInt(tagType, b);
                } else {
                    int[] bytes = new int[componentCount];
                    for (int i = 0; i < componentCount; i++) bytes[i] = _data[tagValueOffset + i];
                    directory.setIntArray(tagType, bytes);
                }
                break;
            case FMT_SINGLE:
            case FMT_DOUBLE:
                if (componentCount == 1) {
                    int i = _data[tagValueOffset];
                    directory.setInt(tagType, i);
                } else {
                    int[] ints = new int[componentCount];
                    for (int i = 0; i < componentCount; i++) ints[i] = _data[tagValueOffset + i];
                    directory.setIntArray(tagType, ints);
                }
                break;
            case FMT_USHORT:
            case FMT_SSHORT:
                if (componentCount == 1) {
                    int i = get16Bits(tagValueOffset);
                    directory.setInt(tagType, i);
                } else {
                    int[] ints = new int[componentCount];
                    for (int i = 0; i < componentCount; i++) ints[i] = get16Bits(tagValueOffset + (i * 2));
                    directory.setIntArray(tagType, ints);
                }
                break;
            case FMT_SLONG:
            case FMT_ULONG:
                if (componentCount == 1) {
                    int i = get32Bits(tagValueOffset);
                    directory.setInt(tagType, i);
                } else {
                    int[] ints = new int[componentCount];
                    for (int i = 0; i < componentCount; i++) ints[i] = get32Bits(tagValueOffset + (i * 4));
                    directory.setIntArray(tagType, ints);
                }
                break;
            default:
                System.out.println("Unknown format code " + formatCode + " for tag " + tagType);
        }
    }

    private int calculateTagValueOffset(int byteCount, int dirEntryOffset, int tiffHeaderOffset) {
        if (byteCount > 4) {
            final int offsetVal = get32Bits(dirEntryOffset + 8);
            if (offsetVal + byteCount > _data.length) {
                return -1;
            }
            return tiffHeaderOffset + offsetVal;
        } else {
            return dirEntryOffset + 8;
        }
    }

    /**
     * Creates a String from the _data buffer starting at the specified offset,
     * and ending where byte=='\0' or where length==maxLength.
     */
    private String readString(int offset, int maxLength) {
        int length = 0;
        while ((offset + length) < _data.length && _data[offset + length] != '\0' && length < maxLength) length++;
        return new String(_data, offset, length);
    }

    /**
     * Determine the offset at which a given InteropArray entry begins within the specified IFD.
     * @param dirStartOffset the offset at which the IFD starts
     * @param entryNumber the zero-based entry number
     */
    private int calculateTagOffset(int dirStartOffset, int entryNumber) {
        return dirStartOffset + 2 + (12 * entryNumber);
    }

    /**
     * Get a 16 bit value from file's native byte order.  Between 0x0000 and 0xFFFF.
     */
    private int get16Bits(int offset) {
        if (offset < 0 || offset + 2 > _data.length) throw new ArrayIndexOutOfBoundsException("attempt to read data outside of exif segment (index " + offset + " where max index is " + (_data.length - 1) + ")");
        if (_isMotorollaByteOrder) {
            return (_data[offset] << 8 & 0xFF00) | (_data[offset + 1] & 0xFF);
        } else {
            return (_data[offset + 1] << 8 & 0xFF00) | (_data[offset] & 0xFF);
        }
    }

    /**
     * Get a 32 bit value from file's native byte order.
     */
    private int get32Bits(int offset) {
        if (offset < 0 || offset + 4 > _data.length) throw new ArrayIndexOutOfBoundsException("attempt to read data outside of exif segment (index " + offset + " where max index is " + (_data.length - 1) + ")");
        if (_isMotorollaByteOrder) {
            return (_data[offset] << 24 & 0xFF000000) | (_data[offset + 1] << 16 & 0xFF0000) | (_data[offset + 2] << 8 & 0xFF00) | (_data[offset + 3] & 0xFF);
        } else {
            return (_data[offset + 3] << 24 & 0xFF000000) | (_data[offset + 2] << 16 & 0xFF0000) | (_data[offset + 1] << 8 & 0xFF00) | (_data[offset] & 0xFF);
        }
    }
}
