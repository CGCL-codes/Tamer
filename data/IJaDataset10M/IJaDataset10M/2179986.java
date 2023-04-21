package org.neurpheus.nlp.morphology.baseimpl;

import java.util.ArrayList;
import java.util.Locale;
import org.neurpheus.nlp.morphology.AnalysisResult;
import org.neurpheus.nlp.morphology.MorphologicalComponent;
import org.neurpheus.nlp.morphology.MorphologyException;
import org.neurpheus.nlp.morphology.QualityConst;
import org.neurpheus.nlp.morphology.SpeedConst;

/**
 *
 * @author jstrychowski
 */
public class TestStemmer extends AbstractStemmer {

    private int quality = QualityConst.POOR;

    private int speed = SpeedConst.FAST;

    private Locale locale;

    public TestStemmer() {
        ArrayList locales = new ArrayList();
        locales.add(new Locale("pl"));
        locales.add(Locale.ENGLISH);
        locales.add(Locale.FRENCH);
        setSupportedLocales(locales);
        setProperty("name", "TestStemmer");
    }

    public TestStemmer(Locale language) {
        locale = language;
        ArrayList locales = new ArrayList();
        locales.add(language);
        setSupportedLocales(locales);
        setProperty("name", "TestStemmer");
    }

    public int getQuality() {
        return quality;
    }

    public int getSpeed() {
        return speed;
    }

    public MorphologicalComponent getInstance(Locale locale) throws MorphologyException {
        return new TestStemmer(locale);
    }

    public AnalysisResult[] getStemmingResults(String wordForm) throws MorphologyException {
        AnalysisResult[] result = new AnalysisResult[3];
        result[0] = new AnalysisResultImpl(wordForm + "_1", 0.7);
        result[1] = new AnalysisResultImpl(wordForm + "_2", 0.2);
        result[2] = new AnalysisResultImpl(wordForm + "_3", 0.1);
        return result;
    }
}
