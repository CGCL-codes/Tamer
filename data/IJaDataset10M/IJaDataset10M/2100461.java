package it.otkbss.rdfVcard4j.binary;

/**
	* This class is the top class extended by all VCard binary object such as LOGO, PHOTO, SOUND and so on. 
	* It gives convenient methods to get base64enc, url and mime_type fiels. 
	*  
	* 
	* @author <a href="mailto:ecaldarola@otkbss.it">Enrico G. Caldarola</a> - <a href="http://www.otkbss.it">www.otkbss.it</a>
	* @version 1.0
	* 
	* @see <a href="http://www.w3.org/TR/vcard-rdf">Representing vCard Objects in RDF/XML</a> 
	* @see <a href="http://www.ietf.org/rfc/rfc2426.txt"> vCard MIME Directory Profile</a>
	*/
public class BINARY_OBJECT {

    private String base64enc;

    private String url;

    private String mime_type;

    /**
	 * This constructor method allows to create a generic VCard binary object 
	 * 
	 * @author Enrico G. Caldarola
	 * @param base64enc The base64 encoding for this binary object
	 * @param url the url location for this VCard binary object
	 * @param mime_type mime type for this VCard binary object
	 * 
	 * 
	 */
    public BINARY_OBJECT(String base64enc, String url, String mime_type) {
        this.base64enc = base64enc;
        this.url = url;
        this.mime_type = mime_type;
    }

    /**
	 * Get the base64 encoding for this VCard binary object
	 * 
	 * @author Enrico G. Caldarola
	 * @return String The base64 encoding for this binary object
	 * 
	 * 
	 */
    public String getBase64enc() {
        return base64enc;
    }

    /**
	 * Set the base64 encoding for this VCard binary object
	 * 
	 * @author Enrico G. Caldarola
	 * @return void 
	 * @param base64enc The base64 encoding for this binary object
	 * 
	 */
    public void setBase64enc(String base64enc) {
        this.base64enc = base64enc;
    }

    /**
	 * Get the url location for this VCard binary object. That is, the phisical address for such resource
	 * 
	 * @author Enrico G. Caldarola
	 * @return url the url location for this VCard binary object
	 * 
	 * 
	 */
    public String getUrl() {
        return url;
    }

    /**
	 * Set the url location for this VCard binary object
	 * 
	 * @author Enrico G. Caldarola
	 * @return void 
	 * @param url the url location for this VCard binary object
	 * 
	 */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
	 * Get the mime type for this VCard binary object. 
	 * 
	 * @author Enrico G. Caldarola
	 * @return mime type for this VCard binary object
	 * 
	 * 
	 */
    public String getMime_type() {
        return mime_type;
    }

    /**
	 * Set the url location for this VCard binary object
	 * 
	 * @author Enrico G. Caldarola
	 * @return void 
	 * @param mimeType the mimeType for this VCard binary object
	 * 
	 */
    public void setMime_type(String mimeType) {
        mime_type = mimeType;
    }
}
