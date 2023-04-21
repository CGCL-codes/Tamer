package com.sun.opengl.util.texture.spi;

import java.io.*;
import javax.media.opengl.*;
import com.sun.opengl.util.*;
import java.awt.image.*;
import javax.swing.*;

/** <p> Reads and writes SGI RGB/RGBA images. </p>

    <p> Written from <a href =
    "http://astronomy.swin.edu.au/~pbourke/dataformats/sgirgb/">Paul
    Bourke's adaptation</a> of the <a href =
    "http://astronomy.swin.edu.au/~pbourke/dataformats/sgirgb/sgiversion.html">SGI
    specification</a>. </p>
*/
public class SGIImage {

    private Header header;

    private int format;

    private byte[] data;

    private int[] rowStart;

    private int[] rowSize;

    private int rleEnd;

    private byte[] tmpData;

    private byte[] tmpRead;

    private static final int MAGIC = 474;

    static class Header {

        short magic;

        byte storage;

        byte bpc;

        short dimension;

        short xsize;

        short ysize;

        short zsize;

        int pixmin;

        int pixmax;

        int dummy;

        String imagename;

        int colormap;

        Header() {
            magic = MAGIC;
        }

        Header(DataInputStream in) throws IOException {
            magic = in.readShort();
            storage = in.readByte();
            bpc = in.readByte();
            dimension = in.readShort();
            xsize = in.readShort();
            ysize = in.readShort();
            zsize = in.readShort();
            pixmin = in.readInt();
            pixmax = in.readInt();
            dummy = in.readInt();
            byte[] tmpname = new byte[80];
            in.read(tmpname);
            int numChars = 0;
            while (tmpname[numChars++] != 0) ;
            imagename = new String(tmpname, 0, numChars);
            colormap = in.readInt();
            byte[] tmp = new byte[404];
            in.read(tmp);
        }

        public String toString() {
            return ("magic: " + magic + " storage: " + (int) storage + " bpc: " + (int) bpc + " dimension: " + dimension + " xsize: " + xsize + " ysize: " + ysize + " zsize: " + zsize + " pixmin: " + pixmin + " pixmax: " + pixmax + " imagename: " + imagename + " colormap: " + colormap);
        }
    }

    private SGIImage(Header header) {
        this.header = header;
    }

    /** Reads an SGI image from the specified file. */
    public static SGIImage read(String filename) throws IOException {
        return read(new FileInputStream(filename));
    }

    /** Reads an SGI image from the specified InputStream. */
    public static SGIImage read(InputStream in) throws IOException {
        DataInputStream dIn = new DataInputStream(new BufferedInputStream(in));
        Header header = new Header(dIn);
        SGIImage res = new SGIImage(header);
        res.decodeImage(dIn);
        return res;
    }

    /** Writes this SGIImage to the specified file name. If
      flipVertically is set, outputs the scanlines from top to bottom
      rather than the default bottom to top order. */
    public void write(String filename, boolean flipVertically) throws IOException {
        write(new File(filename), flipVertically);
    }

    /** Writes this SGIImage to the specified file. If flipVertically is
      set, outputs the scanlines from top to bottom rather than the
      default bottom to top order. */
    public void write(File file, boolean flipVertically) throws IOException {
        writeImage(file, data, header.xsize, header.ysize, header.zsize, flipVertically);
    }

    /** Creates an SGIImage from the specified data in either RGB or
      RGBA format. */
    public static SGIImage createFromData(int width, int height, boolean hasAlpha, byte[] data) {
        Header header = new Header();
        header.xsize = (short) width;
        header.ysize = (short) height;
        header.zsize = (short) (hasAlpha ? 4 : 3);
        SGIImage image = new SGIImage(header);
        image.data = data;
        return image;
    }

    /** Determines from the magic number whether the given InputStream
      points to an SGI RGB image. The given InputStream must return
      true from markSupported() and support a minimum of two bytes
      of read-ahead. */
    public static boolean isSGIImage(InputStream in) throws IOException {
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        if (!in.markSupported()) {
            throw new IOException("Can not test non-destructively whether given InputStream is an SGI RGB image");
        }
        DataInputStream dIn = new DataInputStream(in);
        dIn.mark(4);
        short magic = dIn.readShort();
        dIn.reset();
        return (magic == MAGIC);
    }

    /** Returns the width of the image. */
    public int getWidth() {
        return header.xsize;
    }

    /** Returns the height of the image. */
    public int getHeight() {
        return header.ysize;
    }

    /** Returns the OpenGL format for this texture; e.g. GL.GL_RGB or GL.GL_RGBA. */
    public int getFormat() {
        return format;
    }

    /** Returns the raw data for this texture in the correct
      (bottom-to-top) order for calls to glTexImage2D. */
    public byte[] getData() {
        return data;
    }

    public String toString() {
        return header.toString();
    }

