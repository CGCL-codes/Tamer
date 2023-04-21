package de.fu_berlin.inf.gmanda.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

public class CStringUtils {

    static String spaces = "                ";

    public static synchronized String spaces(int i) {
        if (i > (1 << 20) - 1) throw new IllegalArgumentException("One MB Whitespace should be enough for you!");
        while (spaces.length() < i) {
            spaces = spaces + spaces;
        }
        return spaces.substring(0, i);
    }

    /**
	 * 
	 * @param word
	 *            The word that is supposed to get a plural s depending on the
	 *            number
	 * @param number
	 *            The number of items.
	 * @return Will return "no words" if number is zero, "1 word" if number is
	 *         one and "number words" if number is anything else
	 */
    public static String pluralS(int i, String s) {
        return "" + (i != 0 ? "" + i : "no") + " " + s + (i != 1 ? "s" : "");
    }

    public static String commonPrefix(String a, String b) {
        if (a == null || b == null) throw new NullPointerException();
        char[] c = a.toCharArray();
        char[] d = b.toCharArray();
        int max = Math.min(c.length, d.length);
        int i = 0;
        while (i < max && c[i] == d[i]) {
            i++;
        }
        return a.substring(0, i);
    }

    /**
	 * Returns a concatenation of all strings given in the collection,
	 * separating each two strings with the given separator.
	 */
    public static <T> String join(Iterable<T> strings, String separator) {
        return join(strings, separator, new PlainConverter<T>());
    }

    public interface StringConverter<T> {

        public String toString(T t);
    }

    public interface FromConverter<T> {

        public T fromString(String s);
    }

    public static class PlainConverter<T> implements StringConverter<T> {

        public String toString(Object t) {
            return t.toString();
        }
    }

    public static class JoinConverter<T> implements StringConverter<Collection<T>> {

        String separator;

        StringConverter<? super T> converter;

        public JoinConverter(String separator) {
            this.separator = separator;
            this.converter = new PlainConverter<T>();
        }

        public JoinConverter(String separator, StringConverter<T> subconverter) {
            this.separator = separator;
            this.converter = subconverter;
        }

        public String toString(Collection<T> t) {
            return CStringUtils.join(t, separator, converter);
        }
    }

    public static <T> String join(Iterable<T> strings, String separator, final StringConverter<? super T> converter) {
        Iterator<String> it = Iterators.filter(Iterators.transform(strings.iterator(), new Function<T, String>() {

            public String apply(T arg0) {
                return converter.toString(arg0);
            }
        }), new Predicate<String>() {

            public boolean apply(String arg0) {
                return arg0 != null;
            }
        });
        StringBuilder sb = new StringBuilder();
        if (it.hasNext()) sb.append(it.next());
        while (it.hasNext()) {
            sb.append(separator).append(it.next());
        }
        return sb.toString();
    }

    public static String indent(String s, int i) {
        StringIBuilder sb = new StringIBuilder();
        sb.indent(i);
        sb.append(s);
        return sb.toString();
    }

    /**
	 * Starting from position pos in text will capture all chars until c is
	 * found (including), returning the resulting string.
	 * 
	 * @param c
	 * @param text
	 * @param pos
	 * @return
	 */
    public static String skipAhead(char c, char[] text, int pos) {
        StringBuilder sb = new StringBuilder();
        sb.append(text[pos++]);
        while (pos < text.length && text[pos] != c) {
            sb.append(text[pos]);
            pos++;
        }
        if (pos < text.length) sb.append(text[pos]);
        return sb.toString();
    }

    /**
	 * Starting from position pos in text will capture all chars until delim is
	 * found (including), returning the resulting string.
	 * 
	 * If a character escape preceedes the delimiter it is not interpreted as
	 * finishing the capture.
	 */
    public static String skipAhead(char delim, char escape, char[] text, int pos) {
        StringBuilder sb = new StringBuilder();
        sb.append(text[pos++]);
        while (pos < text.length && text[pos] != delim) {
            if (text[pos] == escape) {
                while (pos < text.length && text[pos] == escape) {
                    sb.append(text[pos]);
                    pos++;
                }
            }
            if (pos < text.length) {
                sb.append(text[pos]);
                pos++;
            }
        }
        if (pos < text.length) sb.append(text[pos]);
        return sb.toString();
    }

