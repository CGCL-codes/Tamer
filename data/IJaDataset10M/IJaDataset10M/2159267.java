package ar.com.ktulu.dict.strategies;

import ar.com.ktulu.dict.Key;
import java.text.CollationKey;
import java.util.ArrayList;

/**
 * Matches words using Levenshtein distance one.
 *
 * @see Levenshtein
 * @author Luis Parravicini <webmaster@ktulu.com.ar>
 */
public class LevenshteinStrategy extends Strategy {

    public LevenshteinStrategy() {
        super("lev", "Match words within Levenshtein distance one");
    }

    public ArrayList getMatches(String word, Key[] keys) {
        if (keys.length == 0) return null;
        ArrayList results = new ArrayList();
        CollationKey k = keys[0].getCollator().getCollationKey(word);
        for (int i = 0; i < keys.length; i++) if (Levenshtein.LD(keys[i].getWord().toLowerCase(), word.toLowerCase()) == 1) results.add(keys[i]);
        return (results.size() == 0 ? null : results);
    }

    public String asSQL(String word) {
        return null;
    }
}
