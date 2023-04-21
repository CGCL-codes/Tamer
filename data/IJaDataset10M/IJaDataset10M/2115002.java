package Tree;

import Temp.Temp;
import Temp.Label;
import java.util.LinkedList;

public class MEM extends Exp {

    public Exp exp;

    public MEM(Exp e) {
        exp = e;
    }

    public LinkedList<Exp> kids() {
        LinkedList<Exp> kids = new LinkedList<Exp>();
        kids.addFirst(exp);
        return kids;
    }

    public Exp build(LinkedList<Exp> kids) {
        return new MEM(kids.getFirst());
    }

    public void accept(IntVisitor v, int d) {
        v.visit(this, d);
    }

    public Temp accept(CodeVisitor v) {
        return v.visit(this);
    }

    public <R> R accept(ResultVisitor<R> v) {
        return v.visit(this);
    }
}
