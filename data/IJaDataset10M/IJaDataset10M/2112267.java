package org.jpedal.io;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.Inflater;
import javax.media.jai.JAI;
import javax.swing.JOptionPane;
import org.jpedal.sun.*;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Strip;

/**
 * Adds the abilty to decode streams to the PdfFileReader class
 */
public class PdfFilteredReader extends PdfFileReader {

    final boolean debugCaching = false;

    private boolean hasJAI = false, checkJAI = false;

    BufferedOutputStream streamCache = null;

    BufferedInputStream bis = null;

    private String filter_type;

    /** lookup for ASCII85 decode */
    private static final long[] base_85_indices = { 85 * 85 * 85 * 85, 85 * 85 * 85, 85 * 85, 85, 1 };

    /** lookup for hex multiplication */
    private static final long[] hex_indices = { 256 * 256 * 256, 256 * 256, 256, 1 };

    boolean containsJBIG;

    /**
	 * main routine which is passed list of filters to decode and the binary
	 * data. JPXDecode/DCTDecode are not handled here (we leave data as is and
	 * then put straight into a JPEG)<br>
	 * <p>
	 * <b>Note</b>
	 * </p>
	 * Not part of API
	 * </p>
	 */
    public final byte[] decodeFilters(byte[] data, String filter_list, Map objData, int width, int height, boolean useNewCCITT, String cacheName) throws Exception {
        streamCache = null;
        bis = null;
        final boolean debug = false;
        if (debug) System.out.println("=================");
        Map decodeParams = new HashMap();
        boolean isCached = ((cacheName != null) && ((data == null) || debugCaching));
        if (objData != null) {
            Object rawParams = objData.get("DecodeParms");
            if (rawParams != null) {
                if (rawParams instanceof String) {
                    convertStringToMap(decodeParams, rawParams);
                } else decodeParams = (Map) rawParams;
            }
        }
        if (filter_list.startsWith("[")) {
            if (filter_list.endsWith("]")) filter_list = filter_list.substring(1, filter_list.length() - 1); else filter_list = filter_list.substring(1);
        } else if (filter_list.endsWith("]")) filter_list = filter_list.substring(0, filter_list.length() - 1);
        if (debug) System.out.println(filter_list);
        if (filter_list.length() > 0) {
            StringTokenizer filters_to_decode = new StringTokenizer(filter_list);
            boolean isIgnored = false;
            if (debug) System.out.println("---------");
            while (filters_to_decode.hasMoreTokens()) {
                filter_type = filters_to_decode.nextToken();
                boolean isDCT = (filter_type.indexOf("/DCTDecode") != -1) || (filter_type.indexOf("/JPXDecode") != -1);
                if (isCached && !isDCT && cacheName != null) setupCachedObjectForDecoding(data, cacheName, debug, filter_type);
                if (filter_type.startsWith("/")) {
                    isIgnored = false;
                    if ((filter_type.indexOf("/FlateDecode") != -1) | (filter_type.indexOf("/Fl") != -1)) {
                        data = flateDecode(data, decodeParams, cacheName);
                    } else if ((filter_type.indexOf("/ASCII85Decode") != -1) | (filter_type.indexOf("/A85") != -1)) {
                        if (data != null) {
                            if (debug) System.out.println("data=" + data.length);
                            data = ascii85DecodeNEW(data);
                            if (debug) System.out.println("final data=" + data.length);
                        }
                        if (bis != null) ascii85Decode(bis, streamCache);
                    } else if (isCCITTEncoded(filter_type)) {
                        if (checkJAI == false) {
                            checkJAI = true;
                            java.io.InputStream in = getClass().getClassLoader().getResourceAsStream("javax/media/jai/RenderedOp.class");
                            if (in == null) {
                                LogWriter.writeLog("[PDF] WARNING-JAI class not present on classpath");
                                System.err.println("[PDF] WARNING-JAI class not present on classpath");
                            } else hasJAI = true;
                        }
                        if (isCached) {
                            int size = bis.available();
                            data = new byte[size];
                            bis.read(data);
                        }
                        byte[] newData = null;
                        if ((useNewCCITT) && (hasJAI) && (data != null)) {
                            TiffDecoder decode = new TiffDecoder(width, height, decodeParams, data);
                            newData = decode.getRawBytes();
                        }
                        if ((newData == null)) data = ccittDecode(data, decodeParams, width, height); else data = newData;
                        if (isCached) {
                            streamCache.write(data);
                        }
                    } else if (filter_type.indexOf("/LZW") != -1) {
                        data = lzwDecode(bis, streamCache, data, decodeParams, width, height, cacheName);
                    } else if ((filter_type.indexOf("/RunLengthDecode") != -1) | (filter_type.indexOf("/RL") != -1)) {
                        data = runLengthDecode(data, bis, streamCache);
                    } else if ((filter_type.indexOf("/ASCIIHexDecode") != -1) | (filter_type.indexOf("/AHx") != -1)) {
                        if (data != null) data = asciiHexDecode(data);
                        if (bis != null) asciiHexDecode(bis, streamCache);
                    } else if (isDCT) {
                        isIgnored = true;
                    } else {
                        LogWriter.writeLog("[PDF] Unsupported decompression stream " + filter_type);
                        data = null;
                    }
                    if (isCached) {
                        if (bis != null) bis.close();
                        if (streamCache != null) {
                            streamCache.flush();
                            streamCache.close();
                        }
                    }
                    if ((debugCaching) && (cacheName != null) && (!isIgnored)) {
                        if (debug) System.out.println(filter_type + " data=" + data.length + " cacheName=" + cacheName);
                        try {
                            bis = new BufferedInputStream(new FileInputStream(cacheName));
                            byte[] cachedData = new byte[bis.available()];
                            bis.read(cachedData);
                            if (cachedData.length != data.length) {
                                System.out.println(filter_type + " Different sizes inMemory=" + data.length + " cached=" + cachedData.length);
                                System.exit(1);
                            }
                            int count = data.length;
                            for (int ii = 0; ii < count; ii++) {
                                if (data[ii] != cachedData[ii]) {
                                    System.out.println(ii + " Different data " + data[ii] + " " + cachedData[ii]);
                                    System.exit(1);
                                }
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            System.exit(1);
                        }
                    }
                }
            }
        }
        return data;
    }

    private void setupCachedObjectForDecoding(byte[] data, String cacheName, final boolean debug, String filter_type) throws IOException, FileNotFoundException {
        File tempFile2 = File.createTempFile("jpedal", ".raw");
        cachedObjects.put(tempFile2.getAbsolutePath(), "x");
        ObjectStore.copy(cacheName, tempFile2.getAbsolutePath());
        File rawFile = new File(cacheName);
        rawFile.delete();
        streamCache = new BufferedOutputStream(new FileOutputStream(cacheName));
        if (debug) System.out.println("cache size=" + tempFile2.length());
        bis = new BufferedInputStream(new FileInputStream(tempFile2));
        if ((data != null) && (debugCaching)) {
            if (debug) System.out.println("Cached stream " + filter_type + " data length=" + data.length);
            if (tempFile2.length() != data.length) {
                if (debug) {
                    System.out.println("inputs different lengths " + tempFile2.length() + " " + data.length);
                    System.exit(1);
                }
            }
        }
    }

    /**
	 * @param decodeParams
	 * @param rawParams
	 */
    public void convertStringToMap(Map decodeParams, Object rawParams) {
        StringTokenizer paraValues = new StringTokenizer(Strip.removeArrayDeleminators((String) rawParams));
        while (paraValues.hasMoreTokens()) {
            String value = paraValues.nextToken();
            if (value.startsWith("<<")) value = value.substring(2).trim();
            if (value.startsWith("/")) {
                String key = value.substring(1);
                value = paraValues.nextToken();
                if (value.endsWith(">>")) value = value.substring(0, value.length() - 2).trim();
                decodeParams.put(key, value);
            }
        }
    }

    /**
	 * Run length decode. If both data and cached stream are present it will check
	 * they are identical
	 */
    private byte[] runLengthDecode(byte[] data, BufferedInputStream bis, BufferedOutputStream streamCache) throws Exception {
        ByteArrayOutputStream bos = null;
        int count = 0, len = 0, nextLen = 0, value = 0, value2 = 0;
        if (data != null) {
            count = data.length;
            bos = new ByteArrayOutputStream(count);
        }
        if (bis != null) count = bis.available();
        if (data != null && bis != null) {
            if (data.length != bis.available()) {
                System.out.println("Different lengths in RunLengthDecode");
                System.out.println(data.length + " " + bis.available());
                System.exit(1);
            }
        }
        for (int i = 0; i < count; i++) {
            if (data != null) len = data[i];
            if (bis != null) {
                nextLen = bis.read();
                if (nextLen >= 128) nextLen = nextLen - 256;
                if (data != null && (len != nextLen)) {
                    System.out.println("Len wrong =" + len + " " + nextLen);
                    System.exit(1);
                }
                len = nextLen;
            }
            if (len < 0) len = 256 + len;
            if (len == 128) {
                i = count;
            } else if (len > 128) {
                i++;
                len = 257 - len;
                if (data != null) value = data[i];
                if (streamCache != null) {
                    value2 = bis.read();
                    if (value2 >= 128) value2 = value2 - 256;
                }
                if (data != null && bis != null) {
                    if (value != value2) {
                        System.out.println("Different values in RunLengthDecode");
                        System.out.println(value + " " + value2 + " " + streamCache);
                        System.exit(1);
                    }
                }
                for (int j = 0; j < len; j++) {
                    if (data != null) bos.write(value);
                    if (streamCache != null) streamCache.write(value2);
                }
            } else {
                i++;
                len++;
                for (int j = 0; j < len; j++) {
                    if (data != null) {
                        value = data[i + j];
                        bos.write(value);
                    }
                    if (streamCache != null) {
                        value2 = bis.read();
                        if (value2 >= 128) value2 = value2 - 256;
                        streamCache.write(value2);
                    }
                    if (data != null && bis != null) {
                        if (value != value2) {
                            System.out.println("2Different values in RunLengthDecode");
                            System.out.println(value + " " + value2);
                            System.exit(1);
                        }
                    }
                }
                i = i + len - 1;
            }
        }
        if (data != null) {
            bos.close();
            data = bos.toByteArray();
        }
        return data;
    }

    /**
	 * Run length decode. If both data and cached stream are present it will check
	 * they are identical
	 */
    private byte[] runJBIGDecode(byte[] data, BufferedInputStream bis, BufferedOutputStream streamCache) throws Exception {
        ByteArrayOutputStream bos = null;
        int count = 0, len = 0, nextLen = 0, value = 0, value2 = 0;
        if (data != null) {
            count = data.length;
            bos = new ByteArrayOutputStream(count);
        }
        if (bis != null) count = bis.available();
        if (data != null && bis != null) {
            if (data.length != bis.available()) {
                System.out.println("Different lengths in RunLengthDecode");
                System.out.println(data.length + " " + bis.available());
                System.exit(1);
            }
        }
        for (int i = 0; i < count; i++) {
            if (data != null) len = data[i];
            if (bis != null) {
                nextLen = bis.read();
                if (nextLen >= 128) nextLen = nextLen - 256;
                if (data != null && (len != nextLen)) {
                    System.out.println("Len wrong =" + len + " " + nextLen);
                    System.exit(1);
                }
                len = nextLen;
            }
            if (len < 0) len = 256 + len;
            if (len == 128) {
                i = count;
            } else if (len > 128) {
                i++;
                len = 257 - len;
                if (data != null) value = data[i];
                if (streamCache != null) {
                    value2 = bis.read();
                    if (value2 >= 128) value2 = value2 - 256;
                }
                if (data != null && bis != null) {
                    if (value != value2) {
                        System.out.println("Different values in RunLengthDecode");
                        System.out.println(value + " " + value2 + " " + streamCache);
                        System.exit(1);
                    }
                }
                for (int j = 0; j < len; j++) {
                    if (data != null) bos.write(value);
                    if (streamCache != null) streamCache.write(value2);
                }
            } else {
                i++;
                len++;
                for (int j = 0; j < len; j++) {
                    if (data != null) {
                        value = data[i + j];
                        bos.write(value);
                    }
                    if (streamCache != null) {
                        value2 = bis.read();
                        if (value2 >= 128) value2 = value2 - 256;
                        streamCache.write(value2);
                    }
                    if (data != null && bis != null) {
                        if (value != value2) {
                            System.out.println("2Different values in RunLengthDecode");
                            System.out.println(value + " " + value2);
                            System.exit(1);
                        }
                    }
                }
                i = i + len - 1;
            }
        }
        if (data != null) {
            bos.close();
            data = bos.toByteArray();
        }
        return data;
    }

    /**
	 * lzw decode using our own class
	 *
	 * @param cacheName
	 */
    private final byte[] lzwDecode(BufferedInputStream bis, BufferedOutputStream streamCache, byte[] data, Map values, int width, int height, String cacheName) throws Exception {
        int predictor = 1;
        int BitsPerComponent = 8;
        int EarlyChange = 1;
        int rows = height, columns = width;
        String value = (String) values.get("Predictor");
        if (value != null) predictor = Integer.parseInt(value);
        value = (String) values.get("Rows");
        if (value != null) rows = Integer.parseInt(value);
        value = (String) values.get("Columns");
        if (value != null) columns = Integer.parseInt(value);
        value = (String) values.get("EarlyChange");
        if (value != null) EarlyChange = Integer.parseInt(value);
        value = (String) values.get("BitsPerComponent");
        if (value != null) BitsPerComponent = Integer.parseInt(value);
        byte[] processedData = null;
        if (rows * columns == 1) {
            if (data != null) {
                byte[] processed_data = new byte[BitsPerComponent * rows * ((columns + 7) >> 3)];
                TIFFLZWDecoder lzw_decode = new TIFFLZWDecoder(columns, predictor, BitsPerComponent);
                lzw_decode.decode(data, processed_data, rows);
                return applyPredictor(predictor, values, processed_data);
            } else {
                return null;
            }
        } else {
            if (bis != null) {
                LZWDecoder2 lzw2 = new LZWDecoder2();
                lzw2.decode(data, streamCache, bis);
            }
            if (data != null) {
                ByteArrayOutputStream processed = new ByteArrayOutputStream();
                LZWDecoder lzw = new LZWDecoder();
                lzw.decode(data, processed);
                processed.close();
                data = processed.toByteArray();
            }
            if (predictor != 1 && predictor != 10) {
                streamCache.flush();
                streamCache.close();
                if (cacheName != null) setupCachedObjectForDecoding(data, cacheName, false, filter_type);
            }
            data = applyPredictor(predictor, values, data);
        }
        return data;
    }

    /**
	 * ccitt decode using Sun class
	 */
    private final byte[] ccittDecode(byte[] data, Map values, int width, int height) throws Exception {
        boolean isBlack = false;
        int columns = 1728;
        int rows = height;
        int k = 0;
        boolean isByteAligned = false;
        String value = (String) values.get("K");
        if (value != null) k = Integer.parseInt(value);
        value = (String) values.get("EncodedByteAlign");
        if (value != null) isByteAligned = Boolean.valueOf(value).booleanValue();
        value = (String) values.get("BlackIs1");
        if (value != null) isBlack = Boolean.valueOf(value).booleanValue();
        value = (String) values.get("Rows");
        if (value != null) rows = Integer.parseInt(value);
        value = (String) values.get("Columns");
        if (value != null) columns = Integer.parseInt(value);
        byte[] processed_data = new byte[rows * ((columns + 7) >> 3)];
        try {
            TIFFFaxDecoder tiff_decode = new TIFFFaxDecoder(1, columns, rows);
            if (k == 0) tiff_decode.decode1D(processed_data, data, 0, rows); else if (k > 0) tiff_decode.decode2D(processed_data, data, 0, rows, 0); else if (k < 0) tiff_decode.decodeT6(processed_data, data, 0, rows, 0, isByteAligned);
            if (!isBlack) {
                for (int i = 0; i < processed_data.length; i++) processed_data[i] = (byte) (255 - processed_data[i]);
            }
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " accessing CCITT filter " + e);
        }
        return processed_data;
    }

