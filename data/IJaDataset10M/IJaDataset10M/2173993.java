package org.neodatis.odb.core.layers.layer4.buffer;

/**The interface for buffered IO
 * 
 * @author osmadja
 *
 */
public interface IBufferedIO {

    void goToPosition(long position);

    long getLength();

    /**
	 * Checks if the new position is in the buffer, if not, flushes the buffer
	 * and rebuilds it to the correct position
	 * 
	 * @param newPosition
	 * @param readOrWrite
	 * @param size
	 *            Size if the data that must be stored
	 * @return The index of the buffer where that contains the position 
	 * 
	 */
    int manageBufferForNewPosition(long newPosition, int readOrWrite, int size);

    boolean isUsingbuffer();

    void setUseBuffer(boolean useBuffer);

    long getCurrentPosition();

    void setCurrentWritePosition(long currentPosition);

    void setCurrentReadPosition(long currentPosition);

    void writeByte(byte b);

    byte[] readBytesOld(int size);

    byte[] readBytes(int size);

    byte readByte();

    void writeBytes(byte[] bytes);

    void flush(int bufferIndex);

    void flushAll();

    long getIoDeviceLength();

    void setIoDeviceLength(long ioDeviceLength);

    void close();

    void clear();

    boolean delete();

    boolean isForTransaction();

    void enableAutomaticDelete(boolean yesOrNo);

    boolean automaticDeleteIsEnabled();
}
