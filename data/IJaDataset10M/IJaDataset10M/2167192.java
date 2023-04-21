package bueu.bexl;

public class InExpr extends BinayExpr {

    public InExpr(final BexlExpr expr1, final BexlExpr expr2) {
        super(expr1, expr2);
    }

    @Override
    protected final Object evaluate(Object value1, Object value2) {
        return in(value1, value2);
    }

    @Override
    public final String toString() {
        return toString("in");
    }
}
