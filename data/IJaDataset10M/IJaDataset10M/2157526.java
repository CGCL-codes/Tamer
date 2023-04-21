package net.sf.jdpa.cg.model;

/**
 * @author Andreas Nilsson
 */
public class Return implements Statement {

    private Expression returnValue;

    public Return() {
        this(null);
    }

    public Return(Expression returnValue) {
        this.returnValue = returnValue;
    }

    public Expression getReturnValue() {
        return returnValue;
    }

    public ConstructType getConstructType() {
        return ConstructType.RETURN;
    }

    public boolean hasReturnValue() {
        return (returnValue != null);
    }

    public int hashCode() {
        return 31 ^ (returnValue == null ? 0 : returnValue.hashCode());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Return)) {
            return false;
        } else {
            Return ret = (Return) obj;
            return (returnValue == ret.returnValue || returnValue != null && returnValue.equals(ret.returnValue));
        }
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("return");
        if (returnValue != null) {
            buffer.append(" ").append(returnValue);
        }
        buffer.append(";");
        return buffer.toString();
    }
}
