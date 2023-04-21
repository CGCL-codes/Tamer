package wssearch.textmining;

import java.util.HashSet;

/**
 * Strategie für Beschreibungs-Textmining: String aufteilen, Stoppwörter entfernen
 * @author Thorsten Theelen
 *
 */
public class DMiningSplitStopw implements DescriptionMiningStrategy {

    public HashSet<String> getWordSet(String s) {
        TextMining tm = TextMining.getInstance();
        return tm.removeStopWords(tm.stringToHashset(s));
    }
}
