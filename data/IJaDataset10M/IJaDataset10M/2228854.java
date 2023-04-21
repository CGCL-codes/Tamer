package cz.muni.fi.pclis.domain;

import cz.muni.fi.pclis.commons.domain.DomainObject;
import cz.muni.fi.pclis.commons.domain.NamedDomainObject;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;

/**
 * Entity representing file uploaded to the server.
 * User: Ľuboš Pecho
 * Date: 27.3.2010
 * Time: 22:19:37
 */
@Entity
public class UploadedFile extends NamedDomainObject {

    /**
     * location of the file
     */
    private String location;

    /**
     * content type of the file
     */
    private String contentType;

    /**
     * constructs new uplodaed file
     */
    public UploadedFile() {
    }

    /**
     * constructs new uploaded file
     * @param name
     * @param location
     * @param contentType
     */
    public UploadedFile(String name, String location, String contentType) {
        super(name);
        this.location = location;
        this.contentType = contentType;
    }

    /**
     *
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the file
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return content type of the file
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the content type of the file
     * @param contentType
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
