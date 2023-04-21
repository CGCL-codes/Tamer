package org.sourceforge.jemm.client.serialization;

import java.io.Serializable;

/**
 * A Decoder takes the raw object from the network layer and converts
 * it into an Object that is seen by the users.
 * 
 * @author Paul
 *
 */
public interface AttributeDecoder {

    Object decode(Serializable obj);
}
