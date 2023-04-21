package org.datanucleus.store.mapped.expression;

import java.math.BigDecimal;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.util.StringUtils;

/**
 * Representation of a FloatPoint literal in a query.
 */
public class FloatingPointLiteral extends NumericExpression implements Literal {

    private final BigDecimal value;

    /** Raw value that this literal represents. */
    Object rawValue;

    /**
     * Creates a floating point literal
     * @param qs the QueryExpression
     * @param mapping the mapping
     * @param value the floating point value
     */
    public FloatingPointLiteral(QueryExpression qs, JavaTypeMapping mapping, Float value) {
        super(qs);
        this.mapping = mapping;
        this.value = new BigDecimal(value.toString());
        st.appendParameter(qs.getStoreManager().getMappingManager().getMappingWithDatastoreMapping(Float.class, false, false, qs.getClassLoaderResolver()), value);
    }

    public Object getValue() {
        return value;
    }

    /**
     * Creates a floating point literal
     * @param qs the QueryExpression
     * @param mapping the mapping
     * @param value the floating point value
     */
    public FloatingPointLiteral(QueryExpression qs, JavaTypeMapping mapping, Double value) {
        super(qs);
        this.mapping = mapping;
        this.value = new BigDecimal(value.toString());
        st.appendParameter(qs.getStoreManager().getMappingManager().getMappingWithDatastoreMapping(Double.class, false, false, qs.getClassLoaderResolver()), value);
    }

    /**
     * Creates a floating point literal
     * @param qs the QueryExpression
     * @param value the floating point value
     */
    public FloatingPointLiteral(QueryExpression qs, BigDecimal value) {
        super(qs);
        this.value = value;
        st.append(StringUtils.exponentialFormatBigDecimal(value));
    }

    public BooleanExpression eq(ScalarExpression expr) {
        assertValidTypeForParameterComparison(expr, NumericExpression.class);
        if (expr instanceof FloatingPointLiteral) {
            return new BooleanLiteral(qs, mapping, value.compareTo(((FloatingPointLiteral) expr).value) == 0);
        } else if (expr instanceof CharacterExpression) {
            CharacterLiteral literal = new CharacterLiteral(qs, mapping, String.valueOf((char) value.intValue()));
            return new BooleanExpression(expr, OP_EQ, literal);
        } else {
            return super.eq(expr);
        }
    }

    public BooleanExpression noteq(ScalarExpression expr) {
        assertValidTypeForParameterComparison(expr, NumericExpression.class);
        if (expr instanceof FloatingPointLiteral) {
            return new BooleanLiteral(qs, mapping, value.compareTo(((FloatingPointLiteral) expr).value) != 0);
        } else if (expr instanceof CharacterExpression) {
            CharacterLiteral literal = new CharacterLiteral(qs, mapping, String.valueOf((char) value.intValue()));
            return new BooleanExpression(expr, OP_NOTEQ, literal);
        } else {
            return super.noteq(expr);
        }
    }

    public BooleanExpression lt(ScalarExpression expr) {
        if (expr instanceof FloatingPointLiteral) {
            return new BooleanLiteral(qs, mapping, value.compareTo(((FloatingPointLiteral) expr).value) < 0);
        } else if (expr instanceof CharacterExpression) {
            CharacterLiteral literal = new CharacterLiteral(qs, mapping, String.valueOf((char) value.intValue()));
            return new BooleanExpression(literal, OP_LT, expr);
        } else {
            return super.lt(expr);
        }
    }

    public BooleanExpression lteq(ScalarExpression expr) {
        if (expr instanceof FloatingPointLiteral) {
            return new BooleanLiteral(qs, mapping, value.compareTo(((FloatingPointLiteral) expr).value) <= 0);
        } else if (expr instanceof CharacterExpression) {
            CharacterLiteral literal = new CharacterLiteral(qs, mapping, String.valueOf((char) value.intValue()));
            return new BooleanExpression(literal, OP_LTEQ, expr);
        } else {
            return super.lteq(expr);
        }
    }

    public BooleanExpression gt(ScalarExpression expr) {
        if (expr instanceof FloatingPointLiteral) {
            return new BooleanLiteral(qs, mapping, value.compareTo(((FloatingPointLiteral) expr).value) > 0);
        } else if (expr instanceof CharacterExpression) {
            CharacterLiteral literal = new CharacterLiteral(qs, mapping, String.valueOf((char) value.intValue()));
            return new BooleanExpression(literal, OP_GT, expr);
        } else {
            return super.gt(expr);
        }
    }

    public BooleanExpression gteq(ScalarExpression expr) {
        if (expr instanceof FloatingPointLiteral) {
            return new BooleanLiteral(qs, mapping, value.compareTo(((FloatingPointLiteral) expr).value) >= 0);
        } else if (expr instanceof CharacterExpression) {
            CharacterLiteral literal = new CharacterLiteral(qs, mapping, String.valueOf((char) value.intValue()));
            return new BooleanExpression(literal, OP_GTEQ, expr);
        } else {
            return super.gteq(expr);
        }
    }

    public ScalarExpression add(ScalarExpression expr) {
        if (expr instanceof FloatingPointLiteral) {
            return new FloatingPointLiteral(qs, value.add(((FloatingPointLiteral) expr).value));
        } else {
            return super.add(expr);
        }
    }

    public ScalarExpression sub(ScalarExpression expr) {
        if (expr instanceof FloatingPointLiteral) {
            return new FloatingPointLiteral(qs, value.subtract(((FloatingPointLiteral) expr).value));
        } else {
            return super.sub(expr);
        }
    }

    public ScalarExpression mul(ScalarExpression expr) {
        if (expr instanceof FloatingPointLiteral) {
            return new FloatingPointLiteral(qs, value.multiply(((FloatingPointLiteral) expr).value));
        } else {
            return super.mul(expr);
        }
    }

    public ScalarExpression div(ScalarExpression expr) {
        if (expr instanceof FloatingPointLiteral) {
            return new FloatingPointLiteral(qs, value.divide(((FloatingPointLiteral) expr).value, BigDecimal.ROUND_DOWN));
        } else {
            return super.mul(expr);
        }
    }

    public ScalarExpression neg() {
        return new FloatingPointLiteral(qs, value.negate());
    }

    /**
     * Method to save a "raw" value that this literal represents.
     * This value differs from the literal value since that is of the same type as this literal.
     * @param val The raw value
     */
    public void setRawValue(Object val) {
        this.rawValue = val;
    }

    /**
     * Accessor for the "raw" value that this literal represents.
     * This value differs from the literal value since that is of the same type as this literal.
     * @return The raw value
     */
    public Object getRawValue() {
        return rawValue;
    }
}
