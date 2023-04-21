package ch.comtools.jsch.jzlib;

public final class Deflate {

    private static final int MAX_MEM_LEVEL = 9;

    private static final int Z_DEFAULT_COMPRESSION = -1;

    private static final int MAX_WBITS = 15;

    private static final int DEF_MEM_LEVEL = 8;

    static class Config {

        int good_length;

        int max_lazy;

        int nice_length;

        int max_chain;

        int func;

        Config(int good_length, int max_lazy, int nice_length, int max_chain, int func) {
            this.good_length = good_length;
            this.max_lazy = max_lazy;
            this.nice_length = nice_length;
            this.max_chain = max_chain;
            this.func = func;
        }
    }

    private static final int STORED = 0;

    private static final int FAST = 1;

    private static final int SLOW = 2;

    private static final Config[] config_table;

    static {
        config_table = new Config[10];
        config_table[0] = new Config(0, 0, 0, 0, STORED);
        config_table[1] = new Config(4, 4, 8, 4, FAST);
        config_table[2] = new Config(4, 5, 16, 8, FAST);
        config_table[3] = new Config(4, 6, 32, 32, FAST);
        config_table[4] = new Config(4, 4, 16, 16, SLOW);
        config_table[5] = new Config(8, 16, 32, 32, SLOW);
        config_table[6] = new Config(8, 16, 128, 128, SLOW);
        config_table[7] = new Config(8, 32, 128, 256, SLOW);
        config_table[8] = new Config(32, 128, 258, 1024, SLOW);
        config_table[9] = new Config(32, 258, 258, 4096, SLOW);
    }

    private static final String[] z_errmsg = { "need dictionary", "stream end", "", "file error", "stream error", "data error", "insufficient memory", "buffer error", "incompatible version", "" };

    private static final int NeedMore = 0;

    private static final int BlockDone = 1;

    private static final int FinishStarted = 2;

    private static final int FinishDone = 3;

    private static final int PRESET_DICT = 0x20;

    private static final int Z_FILTERED = 1;

    private static final int Z_HUFFMAN_ONLY = 2;

    private static final int Z_DEFAULT_STRATEGY = 0;

    private static final int Z_NO_FLUSH = 0;

    private static final int Z_PARTIAL_FLUSH = 1;

    private static final int Z_SYNC_FLUSH = 2;

    private static final int Z_FULL_FLUSH = 3;

    private static final int Z_FINISH = 4;

    private static final int Z_OK = 0;

    private static final int Z_STREAM_END = 1;

    private static final int Z_NEED_DICT = 2;

    private static final int Z_ERRNO = -1;

    private static final int Z_STREAM_ERROR = -2;

    private static final int Z_DATA_ERROR = -3;

    private static final int Z_MEM_ERROR = -4;

    private static final int Z_BUF_ERROR = -5;

    private static final int Z_VERSION_ERROR = -6;

    private static final int INIT_STATE = 42;

    private static final int BUSY_STATE = 113;

    private static final int FINISH_STATE = 666;

    private static final int Z_DEFLATED = 8;

    private static final int STORED_BLOCK = 0;

    private static final int STATIC_TREES = 1;

    private static final int DYN_TREES = 2;

    private static final int Z_BINARY = 0;

    private static final int Z_ASCII = 1;

    private static final int Z_UNKNOWN = 2;

    private static final int Buf_size = 8 * 2;

    private static final int REP_3_6 = 16;

    private static final int REPZ_3_10 = 17;

    private static final int REPZ_11_138 = 18;

    private static final int MIN_MATCH = 3;

    private static final int MAX_MATCH = 258;

    private static final int MIN_LOOKAHEAD = (MAX_MATCH + MIN_MATCH + 1);

    private static final int MAX_BITS = 15;

    private static final int D_CODES = 30;

    private static final int BL_CODES = 19;

    private static final int LENGTH_CODES = 29;

    private static final int LITERALS = 256;

    private static final int L_CODES = (LITERALS + 1 + LENGTH_CODES);

    private static final int HEAP_SIZE = (2 * L_CODES + 1);

    private static final int END_BLOCK = 256;

    ZStream strm;

    int status;

    byte[] pending_buf;

    int pending_buf_size;

    int pending_out;

    int pending;

    int noheader;

    byte data_type;

    byte method;

    int last_flush;

    int w_size;

    int w_bits;

    int w_mask;

    byte[] window;

    int window_size;

    short[] prev;

    short[] head;

    int ins_h;

    int hash_size;

    int hash_bits;

    int hash_mask;

