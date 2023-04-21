package org.sbfc.converter.sbml2dot;

/**
 * Holds the uri and id of a Miriam annotation found in an SBML file.
 * 
 * @author rodrigue
 *
 */
public class MiriamAnnotation {

    String uri;

    String id;

    public MiriamAnnotation(String id, String uri) {
        this.id = id;
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
