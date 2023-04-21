package org.eclipse.core.databinding.observable.list;

/**
 * A single addition of an element to a list or removal of an element from a list.
 *  
 * @since 1.0
 */
public abstract class ListDiffEntry {

    /**
	 * @return the 0-based position of the addition or removal
	 */
    public abstract int getPosition();

    /**
	 * @return true if this represents an addition, false if this represents a removal
	 */
    public abstract boolean isAddition();

    /**
	 * @return the element that was added or removed
	 */
    public abstract Object getElement();

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getClass().getName()).append("{position [").append(getPosition()).append("], isAddition [").append(isAddition()).append("], element [").append(getElement() != null ? getElement().toString() : "null").append("]}");
        return buffer.toString();
    }
}
