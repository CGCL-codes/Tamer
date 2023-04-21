package de.linwave.gtm;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import de.linwave.gtm.typehandler.ITypeHandler;
import de.linwave.gtm.typehandler.TypeHandlerBoolean;
import de.linwave.gtm.typehandler.TypeHandlerBooleanArray;
import de.linwave.gtm.typehandler.TypeHandlerByte;
import de.linwave.gtm.typehandler.TypeHandlerByteArray;
import de.linwave.gtm.typehandler.TypeHandlerChar;
import de.linwave.gtm.typehandler.TypeHandlerCharArray;
import de.linwave.gtm.typehandler.TypeHandlerDate;
import de.linwave.gtm.typehandler.TypeHandlerDouble;
import de.linwave.gtm.typehandler.TypeHandlerDoubleArray;
import de.linwave.gtm.typehandler.TypeHandlerFile;
import de.linwave.gtm.typehandler.TypeHandlerFloat;
import de.linwave.gtm.typehandler.TypeHandlerGeneric;
import de.linwave.gtm.typehandler.TypeHandlerGenericArray;
import de.linwave.gtm.typehandler.TypeHandlerGenericCollection;
import de.linwave.gtm.typehandler.TypeHandlerInt;
import de.linwave.gtm.typehandler.TypeHandlerIntArray;
import de.linwave.gtm.typehandler.TypeHandlerLong;
import de.linwave.gtm.typehandler.TypeHandlerLongArray;
import de.linwave.gtm.typehandler.TypeHandlerShort;
import de.linwave.gtm.typehandler.TypeHandlerShortArray;
import de.linwave.gtm.typehandler.TypeHandlerString;
import de.linwave.gtm.typehandler.TypeHandlerStringArray;
import de.linwave.gtm.typehandler.TypeHandlerfFloatArray;

public class FieldInfo implements Serializable {

    private static final ITypeHandler TYPE_HANDLER_BYTE = new TypeHandlerByte();

    private static final ITypeHandler TYPE_HANDLER_CHAR = new TypeHandlerChar();

    private static final ITypeHandler TYPE_HANDLER_INTEGER = new TypeHandlerInt();

    private static final ITypeHandler TYPE_HANDLER_LONG = new TypeHandlerLong();

    private static final ITypeHandler TYPE_HANDLER_SHORT = new TypeHandlerShort();

    private static final ITypeHandler TYPE_HANDLER_FLOAT = new TypeHandlerFloat();

    private static final ITypeHandler TYPE_HANDLER_DOUBLE = new TypeHandlerDouble();

    private static final ITypeHandler TYPE_HANDLER_BOOLEAN = new TypeHandlerBoolean();

    private static final ITypeHandler TYPE_HANDLER_ARRAY_BYTE = new TypeHandlerByteArray();

    private static final ITypeHandler TYPE_HANDLER_ARRAY_CHAR = new TypeHandlerCharArray();

    private static final ITypeHandler TYPE_HANDLER_ARRAY_INTEGER = new TypeHandlerIntArray();

    private static final ITypeHandler TYPE_HANDLER_ARRAY_LONG = new TypeHandlerLongArray();

    private static final ITypeHandler TYPE_HANDLER_ARRAY_SHORT = new TypeHandlerShortArray();

    private static final ITypeHandler TYPE_HANDLER_ARRAY_FLOAT = new TypeHandlerfFloatArray();

    private static final ITypeHandler TYPE_HANDLER_ARRAY_DOUBLE = new TypeHandlerDoubleArray();

    private static final ITypeHandler TYPE_HANDLER_ARRAY_BOOLEAN = new TypeHandlerBooleanArray();

    private static final ITypeHandler TYPE_HANDLER_ARRAY_STRING = new TypeHandlerStringArray();

    private static final ITypeHandler TYPE_HANDLER_STRING = new TypeHandlerString();

    private static final ITypeHandler TYPE_HANDLER_DATE = new TypeHandlerDate();

    private static final ITypeHandler TYPE_HANDLER_FILE = new TypeHandlerFile();

    public int level;

    public Class<?> clasz;

    public Class<?> generic;

    public Field field;

    public boolean isJava;

    public boolean isPrimitive;

    public boolean isCollection;

    public boolean isArray;

    public boolean isIndexed;

    public String globalNameIDX;

    public boolean isReference;

    public Object value;

    public ITypeHandler typeHandler;

    public String classMetaKey;

