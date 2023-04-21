package org.databene.benerator.primitive.number;

import org.databene.commons.converter.NumberToNumberConverter;
import java.math.BigInteger;

/**
 * Wrapper for a LongGenerator that maps the generated Longs to BigIntegers.<br/>
 * <br/>
 * Created: 07.06.2006 19:04:08
 * @author Volker Bergmann
 */
public abstract class AbstractBigIntegerGenerator extends AbstractNonNullNumberGenerator<BigInteger> {

    private static final BigInteger DEFAULT_MIN = new BigInteger(new byte[] { (byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 });

    private static final BigInteger DEFAULT_MAX = new BigInteger(new byte[] { (byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff });

    /** Initializes the generator to create uniformly distributed random BigIntegers with granularity 1 */
    public AbstractBigIntegerGenerator() {
        this(DEFAULT_MIN, DEFAULT_MAX);
    }

    /** Initializes the generator to create uniformly distributed random BigIntegers with granularity 1 */
    public AbstractBigIntegerGenerator(BigInteger min, BigInteger max) {
        this(min, max, NumberToNumberConverter.convert(1, BigInteger.class));
    }

    /** Initializes the generator to create uniformly distributed random BigIntegers with the specified granularity */
    public AbstractBigIntegerGenerator(BigInteger min, BigInteger max, BigInteger granularity) {
        super(BigInteger.class, min, max, granularity);
    }
}
