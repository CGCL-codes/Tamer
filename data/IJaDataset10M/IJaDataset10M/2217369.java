package net.sf.javadc.util.zlib;

final class InfBlocks {

    private static final int MANY = 1440;

    private static final int[] inflate_mask = { 0x00000000, 0x00000001, 0x00000003, 0x00000007, 0x0000000f, 0x0000001f, 0x0000003f, 0x0000007f, 0x000000ff, 0x000001ff, 0x000003ff, 0x000007ff, 0x00000fff, 0x00001fff, 0x00003fff, 0x00007fff, 0x0000ffff };

    static final int[] border = { 16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15 };

    private static final int Z_OK = 0;

    private static final int Z_STREAM_END = 1;

    private static final int Z_NEED_DICT = 2;

    private static final int Z_ERRNO = -1;

    private static final int Z_STREAM_ERROR = -2;

    private static final int Z_DATA_ERROR = -3;

    private static final int Z_MEM_ERROR = -4;

    private static final int Z_BUF_ERROR = -5;

    private static final int Z_VERSION_ERROR = -6;

    private static final int TYPE = 0;

    private static final int LENS = 1;

    private static final int STORED = 2;

    private static final int TABLE = 3;

    private static final int BTREE = 4;

    private static final int DTREE = 5;

    private static final int CODES = 6;

    private static final int DRY = 7;

    private static final int DONE = 8;

    private static final int BAD = 9;

    int mode;

    int left;

    int table;

    int index;

    int[] blens;

    int[] bb = new int[1];

    int[] tb = new int[1];

    InfCodes codes;

    int last;

    int bitk;

    int bitb;

    int[] hufts;

    byte[] window;

    int end;

    int read;

    int write;

    Object checkfn;

    long check;

    InfBlocks(ZStream z, Object checkfn, int w) {
        hufts = new int[MANY * 3];
        window = new byte[w];
        end = w;
        this.checkfn = checkfn;
        mode = TYPE;
        reset(z, null);
    }

    void free(ZStream z) {
        reset(z, null);
        window = null;
        hufts = null;
    }

    int inflate_flush(ZStream z, int r) {
        int n;
        int p;
        int q;
        p = z.next_out_index;
        q = read;
        n = ((q <= write ? write : end) - q);
        if (n > z.avail_out) {
            n = z.avail_out;
        }
        if (n != 0 && r == Z_BUF_ERROR) {
            r = Z_OK;
        }
        z.avail_out -= n;
        z.total_out += n;
        if (checkfn != null) {
            z.adler = check = z._adler.adler32(check, window, q, n);
        }
        System.arraycopy(window, q, z.next_out, p, n);
        p += n;
        q += n;
        if (q == end) {
            q = 0;
            if (write == end) {
                write = 0;
            }
            n = write - q;
            if (n > z.avail_out) {
                n = z.avail_out;
            }
            if (n != 0 && r == Z_BUF_ERROR) {
                r = Z_OK;
            }
            z.avail_out -= n;
            z.total_out += n;
            if (checkfn != null) {
                z.adler = check = z._adler.adler32(check, window, q, n);
            }
            System.arraycopy(window, q, z.next_out, p, n);
            p += n;
            q += n;
        }
        z.next_out_index = p;
        read = q;
        return r;
    }

