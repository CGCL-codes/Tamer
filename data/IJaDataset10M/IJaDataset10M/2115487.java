package org.nymph.storage.data;

import org.nymph.storage.AbstractPersistent;

/**
 * Data class keeps information songs in playlists.
 *
 * @author Sergey Krutsenko
 *
 * @version 1.0
 *
 */
public class SongsByPlaylists extends AbstractPersistent {

    private int playlistId;

    private int orderId;

    private int songId;

    /**
	 * Create a new <code>SongsByPlaylists</code> instance
	 */
    public SongsByPlaylists() {
        super();
    }

    /**
     * Returns the name of the table to contain instances of this class
     * @return
     */
    protected String getTableName() {
        return "SongsByPlaylist";
    }

    /**
	 * @return the playlistId
	 */
    public int getPlaylistId() {
        return playlistId;
    }

    /**
	 * @param playlistId the playlistId to set
	 */
    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    /**
	 * @return the orderId
	 */
    public int getOrderId() {
        return orderId;
    }

    /**
	 * @param orderId the orderId to set
	 */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
	 * @return the songId
	 */
    public int getSongId() {
        return songId;
    }

    /**
	 * @param songId the songId to set
	 */
    public void setSongId(int songId) {
        this.songId = songId;
    }

    /**
	 * Returns SQL query to create the SongsByPlaylists table
	 * (overridden because of performance for reflection).
	 * @return the String query's text
	 */
    @Override
    public String getCreateQuery() {
        final StringBuffer query = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
        query.append(getTableName());
        query.append(" (playlistId INTEGER, orderId INTEGER, songId INTEGER)");
        return query.toString();
    }

    /**
	 * Returns SQL query to select all records from the SongsByPlaylists table.
	 * @return the String query's text
	 */
    @Override
    public String getSelectQuery() {
        return "SELECT playlistId, orderId, songId FROM " + getTableName();
    }

    /**
	 * Returns SQL query to select all songs that belong to the playlist.
	 * @param id the int playlist's id
	 * @return the String query's text
	 */
    public String getSelectQueryById(final int id) {
        return "SELECT playlistId, orderId, songId FROM " + getTableName() + " WHERE playlistId=" + String.valueOf(id);
    }
}
