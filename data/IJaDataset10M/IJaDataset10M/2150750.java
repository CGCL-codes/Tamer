package org.jopenray.rdp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.log4j.Logger;

public class PstCache {

    protected static Logger logger = Logger.getLogger(Rdp.class);

    public static final int MAX_CELL_SIZE = 0x1000;

    protected static boolean IS_PERSISTENT(int id) {
        return (id < 8 && g_pstcache_fd[id] != null);
    }

    static int g_stamp;

    static File[] g_pstcache_fd = new File[8];

    static int g_pstcache_Bpp;

    static boolean g_pstcache_enumerated = false;

    protected static void touchBitmap(int cache_id, int cache_idx, int stamp) {
        logger.info("PstCache.touchBitmap");
        FileOutputStream fd;
        if (!IS_PERSISTENT(cache_id) || cache_idx >= Rdp.BMPCACHE2_NUM_PSTCELLS) return;
        try {
            fd = new FileOutputStream(g_pstcache_fd[cache_id]);
            fd.write(toBigEndian32(stamp), 12 + cache_idx * (g_pstcache_Bpp * MAX_CELL_SIZE + CELLHEADER.size()), 4);
        } catch (IOException e) {
            return;
        }
    }

    private static byte[] toBigEndian32(int value) {
        byte[] out = new byte[4];
        out[0] = (byte) (value & 0xFF);
        out[1] = (byte) (value & 0xFF00);
        out[2] = (byte) (value & 0xFF0000);
        out[3] = (byte) (value & 0xFF000000);
        return out;
    }

    static boolean pstcache_load_bitmap(int cache_id, int cache_idx) throws IOException, RdesktopException {
        logger.info("PstCache.pstcache_load_bitmap");
        byte[] celldata = null;
        FileInputStream fd;
        Bitmap bitmap;
        byte[] cellHead = null;
        if (!Options.persistent_bitmap_caching) return false;
        if (!IS_PERSISTENT(cache_id) || cache_idx >= Rdp.BMPCACHE2_NUM_PSTCELLS) return false;
        fd = new FileInputStream(g_pstcache_fd[cache_id]);
        int offset = cache_idx * (g_pstcache_Bpp * MAX_CELL_SIZE + CELLHEADER.size());
        fd.read(cellHead, offset, CELLHEADER.size());
        CELLHEADER c = new CELLHEADER(cellHead);
        celldata = new byte[c.length];
        fd.read(celldata);
        logger.debug("Loading bitmap from disk (" + cache_id + ":" + cache_idx + ")\n");
        bitmap = new Bitmap(celldata, c.width, c.height, 0, 0, Options.Bpp);
        Orders.cache.putBitmap(cache_id, cache_idx, bitmap, c.stamp);
        return true;
    }

    static boolean pstcache_put_bitmap(int cache_id, int cache_idx, byte[] bitmap_id, int width, int height, int length, byte[] data) throws IOException {
        logger.info("PstCache.pstcache_put_bitmap");
        FileOutputStream fd;
        CELLHEADER cellhdr = new CELLHEADER();
        if (!IS_PERSISTENT(cache_id) || cache_idx >= Rdp.BMPCACHE2_NUM_PSTCELLS) return false;
        cellhdr.bitmap_id = bitmap_id;
        cellhdr.width = width;
        cellhdr.height = height;
        cellhdr.length = length;
        cellhdr.stamp = 0;
        fd = new FileOutputStream(g_pstcache_fd[cache_id]);
        int offset = cache_idx * (Options.Bpp * MAX_CELL_SIZE + CELLHEADER.size());
        fd.write(cellhdr.toBytes(), offset, CELLHEADER.size());
        fd.write(data);
        return true;
    }

    static int pstcache_enumerate(int cache_id, int[] idlist) throws IOException, RdesktopException {
        logger.info("PstCache.pstcache_enumerate");
        FileInputStream fd;
        int n, c = 0;
        CELLHEADER cellhdr = null;
        if (!(Options.bitmap_caching && Options.persistent_bitmap_caching && IS_PERSISTENT(cache_id))) return 0;
        if (g_pstcache_enumerated) return 0;
        logger.debug("pstcache enumeration... ");
        for (n = 0; n < Rdp.BMPCACHE2_NUM_PSTCELLS; n++) {
            fd = new FileInputStream(g_pstcache_fd[cache_id]);
            byte[] cellhead_data = new byte[CELLHEADER.size()];
            if (fd.read(cellhead_data, n * (g_pstcache_Bpp * MAX_CELL_SIZE + CELLHEADER.size()), CELLHEADER.size()) <= 0) break;
            cellhdr = new CELLHEADER(cellhead_data);
            int result = 0;
            for (int i = 0; i < cellhdr.bitmap_id.length; i++) {
                result += cellhdr.bitmap_id[i];
            }
            if (result != 0) {
                for (int i = 0; i < 8; i++) {
                    idlist[(n * 8) + i] = cellhdr.bitmap_id[i];
                }
                if (cellhdr.stamp != 0) {
                    if (Options.precache_bitmaps && (Options.server_bpp > 8)) {
                        if (pstcache_load_bitmap(cache_id, n)) c++;
                    }
                    g_stamp = Math.max(g_stamp, cellhdr.stamp);
                }
            } else {
                break;
            }
        }
        logger.info(n + " bitmaps in persistent cache, " + c + " bitmaps loaded in memory\n");
        g_pstcache_enumerated = true;
        return n;
    }

    static boolean pstcache_init(int cache_id) {
        String filename;
        if (g_pstcache_enumerated) return true;
        g_pstcache_fd[cache_id] = null;
        if (!(Options.bitmap_caching && Options.persistent_bitmap_caching)) return false;
        g_pstcache_Bpp = Options.Bpp;
        filename = "./cache/pstcache_" + cache_id + "_" + g_pstcache_Bpp;
        logger.debug("persistent bitmap cache file: " + filename);
        File cacheDir = new File("./cache/");
        if (!cacheDir.exists() && !cacheDir.mkdir()) {
            logger.warn("failed to get/make cache directory");
            return false;
        }
        File f = new File(filename);
        try {
            if (!f.exists() && !f.createNewFile()) {
                logger.warn("Could not create cache file");
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        g_pstcache_fd[cache_id] = f;
        return true;
    }
}

class CELLHEADER {

    byte[] bitmap_id = new byte[8];

    int width, height;

    int length;

    int stamp;

    static int size() {
        return 8 * 8 + 8 * 2 + 16 + 32;
    }

    public CELLHEADER() {
    }

    public CELLHEADER(byte[] data) {
        for (int i = 0; i < bitmap_id.length; i++) bitmap_id[i] = data[i];
        width = data[bitmap_id.length];
        height = data[bitmap_id.length + 1];
        length = (data[bitmap_id.length + 2] >> 8) + data[bitmap_id.length + 3];
        stamp = (data[bitmap_id.length + 6] >> 24) + (data[bitmap_id.length + 6] >> 16) + (data[bitmap_id.length + 6] >> 8) + data[bitmap_id.length + 7];
    }

    public byte[] toBytes() {
        return null;
    }
}
