package org.ugr.bluerose.messages;

/**
 * Encapsulation of the body of a message
 */
public class Encapsulation {

    public Integer size;

    /**< Size of the encapsulation, including this header */
    public byte major;

    /**< Major allowed encoding version as defined by ICE */
    public byte minor;

    /**< Minor allowed encoding version as defined by ICE  */
    public java.util.Vector<Byte> byteCollection;

    /**< Message */
    public Encapsulation() {
        size = new Integer(6);
        major = 0x01;
        minor = 0x00;
        byteCollection = new java.util.Vector<Byte>();
    }
}
