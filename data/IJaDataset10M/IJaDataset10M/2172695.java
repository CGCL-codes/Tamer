package de.folt.models.datamodel.phrasetranslate;

/**
 * @author  klemens To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PhraseTranslateResult implements Comparable<PhraseTranslateResult> {

    private int iStartPosition = 0;

    private char sepChar = '!';

    /**
     * @return the sepChar
     */
    public char getSepChar() {
        return sepChar;
    }

    /**
     * @param sepChar the sepChar to set
     */
    public void setSepChar(char sepChar) {
        this.sepChar = sepChar;
    }

    /**
     * 
     */
    private Long[] longPhrase;

    /**
     * 
     */
    private String sourcePhrase;

    /**
     * 
     */
    private String targetPhrase;

    /**
     * 
     */
    public PhraseTranslateResult() {
    }

    /**
     * @param startPosition the start position of this match in the corresponding search phrase
     * @param longPhrase the long Arraya version of the source pharse matching
     * @param targetPhrase the translation of the match
     */
    public PhraseTranslateResult(int startPosition, Long[] longPhrase, String targetPhrase) {
        super();
        iStartPosition = startPosition;
        this.longPhrase = longPhrase;
        this.targetPhrase = targetPhrase;
    }

    /**
     * @param longPhrase
     * @param targetPhrase
     */
    public PhraseTranslateResult(Long[] longPhrase, String targetPhrase) {
        super();
        this.longPhrase = longPhrase;
        this.targetPhrase = targetPhrase;
    }

    /**
     * @param longPhrase the long Array version of the source pharse matching
     * @param sourcePhrase the source string of the match
     * @param targetPhrase the translation of the match
     */
    public PhraseTranslateResult(Long[] longPhrase, String sourcePhrase, String targetPhrase) {
        super();
        this.longPhrase = longPhrase;
        this.sourcePhrase = sourcePhrase;
        this.targetPhrase = targetPhrase;
    }

    /**
     * @param iStartPositioni
     * @param longsearch
     * @param targetPhrase
     */
    public PhraseTranslateResult(int iStartPositioni, String longsearch, String targetPhrase) {
        longsearch = longsearch.replaceFirst(sepChar + "", "");
        String[] longs = longsearch.split(sepChar + "");
        Long[] longpt = new Long[longs.length];
        for (int k = 0; k < longs.length; k++) {
            longpt[k] = Long.parseLong(longs[k]);
        }
        this.longPhrase = longpt;
        this.iStartPosition = iStartPositioni;
        this.targetPhrase = targetPhrase;
    }

    @Override
    public int compareTo(PhraseTranslateResult o) {
        if (this.longPhrase.length >= o.longPhrase.length) return -1; else if (this.longPhrase.length == o.longPhrase.length) return 0;
        return 1;
    }

    /**
     * @return the iStartPosition
     */
    public int getIStartPosition() {
        return iStartPosition;
    }

    /**
     * @return the longPhrase
     */
    public Long[] getLongPhrase() {
        return longPhrase;
    }

    /**
     * @return the sourcePhrase
     */
    public String getSourcePhrase() {
        return sourcePhrase;
    }

    /**
     * @return the targetPhrase
     */
    public String getTargetPhrase() {
        return targetPhrase;
    }

    /**
     * @param iStartPosition the iStartPosition to set
     */
    public void setIStartPosition(int iStartPosition) {
        this.iStartPosition = iStartPosition;
    }

    /**
     * @param longPhrase the longPhrase to set
     */
    public void setLongPhrase(Long[] longPhrase) {
        this.longPhrase = longPhrase;
    }

    /**
     * @param sourcePhrase the sourcePhrase to set
     */
    public void setSourcePhrase(String sourcePhrase) {
        this.sourcePhrase = sourcePhrase;
    }

    /**
     * @param targetPhrase the targetPhrase to set
     */
    public void setTargetPhrase(String targetPhrase) {
        this.targetPhrase = targetPhrase;
    }
}
