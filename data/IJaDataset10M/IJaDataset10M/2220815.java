package org.openexi.schema;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * XSDecimal is a type that encapsulates the decimal type available on the
 * platform.
 */
public final class XSDecimal {

    private static final int DECIMAL_MODE_MAYBE_SIGN = 0;

    private static final int DECIMAL_MODE_MAYBE_INTEGRAL = 1;

    private static final int DECIMAL_MODE_IS_INTEGRAL = 2;

    private static final int DECIMAL_MODE_IS_FRACTION = 3;

    private static final int DECIMAL_MODE_MAYBE_TRAILING_ZEROS = 4;

    private static final BigInteger BINT10 = BigInteger.valueOf(10);

    private final int m_trailingZeros;

    private final BigDecimal m_value;

    /**
   * Validate a string against XML Schema decimal type to return
   * an XSDecimal instance upon success.
   * @param norm string to be parsed
   * @return an XSDecimal instance upon success
   */
    public static XSDecimal parse(String norm) throws SchemaValidatorException {
        int digits = 0;
        int totalDigits = 0, fractionDigits = 0;
        int trailingZeros = 0;
        BigInteger bint = BigInteger.valueOf(0);
        boolean positive = true;
        boolean syntaxInvalid = false;
        int pos, len, mode;
        for (pos = 0, len = norm.length(), mode = DECIMAL_MODE_MAYBE_SIGN; pos < len && !syntaxInvalid; pos++) {
            final char c = norm.charAt(pos);
            switch(mode) {
                case DECIMAL_MODE_MAYBE_SIGN:
                    if (c == '-' || c == '+') {
                        mode = DECIMAL_MODE_MAYBE_INTEGRAL;
                        if (c != '+') positive = false;
                    } else if (c == '.') {
                        mode = DECIMAL_MODE_IS_FRACTION;
                    } else if (c >= '0' && c <= '9') {
                        mode = DECIMAL_MODE_IS_INTEGRAL;
                        ++digits;
                        if (c != '0') ++totalDigits;
                        bint = bint.multiply(BINT10).add(BigInteger.valueOf(c - '0'));
                    } else syntaxInvalid = true;
                    break;
                case DECIMAL_MODE_MAYBE_INTEGRAL:
                case DECIMAL_MODE_IS_INTEGRAL:
                    if (c == '.') mode = DECIMAL_MODE_IS_FRACTION; else if (c >= '0' && c <= '9') {
                        mode = DECIMAL_MODE_IS_INTEGRAL;
                        ++digits;
                        if (totalDigits > 0 || c != '0') ++totalDigits;
                        bint = bint.multiply(BINT10).add(BigInteger.valueOf(c - '0'));
                    } else syntaxInvalid = true;
                    break;
                case DECIMAL_MODE_IS_FRACTION:
                    if (c == '0') {
                        ++trailingZeros;
                        mode = DECIMAL_MODE_MAYBE_TRAILING_ZEROS;
                    } else {
                        if (c >= '1' && c <= '9') {
                            ++digits;
                            ++fractionDigits;
                            bint = bint.multiply(BINT10).add(BigInteger.valueOf(c - '0'));
                        } else {
                            syntaxInvalid = true;
                        }
                    }
                    break;
                case DECIMAL_MODE_MAYBE_TRAILING_ZEROS:
                    assert trailingZeros > 0;
                    if (c == '0') ++trailingZeros; else {
                        if (c >= '1' && c <= '9') {
                            digits += trailingZeros;
                            fractionDigits += trailingZeros;
                            for (int i = 0; i < trailingZeros; i++) bint = bint.multiply(BINT10);
                            ++digits;
                            ++fractionDigits;
                            bint = bint.multiply(BINT10).add(BigInteger.valueOf(c - '0'));
                            trailingZeros = 0;
                            mode = DECIMAL_MODE_IS_FRACTION;
                        } else syntaxInvalid = true;
                    }
                    break;
            }
        }
        if (!syntaxInvalid) {
            digits += trailingZeros;
            if (mode == DECIMAL_MODE_MAYBE_TRAILING_ZEROS) {
                mode = DECIMAL_MODE_IS_FRACTION;
            }
            if (mode == DECIMAL_MODE_IS_FRACTION && fractionDigits == 0) {
                if (trailingZeros == 0) trailingZeros = 1;
            }
            totalDigits += fractionDigits;
        }
        if (syntaxInvalid || mode != DECIMAL_MODE_IS_INTEGRAL && mode != DECIMAL_MODE_IS_FRACTION) throw new SchemaValidatorException(SchemaValidatorException.INVALID_DECIMAL, new String[] { norm }, (EXISchema) null, EXISchema.NIL_NODE); else if (digits == 0) throw new SchemaValidatorException(SchemaValidatorException.INVALID_DECIMAL, new String[] { norm }, (EXISchema) null, EXISchema.NIL_NODE);
        if (!positive) bint = bint.negate();
        return new XSDecimal(new BigDecimal(bint, fractionDigits), trailingZeros);
    }

    public XSDecimal(BigDecimal val) {
        this(val, 0);
    }

    public XSDecimal(BigDecimal val, int trailingZeros) {
        m_value = val;
        m_trailingZeros = trailingZeros;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof XSDecimal) {
            if ((m_value.compareTo(((XSDecimal) obj).m_value) == 0)) return true;
        }
        return false;
    }

    /**
   * Retrieve decimal value.
   */
    public BigDecimal getValue() {
        return m_value;
    }

    public int getFractionDigits() {
        return m_value.scale();
    }

    public int getTrailingZeros() {
        return m_trailingZeros;
    }

    BigInteger getIntegralValue() {
        return m_value.movePointRight(m_value.scale()).abs().toBigInteger();
    }
}
