package it.otkbss.rdfVcard4j.binary;

/**
* This class handles the VCard Key object and extends BINARY_VALUE Object. 
* It inherits convenient methods to get base64enc, url and mime_type fiels. 
*  
* 
* @author <a href="mailto:ecaldarola@otkbss.it">Enrico G. Caldarola</a> - <a href="http://www.otkbss.it">www.otkbss.it</a>
* @version 1.0
* @see <a href="http://www.w3.org/TR/vcard-rdf">Representing vCard Objects in RDF/XML</a> 
* @see <a href="http://www.ietf.org/rfc/rfc2426.txt"> vCard MIME Directory Profile</a>
*/
public class KEY extends BINARY_OBJECT {

    /**
	 * This constructor method allows to create a KEY VCard binary object 
	 * 
	 * @author Enrico G. Caldarola
	 * @param base64enc The base64 encoding for this binary object
	 * @param url the url location for this VCard binary object
	 * @param mime_type mime type for this VCard binary object
	 * 
	 * 
	 */
    public KEY(String base64enc, String url, String mime_type) {
        super(base64enc, url, mime_type);
    }
}
