package edu.umn.cs.nlp.mt.chiang2007.rescoring;

import edu.umn.cs.nlp.LanguageModel;
import edu.umn.cs.nlp.mt.chiang2007.LoglinearTranslationFeatures;

public class TranslationWithLanguageModel {

    private final String translation;

    private final LoglinearTranslationFeatures features;

    public TranslationWithLanguageModel(Translation translation, LanguageModel languageModel) {
        this.translation = translation.toString();
        features = new LoglinearTranslationFeatures(translation.getFeatures(), languageModel.get(this.translation));
    }

    public String toString() {
        return translation;
    }

    public LoglinearTranslationFeatures getFeatures() {
        return features;
    }
}
