package fr.cnes.sitools.portal.multidatasets.opensearch.dto;

/**
 * DTO object to represent opensearch informations
 * 
 * @author AKKA Technologies
 * 
 * @version 
 *
 */
public class OpensearchDescriptionDTO {

    /**
   * Url of the feed
   */
    private String urlFeed;

    /**
   * url of the resource
   */
    private String urlResource;

    /**
   * The id of the opensearch
   */
    private String idOs;

    /**
   * Gets the urlFeed value
   * @return the urlFeed
   */
    public String getUrlFeed() {
        return urlFeed;
    }

    /**
   * Sets the value of urlFeed
   * @param urlFeed the urlFeed to set
   */
    public void setUrlFeed(String urlFeed) {
        this.urlFeed = urlFeed;
    }

    /**
   * Gets the urlResource value
   * @return the urlResource
   */
    public String getUrlResource() {
        return urlResource;
    }

    /**
   * Sets the value of urlResource
   * @param urlResource the urlResource to set
   */
    public void setUrlResource(String urlResource) {
        this.urlResource = urlResource;
    }

    /**
   * Sets the value of idOs
   * @param idOs the idOs to set
   */
    public void setIdOs(String idOs) {
        this.idOs = idOs;
    }

    /**
   * Gets the idOs value
   * @return the idOs
   */
    public String getIdOs() {
        return idOs;
    }
}
