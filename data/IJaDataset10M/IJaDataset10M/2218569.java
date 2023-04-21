package org.apache.axiom.attachments;

import javax.activation.FileDataSource;
import java.io.File;

public class CachedFileDataSource extends FileDataSource {

    String contentType = null;

    public CachedFileDataSource(File arg0) {
        super(arg0);
    }

    public String getContentType() {
        if (this.contentType != null) {
            return contentType;
        } else {
            return super.getContentType();
        }
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
