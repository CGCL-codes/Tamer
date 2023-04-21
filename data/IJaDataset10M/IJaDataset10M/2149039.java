package de.fhdarmstadt.fbi.dtree.testdata;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;
import de.fhdarmstadt.fbi.dtree.model.Alphabet;
import de.fhdarmstadt.fbi.dtree.model.EmptyAlphabetException;
import de.fhdarmstadt.fbi.dtree.model.IllegalPatternException;
import de.fhdarmstadt.fbi.dtree.model.RegularPattern;
import de.fhdarmstadt.fbi.dtree.model.SimplePattern;

/**
 * The DataGenerator can be used to generate test data for a given
 * <code>SimplePattern</code>. This generator enumerates all possible words and allows
 * indexed access to the generated words.
 * <p/>
 * This generator cannot be used to generate negative matches for a certain pattern.
 *
 * @see SimplePattern
 */
public final class DataGenerator {

    /**
   * A pattern row encapsulates a sequence of words with a specific silbling length.
   */
    private static final class PatternRow {

        /**
     * The sizes of the words.
     */
        private int[] silblingSizes;

        /**
     * The total length (number of characters) of the words.
     */
        private int length;

        /**
     * The number of words in this row.
     */
        private int size;

        /**
     * Creates a new pattern row for the given sequence of variables.
     *
     * @param silblingSizes the size of the variable values.
     */
        public PatternRow(final int[] silblingSizes, final int size) {
            this.silblingSizes = new int[silblingSizes.length];
            this.size = size;
            System.arraycopy(silblingSizes, 0, this.silblingSizes, 0, silblingSizes.length);
            for (int i = 0; i < silblingSizes.length; i++) {
                length += silblingSizes[i];
            }
        }

        /**
     * Returns the number of characters within this word set.
     *
     * @return the accumulated length of the variables.
     */
        public final int getLength() {
            return length;
        }

        /**
     * Returns the number of words generatable with this pattern set.
     *
     * @return the number of words
     */
        public final int getSize() {
            return size;
        }

        /**
     * Returns the length of the given segment.
     *
     * @param segment the segment number.
     * @return the length of the requested segment.
     *
     * @throws IndexOutOfBoundsException if the segment number is invalid
     */
        public final int getSegmentLength(final int segment) {
            return silblingSizes[segment];
        }

        public final int getSegmentCount() {
            return silblingSizes.length;
        }

        /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
        public final String toString() {
            final StringBuffer b = new StringBuffer();
            b.append("PatternRow={");
            for (int i = 0; i < silblingSizes.length; i++) {
                if (i != 0) {
                    b.append(",");
                }
                b.append(silblingSizes[i]);
            }
            b.append("}");
            return b.toString();
        }
    }

    /**
   * The pattern used to compute the testdata.
   */
    private SimplePattern pattern;

    /**
   * The minimum length of the generated patterns.
   */
    private int minLength;

    /**
   * The maximum length of the generated patterns.
   */
    private int maxLength;

    /**
   * The number of characters of the constants.
   */
    private int constantLength;

    /**
   * The maximum number of variable characters in the patterns.
   */
    private int variableLength;

    /**
   * The alphabet as array, no duplicates.
   */
    private Character[] alphabet;

    /**
   * The ordered storage for the PatternRows.
   */
    private TreeMap map;

    /**
   * The keys of the storage map.
   */
    private Long[] mapKeys;

    /**
   * The maximum number of generatable patterns.
   */
    private long maxNumberOfPattern;

    private static Random randomNumberGenerator;

    /**
   * Creates a new DataGenerator for the given simple pattern. The pattern is cloned
   * during the construction process to make it immutable.
   *
   * @param pattern  the pattern, never null
   * @param alphabet the alphabet
   * @throws NullPointerException     if either the alphabet or the pattern is null
   * @throws IllegalArgumentException if the alphabet is invalid.
   */
    public DataGenerator(final SimplePattern pattern, final Alphabet alphabet) throws EmptyAlphabetException, IllegalPatternException {
        if (pattern == null) {
            throw new NullPointerException("Pattern must not be null");
        }
        if (pattern.size() == 0) {
            throw new IllegalArgumentException("Pattern must not be empty");
        }
        if (alphabet == null) {
            throw new NullPointerException("Alphabet must not be null");
        }
        if (alphabet.isEmpty()) {
            throw new EmptyAlphabetException("Alphabet must be valid.");
        }
        this.alphabet = alphabet.toArray();
        this.pattern = (SimplePattern) pattern.clone();
        constantLength = 0;
        for (int i = 0; i < pattern.size(); i++) {
            final String constant = pattern.getConstant(i);
            if (alphabet.isValid(constant) == false) {
                throw new IllegalPatternException("Invalid pattern: No match with alphabet [" + pattern.getConstant(i) + "]", findIllegalCharacter(pattern.getConstant(i), alphabet));
            }
            constantLength += constant.length();
        }
        minLength = constantLength;
        maxLength = constantLength;
        map = new TreeMap();
    }

