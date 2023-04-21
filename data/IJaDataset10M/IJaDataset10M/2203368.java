package es.nom.morenojuarez.modulipse.core.lang;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Text tokens for the Modula-2 language.
 */
public final class TextTokens {

    /**
	 * Start comment token.
	 */
    public static final String START_COMMENT = "(*";

    /**
	 * End comment token. 
	 */
    public static final String END_COMMENT = "*)";

    /**
	 * String delimiter token (double quote). 
	 */
    public static final String STRING_DELIMITER1 = "\"";

    /**
	 * String delimiter token (single quote). 
	 */
    public static final String STRING_DELIMITER2 = "'";

    /**
	 * Valid Modula-2 keywords.
	 */
    public static Set<String> keywords = new TreeSet<String>();

    static {
        keywords.add("ABS");
        keywords.add("AND");
        keywords.add("ARRAY");
        keywords.add("BEGIN");
        keywords.add("BITSET");
        keywords.add("BOOLEAN");
        keywords.add("BY");
        keywords.add("CAP");
        keywords.add("CARDINAL");
        keywords.add("CASE");
        keywords.add("CHAR");
        keywords.add("CHR");
        keywords.add("CMPLX");
        keywords.add("COMPLEX");
        keywords.add("CONST");
        keywords.add("DEC");
        keywords.add("DEFINITION");
        keywords.add("DISPOSE");
        keywords.add("DIV");
        keywords.add("DO");
        keywords.add("ELSE");
        keywords.add("ELSIF");
        keywords.add("END");
        keywords.add("EXCEPT");
        keywords.add("EXCL");
        keywords.add("EXIT");
        keywords.add("EXPORT");
        keywords.add("FALSE");
        keywords.add("FINALLY");
        keywords.add("FLOAT");
        keywords.add("FOR");
        keywords.add("FORWARD");
        keywords.add("FROM");
        keywords.add("HALT");
        keywords.add("HIGH");
        keywords.add("IF");
        keywords.add("IM");
        keywords.add("IMPLEMENTATION");
        keywords.add("IMPORT");
        keywords.add("IN");
        keywords.add("INC");
        keywords.add("INCL");
        keywords.add("INT");
        keywords.add("INTEGER");
        keywords.add("INTERRUPTIBLE");
        keywords.add("LENGTH");
        keywords.add("LFLOAT");
        keywords.add("LONGCOMPLEX");
        keywords.add("LONGREAL");
        keywords.add("LOOP");
        keywords.add("MAX");
        keywords.add("MIN");
        keywords.add("MOD");
        keywords.add("MODULE");
        keywords.add("NEW");
        keywords.add("NIL");
        keywords.add("NOT");
        keywords.add("ODD");
        keywords.add("OF");
        keywords.add("OR");
        keywords.add("ORD");
        keywords.add("PACKEDSET");
        keywords.add("POINTER");
        keywords.add("PROC");
        keywords.add("PROCEDURE");
        keywords.add("PROTECTION");
        keywords.add("QUALIFIED");
        keywords.add("RE");
        keywords.add("REAL");
        keywords.add("RECORD");
        keywords.add("REM");
        keywords.add("REPEAT");
        keywords.add("RETRY");
        keywords.add("RETURN");
        keywords.add("SET");
        keywords.add("SIZE");
        keywords.add("THEN");
        keywords.add("TO");
        keywords.add("TRUE");
        keywords.add("TRUNC");
        keywords.add("TYPE");
        keywords.add("UNTIL");
        keywords.add("UNINTERRUPTIBLE");
        keywords.add("VAL");
        keywords.add("VAR");
        keywords.add("WHILE");
        keywords.add("WITH");
    }

    /**
	 * Avoid instantation of this utility class.
	 */
    private TextTokens() {
    }

    /**
	 * Return true if the character <code>c</code> can be part of a Modula-2
	 * identifier, otherwise false.
	 * 
	 * @param c	the character to consider
	 * 
	 * @return	true if the character <code>c</code> can be part of a Modula-2
	 * 			identifier, otherwise false.
	 */
    public static final boolean isModula2IdentifierPart(final char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    /**
	 * Return true if the character <code>c</code> can be the first character in
	 * a Modula-2 identifier, otherwise false.
	 * 
	 * @param c	the character to consider
	 * 
	 * @return	true if the character <code>c</code> can be the first character
	 * 				in a Modula-2 identifier
	 */
    public static final boolean isModula2IdentifierStart(final char c) {
        return Character.isLetter(c);
    }

    /**
	 * Returns the keywords starting with the specified string.
	 * 
	 * @param prefix	keyword prefix
	 * @return	the keywords starting with the specified string
	 */
    public static Set<String> keywordsStartingWith(String prefix) {
        int lastPosition = prefix.length() - 1;
        if (lastPosition < 0) return keywords;
        return ((SortedSet<String>) keywords).subSet(prefix, prefix.substring(0, lastPosition) + (char) (prefix.charAt(lastPosition) + 1));
    }
}
