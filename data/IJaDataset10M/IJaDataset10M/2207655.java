package com.nibble.scriptengine;

import java.util.EnumSet;
import java.util.List;
import com.nibble.scriptengine.NibbleOperators.Operator;
import com.nibble.tools.StringList;

/**
 * The class that finds and returns keywords in a line of code. These can be: identifiers (variables), operators,
 * while..do, begin..end, and statement-end (';') etc. etc.<br />
 * <br />
 * Takes a keyword from the line of code, stores it as token and removes it from the line.
 * 
 * @author Benny Bottema
 */
final class Scanner implements NibbleLexicon {

    /**
	 * List with all words that are initially identified as a keyword or identifier but should be excluded because they
	 * really are something else (like an operator such as <code>instanceof</code>.
	 */
    private static final String[] excludedAlpha = { Operator.INSTANCEOF.toString() };

    /**
	 * Token type when no token was found (indicates absence of a token).
	 */
    public static final String NONE = "none";

    /**
	 * The stringlist containing the lines of code (tokens).
	 */
    private StringList lines;

    /**
	 * Indicates if passive comment ('//') is turned on
	 */
    public boolean passiveComment = false;

    /**
	 * The value of the token when nextToken() is called.
	 */
    private String value;

    /**
	 * The type of the token when nextToken() is called.
	 */
    private String type;

    /**
	 * The value of the previous found token.
	 */
    private String _value;

    /**
	 * The current line of code being processed.
	 */
    private String line = "";

    /**
	 * The current line of code actually being processed.
	 */
    private String cline = "";

    /**
	 * Checks if character is from the alphabet.
	 * 
	 * @param c The character to check
	 * @return boolean Whether given character is an alphabet value
	 */
    private static boolean isAlpha(final char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z' || c == '_');
    }

    /**
	 * Checks if character is an integer or float.
	 * 
	 * @param c The character to check
	 * @return boolean Whether given character is a numerical value or floating point
	 */
    private static boolean isNum(final char c) {
        return isKeyNum(c) || (c == FLOAT);
    }

    /**
	 * Checks if character is is numerical.
	 * 
	 * @param c The character to check
	 * @return boolean Whether given character is a numerical value
	 */
    private static boolean isKeyNum(final char c) {
        return (c >= '0' && c <= '9');
    }

    /**
	 * Constructor; takes a list of codelines and starts looking for the first token.
	 * 
	 * @param scriptlines The stringlist containing the lines of code (tokens)
	 */
    public Scanner(final List<String> scriptlines) {
        lines = new StringList(scriptlines);
        nextToken();
    }

    private void setType(final String s) {
        type = s;
    }

    private void setValue(final String v) {
        if (value != null) {
            if (!value.equals("")) {
                _value = value;
            }
        }
        value = v;
    }

    /**
	 * Returns the token scanned before the current token.
	 * 
	 * @return The last token scanned before the current token.
	 */
    public final String prevToken() {
        return _value;
    }

    /**
	 * Returns the type of the current token
	 * 
	 * @return The type of the current token
	 */
    public final String tokenType() {
        return type;
    }

    /**
	 * Returns the value of the current token
	 * 
	 * @return The value of the current token
	 */
    public final String tokenValue() {
        return value;
    }

    /**
	 * Returns the line number of the current token
	 * 
	 * @return The line number of the current token
	 */
    public final int lineNr() {
        return lines.getNr();
    }

    /**
	 * Returns the line of code being processed
	 * 
	 * @return The line of code being processed
	 */
    public final String getLine() {
        return cline;
    }

    /**
	 * Tries to find the next keyword and matches it against given token.
	 * 
	 * @param tokentype The tokentype nextToken must match
	 * @throws ScanException
	 * @see Scanner#nextToken()
	 * @see Scanner#prevToken()
	 */
    public final void nextToken(final String tokentype) throws ScanException {
        nextToken();
        if (!tokenType().equals(tokentype)) {
            throw new ScanException("invalid token '" + tokenType() + "', expected " + tokentype);
        }
    }

