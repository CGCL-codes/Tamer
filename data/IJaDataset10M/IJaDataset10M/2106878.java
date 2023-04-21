package net.sf.jga;

import junit.framework.Assert;
import net.sf.jga.fn.Functor;

public class SampleFunctor<T> extends Functor<T> {

    private static final long serialVersionUID = -6152819474444709260L;

    public Object[] expect, actual;

    public T returnVal;

    public SampleFunctor(T returnVal, Object... expect) {
        this.expect = expect;
        this.returnVal = returnVal;
    }

    public T eval(Object... args) {
        this.actual = args;
        Assert.assertEquals(expect.length, args.length);
        for (int i = 0; i < args.length; ++i) {
            Assert.assertEquals(expect[i], args[i]);
        }
        return returnVal;
    }
}
