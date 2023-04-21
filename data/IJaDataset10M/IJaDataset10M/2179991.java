package org.processmining.mining.dmcscanning.logutils;

/**
 * Bookmark Intended to provide a convenient means for temporarily saving an
 * iterator's current state, in order to reset it to that state later on.
 * 
 * @author christian
 * 
 *         Christian W. Guenther (christian@deckfour.com)
 * 
 */
public class Bookmark {

    protected int position = 0;

    /**
	 * constructor
	 */
    public Bookmark(int pos) {
        position = pos;
    }

    public int getPosition() {
        return position;
    }
}
