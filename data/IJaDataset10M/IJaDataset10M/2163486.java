package de.tfh.pdvl.hp.protocol;

import de.tfh.pdvl.hp.ParentException;

/**
 * @author timo
 *
 */
public interface Message {

    /**
     * @return Return the message type String (set, error, info, query)
     */
    String getMessageType();

    /**
	 * Accept method used by visitor-design-pattern.
	 * @param v 
	 */
    void accept(Visitor v) throws ParentException;
}
