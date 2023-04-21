package maths;

import lolcode.*;

public class Not extends MathOperator {

    public Not(MathOperator a) {
        super("NOT", a, a);
    }

    @Override
    public Var run() throws RunTimeException {
        Var a = getA().run();
        System.out.println("Not.run(): " + a.getTroof());
        return new Var(!a.getTroof());
    }
}
