package de.huxhorn.lilith.sender;

public interface SendBytesService {

    /**
	 * A byte-sender is expected to send the given byte array if possible.
	 * There is no guarantee that the bytes are really sent out, e.g. in case of an error.
	 * There is no feedback of any kind concerning success or failure!
	 *
	 * @param bytes the bytes to send.
	 */
    void sendBytes(byte[] bytes);

    void startUp();

    void shutDown();
}
