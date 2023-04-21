package phex.msg;

import java.io.*;
import java.util.StringTokenizer;
import phex.common.URN;
import phex.host.HostAddress;
import phex.utils.*;

/**
 * A single response record in a QueryResponse message.
 */
public class MsgResRecord {

    private int fileIndex = 0;

    private String fileName = "";

    private int fileSize = 0;

    private URN urn;

    private String metaData;

    private HostAddress[] alternateLocations;

    /**
     * Create a new MsgResRecord.
     */
    public MsgResRecord() {
    }

    /**
     * Create a new MsgResRecord with all its properties populated.
     *
     * @param fileIndex  the index of the file
     * @param fileSize   the file size (bytes)
     * @param aFileName  a String representation of the file name
     */
    public MsgResRecord(int fileIndex, URN fileURN, int fileSize, String aFileName) {
        this.fileIndex = fileIndex;
        this.fileSize = fileSize;
        this.fileName = aFileName;
        this.urn = fileURN;
    }

    /**
     * Get the current file index.
     *
     * @return the file index
     */
    public int getFileIndex() {
        return fileIndex;
    }

    /**
     * Get the file size (bytes).
     *
     * @return the current file size
     */
    public int getFileSize() {
        return fileSize;
    }

    /**
     * Get the current file name.
     *
     * @return the current file name
     */
    public String getFilename() {
        return fileName;
    }

    public int getSize() {
        return 8 + fileName.length() + urn.getAsString().length() + 2;
    }

    public URN getURN() {
        return urn;
    }

    public HostAddress[] getAlternateLocations() {
        return alternateLocations;
    }

    public String getMetaData() {
        return metaData;
    }

    /**
     * Copy the information from another MsgResRecord into this record.
     *
     * @param b  the MsgResRecord to copy
     */
    public void copy(MsgResRecord b) {
        fileIndex = b.fileIndex;
        fileSize = b.fileSize;
        fileName = b.fileName;
    }

    public int serialize(byte[] outbuf, int offset) {
        offset = IOUtil.serializeIntLE(fileIndex, outbuf, offset);
        offset = IOUtil.serializeIntLE(fileSize, outbuf, offset);
        offset = IOUtil.serializeString(fileName, outbuf, offset);
        outbuf[offset++] = 0;
        offset = IOUtil.serializeString(urn.getAsString(), outbuf, offset);
        outbuf[offset++] = 0;
        return offset;
    }

    public int deserialize(byte[] inbuf, int offset) {
        fileIndex = IOUtil.deserializeIntLE(inbuf, offset);
        offset += 4;
        fileSize = IOUtil.deserializeIntLE(inbuf, offset);
        offset += 4;
        int firstTerminatorIdx = offset;
        while (inbuf[firstTerminatorIdx] != (byte) 0) {
            firstTerminatorIdx++;
        }
        fileName = new String(inbuf, offset, firstTerminatorIdx - offset);
        int secondTerminatorIdx = firstTerminatorIdx + 1;
        while (inbuf[secondTerminatorIdx] != (byte) 0) {
            secondTerminatorIdx++;
        }
        byte[] extensionArea = new byte[secondTerminatorIdx - firstTerminatorIdx - 1];
        System.arraycopy(inbuf, firstTerminatorIdx + 1, extensionArea, 0, secondTerminatorIdx - firstTerminatorIdx - 1);
        parseExtensionArea(extensionArea);
        offset = secondTerminatorIdx + 1;
        return offset;
    }

    private void parseExtensionArea(byte[] extensionArea) {
        try {
            PushbackInputStream inStream = new PushbackInputStream(new ByteArrayInputStream(extensionArea));
            byte b;
            StringBuffer buffer = new StringBuffer();
            GGEPBlock[] ggepBlocks = null;
            while (true) {
                b = (byte) inStream.read();
                if (b == -1) {
                    evaluateExtensionToken(buffer.toString());
                    break;
                } else if (b == GGEPBlock.MAGIC_NUMBER && buffer.length() == 0) {
                    inStream.unread(b);
                    try {
                        ggepBlocks = GGEPBlock.parseGGEPBlocks(inStream);
                    } catch (InvalidGGEPBlockException exp) {
                        Logger.logError(exp);
                    }
                    continue;
                } else if (b == 0x1c) {
                    evaluateExtensionToken(buffer.toString());
                    buffer.setLength(0);
                    continue;
                }
                buffer.append((char) b);
            }
            if (ggepBlocks != null) {
                alternateLocations = GGEPExtension.parseAltExtensionData(ggepBlocks);
            }
        } catch (IOException exp) {
            Logger.logError(exp);
        }
    }

    /**
     * Evaluates the extension tokens except GGEP extensions.
     * @param extension
     */
    private void evaluateExtensionToken(String extension) {
        if (URN.isValidURN(extension)) {
            urn = new URN(extension);
        } else {
            if (metaData == null || metaData.length() == 0) {
                metaData = parseMetaData(extension);
            }
        }
    }

    public String toString() {
        return "[" + "FileIndex=" + fileIndex + ", " + "FileSize=" + fileSize + ", " + "Filename=" + fileName + "]";
    }

    private String parseMetaData(String metaData) {
        StringTokenizer tokenizer = new StringTokenizer(metaData);
        if (tokenizer.countTokens() < 2) {
            return "";
        }
        String first = tokenizer.nextToken();
        String second = tokenizer.nextToken();
        String length = "";
        String frequency = "";
        String bitrate = "";
        boolean isVBR = false;
        boolean bearShare1 = false;
        boolean bearShare2 = false;
        boolean gnotella = false;
        if (second.toLowerCase().startsWith("kbps")) {
            bearShare1 = true;
            if (second.indexOf("VBR") > 0) {
                isVBR = true;
            }
        } else if (first.toLowerCase().endsWith("kbps")) {
            bearShare2 = true;
        }
        if (bearShare1) {
            bitrate = first;
        } else if (bearShare2) {
            int j = first.toLowerCase().indexOf("kbps");
            bitrate = first.substring(0, j);
        }
        if (bearShare1 || bearShare2) {
            String prev = "";
            String token = "";
            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
                if (token.startsWith("kHz")) {
                    frequency = prev;
                }
                prev = token;
            }
            length = token;
        } else if (metaData.endsWith("kHz")) {
            gnotella = true;
            length = first;
            int i = second.indexOf("kbps");
            if (i > -1) {
                bitrate = second.substring(0, i);
            } else {
                gnotella = false;
            }
        }
        if (bearShare1 || bearShare2 || gnotella) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(bitrate);
            buffer.append("Kbps");
            if (isVBR) {
                buffer.append("(VBR)");
            }
            buffer.append(" - ");
            if (frequency != null && frequency.length() > 0) {
                buffer.append(frequency);
                buffer.append("kHz");
                buffer.append(" - ");
            }
            buffer.append(length);
            return buffer.toString();
        }
        return "";
    }
}
