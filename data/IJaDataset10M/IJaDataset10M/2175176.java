package com.volantis.mcs.protocols.forms;

import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.XFFormFieldAttributes;

/**
 * This interface provides a general way for easily supporting different
 * types of fields. It uses the Visitor design pattern.
 *
 * @mock.generate 
 */
public interface FieldType {

    /**
   * Process the field.
   * @param protocol The protocol to use.
   * @param attributes The field attributes.
   */
    public void doField(VolantisProtocol protocol, XFFormFieldAttributes attributes) throws ProtocolException;

    /**
   * Get the handler for the field.
   * @param protocol The protocol to use.
   */
    public FieldHandler getFieldHandler(VolantisProtocol protocol);
}
