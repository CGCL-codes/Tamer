package shef.nlp.buchart.prolog.cafe;

import jp.ac.kobe_u.cs.prolog.lang.*;
import jp.ac.kobe_u.cs.prolog.builtin.*;

public class PRED_not_1 extends Predicate {

    public Term arg1;

    public PRED_not_1(Term a1, Predicate cont) {
        arg1 = a1;
        this.cont = cont;
    }

    public PRED_not_1() {
    }

    public void setArgument(Term[] args, Predicate cont) {
        arg1 = args[0];
        this.cont = cont;
    }

    public Predicate exec(Prolog engine) {
        engine.setB0();
        Term a1;
        a1 = arg1.dereference();
        return new PRED_$dummy_buchart_utils46pl_2_1(a1, cont);
    }

    public int arity() {
        return 1;
    }

    public String toString() {
        return "not(" + arg1 + ")";
    }
}
