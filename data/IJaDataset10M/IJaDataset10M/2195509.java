package org.openexi.schema;

public final class IntBuffer {

    private static final int VALUES_INITIAL = 64;

    private static final int VALUES_INCREMENT = 64;

    private int[] m_buf;

    private int m_length;

    public IntBuffer() {
        m_buf = new int[VALUES_INITIAL];
        m_length = 0;
    }

    public IntBuffer(int initSize) {
        m_buf = new int[initSize];
        m_length = 0;
    }

    public final int length() {
        return m_length;
    }

    public final void append(IntBuffer intBuffer) {
        if (intBuffer != null) {
            while (m_length + intBuffer.m_length > m_buf.length) {
                int[] buf = new int[m_buf.length + VALUES_INCREMENT];
                System.arraycopy(m_buf, 0, buf, 0, m_length);
                m_buf = buf;
            }
            for (int i = 0; i < intBuffer.m_length; i++) {
                m_buf[m_length + i] = intBuffer.m_buf[i];
            }
            m_length += intBuffer.m_length;
        }
    }

    public final IntBuffer append(int intVal) {
        if (m_length == m_buf.length) {
            int[] buf = new int[m_buf.length + VALUES_INCREMENT];
            System.arraycopy(m_buf, 0, buf, 0, m_length);
            m_buf = buf;
        }
        m_buf[m_length] = intVal;
        ++m_length;
        return this;
    }

    public final void clear() {
        m_length = 0;
        for (int i = 0; i < m_buf.length; i++) m_buf[i] = 0;
    }

    public final int intAt(int i) {
        if (i >= m_length) throw new ArrayIndexOutOfBoundsException("Position " + i + " is equal or larger than " + m_length + ".");
        return m_buf[i];
    }

    public final void delete(int st, int limit) {
        if (st >= m_length) throw new ArrayIndexOutOfBoundsException("Position " + st + " is equal or larger than " + m_length + ".");
        if (limit > m_length) limit = m_length;
        for (int i = st, pos = limit; pos < m_length; m_buf[i] = m_buf[pos], i++, pos++) ;
        m_length -= limit - st;
    }

    public final int indexOf(int intVal) {
        for (int i = 0; i < m_length; i++) {
            if (m_buf[i] == intVal) return i;
        }
        return -1;
    }

    public int[] toIntArray() {
        int[] list = new int[m_length];
        System.arraycopy(m_buf, 0, list, 0, m_length);
        return list;
    }
}
