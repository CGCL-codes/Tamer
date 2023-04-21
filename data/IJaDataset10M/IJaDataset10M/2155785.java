package org.tuotoo.util;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import org.tuotoo.logging.LogHolder;
import org.tuotoo.logging.LogLevel;
import org.tuotoo.logging.LogType;

/**
 * This class provides some utility methods for ZLib compression.
 */
public final class ZLibTools {

    /**
     * Compresses the specified data with ZLib in the best compression mode.
     * 
     * @param a_data
     *            The data to compress.
     * @return The compressed data or null, if there was an error while the
     *         compression.
     */
    public static byte[] compress(byte[] a_data) {
        byte[] resultData = null;
        try {
            Deflater zipper = new Deflater(Deflater.BEST_COMPRESSION);
            ByteArrayOutputStream zippedData = new ByteArrayOutputStream();
            DeflaterOutputStream zipStream = new DeflaterOutputStream(zippedData, zipper);
            zipStream.write(a_data, 0, a_data.length);
            zipStream.finish();
            resultData = zippedData.toByteArray();
        } catch (Throwable e) {
        }
        return resultData;
    }

    /**
     * Decompresses the specified data.
     * 
     * @param a_data
     *            The ZLib compressed data (whole block, not only parts).
     * @return The uncompressed data or null, if the specified data are not ZLib
     *         compressed.
     */
    public static byte[] decompress(byte[] a_data) {
        byte[] resultData = null;
        try {
            ByteArrayOutputStream unzippedData = new ByteArrayOutputStream();
            Inflater unzipper = new Inflater();
            unzipper.setInput(a_data);
            byte[] currentByte = new byte[10000];
            int len;
            while ((len = unzipper.inflate(currentByte)) > 0) {
                unzippedData.write(currentByte, 0, len);
            }
            unzippedData.flush();
            resultData = unzippedData.toByteArray();
        } catch (Throwable e) {
            LogHolder.log(LogLevel.INFO, LogType.MISC, "ZLIb decompress() decommpressed failed!");
        }
        return resultData;
    }
}
