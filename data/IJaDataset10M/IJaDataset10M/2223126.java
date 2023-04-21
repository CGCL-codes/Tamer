package com.groovytagger.mp3.tags.util;

import java.text.Collator;
import java.util.Comparator;

public class ID3GenreComparator implements Comparator {

    public ID3GenreComparator() {
    }

    public int compare(Object g1, Object g2) {
        if (g1 instanceof Genre && g2 instanceof Genre) {
            Genre _g1 = (Genre) g1;
            Genre _g2 = (Genre) g2;
            return Collator.getInstance().compare(_g1.toString(), _g2.toString());
        }
        return 0;
    }
}
