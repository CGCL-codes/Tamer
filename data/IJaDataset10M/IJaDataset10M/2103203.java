package jreceiver.common.rec.source;

import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;
import org.esau.ptarmigan.util.PtarURI;
import jreceiver.util.HelperMap;
import jreceiver.common.rec.RecException;

/**
 * Bean to represent a station tune.
 *
 * @author Reed Esau
 * @version $Revision: 1.6 $ $Date: 2003/04/27 23:14:31 $
 */
public class StuneRec extends TuneRec implements Mexternal {

    /**
     * empty ctor needed for building via Digester
     */
    public StuneRec() {
    }

    /**
     * ctor
     */
    public StuneRec(int src_id, String mime, int duration, String title, PtarURI direct_uri, URL content_url, boolean cache_data) {
        this(src_id, mime, duration, title, direct_uri, content_url, null, 0, cache_data);
    }

    /**
     * ctor
     */
    public StuneRec(int src_id, String mime, int duration, String title, PtarURI direct_uri, URL content_url, Vector genres, int bitrate, boolean cache_data) {
        super(src_id, mime, duration, TUNE_TYPE_STATION, title, direct_uri, content_url, genres, bitrate);
        m_cache_data = cache_data;
    }

    /**
     * ctor - construct from a hashtable, usually retrieved via XML-RPC
     */
    public StuneRec(Hashtable hash) throws RecException {
        super(hash);
        m_cache_data = HelperMap.getBool(hash, HKEY_CACHE_DATA);
    }

    /**
     * Store the object as a hash suitable for
     * transport over an XML-RPC connection or similar.
     * <p>
     * Use <code>StuneRec(Hashtable)</code> to transform the data
     * back into an object of this type.
     * <p>
     * @return
     */
    public Hashtable toHash() {
        Hashtable hash = super.toHash();
        hash.put(HKEY_CACHE_DATA, new Boolean(m_cache_data));
        return hash;
    }

    /**
     * Assign the cache_data flag
     */
    public void setCacheData(boolean cache_data) {
        m_cache_data = cache_data;
    }

    /**
     * Obtain the cache_data flag.
     */
    public boolean getCacheData() {
        return m_cache_data;
    }

    /**
     */
    public String toString() {
        return new StringBuffer().append("StuneRec: [").append(super.toString()).append(" [cache_data=").append(m_cache_data).append("]]").toString();
    }

    protected boolean m_cache_data;
}
