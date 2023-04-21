package org.zkoss.idom;

import org.zkoss.idom.impl.*;

/**
 * The iDOM Text.
 *
 * @author tomyeh
 * @see CData
 */
public class Text extends AbstractTextual implements org.w3c.dom.Text {

    /** Constructor.
	 */
    public Text(String text) {
        super(text);
    }

    /** Constructor.
	 */
    public Text() {
    }

    /**
	 * Always returns true to denote it allows to be coalesced
	 * with its siblings with the same type (class).
	 */
    public final boolean isCoalesceable() {
        return true;
    }

    public final String getName() {
        return "#text";
    }

    public final short getNodeType() {
        return TEXT_NODE;
    }
}
