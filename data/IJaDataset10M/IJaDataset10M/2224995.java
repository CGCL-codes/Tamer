package org.jtools.iofs.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.sql.SQLException;

abstract class AbstractDBHandlerBase {

    public byte[] getBinaryContent(DatabaseFile databaseFile) throws IOException, SQLException {
        return getBinaryContent(databaseFile, (Charset) null);
    }

    protected abstract Object getSQLContent(DatabaseFile databaseFile) throws IOException, SQLException;

    public byte[] getBinaryContent(DatabaseFile databaseFile, Charset charset) throws IOException, SQLException {
        Object content = getSQLContent(databaseFile);
        if (content == null) return null;
        if (content instanceof CharSequence) {
            if (charset == null) charset = Charset.defaultCharset();
            return ((CharSequence) content).toString().getBytes(charset);
        }
        return (byte[]) content;
    }

    public byte[] getBinaryContent(DatabaseFile databaseFile, String charsetName) throws IOException, SQLException {
        Charset charset = null;
        if (charsetName != null) charset = Charset.forName(charsetName);
        return getBinaryContent(databaseFile, charset);
    }

    public CharSequence getCharContent(DatabaseFile databaseFile) throws IOException, SQLException {
        return getCharContent(databaseFile, (Charset) null);
    }

    protected CharSequence getCharContent(DatabaseFile databaseFile, Charset charset) throws IOException, SQLException {
        Object content = getSQLContent(databaseFile);
        if (content == null) return null;
        if (content instanceof CharSequence) return (CharSequence) content;
        if (charset == null) charset = Charset.defaultCharset();
        return new String((byte[]) content, charset);
    }

    public CharSequence getCharContent(DatabaseFile databaseFile, String charsetName) throws IOException, SQLException {
        Charset charset = null;
        if (charsetName != null) charset = Charset.forName(charsetName);
        return getCharContent(databaseFile, charset);
    }

    abstract void setSQLContent(DatabaseFile databaseFile, Object content) throws IOException, SQLException;

    public InputStream openInputStream(DatabaseFile databaseFile) throws SQLException, IOException {
        return openInputStream(databaseFile, getBinaryContent(databaseFile));
    }

    public InputStream openInputStream(DatabaseFile databaseFile, Charset charset) throws IOException, SQLException {
        return openInputStream(databaseFile, getBinaryContent(databaseFile, charset));
    }

    private InputStream openInputStream(DatabaseFile databaseFile, byte[] content) throws IOException {
        if (content == null) throw new FileNotFoundException(databaseFile.toURI().toString());
        return new ByteArrayInputStream(content);
    }

    public InputStream openInputStream(DatabaseFile databaseFile, String charset) throws IOException, SQLException {
        return openInputStream(databaseFile, getBinaryContent(databaseFile, charset));
    }

    private static final class DBOutputStream extends ByteArrayOutputStream {

        private final DatabaseFile databaseFile;

        private final AbstractDBHandlerBase dbHandler;

        public DBOutputStream(AbstractDBHandlerBase dbHandler, DatabaseFile databaseFile) {
            this.dbHandler = dbHandler;
            this.databaseFile = databaseFile;
        }

        @Override
        public void flush() throws IOException {
            super.flush();
            try {
                dbHandler.setSQLContent(databaseFile, toByteArray());
            } catch (SQLException e) {
                throw new SQLIOException(e);
            }
        }
    }

    public OutputStream openOutputStream(DatabaseFile databaseFile) throws IOException {
        return new DBOutputStream(this, databaseFile);
    }

    public Reader openReader(DatabaseFile databaseFile) throws IOException, SQLException {
        return openReader(databaseFile, getCharContent(databaseFile));
    }

    public Reader openReader(DatabaseFile databaseFile, Charset charset) throws IOException, SQLException {
        return openReader(databaseFile, getCharContent(databaseFile, charset));
    }

    public Reader openReader(DatabaseFile databaseFile, String charset) throws IOException, SQLException {
        return openReader(databaseFile, getCharContent(databaseFile, charset));
    }

    private Reader openReader(DatabaseFile databaseFile, CharSequence content) throws IOException {
        if (content == null) throw new FileNotFoundException(databaseFile.toURI().toString());
        return new StringReader(content.toString());
    }

    private static final class DBWriter extends Writer {

        private final StringWriter delegate = new StringWriter();

        private final DatabaseFile databaseFile;

        private final AbstractDBHandlerBase dbHandler;

        public DBWriter(AbstractDBHandlerBase dbHandler, DatabaseFile databaseFile) {
            this.dbHandler = dbHandler;
            this.databaseFile = databaseFile;
        }

        @Override
        public void flush() throws IOException {
            delegate.flush();
            try {
                dbHandler.setSQLContent(databaseFile, delegate.getBuffer().toString());
            } catch (SQLException e) {
                throw new SQLIOException(e);
            }
        }

        @Override
        public StringWriter append(char c) {
            return delegate.append(c);
        }

        @Override
        public StringWriter append(CharSequence csq, int start, int end) {
            return delegate.append(csq, start, end);
        }

        @Override
        public StringWriter append(CharSequence csq) {
            return delegate.append(csq);
        }

        @Override
        public void close() throws IOException {
            delegate.close();
        }

        @Override
        public void write(char[] cbuf, int off, int len) {
            delegate.write(cbuf, off, len);
        }

        @Override
        public void write(char[] cbuf) throws IOException {
            delegate.write(cbuf);
        }

        @Override
        public void write(int c) {
            delegate.write(c);
        }

        @Override
        public void write(String str, int off, int len) {
            delegate.write(str, off, len);
        }

        @Override
        public void write(String str) {
            delegate.write(str);
        }
    }

    public Writer openWriter(DatabaseFile databaseFile) throws IOException {
        return openWriter(databaseFile, null);
    }

    public synchronized Writer openWriter(DatabaseFile databaseFile, String encoding) throws IOException {
        return new DBWriter(this, databaseFile);
    }

    public synchronized void setContent(DatabaseFile databaseFile, byte[] content) throws IOException {
        try {
            setSQLContent(databaseFile, content);
        } catch (SQLException e) {
            throw new SQLIOException(e);
        }
    }

    public synchronized void setContent(DatabaseFile databaseFile, CharSequence content) throws IOException {
        try {
            setSQLContent(databaseFile, content);
        } catch (SQLException e) {
            throw new SQLIOException(e);
        }
    }
}
