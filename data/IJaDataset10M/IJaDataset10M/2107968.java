package org.pustefixframework.webservices.jsonws;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.pustefixframework.webservices.jsonws.serializers.ArraySerializer;
import org.pustefixframework.webservices.jsonws.serializers.BeanSerializer;
import org.pustefixframework.webservices.jsonws.serializers.BooleanSerializer;
import org.pustefixframework.webservices.jsonws.serializers.CalendarSerializer;
import org.pustefixframework.webservices.jsonws.serializers.CharacterSerializer;
import org.pustefixframework.webservices.jsonws.serializers.EnumSerializer;
import org.pustefixframework.webservices.jsonws.serializers.ListSerializer;
import org.pustefixframework.webservices.jsonws.serializers.MapSerializer;
import org.pustefixframework.webservices.jsonws.serializers.NumberSerializer;
import org.pustefixframework.webservices.jsonws.serializers.StringSerializer;
import de.schlund.pfixcore.beans.BeanDescriptorFactory;

/**
 * @author mleidig@schlund.de
 */
public class SerializerRegistry {

    Map<Class<?>, Serializer> serializers;

    BeanSerializer beanSerializer;

    ArraySerializer arraySerializer;

    ListSerializer listSerializer;

    MapSerializer mapSerializer;

    EnumSerializer enumSerializer;

    public SerializerRegistry(BeanDescriptorFactory beanDescFactory) {
        serializers = new HashMap<Class<?>, Serializer>();
        beanSerializer = new BeanSerializer(beanDescFactory);
        arraySerializer = new ArraySerializer();
        listSerializer = new ListSerializer();
        mapSerializer = new MapSerializer();
        enumSerializer = new EnumSerializer();
        serializers.put(String.class, new StringSerializer());
        Serializer ser = new NumberSerializer();
        serializers.put(Byte.class, ser);
        serializers.put(Short.class, ser);
        serializers.put(Integer.class, ser);
        serializers.put(Long.class, ser);
        serializers.put(Float.class, ser);
        serializers.put(Double.class, ser);
        serializers.put(Boolean.class, new BooleanSerializer());
        ser = new CalendarSerializer();
        serializers.put(Calendar.class, ser);
        serializers.put(GregorianCalendar.class, ser);
        serializers.put(Date.class, ser);
        serializers.put(Character.class, new CharacterSerializer());
    }

    public void register(Class<?> clazz, Serializer serializer) {
        serializers.put(clazz, serializer);
    }

    public Serializer getSerializer(Class<?> clazz) {
        Serializer ser = serializers.get(clazz);
        if (ser == null) {
            if (clazz.isArray()) ser = arraySerializer; else if (List.class.isAssignableFrom(clazz)) ser = listSerializer; else if (Map.class.isAssignableFrom(clazz)) ser = mapSerializer; else if (Enum.class.isAssignableFrom(clazz)) ser = enumSerializer; else ser = beanSerializer;
        }
        return ser;
    }

    public Serializer getSerializer(Type type) {
        Class<?> clazz = null;
        if (type instanceof Class) clazz = (Class<?>) type; else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class) clazz = (Class<?>) rawType;
        }
        if (clazz != null) return getSerializer(clazz); else throw new RuntimeException("Type not supported: " + type.getClass() + " " + type);
    }
}
