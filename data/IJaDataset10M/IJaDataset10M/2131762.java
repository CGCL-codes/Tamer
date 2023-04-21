package org.semtinel.plugins.thencer.search;

import edu.mit.jwi.*;
import edu.mit.jwi.item.*;
import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Robert
 */
public class wordNetSearch {

    public IDictionary dict;

    Logger log = Logger.getLogger(this.getClass());

    /**
     *  Initialize wordnetsearch
     * @param args Dictionary location
     */
    public wordNetSearch(String args) {
        try {
            openDic(args);
        } catch (Exception ex) {
            log.debug("Error while setting database for dict. " + ex);
        }
    }

    private void openDic(String dictFile) throws Exception {
        URL url = new URL("file", null, dictFile);
        this.dict = new Dictionary(url);
        dict.open();
    }

    /**
     * Searchs for the sense of a word
     * @param s Searchstring
     * @param p Type of word (NOUN etc)
     * @return List of words
     */
    public List<IWord> search(String s, POS p) {
        List<IWord> words = new LinkedList<IWord>();
        IIndexWord idxWord = dict.getIndexWord(s, p);
        for (IWordID w : idxWord.getWordIDs()) {
            IWord word = dict.getWord(w);
            words.add(word);
        }
        return words;
    }
}
