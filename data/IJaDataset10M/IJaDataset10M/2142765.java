package Javalette.Absyn;

public class TBool extends Type implements java.io.Serializable {

    public TBool() {
    }

    public <R, A> R accept(Javalette.Absyn.Type.Visitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}
