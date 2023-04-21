package edu.columbia.hypercontent.util.codec;

import java.util.*;
import java.io.*;
import java.awt.Image;
import java.awt.image.*;

public class GIFImageEncoder implements ImageConsumer {

    protected OutputStream out;

    private ImageProducer producer;

    private int width = -1;

    private int height = -1;

    private int hintflags = 0;

    private boolean started = false;

    private boolean encoding;

    private IOException iox;

    private static final ColorModel rgbModel = ColorModel.getRGBdefault();

    private Hashtable props = null;

    private boolean interlace = false;

    public GIFImageEncoder(Image img, OutputStream out) throws IOException {
        this(img, out, false);
    }

    public GIFImageEncoder(Image img, OutputStream out, boolean interlace) throws IOException {
        this.producer = img.getSource();
        this.out = out;
        this.interlace = interlace;
    }

    int[][] rgbPixels;

    void encodeStart(int width, int height) throws IOException {
        this.width = width;
        this.height = height;
        rgbPixels = new int[height][width];
    }

    void encodePixels(int x, int y, int w, int h, int[] rgbPixels, int off, int scansize) throws IOException {
        for (int row = 0; row < h; ++row) System.arraycopy(rgbPixels, row * scansize + off, this.rgbPixels[y + row], x, w);
    }

    IntHashtable colorHash;

    void encodeDone() throws IOException {
        int transparentIndex = -1;
        int transparentRgb = -1;
        colorHash = new IntHashtable();
        int index = 0;
        for (int row = 0; row < height; ++row) {
            int rowOffset = row * width;
            for (int col = 0; col < width; ++col) {
                int rgb = rgbPixels[row][col];
                boolean isTransparent = ((rgb >>> 24) < 0x80);
                if (isTransparent) {
                    if (transparentIndex < 0) {
                        transparentIndex = index;
                        transparentRgb = rgb;
                    } else if (rgb != transparentRgb) {
                        rgbPixels[row][col] = rgb = transparentRgb;
                    }
                }
                GifEncoderHashitem item = (GifEncoderHashitem) colorHash.get(rgb);
                if (item == null) {
                    if (index >= 256) throw new IOException("too many colors for a GIF");
                    item = new GifEncoderHashitem(rgb, 1, index, isTransparent);
                    ++index;
                    colorHash.put(rgb, item);
                } else ++item.count;
            }
        }
        int logColors;
        if (index <= 2) logColors = 1; else if (index <= 4) logColors = 2; else if (index <= 16) logColors = 4; else logColors = 8;
        int mapSize = 1 << logColors;
        byte[] reds = new byte[mapSize];
        byte[] grns = new byte[mapSize];
        byte[] blus = new byte[mapSize];
        for (Enumeration e = colorHash.elements(); e.hasMoreElements(); ) {
            GifEncoderHashitem item = (GifEncoderHashitem) e.nextElement();
            reds[item.index] = (byte) ((item.rgb >> 16) & 0xff);
            grns[item.index] = (byte) ((item.rgb >> 8) & 0xff);
            blus[item.index] = (byte) (item.rgb & 0xff);
        }
        GIFEncode(out, width, height, interlace, (byte) 0, transparentIndex, logColors, reds, grns, blus);
    }

    byte GetPixel(int x, int y) throws IOException {
        GifEncoderHashitem item = (GifEncoderHashitem) colorHash.get(rgbPixels[y][x]);
        if (item == null) throw new IOException("color not found");
        return (byte) item.index;
    }

    static void writeString(OutputStream out, String str) throws IOException {
        byte[] buf = str.getBytes();
        out.write(buf);
    }

    int Width, Height;

    boolean Interlace;

    int curx, cury;

    int CountDown;

    int Pass = 0;

