package hepple.postag;

import gate.util.BomStrippingInputStreamReader;
import java.util.*;
import java.io.*;
import java.net.URL;

/**
 * A {@link java.util.HashMap} that maps from lexical entry
 * ({@link java.lang.String}) to possible POS categories
 * ({@link java.util.List}
 */
class Lexicon extends HashMap {

    private String encoding;

    /**
   * @deprecated The lexicon file is read at construction time, so setting the
   * encoding later will have no effect.  Use the two argument constructor to
   * set the encoding.
   */
    public void setEncoding(String encoding) {
        throw new IllegalStateException("Cannot change encoding once POS tagger " + "has been constructed.  Use the three " + "argument constructor to specify " + "encoding.");
    }

    /**
   * Constructor.
   * @param lexiconURL an URL for the file contianing the lexicon.
   */
    public Lexicon(URL lexiconURL) throws IOException {
        this(lexiconURL, null);
    }

    /**
   * Constructor.
   * @param lexiconURL an URL for the file contianing the lexicon.
   * @param encoding the character encoding to use for reading the lexicon.
   */
    public Lexicon(URL lexiconURL, String encoding) throws IOException {
        this.encoding = encoding;
        String line;
        BufferedReader lexiconReader;
        if (encoding == null) {
            lexiconReader = new BomStrippingInputStreamReader(lexiconURL.openStream());
        } else {
            lexiconReader = new BomStrippingInputStreamReader(lexiconURL.openStream(), encoding);
        }
        line = lexiconReader.readLine();
        String entry;
        List categories;
        while (line != null) {
            StringTokenizer tokens = new StringTokenizer(line);
            entry = tokens.nextToken();
            categories = new ArrayList();
            while (tokens.hasMoreTokens()) categories.add(tokens.nextToken());
            put(entry, categories);
            line = lexiconReader.readLine();
        }
    }
}
