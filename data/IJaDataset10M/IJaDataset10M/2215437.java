package org.progeeks.mparse.exp;

import java.io.*;
import org.progeeks.util.ObjectUtils;
import org.progeeks.mparse.*;

/**
 *
 *  @version   $Revision: 4115 $
 *  @author    Paul Speed
 */
public abstract class AbstractExpression implements Expression {

    private String name;

    private Producer producer;

    private Expression resolved;

    protected AbstractExpression() {
    }

    protected AbstractExpression(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setProducer(Producer p) {
        this.producer = p;
    }

    public Producer getProducer() {
        return producer;
    }

    /**
     *  Default implementation returns this expression.
     */
    protected Expression doResolve() {
        return this;
    }

    /**
     *  If this expression has not been resolved
     *  previously then this will call the overridable
     *  method resolveExpression.
     */
    public final Expression resolve() {
        if (resolved != null) return resolved;
        resolved = doResolve();
        return resolved;
    }

    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) return false;
        AbstractExpression other = (AbstractExpression) o;
        if (!ObjectUtils.areEqual(producer, other.producer)) return false;
        return ObjectUtils.areEqual(name, other.name);
    }

    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }

    public Repeat rep(int min, int max) {
        return Expressions.repeat(this, min, max);
    }

    public Repeat any() {
        return Expressions.repeat(this, 0);
    }

    public Repeat oneOrMore() {
        return Expressions.repeat(this, 1);
    }

    public Repeat optional() {
        return Expressions.repeat(this, 0, 1);
    }

    public abstract ResultType parse(ParseContext context, CharStream in) throws IOException;

    protected String nameString() {
        if (name == null) return "";
        return "<" + name + ">";
    }

    /**
     *  Version of toString() that will only
     *  print the contents of child nodes if depth
     *  is greater than 0.
     */
    public String toString(int depth) {
        return getClass().getSimpleName() + "@" + System.identityHashCode(this);
    }

    /**
     *  Default implementation returns toString(5)
     */
    public String toString() {
        return toString(5);
    }
}
