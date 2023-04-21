package com.adactus.mpeg21.didl.model;

/**
 * 
 * A resource is an individually identifiable asset such as a video or audio clip, an image, or a textual asset. 
 * A resource may also potentially be a physical object. <br/>
 * <br/>
 * All resources must be locatable via an unambiguous address, if not directly embedded in the document.<br/>
 * <br/>
 * If multiple resources are present in a component, they must be equivalent, i.e. linking up the same image on a site and its mirror.<br/>
 * 
 * @author Thomas Rørvik Skjølberg
 *
 */
public interface Resource {

    /**
	 * 
	 * Get the content encoding of this statement, for example .zip or .gz.
	 * 
	 * @return the content encoding, null if none
	 */
    public String getContentEncoding();

    /**
	 * 
	 * Get the encoding of this statement.
	 * 
	 * @return the encoding, null if none
	 */
    public String getEncoding();

    /**
	 * 
	 * Get the mime-type of this resource.
	 * 
	 * @return the mime-type
	 */
    public String getMimeType();

    /**
	 * 
	 * Get the URL reference of the textual value represented by this statement
	 * 
	 * @return the reference or null if not set
	 */
    public String getRef();

    /**
	 * 
	 * Get the plain text content of this statement
	 * 
	 * @return the text content or null if not set
	 */
    public String getTextContent();
}
