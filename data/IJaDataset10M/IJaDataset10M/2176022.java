package org.processmining.framework.log.rfb.fsio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.processmining.framework.log.rfb.io.RandomAccessStorage;

/**
 * @author Christian W. Guenther (christian@deckfour.org)
 *
 */
public class FS2RandomAccessStorage implements RandomAccessStorage {

    protected FS2VirtualFileSystem vfs;

    protected List<FS2Block> blocks;

    protected long size;

    protected long pointer;

    protected DataOutputStream dataOutputStream;

    protected DataInputStream dataInputStream;

    public FS2RandomAccessStorage(FS2VirtualFileSystem virtualFileSystem) {
        vfs = virtualFileSystem;
        size = 0;
        pointer = 0;
        blocks = new ArrayList<FS2Block>();
        dataOutputStream = new DataOutputStream(new FS2BlockOutputStream());
        dataInputStream = new DataInputStream(new FS2BlockInputStream());
    }

    protected synchronized void adjustSize() {
        if (pointer > size) {
            size = pointer;
        }
    }

    protected int translateToBlockNumber(long offset) {
        return (int) (offset / vfs.blockSize());
    }

    protected int translateToBlockOffset(long offset) {
        return (int) (offset % vfs.blockSize());
    }

    public synchronized void close() throws IOException {
        for (int i = blocks.size() - 1; i >= 0; i--) {
            blocks.remove(i).close();
        }
        size = 0;
        pointer = 0;
    }

    public synchronized RandomAccessStorage copy() throws IOException {
        FS2RandomAccessStorage clone = new FS2RandomAccessStorage(vfs);
        if (blocks.size() > 0) {
            byte[] buffer = new byte[blocks.get(0).size()];
            for (FS2Block block : blocks) {
                FS2Block copyBlock = vfs.allocateBlock();
                block.read(0, buffer);
                copyBlock.write(0, buffer);
                clone.blocks.add(copyBlock);
            }
        }
        clone.size = size;
        clone.pointer = 0;
        return clone;
    }

    public synchronized long getFilePointer() throws IOException {
        return pointer;
    }

    public synchronized long length() throws IOException {
        return size;
    }

    public synchronized void seek(long pos) throws IOException {
        pointer = pos;
        adjustSize();
    }

    public synchronized int skipBytes(int n) throws IOException {
        pointer += n;
        adjustSize();
        return n;
    }

    public synchronized void write(int b) throws IOException {
        dataOutputStream.write(b);
        dataOutputStream.flush();
    }

    public synchronized void write(byte[] b) throws IOException {
        dataOutputStream.write(b);
        dataOutputStream.flush();
    }

    public synchronized void write(byte[] b, int off, int len) throws IOException {
        dataOutputStream.write(b, off, len);
        dataOutputStream.flush();
    }

    public synchronized void writeBoolean(boolean v) throws IOException {
        dataOutputStream.writeBoolean(v);
        dataOutputStream.flush();
    }

    public synchronized void writeByte(int b) throws IOException {
        dataOutputStream.writeByte(b);
        dataOutputStream.flush();
    }

    public synchronized void writeBytes(String str) throws IOException {
        dataOutputStream.writeBytes(str);
        dataOutputStream.flush();
    }

    public synchronized void writeChar(int c) throws IOException {
        dataOutputStream.writeChar(c);
        dataOutputStream.flush();
    }

    public synchronized void writeChars(String str) throws IOException {
        dataOutputStream.writeChars(str);
        dataOutputStream.flush();
    }

    public synchronized void writeDouble(double d) throws IOException {
        dataOutputStream.writeDouble(d);
        dataOutputStream.flush();
    }

    public synchronized void writeFloat(float f) throws IOException {
        dataOutputStream.writeFloat(f);
        dataOutputStream.flush();
    }

    public synchronized void writeInt(int i) throws IOException {
        dataOutputStream.writeInt(i);
        dataOutputStream.flush();
    }

    public synchronized void writeLong(long l) throws IOException {
        dataOutputStream.writeLong(l);
        dataOutputStream.flush();
    }

    public synchronized void writeShort(int s) throws IOException {
        dataOutputStream.writeShort(s);
        dataOutputStream.flush();
    }

    public synchronized void writeUTF(String str) throws IOException {
        dataOutputStream.writeUTF(str);
        dataOutputStream.flush();
    }

    public synchronized boolean readBoolean() throws IOException {
        return dataInputStream.readBoolean();
    }

    public synchronized byte readByte() throws IOException {
        return dataInputStream.readByte();
    }

    public synchronized char readChar() throws IOException {
        return dataInputStream.readChar();
    }

    public synchronized double readDouble() throws IOException {
        return dataInputStream.readDouble();
    }

    public synchronized float readFloat() throws IOException {
        return dataInputStream.readFloat();
    }

    public synchronized void readFully(byte[] b) throws IOException {
        dataInputStream.readFully(b);
    }