    private void decodeImage(DataInputStream in) throws IOException {
        if (header.storage == 1) {
            int x = header.ysize * header.zsize;
            rowStart = new int[x];
            rowSize = new int[x];
            rleEnd = 4 * 2 * x + 512;
            for (int i = 0; i < x; i++) {
                rowStart[i] = in.readInt();
            }
            for (int i = 0; i < x; i++) {
                rowSize[i] = in.readInt();
            }
            tmpRead = new byte[header.xsize * 256];
        }
        tmpData = readAll(in);
        int xsize = header.xsize;
        int ysize = header.ysize;
        int zsize = header.zsize;
        int lptr = 0;
        data = new byte[xsize * ysize * 4];
        byte[] rbuf = new byte[xsize];
        byte[] gbuf = new byte[xsize];
        byte[] bbuf = new byte[xsize];
        byte[] abuf = new byte[xsize];
        for (int y = 0; y < ysize; y++) {
            if (zsize >= 4) {
                getRow(rbuf, y, 0);
                getRow(gbuf, y, 1);
                getRow(bbuf, y, 2);
                getRow(abuf, y, 3);
                rgbatorgba(rbuf, gbuf, bbuf, abuf, data, lptr);
            } else if (zsize == 3) {
                getRow(rbuf, y, 0);
                getRow(gbuf, y, 1);
                getRow(bbuf, y, 2);
                rgbtorgba(rbuf, gbuf, bbuf, data, lptr);
            } else if (zsize == 2) {
                getRow(rbuf, y, 0);
                getRow(abuf, y, 1);
                latorgba(rbuf, abuf, data, lptr);
            } else {
                getRow(rbuf, y, 0);
                bwtorgba(rbuf, data, lptr);
            }
            lptr += 4 * xsize;
        }
        rowStart = null;
        rowSize = null;
        tmpData = null;
        tmpRead = null;
        format = GL.GL_RGBA;
        header.zsize = 4;
    }

    private void getRow(byte[] buf, int y, int z) {
        if (header.storage == 1) {
            int offs = rowStart[y + z * header.ysize] - rleEnd;
            System.arraycopy(tmpData, offs, tmpRead, 0, rowSize[y + z * header.ysize]);
            int iPtr = 0;
            int oPtr = 0;
            for (; ; ) {
                byte pixel = tmpRead[iPtr++];
                int count = (int) (pixel & 0x7F);
                if (count == 0) {
                    return;
                }
                if ((pixel & 0x80) != 0) {
                    while ((count--) > 0) {
                        buf[oPtr++] = tmpRead[iPtr++];
                    }
                } else {
                    pixel = tmpRead[iPtr++];
                    while ((count--) > 0) {
                        buf[oPtr++] = pixel;
                    }
                }
            }
        } else {
            int offs = (y * header.xsize) + (z * header.xsize * header.ysize);
            System.arraycopy(tmpData, offs, buf, 0, header.xsize);
        }
    }

    private void bwtorgba(byte[] b, byte[] dest, int lptr) {
        for (int i = 0; i < b.length; i++) {
            dest[4 * i + lptr + 0] = b[i];
            dest[4 * i + lptr + 1] = b[i];
            dest[4 * i + lptr + 2] = b[i];
            dest[4 * i + lptr + 3] = (byte) 0xFF;
        }
    }

    private void latorgba(byte[] b, byte[] a, byte[] dest, int lptr) {
        for (int i = 0; i < b.length; i++) {
            dest[4 * i + lptr + 0] = b[i];
            dest[4 * i + lptr + 1] = b[i];
            dest[4 * i + lptr + 2] = b[i];
            dest[4 * i + lptr + 3] = a[i];
        }
    }

    private void rgbtorgba(byte[] r, byte[] g, byte[] b, byte[] dest, int lptr) {
        for (int i = 0; i < b.length; i++) {
            dest[4 * i + lptr + 0] = r[i];
            dest[4 * i + lptr + 1] = g[i];
            dest[4 * i + lptr + 2] = b[i];
            dest[4 * i + lptr + 3] = (byte) 0xFF;
        }
    }

    private void rgbatorgba(byte[] r, byte[] g, byte[] b, byte[] a, byte[] dest, int lptr) {
        for (int i = 0; i < b.length; i++) {
            dest[4 * i + lptr + 0] = r[i];
            dest[4 * i + lptr + 1] = g[i];
            dest[4 * i + lptr + 2] = b[i];
            dest[4 * i + lptr + 3] = a[i];
        }
    }

    private static byte imgref(byte[] i, int x, int y, int z, int xs, int ys, int zs) {
        return i[(xs * ys * z) + (xs * y) + x];
    }

    private void writeHeader(DataOutputStream stream, int xsize, int ysize, int zsize, boolean rle) throws IOException {
        stream.writeShort(474);
        stream.write((rle ? 1 : 0));
        stream.write(1);
        stream.writeShort(3);
        stream.writeShort(xsize);
        stream.writeShort(ysize);
        stream.writeShort(zsize);
        stream.writeInt(0);
        stream.writeInt(255);
        stream.writeInt(0);
        for (int i = 0; i < 80; i++) stream.write(0);
        stream.writeInt(0);
        for (int i = 0; i < 404; i++) stream.write(0);
    }

