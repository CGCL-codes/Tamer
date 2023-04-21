package fi.hip.gb.gateway.util;

import java.io.UnsupportedEncodingException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.apache.log4j.Logger;

/**
 * @author Henri Mikkonen <henri.mikkonen@hip.fi>
 *
 */
public class CompressUtil {

    private static final String CHARACTER_ENCODING = "UTF-8";

    private static final int MAX_MESSAGE_SIZE = 5000;

    public static String deCompressBytesToStr(byte[] compressedBytes) {
        Logger logger = Logger.getLogger(CompressUtil.class.getName());
        Inflater decompresser = new Inflater();
        decompresser.setInput(compressedBytes);
        int stringLength = 0;
        byte[] stringBytes = new byte[MAX_MESSAGE_SIZE];
        try {
            stringLength = decompresser.inflate(stringBytes);
        } catch (Exception e) {
            logger.error("The compressed bytes are badly formatted!");
        }
        decompresser.end();
        try {
            return new String(stringBytes, 0, stringLength, CHARACTER_ENCODING);
        } catch (Exception e) {
            logger.error("The XML is not UTF-8 formatted!");
            return null;
        }
    }

    public static String compressStr(String inputString) {
        Logger logger = Logger.getLogger(CompressUtil.class.getName());
        byte[] input = null;
        try {
            input = inputString.getBytes(CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException uee) {
            logger.error("The XML is not UTF-8 formatted!");
        }
        byte[] output = new byte[MAX_MESSAGE_SIZE];
        Deflater compresser = new Deflater();
        compresser.setInput(input);
        compresser.finish();
        int compressedDataLength = compresser.deflate(output);
        byte[] outputBytes = new byte[compressedDataLength];
        for (int i = 0; i < compressedDataLength; i++) {
            outputBytes[i] = output[i];
        }
        return Base64.encodeBytes(outputBytes);
    }
}
