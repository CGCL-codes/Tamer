package org.databene.benerator.distribution.sequence;

import org.databene.benerator.GeneratorContext;
import org.databene.benerator.InvalidGeneratorSetupException;
import org.databene.benerator.PropertyMessage;
import org.databene.benerator.primitive.number.AbstractNonNullNumberGenerator;
import org.databene.benerator.util.RandomUtil;

/**
 * Long Generator that implements a 'random' Long Sequence.<br/>
 * <br/>
 * Created: 03.09.2006 09:53:01
 * @author Volker Bergmann
 */
public class RandomLongGenerator extends AbstractNonNullNumberGenerator<Long> {

    public static final long DEFAULT_MIN = Long.MIN_VALUE / 2 + 1;

    public static final long DEFAULT_MAX = Long.MAX_VALUE / 2 - 1;

    private static final long DEFAULT_GRANULARITY = 1;

    public RandomLongGenerator() {
        this(DEFAULT_MIN, DEFAULT_MAX);
    }

    public RandomLongGenerator(long min, Long max) {
        this(min, max, DEFAULT_GRANULARITY);
    }

    public RandomLongGenerator(long min, Long max, long granularity) {
        super(Long.class, min, max, granularity);
    }

    @Override
    public void init(GeneratorContext context) {
        if (granularity == 0L) throw new InvalidGeneratorSetupException(getClass().getSimpleName() + ".granularity may not be 0");
        if (min > max) throw new InvalidGeneratorSetupException(new PropertyMessage("min", "greater than max"), new PropertyMessage("max", "less than min"));
        super.init(context);
    }

    @Override
    public synchronized Long generate() {
        return generate(min, max, granularity);
    }

    public static long generate(long min, long max, long granularity) {
        if (min == max) return min;
        long range = (max - min) / granularity;
        return min + RandomUtil.randomLong(0, range) * granularity;
    }
}