    int hash_shift;

    int block_start;

    int match_length;

    int prev_match;

    int match_available;

    int strstart;

    int match_start;

    int lookahead;

    int prev_length;

    int max_chain_length;

    int max_lazy_match;

    int level;

    int strategy;

    int good_match;

    int nice_match;

    short[] dyn_ltree;

    short[] dyn_dtree;

    short[] bl_tree;

    Tree l_desc = new Tree();

    Tree d_desc = new Tree();

    Tree bl_desc = new Tree();

    short[] bl_count = new short[MAX_BITS + 1];

    int[] heap = new int[2 * L_CODES + 1];

    int heap_len;

    int heap_max;

    byte[] depth = new byte[2 * L_CODES + 1];

    int l_buf;

    int lit_bufsize;

    int last_lit;

    int d_buf;

    int opt_len;

    int static_len;

    int matches;

    int last_eob_len;

    short bi_buf;

    int bi_valid;

    Deflate() {
        dyn_ltree = new short[HEAP_SIZE * 2];
        dyn_dtree = new short[(2 * D_CODES + 1) * 2];
        bl_tree = new short[(2 * BL_CODES + 1) * 2];
    }

    void lm_init() {
        window_size = 2 * w_size;
        head[hash_size - 1] = 0;
        for (int i = 0; i < hash_size - 1; i++) {
            head[i] = 0;
        }
        max_lazy_match = Deflate.config_table[level].max_lazy;
        good_match = Deflate.config_table[level].good_length;
        nice_match = Deflate.config_table[level].nice_length;
        max_chain_length = Deflate.config_table[level].max_chain;
        strstart = 0;
        block_start = 0;
        lookahead = 0;
        match_length = prev_length = MIN_MATCH - 1;
        match_available = 0;
        ins_h = 0;
    }

    void tr_init() {
        l_desc.dyn_tree = dyn_ltree;
        l_desc.stat_desc = StaticTree.static_l_desc;
        d_desc.dyn_tree = dyn_dtree;
        d_desc.stat_desc = StaticTree.static_d_desc;
        bl_desc.dyn_tree = bl_tree;
        bl_desc.stat_desc = StaticTree.static_bl_desc;
        bi_buf = 0;
        bi_valid = 0;
        last_eob_len = 8;
        init_block();
    }

    void init_block() {
        for (int i = 0; i < L_CODES; i++) dyn_ltree[i * 2] = 0;
        for (int i = 0; i < D_CODES; i++) dyn_dtree[i * 2] = 0;
        for (int i = 0; i < BL_CODES; i++) bl_tree[i * 2] = 0;
        dyn_ltree[END_BLOCK * 2] = 1;
        opt_len = static_len = 0;
        last_lit = matches = 0;
    }

    void pqdownheap(short[] tree, int k) {
        int v = heap[k];
        int j = k << 1;
        while (j <= heap_len) {
            if (j < heap_len && smaller(tree, heap[j + 1], heap[j], depth)) {
                j++;
            }
            if (smaller(tree, v, heap[j], depth)) break;
            heap[k] = heap[j];
            k = j;
            j <<= 1;
        }
        heap[k] = v;
    }

    static boolean smaller(short[] tree, int n, int m, byte[] depth) {
        short tn2 = tree[n * 2];
        short tm2 = tree[m * 2];
        return (tn2 < tm2 || (tn2 == tm2 && depth[n] <= depth[m]));
    }

    void scan_tree(short[] tree, int max_code) {
        int n;
        int prevlen = -1;
        int curlen;
        int nextlen = tree[0 * 2 + 1];
        int count = 0;
        int max_count = 7;
        int min_count = 4;
        if (nextlen == 0) {
            max_count = 138;
            min_count = 3;
        }
        tree[(max_code + 1) * 2 + 1] = (short) 0xffff;
        for (n = 0; n <= max_code; n++) {
            curlen = nextlen;
            nextlen = tree[(n + 1) * 2 + 1];
            if (++count < max_count && curlen == nextlen) {
                continue;
            } else if (count < min_count) {
                bl_tree[curlen * 2] += count;
            } else if (curlen != 0) {
                if (curlen != prevlen) bl_tree[curlen * 2]++;
                bl_tree[REP_3_6 * 2]++;
            } else if (count <= 10) {
                bl_tree[REPZ_3_10 * 2]++;
            } else {
                bl_tree[REPZ_11_138 * 2]++;
            }
            count = 0;
            prevlen = curlen;
            if (nextlen == 0) {
                max_count = 138;
                min_count = 3;
            } else if (curlen == nextlen) {
                max_count = 6;
                min_count = 3;
            } else {
                max_count = 7;
                min_count = 4;
            }
        }
    }

