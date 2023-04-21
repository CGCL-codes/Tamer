package org.apache.lucene.analysis.de;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Loader for text files that represent a list of stopwords.
 * 
 * @deprecated Use {@link org.apache.lucene.analysis.WordlistLoader} instead
 *
 * @author Gerhard Schwarz
 * @version $Id: WordlistLoader.java 150527 2004-09-20 19:27:01Z dnaber $
 */
public class WordlistLoader {

    /**
   * Loads a text file and adds every line as an entry to a HashSet (omitting
   * leading and trailing whitespace). Every line of the file should contain only 
   * one word. The words need to be in lowercase if you make use of an
   * Analyzer which uses LowerCaseFilter (like GermanAnalyzer).
   * 
   * @param wordfile File containing the wordlist
   * @return A HashSet with the file's words
   */
    public static HashSet getWordSet(File wordfile) throws IOException {
        HashSet result = new HashSet();
        FileReader freader = null;
        LineNumberReader lnr = null;
        try {
            freader = new FileReader(wordfile);
            lnr = new LineNumberReader(freader);
            String word = null;
            while ((word = lnr.readLine()) != null) {
                result.add(word.trim());
            }
        } finally {
            if (lnr != null) lnr.close();
            if (freader != null) freader.close();
        }
        return result;
    }

    /**
   * @param path      Path to the wordlist
   * @param wordfile  Name of the wordlist
   * 
   * @deprecated Use {@link #getWordSet(File)} getWordSet(File)} instead
   */
    public static Hashtable getWordtable(String path, String wordfile) throws IOException {
        return getWordtable(new File(path, wordfile));
    }

    /**
   * @param wordfile  Complete path to the wordlist
   * 
   * @deprecated Use {@link #getWordSet(File)} getWordSet(File)} instead
   */
    public static Hashtable getWordtable(String wordfile) throws IOException {
        return getWordtable(new File(wordfile));
    }

    /**
   * @param wordfile  File object that points to the wordlist
   *
   * @deprecated Use {@link #getWordSet(File)} getWordSet(File)} instead
   */
    public static Hashtable getWordtable(File wordfile) throws IOException {
        HashSet wordSet = (HashSet) getWordSet(wordfile);
        Hashtable result = makeWordTable(wordSet);
        return result;
    }

    /**
   * Builds a wordlist table, using words as both keys and values
   * for backward compatibility.
   *
   * @param wordSet   stopword set
   */
    private static Hashtable makeWordTable(HashSet wordSet) {
        Hashtable table = new Hashtable();
        for (Iterator iter = wordSet.iterator(); iter.hasNext(); ) {
            String word = (String) iter.next();
            table.put(word, word);
        }
        return table;
    }
}
