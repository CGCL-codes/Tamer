package com.exult.android;

public class ShapeFrame {

    private byte data[];

    private int datalen;

    private short xleft;

    private short xright;

    private short yabove;

    private short ybelow;

    private boolean rle;

    public byte[] getData() {
        return data;
    }

    public int getSize() {
        return datalen;
    }

    public ShapeFrame reflect() {
        if (data == null) return null;
        int w = getWidth(), h = getHeight();
        if (w < h) w = h; else h = w;
        ShapeFrame reflected = new ShapeFrame();
        reflected.rle = true;
        reflected.xleft = yabove;
        reflected.yabove = xleft;
        reflected.xright = ybelow;
        reflected.ybelow = xright;
        ImageBuf ibuf = new ImageBuf(h, w);
        ibuf.fill8((byte) 255);
        int xoff = reflected.xleft, yoff = reflected.yabove;
        int in = 0;
        int scanlen;
        while ((scanlen = EUtil.Read2(data, in)) != 0) {
            in += 2;
            boolean encoded = (scanlen & 1) != 0;
            scanlen = scanlen >> 1;
            int scanx = (short) EUtil.Read2(data, in);
            int scany = (short) EUtil.Read2(data, in + 2);
            in += 4;
            if (!encoded) {
                ibuf.copy8(data, in, 1, scanlen, xoff + scany, yoff + scanx);
                in += scanlen;
                continue;
            }
            for (int b = 0; b < scanlen; ) {
                byte bcnt = data[in++];
                boolean repeat = (bcnt & 1) != 0;
                bcnt = (byte) (bcnt >> 1);
                if (repeat) {
                    byte pix = data[in++];
                    ibuf.fill8(pix, 1, bcnt, xoff + scany, yoff + scanx + b);
                } else {
                    ibuf.copy8(data, in, 1, bcnt, xoff + scany, yoff + scanx + b);
                    in += bcnt;
                }
                b += bcnt;
            }
        }
        reflected.createRle(ibuf.getPixels(), w, h);
        return (reflected);
    }

    public ShapeFrame translatePalette(byte transTo[]) {
        if (data == null) return null;
        byte buf[] = new byte[datalen];
        System.arraycopy(data, 0, buf, 0, datalen);
        ShapeFrame newShape = new ShapeFrame();
        newShape.rle = true;
        newShape.xleft = yabove;
        newShape.yabove = xleft;
        newShape.xright = ybelow;
        newShape.ybelow = xright;
        newShape.datalen = datalen;
        newShape.data = buf;
        int in = 0;
        int scanlen;
        while ((scanlen = EUtil.Read2(data, in)) != 0) {
            in += 2;
            boolean encoded = (scanlen & 1) != 0;
            scanlen = scanlen >> 1;
            in += 4;
            if (!encoded) {
                for (int i = 0; i < scanlen; ++i) {
                    buf[in] = transTo[(int) data[in] & 0xff];
                    ++in;
                }
                continue;
            }
            for (int b = 0; b < scanlen; ) {
                byte bcnt = data[in++];
                boolean repeat = (bcnt & 1) != 0;
                bcnt = (byte) (bcnt >> 1);
                if (repeat) {
                    buf[in] = transTo[(int) data[in] & 0xff];
                    ++in;
                } else {
                    for (int i = 0; i < bcnt; ++i) {
                        buf[in] = transTo[(int) data[in] & 0xff];
                        ++in;
                    }
                }
                b += bcnt;
            }
        }
        return newShape;
    }

    public ShapeFrame() {
        data = null;
        datalen = 0;
    }

    public ShapeFrame(byte pixels[], int w, int h, int xoff, int yoff, boolean setrle) {
        xleft = (short) xoff;
        xright = (short) (w - xoff - 1);
        yabove = (short) yoff;
        ybelow = (short) (h - yoff - 1);
        rle = setrle;
        if (!rle) {
            datalen = EConst.c_num_tile_bytes;
            data = new byte[EConst.c_num_tile_bytes];
            data = pixels;
        } else {
        }
    }

