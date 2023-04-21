package net.sourceforge.jtds.test;

import net.sourceforge.jtds.jdbc.Driver;
import net.sourceforge.jtds.jdbc.SharedNamedPipe;
import net.sourceforge.jtds.jdbc.Support;
import net.sourceforge.jtds.jdbc.TdsCore;

/**
 * Unit tests for the {@link SharedNamedPipe} class.
 *
 * @author David D. Kilzer
 * @version $Id: NamedPipeUnitTest.java,v 1.1.1.1 2007/01/24 20:51:56 mwillett Exp $
 */
public class NamedPipeUnitTest extends UnitTestBase {

    /**
     * Constructor.
     *
     * @param name The name of the test.
     */
    public NamedPipeUnitTest(final String name) {
        super(name);
    }

    /**
     * Test that {@link Support#calculateNamedPipeBufferSize(int, int)}
     * sets the buffer size appropriately for TDS 4.2 when the packet
     * size is set to 0.
     */
    public void testCalculateBufferSize_TDS42() {
        int length = invoke_calculateBufferSize(Driver.TDS42, 0);
        assertEquals(TdsCore.MIN_PKT_SIZE, length);
    }

    /**
     * Test that {@link Support#calculateNamedPipeBufferSize(int, int)}
     * sets the buffer size appropriately for TDS 5.0 when the packet
     * size is set to 0.
     */
    public void testCalculateBufferSize_TDS50() {
        int length = invoke_calculateBufferSize(Driver.TDS50, 0);
        assertEquals(TdsCore.MIN_PKT_SIZE, length);
    }

    /**
     * Test that {@link Support#calculateNamedPipeBufferSize(int, int)}
     * sets the buffer size appropriately for TDS 7.0 when the packet
     * size is set to 0.
     */
    public void testCalculateBufferSize_TDS70() {
        int length = invoke_calculateBufferSize(Driver.TDS70, 0);
        assertEquals(TdsCore.DEFAULT_MIN_PKT_SIZE_TDS70, length);
    }

    /**
     * Test that {@link Support#calculateNamedPipeBufferSize(int, int)}
     * sets the buffer size appropriately for TDS 8.0 when the packet
     * size is set to 0.
     */
    public void testCalculateBufferSize_TDS80() {
        int length = invoke_calculateBufferSize(Driver.TDS80, 0);
        assertEquals(TdsCore.DEFAULT_MIN_PKT_SIZE_TDS70, length);
    }

    /**
     * Helper method to invoke {@link Support#calculateNamedPipeBufferSize(int, int)}
     * using reflection.
     *
     * @param tdsVersion The TDS version as an <code>int</code>.
     * @param packetSize The packet size as an <code>int</code>.
     * @return Result of calling {@link Support#calculateNamedPipeBufferSize(int, int)}.
     */
    private int invoke_calculateBufferSize(int tdsVersion, int packetSize) {
        Class[] classes = new Class[] { int.class, int.class };
        Object[] objects = new Object[] { new Integer(tdsVersion), new Integer(packetSize) };
        return ((Integer) invokeStaticMethod(Support.class, "calculateNamedPipeBufferSize", classes, objects)).intValue();
    }
}
