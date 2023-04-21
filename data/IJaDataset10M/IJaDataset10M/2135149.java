package net.smdp.java.lib;

import net.smdp.java.lib.exception.*;
import java.sql.*;

class SmdpDbAddManager {

    private SmdpDatabaseManager smdpDatabaseManager = null;

    private SmdpSqlGenerator sqlGen = null;

    /** Creates new SmpDbAddManager */
    SmdpDbAddManager(SmdpDatabaseManager smdpDatabaseManager) {
        this.smdpDatabaseManager = smdpDatabaseManager;
        this.sqlGen = new SmdpSqlGenerator();
    }

    /**
     * add's an artist to a/the database
     * @param String containing the artists-name
     * @return int containing the new artist-id or an id pointing to an
     * already existing entry for this artist.
     * @exception SmdpDatabaseException if something goes wrong
     * @exception SmdpDuplicateException if the artist already exists
     */
    int addArtist(String artistName) throws SmdpDatabaseException, SmdpDuplicateException {
        SmdpDbQueryManager queryManager = new SmdpDbQueryManager(this.smdpDatabaseManager);
        int artistId = queryManager.getArtistIdByName(artistName);
        if (artistId == 0) {
            String statement = sqlGen.insertIntoTable(smdpDatabaseManager.TBL_ARTIST, "artist_name", artistName);
            try {
                this.smdpDatabaseManager.sendUpdate(statement);
            } catch (SQLException e) {
                throw new SmdpDatabaseException(e.getMessage());
            }
            artistId = queryManager.getArtistIdByName(artistName);
        } else {
            throw new SmdpDuplicateException("Artist '" + artistName + "' already exists!");
        }
        return (artistId);
    }

    /**
     * add's an album to a/the database
     * @param String containing the albums-name
     * @return int containing the new album-id or an id pointing to an
     * already existing entry for this album.
     * @exception SmdpDatabaseException if something goes wrong
     * @exception SmdpDuplicateException if the album already exists
     */
    int addAlbum(String albumName, int artistId) throws SmdpDatabaseException, SmdpDuplicateException {
        SmdpDbQueryManager queryManager = new SmdpDbQueryManager(this.smdpDatabaseManager);
        int albumId = queryManager.getAlbumIdByName(albumName);
        if (albumId == 0) {
            String[] row = { "artist_id", "album_name" };
            String[] value = { Integer.toString(artistId), albumName };
            String statement = sqlGen.insertIntoTable(smdpDatabaseManager.TBL_ALBUM, row, value);
            try {
                this.smdpDatabaseManager.sendUpdate(statement);
            } catch (SQLException e) {
                throw new SmdpDatabaseException(e.getMessage());
            }
            artistId = queryManager.getAlbumIdByName(albumName);
        } else {
            throw new SmdpDuplicateException("Album '" + albumName + "' already exists!");
        }
        return (albumId);
    }
}
