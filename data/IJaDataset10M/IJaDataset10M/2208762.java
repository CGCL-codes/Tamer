package gcr.mmm2.model;

import gcr.mmm2.rdb.RDBConnection;
import gcr.mmm2.rdb.ResultSetWrapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Breaking up the MediaManager class
 * 
 * @author Benjamin
 * 
 */
public class AlbumFactory {

    public static IAlbum createAlbum(String title, IUser user) {
        int id = RDBConnection.getNextVal("media_id_seq");
        try {
            String query = "insert into album (id, title, created_by, date_added) " + "values (" + id + ", '" + title + "', " + user.getID() + ", now())";
            RDBConnection.execute(query);
            MediaManager.createRDFAlbumEntry(id);
            return new AlbumImpl(id);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List listAlbums(IPhoto photo) {
        return listAlbums(null, photo, MediaManager.SORT_DATE_DESC);
    }

    public static List listAlbums(IPhoto photo, int sortOrder) {
        return listAlbums(null, photo, sortOrder);
    }

    public static List listAlbums(IUser user) {
        return listAlbums(user, null, MediaManager.SORT_DATE_DESC);
    }

    public static List listAlbums(IUser user, int sortOrder) {
        return listAlbums(user, null, sortOrder);
    }

    public static List listAlbums(IUser user, IPhoto photo, int sortOrder) {
        String query = "as the_date from album a left join album_contents ac on a.id=ac.album_id where a.date_inactive is null ";
        if (user != null) {
            query = query + " and a.created_by=" + user.getID();
        }
        if (photo != null) {
            query = query + " and ac.photo_id=" + photo.getID() + " and ac.date_inactive is null";
        }
        if (sortOrder == MediaManager.SORT_DATE_DESC) {
            query = "select a.id as the_id, max(ac.date_added) " + query + " group by the_id order by the_date desc";
        } else if (sortOrder == MediaManager.SORT_DATE_ASC) {
            query = "select a.id as the_id, min(ac.date_added) " + query + " group by the_id order by the_date asc";
        }
        return listAlbums(query);
    }

    protected static List listAlbums(String query) {
        ResultSetWrapper rsw = RDBConnection.executeQuery(query);
        ResultSet rs = rsw.getResultSet();
        ArrayList l = new ArrayList();
        try {
            while (rs.next()) {
                int id = rs.getInt(1);
                IAlbum p = AlbumFactory.getAlbumByID(id);
                if (p != null) {
                    l.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rsw.closeAll();
        }
        return l;
    }

    public static IAlbum getAlbumByID(int id) {
        return CacheManager.getAlbum(id);
    }
}
