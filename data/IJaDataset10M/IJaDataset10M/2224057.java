package net.sf.jfpl.arith;

import net.sf.jfpl.core.FunN;

/**
 * 
 * @author <a href="mailto:sunguilin@users.sourceforge.net">Guile</a>
 */
public class ArithLong extends Arith<Long> {

    @Override
    public FunN<Long> add() {
        return new FunN<Long>() {

            @Override
            public Long call(Long lhs, Long rhs) {
                return lhs + rhs;
            }
        };
    }

    @Override
    public FunN<Long> divide() {
        return new FunN<Long>() {

            @Override
            public Long call(Long lhs, Long rhs) {
                return lhs / rhs;
            }
        };
    }

    @Override
    public FunN<Long> multiply() {
        return new FunN<Long>() {

            @Override
            public Long call(Long lhs, Long rhs) {
                return lhs * rhs;
            }
        };
    }

    @Override
    public FunN<Long> sub() {
        return new FunN<Long>() {

            @Override
            public Long call(Long lhs, Long rhs) {
                return lhs - rhs;
            }
        };
    }
}
