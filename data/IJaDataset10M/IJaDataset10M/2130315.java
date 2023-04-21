package com.netexplode.jtunes.model.media;

/**
 * <code>ItunesTag</code> represents .....
 * 
 * @author ykim
 * @version $Revision: 1.1 $
 * @since 0.1
 */
public class ItunesTag implements Tag {

    private String album;

    private String artist;

    private String comment;

    private String genre;

    private String title;

    private int totalTracks;

    private int trackNumber;

    private int year;

    /**
     * @return Returns the album.
     */
    public String getAlbum() {
        return this.album;
    }

    /**
     * @param album
     *            The album to set.
     */
    public void setAlbum(String album) {
        this.album = album;
    }

    /**
     * @return Returns the artist.
     */
    public String getArtist() {
        return this.artist;
    }

    /**
     * @param artist
     *            The artist to set.
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * @return Returns the comment.
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * @param comment
     *            The comment to set.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return Returns the genre.
     */
    public String getGenre() {
        return this.genre;
    }

    /**
     * @param genre
     *            The genre to set.
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Returns the totalTracks.
     */
    public int getTotalTracks() {
        return this.totalTracks;
    }

    /**
     * @param totalTracks
     *            The totalTracks to set.
     */
    public void setTotalTracks(int totalTracks) {
        this.totalTracks = totalTracks;
    }

    /**
     * @return Returns the trackNumber.
     */
    public int getTrackNumber() {
        return this.trackNumber;
    }

    /**
     * @param trackNumber
     *            The trackNumber to set.
     */
    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    /**
     * @return Returns the year.
     */
    public int getYear() {
        return this.year;
    }

    /**
     * @param year
     *            The year to set.
     */
    public void setYear(int year) {
        this.year = year;
    }
}
