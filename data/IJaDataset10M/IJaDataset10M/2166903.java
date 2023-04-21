package net.sf.japi.io.args;

import java.io.IOException;
import java.util.Iterator;

/**
 * Class with utility methods for joining strings.
 * The class is intended to be as flexible and convenient as possible.
 * Therefore it mostly operates on {@link CharSequence} instead of {@link String}, so you're free to use other kinds of "Strings" as well, not only {@link String} itself.
 * Apart from methods that create a new {@link String}, you will also find methods that work on {@link Appendable}s so you can use these methods to append on implementations of {@link Appendable}.
 * <p />
 * Because {@link Appendable#append(CharSequence)} throws {@link IOException}, methods that work on {@link Appendable} have been overloaded to work on certain known {@link Appendable}-implementations that do not throw an {@link IOException} when appending.
 * Currently this is namely {@link StringBuilder} and {@link StringBuffer}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @since 0.2
 */
public final class StringJoiner {

    /**
     * Utility class - do not instanciate.
     */
    private StringJoiner() {
    }

    /**
     * Join Strings.
     * @param delim delimiter to use for joining the strings
     * @param strings Strings to join
     * @return joined string
     */
    public static String join(final CharSequence delim, final CharSequence... strings) {
        return join(new StringBuilder(), delim, strings).toString();
    }

    /**
     * Join Strings.
     * @param delim delimiter to use for joining the strings
     * @param strings Strings to join
     * @return joined string
     */
    public static String join(final CharSequence delim, final Iterable<? extends CharSequence> strings) {
        return join(new StringBuilder(), delim, strings).toString();
    }

    /**
     * Join Strings.
     * @param delim delimiter to use for joining the strings
     * @param strings Strings to join
     * @return joined string
     */
    public static String join(final CharSequence delim, final Iterator<? extends CharSequence> strings) {
        return join(new StringBuilder(), delim, strings).toString();
    }

    /**
     * Join Strings to an Appendable.
     * @param dest Appendable to join Strings to
     * @param delim delimiter to use for joining the strings
     * @param strings Strings to join
     * @return Appendable
     * @throws IOException In case of I/O problems on <var>dest</var>.
     */
    public static <A extends Appendable> A join(final A dest, final CharSequence delim, final CharSequence... strings) throws IOException {
        for (int i = 0; i < strings.length; i++) {
            if (i > 0) {
                dest.append(delim);
            }
            dest.append(strings[i]);
        }
        return dest;
    }

    /**
     * Join Strings to an Appendable.
     * @param dest Appendable to join Strings to
     * @param delim delimiter to use for joining the strings
     * @param strings Strings to join
     * @return Appendable
     * @throws IOException In case of I/O problems on <var>dest</var>.
     */
    public static <A extends Appendable> A join(final A dest, final CharSequence delim, final Iterable<? extends CharSequence> strings) throws IOException {
        return join(dest, delim, strings.iterator());
    }

    /**
     * Join Strings to an Appendable.
     * @param dest Appendable to join Strings to
     * @param delim delimiter to use for joining the strings
     * @param strings Strings to join
     * @return Appendable
     * @throws IOException In case of I/O problems on <var>dest</var>.
     */
    public static <A extends Appendable> A join(final A dest, final CharSequence delim, final Iterator<? extends CharSequence> strings) throws IOException {
        if (strings.hasNext()) {
            dest.append(strings.next());
        }
        while (strings.hasNext()) {
            dest.append(delim);
            dest.append(strings.next());
        }
        return dest;
    }

    /**
     * Join Strings to a StringBuilder.
     * @param dest StringBuilder to join Strings to
     * @param delim delimiter to use for joining the strings
     * @param strings Strings to join
     * @return supplied StringBuilder (<var>dest</var>)
     */
    public static StringBuilder join(final StringBuilder dest, final CharSequence delim, final CharSequence... strings) {
        for (int i = 0; i < strings.length; i++) {
            if (i > 0) {
                dest.append(delim);
            }
            dest.append(strings[i]);
        }
        return dest;
    }

    /**
     * Join Strings to a StringBuilder.
     * @param dest StringBuilder to join Strings to
     * @param delim delimiter to use for joining the strings
     * @param strings Strings to join
     * @return supplied StringBuilder (<var>dest</var>)
     */
    public static StringBuilder join(final StringBuilder dest, final CharSequence delim, final Iterable<? extends CharSequence> strings) {
        return join(dest, delim, strings.iterator());
    }

    /**
     * Join Strings to a StringBuilder.
     * @param dest StringBuilder to join Strings to
     * @param delim delimiter to use for joining the strings
     * @param strings Strings to join
     * @return supplied StringBuilder (<var>dest</var>)
     */
    public static StringBuilder join(final StringBuilder dest, final CharSequence delim, final Iterator<? extends CharSequence> strings) {
        if (strings.hasNext()) {
            dest.append(strings.next());
        }
        while (strings.hasNext()) {
            dest.append(delim);
            dest.append(strings.next());
        }
        return dest;
    }

    /**
     * Join Strings to a StringBuffer.
     * @param dest StringBuffer to join Strings to
     * @param delim delimiter to use for joining the strings
     * @param strings Strings to join
     * @return supplied StringBuffer (<var>dest</var>)
     */
    public static StringBuffer join(final StringBuffer dest, final CharSequence delim, final CharSequence... strings) {
        for (int i = 0; i < strings.length; i++) {
            if (i > 0) {
                dest.append(delim);
            }
            dest.append(strings[i]);
        }
        return dest;
    }

    /**
     * Join Strings to a StringBuffer.
     * @param dest StringBuffer to join Strings to
     * @param delim delimiter to use for joining the strings
     * @param strings Strings to join
     * @return supplied StringBuffer (<var>dest</var>)
     */
    public static StringBuffer join(final StringBuffer dest, final CharSequence delim, final Iterable<? extends CharSequence> strings) {
        return join(dest, delim, strings.iterator());
    }

    /**
     * Join Strings to a StringBuffer.
     * @param dest StringBuffer to join Strings to
     * @param delim delimiter to use for joining the strings
     * @param strings Strings to join
     * @return supplied StringBuffer (<var>dest</var>)
     */
    public static StringBuffer join(final StringBuffer dest, final CharSequence delim, final Iterator<? extends CharSequence> strings) {
        if (strings.hasNext()) {
            dest.append(strings.next());
        }
        while (strings.hasNext()) {
            dest.append(delim);
            dest.append(strings.next());
        }
        return dest;
    }
}