    void GIFEncode(OutputStream outs, int Width, int Height, boolean Interlace, byte Background, int Transparent, int BitsPerPixel, byte[] Red, byte[] Green, byte[] Blue) throws IOException {
        byte B;
        int LeftOfs, TopOfs;
        int ColorMapSize;
        int InitCodeSize;
        int i;
        this.Width = Width;
        this.Height = Height;
        this.Interlace = Interlace;
        ColorMapSize = 1 << BitsPerPixel;
        LeftOfs = TopOfs = 0;
        CountDown = Width * Height;
        Pass = 0;
        if (BitsPerPixel <= 1) InitCodeSize = 2; else InitCodeSize = BitsPerPixel;
        curx = 0;
        cury = 0;
        writeString(outs, "GIF89a");
        Putword(Width, outs);
        Putword(Height, outs);
        B = (byte) 0x80;
        B |= (byte) ((8 - 1) << 4);
        B |= (byte) ((BitsPerPixel - 1));
        Putbyte(B, outs);
        Putbyte(Background, outs);
        Putbyte((byte) 0, outs);
        for (i = 0; i < ColorMapSize; ++i) {
            Putbyte(Red[i], outs);
            Putbyte(Green[i], outs);
            Putbyte(Blue[i], outs);
        }
        if (Transparent != -1) {
            Putbyte((byte) '!', outs);
            Putbyte((byte) 0xf9, outs);
            Putbyte((byte) 4, outs);
            Putbyte((byte) 1, outs);
            Putbyte((byte) 0, outs);
            Putbyte((byte) 0, outs);
            Putbyte((byte) Transparent, outs);
            Putbyte((byte) 0, outs);
        }
        Putbyte((byte) ',', outs);
        Putword(LeftOfs, outs);
        Putword(TopOfs, outs);
        Putword(Width, outs);
        Putword(Height, outs);
        if (Interlace) Putbyte((byte) 0x40, outs); else Putbyte((byte) 0x00, outs);
        Putbyte((byte) InitCodeSize, outs);
        compress(InitCodeSize + 1, outs);
        Putbyte((byte) 0, outs);
        Putbyte((byte) ';', outs);
    }

    void BumpPixel() {
        ++curx;
        if (curx == Width) {
            curx = 0;
            if (!Interlace) ++cury; else {
                switch(Pass) {
                    case 0:
                        cury += 8;
                        if (cury >= Height) {
                            ++Pass;
                            cury = 4;
                        }
                        break;
                    case 1:
                        cury += 8;
                        if (cury >= Height) {
                            ++Pass;
                            cury = 2;
                        }
                        break;
                    case 2:
                        cury += 4;
                        if (cury >= Height) {
                            ++Pass;
                            cury = 1;
                        }
                        break;
                    case 3:
                        cury += 2;
                        break;
                }
            }
        }
    }

    static final int EOF = -1;

    int GIFNextPixel() throws IOException {
        byte r;
        if (CountDown == 0) return EOF;
        --CountDown;
        r = GetPixel(curx, cury);
        BumpPixel();
        return r & 0xff;
    }

    void Putword(int w, OutputStream outs) throws IOException {
        Putbyte((byte) (w & 0xff), outs);
        Putbyte((byte) ((w >> 8) & 0xff), outs);
    }

    void Putbyte(byte b, OutputStream outs) throws IOException {
        outs.write(b);
    }

    static final int BITS = 12;

    static final int HSIZE = 5003;

    int n_bits;

    int maxbits = BITS;

    int maxcode;

    int maxmaxcode = 1 << BITS;

    final int MAXCODE(int n_bits) {
        return (1 << n_bits) - 1;
    }

    int[] htab = new int[HSIZE];

    int[] codetab = new int[HSIZE];

    int hsize = HSIZE;

    int free_ent = 0;

    boolean clear_flg = false;

    int g_init_bits;

    int ClearCode;

    int EOFCode;

