package com.jxva.util.zip;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipException;

public class ZipOutputStream extends DeflaterOutputStream implements ZipConstants {

    private String encoding = "UTF-8";

    private ZipEntry entry;

    private Vector<ZipEntry> entries = new Vector<ZipEntry>();

    private Hashtable<String, ZipEntry> names = new Hashtable<String, ZipEntry>();

    private CRC32 crc = new CRC32();

    private long written;

    private long locoff = 0;

    private String comment;

    private int method = DEFLATED;

    private boolean finished;

    private boolean closed = false;

    /**
     * Check to make sure that this stream has not been closed
     */
    private void ensureOpen() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
    }

    /**
     * Compression method for uncompressed (STORED) entries.
     */
    public static final int STORED = ZipEntry.STORED;

    /**
     * Compression method for compressed (DEFLATED) entries.
     */
    public static final int DEFLATED = ZipEntry.DEFLATED;

    /**
     * Creates a new ZIP output stream.
     * @param out the actual output stream
     */
    public ZipOutputStream(OutputStream out) {
        super(out, new Deflater(Deflater.DEFAULT_COMPRESSION, true));
        usesDefaultDeflater = true;
    }

    public ZipOutputStream(OutputStream out, String encoding) {
        this(out);
        this.encoding = encoding;
    }

    /**
     * Sets the ZIP file comment.
     * @param comment the comment string
     * @exception IllegalArgumentException if the length of the specified
     *		  ZIP file comment is greater than 0xFFFF bytes
     */
    public void setComment(String comment) {
        if (comment != null && comment.length() > 0xffff / 3 && getUTF8Length(comment) > 0xffff) {
            throw new IllegalArgumentException("ZIP file comment too long.");
        }
        this.comment = comment;
    }

    /**
     * Sets the default compression method for subsequent entries. This
     * default will be used whenever the compression method is not specified
     * for an individual ZIP file entry, and is initially set to DEFLATED.
     * @param method the default compression method
     * @exception IllegalArgumentException if the specified compression method
     *		  is invalid
     */
    public void setMethod(int method) {
        if (method != DEFLATED && method != STORED) {
            throw new IllegalArgumentException("invalid compression method");
        }
        this.method = method;
    }

    /**
     * Sets the compression level for subsequent entries which are DEFLATED.
     * The default setting is DEFAULT_COMPRESSION.
     * @param level the compression level (0-9)
     * @exception IllegalArgumentException if the compression level is invalid
     */
    public void setLevel(int level) {
        def.setLevel(level);
    }

    /**
     * Begins writing a new ZIP file entry and positions the stream to the
     * start of the entry data. Closes the current entry if still active.
     * The default compression method will be used if no compression method
     * was specified for the entry, and the current time will be used if
     * the entry has no set modification time.
     * @param e the ZIP entry to be written
     * @exception ZipException if a ZIP format error has occurred
     * @exception IOException if an I/O error has occurred
     */
    public void putNextEntry(ZipEntry e) throws IOException {
        ensureOpen();
        if (entry != null) {
            closeEntry();
        }
        if (e.time == -1) {
            e.setTime(System.currentTimeMillis());
        }
        if (e.method == -1) {
            e.method = method;
        }
        switch(e.method) {
            case DEFLATED:
                if (e.size == -1 || e.csize == -1 || e.crc == -1) {
                    e.flag = 8;
                } else if (e.size != -1 && e.csize != -1 && e.crc != -1) {
                    e.flag = 0;
                } else {
                    throw new ZipException("DEFLATED entry missing size, compressed size, or crc-32");
                }
                e.version = 20;
                break;
            case STORED:
                if (e.size == -1) {
                    e.size = e.csize;
                } else if (e.csize == -1) {
                    e.csize = e.size;
                } else if (e.size != e.csize) {
                    throw new ZipException("STORED entry where compressed != uncompressed size");
                }
                if (e.size == -1 || e.crc == -1) {
                    throw new ZipException("STORED entry missing size, compressed size, or crc-32");
                }
                e.version = 10;
                e.flag = 0;
                break;
            default:
                throw new ZipException("unsupported compression method");
        }
        e.offset = written;
        if (names.put(e.name, e) != null) {
            throw new ZipException("duplicate entry: " + e.name);
        }
        writeLOC(e);
        entries.addElement(e);
        entry = e;
    }

    /**
     * Closes the current ZIP entry and positions the stream for writing
     * the next entry.
     * @exception ZipException if a ZIP format error has occurred
     * @exception IOException if an I/O error has occurred
     */
    public void closeEntry() throws IOException {
        ensureOpen();
        ZipEntry e = entry;
        if (e != null) {
            switch(e.method) {
                case DEFLATED:
                    def.finish();
                    while (!def.finished()) {
                        deflate();
                    }
                    if ((e.flag & 8) == 0) {
                        if (e.size != def.getTotalIn()) {
                            throw new ZipException("invalid entry size (expected " + e.size + " but got " + def.getTotalIn() + " bytes)");
                        }
                        if (e.csize != def.getTotalOut()) {
                            throw new ZipException("invalid entry compressed size (expected " + e.csize + " but got " + def.getTotalOut() + " bytes)");
                        }
                        if (e.crc != crc.getValue()) {
                            throw new ZipException("invalid entry CRC-32 (expected 0x" + Long.toHexString(e.crc) + " but got 0x" + Long.toHexString(crc.getValue()) + ")");
                        }
                    } else {
                        e.size = def.getTotalIn();
                        e.csize = def.getTotalOut();
                        e.crc = crc.getValue();
                        writeEXT(e);
                    }
                    def.reset();
                    written += e.csize;
                    break;
                case STORED:
                    if (e.size != written - locoff) {
                        throw new ZipException("invalid entry size (expected " + e.size + " but got " + (written - locoff) + " bytes)");
                    }
                    if (e.crc != crc.getValue()) {
                        throw new ZipException("invalid entry crc-32 (expected 0x" + Long.toHexString(e.crc) + " but got 0x" + Long.toHexString(crc.getValue()) + ")");
                    }
                    break;
                default:
                    throw new InternalError("invalid compression method");
            }
            crc.reset();
            entry = null;
        }
    }

    /**
     * Writes an array of bytes to the current ZIP entry data. This method
     * will block until all the bytes are written.
     * @param b the data to be written
     * @param off the start offset in the data
     * @param len the number of bytes that are written
     * @exception ZipException if a ZIP file error has occurred
     * @exception IOException if an I/O error has occurred
     */
    public synchronized void write(byte[] b, int off, int len) throws IOException {
        ensureOpen();
        if (off < 0 || len < 0 || off > b.length - len) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        if (entry == null) {
            throw new ZipException("no current ZIP entry");
        }
        switch(entry.method) {
            case DEFLATED:
                super.write(b, off, len);
                break;
            case STORED:
                written += len;
                if (written - locoff > entry.size) {
                    throw new ZipException("attempt to write past end of STORED entry");
                }
                out.write(b, off, len);
                break;
            default:
                throw new InternalError("invalid compression method");
        }
        crc.update(b, off, len);
    }

    /**
     * Finishes writing the contents of the ZIP output stream without closing
     * the underlying stream. Use this method when applying multiple filters
     * in succession to the same output stream.
     * @exception ZipException if a ZIP file error has occurred
     * @exception IOException if an I/O exception has occurred
     */
    public void finish() throws IOException {
        ensureOpen();
        if (finished) {
            return;
        }
        if (entry != null) {
            closeEntry();
        }
        if (entries.size() < 1) {
            throw new ZipException("ZIP file must have at least one entry");
        }
        long off = written;
        Enumeration<ZipEntry> e = entries.elements();
        while (e.hasMoreElements()) {
            writeCEN(e.nextElement());
        }
        writeEND(off, written - off);
        finished = true;
    }

    /**
     * Closes the ZIP output stream as well as the stream being filtered.
     * @exception ZipException if a ZIP file error has occurred
     * @exception IOException if an I/O error has occurred
     */
    public void close() throws IOException {
        if (!closed) {
            super.close();
            closed = true;
        }
    }

    private void writeLOC(ZipEntry e) throws IOException {
        writeInt(LOCSIG);
        writeShort(e.version);
        writeShort(e.flag);
        writeShort(e.method);
        writeInt(e.time);
        if ((e.flag & 8) == 8) {
            writeInt(0);
            writeInt(0);
            writeInt(0);
        } else {
            writeInt(e.crc);
            writeInt(e.csize);
            writeInt(e.size);
        }
        byte[] nameBytes = null;
        try {
            if (this.encoding.toUpperCase().equals("UTF-8")) nameBytes = getUTF8Bytes(e.name); else nameBytes = e.name.getBytes(this.encoding);
        } catch (Exception byteE) {
            nameBytes = getUTF8Bytes(e.name);
        }
        writeShort(nameBytes.length);
        writeShort(e.extra != null ? e.extra.length : 0);
        writeBytes(nameBytes, 0, nameBytes.length);
        if (e.extra != null) {
            writeBytes(e.extra, 0, e.extra.length);
        }
        locoff = written;
    }

    private void writeEXT(ZipEntry e) throws IOException {
        writeInt(EXTSIG);
        writeInt(e.crc);
        writeInt(e.csize);
        writeInt(e.size);
    }

    private void writeCEN(ZipEntry e) throws IOException {
        writeInt(CENSIG);
        writeShort(e.version);
        writeShort(e.version);
        writeShort(e.flag);
        writeShort(e.method);
        writeInt(e.time);
        writeInt(e.crc);
        writeInt(e.csize);
        writeInt(e.size);
        byte[] nameBytes = null;
        try {
            if (this.encoding.toUpperCase().equals("UTF-8")) nameBytes = getUTF8Bytes(e.name); else nameBytes = e.name.getBytes(this.encoding);
        } catch (Exception byteE) {
            nameBytes = getUTF8Bytes(e.name);
        }
        writeShort(nameBytes.length);
        writeShort(e.extra != null ? e.extra.length : 0);
        byte[] commentBytes;
        if (e.comment != null) {
            commentBytes = getUTF8Bytes(e.comment);
            writeShort(commentBytes.length);
        } else {
            commentBytes = null;
            writeShort(0);
        }
        writeShort(0);
        writeShort(0);
        writeInt(0);
        writeInt(e.offset);
        writeBytes(nameBytes, 0, nameBytes.length);
        if (e.extra != null) {
            writeBytes(e.extra, 0, e.extra.length);
        }
        if (commentBytes != null) {
            writeBytes(commentBytes, 0, commentBytes.length);
        }
    }

    private void writeEND(long off, long len) throws IOException {
        writeInt(ENDSIG);
        writeShort(0);
        writeShort(0);
        writeShort(entries.size());
        writeShort(entries.size());
        writeInt(len);
        writeInt(off);
        if (comment != null) {
            byte[] b = getUTF8Bytes(comment);
            writeShort(b.length);
            writeBytes(b, 0, b.length);
        } else {
            writeShort(0);
        }
    }

    private void writeShort(int v) throws IOException {
        OutputStream out = this.out;
        out.write((v >>> 0) & 0xff);
        out.write((v >>> 8) & 0xff);
        written += 2;
    }

    private void writeInt(long v) throws IOException {
        OutputStream out = this.out;
        out.write((int) ((v >>> 0) & 0xff));
        out.write((int) ((v >>> 8) & 0xff));
        out.write((int) ((v >>> 16) & 0xff));
        out.write((int) ((v >>> 24) & 0xff));
        written += 4;
    }

    private void writeBytes(byte[] b, int off, int len) throws IOException {
        super.out.write(b, off, len);
        written += len;
    }

    static int getUTF8Length(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch <= 0x7f) {
                count++;
            } else if (ch <= 0x7ff) {
                count += 2;
            } else {
                count += 3;
            }
        }
        return count;
    }

    private static byte[] getUTF8Bytes(String s) {
        char[] c = s.toCharArray();
        int len = c.length;
        int count = 0;
        for (int i = 0; i < len; i++) {
            int ch = c[i];
            if (ch <= 0x7f) {
                count++;
            } else if (ch <= 0x7ff) {
                count += 2;
            } else {
                count += 3;
            }
        }
        byte[] b = new byte[count];
        int off = 0;
        for (int i = 0; i < len; i++) {
            int ch = c[i];
            if (ch <= 0x7f) {
                b[off++] = (byte) ch;
            } else if (ch <= 0x7ff) {
                b[off++] = (byte) ((ch >> 6) | 0xc0);
                b[off++] = (byte) ((ch & 0x3f) | 0x80);
            } else {
                b[off++] = (byte) ((ch >> 12) | 0xe0);
                b[off++] = (byte) (((ch >> 6) & 0x3f) | 0x80);
                b[off++] = (byte) ((ch & 0x3f) | 0x80);
            }
        }
        return b;
    }
}