    /**
   * Searches the given word for the first illegal character. This is a helper method used
   * to provide more detailed exception messages.
   *
   * @param constant the constant that caused the error
   * @param alphabet the alphabet to match the word against
   * @return the character.
   */
    private static Character findIllegalCharacter(final String constant, final Alphabet alphabet) {
        final char[] chars = constant.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            final Character c = new Character(chars[i]);
            if (alphabet.contains(c) == false) {
                return c;
            }
        }
        return null;
    }

    /**
   * Returns the regular pattern used in the data generator.
   *
   * @return the pattern used, never null.
   */
    public final RegularPattern getPattern() {
        return pattern;
    }

    /**
   * Returns the maximum length of the generated words.
   *
   * @return the maximum length
   */
    public final int getMaxLength() {
        return maxLength;
    }

    /**
   * Defines the maximum length of the generated words. Setting this value triggers the
   * word recomputation.
   *
   * @param maxLength the maximum length of the generated word
   * @throws IllegalArgumentException if the given length is lower than the defined
   *                                  minimum length.
   */
    public final void setMaxLength(final int maxLength) {
        if (maxLength < minLength) {
            throw new IllegalArgumentException("Maximum length must be greater or equal to the pattern's minimum length");
        }
        this.maxLength = maxLength;
        this.variableLength = maxLength - constantLength;
        computeWordSilblings();
    }

    /**
   * Returns the minimum length of the generated words.
   *
   * @return the minimum length
   */
    public final int getMinLength() {
        return minLength;
    }

    /**
   * Defines the minimum length of the generated words. Setting this value triggers the
   * word recomputation.
   *
   * @param minLength the minimum length of the generated words
   * @throws IllegalArgumentException if the given length is lower than the length of the
   *                                  constants or greater than the defined maximum
   *                                  length.
   */
    public final void setMinLength(final int minLength) {
        if (minLength < constantLength) {
            throw new IllegalArgumentException("Minimum length must be at least as long as the " + "constant values of the given pattern.");
        }
        if (minLength > maxLength) {
            throw new IllegalArgumentException("Minimum length cannot be greater than the defined " + "maximum length of the generated words.");
        }
        this.minLength = minLength;
        computeWordSilblings();
    }

    public final void setBounds(final int min, final int max) {
        if (max < min) {
            throw new IllegalArgumentException("Maximum length must be greater or equal to the generator's minimum length");
        }
        if (min < constantLength) {
            throw new IllegalArgumentException("Minimum length must be at least as long as the " + "constant values of the pattern.");
        }
        if (min > max) {
            throw new IllegalArgumentException("Minimum length cannot be greater than the defined " + "maximum length of the generated words.");
        }
        this.minLength = min;
        this.maxLength = max;
        this.variableLength = maxLength - constantLength;
        computeWordSilblings();
    }

    /**
   * Returns the alphabet used for the generation process.
   *
   * @return the alphabet as array.
   */
    protected final Character[] getAlphabet() {
        return alphabet;
    }

    /**
   * Returns the size of the constant elements in the used pattern.
   *
   * @return the constant size.
   */
    public final int getConstantLength() {
        return constantLength;
    }

    /**
   * Returns the number of variables in the pattern.
   *
   * @return the number of variables.
   */
    public final int getVariablesCount() {
        return variableLength;
    }

    /**
   * Returns the maximum number of words, which are generatable using the current
   * settings.
   *
   * @return the maximum number of words.
   */
    public final long getMaxNumberOfWords() {
        return maxNumberOfPattern;
    }

    /**
   * Generate the string for the given position.
   *
   * @param pos the position with the enumerated set of results.
   * @return the generated string, never null.
   *
   * @throws IndexOutOfBoundsException if the given position is negative or greater or
   *                                   equal to the maximum number of generatable words.
   */
    public final String generateString(final long pos) {
        return generateString(pos, false);
    }

    /**
   * Generate the string for the given position. Strings generated with identify 'true'
   * are not guaranteed to fit the alphabet. <br> The identify setting converts all
   * variable contents into lower case characters to make variable parts visible.
   *
   * @param pos      the position with the enumerated set of results.
   * @param identify identifies the variable parts of the string. This is a debug setting,
   *                 your results might be screwed if this is set to true on a production
   *                 system.
   * @return the generated string, never null.
   *
   * @throws IndexOutOfBoundsException if the given position is negative or greater or
   *                                   equal to the maximum number of generatable words.
   */
    public final String generateString(final long pos, final boolean identify) {
        if (pos < 0) {
            throw new IndexOutOfBoundsException("Position must not be negative.");
        }
        if (pos >= maxNumberOfPattern) {
            throw new IndexOutOfBoundsException("Position must be lower than the maximum number of words generatable.");
        }
        final Long key = new Long(pos);
        int result = Arrays.binarySearch(mapKeys, key);
        if (result < 0) {
            result = -(result + 2);
            if (result == -1) {
                return "";
            }
        }
        final Long realKey = mapKeys[result];
        final PatternRow row = (PatternRow) map.get(realKey);
        final char[] data = new char[row.getLength()];
        internalGenerateString((int) (pos - realKey.longValue()), data, 0);
        if (identify) {
            for (int i = 0; i < data.length; i++) {
                data[i] = Character.toLowerCase(data[i]);
            }
        }
        final StringBuffer b = new StringBuffer(row.getLength() + getConstantLength());
        int segmentStart = 0;
        for (int i = 0; i < pattern.size(); i++) {
            if (i != 0) {
                final int l = row.getSegmentLength(i - 1);
                b.append(data, segmentStart, l);
                segmentStart += l;
            }
            b.append(pattern.getConstant(i));
        }
        return b.toString();
    }

    /**
   * Internal generator method. Generates the result instance for the given position
   * within the set of variable lengths.
   *
   * @param wordPos the enumerated position within the current pattern.
   * @param data    the generated string.
   * @param charPos currently generated position (recursion helper).
   */
    private void internalGenerateString(final int wordPos, final char[] data, final int charPos) {
        if (wordPos < 0) {
            throw new IndexOutOfBoundsException("WordPos is negative: " + wordPos);
        }
        if (charPos < data.length) {
            final int charIdx = (wordPos % alphabet.length);
            data[charPos] = alphabet[charIdx].charValue();
            internalGenerateString(wordPos / alphabet.length, data, charPos + 1);
        }
    }

    /**
   * Computes the number of words for the given maximum and minimum length and computes
   * the starting positions for the pattern rows.
   */
    private void computeWordSilblings() {
        map.clear();
        mapKeys = null;
        final int[] positions = new int[pattern.size() - 1];
        maxNumberOfPattern = generatePatternLevel(0, 0, positions, 0);
        mapKeys = (Long[]) map.keySet().toArray(new Long[map.size()]);
    }

    /**
   * Generates an enumeration of all word sequences. This method enumerates the result
   * words and collects the starting positions for all pattern rows.
   * <p/>
   * A pattern row encapsulates all words with a certain segment length.
   *
   * @param count     the currently known number of patterns.
   * @param usedChars the number of chars used so far.
   * @param positions the collected segement sizes
   * @param level     the current segment, which should be computed.
   * @return the total number of words generated by the current row.
   */
    private long generatePatternLevel(final long count, final int usedChars, final int[] positions, final int level) {
        if (level == (positions.length)) {
            final int minUsed = (minLength - constantLength);
            if (usedChars < minUsed) {
                return count;
            } else {
                final int size = (int) Math.pow(alphabet.length, usedChars);
                final PatternRow row = new PatternRow(positions, size);
                map.put(new Long(count), row);
                return size + count;
            }
        } else {
            final int maxSize = variableLength - usedChars;
            long newcount = count;
            for (int i = 0; i <= maxSize; i++) {
                positions[level] = i;
                newcount = generatePatternLevel(newcount, usedChars + i, positions, level + 1);
            }
            return newcount;
        }
    }

    /**
   * Returns a true random number. Depending on the installed cryptography extensions,
   * this implementation might use a secure randomizer instead of the plain insecure one.
   *
   * @param max the maximum number.
   * @return an int value in the range from 0 to max.
   */
    private long getRandomNumber(final long max) {
        if (max == 0) {
            throw new IllegalArgumentException("Max must be greater or equal to one");
        }
        if (randomNumberGenerator == null) {
            try {
                randomNumberGenerator = SecureRandom.getInstance("SHA1PRNG");
            } catch (Exception e) {
                randomNumberGenerator = new Random();
            }
        }
        final double chunkRandom = randomNumberGenerator.nextDouble();
        return (long) Math.floor(max * chunkRandom);
    }

    /**
   * Returns a true random number. Depending on the installed cryptography extensions,
   * this implementation might use a secure randomizer instead of the plain insecure one.
   *
   * @return a random number.
   */
    private long getRandomNumber() {
        return getRandomNumber(getMaxNumberOfWords());
    }

    public final String[] getRandomSetWithDuplicates(final long count) {
        if (count <= 0) {
            throw new IndexOutOfBoundsException("Count must be a positive integer");
        }
        if (count > getMaxNumberOfWords()) {
            throw new IndexOutOfBoundsException("Not enough words.");
        }
        final ArrayList result = new ArrayList();
        for (int i = 0; i < count; i++) {
            final long position = getRandomNumber();
            result.add(generateString(position, false));
        }
        return (String[]) result.toArray(new String[result.size()]);
    }

    public final String[] getRandomSet(final long count) {
        if (count <= 0) {
            throw new IndexOutOfBoundsException("Count must be a positive integer");
        }
        if (count > getMaxNumberOfWords()) {
            throw new IndexOutOfBoundsException("Not enough words.");
        }
        final NumberSequence sequence = new NumberSequence(getMaxNumberOfWords());
        final TreeSet result = new TreeSet();
        int i = 0;
        while ((i < count) && (sequence.size() > 0)) {
            final long idx = getRandomNumber(sequence.size());
            final long position = sequence.getPositionForIndex(idx);
            if (result.add(generateString(position, false))) {
                i += 1;
            }
            if (sequence.drawPosition(position) == false) {
                throw new IllegalStateException("Duplicate draw() in Sequence");
            }
        }
        return (String[]) result.toArray(new String[result.size()]);
    }
}
