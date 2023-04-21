package org.xsocket.group;

import java.io.IOException;
import java.io.Serializable;
import org.xsocket.IDataSink;
import org.xsocket.IDataSource;

/**   
 *  
 * @author grro@xsocket.org
 */
final class MulticastMessageHeader implements Serializable {

    private static final long serialVersionUID = 8476515469885702677L;

    private static final Byte MAGIC_BYTE = 102;

    private String sourceAddress = null;

    private byte cmd = 0;

    public MulticastMessageHeader(String sourceAddress, byte cmd) {
        this.sourceAddress = sourceAddress;
        this.cmd = cmd;
    }

    private MulticastMessageHeader(IDataSource dataSource) throws IOException {
        byte magicByte = dataSource.readByte();
        if (magicByte != MAGIC_BYTE) {
            throw new IOException("invalid protocol exception. Invalid magic byte: " + magicByte + "(expected: " + MAGIC_BYTE + ")");
        }
        cmd = dataSource.readByte();
        int addressLenth = dataSource.readInt();
        sourceAddress = new String(dataSource.readBytesByLength(addressLenth), "UTF-8");
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public byte getCommand() {
        return cmd;
    }

    public static MulticastMessageHeader readFrom(IDataSource dataSource) throws IOException {
        return new MulticastMessageHeader(dataSource);
    }

    public int writeTo(IDataSink dataSink) throws IOException {
        int written = 0;
        written += dataSink.write(MAGIC_BYTE);
        written += dataSink.write(cmd);
        byte[] serializedAddress = sourceAddress.getBytes("UTF-8");
        written += dataSink.write(serializedAddress.length);
        written += dataSink.write(serializedAddress);
        return written;
    }

    @Override
    public String toString() {
        return ("cmd=" + cmd + ", sourceAddress=" + sourceAddress);
    }
}
