package com.streamsicle.fluid;

import java.io.*;
import java.util.*;
import org.apache.log4j.Category;

/**
 * A helper class for reading individual frames from an
 * MP3 input stream.
 * <P>
 * For each frame the header is first read to determine
 * the framesize as well as the delay. Other information
 * such as the type, layer, bitrate and sampling frequency
 * is extracted in the process as well.
 *
 * @author Lars Samuelsson
 * @author Matt Hall
 * @author John Watkinson
 */
public class MP3FrameReader {

    static Category log = Category.getInstance(MP3FrameReader.class);

    /**
     * MPEG 1 type
     */
    public static final int MPEG1 = 3;

    /**
     * MPEG 2 type
     */
    public static final int MPEG2 = 2;

    /**
     * MPEG 2.5 type
     */
    public static final int MPEG25 = 0;

    /**
     * Layer I type
     */
    public static final int LAYERI = 3;

    /**
     * Layer II type
     */
    public static final int LAYERII = 2;

    /**
     * Layer III type
     */
    public static final int LAYERIII = 1;

    private int[][] bitrates = { { -1, 0, 0, 0, -1, 0, 0, 0 }, { -1, 8, 8, 32, -1, 32, 32, 32 }, { -1, 16, 16, 48, -1, 40, 48, 64 }, { -1, 24, 24, 56, -1, 48, 56, 96 }, { -1, 32, 32, 64, -1, 56, 64, 128 }, { -1, 64, 64, 80, -1, 64, 80, 160 }, { -1, 80, 80, 96, -1, 80, 96, 192 }, { -1, 56, 56, 112, -1, 96, 112, 224 }, { -1, 64, 64, 128, -1, 112, 128, 256 }, { -1, 80, 80, 144, -1, 128, 160, 288 }, { -1, 96, 96, 160, -1, 160, 192, 320 }, { -1, 112, 112, 176, -1, 192, 224, 352 }, { -1, 128, 128, 192, -1, 224, 256, 384 }, { -1, 144, 144, 224, -1, 256, 320, 416 }, { -1, 160, 160, 256, -1, 320, 384, 448 }, { -1, -1, -1, -1, -1, -1, -1, -1 } };

    private int prevBitrate = 0;

    private int frequencies[][] = { { 11025, -1, 22050, 44100 }, { 12000, -1, 24000, 48000 }, { 8000, -1, 16000, 32000 }, { -1, -1, -1, -1 } };

    private int prevFrequency = 0;

    private final int headerlength = 4;

    private byte[] header;

    private final int framesync = 2047;

    private int framesize, bitrate, frequency, type, layer;

    private Delay delay;

    private PushbackInputStream mp3input;

    /**
     * Creates a frame reader on the specified input stream.
     *
     * @param mp3input An input stream containing MP3 data
     */
    public MP3FrameReader(InputStream mp3input) {
        this.mp3input = new PushbackInputStream(mp3input, 10);
        readId3Tag();
    }

    private static final byte[] ID3_IDENTIFIER = { (byte) 'I', (byte) 'D', (byte) '3' };

    /**
     * reads an ID3v2 tag header if it exists, and reads the whole tag
     * to get it out of the stream.
     * see http://www.id3.org/id3v2.4.0-structure.txt or
     * http://www.id3.org/id3v2.3.0.txt for details on the format
     */
    private void readId3Tag() {
        try {
            byte[] tagHeader = new byte[10];
            mp3input.read(tagHeader);
            byte[] identifier = new byte[3];
            System.arraycopy(tagHeader, 0, identifier, 0, 3);
            if ((identifier[0] == ID3_IDENTIFIER[0]) && (identifier[1] == ID3_IDENTIFIER[1]) && (identifier[2] == ID3_IDENTIFIER[2])) {
                int tagSize = (tagHeader[6] & 0x7F) << 21;
                tagSize = tagSize | ((tagHeader[7] & 0x7F) << 14);
                tagSize = tagSize | ((tagHeader[8] & 0x7F) << 7);
                tagSize = tagSize | (tagHeader[9] & 0x7F);
                byte[] tag = new byte[tagSize];
                mp3input.read(tag);
            } else {
                mp3input.unread(tagHeader);
            }
        } catch (java.io.IOException ioe) {
            System.err.println("Exception while reading ID3 tag");
            ioe.printStackTrace();
        }
    }

    /**
     * Reads the next frame from the input stream until
     * the file ends or garbled data is found.
     *
     * The tag is considered garbled data as it has
     * nothing to do in a continous stream of MP3 frames.
     *
     * @return An MP3 frame or null if the end of the
     *         file or a tag has been reached
     */
    public byte[] nextFrame() throws IOException {
        byte[] frame = null;
        scanForSynch();
        byte[] header = read(headerlength);
        try {
            if (decodeHeader(header)) {
                int datasize = framesize - header.length;
                frame = new byte[framesize];
                System.arraycopy(header, 0, frame, 0, header.length);
                if (mp3input.read(frame, header.length, datasize) == -1) {
                    log.error("read returned -1, returning null");
                    return null;
                }
            }
        } catch (Exception e) {
            log.error("garbled data, returning null");
            prevBitrate = 0;
            prevFrequency = 0;
            return null;
        }
        return frame;
    }

