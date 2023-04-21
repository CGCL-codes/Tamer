package ao.util.persist;

import ao.util.io.Dirs;
import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Date: Sep 1, 2008
 * Time: 4:31:04 AM
 */
public class PersistentBytes {

    private PersistentBytes() {
    }

    public static byte[] retrieve(String fromFile) {
        return retrieve(new File(fromFile));
    }

    public static byte[] retrieve(File fromFile) {
        try {
            if (fromFile == null || !fromFile.canRead()) return null;
            return (fromFile.length() < 1024 * 1024) ? doRetrieve(fromFile) : doRetrieveNew(fromFile);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static byte[] doRetrieveNew(File cacheFile) throws Exception {
        byte[] cached = new byte[(int) cacheFile.length()];
        FileInputStream f = new FileInputStream(cacheFile);
        try {
            FileChannel ch = f.getChannel();
            Mmap.bytes(cached, 0, ch);
            ch.close();
        } finally {
            f.close();
        }
        return cached;
    }

    private static byte[] doRetrieve(File cacheFile) throws Exception {
        byte[] cached = new byte[(int) cacheFile.length()];
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(cacheFile));
        try {
            if (in.read(cached) != cached.length) {
                throw new Error("could not read entire file");
            }
        } finally {
            in.close();
        }
        return cached;
    }

    public static void persist(byte vals[], String filename) {
        persist(vals, new File(filename));
    }

    public static void persist(byte vals[], File toFile) {
        try {
            doPersist(vals, toFile);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static void doPersist(byte vals[], File cacheFile) throws Exception {
        Dirs.pathTo(cacheFile);
        cacheFile.createNewFile();
        DataOutputStream cache = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(cacheFile)));
        for (byte val : vals) {
            cache.writeByte(val);
        }
        cache.close();
    }
}
