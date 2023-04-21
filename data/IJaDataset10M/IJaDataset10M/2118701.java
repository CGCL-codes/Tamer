package org.hip.kernel.bom.model;

/**
 * 	Holds information about the property to database 
 *	column mapping.
 * 
 * 	@author		Benno Luthiger
 * 	@see		org.hip.kernel.bom.model.MappingDef
 */
public interface RelationshipDefDef extends MetaModelObject {

    public static final String homeName = "homeName".intern();

    public static final String keyDefName = "keyDefName".intern();

    public static final String columnName = "columnName".intern();

    public static final String domainObject = "domainObject".intern();
}
