package org.xcsoar;

import java.io.IOException;
import java.io.InputStream;
import android.util.Log;

/**
 * A wrapper for an InputStream which allows reading with a timeout.
 */
class InputThread extends Thread {

    private static final String TAG = "XCSoar";

    static final int BUFFER_SIZE = 256;

    InputStream is;

    int timeout = 0;

    byte[] buffer = new byte[BUFFER_SIZE];

    int head, tail;

    InputThread(InputStream _is) {
        is = _is;
        start();
    }

    synchronized void close() {
        InputStream is2 = is;
        if (is2 == null) return;
        is = null;
        try {
            is2.close();
        } catch (IOException e) {
        }
        notifyAll();
    }

    synchronized void flush() {
        head = tail;
    }

    void setTimeout(int _timeout) {
        timeout = _timeout;
    }

    private void shift() {
        System.arraycopy(buffer, head, buffer, 0, tail - head);
        tail -= head;
        head = 0;
    }

    @Override
    public void run() {
        try {
            while (true) {
                InputStream is2;
                if (tail >= BUFFER_SIZE) {
                    synchronized (this) {
                        while (is != null && head == 0) {
                            try {
                                wait();
                            } catch (InterruptedException e) {
                            }
                        }
                        is2 = is;
                        shift();
                    }
                } else is2 = is;
                if (is2 == null) break;
                int n = is2.read(buffer, tail, BUFFER_SIZE - tail);
                if (n < 0) break;
                synchronized (this) {
                    boolean wasEmpty = head >= tail;
                    tail += n;
                    if (wasEmpty) notifyAll();
                }
            }
        } catch (IOException e) {
            if (is != null) Log.e(TAG, "Failed to read from Bluetooth socket: " + e.getMessage());
        }
    }

    public synchronized int read() {
        if (head >= tail) {
            if (timeout <= 0) return -1;
            try {
                wait(timeout);
            } catch (InterruptedException e) {
                return -1;
            }
            if (head >= tail) return -1;
        }
        int ch = (int) buffer[head] & 0xff;
        ++head;
        if (tail >= BUFFER_SIZE) notifyAll();
        return ch;
    }
}
