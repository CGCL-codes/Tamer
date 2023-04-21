package org.uithin.tools.constraints;

import EDU.Washington.grad.gjb.cassowary.ClConstraint;
import EDU.Washington.grad.gjb.cassowary.ClLinearEquation;
import EDU.Washington.grad.gjb.cassowary.ClLinearExpression;
import EDU.Washington.grad.gjb.cassowary.ClLinearInequality;
import EDU.Washington.grad.gjb.cassowary.ClStrength;

/**
 * A linear constraint of the form:
 * <br>
 * k1.v1 + k2.v2 + ... + kN.vN + c <op> 0
 * <br>
 * Each variable has an attribute that indicates
 * whether it should be considered as a variable or as a constant. 
 * @author gpothier
 */
public class Constraint {

    private double[] _k;

    private PropertyWrapper[] _v;

    private Type[] _t;

    private double _c;

    private Operator _op;

    private ClStrength _strength;

    public enum Type {

        VAR, CONST
    }

    public enum Operator {

        EQ, GEQ, LEQ
    }

    public Constraint(double[] a_k, PropertyWrapper[] a_v, Type[] a_t, double a_c, Operator a_op) {
        this(a_k, a_v, a_t, a_c, a_op, ClStrength.required);
    }

    public Constraint(double[] a_k, PropertyWrapper[] a_v, Type[] a_t, double a_c, Operator a_op, ClStrength a_strength) {
        for (PropertyWrapper v : a_v) assert v != null;
        _c = a_c;
        _k = a_k;
        _op = a_op;
        _t = a_t;
        _v = a_v;
        _strength = a_strength;
    }

    public double get_c() {
        return _c;
    }

    public double[] get_k() {
        return _k;
    }

    public Type[] get_t() {
        return _t;
    }

    public PropertyWrapper[] get_v() {
        return _v;
    }

    public ClStrength get_strength() {
        return _strength;
    }

    public ClConstraint createClConstraint() {
        double tC = _c;
        ClLinearExpression expr = new ClLinearExpression();
        for (int i = 0; i < _k.length; i++) {
            double k = _k[i];
            PropertyWrapper v = _v[i];
            Type t = _t[i];
            if (t == Type.CONST) tC += k * v.value(); else if (t == Type.VAR) expr.addVariable(v, k);
        }
        expr.set_constant(tC);
        switch(_op) {
            case EQ:
                return new ClLinearEquation(expr, _strength);
            case GEQ:
                return new ClLinearInequality(expr, _strength);
            case LEQ:
                return new ClLinearInequality(expr.multiplyMe(-1), _strength);
            default:
                throw new RuntimeException();
        }
    }
}
