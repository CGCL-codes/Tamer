package org.yajul.fix.util;

/**
 * An array of bytes as a char sequence.
* <br>
* User: josh
* Date: Jul 22, 2009
* Time: 5:01:47 PM
*/
public class ByteCharSequence implements CharSequence {

    private int start;

    private int end;

    private byte[] bytes;

    public ByteCharSequence(byte[] bytes) {
        this(0, bytes.length, bytes);
    }

    public ByteCharSequence(int start, int end, byte[] bytes) {
        this.start = start;
        this.end = end;
        this.bytes = bytes;
    }

    public int length() {
        return (end - start);
    }

    public char charAt(int index) {
        int i = start + index;
        if (i >= end) throw new IndexOutOfBoundsException("index " + i + " >= " + end);
        return (char) bytes[start + index];
    }

    public CharSequence subSequence(int start, int end) {
        return new ByteCharSequence(this.start + start, this.start + end, bytes);
    }
}
