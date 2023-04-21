package net.sourceforge.ws_jdbc.shared;

public class Blob implements java.sql.Blob {

    protected byte[] theData;

    public Blob() {
    }

    public Blob(byte[] newData) {
        theData = newData;
    }

    /*****************************************************
 **********                                 **********
 ********** Beginning of API implementation **********
 **********                                 **********
 *****************************************************/
    public java.io.InputStream getBinaryStream() throws java.sql.SQLException {
        return new java.io.ByteArrayInputStream(theData);
    }

    public byte[] getBytes(long pos, int length) throws java.sql.SQLException {
        byte[] res = null;
        if (pos < 0 || pos > theData.length) throw new java.sql.SQLException("pos is out of bounds"); else if (length <= 0) throw new java.sql.SQLException("length must be greater than 0");
        int maxLen = theData.length - ((int) pos - 1);
        if (length > maxLen) length = maxLen;
        res = new byte[length];
        System.arraycopy(theData, (int) pos, res, 0, length);
        return res;
    }

    public long length() throws java.sql.SQLException {
        return theData.length;
    }

    public long position(java.sql.Blob pattern, long start) throws java.sql.SQLException {
        if (pattern == null) throw new java.sql.SQLException("pattern must be set");
        long length = pattern.length();
        return position(pattern.getBytes(0, (int) length), start);
    }

    public long position(byte[] pattern, long start) throws java.sql.SQLException {
        int maxIndex = theData.length - pattern.length;
        long res = -1;
        boolean found = false;
        if (pattern == null) throw new java.sql.SQLException("pattern must be set"); else if (start < 1 || start > theData.length) throw new java.sql.SQLException("start is out of bounds");
        for (int i = (int) start - 1; i < maxIndex && !found; i++) {
            if (pattern[0] == theData[i]) {
                found = true;
                for (int j = 0; j < pattern.length && !found; j++) if (pattern[j] != theData[i + j]) found = false;
                if (found) res = i;
            }
        }
        return res;
    }

    public java.io.OutputStream setBinaryStream(final long pos) throws java.sql.SQLException {
        if (pos < 0 || pos > theData.length) throw new java.sql.SQLException("pos is out of bounds");
        return new java.io.ByteArrayOutputStream() {

            {
                write(theData, 0, (int) pos - 1);
            }

            public void flush() throws java.io.IOException {
                byte[] newData = toByteArray();
                if (newData.length < theData.length) System.arraycopy(newData, 0, theData, 0, newData.length); else theData = newData;
            }

            public void close() throws java.io.IOException {
                flush();
            }
        };
    }

    public int setBytes(long pos, byte[] bytes) throws java.sql.SQLException {
        int len = 0;
        if (bytes != null) len = bytes.length;
        return setBytes(pos, bytes, 0, len);
    }

    public int setBytes(long pos, byte[] bytes, int offset, int len) throws java.sql.SQLException {
        java.io.OutputStream outputStream = setBinaryStream(pos);
        try {
            outputStream.write(bytes, offset, len);
            outputStream.close();
        } catch (java.io.IOException e) {
            throw new java.sql.SQLException("Write error: " + e.getMessage());
        }
        return len;
    }

    public void truncate(long len) throws java.sql.SQLException {
        if (len < 0 || len > theData.length) throw new java.sql.SQLException("len is out of bounds");
        int length = (int) len;
        byte[] newData = new byte[length];
        System.arraycopy(theData, 0, newData, 0, length);
        theData = newData;
    }
}
