package jreceiver.common.callback.rec;

import java.util.Locale;
import java.util.Hashtable;
import java.net.URL;
import java.net.MalformedURLException;
import jreceiver.util.HelperMap;

/**
 * Bean to represent a url setting item
 *
 * @author Reed Esau
 * @version $Revision: 1.2 $ $Date: 2002/07/20 01:40:43 $
 */
public final class DsettingUrlRec extends DsettingRec implements DsettingUrl {

    public DsettingUrlRec(String id, boolean is_required, String name, String desc, Locale locale, URL def_url) {
        super(id, DSETTING_TYPE_URL, is_required, name, desc, locale);
        this.value = null;
        this.def_value = def_url;
    }

    /**
     * ctor - construct from a hashtable, usually retrieved via XML-RPC
     */
    public DsettingUrlRec(Hashtable hash) throws MalformedURLException {
        super(hash);
        String s_url = HelperMap.get(hash, DsettingUrl.HKEY_URL_VALUE);
        if (s_url != null && s_url.trim().length() > 0) value = new URL(s_url); else value = null;
        String s_def_url = HelperMap.get(hash, DsettingUrl.HKEY_URL_DEF_VALUE);
        if (s_def_url != null && s_def_url.trim().length() > 0) def_value = new URL(s_def_url); else def_value = null;
    }

    public URL getUrl() {
        return (URL) value;
    }

    public void setUrl(URL url) {
        this.value = url;
    }

    public String getValueUrl() {
        return value != null ? ((URL) value).toExternalForm() : "";
    }

    public void setValueUrl(String s_url) throws MalformedURLException {
        if (s_url != null && s_url.trim().length() > 0) value = new URL(s_url); else value = null;
    }

    public URL getDefaultUrl() {
        return (URL) def_value;
    }

    public void setDefaultUrl(URL def_url) {
        this.def_value = def_url;
    }

    public String getDefaultValueUrl() {
        return def_value != null ? ((URL) def_value).toExternalForm() : "";
    }

    public void setDefaultValueUrl(String s_def_url) throws MalformedURLException {
        if (s_def_url != null && s_def_url.trim().length() > 0) def_value = new URL(s_def_url); else def_value = null;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    public String toString() {
        return new StringBuffer().append("DsettingUrlRec: [").append(super.toString()).append("]").toString();
    }

    /**
     * Store the object as a hash suitable for
     * transport over an XML-RPC connection or similar.
     * <p>
     * Use <code>DsettingRec(Hashtable)</code> to transform the data
     * back into an object of this type.
     * <p>
     * @return
     */
    public Hashtable toHash() {
        Hashtable hash = super.toHash();
        if (value != null) hash.put(DsettingUrl.HKEY_URL_VALUE, getValueUrl());
        if (def_value != null) hash.put(DsettingUrl.HKEY_URL_DEF_VALUE, getDefaultValueUrl());
        return hash;
    }
}
