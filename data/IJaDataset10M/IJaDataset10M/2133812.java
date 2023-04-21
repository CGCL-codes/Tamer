package jcifs.smb;

class SmbComQueryInformation extends ServerMessageBlock {

    SmbComQueryInformation(String filename) {
        path = filename;
        command = SMB_COM_QUERY_INFORMATION;
    }

    int writeParameterWordsWireFormat(byte[] dst, int dstIndex) {
        return 0;
    }

    int writeBytesWireFormat(byte[] dst, int dstIndex) {
        int start = dstIndex;
        dst[dstIndex++] = (byte) 0x04;
        dstIndex += writeString(path, dst, dstIndex);
        return dstIndex - start;
    }

    int readParameterWordsWireFormat(byte[] buffer, int bufferIndex) {
        return 0;
    }

    int readBytesWireFormat(byte[] buffer, int bufferIndex) {
        return 0;
    }

    public String toString() {
        return new String("SmbComQueryInformation[" + super.toString() + ",filename=" + path + "]");
    }
}
