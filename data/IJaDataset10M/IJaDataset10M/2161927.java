package com.frinika.sequencer.model;

public interface Selectable {

    /**
	 *  set selected flag (for GUI use only)
	 * @param b 
	 */
    void setSelected(boolean b);

    /**
	 * Complete copy of object.
	 * 
	 * @param parent owner of the new object;
	 * @return
	 */
    Selectable deepCopy(Selectable parent);

    /**
	 * Move object and all children by tick
	 * @param tick
	 */
    void deepMove(long tick);

    /**
	 * 
	 * Remove from model making sure the history is informed
	 */
    void removeFromModel();

    /**
	 * 
	 * Add to the model making sure the history is informed
	 */
    void addToModel();

    /**
	 * return the left tick mark for move operations
	 * without quantize a move to destTick should  move item by destTick - leftTickForMove();
	 * 
	 * @return
	 */
    long leftTickForMove();

    long rightTickForMove();
}
