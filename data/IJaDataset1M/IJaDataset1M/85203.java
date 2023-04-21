package org.gbif.portal.model.resources;

import java.io.Serializable;
import org.gbif.portal.model.IntegerEnumType;

/**
 * Resource Type enumerated type.
 * 
 * @author dmartin
 */
public class ResourceType extends IntegerEnumType implements Serializable {

    private static final long serialVersionUID = 4595856724472502195L;

    public static final ResourceType IMAGE_DATA_RESOURCE = new ResourceType("image_data_resource", 1);

    public ResourceType() {
    }

    private ResourceType(String name, int value) {
        super(name, Integer.valueOf(value));
    }
}