    int build_bl_tree() {
        int max_blindex;
        scan_tree(dyn_ltree, l_desc.max_code);
        scan_tree(dyn_dtree, d_desc.max_code);
        bl_desc.build_tree(this);
        for (max_blindex = BL_CODES - 1; max_blindex >= 3; max_blindex--) {
            if (bl_tree[Tree.bl_order[max_blindex] * 2 + 1] != 0) break;
        }
        opt_len += 3 * (max_blindex + 1) + 5 + 5 + 4;
        return max_blindex;
    }

    void send_all_trees(int lcodes, int dcodes, int blcodes) {
        int rank;
        send_bits(lcodes - 257, 5);
        send_bits(dcodes - 1, 5);
        send_bits(blcodes - 4, 4);
        for (rank = 0; rank < blcodes; rank++) {
            send_bits(bl_tree[Tree.bl_order[rank] * 2 + 1], 3);
        }
        send_tree(dyn_ltree, lcodes - 1);
        send_tree(dyn_dtree, dcodes - 1);
    }

    void send_tree(short[] tree, int max_code) {
        int n;
        int prevlen = -1;
        int curlen;
        int nextlen = tree[0 * 2 + 1];
        int count = 0;
        int max_count = 7;
        int min_count = 4;
        if (nextlen == 0) {
            max_count = 138;
            min_count = 3;
        }
        for (n = 0; n <= max_code; n++) {
            curlen = nextlen;
            nextlen = tree[(n + 1) * 2 + 1];
            if (++count < max_count && curlen == nextlen) {
                continue;
            } else if (count < min_count) {
                do {
                    send_code(curlen, bl_tree);
                } while (--count != 0);
            } else if (curlen != 0) {
                if (curlen != prevlen) {
                    send_code(curlen, bl_tree);
                    count--;
                }
                send_code(REP_3_6, bl_tree);
                send_bits(count - 3, 2);
            } else if (count <= 10) {
                send_code(REPZ_3_10, bl_tree);
                send_bits(count - 3, 3);
            } else {
                send_code(REPZ_11_138, bl_tree);
                send_bits(count - 11, 7);
            }
            count = 0;
            prevlen = curlen;
            if (nextlen == 0) {
                max_count = 138;
                min_count = 3;
            } else if (curlen == nextlen) {
                max_count = 6;
                min_count = 3;
            } else {
                max_count = 7;
                min_count = 4;
            }
        }
    }

    final void put_byte(byte[] p, int start, int len) {
        System.arraycopy(p, start, pending_buf, pending, len);
        pending += len;
    }

    final void put_byte(byte c) {
        pending_buf[pending++] = c;
    }

    final void put_short(int w) {
        put_byte((byte) (w));
        put_byte((byte) (w >>> 8));
    }

    final void putShortMSB(int b) {
        put_byte((byte) (b >> 8));
        put_byte((byte) (b));
    }

    final void send_code(int c, short[] tree) {
        int c2 = c * 2;
        send_bits((tree[c2] & 0xffff), (tree[c2 + 1] & 0xffff));
    }

    void send_bits(int value, int length) {
        int len = length;
        if (bi_valid > (int) Buf_size - len) {
            int val = value;
            bi_buf |= ((val << bi_valid) & 0xffff);
            put_short(bi_buf);
            bi_buf = (short) (val >>> (Buf_size - bi_valid));
            bi_valid += len - Buf_size;
        } else {
            bi_buf |= (((value) << bi_valid) & 0xffff);
            bi_valid += len;
        }
    }

    void _tr_align() {
        send_bits(STATIC_TREES << 1, 3);
        send_code(END_BLOCK, StaticTree.static_ltree);
        bi_flush();
        if (1 + last_eob_len + 10 - bi_valid < 9) {
            send_bits(STATIC_TREES << 1, 3);
            send_code(END_BLOCK, StaticTree.static_ltree);
            bi_flush();
        }
        last_eob_len = 7;
    }

