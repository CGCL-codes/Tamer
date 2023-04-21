package net.sourceforge.retriever.fetcher;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * A resource is a container for crawled information.
 * </p>
 * 
 * <p>
 * Resources are fetched by the crawler and then boil up for third-party applications 
 * to consume it.
 * </p>
 */
public class Resource {

    private URL url;

    private String contentType;

    private String charset;

    private InputStream inputStream;

    private final List<String> innerURLs = new ArrayList<String>();

    /**
	 * <p>
	 * Constructs a <code>Resource</code> object.
	 * </p>
	 * 
	 * <p>
	 * Every resource has an associated URL.
	 * </p>
	 * 
	 * @param url The associated URL.
	 */
    public Resource(final URL url) {
        this.url = url;
    }

    /**
	 * Returns the resource's URL.
	 * 
	 * @return The resource's URL.
	 */
    public URL getURL() {
        return this.url;
    }

    /**
	 * Modifies the associated URL.
	 * 
	 * @param url The new URL.
	 */
    public void setURL(final URL url) {
        this.url = url;
    }

    /**
	 * Returns the resource's content-type.
	 * 
	 * @return The resource's content-type.
	 */
    public String getContentType() {
        return this.contentType;
    }

    /**
	 * Defines a content-type for the resource.
	 * 
	 * @param contentType The resource's content-type.
	 */
    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    /**
	 * Returns the resource's charset.
	 * 
	 * This is guess based on the analysis of byte portions, so the result
	 * may not be accurate or even null.
	 * 
	 * @return the resource's charset.
	 */
    public String getCharset() {
        return this.charset;
    }

    /**
	 * Sets the resource's charset.
	 * 
	 * @param charset The resource's charset.
	 */
    public void setCharset(final String charset) {
        this.charset = charset;
    }

    /**
	 * Returns an InputStream to the resource's data.
	 * 
	 * @return An InputStream to the resource's data.
	 */
    public InputStream getInputStream() {
        return this.inputStream;
    }

    /**
	 * Sets an InputStream to the resource's data.
	 * 
	 * @param inputStream Stream to the resource's data.
	 */
    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
	 * <p>
	 * Some resources have inner resources associated. Examples are http resources
	 * and file resource. Every hyperlinked resource or every resource inside a folder
	 * in a hard disk is an inner resource.
	 * </p>
	 * 
	 * <p>
	 * This method allows these inner resources to be retrieved.
	 * </p>
	 * 
	 * @return The inner resources.
	 */
    public List<String> getInnerURLs() {
        return this.innerURLs;
    }

    /**
	 * Adds an inner resource.
	 * 
	 * @param innerURL A resource to be added as a child.
	 */
    public void addInnerURL(final String innerURL) {
        this.innerURLs.add(innerURL);
    }

    /**
	 * Adds a set of inner resources.
	 * 
	 * @param innerURLs Resources to be added as children.
	 */
    public void addInnerURLs(final List<String> innerURLs) {
        this.innerURLs.addAll(innerURLs);
    }
}
