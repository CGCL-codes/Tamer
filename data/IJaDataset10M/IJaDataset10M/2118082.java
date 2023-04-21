package com.metaparadigm.jsonrpc;

public class BooleanSerializer extends AbstractSerializer {

    private static final long serialVersionUID = 1;

    private static Class[] _serializableClasses = new Class[] { boolean.class, Boolean.class };

    private static Class[] _JSONClasses = new Class[] { Boolean.class, String.class };

    public Class[] getSerializableClasses() {
        return _serializableClasses;
    }

    public Class[] getJSONClasses() {
        return _JSONClasses;
    }

    public ObjectMatch tryUnmarshall(SerializerState state, Class clazz, Object jso) throws UnmarshallException {
        return ObjectMatch.OKAY;
    }

    public Object unmarshall(SerializerState state, Class clazz, Object jso) throws UnmarshallException {
        if (jso instanceof String) {
            try {
                jso = new Boolean((String) jso);
            } catch (Exception e) {
                throw new UnmarshallException("Cannot convert " + jso + " to Boolean");
            }
        }
        if (clazz == boolean.class) {
            return new Boolean(((Boolean) jso).booleanValue());
        } else {
            return jso;
        }
    }

    public Object marshall(SerializerState state, Object o) throws MarshallException {
        return o;
    }
}
