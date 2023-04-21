package de.psychomatic.mp3db.sorting;

import java.util.Comparator;
import org.apache.log4j.Logger;
import de.psychomatic.mp3db.dblayer.Album;

/**
 * Comparator for cover of albums
 * @author Kykal
 */
public class AlbumCoverComparator implements Comparator {

    /**
     * Compares two albums by cover
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object arg0, Object arg1) {
        int result = 0;
        Album a1 = ((Album) arg0);
        Album a2 = ((Album) arg1);
        if (a1.getCover() != 0 && a2.getCover() == 0) {
            result = -1;
        } else if (a1.getCover() == 0 && a2.getCover() != 0) {
            result = 1;
        }
        return result;
    }

    /**
     * Logger for this class
     */
    private static Logger _log = Logger.getLogger(AlbumCoverComparator.class);
}
