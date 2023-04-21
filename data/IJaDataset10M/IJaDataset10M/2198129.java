package com.conf;

/**
 *
 * @author marco
 */
public class Qrcode {

    private String url;

    private String dim;

    private String storage;

    /**
     *  blocco di proxy
     */
    public static final String TAG_XML_QRCODE = "qrcode";

    /**
     * 
     */
    public static final String TAG_XML_QRCODE_DIM = "dim";

    /**
     * 
     */
    public static final String TAG_XML_QRCODE_URL = "url";

    /**
     * 
     */
    public static final String TAG_XML_QRCODE_STORAGE = "storage";

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the dim
     */
    public String getDim() {
        return dim;
    }

    /**
     * @param dim
     */
    public void setDim(String dim) {
        this.dim = dim;
    }

    /**
     * 
     * @return
     */
    public String getStorage() {
        return storage;
    }

    /**
     * 
     * @param storage
     */
    public void setStorage(String storage) {
        this.storage = storage;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "url: " + url + " - " + "storage: " + storage + " - " + "dim: " + dim;
    }
}
