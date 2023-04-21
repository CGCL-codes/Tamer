package org.databene.benerator.primitive;

import org.databene.benerator.*;
import org.databene.benerator.wrapper.NonNullGeneratorWrapper;
import org.databene.benerator.wrapper.ProductWrapper;
import org.databene.commons.CollectionUtil;
import org.databene.commons.ArrayFormat;
import java.util.Set;
import java.util.TreeSet;

/**
 * Generates unique strings of fixed length.<br/>
 * <br/>
 * Created: 15.11.2007 14:07:49
 * @author Volker Bergmann
 */
public class UniqueFixedLengthStringGenerator extends NonNullGeneratorWrapper<int[], String> {

    public static final Set<Character> DEFAULT_CHAR_SET = CollectionUtil.toSet('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');

    private static final int DEFAULT_LENGTH = 4;

    private static final boolean DEFAULT_ORDERED = false;

    private char[] digitSymbols;

    private int length;

    private boolean ordered;

    public UniqueFixedLengthStringGenerator() {
        this(DEFAULT_CHAR_SET, DEFAULT_LENGTH, DEFAULT_ORDERED);
    }

    public UniqueFixedLengthStringGenerator(Set<Character> chars, int length, boolean ordered) {
        super(null);
        this.digitSymbols = CollectionUtil.toCharArray(new TreeSet<Character>(chars));
        this.length = length;
        this.ordered = ordered;
    }

    public Class<String> getGeneratedType() {
        return String.class;
    }

    @Override
    public synchronized void init(GeneratorContext context) {
        assertNotInitialized();
        if (ordered) setSource(new IncrementalIntsGenerator(digitSymbols.length, length)); else setSource(new UniqueIntsGenerator(digitSymbols.length, length));
        super.init(context);
    }

    public String generate() {
        ProductWrapper<int[]> wrapper = generateFromSource();
        if (wrapper == null) return null;
        int[] ordinals = wrapper.unwrap();
        char[] buffer = new char[length];
        for (int i = 0; i < length; i++) buffer[i] = digitSymbols[ordinals[i]];
        return new String(buffer);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[length=" + length + ", charset=" + ArrayFormat.formatChars(",", digitSymbols) + ']';
    }
}
