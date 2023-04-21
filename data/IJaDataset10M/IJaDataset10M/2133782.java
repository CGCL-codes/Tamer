package org.nymph.storage.data;

import org.nymph.storage.AbstractPersistent;
import org.nymph.storage.Database;
import android.database.Cursor;

/**
 * Data class keeps information about songs. 
 *
 * @author Sergey Krutsenko
 *
 * @version 1.0
 *
 */
public class Songs extends AbstractPersistent {

    private int songId;

    private String songUri;

    private String songName;

    private int songScore;

    private int songRating;

    private int artistId;

    private int albumId;

    private int genreId;

    private int typeId;

    /**
	 * Create a new <code>Songs</code> instance
	 */
    public Songs() {
        super();
    }

    /**
     * Returns the name of the table to contain instances of this class
     * @return
     */
    protected String getTableName() {
        return "Songs";
    }

    /**
	 * Returns song's unique ID.
	 * @return the unique int id
	 */
    public int getSongId() {
        return songId;
    }

    /**
	 * Sets song's unigue ID.
	 * @param songId the int id to set
	 */
    public void setSongId(int songId) {
        this.songId = songId;
    }

    /**
	 * Returns song's source.
	 * @return the String defining song's source
	 */
    public String getSongUri() {
        return songUri;
    }

    /**
	 * Sets song's source.
	 * @param songUri the String to set as song's source
	 */
    public void setSongUri(String songUri) {
        this.songUri = songUri;
    }

    /**
	 * Returns song's name.
	 * @return the String as song's name
	 */
    public String getSongName() {
        return songName;
    }

    /**
	 * Sets song's name.
	 * @param songName the String name to set
	 */
    public void setSongName(String songName) {
        this.songName = songName;
    }

    /**
	 * Returns song's score.
	 * @return the songScore
	 */
    public int getSongScore() {
        return songScore;
    }

    /**
	 * Sets song's score.
	 * @param songScore the songScore to set
	 */
    public void setSongScore(int songScore) {
        this.songScore = songScore;
    }

    /**
	 * Returns song's rating.
	 * @return the songRating
	 */
    public int getSongRating() {
        return songRating;
    }

    /**
	 * Sets song's rating.
	 * @param songRating the songRating to set
	 */
    public void setSongRating(int songRating) {
        this.songRating = songRating;
    }

    /**
	 * Returns artist's id to who song is belonged.
	 * @return the artistId
	 */
    public int getArtistId() {
        return artistId;
    }

    /**
	 * Sets artist's id to who song is belonged.
	 * @param artistId the artistId to set
	 */
    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    /**
	 * Returns album id to which song is belonged.
	 * @return the albumId
	 */
    public int getAlbumId() {
        return albumId;
    }

    /**
	 * Sets album id to which song is belonged.
	 * @param albumId the albumId to set
	 */
    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    /**
	 * Returns genre to which song is belonged.
	 * @return the genreId
	 */
    public int getGenreId() {
        return genreId;
    }

    /**
	 * Sets genre to which song is belonged.
	 * @param genreId the gengreId to set
	 */
    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    /**
	 * Returns type of song: audio, video, audio stream, video stream.
	 * @return the typeId
	 */
    public int getTypeId() {
        return typeId;
    }

    /**
	 * Sets type of song: audio, video, stream.
	 * @param typeId the typeId to set
	 */
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    /**
	 * Returns SQL query to create the Songs table
	 * (overridden because of performance for reflection).
	 * @return the String query's text
	 */
    @Override
    public String getCreateQuery() {
        final StringBuffer query = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
        query.append(getTableName());
        query.append(" (songId INTEGER, songUri TEXT, songName TEXT, songScore INTEGER,");
        query.append(" songRating INTEGER, artistId INTEGER, albumId INTEGER, genreId INTEGER, typeId INTEGER)");
        return query.toString();
    }

    /**
	 * Returns SQL query to select all records from the Songs table
	 * (overridden because of performance for reflection).
	 * @return the String query's text
	 */
    @Override
    public String getSelectQuery() {
        return "SELECT songId, songUri, songName, songScore, " + "songRating, artistId, albumId, genreId, typeId FROM " + getTableName();
    }

    /**
	 * Returns SQL query to select only one records from the Songs table.
	 * @param id the song's id
	 * @return the String query's text
	 */
    public String getSelectQueryById(final int id) {
        return "SELECT songId, songUri, songName, songScore, " + "songRating, artistId, albumId, genreId, typeId FROM " + getTableName() + " WHERE songId=" + String.valueOf(id);
    }

    /**
	 * Returns SQL query to select max id from the class table.
	 * @return the query text
	 */
    @Override
    protected String getSelectQueryMaxId() {
        return "SELECT MAX(songId) FROM " + getTableName();
    }
}
