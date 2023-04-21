package net.sf.jml;

import net.sf.jml.protocol.MsnMessage;

/**
 * MsnMessage iterator.
 * 
 * @author Roger Chen
 */
public interface MsnMessageIterator {

    /**
     * Has previous MsnMessage.
     * 
     * @return
     * 		has previous MsnMessage
     */
    boolean hasPrevious();

    /**
     * Get the previous MsnMessage.
     * 
     * @return
     * 		previous MsnMessage
     */
    MsnMessage previous();
}
