package org.sodbeans.matching.parser;

/**
 *
 * @author Susanna Siebert and Andreas Stefik
 */
public class MatchKeyword {

    private int matchID;

    private int startOffset;

    private int endOffset;

    private int isPrefix;

    public MatchKeyword() {
    }

    public int[] translate() {
        int[] result = new int[4];
        result[0] = matchID;
        result[1] = startOffset;
        result[2] = endOffset;
        result[3] = isPrefix;
        return result;
    }

    public int getMatchID() {
        return matchID;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public boolean getIsPrefix() {
        return isPrefix == 1;
    }

    /**
     * @param matchID the matchID to set
     */
    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }

    /**
     * @param startOffset the startOffset to set
     */
    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    /**
     * @param endOffset the endOffset to set
     */
    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    /**
     * @param isPrefix the isPrefix to set
     */
    public void setIsPrefix(boolean isPrefix) {
        if (isPrefix) {
            this.isPrefix = 1;
        } else {
            this.isPrefix = 0;
        }
    }

    public String toString() {
        int[] translate = this.translate();
        String result = "";
        for (int i = 0; i < translate.length; i++) {
            result += translate[i] + " ";
        }
        return result;
    }
}
