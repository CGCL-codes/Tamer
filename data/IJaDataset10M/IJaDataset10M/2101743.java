package org.wwweeeportal.util;

import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.springframework.core.convert.converter.*;
import org.wwweeeportal.util.convert.*;

/**
 * String Utilities.
 */
public abstract class StringUtil {

    /**
   * A {@link NoOpConverter} for String values.
   * 
   * @see NoOpConverter
   */
    public static final Converter<String, String> STRING_NO_OP_CONVERTER = new NoOpConverter<String>(null, String.class);

    /**
   * A {@link ObjectToStringConverter} for Object values.
   * 
   * @see ObjectToStringConverter
   */
    public static final Converter<Object, String> OBJECT_STRING_CONVERTER = new ObjectToStringConverter<Object>();

    /**
   * A {@link Converter} which will {@linkplain String#trim() trim} it's input String.
   * 
   * @see String#trim()
   * @see #mkNull(String, boolean)
   */
    public static final Converter<String, String> STRING_TRIM_CONVERTER = new AbstractConverter<String, String>() {

        @Override
        protected String convertImpl(final String input) throws Exception {
            return mkNull(input, true);
        }
    };

    /**
   * A {@link Converter} which will {@linkplain Boolean#valueOf(String) parse} a {@link Boolean} from a String.
   * 
   * @see Boolean#valueOf(String)
   */
    public static final Converter<String, Boolean> STRING_BOOLEAN_CONVERTER = new AbstractConverter<String, Boolean>() {

        @Override
        protected Boolean convertImpl(final String input) throws Exception {
            return Boolean.valueOf(input);
        }
    };

    /**
   * A {@link Converter} which will {@linkplain Integer#valueOf(String) parse} a {@link Integer} from a String.
   * 
   * @see Integer#valueOf(String)
   */
    public static final Converter<String, Integer> STRING_INTEGER_CONVERTER = new AbstractConverter<String, Integer>() {

        @Override
        protected Integer convertImpl(final String input) throws Exception {
            return Integer.valueOf(input);
        }
    };

    /**
   * A {@link Converter} which will {@linkplain Long#valueOf(String) parse} a {@link Long} from a String.
   * 
   * @see Long#valueOf(String)
   */
    public static final Converter<String, Long> STRING_LONG_CONVERTER = new AbstractConverter<String, Long>() {

        @Override
        protected Long convertImpl(final String input) throws Exception {
            return Long.valueOf(input);
        }
    };

    /**
   * A {@link Converter} which will {@linkplain Float#valueOf(String) parse} a {@link Float} from a String.
   * 
   * @see Float#valueOf(String)
   */
    public static final Converter<String, Float> STRING_FLOAT_CONVERTER = new AbstractConverter<String, Float>() {

        @Override
        protected Float convertImpl(final String input) throws Exception {
            return Float.valueOf(input);
        }
    };

    /**
   * A {@link Converter} which will {@linkplain Double#valueOf(String) parse} a {@link Double} from a String.
   * 
   * @see Double#valueOf(String)
   */
    public static final Converter<String, Double> STRING_DOUBLE_CONVERTER = new AbstractConverter<String, Double>() {

        @Override
        protected Double convertImpl(final String input) throws Exception {
            return Double.valueOf(input);
        }
    };

    /**
   * A {@link Converter} which will {@linkplain Double#valueOf(String) parse} a {@link Double} from a String.
   * 
   * @see Double#valueOf(String)
   */
    public static final Converter<String, Character> STRING_CHARACTER_CONVERTER = new AbstractConverter<String, Character>() {

        @Override
        protected Character convertImpl(final String input) throws Exception {
            if (input.isEmpty()) return null;
            return Character.valueOf(input.charAt(0));
        }
    };

    /**
   * A {@link Converter} which will {@linkplain Pattern#compile(String, int) compile} a {@link Pattern} from a String.
   * 
   * @see StringPatternConverter
   */
    public static final Converter<String, Pattern> STRING_PATTERN_CONVERTER = new StringPatternConverter(0);

    /**
   * A {@link Converter} which will {@linkplain Pattern#compile(String, int) compile} a
   * {@linkplain Pattern#CASE_INSENSITIVE case insensitive} {@link Pattern} from a String.
   * 
   * @see StringPatternConverter
   */
    public static final Converter<String, Pattern> STRING_PATTERN_CASE_INSENSITIVE_CONVERTER = new StringPatternConverter(Pattern.CASE_INSENSITIVE);

