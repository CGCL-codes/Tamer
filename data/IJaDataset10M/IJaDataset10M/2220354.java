package ar.com.ktulu.dict.strategies;

import ar.com.ktulu.dict.Key;
import java.text.CollationKey;
import java.util.ArrayList;

/**
 * Matches words using the work searched for as a substring of the words in the
 * database.
 *
 * @author Luis Parravicini <webmaster@ktulu.com.ar>
 */
public class SubStringStrategy extends Strategy {

    public SubStringStrategy() {
        super("substring", "Match substring occurring anywhere in " + "the word");
    }

    public ArrayList getMatches(String word, Key[] keys) {
        if (keys.length == 0) return null;
        ArrayList results = new ArrayList();
        CollationKey k = keys[0].getCollator().getCollationKey(word);
        for (int i = 0; i < keys.length; i++) if (keys[i].getWord().toLowerCase().indexOf(word.toLowerCase()) != -1) results.add(keys[i]);
        return (results.size() == 0 ? null : results);
    }

    public String asSQL(String word) {
        return "UPPER(WORD) LIKE '%'||UPPER('" + word + "')||'%'";
    }
}