    void compress(int init_bits, OutputStream outs) throws IOException {
        int fcode;
        int i;
        int c;
        int ent;
        int disp;
        int hsize_reg;
        int hshift;
        g_init_bits = init_bits;
        clear_flg = false;
        n_bits = g_init_bits;
        maxcode = MAXCODE(n_bits);
        ClearCode = 1 << (init_bits - 1);
        EOFCode = ClearCode + 1;
        free_ent = ClearCode + 2;
        char_init();
        ent = GIFNextPixel();
        hshift = 0;
        for (fcode = hsize; fcode < 65536; fcode *= 2) ++hshift;
        hshift = 8 - hshift;
        hsize_reg = hsize;
        cl_hash(hsize_reg);
        output(ClearCode, outs);
        outer_loop: while ((c = GIFNextPixel()) != EOF) {
            fcode = (c << maxbits) + ent;
            i = (c << hshift) ^ ent;
            if (htab[i] == fcode) {
                ent = codetab[i];
                continue;
            } else if (htab[i] >= 0) {
                disp = hsize_reg - i;
                if (i == 0) disp = 1;
                do {
                    if ((i -= disp) < 0) i += hsize_reg;
                    if (htab[i] == fcode) {
                        ent = codetab[i];
                        continue outer_loop;
                    }
                } while (htab[i] >= 0);
            }
            output(ent, outs);
            ent = c;
            if (free_ent < maxmaxcode) {
                codetab[i] = free_ent++;
                htab[i] = fcode;
            } else cl_block(outs);
        }
        output(ent, outs);
        output(EOFCode, outs);
    }

    int cur_accum = 0;

    int cur_bits = 0;

    int masks[] = { 0x0000, 0x0001, 0x0003, 0x0007, 0x000F, 0x001F, 0x003F, 0x007F, 0x00FF, 0x01FF, 0x03FF, 0x07FF, 0x0FFF, 0x1FFF, 0x3FFF, 0x7FFF, 0xFFFF };

    void output(int code, OutputStream outs) throws IOException {
        cur_accum &= masks[cur_bits];
        if (cur_bits > 0) cur_accum |= (code << cur_bits); else cur_accum = code;
        cur_bits += n_bits;
        while (cur_bits >= 8) {
            char_out((byte) (cur_accum & 0xff), outs);
            cur_accum >>= 8;
            cur_bits -= 8;
        }
        if (free_ent > maxcode || clear_flg) {
            if (clear_flg) {
                maxcode = MAXCODE(n_bits = g_init_bits);
                clear_flg = false;
            } else {
                ++n_bits;
                if (n_bits == maxbits) maxcode = maxmaxcode; else maxcode = MAXCODE(n_bits);
            }
        }
        if (code == EOFCode) {
            while (cur_bits > 0) {
                char_out((byte) (cur_accum & 0xff), outs);
                cur_accum >>= 8;
                cur_bits -= 8;
            }
            flush_char(outs);
        }
    }

    void cl_block(OutputStream outs) throws IOException {
        cl_hash(hsize);
        free_ent = ClearCode + 2;
        clear_flg = true;
        output(ClearCode, outs);
    }

    void cl_hash(int hsize) {
        for (int i = 0; i < hsize; ++i) htab[i] = -1;
    }

    int a_count;

    void char_init() {
        a_count = 0;
    }

    byte[] accum = new byte[256];

    void char_out(byte c, OutputStream outs) throws IOException {
        accum[a_count++] = c;
        if (a_count >= 254) flush_char(outs);
    }

    void flush_char(OutputStream outs) throws IOException {
        if (a_count > 0) {
            outs.write(a_count);
            outs.write(accum, 0, a_count);
            a_count = 0;
        }
    }

    public synchronized void encode() throws IOException {
        encoding = true;
        iox = null;
        producer.startProduction(this);
        while (encoding) try {
            wait();
        } catch (InterruptedException e) {
        }
        if (iox != null) throw iox;
    }

    private boolean accumulate = false;

    private int[] accumulator;

    private void encodePixelsWrapper(int x, int y, int w, int h, int[] rgbPixels, int off, int scansize) throws IOException {
        if (!started) {
            started = true;
            encodeStart(width, height);
            if ((hintflags & TOPDOWNLEFTRIGHT) == 0) {
                accumulate = true;
                accumulator = new int[width * height];
            }
        }
        if (accumulate) for (int row = 0; row < h; ++row) System.arraycopy(rgbPixels, row * scansize + off, accumulator, (y + row) * width + x, w); else encodePixels(x, y, w, h, rgbPixels, off, scansize);
    }

    private void encodeFinish() throws IOException {
        if (accumulate) {
            encodePixels(0, 0, width, height, accumulator, 0, width);
            accumulator = null;
            accumulate = false;
        }
    }

