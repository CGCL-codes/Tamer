package org.apache.axis2.builder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;
import org.apache.commons.fileupload.disk.DiskFileItem;

public class DiskFileDataSource implements DataSource {

    private DiskFileItem diskFileItem;

    public DiskFileDataSource(DiskFileItem diskFileItem) {
        this.diskFileItem = diskFileItem;
    }

    public String getContentType() {
        return this.diskFileItem.getContentType();
    }

    public InputStream getInputStream() throws IOException {
        return this.diskFileItem.getInputStream();
    }

    public String getName() {
        return this.diskFileItem.getName();
    }

    public OutputStream getOutputStream() throws IOException {
        return this.diskFileItem.getOutputStream();
    }

    public void delete() {
        this.diskFileItem.delete();
    }
}
