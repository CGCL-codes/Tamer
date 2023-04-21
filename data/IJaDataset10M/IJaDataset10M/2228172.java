package net.innig.macker.example.conventions;

import java.util.*;
import java.io.*;

public final class AlphaTree extends StringTree {

    public AlphaTree(AlphaTree parent, String word) {
        super(parent, word);
    }

    protected Set getChildSuffixes() {
        if (lettersAsStrings == null) {
            lettersAsStrings = new TreeSet();
            for (char c = 'a'; c <= 'z'; c++) lettersAsStrings.add(String.valueOf(c));
        }
        return lettersAsStrings;
    }

    protected StringTree makeChild(String childWord) {
        return new AlphaTree(this, childWord);
    }

    private static Set lettersAsStrings;
}
