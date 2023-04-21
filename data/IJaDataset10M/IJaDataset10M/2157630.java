package org.musicbrainz.model;

import java.util.ArrayList;
import java.util.List;
import org.musicbrainz.webservice.WebService;

/**
 * <p>Represents an Audio CD.</p>
 * 
 * <p>This class represents an Audio CD. A disc can have an ID (the
 * MusicBrainz DiscID), which is calculated from the CD's table of
 * contents (TOC). There may also be data from the TOC like the length
 * of the disc in sectors, as well as position and length of the tracks.</p>
 * 
 * <p>Note that different TOCs, maybe due to different pressings, lead to
 * different DiscIDs. Conversely, if two different discs have the same
 * TOC, they also have the same DiscID (which is unlikely but not
 * impossible). DiscIDs are always 28 characters long and look like this:
 * <code>J68I_CDcUFdCRCIbHSEbTBCbooA-</code>. Sometimes they are also
 * referred to as CDIndex IDs.</p>
 * 
 * <p>The  MusicBrainz {@link WebService} only returns	the DiscID and the
 * number of sectors.</p>
 * 
 * @author Patrick Ruhkopf
 */
public class Disc {

    /**
	 * The length in sectors
	 */
    private Integer sectors;

    /**
	 * The 28-character disc id
	 */
    private String discId;

    /**
	 * The number of the first track
	 */
    private Integer firstTrackNum;

    /**
	 * The number of the last track
	 */
    private Integer lastTrackNum;

    /**
	 * The disc's Tracks
	 */
    private List<Disc.Track> tracks;

    /**
	 * Default constructor
	 */
    public Disc() {
        tracks = new ArrayList<Disc.Track>();
    }

    /**
	 * @return a string containing a 28-character DiscID 
	 */
    public String getDiscId() {
        return discId;
    }

    /**
	 * @param discId the discId to set
	 */
    public void setDiscId(String discId) {
        this.discId = discId;
    }

    /**
	 * @return the sectors
	 */
    public Integer getSectors() {
        return sectors;
    }

    /**
	 * @param sectors the sectors to set
	 */
    public void setSectors(Integer sectors) {
        this.sectors = sectors;
    }

    /**
	 * Returns the number of the first track on this disc.
	 * @return: an integer containing the track number
	 */
    public Integer getFirstTrackNum() {
        return firstTrackNum;
    }

    /**
	 * Sets the number of the first track on this disc.
	 * @param firstTrackNum: an integer containing the track number
	 */
    public void setFirstTrackNum(Integer firstTrackNum) {
        this.firstTrackNum = firstTrackNum;
    }

    /**
	 * Returns the number of the last track on this disc.
	 * @return: an integer containing the track number
	 */
    public Integer getLastTrackNum() {
        return lastTrackNum;
    }

    /**
	 * Sets the number of the last track on this disc.
	 * @param lastTrackNum: an integer containing the track number
	 */
    public void setLastTrackNum(Integer lastTrackNum) {
        this.lastTrackNum = lastTrackNum;
    }

    /**
	 * <p>Returns the sector offset and length of this disc.</p>
	 *
	 * <p>This method returns a list of Disc.Tracks containing the track
	 * offset and length in sectors for all tracks on this disc.
	 * The track offset is measured from the beginning of the disc,
	 * the length is relative to the track's offset. Note that the
	 * leadout track is <em>not</em> included.</p>
	 * 
	 * @return a list of Disc.Tracks that contain offset and length as Integers
	 */
    public List<Disc.Track> getTracks() {
        return tracks;
    }

    /**
	 * <p>Adds a track to the list.</p>
	 *
	 * <p>This method adds a Track (which contains offset, length) to the list of
	 * tracks. The leadout track must <em>not</em> be added. The total
	 * length of the disc can be set using {@link Disc#setSectors(Integer)}.</p>
	 * 
	 * @param track: a Disc.Track that contains offset and length as Integers
	 * @see getTracks()
	 */
    public void addTrack(Disc.Track track) {
        tracks.add(track);
    }

    @Override
    public String toString() {
        return "Disc discId=" + discId + ", sectors=" + getSectors();
    }

    /**
	 * A Track consists of the track offset and length in sectors for all tracks on a disc.
	 * 
	 * @author Chris Colvard
	 */
    public static class Track {

        Integer offset;

        Integer length;

        public Track(Integer offset, Integer length) {
            this.offset = offset;
            this.length = length;
        }

        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }
    }
}
