package org.apache.batik.gvt.text;

import org.apache.batik.gvt.TextNode;

/**
 * Marker interface, mostly, that encapsulates information about a
 * selection gesture.
 *
 * @author <a href="mailto:bill.haneman@ireland.sun.com">Bill Haneman</a>
 * @version $Id: Mark.java,v 1.1 2005/11/21 09:51:34 dev Exp $ 
 */
public interface Mark {

    public TextNode getTextNode();

    /**
     * Returns the index of the character that has been hit.
     *
     * @return The character index.
     */
    public int getCharIndex();
}
