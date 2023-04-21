package ch.ethz.mxquery.sms.ftstore;

/**
 * Helper class for sentence information
 * @author jimhof
 */
public class SentenceToken {

    private int sentenceCount;

    private int startingPosition;

    private int endPosition;

    public SentenceToken(int sentenceCount, int startPos, int endPos) {
        this.sentenceCount = sentenceCount;
        this.startingPosition = startPos;
        this.endPosition = endPos;
    }

    public int getStartingPosition() {
        return this.startingPosition;
    }

    public int getEndPosition() {
        return this.endPosition;
    }

    public int getSentenceCount() {
        return this.sentenceCount;
    }
}
