package org.geonetwork.domain.ebrim.extensionpackage.coreisometadata.association;

import org.geonetwork.domain.ebrim.informationmodel.association.Association;
import org.geonetwork.domain.ebrim.informationmodel.core.datatype.URI;

/**
 * Provides information about constraints which apply to the resource(s).
 * 
 * Source object type:
 * urn:x-ogc:specification:csw-ebrim-cim:ObjectType:ResourceMetadata
 * 
 * Target object type:
 * urn:x-ogc:specification:csw-ebrim:ObjectType:Rights
 * 
 * @author heikki doeleman
 *
 */
public class ResourceConstraints extends Association {

    public ResourceConstraints() {
        super();
        this.associationType = new URI("urn:x-ogc:specification:csw-ebrim-cim:AssociationType:ResourceConstraints");
    }
}
