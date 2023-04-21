package com.apelon.dts.db.admin;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.zip.CRC32;

/**
 * Utility class for generating checksum values for a given file. Current implementation
 * uses CRC32 to get the checksum.
 *
 * @since DTS 3.4.0
 * Copyright (c) 2006 Apelon, Inc. All rights reserved.
 */
public class ChecksumUtil {

    private ChecksumUtil() {
    }

    /**
   * Returns a checksum key for the table file.
   * Extracts the table name from the absolute file path provided.
   * @param tableNameFile   Absolute path for the table file.
   * @return  a checksum key. For example, the checksum key
   * for tableNameFile D:\...\DTS_NAMESPACE.FULL would be CRC32(DTS_NAMESPACE.FULL).
   *
   * @see #CRC_KEY
   */
    public static String getChecksumKey(final String tableNameFile) {
        String tableName = tableNameFile.substring(0, tableNameFile.lastIndexOf(FS));
        tableName = tableName.substring(tableName.lastIndexOf(FS) + 1, tableName.length());
        return CRC_KEY + "(" + tableName + ")";
    }

    /**
   * Returns a checksum value for the given file.
   * @param  fileName     The file name for which checksum is to be calculated.
   * @return  checksum value (using CRC32 algorithm).
   *
   * @see CRC32
   *
   * @throws IOException
   */
    public static long getFileChecksum(final String fileName) throws IOException {
        File file = new File(fileName);
        byte[] buffer = new byte[(4 << 10)];
        CRC32 checksum;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            checksum = new CRC32();
            checksum.reset();
            int bytesRead;
            while ((bytesRead = in.read(buffer)) >= 0) {
                checksum.update(buffer, 0, bytesRead);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return checksum.getValue();
    }

    /**
   * CRC property key string.
   */
    public static final String CRC_KEY = "CRC32";

    /**
   * File Separator
   */
    public static final String FS = System.getProperty("file.separator");
}
