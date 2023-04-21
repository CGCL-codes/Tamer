package net.sourceforge.cobertura.reporting.html;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class JavaToHtml {

    public static final class State {

        public static final int COMMENT_JAVADOC = 0;

        public static final int COMMENT_MULTI = 1;

        public static final int COMMENT_SINGLE = 2;

        public static final int DEFAULT = 3;

        public static final int KEYWORD = 4;

        public static final int IMPORT_NAME = 5;

        public static final int PACKAGE_NAME = 6;

        public static final int QUOTE_DOUBLE = 8;

        public static final int QUOTE_SINGLE = 9;

        private State() {
        }
    }

    private static final Collection javaKeywords;

    private static final Collection javaPrimitiveLiterals;

    private static final Collection javaPrimitiveTypes;

    static {
        final String[] javaKeywordsArray = { "abstract", "assert", "break", "case", "catch", "class", "const", "continue", "default", "do", "else", "extends", "final", "finally", "for", "goto", "if", "interface", "implements", "import", "instanceof", "native", "new", "package", "private", "protected", "public", "return", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "volatile", "while" };
        final String javaPrimitiveTypesArray[] = { "boolean", "byte", "char", "double", "float", "int", "long", "short", "void" };
        final String javaPrimitiveLiteralsArray[] = { "false", "null", "true" };
        javaKeywords = new HashSet(Arrays.asList(javaKeywordsArray));
        javaPrimitiveTypes = new HashSet(Arrays.asList(javaPrimitiveTypesArray));
        javaPrimitiveLiterals = new HashSet(Arrays.asList(javaPrimitiveLiteralsArray));
    }

    private int state = State.DEFAULT;

    private static String escapeEntity(final char character) {
        if (character == '&') return "&amp;"; else if (character == '<') return "&lt;"; else if (character == '>') return "&gt;"; else if (character == '\t') return "        "; else return Character.toString(character);
    }

    /**
	 * Add HTML colorization to a block of Java code.
	 *
	 * @param text The block of Java code.
	 * @return The same block of Java code with added span tags.
	 *         Newlines are preserved.
	 */
    public String process(final String text) {
        if (text == null) throw new IllegalArgumentException("\"text\" can not be null.");
        StringBuffer ret = new StringBuffer();
        int begin, end, nextCR;
        begin = 0;
        end = text.indexOf('\n', begin);
        nextCR = text.indexOf('\r', begin);
        if ((nextCR != -1) && ((end == -1) || (nextCR < end))) end = nextCR;
        while (end != -1) {
            ret.append(processLine(text.substring(begin, end)) + "<br/>");
            if ((end + 1 < text.length()) && ((text.charAt(end + 1) == '\n') || (text.charAt(end + 1) == '\r'))) {
                ret.append(text.substring(end, end + 1));
                begin = end + 2;
            } else {
                ret.append(text.charAt(end));
                begin = end + 1;
            }
            end = text.indexOf('\n', begin);
            nextCR = text.indexOf('\r', begin);
            if ((nextCR != -1) && ((end == -1) || (nextCR < end))) end = nextCR;
        }
        ret.append(processLine(text.substring(begin)));
        return ret.toString();
    }

    /**
	 * Add HTML colorization to a single line of Java code.
	 *
	 * @param line One line of Java code.
	 * @return The same line of Java code with added span tags.
	 */
    private String processLine(final String line) {
        if (line == null) throw new IllegalArgumentException("\"line\" can not be null.");
        if ((line.indexOf('\n') != -1) || (line.indexOf('\r') != -1)) throw new IllegalArgumentException("\"line\" can not contain newline or carriage return characters.");
        StringBuffer ret = new StringBuffer();
        int currentIndex = 0;
        while (currentIndex != line.length()) {
            if (state == State.DEFAULT) {
                if ((currentIndex + 2 < line.length()) && line.substring(currentIndex, currentIndex + 3).equals("/**")) {
                    state = State.COMMENT_JAVADOC;
                } else if ((currentIndex + 1 < line.length()) && line.substring(currentIndex, currentIndex + 2).equals("/*")) {
                    state = State.COMMENT_MULTI;
                } else if ((currentIndex + 1 < line.length()) && (line.substring(currentIndex, currentIndex + 2).equals("//"))) {
                    state = State.COMMENT_SINGLE;
                } else if (Character.isJavaIdentifierStart(line.charAt(currentIndex))) {
                    state = State.KEYWORD;
                } else if (line.charAt(currentIndex) == '\'') {
                    state = State.QUOTE_SINGLE;
                } else if (line.charAt(currentIndex) == '"') {
                    state = State.QUOTE_DOUBLE;
                } else {
                    ret.append(escapeEntity(line.charAt(currentIndex++)));
                }
            } else if ((state == State.COMMENT_MULTI) || (state == State.COMMENT_JAVADOC)) {
                ret.append("<span class=\"comment\">");
                while ((currentIndex != line.length()) && !((currentIndex + 1 < line.length()) && (line.substring(currentIndex, currentIndex + 2).equals("*/")))) {
                    ret.append(escapeEntity(line.charAt(currentIndex++)));
                }
                if (currentIndex == line.length()) {
                    ret.append("</span>");
                } else {
                    ret.append("*/</span>");
                    state = State.DEFAULT;
                    currentIndex += 2;
                }
            } else if (state == State.COMMENT_SINGLE) {
                ret.append("<span class=\"comment\">");
                while (currentIndex != line.length()) {
                    ret.append(escapeEntity(line.charAt(currentIndex++)));
                }
                ret.append("</span>");
                state = State.DEFAULT;
            } else if (state == State.KEYWORD) {
                StringBuffer tmp = new StringBuffer();
                do {
                    tmp.append(line.charAt(currentIndex++));
                } while ((currentIndex != line.length()) && (Character.isJavaIdentifierPart(line.charAt(currentIndex))));
                if (javaKeywords.contains(tmp.toString())) ret.append("<span class=\"keyword\">" + tmp + "</span>"); else if (javaPrimitiveLiterals.contains(tmp.toString())) ret.append("<span class=\"keyword\">" + tmp + "</span>"); else if (javaPrimitiveTypes.contains(tmp.toString())) ret.append("<span class=\"keyword\">" + tmp + "</span>"); else ret.append(tmp);
                if (tmp.toString().equals("import")) state = State.IMPORT_NAME; else if (tmp.toString().equals("package")) state = State.PACKAGE_NAME; else state = State.DEFAULT;
            } else if (state == State.IMPORT_NAME) {
                ret.append(escapeEntity(line.charAt(currentIndex++)));
                state = State.DEFAULT;
            } else if (state == State.PACKAGE_NAME) {
                ret.append(escapeEntity(line.charAt(currentIndex++)));
                state = State.DEFAULT;
            } else if (state == State.QUOTE_DOUBLE) {
                ret.append("<span class=\"string\">");
                do {
                    ret.append(escapeEntity(line.charAt(currentIndex++)));
                } while ((currentIndex != line.length()) && (!(line.charAt(currentIndex) == '"') || ((line.charAt(currentIndex - 1) == '\\') && (line.charAt(currentIndex - 2) != '\\'))));
                if (currentIndex == line.length()) {
                    ret.append("</span>");
                } else {
                    ret.append("\"</span>");
                    state = State.DEFAULT;
                    currentIndex++;
                }
            } else if (state == State.QUOTE_SINGLE) {
                ret.append("<span class=\"string\">");
                do {
                    ret.append(escapeEntity(line.charAt(currentIndex++)));
                } while ((currentIndex != line.length()) && (!(line.charAt(currentIndex) == '\'') || ((line.charAt(currentIndex - 1) == '\\') && (line.charAt(currentIndex - 2) != '\\'))));
                if (currentIndex == line.length()) {
                    ret.append("</span>");
                } else {
                    ret.append("\'</span>");
                    state = State.DEFAULT;
                    currentIndex++;
                }
            } else {
                ret.append(escapeEntity(line.charAt(currentIndex++)));
            }
        }
        return ret.toString();
    }

    /**
	 * Reset the state of this Java parser.  Call this if you have
	 * been parsing one Java file and you want to begin parsing
	 * another Java file.
	 *
	 */
    public void reset() {
        state = State.DEFAULT;
    }
}
