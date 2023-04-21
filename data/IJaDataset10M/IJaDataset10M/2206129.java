package com.teletalk.jserver.util.filedb;

import java.io.IOException;
import com.teletalk.jserver.util.StringUtils;

/**
 * This class is a direct implementation of the <code>BlockFile</code> interface. By default 
 * this class will create an instance of <code>DataIOFile</code> as data storage.<br>
 * <br>
 * Note: Since an object of this class maintains it's own copy of the current file pointer index it's inadvisable to 
 * modify the file pointer in any other way than by calling the method {@link #setFilePointer(long)} (for instance through the 
 * method {@link DataIO#setFilePointer(long)} in  the associated DataIO object). If however this should be necessary, it's very 
 * important that the file pointer of the object of this class gets a valid value. This can be accomplished by calling the method 
 * {@link #setFilePointer(long)} with <code>0L</code> as argument.
 * 
 * @author Tobias L�fstrand
 * 
 * @since 1.1
 */
public class DefaultBlockFile implements BlockFile, FileDBConstants {

    private final DataIO file;

    private final boolean readOnlyMode;

    private final int blockDataOffset;

    private final int blockSize;

    private int blockCapacity;

    private long currentFilePosition;

    private long lastWrite = -1;

    /**
	 * Creates a new DefaultBlockFile that uses a <code>DataIOFile</code> as data storage.
	 * 
	 * @param fileName the name of the file to read and write data to/from.
	 * @param fileAccessMode the file access mode (see {@link FileDBConstants} for details).
	 * @param blockSize the size of the blocks.
	 * 
	 * @exception IOException if an I/O error occurs.
	 */
    public DefaultBlockFile(final String fileName, final String fileAccessMode, final int blockSize) throws IOException {
        this(new DataIOFile(fileName, fileAccessMode), blockSize);
    }

    /**
	 * Creates a new DefaultBlockFile that uses the specified DataIO object as data storage.
	 * 
	 * @param dataIO the DataIO object to be used as data storage.
	 * @param blockSize the size of the blocks.
	 * 
	 * @exception IOException if an I/O error occurs.
	 */
    public DefaultBlockFile(DataIO dataIO, final int blockSize) throws IOException {
        this(dataIO, blockSize, 0);
    }

    /**
	 * Creates a new DefaultBlockFile that uses a <code>DataIOFile</code> as data storage.
	 * 
	 * @param fileName the name of the file to read and write data to/from.
	 * @param fileAccessMode the file access mode (see {@link FileDBConstants} for details).
	 * @param blockSize the size of the blocks.
	 * @param blockDataOffset the number of bytes before the first block in this BlockFile.
	 * 
	 * @exception IOException if an I/O error occurs.
	 */
    public DefaultBlockFile(final String fileName, final String fileAccessMode, final int blockSize, final int blockDataOffset) throws IOException {
        this(new DataIOFile(fileName, fileAccessMode), blockSize, blockDataOffset);
    }

    /**
	 * Creates a new DefaultBlockFile that uses a <code>DataIOFile</code> as data storage.
	 * 
	 * @param dataIO the DataIO object to be used as data storage.
	 * @param blockSize the size of the blocks.
	 * @param blockDataOffset the number of bytes before the first block in this BlockFile.
	 * 
	 * @exception IOException if an I/O error occurs.
	 */
    public DefaultBlockFile(DataIO dataIO, final int blockSize, final int blockDataOffset) throws IOException {
        this.file = dataIO;
        this.readOnlyMode = file.isReadOnly();
        resetFilePointer(0L);
        this.blockDataOffset = blockDataOffset;
        this.blockSize = blockSize;
        this.blockCapacity = calculateNumberOfBlocksInFile();
        this.lastWrite = this.file.getLastModified();
    }

    /**
	 * Calculates the number of blocks in the file. No shit Sherlock.
	 */
    private int calculateNumberOfBlocksInFile() throws IOException {
        final long fLength = file.length() - this.blockDataOffset;
        if (fLength > 0) return (int) (fLength / this.blockSize); else return 0;
    }

    /**
    * Flushes any buffered data to the underlying file used by this object..
    * 
    * @since 2.1.3 (20060330)
    */
    public void flush() throws IOException {
        this.file.flush();
        this.lastWrite = System.currentTimeMillis();
    }

    /**
    * Gets the time that the underlying file used by this object was last modified. 
    * 
    * @since 2.1.3 (20060330)
    */
    public long getLastModified() {
        return this.file.getLastModified();
    }