    public static String wrap(String input, int startDepth, int indentDepth, int width) {
        input = input.trim().replaceAll("\\n *", "\n");
        List<String> lines = new LinkedList<String>();
        for (String s : input.split("\\n\\n")) {
            if (s.trim().length() > 0) lines.add(s.replaceAll("\\s+", " "));
        }
        input = CStringUtils.join(lines, "\n");
        if (input.length() + startDepth <= width && !input.contains("\n")) {
            return input;
        }
        List<String> paragraphs = new LinkedList<String>();
        StringBuilder sb = new StringBuilder();
        sb.append(spaces(startDepth));
        for (String s : input.split("\n")) {
            boolean wordAdded = false;
            StringBuilder currentParagraph = new StringBuilder();
            for (String nextWord : s.split("\\s+")) {
                if (nextWord.length() == 0) continue;
                if (sb.length() + nextWord.length() <= width || sb.length() <= indentDepth) {
                    sb.append(nextWord).append(' ');
                    wordAdded = true;
                } else {
                    String toAdd = sb.toString();
                    if (wordAdded) {
                        toAdd = trimTrailing(toAdd);
                    }
                    currentParagraph.append(toAdd).append('\n');
                    sb = new StringBuilder(spaces(indentDepth));
                    sb.append(nextWord).append(' ');
                }
            }
            String toAdd = sb.toString();
            if (wordAdded) {
                toAdd = trimTrailing(toAdd);
            }
            currentParagraph.append(toAdd);
            paragraphs.add(currentParagraph.toString());
            sb = new StringBuilder(spaces(indentDepth));
        }
        return CStringUtils.join(paragraphs, "\n\n").substring(startDepth);
    }

    public static String trimTrailing(String s) {
        return org.apache.commons.lang.StringUtils.stripEnd(s, " \n\r\t\f");
    }

    public static List<String> split(String s, char separator, char escapeChar) {
        List<String> result = new LinkedList<String>();
        if (s == null) return result;
        StringBuilder sb = new StringBuilder();
        char[] text = s.toCharArray();
        for (int i = 0; i < text.length; i++) {
            if (text[i] == escapeChar) {
                String skipped = skipAhead('\"', text, i);
                i += skipped.length() - 1;
                sb.append(skipped);
                continue;
            }
            if (text[i] == separator) {
                String segment = sb.toString();
                if (segment.length() > 0) result.add(segment);
                sb = new StringBuilder();
                continue;
            }
            sb.append(text[i]);
        }
        String segment = sb.toString();
        if (segment.length() > 0) result.add(segment);
        return result;
    }

    public static int countNewLinesAtBeginning(String tag) {
        int result = 0;
        StringTokenizer st = new StringTokenizer(tag, " \t\n\r\f", true);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (!" \t\n\r\f".contains(token)) {
                break;
            }
            if ("\r\n\f".contains(token)) {
                result++;
            }
        }
        return result;
    }

    /**
	 * Returns leading white space characters in the specified string.
	 */
    public static String getLeadingWhiteSpace(String str) {
        return str.substring(0, CStringUtils.getLeadingWhiteSpaceWidth(str));
    }

    /**
	 * Returns the number of leading white space characters in the specified
	 * string.
	 */
    public static int getLeadingWhiteSpaceWidth(String str) {
        int whitespace = 0;
        while (whitespace < str.length()) {
            char ch = str.charAt(whitespace);
            if (ch == ' ' || ch == '\t') whitespace++; else break;
        }
        return whitespace;
    }

    private static final String PLAIN_ASCII = "AaEeIiOoUu" + "AaEeIiOoUuYy" + "AaEeIiOoUuYy" + "AaOoNn" + "AaEeIiOoUuYy" + "Aa" + "Cc" + "OoUu";

    private static final String UNICODE = "ÀàÈèÌìÒòÙù" + "ÁáÉéÍíÓóÚúÝý" + "ÂâÊêÎîÔôÛûŶŷ" + "ÃãÕõÑñ" + "ÄäËëÏïÖöÜüŸÿ" + "Åå" + "Çç" + "ŐőŰű";

    /**
	 * Taken from http://www.rgagnon.com/javadetails/java-0456.html
	 */
    public static String convertNonAscii(String s) {
        if (s == null) return null;
        StringBuilder sb = new StringBuilder();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            int pos = UNICODE.indexOf(c);
            if (pos > -1) {
                sb.append(PLAIN_ASCII.charAt(pos));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
