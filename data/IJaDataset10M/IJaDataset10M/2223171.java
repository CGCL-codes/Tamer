package org.databene.benerator.primitive;

import org.databene.benerator.Generator;
import org.databene.benerator.GeneratorContext;
import org.databene.benerator.InvalidGeneratorSetupException;
import org.databene.benerator.factory.GeneratorFactory;
import org.databene.benerator.factory.StochasticGeneratorFactory;
import org.databene.benerator.wrapper.NonNullGeneratorProxy;
import org.databene.model.data.Uniqueness;
import java.util.Locale;

/**
 * Generates Strings that comply to a regular expression.<br/>
 * <br/>
 * Created: 18.07.2006 19:32:52
 * @since 0.1
 * @author Volker Bergmann
 */
public class RegexStringGenerator extends NonNullGeneratorProxy<String> {

    /** Optional String representation of a regular expression */
    private String pattern;

    private boolean unique;

    private boolean ordered;

    /** The locale from which to choose letters */
    private Locale locale;

    private int minLength;

    private int maxLength;

    /** Initializes the generator to an empty regular expression, a maxQuantity of 30 and the fallback locale */
    public RegexStringGenerator() {
        this(30);
    }

    /** Initializes the generator to an empty regular expression and the fallback locale */
    public RegexStringGenerator(int maxLength) {
        this((String) null, maxLength);
    }

    /** Initializes the generator to a maxQuantity of 30 and the fallback locale */
    public RegexStringGenerator(String pattern) {
        this(pattern, 30);
    }

    /** Initializes the generator to the fallback locale */
    public RegexStringGenerator(String pattern, int maxLength) {
        this(pattern, maxLength, false);
    }

    /** Initializes the generator with the String representation of a regular expression */
    public RegexStringGenerator(String pattern, Integer maxLength, boolean unique) {
        super(String.class);
        this.pattern = pattern;
        this.maxLength = maxLength;
        this.unique = unique;
        this.ordered = false;
    }

    /** Sets the String representation of the regular expression */
    public String getPattern() {
        return pattern;
    }

    /** Returns the String representation of the regular expression */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    /** ensures consistency of the generators state */
    @Override
    public void init(GeneratorContext context) {
        Generator<String> tmp = getGeneratorFactory(context).createRegexStringGenerator(pattern, minLength, maxLength, Uniqueness.instance(unique, ordered));
        try {
            setSource(tmp);
            super.init(context);
        } catch (Exception e) {
            throw new InvalidGeneratorSetupException("Illegal regular expression: ", e);
        }
    }

    protected GeneratorFactory getGeneratorFactory(GeneratorContext context) {
        return (context != null ? context.getGeneratorFactory() : new StochasticGeneratorFactory());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + (unique ? "unique '" : "'") + pattern + "']";
    }
}
