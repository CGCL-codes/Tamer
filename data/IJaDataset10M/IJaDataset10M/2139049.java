package org.yactu.tools.streams;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author ploix
 *
 * Encapsulate the behavior of a reader that reads in a buffer (char[]) and then
 * generates messages after each read. Should be launched inside a Thread
 * otherwise useless !
 */
public class InputStreamEventSender implements Runnable {

    private static final String __rcs_id__ = "$Id: InputStreamEventSender.java 1945 2006-10-10 07:03:17Z ploix $";

    /**
     * Classical design pattern : delegate to handle the event listeners.
     */
    private CharBufferReadSupport __support = new CharBufferReadSupport();

    /**
     * Sould we stop after the next read has been achived ?
     */
    private boolean __willStop = false;

    /**
     * We are stopped, aren't we ?
     */
    private boolean __stopped = true;

    /**
     * Here is the input reader.
     */
    private InputStreamReader __ISReader = null;

    /**
     * Build a new object on top of this reader.
     *
     * @param reader :
     *            reader from which the data should be read.
     */
    public InputStreamEventSender(InputStreamReader reader) {
        __ISReader = reader;
    }

    /**
     * Adds a listener to the list of listening objects.
     *
     * @param listener :
     *            listener to be added.
     */
    public void addCharBufferReadListener(CharBufferReadListener listener) {
        __support.addEventListener(listener);
    }

    /**
     * removes a listener from the list of interested listeners.
     *
     * @param listener
     */
    public void removeCharBufferReadListener(CharBufferReadListener listener) {
        __support.removeEventListener(listener);
    }

    /**
     * ask the StreamsThreadedBridge to stop after next read object
     *
     */
    public void pleaseStop() {
        __willStop = true;
    }

    /**
     * tells wether it will stop after the next read.
     *
     * @return : true if and only if it is still running but will stop after
     *         next read.
     */
    public boolean isStopping() {
        return (!__stopped && __willStop);
    }

    /**
     * tells whether it is stopped or not.
     *
     * @return : stopped = true, otherwise false. false if is stopping but not
     *         stopped now (still reading)
     */
    public boolean isStopped() {
        return __stopped;
    }

    /**
     * Heart of the class : almost infinite loop that breaks once told to to so
     * after the next read. Fires an event once data is read.
     */
    public void run() {
        __stopped = false;
        int _size = 1024;
        char[] _buf = new char[_size];
        int _pos = 0;
        int i;
        try {
            do {
                i = __ISReader.read(_buf, _pos, _size - _pos);
                if (i != -1) {
                    __support.fireBufferRead(new CharBufferReadEvent(this, _buf, _pos, i));
                    _pos += i;
                    if (_pos > _size * 2 / 3) _pos = 0;
                }
            } while ((i != -1) && (!__willStop));
        } catch (IOException ioe) {
        } finally {
            __support.fireEndOfStream();
            __stopped = true;
        }
    }
}
