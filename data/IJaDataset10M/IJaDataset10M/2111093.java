package org.proteomecommons.mzml.zip.sax;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import org.apache.xerces.impl.dv.util.Base64;
import org.proteomecommons.mzml.zip.MzmlTags;
import org.proteomecommons.mzml.zip.Util;

/**
 * 
 * @author Bryan Smith - bryanesmith@gmail.com
 */
public abstract class MZSquashSAXHandler {

    /**
     * 
     */
    private InputStream in = null;

    /**
     * 
     */
    private File tmp = null;

    /**
     * 
     */
    private static enum CompressionType {

        NONE, ZLIB
    }

    ;

    /**
     * 
     * @param file
     */
    public final void parse(File inputFile) throws Exception {
        int inputVersion = Util.getVersion(inputFile);
        int lastbinaryDataArrayListArrayLength = -1;
        CompressionType lastCompressionRead = null;
        tmp = new File(inputFile.getParent(), inputFile.getName() + ".tmp." + System.currentTimeMillis());
        if (tmp.exists()) {
            throw new Exception("Want to create temporary file, already exists: " + tmp.getAbsolutePath());
        }
        tmp.createNewFile();
        Util.decompressGZIP(inputFile, tmp, 4);
        in = new BufferedInputStream(new FileInputStream(tmp));
        byte[] charBuf = new byte[1];
        startDocument();
        while (Util.read(charBuf, in) != -1) {
            if (Util.bytesMatch(charBuf, Util.OPEN_TAG)) {
                String tagName = null;
                List<MZSquashSAXAttribute> attributes = new LinkedList();
                boolean isBinary = false;
                boolean isClosingTag = false;
                Util.read(charBuf, in);
                if (Util.bytesMatch(charBuf, Util.SLASH)) {
                    isClosingTag = true;
                    Util.read(charBuf, in);
                }
                byte b = charBuf[0];
                tagName = MzmlTags.byteToStringMap.get(b);
                if (tagName.equals("binary") && !isClosingTag) {
                    isBinary = true;
                }
                Util.read(charBuf, in);
                while (!(Util.bytesMatch(charBuf, Util.CLOSE_TAG) || Util.bytesMatch(charBuf, Util.SLASH))) {
                    b = charBuf[0];
                    String attributeName = MzmlTags.byteToStringMap.get(b);
                    StringBuffer attrValueBuffer = new StringBuffer();
                    Util.read(charBuf, in);
                    while (!Util.bytesMatch(charBuf, Util.NULL)) {
                        attrValueBuffer.append(new String(charBuf));
                        Util.read(charBuf, in);
                    }
                    attributes.add(new MZSquashSAXAttribute(attributeName, attrValueBuffer.toString()));
                    if (tagName.equals("binaryDataArray") && attributeName.trim().equalsIgnoreCase("arrayLength")) {
                        lastbinaryDataArrayListArrayLength = Integer.parseInt(attrValueBuffer.toString());
                    } else if (tagName.equals("cvParam") && attributeName.trim().equalsIgnoreCase("name") && attrValueBuffer.toString().trim().equalsIgnoreCase("zlib compression")) {
                        lastCompressionRead = CompressionType.ZLIB;
                    } else if (tagName.equals("cvParam") && attributeName.trim().equalsIgnoreCase("name") && attrValueBuffer.toString().trim().equalsIgnoreCase("no compression")) {
                        lastCompressionRead = CompressionType.NONE;
                    }
                    Util.read(charBuf, in);
                }
                if (Util.bytesMatch(charBuf, Util.CLOSE_TAG)) {
                    if (!isClosingTag) {
                        startElement(tagName, attributes);
                    } else {
                        endElement(tagName);
                    }
                } else if (Util.bytesMatch(charBuf, Util.SLASH)) {
                    Util.read(charBuf, in);
                    if (!Util.bytesMatch(charBuf, Util.CLOSE_TAG)) {
                        throw new RuntimeException("Expected bytes for \">\" to close tag, instead found: " + new String(charBuf));
                    }
                    startElement(tagName, attributes);
                    endElement(tagName);
                }
                if (isBinary) {
                    readBinaryArray(lastCompressionRead, lastbinaryDataArrayListArrayLength);
                    lastCompressionRead = null;
                    lastbinaryDataArrayListArrayLength = -1;
                } else {
                }
            } else {
            }
        }
        endDocument();
    }

    /**
     * 
     * @throws java.lang.Exception
     */
    public final void close() throws Exception {
        try {
            in.close();
        } catch (Exception ex) {
        }
        try {
            tmp.delete();
        } catch (Exception ex) {
        }
        if (tmp != null && tmp.exists()) {
            throw new Exception("Failed to delete temporary file: " + tmp.getAbsolutePath());
        }
    }

