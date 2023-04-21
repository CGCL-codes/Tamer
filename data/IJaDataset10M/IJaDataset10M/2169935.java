package se.slackers.locality.media;

public class Frame {

    /**
	 * Frame length in ms
	 */
    private long length;

    /**
	 * Frame size in bytes
	 */
    private int size;

    /**
	 * Data buffer.
	 */
    private byte[] data;

    public Frame() {
        length = 0;
        size = 0;
        data = null;
    }

    public Frame(int allocationSize) {
        length = 0;
        size = 0;
        data = new byte[allocationSize];
    }

    /**
	 * Makes a deep copy of the given frame.
	 * @param frame
	 */
    public Frame(Frame frame) {
        length = frame.length;
        size = frame.size;
        data = new byte[size];
        System.arraycopy(frame.data, 0, data, 0, size);
    }

    /**
	 * Get the length of this frame in milliseconds
	 * @return Length of frame in Ms
	 */
    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    /**
	 * Get the size of the frame in bytes.
	 * @return Size of frame in bytes
	 */
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
	 * Returns a reference to the data
	 * @return
	 */
    public byte[] getData() {
        return data;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(getLength());
        str.append(" ms [");
        str.append(getSize());
        str.append(" bytes]");
        return str.toString();
    }
}
