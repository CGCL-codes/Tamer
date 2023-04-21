package ar.com.ktulu.dict.strategies;

import ar.com.ktulu.dict.Key;
import ar.com.ktulu.dict.DatabaseException;
import java.text.CollationKey;
import java.util.ArrayList;
import gnu.regexp.RE;
import gnu.regexp.RESyntax;
import gnu.regexp.REException;

/**
 * Matches words using POSIX basic regular expressions.
 *
 * @author Luis Parravicini <webmaster@ktulu.com.ar>
 */
public class REPOSIXStrategy extends Strategy {

    public REPOSIXStrategy() {
        super("re", "POSIX basic regular expressions");
    }

    public ArrayList getMatches(String word, Key[] keys) throws DatabaseException {
        if (keys.length == 0) return null;
        RE re;
        try {
            re = new RE(word, RE.REG_ICASE, RESyntax.RE_SYNTAX_POSIX_BASIC);
        } catch (REException e) {
            System.out.println(e);
            return null;
        }
        ArrayList results = new ArrayList();
        for (int i = 0; i < keys.length; i++) if (re.isMatch(keys[i].getWord())) results.add(keys[i]);
        return (results.size() == 0 ? null : results);
    }

    public String asSQL(String word) {
        return "UPPER(WORD) ~*('" + word.toUpperCase() + "')";
    }
}
