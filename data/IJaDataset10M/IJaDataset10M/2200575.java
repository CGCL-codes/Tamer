package koopa.tokenizers.cobol;

import java.io.IOException;
import koopa.tokenizers.Tokenizer;
import koopa.tokenizers.cobol.tags.AreaTag;
import koopa.tokenizers.cobol.tags.SyntacticTag;
import koopa.tokenizers.util.ThreadedTokenizerBase;
import koopa.tokens.Token;
import org.apache.log4j.Logger;

public class SeparatorTokenizer extends ThreadedTokenizerBase implements Tokenizer {

    private static final Logger LOGGER = Logger.getLogger("tokenising.separators");

    private Tokenizer tokenizer = null;

    public SeparatorTokenizer(Tokenizer tokenizer) {
        super();
        assert (tokenizer != null);
        this.tokenizer = tokenizer;
    }

    protected void tokenize() throws IOException {
        while (!hasQuit()) {
            final Token token = tokenizer.nextToken();
            if (token == null) {
                break;
            } else if (token.hasTag(AreaTag.PROGRAM_TEXT_AREA) && !token.hasTag(AreaTag.COMMENT)) {
                separate(token);
            } else {
                enqueue(token);
            }
        }
    }

    private void separate(final Token token) {
        final String text = token.getText();
        final int length = text.length();
        int position = 0;
        while (position < length) {
            final char c = text.charAt(position);
            if (isSeparatorFollowedBySpace(text, length, position, c)) {
                position = separator(token, c, position);
                if (position < length) {
                    position = whitespace(token, text, position, length);
                }
            } else if (isSeparator(c)) {
                position = separator(token, c, position);
            } else if (isWhitespace(c)) {
                position = whitespace(token, text, position, length);
            } else if (startOfInlineComment(text, position, length, c)) {
                position = inlineComment(token, position, length);
            } else if (startsStringLiteral(c)) {
                position = stringLiteral(token, text, position, length, c);
            } else if (isPseudoLiteralMarker(text, length, position, c)) {
                position = pseudoLiteral(token, position);
            } else if (startOfHexadecimal(text, length, position, c)) {
                position = hexadecimal(token, text, position, length, text.charAt(position + 1));
            } else if (startOfSignedNumber(text, position, length, c)) {
                position = signedNumber(token, text, position, length);
            } else if (startsDotDecimal(text, length, position, c)) {
                position = dotDecimal(token, text, position, length);
            } else if (isLetterOrDigit(c)) {
                position = cobolWordOrNumber(token, text, position, length);
            } else {
                position = other(token, text, position, length);
            }
        }
    }

    private boolean startsDotDecimal(final String text, final int length, int position, final char c) {
        return c == '.' && position + 1 < length && isDigit(text.charAt(position + 1));
    }

    private boolean isPseudoLiteralMarker(final String text, final int length, int position, final char c) {
        return c == '=' && position + 1 < length && text.charAt(position + 1) == '=';
    }

    private boolean startsStringLiteral(final char c) {
        return c == '"' || c == '\'';
    }

    private boolean isWhitespace(final char c) {
        return c == ' ' || c == '\t';
    }

    private boolean isSeparator(final char c) {
        return c == '(' || c == ')' || c == ':';
    }

    private boolean isSeparatorFollowedBySpace(final String text, final int length, int position, final char c) {
        return (c == ',' || c == ';' || c == '.') && (position + 1 == length || text.charAt(position + 1) == ' ');
    }

    private boolean startOfHexadecimal(final String text, final int length, int position, final char c) {
        return (c == 'x' || c == 'X') && position + 1 < length && (text.charAt(position + 1) == '"' || text.charAt(position + 1) == '\'');
    }

    private boolean startOfInlineComment(final String text, int position, final int length, final char c) {
        return c == '*' && position + 1 < length && text.charAt(position + 1) == '>';
    }

    private boolean startOfSignedNumber(final String text, final int position, final int length, final char first) {
        if (first != '+' && first != '-') {
            return false;
        }
        if (position + 1 >= length) {
            return false;
        }
        final char d = text.charAt(position + 1);
        if (isDigit(d)) {
            return true;
        }
        if (d != '.') {
            return false;
        }
        if (position + 2 >= length) {
            return false;
        }
        final char e = text.charAt(position + 2);
        return isDigit(e);
    }

