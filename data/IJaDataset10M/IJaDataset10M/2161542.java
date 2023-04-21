package net.java.sip.communicator.impl.media.codec.video.h264;

import java.awt.*;
import java.util.*;
import javax.media.*;
import javax.media.format.*;
import net.java.sip.communicator.impl.media.codec.*;
import net.sf.fmj.media.*;

/**
 * Packets supplied data and encapsulates it in RTP in accord with RFC3984.
 * 
 * @author Damian Minkov
 * @author Lubomir Marinov
 */
public class Packetizer extends AbstractPacketizer {

    private static final String PLUGIN_NAME = "H264 Packetizer";

    private static int DEF_WIDTH = 352;

    private static int DEF_HEIGHT = 288;

    private static final int MAX_PAYLOAD_SIZE = 512;

    private static final Format[] defOutputFormats = { new VideoFormat(Constants.H264_RTP) };

    private Vector<byte[]> nals = new Vector<byte[]>();

    private long lastTimeStamp = 0;

    private int seq = 0;

    /**
     * Constructor
     */
    public Packetizer() {
        DEF_WIDTH = Constants.VIDEO_WIDTH;
        DEF_HEIGHT = Constants.VIDEO_HEIGHT;
        inputFormats = new Format[] { new VideoFormat(Constants.H264) };
        inputFormat = null;
        outputFormat = null;
    }

    private Format[] getMatchingOutputFormats(Format in) {
        VideoFormat videoIn = (VideoFormat) in;
        Dimension inSize = videoIn.getSize();
        return new VideoFormat[] { new VideoFormat(Constants.H264_RTP, inSize, Format.NOT_SPECIFIED, Format.byteArray, videoIn.getFrameRate()) };
    }

    /**
     * Return the list of formats supported at the output.
     */
    public Format[] getSupportedOutputFormats(Format in) {
        if (in == null) return defOutputFormats;
        if (!(in instanceof VideoFormat) || null == JNIDecoder.matches(in, inputFormats)) return new Format[0];
        return getMatchingOutputFormats(in);
    }

    public Format setInputFormat(Format in) {
        if (!(in instanceof VideoFormat) || null == JNIDecoder.matches(in, inputFormats)) return null;
        inputFormat = in;
        return in;
    }

    public Format setOutputFormat(Format out) {
        if (!(out instanceof VideoFormat) || null == JNIDecoder.matches(out, getMatchingOutputFormats(inputFormat))) return null;
        VideoFormat videoOut = (VideoFormat) out;
        Dimension outSize = videoOut.getSize();
        if (outSize == null) {
            Dimension inSize = ((VideoFormat) inputFormat).getSize();
            if (inSize == null) outSize = new Dimension(DEF_WIDTH, DEF_HEIGHT); else outSize = inSize;
        }
        outputFormat = new VideoFormat(videoOut.getEncoding(), outSize, outSize.width * outSize.height, Format.byteArray, videoOut.getFrameRate());
        return outputFormat;
    }

    public int process(Buffer inBuffer, Buffer outBuffer) {
        if (nals.size() > 0) {
            byte[] buf = nals.remove(0);
            if (buf.length > MAX_PAYLOAD_SIZE) {
                int bufOfset = 0;
                int size = buf.length;
                int nri = buf[bufOfset] & 0x60;
                byte[] tmp = new byte[MAX_PAYLOAD_SIZE];
                tmp[0] = 28;
                tmp[0] |= nri;
                tmp[1] = buf[bufOfset];
                tmp[1] |= 1 << 7;
                tmp[1] &= ~(1 << 6);
                bufOfset += 1;
                size -= 1;
                int currentSIx = 0;
                while (size + 2 > MAX_PAYLOAD_SIZE) {
                    System.arraycopy(buf, bufOfset, tmp, 2, MAX_PAYLOAD_SIZE - 2);
                    nals.add(currentSIx++, tmp.clone());
                    bufOfset += MAX_PAYLOAD_SIZE - 2;
                    size -= MAX_PAYLOAD_SIZE - 2;
                    tmp[1] &= ~(1 << 7);
                }
                byte[] tmp2 = new byte[size + 2];
                tmp2[0] = tmp[0];
                tmp2[1] = tmp[1];
                tmp2[1] |= 1 << 6;
                System.arraycopy(buf, bufOfset, tmp2, 2, size);
                nals.add(currentSIx++, tmp2);
                return INPUT_BUFFER_NOT_CONSUMED | OUTPUT_BUFFER_NOT_FILLED;
            }
            outBuffer.setData(buf);
            outBuffer.setLength(buf.length);
            outBuffer.setOffset(0);
            outBuffer.setTimeStamp(lastTimeStamp);
            outBuffer.setSequenceNumber(seq++);
            if (nals.size() > 0) {
                return BUFFER_PROCESSED_OK | INPUT_BUFFER_NOT_CONSUMED;
            } else {
                outBuffer.setFlags(outBuffer.getFlags() | Buffer.FLAG_RTP_MARKER);
                return BUFFER_PROCESSED_OK;
            }
        }
        if (isEOM(inBuffer)) {
            propagateEOM(outBuffer);
            reset();
            return BUFFER_PROCESSED_OK;
        }
        if (inBuffer.isDiscard()) {
            outBuffer.setDiscard(true);
            reset();
            return BUFFER_PROCESSED_OK;
        }
        Format inFormat = inBuffer.getFormat();
        if (inFormat != inputFormat && !inFormat.matches(inputFormat)) {
            setInputFormat(inFormat);
        }
        int inputLength = inBuffer.getLength();
        if (inputLength < 10) {
            outBuffer.setDiscard(true);
            reset();
            return BUFFER_PROCESSED_OK;
        }
        byte[] r = (byte[]) inBuffer.getData();
        int inputOffset = inBuffer.getOffset();
        int ix = 3 + inputOffset;
        int prevIx = 1 + inputOffset;
        while ((ix = ff_avc_find_startcode(r, ix, r.length)) < r.length) {
            int len = ix - prevIx;
            byte[] b = new byte[len - 4];
            System.arraycopy(r, prevIx + 3, b, 0, b.length);
            nals.add(b);
            prevIx = ix;
            ix = prevIx + 3;
        }
        int len = ix - prevIx;
        if (len < 0) {
            outBuffer.setDiscard(true);
            return BUFFER_PROCESSED_OK;
        }
        byte[] b = new byte[len - 3];
        System.arraycopy(r, prevIx + 3, b, 0, b.length);
        nals.add(b);
        lastTimeStamp = inBuffer.getTimeStamp();
        return INPUT_BUFFER_NOT_CONSUMED | OUTPUT_BUFFER_NOT_FILLED;
    }

    /**
     * Find a NAL in the encoded data.
     * @param buff the encoded data
     * @param startIx starting index
     * @param endIx end index
     * @return starting position of the NAL
     */
    private int ff_avc_find_startcode(byte[] buff, int startIx, int endIx) {
        while (startIx < (endIx - 3)) {
            if (buff[startIx] == 0 && buff[startIx + 1] == 0 && buff[startIx + 2] == 1) return startIx;
            startIx++;
        }
        return endIx;
    }

    public synchronized void open() throws ResourceUnavailableException {
        if (!opened) {
            super.open();
            opened = true;
        }
    }

    public synchronized void close() {
        if (opened) {
            opened = false;
            super.close();
            if (nals != null) nals.clear();
        }
    }

    public String getName() {
        return PLUGIN_NAME;
    }
}
