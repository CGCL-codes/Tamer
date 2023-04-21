package org.mobicents.media.server.impl.rtp.sdp;

import org.mobicents.media.server.spi.format.Format;
import org.mobicents.media.server.spi.format.Formats;

/**
 * Implements RTP formats collection with fast search.
 *
 * We assume that RTP formats collection varies slow.
 * @author kulikov
 */
public class RTPFormats {

    private static final int size = 10;

    private RTPFormat[] rtpFormats;

    private int len;

    private Formats formats = new Formats();

    private int cursor;

    /**
     * Creates new format collection with default size.
     */
    public RTPFormats() {
        this.rtpFormats = new RTPFormat[size];
    }

    /**
     * Creates new formats collection with specified size
     *
     * @param size the size of collection to be created.
     */
    public RTPFormats(int size) {
        this.rtpFormats = new RTPFormat[size];
    }

    public void add(RTPFormat rtpFormat) {
        if (len == rtpFormats.length) throw new IllegalStateException("Space limit has exceeded");
        rtpFormats[len++] = rtpFormat;
        formats.add(rtpFormat.getFormat());
    }

    public void add(RTPFormats fmts) {
        for (int i = 0; i < fmts.len; i++) {
            rtpFormats[len++] = fmts.rtpFormats[i];
            formats.add(fmts.rtpFormats[i].getFormat());
        }
    }

    public void remove(RTPFormat rtpFormat) {
        int pos = -1;
        for (int i = 0; i < len; i++) {
            pos++;
            if (rtpFormats[i].getID() == rtpFormat.getID()) break;
        }
        if (pos == -1) {
            throw new IllegalArgumentException("Unknown format " + rtpFormat);
        }
        System.arraycopy(rtpFormats, pos + 1, rtpFormats, pos, len - pos);
        len--;
        formats.remove(rtpFormat.getFormat());
    }

    public void clean() {
        for (int i = 0; i < size; i++) {
            rtpFormats[i] = null;
        }
        len = 0;
        cursor = 0;
    }

    public int size() {
        return len;
    }

    public RTPFormat getRTPFormat(int payload) {
        for (int i = 0; i < len; i++) {
            if (rtpFormats[i].getID() == payload) return rtpFormats[i];
        }
        return null;
    }

    public RTPFormat getRTPFormat(Format format) {
        for (int i = 0; i < len; i++) {
            if (rtpFormats[i].getFormat().matches(format)) return rtpFormats[i];
        }
        return null;
    }

    public RTPFormat[] toArray() {
        RTPFormat[] fmts = new RTPFormat[len];
        System.arraycopy(rtpFormats, 0, fmts, 0, len);
        return fmts;
    }

    public Formats getFormats() {
        return formats;
    }

    public RTPFormat find(int p) {
        for (int i = 0; i < size; i++) {
            if (rtpFormats[i] != null && rtpFormats[i].getID() == p) {
                return rtpFormats[i];
            }
        }
        return null;
    }

    public boolean contains(Format fmt) {
        for (int i = 0; i < size; i++) {
            if (rtpFormats[i] != null && rtpFormats[i].getFormat().matches(fmt)) {
                return true;
            }
        }
        return false;
    }

    public RTPFormat find(Format fmt) {
        for (int i = 0; i < size; i++) {
            if (rtpFormats[i] != null && rtpFormats[i].getFormat().matches(fmt)) {
                return rtpFormats[i];
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return len == 0;
    }

    public void rewind() {
        cursor = 0;
    }

    public boolean hasMore() {
        return cursor != len;
    }

    public RTPFormat next() {
        return rtpFormats[cursor++];
    }

    public void intersection(RTPFormats other, RTPFormats res) {
        for (int i = 0; i < this.len; i++) {
            for (int j = 0; j < other.len; j++) {
                if (this.rtpFormats[i].getFormat().matches(other.rtpFormats[j].getFormat())) {
                    res.add(this.rtpFormats[i]);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("RTPFormats{");
        for (int i = 0; i < this.len; i++) {
            buffer.append(rtpFormats[i]);
            if (i != len - 1) buffer.append(",");
        }
        buffer.append("}");
        return buffer.toString();
    }
}
