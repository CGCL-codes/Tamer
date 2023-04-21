package org.neodatis.fs.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.neodatis.fs.Block;
import org.neodatis.fs.NdfsConfig;
import org.neodatis.fs.NdfsException;
import org.neodatis.fs.NdfsFile;
import org.neodatis.tool.DLogger;

/**
 * @author olivier
 *
 */
public class BlockReader {

    protected RandomAccessFile raf;

    protected long blockId;

    protected NdfsFile file;

    protected int blockSize;

    public BlockReader(NdfsFile file) {
        this.file = file;
        this.blockSize = file.getProperties().getBlockSize();
        initRaf();
        blockId = 0;
    }

    private void initRaf() {
        String fullName = new File(file.getFullName()).getAbsolutePath();
        File parentDir = new File(fullName).getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        try {
            this.raf = new RandomAccessFile(fullName, "rw");
        } catch (FileNotFoundException e) {
            throw new NdfsException(e);
        }
    }

    public void setBlockId(long blockId) {
        this.blockId = blockId;
    }

    public boolean hasNext() throws IOException {
        return (this.blockId + 1) * blockSize < raf.length();
    }

    public Block next() throws IOException {
        if (NdfsConfig.debug()) {
            DLogger.info(String.format("Reading block id %d at position %d", blockId, raf.getFilePointer()));
        }
        Block block = new Block(file, blockId);
        int size = raf.read(block.bytes);
        if (size != blockSize) {
            throw new NdfsException(String.format("Reading block id %d and its size is %d instead of %d", blockId, size, blockSize));
        }
        blockId++;
        return block;
    }

    public void close() throws IOException {
        raf.close();
    }

    public long getNumberOfBlocks() throws IOException {
        return raf.length() / blockSize;
    }

    public long getFileSize() throws IOException {
        return raf.length();
    }

    /**
	 * @param blockId2
	 * @return
	 */
    public Block readBlock(long blockIdToRead) {
        try {
            raf.seek(blockIdToRead * blockSize);
            Block block = new Block(file, blockIdToRead);
            int size = raf.read(block.bytes);
            return block;
        } catch (NdfsException e) {
            throw e;
        } catch (Exception e) {
            throw new NdfsException(e);
        }
    }
}
