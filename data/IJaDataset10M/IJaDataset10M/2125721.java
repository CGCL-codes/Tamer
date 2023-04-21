package org.rubypeople.rdt.internal.core.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * RubyTokenizer
 * 
 * @author CAWilliams
 *  
 */
public class RubyTokenizer {

    private static final char COMMENT_START = '#';

    private static final char INSTANCE_START = '@';

    private static final char GLOBAL_START = '$';

    private int currentPosition;

    private int maxPosition;

    private char endChar = 0;

    private String str;

    private boolean inString = false;

    private boolean tempInString = false;

    private char tempEndChar;

    private static final String percentChars = "qQr";

    private static final String delimiters = " .;('\"/\t,[]";

    private static final String BRACKETS = "({[";

    /**
	 * @param curLine
	 */
    public RubyTokenizer(String curLine) {
        this.str = curLine;
        currentPosition = 0;
        maxPosition = str.length();
        setMaxDelimChar();
    }

    /**
	 * maxDelimChar stores the value of the delimiter character with the highest
	 * value. It is used to optimize the detection of delimiter characters.
	 */
    private char maxDelimChar;

    /**
	 * Set maxDelimChar to the highest char in the delimiter set.
	 */
    private void setMaxDelimChar() {
        if (delimiters == null) {
            maxDelimChar = 0;
            return;
        }
        char m = 0;
        for (int i = 0; i < delimiters.length(); i++) {
            char c = delimiters.charAt(i);
            if (m < c) m = c;
        }
        maxDelimChar = m;
    }

    /**
	 * @return
	 */
    public boolean hasMoreTokens() {
        return countTokens() > 0;
    }

    /**
	 * @return
	 */
    private boolean isStartOfComment(int newPosition) {
        return (str.charAt(newPosition) == COMMENT_START) && (!inString(str, newPosition));
    }

    /**
	 * @return
	 */
    public RubyToken nextRubyToken() {
        int start = skipDelimiters(currentPosition, false);
        if (start >= maxPosition) throw new NoSuchElementException();
        if (isStartOfComment(start)) throw new NoSuchElementException();
        currentPosition = scanToken(start, false);
        String text = str.substring(start, currentPosition);
        if (isStringBeginning(text) && !inQuotes(start, str)) {
            inString = true;
            int endCharIndex = getEndCharIndex(text.charAt(0));
            endChar = getMatchingBracket(text.charAt(endCharIndex));
            if (text.length() == endCharIndex + 1) {
                return nextRubyToken();
            }
            text = text.substring(endCharIndex + 1);
            start = start + endCharIndex + 1;
        }
        if (text.charAt(text.length() - 1) == endChar) {
            endChar = 0;
            inString = false;
            if (text.length() == 1) return nextRubyToken();
            text = text.substring(0, text.length() - 1);
        }
        return new RubyToken(getType(text, start), text, start);
    }

    /**
	 * Returns true if the index position in curLine is inside quotes
	 * 
	 * @param index
	 *            The index position to check
	 * @param curLine
	 *            The String to check @returntrue if the index position in
	 *            curLine is inside quotes
	 */
    private boolean inQuotes(int index, String curLine) {
        List openQuotes = new ArrayList();
        for (int curPosition = 0; curPosition < index; curPosition++) {
            char c = curLine.charAt(curPosition);
            if (isQuoteChar(c)) {
                Character newChar = new Character(c);
                if (!openQuotes.isEmpty()) {
                    Character open = (Character) openQuotes.get(openQuotes.size() - 1);
                    if (newChar.equals(open)) {
                        openQuotes.remove(openQuotes.size() - 1);
                        continue;
                    }
                }
                openQuotes.add(newChar);
            }
        }
        return !openQuotes.isEmpty() || inPercentString('q', index, curLine) || inPercentString('Q', index, curLine);
    }