    private synchronized void stop() {
        encoding = false;
        notifyAll();
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setProperties(Hashtable props) {
        this.props = props;
    }

    public void setColorModel(ColorModel model) {
    }

    public void setHints(int hintflags) {
        this.hintflags = hintflags;
    }

    public void setPixels(int x, int y, int w, int h, ColorModel model, byte[] pixels, int off, int scansize) {
        int[] rgbPixels = new int[w];
        for (int row = 0; row < h; ++row) {
            int rowOff = off + row * scansize;
            for (int col = 0; col < w; ++col) rgbPixels[col] = model.getRGB(pixels[rowOff + col] & 0xff);
            try {
                encodePixelsWrapper(x, y + row, w, 1, rgbPixels, 0, w);
            } catch (IOException e) {
                iox = e;
                stop();
                return;
            }
        }
    }

    public void setPixels(int x, int y, int w, int h, ColorModel model, int[] pixels, int off, int scansize) {
        if (model == rgbModel) {
            try {
                encodePixelsWrapper(x, y, w, h, pixels, off, scansize);
            } catch (IOException e) {
                iox = e;
                stop();
                return;
            }
        } else {
            int[] rgbPixels = new int[w];
            for (int row = 0; row < h; ++row) {
                int rowOff = off + row * scansize;
                for (int col = 0; col < w; ++col) rgbPixels[col] = model.getRGB(pixels[rowOff + col]);
                try {
                    encodePixelsWrapper(x, y + row, w, 1, rgbPixels, 0, w);
                } catch (IOException e) {
                    iox = e;
                    stop();
                    return;
                }
            }
        }
    }

    public void imageComplete(int status) {
        producer.removeConsumer(this);
        if (status == ImageConsumer.IMAGEABORTED) iox = new IOException("image aborted"); else {
            try {
                encodeFinish();
                encodeDone();
            } catch (IOException e) {
                iox = e;
            }
        }
        stop();
    }
}

class GifEncoderHashitem {

    public int rgb;

    public int count;

    public int index;

    public boolean isTransparent;

    public GifEncoderHashitem(int rgb, int count, int index, boolean isTransparent) {
        this.rgb = rgb;
        this.count = count;
        this.index = index;
        this.isTransparent = isTransparent;
    }
}

class IntHashtable extends Dictionary implements Cloneable {

    private IntHashtableEntry table[];

    private int count;

    private int threshold;

    private float loadFactor;

    public IntHashtable(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0 || loadFactor <= 0.0) throw new IllegalArgumentException();
        this.loadFactor = loadFactor;
        table = new IntHashtableEntry[initialCapacity];
        threshold = (int) (initialCapacity * loadFactor);
    }

    public IntHashtable(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public IntHashtable() {
        this(101, 0.75f);
    }

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public synchronized Enumeration keys() {
        return new IntHashtableEnumerator(table, true);
    }

    public synchronized Enumeration elements() {
        return new IntHashtableEnumerator(table, false);
    }

    public synchronized boolean contains(Object value) {
        if (value == null) throw new NullPointerException();
        IntHashtableEntry tab[] = table;
        for (int i = tab.length; i-- > 0; ) {
            for (IntHashtableEntry e = tab[i]; e != null; e = e.next) {
                if (e.value.equals(value)) return true;
            }
        }
        return false;
    }

    public synchronized boolean containsKey(int key) {
        IntHashtableEntry tab[] = table;
        int hash = key;
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (IntHashtableEntry e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash && e.key == key) return true;
        }
        return false;
    }

