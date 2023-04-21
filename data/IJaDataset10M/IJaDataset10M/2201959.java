package wssearch.textmining;

import java.util.HashSet;

/**
 * Interface für Beschreibungs-Textmining-Strategien
 * @author Thorsten Theelen
 *
 */
public interface DescriptionMiningStrategy {

    /**
	 * Zerlegt einen String in eine Menge von Schlüsselwörtern
	 * @param s zu zerlegender String
	 * @return Schlüsselwortmenge
	 */
    public HashSet<String> getWordSet(String s);
}
