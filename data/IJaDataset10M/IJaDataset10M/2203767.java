package org.apache.sanselan.formats.tiff.constants;

import org.apache.sanselan.SanselanConstants;
import org.apache.sanselan.common.BinaryConstants;

public interface TiffConstants extends SanselanConstants, TiffFieldTypeConstants, TiffDirectoryConstants, AllTagConstants, BinaryConstants {

    public static final int DEFAULT_TIFF_BYTE_ORDER = BYTE_ORDER_INTEL;

    public static final int TIFF_HEADER_SIZE = 8;

    public static final int TIFF_DIRECTORY_HEADER_LENGTH = 2;

    public static final int TIFF_DIRECTORY_FOOTER_LENGTH = 4;

    public static final int TIFF_ENTRY_LENGTH = 12;

    public static final int TIFF_ENTRY_MAX_VALUE_LENGTH = 4;

    public static final int TIFF_COMPRESSION_UNCOMPRESSED_1 = 1;

    public static final int TIFF_COMPRESSION_UNCOMPRESSED = TIFF_COMPRESSION_UNCOMPRESSED_1;

    public static final int TIFF_COMPRESSION_CCITT_1D = 2;

    public static final int TIFF_COMPRESSION_CCITT_GROUP_3 = 3;

    public static final int TIFF_COMPRESSION_CCITT_GROUP_4 = 4;

    public static final int TIFF_COMPRESSION_LZW = 5;

    public static final int TIFF_COMPRESSION_JPEG = 6;

    public static final int TIFF_COMPRESSION_UNCOMPRESSED_2 = 32771;

    public static final int TIFF_COMPRESSION_PACKBITS = 32773;
}
