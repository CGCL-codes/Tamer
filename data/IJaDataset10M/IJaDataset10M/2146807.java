package net.sourceforge.x360mediaserve.util.database.items.containers;

import java.io.Serializable;
import net.sourceforge.x360mediaserve.api.database.items.container.AlbumItem;

public class AlbumItemImpl extends ContainerItemImpl implements AlbumItem {

    private long numberOfSongs;

    private String artist;

    private Serializable artistref;

    /**
	 * 
	 */
    public AlbumItemImpl() {
        super();
    }

    public AlbumItemImpl(AlbumItem item) {
        super(item);
        artist = item.getArtistName();
        artistref = item.getArtistref();
        numberOfSongs = item.getNumberOfSongs();
        albumArtFormat = item.getAlbumArtFormat();
        albumArtLocation = item.getAlbumArtLocation();
    }

    public String getArtistName() {
        return artist;
    }

    public Serializable getArtistref() {
        return artistref;
    }

    public long getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setArtistref(Serializable artistref) {
        this.artistref = artistref;
    }

    public void setNumberOfSongs(long numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    String artistid;

    public String getArtistId() {
        return this.artistid;
    }

    private String albumArtFormat;

    private String albumArtLocation;

    public String getAlbumArtFormat() {
        return albumArtFormat;
    }

    public String getAlbumArtLocation() {
        return albumArtLocation;
    }

    public void setAlbumArtFormat(String albumArtFormat) {
        this.albumArtFormat = albumArtFormat;
    }

    public void setAlbumArtLocation(String albumArtLocation) {
        this.albumArtLocation = albumArtLocation;
    }
}
