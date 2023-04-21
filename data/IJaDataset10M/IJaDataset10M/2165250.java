package net.googlecode.demenkov.utils;

import net.googlecode.demenkov.domains.Vote;
import java.util.Comparator;

public class VoteComparatorByDate implements Comparator<Vote> {

    @Override
    public int compare(Vote o1, Vote o2) {
        return o2.getDate().compareTo(o1.getDate());
    }
}
