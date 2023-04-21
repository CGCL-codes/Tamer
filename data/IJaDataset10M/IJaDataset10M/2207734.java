package org.geonetwork.domain.ebrim.extensionpackage.basicextension.slottype;

import org.geonetwork.domain.ebrim.informationmodel.core.Slot;
import org.geonetwork.domain.ebrim.informationmodel.core.datatype.LongName;
import org.hibernate.search.annotations.Indexed;

/**
 * 
 * Spatial characteristics of the intellectual content of the resource.
 * 
 * 
 * From OGC 07-144r2 : CSW-ebRIM_Registry_Service__Part_2_Basic_extension_package.pdf:
 * 
 * Table 45 � Slot: Spatial Name http://purl.org/dc/terms/spatial Definition Spatial characteristics
 * of the intellectual content of the resource. Source DCMI Metadata terms
 * <http://dublincore.org/documents/dcmi-terms/#spatial> Slot type
 * urn:oasis:names:tc:ebxml-regrep:DataType:ObjectRef Parent object type
 * urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject
 * 
 * The slotType attribute value refers to a geographic scheme or a geometry data type node.
 * 
 * @author heikki
 * 
 */
@Indexed
public class SpatialSlot extends Slot {

    public SpatialSlot() {
        slotType = new LongName("urn:oasis:names:tc:ebxml-regrep:DataType:ObjectRef");
    }
}
