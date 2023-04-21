package ao.util.persist;

import ao.util.io.Dirs;
import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Date: Nov 16, 2008
 * Time: 5:06:59 PM
 */
public class PersistentChars {

    private static final String testOut = Dirs.get("test/out") + "/test.char";

    public static void main(String[] args) {
        char confirm[] = PersistentChars.retrieve(testOut);
        for (int i = 0; i < confirm.length; i++) {
            if (confirm[i] != ((char) i)) {
                System.out.println(i);
            }
        }
    }

    private PersistentChars() {
    }

    private static final int N_BYTE = Character.SIZE / 8;

    public static char[] retrieve(String fromFile) {
        return retrieve(new File(fromFile));
    }

    public static char[] retrieve(File fromFile) {
        try {
            if (fromFile == null || !fromFile.canRead()) return null;
            return doRetrieve(fromFile);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static char[] doRetrieve(File cacheFile) throws Exception {
        char[] cached = new char[(int) (cacheFile.length() / N_BYTE)];
        FileInputStream f = new FileInputStream(cacheFile);
        try {
            FileChannel ch = f.getChannel();
            int offset = Mmap.chars(cached, 0, ch);
            if (offset < cached.length) {
                Mmap.chars(cached, offset, ch);
            }
        } finally {
            f.close();
        }
        return cached;
    }

    public static void persist(char vals[], String fileName) {
        persist(vals, new File(fileName));
    }

    public static void persist(char vals[], File toFile) {
        try {
            doPersist(vals, toFile);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static void doPersist(char vals[], File cacheFile) throws Exception {
        cacheFile.createNewFile();
        DataOutputStream cache = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(cacheFile)));
        for (char val : vals) {
            cache.writeChar(val);
        }
        cache.close();
    }
}
