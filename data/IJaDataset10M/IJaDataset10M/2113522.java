package Javalette.Absyn;

public class LTrue extends Literal implements java.io.Serializable {

    public LTrue() {
    }

    public <R, A> R accept(Javalette.Absyn.Literal.Visitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}
