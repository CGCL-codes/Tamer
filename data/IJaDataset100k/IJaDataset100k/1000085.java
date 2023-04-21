package net.sf.jfpl.core;

import net.sf.jfpl.tuple.Pair;

/**
 * 
 * @author <a href="mailto:sunguilin@users.sourceforge.net">Guile</a>
 */
public abstract class Fun4<P1, P2, P3, P4, R> extends Fun3<P1, P2, Pair<P3, P4>, R> {

    public abstract R call(P1 a, P2 b, P3 c, P4 d);

    @Override
    public R call(P1 condition, P2 t, Pair<P3, P4> f) {
        return call(condition, t, f.car(), f.cdr());
    }

    @Override
    public Fun3<P2, P3, P4, R> bind1(final P1 a) {
        return new Fun3<P2, P3, P4, R>() {

            @Override
            public R call(P2 b, P3 c, P4 d) {
                return Fun4.this.call(a, b, c, d);
            }
        };
    }

    public Fun3<P1, P3, P4, R> bind2(final P2 b) {
        return new Fun3<P1, P3, P4, R>() {

            @Override
            public R call(P1 condition, P3 c, P4 d) {
                return Fun4.this.call(condition, b, c, d);
            }
        };
    }

    public Fun3<P1, P2, P4, R> bind3(final P3 c) {
        return new Fun3<P1, P2, P4, R>() {

            @Override
            public R call(P1 condition, P2 t, P4 d) {
                return Fun4.this.call(condition, t, c, d);
            }
        };
    }

    public Fun3<P1, P2, P3, R> bind4(final P4 d) {
        return new Fun3<P1, P2, P3, R>() {

            @Override
            public R call(P1 condition, P2 t, P3 f) {
                return Fun4.this.call(condition, t, f, d);
            }
        };
    }
}
