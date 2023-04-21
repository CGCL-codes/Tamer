package com.knowgate.jcifs.smb;

import java.io.InputStream;
import java.io.IOException;
import com.knowgate.misc.Gadgets;

abstract class AndXServerMessageBlock extends ServerMessageBlock {

    private static final int ANDX_COMMAND_OFFSET = 1;

    private static final int ANDX_RESERVED_OFFSET = 2;

    private static final int ANDX_OFFSET_OFFSET = 3;

    private byte andxCommand = (byte) 0xFF;

    private int andxOffset = 0;

    ServerMessageBlock andx = null;

    AndXServerMessageBlock() {
    }

    AndXServerMessageBlock(ServerMessageBlock andx) {
        this.andx = andx;
        if (andx != null) {
            andxCommand = andx.command;
        }
    }

    abstract int readBytesDirectWireFormat(InputStream in, int byteCount, byte[] buffer, int bufferIndex) throws IOException;

    int getBatchLimit(byte command) {
        return 0;
    }

    int writeWireFormat(byte[] dst, int dstIndex) {
        int start = headerStart = dstIndex;
        dstIndex += writeHeaderWireFormat(dst, dstIndex);
        dstIndex += writeAndXWireFormat(dst, dstIndex);
        length = dstIndex - start;
        if (digest != null) {
            digest.sign(dst, headerStart, length, this, response);
        }
        return length;
    }

    int readWireFormat(InputStream in, byte[] buffer, int bufferIndex) throws IOException {
        int start = bufferIndex;
        if (in.read(buffer, bufferIndex, HEADER_LENGTH) != HEADER_LENGTH) {
            throw new IOException("unexpected EOF reading smb header");
        }
        bufferIndex += readHeaderWireFormat(buffer, bufferIndex);
        bufferIndex += readAndXWireFormat(in, buffer, bufferIndex);
        length = bufferIndex - start;
        return length;
    }

    int writeAndXWireFormat(byte[] dst, int dstIndex) {
        int start = dstIndex;
        wordCount = writeParameterWordsWireFormat(dst, start + ANDX_OFFSET_OFFSET + 2);
        wordCount += 4;
        dstIndex += wordCount + 1;
        wordCount /= 2;
        dst[start] = (byte) (wordCount & 0xFF);
        byteCount = writeBytesWireFormat(dst, dstIndex + 2);
        dst[dstIndex++] = (byte) (byteCount & 0xFF);
        dst[dstIndex++] = (byte) ((byteCount >> 8) & 0xFF);
        dstIndex += byteCount;
        if (andx == null || USE_BATCHING == false || batchLevel >= getBatchLimit(andx.command)) {
            andxCommand = (byte) 0xFF;
            andx = null;
            dst[start + ANDX_COMMAND_OFFSET] = (byte) 0xFF;
            dst[start + ANDX_RESERVED_OFFSET] = (byte) 0x00;
            dst[start + ANDX_OFFSET_OFFSET] = (byte) 0x00;
            dst[start + ANDX_OFFSET_OFFSET + 1] = (byte) 0x00;
            return dstIndex - start;
        }
        andx.batchLevel = batchLevel + 1;
        dst[start + ANDX_COMMAND_OFFSET] = andxCommand;
        dst[start + ANDX_RESERVED_OFFSET] = (byte) 0x00;
        andxOffset = dstIndex - headerStart;
        writeInt2(andxOffset, dst, start + ANDX_OFFSET_OFFSET);
        andx.useUnicode = useUnicode;
        if (andx instanceof AndXServerMessageBlock) {
            andx.uid = uid;
            dstIndex += ((AndXServerMessageBlock) andx).writeAndXWireFormat(dst, dstIndex);
        } else {
            int andxStart = dstIndex;
            andx.wordCount = andx.writeParameterWordsWireFormat(dst, dstIndex);
            dstIndex += andx.wordCount + 1;
            andx.wordCount /= 2;
            dst[andxStart] = (byte) (andx.wordCount & 0xFF);
            andx.byteCount = andx.writeBytesWireFormat(dst, dstIndex + 2);
            dst[dstIndex++] = (byte) (andx.byteCount & 0xFF);
            dst[dstIndex++] = (byte) ((andx.byteCount >> 8) & 0xFF);
            dstIndex += andx.byteCount;
        }
        return dstIndex - start;
    }