    /**
   * A {@link Converter} which will {@linkplain Pattern#quote(String) quote} a {@link Pattern} String.
   * 
   * @see Pattern#quote(String)
   */
    public static final Converter<String, String> PATTERN_QUOTE_CONVERTER = new AbstractConverter<String, String>() {

        @Override
        protected String convertImpl(final String input) throws Exception {
            return Pattern.quote(input);
        }
    };

    /**
   * A {@link Converter} which will create a {@link MessageFormat} from a String.
   * 
   * @see MessageFormat#MessageFormat(String)
   */
    public static final Converter<String, MessageFormat> STRING_MESSAGE_FORMAT_CONVERTER = new AbstractConverter<String, MessageFormat>() {

        @Override
        protected MessageFormat convertImpl(final String input) throws Exception {
            return new MessageFormat(input);
        }
    };

    /**
   * A {@link Converter} which will accept a String containing comma separated values, and return those values split out
   * into an array.
   * 
   * @see StringSplitConverter
   */
    public static final Converter<String, String[]> COMMA_SEPARATED_STRING_SPLIT_CONVERTER = new StringSplitConverter<String>(Pattern.compile("[\\s]*,[\\s]*"), 0);

    /**
   * A {@link Converter} which will encode a List of String components into a single String with each component
   * separated by a dash ('<code>-</code>'). A dash occurring within a component is
   * {@linkplain org.wwweeeportal.util.StringUtil.ComponentsToDelimitedStringConverter.DelimiterEscapeMethod#DUPLICATE
   * duplicated}.
   * 
   * @see ComponentsToDelimitedStringConverter
   * @see #DASH_DELIMITED_STRING_TO_COMPONENTS_CONVERTER
   */
    public static final ComponentsToDelimitedStringConverter COMPONENTS_TO_DASH_DELIMITED_STRING_CONVERTER = new ComponentsToDelimitedStringConverter('-', ComponentsToDelimitedStringConverter.DelimiterEscapeMethod.DUPLICATE);

    /**
   * A {@link Converter} which will decode a List of String components from a single String where each component is
   * separated by a dash ('<code>-</code>'). A dash occurring within a component is
   * {@linkplain org.wwweeeportal.util.StringUtil.ComponentsToDelimitedStringConverter.DelimiterEscapeMethod#DUPLICATE
   * duplicated}.
   * 
   * @see DelimitedStringToComponentsConverter
   * @see #COMPONENTS_TO_DASH_DELIMITED_STRING_CONVERTER
   */
    public static final DelimitedStringToComponentsConverter DASH_DELIMITED_STRING_TO_COMPONENTS_CONVERTER = new DelimitedStringToComponentsConverter('-', ComponentsToDelimitedStringConverter.DelimiterEscapeMethod.DUPLICATE);

    /**
   * A {@link Converter} which will encode a List of String components into a single String with each component
   * separated by a dot ('<code>.</code>'). A dot occurring within a component is
   * {@linkplain org.wwweeeportal.util.StringUtil.ComponentsToDelimitedStringConverter.DelimiterEscapeMethod#BACKSLASH
   * backslashed}.
   * 
   * @see ComponentsToDelimitedStringConverter
   * @see #DOT_DELIMITED_STRING_TO_COMPONENTS_CONVERTER
   */
    public static final ComponentsToDelimitedStringConverter COMPONENTS_TO_DOT_DELIMITED_STRING_CONVERTER = new ComponentsToDelimitedStringConverter('.', ComponentsToDelimitedStringConverter.DelimiterEscapeMethod.BACKSLASH);

    /**
   * A {@link Converter} which will decode a List of String components from a single String where each component is
   * separated by a dot ('<code>.</code>'). A dot occurring within a component is
   * {@linkplain org.wwweeeportal.util.StringUtil.ComponentsToDelimitedStringConverter.DelimiterEscapeMethod#BACKSLASH
   * backslashed}.
   * 
   * @see DelimitedStringToComponentsConverter
   * @see #COMPONENTS_TO_DOT_DELIMITED_STRING_CONVERTER
   */
    public static final DelimitedStringToComponentsConverter DOT_DELIMITED_STRING_TO_COMPONENTS_CONVERTER = new DelimitedStringToComponentsConverter('.', ComponentsToDelimitedStringConverter.DelimiterEscapeMethod.BACKSLASH);

