package sound;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import com.jcraft.jogg.*;
import com.jcraft.jorbis.*;

/**
 * Decompresses an Ogg file.
 * <p>
 * How to use:<br>
 * 1. Create OggInputStream passing in the input stream of the packed ogg file<br>
 * 2. Fetch format and sampling rate using getFormat() and getRate(). Use it to
 *    initalize the sound player.<br>
 * 3. Read the PCM data using one of the read functions, and feed it to your player.
 * <p>
 * OggInputStream provides a read(ByteBuffer, int, int) that can be used to read
 * data directly into a native buffer.
 */
public class OggInputStream extends FilterInputStream {

    /** The mono 16 bit format */
    public static final int FORMAT_MONO16 = 1;

    /** The stereo 16 bit format */
    public static final int FORMAT_STEREO16 = 2;

    private float[][][] _pcm = new float[1][][];

    private int[] _index;

    private boolean eos = false;

    private SyncState syncState = new SyncState();

    private StreamState streamState = new StreamState();

    private Page page = new Page();

    private Packet packet = new Packet();

    private Info info = new Info();

    private Comment comment = new Comment();

    private DspState dspState = new DspState();

    private Block block = new Block(dspState);

    private static int convsize = 4096 * 2;

    private static byte[] convbuffer = new byte[convsize];

    private int convbufferOff = 0;

    private int convbufferSize = 0;

    private byte readDummy[] = new byte[1];

    /**
	 * Creates an OggInputStream that decompressed the specified ogg file.
	 */
    public OggInputStream(InputStream input) {
        super(input);
        try {
            initVorbis();
            _index = new int[info.channels];
        } catch (Exception e) {
            e.printStackTrace();
            eos = true;
        }
    }

    /**
	 * Gets the format of the ogg file. Is either FORMAT_MONO16 or FORMAT_STEREO16
	 */
    public int getFormat() {
        if (info.channels == 1) {
            return FORMAT_MONO16;
        } else {
            return FORMAT_STEREO16;
        }
    }

    /**
	 * Gets the rate of the pcm audio.
	 */
    public int getRate() {
        return info.rate;
    }

    /**
	 * Reads the next byte of data from this input stream. The value byte is
	 * returned as an int in the range 0 to 255. If no byte is available because
	 * the end of the stream has been reached, the value -1 is returned. This
	 * method blocks until input data is available, the end of the stream is
	 * detected, or an exception is thrown. 
	 * @return the next byte of data, or -1 if the end of the stream is reached.
	 */
    public int read() throws IOException {
        int retVal = read(readDummy, 0, 1);
        return (retVal == -1 ? -1 : readDummy[0]);
    }

    /**
	 * Reads up to len bytes of data from the input stream into an array of bytes.
	 * @param b the buffer into which the data is read.
	 * @param off the start offset of the data.
	 * @param len the maximum number of bytes read.
	 * @return the total number of bytes read into the buffer, or -1 if there is
	 *         no more data because the end of the stream has been reached. 
	 */
    public int read(byte b[], int off, int len) throws IOException {
        if (eos) {
            return -1;
        }
        int bytesRead = 0;
        while (!eos && (len > 0)) {
            fillConvbuffer();
            if (!eos) {
                int bytesToCopy = Math.min(len, convbufferSize - convbufferOff);
                System.arraycopy(convbuffer, convbufferOff, b, off, bytesToCopy);
                convbufferOff += bytesToCopy;
                bytesRead += bytesToCopy;
                len -= bytesToCopy;
                off += bytesToCopy;
            }
        }
        return bytesRead;
    }

