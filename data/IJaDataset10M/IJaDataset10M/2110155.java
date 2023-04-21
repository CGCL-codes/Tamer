package tei.cr.component.frequencyList;

import tei.cr.component.frequencyList.FrequencyList;
import java.util.Enumeration;
import java.util.Hashtable;

public class FrequencyListByPosition implements FrequencyList {

    private static final int HEAD_OPTIMUM_INCREMENT = 100;

    private static final int ITEM_OPTIMUM_INCREMENT = 500;

    private int corpusSize = 0;

    private int vars_nbr = 0;

    private String[] vars = new String[HEAD_OPTIMUM_INCREMENT];

    private int[] freqs = new int[ITEM_OPTIMUM_INCREMENT];

    private String title = null;

    public FrequencyListByPosition() {
    }

    /** return 0 if failed, 1 if succeed */
    public int addItemOccurrence(String item) {
        if ((item == null) || item.equals("")) {
            return 0;
        }
        int index = getVarIndex(item);
        if (index == -1) {
            index = addVar(item);
        }
        addOccurrence(index);
        corpusSize++;
        return 1;
    }

    public int getCorpusSize() {
        return corpusSize;
    }

    public int getItemsNumber() {
        return vars_nbr;
    }

    public boolean existsItem(String item) {
        if (getVarIndex(item) == -1) {
            return false;
        }
        return true;
    }

    public void addFrequencyList(FrequencyList lexicon) {
        throw new UnsupportedOperationException("");
    }

    /**
     * @param item the lexical entry. The entry should not be null or
     * equals to an empty String.
     * @return the number of occurrences, or -1 if the entry does not exist.
     */
    public int getItemFrequency(String item) {
        if ((item == null) || item.equals("")) {
            return -1;
        }
        int index = getVarIndex(item);
        if (index == -1) {
            return index;
        }
        return getFreq(index);
    }

    public int getItemFrequency(int index) {
        return getFreq(index);
    }

    public Hashtable getLexiconAsHashtable() {
        Hashtable lexiconHT = new Hashtable(vars_nbr);
        for (int i = 0; i < vars_nbr; i++) {
            lexiconHT.put((Object) vars[i], (Object) String.valueOf(getFreq(i)));
        }
        return lexiconHT;
    }

    /**
     * Return an Enumeration of all the items of the lexicon.
     */
    public Enumeration getItemsAsEnumeration() {
        return getLexiconAsHashtable().keys();
    }

    /**
     * Return a string array of all the items of the lexicon.
     */
    public String[] getItemNames() {
        String[] newArr = new String[vars_nbr];
        System.arraycopy(vars, 0, newArr, 0, vars_nbr);
        return newArr;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    private int getVarIndex(String var) {
        for (int i = 0; i < vars_nbr; i++) {
            if (vars[i] == var) {
                return i;
            }
        }
        return -1;
    }

    private void addOccurrence(int index) {
        if (corpusSize == freqs.length) {
            int[] newItem = new int[freqs.length + ITEM_OPTIMUM_INCREMENT];
            System.arraycopy(freqs, 0, newItem, 0, freqs.length);
            freqs = newItem;
        }
        freqs[corpusSize] = index;
    }

    private int addVar(String var) {
        if ((vars_nbr + 1) > vars.length) {
            increase_vars();
        }
        vars[vars_nbr] = var;
        vars_nbr++;
        return (vars_nbr - 1);
    }

    private void increase_vars() {
        String[] newVars = new String[vars.length + HEAD_OPTIMUM_INCREMENT];
        System.arraycopy(vars, 0, newVars, 0, vars.length);
        vars = newVars;
    }

    private int getFreq(int index) {
        int freq = 0;
        for (int i = 0; i < freqs.length; i++) {
            if (freqs[i] == index) {
                freq++;
            }
        }
        return freq;
    }
}