    boolean _tr_tally(int dist, int lc) {
        pending_buf[d_buf + last_lit * 2] = (byte) (dist >>> 8);
        pending_buf[d_buf + last_lit * 2 + 1] = (byte) dist;
        pending_buf[l_buf + last_lit] = (byte) lc;
        last_lit++;
        if (dist == 0) {
            dyn_ltree[lc * 2]++;
        } else {
            matches++;
            dist--;
            dyn_ltree[(Tree._length_code[lc] + LITERALS + 1) * 2]++;
            dyn_dtree[Tree.d_code(dist) * 2]++;
        }
        if ((last_lit & 0x1fff) == 0 && level > 2) {
            int out_length = last_lit * 8;
            int in_length = strstart - block_start;
            int dcode;
            for (dcode = 0; dcode < D_CODES; dcode++) {
                out_length += (int) dyn_dtree[dcode * 2] * (5L + Tree.extra_dbits[dcode]);
            }
            out_length >>>= 3;
            if ((matches < (last_lit / 2)) && out_length < in_length / 2) return true;
        }
        return (last_lit == lit_bufsize - 1);
    }

    void compress_block(short[] ltree, short[] dtree) {
        int dist;
        int lc;
        int lx = 0;
        int code;
        int extra;
        if (last_lit != 0) {
            do {
                dist = ((pending_buf[d_buf + lx * 2] << 8) & 0xff00) | (pending_buf[d_buf + lx * 2 + 1] & 0xff);
                lc = (pending_buf[l_buf + lx]) & 0xff;
                lx++;
                if (dist == 0) {
                    send_code(lc, ltree);
                } else {
                    code = Tree._length_code[lc];
                    send_code(code + LITERALS + 1, ltree);
                    extra = Tree.extra_lbits[code];
                    if (extra != 0) {
                        lc -= Tree.base_length[code];
                        send_bits(lc, extra);
                    }
                    dist--;
                    code = Tree.d_code(dist);
                    send_code(code, dtree);
                    extra = Tree.extra_dbits[code];
                    if (extra != 0) {
                        dist -= Tree.base_dist[code];
                        send_bits(dist, extra);
                    }
                }
            } while (lx < last_lit);
        }
        send_code(END_BLOCK, ltree);
        last_eob_len = ltree[END_BLOCK * 2 + 1];
    }

    void set_data_type() {
        int n = 0;
        int ascii_freq = 0;
        int bin_freq = 0;
        while (n < 7) {
            bin_freq += dyn_ltree[n * 2];
            n++;
        }
        while (n < 128) {
            ascii_freq += dyn_ltree[n * 2];
            n++;
        }
        while (n < LITERALS) {
            bin_freq += dyn_ltree[n * 2];
            n++;
        }
        data_type = (byte) (bin_freq > (ascii_freq >>> 2) ? Z_BINARY : Z_ASCII);
    }

    void bi_flush() {
        if (bi_valid == 16) {
            put_short(bi_buf);
            bi_buf = 0;
            bi_valid = 0;
        } else if (bi_valid >= 8) {
            put_byte((byte) bi_buf);
            bi_buf >>>= 8;
            bi_valid -= 8;
        }
    }

    void bi_windup() {
        if (bi_valid > 8) {
            put_short(bi_buf);
        } else if (bi_valid > 0) {
            put_byte((byte) bi_buf);
        }
        bi_buf = 0;
        bi_valid = 0;
    }

    void copy_block(int buf, int len, boolean header) {
        int index = 0;
        bi_windup();
        last_eob_len = 8;
        if (header) {
            put_short((short) len);
            put_short((short) ~len);
        }
        put_byte(window, buf, len);
    }

    void flush_block_only(boolean eof) {
        _tr_flush_block(block_start >= 0 ? block_start : -1, strstart - block_start, eof);
        block_start = strstart;
        strm.flush_pending();
    }

    int deflate_stored(int flush) {
        int max_block_size = 0xffff;
        int max_start;
        if (max_block_size > pending_buf_size - 5) {
            max_block_size = pending_buf_size - 5;
        }
        while (true) {
            if (lookahead <= 1) {
                fill_window();
                if (lookahead == 0 && flush == Z_NO_FLUSH) return NeedMore;
                if (lookahead == 0) break;
            }
            strstart += lookahead;
            lookahead = 0;
            max_start = block_start + max_block_size;
            if (strstart == 0 || strstart >= max_start) {
                lookahead = (int) (strstart - max_start);
                strstart = (int) max_start;
                flush_block_only(false);
                if (strm.avail_out == 0) return NeedMore;
            }
            if (strstart - block_start >= w_size - MIN_LOOKAHEAD) {
                flush_block_only(false);
                if (strm.avail_out == 0) return NeedMore;
            }
        }
        flush_block_only(flush == Z_FINISH);
        if (strm.avail_out == 0) return (flush == Z_FINISH) ? FinishStarted : NeedMore;
        return flush == Z_FINISH ? FinishDone : BlockDone;
    }

