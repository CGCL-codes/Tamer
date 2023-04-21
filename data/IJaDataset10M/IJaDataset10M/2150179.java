package net.sf.woko.controller;

/**
 * Exception thrown when a facet is not found by the WokoActionBean
 *
 * @author Remi VANKEISBELCK - remi 'at' rvkb.com
 * @since 2.0
 */
public class FacetNotFoundException extends Exception {

    private String facetName;

    private String profileId;

    private Object object;

    /**
     * Create the exception with passed parameters
     * @param facetName the name used to lookup the facet
     * @param profileId the profile id used to lookup the facet
     * @param object the target object used to lookup the facet
     */
    public FacetNotFoundException(String facetName, String profileId, Object object) {
        super("The facet could not be retrieved for supplied parameters" + "\nfacetName=" + facetName + "\nprofileId=" + profileId + "\nobject=" + object);
        this.facetName = facetName;
        this.profileId = profileId;
        this.object = object;
    }

    /**
     * Return the name used to lookup the facet
     * @return the name used to lookup the facet
     */
    public String getFacetName() {
        return facetName;
    }

    /**
     * Return the id of the profile used to lookup the facet
     * @return the id of the profile used to lookup the facet
     */
    public String getProfileId() {
        return profileId;
    }

    /**
     * Return the target object used to lookup the facet
     * @return the target object used to lookup the facet
     */
    public Object getObject() {
        return object;
    }
}
