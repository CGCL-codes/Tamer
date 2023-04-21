package edu.columbia.hypercontent.dict.impl;

import edu.columbia.hypercontent.dict.Dictionary;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: Feb 4, 2004
 * Time: 1:34:32 PM
 * To change this template use Options | File Templates.
 */
public class CompositeDictionaryImpl implements Dictionary {

    Dictionary[] dicts = new Dictionary[0];

    public CompositeDictionaryImpl() {
    }

    public synchronized void addDictionary(Dictionary dict) {
        Dictionary[] newDicts = new Dictionary[dicts.length + 1];
        System.arraycopy(dicts, 0, newDicts, 0, dicts.length);
        newDicts[dicts.length] = dict;
        dicts = newDicts;
    }

    public boolean isCorrect(String word) {
        for (int i = 0; i < dicts.length; i++) {
            Dictionary dict = dicts[i];
            if (dict.isCorrect(word)) {
                return true;
            }
        }
        return false;
    }

    public String[] getSuggestions(String word) {
        ArrayList sugs = new ArrayList();
        for (int i = 0; i < dicts.length; i++) {
            String[] s = dicts[i].getSuggestions(word);
            for (int j = 0; j < s.length; j++) {
                sugs.add(s[j]);
            }
        }
        return (String[]) sugs.toArray(new String[sugs.size()]);
    }
}
