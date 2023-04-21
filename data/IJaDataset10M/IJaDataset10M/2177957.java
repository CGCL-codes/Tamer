package lempinen.neatseeker.core;

import java.io.*;
import java.util.TreeMap;

/**
 * Class for reading and querying a stopword list.
 * <p>
 * The StopWordList class reads in a text file specified in the constructor
 * argument, parses the file using StreamTokenizer and constructs a
 * TreeMap that holds the stopword list.
 * <p>
 * The class provides a single method for determining whether a word is on
 * a stopword list or not.
 *
 * @author Sami Lempinen <lempinen@iki.fi>
 * @version $Id: StopWordList.java,v 1.2 2000/11/07 16:55:14 lempinen Exp $
 */
public class StopWordList {

    /** The TreeMap that holds the stopword list. */
    protected TreeMap stopwords;

    /**
    * Creates a new StopWordList, based on the list in <code>textfile</code>.
    */
    public StopWordList(String textfile) throws IOException {
        if (Boolean.getBoolean("neatseeker.debug")) System.out.println("+ Reading stopwords from " + textfile);
        stopwords = new TreeMap();
        FileReader reader = null;
        try {
            reader = new FileReader(new File(textfile));
        } catch (Exception e) {
            throw new RuntimeException("Could not open " + textfile + " for reading.");
        }
        StreamTokenizer parser = new StreamTokenizer(reader);
        while (parser.nextToken() != StreamTokenizer.TT_EOF) {
            if (parser.ttype == StreamTokenizer.TT_WORD) {
                stopwords.put(parser.sval, new Boolean(true));
            }
        }
        reader.close();
    }

    /**
    * Returns true if <code>word</code> is on the stopword list.
    */
    public boolean isStopWord(String word) {
        return stopwords.containsKey(word.toLowerCase());
    }
}
