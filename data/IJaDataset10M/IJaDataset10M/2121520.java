package il.ac.tau.dbcourse.db;

import il.ac.tau.dbcourse.db.annotations.SQLTable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@SQLTable(table = "ALBUM")
public class Album extends AbsPersistence {

    private String freedbId = "";

    private String title = "";

    private int length;

    private Genre genre;

    private int year;

    private Artist artist;

    public Album(Artist artist) {
        tableName = PersistenceManager.getTableName(this.getClass());
        this.artist = artist;
        setDirty(true);
    }

    @Override
    public boolean isDirty() {
        if (super.isDirty()) return true;
        if (artist.isDirty()) return true;
        return false;
    }

    public Album(Connection conn, Long id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            tableName = PersistenceManager.getTableName(this.getClass());
            stmt = conn.prepareStatement("select * from " + tableName + " where id=?");
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (!rs.next()) throw new SQLException();
            title = rs.getString("TITLE");
            length = rs.getInt("LENGTH");
            Long genreId = rs.getLong("GENRE_ID");
            if (genreId != 0) genre = new Genre(conn, genreId); else genre = null;
            year = rs.getInt("YEAR");
            artist = new Artist(conn, rs.getLong("ARTIST_ID"));
            this.id = id;
            setDirty(false);
            rs.close();
            stmt.close();
            rs = null;
            stmt = null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
        }
    }

    public List<Track> getAllTracks(Connection conn) throws DBException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("select ID from TRACK where ALBUM_ID=?");
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            List<Track> results = new ArrayList<Track>();
            while (rs.next()) {
                results.add(new Track(conn, rs.getLong("ID")));
            }
            return results;
        } catch (SQLException e) {
            throw new DBException("Could not search for tracks");
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
        }
    }

    public int getTrackCount(Connection conn) throws DBException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("select COUNT(*) \"Track_Count\" from TRACK where ALBUM_ID=?");
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            int result = (rs.next()) ? rs.getInt("Track_Count") : 0;
            rs.close();
            stmt.close();
            return result;
        } catch (SQLException e) {
            throw new DBException("Could not search for tracks");
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
        }
    }

    protected Long saveImpl(Connection conn) {
        try {
            if (artist.isDirty()) artist.saveImpl(conn);
            if ((genre != null) && (genre.isDirty())) genre.saveImpl(conn);
            PreparedStatement stmt;
            if (id == null) {
                stmt = conn.prepareStatement("insert into " + tableName + " " + "(ARTIST_ID, FREEDB_ID, GENRE_ID, LENGTH, YEAR, TITLE) " + "values (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            } else {
                stmt = conn.prepareStatement("update " + tableName + " set " + "ARTIST_ID= ?, FREEDB_ID = ?, GENRE_ID = ?, LENGTH = ?, YEAR = ?, TITLE = ? " + "where ID = ?");
            }
            stmt.setLong(1, artist.getId());
            stmt.setString(2, freedbId);
            stmt.setLong(3, (genre != null) ? genre.getId() : null);
            stmt.setInt(4, length);
            stmt.setInt(5, year);
            stmt.setString(6, title);
            if (id != null) stmt.setLong(7, getId());
            stmt.executeUpdate();
            setDirty(false);
            if (id == null) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    String rowId = rs.getString(1);
                    id = PersistenceManager.getId(conn, this.getClass(), rowId);
                }
                rs.close();
                rs = null;
            }
            stmt.close();
            stmt = null;
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title.compareTo(this.title) != 0) setDirty(true);
        this.title = title;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        if (length != this.length) setDirty(true);
        this.length = length;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        if ((genre == null) && (this.genre != null)) setDirty(true);
        if ((genre != null) && (this.genre == null)) setDirty(true);
        if ((genre != null) && (this.genre != null) && (genre.id != this.genre.id)) setDirty(true);
        this.genre = genre;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        if (year != this.year) setDirty(true);
        this.year = year;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) throws DBException {
        if (artist == null) throw new DBException("Artist can't be null");
        if (artist.id != this.artist.id) setDirty(true);
        this.artist = artist;
    }

    public void setFreedbId(String freedbId) {
        if (freedbId.compareTo(this.freedbId) != 0) setDirty(true);
        this.freedbId = freedbId;
    }

    public String getFreedbId() {
        return freedbId;
    }
}
