package de.tum.in.botl.metamodel.implementation;

import java.util.Map;
import de.tum.in.botl.metamodel.interfaces.ExtensibleMetamodelInterface;
import de.tum.in.botl.metamodel.interfaces.ExtensibleMetamodelWithIdInterface;
import de.tum.in.botl.metamodel.interfaces.MetamodelFactory;

public class StandardMetamodelFactory implements MetamodelFactory {

    private Map typeNamesToBasicTypes;

    private Map typeNamesToDefaultValues;

    public StandardMetamodelFactory(Map typeNamesToBasicTypes, Map typeNamesToDefaultValues) {
        this.typeNamesToBasicTypes = typeNamesToBasicTypes;
        this.typeNamesToDefaultValues = typeNamesToDefaultValues;
    }

    public ExtensibleMetamodelInterface createExtensibleMetamodel(String name) {
        return new Metamodel(name, typeNamesToBasicTypes, typeNamesToDefaultValues);
    }

    public ExtensibleMetamodelWithIdInterface createExtensibleMetamodelWithId(String name, String id) {
        Metamodel mm = new Metamodel(name, typeNamesToBasicTypes, typeNamesToDefaultValues);
        mm.setId(id);
        return (ExtensibleMetamodelWithIdInterface) mm;
    }

    protected static String getNewId(Object o) {
        return "id" + o.hashCode();
    }
}
