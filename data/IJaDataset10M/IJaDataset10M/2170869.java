package org.columba.core.association;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.columba.core.association.api.IAssociation;

@Entity
public class Association implements IAssociation {

    @Id
    @GeneratedValue
    Long id;

    String itemId;

    String serviceId;

    String metaDataId;

    public Association() {
        super();
    }

    public Association(String iId, String sId, String mdId) {
        itemId = iId;
        serviceId = sId;
        metaDataId = mdId;
    }

    public String getMetaDataId() {
        return metaDataId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getItemId() {
        return itemId;
    }
}
