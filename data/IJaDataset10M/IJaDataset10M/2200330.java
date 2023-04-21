package net.sf.staccatocommons.lang.function;

import net.sf.staccatocommons.defs.Applicable;
import net.sf.staccatocommons.defs.function.Function;
import net.sf.staccatocommons.defs.function.Function2;
import net.sf.staccatocommons.defs.function.Function3;
import net.sf.staccatocommons.restrictions.check.NonNull;
import net.sf.staccatocommons.restrictions.processing.ForceRestrictions;

/**
 * 
 * @author flbulgarelli
 * 
 * @param <A>
 * @param <B>
 * @param <C>
 */
public abstract class AbstractFunction2<A, B, C> extends AbstractDelayable2<A, B, C> implements Function2<A, B, C> {

    @NonNull
    public Function<B, C> apply(final A arg0) {
        return new AbstractFunction<B, C>() {

            public C apply(B arg1) {
                return AbstractFunction2.this.apply(arg0, arg1);
            }
        };
    }

    public Function2<B, A, C> flip() {
        return new AbstractFunction2<B, A, C>() {

            public C apply(B arg1, A arg0) {
                return AbstractFunction2.this.apply(arg0, arg1);
            }
        };
    }

    public final Function2<A, B, C> nullSafe() {
        return new AbstractFunction2<A, B, C>() {

            public C apply(A arg0, B arg1) {
                if (arg0 == null || arg1 == null) return null;
                return AbstractFunction2.this.apply(arg0, arg1);
            }
        };
    }

    @ForceRestrictions
    public <D> Function2<D, B, C> of(@NonNull final Applicable<? super D, ? extends A> function) {
        return new AbstractFunction2<D, B, C>() {

            public C apply(D arg0, B arg1) {
                return AbstractFunction2.this.apply(function.apply(arg0), arg1);
            }
        };
    }

    public <A2, B2, D> Function3<A, B, A2, D> then(final Function2<C, B2, D> combinator, final Function<? super A2, ? extends B2> other) {
        return new AbstractFunction3<A, B, A2, D>() {

            public D apply(A arg0, B arg1, A2 arg2) {
                return combinator.apply(AbstractFunction2.this.apply(arg0, arg1), other.apply(arg2));
            }
        };
    }

    public <D> Function2<A, B, D> then(Function<? super C, ? extends D> other) {
        return (Function2<A, B, D>) other.of(this);
    }

    public String toString() {
        return "Function2";
    }
}