    void _tr_stored_block(int buf, int stored_len, boolean eof) {
        send_bits((STORED_BLOCK << 1) + (eof ? 1 : 0), 3);
        copy_block(buf, stored_len, true);
    }

    void _tr_flush_block(int buf, int stored_len, boolean eof) {
        int opt_lenb, static_lenb;
        int max_blindex = 0;
        if (level > 0) {
            if (data_type == Z_UNKNOWN) set_data_type();
            l_desc.build_tree(this);
            d_desc.build_tree(this);
            max_blindex = build_bl_tree();
            opt_lenb = (opt_len + 3 + 7) >>> 3;
            static_lenb = (static_len + 3 + 7) >>> 3;
            if (static_lenb <= opt_lenb) opt_lenb = static_lenb;
        } else {
            opt_lenb = static_lenb = stored_len + 5;
        }
        if (stored_len + 4 <= opt_lenb && buf != -1) {
            _tr_stored_block(buf, stored_len, eof);
        } else if (static_lenb == opt_lenb) {
            send_bits((STATIC_TREES << 1) + (eof ? 1 : 0), 3);
            compress_block(StaticTree.static_ltree, StaticTree.static_dtree);
        } else {
            send_bits((DYN_TREES << 1) + (eof ? 1 : 0), 3);
            send_all_trees(l_desc.max_code + 1, d_desc.max_code + 1, max_blindex + 1);
            compress_block(dyn_ltree, dyn_dtree);
        }
        init_block();
        if (eof) {
            bi_windup();
        }
    }

    void fill_window() {
        int n, m;
        int p;
        int more;
        do {
            more = (window_size - lookahead - strstart);
            if (more == 0 && strstart == 0 && lookahead == 0) {
                more = w_size;
            } else if (more == -1) {
                more--;
            } else if (strstart >= w_size + w_size - MIN_LOOKAHEAD) {
                System.arraycopy(window, w_size, window, 0, w_size);
                match_start -= w_size;
                strstart -= w_size;
                block_start -= w_size;
                n = hash_size;
                p = n;
                do {
                    m = (head[--p] & 0xffff);
                    head[p] = (m >= w_size ? (short) (m - w_size) : 0);
                } while (--n != 0);
                n = w_size;
                p = n;
                do {
                    m = (prev[--p] & 0xffff);
                    prev[p] = (m >= w_size ? (short) (m - w_size) : 0);
                } while (--n != 0);
                more += w_size;
            }
            if (strm.avail_in == 0) return;
            n = strm.read_buf(window, strstart + lookahead, more);
            lookahead += n;
            if (lookahead >= MIN_MATCH) {
                ins_h = window[strstart] & 0xff;
                ins_h = (((ins_h) << hash_shift) ^ (window[strstart + 1] & 0xff)) & hash_mask;
            }
        } while (lookahead < MIN_LOOKAHEAD && strm.avail_in != 0);
    }

    int deflate_fast(int flush) {
        int hash_head = 0;
        boolean bflush;
        while (true) {
            if (lookahead < MIN_LOOKAHEAD) {
                fill_window();
                if (lookahead < MIN_LOOKAHEAD && flush == Z_NO_FLUSH) {
                    return NeedMore;
                }
                if (lookahead == 0) break;
            }
            if (lookahead >= MIN_MATCH) {
                ins_h = (((ins_h) << hash_shift) ^ (window[(strstart) + (MIN_MATCH - 1)] & 0xff)) & hash_mask;
                hash_head = (head[ins_h] & 0xffff);
                prev[strstart & w_mask] = head[ins_h];
                head[ins_h] = (short) strstart;
            }
            if (hash_head != 0L && ((strstart - hash_head) & 0xffff) <= w_size - MIN_LOOKAHEAD) {
                if (strategy != Z_HUFFMAN_ONLY) {
                    match_length = longest_match(hash_head);
                }
            }
            if (match_length >= MIN_MATCH) {
                bflush = _tr_tally(strstart - match_start, match_length - MIN_MATCH);
                lookahead -= match_length;
                if (match_length <= max_lazy_match && lookahead >= MIN_MATCH) {
                    match_length--;
                    do {
                        strstart++;
                        ins_h = ((ins_h << hash_shift) ^ (window[(strstart) + (MIN_MATCH - 1)] & 0xff)) & hash_mask;
                        hash_head = (head[ins_h] & 0xffff);
                        prev[strstart & w_mask] = head[ins_h];
                        head[ins_h] = (short) strstart;
                    } while (--match_length != 0);
                    strstart++;
                } else {
                    strstart += match_length;
                    match_length = 0;
                    ins_h = window[strstart] & 0xff;
                    ins_h = (((ins_h) << hash_shift) ^ (window[strstart + 1] & 0xff)) & hash_mask;
                }
            } else {
                bflush = _tr_tally(0, window[strstart] & 0xff);
                lookahead--;
                strstart++;
            }
            if (bflush) {
                flush_block_only(false);
                if (strm.avail_out == 0) return NeedMore;
            }
        }
        flush_block_only(flush == Z_FINISH);
        if (strm.avail_out == 0) {
            if (flush == Z_FINISH) return FinishStarted; else return NeedMore;
        }
        return flush == Z_FINISH ? FinishDone : BlockDone;
    }

