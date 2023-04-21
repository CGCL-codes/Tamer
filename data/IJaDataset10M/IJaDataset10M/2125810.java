package syntaxtree;

import visitor.ObjectVisitor;
import visitor.Visitor;
import visitor.TypeVisitor;

public class Paren extends Exp {

    public Exp e;

    public Paren(Exp ae) {
        e = ae;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }

    public Object accept(ObjectVisitor v) {
        return v.visit(this);
    }

    public Types.Type accept(Types.FillTable v, Types.Type arg) {
        return v.visit(this, arg);
    }

    public Translate.Exp accept(Translate.Translate v, Types.Type arg) {
        return v.visit(this, arg);
    }
}
