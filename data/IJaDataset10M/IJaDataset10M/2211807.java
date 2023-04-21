package org.donnchadh.esslli2008.statmt;

public class SentencePair {

    private final Sentence englishSentence;

    private final Sentence foreignSentence;

    public SentencePair(Sentence englishSentence, Sentence foreignSentence) {
        this.englishSentence = englishSentence;
        this.foreignSentence = foreignSentence;
    }

    public Sentence getEnglishSentence() {
        return englishSentence;
    }

    public Sentence getForeignSentence() {
        return foreignSentence;
    }
}
