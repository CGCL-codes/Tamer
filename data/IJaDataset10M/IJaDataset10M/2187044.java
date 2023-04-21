package org.osmdroid.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * GEMF File handler class.
 * 
 * Reference: https://sites.google.com/site/abudden/android-map-store
 * 
 * @author A. S. Budden
 * @author Erik Burrows
 * 
 */
public class GEMFFile {

    private static final long FILE_SIZE_LIMIT = 1 * 1024 * 1024 * 1024;

    private static final int FILE_COPY_BUFFER_SIZE = 1024;

    private static final int VERSION = 4;

    private static final int TILE_SIZE = 256;

    private static final int U32_SIZE = 4;

    private static final int U64_SIZE = 8;

    private String mLocation;

    private List<RandomAccessFile> mFiles = new ArrayList<RandomAccessFile>();

    private List<GEMFRange> mRangeData = new ArrayList<GEMFRange>();

    private List<Long> mFileSizes = new ArrayList<Long>();

    private HashMap<Integer, String> mSources = new HashMap<Integer, String>();

    private boolean mSourceLimited = false;

    private int mCurrentSource = 0;

    public GEMFFile(File pLocation) throws FileNotFoundException, IOException {
        this(pLocation.getAbsolutePath());
    }

    public GEMFFile(String pLocation) throws FileNotFoundException, IOException {
        mLocation = pLocation;
        openFiles();
        readHeader();
    }