    /**
	 * ascii85decode using our own implementation
	 */
    private final byte[] ascii85DecodeOLD(byte[] data) {
        int special_cases = 0;
        String line = "";
        CharArrayWriter rawValuesRead = new CharArrayWriter(data.length);
        BufferedReader mappingStream = null;
        ByteArrayInputStream bis = null;
        try {
            bis = new ByteArrayInputStream(data);
            mappingStream = new BufferedReader(new InputStreamReader(bis));
            if (mappingStream != null) {
                while (true) {
                    line = mappingStream.readLine();
                    if (line == null) break;
                    rawValuesRead.write(line);
                }
            }
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " reading ASCII stream ");
        }
        char[] valuesRead = rawValuesRead.toCharArray();
        if (mappingStream != null) {
            try {
                mappingStream.close();
                bis.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        int data_size = valuesRead.length;
        for (int i = 0; i < data_size; i++) {
            if (valuesRead[i] == 122) special_cases++;
        }
        int output_pointer = 0;
        long value = 0;
        byte[] temp_data = new byte[data_size + 1 + (special_cases * 3)];
        int ii = 0;
        for (int i = 0; i < data_size; i++) {
            value = 0;
            int next = valuesRead[i];
            while ((next == 10) || (next == 13)) {
                i++;
                next = valuesRead[i];
            }
            if (next == 122) {
                for (int i3 = 0; i3 < 4; i3++) {
                    temp_data[output_pointer] = 0;
                    output_pointer++;
                }
            } else if ((data_size - i > 4) && (next > 32) && (next < 118)) {
                for (ii = 0; ii < 5; ii++) {
                    next = valuesRead[i];
                    while ((next == 10) || (next == 13)) {
                        i++;
                        next = valuesRead[i];
                    }
                    i++;
                    value = value + ((next - 33) * base_85_indices[ii]);
                }
                for (int i3 = 0; i3 < 4; i3++) {
                    temp_data[output_pointer] = (byte) ((value / hex_indices[i3]) & 255);
                    output_pointer++;
                }
                i--;
            }
        }
        byte[] processed_data = new byte[output_pointer];
        System.arraycopy(temp_data, 0, processed_data, 0, output_pointer);
        return processed_data;
    }

    /**
	 * ascii85decode using our own implementation
	 */
    private final byte[] ascii85DecodeNEW(byte[] valuesRead) {
        int special_cases = 0, returns = 0;
        String line = "";
        int data_size = valuesRead.length;
        for (int i = 0; i < data_size; i++) {
            if (valuesRead[i] == 122) special_cases++;
            if ((valuesRead[i] == 10) || (valuesRead[i] == 10)) returns++;
        }
        int output_pointer = 0;
        long value = 0;
        byte[] temp_data = new byte[data_size - returns + 1 + (special_cases * 3)];
        int ii = 0;
        for (int i = 0; i < data_size; i++) {
            value = 0;
            int next = valuesRead[i];
            while ((next == 10) || (next == 13)) {
                i++;
                if (i == data_size) next = 0; else next = valuesRead[i];
            }
            if (next == 122) {
                for (int i3 = 0; i3 < 4; i3++) {
                    temp_data[output_pointer] = 0;
                    output_pointer++;
                }
            } else if ((data_size - i > 4) && (next > 32) && (next < 118)) {
                String list = "";
                for (ii = 0; ii < 5; ii++) {
                    next = valuesRead[i];
                    list = list + next + " ";
                    while ((next == 10) || (next == 13)) {
                        i++;
                        if (i == data_size) next = 0; else next = valuesRead[i];
                    }
                    i++;
                    if (((next > 32) && (next < 118)) || (next == 126)) value = value + ((next - 33) * base_85_indices[ii]);
                }
                for (int i3 = 0; i3 < 4; i3++) {
                    temp_data[output_pointer] = (byte) ((value / hex_indices[i3]) & 255);
                    output_pointer++;
                }
                i--;
            }
        }
        byte[] processed_data = new byte[output_pointer];
        System.arraycopy(temp_data, 0, processed_data, 0, output_pointer);
        return processed_data;
    }

    /**
	 * ascii85decode using our own implementation
	 */
    private final void ascii85Decode(BufferedInputStream bis, BufferedOutputStream streamCache) {
        long value = 0;
        int nextValue = 0;
        try {
            int data_size = bis.available(), lastValue = 0;
            boolean ignoreLastItem = false;
            while (bis.available() > 0) {
                value = 0;
                nextValue = read(bis);
                if (nextValue == 122) {
                    for (int i3 = 0; i3 < 4; i3++) streamCache.write(0);
                } else if ((bis.available() >= 4) && (nextValue > 32) && (nextValue < 118)) {
                    lastValue = nextValue;
                    value = value + ((nextValue - 33) * base_85_indices[0]);
                    String list = "";
                    for (int ii = 1; ii < 5; ii++) {
                        nextValue = read(bis);
                        list = list + nextValue + " ";
                        if (nextValue == -1) nextValue = 0;
                        if (nextValue == -1) ignoreLastItem = true;
                        lastValue = nextValue;
                        if (((nextValue > 32) && (nextValue < 118)) || (nextValue == 126)) value = value + ((nextValue - 33) * base_85_indices[ii]);
                    }
                    if (!ignoreLastItem) {
                        for (int i3 = 0; i3 < 4; i3++) {
                            byte b = (byte) ((value / hex_indices[i3]) & 255);
                            streamCache.write(b);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int read(BufferedInputStream bis) throws IOException {
        int nextValue = bis.read();
        while ((nextValue == 13) || (nextValue == 10)) nextValue = bis.read();
        return nextValue;
    }

    /**
	 * asciihexdecode using our own implementation
	 */
    private final byte[] asciiHexDecode(byte[] data) throws IOException {
        String line = "";
        StringBuffer value = new StringBuffer();
        StringBuffer valuesRead = new StringBuffer();
        BufferedReader mappingStream = null;
        ByteArrayInputStream bis = null;
        try {
            bis = new ByteArrayInputStream(data);
            mappingStream = new BufferedReader(new InputStreamReader(bis));
            if (mappingStream != null) {
                while (true) {
                    line = mappingStream.readLine();
                    if (line == null) break;
                    valuesRead.append(line);
                }
            }
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " reading ASCII stream ");
        }
        if (mappingStream != null) {
            try {
                mappingStream.close();
                bis.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        int data_size = valuesRead.length();
        int i = 0, count = 0;
        char current = ' ';
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        while (true) {
            current = valuesRead.charAt(i);
            if (((current >= '0') & (current <= '9')) | ((current >= 'a') & (current <= 'f')) | ((current >= 'A') & (current <= 'F'))) {
                value.append(current);
                if (count == 1) {
                    bos.write(Integer.valueOf(value.toString(), 16).intValue());
                    count = 0;
                    value = new StringBuffer();
                } else count++;
            }
            if (current == '>') break;
            i++;
            if (i == data_size) break;
        }
        if (count == 1) {
            value.append('0');
            bos.write(Integer.valueOf(value.toString(), 16).intValue());
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
	 * asciihexdecode using our own implementation
	 */
    private final void asciiHexDecode(BufferedInputStream bis, BufferedOutputStream streamCache) throws IOException {
        StringBuffer value = new StringBuffer();
        char current = ' ';
        int count = bis.available();
        for (int i = 0; i < count; i++) {
            current = (char) bis.read();
            while (current == '\n') current = (char) bis.read();
            if (((current >= '0') & (current <= '9')) | ((current >= 'a') & (current <= 'f')) | ((current >= 'A') & (current <= 'F'))) {
                value.append(current);
                if (count == 1) {
                    streamCache.write(Integer.valueOf(value.toString(), 16).intValue());
                    count = 0;
                    value = new StringBuffer();
                } else count++;
            }
            if (current == '>') break;
        }
        if (count == 1) {
            value.append('0');
            streamCache.write(Integer.valueOf(value.toString(), 16).intValue());
        }
    }

    /**
	 * flate decode - use a byte array stream to decompress data in memory
	 *
	 * @param cacheName
	 */
    private final byte[] flateDecode(byte[] data, Map params, String cacheName) throws Exception {
        byte[] returnData = null;
        int predictor = 1;
        String value = (String) params.get("Predictor");
        if (value != null) predictor = Integer.parseInt(value);
        if (data != null) {
            Inflater inf = new Inflater();
            inf.setInput(data);
            int size = data.length;
            ByteArrayOutputStream bos = new ByteArrayOutputStream(size);
            int bufSize = 512000;
            if (size < bufSize) bufSize = size;
            byte[] buf = new byte[bufSize];
            int debug = 20;
            int count;
            while (!inf.finished()) {
                count = inf.inflate(buf);
                bos.write(buf, 0, count);
                if (inf.getRemaining() == 0) break;
            }
            data = bos.toByteArray();
        }
        if (bis != null) {
            try {
                InputStream inf;
                inf = new java.util.zip.InflaterInputStream(bis);
                int i = 0;
                while (true) {
                    int b = inf.read();
                    if (returnData != null) {
                        if (b != (returnData[i] & 255)) {
                            System.out.println("Different in flate at " + i);
                            System.exit(1);
                        }
                    }
                    if ((inf.available() == 0) || (b == -1)) break;
                    streamCache.write(b);
                    i++;
                }
                if (predictor != 1 && predictor != 10) {
                    streamCache.flush();
                    streamCache.close();
                    if (cacheName != null) setupCachedObjectForDecoding(data, cacheName, false, filter_type);
                }
            } catch (Exception e) {
            }
        }
        returnData = applyPredictor(predictor, params, data);
        return returnData;
    }

    /**
	 * implement predictor function
	 */
    private void applyPredictorFunction(int mainPred, Map params, BufferedInputStream bis, OutputStream bos) throws Exception {
        int predictor = mainPred;
        int bytesAvaiable = bis.available();
        int colors = 1, bitsPerComponent = 8, columns = 1, earlyChange = 1;
        String value = (String) params.get("Colors");
        if (value != null) colors = Integer.parseInt(value);
        value = (String) params.get("BitsPerComponent");
        if (value != null) bitsPerComponent = Integer.parseInt(value);
        value = (String) params.get("Columns");
        if (value != null) columns = Integer.parseInt(value);
        int bpp = (colors * bitsPerComponent + 7) / 8;
        int rowLength = (columns * colors * bitsPerComponent + 7) / 8 + bpp;
        byte[] thisLine = new byte[rowLength];
        byte[] lastLine = new byte[rowLength];
        try {
            int byteCount = 0;
            while (true) {
                if (bytesAvaiable <= byteCount) break;
                predictor = mainPred;
                if (predictor == 15) {
                    predictor = bis.read();
                    if (predictor == -1) break;
                    predictor = predictor + 10;
                } else if (predictor >= 10) {
                    bis.read();
                }
                int i = 0;
                int offset = bpp;
                int bytesToRead = rowLength;
                while (offset < bytesToRead) {
                    i = bis.read(thisLine, offset, bytesToRead - offset);
                    if (i == -1) break;
                    offset += i;
                    byteCount += i;
                }
                if (i == -1) break;
                switch(predictor) {
                    case 2:
                        for (int i1 = bpp; i1 < rowLength; i1++) {
                            int sub = thisLine[i1] & 0xff;
                            int raw = lastLine[i1 - bpp] & 0xff;
                            thisLine[i1] = (byte) ((sub + raw) & 0xff);
                            bos.write(thisLine[i1]);
                        }
                        break;
                    case 10:
                        for (int i1 = bpp; i1 < rowLength; i1++) bos.write(thisLine[i1]);
                        break;
                    case 11:
                        for (int i1 = bpp; i1 < rowLength; i1++) {
                            int sub = thisLine[i1] & 0xff;
                            int raw = thisLine[i1 - bpp] & 0xff;
                            thisLine[i1] = (byte) ((sub + raw));
                            bos.write(thisLine[i1]);
                        }
                        break;
                    case 12:
                        for (int i1 = bpp; i1 < rowLength; i1++) {
                            int up = thisLine[i1] & 0xff;
                            int prior = lastLine[i1] & 0xff;
                            thisLine[i1] = (byte) ((up + prior) & 0xff);
                            bos.write(thisLine[i1]);
                        }
                        break;
                    case 13:
                        for (int i1 = bpp; i1 < rowLength; i1++) {
                            int av = thisLine[i1] & 0xff;
                            int floor = ((thisLine[i1 - bpp] & 0xff) + (lastLine[i1] & 0xff) >> 1);
                            thisLine[i1] = (byte) (av + floor);
                            bos.write(thisLine[i1]);
                        }
                        break;
                    case 14:
                        for (int i1 = bpp; i1 < rowLength; i1++) {
                            int a = thisLine[i1 - bpp] & 0xff, b = lastLine[i1] & 0xff, c = lastLine[i1 - bpp] & 0xff;
                            int p = a + b - c;
                            int pa = p - a, pb = p - b, pc = p - c;
                            if (pa < 0) pa = -pa;
                            if (pb < 0) pb = -pb;
                            if (pc < 0) pc = -pc;
                            if (pa <= pb && pa <= pc) thisLine[i1] = (byte) (thisLine[i1] + a); else if (pb <= pc) thisLine[i1] = (byte) (thisLine[i1] + b); else thisLine[i1] = (byte) (thisLine[i1] + c);
                            bos.write(thisLine[i1]);
                        }
                        break;
                    case 15:
                        break;
                    default:
                        break;
                }
                for (int ii = 0; ii < lastLine.length; ii++) lastLine[ii] = thisLine[ii];
            }
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private byte[] applyPredictor(int predictor, Map params, byte[] data) throws Exception {
        if (predictor == 1 || predictor == 10) {
            return data;
        } else {
            boolean isCached = data == null;
            if (isCached) {
                applyPredictorFunction(predictor, params, bis, streamCache);
                return null;
            } else {
                BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(data));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                applyPredictorFunction(predictor, params, bis, bos);
                byte[] returnData = bos.toByteArray();
                return returnData;
            }
        }
    }

    /**
	 * decide if we convert straight to image or into byte stream so we can
	 * manipulate further
	 */
    private boolean isCCITTEncoded(String filter) {
        if (filter == null) return false; else return ((filter.startsWith("/CCITT")) | (filter.startsWith("/CCF")));
    }
}
