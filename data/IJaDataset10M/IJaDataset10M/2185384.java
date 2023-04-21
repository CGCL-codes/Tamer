package com.aptana.ide.editors.views.outline;

/**
 * @author Paul Colton
 */
public class OutlineViewEvent {

    private OutlineItem _item;

    /**
	 * getItem
	 *
	 * @return OutlineItem
	 */
    public OutlineItem getItem() {
        return this._item;
    }

    /**
	 * OutlineViewEvent
	 *
	 * @param item
	 */
    public OutlineViewEvent(OutlineItem item) {
        this._item = item;
    }
}
