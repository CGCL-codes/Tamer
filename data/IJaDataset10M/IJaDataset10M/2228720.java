package musicbox.backend.comparators;

import java.util.Comparator;
import noTalent.MusicOutputDesign;

/**
 *
 * @author Isaac Hammon
 */
public class TrackLengthComparator implements Comparator<MusicOutputDesign> {

    /**
     * 
     */
    public TrackLengthComparator() {
    }

    @Override
    public int compare(MusicOutputDesign m1, MusicOutputDesign m2) {
        if (m1.getTrackLength().compareToIgnoreCase(m2.getTrackLength()) < 0) return -1; else if (m1.getTrackLength().compareToIgnoreCase(m2.getTrackLength()) == 0) return 0; else return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().toString().compareTo("TrackLengthComparator") == 0) return true; else return false;
    }
}
