package org.isqlviewer.sql.embedded;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO Add Procedures JavaDoc inforamation
 * <p>
 * 
 * @author Mark A. Kobold &lt;mkobold at isqlviewer dot com&gt;
 * @version 1.0
 */
public class Procedures {

    /**
     * Function wrapper to support regex replace within Apache Derby.
     * <p>
     * This function is primarily used to do bookmark folder renames.
     * 
     * @see Matcher#replaceFirst(String)
     */
    public static String regexReplace(String source, String regex, String target) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        return matcher.replaceFirst(target);
    }
}
