package net.sourceforge.magex.mobile.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import net.sourceforge.magex.mobile.Magex;

/**
 * The Normalizer class contains the method for normalizing strings given by the user, i.e\. converts them to a format
 * suitable for performing map searches.
 */
public abstract class Normalizer {

    /** The lookup index resource name in JAR */
    private static final String LOOKUP_INDEX_FILE = "/unicode/specchar.idx";

    /** The replacement table resource name in JAR */
    private static final String REPL_TABLE_FILE = "/unicode/specchar.dat";

    /** Lookup index entry length in bytes */
    private static final int LOOKUP_ENTRY_SIZE = 9;

    /** Lookup index read buffer size */
    private static final int BUF_SIZE = 1000;

    /** 
     * List of characters, that have a replacement in the normalization replacement table.
     * Consists of a 9-byte records: start character, end character, replacement length in bytes and offset
     * in the replacement table. Loaded upon entry to normalize function and unloaded at the end.
     */
    private static byte[] lookupIndex;

    /** Number of entries in the lookup index */
    private static int lookupIndexLen;

    /**
     * Normalize the given string, i.e\. remove all diacritics, uppercase etc.
     *
     * @param str the input string
     * @return the normalized string, suitable for searching within the map
     */
    public static String normalize(String str) {
        StringBuffer sb;
        String replacement;
        try {
            str = str.toUpperCase();
            loadLookupIndex();
            sb = new StringBuffer();
            for (int i = 0; i < str.length(); ++i) {
                if ((replacement = getReplacement(str.charAt(i))) != null) {
                    sb.append(replacement);
                } else {
                    sb.append(str.charAt(i));
                }
            }
            return sb.toString();
        } catch (Exception e) {
            if (Magex.EMU_DEBUG) {
                e.printStackTrace();
            }
            return str;
        } finally {
            lookupIndex = null;
        }
    }

    /**
     * Find a replacement for the given character in the lookup index and replacement table.
     * Binary search in the lookup index, followed by a search in the replacement table, if 
     * anything has been found in the index.
     */
    private static String getReplacement(char c) throws IOException {
        int hi = lookupIndexLen - 1, lo = 0;
        int mid;
        char curBeg, curEnd;
        int offset = -1;
        byte len = 0;
        InputStream data;
        byte[] replBytes;
        while (lo <= hi) {
            mid = (hi + lo) / 2;
            curBeg = (char) ((lookupIndex[mid * LOOKUP_ENTRY_SIZE] << 8) | (0xFF & lookupIndex[mid * LOOKUP_ENTRY_SIZE + 1]));
            curEnd = (char) ((lookupIndex[mid * LOOKUP_ENTRY_SIZE + 2] << 8) | (0xFF & lookupIndex[mid * LOOKUP_ENTRY_SIZE + 3]));
            if (c < curBeg) {
                hi = mid - 1;
            } else if (c > curEnd) {
                lo = mid + 1;
            } else {
                len = lookupIndex[mid * LOOKUP_ENTRY_SIZE + 4];
                offset = (lookupIndex[mid * LOOKUP_ENTRY_SIZE + 5] << 24) | (0xFF0000 & (lookupIndex[mid * LOOKUP_ENTRY_SIZE + 6] << 16)) | (0xFF00 & (lookupIndex[mid * LOOKUP_ENTRY_SIZE + 7] << 8)) | (0xFF & lookupIndex[mid * LOOKUP_ENTRY_SIZE + 8]);
                offset += len * (c - curBeg);
                break;
            }
        }
        if (offset == -1) {
            return null;
        }
        data = Normalizer.class.getResourceAsStream(REPL_TABLE_FILE);
        data.skip(offset);
        data.read(replBytes = new byte[len]);
        return new String(replBytes, "UTF8");
    }

    /**
     * Loads the lookup index into an array in memory.
     */
    private static void loadLookupIndex() throws IOException {
        InputStream data = Normalizer.class.getResourceAsStream(LOOKUP_INDEX_FILE);
        if (data.available() != -1) {
            lookupIndex = new byte[data.available()];
            data.read(lookupIndex);
        } else {
            Vector loaded = new Vector();
            int curLoaded = 0;
            int totalLoaded = 0;
            int read;
            byte[] buf = new byte[BUF_SIZE];
            while ((read = data.read(buf, curLoaded, BUF_SIZE - curLoaded)) != -1) {
                curLoaded += read;
                totalLoaded += read;
                if (curLoaded == BUF_SIZE) {
                    loaded.addElement(buf);
                    buf = new byte[BUF_SIZE];
                    curLoaded = 0;
                }
            }
            lookupIndex = new byte[totalLoaded];
            curLoaded = 0;
            for (int i = 0; i < loaded.size(); ++i) {
                System.arraycopy((byte[]) loaded.elementAt(i), 0, lookupIndex, curLoaded, read = ((byte[]) loaded.elementAt(i)).length);
                curLoaded += read;
            }
        }
        lookupIndexLen = lookupIndex.length / LOOKUP_ENTRY_SIZE;
    }
}
