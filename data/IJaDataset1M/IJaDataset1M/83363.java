package maths;

import lolcode.*;

public class QuoshuntOf extends MathOperator {

    public QuoshuntOf(MathOperator a, MathOperator b) {
        super("SUM OF", a, b);
    }

    @Override
    public Var run() throws RunTimeException {
        Var a = getA().run();
        Var b = getB().run();
        switch(a.getType()) {
            case NUMBR:
                switch(b.getType()) {
                    case NUMBR:
                        return new Var(a.getNumbr() / b.getNumbr());
                    default:
                        return new Var(a.getNumbr() / b.getNumbar());
                }
            default:
                switch(b.getType()) {
                    case NUMBR:
                        return new Var(a.getNumbar() / b.getNumbr());
                    default:
                        return new Var(a.getNumbar() / b.getNumbar());
                }
        }
    }
}