    public int read(byte shapes[], int shapelen, int frnum) {
        int framenum = frnum;
        rle = false;
        if (shapelen == 0) return 0;
        int dlen = EUtil.Read4(shapes, 0);
        int hdrlen = EUtil.Read4(shapes, 4);
        if (dlen == shapelen) {
            rle = true;
            int nframes = (hdrlen - 4) / 4;
            if (framenum >= nframes) return (nframes);
            int frameoff, framelen;
            if (framenum == 0) {
                frameoff = hdrlen;
                framelen = nframes > 1 ? EUtil.Read4(shapes, 8) - frameoff : dlen - frameoff;
            } else {
                int from = 8 + (framenum - 1) * 4;
                frameoff = EUtil.Read4(shapes, from);
                if (framenum == nframes - 1) framelen = dlen - frameoff; else framelen = EUtil.Read4(shapes, from + 4) - frameoff;
            }
            getRleShape(shapes, frameoff, framelen);
            return (nframes);
        }
        framenum &= 31;
        xleft = yabove = EConst.c_tilesize;
        xright = ybelow = -1;
        data = new byte[EConst.c_num_tile_bytes];
        datalen = EConst.c_num_tile_bytes;
        System.arraycopy(shapes, framenum * EConst.c_num_tile_bytes, data, 0, datalen);
        return (shapelen / EConst.c_num_tile_bytes);
    }

    private void getRleShape(byte shapes[], int framePos, int len) {
        xright = (short) EUtil.Read2(shapes, framePos);
        xleft = (short) EUtil.Read2(shapes, framePos + 2);
        yabove = (short) EUtil.Read2(shapes, framePos + 4);
        ybelow = (short) EUtil.Read2(shapes, framePos + 6);
        len -= 8;
        data = new byte[len + 2];
        datalen = len + 2;
        System.arraycopy(shapes, framePos + 8, data, 0, len);
        data[len] = 0;
        data[len + 1] = 0;
        rle = true;
    }

    private static int SkipTransparent(byte pixels[], int ind, int x, int w) {
        while (x < w && pixels[ind] == -1) {
            x++;
            ind++;
        }
        return (x);
    }

    private static int Find_runs(short runs[], byte pixels[], int ind, int x, int w) {
        int runcnt = 0;
        while (x < w && pixels[ind] != -1) {
            int run = 0;
            while (x < w - 1 && pixels[ind] == pixels[ind + 1]) {
                x++;
                ind++;
                run++;
            }
            if (run > 0) {
                run = ((run + 1) << 1) | 1;
                x++;
                ind++;
            } else do {
                x++;
                ind++;
                run += 2;
            } while (x < w && pixels[ind] != -1 && (x == w - 1 || pixels[ind] != pixels[ind + 1]));
            runs[runcnt++] = (short) run;
        }
        runs[runcnt] = 0;
        return (x);
    }

    public static byte[] encodeRle(byte pixels[], int w, int h, int xoff, int yoff) {
        byte buf[] = new byte[w * h * 2 + 16 * h];
        int out = 0;
        int ind = 0;
        int newx;
        short runs[] = new short[200];
        for (int y = 0; y < h; y++) {
            int x, oldx = 0;
            ind = y * w;
            for (x = 0; (x = SkipTransparent(pixels, ind, x, w)) < w; x = oldx = newx) {
                ind += x - oldx;
                newx = Find_runs(runs, pixels, ind, x, w);
                if (runs[1] == 0 && (runs[0] & 1) == 0) {
                    int len = runs[0] >> 1;
                    out = EUtil.Write2(buf, out, runs[0]);
                    out = EUtil.Write2(buf, out, x - xoff);
                    out = EUtil.Write2(buf, out, y - yoff);
                    EUtil.Memcpy(buf, out, pixels, ind, len);
                    ind += len;
                    out += len;
                    continue;
                }
                out = EUtil.Write2(buf, out, ((newx - x) << 1) | 1);
                out = EUtil.Write2(buf, out, x - xoff);
                out = EUtil.Write2(buf, out, y - yoff);
                for (int i = 0; runs[i] != 0; i++) {
                    int len = runs[i] >> 1;
                    if ((runs[i] & 1) != 0) {
                        while (len > 0) {
                            int c = len > 127 ? 127 : len;
                            buf[out++] = (byte) ((c << 1) | 1);
                            buf[out++] = pixels[ind];
                            ind += c;
                            len -= c;
                        }
                    } else while (len > 0) {
                        int c = len > 127 ? 127 : len;
                        buf[out++] = (byte) (c << 1);
                        System.arraycopy(pixels, ind, buf, out, c);
                        out += c;
                        ind += c;
                        len -= c;
                    }
                }
            }
        }
        out = EUtil.Write2(buf, out, 0);
        int len = out;
        byte data[] = new byte[len];
        EUtil.Memcpy(data, 0, buf, 0, len);
        return data;
    }

