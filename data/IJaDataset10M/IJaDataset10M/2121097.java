package org.fenggui.binding.render.text.advanced;

/**
 * A Interface which defines methods for a selection
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public interface IContentSelection {

    /**
	 * Returns true if this object has a selected part, false otherwise.
	 * 
	 * @return true if a selected part is within this object, false otherwise.
	 */
    public abstract boolean hasSelection();

    /**
	 * Returns the selection start within this object.
	 * @return start position in atoms.
	 */
    public abstract int getSelectionStart();

    /**
	 * Returns the selection end within this object.
	 * 
	 * @return end position in atoms.
	 */
    public abstract int getSelectionEnd();

    /**
	 * Removes all selections from this object. No content is removed.
	 */
    public abstract void clearSelection();

    /**
	 * Removes everything that is selected including the content of this object.
	 */
    public abstract void removeSelection(IContentFactory factory, boolean pwdField);

    /**
	 * Selected the parts between the to atom positions.
	 * 
	 * @param start start position in atoms
	 * @param end end position in atoms
	 */
    public abstract void setSelection(int start, int end);
}
