package net.sf.crepido.base;

public abstract class AbstractTernaryFunction<A1, A2, A3, R> extends AbstractFunction<R> implements ITernaryFunction<A1, A2, A3, R> {

    public abstract R apply(A1 arg1, A2 arg2, A3 arg3);

    public int arity() {
        return 3;
    }

    public AbstractBinaryFunction<A2, A3, R> partial(final A1 arg) {
        return new AbstractBinaryFunction<A2, A3, R>() {

            public R apply(final A2 arg2, final A3 arg3) {
                return AbstractTernaryFunction.this.apply(arg, arg2, arg3);
            }
        };
    }

    public AbstractUnaryFunction<A3, R> partial(final A1 arg1, final A2 arg2) {
        return new AbstractUnaryFunction<A3, R>() {

            public R apply(final A3 arg3) {
                return AbstractTernaryFunction.this.apply(arg1, arg2, arg3);
            }
        };
    }

    public AbstractNullaryFunction<R> partial(final A1 arg1, final A2 arg2, final A3 arg3) {
        return new AbstractNullaryFunction<R>() {

            public R apply() {
                return AbstractTernaryFunction.this.apply(arg1, arg2, arg3);
            }
        };
    }

    public AbstractUnaryFunction<A1, AbstractUnaryFunction<A2, AbstractUnaryFunction<A3, R>>> curry() {
        return new AbstractUnaryFunction<A1, AbstractUnaryFunction<A2, AbstractUnaryFunction<A3, R>>>() {

            public AbstractUnaryFunction<A2, AbstractUnaryFunction<A3, R>> apply(final A1 arg1) {
                return AbstractTernaryFunction.this.partial(arg1).curry();
            }
        };
    }

    public <R2> AbstractTernaryFunction<A1, A2, A3, R2> then(final IUnaryFunction<? super R, R2> f) {
        return new AbstractTernaryFunction<A1, A2, A3, R2>() {

            public R2 apply(A1 arg1, A2 arg2, A3 arg3) {
                return f.apply(AbstractTernaryFunction.this.apply(arg1, arg2, arg3));
            }
        };
    }

    public AbstractTernaryProcedure<A1, A2, A3> toProcedure() {
        return new AbstractTernaryProcedure<A1, A2, A3>() {

            public void apply(final A1 arg1, final A2 arg2, final A3 arg3) {
                AbstractTernaryFunction.this.apply(arg1, arg2, arg3);
            }
        };
    }

    public AbstractTernaryFunction<A1, A2, A3, R> safe(final R defaultValue) {
        return this.safe(null);
    }

    public AbstractTernaryFunction<A1, A2, A3, R> safe(final R defaultValue, final IUnaryProcedure<? super Exception> doOnException) {
        return new AbstractTernaryFunction<A1, A2, A3, R>() {

            public R apply(final A1 arg1, final A2 arg2, final A3 arg3) {
                R ret = defaultValue;
                try {
                    ret = AbstractTernaryFunction.this.apply(arg1, arg2, arg3);
                } catch (Exception e) {
                    if (doOnException != null) {
                        doOnException.apply(e);
                    }
                }
                return ret;
            }
        };
    }
}
