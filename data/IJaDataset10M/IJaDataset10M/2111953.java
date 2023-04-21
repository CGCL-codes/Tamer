package jgrail.gui;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import jgrail.lexicon.Lexicon;
import buoy.widget.BList;

public class LexiconList implements Observer {

    private BList list;

    public LexiconList(BList list) {
        this.list = list;
    }

    public void update(Observable o, Object arg) {
        if (o instanceof Lexicon) {
            Lexicon l = (Lexicon) o;
            Set<String> s = l.getLexemes();
            SortedSet<String> ss = new TreeSet<String>(s);
            list.setContents(ss);
        }
    }
}
