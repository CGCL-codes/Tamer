package model;

import java.util.List;

/**
 *
 * @author rayman
 */
public class JOFSProperty {

    private String propertyURI;

    private List<String> destinationURIs;

    public JOFSProperty(String propertyURI, List<String> destinationURIs) {
        this.propertyURI = propertyURI;
        this.destinationURIs = destinationURIs;
    }

    public List<String> getDestinationURIs() {
        return destinationURIs;
    }

    public void setDestinationURIs(List<String> destinationURIs) {
        this.destinationURIs = destinationURIs;
    }

    public String getPropertyURI() {
        return propertyURI;
    }

    public void setPropertyURI(String propertyURI) {
        this.propertyURI = propertyURI;
    }
}
