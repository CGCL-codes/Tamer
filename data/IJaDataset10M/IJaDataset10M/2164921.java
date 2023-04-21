package softwareengineering.tokeniser;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import softwareengineering.Config;

/**
 * Split a string containing the contents of an HTML file up into
 * atomic chunks. Specifcally it splits allong white-space and at
 * special char boundries.
 *
 * @version 1.00, 08 June 2009
 */
public class Tokeniser {

    /**
     * Log message handler
     */
    private static final Logger LOG = Logger.getLogger(Tokeniser.class.getName());

    /**
     * Array of special sequences that should be split allong
     */
    private static final String[] SPECIAL_TOKENS = Config.getSpecialTokens();

    /**
     * The string to be tokeniser
     */
    private final String input;

    /**
     * The offset of the current character beeing considered
     */
    private int position;

    /**
     * The current line number
     */
    private int line;

    /**
     * The current column number
     */
    private int column;

    /**
     * Construct a new instance of Tokeniser using the HTML data given
     * in the parameter.
     * 
     * @param fileContents  The HTML data
     */
    public Tokeniser(String fileContents) {
        this.input = fileContents;
        this.position = 0;
        this.line = 0;
        this.column = 0;
    }

    /**
     * Parse the input string into a list of Token objects.
     *
     * @return A list of token objects
     */
    public List<Token> createTokens() {
        LOG.info("BEGIN tokenising the input string.");
        List<Token> tokens = new ArrayList<Token>();
        int i = 0;
        while (i < input.length()) {
            if (isWhitespace(i)) {
                int end = findWhitespaceEnd(i + 1);
                String tok = input.substring(i, end);
                tokens.add(new Token(tok, line, column, true, false));
                i = end;
            } else if (isSpecial(i)) {
                int end = findSpecialEnd(i);
                String tok = input.substring(i, end);
                tokens.add(new Token(tok, line, column, false, true));
                i = end;
            } else {
                int end = scanToken(i);
                String tok = input.substring(i, end);
                tokens.add(new Token(tok, line, column, false, false));
                i = end;
            }
        }
        LOG.info("Found " + tokens.size() + " tokens");
        LOG.info("END tokenising the input string.");
        return tokens;
    }

    /**
     * From the startIndex given, search forward in the input string
     * for then last index of the current token. This will be the
     * first occurance of whitespace of a special deliminator.
     *
     * @param startIndex    The position in the input to start
     * @return  The position in the input for the end
     */
    protected int scanToken(int startIndex) {
        int endIndex = startIndex;
        while (endIndex < input.length()) {
            if (isWhitespace(endIndex)) {
                break;
            }
            if (isSpecial(endIndex)) {
                findSpecialEnd(endIndex);
                break;
            }
            endIndex++;
        }
        column += endIndex - startIndex;
        return endIndex;
    }

    /**
     * From the startIndex given find the last index of a sequence of
     * whitespace characters.
     * 
     * @param startIndex    The position in the input to start
     * @return The position in the input for the end
     */
    protected int findWhitespaceEnd(int startIndex) {
        int i = startIndex;
        while (i < input.length() && isWhitespace(i)) {
            if (isNewLine(i)) {
                line++;
                column = 0;
            } else {
                column++;
            }
            i++;
        }
        return i;
    }

    /**
     * From the startIndex given find the last index of a sequence of
     * characters denoting a special deliminator (eg ">").
     *
     * @param startIndex    The position in the input to start
     * @return The position in the input for the end
     */
    protected int findSpecialEnd(int startIndex) {
        boolean found = false;
        int endIndex = -1;
        for (int i = 0; !found && i < SPECIAL_TOKENS.length; i++) {
            String sym = SPECIAL_TOKENS[i];
            if (input.substring(startIndex).startsWith(sym)) {
                found = true;
                endIndex = startIndex + sym.length();
            }
        }
        if (endIndex != -1) column += endIndex - startIndex;
        return endIndex;
    }

    /**
     * Determine whether the character in the input string at the
     * given index is a new line.
     *
     * @param index The position of the character to check
     * @return True if the character is a new-line, false otherwise.
     */
    protected boolean isNewLine(int index) {
        return input.charAt(index) == '\n';
    }

    /**
     * Determine whether the character in the input string at the
     * given index is a white space character.
     *
     * @param index The position of the character to check
     * @return True if the character is whitespace, false otherwise.
     */
    protected boolean isWhitespace(int index) {
        return Character.isWhitespace(input.charAt(index));
    }

    /**
     * Determine whether the character in the input string at the
     * given index is a special deliminator sequence such as ">".
     *
     * @param index The position to start checking at
     * @return True if it starts at this index, false otherwise
     */
    protected boolean isSpecial(int index) {
        return findSpecialEnd(index) != -1;
    }
}
