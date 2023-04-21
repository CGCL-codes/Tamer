package org.ugr.bluerose;

import org.ugr.bluerose.messages.MessageHeader;

/**
 * Result for the runMethod method of the servant of an object
 *
 * @author Carlos Rodriguez Dominguez
 * @date 03-03-2010
 */
public class MethodResult {

    public byte status;

    public java.util.Vector<Byte> result;

    public MethodResult() {
        status = MessageHeader.SUCCESS_STATUS;
        result = new java.util.Vector<Byte>();
    }
}
