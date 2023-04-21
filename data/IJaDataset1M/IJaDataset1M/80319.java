package ar.com.ktulu.dict.strategies;

import ar.com.ktulu.dict.Key;
import java.text.CollationKey;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Does an exact match on the words searched.
 *
 * @author Luis Parravicini <webmaster@ktulu.com.ar>
 */
public class ExactStrategy extends Strategy {

    public ExactStrategy() {
        super("exact", "Match words exactly");
    }

    public ArrayList getMatches(String word, Key[] keys) {
        if (keys.length == 0) return null;
        ArrayList results = new ArrayList();
        CollationKey k = keys[0].getCollator().getCollationKey(word);
        int pos = Arrays.binarySearch(keys, k);
        if (pos < 0) return null;
        int i = pos;
        while (pos >= 0 && k.compareTo(keys[pos].getCollationKey()) == 0) pos--;
        if (pos < 0 || k.compareTo(keys[pos].getCollationKey()) != 0) pos++;
        for (i = pos; i < keys.length && k.compareTo(keys[i].getCollationKey()) == 0; i++) results.add(keys[i]);
        return (results.size() == 0 ? null : results);
    }

    public String asSQL(String word) {
        return "UPPER(WORD) = UPPER('" + word + "')";
    }
}
