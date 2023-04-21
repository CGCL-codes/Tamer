package net.sf.doolin.gui.field.support;

import net.sf.doolin.gui.field.FieldCheck;

/**
 * Support interface for a <code>{@link FieldCheck}</code> field.
 * 
 * @author Damien Coraboeuf
 * @version $Id: CheckSupport.java,v 1.1 2007/08/10 16:54:46 guinnessman Exp $
 */
public interface CheckSupport extends FieldSupport<FieldCheck> {

    /**
	 * Sets the selected state
	 * 
	 * @param selected
	 *            Selected state
	 */
    void setSelected(boolean selected);

    /**
	 * Gets the selected state
	 * 
	 * @return Selected state
	 */
    boolean isSelected();
}