    public void scanForSynch() throws IOException {
        int h1 = 0, h2 = 0;
        while ((h1 != 0xFF) || ((h2 & 0xE0) != 0xE0)) {
            h1 = h2;
            h2 = mp3input.read();
            if (h2 == -1) {
                return;
            }
        }
        mp3input.unread(h2);
        mp3input.unread(h1);
    }

    /**
     * Reads the given number of bytes from the stream.
     *
     * @param len The number of bytes to be read
     * @return    The read bytes
     */
    public byte[] read(int len) throws IOException {
        byte[] bytes = new byte[len];
        mp3input.read(bytes);
        return bytes;
    }

    /**
     * Extracts type, layer, bitrate, frequency, framesize and
     * delay from the header.
     * <P>
     * <B>Header bits</B><BR>
     * <TABLE>
     * <TR><TD Align="Right"> 31 - 21 </TD>
     * <TD Align="Left"> frame sync (all set in a valid frame)</TD></TR>
     * <TR><TD Align="Right"> 20 - 19 </TD>
     * <TD Align="Left"> MPEG audio version </TD></TR>
     * <TR><TD Align="Right"> 18 - 17 </TD>
     * <TD Align="Left"> layer description </TD></TR>
     * <TR><TD Align="Right"> 16 </TD>
     * <TD Align="Left"> protection bit </TD></TR>
     * <TR><TD Align="Right"> 15 - 12 </TD>
     * <TD Align="Left"> bitrate index </TD></TR>
     * <TR><TD Align="Right"> 11 - 10 </TD>
     * <TD Align="Left"> sampling rate frequency index </TD></TR>
     * <TR><TD Align="Right"> 09 </TD>
     * <TD Align="Left"> padding bit </TD></TR>
     * <TR><TD Align="Right"> 08 </TD>
     * <TD Align="Left"> private bit </TD></TR>
     * <TR><TD Align="Right"> 07 - 06 </TD>
     * <TD Align="Left"> channel mode </TD></TR>
     * <TR><TD Align="Right"> 05 - 04 </TD>
     * <TD Align="Left"> mode extension </TD></TR>
     * <TR><TD Align="Right"> 03 </TD>
     * <TD Align="Left"> copyright bit </TD></TR>
     * <TR><TD Align="Right"> 02 </TD>
     * <TD Align="Left"> original indication bit </TD></TR>
     * <TR><TD Align="Right"> 01 - 00 </TD>
     * <TD Align="Left"> emphasis </TD></TR>
     * </TABLE>
     *
     * @param headerBytes A frame header to be decoded
     * @return            true if this header could be decoded
     */
    public boolean decodeHeader(byte[] headerBytes) {
        int x, y, pad;
        Bitmask header = new Bitmask(headerBytes);
        if (header.get(21, 31) != framesync) {
            log.debug("Framesync failure.");
            return false;
        }
        type = header.get(19, 20);
        layer = header.get(17, 18);
        x = ((type & 0x01) << 2) + layer;
        y = header.get(12, 15);
        if (bitrates[y][x] < 0) {
            log.debug("Bitrate failure.");
            if (prevBitrate == 0) {
                return false;
            }
            bitrate = prevBitrate;
        } else {
            prevBitrate = bitrate;
            bitrate = bitrates[y][x];
        }
        x = type;
        y = header.get(10, 11);
        if (frequencies[y][x] < 0) {
            log.debug("Frequency failure.");
            if (prevFrequency == 0) {
                return false;
            }
        } else {
            prevFrequency = frequency;
            frequency = frequencies[y][x];
        }
        pad = header.get(9);
        if (type == MPEG1) {
            if (layer == LAYERI) {
                framesize = 48 * (bitrate * 1000) / frequency + pad;
            } else {
                framesize = 144 * (bitrate * 1000) / frequency + pad;
            }
        } else {
            if (layer == LAYERI) {
                framesize = 24 * (bitrate * 1000) / frequency + pad;
            } else {
                framesize = 72 * (bitrate * 1000) / frequency + pad;
            }
        }
        delay = new Delay((double) framesize * 8 * 1000 / bitrate / 1000);
        return true;
    }

    /**
     * Fetches the bitrate based on the last header read
     *
     * @return The current bitrate
     */
    public int getBitrate() {
        return bitrate;
    }

    /**
     * Fetches the frequency based on the last header read
     *
     * @return The current frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Fetches the type based on the last header read
     *
     * @return The current type
     */
    public int getType() {
        return type;
    }

    /**
     * Fetches the layer based on the last header read
     *
     * @return The current layer
     */
    public int getLayer() {
        return layer;
    }

    /**
     * Fetches the delay based on the last header read
     *
     * @return The current delay
     */
    public Delay getDelay() {
        return delay;
    }

    /**
     * Fetches the frame size based on the last header read
     *
     * @return The current frame size
     */
    public int getFrameSize() {
        return framesize;
    }

    /**
     * Fetches the last read header as an array of bytes
     *
     * @return the last read header
     */
    public byte[] getHeader() {
        return header;
    }
}
