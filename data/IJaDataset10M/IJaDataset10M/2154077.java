package net.ardvaark.jackbot.plugin.cheddarbot;

import java.util.Hashtable;
import java.util.Enumeration;
import java.io.Serializable;

@SuppressWarnings("unchecked")
public class dWord implements Serializable {

    private static final long serialVersionUID = 1L;

    String theWord;

    public Hashtable links;

    Hashtable weights;

    int probabilitySum;

    dWord(String theWord) {
        links = new Hashtable();
        weights = new Hashtable();
        this.theWord = theWord;
        probabilitySum = 0;
    }

    public boolean hasNext() {
        return (weights.size() > 0);
    }

    public void addLink(String word, dWord otherWord) {
        if (weights.containsKey(word)) {
            int tempWeight = ((Integer) weights.get(word)).intValue();
            tempWeight++;
            weights.put(word, new Integer(tempWeight));
            probabilitySum++;
        } else {
            weights.put(word, new Integer(1));
            links.put(word, otherWord);
            probabilitySum++;
        }
    }

    public String value() {
        return theWord;
    }

    public int countLinks() {
        return links.size();
    }

    public dWord next() {
        int nth = (int) Math.round(Math.random() * probabilitySum);
        int runningSum = 0;
        String tempString = "";
        Enumeration weightlist = weights.keys();
        while (weightlist.hasMoreElements() && (runningSum <= nth)) {
            tempString = (String) weightlist.nextElement();
            runningSum += ((Integer) weights.get(tempString)).intValue();
        }
        if (weightlist.hasMoreElements()) {
            return (dWord) links.get(tempString);
        } else {
            return (dWord) links.elements().nextElement();
        }
    }
}
