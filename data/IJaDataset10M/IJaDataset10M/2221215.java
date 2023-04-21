package org.jfonia.connect;

/**
 * @author wijnand.schepens@gmail.com
 *
 */
public abstract class BinaryFunction<A, B, C> extends BasicNode implements Value<C> {

    protected Value<A> val1;

    protected Value<B> val2;

    public C get() {
        return calculate(val1.get(), val2.get());
    }

    public abstract C calculate(A a, B b);
}
