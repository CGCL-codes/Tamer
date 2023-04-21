package org.databene.benerator.sample;

import org.databene.benerator.GeneratorContext;
import org.databene.benerator.InvalidGeneratorSetupException;
import org.databene.benerator.NonNullGenerator;
import org.databene.benerator.distribution.Distribution;
import org.databene.benerator.distribution.SequenceManager;
import org.databene.benerator.util.RandomUtil;
import org.databene.benerator.wrapper.ProductWrapper;
import org.databene.commons.ConfigurationError;
import java.util.List;
import java.util.ArrayList;

/**
 * Generates values from a non-weighted list of samples, applying an explicitly defined distribution.<br/>
 * <br/>
 * Created: 07.06.2006 19:04:08
 * @since 0.1
 * @author Volker Bergmann
 */
public class SampleGenerator<E> extends AbstractSampleGenerator<E> {

    /** Keeps the Sample information */
    private List<E> samples = new ArrayList<E>();

    /** Sequence for choosing a List index of the sample list */
    private Distribution distribution = null;

    /** Sequence for choosing a List index of the sample list */
    private NonNullGenerator<Integer> indexGenerator = null;

    private boolean unique;

    public SampleGenerator() {
        this(null);
    }

    /** Initializes the generator to an empty sample list */
    public SampleGenerator(Class<E> generatedType) {
        this(generatedType, new ArrayList<E>());
    }

    /** Initializes the generator to a sample list */
    public SampleGenerator(Class<E> generatedType, E... values) {
        super(generatedType);
        setValues(values);
        this.distribution = SequenceManager.RANDOM_SEQUENCE;
    }

    /** Initializes the generator to a sample list */
    public SampleGenerator(Class<E> generatedType, Distribution distribution, E... values) {
        super(generatedType);
        this.distribution = distribution;
        setValues(values);
    }

    /** Initializes the generator to a sample list */
    public SampleGenerator(Class<E> generatedType, Iterable<E> values) {
        super(generatedType);
        setValues(values);
        this.distribution = SequenceManager.RANDOM_SEQUENCE;
    }

    /** Initializes the generator to a sample list */
    public SampleGenerator(Class<E> generatedType, Distribution distribution, boolean unique, Iterable<E> values) {
        super(generatedType);
        this.distribution = distribution;
        this.unique = unique;
        setValues(values);
    }

    /** Adds a value to the sample list */
    @Override
    public <T extends E> void addValue(T value) {
        if (unique && this.contains(value)) throw new ConfigurationError("Trying to add a duplicate value (" + value + ") " + "to unique generator: " + this);
        samples.add(value);
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public <T extends E> boolean contains(E value) {
        return samples.contains(value);
    }

    @Override
    public void clear() {
        this.samples.clear();
    }

    @Override
    public long getVariety() {
        return samples.size();
    }

    /** Initializes all attributes */
    @Override
    public void init(GeneratorContext context) {
        assertNotInitialized();
        if (samples.size() == 0) throw new InvalidGeneratorSetupException("No samples defined in " + this); else {
            indexGenerator = distribution.createNumberGenerator(Integer.class, 0, samples.size() - 1, 1, unique);
            indexGenerator.init(context);
        }
        super.init(context);
    }

    public ProductWrapper<E> generate(ProductWrapper<E> wrapper) {
        assertInitialized();
        Integer index;
        if (samples.size() > 0 && (index = indexGenerator.generate()) != null) return wrapper.wrap(samples.get(index)); else return null;
    }

    @Override
    public void reset() {
        indexGenerator.reset();
        super.reset();
    }

    @Override
    public void close() {
        indexGenerator.close();
        super.close();
    }

    /** Convenience utility method that chooses one sample out of a list with uniform random distribution */
    public static <T> T generate(T... samples) {
        return samples[RandomUtil.randomInt(0, samples.length - 1)];
    }

    /** Convenience utility method that chooses one sample out of a list with uniform random distribution */
    public static <T> T generate(List<T> samples) {
        return samples.get(RandomUtil.randomInt(0, samples.size() - 1));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
