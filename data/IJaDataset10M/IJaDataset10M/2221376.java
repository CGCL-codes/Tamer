package org.metadon.beans;

/**
 *
 * @author Hannes
 */
public class BlogMultimedia extends Blog {

    private Photo photo = null;

    /**
     * Creates a new instance of BlogMultimedia
     */
    public BlogMultimedia() {
        super();
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Photo getPhoto() {
        return this.photo;
    }
}