    public GEMFFile(final String pLocation, final List<File> pSourceFolders) throws FileNotFoundException, IOException {
        this.mLocation = pLocation;
        HashMap<String, HashMap<Integer, HashMap<Integer, HashMap<Integer, File>>>> dirIndex = new HashMap<String, HashMap<Integer, HashMap<Integer, HashMap<Integer, File>>>>();
        for (File sourceDir : pSourceFolders) {
            HashMap<Integer, HashMap<Integer, HashMap<Integer, File>>> zList = new HashMap<Integer, HashMap<Integer, HashMap<Integer, File>>>();
            for (File zDir : sourceDir.listFiles()) {
                try {
                    Integer.parseInt(zDir.getName());
                } catch (NumberFormatException e) {
                    continue;
                }
                HashMap<Integer, HashMap<Integer, File>> xList = new HashMap<Integer, HashMap<Integer, File>>();
                for (File xDir : zDir.listFiles()) {
                    try {
                        Integer.parseInt(xDir.getName());
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    HashMap<Integer, File> yList = new HashMap<Integer, File>();
                    for (File yFile : xDir.listFiles()) {
                        try {
                            Integer.parseInt(yFile.getName().substring(0, yFile.getName().indexOf('.')));
                        } catch (NumberFormatException e) {
                            continue;
                        }
                        yList.put(Integer.parseInt(yFile.getName().substring(0, yFile.getName().indexOf('.'))), yFile);
                    }
                    xList.put(new Integer(xDir.getName()), yList);
                }
                zList.put(Integer.parseInt(zDir.getName()), xList);
            }
            dirIndex.put(sourceDir.getName(), zList);
        }
        HashMap<String, Integer> sourceIndex = new HashMap<String, Integer>();
        HashMap<Integer, String> indexSource = new HashMap<Integer, String>();
        int si = 0;
        for (String source : dirIndex.keySet()) {
            sourceIndex.put(source, new Integer(si));
            indexSource.put(new Integer(si), source);
            ++si;
        }
        List<GEMFRange> ranges = new ArrayList<GEMFRange>();
        for (String source : dirIndex.keySet()) {
            for (Integer zoom : dirIndex.get(source).keySet()) {
                HashMap<List<Integer>, List<Integer>> ySets = new HashMap<List<Integer>, List<Integer>>();
                for (Integer x : dirIndex.get(source).get(zoom).keySet()) {
                    List<Integer> ySet = new ArrayList<Integer>();
                    for (Integer y : dirIndex.get(source).get(zoom).get(x).keySet()) {
                        ySet.add(y);
                    }
                    if (!ySets.containsKey(ySet)) {
                        ySets.put(ySet, new ArrayList<Integer>());
                    }
                    ySets.get(ySet).add(x);
                }
                HashMap<List<Integer>, List<Integer>> xSets = new HashMap<List<Integer>, List<Integer>>();
                for (List<Integer> ySet : ySets.keySet()) {
                    TreeSet<Integer> xList = new TreeSet<Integer>(ySets.get(ySet));
                    List<Integer> xSet = new ArrayList<Integer>();
                    for (int i = xList.first(); i < xList.last() + 1; ++i) {
                        if (xList.contains(new Integer(i))) {
                            xSet.add(new Integer(i));
                        } else {
                            if (xSet.size() > 0) {
                                xSets.put(ySet, xSet);
                                xSet = new ArrayList<Integer>();
                            }
                        }
                    }
                    xSets.put(ySet, xSet);
                }
                for (List<Integer> xSet : xSets.keySet()) {
                    TreeSet<Integer> yList = new TreeSet<Integer>(xSet);
                    TreeSet<Integer> xList = new TreeSet<Integer>(ySets.get(xSet));
                    GEMFRange range = new GEMFFile.GEMFRange();
                    range.zoom = zoom;
                    range.sourceIndex = sourceIndex.get(source);
                    range.xMin = xList.first();
                    range.xMax = xList.last();
                    for (int i = yList.first(); i < yList.last() + 1; ++i) {
                        if (yList.contains(new Integer(i))) {
                            if (range.yMin == null) range.yMin = i;
                            range.yMax = i;
                        } else {
                            ranges.add(range);
                            range = new GEMFFile.GEMFRange();
                            range.zoom = zoom;
                            range.sourceIndex = sourceIndex.get(source);
                            range.xMin = xList.first();
                            range.xMax = xList.last();
                        }
                    }
                    ranges.add(range);
                }
            }
        }
        int source_list_size = 0;
        for (String source : sourceIndex.keySet()) source_list_size += (U32_SIZE + U32_SIZE + source.length());
        long offset = U32_SIZE + U32_SIZE + U32_SIZE + source_list_size + ranges.size() * ((U32_SIZE * 6) + U64_SIZE) + U32_SIZE;
        for (GEMFRange range : ranges) {
            range.offset = offset;
            for (int x = range.xMin; x < range.xMax + 1; ++x) {
                for (int y = range.yMin; y < range.yMax + 1; ++y) {
                    offset += (U32_SIZE + U64_SIZE);
                }
            }
        }
        long headerSize = offset;
        RandomAccessFile gemfFile = new RandomAccessFile(pLocation, "rw");
        gemfFile.writeInt(VERSION);
        gemfFile.writeInt(TILE_SIZE);
        gemfFile.writeInt(sourceIndex.size());
        for (String source : sourceIndex.keySet()) {
            gemfFile.writeInt(sourceIndex.get(source));
            gemfFile.writeInt(source.length());
            gemfFile.write(source.getBytes());
        }
        gemfFile.writeInt(ranges.size());
        for (GEMFRange range : ranges) {
            gemfFile.writeInt(range.zoom);
            gemfFile.writeInt(range.xMin);
            gemfFile.writeInt(range.xMax);
            gemfFile.writeInt(range.yMin);
            gemfFile.writeInt(range.yMax);
            gemfFile.writeInt(range.sourceIndex);
            gemfFile.writeLong(range.offset);
        }
        for (GEMFRange range : ranges) {
            for (int x = range.xMin; x < range.xMax + 1; ++x) {
                for (int y = range.yMin; y < range.yMax + 1; ++y) {
                    gemfFile.writeLong(offset);
                    long fileSize = dirIndex.get(indexSource.get(range.sourceIndex)).get(range.zoom).get(x).get(y).length();
                    gemfFile.writeInt((int) fileSize);
                    offset += fileSize;
                }
            }
        }
        byte[] buf = new byte[FILE_COPY_BUFFER_SIZE];
        long currentOffset = headerSize;
        int fileIndex = 0;
        for (GEMFRange range : ranges) {
            for (int x = range.xMin; x < range.xMax + 1; ++x) {
                for (int y = range.yMin; y < range.yMax + 1; ++y) {
                    long fileSize = dirIndex.get(indexSource.get(range.sourceIndex)).get(range.zoom).get(x).get(y).length();
                    if (currentOffset + fileSize > FILE_SIZE_LIMIT) {
                        gemfFile.close();
                        ++fileIndex;
                        gemfFile = new RandomAccessFile(pLocation + "-" + fileIndex, "rw");
                        currentOffset = 0;
                    } else {
                        currentOffset += fileSize;
                    }
                    FileInputStream tile = new FileInputStream(dirIndex.get(indexSource.get(range.sourceIndex)).get(range.zoom).get(x).get(y));
                    int read = tile.read(buf, 0, FILE_COPY_BUFFER_SIZE);
                    while (read != -1) {
                        gemfFile.write(buf, 0, read);
                        read = tile.read(buf, 0, FILE_COPY_BUFFER_SIZE);
                    }
                    tile.close();
                }
            }
        }
        gemfFile.close();
        openFiles();
        readHeader();
    }

    public void close() throws IOException {
        for (RandomAccessFile file : mFiles) {
            file.close();
        }
    }

    private void openFiles() throws FileNotFoundException {
        File base = new File(mLocation);
        mFiles.add(new RandomAccessFile(base, "r"));
        int i = 0;
        for (; ; ) {
            i = i + 1;
            File nextFile = new File(mLocation + "-" + i);
            if (nextFile.exists()) {
                mFiles.add(new RandomAccessFile(nextFile, "r"));
            } else {
                break;
            }
        }
    }

    private void readHeader() throws IOException {
        final RandomAccessFile baseFile = mFiles.get(0);
        for (RandomAccessFile file : mFiles) {
            mFileSizes.add(file.length());
        }
        int version = baseFile.readInt();
        if (version != VERSION) {
            throw new IOException("Bad file version: " + version);
        }
        int tile_size = baseFile.readInt();
        if (tile_size != TILE_SIZE) {
            throw new IOException("Bad tile size: " + tile_size);
        }
        int sourceCount = baseFile.readInt();
        for (int i = 0; i < sourceCount; i++) {
            int sourceIndex = baseFile.readInt();
            int sourceNameLength = baseFile.readInt();
            byte[] nameData = new byte[sourceNameLength];
            baseFile.read(nameData, 0, sourceNameLength);
            String sourceName = new String(nameData);
            mSources.put(new Integer(sourceIndex), sourceName);
        }
        int num_ranges = baseFile.readInt();
        for (int i = 0; i < num_ranges; i++) {
            GEMFRange rs = new GEMFRange();
            rs.zoom = baseFile.readInt();
            rs.xMin = baseFile.readInt();
            rs.xMax = baseFile.readInt();
            rs.yMin = baseFile.readInt();
            rs.yMax = baseFile.readInt();
            rs.sourceIndex = baseFile.readInt();
            rs.offset = baseFile.readLong();
            mRangeData.add(rs);
        }
    }

    public String getName() {
        return mLocation;
    }

    public HashMap<Integer, String> getSources() {
        return mSources;
    }

    public void selectSource(int pSource) {
        if (mSources.containsKey(new Integer(pSource))) {
            mSourceLimited = true;
            mCurrentSource = pSource;
        }
    }

    public void acceptAnySource() {
        mSourceLimited = false;
    }

    public Set<Integer> getZoomLevels() {
        Set<Integer> zoomLevels = new TreeSet<Integer>();
        for (GEMFRange rs : mRangeData) {
            zoomLevels.add(rs.zoom);
        }
        return zoomLevels;
    }

    public InputStream getInputStream(int pX, int pY, int pZ) {
        GEMFRange range = null;
        for (GEMFRange rs : mRangeData) {
            if ((pZ == rs.zoom) && (pX >= rs.xMin) && (pX <= rs.xMax) && (pY >= rs.yMin) && (pY <= rs.yMax) && ((!mSourceLimited) || (rs.sourceIndex == mCurrentSource))) {
                range = rs;
                break;
            }
        }
        if (range == null) {
            return null;
        }
        byte[] dataBuf;
        long dataOffset;
        int dataLength;
        try {
            int numY = range.yMax + 1 - range.yMin;
            int xIndex = pX - range.xMin;
            int yIndex = pY - range.yMin;
            long offset = (xIndex * numY) + yIndex;
            offset *= (U32_SIZE + U64_SIZE);
            offset += range.offset;
            RandomAccessFile baseFile = mFiles.get(0);
            baseFile.seek(offset);
            dataOffset = baseFile.readLong();
            dataLength = baseFile.readInt();
            RandomAccessFile pDataFile = mFiles.get(0);
            int index = 0;
            if (dataOffset > mFileSizes.get(0)) {
                int fileListCount = mFileSizes.size();
                while ((index < (fileListCount - 1)) && (dataOffset > mFileSizes.get(index))) {
                    dataOffset -= mFileSizes.get(index);
                    index += 1;
                }
                pDataFile = mFiles.get(index);
            }
            dataBuf = new byte[dataLength];
            pDataFile.seek(dataOffset);
            int read = pDataFile.read(dataBuf, 0, dataLength);
            while (read < dataLength) {
                read += pDataFile.read(dataBuf, read, dataLength);
            }
        } catch (java.io.IOException e) {
            return null;
        }
        return new ByteArrayInputStream(dataBuf, 0, dataLength);
    }

    private class GEMFRange {

        Integer zoom;

        Integer xMin;

        Integer xMax;

        Integer yMin;

        Integer yMax;

        Integer sourceIndex;

        Long offset;

        public String toString() {
            return String.format("GEMF Range: source=%d, zoom=%d, x=%d-%d, y=%d-%d, offset=0x%08X", sourceIndex, zoom, xMin, xMax, yMin, yMax, offset);
        }
    }

    ;
}
