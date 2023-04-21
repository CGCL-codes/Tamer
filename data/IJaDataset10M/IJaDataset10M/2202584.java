package stegoFile;

import common.IWriteStegoFile;

public class WriteStegoFileImpl implements IWriteStegoFile {

    private byte[] messageBytes;

    private int byteIndex;

    private int bitIndex;

    private int maxByteIndex;

    public boolean knownSizeStegoText;

    public WriteStegoFileImpl(int messageSize) {
        byteIndex = 0;
        bitIndex = 7;
        maxByteIndex = messageSize;
        messageBytes = new byte[maxByteIndex];
        knownSizeStegoText = true;
    }

    public WriteStegoFileImpl() {
        byteIndex = 0;
        bitIndex = 7;
        maxByteIndex = 20;
        messageBytes = new byte[maxByteIndex];
        knownSizeStegoText = false;
    }

    public void setNextBits(byte writeByte, int nrOfBits) throws IndexOutOfBoundsException {
        if (hasMoreBits() == false) {
            throw new IndexOutOfBoundsException();
        }
        if (nrOfBits <= bitIndex + 1) {
            writeByte = (byte) (writeByte << (bitIndex - nrOfBits + 1));
            byte b = messageBytes[byteIndex];
            b = (byte) (b | writeByte);
            messageBytes[byteIndex] = b;
            bitIndex = bitIndex - nrOfBits;
            if (bitIndex < 0) {
                if (byteIndex == (maxByteIndex - 1) && knownSizeStegoText == false) {
                    increaseMessageSize();
                }
                byteIndex = byteIndex + 1;
                bitIndex = 7;
            }
        }
    }

    private void increaseMessageSize() {
        maxByteIndex = maxByteIndex + 20;
        byte[] aux = messageBytes;
        messageBytes = new byte[maxByteIndex];
        System.arraycopy(aux, 0, messageBytes, 0, aux.length);
    }

    public byte[] getMessage() {
        return messageBytes;
    }

    public boolean hasMoreBits() {
        if (knownSizeStegoText) {
            if (byteIndex == maxByteIndex) {
                return false;
            }
        }
        return true;
    }

    public int getSize() {
        return maxByteIndex * 8;
    }
}