    /**
   * Evaluate two Strings for {@linkplain String#equals(Object) equality}, tolerating <code>null</code> values.
   * 
   * @param string1 The first String
   * @param string2 The second String
   * @param caseSensitive If <code>false</code>, {@linkplain String#equalsIgnoreCase(String) ignore case} in the
   * comparison.
   * @return <code>true</code> if the strings are equal according the the criteria, or both <code>null</code>.
   * @see String#equals(Object)
   */
    public static final boolean equal(final String string1, final String string2, final boolean caseSensitive) {
        if (string1 == null) return (string2 == null); else if (string2 == null) return false; else if (string1 == string2) return true;
        return (caseSensitive) ? string1.equals(string2) : string1.equalsIgnoreCase(string2);
    }

    /**
   * Return the {@linkplain String#trim() trimmed} (optionally) value of the given <code>string</code>, or
   * <code>null</code> if it's {@linkplain String#isEmpty() empty}.
   * 
   * @param string The string to be evaluated.
   * @param trim <code>true</code> if the value should be {@linkplain String#trim() trimmed}.
   * @return The string value, or <code>null</code>.
   * @see String#trim()
   * @see String#isEmpty()
   */
    public static final String mkNull(String string, boolean trim) {
        if (string == null) return null;
        if (trim) string = string.trim();
        if (string.isEmpty()) return null;
        return string;
    }

    /**
   * Get the {@linkplain String#valueOf(Object) string value} of the supplied <code>object</code>.
   * 
   * @param object The object to be converted to a String.
   * @param defaultValue The value to be returned if the supplied <code>object</code> is <code>null</code>.
   * @return The {@linkplain String#valueOf(Object) string value} of the supplied object.
   * @see String#valueOf(Object)
   */
    public static final String toString(final Object object, final String defaultValue) {
        return (object != null) ? String.valueOf(object) : defaultValue;
    }

    /**
   * A {@link Converter} which will {@linkplain Object#toString() convert} the specified input type into a String.
   * 
   * @param <I> The type of object this parser accepts as input.
   * @see Object#toString()
   */
    public static class ObjectToStringConverter<I> extends AbstractConverter<I, String> {

        @Override
        protected String convertImpl(final I input) throws Exception {
            return input.toString();
        }
    }

    ;

    /**
   * A {@link Converter} which can add a prefix and/or suffix String to each input String.
   */
    public static class StringConcatConverter extends DefaultingConverter<String, String> {

        /**
     * The String to be prepended to any input.
     */
        protected final String prefix;

        /**
     * The String to be appended to any input.
     */
        protected final String suffix;

        /**
     * Construct a new <code>StringConcatConverter</code>.
     * 
     * @param convertNullSource {@link #convertNullSource()} result.
     * @param nullSourceResult {@link #getNullSourceResult()} result.
     * @param nullSourceDefault {@link #getNullSourceDefault()} result.
     * @param prefix The String to be prepended to any input.
     * @param suffix The String to be appended to any input.
     */
        public StringConcatConverter(final boolean convertNullSource, final String nullSourceResult, final String nullSourceDefault, final String prefix, final String suffix) {
            super(convertNullSource, nullSourceResult, nullSourceDefault, null);
            this.prefix = mkNull(prefix, false);
            this.suffix = mkNull(suffix, false);
            return;
        }

        @Override
        protected String convertImpl(final String input) throws Exception {
            if ((prefix == null) && (suffix == null)) return input;
            final StringBuffer result = new StringBuffer();
            if (prefix != null) result.append(prefix);
            if (input != null) result.append(input);
            if (suffix != null) result.append(suffix);
            return result.toString();
        }
    }

    ;

    /**
   * A {@link Converter} which will {@linkplain String#substring(int, int) substring} the given input.
   * 
   * @see String#substring(int, int)
   */
    public static class SubstringConverter extends AbstractConverter<String, String> {

        /**
     * The beginning index, inclusive.
     */
        protected final int beginIndex;

        /**
     * The ending index, exclusive.
     */
        protected final int endIndex;

