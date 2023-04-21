package benchmark.ratpoly.instrumented;

import piaba.symlib.array.SymArray;
import piaba.symlib.array.SymArrayUtil;
import piaba.symlib.number.SymInt;
import piaba.symlib.number.SymIntConst;
import piaba.symlib.number.SymNumber;

/*************************************************************************
 *  Compilation:  javac RatPoly.java
 *  Execution:    java RatPoly
 *
 *  A polynomial with arbitrary precision rational coefficients.
 *
 *************************************************************************/
public class RatPoly {

    public static SymArray<BigRational> createArray_BigRational(SymNumber size) {
        BigRational[] ar = new BigRational[size.getInt()];
        return new SymArray<BigRational>(ar);
    }

    public static final RatPoly ZERO = new RatPoly(BigRational.ZERO, SymInt.ZERO);

    private SymArray<BigRational> coef;

    private SymNumber deg;

    public RatPoly(BigRational a, SymNumber b) {
        coef = createArray_BigRational(b.add(SymIntConst.ONE));
        for (SymNumber i = SymInt.ZERO; i.lt(b).eval(); i = i.add(SymInt.ONE)) {
            coef.set(i, BigRational.ZERO);
        }
        coef.set(b, a);
        deg = degree();
    }

    public SymNumber degree() {
        SymNumber d = SymInt.ZERO;
        for (SymNumber i = SymInt.ZERO; i.lt(coef.length()).eval(); i = i.add(SymInt.ONE)) {
            if (coef.get(i).compareTo_(BigRational.ZERO).neq(SymInt.ZERO).eval()) {
                d = i;
            }
        }
        return d;
    }

    public RatPoly plus(RatPoly b) {
        RatPoly a = this;
        RatPoly c = new RatPoly(BigRational.ZERO, max_(a.deg, b.deg));
        for (SymNumber i = SymInt.ZERO; i.lte(a.deg).eval(); i = i.add(SymInt.ONE)) {
            c.coef.set(i, c.coef.get(i).plus(a.coef.get(i)));
        }
        for (SymNumber i = SymInt.ZERO; i.lte(b.deg).eval(); i = i.add(SymInt.ONE)) {
            c.coef.set(i, c.coef.get(i).plus(b.coef.get(i)));
        }
        c.deg = c.degree();
        return c;
    }

    private static SymNumber max_(SymNumber a_, SymNumber b_) {
        return a_.gte(b_).eval() ? a_ : b_;
    }

    public RatPoly minus(RatPoly b) {
        RatPoly a = this;
        RatPoly c = new RatPoly(BigRational.ZERO, max_(a.deg, b.deg));
        for (SymNumber i = SymInt.ZERO; i.lte(a.deg).eval(); i = i.add(SymInt.ONE)) {
            c.coef.set(i, c.coef.get(i).plus(a.coef.get(i)));
        }
        for (SymNumber i = SymInt.ZERO; i.lte(b.deg).eval(); i = i.add(SymInt.ONE)) {
            c.coef.set(i, c.coef.get(i).minus(b.coef.get(i)));
        }
        c.deg = c.degree();
        return c;
    }

    public RatPoly times(RatPoly b) {
        RatPoly a = this;
        RatPoly c = new RatPoly(BigRational.ZERO, a.deg.add(b.deg));
        for (SymNumber i = SymInt.ZERO; i.lte(a.deg).eval(); i = i.add(SymInt.ONE)) {
            for (SymNumber j = SymInt.ZERO; j.lte(b.deg).eval(); j = j.add(SymInt.ONE)) {
                c.coef.set(i.add(j), c.coef.get(i.add(j)).plus(a.coef.get(i).times(b.coef.get(j))));
            }
        }
        c.deg = c.degree();
        return c;
    }

    public RatPoly divides(RatPoly b) {
        RatPoly a = this;
        if (b.deg.eq(SymInt.ZERO).and((b.coef.get(SymInt.ZERO).compareTo_(BigRational.ZERO).eq(SymInt.ZERO))).eval()) {
            throw new RuntimeException("Divide by zero polynomial");
        }
        if (a.deg.lt(b.deg).eval()) {
            return ZERO;
        }
        BigRational coefficient = a.coef.get(a.deg).divides(b.coef.get(b.deg));
        SymNumber exponent = a.deg.minus(b.deg);
        RatPoly c = new RatPoly(coefficient, exponent);
        return c.plus((a.minus(b.times(c)).divides(b)));
    }

    public RatPoly truncate(SymInt d) {
        RatPoly p = new RatPoly(BigRational.ZERO, d);
        for (SymNumber i = SymInt.ZERO; i.lte(d).eval(); i = i.add(SymInt.ONE)) {
            p.coef.set(i, coef.get(i));
        }
        p.deg = p.degree();
        return p;
    }

    public BigRational evaluate(BigRational x) {
        BigRational p = BigRational.ZERO;
        for (SymNumber i = deg; i.gte(SymInt.ZERO).eval(); i = i.minus(SymInt.ONE)) {
            p = coef.get(i).plus(x.times(p));
        }
        return p;
    }

    public RatPoly differentiate() {
        if (deg.eq(SymInt.ZERO).eval()) return ZERO;
        RatPoly deriv = new RatPoly(BigRational.ZERO, deg.minus(SymInt.ONE));
        for (SymNumber i = SymInt.ZERO; i.lt(deg).eval(); i = i.add(SymInt.ONE)) {
            deriv.coef.set(i, coef.get(i.add(SymInt.ONE)).times(new BigRational(i.add(SymInt.ONE))));
        }
        deriv.deg = deriv.degree();
        return deriv;
    }

    public RatPoly integrate() {
        RatPoly integral = new RatPoly(BigRational.ZERO, deg.add(SymInt.ONE));
        for (SymNumber i = SymInt.ZERO; i.lte(deg).eval(); i = i.add(SymInt.ONE)) {
            integral.coef.set(i.add(SymInt.ONE), coef.get(i).divides(new BigRational(i.add(SymInt.ONE))));
        }
        integral.deg = integral.degree();
        return integral;
    }

    public BigRational integrate(BigRational a, BigRational b) {
        RatPoly integral = integrate();
        return integral.evaluate(b).minus(integral.evaluate(a));
    }
}
