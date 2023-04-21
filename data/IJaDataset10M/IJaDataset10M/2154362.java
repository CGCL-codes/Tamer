package com.peterhi.player;

import java.util.Comparator;

public class ClassmateComparator implements Comparator<Classmate> {

    public int compare(Classmate c1, Classmate c2) {
        String e1 = c1.getEmail();
        String e2 = c2.getEmail();
        return e1.compareTo(e2);
    }
}