    public synchronized Object get(int key) {
        IntHashtableEntry tab[] = table;
        int hash = key;
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (IntHashtableEntry e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash && e.key == key) return e.value;
        }
        return null;
    }

    public Object get(Object okey) {
        if (!(okey instanceof Integer)) throw new InternalError("key is not an Integer");
        Integer ikey = (Integer) okey;
        int key = ikey.intValue();
        return get(key);
    }

    protected void rehash() {
        int oldCapacity = table.length;
        IntHashtableEntry oldTable[] = table;
        int newCapacity = oldCapacity * 2 + 1;
        IntHashtableEntry newTable[] = new IntHashtableEntry[newCapacity];
        threshold = (int) (newCapacity * loadFactor);
        table = newTable;
        for (int i = oldCapacity; i-- > 0; ) {
            for (IntHashtableEntry old = oldTable[i]; old != null; ) {
                IntHashtableEntry e = old;
                old = old.next;
                int index = (e.hash & 0x7FFFFFFF) % newCapacity;
                e.next = newTable[index];
                newTable[index] = e;
            }
        }
    }

    public synchronized Object put(int key, Object value) {
        if (value == null) throw new NullPointerException();
        IntHashtableEntry tab[] = table;
        int hash = key;
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (IntHashtableEntry e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash && e.key == key) {
                Object old = e.value;
                e.value = value;
                return old;
            }
        }
        if (count >= threshold) {
            rehash();
            return put(key, value);
        }
        IntHashtableEntry e = new IntHashtableEntry();
        e.hash = hash;
        e.key = key;
        e.value = value;
        e.next = tab[index];
        tab[index] = e;
        ++count;
        return null;
    }

    public Object put(Object okey, Object value) {
        if (!(okey instanceof Integer)) throw new InternalError("key is not an Integer");
        Integer ikey = (Integer) okey;
        int key = ikey.intValue();
        return put(key, value);
    }

    public synchronized Object remove(int key) {
        IntHashtableEntry tab[] = table;
        int hash = key;
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (IntHashtableEntry e = tab[index], prev = null; e != null; prev = e, e = e.next) {
            if (e.hash == hash && e.key == key) {
                if (prev != null) prev.next = e.next; else tab[index] = e.next;
                --count;
                return e.value;
            }
        }
        return null;
    }

    public Object remove(Object okey) {
        if (!(okey instanceof Integer)) throw new InternalError("key is not an Integer");
        Integer ikey = (Integer) okey;
        int key = ikey.intValue();
        return remove(key);
    }

    public synchronized void clear() {
        IntHashtableEntry tab[] = table;
        for (int index = tab.length; --index >= 0; ) tab[index] = null;
        count = 0;
    }

    public synchronized Object clone() {
        try {
            IntHashtable t = (IntHashtable) super.clone();
            t.table = new IntHashtableEntry[table.length];
            for (int i = table.length; i-- > 0; ) t.table[i] = (table[i] != null) ? (IntHashtableEntry) table[i].clone() : null;
            return t;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public synchronized String toString() {
        int max = size() - 1;
        StringBuffer buf = new StringBuffer();
        Enumeration k = keys();
        Enumeration e = elements();
        buf.append("{");
        for (int i = 0; i <= max; ++i) {
            String s1 = k.nextElement().toString();
            String s2 = e.nextElement().toString();
            buf.append(s1 + "=" + s2);
            if (i < max) buf.append(", ");
        }
        buf.append("}");
        return buf.toString();
    }
}

class IntHashtableEntry {

    int hash;

    int key;

    Object value;

    IntHashtableEntry next;

    protected Object clone() {
        IntHashtableEntry entry = new IntHashtableEntry();
        entry.hash = hash;
        entry.key = key;
        entry.value = value;
        entry.next = (next != null) ? (IntHashtableEntry) next.clone() : null;
        return entry;
    }
}

class IntHashtableEnumerator implements Enumeration {

    boolean keys;

    int index;

    IntHashtableEntry table[];

    IntHashtableEntry entry;

    IntHashtableEnumerator(IntHashtableEntry table[], boolean keys) {
        this.table = table;
        this.keys = keys;
        this.index = table.length;
    }

    public boolean hasMoreElements() {
        if (entry != null) return true;
        while (index-- > 0) if ((entry = table[index]) != null) return true;
        return false;
    }

    public Object nextElement() {
        if (entry == null) while ((index-- > 0) && ((entry = table[index]) == null)) ;
        if (entry != null) {
            IntHashtableEntry e = entry;
            entry = e.next;
            return keys ? new Integer(e.key) : e.value;
        }
        throw new NoSuchElementException("IntHashtableEnumerator");
    }
}