    private void writeImage(File file, byte[] data, int xsize, int ysize, int zsize, boolean yflip) throws IOException {
        byte[] tmpData = new byte[xsize * ysize * zsize];
        int dest = 0;
        for (int i = 0; i < zsize; i++) {
            for (int j = i; j < (xsize * ysize * zsize); j += zsize) {
                tmpData[dest++] = data[j];
            }
        }
        data = tmpData;
        int[] starttab = new int[ysize * zsize];
        int[] lengthtab = new int[ysize * zsize];
        int lookahead = 3;
        byte[] rlebuf = new byte[2 * xsize * ysize * zsize];
        int cur_loc = 0;
        int ptr = 0;
        int total_size = 0;
        int ystart = 0;
        int yincr = 1;
        int yend = ysize;
        if (yflip) {
            ystart = ysize - 1;
            yend = -1;
            yincr = -1;
        }
        boolean DEBUG = false;
        for (int z = 0; z < zsize; z++) {
            for (int y = ystart; y != yend; y += yincr) {
                int x = 0;
                byte count = 0;
                boolean repeat_mode = false;
                boolean should_switch = false;
                int start_ptr = ptr;
                int num_ptr = ptr++;
                byte repeat_val = 0;
                while (x < xsize) {
                    should_switch = false;
                    if (repeat_mode) {
                        if (imgref(data, x, y, z, xsize, ysize, zsize) != repeat_val) {
                            should_switch = true;
                        }
                    } else {
                        if ((x + lookahead) < xsize) {
                            should_switch = true;
                            for (int i = 1; i <= lookahead; i++) {
                                if (DEBUG) System.err.println("left side was " + ((int) imgref(data, x, y, z, xsize, ysize, zsize)) + ", right side was " + (int) imgref(data, x + i, y, z, xsize, ysize, zsize));
                                if (imgref(data, x, y, z, xsize, ysize, zsize) != imgref(data, x + i, y, z, xsize, ysize, zsize)) should_switch = false;
                            }
                        }
                    }
                    if (should_switch || (count == 127)) {
                        if (x > 0) {
                            if (repeat_mode) rlebuf[num_ptr] = count; else rlebuf[num_ptr] = (byte) (count | 0x80);
                        }
                        if (repeat_mode) {
                            if (should_switch) repeat_mode = false;
                            rlebuf[ptr++] = repeat_val;
                        } else {
                            if (should_switch) repeat_mode = true;
                            repeat_val = imgref(data, x, y, z, xsize, ysize, zsize);
                        }
                        if (x > 0) {
                            num_ptr = ptr++;
                            count = 0;
                        }
                    }
                    if (!repeat_mode) {
                        rlebuf[ptr++] = imgref(data, x, y, z, xsize, ysize, zsize);
                    }
                    count++;
                    if (x == xsize - 1) {
                        if (repeat_mode) {
                            rlebuf[num_ptr] = count;
                            rlebuf[ptr++] = repeat_val;
                        } else rlebuf[num_ptr] = (byte) (count | 0x80);
                        rlebuf[ptr++] = 0;
                    }
                    x++;
                }
                int rowlen = ptr - start_ptr;
                if (yflip) lengthtab[ysize * z + (ysize - y - 1)] = rowlen; else lengthtab[ysize * z + y] = rowlen;
                if (yflip) starttab[ysize * z + (ysize - y - 1)] = cur_loc; else starttab[ysize * z + y] = cur_loc;
                cur_loc += rowlen;
            }
        }
        total_size = ptr;
        if (DEBUG) System.err.println("total_size was " + total_size);
        DataOutputStream stream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        writeHeader(stream, xsize, ysize, zsize, true);
        int SIZEOF_INT = 4;
        for (int i = 0; i < (ysize * zsize); i++) stream.writeInt(starttab[i] + 512 + (2 * ysize * zsize * SIZEOF_INT));
        for (int i = 0; i < (ysize * zsize); i++) stream.writeInt(lengthtab[i]);
        for (int i = 0; i < total_size; i++) stream.write(rlebuf[i]);
        stream.close();
    }

    private byte[] readAll(DataInputStream in) throws IOException {
        byte[] dest = new byte[16384];
        int pos = 0;
        int numRead = 0;
        boolean done = false;
        do {
            numRead = in.read(dest, pos, dest.length - pos);
            if (pos == dest.length) {
                byte[] newDest = new byte[2 * dest.length];
                System.arraycopy(dest, 0, newDest, 0, pos);
                dest = newDest;
            }
            if (numRead > 0) {
                pos += numRead;
            }
            done = ((numRead == -1) || (in.available() == 0));
        } while (!done);
        if (pos != dest.length) {
            byte[] finalDest = new byte[pos];
            System.arraycopy(dest, 0, finalDest, 0, pos);
            dest = finalDest;
        }
        return dest;
    }
}
