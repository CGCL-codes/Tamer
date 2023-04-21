package jj2000.j2k.image.output;

import jj2000.j2k.image.*;
import jj2000.j2k.util.*;
import java.io.*;

/**
 * This class writes a component from an image in 8 bit unsigned data to a
 * binary PGM file. The size of the image that is written to the file is the
 * size of the component from which to get the data, not the size of the
 * source image (they differ if there is some sub-sampling).
 *
 * <p>Before writing, all coefficients are inversly level shifted and then
 * "saturated" (they are limited to the nominal dynamic range).<br> <u>Ex:</u>
 * if the nominal range is 0-255, the following algorithm is applied:<br>
 * <tt>if coeff<0, output=0<br> if coeff>255, output=255<br> else
 * output=coeff</tt></p>
 *
 * <p>The write() methods of an object of this class may not be called
 * concurrently from different threads.</p>
 *
 * <p>NOTE: This class is not thread safe, for reasons of internal
 * buffering.</p>
 * */
public class ImgWriterPGM extends ImgWriter {

    /** Value used to inverse level shift */
    private int levShift;

    /** Where to write the data */
    private RandomAccessFile out;

    /** The index of the component from where to get the data */
    private int c;

    /** The number of fractional bits in the source data */
    private int fb;

    /** A DataBlk, just used to avoid allocating a new one each time
        it is needed */
    private DataBlkInt db = new DataBlkInt();

    /** The offset of the raw pixel data in the PGM file */
    private int offset;

    private byte buf[];

    /**
     * Creates a new writer to the specified File object, to write data from
     * the specified component.
     *
     * <p>The size of the image that is written to the file is the size of the
     * component from which to get the data, specified by b, not the size of
     * the source image (they differ if there is some sub-sampling).</p>
     *
     * @param out The file where to write the data
     *
     * @param imgSrc The source from where to get the image data to write.
     *
     * @param c The index of the component from where to get the data.
     * */
    public ImgWriterPGM(File out, BlkImgDataSrc imgSrc, int c) throws IOException {
        if (c < 0 || c >= imgSrc.getNumComps()) {
            throw new IllegalArgumentException("Invalid number of components");
        }
        if (imgSrc.getNomRangeBits(c) > 8) {
            FacilityManager.getMsgLogger().println("Warning: Component " + c + " has nominal bitdepth " + imgSrc.getNomRangeBits(c) + ". Pixel values will be " + "down-shifted to fit bitdepth of 8 for PGM file", 8, 8);
        }
        if (out.exists() && !out.delete()) {
            throw new IOException("Could not reset file");
        }
        this.out = new RandomAccessFile(out, "rw");
        src = imgSrc;
        this.c = c;
        w = imgSrc.getImgWidth();
        h = imgSrc.getImgHeight();
        fb = imgSrc.getFixedPoint(c);
        levShift = 1 << (imgSrc.getNomRangeBits(c) - 1);
        writeHeaderInfo();
    }

    /**
     * Creates a new writer to the specified file, to write data from the
     * specified component.
     *
     * <P>The size of the image that is written to the file is the size of the
     * component from which to get the data, specified by b, not the size of
     * the source image (they differ if there is some sub-sampling).
     *
     * @param fname The name of the file where to write the data
     *
     * @param imgSrc The source from where to get the image data to write.
     *
     * @param c The index of the component from where to get the data.
     * */
    public ImgWriterPGM(String fname, BlkImgDataSrc imgSrc, int c) throws IOException {
        this(new File(fname), imgSrc, c);
    }

    /**
     * Closes the underlying file or netwrok connection to where the data is
     * written. Any call to other methods of the class become illegal after a
     * call to this one.
     *
     * @exception IOException If an I/O error occurs.
     * */
    @Override
    public void close() throws IOException {
        int i;
        if (out.length() != w * h + offset) {
            out.seek(out.length());
            for (i = offset + w * h - (int) out.length(); i > 0; i--) {
                out.writeByte(0);
            }
        }
        out.close();
        src = null;
        out = null;
        db = null;
    }

    /**
     * Writes all buffered data to the file or resource.
     *
     * @exception IOException If an I/O error occurs.
     * */
    @Override
    public void flush() throws IOException {
        buf = null;
    }

