package com.cooliris.media;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import com.cooliris.cache.CacheService;
import android.util.Log;

public final class DiskCache {

    private static final String TAG = "DiskCache";

    private static final int CHUNK_SIZE = 1048576;

    private static final int INDEX_HEADER_MAGIC = 0xcafe;

    private static final int INDEX_HEADER_VERSION = 2;

    private static final String INDEX_FILE_NAME = "index";

    private static final String CHUNK_FILE_PREFIX = "chunk_";

    private final String mCacheDirectoryPath;

    private LongSparseArray<Record> mIndexMap;

    private final LongSparseArray<RandomAccessFile> mChunkFiles = new LongSparseArray<RandomAccessFile>();

    private int mTailChunk = 0;

    private int mNumInsertions = 0;

    public DiskCache(String cacheDirectoryName) {
        String cacheDirectoryPath = CacheService.getCachePath(cacheDirectoryName);
        File cacheDirectory = new File(cacheDirectoryPath);
        if (!cacheDirectory.isDirectory() && !cacheDirectory.mkdirs()) {
            Log.e(TAG, "Unable to create cache directory " + cacheDirectoryPath);
        }
        mCacheDirectoryPath = cacheDirectoryPath;
        loadIndex();
    }

    @Override
    public void finalize() {
        shutdown();
    }

    public byte[] get(long key, long timestamp) {
        Record record = null;
        synchronized (mIndexMap) {
            record = mIndexMap.get(key);
        }
        if (record != null) {
            if (record.timestamp < timestamp) {
                Log.i(TAG, "File has been updated to " + timestamp + " since the last time " + record.timestamp + " stored in cache.");
                return null;
            }
            try {
                RandomAccessFile chunkFile = getChunkFile(record.chunk);
                if (chunkFile != null) {
                    byte[] data = new byte[record.size];
                    chunkFile.seek(record.offset);
                    chunkFile.readFully(data);
                    return data;
                }
            } catch (Exception e) {
                Log.e(TAG, "Unable to read from chunk file");
            }
        }
        return null;
    }

    public boolean isDataAvailable(long key, long timestamp) {
        Record record = null;
        synchronized (mIndexMap) {
            record = mIndexMap.get(key);
        }
        if (record == null) {
            return false;
        }
        if (record.timestamp < timestamp) {
            return false;
        }
        if (record.size == 0) return false;
        return true;
    }

    public void put(long key, byte[] data, long timestamp) {
        Record record = null;
        synchronized (mIndexMap) {
            record = mIndexMap.get(key);
        }
        if (record != null && data.length <= record.sizeOnDisk) {
            int currentChunk = record.chunk;
            try {
                RandomAccessFile chunkFile = getChunkFile(record.chunk);
                if (chunkFile != null) {
                    chunkFile.seek(record.offset);
                    chunkFile.write(data);
                    synchronized (mIndexMap) {
                        mIndexMap.put(key, new Record(currentChunk, record.offset, data.length, record.sizeOnDisk, timestamp));
                    }
                    if (++mNumInsertions == 32) {
                        flush();
                    }
                    return;
                }
            } catch (Exception e) {
                Log.e(TAG, "Unable to read from chunk file");
            }
        }
        final int chunk = mTailChunk;
        final RandomAccessFile chunkFile = getChunkFile(chunk);
        if (chunkFile != null) {
            try {
                final int offset = (int) chunkFile.length();
                chunkFile.seek(offset);
                chunkFile.write(data);
                synchronized (mIndexMap) {
                    mIndexMap.put(key, new Record(chunk, offset, data.length, data.length, timestamp));
                }
                if (offset + data.length > CHUNK_SIZE) {
                    ++mTailChunk;
                }
                if (++mNumInsertions == 32) {
                    flush();
                }
            } catch (IOException e) {
                Log.e(TAG, "Unable to write new entry to chunk file");
            }
        } else {
            Log.e(TAG, "getChunkFile() returned null");
        }
    }

    public void delete(long key) {
        synchronized (mIndexMap) {
            mIndexMap.remove(key);
        }
    }

    public void deleteAll() {
        shutdown();
        File cacheDirectory = new File(mCacheDirectoryPath);
        String[] cacheFiles = cacheDirectory.list();
        if (cacheFiles == null) return;
        for (String cacheFile : cacheFiles) {
            new File(cacheDirectory, cacheFile).delete();
        }
    }

    public void flush() {
        if (mNumInsertions != 0) {
            mNumInsertions = 0;
            writeIndex();
        }
    }

    public void close() {
        writeIndex();
        shutdown();
    }

    private void shutdown() {
        synchronized (mChunkFiles) {
            for (int i = 0, size = mChunkFiles.size(); i < size; ++i) {
                try {
                    mChunkFiles.valueAt(i).close();
                } catch (Exception e) {
                    Log.e(TAG, "Unable to close chunk file");
                }
            }
            mChunkFiles.clear();
        }
        if (mIndexMap != null) {
            synchronized (mIndexMap) {
                if (mIndexMap != null) {
                    mIndexMap.clear();
                }
            }
        }
    }

