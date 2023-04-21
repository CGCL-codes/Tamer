package ch.ethz.mxquery.datamodel.adm;

import java.util.Vector;
import ch.ethz.mxquery.datamodel.MXQueryDouble;
import ch.ethz.mxquery.util.Utils;

/**
 * Represents a StringMatch of the Fulltext Data Model: Roughly speaking, a
 * match of query tokens with document tokens
 * 
 * @author Peter
 * 
 */
public class StringMatch {

    LinguisticToken[] myTokens;

    int queryPos;

    MXQueryDouble score = new MXQueryDouble(0);

    public StringMatch(Vector myTokens, int queryPos) {
        LinguisticToken[] toks = new LinguisticToken[myTokens.size()];
        for (int i = 0; i < myTokens.size(); i++) {
            toks[i] = (LinguisticToken) myTokens.elementAt(i);
        }
        this.myTokens = toks;
        this.queryPos = queryPos;
    }

    /** 
     * Build a new StringMatch by "coercing" all given StringMatches into one
     * @param toMerge
     */
    public StringMatch(StringMatch[] toMerge) {
        Vector allToks = new Vector();
        for (int i = 0; i < toMerge.length; i++) {
            allToks = Utils.addArrayToVector(toMerge[i].myTokens, allToks);
        }
        myTokens = new LinguisticToken[allToks.size()];
        for (int i = 0; i < myTokens.length; i++) {
            myTokens[i] = (LinguisticToken) allToks.elementAt(i);
        }
        queryPos = toMerge[0].queryPos;
        score = new MXQueryDouble(0);
    }

    public StringMatch(LinguisticToken[] myTokens, int queryPos) {
        this.myTokens = myTokens;
        this.queryPos = queryPos;
    }

    /**
     * Get the start position of the StringMatch
     * 
     * @return the integer position of the start
     */
    public int getStartPos() {
        return myTokens[0].getPosition();
    }

    /**
     * Get the end position of the StringMatch
     * 
     * @return the integer position of the end
     */
    public int getEndPos() {
        return myTokens[myTokens.length - 1].getPosition();
    }

    /**
     * Get the start sentence of the StringMatch
     * 
     * @return the integer position of the start
     */
    public int getStartSentence() {
        return myTokens[0].getSentence();
    }

    /**
     * Get the end sentence of the StringMatch
     * 
     * @return the integer position of the end
     */
    public int getEndSentence() {
        return myTokens[myTokens.length - 1].getSentence();
    }

    /**
     * Get the positions of all tokens in the string match
     * @return the values of all positions
     */
    public int[] getAllPositions() {
        int[] res = new int[myTokens.length];
        for (int i = 0; i < myTokens.length; i++) res[i] = myTokens[i].getPosition();
        return res;
    }

    /**
     * Get the start paragraph of the StringMatch
     * 
     * @return the integer position of the start
     */
    public int getStartParagraph() {
        return myTokens[0].getParagraph();
    }

    /**
     * Get the end paragraph of the StringMatch
     * 
     * @return the integer position of the end
     */
    public int getEndParagraph() {
        return myTokens[myTokens.length - 1].getParagraph();
    }

    public int[] getAllLevels() {
        int[] res = new int[myTokens.length];
        for (int i = 0; i < myTokens.length; i++) res[i] = myTokens[i].getDeweyId().getDeweyLevel();
        return res;
    }

    public String[] getAllValues() {
        String[] res = new String[myTokens.length];
        for (int i = 0; i < myTokens.length; i++) res[i] = myTokens[i].getText();
        return res;
    }
}
