package org.sourceforge.jemm.client.serialization;

import java.io.Serializable;

/**
 * An AttributeEncoder/Decoder that makes no changes on encoding or decoding, and relies
 * on the Primitive to Object Primitive wrapper conversions.
 * 
 * ie int is converted to Integer on encode and on decode an Integer is still used.
 * 
 * @author Paul Keeble
 *
 */
public class PrimitiveSerializer implements AttributeEncoder, AttributeDecoder {

    @Override
    public Serializable encode(Object obj) {
        return (Serializable) obj;
    }

    @Override
    public Object decode(Serializable obj) {
        return obj;
    }
}
