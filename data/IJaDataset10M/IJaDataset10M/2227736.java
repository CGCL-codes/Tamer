package nl.utwente.ewi.hmi.deira.om.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * This class provides a way to treat multiple audiostreams as one.
 * It was obtained externally and is further undocumented (see the comments
 * at the top of the source code file for the source and license information).
 */
public class SequenceAudioInputStream extends AudioInputStream {

    private ArrayList<AudioInputStream> m_audioInputStreamList;

    private int m_nCurrentStream;

    public SequenceAudioInputStream(AudioFormat audioFormat, Collection<AudioInputStream> audioInputStreams) {
        super(new ByteArrayInputStream(new byte[0]), audioFormat, AudioSystem.NOT_SPECIFIED);
        m_audioInputStreamList = new ArrayList<AudioInputStream>(audioInputStreams);
        m_nCurrentStream = 0;
    }

    private AudioInputStream getCurrentStream() {
        return (AudioInputStream) m_audioInputStreamList.get(m_nCurrentStream);
    }

    private boolean advanceStream() {
        m_nCurrentStream++;
        boolean bAnotherStreamAvailable = (m_nCurrentStream < m_audioInputStreamList.size());
        return bAnotherStreamAvailable;
    }

    public long getFrameLength() {
        long lLengthInFrames = 0;
        Iterator<AudioInputStream> streamIterator = m_audioInputStreamList.iterator();
        while (streamIterator.hasNext()) {
            AudioInputStream stream = (AudioInputStream) streamIterator.next();
            long lLength = stream.getFrameLength();
            if (lLength == AudioSystem.NOT_SPECIFIED) {
                return AudioSystem.NOT_SPECIFIED;
            } else {
                lLengthInFrames += lLength;
            }
        }
        return lLengthInFrames;
    }

    public int read() throws IOException {
        AudioInputStream stream = getCurrentStream();
        int nByte = stream.read();
        if (nByte == -1) {
            boolean bAnotherStreamAvailable = advanceStream();
            if (bAnotherStreamAvailable) {
                return read();
            } else {
                return -1;
            }
        } else {
            return nByte;
        }
    }

    public int read(byte[] abData, int nOffset, int nLength) throws IOException {
        AudioInputStream stream = getCurrentStream();
        int nBytesRead = stream.read(abData, nOffset, nLength);
        if (nBytesRead == -1) {
            boolean bAnotherStreamAvailable = advanceStream();
            if (bAnotherStreamAvailable) {
                return read(abData, nOffset, nLength);
            } else {
                return -1;
            }
        } else {
            return nBytesRead;
        }
    }

    public long skip(long lLength) throws IOException {
        throw new IOException("skip() is not implemented in class SequenceInputStream");
    }

    public int available() throws IOException {
        return getCurrentStream().available();
    }

    public void close() throws IOException {
    }

    public void mark(int nReadLimit) {
        throw new RuntimeException("mark() is not implemented in class SequenceInputStream");
    }

    public void reset() throws IOException {
        throw new IOException("reset() is not implemented in class SequenceInputStream");
    }

    public boolean markSupported() {
        return false;
    }
}