    /**
	 * @param index
	 * @param curLine
	 * @return
	 */
    private boolean inPercentString(char type, int index, String curLine) {
        int end = endIndexOf(curLine, "%" + type);
        if (end == -1) return false;
        if (end > index) return false;
        char c = curLine.charAt(end);
        if (isOpenBracket(c)) c = getMatchingBracket(c);
        for (int i = end + 1; i < index; i++) {
            if (curLine.charAt(i) == c && curLine.charAt(i - 1) != '\\') {
                return false;
            }
        }
        return true;
    }

    /**
	 * @param myLine
	 * @param prefix
	 * @return
	 */
    private int endIndexOf(String myLine, String token) {
        if (myLine.indexOf(token) == -1) return -1;
        return myLine.indexOf(token) + token.length();
    }

    /**
	 * @param text
	 * @return
	 */
    private int getEndCharIndex(char c) {
        if (isQuoteChar(c)) return 0;
        return 2;
    }

    /**
	 * @param text
	 * @return
	 */
    private boolean isStringBeginning(String text) {
        if (text == null || text.length() == 0) return false;
        if (isQuoteChar(text.charAt(0))) return true;
        if (isStartOfPercentString(text)) return true;
        return false;
    }

    /**
	 * @param text
	 * @return
	 */
    private boolean isStartOfPercentString(String text) {
        if (text.length() < 3) return false;
        if (text.charAt(0) != '%') return false;
        if (percentChars.indexOf(text.charAt(1)) == -1) return false;
        return true;
    }

    /**
	 * @param c
	 * @return
	 */
    private boolean isDelimiter(char c) {
        return c <= maxDelimChar && delimiters.indexOf(c) >= 0;
    }

    /**
	 * Returns true if the given index in the String curLine is inside a regular
	 * expression
	 * 
	 * @param index
	 *            the int position to check
	 * @param curLine
	 *            the String to check within
	 * @return
	 */
    private boolean inRegex(int index, String curLine) {
        boolean insideregex = false;
        for (int curPosition = 0; curPosition < index; curPosition++) {
            if (curLine.charAt(curPosition) == '/') {
                insideregex = !insideregex;
            }
        }
        return insideregex || inPercentString('r', index, curLine);
    }

    /**
	 * @param curLine
	 * @param start
	 * @return
	 */
    private boolean inString(String curLine, int start) {
        return inQuotes(start, curLine) || inRegex(start, curLine);
    }

    private int getType(String text, int start) {
        if (inString(str, start)) {
            if (isVariableSubstitution(text)) {
                return RubyToken.VARIABLE_SUBSTITUTION;
            }
            if (inVariableSubstitution(str, start)) {
                if (text.startsWith("@@")) {
                    return RubyToken.CLASS_VARIABLE;
                }
                if (text.charAt(0) == INSTANCE_START) {
                    return RubyToken.INSTANCE_VARIABLE;
                }
                if (text.charAt(0) == GLOBAL_START) {
                    return RubyToken.GLOBAL;
                }
                return RubyToken.IDENTIFIER;
            }
            return RubyToken.STRING_TEXT;
        }
        if (Character.isUpperCase(text.charAt(0)) && Character.isLetter(text.charAt(0))) {
            return RubyToken.CONSTANT;
        }
        if (text.equals("end")) {
            return RubyToken.END;
        }
        if (text.equals("def")) {
            return RubyToken.METHOD;
        }
        if (text.equals("require")) {
            return RubyToken.REQUIRES;
        }
        if (text.equals("class")) {
            return RubyToken.CLASS;
        }
        if (text.equals("module")) {
            return RubyToken.MODULE;
        }
        if (text.equals("for")) {
            return RubyToken.FOR;
        }
        if (text.equals("do")) {
            return RubyToken.DO;
        }
        if (text.equals("begin")) {
            return RubyToken.BEGIN;
        }
        if (text.equals("case")) {
            return RubyToken.CASE;
        }
        if (text.equals("unless")) {
            if (isStartOfStatement(str, start)) return RubyToken.UNLESS;
            return RubyToken.UNLESS_MODIFIER;
        }
        if (text.equals("until")) {
            if (isStartOfStatement(str, start)) return RubyToken.UNTIL;
            return RubyToken.UNTIL_MODIFIER;
        }
        if (text.equals("while")) {
            if (isStartOfStatement(str, start)) return RubyToken.WHILE;
            return RubyToken.WHILE_MODIFIER;
        }
        if (text.equals("if")) {
            if (isStartOfStatement(str, start)) return RubyToken.IF;
            return RubyToken.IF_MODIFIER;
        }
        if (text.equals("private")) {
            return RubyToken.PRIVATE;
        }
        if (text.equals("protected")) {
            return RubyToken.PROTECTED;
        }
        if (text.equals("attr_reader")) {
            return RubyToken.ATTR_READER;
        }
        if (text.equals("attr_writer")) {
            return RubyToken.ATTR_WRITER;
        }
        if (text.equals("attr_accessor")) {
            return RubyToken.ATTR_ACCESSOR;
        }
        if (text.startsWith(":")) {
            return RubyToken.SYMBOL;
        }
        if (text.startsWith("@@")) {
            return RubyToken.CLASS_VARIABLE;
        }
        if (text.charAt(0) == INSTANCE_START) {
            return RubyToken.INSTANCE_VARIABLE;
        }
        if (text.charAt(0) == GLOBAL_START) {
            return RubyToken.GLOBAL;
        }
        return RubyToken.IDENTIFIER;
    }

