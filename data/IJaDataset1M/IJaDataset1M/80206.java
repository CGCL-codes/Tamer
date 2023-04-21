package de.beas.explicanto.template;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * TemplateResource encodes a resource from the template. A resource is a file
 * from the template archive.
 * 
 * @author marius.staicu
 * @version 1.0
 *  
 */
public class TemplateResource {

    private boolean template = false;

    private String name = "";

    private ByteArrayInputStream content = null;

    private long size;

    /**
	 * Extract the resource ZipEntry from the ZipInputStream.
	 * 
	 * @param zipStream -
	 *            the template stream
	 * @param entry -
	 *            the zip entry for the current resource
	 * @throws IOException
	 */
    public TemplateResource(ZipInputStream zipStream, ZipEntry entry) throws IOException {
        if (null == zipStream) throw new NullPointerException();
        if (null == entry) throw new NullPointerException();
        name = entry.getName();
        if (name.equalsIgnoreCase(TemplateResources.TEMPLATE_ROOT)) template = true;
        size = entry.getSize();
        content = readContent(zipStream);
    }

    /**
	 * @return the resource name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return the resource content.
	 */
    public ByteArrayInputStream getContent() {
        content.reset();
        return content;
    }

    /**
	 * @return -true if the resource is the TEMPLATE_ROOT file <br>
	 *         -false if the resource is a normal file.
	 */
    public boolean isTemplate() {
        return template;
    }

    /**
	 * @return the size of the resource.
	 */
    public long getSize() {
        return size;
    }

    /**
	 * 
	 * Reads the content from the ZIP file into a ByteArrayInputStream
	 * 
	 * @param stream
	 * @return InputStream associated with the current resource
	 * @throws IOException
	 */
    private ByteArrayInputStream readContent(ZipInputStream stream) throws IOException {
        byte[] buffer = new byte[(int) size];
        int offset = 0;
        int remaining = (int) size;
        int read = 0;
        while (remaining > 0) {
            read = stream.read(buffer, offset, remaining);
            if (read > 0) {
                offset += read;
                remaining -= read;
            }
        }
        return new ByteArrayInputStream(buffer);
    }
}
