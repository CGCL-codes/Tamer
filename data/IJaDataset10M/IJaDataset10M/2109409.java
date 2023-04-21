package jaxlib.sql;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * A reader which closes a given <tt>ResultSet</tt> when the end of the stream has been reached.
 * <p>
 * The <tt>ResultSet</tt> gets also closed if the stream gets closed. The stream is closed if the end of
 * the stream has been reached and releases the reference to the underlying <tt>ResultSet</tt> instance.
 * A <tt>ResultCellReader</tt> can optionally also close the {@link Statement} and the 
 * {@link Connection} of the <tt>ResultSet</tt>.
 * </p><p>
 * Exceptions of type {@link SQLException} thrown by the <tt>ResultSet</tt> are rethrown by the stream
 * as cause of an {@link IOException}.
 * </p><p>
 * Instances of this class are not safe to be used by multiple threads concurrently.
 * </p>
 *
 * @see ResultSet#close()
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: ResultCellReader.java 2730 2009-04-21 01:12:29Z joerg_wassmer $
 */
public class ResultCellReader extends Reader {

    private Reader in;

    private ResultSet resultSet;

    private final boolean closeStatement;

    private final boolean closeConnection;

    /**
   * Constructs a new <tt>ResultCellReader</tt> which closes the specified stream and resultset
   * if the end of the stream has been reached or the reader itself gets closed.
   *
   * @param in              the stream to delegate to.
   * @param resultSet       the resultset to close.
   *
   * @throws NullPointerException if <code>(in == null) || (resultSet == null)</code>.
   *
   * @since JaXLib 1.0
   */
    public ResultCellReader(Reader in, ResultSet resultSet) {
        this(in, resultSet, false, false);
    }

    /**
   * Constructs a new <tt>ResultCellReader</tt> which closes the specified stream and resultset
   * if the end of the stream has been reached or the reader itself gets closed.
   *
   * @param in              the stream to delegate to.
   * @param resultSet       the resultset to close.
   * @param closeStatement  if <tt>true</tt> then the resultsets' statement will also be closed.
   * @param closeConnection if <tt>true</tt> then the resultsets' connection will also be closed.
   *
   * @throws NullPointerException if <code>(in == null) || (resultSet == null)</code>.
   *
   * @since JaXLib 1.0
   */
    public ResultCellReader(Reader in, ResultSet resultSet, boolean closeStatement, boolean closeConnection) {
        super();
        if (in == null) throw new NullPointerException("in");
        if (resultSet == null) throw new NullPointerException("resultSet");
        this.in = null;
        this.resultSet = resultSet;
        this.closeStatement = closeStatement;
        this.closeConnection = closeConnection;
    }

    private static IOException asIOException(SQLException ex) {
        return (IOException) new IOException().initCause(ex);
    }

    private Reader ensureOpen() throws ClosedChannelException {
        Reader in = this.in;
        if (in == null) throw new ClosedChannelException();
        return in;
    }

    @Override
    public void close() throws IOException {
        Reader in = this.in;
        if (in != null) {
            ResultSet rs = this.resultSet;
            this.in = null;
            this.resultSet = null;
            IOException ex = null;
            try {
                in.close();
            } catch (final IOException sex) {
                ex = sex;
            }
            if (rs != null) {
                Statement st = null;
                Connection c = null;
                try {
                    st = rs.getStatement();
                    c = st.getConnection();
                    rs.close();
                } catch (final SQLException sex) {
                    if (ex == null) ex = asIOException(sex);
                }
                if ((st != null) && this.closeStatement) {
                    try {
                        st.close();
                    } catch (final SQLException sex) {
                        if (ex == null) ex = asIOException(sex);
                    }
                }
                if ((c != null) && this.closeConnection) {
                    try {
                        c.close();
                    } catch (final SQLException sex) {
                        if (ex == null) ex = asIOException(sex);
                    }
                }
            }
            if (ex != null) throw ex;
        }
    }

    @Override
    @SuppressWarnings("finally")
    public void mark(int readAheadLimit) throws IOException {
        Reader in = ensureOpen();
        try {
            in.mark(readAheadLimit);
        } catch (final IOException ex) {
            try {
                close();
            } finally {
                throw ex;
            }
        }
    }

    @Override
    public boolean markSupported() {
        return (this.in != null) && this.in.markSupported();
    }

    @Override
    public int read() throws IOException {
        int b = -1;
        IOException ex = null;
        Reader in = ensureOpen();
        try {
            b = in.read();
        } catch (final IOException sex) {
            ex = sex;
        } finally {
            if (b < 0) {
                try {
                    close();
                } catch (final IOException sex) {
                    if (ex == null) ex = sex;
                }
            }
        }
        if (ex != null) throw ex;
        return b;
    }

    @Override
    public int read(char[] buf, int offs, int len) throws IOException {
        int count = -1;
        IOException ex = null;
        Reader in = ensureOpen();
        try {
            count = in.read(buf, offs, len);
        } catch (final IOException sex) {
            ex = sex;
        } finally {
            if (count < 0) {
                try {
                    close();
                } catch (final IOException sex) {
                    if (ex == null) ex = sex;
                }
            }
        }
        if (ex != null) throw ex;
        return count;
    }

    @Override
    public int read(CharBuffer buf) throws IOException {
        int count = -1;
        IOException ex = null;
        Reader in = ensureOpen();
        try {
            count = in.read(buf);
        } catch (final IOException sex) {
            ex = sex;
        } finally {
            if (count < 0) {
                try {
                    close();
                } catch (final IOException sex) {
                    if (ex == null) ex = sex;
                }
            }
        }
        if (ex != null) throw ex;
        return count;
    }

    @Override
    @SuppressWarnings("finally")
    public boolean ready() throws IOException {
        Reader in = ensureOpen();
        try {
            return in.ready();
        } catch (final IOException ex) {
            try {
                close();
            } finally {
                throw ex;
            }
        }
    }

    @Override
    @SuppressWarnings("finally")
    public void reset() throws IOException {
        Reader in = ensureOpen();
        try {
            in.reset();
        } catch (final IOException ex) {
            try {
                close();
            } finally {
                throw ex;
            }
        }
    }

    @Override
    @SuppressWarnings("finally")
    public long skip(long count) throws IOException {
        Reader in = ensureOpen();
        if (count <= 0) return 0;
        long skipped;
        try {
            skipped = in.skip(count);
        } catch (final IOException ex) {
            try {
                close();
            } finally {
                throw ex;
            }
        }
        if (skipped <= 0) close();
        return skipped;
    }
}
