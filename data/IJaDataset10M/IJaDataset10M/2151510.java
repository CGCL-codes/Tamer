package org.xteam.sled.semantic.exp;

public abstract class ExpNarrow extends Exp {

    protected Exp expr;

    protected int width;

    public ExpNarrow(Exp expr, int width) {
        this.expr = expr;
        this.width = width;
        this.expr.setParent(this);
    }

    public int width() {
        return width;
    }

    public Exp getExpr() {
        return expr;
    }

    public int hashCode() {
        return expr.hashCode() + width;
    }

    public void replace(Exp oldExp, Exp newExp) {
        if (oldExp == expr) expr = newExp;
    }
}
