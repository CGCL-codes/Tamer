package org.s3b.search.rdf;

/**
 * An RDF URI.
 * 
 * @author Mariusz Cygan
 */
public class URI implements Resource {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof URI && uri != null) {
            return uri.equals(((URI) obj).getURI());
        }
        return false;
    }

    private String uri;

    /**
	 * Creates a new URI with the supplied uri.
	 * 
	 * @param _uri
	 */
    public URI(String _uri) {
        this.uri = _uri;
    }

    /**
	 * Returns the String-representation of this uri.
	 * 
	 * @return
	 */
    public String getURI() {
        return uri;
    }

    /**
	 * Returns the String-representation of this uri.
	 * 
	 * @return
	 */
    @Override
    public String toString() {
        return uri;
    }
}
