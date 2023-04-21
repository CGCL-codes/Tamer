package at.jku.xlwrap.map.expr.val;

import at.jku.xlwrap.common.XLWrapException;
import at.jku.xlwrap.exec.ExecutionContext;
import at.jku.xlwrap.map.expr.XLExpr0;

/**
 * @author dorgon
 *
 */
public abstract class XLExprValue<T> extends XLExpr0 {

    /** the primitive Java value */
    protected T value;

    /** casted flag, set if a type cast function was applied by the user */
    protected boolean casted = false;

    public XLExprValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public XLExprValue<?> eval(ExecutionContext context) throws XLWrapException {
        return this;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @SuppressWarnings("unchecked")
    public int compareTo(XLExprValue<?> other) {
        if (value instanceof Comparable && other.value instanceof Comparable) {
            if (value instanceof Long && other.value instanceof Double) return ((Double) ((Long) value).doubleValue()).compareTo((Double) other.value); else if (value instanceof Double && other.value instanceof Long) return ((Double) value).compareTo((Double) ((Long) other.value).doubleValue()); else return ((Comparable) value).compareTo(other.value);
        } else {
            if (value.equals(other.value)) return 0; else return -1;
        }
    }

    /**
	 * sets the casted flag
	 */
    public void setCastedFlag() {
        casted = true;
    }

    /**
	 * @return the casted
	 */
    public boolean isCasted() {
        return casted;
    }
}
