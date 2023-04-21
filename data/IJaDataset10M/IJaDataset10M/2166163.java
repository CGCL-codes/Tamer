package org.geonetwork.domain.ebrim.extensionpackage.basicextension.association;

import org.geonetwork.domain.ebrim.informationmodel.association.Association;
import org.geonetwork.domain.ebrim.informationmodel.core.datatype.URI;
import org.hibernate.search.annotations.Indexed;

/**
 * 
 * Associates a Service with a ServiceProfile resource that describes its essential capabilities.
 * 
 * 
 * 8.2.2 Presents The "Presents" association relates a Service offer with a ServiceProfile resource
 * (see 8.1.2) that describes its essential capabilities; this association type derives from the top
 * level of the OWL-S service ontology [OWL-S]. For an association of this type, the source and
 * target objects shall be of the types indicated in Figure 4. i.e. Service and ServiceProfile,
 * resp.
 * 
 * Table 17 � Association type: Presents Property Value Identifier
 * urn:ogc:def:ebRIM-AssociationType:OGC:Presents Name Presents Description Associates a Service
 * with a ServiceProfile resource that describes its essential capabilities. Parent
 * urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType Code Presents NOTE See
 * <http://www.daml.org/services/owl-s/1.1/overview/>.
 * 
 * @author heikki
 * 
 */
@Indexed
public class Presents extends Association {

    public Presents() {
        super();
        associationType = new URI();
        associationType.setValue("urn:ogc:def:ebRIM-ObjectType:OGC:ServiceProfile");
    }
}
