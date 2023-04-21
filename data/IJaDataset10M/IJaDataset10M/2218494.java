package org.tritonus.lowlevel.pvorbis;

import org.tritonus.lowlevel.pogg.Buffer;
import org.tritonus.share.TDebug;

/** Holds the static part of a codebook.
 */
@SuppressWarnings("unused")
public class StaticCodebook implements VorbisConstants {

    private int m_nDimensions;

    private int m_nEntries;

    private int[] m_anLengths;

    private int m_nMaptype;

    private int m_nQMin;

    private int m_nQDelta;

    private int m_nQQuant;

    private int m_nQSequencep;

    private int[] m_anQuants;

    public StaticCodebook() {
        if (TDebug.TraceVorbisNative) {
            TDebug.out("StaticCodebook.<init>(): begin");
        }
        if (TDebug.TraceVorbisNative) {
            TDebug.out("StaticCodebook.<init>(): end");
        }
    }

    /**
	 */
    public void clear() {
        m_nDimensions = 0;
        m_nEntries = 0;
        m_anLengths = null;
        m_nMaptype = 0;
        m_nQMin = 0;
        m_nQDelta = 0;
        m_nQQuant = 0;
        m_nQSequencep = 0;
        m_anQuants = null;
    }

    /** Initializes the comment object.
		Sets the vendor string to null and 
		removes all comments.
	 */
    public void init() {
    }

    public int pack(Buffer buffer) {
        return 0;
    }

    public int unpack(Buffer buffer) {
        int nMagic = buffer.read(24);
        if (nMagic != 0x564342) {
            clear();
            return -1;
        }
        m_nDimensions = buffer.read(16);
        m_nEntries = buffer.read(24);
        if (m_nEntries == -1) {
            clear();
            return -1;
        }
        m_anLengths = new int[m_nEntries];
        if (!buffer.readFlag()) {
            if (buffer.readFlag()) {
                for (int i = 0; i < m_nEntries; i++) {
                    if (buffer.readFlag()) {
                        int nLength = buffer.read(5);
                        if (nLength == -1) {
                            clear();
                            return -1;
                        }
                        m_anLengths[i] = nLength + 1;
                    } else {
                        m_anLengths[i] = 0;
                    }
                }
            } else {
                for (int i = 0; i < m_nEntries; i++) {
                    int nLength = buffer.read(5);
                    if (nLength == -1) {
                        clear();
                        return -1;
                    }
                    m_anLengths[i] = nLength + 1;
                }
            }
        } else {
            int nLength = buffer.read(5) + 1;
            for (int i = 0; i < m_nEntries; ) {
                int nCount = buffer.read(ilog(m_nEntries - i));
                if (nCount == -1) {
                    clear();
                    return -1;
                }
                for (int j = 0; j < nCount; j++, i++) {
                    m_anLengths[i] = nLength;
                }
                nLength++;
            }
        }
        m_nMaptype = buffer.read(4);
        if (m_nMaptype == 1 || m_nMaptype == 2) {
            m_nQMin = buffer.read(32);
            m_nQDelta = buffer.read(32);
            m_nQQuant = buffer.read(4) + 1;
            m_nQSequencep = buffer.read(1);
            int nQuantVals = 0;
            if (m_nMaptype == 1) {
                nQuantVals = calculateMaptype1Quantvals();
            } else if (m_nMaptype == 2) {
                nQuantVals = m_nEntries * m_nDimensions;
            }
            m_anQuants = new int[nQuantVals];
            for (int i = 0; i < nQuantVals; i++) {
                m_anQuants[i] = buffer.read(m_nQQuant);
            }
            if (nQuantVals != 0 && m_anQuants[nQuantVals - 1] == -1) {
                clear();
                return -1;
            }
        } else if (m_nMaptype > 2) {
            clear();
            return -1;
        }
        return 0;
    }

    /** Calculate the number of quants in a maptype 1 codebook.
	 */
    private int calculateMaptype1Quantvals() {
        int vals = (int) Math.floor(Math.pow(m_nEntries, 1.0f / m_nDimensions));
        while (true) {
            int acc = 1;
            int acc1 = 1;
            int i;
            for (i = 0; i < m_nDimensions; i++) {
                acc *= vals;
                acc1 *= vals + 1;
            }
            if (acc <= m_nEntries && acc1 > m_nEntries) {
                return vals;
            } else {
                if (acc > m_nEntries) {
                    vals--;
                } else {
                    vals++;
                }
            }
        }
    }

    private static int ilog(int v) {
        int ret = 0;
        while (v != 0) {
            ret++;
            v >>= 1;
        }
        return ret;
    }
}
