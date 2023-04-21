package com.dbxml.util;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * LoggingStream filters an OutputStream, generating a log-like format
 * for each line that is sent to the OutputStream.
 */
public class LoggingStream extends FilterOutputStream {

    private static final DateFormat IDF = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss] ", new DateFormatSymbols(Locale.US));

    private ByteArrayOutputStream bos;

    private int buffer = 128;

    private DateFormat df = IDF;

    public LoggingStream(OutputStream out) {
        super(out);
        bos = new ByteArrayOutputStream(buffer);
    }

    public LoggingStream(OutputStream out, DateFormat df) {
        this(out);
        this.df = df;
    }

    public LoggingStream(OutputStream out, int buffer) {
        super(out);
        this.buffer = buffer;
        bos = new ByteArrayOutputStream(buffer);
    }

    public LoggingStream(OutputStream out, int buffer, DateFormat df) {
        this(out, buffer);
        this.df = df;
    }

    public void setDateFormat(DateFormat df) {
        this.df = df;
    }

    public DateFormat getDateFormat() {
        return df;
    }

    public void write(byte[] buf) throws IOException {
        write(buf, 0, buf.length);
    }

    private boolean isWhitespace(byte[] buf) {
        for (int i = 0; i < buf.length; i++) {
            byte b = buf[i];
            switch(b) {
                case 8:
                case 10:
                case 12:
                case 13:
                case 32:
                    continue;
                default:
                    return false;
            }
        }
        return true;
    }

    private void flushBuffer() throws IOException {
        byte[] b = bos.toByteArray();
        if (!isWhitespace(b)) out.write(UTF8.toUTF8(df.format(new Date())));
        out.write(b);
        bos = new ByteArrayOutputStream(buffer);
    }

    public void write(int b) throws IOException {
        bos.write(b);
        if (b == '\n') flushBuffer();
    }

    public void write(byte[] buf, int off, int len) throws IOException {
        for (int i = off; i < len; i++) write(buf[i]);
    }

    public void close() throws IOException {
        flushBuffer();
        out.close();
    }
}
