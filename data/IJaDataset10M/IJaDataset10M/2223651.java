package at.langegger.xlwrap.map.expr.val;

import at.langegger.xlwrap.common.XLWrapException;
import at.langegger.xlwrap.map.expr.XLExpr;

/**
 * @author dorgon
 * 
 */
public class E_Long extends XLExprNumber<Long> {

    /**
	 * parse string to long integer
	 * 
	 * @param value
	 */
    public E_Long(String value) {
        super(Long.parseLong(value));
    }

    /**
	 * @param value
	 */
    public E_Long(Long value) {
        super(value);
    }

    @Override
    public XLExpr copy() {
        return new E_Long(new Long(value));
    }

    @Override
    public XLExprNumber<?> add(XLExprNumber<?> other) throws XLWrapException {
        if (other instanceof E_Long) return new E_Long(this.value + (Long) other.value); else if (other instanceof E_Double) return new E_Double(this.value + (Double) other.value);
        throw new XLWrapException("Incompatible summands: " + this + " + " + other);
    }

    @Override
    public XLExprNumber<?> divide(XLExprNumber<?> other) throws XLWrapException {
        if (other instanceof E_Long) {
            long rest = value % ((E_Long) other).getValue();
            if (rest == 0) return new E_Long(value / ((E_Long) other).getValue()); else return new E_Double((double) value / ((E_Long) other).getValue());
        } else if (other instanceof E_Double) return new E_Double(value / ((E_Double) other).getValue());
        throw new XLWrapException("Incompatible division arguments: " + this + " / " + other);
    }

    @Override
    public XLExprNumber<?> multiply(XLExprNumber<?> other) throws XLWrapException {
        if (other instanceof E_Long) return new E_Long(value * ((E_Long) other).getValue()); else if (other instanceof E_Double) return new E_Double(value * ((E_Double) other).getValue());
        throw new XLWrapException("Incompatible multipliers: " + this + " / " + other);
    }

    @Override
    public XLExprValue<?> power(XLExprValue<?> other) throws XLWrapException {
        double o;
        if (other instanceof E_Long) o = ((E_Long) other).getValue().doubleValue(); else if (other instanceof E_Double) o = ((E_Double) other).getValue(); else throw new XLWrapException("Incompatible division arguments: " + this + " / " + other);
        double result = Math.pow(value.doubleValue(), o);
        if (result == (long) result) return new E_Long((long) result); else return new E_Double(result);
    }
}
