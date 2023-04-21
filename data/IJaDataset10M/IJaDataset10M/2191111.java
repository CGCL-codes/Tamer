package edu.ucsd.ncmir.jinx.xml;

import edu.sdsc.grid.io.GeneralFile;
import edu.sdsc.grid.io.GeneralFileInputStream;
import edu.ucsd.ncmir.spl.filesystem.GeneralFileFactory;
import edu.ucsd.ncmir.spl.gui.StatusDialog;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

class JxWrappedInputStream extends InputStream {

    private long _length;

    private long _percent;

    private GeneralFileInputStream _gfis;

    private StatusDialog _sd;

    JxWrappedInputStream(GeneralFile gf) throws IOException {
        this._length = gf.length();
        if (this._length == 0) throw new EOFException("Empty file.");
        this._sd = new StatusDialog("XML Loader");
        this._sd.updateText("Opening " + gf);
        this._percent = this._length / 100;
        this._gfis = GeneralFileFactory.createInputStream(gf);
        this._sd.updateText("Loading. . .");
        this._sd.updateProgress(0);
    }

    private long _bytes_read = 0;

    @Override
    public int read() throws IOException {
        return this._gfis.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        this._bytes_read++;
        return this.read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        long old_percent;
        if (this._percent > 0) old_percent = this._bytes_read / this._percent; else old_percent = 100;
        int l = this._gfis.read(b, off, len);
        if (l != -1) {
            this._bytes_read += l;
            long new_percent;
            if (this._percent > 0) new_percent = this._bytes_read / this._percent; else new_percent = 100;
            if (old_percent != new_percent) this._sd.updateProgress((int) new_percent);
        }
        return l;
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public int available() throws IOException {
        return this._gfis.available();
    }

    @Override
    public long skip(long n) throws IOException {
        this._bytes_read += n;
        return this._gfis.skip(n);
    }

    @Override
    public void close() throws IOException {
        this._sd.close();
        this._gfis.close();
    }

    @Override
    public void reset() throws IOException {
        this._bytes_read = 0;
        this._gfis.reset();
    }
}
