package org.vrforcad.lib.network.beans;

import java.io.Serializable;

/**
 * File request bean.
 * @version 1.0 
 * @author Daniel Cioi <dan.cioi@vrforcad.org>
 */
public class POFileRequest implements PassingObject, Serializable {

    private static final long serialVersionUID = 1L;

    private String fileName;

    private int version;

    private String extension;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
