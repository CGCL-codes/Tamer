package ar.edu.unq.yaqc4j.generators.java.lang;

import ar.edu.unq.yaqc4j.generators.Gen;
import ar.edu.unq.yaqc4j.randoms.Distribution;

/**
 * Default Float random generator.
 * 
 * @author Pablo
 * 
 */
public final class FloatGen implements Gen<Float> {

    /**
     * the INT_GEN.
     */
    private static final Gen<Integer> INT_GEN = new IntegerGen();

    /**
     * @{inheritDoc
     */
    public Float arbitrary(final Distribution random, final long minsize, final long maxsize) {
        float a = (float) INT_GEN.arbitrary(random, minsize, maxsize);
        float b = (float) INT_GEN.arbitrary(random, minsize, maxsize);
        float c = (float) Math.abs(INT_GEN.arbitrary(random, minsize, maxsize));
        return a + (b / (c + 1.0f));
    }
}