    /**
	 * @param str2
	 * @param start
	 * @return
	 */
    private boolean inVariableSubstitution(String str2, int start) {
        boolean inVarSubs = false;
        for (int i = 0; i < start; i++) {
            if (inVarSubs && str2.charAt(i) == '}') {
                inVarSubs = false;
                continue;
            }
            if (str2.charAt(i) == '#') {
                if (str2.charAt(i + 1) == '@' || str2.charAt(i + 1) == '$' || str2.charAt(i + 1) == '{') {
                    inVarSubs = true;
                }
            }
        }
        return inVarSubs;
    }

    /**
	 * @param text
	 * @return
	 */
    private boolean isVariableSubstitution(String text) {
        if (text == null || text.length() < 2) return false;
        if (text.charAt(0) != COMMENT_START) return false;
        if (text.charAt(1) != '{') {
            if (text.charAt(1) == INSTANCE_START || text.charAt(1) == GLOBAL_START) return true;
        }
        if (text.length() < 3) return false;
        if (text.charAt(2) == INSTANCE_START || text.charAt(2) == GLOBAL_START) return true;
        return false;
    }

    /**
	 * Returns true if the index is preceded by any number whitespaces and the
	 * first non whitespace character is ';' or '='
	 * 
	 * @param line
	 * @param index
	 * @return
	 */
    private boolean isStartOfStatement(String line, int index) {
        for (int curPos = index - 1; curPos >= 0; curPos--) {
            char c = line.charAt(curPos);
            if (c == ';') return true;
            if (c == '=') return true;
            if (!Character.isWhitespace(c)) return false;
        }
        return true;
    }

    /**
	 * Calculates the number of times that this tokenizer's
	 * <code>nextToken</code> method can be called before it generates an
	 * exception. The current position is not advanced.
	 * 
	 * @return the number of tokens remaining in the string using the current
	 *         delimiter set.
	 * @see java.util.StringTokenizer#nextToken()
	 */
    public int countTokens() {
        int count = 0;
        int currpos = currentPosition;
        tempEndChar = endChar;
        tempInString = inString;
        while (currpos < maxPosition) {
            currpos = skipDelimiters(currpos, true);
            int endPos = scanToken(currpos, true);
            String text = str.substring(currpos, endPos);
            if (currpos >= maxPosition) break;
            if (isStartOfComment(currpos)) break;
            if (isStringBeginning(text) && !tempInString) {
                tempInString = true;
                int endCharIndex = getEndCharIndex(text.charAt(0));
                tempEndChar = text.charAt(endCharIndex);
                if (text.length() == endCharIndex + 1) {
                    currpos = endPos;
                    continue;
                }
                text = text.substring(endCharIndex + 1);
            }
            if (inString && text.charAt(text.length() - 1) == tempEndChar) {
                tempEndChar = 0;
                if (text.length() == 1) {
                    currpos = endPos;
                    continue;
                }
                text = text.substring(0, text.length() - 1);
            }
            currpos = endPos;
            count++;
        }
        return count;
    }

