package org.apache.myfaces.view.facelets.tag.jstl.core;

import javax.el.ELContext;
import javax.el.ValueExpression;

/**
 * @author Jacob Hookom
 * @version $Id: IterationStatusExpression.java,v 1.4 2008/07/13 19:01:44 rlubke Exp $
 */
public final class IterationStatusExpression extends ValueExpression {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final IterationStatus status;

    /**
     * 
     */
    public IterationStatusExpression(IterationStatus status) {
        this.status = status;
    }

    public Object getValue(ELContext context) {
        return this.status;
    }

    public void setValue(ELContext context, Object value) {
        throw new UnsupportedOperationException("Cannot set IterationStatus");
    }

    public boolean isReadOnly(ELContext context) {
        return true;
    }

    public Class getType(ELContext context) {
        return IterationStatus.class;
    }

    public Class getExpectedType() {
        return IterationStatus.class;
    }

    public String getExpressionString() {
        return this.toString();
    }

    public boolean equals(Object obj) {
        return this.status.equals(obj);
    }

    public int hashCode() {
        return this.status.hashCode();
    }

    public boolean isLiteralText() {
        return true;
    }

    public String toString() {
        return this.status.toString();
    }
}
