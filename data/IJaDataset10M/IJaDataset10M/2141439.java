package org.ocallahan.chronicle;

import java.util.HashMap;

/**
 * @author roc
 */
public abstract class Type {

    private Identifier identifier;

    Type(JSONObject object) throws JSONParserException {
        identifier = Identifier.parse(object);
    }

    Type() {
        identifier = new Identifier("void");
    }

    Type(Identifier ident) {
        this.identifier = ident;
    }

    /**
	 * Unwrap typedefs and annotations to get to the underlying type
	 */
    public Type getBareType() {
        return this;
    }

    public static interface Receiver {

        public void receive(Type t);
    }

    public static class Promise {

        Promise(String typeKey) {
            this(typeKey, null);
        }

        Promise(String typeKey, Type t) {
            this.typeKey = typeKey;
            this.t = t;
        }

        /**
    	 * Call this on any thread. When the type and all its referenced types
    	 * are fully loaded, the receiver will be called on the session thread
    	 * with no locks held.
    	 * @param r
    	 */
        public void realize(Session session, final Receiver r) {
            TypeManager tm = session.getTypeManager();
            synchronized (tm) {
                if (t != null) {
                    session.runOnThread(new Runnable() {

                        public void run() {
                            r.receive(t);
                        }
                    });
                    return;
                }
                tm.realizePromise(this, r);
            }
        }

        /**
    	 * Must be called with type manager lock held. This is called when the
    	 * type manager has resolved the type for this promise, while it commits
    	 * a set of resolved types.
    	 */
        void setType(Type t) {
            this.t = t;
        }

        public String getTypeKey() {
            return typeKey;
        }

        private Type t;

        private String typeKey;
    }

    void finish(JSONObject object, TypeManager manager) throws JSONParserException {
    }

    public static final int UNKNOWN_SIZE = 1;