    /**
     * 
     * @param lastCompressionRead
     */
    private void readBinaryArray(CompressionType lastCompressionRead, int lastBinaryDataArrayListArrayLength) throws Exception {
        try {
            if (lastCompressionRead == null) {
                throw new NullPointerException("Compression type for binary data not found. This is a required tag.");
            }
            if (lastBinaryDataArrayListArrayLength == -1) {
                throw new NullPointerException("The data array length was not found. This is a required value.");
            }
            try {
                byte[] lenBytes = new byte[4];
                Util.read(lenBytes, in);
                int len = Util.convertBytesToInt(lenBytes);
                byte[] compressedBytes = new byte[len];
                Util.read(compressedBytes, in);
                byte[] decompressedBytes = null;
                Inflater decompressor = null;
                ByteArrayOutputStream bos = null;
                try {
                    decompressor = new Inflater();
                    decompressor.setInput(compressedBytes);
                    bos = new ByteArrayOutputStream(lastBinaryDataArrayListArrayLength * 4);
                    byte[] buf = new byte[1024];
                    while (!decompressor.finished()) {
                        int count = decompressor.inflate(buf);
                        bos.write(buf, 0, count);
                    }
                    decompressedBytes = bos.toByteArray();
                } finally {
                    try {
                        bos.close();
                    } catch (Exception nope) {
                    }
                    compressedBytes = null;
                }
                byte[] floatBytes = new byte[lastBinaryDataArrayListArrayLength * 4];
                for (int floatIndex = 0; floatIndex < decompressedBytes.length; floatIndex += 4) {
                    int intBits = 0;
                    for (int i = 0; i < 4; i++) {
                        int nextIntBitLayer = -1;
                        if (i == 0) {
                            nextIntBitLayer = (decompressedBytes[floatIndex + i] << (0)) & 0x000000ff;
                        } else if (i == 1) {
                            nextIntBitLayer = (decompressedBytes[floatIndex + i] << (8)) & 0x0000ff00;
                        } else if (i == 2) {
                            nextIntBitLayer = (decompressedBytes[floatIndex + i] << (16)) & 0x00ff0000;
                        } else if (i == 3) {
                            nextIntBitLayer = (decompressedBytes[floatIndex + i] << (24)) & 0xff000000;
                        } else {
                            throw new RuntimeException("Expecting 0 <= i <= 3, found i = " + i + ". To tired to program correctly.");
                        }
                        intBits |= nextIntBitLayer;
                    }
                    intBits &= 0xffffffff;
                    float f = Float.intBitsToFloat(intBits);
                    Util.putFloatIntoDecodedArray(floatBytes, floatIndex, f);
                }
                decompressedBytes = null;
                switch(lastCompressionRead) {
                    case ZLIB:
                        floatBytes = Util.compressZLIB(floatBytes);
                        break;
                    case NONE:
                        break;
                    default:
                        throw new RuntimeException("Unrecognized compression type: " + lastCompressionRead);
                }
                ByteBuffer bbuf = ByteBuffer.allocate(floatBytes.length);
                bbuf.put(floatBytes);
                floatBytes = bbuf.order(ByteOrder.LITTLE_ENDIAN).array();
                String base64Str = Base64.encode(floatBytes);
                int bufferSize = 1024 * 4;
                for (int i = 0; i < base64Str.length(); i += bufferSize) {
                    int batchSize = bufferSize;
                    if (i + batchSize > base64Str.length()) {
                        batchSize = base64Str.length() - i;
                    }
                    binary(base64Str.substring(i, i + batchSize).toCharArray());
                }
            } finally {
            }
        } catch (DataFormatException dfe) {
            IOException ioe = new IOException(dfe.getClass().getSimpleName() + ": " + dfe.getMessage());
            ioe.setStackTrace(dfe.getStackTrace());
            throw ioe;
        }
    }

    /**
     * <p>Fired when a document starts.</p>
     * @throws java.lang.Exception
     */
    public abstract void startDocument() throws Exception;

    /**
     * <p>Fired when a document ends.</p>
     * @throws java.lang.Exception
     */
    public abstract void endDocument() throws Exception;

    /**
     * <p>Fired when an element's opening tag is read.</p>
     * @param name
     * @param attributes
     * @throws java.lang.Exception
     */
    public abstract void startElement(String name, List<MZSquashSAXAttribute> attributes) throws Exception;

    /**
     * <p>Fired when an element's closing tag is read, or when an empty tag ends.</p>
     * @param name
     * @throws java.lang.Exception
     */
    public abstract void endElement(String name) throws Exception;

    /**
     * <p>Fired when some character read from binary tags. There is no contract specifying how many characters are buffered in memory.</p>
     * @param characters
     * @throws java.lang.Exception
     */
    public abstract void binary(char[] characters) throws Exception;
}
