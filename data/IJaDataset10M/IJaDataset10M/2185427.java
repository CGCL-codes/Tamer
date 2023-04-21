package gumbo.core.util;

import gumbo.core.struct.StructUtils;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Utilities related to general purpose string parsing and printing.
 * @author jonb
 * @see StringUtils
 * @see TimeUtils
 * @see TypeUtils
 */
public class StringUtils {

    private StringUtils() {
    }

    public static final String PARSE_WHITE_STRING = "\\s\\s*";

    public static final String PARSE_BLACK_STRING = "\\s*[,;|]\\s*";

    public static final Pattern PARSE_QUOTED_PATTERN = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");

    /**
	 * Parses a string and returns a list of "regex" delimited sub-strings (see
	 * {@link String#split()}).
	 * @param string Constant input string. If empty or null, returns an empty
	 * list.
	 * @param regex String delimiter expressed in regex format. If empty or
	 * null, returns a list with input string.
	 * @return Constant list of sub-strings. Never null. Empty if no sub-strings
	 * are found.
	 */
    public static List<String> parseStrings(String string, String regex) {
        if (string == null || string.isEmpty()) return StructUtils.constListNone();
        if (regex == null || regex.isEmpty()) return StructUtils.constListOne(string);
        String[] subStrings = string.split(regex);
        return StructUtils.constListAll(subStrings);
    }

    /**
	 * Parses a string and returns a list of "white space" delimited substrings.
	 * Leading and trailing white space will be trimmed from the
	 * substrings. The substrings will contain no whitespace. For quoted strings see
	 * {@link #parseQuotedStrings}.
	 * <p>
	 * For
	 * example, " My String   '0 1' " would result in four substrings,
	 * "My", "String", "'0", and "1'".
	 * @param string Constant input string. If empty or null, Returns an empty
	 * list.
	 * @return Constant list of sub-strings. Never null. Empty if no sub-strings
	 * are found.
	 */
    public static List<String> parseWhiteStrings(String string) {
        if (string == null || string.isEmpty()) return StructUtils.constListNone();
        String[] subStrings = string.split(PARSE_WHITE_STRING);
        return StructUtils.constListAll(subStrings);
    }

    /**
	 * Parses a string and returns a list of "black space" delimited sub-strings
	 * (i.e. one of ",;|"). Leading and trailing white space will be trimmed
	 * from the sub-strings. Sub-string may contain whitespace. For example,
	 * " My String 0 ,, My String 1 " would result in three substrings,
	 * "My String 0", "", and "My String 1". Intended for use in parsing a list
	 * of global IDs and file paths, which may contain white space.
	 * @param string Constant input string. If empty or null, Returns an empty
	 * list.
	 * @return Constant list of sub-strings. Never null. Empty if no sub-strings
	 * are found.
	 */
    public static List<String> parseBlackStrings(String string) {
        if (string == null || string.isEmpty()) return StructUtils.constListNone();
        String[] subStrings = string.split(PARSE_BLACK_STRING);
        return StructUtils.constListAll(subStrings);
    }

