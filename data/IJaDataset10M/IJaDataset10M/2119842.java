package jreceiver.j2me.common.rpc;

import java.util.Vector;
import jreceiver.j2me.common.rpc.RpcException;
import jreceiver.j2me.common.rpc.RpcBase;
import jreceiver.j2me.common.rec.util.TuneQuery;

/**
 * Menu-related queries and updates for a (possibly-remote) JRec server,
 * returning MenuRecs or String-formatted results.
 * <p>
 * <b>TUNE QUERIES</b>
 * <P>
 * A 'tune query' is a shorthand means of specifying a filter.
 * <p>
 * For example, to obtain a list of albums for an artist:
 * <pre>
 * Menus mnu_rpc = RpcFactory.newMenus();
 *
 * TuneQuery tq = new TuneQuery();
 * tq.setArtistName("Pink Floyd");
 *
 * Vector albums = mnu_rpc.getAlbumMenuRecs(tq, driver_id, 0, Menu.NO_LIMIT);
 * Iterator it = albums.iterator();
 * while (it.hasNext()) {
 *    Menu menu = (Menu)it.next();
 *    System.out.println("album_name=" + menu.getMenuText()
 *                       + " tune_count=" + menu.getTuneCount());
 * }
 * </pre>
 *
 * You can even combine several criteria.  For example, query the
 * titles for a specific album that start with "An":
 * <pre>
 * TuneQuery tq = new TuneQuery();
 * tq.setArtistName("Pink Floyd");
 * tq.setAlbumName("The Wall");
 * tq.setTitleName("^An", true);   //true==IS_REG_EXP
 * </pre>
 * might return:
 * <p>
 * Another Brick In The Wall (Part 1)<br>
 * Another Brick In The Wall (Part 2)<br>
 * Another Brick In The Wall (Part 3)<br>
 * Anybody Out There?<br>
 * <p>
 * Note that you can specify whether match will be exact (default) or
 * a regular expression.
 *
 * The regular expression queries are a powerful feature that allow
 * such things as selecting over a large list.  The Rio driver does
 * this to support queries from the remote:
 *
 * <pre>
 *      ^[2abc][5jkl][4ghi].*'
 * </pre>
 *
 * <P>...to find all titles where "2", "a", "b" or "c" is the first letter,
 * etc.
 *
 * <P>Note that the regular-expression capabilities may be limited by
 * the database.   See the MySQL documentation of REGEXP.
 *
 * <P>Once you have the artists, albums, genres or titles you want to
 * play, consult the Tunes.getKeysForQuery() method which is quite
 * similar to the menu interface, but returns tune src_ids.
 *
 *
 * <b>ENCODING</b>
 *
 * Obtain a list of artists, albums, genres or tune titles custom formatted
 * into a large string for use in a menu.
 * <p>
 * You must specify a formatting patthen.  For example, when using encodeAlbums:
 * <pre>
 *     String pattern = "{0,number,#}={1},0,0:{2}\r\n";
 * </pre>
 * where {0} will be replaced with the index (starting at begin), {1} will be
 * replaced with the number of tunes for the artist (as stored in the
 * database) and {2} is the artist name.
 *
 * @author Reed Esau
 * @version $Revision: 1.2 $ $Date: 2002/12/29 00:44:07 $
 */
public interface Menus extends RpcBase {

    public static final String HANDLER_NAME = "Menus";

    public static final String GET_ARTIST_MENU_RECS = "getArtistMenuRecs";

    public static final String GET_ALBUM_MENU_RECS = "getAlbumMenuRecs";

    public static final String GET_GENRE_MENU_RECS = "getGenreMenuRecs";

    public static final String GET_TITLE_MENU_RECS = "getTitleMenuRecs";

    public static final String ENCODE_ARTISTS = "encodeArtists";

    public static final String ENCODE_ALBUMS = "encodeAlbums";

    public static final String ENCODE_GENRES = "encodeGenres";

    public static final String ENCODE_TITLES = "encodeTitles";

    /**
     * obtain a list of MenuRecs that specify artist names
     */
    public Vector getArtistMenuRecs(TuneQuery tune_query, int driver_id, int rec_offset, int rec_count) throws RpcException;

    /**
     * obtain a list of MenuRecs that specify album/cd names
     */
    public Vector getAlbumMenuRecs(TuneQuery tune_query, int driver_id, int rec_offset, int rec_count) throws RpcException;

    /**
     * obtain a list of MenuRecs that specify genre names
     */
    public Vector getGenreMenuRecs(TuneQuery tune_query, int driver_id, int rec_offset, int rec_count) throws RpcException;

    /**
     * obtain a list of MenuRecs that specify tune titles
     */
    public Vector getTitleMenuRecs(TuneQuery tune_query, int driver_id, int rec_offset, int rec_count) throws RpcException;

    /**
     * obtain a list of artists, custom formatted, for use in a menu.
     */
    public String encodeArtists(TuneQuery tune_query, int driver_id, String pattern, int rec_offset, int rec_count) throws RpcException;

    /**
     * obtain a list of albums, custom formatted, for use in a menu.
     */
    public String encodeAlbums(TuneQuery tune_query, int driver_id, String pattern, int rec_offset, int rec_count) throws RpcException;

    /**
     * obtain a list of genres, custom formatted, for use in a menu.
     */
    public String encodeGenres(TuneQuery tune_query, int driver_id, String pattern, int rec_offset, int rec_count) throws RpcException;

    /**
     * obtain a list of titles, custom formatted, for use in a menu.
     */
    public String encodeTitles(TuneQuery tune_query, int driver_id, String pattern, int rec_offset, int rec_count) throws RpcException;
}