    /**
	 * Reads up to len bytes of data from the input stream into a ByteBuffer.
	 * @param b the buffer into which the data is read.
	 * @param off the start offset of the data.
	 * @param len the maximum number of bytes read.
	 * @return the total number of bytes read into the buffer, or -1 if there is
	 *         no more data because the end of the stream has been reached. 
	 */
    public int read(ByteBuffer b, int off, int len) throws IOException {
        if (eos) {
            return -1;
        }
        b.position(off);
        int bytesRead = 0;
        while (!eos && (len > 0)) {
            fillConvbuffer();
            if (!eos) {
                int bytesToCopy = Math.min(len, convbufferSize - convbufferOff);
                b.put(convbuffer, convbufferOff, bytesToCopy);
                convbufferOff += bytesToCopy;
                bytesRead += bytesToCopy;
                len -= bytesToCopy;
            }
        }
        return bytesRead;
    }

    /**
	 * Helper function. Decodes a packet to the convbuffer if it is empty. 
	 * Updates convbufferSize, convbufferOff, and eos.
	 */
    private void fillConvbuffer() throws IOException {
        if (convbufferOff >= convbufferSize) {
            convbufferSize = lazyDecodePacket();
            convbufferOff = 0;
            if (convbufferSize == -1) {
                eos = true;
            }
        }
    }

    /**
	 * Returns 0 after EOF is reached, otherwise always return 1.
	 * <p>
	 * Programs should not count on this method to return the actual number of
	 * bytes that could be read without blocking.
	 * @return 1 before EOF and 0 after EOF is reached. 
	 */
    public int available() throws IOException {
        return (eos ? 0 : 1);
    }

    /**
	 * OggInputStream does not support mark and reset. This function does nothing.
	 */
    public void reset() throws IOException {
    }

    /**
	 * OggInputStream does not support mark and reset.
	 * @return false.
	 */
    public boolean markSupported() {
        return false;
    }

    /**
	 * Skips over and discards n bytes of data from the input stream. The skip
	 * method may, for a variety of reasons, end up skipping over some smaller
	 * number of bytes, possibly 0. The actual number of bytes skipped is returned. 
	 * @param n the number of bytes to be skipped. 
	 * @return the actual number of bytes skipped.
	 */
    public long skip(long n) throws IOException {
        int bytesRead = 0;
        while (bytesRead < n) {
            int res = read();
            if (res == -1) {
                break;
            }
            bytesRead++;
        }
        return bytesRead;
    }

    /**
	 * Initalizes the vorbis stream. Reads the stream until info and comment are read.
	 */
    private void initVorbis() throws Exception {
        syncState.init();
        int index = syncState.buffer(4096);
        byte buffer[] = syncState.data;
        int bytes = in.read(buffer, index, 4096);
        syncState.wrote(bytes);
        if (syncState.pageout(page) != 1) {
            if (bytes < 4096) return;
            throw new Exception("Input does not appear to be an Ogg bitstream.");
        }
        streamState.init(page.serialno());
        info.init();
        comment.init();
        if (streamState.pagein(page) < 0) {
            throw new Exception("Error reading first page of Ogg bitstream data.");
        }
        if (streamState.packetout(packet) != 1) {
            throw new Exception("Error reading initial header packet.");
        }
        if (info.synthesis_headerin(comment, packet) < 0) {
            throw new Exception("This Ogg bitstream does not contain Vorbis audio data.");
        }
        int i = 0;
        while (i < 2) {
            while (i < 2) {
                int result = syncState.pageout(page);
                if (result == 0) break;
                if (result == 1) {
                    streamState.pagein(page);
                    while (i < 2) {
                        result = streamState.packetout(packet);
                        if (result == 0) {
                            break;
                        }
                        if (result == -1) {
                            throw new Exception("Corrupt secondary header. Exiting.");
                        }
                        info.synthesis_headerin(comment, packet);
                        i++;
                    }
                }
            }
            index = syncState.buffer(4096);
            buffer = syncState.data;
            bytes = in.read(buffer, index, 4096);
            if (bytes < 0) {
                bytes = 0;
            }
            if (bytes == 0 && i < 2) {
                throw new Exception("End of file before finding all Vorbis headers!");
            }
            syncState.wrote(bytes);
        }
        convsize = 4096 / info.channels;
        dspState.synthesis_init(info);
        block.init(dspState);
    }

