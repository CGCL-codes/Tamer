package com.frinika.sequencer.gui.selection;

import com.frinika.sequencer.model.Selectable;

public interface SelectionListener<T extends Selectable> {

    /**
	 * Notify observers that the slection has changed.
	 * @param src
	 */
    public void selectionChanged(SelectionContainer<? extends T> src);
}
