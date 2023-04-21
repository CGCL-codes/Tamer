package com.ourbunny.jshout;

public class Segment {

    private long time;

    private byte[] data;

    /**
	 * Create a new Segment object
	 * @param d byte array of media data
	 * @param t the number of milliseconds the data represents
	 */
    public Segment(byte[] d, long t) {
        this.time = t;
        this.data = d;
    }

    /**
	 * Get Segment data
	 * @return data as an array of bytes
	 */
    public byte[] getData() {
        return data;
    }

    /**
	 * Set Segment data
	 * @param data byte array of media data
	 */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
	 * Get Segment time
	 * @return milliseconds to play data
	 */
    public long getTime() {
        return time;
    }

    /**
	 * Set Segment time
	 * @param time milliseconds to play data
	 */
    public void setTime(long time) {
        this.time = time;
    }

    /**
	 * Get Data size
	 * @return the number of bytes in the Segment
	 */
    public int getSize() {
        return this.data.length;
    }
}
