package net.firefly.client.model.data;

import java.io.Serializable;
import java.text.Collator;

public class Song implements Comparable<Song>, Cloneable, Serializable {

    private static final long serialVersionUID = 5882214037382735970L;

    protected long databaseItemId;

    protected boolean partOfACompilation;

    protected String genre;

    protected String artistAlbum;

    protected Artist artist;

    protected Album album;

    protected String year;

    protected String title;

    protected String sortTitle;

    protected String discNumber;

    protected String trackNumber;

    protected long size;

    protected long time;

    protected String type;

    private static Collator collator = Collator.getInstance();

    static {
        collator.setStrength(Collator.PRIMARY);
    }

    public Song() {
        this.partOfACompilation = false;
    }

    public String toString() {
        return title;
    }

    public boolean equals(Object o) {
        Song s;
        try {
            s = (Song) o;
        } catch (Exception e) {
            return false;
        }
        if (o == null) {
            return false;
        }
        if (this.databaseItemId == s.databaseItemId && this.partOfACompilation == s.isPartOfACompilation() && ((this.genre != null && this.genre.equals(s.getGenre())) || (this.genre == null && s.getGenre() == null)) && ((this.artist != null && this.artist.equals(s.getArtist())) || (this.artist == null && s.getArtist() == null)) && ((this.album != null && this.album.equals(s.getAlbum())) || (this.album == null && s.getAlbum() == null)) && ((this.year != null && this.year.equals(s.getYear())) || (this.year == null && s.getYear() == null)) && ((this.title != null && this.title.equals(s.getTitle())) || (this.title == null && s.getTitle() == null)) && ((this.type != null && this.type.equals(s.getType())) || (this.type == null && s.getType() == null)) && ((this.discNumber != null && this.discNumber.equals(s.getDiscNumber())) || (this.discNumber == null && s.getDiscNumber() == null)) && ((this.trackNumber != null && this.trackNumber.equals(s.getTrackNumber())) || (this.trackNumber == null && s.getTrackNumber() == null)) && this.size == s.getSize() && this.time == s.getTime()) {
            return true;
        }
        return false;
    }

    /**
	 * @return Returns the album.
	 */
    public Album getAlbum() {
        return album;
    }

    /**
	 * @param album
	 *            The album to set.
	 */
    public void setAlbum(Album album) {
        this.album = album;
    }

    /**
	 * @return Returns the artist.
	 */
    public Artist getArtist() {
        return artist;
    }

    /**
	 * @param artist
	 *            The artist to set.
	 */
    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    /**
	 * @return Returns the databaseItemId.
	 */
    public long getDatabaseItemId() {
        return databaseItemId;
    }

    /**
	 * @param databaseItemId
	 *            The databaseItemId to set.
	 */
    public void setDatabaseItemId(long databaseItemId) {
        this.databaseItemId = databaseItemId;
    }

    /**
	 * @return Returns the discNumber.
	 */
    public String getDiscNumber() {
        return discNumber;
    }

    /**
	 * @param diskNumber
	 *            The diskNumber to set.
	 */
    public void setDiscNumber(String discNumber) {
        this.discNumber = discNumber;
    }

    /**
	 * @return Returns the partOfACompilation.
	 */
    public boolean isPartOfACompilation() {
        return partOfACompilation;
    }

    /**
	 * @param partOfACompilation
	 *            The partOfACompilation to set.
	 */
    public void setPartOfACompilation(boolean partOfACompilation) {
        this.partOfACompilation = partOfACompilation;
    }

    /**
	 * @return Returns the size.
	 */
    public long getSize() {
        return size;
    }

    /**
	 * @param size
	 *            The size to set.
	 */
    public void setSize(long size) {
        this.size = size;
    }

    /**
	 * @return Returns the time.
	 */
    public long getTime() {
        return time;
    }

    /**
	 * @param time
	 *            The time to set.
	 */
    public void setTime(long time) {
        this.time = time;
    }

    /**
	 * @return Returns the title.
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * @param title
	 *            The title to set.
	 */
    public void setTitle(String title) {
        this.title = title;
        if (this.sortTitle == null) {
            this.sortTitle = title;
        }
    }

    /**
    * @return Returns the sortTitle.
    */
    public String getSortTitle() {
        return sortTitle;
    }

    /**
    * @param sortTitle
    *            The sortTitle to set.
    */
    public void setSortTitle(String sortTitle) {
        this.sortTitle = sortTitle;
    }

    /**
	 * @return Returns the trackNumber.
	 */
    public String getTrackNumber() {
        return trackNumber;
    }

    /**
	 * @param trackNumber
	 *            The trackNumber to set.
	 */
    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    /**
	 * @return Returns the year.
	 */
    public String getYear() {
        return year;
    }

    /**
	 * @param year
	 *            The year to set.
	 */
    public void setYear(String year) {
        this.year = year;
    }

    /**
	 * @return Returns the artistAlbum.
	 */
    public String getArtistAlbum() {
        return this.artistAlbum;
    }

    /**
	 * @param artistAlbum
	 *            The artistAlbum to set.
	 */
    public void setArtistAlbum(String artistAlbum) {
        this.artistAlbum = artistAlbum;
    }

    /**
	 * @return the genre
	 */
    public String getGenre() {
        return genre;
    }

    /**
	 * @param genre the genre to set
	 */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
	 * @return the type
	 */
    public String getType() {
        return type;
    }

    /**
	 * @param type the type to set
	 */
    public void setType(String type) {
        this.type = type;
    }

    public Object clone() {
        Song s = new Song();
        s.album = album;
        s.artist = artist;
        s.artistAlbum = artistAlbum;
        s.databaseItemId = databaseItemId;
        s.discNumber = discNumber;
        s.genre = genre;
        s.partOfACompilation = partOfACompilation;
        s.size = size;
        s.time = time;
        s.title = title;
        s.type = type;
        return s;
    }

    @Override
    public int compareTo(Song a) {
        return collator.compare(this.sortTitle, a.getSortTitle());
    }
}