    /**
    * Gets the time that a write was last performed on the block file through this object. 
    * 
    * @since 2.1.3 (20060330)
    */
    public long getLastWrite() {
        return this.lastWrite;
    }

    /**
    * Checks if the underlying file has been modified externally, i.e. through another interface or application.<br>
    * <br>
    * <b>Note:</b> Care should be exercised when using this method in write mode and when the storage is 
    * located on a different computer. 
    * 
    * @since 2.1.3 (20060330)
    */
    public boolean isModifiedExternally() {
        return this.getLastModified() > (this.getLastWrite() + 100);
    }

    /**
	 * Gets the DataOI object associated with this DefaultBlockFile.
	 * 
	 * @return the DataOI object associated with this DefaultBlockFile.
	 */
    public DataIO getDataIO() {
        return this.file;
    }

    /**
	 * Checks if this DefaultBlockFile is in read only mode.
	 * 
	 * @return <code>true</code> if this DefaultBlockFile is in read only mode, otherwise <code>false</code>.
	 */
    public boolean isReadOnly() {
        return this.readOnlyMode;
    }

    /**
	 * Gets the file header size, i.e. the number of bytes before the first block in this DefaultBlockFile.
	 * 
	 * @return the file header size.
	 */
    public int getFileHeaderSize() {
        return this.blockDataOffset;
    }

    /**
	 * Gets the size of the blocks in this DefaultBlockFile.
	 * 
	 * @return the size of the blocks in this DefaultBlockFile.
	 */
    public int getBlockSize() {
        return this.blockSize;
    }

    /**
	 * Gets the number of blocks that this DefaultBlockFile currently has room for.
	 * 
	 * @return the number of blocks that this DefaultBlockFile currently has room for.
	 */
    public int getBlockCapacity() {
        return this.blockCapacity;
    }

    /**
	 * Sets the number of blocks that this DefaultBlockFile should have room for.
	 * 
	 * @param blockCapacity the number of blocks that DefaultBlockFile BlockFile should have room for.
	 * 
	 * @exception IOException if an I/O error occurs.
	 */
    public void setBlockCapacity(final int blockCapacity) throws IOException {
        this.blockCapacity = blockCapacity;
        this.file.setLength((blockCapacity * this.blockSize) + this.blockDataOffset);
        this.lastWrite = System.currentTimeMillis();
    }

    /**
	 * Gets the current index of the file pointer.
	 * 
	 * @return the current index of the file pointer.
	 * 
	 * @exception IOException if an I/O error occurs.
	 */
    public long getFilePointer() throws IOException {
        return currentFilePosition;
    }

    /**
	 * Sets the current index of the file pointer.
	 * 
	 * @param fp the new index of the file pointer.
	 * 
	 * @exception IOException if an I/O error occurs.
	 */
    public void setFilePointer(final long fp) throws IOException {
        if (currentFilePosition != fp) {
            this.currentFilePosition = fp;
            this.file.setFilePointer(currentFilePosition);
        }
    }

    /**
	 * Gets the index of the first byte of the specified block number.
	 * 
	 * @param blockNumber an index of a block.
	 * 
	 * @return the index of the first byte of the specified block number.
	 */
    public long getBlockStartFP(final int blockNumber) {
        return blockNumber * this.blockSize + this.blockDataOffset;
    }

    /**
	 * Resets the file pointer.
	 */
    private void resetFilePointer(final long fp) throws IOException {
        this.currentFilePosition = fp;
        this.file.setFilePointer(fp);
    }

    /**
	 * Reads the data in the block specied by parameter <code>blockNumber</code>.
	 * 
	 * @param blockNumber the index of the block to read.
	 * 
	 * @return the data in the block. The size of the returned byte array will be the current block size.
	 * 
	 * @exception IOException if an I/O error occurs.
	 */
    public byte[] readBlock(final int blockNumber) throws IOException {
        final byte[] blockBuf = new byte[this.blockSize];
        this.readBlocks(blockNumber, blockBuf, 0, 1);
        return blockBuf;
    }

    /**
	 * Reads the data in the blocks specied by parameter <code>blockNumbers</code>.
	 * 
	 * @param blockNumbers the indices of the blocks to read.
	 * 
	 * @return the data in the blocks. The size of the returned byte array will be the current block size * the length of the <code>blockNumbers</code> array.
	 * 
	 * @exception IOException if an I/O error occurs.
	 */
    public byte[] readBlocks(final int[] blockNumbers) throws IOException {
        final byte[] blockBuf = new byte[this.blockSize * blockNumbers.length];
        int numberOfAdjacentBlocks;
        for (int i = 0; i < blockNumbers.length; ) {
            numberOfAdjacentBlocks = 1;
            for (int q = i; (q < (blockNumbers.length - 1)) && ((blockNumbers[q] + 1) == blockNumbers[q + 1]); q++, numberOfAdjacentBlocks++) ;
            this.readBlocks(blockNumbers[i], blockBuf, i * this.blockSize, numberOfAdjacentBlocks);
            i += numberOfAdjacentBlocks;
        }
        return blockBuf;
    }