    private void createRle(byte pixels[], int w, int h) {
        data = encodeRle(pixels, w, h, xleft, yabove);
        datalen = data.length;
    }

    public void createRle(byte pixels[], int xl, int ya, int w, int h) {
        xleft = (short) xl;
        yabove = (short) ya;
        xright = (short) (xleft + w - 1);
        ybelow = (short) (yabove + h - 1);
        data = encodeRle(pixels, w, h, xleft, yabove);
        datalen = data.length;
        rle = true;
    }

    public final int getWidth() {
        return xleft + xright + 1;
    }

    public final int getHeight() {
        return yabove + ybelow + 1;
    }

    public final int getXRight() {
        return xright;
    }

    public final int getXLeft() {
        return xleft;
    }

    public final int getYAbove() {
        return yabove;
    }

    public final int getYBelow() {
        return ybelow;
    }

    public final boolean isRle() {
        return rle;
    }

    public final boolean isEmpty() {
        return data[0] == 0 && data[1] == 0;
    }

    public void paint(ImageBuf win, int xoff, int yoff) {
        if (rle) paintRle(win, xoff, yoff); else win.copy8(data, 0, EConst.c_tilesize, EConst.c_tilesize, xoff - EConst.c_tilesize, yoff - EConst.c_tilesize);
    }

    public void paintRle(ImageBuf win, int xoff, int yoff) {
        int w = getWidth(), h = getHeight();
        if (w >= EConst.c_tilesize || h >= EConst.c_tilesize) {
            if (!win.isVisible(xoff - xleft, yoff - yabove, w, h)) return;
        }
        win.paintRle(xoff, yoff, data);
    }

    public void paintRleTranslucent(ImageBuf win, int xoff, int yoff, ImageBuf.XformPalette xforms[]) {
        int xfcnt = xforms.length;
        assert (rle);
        int w = getWidth(), h = getHeight();
        if (w >= EConst.c_tilesize || h >= EConst.c_tilesize) if (!win.isVisible(xoff - xleft, yoff - yabove, w, h)) return;
        int xfstart = 0xff - xfcnt;
        int in = 0;
        int scanlen;
        while ((scanlen = EUtil.Read2(data, in)) != 0) {
            in += 2;
            boolean encoded = (scanlen & 1) != 0;
            scanlen = scanlen >> 1;
            int scanx = (short) EUtil.Read2(data, in);
            in += 2;
            int scany = (short) EUtil.Read2(data, in);
            in += 2;
            if (!encoded) {
                win.copyLineTranslucent8(data, in, scanlen, xoff + scanx, yoff + scany, xfstart, 0xfe, xforms);
                in += scanlen;
                continue;
            }
            for (int b = 0; b < scanlen; ) {
                int bcnt = data[in++] & 0xff;
                boolean repeat = (bcnt & 1) != 0;
                bcnt = bcnt >> 1;
                if (repeat) {
                    int pix = data[in++] & 0xff;
                    if (pix >= xfstart && pix <= 0xfe) win.fillLineTranslucent8(bcnt, xoff + scanx + b, yoff + scany, xforms[pix - xfstart]); else win.fillLine8((byte) pix, bcnt, xoff + scanx + b, yoff + scany);
                } else {
                    win.copyLineTranslucent8(data, in, bcnt, xoff + scanx + b, yoff + scany, xfstart, 0xfe, xforms);
                    in += bcnt;
                }
                b += bcnt;
            }
        }
    }

