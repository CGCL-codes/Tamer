package org.exolab.jms.net;

/**
 * Service which echoes input parameters, for testing purposes
 *
 * @version     $Revision: 1.1 $ $Date: 2004/11/26 01:51:07 $
 * @author      <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 */
public class EchoServiceImpl implements EchoService {

    /**
     * Returns the passed value
     *
     * @param value the value
     * @return the value
     */
    public boolean echoBoolean(boolean value) {
        return value;
    }

    /**
     * Returns the passed value
     *
     * @param value the value
     * @return the value
     */
    public byte echoByte(byte value) {
        return value;
    }

    /**
     * Returns the passed value
     *
     * @param value the value
     * @return the value
     */
    public char echoChar(char value) {
        return value;
    }

    /**
     * Returns the passed value
     *
     * @param value the value
     * @return the value
     */
    public short echoShort(short value) {
        return value;
    }

    /**
     * Returns the passed value
     *
     * @param value the value
     * @return the value
     */
    public int echoInt(int value) {
        return value;
    }

    /**
     * Returns the passed value
     *
     * @param value the value
     * @return the value
     */
    public long echoLong(long value) {
        return value;
    }

    /**
     * Returns the passed value
     *
     * @param value the value
     * @return the value
     */
    public float echoFloat(float value) {
        return value;
    }

    /**
     * Returns the passed value
     *
     * @param value the value
     * @return the value
     */
    public double echoDouble(double value) {
        return value;
    }

    /**
     * Returns the passed value
     *
     * @param value the value
     * @return the value
     */
    public Object echoObject(Object value) {
        return value;
    }
}
