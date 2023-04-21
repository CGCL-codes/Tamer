package org.wportal.jspwiki.dbprovider.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * User: SimonLei
 * Date: 2004-10-10
 * Time: 13:04:24
 * $Id: AttachmentData.java,v 1.1 2004/10/19 07:31:58 echou Exp $
 */
public class AttachmentData {

    private String id;

    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(data);
    }
}