    public synchronized void readFully(byte[] b, int off, int len) throws IOException {
        dataInputStream.readFully(b, off, len);
    }

    public synchronized int readInt() throws IOException {
        return dataInputStream.readInt();
    }

    public synchronized String readLine() throws IOException {
        return dataInputStream.readLine();
    }

    public synchronized long readLong() throws IOException {
        return dataInputStream.readLong();
    }

    public synchronized short readShort() throws IOException {
        return dataInputStream.readShort();
    }

    public synchronized String readUTF() throws IOException {
        return dataInputStream.readUTF();
    }

    public synchronized int readUnsignedByte() throws IOException {
        return dataInputStream.readUnsignedByte();
    }

    public synchronized int readUnsignedShort() throws IOException {
        return dataInputStream.readUnsignedShort();
    }

    /**
	 * Internal support class implementing an input stream over
	 * a list of blocks, as implemented by the enclosing class
	 * 
	 * @author Christian W. Guenther (christian@deckfour.org)
	 *
	 */
    protected class FS2BlockInputStream extends InputStream {

        @Override
        public synchronized int read() throws IOException {
            int blockNumber = translateToBlockNumber(pointer);
            if (blockNumber >= blocks.size()) {
                throw new AssertionError("addressing invalid block for reading! (1)");
            }
            FS2Block block = blocks.get(blockNumber);
            int blockOffset = translateToBlockOffset(pointer);
            pointer++;
            return block.read(blockOffset);
        }

        @Override
        public synchronized int read(byte[] buffer, int offset, int length) throws IOException {
            int blockNumber = translateToBlockNumber(pointer);
            if (blockNumber >= blocks.size()) {
                throw new AssertionError("addressing invalid block for reading! (1)");
            }
            int blockOffset = translateToBlockOffset(pointer);
            FS2Block block = blocks.get(blockNumber);
            int readBytes = block.read(blockOffset, buffer, offset, length);
            length -= readBytes;
            offset += readBytes;
            while (length > 0) {
                blockNumber++;
                if (blockNumber < blocks.size()) {
                    block = blocks.get(blockNumber);
                    int readNow = block.read(0, buffer, offset, length);
                    readBytes += readNow;
                    offset += readNow;
                    length -= readNow;
                } else {
                    break;
                }
            }
            pointer += readBytes;
            return readBytes;
        }

        @Override
        public synchronized int read(byte[] buffer) throws IOException {
            return this.read(buffer, 0, buffer.length);
        }

        @Override
        public synchronized long skip(long skip) throws IOException {
            long nPointer = pointer + skip;
            if (nPointer > size) {
                long skipped = size - pointer;
                pointer = size;
                return skipped;
            } else {
                pointer += skip;
                return skip;
            }
        }

        @Override
        public boolean markSupported() {
            return false;
        }
    }

    /**
	 * Internal support class implementing an output stream over
	 * a list of blocks, as implemented by the enclosing class
	 * 
	 * @author Christian W. Guenther (christian@deckfour.org)
	 *
	 */
    protected class FS2BlockOutputStream extends OutputStream {

        @Override
        public synchronized void write(int value) throws IOException {
            int blockNumber = translateToBlockNumber(pointer);
            int blockOffset = translateToBlockOffset(pointer);
            while (blockNumber >= blocks.size()) {
                blocks.add(vfs.allocateBlock());
            }
            FS2Block block = blocks.get(blockNumber);
            block.write(blockOffset, value);
            pointer++;
            adjustSize();
        }

        @Override
        public synchronized void write(byte[] buffer, int offset, int length) throws IOException {
            int blockNumber = translateToBlockNumber(pointer);
            int blockOffset = translateToBlockOffset(pointer);
            while (blockNumber >= blocks.size()) {
                blocks.add(vfs.allocateBlock());
            }
            FS2Block block = blocks.get(blockNumber);
            int bytesToWrite = block.size() - blockOffset;
            if (bytesToWrite > length) {
                bytesToWrite = length;
            }
            block.write(blockOffset, buffer, offset, bytesToWrite);
            length -= bytesToWrite;
            offset += bytesToWrite;
            int writtenBytes = bytesToWrite;
            while (length > 0) {
                blockNumber++;
                if (blockNumber >= blocks.size()) {
                    blocks.add(vfs.allocateBlock());
                }
                block = blocks.get(blockNumber);
                bytesToWrite = block.size();
                if (bytesToWrite > length) {
                    bytesToWrite = length;
                }
                block.write(0, buffer, offset, bytesToWrite);
                writtenBytes += bytesToWrite;
                length -= bytesToWrite;
                offset += bytesToWrite;
            }
            pointer += writtenBytes;
            adjustSize();
        }

        @Override
        public synchronized void write(byte[] buffer) throws IOException {
            this.write(buffer, 0, buffer.length);
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        protected void finalize() throws Throwable {
            this.close();
            super.finalize();
        }
    }
}
