package com.google.gwt.user.server.rpc;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.TypeCheckedGenericClass;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetValidator;
import com.google.gwt.user.server.rpc.impl.DequeMap;
import com.google.gwt.user.server.rpc.impl.SerializabilityUtil;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;

/**
 * This class is defined outside of the TypeCheckedObjectTestSetFactory because
 * of a bug where custom field serializers cannot be inner classes. Once we fix
 * this bug we can move this class into the test set factory.
 */
@SuppressWarnings({ "rawtypes", "unused" })
public class TypeCheckedGenericClass_ServerCustomFieldSerializer {

    @SuppressWarnings("unchecked")
    public static void deserializeChecked(ServerSerializationStreamReader streamReader, TypeCheckedGenericClass instance, Type[] expectedParameterTypes, DequeMap<TypeVariable<?>, Type> resolvedTypes) throws SerializationException {
        Object junkKey = streamReader.readObject();
        Object junkValue = streamReader.readObject();
        if (instance.getClass() != TypeCheckedGenericClass.class || ((instance.getMarkerKey() instanceof Integer) && ((Integer) instance.getMarkerKey()).intValue() == 54321 && (instance.getMarkerValue() instanceof String) && ((String) instance.getMarkerValue()).equals("LocalMarker"))) {
            instance.setMarker(TypeCheckedObjectsTestSetValidator.markerKey, TypeCheckedObjectsTestSetValidator.markerValue);
        } else {
            throw new SerializationException("Incorrect markers in TypeCheckedGenericClass server deserialization. " + "Custom instantiate probably not called.");
        }
        try {
            Field declField = TypeCheckedGenericClass.class.getField("hashField");
            Type declGenericType = declField.getGenericType();
            SerializabilityUtil.resolveTypes(declGenericType, resolvedTypes);
            instance.hashField = (HashMap) streamReader.readObject(declGenericType, resolvedTypes);
            SerializabilityUtil.releaseTypes(declGenericType, resolvedTypes);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void deserialize(ServerSerializationStreamReader streamReader, TypeCheckedGenericClass instance) throws SerializationException {
        Object junkKey = streamReader.readObject();
        Object junkValue = streamReader.readObject();
        if (instance.getClass() != TypeCheckedGenericClass.class || ((instance.getMarkerKey() instanceof Integer) && ((Integer) instance.getMarkerKey()).intValue() == 54321 && (instance.getMarkerValue() instanceof String) && ((String) instance.getMarkerValue()).equals("LocalMarker"))) {
            instance.setMarker(TypeCheckedObjectsTestSetValidator.markerKey, TypeCheckedObjectsTestSetValidator.markerValue);
        } else {
            throw new SerializationException("Incorrect markers in TypeCheckedGenericClass server deserialization. " + "Custom instantiate probably not called.");
        }
        instance.hashField = (HashMap) streamReader.readObject();
    }

    public static TypeCheckedGenericClass instantiateChecked(ServerSerializationStreamReader streamReader, Type[] expectedParameterTypes, DequeMap<TypeVariable<?>, Type> resolvedTypes) {
        TypeCheckedGenericClass<Integer, String> result = new TypeCheckedGenericClass<Integer, String>();
        result.setMarker(54321, "LocalMarker");
        return result;
    }

    public static TypeCheckedGenericClass instantiate(ServerSerializationStreamReader streamReader) {
        TypeCheckedGenericClass<Integer, String> result = new TypeCheckedGenericClass<Integer, String>();
        result.setMarker(54321, "LocalMarker");
        return result;
    }
}
