package fido.linguistic;

import java.util.*;
import fido.db.*;
import fido.grammar.GrammarParseException;
import fido.frontend.FidoMessage;
import fido.util.BooleanTree;

/**
 * Attempts to find a word string in one of four ways:
 * <OL>
 * <LI> WordClassification - Identifies classes of words, such as numbers and dates
 * <LI> Dictionary - List of all words known to the system
 * <LI> Morphology - Looks for prefixes and suffixes to strip off to find the root
 *                   form of a word.  See
 *                   <A HREF=http://fido.sf.net/developer/concepts/Morphology.php>
 *                   Morphology</A> for a description on morphology tags.
 * <LI> Unknown Word - Special word in the Dictionary that is used if the word is
 *                     not found.
 * </OL>
 *
 * @see fido.db.WordClassificationTable
 * @see fido.db.DictionaryTable
 * @see fido.db.LanguageMorphologyTable
 */
public class WordLookup {

    /**
	 * Creates a new WordLookup instance.
	 */
    public WordLookup() {
    }

    /**
	 * The word string is passed to the WordClassificationTable for
	 * a regular expression type matching.  This test is for classes
	 * of words, such as numbers.  Instead of storing every number
	 * in the Dictionary, only one number class is stored.  If the
	 * word string is a number, the number class is used to create
	 * the WordSense.
	 * 
	 * If the word is not classified, the word is looked up in the Dictionary.
	 * Looking up the word may return mutliple entries from the Dictionary.  
	 * Every entry in the Dictionary represents a Word Sense.<P>
	 * 
	 * If no entries are returned from the Dictionary, the word is passed
	 * off to the Morphology module.  The
	 * Morphology module attempts to find a root form of the string the
	 * user typed.  This includes verb tenses, plurals, and possessives.<P>
	 * 
	 * If no entries are returned from the Morphology module, the word is
	 * assigned <I>UNKNOWN_WORD</I> which has a grammar string.  This 
	 * means the word is not known to the system, but <I>UNKNOWN_WORD</I>
	 * contains a grammar string that allows the word to be parsed.<P>
	 *
	 * This method is public for the GrammarFrontEnd to be able to lookup
	 * one word at a time.  For processing commands, the words will be
	 * put into a Vector and lookupWords() should be called.<P>
	 * 
	 * @exception FidoDatabaseException Thrown if there is an error contacting or
	 *                                  processing a database request.
	 *
	 * @exception GrammarParseException Thrown if a word in the Dictionary has
	 *                                  a grammar string that cannot be parsed.
	 *
	 * @exception GrammarLinkNotFoundException Thrown if a word in the Dictionary
	 *                                         has a grammar link type that is not
	 *                                         found in the GrammarLinkTypes table
	 *
	 * @exception LookupWordFailedException Thrown if a word cannot be resolved and
	 *                                      the special word UNKNOWN_WORD is not found
	 *                                      either.
	 * 
	 * @see fido.db.WordClassificationTable
	 * @see fido.db.DictionaryTable
	 * @see fido.db.LanguageMorphologyTable
	 * @see fido.db.WordSense
	 * @see fido.linguistic.WordPackage
	 */
    public void lookupWord(WordPackage word, String language) throws FidoDatabaseException, GrammarParseException, GrammarLinkNotFoundException, LookupWordFailedException {
        WordClassificationTable wordClassification = new WordClassificationTable();
        DictionaryTable dictionary = new DictionaryTable();
        LanguageMorphologyTable languageList = new LanguageMorphologyTable();
        String wordString = word.getSurfaceForm();
        FidoMessage.log("[WordLookup] Looking up word '" + wordString + "'");
        WordSense sense = wordClassification.classify(wordString);
        if (sense != null) {
            FidoMessage.log("[WordClassification] Classified word:");
            FidoMessage.log("[WordClassification] Object = " + sense.getRepresentedObject());
            FidoMessage.log("[WordClassification] Grammar String = " + sense.getGrammarString());
            sense.setRootForm(wordString);
            word.addWordSense(sense);
            return;
        }
        Collection list = dictionary.lookupWord(wordString);
        if (list.isEmpty() == false) {
            FidoMessage.log("[Dictionary] Found word:");
            int num = 1;
            for (Iterator it = list.iterator(); it.hasNext(); ++num) {
                sense = (WordSense) it.next();
                FidoMessage.log("[Dictionary] Sense " + num + ":");
                FidoMessage.log("[Dictionary] Object = " + sense.getRepresentedObject());
                FidoMessage.log("[Dictionary] Grammar String = " + sense.getGrammarString());
                sense.setRootForm(wordString);
                word.addWordSense(sense);
            }
            return;
        }
        boolean found = false;
        list = languageList.recognize(language, wordString);
        if (list.isEmpty() == false) {
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                MorphologyRecognizeMatch match = (MorphologyRecognizeMatch) it.next();
                String root = match.getRootString();
                FidoMessage.log("[Morphology] Found root '" + root + "'");
                Collection tags = match.getMorphologyTags();
                FidoMessage.log("[Morphology] Morphology tags:");
                for (Iterator it2 = tags.iterator(); it2.hasNext(); ) {
                    String tag = (String) it2.next();
                    FidoMessage.log("[Morphology] " + tag);
                }
                Collection lookup = dictionary.lookupWord(root);
                if (lookup.isEmpty() == false) {
                    FidoMessage.log("[Morphology] Found '" + root + "' in the dictionary");
                    found = true;
                    int num = 1;
                    for (Iterator it2 = lookup.iterator(); it2.hasNext(); ++num) {
                        sense = (WordSense) it2.next();
                        FidoMessage.log("[Morphology] Sense " + num + ":");
                        FidoMessage.log("[Morphology] Object = " + sense.getRepresentedObject());
                        FidoMessage.log("[Morphology] Grammar String = " + sense.getGrammarString());
                        sense.addMorphologyTags(tags);
                        sense.setRootForm(root);
                        word.addWordSense(sense);
                    }
                }
            }
        }
        if (found == true) return;
        FidoMessage.log("[Dictionary] Looking up word 'UNKNOWN_WORD' in the dictionary");
        list = dictionary.lookupWord("UNKNOWN_WORD");
        if (list.isEmpty() == false) {
            FidoMessage.log("[Dictionary] Found UNKNOWN_WORD word:");
            int num = 1;
            for (Iterator it = list.iterator(); it.hasNext(); ++num) {
                sense = (WordSense) it.next();
                FidoMessage.log("[Dictionary] Sense " + num + ":");
                FidoMessage.log("[Dictionary] Object = " + sense.getRepresentedObject());
                FidoMessage.log("[Dictionary] Grammar String = " + sense.getGrammarString());
                sense.setRootForm("?" + wordString);
                word.addWordSense(sense);
            }
            return;
        }
        throw new LookupWordFailedException("Word '" + wordString + "' not found and 'UNKNOWN_WORD' not found either");
    }

    /**
	 * Calls lookupWord() for each word in the parameter <I>word</I>
	 * Vector.  Each entry in the Vector of WordPackages will be updated
	 * with one to many WordSenses.  If any word cannot be found, a
	 * LookupWordFailException will be thrown.
	 * 
	 * @param words Vector of WordPackages
	 * @param language Language of the words
	 * 
	 * @exception FidoDatabaseException Thrown if there is an error contacting or
	 *                                  processing a database request.
	 *
	 * @exception GrammarParseException Thrown if a word in the Dictionary has
	 *                                  a grammar string that cannot be parsed.
	 *
	 * @exception GrammarLinkNotFoundException Thrown if a word in the Dictionary
	 *                                         has a grammar link type that is not
	 *                                         found in the GrammarLinkTypes table
	 *
	 * @exception LookupWordFailedException Thrown if a word cannot be resolved and
	 *                                      the special word UNKNOWN_WORD is not found
	 *                                      either.
	 * 
	 * @see fido.db.DictionaryTable
	 * @see fido.linguistic.WordPackage
	 * @see fido.db.WordSense
	 * @see #lookupWord(WordPackage, String)
	 */
    public void lookupWords(Vector words, String language) throws FidoDatabaseException, GrammarParseException, GrammarLinkNotFoundException, LookupWordFailedException {
        for (Iterator it = words.iterator(); it.hasNext(); ) {
            WordPackage word = (WordPackage) it.next();
            lookupWord(word, language);
        }
    }
}
