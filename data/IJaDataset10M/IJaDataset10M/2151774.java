package org.soda.dpws.attachments;

import java.util.HashMap;
import java.util.Map;
import javax.activation.DataHandler;

/**
 * 
 * 
 */
public class SimpleAttachment implements Attachment {

    private DataHandler handler;

    private String id;

    private Map<String, String> headers = new HashMap<String, String>();

    private boolean xop;

    /**
   * @param id
   * @param handler
   */
    public SimpleAttachment(final String id, final DataHandler handler) {
        this.id = id;
        this.handler = handler;
    }

    public String getId() {
        return id;
    }

    public DataHandler getDataHandler() {
        return handler;
    }

    /**
   * @param name
   * @param value
   */
    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public boolean isXOP() {
        return xop;
    }

    /**
   * @param xop
   */
    public void setXOP(boolean xop) {
        this.xop = xop;
    }
}
