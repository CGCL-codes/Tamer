package org.pz.platypus.parser;

import org.pz.platypus.GDD;
import org.pz.platypus.parser.Token;
import java.util.ArrayList;

/**
 * The ArrayList of Tokens that we generate in the parser and pass
 * to the output plug-in.
 *
 * @author alb
 */
public class TokenList extends ArrayList<Token> {

    public TokenList() {
    }

    ;

    /**
     * Gets the next token after the token pointed to by the tokenNumber
     *
     * @param tokNumber the number of the current token
     * @return the token after the current token. Null, if there's an error or the current token
     *         is the last token
     */
    public Token getNextToken(int tokNumber) {
        if (tokNumber < 0) {
            return (null);
        }
        Token nextTok;
        try {
            nextTok = get(tokNumber + 1);
        } catch (IndexOutOfBoundsException ioe) {
            return (null);
        }
        return (nextTok);
    }

    /**
     * Returns the token previous to the one whose number is passed in
     *
     * @param tokNum the number of the current token
     * @return the token priort to the current token, or null if an error occurred.
     */
    public Token getPrevToken(int tokNum) {
        if (tokNum <= 0) {
            return (null);
        }
        return (get(tokNum - 1));
    }

    public Token getLastToken() {
        if (this.size() == 0) return (null);
        return (this.get(this.size() - 1));
    }

    /**
     * Gets all the preceding tokens in this input line and sees whether any
     * of them contain text.
     *
     * @param tokNum the present token
     * @return true if text is emitted false if not
     */
    public boolean lineSoFarEmitsText(int tokNum) {
        assert (tokNum >= 0 && tokNum < this.size());
        int i;
        Token currTok = (Token) get(tokNum);
        Token newTok = null;
        for (i = tokNum; i >= 0; i--) {
            newTok = (Token) get(i);
            if (!currTok.sourceEquals(newTok)) {
                break;
            }
        }
        if (!(i == 0 && (newTok != null && currTok.sourceEquals(newTok)))) {
            i++;
        }
        if (i == tokNum) {
            return (false);
        }
        for (int j = i; j < tokNum; j++) {
            Token tok = get(j);
            if (tok.getType() == TokenType.TEXT) {
                return (true);
            }
        }
        return (false);
    }

    /**
     * dumps all the tokens in the output token list to System.out
     *
     * @param gdd the GDD
     */
    public void dump(final GDD gdd) {
        Token t;
        final Object[] toks = this.toArray();
        for (int i = 0; i < toks.length; i++) {
            t = (Token) toks[i];
            System.out.println(t.toString(gdd));
        }
    }

    /**
     * See if next immediate token (if any), is of type tokType.
     * @param i the current position
     * @param tokType tokType to compare with
     * @return if i+1 th token type is equal to tokType.
     */
    public boolean isNextToken(int i, TokenType tokType) {
        Token nextTok = getNextToken(i);
        if (nextTok == null) return false;
        return nextTok.getType().equals(tokType);
    }

    /**
     * See if the next immediate token (if any), is equal to s.
     * @param i the current position
     * @param s the token str
     * @return if i+1 th token contents are equal to s.
     */
    public boolean areNextTokenContentsEqualTo(int i, String s) {
        Token nextTok = getNextToken(i);
        if (nextTok == null) return false;
        return nextTok.getContent().equals(s);
    }

    /** Start searching for tokType token in the list.
     *  Search from currTokIndex + 1 to the end.
     *
     * @param currTokIndex
     * @param tokType
     * @return the index at which tokType is found
     */
    public int searchAheadFor(int currTokIndex, TokenType tokType) {
        for (int i = currTokIndex + 1; i < size(); i++) {
            Token nextTok = get(i);
            if (nextTok.getType() == tokType) {
                return i;
            }
        }
        throw new IllegalArgumentException("Token " + tokType + " not found");
    }
}
