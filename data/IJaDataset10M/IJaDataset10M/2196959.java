package net.sf.jfpl.arith;

import net.sf.jfpl.core.FunN;

/**
 * 
 * @author <a href="mailto:sunguilin@users.sourceforge.net">Guile</a>
 */
public class ArithDouble extends Arith<Double> {

    @Override
    public FunN<Double> add() {
        return new FunN<Double>() {

            @Override
            public Double call(Double lhs, Double rhs) {
                return lhs + rhs;
            }
        };
    }

    @Override
    public FunN<Double> divide() {
        return new FunN<Double>() {

            @Override
            public Double call(Double lhs, Double rhs) {
                return lhs / rhs;
            }
        };
    }

    @Override
    public FunN<Double> multiply() {
        return new FunN<Double>() {

            @Override
            public Double call(Double lhs, Double rhs) {
                return lhs * rhs;
            }
        };
    }

    @Override
    public FunN<Double> sub() {
        return new FunN<Double>() {

            @Override
            public Double call(Double lhs, Double rhs) {
                return lhs - rhs;
            }
        };
    }
}
