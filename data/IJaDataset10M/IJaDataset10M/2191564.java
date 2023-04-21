package org.jcvi.glk.sort;

import java.util.Comparator;
import org.jcvi.glk.SequenceReadType;

/**
 * 
 * 
 * @author jsitz
 * @author dkatzel
 */
public class SortSequenceReadTypesByName implements Comparator<SequenceReadType> {

    @Override
    public int compare(SequenceReadType type1, SequenceReadType type2) {
        return type1.getName().compareTo(type2.getName());
    }
}