    public int getSize() {
        return UNKNOWN_SIZE;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public abstract String getTypeKind();

    public String getName() {
        if (identifier.getName() != null) return identifier.toString();
        return getIntrinsicName();
    }

    public String getIntrinsicName() {
        return getTypeKind();
    }

    public static class Void extends Type {

        @Override
        public String getTypeKind() {
            return "void";
        }
    }

    public static final Void VOID = new Void();

    public static class Int extends Type {

        Int(JSONObject object) throws JSONParserException {
            super(object);
            byteSize = object.getIntRequired("byteSize");
            isSigned = object.getBooleanOptional("signed", false);
        }

        public int getSize() {
            return byteSize;
        }

        public boolean isSigned() {
            return isSigned;
        }

        @Override
        public String getTypeKind() {
            return "int";
        }

        private int byteSize;

        private boolean isSigned;
    }

    public static class Float extends Type {

        Float(JSONObject object) throws JSONParserException {
            super(object);
            byteSize = object.getIntRequired("byteSize");
        }

        public int getSize() {
            return byteSize;
        }

        @Override
        public String getTypeKind() {
            return "float";
        }

        private int byteSize;
    }

    public static class Pointer extends Type {

        Pointer(JSONObject object, TypeManager manager) throws JSONParserException {
            super(object);
            this.session = manager.getSession();
            manager.loadType(object.getString("innerTypeKey"));
            isReference = object.getBooleanOptional("isReference", false);
        }

        @Override
        void finish(JSONObject object, TypeManager manager) throws JSONParserException {
            innerType = manager.resolveType(object.getString("innerTypeKey"));
            super.finish(object, manager);
        }

        public Pointer(Session session, Type inner, boolean isReference) {
            super(Identifier.EMPTY);
            this.session = session;
            this.innerType = inner;
            this.isReference = isReference;
        }

        public int getSize() {
            return session.getArchitecture().getPointerSize();
        }

        public Type getInnerType() {
            return innerType;
        }

        public boolean isReference() {
            return isReference;
        }

        public Architecture getArchitecture() {
            return session.getArchitecture();
        }

        @Override
        public String getTypeKind() {
            return "pointer";
        }

        @Override
        public String getIntrinsicName() {
            return innerType.getName() + "*";
        }

        private Session session;

        private Type innerType;

        private boolean isReference;
    }

    public static class Typedef extends Type {

        Typedef(JSONObject object, TypeManager manager) throws JSONParserException {
            super(object);
            manager.loadType(object.getString("innerTypeKey"));
        }

        @Override
        void finish(JSONObject object, TypeManager manager) throws JSONParserException {
            innerType = manager.resolveType(object.getString("innerTypeKey"));
            super.finish(object, manager);
        }

        public int getSize() {
            return innerType.getSize();
        }

        public Type getInnerType() {
            return innerType;
        }

        @Override
        public Type getBareType() {
            return innerType.getBareType();
        }

        @Override
        public String getTypeKind() {
            return "typedef";
        }

        @Override
        public String getIntrinsicName() {
            return innerType.getName();
        }

        private Type innerType;
    }

    public static class Annotation extends Type {

        public static enum Kind {

            CONST, VOLATILE, RESTRICT
        }

        private static HashMap<String, Kind> stringToKind = new HashMap<String, Kind>();

        private static HashMap<Kind, String> kindToString = new HashMap<Kind, String>();

        private static void addKind(String s, Kind k) {
            stringToKind.put(s, k);
            kindToString.put(k, s);
        }

        static {
            addKind("const", Kind.CONST);
            addKind("volatile", Kind.VOLATILE);
            addKind("restrict", Kind.RESTRICT);
        }

        Annotation(JSONObject object, TypeManager manager) throws JSONParserException {
            super(object);
            manager.loadType(object.getString("innerTypeKey"));
            String annotation = object.getStringRequired("annotation");
            kind = stringToKind.get(annotation);
            if (kind == null) throw new JSONParserException("Unknown annotation kind: " + annotation);
        }

        @Override
        void finish(JSONObject object, TypeManager manager) throws JSONParserException {
            innerType = manager.resolveType(object.getString("innerTypeKey"));
            super.finish(object, manager);
        }

        public int getSize() {
            return innerType.getSize();
        }

        @Override
        public Type getBareType() {
            return innerType.getBareType();
        }

        public Type getInnerType() {
            return innerType;
        }

        public Kind getKind() {
            return kind;
        }

        @Override
        public String getTypeKind() {
            return "annotation";
        }

        @Override
        public String getIntrinsicName() {
            return innerType.getName() + " " + kindToString.get(kind);
        }

        private Kind kind;

        private Type innerType;
    }

    public static class EnumValue {

        EnumValue(JSONObject object) throws JSONParserException {
            name = object.getStringRequired("name");
            Object val = object.getValue("value");
            if (!(val instanceof Number)) throw new JSONParserException("value is not a number: " + val);
            Number n = (Number) val;
            value = n.longValue();
        }

        public String getName() {
            return name;
        }

        public long getValue() {
            return value;
        }

        private String name;

        private long value;
    }

    public static class Enum extends Type {

        Enum(JSONObject object) throws JSONParserException {
            super(object);
            if (object.getBooleanOptional("partial", false)) {
                byteSize = UNKNOWN_SIZE;
                values = null;
                return;
            }
            byteSize = object.getIntRequired("byteSize");
            Object[] vals = object.getArrayRequired("values");
            values = new EnumValue[vals.length];
            for (int i = 0; i < vals.length; ++i) {
                Object o = vals[i];
                if (!(o instanceof JSONObject)) throw new JSONParserException("value is not an object: " + o);
                JSONObject jso = (JSONObject) o;
                values[i] = new EnumValue(jso);
            }
        }

        public int getSize() {
            return byteSize;
        }

        public EnumValue[] getValues() {
            return values;
        }

        public boolean isPartial() {
            return values == null;
        }

        @Override
        public String getTypeKind() {
            return "enum";
        }

        private int byteSize;

        private EnumValue[] values;
    }

    public static class Array extends Type {

        Array(JSONObject object, TypeManager manager) throws JSONParserException {
            super(object);
            manager.loadType(object.getStringRequired("innerTypeKey"));
            length = object.getInt("length");
        }

        @Override
        void finish(JSONObject object, TypeManager manager) throws JSONParserException {
            innerType = manager.resolveType(object.getStringRequired("innerTypeKey"));
            super.finish(object, manager);
        }

        public int getSize() {
            if (length == null) return super.getSize();
            return innerType.getSize() * length;
        }

        public Integer getLength() {
            return length;
        }

        public Type getInnerType() {
            return innerType;
        }

        @Override
        public String getTypeKind() {
            return "array";
        }

        @Override
        public String getIntrinsicName() {
            return innerType.getName() + "[]";
        }

        private Integer length;

        private Type innerType;
    }

    public static class Field {

        Field(JSONObject object, TypeManager manager) throws JSONParserException {
            name = object.getString("name");
            isSynthetic = object.getBooleanOptional("synthetic", false);
            isSubobject = object.getBooleanOptional("isSubobject", false);
            byteOffset = object.getIntRequired("byteOffset");
            manager.loadType(object.getStringRequired("typeKey"));
        }

        void finish(JSONObject object, TypeManager manager) throws JSONParserException {
            type = manager.resolveType(object.getStringRequired("typeKey"));
        }

        public int getByteOffset() {
            return byteOffset;
        }

        public boolean isSubobject() {
            return isSubobject;
        }

        public boolean isSynthetic() {
            return isSynthetic;
        }

        public String getName() {
            return name;
        }

        public Type getType() {
            return type;
        }

        public String toString() {
            if (name != null) return name;
            if (isSubobject) return type.getName();
            return "<anon-" + type.getName() + ">";
        }

        private String name;

        private boolean isSynthetic;

        private boolean isSubobject;

        private int byteOffset;

        private Type type;
    }

    public static class BitField extends Field {

        BitField(JSONObject object, TypeManager manager) throws JSONParserException {
            super(object, manager);
            byteSize = object.getIntRequired("byteSize");
            bitOffset = object.getIntRequired("bitOffset");
            bitSize = object.getIntRequired("bitSize");
        }

        public int getBitOffset() {
            return bitOffset;
        }

        public int getBitSize() {
            return bitSize;
        }

        public int getByteSize() {
            return byteSize;
        }

        private int byteSize;

        private int bitOffset;

        private int bitSize;
    }

    public static class Struct extends Type {

        public static enum Kind {

            STRUCT, CLASS, UNION
        }

        private static HashMap<String, Kind> stringToKind = new HashMap<String, Kind>();

        private static HashMap<Kind, String> kindToString = new HashMap<Kind, String>();

        private static void addKind(String s, Kind k) {
            stringToKind.put(s, k);
            kindToString.put(k, s);
        }

        static {
            addKind("struct", Kind.STRUCT);
            addKind("class", Kind.CLASS);
            addKind("union", Kind.UNION);
        }

        public static String kindToString(Kind kind) {
            return kindToString.get(kind);
        }

        Struct(JSONObject object, TypeManager manager) throws JSONParserException {
            super(object);
            String kindStr = object.getStringRequired("structKind");
            kind = stringToKind.get(kindStr);
            if (kind == null) throw new JSONParserException("Unknown annotation kind: " + kindStr);
            if (object.getBooleanOptional("partial", false)) {
                byteSize = UNKNOWN_SIZE;
                fields = null;
                return;
            }
            byteSize = object.getIntRequired("byteSize");
            Object[] fieldObjs = object.getArrayRequired("fields");
            fields = new Field[fieldObjs.length];
            for (int i = 0; i < fieldObjs.length; ++i) {
                Object o = fieldObjs[i];
                if (!(o instanceof JSONObject)) throw new JSONParserException("Expected field object, got " + o);
                JSONObject jso = (JSONObject) o;
                Field f;
                if (jso.hasValue("bitSize")) {
                    f = new BitField(jso, manager);
                } else {
                    f = new Field(jso, manager);
                }
                fields[i] = f;
            }
        }

        @Override
        void finish(JSONObject object, TypeManager manager) throws JSONParserException {
            Object[] fieldObjs = object.getArray("fields");
            if (fieldObjs != null && fields != null) {
                for (int i = 0; i < fieldObjs.length; ++i) {
                    fields[i].finish((JSONObject) fieldObjs[i], manager);
                }
            }
            super.finish(object, manager);
        }

        public int getSize() {
            return byteSize;
        }

        public Kind getKind() {
            return kind;
        }

        public Field[] getFields() {
            return fields;
        }

        public boolean isPartial() {
            return fields == null;
        }

        @Override
        public String getTypeKind() {
            return "struct";
        }

        @Override
        public String getIntrinsicName() {
            StringBuilder builder = new StringBuilder();
            builder.append(kindToString.get(kind));
            builder.append(" { ");
            for (Field f : fields) {
                builder.append(f.getType().getName());
                builder.append(" ");
                builder.append(f.getName());
                builder.append("; ");
            }
            builder.append("}");
            return builder.toString();
        }

        private int byteSize;

        private Kind kind;

        private Field[] fields;
    }

    public static class Parameter {

        Parameter(JSONObject object, TypeManager manager) throws JSONParserException {
            identifier = Identifier.parse(object);
            manager.loadType(object.getString("typeKey"));
        }

        void finish(JSONObject object, TypeManager manager) throws JSONParserException {
            String typeKey = object.getString("typeKey");
            if (typeKey != null) {
                type = manager.resolveType(typeKey);
            }
        }

        public Identifier getIdentifier() {
            return identifier;
        }

        public Type getType() {
            return type;
        }

        private Identifier identifier;

        private Type type;
    }

    public static class Function extends Type {

        Function(JSONObject object, TypeManager manager) throws JSONParserException {
            super(object);
            manager.loadType(object.getString("resultTypeKey"));
            Object[] paramObjs = object.getArray("parameters");
            if (paramObjs != null) {
                parameters = new Parameter[paramObjs.length];
                for (int i = 0; i < paramObjs.length; ++i) {
                    Object o = paramObjs[i];
                    if (!(o instanceof JSONObject)) throw new JSONParserException("Expected parameter object, got " + o);
                    JSONObject jso = (JSONObject) o;
                    parameters[i] = new Parameter(jso, manager);
                }
            }
        }

        @Override
        void finish(JSONObject object, TypeManager manager) throws JSONParserException {
            resultType = manager.resolveType(object.getString("resultTypeKey"));
            Object[] paramObjs = object.getArray("parameters");
            if (paramObjs != null) {
                for (int i = 0; i < paramObjs.length; ++i) {
                    parameters[i].finish((JSONObject) paramObjs[i], manager);
                }
            }
            super.finish(object, manager);
        }

        public Parameter[] getParameters() {
            return parameters;
        }

        public Type getResultType() {
            return resultType;
        }

        @Override
        public String getTypeKind() {
            return "function";
        }

        @Override
        public String getIntrinsicName() {
            StringBuilder builder = new StringBuilder();
            builder.append("(");
            builder.append(resultType.getName());
            builder.append(" *)(");
            boolean isFirst = true;
            for (Parameter p : parameters) {
                if (!isFirst) {
                    builder.append(", ");
                }
                builder.append(p.getType().getName());
                if (p.getIdentifier().getName() != null) {
                    builder.append(" ");
                    builder.append(p.getIdentifier().toString());
                }
            }
            builder.append(")");
            return builder.toString();
        }

        private Parameter[] parameters;

        private Type resultType;
    }

    static Type createFor(JSONObject object, TypeManager manager) throws JSONParserException {
        String kind = object.getStringRequired("kind").intern();
        if (kind == "int") return new Int(object);
        if (kind == "float") return new Float(object);
        if (kind == "pointer") return new Pointer(object, manager);
        if (kind == "typedef") return new Typedef(object, manager);
        if (kind == "annotation") return new Annotation(object, manager);
        if (kind == "array") return new Array(object, manager);
        if (kind == "enum") return new Enum(object);
        if (kind == "struct") return new Struct(object, manager);
        if (kind == "function") return new Function(object, manager);
        throw new JSONParserException("Unknown kind: " + kind);
    }
}
