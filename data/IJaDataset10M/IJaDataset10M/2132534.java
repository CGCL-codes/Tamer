package com.abso.mp3tunes.locker.core.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Element;

/**
 * An artist is associated with one or more tracks stored on the Locker.
 */
public final class Artist extends AbstractLockerData {

    /** The unique identifier. */
    private String id;

    /** The name of the artist. */
    private String name;

    /** The total size (in bytes) of the tracks associated with the artist. */
    private long size;

    /** The number of albums associated with the artist. */
    private int albumCount;

    /** The number of tracks associated with the artist. */
    private int trackCount;

    /**
	 * Constructs a new artist.
	 * 
	 * @param item
	 *            the &lt;item&gt; XML element.
	 */
    public Artist(Element item) {
        this.id = LockerDataUtils.getString(item, "artistId");
        this.name = LockerDataUtils.getString(item, "artistName");
        this.size = LockerDataUtils.getLong(item, "artistSize", -1);
        this.albumCount = LockerDataUtils.getInt(item, "albumCount", -1);
        this.trackCount = LockerDataUtils.getInt(item, "trackCount", -1);
    }

    /**
	 * Returns the unique identifier.
	 * 
	 * @return the unique identifier.
	 */
    public String getId() {
        return id;
    }

    /**
	 * Returns the name of the artist.
	 * 
	 * @return the name of the artist.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Returns the total size (in bytes) of the tracks associated with the
	 * artist.
	 * 
	 * @return the total size.
	 */
    public long getSize() {
        return size;
    }

    /**
	 * Returns the number of albums associated with the artist.
	 * 
	 * @return the number of albums.
	 */
    public int getAlbumCount() {
        return albumCount;
    }

    /**
	 * Returns the number of tracks associated with the artist.
	 * 
	 * @return the number of tracks.
	 */
    public int getTrackCount() {
        return trackCount;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public int hashCode() {
        return getId().hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Artist)) {
            return false;
        }
        return getId().equals(((Artist) obj).getId());
    }
}