    int readAndXWireFormat(InputStream in, byte[] buffer, int bufferIndex) throws IOException {
        int start = bufferIndex;
        if ((wordCount = in.read()) == -1) {
            throw new IOException("unexpected EOF reading smb wordCount");
        }
        buffer[bufferIndex++] = (byte) (wordCount & 0xFF);
        if (wordCount != 0) {
            if (in.read(buffer, bufferIndex, wordCount * 2) != (wordCount * 2)) {
                throw new IOException("unexpected EOF reading andx parameter words");
            }
            andxCommand = buffer[bufferIndex];
            bufferIndex += 2;
            andxOffset = readInt2(buffer, bufferIndex);
            bufferIndex += 2;
            if (andxOffset == 0) {
                andxCommand = (byte) 0xFF;
            }
            if (wordCount > 2) {
                bufferIndex += readParameterWordsWireFormat(buffer, bufferIndex);
            }
        }
        if (in.read(buffer, bufferIndex, 2) != 2) {
            throw new IOException("unexpected EOF reading smb byteCount");
        }
        byteCount = readInt2(buffer, bufferIndex);
        bufferIndex += 2;
        if (byteCount != 0) {
            int n;
            n = readBytesDirectWireFormat(in, byteCount, buffer, bufferIndex);
            if (n == 0) {
                if (in.read(buffer, bufferIndex, byteCount) != byteCount) {
                    throw new IOException("unexpected EOF reading andx bytes");
                }
                n = readBytesWireFormat(buffer, bufferIndex);
            }
            bufferIndex += byteCount;
        }
        if (errorCode != 0 || andxCommand == (byte) 0xFF) {
            andxCommand = (byte) 0xFF;
            andx = null;
        } else if (andx == null) {
            andxCommand = (byte) 0xFF;
            throw new IOException("no andx command supplied with response");
        } else {
            bufferIndex += in.read(buffer, bufferIndex, andxOffset - (bufferIndex - headerStart));
            andx.headerStart = headerStart;
            andx.command = andxCommand;
            andx.errorCode = errorCode;
            andx.flags = flags;
            andx.flags2 = flags2;
            andx.tid = tid;
            andx.pid = pid;
            andx.uid = uid;
            andx.mid = mid;
            andx.useUnicode = useUnicode;
            if (andx instanceof AndXServerMessageBlock) {
                bufferIndex += ((AndXServerMessageBlock) andx).readAndXWireFormat(in, buffer, andxOffset - headerStart);
            } else {
                if ((andx.wordCount = in.read()) == -1) {
                    throw new IOException("unexpected EOF reading smb wordCount");
                }
                buffer[bufferIndex++] = (byte) (andx.wordCount & 0xFF);
                if (andx.wordCount != 0) {
                    if (in.read(buffer, bufferIndex, andx.wordCount * 2) != (andx.wordCount * 2)) {
                        throw new IOException("unexpected EOF reading andx parameter words");
                    }
                    if (andx.wordCount > 2) {
                        bufferIndex += andx.readParameterWordsWireFormat(buffer, bufferIndex);
                    }
                }
                if (in.read(buffer, bufferIndex, 2) != 2) {
                    throw new IOException("unexpected EOF reading smb byteCount");
                }
                andx.byteCount = readInt2(buffer, bufferIndex);
                bufferIndex += 2;
                if (andx.byteCount != 0) {
                    if (in.read(buffer, bufferIndex, andx.byteCount) != andx.byteCount) {
                        throw new IOException("unexpected EOF reading andx bytes");
                    }
                    andx.readBytesWireFormat(buffer, bufferIndex);
                    bufferIndex += andx.byteCount;
                }
            }
            andx.received = true;
        }
        return bufferIndex - start;
    }

    public String toString() {
        return new String(super.toString() + ",andxCommand=0x" + Gadgets.toHexString(andxCommand, 2) + ",andxOffset=" + andxOffset);
    }
}
