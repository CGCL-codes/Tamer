package org.databene.benerator.wrapper;

import org.databene.benerator.Generator;
import org.databene.benerator.GeneratorContext;
import org.databene.benerator.InvalidGeneratorSetupException;
import org.databene.benerator.distribution.Distribution;
import org.databene.benerator.distribution.SequenceManager;

/**
 * This forwards a source generator's products.
 * Iterates through the products of another generator with a variable step width.
 * This is intended mainly for use with importing generators that provide data
 * volumes too big to keep in RAM.<br/>
 * <br/>
 * Created: 26.08.2006 16:16:04
 * @since 0.1
 * @author Volker Bergmann
 */
public class SkipGeneratorProxy<E> extends CardinalGenerator<E, E> {

    public static final int DEFAULT_MIN_INCREMENT = 1;

    public static final int DEFAULT_MAX_INCREMENT = 1;

    private int minIncrement;

    private int maxIncrement;

    private int count;

    private Integer limit;

    public SkipGeneratorProxy() {
        this(null);
    }

    /** Initializes the generator to iterate with increment 1 */
    public SkipGeneratorProxy(Generator<E> source) {
        this(source, DEFAULT_MIN_INCREMENT, DEFAULT_MAX_INCREMENT);
    }

    public SkipGeneratorProxy(Integer minIncrement, Integer maxIncrement) {
        this(null, minIncrement, maxIncrement);
    }

    /** Initializes the generator to use a random increment of uniform distribution */
    public SkipGeneratorProxy(Generator<E> source, Integer minIncrement, Integer maxIncrement) {
        this(source, minIncrement, maxIncrement, SequenceManager.RANDOM_SEQUENCE, null);
    }

    /** Initializes the generator to use a random increment of uniform distribution */
    public SkipGeneratorProxy(Generator<E> source, int minIncrement, int maxIncrement, Distribution incrementDistribution, Integer limit) {
        super(source, false, minIncrement, maxIncrement, 1, incrementDistribution);
        this.minIncrement = minIncrement;
        this.maxIncrement = maxIncrement;
        this.count = 0;
        this.limit = limit;
    }

    public Class<E> getGeneratedType() {
        return getSource().getGeneratedType();
    }

    @Override
    public void init(GeneratorContext context) {
        if (minIncrement < 0) throw new InvalidGeneratorSetupException("minIncrement is less than zero");
        if (maxIncrement < 0) throw new InvalidGeneratorSetupException("maxIncrement is less than zero");
        if (minIncrement > maxIncrement) throw new InvalidGeneratorSetupException("minIncrement (" + minIncrement + ") is larger than maxIncrement (" + maxIncrement + ")");
        super.init(context);
    }

    public ProductWrapper<E> generate(ProductWrapper<E> wrapper) {
        Integer increment = generateCardinal();
        if (increment == null) return null;
        for (long i = 0; i < increment - 1; i++) generateFromSource();
        count += increment;
        if (limit != null && count > limit) return null;
        return getSource().generate(wrapper);
    }

    @Override
    public void reset() {
        super.reset();
        count = 0;
    }
}
