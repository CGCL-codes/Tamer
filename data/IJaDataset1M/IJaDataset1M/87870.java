package Javalette.Absyn;

public class LInteger extends Literal implements java.io.Serializable {

    public Integer integer_;

    public LInteger(Integer p1) {
        integer_ = p1;
    }

    public <R, A> R accept(Javalette.Absyn.Literal.Visitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}
