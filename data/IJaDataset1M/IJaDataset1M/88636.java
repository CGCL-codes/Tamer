package jreceiver.j2me.common.rec.source;

import java.util.Hashtable;
import jreceiver.j2me.common.rec.BaseRec;
import jreceiver.j2me.util.HelperMap;

/**
 * Bean to represent a single filterable source
 *
 * @author Reed Esau
 * @version $Revision: 1.1 $ $Date: 2002/11/29 07:30:04 $
 */
public class FilterableRec extends BaseRec implements Filterable {

    /** ctor - default */
    public FilterableRec() {
        this(0, null);
    }

    /** ctor - normal */
    public FilterableRec(int pl_src_id, String raw_filter) {
        m_pl_src_id = pl_src_id;
        m_raw_filter = raw_filter;
    }

    /**
     * ctor - construct from a hashtable, usually retrieved via XML-RPC
     */
    public FilterableRec(Hashtable hash) {
        this(HelperMap.getInt(hash, HKEY_KEY), HelperMap.get(hash, HKEY_FILTER));
    }

    public Object getKey() {
        return new Integer(m_pl_src_id);
    }

    /**
    * assign the Number-derived, Key-derived or String that serves as this Rec's key.
    */
    public void setKey(Object new_key) {
        m_pl_src_id = ((Integer) new_key).intValue();
    }

    public String getRawFilter() {
        return m_raw_filter;
    }

    public void setRawFilter(String raw_filter) {
        m_raw_filter = raw_filter;
    }

    /**
     * Store the object as a hash suitable for
     * transport over an XML-RPC connection or similar.
     * <p>
     * Use <code>FilterableRec(Hashtable)</code> to transform the data
     * back into an object of this type.
     * <p>
     * @return
     */
    public Hashtable toHash() {
        Hashtable hash = new Hashtable();
        hash.put(HKEY_KEY, getKey());
        if (m_raw_filter != null) hash.put(HKEY_FILTER, m_raw_filter);
        return hash;
    }

    /**
     */
    public String toString() {
        return new StringBuffer().append("FilterableRec: [pl_src_id=").append(getKey()).append(", raw_filter=").append(m_raw_filter).append("]]").toString();
    }

    protected int m_pl_src_id = 0;

    protected String m_raw_filter = null;
}
