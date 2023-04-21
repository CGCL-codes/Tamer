package org.apache.lucene.analysis.tokenattributes;

import org.apache.lucene.index.Payload;
import org.apache.lucene.util.Attribute;

/**
 * The payload of a Token. See also {@link Payload}.
 */
public interface PayloadAttribute extends Attribute {

    /**
   * Returns this Token's payload.
   */
    public Payload getPayload();

    /** 
   * Sets this Token's payload.
   */
    public void setPayload(Payload payload);
}
