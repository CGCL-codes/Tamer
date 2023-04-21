package absyn;

public class NegationExpr extends Expr {

    public Expr value;

    public NegationExpr(int pos, Expr v) {
        super(pos);
        value = v;
    }
}
