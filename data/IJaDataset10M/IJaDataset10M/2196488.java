package org.hip.kernel.bom.model;

/**
 * 	The PropertyDefDef describes the PropertyDef.
 * 
 * 	@author	Benno Luthiger
 */
public interface PropertyDefDef extends MetaModelObject {

    public static final String propertyTypeSimple = "simple".intern();

    public static final String propertyTypeComposite = "composite".intern();

    public static final String propertyTypeObjectRef = "objectRef".intern();

    public static final String propertyName = "propertyName".intern();

    public static final String propertyType = "propertyType".intern();

    public static final String valueType = "valueType".intern();

    public static final String formatPattern = "formatPattern".intern();

    public static final String mappingDef = "mappingDef".intern();

    public static final String relationshipDef = "relationshipDef".intern();
}
