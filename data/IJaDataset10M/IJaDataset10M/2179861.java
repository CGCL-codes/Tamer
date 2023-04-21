package org.enjavers.jethro.dspi.dto.domains;

import java.util.Map;
import org.enjavers.jethro.api.dto.DTODomain;
import org.enjavers.jethro.api.dto.DTODomainException;

/**
 * This represents the integer numbers domain; each IntegerDomain
 * can support or not negative values, and can be ranged (bounds are
 * assumed as inclusive).
 * By default (i.e., if a domain param is missing), each integer domain
 * holds all numbers between Integer.MIN_VALUE and Integer.MAX_VALUE.
 * @author Alessandro Lombardi
 */
public class IntegersDomain extends DTODomain {

    public static final String NEGATIVE_VALUES = "negative";

    public static final String LOWER_BOUND = "lower-bound";

    public static final String UPPER_BOUND = "upper-bound";

    protected boolean _negative_values = true;

    protected Integer _lower_bound = null;

    protected Integer _upper_bound = null;

    public IntegersDomain() {
        super();
    }

    public IntegersDomain(Map i_domain_params) {
        super(i_domain_params);
        _negative_values = _domain_params.get(NEGATIVE_VALUES) == null || new Boolean(_domain_params.get(NEGATIVE_VALUES).toString()).booleanValue();
        _lower_bound = _domain_params.get(LOWER_BOUND) != null ? new Integer(_domain_params.get(LOWER_BOUND).toString()) : (_negative_values ? new Integer(Integer.MIN_VALUE) : new Integer(0));
        _upper_bound = _domain_params.get(UPPER_BOUND) != null ? new Integer(_domain_params.get(UPPER_BOUND).toString()) : new Integer(Integer.MAX_VALUE);
        if (_lower_bound.intValue() > _upper_bound.intValue()) throw new IllegalArgumentException("In IntegerDomain must be: lower bound<=upperer bound. ");
        if (!_negative_values && (_lower_bound.intValue() < 0 || _upper_bound.intValue() < 0)) throw new IllegalArgumentException("This IntegerDomain cannot have negative values (check bounds).");
    }

    public boolean isInDomain(Object i_value) {
        if (i_value == null) return allowsNull();
        if (getDomainClass().isInstance(i_value)) {
            return ((Integer) i_value).intValue() >= _lower_bound.intValue() && ((Integer) i_value).intValue() <= _upper_bound.intValue();
        }
        return false;
    }

    public Class getDomainClass() {
        return Integer.class;
    }

    public Object getDefaultElement() {
        return new Integer(0);
    }

    public String toString() {
        return super.toString() + "; allows negative values = " + _negative_values + ", lower bound = " + _lower_bound + ", upper bound = " + _upper_bound;
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODomain#parseObject(java.lang.Object)
	 */
    public Object parseObject(Object i_value) throws DTODomainException {
        if (isInDomain(i_value)) return i_value;
        if (i_value instanceof String) {
            try {
                Integer intVal = new Integer(Integer.parseInt((String) i_value));
                if (isInDomain(intVal)) return intVal; else throw new DTODomainException(intVal + " is not member of this domain: " + this);
            } catch (Exception ex) {
                throw new DTODomainException("Cannot parse " + i_value + ".");
            }
        }
        throw new DTODomainException("Cannot parse " + i_value.getClass().getName() + " (" + i_value + ").");
    }

    public boolean equals(Object i_obj) {
        if (i_obj == null || !(i_obj instanceof IntegersDomain)) return false;
        boolean same_params = _domain_params == null ? ((IntegersDomain) i_obj)._domain_params == null : _domain_params.equals(((IntegersDomain) i_obj)._domain_params);
        return (_allows_null == ((IntegersDomain) i_obj)._allows_null) && (_negative_values == ((IntegersDomain) i_obj)._negative_values) && (_lower_bound == ((IntegersDomain) i_obj)._lower_bound) && (_upper_bound == ((IntegersDomain) i_obj)._upper_bound) && same_params;
    }
}
