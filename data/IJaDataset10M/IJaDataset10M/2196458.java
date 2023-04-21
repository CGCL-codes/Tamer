package org.hsqldb.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import org.hsqldb.HsqlException;
import org.hsqldb.SessionInterface;
import org.hsqldb.Trace;
import org.hsqldb.result.ResultLob;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

public class ClobDataRemoteClient implements ClobData {

    long id;

    final long length;

    SessionInterface session;

    boolean hasWriter;

    public ClobDataRemoteClient(long id, long length) {
        this.id = id;
        this.length = length;
    }

    public void free() {
    }

    public InputStream getAsciiStream() {
        return null;
    }

    public Reader getCharacterStream(long pos, long length) {
        return null;
    }

    public Reader getCharacterStream() {
        return null;
    }

    public char[] getChars(final long position, int length) throws HsqlException {
        if (!isInLimits(this.length, position, length)) {
            throw new IndexOutOfBoundsException();
        }
        ResultLob resultOut = ResultLob.newLobGetCharsRequest(id, position, length);
        ResultLob resultIn = (ResultLob) session.execute(resultOut);
        return resultIn.getCharArray();
    }

    public char[] getClonedChars() {
        return null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubString(long position, int length) throws HsqlException {
        return new String(getChars(position, length));
    }

    public long length() {
        return length;
    }

    public long position(String searchstr, long start) throws HsqlException {
        return 0L;
    }

    public long position(ClobData searchstr, long start) {
        return 0L;
    }

    public OutputStream setAsciiStream(long pos) {
        return null;
    }

    public Writer setCharacterStream(long pos) {
        return null;
    }

    public int setString(long pos, String str) throws HsqlException {
        throw Trace.error(Trace.OPERATION_NOT_SUPPORTED);
    }

    public int setString(long pos, String str, int offset, int len) throws HsqlException {
        throw Trace.error(Trace.OPERATION_NOT_SUPPORTED);
    }

    public int setChars(long pos, char[] chars, int offset, int len) throws HsqlException {
        throw Trace.error(Trace.OPERATION_NOT_SUPPORTED);
    }

    public void truncate(long len) throws HsqlException {
        throw Trace.error(Trace.OPERATION_NOT_SUPPORTED);
    }

    public int getStreamBlockSize() {
        return 256 * 1024;
    }

    public void setSession(SessionInterface session) {
        this.session = session;
    }

    public static ClobDataRemoteClient readClobDataClient(RowInputInterface in) throws HsqlException {
        try {
            long id = in.readLong();
            long length = in.readLong();
            return new ClobDataRemoteClient(id, length);
        } catch (IOException e) {
            throw Trace.error(Trace.TRANSFER_CORRUPTED);
        }
    }

    public void write(RowOutputInterface out) throws IOException, HsqlException {
        out.writeLong(id);
        out.writeLong(length);
    }

    static boolean isInLimits(long fullLength, long pos, long len) {
        return pos >= 0 && len >= 0 && pos + len <= fullLength;
    }

    public boolean isClosed() {
        return false;
    }

    void checkClosed() throws HsqlException {
        if (isClosed()) {
            throw Trace.error(Trace.BLOB_IS_NO_LONGER_VALID);
        }
    }
}
