package adamb.util;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 A fixed size circular queue of bytes.
 */
public class CircularByteQueue {

    private byte[] array;

    private int head;

    private int size;

    /**
	 @isFull true means the given buffer is full of valid data, in which case the
	 queue will initially be full.  Otherwise the queue will be empty.
	 */
    public CircularByteQueue(byte[] buffer, boolean isFull) {
        assert buffer.length > 0;
        array = buffer;
        head = 0;
        if (isFull) size = buffer.length; else size = 0;
    }

    public boolean equals(byte[] bytes) {
        if (bytes.length == size) {
            if (size > 0) {
                int n = array.length - head;
                int j = 0;
                for (int i = 0; i < n; i++) {
                    if (array[head + i] == bytes[j]) j++; else return false;
                }
                n = size - n;
                for (int i = 0; i < n; i++) {
                    if (array[i] == bytes[j]) j++; else return false;
                }
            }
            return true;
        } else return false;
    }

    public void put(byte b) {
        if (head + size < array.length) {
            array[head + size] = b;
            size++;
        } else {
            int tail = size - (array.length - head);
            array[tail] = b;
            if (tail == head) {
                head++;
                if (head == array.length) head = 0;
            } else size++;
        }
    }

    public static class Tester {

        @Test
        public void test() {
            {
                byte[] buf = new byte[] { 1 };
                CircularByteQueue q = new CircularByteQueue(buf, true);
                assertTrue(q.equals(new byte[] { 1 }));
                q.put((byte) 2);
                assertTrue(q.equals(new byte[] { 2 }));
            }
            {
                byte[] buf = new byte[] { 1 };
                CircularByteQueue q = new CircularByteQueue(buf, false);
                assertTrue(q.equals(new byte[0]));
                q.put((byte) 2);
                assertTrue(q.equals(new byte[] { 2 }));
                q.put((byte) 3);
                assertTrue(q.equals(new byte[] { 3 }));
            }
            {
                byte[] buf = new byte[] { 1, 2 };
                CircularByteQueue q = new CircularByteQueue(buf, true);
                assertTrue(q.equals(new byte[] { 1, 2 }));
                q.put((byte) 0);
                assertTrue(q.equals(new byte[] { 2, 0 }));
                q.put((byte) 1);
                assertTrue(q.equals(new byte[] { 0, 1 }));
                q.put((byte) 2);
                assertTrue(q.equals(new byte[] { 1, 2 }));
                q.put((byte) 3);
                assertTrue(q.equals(new byte[] { 2, 3 }));
            }
            {
                byte[] buf = new byte[5];
                CircularByteQueue q = new CircularByteQueue(buf, false);
                for (int i = 0; i < buf.length; i++) q.put((byte) i);
                assertTrue(q.equals(new byte[] { 0, 1, 2, 3, 4 }));
                for (int i = 5; i < buf.length + 5; i++) q.put((byte) i);
                assertTrue(q.equals(new byte[] { 5, 6, 7, 8, 9 }));
                for (int i = 10; i < buf.length + 10; i++) q.put((byte) i);
                assertTrue(q.equals(new byte[] { 10, 11, 12, 13, 14 }));
            }
            {
                CircularByteQueue q = new CircularByteQueue("Hell".getBytes(), true);
                assertTrue(q.equals("Hell".getBytes()));
                q.put((byte) 'o');
                assertTrue(q.equals("ello".getBytes()));
            }
        }
    }
}
