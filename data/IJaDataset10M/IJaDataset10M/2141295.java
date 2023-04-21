package com.doculibre.intelligid.utils.comparators;

import java.util.Comparator;
import com.doculibre.intelligid.utils.AccentApostropheCleaner;

public abstract class AbstractTextComparator<T> implements Comparator<T> {

    @Override
    public int compare(T o1, T o2) {
        String text1 = getText(o1);
        String text2 = getText(o2);
        int resultat;
        if (text1 == null && text2 == null) {
            resultat = 0;
        } else if (text1 == null) {
            resultat = -1;
        } else if (text2 == null) {
            resultat = 1;
        } else {
            text1 = text1.toLowerCase();
            text1 = AccentApostropheCleaner.removeAccents(text1);
            text1 = AccentApostropheCleaner.removeApostrophe(text1);
            text2 = text2.toLowerCase();
            text2 = AccentApostropheCleaner.removeAccents(text2);
            text2 = AccentApostropheCleaner.removeApostrophe(text2);
            resultat = text1.compareTo(text2);
        }
        return resultat;
    }

    protected abstract String getText(T object);
}
