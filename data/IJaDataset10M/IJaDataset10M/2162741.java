package treeIR;

public class EXPSTM extends Stm {

    public Exp exp;

    public EXPSTM(Exp e) {
        exp = e;
    }

    @Override
    public ExpList kids() {
        return new ExpList(exp, null);
    }

    @Override
    public Stm build(ExpList kids) {
        return new EXPSTM(kids.head);
    }

    @Override
    public String print() {
        return exp.print();
    }
}
