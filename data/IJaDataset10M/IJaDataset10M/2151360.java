package uk.co.wilson.ng.runtime.metaclass.primitives.biginteger;

import java.math.BigDecimal;
import java.math.BigInteger;
import ng.runtime.metaclass.Conversion;
import ng.runtime.metaclass.primitives.biginteger.BigIntegerConversion;
import ng.runtime.threadcontext.ExtendedThreadContext;
import ng.runtime.threadcontext.NotPerformed;
import uk.co.wilson.ng.runtime.metaclass.primitives.ConversionWrapper;

/**
 * @author John
 * 
 */
public class BigIntegerConversionWrapper extends ConversionWrapper implements BigIntegerConversion {

    /**
   * @param delegate
   */
    public BigIntegerConversionWrapper(final Conversion delegate) {
        super(delegate);
    }

    public BigDecimal doAsBigDecimal(final ExtendedThreadContext tc, final BigInteger value) throws NotPerformed {
        return doAsBigDecimal(tc, (Object) value);
    }

    public boolean doAsBoolean(final ExtendedThreadContext tc, final BigInteger value) throws NotPerformed {
        return doAsBoolean(tc, (Object) value);
    }

    public byte doAsByte(final ExtendedThreadContext tc, final BigInteger value) throws NotPerformed {
        return doAsByte(tc, (Object) value);
    }

    public char doAsChar(final ExtendedThreadContext tc, final BigInteger value) throws NotPerformed {
        return doAsChar(tc, (Object) value);
    }

    public double doAsDouble(final ExtendedThreadContext tc, final BigInteger value) throws NotPerformed {
        return doAsDouble(tc, (Object) value);
    }

    public float doAsFloat(final ExtendedThreadContext tc, final BigInteger value) throws NotPerformed {
        return doAsFloat(tc, (Object) value);
    }

    public int doAsInt(final ExtendedThreadContext tc, final BigInteger value) throws NotPerformed {
        return doAsInt(tc, (Object) value);
    }

    public long doAsLong(final ExtendedThreadContext tc, final BigInteger value) throws NotPerformed {
        return doAsLong(tc, (Object) value);
    }

    public short doAsShort(final ExtendedThreadContext tc, final BigInteger value) throws NotPerformed {
        return doAsShort(tc, (Object) value);
    }

    public String doAsString(final ExtendedThreadContext tc, final BigInteger value) throws NotPerformed {
        return doAsString(tc, (Object) value);
    }
}