    int deflate_slow(int flush) {
        int hash_head = 0;
        boolean bflush;
        while (true) {
            if (lookahead < MIN_LOOKAHEAD) {
                fill_window();
                if (lookahead < MIN_LOOKAHEAD && flush == Z_NO_FLUSH) {
                    return NeedMore;
                }
                if (lookahead == 0) break;
            }
            if (lookahead >= MIN_MATCH) {
                ins_h = (((ins_h) << hash_shift) ^ (window[(strstart) + (MIN_MATCH - 1)] & 0xff)) & hash_mask;
                hash_head = (head[ins_h] & 0xffff);
                prev[strstart & w_mask] = head[ins_h];
                head[ins_h] = (short) strstart;
            }
            prev_length = match_length;
            prev_match = match_start;
            match_length = MIN_MATCH - 1;
            if (hash_head != 0 && prev_length < max_lazy_match && ((strstart - hash_head) & 0xffff) <= w_size - MIN_LOOKAHEAD) {
                if (strategy != Z_HUFFMAN_ONLY) {
                    match_length = longest_match(hash_head);
                }
                if (match_length <= 5 && (strategy == Z_FILTERED || (match_length == MIN_MATCH && strstart - match_start > 4096))) {
                    match_length = MIN_MATCH - 1;
                }
            }
            if (prev_length >= MIN_MATCH && match_length <= prev_length) {
                int max_insert = strstart + lookahead - MIN_MATCH;
                bflush = _tr_tally(strstart - 1 - prev_match, prev_length - MIN_MATCH);
                lookahead -= prev_length - 1;
                prev_length -= 2;
                do {
                    if (++strstart <= max_insert) {
                        ins_h = (((ins_h) << hash_shift) ^ (window[(strstart) + (MIN_MATCH - 1)] & 0xff)) & hash_mask;
                        hash_head = (head[ins_h] & 0xffff);
                        prev[strstart & w_mask] = head[ins_h];
                        head[ins_h] = (short) strstart;
                    }
                } while (--prev_length != 0);
                match_available = 0;
                match_length = MIN_MATCH - 1;
                strstart++;
                if (bflush) {
                    flush_block_only(false);
                    if (strm.avail_out == 0) return NeedMore;
                }
            } else if (match_available != 0) {
                bflush = _tr_tally(0, window[strstart - 1] & 0xff);
                if (bflush) {
                    flush_block_only(false);
                }
                strstart++;
                lookahead--;
                if (strm.avail_out == 0) return NeedMore;
            } else {
                match_available = 1;
                strstart++;
                lookahead--;
            }
        }
        if (match_available != 0) {
            bflush = _tr_tally(0, window[strstart - 1] & 0xff);
            match_available = 0;
        }
        flush_block_only(flush == Z_FINISH);
        if (strm.avail_out == 0) {
            if (flush == Z_FINISH) return FinishStarted; else return NeedMore;
        }
        return flush == Z_FINISH ? FinishDone : BlockDone;
    }

