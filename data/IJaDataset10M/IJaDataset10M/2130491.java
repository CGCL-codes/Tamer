package net.sourceforge.theba.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * A sub-class of VolumeReader that access data in a slice-based fashion from
 * the hard-drive
 *
 * @author jens
 */
public class SliceReader implements VolumeReader {

    public int width, height, depth;

    protected HashMap<Integer, short[]> cache = new HashMap<Integer, short[]>();

    protected int cacheSize = 1;

    protected RandomAccessFile file;

    protected String filename;

    protected ArrayList<short[]> preallocatedDataArrays = new ArrayList<short[]>();

    protected LinkedList<Integer> queue = new LinkedList<Integer>();

    private byte[] readAhead;

    /**
     * Retrieve an image-slice from this file
     *
     * @param sliceNo
     * @param output
     */
    private byte[] tmp = null;

    public SliceReader(int width, int height, int depth, int cacheSIze) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        readAhead = new byte[width * height * 2 * 5];
        this.cacheSize = cacheSIze;
    }

    public SliceReader(final String filename, int width, int height, int depth, int cacheSize) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.filename = filename;
        readAhead = new byte[width * height * 2 * 5];
        try {
            file = new RandomAccessFile(filename, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.cacheSize = cacheSize;
    }

    public synchronized void close() {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() {
    }

    public RandomAccessFile getFile() {
        return file;
    }

    public synchronized short[] getSlice(int sliceNo) {
        short[] sliceCache = cache.get(sliceNo);
        if (sliceCache != null) {
            return sliceCache;
        }
        final short[] output;
        if (preallocatedDataArrays.size() > 0) {
            output = preallocatedDataArrays.remove(0);
        } else {
            output = new short[width * height];
        }
        getSlice(sliceNo, output);
        queue.add(sliceNo);
        cache.put(sliceNo, output);
        updateCache();
        return output;
    }

    public synchronized void putSlice(short[] data, int pos) {
        System.err.print("Warning, attempting to write to a sliceReader");
    }

    public synchronized void updateCache() {
        while (queue.size() > cacheSize) {
            int deletedSlice = queue.removeFirst();
            short[] sliceCache = cache.remove(deletedSlice);
            preallocatedDataArrays.add(sliceCache);
        }
    }

    public synchronized void getSlice(int sliceNo, short[] output) {
        if (tmp == null) tmp = new byte[output.length * 2];
        try {
            file.seek(output.length * 2 * sliceNo);
            file.readFully(tmp);
            for (int i = 0; i < width * height; i++) {
                output[i] = (short) ((tmp[i * 2] & 0xff) | ((tmp[i * 2 + 1] & 0xff) << 8));
            }
        } catch (Exception e) {
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public void destroy() {
        flush();
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