        /**
     * Construct a new <code>SubstringConverter</code>.
     * 
     * @param beginIndex The beginning index, inclusive.
     * @param endIndex The ending index, exclusive, or <code>-1</code> for the rest of the string.
     * @throws IllegalArgumentException If <code>beginIndex</code> is negative.
     */
        public SubstringConverter(final int beginIndex, final int endIndex) throws IllegalArgumentException {
            if (beginIndex < 0) throw new IllegalArgumentException("negative beginIndex");
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
            return;
        }

        @Override
        protected String convertImpl(final String input) throws Exception {
            return (endIndex < 0) ? input.substring(beginIndex) : input.substring(beginIndex, endIndex);
        }
    }

    ;

    /**
   * A {@link Converter} which will {@linkplain Pattern#split(CharSequence, int) split} the given input around matches
   * of a {@link Pattern}.
   * 
   * @param <S> The type of input this converter accepts.
   * @see Pattern#split(CharSequence, int)
   */
    public static class StringSplitConverter<S extends CharSequence> extends AbstractConverter<S, String[]> {

        /**
     * The {@link Pattern} to split on.
     */
        protected final Pattern pattern;

        /**
     * The number of times the pattern will be applied.
     */
        protected final int limit;

        /**
     * Construct a new <code>StringSplitConverter</code>.
     * 
     * @param pattern The {@link Pattern} to split on.
     * @param limit The number of times the pattern will be applied.
     * @throws IllegalArgumentException If <code>pattern</code> is <code>null</code>.
     */
        public StringSplitConverter(final Pattern pattern, final int limit) throws IllegalArgumentException {
            if (pattern == null) throw new IllegalArgumentException("null pattern");
            this.pattern = pattern;
            this.limit = limit;
            return;
        }

        @Override
        protected String[] convertImpl(final S input) throws Exception {
            return pattern.split(input, limit);
        }
    }

    ;

    /**
   * A {@link Converter} which can hex escape select values from within an input String.
   */
    public static class StringHexEscapeConverter extends AbstractConverter<String, String> {

        /**
     * The Pattern identifying values needing to be hex escaped.
     */
        protected final Pattern escapeValues;

        /**
     * The character used within the result to indicate a hex escape sequence follows.
     */
        protected final char escapeChar;

        /**
     * Construct a new <code>StringHexEscapeConverter</code>.
     * 
     * @param escapeValues The Pattern identifying values needing to be hex escaped.
     * @param escapeChar The character used within the result to indicate a hex escape sequence follows.
     * @throws IllegalArgumentException If <code>escapeValues</code> is <code>null</code>.
     */
        public StringHexEscapeConverter(final Pattern escapeValues, final char escapeChar) throws IllegalArgumentException {
            if (escapeValues == null) throw new IllegalArgumentException("null escapeValues");
            this.escapeValues = escapeValues;
            this.escapeChar = escapeChar;
            return;
        }

        /**
     * Create a hex escape sequence from the supplied input <code>value</code>.
     * 
     * @param value The value being escaped.
     * @param escapeChar The character which will be prepended to the result.
     * @param escapedHexLength The number of hex digits to be used in the sequence (the result will be zero padded).
     * @return The hex encoded result String.
     * @throws IllegalArgumentException If <code>escapedHexLength</code> is less than <code>1</code> or if the supplied
     * <code>value</code> is too large to be encoded into a hex string of length <code>escapedHexLength</code>.
     */
        public static final String getHexEscapeString(final long value, final char escapeChar, final int escapedHexLength) throws IllegalArgumentException {
            if (escapedHexLength < 1) throw new IllegalArgumentException("Invalid escapedHexLength");
            final String hs = Long.toHexString(value).toUpperCase();
            if (hs.length() > escapedHexLength) throw new IllegalArgumentException("The value '" + value + "' is too large to be encoded into a " + escapedHexLength + " character hex string");
            final StringBuffer sb = new StringBuffer(escapedHexLength + 1);
            sb.append(escapeChar);
            for (int i = hs.length(); i < escapedHexLength; i++) {
                sb.append('0');
            }
            sb.append(hs);
            return sb.toString();
        }