    /**
	 * Returns the matching bracket for a given character. If it is not a
	 * bracket, returns the character
	 * 
	 * @param c
	 * @return
	 */
    private char getMatchingBracket(char c) {
        switch(c) {
            case '(':
                return ')';
            case '{':
                return '}';
            case '[':
                return ']';
            default:
                return c;
        }
    }

    /**
	 * Skips delimiters starting from the specified position. If retDelims is
	 * false, returns the index of the first non-delimiter character at or after
	 * startPos. If retDelims is true, startPos is returned.
	 */
    private int skipDelimiters(int startPos, boolean temp) {
        if (delimiters == null) throw new NullPointerException();
        int position = startPos;
        while (position < maxPosition) {
            char c = str.charAt(position);
            if (!inString(temp) && isStringBeginning(str.substring(position))) {
                setInString(temp, true);
                int offset = getEndCharIndex(c);
                setEndChar(temp, getMatchingBracket(str.charAt(position + offset)));
                position += offset + 1;
                continue;
            }
            if (inString(temp)) {
                if (isUnescapedEndOfString(temp, position)) {
                    setInString(temp, false);
                    setEndChar(temp, (char) 0);
                    position++;
                    continue;
                }
                if (!Character.isWhitespace(c)) break;
                position++;
                continue;
            }
            if (!isDelimiter(c)) break;
            position++;
        }
        return position;
    }

    /**
	 * @param c
	 * @return
	 */
    private boolean isOpenBracket(char c) {
        return BRACKETS.indexOf(c) != -1;
    }

    /**
	 * @param temp
	 * @param value
	 */
    private void setEndChar(boolean temp, char value) {
        if (temp) {
            tempEndChar = value;
        } else {
            endChar = value;
        }
    }

    /**
	 * @param temp
	 * @param value
	 */
    private void setInString(boolean temp, boolean value) {
        if (temp) {
            tempInString = value;
        } else {
            inString = value;
        }
    }

    /**
	 * Skips ahead from startPos and returns the index of the next delimiter
	 * character encountered, or maxPosition if no such delimiter is found.
	 */
    private int scanToken(int startPos, boolean temp) {
        int position = startPos;
        while (position < maxPosition) {
            char c = str.charAt(position);
            if (inString(temp)) {
                if (Character.isWhitespace(c)) break;
                if (position == startPos) {
                    position++;
                    continue;
                }
                if (isVariableSubstitution(str.substring(position))) break;
                if (isUnescapedEndOfString(temp, position)) {
                    position++;
                    break;
                }
            } else {
                if (isDelimiter(c)) break;
            }
            position++;
        }
        return position;
    }

    /**
	 * @param temp
	 * @param position
	 * @return
	 */
    private boolean isUnescapedEndOfString(boolean temp, int position) {
        return str.charAt(position) == endChar(temp) && str.charAt(position - 1) != '\\';
    }

    /**
	 * @param temp
	 * @return
	 */
    private boolean inString(boolean temp) {
        if (!temp) {
            return inString;
        } else {
            return tempInString;
        }
    }

    /**
	 * @param temp
	 * @return
	 */
    private char endChar(boolean temp) {
        if (!temp) {
            return endChar;
        } else {
            return tempEndChar;
        }
    }

    /**
	 * Returns true if the given char c is ' or "
	 * 
	 * @param c
	 *            character to test
	 * @return boolean indicating if character is a quote
	 */
    private boolean isQuoteChar(char c) {
        return (c == '\'') || (c == '"');
    }
}
