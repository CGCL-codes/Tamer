package ar.com.ktulu.dict;

import java.text.CollationKey;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;

/**
 * Given a list of <code>Matches</code>, it iterates over all of them.
 *
 * @see Matches
 * @author Luis Parravicini <webmaster@ktulu.com.ar>
 */
public class MatchesContainer implements Matches {

    ArrayList matches;

    int size, idx;

    public MatchesContainer(ArrayList matches) throws DatabaseException {
        this.matches = matches;
        idx = 0;
        size = countMatches();
    }

    public Database getDatabase() {
        return ((Matches) matches.get(idx)).getDatabase();
    }

    public String getWord() {
        return ((Matches) matches.get(idx)).getWord();
    }

    public int matches() {
        return size;
    }

    public boolean hasNext() throws DatabaseException {
        boolean has = false;
        while (idx != matches.size()) {
            if (!(has = ((Matches) matches.get(idx)).hasNext())) idx++; else break;
        }
        return has;
    }

    public Definition next() throws DatabaseException {
        if (!hasNext()) throw new NoSuchElementException("No more matches");
        return ((Matches) matches.get(idx)).next();
    }

    public String nextWord() throws DatabaseException {
        if (!hasNext()) throw new NoSuchElementException("No more matches");
        return ((Matches) matches.get(idx)).nextWord();
    }

    protected int countMatches() throws DatabaseException {
        int size = 0;
        for (Iterator i = matches.iterator(); i.hasNext(); ) size += ((Matches) i.next()).matches();
        return size;
    }
}
