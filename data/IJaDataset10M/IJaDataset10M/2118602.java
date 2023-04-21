package org.fao.fenix.domain4.client.layer.util;

import java.io.Serializable;

public class OnlineResourceType implements Serializable {

    private String type;

    private String href;

    /**
     * 
     */
    public OnlineResourceType() {
        super();
    }

    /**
     * @param type
     * @param href
     */
    public OnlineResourceType(String type, String href) {
        super();
        this.type = type;
        this.href = href;
    }

    /**
     * @return the href
     */
    public String getHref() {
        return href;
    }

    /**
     * @param href
     *            the href to set
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}
