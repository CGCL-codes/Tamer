package au.reba.PlaylistManager.Model.SongLinks.Song;

import java.net.InetAddress;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Logger;
import org.jaudiotagger.tag.vorbiscomment.util.Base64Coder;
import au.reba.PlaylistManager.PlaylistManager;
import au.reba.PlaylistManager.Model.SongLinks.Song.SongInfo.SongLength;
import au.reba.PlaylistManager.Util.Logging.PMLogger;

public abstract class AbstractSongInfo implements Comparable<AbstractSongInfo> {

    protected TreeMap<String, SongInfo> songLinkParents = new TreeMap<String, SongInfo>();

    protected PMLogger logger = (PMLogger) Logger.getLogger(PlaylistManager.LOGGER_KEY);

    /**
     * Return the key to use when putting songs into sorted collections. Each 
     * song will have a unique key
     */
    public abstract String getJPlayManKey();

    /**
     * Return the file location, even if file could not actually be found on the 
     * file system
     */
    public abstract String getFileLocation();

    /**
     * Return the length of the song, or 0 if the file could not be found on the
     * file system
     */
    public abstract SongLength getLength();

    public abstract String toString();

    /**
     * @return an "extended" String describing the ISongInfo object
     */
    public abstract String toStringExtended();

    /**
     * @return true if the file read only on the file system, false otherwise
     */
    public abstract boolean isReadOnly();

    /**
	 * Generate a new unique key for this song, of the form 
	 * <user_name>@<machine_name>*<timestamp>*<fileLocation>
	 */
    protected String generateJPlayManKey() {
        String key = System.getProperty("user.name") + "@";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            key += addr.getHostName();
        } catch (Exception e) {
        }
        Date d = new Date();
        key += "*" + d.getTime() + "*" + getFileLocation();
        key = Base64Coder.encode(key);
        return key;
    }

    /**
	 * @return well-formed Vector (ie, will not be null, but may be empty) 
	 * containing all the Song objects this one is linked to. 
	 * 
	 * Songs will be ordered by artist - title (and file location if further 
	 * granularity needed)
	 * 
	 * NB: This will be a COPY of the data stored in this class, so changing 
	 * the returned Vector will have no affect on the actual SongLinks for this 
	 * Song.
	 */
    public Vector<SongInfo> getParentLinks() {
        AbstractSongInfo.ToStringExtendedComparator comparator = new AbstractSongInfo.ToStringExtendedComparator();
        TreeSet<SongInfo> temp = new TreeSet<SongInfo>(comparator);
        temp.addAll(songLinkParents.values());
        Vector<SongInfo> copy = new Vector<SongInfo>();
        for (SongInfo s : temp) copy.add(s);
        return copy;
    }

    /**
	 * Add @param song2 as a parent in a SongLink to this 
	 * @return true if link successfully added, false otherwise (link already 
	 * exists)
	 */
    protected boolean addParentLink(SongInfo song2) {
        if (!songLinkParents.containsKey(song2.getJPlayManKey())) {
            songLinkParents.put(song2.getJPlayManKey(), song2);
            return true;
        } else {
            logger.warning("Song " + toStringExtended() + " already contains a parent link to Song " + song2.toStringExtended());
            return false;
        }
    }

    /**
	 * Remove @param song2 as a parent link to this
	 * @return true if link successfully removed, false otherwise (link did not 
	 * exist)
	 */
    protected boolean removeParentLink(SongInfo song2) {
        if (songLinkParents.containsKey(song2.getJPlayManKey())) {
            songLinkParents.remove(song2.getJPlayManKey());
            return true;
        } else {
            logger.warning("Song " + toStringExtended() + " does not contain a parent link to Song " + song2.toStringExtended());
            return false;
        }
    }

    /**
     * compareTo method from Comparable interface. Compares two AbstractSongInfo
     * objects based on their jPlayMan Keys
     * 
     * @return a negative integer, zero, or a positive integer as this object is 
     *          less than, equal to, or greater than the specified object. If 
     *          the two objects have the same jPlayMan key but are different 
     *          concrete classes then the SongInfo object precedes (is less than)
     *          the SongInfoHolder object
     */
    public int compareTo(AbstractSongInfo obj) {
        if (obj == null) return 1;
        int comparison = getJPlayManKey().compareTo(obj.getJPlayManKey());
        if (comparison == 0) {
            if (!this.getClass().equals(obj.getClass())) if (this instanceof SongInfo) return -1; else return 1;
        }
        return comparison;
    }

    /**
     * Compares two AbstractSongInfo objects based on their jPlayMan Keys
     * 
     * @return true if the supplied Object (another ISongInfo object) is equal 
     *          to this object, false otherwise.
     * 
     * ISongInfo objects are "consistent with equals" (see Comparable interface 
     * spec)
     */
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof AbstractSongInfo)) return false; else return (compareTo((AbstractSongInfo) obj) == 0);
    }

    public int hashCode() {
        return getJPlayManKey().hashCode();
    }

    /**
     * Allows Songs to be added to Collections in the order specified by their
     * toStringExtended method
     * 
     * @author Reba Kearns
     */
    public static class ToStringExtendedComparator implements Comparator<AbstractSongInfo> {

        /**
         * Compare two song objects by their toStringExtended method
         */
        public int compare(AbstractSongInfo arg0, AbstractSongInfo arg1) {
            return arg0.toStringExtended().compareTo(arg1.toStringExtended());
        }
    }
}
