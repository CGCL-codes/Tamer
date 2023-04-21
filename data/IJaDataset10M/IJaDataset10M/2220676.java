package absyn;

public class SeqExpr extends Expr {

    public ExprList exprList;

    public SeqExpr(int pos, ExprList list) {
        super(pos);
        this.exprList = list;
    }
}
