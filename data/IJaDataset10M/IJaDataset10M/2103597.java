package org.mmi.ont.voc2owl.trans;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Utility class that performs a string manipulation. It replaces this pattern
 * <i>[^a-zA-Z0-9-_]+</i> with =<i>_</i> and this pattern <i>(_+)$</i> with
 * nothing (deletes it). If it starts with a number an underscore is added. Also
 * it replaces any 2 consecutive underscore with one underscore.
 * </p>
 * <hr>
 * 
 * @author : $Author: luisbermudez $
 * @version : $Revision: 1.1 $
 * @since : Aug 8, 2006
 */
public class StringManipulationUtil implements StringManipulationInterface {

    private final String[] patterns = { "[^a-zA-Z0-9-_]+", "(_+)$" };

    private final String[] replace = { "_", "" };

    public String replaceString(String s) {
        String rep = s;
        for (int i = 0; i < patterns.length; i++) {
            Pattern p = Pattern.compile(patterns[i]);
            Matcher m = p.matcher(rep);
            rep = m.replaceAll(replace[i]);
        }
        return clean(appendUnderScoreStart(rep));
    }

    private String appendUnderScoreStart(String s) {
        String rep = s;
        Pattern p_ = Pattern.compile("[^a-zA-Z_]");
        Matcher m_ = p_.matcher(rep);
        if (m_.lookingAt()) {
            rep = "_" + rep;
        }
        return rep;
    }

    private String clean(String s) {
        String rep = s;
        Pattern p_ = Pattern.compile("_{2,}");
        Matcher m_ = p_.matcher(rep);
        rep = m_.replaceAll("_");
        return rep;
    }
}
