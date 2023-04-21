package com.myapp.util.soundsorter.wizard.gui;

import java.util.Comparator;
import com.myapp.util.soundsorter.wizard.model.ISong;

final class SongComparator implements Comparator<ISong> {

    @Override
    public int compare(ISong o1, ISong o2) {
        return String.CASE_INSENSITIVE_ORDER.compare(o1.getFile(), o2.getFile());
    }
}
