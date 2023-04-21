package org.objectwiz.ui;

import org.objectwiz.Constants;
import org.objectwiz.EntityRepresentation;
import org.objectwiz.metadata.MappedClass;

/**
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class EnumConverter implements ValueConverter<Object, Object> {

    private MappedClass enumClass;

    public EnumConverter(MappedClass enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public Object convert(Object object) throws Exception {
        if (object == null) return null;
        EntityRepresentation representation = enumClass.getMetadata().getEntityRepresentation();
        return (String) representation.getValue(Constants.PERSISTABLE_ENUM_NAME_ATTRIBUTE, object);
    }

    @Override
    public Object unconvert(Object enumRef) throws Exception {
        if (enumRef == null) return null;
        EntityRepresentation representation = enumClass.getMetadata().getEntityRepresentation();
        Object obj = representation.newInstance(enumClass);
        representation.setValue(Constants.PERSISTABLE_ENUM_NAME_ATTRIBUTE, obj, enumRef.toString());
        return obj;
    }
}