    /**
	 * Reads only a part of the block specified by parameter <code>blockNumber</code>.
	 * 
	 * @param blockNumber the index of the block to read.
	 * @param blockOffset the offset from the beginning of the block.
	 * @param partialBlockData the byte array into which data will be read.
	 * @param partialBlockDataOffset the offset in the partialBlockData array where the read data is to be copied.
	 * @param partialBlockDataLength the number of bytes of data to read.
	 * 
	 * @exception IOException if an I/O error occurs.
	 */
    public void readPartialBlock(final int blockNumber, final int blockOffset, final byte[] partialBlockData, final int partialBlockDataOffset, final int partialBlockDataLength) throws IOException {
        if ((partialBlockDataLength + blockOffset) > this.blockSize) throw new IOException("Attempted to read partial block, but the amount of data to read exceeded the blocksize (blockNumber: " + blockNumber + ", blockOffset: " + blockOffset + ", data length: " + partialBlockDataLength + " ).");
        this.setFilePointer(this.getBlockStartFP(blockNumber) + blockOffset);
        this.read(partialBlockData, partialBlockDataOffset, partialBlockDataLength);
    }

    /**
    * Reads only a part of the blocks specified by parameter <code>blockNumbers</code>.
    * 
    * @param blockNumbers the indices of the blocks to read.
    * @param blockOffset the offset from the beginning of the block.
    * @param partialBlockData the byte array into which data will be read.
    * @param partialBlockDataOffset the offset in the partialBlockData array where the read data is to be copied.
    * @param partialBlockDataLength the number of bytes of data to read in each block (i.e. NOT the total length of the data).
    * 
    * @exception IOException if an I/O error occurs.
    */
    public void readPartialBlocks(final int[] blockNumbers, final int blockOffset, final byte[] partialBlockData, final int partialBlockDataOffset, final int partialBlockDataLength) throws IOException {
        if ((partialBlockDataLength + blockOffset) > this.blockSize) throw new IOException("Attempted to read partial block, but the amount of data to read exceeded the blocksize (blockNumbers: " + StringUtils.toString(blockNumbers) + ", blockOffset: " + blockOffset + ", data length: " + partialBlockDataLength + " ).");
        int offset = 0;
        for (int i = 0; i < blockNumbers.length; i++) {
            this.setFilePointer(this.getBlockStartFP(blockNumbers[i]) + blockOffset);
            this.read(partialBlockData, offset + partialBlockDataOffset, partialBlockDataLength);
            offset += partialBlockDataLength;
        }
    }

    /**
	 * Reads some blocks...
	 */
    private void readBlocks(final int startBlock, final byte[] buf, final int bufOffset, final int numberOfBlocksToRead) throws IOException {
        if ((startBlock + numberOfBlocksToRead) > this.blockCapacity) throw new IOException("Block outside file bounds (startBlock: " + startBlock + ", numberOfBlocksToRead: " + numberOfBlocksToRead + ", blockCapacity: " + blockCapacity + " ).");
        this.setFilePointer(this.getBlockStartFP(startBlock));
        this.read(buf, bufOffset, this.blockSize * numberOfBlocksToRead);
    }

    /**
	 * Writes a block of data to this BlockFile. The maximum number of bytes that will be written from the 
	 * specified array will be equal to the block size, or less if the specified array doesn't contain that many 
	 * bytes.
	 * 
	 * @param blockNumber the index of the block to write to.
	 * @param blockData the data to be written. 
	 * @param blockDataOffset the start offset in the blockData.
	 * 
	 * @exception IOException if an I/O error occurs.
	 */
    public void writeBlock(final int blockNumber, final byte[] blockData, final int blockDataOffset) throws IOException {
        this.setFilePointer(this.getBlockStartFP(blockNumber));
        int dataToWrite = ((blockData.length - blockDataOffset) > this.blockSize) ? this.blockSize : (blockData.length - blockDataOffset);
        this.write(blockData, blockDataOffset, dataToWrite);
    }