    /**
	 * Tries to find the next keyword it recogonizes from the value-arrays. The token is attempted to be recognized in
	 * the following order.
	 * <ol>
	 * <li>identifier (ie. <code>someVariable</code>)</li>
	 * <li>keyword (ie. <code>import</code>)</li>
	 * <li>numerical (ie. <code>12345</code>)</li>
	 * <li>string (ie. <code>"example string"</code>)</li>
	 * <li>commentor (ie. <code>// this is a comment</code>)</li>
	 * <li>post/prefix operator (ie. <code>++</code>)</li>
	 * <li>compound operator (ie. <code>-=</code>)</li>
	 * <li>operator (ie. <code>*</code>)</li>
	 * <li>syntactic words (ie. <code>[</code>)</li>
	 * <li>unknown char (ie. any non-recognized character <code>#</code>)</li>
	 * </ol>
	 * 
	 * @see Scanner#extractKeywordOrIdentifier()
	 * @see Scanner#extractNumber()
	 * @see Scanner#extractOperator(EnumSet)
	 * @see Scanner#extractString()
	 * @see Scanner#extractToken(String, String)
	 * @see Scanner#extractToken(String[], String)
	 * @see Scanner#excludedAlpha
	 */
    public final void nextToken() {
        setType(NONE);
        setValue("");
        while (true) {
            if (line == null) {
                return;
            } else if ((line = line.trim()).length() > 0) {
                break;
            } else {
                cline = line = lines.nextString();
                passiveComment = false;
            }
        }
        final boolean keywordOrIdentifier = isAlpha(line.charAt(0));
        boolean isExcluded = false;
        if (keywordOrIdentifier) {
            extractKeywordOrIdentifier();
            if (isExcluded = tokenType().equals(IDENTIFIER) && isExcludedIdentifier(tokenValue())) {
                line = tokenValue() + line;
            }
        }
        if (!keywordOrIdentifier || isExcluded) {
            if (isKeyNum(line.charAt(0))) extractNumber(); else if (line.charAt(0) == STRING) extractString(); else if (!extractToken(commentors, null)) {
                if (!extractOperator(Operator.fix)) {
                    if (!extractOperator(Operator.compound)) {
                        if (!extractOperator(Operator.extended)) {
                            if (!extractOperator(Operator.single)) {
                                if (!extractToken(other, null)) {
                                    setValue(line.substring(0, 1));
                                    setType(tokenValue());
                                    line = line.substring(1, line.length());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
	 * Determines whether a token occurs in a list of excluded tokens.
	 * 
	 * @param token The token check for exclusion.
	 * @return Whether the specified token is excluded as identifier
	 */
    private boolean isExcludedIdentifier(final String token) {
        for (final String excluded : excludedAlpha) {
            if (token.equals(excluded)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Checks whether token is a keyword or an identifier. Keywords are 'while', 'if', 'case' etc. Assumes it is an
	 * identifier until it matches a keyword
	 */
    private void extractKeywordOrIdentifier() {
        int wordLength = determineWordLength(line);
        setValue(extractToken(wordLength, 0));
        if (!matchKeywordAndSetType(keywords, keytypes)) {
            if (!matchKeywordAndSetType(words, words)) {
                if (!matchKeywordAndSetType(classKeywords, classKeytypes)) {
                    if (!matchKeywordAndSetType(classWords, classWords)) {
                        setType(IDENTIFIER);
                    }
                }
            }
        }
    }

    /**
	 * Determines the number of characters the next token has.
	 * 
	 * @param line The line of code to scan for the next token.
	 * @return The length of the token that can be extracted from the current line of code.
	 */
    private int determineWordLength(final String line) {
        int wordLength = 0;
        int lineLength = line.length();
        while (wordLength < lineLength && (isAlpha(line.charAt(wordLength)) || isKeyNum(line.charAt(wordLength)))) {
            wordLength++;
        }
        return wordLength;
    }

    /**
	 * Runs through a list of words and checks if one of them matches the current token being scanned. If matched the
	 * parallel semantic type is set from the second array.
	 * 
	 * @param words The list of words to match against the current token being scanned.
	 * @param types The semantic types that go with the specified list with words to match.
	 * @return Returns whether this function was able to match a word and set its semantic type.
	 */
    private boolean matchKeywordAndSetType(final String[] words, final String[] types) {
        for (int i = 0; i < words.length; i++) {
            if (tokenValue().equals(words[i])) {
                setType(types[i]);
                return true;
            }
        }
        return false;
    }

    /**
	 * Extracts a complete number. Keeps adding the integer until it counters a non-integer.
	 */
    private void extractNumber() {
        final int numberLength = determineNumberLength(line);
        setValue(extractToken(numberLength, 0));
        setType((tokenValue().indexOf(FLOAT) == -1) ? LITERAL_INTEGER : LITERAL_FLOAT);
    }

    /**
	 * Refer to {@link #determineWordLength(String)} for information.
	 * 
	 * @see #determineWordLength(String)
	 */
    private int determineNumberLength(final String line) {
        int numberLength = 0;
        final int lineLength = line.length();
        while (numberLength < lineLength && isNum(line.charAt(numberLength))) {
            numberLength++;
        }
        return numberLength;
    }

    /**
	 * Extracts a complete string. Keeps adding the character until it counters the end of the string.
	 */
    private void extractString() {
        int stringLength = determineStringLength(line);
        setValue(extractToken(stringLength, 1));
        setType(LITERAL_STRING);
    }

    /**
	 * Refer to {@link #determineWordLength(String)} for information.
	 * 
	 * @see #determineWordLength(String)
	 */
    private int determineStringLength(final String line) {
        int stringLength = 1;
        final int lineLength = line.length();
        boolean escape = false;
        while (stringLength < lineLength && (escape || line.charAt(stringLength) != STRING)) {
            escape = !escape && line.charAt(stringLength) == ESCAPE;
            stringLength++;
        }
        return stringLength;
    }

    /**
	 * Removes next token from the line of code being scanned and return the token that was ultimately extracted.
	 * 
	 * @param tokenLength The length of the token that can be extracted.
	 * @param offSet Indicates where the token starts (useful to get string values without the quotes).
	 * @return The token that was actually extracted from the line of code currently being scanned.
	 */
    private String extractToken(int tokenLength, final int offSet) {
        final int lineLength = line.length();
        final String token = line.substring(offSet, tokenLength);
        line = line.substring(tokenLength + offSet, lineLength);
        return token;
    }

    /**
	 * Runs through an list of {@link Operator}s checks if the next token matches one of these. If so, it stores its
	 * value, and sets its type to the specified linguistic type, or the same as its value if it not applicable.
	 * 
	 * @param operators The <code>EnumSet</code> containing the list with <code>Operators</code>.
	 * @return A boolean value indicating a match was found and token was stored.
	 */
    private boolean extractOperator(final EnumSet<Operator> operators) {
        for (final Operator o : operators) {
            if (extractToken(o.toString(), o.getType().toString())) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Runs through an array with valid tokens and checks if the next word matches one of these. If so, it stores its
	 * value, and sets its type to the specified token type, or the same as its value if ttype is null.
	 * 
	 * @param tokens The array with possible / valid tokens.
	 * @param tokensType The type of any of the specified tokens, if there is a match in the current line of code.
	 * @return A boolean value indicating a match was found and token was stored.
	 * @see Scanner#extractToken(String, String)
	 * @see Scanner#extractToken(String[], String)
	 */
    private boolean extractToken(final String[] tokens, final String tokensType) {
        for (final String token : tokens) {
            if (extractToken(token, tokensType)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Checks the current line of code for a single token. If the line starts with the specified token, it is removed
	 * from the line, stored as token value and the specified token type is stored along with the value.
	 * 
	 * @param tokenType The symantical value of the token, if found.
	 * @param token The token to look for.
	 * @return Whether the specified token was found in the current line of code and stored.
	 */
    private boolean extractToken(final String token, final String tokenType) {
        if (line.startsWith(token)) {
            setValue(token);
            setType((tokenType == null) ? token : tokenType);
            line = line.substring(tokenValue().length(), line.length());
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Restores the last token. This is useful when the parser needs to 'peek' at the next token, without skipping one
	 * in the process.
	 * 
	 * @param token The token that needs to be restored
	 */
    public final void backToken(final String token) {
        line = token + value + line;
        nextToken();
    }
}