    public FieldInfo(Class<?> clasz, Field field, int level) {
        String typeName = field.getType().getName();
        this.clasz = clasz;
        this.field = field;
        this.level = level;
        this.isPrimitive = isPrimitive(field.getType());
        this.isCollection = isCollection(field.getType());
        this.isArray = field.getType().isArray();
        this.isReference = (!isPrimitive && (!isCollection && !isArray));
        this.isIndexed = IndexUtils.isIndexed(clasz, field.getName());
        this.globalNameIDX = IndexUtils.getGlobalNameIDX(clasz);
        this.classMetaKey = GlobalName.buildGlobalName(GTM.CLASS, GlobalName.Class2GlobalName(clasz), field.getName());
        if (isArray) {
            if ("[B".equals(typeName) || "[Ljava.lang.Byte;".equals(typeName)) typeHandler = TYPE_HANDLER_ARRAY_BYTE; else if ("[C".equals(typeName) || "[Ljava.lang.Character;".equals(typeName)) typeHandler = TYPE_HANDLER_ARRAY_CHAR; else if ("[I".equals(typeName) || "[Ljava.lang.Integer;".equals(typeName)) typeHandler = TYPE_HANDLER_ARRAY_INTEGER; else if ("[J".equals(typeName) || "[Ljava.lang.Long;".equals(typeName)) typeHandler = TYPE_HANDLER_ARRAY_LONG; else if ("[S".equals(typeName) || "[Ljava.lang.Short;".equals(typeName)) typeHandler = TYPE_HANDLER_ARRAY_SHORT; else if ("[F".equals(typeName) || "[Ljava.lang.Float;".equals(typeName)) typeHandler = TYPE_HANDLER_ARRAY_FLOAT; else if ("[D".equals(typeName) || "[Ljava.lang.Double;".equals(typeName)) typeHandler = TYPE_HANDLER_ARRAY_DOUBLE; else if ("[Z".equals(typeName) || "[Ljava.lang.Boolean;".equals(typeName)) typeHandler = TYPE_HANDLER_ARRAY_BOOLEAN; else if ("[Ljava.lang.String;".equals(typeName)) typeHandler = TYPE_HANDLER_ARRAY_STRING; else typeHandler = new TypeHandlerGenericArray(new TypeHandlerGeneric());
        } else if (isCollection) {
            String containedType = field.getGenericType().toString();
            containedType = containedType.substring(containedType.lastIndexOf('<') + 1);
            containedType = containedType.substring(0, containedType.length() - 1);
            try {
                generic = Class.forName(containedType);
            } catch (ClassNotFoundException ex) {
                throw new IllegalArgumentException("Class " + containedType + " not found", ex);
            }
            isPrimitive = isPrimitive(generic);
            typeHandler = new TypeHandlerGenericCollection(new TypeHandlerGeneric(generic));
        } else if (isPrimitive) {
            Type type = field.getType();
            if (type == Boolean.TYPE || "java.lang.Boolean".equals(typeName)) typeHandler = TYPE_HANDLER_BOOLEAN; else if (type == Character.TYPE || "java.lang.Character".equals(typeName)) typeHandler = TYPE_HANDLER_CHAR; else if (type == Byte.TYPE || "java.lang.Byte".equals(typeName)) typeHandler = TYPE_HANDLER_BYTE; else if (type == Short.TYPE || "java.lang.Short".equals(typeName)) typeHandler = TYPE_HANDLER_SHORT; else if (type == Integer.TYPE || "java.lang.Integer".equals(typeName)) typeHandler = TYPE_HANDLER_INTEGER; else if (type == Long.TYPE || "java.lang.Long".equals(typeName)) typeHandler = TYPE_HANDLER_LONG; else if (type == Float.TYPE || "java.lang.Float".equals(typeName)) typeHandler = TYPE_HANDLER_FLOAT; else if (type == Double.TYPE || "java.lang.Double".equals(typeName)) typeHandler = TYPE_HANDLER_DOUBLE; else if ("java.lang.String".equals(typeName)) typeHandler = TYPE_HANDLER_STRING; else throw new IllegalArgumentException("Primitive TypeHandler for type " + type.getClass().getName() + " not implemented!");
        } else if (field.getType().getName().startsWith("java.")) {
            isJava = true;
            if ("java.util.Date".equals(typeName)) typeHandler = TYPE_HANDLER_DATE; else if ("java.io.File".equals(typeName)) typeHandler = TYPE_HANDLER_FILE; else throw new IllegalArgumentException("TypeHandler for type " + typeName + " not implemented!");
        }
    }

    public boolean isPrimitive(Class<?> c) {
        if (c.isPrimitive()) return true;
        String typeName = c.getName();
        if (typeName.startsWith("[L")) typeName = typeName.substring(2);
        if (typeName.startsWith("[")) typeName = typeName.substring(1);
        if ("B".equals(typeName) || typeName.contains("java.lang.Byte")) return true; else if ("C".equals(typeName) || typeName.contains("java.lang.Character")) return true; else if ("I".equals(typeName) || typeName.contains("java.lang.Integer")) return true; else if ("J".equals(typeName) || typeName.contains("java.lang.Long")) return true; else if ("S".equals(typeName) || typeName.contains("java.lang.Short")) return true; else if ("F".equals(typeName) || typeName.contains("java.lang.Float")) return true; else if ("D".equals(typeName) || typeName.contains("java.lang.Double")) return true; else if ("Z".equals(typeName) || typeName.contains("java.lang.Boolean")) return true; else if (typeName.contains("java.lang.String")) return true; else return false;
    }

    /**
	 * Test if a given class is a java collection
	 * 
	 * @param clasz
	 * @return
	 */
    private boolean isCollection(Class<?> c) {
        try {
            c.asSubclass(Collection.class);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getGenericName() {
        if (generic == null) return null;
        if (generic.getName().startsWith("class ")) return generic.getName().substring("class ".length()); else return generic.getName();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(clasz);
        if (clasz.getSuperclass() != Object.class) {
            sb.append(" (").append(clasz.getSuperclass().getName()).append(")");
        }
        sb.append(" field=").append(field);
        sb.append(", lvl=").append(level);
        sb.append(", isPprimitive=").append(isPrimitive);
        sb.append(", isCollection=").append(isCollection);
        return sb.toString();
    }
}
