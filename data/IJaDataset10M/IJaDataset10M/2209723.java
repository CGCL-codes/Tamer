package org.eclipse.core.databinding.observable.value;

import org.eclipse.core.databinding.observable.Diffs;

/**
 * @since 1.0
 * 
 */
public abstract class ValueDiff {

    /**
	 * Creates a value diff.
	 */
    public ValueDiff() {
    }

    /**
	 * @return the old value
	 */
    public abstract Object getOldValue();

    /**
	 * @return the new value
	 */
    public abstract Object getNewValue();

    public boolean equals(Object obj) {
        if (obj instanceof ValueDiff) {
            ValueDiff val = (ValueDiff) obj;
            return Diffs.equals(val.getNewValue(), getNewValue()) && Diffs.equals(val.getOldValue(), getOldValue());
        }
        return false;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        Object nv = getNewValue();
        Object ov = getOldValue();
        result = prime * result + ((nv == null) ? 0 : nv.hashCode());
        result = prime * result + ((ov == null) ? 0 : ov.hashCode());
        return result;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()).append("{oldValue [").append(getOldValue() != null ? getOldValue().toString() : "null").append("], newValue [").append(getNewValue() != null ? getNewValue().toString() : "null").append("]}");
        return buffer.toString();
    }
}
