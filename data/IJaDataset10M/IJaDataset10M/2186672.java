package org.activebpel.rt.bpel.impl.list;

/**
 * Wsdl catalog filter. Current impl only controls row offset and number of rows
 * displayed.
 */
public class AeCatalogListingFilter extends AeListingFilter {

    /** The type uri to select which is null or empty if none selected. */
    private String mTypeURI;

    /** The resource name to filter on, may contain asterisk as wild card. */
    private String mResource;

    /** The namespace to filter on, may contain asterisk as wild card. */
    private String mNamespace;

    /**
    * Constructor.
    */
    public AeCatalogListingFilter() {
        super();
    }

    /**
    * @return Returns the typeURI, which is null or empty if none selected for fuiltering.
    */
    public String getTypeURI() {
        return mTypeURI;
    }

    /**
    * @param aTypeURI The typeURI to set.
    */
    public void setTypeURI(String aTypeURI) {
        mTypeURI = aTypeURI;
    }

    /**
    * @return Returns the resource.
    */
    public String getResource() {
        return mResource;
    }

    /**
    * @param aResource The resource to set.
    */
    public void setResource(String aResource) {
        mResource = aResource;
    }

    /**
    * @return Returns the namespace.
    */
    public String getNamespace() {
        return mNamespace;
    }

    /**
    * @param aNamespace The namespace to set.
    */
    public void setNamespace(String aNamespace) {
        mNamespace = aNamespace;
    }
}
