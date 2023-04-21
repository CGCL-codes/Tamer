package org.uguess.birt.report.engine.spreadsheet.model;

import org.uguess.birt.report.engine.layout.wrapper.Style;

/**
 * This interface represents an abstract block object.
 */
public interface Block {

    /**
	 * Returns the style object associated with current object.
	 * 
	 * @return
	 */
    Style getStyle();

    /**
	 * Sets the style object of current object.
	 * 
	 * @param style
	 */
    void setStyle(Style style);

    /**
	 * Returns if current object is empty.
	 * 
	 * @return
	 */
    boolean isEmpty();
}