    /**
	 * Writes multiple blocks of data to this BlockFile. The maximum number of bytes that will be written from the 
	 * specified array will be equal to the block size times the length of the specified block numbers array, or less if the specified array 
	 * doesn't contain that many bytes.
	 * 
	 * @param blockNumbers the indices of the blocks to write to.
	 * @param blockData the data to be written.
	 * @param blockDataOffset the start offset in the blockData.
	 * 
	 * @exception IOException if an I/O error occurs.
	 */
    public void writeBlocks(final int[] blockNumbers, final byte[] blockData, final int blockDataOffset) throws IOException {
        if ((blockData.length - blockDataOffset) <= ((blockNumbers.length - 1) * this.blockSize)) {
            StringBuffer numbers = new StringBuffer();
            for (int i = 0; i < blockNumbers.length; i++) {
                numbers.append(String.valueOf(blockNumbers[i]));
                if (i < (blockNumbers.length - 1)) numbers.append(", ");
            }
            throw new IOException("Attempted to write blocks, but the length of the data was to small for the specified number of block numbers (blockNumbers: [" + numbers + "], data length: " + (blockData.length - blockDataOffset) + ", blockSize: " + this.blockSize + " ).");
        }
        int numberOfAdjacentBlocks;
        int dataLength;
        for (int i = 0; i < blockNumbers.length; ) {
            numberOfAdjacentBlocks = 1;
            for (int q = i; (q < (blockNumbers.length - 1)) && ((blockNumbers[q] + 1) == blockNumbers[q + 1]); q++, numberOfAdjacentBlocks++) ;
            this.setFilePointer(this.getBlockStartFP(blockNumbers[i]));
            if ((i + numberOfAdjacentBlocks) >= blockNumbers.length) {
                dataLength = (blockData.length - blockDataOffset) - i * this.blockSize;
                if (dataLength > (this.blockSize * numberOfAdjacentBlocks)) dataLength = (this.blockSize * numberOfAdjacentBlocks);
            } else dataLength = this.blockSize * numberOfAdjacentBlocks;
            this.write(blockData, blockDataOffset + (i * this.blockSize), dataLength);
            i += numberOfAdjacentBlocks;
        }
    }

    /**
	 * Writes data to only a part of the block specified by parameter <code>blockNumber</code>.
	 * 
	 * @param blockNumber the index of the block to write to.
	 * @param blockOffset the offset from the beginning of the block where writing will start.
	 * @param blockData the data to be written.
	 * @param blockDataOffset the offset in the partialBlockData array from which to get data to be written.
	 * @param blockDataLength the number of bytes of data to write.
	 * 
	 * @exception IOException if an I/O error occurs.
	 */
    public void writePartialBlock(final int blockNumber, final int blockOffset, final byte[] blockData, final int blockDataOffset, final int blockDataLength) throws IOException {
        if ((blockDataLength + blockOffset) > this.blockSize) throw new IOException("Attempted to write partial block, but the length of the data exceeded the blocksize (blockNumber: " + blockNumber + ", blockOffset: " + blockOffset + ", data length: " + blockDataLength + ", blockSize: " + this.blockSize + " ).");
        this.setFilePointer(this.getBlockStartFP(blockNumber) + blockOffset);
        this.write(blockData, blockDataOffset, blockDataLength);
    }

    /**
	 * Reads data from the current file position.
	 * 
	 * @param buf the buffer to read data into.
	 * @param bufOffset the start offset in buf.
	 * @param length the amount of data to read.
	 * 
	 * @exception IOException if an I/O error occurs.
	 */
    public final void read(final byte[] buf, final int bufOffset, final int length) throws IOException {
        currentFilePosition = -currentFilePosition;
        this.file.readFully(buf, bufOffset, length);
        currentFilePosition = (-currentFilePosition) + length;
    }

    /**
	 * Writes data to the current file position.
	 * 
	 * @param buf the buffer to write data from.
	 * @param bufOffset the start offset in buf.
	 * @param length the amount of data to write.
	 * 
	 * @exception IOException if an I/O error occurs.
	 */
    public final void write(final byte[] buf, final int bufOffset, final int length) throws IOException {
        if (this.readOnlyMode) throw new IOException("Cannot write in read only mode!");
        currentFilePosition = -currentFilePosition;
        this.file.write(buf, bufOffset, length);
        currentFilePosition = (-currentFilePosition) + length;
        this.lastWrite = System.currentTimeMillis();
    }

    /**
	 * Closes this DefaultBlockFile.
	 * 
	 * @exception IOException if an I/O error occurs.
	 */
    public final void close() throws IOException {
        this.file.flush();
        this.file.close();
    }
}
