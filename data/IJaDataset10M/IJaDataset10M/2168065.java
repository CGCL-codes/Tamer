package org.liris.schemerger.core.dataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.liris.schemerger.core.IDataSet;
import org.liris.schemerger.core.event.IItemsetEvent;
import com.google.common.collect.ForwardingMap;

/**
 * A database of sequences. Meant for algorithms that count the pattern
 * frequency over a database of sequence, not in a single sequence of events.
 * (SPADE, PrefixSpan, Apriori, etc.)
 * 
 * @author Damien Cram
 * 
 */
public class SequenceDB<E extends IItemsetEvent> extends ForwardingMap<Integer, ItemsetSequence<E>> implements IDataSet {

    private static final long serialVersionUID = 1L;

    private Map<Integer, ItemsetSequence<E>> db = new TreeMap<Integer, ItemsetSequence<E>>();

    @Override
    public String toString() {
        String s = "";
        List<Integer> keys = new ArrayList<Integer>(this.keySet());
        Collections.sort(keys);
        for (int sid : keys) {
            s += "\nsid=" + sid + this.get(sid).toString();
        }
        return s;
    }

    @Override
    protected Map<Integer, ItemsetSequence<E>> delegate() {
        return db;
    }
}