    int longest_match(int cur_match) {
        int chain_length = max_chain_length;
        int scan = strstart;
        int match;
        int len;
        int best_len = prev_length;
        int limit = strstart > (w_size - MIN_LOOKAHEAD) ? strstart - (w_size - MIN_LOOKAHEAD) : 0;
        int nice_match = this.nice_match;
        int wmask = w_mask;
        int strend = strstart + MAX_MATCH;
        byte scan_end1 = window[scan + best_len - 1];
        byte scan_end = window[scan + best_len];
        if (prev_length >= good_match) {
            chain_length >>= 2;
        }
        if (nice_match > lookahead) nice_match = lookahead;
        do {
            match = cur_match;
            if (window[match + best_len] != scan_end || window[match + best_len - 1] != scan_end1 || window[match] != window[scan] || window[++match] != window[scan + 1]) continue;
            scan += 2;
            match++;
            do {
            } while (window[++scan] == window[++match] && window[++scan] == window[++match] && window[++scan] == window[++match] && window[++scan] == window[++match] && window[++scan] == window[++match] && window[++scan] == window[++match] && window[++scan] == window[++match] && window[++scan] == window[++match] && scan < strend);
            len = MAX_MATCH - (int) (strend - scan);
            scan = strend - MAX_MATCH;
            if (len > best_len) {
                match_start = cur_match;
                best_len = len;
                if (len >= nice_match) break;
                scan_end1 = window[scan + best_len - 1];
                scan_end = window[scan + best_len];
            }
        } while ((cur_match = (prev[cur_match & wmask] & 0xffff)) > limit && --chain_length != 0);
        if (best_len <= lookahead) return best_len;
        return lookahead;
    }

    int deflateInit(ZStream strm, int level, int bits) {
        return deflateInit2(strm, level, Z_DEFLATED, bits, DEF_MEM_LEVEL, Z_DEFAULT_STRATEGY);
    }

    int deflateInit(ZStream strm, int level) {
        return deflateInit(strm, level, MAX_WBITS);
    }

    int deflateInit2(ZStream strm, int level, int method, int windowBits, int memLevel, int strategy) {
        int noheader = 0;
        strm.msg = null;
        if (level == Z_DEFAULT_COMPRESSION) level = 6;
        if (windowBits < 0) {
            noheader = 1;
            windowBits = -windowBits;
        }
        if (memLevel < 1 || memLevel > MAX_MEM_LEVEL || method != Z_DEFLATED || windowBits < 9 || windowBits > 15 || level < 0 || level > 9 || strategy < 0 || strategy > Z_HUFFMAN_ONLY) {
            return Z_STREAM_ERROR;
        }
        strm.dstate = (Deflate) this;
        this.noheader = noheader;
        w_bits = windowBits;
        w_size = 1 << w_bits;
        w_mask = w_size - 1;
        hash_bits = memLevel + 7;
        hash_size = 1 << hash_bits;
        hash_mask = hash_size - 1;
        hash_shift = ((hash_bits + MIN_MATCH - 1) / MIN_MATCH);
        window = new byte[w_size * 2];
        prev = new short[w_size];
        head = new short[hash_size];
        lit_bufsize = 1 << (memLevel + 6);
        pending_buf = new byte[lit_bufsize * 4];
        pending_buf_size = lit_bufsize * 4;
        d_buf = lit_bufsize / 2;
        l_buf = (1 + 2) * lit_bufsize;
        this.level = level;
        this.strategy = strategy;
        this.method = (byte) method;
        return deflateReset(strm);
    }

    int deflateReset(ZStream strm) {
        strm.total_in = strm.total_out = 0;
        strm.msg = null;
        strm.data_type = Z_UNKNOWN;
        pending = 0;
        pending_out = 0;
        if (noheader < 0) {
            noheader = 0;
        }
        status = (noheader != 0) ? BUSY_STATE : INIT_STATE;
        strm.adler = strm._adler.adler32(0, null, 0, 0);
        last_flush = Z_NO_FLUSH;
        tr_init();
        lm_init();
        return Z_OK;
    }

    int deflateEnd() {
        if (status != INIT_STATE && status != BUSY_STATE && status != FINISH_STATE) {
            return Z_STREAM_ERROR;
        }
        pending_buf = null;
        head = null;
        prev = null;
        window = null;
        return status == BUSY_STATE ? Z_DATA_ERROR : Z_OK;
    }

    int deflateParams(ZStream strm, int _level, int _strategy) {
        int err = Z_OK;
        if (_level == Z_DEFAULT_COMPRESSION) {
            _level = 6;
        }
        if (_level < 0 || _level > 9 || _strategy < 0 || _strategy > Z_HUFFMAN_ONLY) {
            return Z_STREAM_ERROR;
        }
        if (config_table[level].func != config_table[_level].func && strm.total_in != 0) {
            err = strm.deflate(Z_PARTIAL_FLUSH);
        }
        if (level != _level) {
            level = _level;
            max_lazy_match = config_table[level].max_lazy;
            good_match = config_table[level].good_length;
            nice_match = config_table[level].nice_length;
            max_chain_length = config_table[level].max_chain;
        }
        strategy = _strategy;
        return err;
    }

