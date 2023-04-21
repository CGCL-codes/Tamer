package wssearch.textmining;

import java.util.HashSet;

/**
 * Strategie für Beschreibungs-Textmining: String aufteilen, Stoppwörter entfernen,
 * Stemming, nochmal Stoppwörter entfernen
 * @author Thorsten Theelen
 *
 */
public class DMiningSplitStopwStemStopw implements DescriptionMiningStrategy {

    public HashSet<String> getWordSet(String s) {
        TextMining tm = TextMining.getInstance();
        return tm.removeStopWords(tm.stemmer(tm.removeStopWords(tm.stringToHashset(s))));
    }
}