    /**
	 * Parses a string and returns a list of "white space" delimited substrings,
	 * with quote recognition. Leading and trailing white space and string
	 * quotes will be trimmed from the substrings. To include whitespace in a
	 * substring, enclose the substring in matching single or double quotes. To
	 * include a quote in a substring, escape it with backslash, "\"". To
	 * include a backslash in a substring, escape it with a backslash, "\\".
	 * <p>
	 * For example, " My String '0 1\\\' '" would result in three substrings,
	 * "My", "String", and "0 1\'".
	 * @param string Constant input string. If empty or null, Returns an empty
	 * list.
	 * @return Constant list of sub-strings. Never null. Empty if no sub-strings
	 * are found.
	 * @see <a href="http://stackoverflow.com/questions/366202/regex-for-splitting-a-string-using-space-when-not-surrounded-by-single-or-double">Derived from</a>
	 */
    public static List<String> parseQuotedStrings(String string) {
        List<String> result = new ArrayList<String>();
        java.util.regex.Matcher regexMatcher = PARSE_QUOTED_PATTERN.matcher(string);
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                result.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                result.add(regexMatcher.group(2));
            } else {
                result.add(regexMatcher.group());
            }
        }
        return result;
    }

    /**
	 * Detects "white space" delimited substrings that are quoted. Intended for
	 * use in conjunction with parseQuotedStrings() so that quoted substrings
	 * can be used literally (e.g. most special characters lose their special
	 * meaning). The length of the result will be the same as that for a parsed
	 * string.
	 * @param string Constant input string. If empty or null, Returns an empty
	 * list.
	 * @return Constant list of "isQuoted" flags. Never null. Empty if no
	 * sub-strings are found.
	 * @see #parseQuotedStrings(String)
	 */
    public static List<Boolean> detectQuotedStrings(String string) {
        List<Boolean> result = new ArrayList<Boolean>();
        java.util.regex.Matcher regexMatcher = PARSE_QUOTED_PATTERN.matcher(string);
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                result.add(true);
            } else if (regexMatcher.group(2) != null) {
                result.add(true);
            } else {
                result.add(false);
            }
        }
        return result;
    }

    /**
	 * Wraps text in a string on line boundaries.  Skips non-printing white
	 * space (e.g. \n, \t, etc.).
	 * @param text Input text. Never null. If empty, returns empty list.
	 * @param lineLen Line length (>0).
	 * @return Ceded list of line strings. Never null. Empty if text is empty.
	 */
    public static List<String> lineWrap(String text, int lineLen) {
        AssertUtils.assertNonNullArg(text);
        if (lineLen < 1) throw new IllegalArgumentException("lineLen must be >0. lineLen=" + lineLen);
        List<String> lines = new ArrayList<String>();
        if (text.isEmpty()) return lines;
        if (text.length() <= lineLen) {
            lines.add(text);
            return lines;
        }
        text = text.replaceAll("\\p{Cntrl}", "");
        char[] chars = text.toCharArray();
        StringBuffer line = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            line.append(chars[i]);
            if (line.length() <= lineLen) continue;
            lines.add(line.toString());
            line.setLength(0);
        }
        if (line.length() > 0) {
            lines.add(line.toString());
        }
        return lines;
    }

    /**
	 * Wraps text in a string on word boundaries. This is fairly primitive in
	 * that space is used for word boundaries, and spaces are left at the end of
	 * lines.  Derived from
	 * http://progcookbook.blogspot.com/2006/02/text-wrapping-function-for-java.html.
	 * @param text Input text. Never null. If empty, returns empty list.
	 * @param lineLen Line length (>0).
	 * @return Ceded list of line strings. Never null. Empty if text is empty.
	 */
    public static List<String> wordWrap(String text, int lineLen) {
        AssertUtils.assertNonNullArg(text);
        if (lineLen < 1) throw new IllegalArgumentException("lineLen must be >0. lineLen=" + lineLen);
        List<String> lines = new ArrayList<String>();
        if (text.isEmpty()) return lines;
        if (text.length() <= lineLen) {
            lines.add(text);
            return lines;
        }
        char[] chars = text.toCharArray();
        StringBuffer line = new StringBuffer();
        StringBuffer word = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            word.append(chars[i]);
            if (chars[i] == ' ') {
                if ((line.length() + word.length()) > lineLen) {
                    lines.add(line.toString());
                    line.delete(0, line.length());
                }
                line.append(word);
                word.delete(0, word.length());
            }
        }
        if (word.length() > 0) {
            if ((line.length() + word.length()) > lineLen) {
                lines.add(line.toString());
                line.delete(0, line.length());
            }
            line.append(word);
        }
        if (line.length() > 0) {
            lines.add(line.toString());
        }
        return lines;
    }

    /**
	 * Converts a list of line string into a text string. Each line string will
	 * be prefixed and suffixed with the given strings. Typically, prefix is a
	 * fixed size indent.
	 * @param prepend Prepended to each text line. None if null.
	 * @param lines List of line strings. Never null.
	 * @param postpend Postpended to each line. None if null.
	 * @return The result. Never null.
	 */
    public static String printLines(String prepend, List<String> lines, String postpend) {
        if (prepend == null) prepend = "";
        AssertUtils.assertNonNullArg(lines);
        if (postpend == null) postpend = "";
        StringWriter text = new StringWriter();
        PrintWriter printer = new PrintWriter(text);
        for (String line : lines) {
            printer.println(prepend + line + postpend);
        }
        return text.toString();
    }

    /**
	 * Converts a list of line string into a text string, with each line
	 * beginning with a line number. The line number will be formatted according
	 * to the format string (e.g. "  %d: "), starting with the specified number.
	 * @param format Line number format string. "%d" if null.
	 * @param start Number of the the first line.
	 * @param lines List of line strings. Never null.
	 * @return The result. Never null.
	 */
    public static String numberLines(String format, int start, List<String> lines) {
        if (format == null) format = "%d";
        AssertUtils.assertNonNullArg(lines);
        StringWriter text = new StringWriter();
        PrintWriter printer = new PrintWriter(text);
        int lineNumber = start;
        for (String line : lines) {
            printer.println(String.format(format, lineNumber++) + line);
        }
        return text.toString();
    }

    /**
	 * Reads the contents of a file as a string using the platform default
	 * character set.
	 * @param path File path. Never empty.
	 * @return The result. Never null.
	 * @throws IOException If there is a problem reading the file.
	 */
    public static String readFile(String path) throws IOException {
        AssertUtils.assertNonEmptyArg(path);
        return new Scanner(new File(path)).useDelimiter(END_OF_FILE).next();
    }

    /**
	 * Reads the contents of a file as a string using a given character set.
	 * @param path File path. Never empty.
	 * @param charset Character set name (e.g. "UTF_8"). Never empty.
	 * @return The result. Never null.
	 * @throws IOException If there is a problem reading the file.
	 */
    public static String readFile(String path, String charset) throws IOException {
        AssertUtils.assertNonEmptyArg(path);
        AssertUtils.assertNonEmptyArg(charset);
        return new Scanner(new File(path), charset).useDelimiter(END_OF_FILE).next();
    }

    private static String END_OF_FILE = "\\Z";
}
