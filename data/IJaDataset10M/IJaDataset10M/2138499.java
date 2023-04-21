package jreceiver.common.rec.source;

import java.net.URL;
import java.util.Hashtable;
import org.esau.ptarmigan.util.PtarURI;
import jreceiver.common.rec.RecException;
import jreceiver.common.rec.site.Site;
import jreceiver.util.HelperMap;

/**
 * Abstract class to provide basic implementation of Playlist interface
 *
 * @author Reed Esau
 * @version $Revision: 1.4 $ $Date: 2003/04/27 23:14:31 $
 */
public abstract class PlaylistRec extends SourceRec implements Playlist {

    /**
     * ctor - minimalist
     */
    protected PlaylistRec(int src_id, int pl_type) {
        super(src_id, null, Site.SITE_HOME, Source.SRCTYPE_PLAYLIST, null, 0);
        m_pl_type = pl_type;
    }

    /**
     * ctor
     */
    protected PlaylistRec(int src_id, String mime, int duration, PtarURI direct_uri, URL content_url, int pl_type, String name, String order_by, int tune_count) {
        super(src_id, name, Site.SITE_HOME, Source.SRCTYPE_PLAYLIST, mime, duration, null, direct_uri, content_url);
        m_pl_type = pl_type;
        m_order_by = order_by;
        m_tune_count = tune_count;
    }

    /**
     * ctor - construct from a hashtable, usually retrieved via XML-RPC
     */
    protected PlaylistRec(Hashtable hash) throws RecException {
        super(hash);
        m_pl_type = HelperMap.getInt(hash, HKEY_PL_TYPE);
        m_order_by = HelperMap.get(hash, HKEY_ORDER_BY);
        m_tune_count = HelperMap.getInt(hash, HKEY_TUNE_COUNT);
    }

    /**
     * Store the object as a hash suitable for
     * transport over an XML-RPC connection or similar.
     */
    public Hashtable toHash() {
        Hashtable hash = super.toHash();
        hash.put(HKEY_PL_TYPE, new Integer(m_pl_type));
        if (m_order_by != null) hash.put(HKEY_ORDER_BY, m_order_by);
        hash.put(HKEY_TUNE_COUNT, new Integer(m_tune_count));
        return hash;
    }

    /**
    * obtain the 'type' of the playlist (Playlist.PLAYLIST_TYPE_*)
    */
    public int getPlaylistType() {
        return m_pl_type;
    }

    /**
    * assign the 'type' of the playlist (Playlist.PLAYLIST_TYPE_*)
    */
    public void setPlaylistType(int pl_type) {
        m_pl_type = pl_type;
    }

    public int getTuneCount() {
        return m_tune_count;
    }

    public void setTuneCount(int tune_count) {
        m_tune_count = tune_count;
    }

    public String getOrderBy() {
        return m_order_by != null ? m_order_by : "";
    }

    public void setOrderBy(String order_by) {
        m_order_by = order_by;
    }

    public boolean getChecked() {
        return m_is_checked;
    }

    public void setChecked(boolean is_checked) {
        m_is_checked = is_checked;
    }

    public boolean getIsDynamicPlaylist() {
        return m_pl_type == PLAYLIST_TYPE_DYNAMIC;
    }

    public boolean getIsTreePlaylist() {
        return m_pl_type == PLAYLIST_TYPE_TREE;
    }

    public boolean getIsFilePlaylist() {
        return m_pl_type == PLAYLIST_TYPE_FILE;
    }

    public boolean getIsStationPlaylist() {
        return m_pl_type == PLAYLIST_TYPE_STATION;
    }

    /**
     */
    public String toString() {
        return new StringBuffer().append("PlaylistRec: [").append(super.toString()).append(" [pl_type=").append(m_pl_type).append(", order_by=").append(m_order_by).append(", tune_count=").append(m_tune_count).append(", is_checked=").append(m_is_checked).append("]]").toString();
    }

    /**
     * Create a PlaylistRec object preserved as a hash
     * that has arrived over an XML-RPC connection
     * or similar.
     * <p>
     * The hash should have been created with
     * <code>toHash()</code>.
     */
    public static SourceRec createInstance(Hashtable hash) throws RecException {
        int pl_type = HelperMap.getInt(hash, Playlist.HKEY_PL_TYPE);
        SourceRec rec = null;
        switch(pl_type) {
            case PlaylistRec.PLAYLIST_TYPE_DYNAMIC:
                rec = new DplaylistRec(hash);
                break;
            case PlaylistRec.PLAYLIST_TYPE_FILE:
                rec = new FplaylistRec(hash);
                break;
            case PlaylistRec.PLAYLIST_TYPE_TREE:
                rec = new HplaylistRec(hash);
                break;
            case PlaylistRec.PLAYLIST_TYPE_STATION:
                rec = new SplaylistRec(hash);
                break;
            default:
                throw new RecException("playlist type not recognized");
        }
        return rec;
    }

    /**
     * Create a basic PlaylistRec object of the specified type.
     * <p>
     *
     * @param pl_type
     * @return
     */
    public static SourceRec createInstance(int pl_type, String name, String order_by, int duration, int tune_count) throws RecException {
        SourceRec rec = null;
        switch(pl_type) {
            case PlaylistRec.PLAYLIST_TYPE_DYNAMIC:
                rec = new DplaylistRec(0, null, duration, null, null, name, order_by, tune_count);
                break;
            case PlaylistRec.PLAYLIST_TYPE_FILE:
                rec = new FplaylistRec(0, null, duration, null, null, name, order_by, tune_count);
                break;
            case PlaylistRec.PLAYLIST_TYPE_TREE:
                rec = new HplaylistRec(0, null, duration, null, null, name, order_by, tune_count);
                break;
            case PlaylistRec.PLAYLIST_TYPE_STATION:
                rec = new SplaylistRec(0, null, duration, null, null, name, order_by, tune_count);
                break;
            default:
                throw new RecException("playlist type not recognized");
        }
        return rec;
    }

    /**
    * File(1), Dynamic(2), Tree(4) or Station(8)
    */
    protected int m_pl_type = 0;

    /**
     * the number of tunes in the playlist, as of the time it
     * was last queried for its count
     */
    protected int m_tune_count = -1;

    /**
     * how the playlist will be sorted
     */
    protected String m_order_by = null;

    /** is the item selected in the form? */
    protected boolean m_is_checked = false;
}
