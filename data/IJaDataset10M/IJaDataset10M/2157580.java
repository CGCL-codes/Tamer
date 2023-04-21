package com.bonkey.filesystem.remote;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import nu.xom.Attribute;
import nu.xom.Element;
import com.bonkey.filesystem.browsable.AbstractBrowsableFile;
import com.bonkey.filesystem.browsable.BrowsableFile;
import com.bonkey.filesystem.browsable.BrowsableItem;
import com.bonkey.filesystem.writable.WritableFileSystem;

public class RemoteWritableFile extends AbstractBrowsableFile {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2934581257204455405L;

    private static final String A_SIZE = "size";

    private static final String A_LAST_MODIFIED = "lastModified";

    /**
	 * The size of this file in bytes.
	 */
    private long size;

    /**
	 * The date that this file was last modified.
	 */
    private Date lastModified;

    public RemoteWritableFile(Element e) {
        super(e);
    }

    public RemoteWritableFile(String name, String filesystem, String uri, Date lastModified, long size) {
        super(name, filesystem, uri);
        this.lastModified = lastModified;
        this.size = size;
    }

    public Date getLastModified() throws IOException {
        return this.lastModified;
    }

    public long getSize() throws IOException {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Element toXML() {
        Element e = super.toXML();
        e.addAttribute(new Attribute(A_SIZE, Long.toString(size)));
        e.addAttribute(new Attribute(A_LAST_MODIFIED, Long.toString(lastModified.getTime())));
        return e;
    }
}