    /**
     * Writes the data of the specified area to the file, coordinates are
     * relative to the current tile of the source. Before writing, the
     * coefficients are limited to the nominal range.
     *
     * <p>This method may not be called concurrently from different
     * threads.</p>
     *
     * <p>If the data returned from the BlkImgDataSrc source is progressive,
     * then it is requested over and over until it is not progressive
     * anymore.</p>
     *
     * @param ulx The horizontal coordinate of the upper-left corner of the
     * area to write, relative to the current tile.
     *
     * @param uly The vertical coordinate of the upper-left corner of the area
     * to write, relative to the current tile.
     *
     * @param width The width of the area to write.
     *
     * @param height The height of the area to write.
     *
     * @exception IOException If an I/O error occurs.
     * */
    @Override
    public void write(int ulx, int uly, int w, int h) throws IOException {
        int k, i, j;
        int fracbits = fb;
        int tOffx, tOffy;
        db.ulx = ulx;
        db.uly = uly;
        db.w = w;
        db.h = h;
        tOffx = src.getCompULX(c) - (int) Math.ceil(src.getImgULX() / (double) src.getCompSubsX(c));
        tOffy = src.getCompULY(c) - (int) Math.ceil(src.getImgULY() / (double) src.getCompSubsY(c));
        if (db.data != null && db.data.length < w * h) {
            db.data = null;
        }
        do {
            db = (DataBlkInt) src.getInternCompData(db, c);
        } while (db.progressive);
        int tmp, maxVal = (1 << src.getNomRangeBits(c)) - 1;
        int downShift = src.getNomRangeBits(c) - 8;
        if (downShift < 0) {
            downShift = 0;
        }
        if (buf == null || buf.length < w) {
            buf = new byte[w];
        }
        for (i = 0; i < h; i++) {
            out.seek(this.offset + this.w * (uly + tOffy + i) + ulx + tOffx);
            if (fracbits == 0) {
                for (k = db.offset + i * db.scanw + w - 1, j = w - 1; j >= 0; j--, k--) {
                    tmp = db.data[k] + levShift;
                    buf[j] = (byte) (((tmp < 0) ? 0 : ((tmp > maxVal) ? maxVal : tmp)) >> downShift);
                }
            } else {
                for (k = db.offset + i * db.scanw + w - 1, j = w - 1; j >= 0; j--, k--) {
                    tmp = (db.data[k] >> fracbits) + levShift;
                    buf[j] = (byte) (((tmp < 0) ? 0 : ((tmp > maxVal) ? maxVal : tmp)) >> downShift);
                }
            }
            out.write(buf, 0, w);
        }
    }

    /**
     * Writes the source's current tile to the output. The requests of data
     * issued to the source BlkImgDataSrc object are done by strips, in order
     * to reduce memory usage.
     *
     * <p>If the data returned from the BlkImgDataSrc source is progressive,
     * then it is requested over and over until it is not progressive
     * anymore.</p>
     *
     * @exception IOException If an I/O error occurs.
     *
     * @see DataBlk
     * */
    @Override
    public void write() throws IOException {
        int i;
        int tIdx = src.getTileIdx();
        int tw = src.getTileCompWidth(tIdx, c);
        int th = src.getTileCompHeight(tIdx, c);
        for (i = 0; i < th; i += DEF_STRIP_HEIGHT) {
            write(0, i, tw, (th - i < DEF_STRIP_HEIGHT) ? th - i : DEF_STRIP_HEIGHT);
        }
    }

    /**
     * Writes the header info of the PGM file :
     *
     * P5
     * width height
     * 255
     *
     * @exception IOException If there is an IOException
     * */
    private void writeHeaderInfo() throws IOException {
        byte[] byteVals;
        int i;
        String val;
        out.writeByte('P');
        out.write('5');
        out.write('\n');
        offset = 3;
        val = String.valueOf(w);
        byteVals = val.getBytes();
        for (i = 0; i < byteVals.length; i++) {
            out.writeByte(byteVals[i]);
            offset++;
        }
        out.write(' ');
        offset++;
        val = String.valueOf(h);
        byteVals = val.getBytes();
        for (i = 0; i < byteVals.length; i++) {
            out.writeByte(byteVals[i]);
            offset++;
        }
        out.write('\n');
        out.write('2');
        out.write('5');
        out.write('5');
        out.write('\n');
        offset += 5;
    }

    /**
     * Returns a string of information about the object, more than 1 line
     * long. The information string includes information from the underlying
     * RandomAccessFile (its toString() method is called in turn).
     *
     * @return A string of information about the object.
     * */
    @Override
    public String toString() {
        return "ImgWriterPGM: WxH = " + w + "x" + h + ", Component=" + c + "\nUnderlying RandomAccessFile:\n" + out.toString();
    }
}
