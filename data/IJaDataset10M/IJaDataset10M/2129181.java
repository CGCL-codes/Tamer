package akey.util.cddb;

import java.sql.SQLException;
import java.sql.Connection;
import java.util.Vector;
import akey.util.cddb.CddbEntry;

public abstract class CddbDatabaseInterface {

    public Connection c;

    protected CddbDatabaseInterface() {
    }

    public abstract void openConnection() throws SQLException;

    public abstract Vector getEntryList(long cddbId, int disclen, int[] offsets) throws SQLException;

    public boolean updateEntry(CddbEntry cddbEntry) throws SQLException {
        int artistId = this.getArtistId(cddbEntry.getArtist());
        if (artistId == 0) {
            System.out.println("updateEntry: Artist not in database");
            return false;
        }
        int genreId = this.getGenreId(cddbEntry.getGenre());
        long cddbId = cddbEntry.generateCddbId();
        int cdId = this.locateCd(cddbId, genreId);
        if (cdId == 0) {
            System.out.println("updateEntry: cdId not in database");
            return false;
        }
        int cdVer, entryVer;
        cdVer = this.getVersion(cdId);
        entryVer = cddbEntry.getVersion();
        if (entryVer <= cdVer) {
            System.out.println("updateEntry: Version check failed: " + entryVer + "<=" + cdVer);
            return false;
        }
        deleteCdAndTracks(cdId);
        return insertEntry(cddbEntry);
    }

    public boolean insertEntry(CddbEntry cddbEntry) throws SQLException {
        long cddbId = cddbEntry.generateCddbId();
        int genreId = this.getGenreId(cddbEntry.getGenre());
        int cdId = this.locateCd(cddbId, genreId);
        if (cdId != 0) {
            System.out.println("insertEntry exists:  cddbId " + cddbId + " cdId: " + cdId + " " + genreId);
            return false;
        }
        ;
        String dtitle = cddbEntry.getTitle();
        int artistId = this.getArtistId(cddbEntry.getArtist());
        if (artistId == 0) {
            artistId = this.insertArtist(cddbEntry.getArtist());
            if (artistId == 0) {
                System.out.println("Throw exception: insertArtist failed");
                return false;
            }
        }
        if (cdId == 0) {
            cdId = this.insertCd(cddbId, artistId, dtitle, cddbEntry.getVersion(), cddbEntry.getNumTracks(), cddbEntry.getDiscLength(), cddbEntry.getExtd());
            if (cdId == 0) {
                System.err.println("foobar'ed beyond all expectation!!!");
                System.exit(0);
                return false;
            }
        }
        int numTracks = cddbEntry.getNumTracks();
        if (numTracks == 0) {
            System.out.println("numTracks == 0 !!!");
            System.exit(0);
        }
        for (int i = 0; i < numTracks; i++) {
            this.insertTrack(cdId, i, cddbEntry.getOffset(i), cddbEntry.getTrackTitle(i), artistId, cddbEntry.getExtt(i));
        }
        this.insertCddbId(cdId, cddbId, genreId);
        int numCddbAliases = cddbEntry.getNumCddbIds();
        for (int i = 0; i < numCddbAliases; i++) {
            long id;
            id = cddbEntry.getCddbId(i);
            if (id != 0) this.insertCddbAlias(cdId, cddbId, id, genreId);
        }
        return true;
    }

    protected String massageField(String field) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < field.length(); i++) {
            char ch;
            ch = field.charAt(i);
            if (ch == '\"') sb.append("\\\""); else if (ch == '\'') sb.append("\'\'"); else if (ch == '\\') sb.append("\\\\"); else if (ch == '\n') sb.append("\\n"); else sb.append(ch);
        }
        return sb.toString();
    }

    public abstract void deleteCd(int cdId) throws SQLException;

    public abstract int getVersion(int cdId) throws SQLException;

    public abstract int getGenreId(String s) throws SQLException;

    public abstract int getArtistId(String s) throws SQLException;

    public abstract int insertArtist(String s) throws SQLException;

    public abstract int locateCd(long cddbId, int genreId) throws SQLException;

    public abstract int insertCd(long cddbId, int artistId, String dtitle, int version, int numTracks, int disclen, String extd) throws SQLException;

    public abstract void insertTrack(int cdId, int trackNum, int offset, String ttitle, int artistId, String extt) throws SQLException;

    public abstract void insertCddbId(int cdId, long cddbId, int genreId) throws SQLException;

    public abstract void insertCddbAlias(int cdId, long cddbId, long cddbIdAlias, int genreId) throws SQLException;

    public abstract CddbEntry fetchEntry(long cddbId, String genre) throws SQLException, CddbEntryNotFoundException;

    public abstract void close();

    public abstract void deleteCdAndTracks(int cdId) throws SQLException;
}