        @Override
        protected String convertImpl(final String input) throws Exception {
            final Matcher matcher = escapeValues.matcher(input);
            StringBuffer result = new StringBuffer();
            while (matcher.find()) {
                final StringBuffer replacement = new StringBuffer();
                for (int i = matcher.start(); i < matcher.end(); i++) {
                    replacement.append(getHexEscapeString(input.charAt(i), escapeChar, 2));
                }
                matcher.appendReplacement(result, replacement.toString());
            }
            matcher.appendTail(result);
            return result.toString();
        }
    }

    ;

    /**
   * A {@link Converter} which replaces each substring of an input String that matches a literal target sequence with a
   * specified literal replacement sequence.
   * 
   * @see String#replace(CharSequence, CharSequence)
   */
    public static class StringReplaceConverter extends AbstractConverter<String, String> {

        /**
     * The sequence of char values to be replaced.
     */
        protected final CharSequence target;

        /**
     * The replacement sequence of char values.
     */
        protected final CharSequence replacement;

        /**
     * Construct a new <code>StringReplaceConverter</code>.
     * 
     * @param target The sequence of char values to be replaced.
     * @param replacement The replacement sequence of char values.
     * @throws IllegalArgumentException If <code>target</code> or <code>replacement</code> is <code>null</code>.
     */
        public StringReplaceConverter(final CharSequence target, final CharSequence replacement) throws IllegalArgumentException {
            if (target == null) throw new IllegalArgumentException("null target");
            if (replacement == null) throw new IllegalArgumentException("null replacement");
            this.target = target;
            this.replacement = replacement;
            return;
        }

        @Override
        protected String convertImpl(final String input) throws Exception {
            return input.replace(target, replacement);
        }
    }

    ;

    /**
   * A {@link Converter} which will {@linkplain Pattern#compile(String, int) compile} a {@link Pattern} from a String.
   * 
   * @see Pattern#compile(String, int)
   */
    public static class StringPatternConverter extends AbstractConverter<String, Pattern> {

        /**
     * @see Pattern#flags()
     */
        protected final int flags;

        /**
     * Construct a new <code>StringPatternConverter</code>.
     * 
     * @param flags Match {@linkplain Pattern#flags() flags}.
     */
        public StringPatternConverter(final int flags) {
            this.flags = flags;
            return;
        }

        @Override
        protected Pattern convertImpl(final String input) throws Exception {
            return Pattern.compile(input, flags);
        }
    }

    ;

    /**
   * A {@link Converter} designed to encode a list of String components into a single value, delimited by some
   * character.
   * 
   * @see DelimitedStringToComponentsConverter
   */
    public static class ComponentsToDelimitedStringConverter extends AbstractConverter<List<String>, String> {

        /**
     * What method is used to escape delimiters occurring within one of the components?
     */
        public static enum DelimiterEscapeMethod {

            /**
       * Any occurrence of the delimiter within one of the components will be escaped by having the delimiter occur
       * twice in a row at that position. For example, using '-' as the delimiter, and given the input { "hello-there",
       * "world" } the output would be "hello--there-world".
       */
            DUPLICATE, /**
       * Any occurrence of the delimiter within one of the components will be escaped using a backslash ('<code>\</code>
       * ') character. Any occurrence of the backslash character will itself also be escaped. For example, using '.' as
       * the delimiter, and given the input { "c:\hello.txt", "d:\world.txt" } the output would be
       * "c:\\hello\.txt.d:\\world\.txt".
       */
            BACKSLASH
        }

        ;

        /**
     * The character used to delimit components.
     */
        protected final char delimiter;

        /**
     * Used to escape occurrences of the delimiter within each component.
     */
        protected final Converter<String, String> delimiterEscaper;

        /**
     * Construct a new <code>ComponentsToDelimitedStringConverter</code>.
     * 
     * @param delimiter The character used to delimit components.
     * @param delimiterEscapeMethod The {@linkplain DelimiterEscapeMethod method} used to escape delimiters occurring
     * within one of the components.
     */
        public ComponentsToDelimitedStringConverter(final char delimiter, final DelimiterEscapeMethod delimiterEscapeMethod) {
            this.delimiter = delimiter;
            switch(delimiterEscapeMethod) {
                case DUPLICATE:
                    delimiterEscaper = new StringReplaceConverter(String.valueOf(delimiter), String.valueOf(delimiter) + String.valueOf(delimiter));
                    break;
                case BACKSLASH:
                    if (delimiter == '\\') throw new IllegalArgumentException("Cannot use backslash escaping with a backslash delimiter");
                    delimiterEscaper = new ConverterChain<String, String, String>(new StringReplaceConverter(String.valueOf("\\"), "\\\\"), new StringReplaceConverter(String.valueOf(delimiter), "\\" + delimiter));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown DelimiterEscapeMethod: " + delimiterEscapeMethod);
            }
            return;
        }

