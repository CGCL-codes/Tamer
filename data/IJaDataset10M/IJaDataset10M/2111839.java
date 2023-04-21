package translate;

public class OpExp extends Ex {

    public OpExp(Exp l, Exp r, int o) {
        super(new tree.BINOP(o, l.unEx(), r.unEx()));
    }
}
