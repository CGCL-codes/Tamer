package org.zeroexchange.model.i18n;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.zeroexchange.model.resource.Resource;

/**
 * The table that contains the Resource strings.
 * 
 * @author black
 */
@Entity
public class ResourceStrings extends Strings {

    public static final String FIELD_RESOURCE = "resource";

    private Resource resource;

    @ManyToOne
    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
