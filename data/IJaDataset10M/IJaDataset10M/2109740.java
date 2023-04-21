package asa;

/**
 *
 * @author scriptoff
 */
public class ASAInstExp extends ASAInst implements NeedBlocInterface {

    private ASAExpr expr;

    public ASAInstExp(ASAExpr expr) {
        this.expr = expr;
    }

    @Override
    public void evalue() {
        expr.evalue();
    }

    @Override
    public ASAExpr getExpr1() {
        return expr;
    }

    @Override
    public ASAExpr getExpr2() {
        return null;
    }

    @Override
    public String getLabelExplorateur() {
        return "InstExp";
    }

    @Override
    public void setBl(ASABloc bl) {
        expr.setBl(bl);
    }
}
