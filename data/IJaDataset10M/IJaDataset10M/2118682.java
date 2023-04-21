package net.ob3d.domainmodel;

import java.util.Comparator;

public class RatingEntryComparator implements Comparator<RatingEntry> {

    public int compare(RatingEntry r1, RatingEntry r2) {
        if (r1 == null || r2 == null) return 0;
        return r1.getPosition() - r2.getPosition();
    }
}
