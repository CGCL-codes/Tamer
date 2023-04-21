package Javalette.Absyn;

public abstract class Unary_operator {

    public abstract <R, A> R accept(Unary_operator.Visitor<R, A> v, A arg);

    public interface Visitor<R, A> {

        public R visit(Javalette.Absyn.Negative p, A arg);

        public R visit(Javalette.Absyn.Logicalneg p, A arg);
    }
}
