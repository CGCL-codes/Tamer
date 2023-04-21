package org.yactu.tools.streams;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import org.yactu.tools.streams.StreamsThreadedBridge;
import junit.framework.TestCase;

/**
 * @author ploix
 *  
 */
public class StreamsThreadedBridgeTest extends TestCase {

    /**
     * Checks that when we enter a byte [], it is read with the same value at
     * the other end.
     *  
     */
    public void testIdemByteArrayInputOutput() {
        byte[] _b = new byte[Byte.MAX_VALUE + 1];
        byte[] _b2 = new byte[Byte.MAX_VALUE + 1];
        int _length;
        int _pos = 0;
        for (int i = 0; i < Byte.MAX_VALUE + 1; i++) _b[i] = (byte) i;
        try {
            PipedInputStream _BridgeIS = new PipedInputStream();
            PipedOutputStream _OS = new PipedOutputStream(_BridgeIS);
            PipedOutputStream _BridgeOS = new PipedOutputStream();
            PipedInputStream _IS = new PipedInputStream(_BridgeOS);
            new StreamsThreadedBridge(_BridgeIS, _BridgeOS).start();
            _OS.write(_b);
            while (_pos != _b2.length) {
                _length = _IS.read(_b2, _pos, 1);
                _pos += _length;
            }
            for (int i = 0; i < _b2.length; i++) {
                assertEquals(_b[i], _b2[i]);
            }
        } catch (IOException ioe) {
            assertTrue(false);
        }
    }
}