        /**
     * Get the character used to delimit components.
     * 
     * @return The character used to delimit components.
     */
        public char getDelimiter() {
            return delimiter;
        }

        @Override
        protected String convertImpl(final List<String> components) throws Exception {
            final StringBuffer result = new StringBuffer();
            boolean firstComponent = true;
            for (String component : components) {
                if (component == null) continue;
                final String encodedComponent = ConversionUtil.invokeConverter(delimiterEscaper, component);
                if (!firstComponent) result.append(delimiter);
                result.append(encodedComponent);
                firstComponent = false;
            }
            return result.toString();
        }
    }

    ;

    /**
   * A {@link Converter} designed to decode a list of String components which has been encoded into a single value,
   * delimited by some character.
   * 
   * @see ComponentsToDelimitedStringConverter
   */
    public static class DelimitedStringToComponentsConverter extends AbstractConverter<String, List<String>> {

        /**
     * The character used to delimit components.
     */
        protected final char delimiter;

        /**
     * The {@linkplain org.wwweeeportal.util.StringUtil.ComponentsToDelimitedStringConverter.DelimiterEscapeMethod
     * method} used to escape delimiters occurring within one of the components.
     */
        protected final ComponentsToDelimitedStringConverter.DelimiterEscapeMethod delimiterEscapeMethod;

        /**
     * Construct a new <code>DelimitedStringToComponentsConverter</code>.
     * 
     * @param delimiter The character used to delimit components.
     * @param delimiterEscapeMethod The
     * {@linkplain org.wwweeeportal.util.StringUtil.ComponentsToDelimitedStringConverter.DelimiterEscapeMethod method}
     * used to escape delimiters occurring within one of the components.
     */
        public DelimitedStringToComponentsConverter(final char delimiter, final ComponentsToDelimitedStringConverter.DelimiterEscapeMethod delimiterEscapeMethod) {
            this.delimiter = delimiter;
            this.delimiterEscapeMethod = delimiterEscapeMethod;
            switch(delimiterEscapeMethod) {
                case DUPLICATE:
                    break;
                case BACKSLASH:
                    if (delimiter == '\\') throw new IllegalArgumentException("Cannot use backslash escaping with a backslash delimiter");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown DelimiterEscapeMethod: " + delimiterEscapeMethod);
            }
            return;
        }

        /**
     * Get the character used to delimit components.
     * 
     * @return The character used to delimit components.
     */
        public char getDelimiter() {
            return delimiter;
        }

        @Override
        protected List<String> convertImpl(final String input) throws Exception {
            if ((input == null) || (input.isEmpty())) return null;
            final ArrayList<String> components = new ArrayList<String>();
            final StringBuffer currentComponent = new StringBuffer();
            for (int i = 0; i < input.length(); i++) {
                final char c = input.charAt(i);
                switch(delimiterEscapeMethod) {
                    case DUPLICATE:
                        if (c == delimiter) {
                            if ((i + 1 < input.length()) && (input.charAt(i + 1) == delimiter)) {
                                currentComponent.append(c);
                                i++;
                            } else {
                                components.add(currentComponent.toString());
                                currentComponent.setLength(0);
                            }
                        } else {
                            currentComponent.append(c);
                        }
                        break;
                    case BACKSLASH:
                        if (c == '\\') {
                            if (i + 1 < input.length()) {
                                currentComponent.append(input.charAt(i + 1));
                                i++;
                            } else {
                                currentComponent.append(c);
                            }
                        } else if (c == delimiter) {
                            components.add(currentComponent.toString());
                            currentComponent.setLength(0);
                        } else {
                            currentComponent.append(c);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown DelimiterEscapeMethod: " + delimiterEscapeMethod);
                }
            }
            components.add(currentComponent.toString());
            components.trimToSize();
            return components;
        }
    }

    ;
}
