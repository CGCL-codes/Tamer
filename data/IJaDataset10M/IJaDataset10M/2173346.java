package com.wordpower.expr;

import java.util.Enumeration;
import java.util.Vector;
import com.wordpower.model.Library;
import com.wordpower.model.WordEntry;

/**
 * <p>Filter expression used to take out the words.</p>
 * The expression always takes the following form:
 * 
 * ModifierDefinition[Range] Conjunction ModifierDefinition[Range]
 * 
 * <p>Definitions:
 * 	<ul>
 * 		<li>* - Returns all the words from all the libraries.</li>
 * 		<li>*ANumber - Returns all the words from the library at ANumber,
 * 		where the number is the order of the library appeared in the
 * 		"Add Library Form", for example, if the library "A", "B" and "C"
 * 		are the first, second and third entry in the "Add Library Form",
 * 		*2 will return all the words in the library "B"</li>
 * 		<li>*ALetter - Returns all the words starting with the specified
 * 		letter in all the libraries.</li>
 * 		<li>*ALetterANumber or *ANumberALetter - Returns all the words
 * 		starting with the specified letter "ALetter" from the specified
 * 		library "ANumber".</li>
 * 	</ul>
 * </p>
 * 
 * <p>Ranges:
 * 	<ul>
 * 		<li>min-max - Within the words specified by a Definition, return
 * 		the words from the min-th to the max-th. Leaving out the min means
 * 		from the first word, leaving out the max means to the last word.</li>
 * 		<li>aNumber - Within the words specified by a Definition, return
 * 		a number of words specified by "aNumber" will be chosen randomly
 * 		and returned.</li>
 * 	</ul>
 * </p>
 * 
 * <p>Modifiers:
 * 	<ul>
 * 		<li>' - Invert the set, meaning those chosen will not be selected,
 * 		but those not chosen will be selected.</li>
 * 		<li>! - Shuffle the set, meaning the order of the set will randomize.</li>
 * 	</ul>
 * </p>
 * 
 * <p>Conjunctions:
 * 	<ul>
 * 		<li>& - Only the words that appear on both sets will be chosen.</li>
 * 		<li>| - All words in both sets will be chosen.</li>
 * 	</ul>
 * </p>
 * @author hytparadisee
 *
 */
public class FilterExpressionParser {

    public static void validate(String expr) throws ParseException {
        if ("*".equals(expr)) return; else if (expr.indexOf('-') != -1) return; else throw new ParseException();
    }

    public static WordEntry[] evalutate(Library[] libraries, String expr) throws ParseException {
        if ("*".equals(expr)) return getAll(libraries, expr); else if (expr.indexOf('-') != -1) return getLetterRange(libraries, expr); else throw new ParseException();
    }

    private static WordEntry[] getAll(Library[] libraries, String expr) throws ParseException {
        Vector v = new Vector();
        for (int i = 0; i < libraries.length; i++) {
            Enumeration e = libraries[i].words();
            while (e.hasMoreElements()) {
                String word = (String) e.nextElement();
                String meaning = libraries[i].getMeaning(word);
                WordEntry we = new WordEntry(word, meaning);
                if (!v.contains(we)) v.addElement(we);
            }
        }
        WordEntry[] ret = new WordEntry[v.size()];
        v.copyInto(ret);
        return ret;
    }

    private static WordEntry[] getLetterRange(Library[] libraries, String expr) throws ParseException {
        try {
            char start = Character.toLowerCase(expr.charAt(expr.indexOf('-') - 1));
            char end = Character.toLowerCase(expr.charAt(expr.indexOf('-') + 1));
            Vector v = new Vector();
            for (int i = 0; i < libraries.length; i++) {
                Enumeration e = libraries[i].words();
                while (e.hasMoreElements()) {
                    String word = (String) e.nextElement();
                    String meaning = libraries[i].getMeaning(word);
                    WordEntry we = new WordEntry(word, meaning);
                    char fc = Character.toLowerCase(word.charAt(0));
                    if (fc >= start && fc <= end) v.addElement(we);
                }
            }
            WordEntry[] ret = new WordEntry[v.size()];
            v.copyInto(ret);
            return ret;
        } catch (Exception ex) {
            throw new ParseException(ex.toString());
        }
    }
}
