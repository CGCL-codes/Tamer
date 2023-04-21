package net.sf.jfpl.arith;

import net.sf.jfpl.core.FunN;

/**
 * 
 * @author <a href="mailto:sunguilin@users.sourceforge.net">Guile</a>
 */
public class ArithInt extends Arith<Integer> {

    @Override
    public FunN<Integer> add() {
        return new FunN<Integer>() {

            @Override
            public Integer call(Integer lhs, Integer rhs) {
                return lhs + rhs;
            }
        };
    }

    @Override
    public FunN<Integer> divide() {
        return new FunN<Integer>() {

            @Override
            public Integer call(Integer lhs, Integer rhs) {
                return lhs / rhs;
            }
        };
    }

    @Override
    public FunN<Integer> multiply() {
        return new FunN<Integer>() {

            @Override
            public Integer call(Integer lhs, Integer rhs) {
                return lhs * rhs;
            }
        };
    }

    @Override
    public FunN<Integer> sub() {
        return new FunN<Integer>() {

            @Override
            public Integer call(Integer lhs, Integer rhs) {
                return lhs - rhs;
            }
        };
    }
}
