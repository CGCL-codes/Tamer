package org.tuotoo.crypto.tinytls;

public final class TLSHandshakeRecord extends AbstractTLSRecord {

    public static final int HEADER_LENGTH = 4;

    public TLSHandshakeRecord(byte[] data, int off) {
        m_Header = new byte[HEADER_LENGTH];
        System.arraycopy(data, off, m_Header, 0, 4);
        m_Type = m_Header[0];
        m_dataLen = ((m_Header[1] & 0xFF) << 16) | ((m_Header[2] & 0xFF) << 8) | (m_Header[3] & 0xFF);
        m_Data = new byte[m_dataLen];
        System.arraycopy(data, off + 4, m_Data, 0, m_dataLen);
    }

    public int getHeaderLength() {
        return HEADER_LENGTH;
    }
}