    /**
	 * Decodes a packet.
	 */
    private int decodePacket(Packet packet) {
        final boolean bigEndian = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
        if (block.synthesis(packet) == 0) {
            dspState.synthesis_blockin(block);
        }
        int convOff = 0;
        int samples;
        while ((samples = dspState.synthesis_pcmout(_pcm, _index)) > 0) {
            float[][] pcm = _pcm[0];
            int bout = (samples < convsize ? samples : convsize);
            for (int i = 0; i < info.channels; i++) {
                int ptr = (i << 1) + convOff;
                int mono = _index[i];
                for (int j = 0; j < bout; j++) {
                    int val = (int) (pcm[i][mono + j] * 32767.);
                    val = Math.max(-32768, Math.min(32767, val));
                    val |= (val < 0 ? 0x8000 : 0);
                    convbuffer[ptr + 0] = (byte) (bigEndian ? val >>> 8 : val);
                    convbuffer[ptr + 1] = (byte) (bigEndian ? val : val >>> 8);
                    ptr += (info.channels) << 1;
                }
            }
            convOff += 2 * info.channels * bout;
            dspState.synthesis_read(bout);
        }
        return convOff;
    }

    /**
	 * Decodes the next packet.
	 * @return bytes read into convbuffer of -1 if end of file
	 */
    private int lazyDecodePacket() throws IOException {
        int result = getNextPacket(packet);
        if (result == -1) {
            return -1;
        }
        return decodePacket(packet);
    }

    /**
	 * @param packet where to put the packet.
	 */
    private int getNextPacket(Packet packet) throws IOException {
        boolean fetchedPacket = false;
        while (!eos && !fetchedPacket) {
            int result1 = streamState.packetout(packet);
            if (result1 == 0) {
                int result2 = 0;
                while (!eos && result2 == 0) {
                    result2 = syncState.pageout(page);
                    if (result2 == 0) {
                        fetchData();
                    }
                }
                if ((result2 == 0) && (page.eos() != 0)) {
                    return -1;
                }
                if (result2 == 0) {
                    fetchData();
                } else if (result2 == -1) {
                    System.out.println("syncState.pageout(page) result == -1");
                    return -1;
                } else {
                    int result3 = streamState.pagein(page);
                }
            } else if (result1 == -1) {
                System.out.println("streamState.packetout(packet) result == -1");
                return -1;
            } else {
                fetchedPacket = true;
            }
        }
        return 0;
    }

    /**
	 * Copys data from input stream to syncState.
	 */
    private void fetchData() throws IOException {
        if (!eos) {
            int index = syncState.buffer(4096);
            if (index < 0) {
                eos = true;
                return;
            }
            int bytes = in.read(syncState.data, index, 4096);
            syncState.wrote(bytes);
            if (bytes == 0) {
                eos = true;
            }
        }
    }

    /**
	 * Gets information on the ogg.
	 */
    public String toString() {
        String s = "";
        s = s + "version         " + info.version + "\n";
        s = s + "channels        " + info.channels + "\n";
        s = s + "rate (hz)       " + info.rate;
        return s;
    }

    /**
	 * Tests this class by decoding an ogg file to a byte buffer.
	 */
    public static void main(String args[]) {
        try {
            InputStream in = new Object().getClass().getResourceAsStream("/sound/background.ogg");
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream(1024 * 256);
            byteOut.reset();
            byte copyBuffer[] = new byte[1024 * 4];
            OggInputStream oggInput = new OggInputStream(in);
            boolean done = false;
            while (!done) {
                int bytesRead = oggInput.read(copyBuffer, 0, copyBuffer.length);
                byteOut.write(copyBuffer, 0, bytesRead);
                done = (bytesRead != copyBuffer.length || bytesRead < 0);
            }
            System.out.println(byteOut.size() + " bytes read");
            System.out.println(oggInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
