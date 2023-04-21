package edu.jhu.sa.util.sentence;

import edu.jhu.sa.util.suffix_array.BasicPhrase;
import edu.jhu.sa.util.suffix_array.SuffixArrayFactory;
import java.util.*;

/**
 * Vocabulary is the class that keeps track of the unique words
 * that occur in a corpus of text for a particular language.  
 * It assigns integer IDs to Words, which is useful when we are
 * creating suffix arrays or doing similar things.
 *
 * @author Chris Callison-Burch
 * @since  8 February 2005
 * @author Lane Schwartz
 * @version $LastChangedDate:2008-07-30 17:15:52 -0400 (Wed, 30 Jul 2008) $
 */
public class Vocabulary implements Iterable<String> {

    /** The unknown word's ID will be the size of the vocabulary,
	  * ensuring that it is outside of the vocabulary. */
    public static int UNKNOWN_WORD = 0;

    public static final String UNKNOWN_WORD_STRING = "UNK";

    Map<String, Integer> wordToIDMap;

    List<String> vocabList;

    /** Determines whether new words may be added to the vocabulary. */
    boolean isFixed;

    /**
	 * Constructor creates an empty vocabulary.
	 */
    public Vocabulary() {
        wordToIDMap = new Hashtable<String, Integer>();
        vocabList = new Vector<String>();
        isFixed = false;
    }

    /** 
	 * Constructor creates a fixed vocabulary from the given set of words.
	 */
    public Vocabulary(Set<String> words) {
        wordToIDMap = new Hashtable<String, Integer>();
        vocabList = new Vector<String>();
        vocabList.addAll(words);
        alphabetize();
        isFixed = true;
        UNKNOWN_WORD = vocabList.size();
    }

    /**
	 * @return the ID for wordString
	 */
    public int getID(String wordString) {
        Integer ID = wordToIDMap.get(wordString);
        if (ID == null) {
            return UNKNOWN_WORD;
        } else {
            return ID.intValue();
        }
    }

    /**
	 * @return the String for a word ID
	 */
    public String getWord(int wordID) {
        if (wordID >= vocabList.size() || wordID < 0) {
            return UNKNOWN_WORD_STRING;
        }
        return vocabList.get(wordID);
    }

    /**
	 * @return an Interator over all words in the Vocabulary.
	 */
    public Iterator<String> iterator() {
        return vocabList.iterator();
    }

    public List<String> getWords() {
        return vocabList;
    }

    /** 
	 * Adds a word to the vocabulary.
	 * @returns the ID of the word, or UNKNOWN_WORD if 
	 * the word is new and the vocabulary is fixed.
	 */
    public int addWord(String wordString) {
        Integer ID = wordToIDMap.get(wordString);
        if (ID != null) {
            return ID.intValue();
        } else if (!isFixed) {
            ID = new Integer(vocabList.size());
            vocabList.add(wordString);
            wordToIDMap.put(wordString, ID);
            UNKNOWN_WORD = vocabList.size();
            return ID.intValue();
        } else {
            return UNKNOWN_WORD;
        }
    }

    /**
	 * @return the number of unique words in the vocabulary.
	 */
    public int size() {
        return vocabList.size();
    }

    /** 
	 * Fixes the size of the vocabulary so that new words
	 * may not be added.
	 */
    public void fixVocabulary() {
        isFixed = true;
    }

    /**
	 * @return true if there are unknown words in the phrase
	 */
    public boolean containsUnknownWords(BasicPhrase phrase) {
        for (int i = 0; i < phrase.size(); i++) {
            if (phrase.getWordID(i) == UNKNOWN_WORD) return true;
        }
        return false;
    }

    /**
	 * Checks that the Vocabularies are the same, by first checking that they have
	 * the same number of items, and then checking that each word corresoponds
	 * to the same ID
	 * @param o the Vocabulary to check equivalence with
	 * @return true if the other object is a Vocabulary representing the same set of
	 * words with identically assigned IDs
	 */
    public boolean equals(Object o) {
        if (o == this) return true; else if (o == null) return false; else if (!o.getClass().isInstance(this)) return false; else {
            Vocabulary other = (Vocabulary) o;
            if (other.size() != this.size()) return false;
            for (int i = 0; i < this.size(); i++) {
                String thisWord = (String) this.vocabList.get(i);
                String otherWord = (String) other.vocabList.get(i);
                if (!(thisWord.equals(otherWord))) return false;
                Integer thisID = this.wordToIDMap.get(thisWord);
                Integer otherID = other.wordToIDMap.get(otherWord);
                if (thisID != null && otherID != null) {
                    if (!(thisID.equals(otherID))) return false;
                }
            }
            return true;
        }
    }

    public String toString() {
        return vocabList.toString();
    }

    /** 
	 * Sorts the vocabulary alphabetically and re-assigns IDs
	 * in ascending order.
	 */
    public void alphabetize() {
        Collections.sort(vocabList);
        wordToIDMap = new Hashtable<String, Integer>();
        for (int i = 0; i < vocabList.size(); i++) {
            String wordString = vocabList.get(i);
            wordToIDMap.put(wordString, new Integer(i));
        }
        UNKNOWN_WORD = getID(UNKNOWN_WORD_STRING);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.out.println("Usage: java Vocabulary file corpusName lang outputDir");
            System.exit(0);
        }
        String inputFilename = args[0];
        String corpusName = args[1];
        String lang = args[2];
        String outputDirectory = args[3];
        Vocabulary vocab = new Vocabulary();
        SuffixArrayFactory.createVocabulary(inputFilename, vocab);
        SuffixArrayFactory.saveVocabulary(vocab, lang, corpusName, outputDirectory);
    }
}
