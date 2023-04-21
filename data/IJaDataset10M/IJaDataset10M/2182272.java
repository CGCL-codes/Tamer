package org.in4ama.documentengine.compile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.IOUtils;
import org.in4ama.documentautomator.util.XmlHelper;
import org.w3c.dom.Document;

/** Represents an entry of the ZIP file. */
public class ProjectEntry {

    private String fileName;

    private byte[] content;

    /** Creates a new instance of the ProjectEntry 
	 * @throws IOException */
    public ProjectEntry(String fileName, ZipInputStream in) throws IOException {
        this.fileName = fileName;
        this.content = IOUtils.toByteArray(in);
    }

    /** Returns the name of the file stored in this entry. */
    public String getFileName() {
        return fileName;
    }

    /** Returns the content stored in this entry. */
    public byte[] getContent() {
        return content;
    }

    public Document getContentDoc() throws Exception {
        InputStream in = new ByteArrayInputStream(content);
        return XmlHelper.parseDocumentInputStream(in);
    }
}