    private String getIndexFilePath() {
        return mCacheDirectoryPath + INDEX_FILE_NAME;
    }

    private void loadIndex() {
        final String indexFilePath = getIndexFilePath();
        try {
            final FileInputStream fileInput = new FileInputStream(indexFilePath);
            final BufferedInputStream bufferedInput = new BufferedInputStream(fileInput, 1024);
            final DataInputStream dataInput = new DataInputStream(bufferedInput);
            final int magic = dataInput.readInt();
            final int version = dataInput.readInt();
            boolean valid = true;
            if (magic != INDEX_HEADER_MAGIC) {
                Log.e(TAG, "Index file appears to be corrupt (" + magic + " != " + INDEX_HEADER_MAGIC + "), " + indexFilePath);
                valid = false;
            }
            if (valid && version != INDEX_HEADER_VERSION) {
                Log.e(TAG, "Index file version " + version + " not supported");
                valid = false;
            }
            if (valid) {
                mTailChunk = dataInput.readShort();
            }
            if (valid) {
                final int numEntries = dataInput.readInt();
                mIndexMap = new LongSparseArray<Record>(numEntries);
                synchronized (mIndexMap) {
                    for (int i = 0; i < numEntries; ++i) {
                        final long key = dataInput.readLong();
                        final int chunk = dataInput.readShort();
                        final int offset = dataInput.readInt();
                        final int size = dataInput.readInt();
                        final int sizeOnDisk = dataInput.readInt();
                        final long timestamp = dataInput.readLong();
                        mIndexMap.append(key, new Record(chunk, offset, size, sizeOnDisk, timestamp));
                    }
                }
            }
            dataInput.close();
            if (!valid) {
                deleteAll();
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            Log.e(TAG, "Unable to read the index file " + indexFilePath);
        } finally {
            if (mIndexMap == null) {
                mIndexMap = new LongSparseArray<Record>();
            }
        }
    }

    private void writeIndex() {
        File tempFile = null;
        final String tempFilePath = mCacheDirectoryPath;
        final String indexFilePath = getIndexFilePath();
        try {
            tempFile = File.createTempFile("DiskCache", null, new File(tempFilePath));
        } catch (Exception e) {
            Log.e(TAG, "Unable to create or tempFile " + tempFilePath);
            return;
        }
        try {
            final FileOutputStream fileOutput = new FileOutputStream(tempFile);
            final BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput, 1024);
            final DataOutputStream dataOutput = new DataOutputStream(bufferedOutput);
            final int numRecords = mIndexMap.size();
            dataOutput.writeInt(INDEX_HEADER_MAGIC);
            dataOutput.writeInt(INDEX_HEADER_VERSION);
            dataOutput.writeShort(mTailChunk);
            dataOutput.writeInt(numRecords);
            for (int i = 0; i < numRecords; ++i) {
                final long key = mIndexMap.keyAt(i);
                final Record record = mIndexMap.valueAt(i);
                dataOutput.writeLong(key);
                dataOutput.writeShort(record.chunk);
                dataOutput.writeInt(record.offset);
                dataOutput.writeInt(record.size);
                dataOutput.writeInt(record.sizeOnDisk);
                dataOutput.writeLong(record.timestamp);
            }
            dataOutput.close();
            tempFile.renameTo(new File(indexFilePath));
        } catch (Exception e) {
            Log.e(TAG, "Unable to write the index file " + indexFilePath);
            tempFile.delete();
        }
    }

    private RandomAccessFile getChunkFile(int chunk) {
        RandomAccessFile chunkFile = null;
        synchronized (mChunkFiles) {
            chunkFile = mChunkFiles.get(chunk);
        }
        if (chunkFile == null) {
            final String chunkFilePath = mCacheDirectoryPath + CHUNK_FILE_PREFIX + chunk;
            try {
                chunkFile = new RandomAccessFile(chunkFilePath, "rw");
            } catch (FileNotFoundException e) {
                Log.e(TAG, "Unable to create or open the chunk file " + chunkFilePath);
            }
            synchronized (mChunkFiles) {
                mChunkFiles.put(chunk, chunkFile);
            }
        }
        return chunkFile;
    }

    private static final class Record {

        public Record(int chunk, int offset, int size, int sizeOnDisk, long timestamp) {
            this.chunk = chunk;
            this.offset = offset;
            this.size = size;
            this.timestamp = timestamp;
            this.sizeOnDisk = sizeOnDisk;
        }

        public final long timestamp;

        public final int chunk;

        public final int offset;

        public final int size;

        public final int sizeOnDisk;
    }
}