    int proc(ZStream z, int r) {
        int t;
        int b;
        int k;
        int p;
        int n;
        int q;
        int m;
        {
            p = z.next_in_index;
            n = z.avail_in;
            b = bitb;
            k = bitk;
        }
        {
            q = write;
            m = (q < read ? read - q - 1 : end - q);
        }
        while (true) {
            switch(mode) {
                case TYPE:
                    while (k < 3) {
                        if (n != 0) {
                            r = Z_OK;
                        } else {
                            bitb = b;
                            bitk = k;
                            z.avail_in = n;
                            z.total_in += p - z.next_in_index;
                            z.next_in_index = p;
                            write = q;
                            return inflate_flush(z, r);
                        }
                        ;
                        n--;
                        b |= (z.next_in[p++] & 0xff) << k;
                        k += 8;
                    }
                    t = (b & 7);
                    last = t & 1;
                    switch(t >>> 1) {
                        case 0:
                            {
                                b >>>= 3;
                                k -= 3;
                            }
                            t = k & 7;
                            {
                                b >>>= t;
                                k -= t;
                            }
                            mode = LENS;
                            break;
                        case 1:
                            {
                                int[] bl = new int[1];
                                int[] bd = new int[1];
                                int[][] tl = new int[1][];
                                int[][] td = new int[1][];
                                InfTree.inflate_trees_fixed(bl, bd, tl, td, z);
                                codes = new InfCodes(bl[0], bd[0], tl[0], td[0], z);
                            }
                            {
                                b >>>= 3;
                                k -= 3;
                            }
                            mode = CODES;
                            break;
                        case 2:
                            {
                                b >>>= 3;
                                k -= 3;
                            }
                            mode = TABLE;
                            break;
                        case 3:
                            {
                                b >>>= 3;
                                k -= 3;
                            }
                            mode = BAD;
                            z.msg = "invalid block type";
                            r = Z_DATA_ERROR;
                            bitb = b;
                            bitk = k;
                            z.avail_in = n;
                            z.total_in += p - z.next_in_index;
                            z.next_in_index = p;
                            write = q;
                            return inflate_flush(z, r);
                    }
                    break;
                case LENS:
                    while (k < 32) {
                        if (n != 0) {
                            r = Z_OK;
                        } else {
                            bitb = b;
                            bitk = k;
                            z.avail_in = n;
                            z.total_in += p - z.next_in_index;
                            z.next_in_index = p;
                            write = q;
                            return inflate_flush(z, r);
                        }
                        ;
                        n--;
                        b |= (z.next_in[p++] & 0xff) << k;
                        k += 8;
                    }
                    if ((~b >>> 16 & 0xffff) != (b & 0xffff)) {
                        mode = BAD;
                        z.msg = "invalid stored block lengths";
                        r = Z_DATA_ERROR;
                        bitb = b;
                        bitk = k;
                        z.avail_in = n;
                        z.total_in += p - z.next_in_index;
                        z.next_in_index = p;
                        write = q;
                        return inflate_flush(z, r);
                    }
                    left = b & 0xffff;
                    b = k = 0;
                    mode = left != 0 ? STORED : last != 0 ? DRY : TYPE;
                    break;
                case STORED:
                    if (n == 0) {
                        bitb = b;
                        bitk = k;
                        z.avail_in = n;
                        z.total_in += p - z.next_in_index;
                        z.next_in_index = p;
                        write = q;
                        return inflate_flush(z, r);
                    }
                    if (m == 0) {
                        if (q == end && read != 0) {
                            q = 0;
                            m = (q < read ? read - q - 1 : end - q);
                        }
                        if (m == 0) {
                            write = q;
                            r = inflate_flush(z, r);
                            q = write;
                            m = (q < read ? read - q - 1 : end - q);
                            if (q == end && read != 0) {
                                q = 0;
                                m = (q < read ? read - q - 1 : end - q);
                            }
                            if (m == 0) {
                                bitb = b;
                                bitk = k;
                                z.avail_in = n;
                                z.total_in += p - z.next_in_index;
                                z.next_in_index = p;
                                write = q;
                                return inflate_flush(z, r);
                            }
                        }
                    }
                    r = Z_OK;
                    t = left;
                    if (t > n) {
                        t = n;
                    }
                    if (t > m) {
                        t = m;
                    }
                    System.arraycopy(z.next_in, p, window, q, t);
                    p += t;
                    n -= t;
                    q += t;
                    m -= t;
                    if ((left -= t) != 0) {
                        break;
                    }
                    mode = last != 0 ? DRY : TYPE;
                    break;
                case TABLE:
                    while (k < 14) {
                        if (n != 0) {
                            r = Z_OK;
                        } else {
                            bitb = b;
                            bitk = k;
                            z.avail_in = n;
                            z.total_in += p - z.next_in_index;
                            z.next_in_index = p;
                            write = q;
                            return inflate_flush(z, r);
                        }
                        ;
                        n--;
                        b |= (z.next_in[p++] & 0xff) << k;
                        k += 8;
                    }
                    table = t = b & 0x3fff;
                    if ((t & 0x1f) > 29 || (t >> 5 & 0x1f) > 29) {
                        mode = BAD;
                        z.msg = "too many length or distance symbols";
                        r = Z_DATA_ERROR;
                        bitb = b;
                        bitk = k;
                        z.avail_in = n;
                        z.total_in += p - z.next_in_index;
                        z.next_in_index = p;
                        write = q;
                        return inflate_flush(z, r);
                    }
                    t = 258 + (t & 0x1f) + (t >> 5 & 0x1f);
                    blens = new int[t];
                    {
                        b >>>= 14;
                        k -= 14;
                    }
                    index = 0;
                    mode = BTREE;
                case BTREE:
                    while (index < 4 + (table >>> 10)) {
                        while (k < 3) {
                            if (n != 0) {
                                r = Z_OK;
                            } else {
                                bitb = b;
                                bitk = k;
                                z.avail_in = n;
                                z.total_in += p - z.next_in_index;
                                z.next_in_index = p;
                                write = q;
                                return inflate_flush(z, r);
                            }
                            ;
                            n--;
                            b |= (z.next_in[p++] & 0xff) << k;
                            k += 8;
                        }
                        blens[border[index++]] = b & 7;
                        {
                            b >>>= 3;
                            k -= 3;
                        }
                    }
                    while (index < 19) {
                        blens[border[index++]] = 0;
                    }
                    bb[0] = 7;
                    t = InfTree.inflate_trees_bits(blens, bb, tb, hufts, z);
                    if (t != Z_OK) {
                        r = t;
                        if (r == Z_DATA_ERROR) {
                            blens = null;
                            mode = BAD;
                        }
                        bitb = b;
                        bitk = k;
                        z.avail_in = n;
                        z.total_in += p - z.next_in_index;
                        z.next_in_index = p;
                        write = q;
                        return inflate_flush(z, r);
                    }
                    index = 0;
                    mode = DTREE;
                case DTREE:
                    while (true) {
                        t = table;
                        if (!(index < 258 + (t & 0x1f) + (t >> 5 & 0x1f))) {
                            break;
                        }
                        int[] h;
                        int i, j, c;
                        t = bb[0];
                        while (k < t) {
                            if (n != 0) {
                                r = Z_OK;
                            } else {
                                bitb = b;
                                bitk = k;
                                z.avail_in = n;
                                z.total_in += p - z.next_in_index;
                                z.next_in_index = p;
                                write = q;
                                return inflate_flush(z, r);
                            }
                            ;
                            n--;
                            b |= (z.next_in[p++] & 0xff) << k;
                            k += 8;
                        }
                        if (tb[0] == -1) {
                        }
                        t = hufts[(tb[0] + (b & inflate_mask[t])) * 3 + 1];
                        c = hufts[(tb[0] + (b & inflate_mask[t])) * 3 + 2];
                        if (c < 16) {
                            b >>>= t;
                            k -= t;
                            blens[index++] = c;
                        } else {
                            i = c == 18 ? 7 : c - 14;
                            j = c == 18 ? 11 : 3;
                            while (k < t + i) {
                                if (n != 0) {
                                    r = Z_OK;
                                } else {
                                    bitb = b;
                                    bitk = k;
                                    z.avail_in = n;
                                    z.total_in += p - z.next_in_index;
                                    z.next_in_index = p;
                                    write = q;
                                    return inflate_flush(z, r);
                                }
                                ;
                                n--;
                                b |= (z.next_in[p++] & 0xff) << k;
                                k += 8;
                            }
                            b >>>= t;
                            k -= t;
                            j += b & inflate_mask[i];
                            b >>>= i;
                            k -= i;
                            i = index;
                            t = table;
                            if (i + j > 258 + (t & 0x1f) + (t >> 5 & 0x1f) || c == 16 && i < 1) {
                                blens = null;
                                mode = BAD;
                                z.msg = "invalid bit length repeat";
                                r = Z_DATA_ERROR;
                                bitb = b;
                                bitk = k;
                                z.avail_in = n;
                                z.total_in += p - z.next_in_index;
                                z.next_in_index = p;
                                write = q;
                                return inflate_flush(z, r);
                            }
                            c = c == 16 ? blens[i - 1] : 0;
                            do {
                                blens[i++] = c;
                            } while (--j != 0);
                            index = i;
                        }
                    }
                    tb[0] = -1;
                    {
                        int[] bl = new int[1];
                        int[] bd = new int[1];
                        int[] tl = new int[1];
                        int[] td = new int[1];
                        InfCodes c;
                        bl[0] = 9;
                        bd[0] = 6;
                        t = table;
                        t = InfTree.inflate_trees_dynamic(257 + (t & 0x1f), 1 + (t >> 5 & 0x1f), blens, bl, bd, tl, td, hufts, z);
                        if (t != Z_OK) {
                            if (t == Z_DATA_ERROR) {
                                blens = null;
                                mode = BAD;
                            }
                            r = t;
                            bitb = b;
                            bitk = k;
                            z.avail_in = n;
                            z.total_in += p - z.next_in_index;
                            z.next_in_index = p;
                            write = q;
                            return inflate_flush(z, r);
                        }
                        codes = new InfCodes(bl[0], bd[0], hufts, tl[0], hufts, td[0], z);
                    }
                    blens = null;
                    mode = CODES;
                case CODES:
                    bitb = b;
                    bitk = k;
                    z.avail_in = n;
                    z.total_in += p - z.next_in_index;
                    z.next_in_index = p;
                    write = q;
                    if ((r = codes.proc(this, z, r)) != Z_STREAM_END) {
                        return inflate_flush(z, r);
                    }
                    r = Z_OK;
                    codes.free(z);
                    p = z.next_in_index;
                    n = z.avail_in;
                    b = bitb;
                    k = bitk;
                    q = write;
                    m = (q < read ? read - q - 1 : end - q);
                    if (last == 0) {
                        mode = TYPE;
                        break;
                    }
                    mode = DRY;
                case DRY:
                    write = q;
                    r = inflate_flush(z, r);
                    q = write;
                    m = (q < read ? read - q - 1 : end - q);
                    if (read != write) {
                        bitb = b;
                        bitk = k;
                        z.avail_in = n;
                        z.total_in += p - z.next_in_index;
                        z.next_in_index = p;
                        write = q;
                        return inflate_flush(z, r);
                    }
                    mode = DONE;
                case DONE:
                    r = Z_STREAM_END;
                    bitb = b;
                    bitk = k;
                    z.avail_in = n;
                    z.total_in += p - z.next_in_index;
                    z.next_in_index = p;
                    write = q;
                    return inflate_flush(z, r);
                case BAD:
                    r = Z_DATA_ERROR;
                    bitb = b;
                    bitk = k;
                    z.avail_in = n;
                    z.total_in += p - z.next_in_index;
                    z.next_in_index = p;
                    write = q;
                    return inflate_flush(z, r);
                default:
                    r = Z_STREAM_ERROR;
                    bitb = b;
                    bitk = k;
                    z.avail_in = n;
                    z.total_in += p - z.next_in_index;
                    z.next_in_index = p;
                    write = q;
                    return inflate_flush(z, r);
            }
        }
    }

    void reset(ZStream z, long[] c) {
        if (c != null) {
            c[0] = check;
        }
        if (mode == BTREE || mode == DTREE) {
            blens = null;
        }
        if (mode == CODES) {
            codes.free(z);
        }
        mode = TYPE;
        bitk = 0;
        bitb = 0;
        read = write = 0;
        if (checkfn != null) {
            z.adler = check = z._adler.adler32(0L, null, 0, 0);
        }
    }

    void set_dictionary(byte[] d, int start, int n) {
        System.arraycopy(d, start, window, 0, n);
        read = write = n;
    }

    int sync_point() {
        return mode == LENS ? 1 : 0;
    }
}
