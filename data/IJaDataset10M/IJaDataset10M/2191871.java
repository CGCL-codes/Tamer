package net.sf.jabref.export.layout.format;

import net.sf.jabref.export.layout.AbstractParamLayoutFormatter;

/**
 * Formatter that does regexp replacement.
 *
 * To use this formatter, a two-part argument must be given. The parts are
 * separated by a comma. To indicate the comma character, use an escape
 * sequence: \,
 * For inserting newlines and tabs in arguments, use \n and \t, respectively.
 *
 * The first part is the regular expression to search for. Remember that any commma
 * character must be preceded by a backslash, and consequently a literal backslash must
 * be written as a pair of backslashes. A description of Java regular expressions can be
 * found at:
 *   http://java.sun.com/j2se/1.4.2/docs/api/java/util/regex/Pattern.html
 *
 * The second part is the text to replace all matches with.
 *
 * For instance:
 *  \format[Replace(and,&)]{\author} :
 *      will output the "author" field after replacing all occurences of "and"
 *      by "&"
 *
 *  \format[Replace(\s,_)]{\author} :
 *      will output the "author" field after replacing all whitespace
 *      by underscores.
 *
 *  \format[Replace(\,,;)]{\author} :
 *      will output the "author" field after replacing all commas by semicolons.
 *
 */
public class Replace extends AbstractParamLayoutFormatter {

    private String regex = null, replaceWith = null;

    public void setArgument(String arg) {
        String[] parts = parseArgument(arg);
        if (parts.length < 2) return;
        regex = parts[0];
        replaceWith = parts[1];
    }

    public String format(String fieldText) {
        if (regex == null) return fieldText;
        return fieldText.replaceAll(regex, replaceWith);
    }
}
