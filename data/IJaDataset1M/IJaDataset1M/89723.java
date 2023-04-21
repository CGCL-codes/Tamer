package net.sourceforge.freejava.closure.alt;

public abstract class Pred4<A, B, C, D> implements Func4<Boolean, A, B, C, D> {

    @Override
    public final Boolean eval(A a, B b, C c, D d) {
        return test(a, b, c, d);
    }

    public abstract boolean test(A a, B b, C c, D d);
}