    private int separator(final Token token, final char c, final int start) {
        final Token separator = token.subtoken(start, start + 1);
        separator.addTag(AreaTag.PROGRAM_TEXT_AREA);
        separator.addTag(SyntacticTag.SEPARATOR);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Separator: " + separator);
        }
        enqueue(separator);
        return start + 1;
    }

    private int whitespace(final Token token, final String text, final int start, final int length) {
        int position = start + 1;
        while (position < length && isWhitespace(text.charAt(position))) {
            position += 1;
        }
        final Token separator = token.subtoken(start, position);
        separator.addTag(AreaTag.PROGRAM_TEXT_AREA);
        separator.addTag(SyntacticTag.SEPARATOR);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Separator: " + separator);
        }
        enqueue(separator);
        return position;
    }

    private int stringLiteral(final Token token, final String text, final int start, final int length, final char quotationMark) {
        int position = start + 1;
        while (position < length) {
            final char c = text.charAt(position);
            if (c != quotationMark) {
                position += 1;
                continue;
            }
            if (position + 1 == length) {
                final Token stringliteral = token.subtoken(start);
                stringliteral.addTag(AreaTag.PROGRAM_TEXT_AREA);
                stringliteral.addTag(SyntacticTag.CHARACTER_STRING);
                stringliteral.addTag(SyntacticTag.STRING_LITERAL);
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("String literal: " + stringliteral);
                }
                enqueue(stringliteral);
                return position + 1;
            }
            final char d = text.charAt(position + 1);
            if (d == quotationMark) {
                position += 2;
                continue;
            }
            final Token stringliteral = token.subtoken(start, position + 1);
            stringliteral.addTag(AreaTag.PROGRAM_TEXT_AREA);
            stringliteral.addTag(SyntacticTag.CHARACTER_STRING);
            stringliteral.addTag(SyntacticTag.STRING_LITERAL);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("String literal: " + stringliteral);
            }
            enqueue(stringliteral);
            return position + 1;
        }
        final Token stringliteral = token.subtoken(start);
        stringliteral.addTag(AreaTag.PROGRAM_TEXT_AREA);
        stringliteral.addTag(SyntacticTag.CHARACTER_STRING);
        stringliteral.addTag(SyntacticTag.STRING_LITERAL);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Incomplete string literal: " + stringliteral);
        }
        enqueue(stringliteral);
        return length;
    }

    private int inlineComment(final Token token, final int start, final int length) {
        final Token comment = token.subtoken(start);
        comment.removeTag(AreaTag.PROGRAM_TEXT_AREA);
        comment.addTag(AreaTag.COMMENT);
        comment.addTag(SyntacticTag.CHARACTER_STRING);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Inline comment: " + comment);
        }
        enqueue(comment);
        return length;
    }

    private int pseudoLiteral(final Token token, final int start) {
        final Token pseudoLiteral = token.subtoken(start, start + 2);
        pseudoLiteral.addTag(AreaTag.PROGRAM_TEXT_AREA);
        pseudoLiteral.addTag(SyntacticTag.SEPARATOR);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Pseudo literal separator: " + pseudoLiteral);
        }
        enqueue(pseudoLiteral);
        return start + 2;
    }

    private int hexadecimal(final Token token, final String text, final int start, final int length, final char quotationMark) {
        int position = start + 2;
        while (position < length) {
            final char c = text.charAt(position);
            if (c != quotationMark) {
                position += 1;
                continue;
            }
            final Token hexadecimal = token.subtoken(start, position + 1);
            hexadecimal.addTag(AreaTag.PROGRAM_TEXT_AREA);
            hexadecimal.addTag(SyntacticTag.CHARACTER_STRING);
            hexadecimal.addTag(SyntacticTag.HEXADECIMAL_LITERAL);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Hexadecimal: " + hexadecimal);
            }
            enqueue(hexadecimal);
            return position + 1;
        }
        final Token hexadecimal = token.subtoken(start);
        hexadecimal.addTag(AreaTag.PROGRAM_TEXT_AREA);
        hexadecimal.addTag(SyntacticTag.CHARACTER_STRING);
        hexadecimal.addTag(SyntacticTag.HEXADECIMAL_LITERAL);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Incomplete hexadecimal: " + hexadecimal);
        }
        enqueue(hexadecimal);
        return length;
    }

    private int signedNumber(final Token token, final String text, final int start, final int length) {
        final int lengthOfNumber = tryNumericLiteral(text, start + 1, length);
        final Token numericLiteral = token.subtoken(start, start + 1 + lengthOfNumber);
        numericLiteral.addTag(AreaTag.PROGRAM_TEXT_AREA);
        numericLiteral.addTag(SyntacticTag.CHARACTER_STRING);
        numericLiteral.addTag(isDecimal(numericLiteral) ? SyntacticTag.DECIMAL_LITERAL : SyntacticTag.INTEGER_LITERAL);
        numericLiteral.addTag(SyntacticTag.SIGNED);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Signed numeric literal: " + numericLiteral);
        }
        enqueue(numericLiteral);
        return start + 1 + lengthOfNumber;
    }

    private int dotDecimal(final Token token, final String text, final int start, final int length) {
        int position = start + 1;
        while (position < length) {
            final char c = text.charAt(position);
            if (isDigit(c)) {
                position += 1;
                continue;
            }
            final Token dotDecimal = token.subtoken(start, position);
            dotDecimal.addTag(AreaTag.PROGRAM_TEXT_AREA);
            dotDecimal.addTag(SyntacticTag.CHARACTER_STRING);
            dotDecimal.addTag(SyntacticTag.DECIMAL_LITERAL);
            dotDecimal.addTag(SyntacticTag.UNSIGNED);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Dot decimal: " + dotDecimal);
            }
            enqueue(dotDecimal);
            return position;
        }
        final Token dotDecimal = token.subtoken(start);
        dotDecimal.addTag(AreaTag.PROGRAM_TEXT_AREA);
        dotDecimal.addTag(SyntacticTag.CHARACTER_STRING);
        dotDecimal.addTag(SyntacticTag.DECIMAL_LITERAL);
        dotDecimal.addTag(SyntacticTag.UNSIGNED);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Dot decimal: " + dotDecimal);
        }
        enqueue(dotDecimal);
        return length;
    }

    private int cobolWordOrNumber(final Token token, final String text, final int start, final int length) {
        int match = tryCobolWord(text, start, length);
        if (match > 0) {
            final Token cobolWord = token.subtoken(start, start + match);
            cobolWord.addTag(AreaTag.PROGRAM_TEXT_AREA);
            cobolWord.addTag(SyntacticTag.CHARACTER_STRING);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Cobol word: " + cobolWord);
            }
            enqueue(cobolWord);
            return start + match;
        }
        match = tryNumericLiteral(text, start, length);
        if (match > 0) {
            final Token numericLiteral = token.subtoken(start, start + match);
            numericLiteral.addTag(AreaTag.PROGRAM_TEXT_AREA);
            numericLiteral.addTag(SyntacticTag.CHARACTER_STRING);
            numericLiteral.addTag(isDecimal(numericLiteral) ? SyntacticTag.DECIMAL_LITERAL : SyntacticTag.INTEGER_LITERAL);
            numericLiteral.addTag(SyntacticTag.UNSIGNED);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Unsigned numeric literal: " + numericLiteral);
            }
            enqueue(numericLiteral);
            return start + match;
        }
        return start;
    }

    private boolean isDecimal(Token numericLiteral) {
        final String text = numericLiteral.getText();
        return text.indexOf('.') >= 0 || text.indexOf(',') >= 0;
    }

    private int tryCobolWord(final String text, final int start, final int length) {
        boolean sawLetter = false;
        int lastLetterOrDigit = start - 1;
        int position = start;
        while (position < length) {
            final char c = text.charAt(position);
            if (isLetter(c)) {
                sawLetter = true;
                lastLetterOrDigit = position;
                position += 1;
                continue;
            }
            if (isDigit(c)) {
                lastLetterOrDigit = position;
                position += 1;
                continue;
            }
            if (c == '-' || c == '_') {
                position += 1;
                continue;
            }
            break;
        }
        if (position == start || !sawLetter) {
            return 0;
        }
        return lastLetterOrDigit - start + 1;
    }

    private int tryNumericLiteral(final String text, final int start, final int length) {
        boolean seenDecimalPoint = false;
        int lastDigit = start - 1;
        int position = start;
        while (position < length) {
            final char c = text.charAt(position);
            if (isDigit(c)) {
                lastDigit = position;
                position += 1;
                continue;
            }
            if (c == '.' || c == ',') {
                if (seenDecimalPoint) {
                    break;
                }
                seenDecimalPoint = true;
                position += 1;
                continue;
            }
            break;
        }
        if (position == start) {
            return 0;
        }
        return lastDigit - start + 1;
    }

    private boolean isLetterOrDigit(char c) {
        return isLetter(c) || isDigit(c);
    }

    private boolean isLetter(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
    }

    private boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    private int other(final Token token, final String text, final int start, final int length) {
        int position = start;
        while (position < length) {
            final char c = text.charAt(position);
            if (isSeparatorFollowedBySpace(text, length, position, c)) {
                break;
            } else if (isSeparator(c)) {
                break;
            } else if (isWhitespace(c)) {
                break;
            } else if (startOfInlineComment(text, position, length, c)) {
                break;
            } else if (startsStringLiteral(c)) {
                break;
            } else if (isPseudoLiteralMarker(text, length, position, c)) {
                break;
            } else if (startOfHexadecimal(text, length, position, c)) {
                break;
            } else if (startOfSignedNumber(text, position, length, c)) {
                break;
            } else if (startsDotDecimal(text, length, position, c)) {
                break;
            } else if (isLetterOrDigit(c)) {
                break;
            } else {
                position += 1;
            }
        }
        final Token separator = token.subtoken(start, position);
        separator.addTag(AreaTag.PROGRAM_TEXT_AREA);
        separator.addTag(SyntacticTag.CHARACTER_STRING);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Other: " + separator);
        }
        enqueue(separator);
        return position;
    }

    public void quit() {
        quitMe();
        tokenizer.quit();
    }
}
