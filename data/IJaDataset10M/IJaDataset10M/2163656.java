package org.phramer.v1.decoder;

import java.util.*;
import org.phramer.v1.decoder.*;
import org.phramer.v1.decoder.token.*;

public class PhramerInput {

    public PhramerInput(FToken[] fWords, boolean[] isPreTranslated, int[] phraseLength, List<PhraseTranslationVariant>[] prePhraseTranslations, Map<String, String[]> metaData) {
        this.fWords = fWords;
        this.isPreTranslated = isPreTranslated;
        this.phraseLength = phraseLength;
        this.prePhraseTranslations = prePhraseTranslations;
        this.metaData = metaData;
    }

    public PhramerInput(FToken[] fWords, Map<String, String[]> metaData) {
        this.fWords = fWords;
        this.isPreTranslated = null;
        this.phraseLength = null;
        this.prePhraseTranslations = null;
        this.metaData = metaData;
    }

    private final FToken[] fWords;

    private final boolean isPreTranslated[];

    private final int phraseLength[];

    private final List<PhraseTranslationVariant>[] prePhraseTranslations;

    public boolean hasPreTranslations() {
        return isPreTranslated != null;
    }

    public FToken[] getFWords() {
        return fWords;
    }

    public boolean[] getIsPreTranslated() {
        return isPreTranslated;
    }

    public int[] getPreTranslatedPhraseLength() {
        return phraseLength;
    }

    public List<PhraseTranslationVariant>[] getPrePhraseTranslations() {
        return prePhraseTranslations;
    }

    public static PhramerInput getNonXML(String fSentence, TokenBuilder tokenBuilder, Map<String, String[]> metaData) {
        return new PhramerInput(tokenize(fSentence, tokenBuilder), metaData);
    }

    /** Perform simple tokenization of the input. Uses .intern() */
    private static FToken[] tokenize(String input, TokenBuilder tokenBuilder) {
        return PhramerTools.tokenizeIntoFToken(input, tokenBuilder);
    }

    public Object constraintsDescriptor = null;

    private final Map<String, String[]> metaData;

    public String[] getMetaData(String key) {
        if (metaData != null) return metaData.get(key);
        return null;
    }
}
