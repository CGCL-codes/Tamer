package com.knowgate.jcifs.smb;

import java.util.Enumeration;
import com.knowgate.debug.*;

abstract class SmbComTransactionResponse extends ServerMessageBlock implements Enumeration {

    private static final int SETUP_OFFSET = 61;

    private static final int DISCONNECT_TID = 0x01;

    private static final int ONE_WAY_TRANSACTION = 0x02;

    private int totalParameterCount;

    private int totalDataCount;

    private int parameterCount;

    private int parameterOffset;

    private int parameterDisplacement;

    private int dataOffset;

    private int dataDisplacement;

    private int setupCount;

    private int pad;

    private int pad1;

    private boolean parametersDone, dataDone;

    private int bufParameterStart;

    private int bufDataStart;

    int dataCount;

    byte subCommand;

    boolean hasMore = true;

    boolean isPrimary = true;

    byte[] txn_buf;

    int status;

    int numEntries;

    FileEntry[] results;

    SmbComTransactionResponse() {
        txn_buf = null;
    }

    public void reset() {
        bufDataStart = 0;
        isPrimary = hasMore = true;
        parametersDone = dataDone = false;
    }

    public boolean hasMoreElements() {
        return hasMore;
    }

    public Object nextElement() {
        if (isPrimary) {
            isPrimary = false;
        }
        return this;
    }

    int writeParameterWordsWireFormat(byte[] dst, int dstIndex) {
        return 0;
    }

    int writeBytesWireFormat(byte[] dst, int dstIndex) {
        return 0;
    }

    int readParameterWordsWireFormat(byte[] buffer, int bufferIndex) {
        int start = bufferIndex;
        totalParameterCount = readInt2(buffer, bufferIndex);
        if (bufDataStart == 0) {
            bufDataStart = totalParameterCount;
        }
        bufferIndex += 2;
        totalDataCount = readInt2(buffer, bufferIndex);
        bufferIndex += 4;
        parameterCount = readInt2(buffer, bufferIndex);
        bufferIndex += 2;
        parameterOffset = readInt2(buffer, bufferIndex);
        bufferIndex += 2;
        parameterDisplacement = readInt2(buffer, bufferIndex);
        bufferIndex += 2;
        dataCount = readInt2(buffer, bufferIndex);
        bufferIndex += 2;
        dataOffset = readInt2(buffer, bufferIndex);
        bufferIndex += 2;
        dataDisplacement = readInt2(buffer, bufferIndex);
        bufferIndex += 2;
        setupCount = buffer[bufferIndex] & 0xFF;
        bufferIndex += 2;
        if (setupCount != 0) {
            if (DebugFile.trace) DebugFile.writeln("setupCount is not zero: " + setupCount);
        }
        return bufferIndex - start;
    }

    int readBytesWireFormat(byte[] buffer, int bufferIndex) {
        pad = pad1 = 0;
        int n;
        if (parameterCount > 0) {
            bufferIndex += pad = parameterOffset - (bufferIndex - headerStart);
            System.arraycopy(buffer, bufferIndex, txn_buf, bufParameterStart + parameterDisplacement, parameterCount);
            bufferIndex += parameterCount;
        }
        if (dataCount > 0) {
            bufferIndex += pad1 = dataOffset - (bufferIndex - headerStart);
            System.arraycopy(buffer, bufferIndex, txn_buf, bufDataStart + dataDisplacement, dataCount);
            bufferIndex += dataCount;
        }
        if (!parametersDone && (parameterDisplacement + parameterCount) == totalParameterCount) {
            parametersDone = true;
        }
        if (!dataDone && (dataDisplacement + dataCount) == totalDataCount) {
            dataDone = true;
        }
        if (parametersDone && dataDone) {
            hasMore = false;
            readParametersWireFormat(txn_buf, bufParameterStart, totalParameterCount);
            readDataWireFormat(txn_buf, bufDataStart, totalDataCount);
        }
        return pad + parameterCount + pad1 + dataCount;
    }

    abstract int writeSetupWireFormat(byte[] dst, int dstIndex);

    abstract int writeParametersWireFormat(byte[] dst, int dstIndex);

    abstract int writeDataWireFormat(byte[] dst, int dstIndex);

    abstract int readSetupWireFormat(byte[] buffer, int bufferIndex, int len);

    abstract int readParametersWireFormat(byte[] buffer, int bufferIndex, int len);

    abstract int readDataWireFormat(byte[] buffer, int bufferIndex, int len);

    public String toString() {
        return new String(super.toString() + ",totalParameterCount=" + totalParameterCount + ",totalDataCount=" + totalDataCount + ",parameterCount=" + parameterCount + ",parameterOffset=" + parameterOffset + ",parameterDisplacement=" + parameterDisplacement + ",dataCount=" + dataCount + ",dataOffset=" + dataOffset + ",dataDisplacement=" + dataDisplacement + ",setupCount=" + setupCount + ",pad=" + pad + ",pad1=" + pad1);
    }
}
