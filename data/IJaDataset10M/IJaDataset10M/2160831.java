package org.jscsi.scsi.protocol.sense.additional;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ActualRetryCount implements SenseKeySpecificField {

    private int actualRetryCount;

    public ActualRetryCount() {
        this.actualRetryCount = -1;
    }

    public ActualRetryCount(int actualRetryCount) {
        this.actualRetryCount = actualRetryCount;
    }

    public int getActualRetryCount() {
        return actualRetryCount;
    }

    public void decode(byte[] header, ByteBuffer buffer) throws IOException {
        decode(buffer);
    }

    public byte[] encode() {
        byte[] encodedData = new byte[3];
        encodedData[0] = (byte) 0x80;
        encodedData[1] = (byte) ((this.actualRetryCount >>> 8) & 0xFF);
        encodedData[2] = (byte) (this.actualRetryCount & 0xFF);
        return encodedData;
    }

    @SuppressWarnings("unchecked")
    public ActualRetryCount decode(ByteBuffer buffer) throws IOException {
        byte[] encodedData = new byte[3];
        buffer.get(encodedData);
        this.actualRetryCount = (encodedData[2] & 0xFF);
        this.actualRetryCount |= ((encodedData[1] & 0xFF) << 8);
        return this;
    }
}
