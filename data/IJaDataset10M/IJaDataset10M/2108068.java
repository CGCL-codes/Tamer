package net.sourceforge.theba.core;

import net.sourceforge.theba.core.gui.ThebaPrefs;
import net.sourceforge.theba.core.io.SliceWriter;
import net.sourceforge.theba.core.io.VolumeReader;
import net.sourceforge.theba.core.math.Point3D;

/**
 * This class represents a three-dimensional volume, with short as its base
 * datatype.
 * <p/>
 * The volume is stored slice, by sice, line by line linearly in memory. Hence
 * the orientation of the volume has a lot to say when it comes to cache
 * efficiency. The current stack is optimized for viewing along the width/height
 * axis.
 * <p/>
 * For best efficiency the entire depth-range should be in cache at once.
 *
 * @author jensbw
 */
public class Stack {

    private int width;

    private int height;

    private int depth;

    private boolean writable = false;

    private VolumeReader writer;

    public VolumeReader getWriter() {
        return writer;
    }

    public void flush() {
        writer.flush();
    }

    public boolean isWritable() {
        return writable;
    }

    public Stack(int w, int h, int d, String fileName) {
        width = w;
        height = h;
        depth = d;
        writer = new SliceWriter(fileName, w, h, d, ThebaPrefs.getInstance().getInt("cacheSize", 500));
        writable = true;
    }

    public Stack(VolumeReader reader) {
        width = reader.getWidth();
        height = reader.getHeight();
        depth = reader.getDepth();
        writer = reader;
    }

    /**
     * Sets a voxel to the given value. Values outside of bounds may result in
     * an Exception, or erroneous values (use this function with caution)
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public void setVoxelUnchecked(int x, int y, int z, int val) {
        short[] slice = writer.getSlice(z);
        slice[x + y * width] = (short) val;
    }

    /**
     * Set a voxel to the given value. Values outside of bounds are ignored
     *
     * @param x
     * @param y
     * @param z
     * @param val
     */
    public void setVoxel(int x, int y, int z, int val) {
        if (x < 0 || y < 0 || z < 0 || x >= width || y >= height || z >= depth) {
            return;
        }
        short[] slice = writer.getSlice(z);
        slice[x + y * width] = (short) val;
    }

    /**
     * Gets a voxel from the volume. Values outside of bounds may result in an
     * Exception, or erroneous values (use this function with caution)
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public short getVoxelUnchecked(int x, int y, int z) {
        return writer.getSlice(z)[x + y * width];
    }

    /**
     * Gets a voxel. Values outside of bounds are returned as 0
     *
     * @param x
     * @param y
     * @param z
     */
    public short getVoxel(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= width || y >= height || z >= depth) return 0;
        short[] slice = writer.getSlice(z);
        return slice[x + y * width];
    }

    /**
     * Gets a voxel. Values outside of bounds are returned as 0
     */
    public short getVoxel(Point3D p) {
        if (p.x < 0 || p.y < 0 || p.z < 0 || p.x >= width || p.y >= height || p.z >= depth) return 0;
        short[] slice = writer.getSlice(p.z);
        return slice[p.x + p.y * width];
    }

    public short[] getSlice(int z) {
        return writer.getSlice(z);
    }

    public void putSlice(short[] data, int z) {
        writer.putSlice(data, z);
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