    int deflateSetDictionary(ZStream strm, byte[] dictionary, int dictLength) {
        int length = dictLength;
        int index = 0;
        if (dictionary == null || status != INIT_STATE) return Z_STREAM_ERROR;
        strm.adler = strm._adler.adler32(strm.adler, dictionary, 0, dictLength);
        if (length < MIN_MATCH) return Z_OK;
        if (length > w_size - MIN_LOOKAHEAD) {
            length = w_size - MIN_LOOKAHEAD;
            index = dictLength - length;
        }
        System.arraycopy(dictionary, index, window, 0, length);
        strstart = length;
        block_start = length;
        ins_h = window[0] & 0xff;
        ins_h = (((ins_h) << hash_shift) ^ (window[1] & 0xff)) & hash_mask;
        for (int n = 0; n <= length - MIN_MATCH; n++) {
            ins_h = (((ins_h) << hash_shift) ^ (window[(n) + (MIN_MATCH - 1)] & 0xff)) & hash_mask;
            prev[n & w_mask] = head[ins_h];
            head[ins_h] = (short) n;
        }
        return Z_OK;
    }

    int deflate(ZStream strm, int flush) {
        int old_flush;
        if (flush > Z_FINISH || flush < 0) {
            return Z_STREAM_ERROR;
        }
        if (strm.next_out == null || (strm.next_in == null && strm.avail_in != 0) || (status == FINISH_STATE && flush != Z_FINISH)) {
            strm.msg = z_errmsg[Z_NEED_DICT - (Z_STREAM_ERROR)];
            return Z_STREAM_ERROR;
        }
        if (strm.avail_out == 0) {
            strm.msg = z_errmsg[Z_NEED_DICT - (Z_BUF_ERROR)];
            return Z_BUF_ERROR;
        }
        this.strm = strm;
        old_flush = last_flush;
        last_flush = flush;
        if (status == INIT_STATE) {
            int header = (Z_DEFLATED + ((w_bits - 8) << 4)) << 8;
            int level_flags = ((level - 1) & 0xff) >> 1;
            if (level_flags > 3) level_flags = 3;
            header |= (level_flags << 6);
            if (strstart != 0) header |= PRESET_DICT;
            header += 31 - (header % 31);
            status = BUSY_STATE;
            putShortMSB(header);
            if (strstart != 0) {
                putShortMSB((int) (strm.adler >>> 16));
                putShortMSB((int) (strm.adler & 0xffff));
            }
            strm.adler = strm._adler.adler32(0, null, 0, 0);
        }
        if (pending != 0) {
            strm.flush_pending();
            if (strm.avail_out == 0) {
                last_flush = -1;
                return Z_OK;
            }
        } else if (strm.avail_in == 0 && flush <= old_flush && flush != Z_FINISH) {
            strm.msg = z_errmsg[Z_NEED_DICT - (Z_BUF_ERROR)];
            return Z_BUF_ERROR;
        }
        if (status == FINISH_STATE && strm.avail_in != 0) {
            strm.msg = z_errmsg[Z_NEED_DICT - (Z_BUF_ERROR)];
            return Z_BUF_ERROR;
        }
        if (strm.avail_in != 0 || lookahead != 0 || (flush != Z_NO_FLUSH && status != FINISH_STATE)) {
            int bstate = -1;
            switch(config_table[level].func) {
                case STORED:
                    bstate = deflate_stored(flush);
                    break;
                case FAST:
                    bstate = deflate_fast(flush);
                    break;
                case SLOW:
                    bstate = deflate_slow(flush);
                    break;
                default:
            }
            if (bstate == FinishStarted || bstate == FinishDone) {
                status = FINISH_STATE;
            }
            if (bstate == NeedMore || bstate == FinishStarted) {
                if (strm.avail_out == 0) {
                    last_flush = -1;
                }
                return Z_OK;
            }
            if (bstate == BlockDone) {
                if (flush == Z_PARTIAL_FLUSH) {
                    _tr_align();
                } else {
                    _tr_stored_block(0, 0, false);
                    if (flush == Z_FULL_FLUSH) {
                        for (int i = 0; i < hash_size; i++) head[i] = 0;
                    }
                }
                strm.flush_pending();
                if (strm.avail_out == 0) {
                    last_flush = -1;
                    return Z_OK;
                }
            }
        }
        if (flush != Z_FINISH) return Z_OK;
        if (noheader != 0) return Z_STREAM_END;
        putShortMSB((int) (strm.adler >>> 16));
        putShortMSB((int) (strm.adler & 0xffff));
        strm.flush_pending();
        noheader = -1;
        return pending != 0 ? Z_OK : Z_STREAM_END;
    }
}
