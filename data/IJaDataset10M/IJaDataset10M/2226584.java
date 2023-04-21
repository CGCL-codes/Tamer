package org.aplikator.client.descriptor;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EntityDTO extends ClientDescriptorBase implements Serializable {

    @SuppressWarnings("unused")
    private EntityDTO() {
    }

    public EntityDTO(String id, String localizedName) {
        super(id, localizedName);
    }

    @Override
    public String toString() {
        return "EntityDTO [" + getId() + "]";
    }
}
