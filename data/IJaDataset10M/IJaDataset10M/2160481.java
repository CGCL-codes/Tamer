package net.sf.beezle.ssass.scss;

import net.sf.beezle.mork.misc.GenericException;

public class Declaration implements Base, SsassDeclaration {

    private final String property;

    private final Expr expr;

    private final Prio prio;

    public Declaration(String property, Expr expr, Prio prio) {
        this.property = property;
        this.expr = expr;
        this.prio = prio;
    }

    @Override
    public void toCss(Output output) throws GenericException {
        output.propertyPrefix();
        output.object(property, ":");
        output.spaceOpt();
        output.object(expr);
        if (prio != null) {
            output.spaceOpt();
            output.base(prio);
        }
    }
}
