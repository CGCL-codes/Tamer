package org.ensembl.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Dynamic list of longs.
 * 
 * 
 * @author <a href="mailto:craig@ebi.ac.uk">Craig Melsopp </a>
 *  
 */
public class LongList {

    protected List list = new LinkedList();

    protected int size = 0;

    protected long[] result = null;

    public LongList() {
    }

    public LongList(long[] ls) {
        add(ls);
    }

    public void add(long l) {
        list.add(new Long(l));
        size++;
        result = null;
    }

    public long[] toArray() {
        if (result == null) {
            result = new long[size];
            Iterator iter = list.listIterator();
            for (int i = 0; i < size; i++) result[i] = ((Long) iter.next()).longValue();
        }
        return result;
    }

    public int size() {
        return size;
    }

    /**
	 * Comma separated list of items in list.
	 * 
	 * @return string containing all the items in this list, each is followed by
	 *         a comma.
	 */
    public String toCommaSeparatedString() {
        return StringUtil.toString(toArray());
    }

    /**
	 * @param ls ids to add to the list.
	 */
    public void add(long[] ls) {
        for (int i = 0; i < ls.length; i++) add(ls[i]);
    }
}