    public void paintRleTransformed(ImageBuf win, int xoff, int yoff, ImageBuf.XformPalette xform) {
        assert (rle);
        int w = getWidth(), h = getHeight();
        if (w >= EConst.c_tilesize || h >= EConst.c_tilesize) if (!win.isVisible(xoff - xleft, yoff - yabove, w, h)) return;
        int in = 0;
        int scanlen;
        while ((scanlen = EUtil.Read2(data, in)) != 0) {
            in += 2;
            int encoded = scanlen & 1;
            scanlen = scanlen >> 1;
            short scanx = (short) EUtil.Read2(data, in);
            short scany = (short) EUtil.Read2(data, in + 2);
            in += 4;
            if (encoded == 0) {
                win.fillLineTranslucent8(scanlen, xoff + scanx, yoff + scany, xform);
                in += scanlen;
                continue;
            }
            for (int b = 0; b < scanlen; ) {
                int bcnt = (int) data[in++] & 0xff;
                int repeat = bcnt & 1;
                bcnt = bcnt >> 1;
                in += repeat != 0 ? 1 : bcnt;
                win.fillLineTranslucent8(bcnt, xoff + scanx + b, yoff + scany, xform);
                b += bcnt;
            }
        }
    }

    public void paintRleOutline(ImageBuf win, int xoff, int yoff, byte color) {
        assert (rle);
        int w = getWidth(), h = getHeight();
        if (w >= EConst.c_tilesize || h >= EConst.c_tilesize) if (!win.isVisible(xoff - xleft, yoff - yabove, w, h)) return;
        int firsty = -10000;
        int lasty = -10000;
        int ind = 0;
        int scanlen;
        while ((scanlen = EUtil.Read2(data, ind)) != 0) {
            ind += 2;
            int encoded = scanlen & 1;
            scanlen = scanlen >> 1;
            short scanx = (short) EUtil.Read2(data, ind);
            short scany = (short) EUtil.Read2(data, ind + 2);
            ind += 4;
            int x = xoff + scanx;
            int y = yoff + scany;
            if (firsty == -10000) {
                firsty = y;
                lasty = y + h - 1;
            }
            win.putPixel(color, x, y);
            win.putPixel(color, x + scanlen - 1, y);
            if (encoded == 0) {
                if (y == firsty || y == lasty) win.fillLine8(color, scanlen, x, y);
                ind += scanlen;
                continue;
            }
            for (int b = 0; b < scanlen; ) {
                int bcnt = (int) (data[ind++]) & 0xff;
                int repeat = bcnt & 1;
                bcnt = bcnt >> 1;
                if (repeat != 0) ind++; else ind += bcnt;
                if (y == firsty || y == lasty) win.fillLine8(color, bcnt, x + b, y);
                b += bcnt;
            }
        }
    }

    public final boolean hasPoint(int x, int y) {
        if (!rle) {
            return x >= -xleft && x < xright && y >= -yabove && y < ybelow;
        }
        int in = 0;
        int scanlen;
        while ((scanlen = EUtil.Read2(data, in)) != 0) {
            in += 2;
            boolean encoded = (scanlen & 1) != 0;
            scanlen = scanlen >> 1;
            int scanx = (short) EUtil.Read2(data, in);
            in += 2;
            int scany = (short) EUtil.Read2(data, in);
            in += 2;
            if (y == scany && x >= scanx - 1 && x <= scanx + scanlen) return (true);
            if (!encoded) {
                in += scanlen;
                continue;
            }
            for (int b = 0; b < scanlen; ) {
                byte bcnt = data[in++];
                int repeat = bcnt & 1;
                bcnt = (byte) ((bcnt & 0xff) >> 1);
                if (repeat != 0) in++; else in += bcnt;
                b += bcnt;
            }
        }
        return false;
    }

    public final boolean boxHasPoint(int x, int y) {
        return x >= -xleft && x < xright && y >= -yabove && y < ybelow;
    }
}
