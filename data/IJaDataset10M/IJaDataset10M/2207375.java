package org.geonetwork.domain.ebrim.extensionpackage.basicextension.datatype;

/**
 * A homogeneous geometry collection that includes one or more Surface members.
 * 
 * 
 * From OGC 07-144r2 : CSW-ebRIM_Registry_Service__Part_2_Basic_extension_package.pdf:
 * 
 * 8.3.11 GM_MultiSurface The GM_MultiSurface data type represents a homogeneous geometry collection that includes one
 * or more Surface members (see ISO 19107, Clause 6.5.6). The lexical representation is a <gml:MultiSurface> element.
 * 
 * Table 33 � Data type: GM_MultiSurface Property Value Identifier urn:ogc:def:dataType:ISO-19107:GM_MultiSurface Name
 * GM_ MultiSurface Description A homogeneous geometry collection that includes one or more gml:Surface members. It is
 * represented by a gml:MultiSurface element. Parent urn:ogc:def:dataType:ISO-19107: GM_Aggregate Code GM_MultiSurface
 * 
 * @author heikki
 * 
 */
public class GM_MultiSurface extends GM_Aggregate {
}
