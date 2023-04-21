package com.sleepycat.persist.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sleepycat.compat.DbCompat;
import com.sleepycat.persist.model.ClassMetadata;
import com.sleepycat.persist.model.EntityModel;
import com.sleepycat.persist.model.FieldMetadata;
import com.sleepycat.persist.raw.RawField;
import com.sleepycat.persist.raw.RawObject;
import com.sleepycat.je.utilint.IdentityHashMap;

/**
 * Format for a composite key class.
 *
 * This class is similar to ComplexFormat in that a composite key class and
 * other complex classes have fields, and the Accessor interface is used to
 * access those fields.  Composite key classes are different in the following
 * ways:
 *
 * - The superclass must be Object.  No inheritance is allowed.
 *
 * - All instance fields must be annotated with @KeyField, which determines
 *   their order in the data bytes.
 *
 * - Although fields may be reference types (primitive wrappers or other simple
 *   reference types), they are stored as if they were primitives.  No object
 *   format ID is stored, and the class of the object must be the declared
 *   classs of the field; i.e., no polymorphism is allowed for key fields.
 *   In other words, a composite key is stored as an ordinary tuple as defined
 *   in the com.sleepycat.bind.tuple package.  This keeps the key small and
 *   gives it a well defined sort order.
 *
 * - If the key class implements Comparable, it is called by the Database
 *   btree comparator.  It must therefore be available during JE recovery,
 *   before the store and catalog have been opened.  To support this, this
 *   format can be constructed during recovery.  A SimpleCatalog singleton
 *   instance is used to provide a catalog of simple types that is used by
 *   the composite key format.
 *
 * - When interacting with the Accessor, the composite key format treats the
 *   Accessor's non-key fields as its key fields.  The Accessor's key fields
 *   are secondary keys, while the composite format's key fields are the
 *   component parts of a single key.
 *
 * @author Mark Hayes
 */
public class CompositeKeyFormat extends Format {

    private static final long serialVersionUID = 306843428409314630L;

    private ClassMetadata metadata;

    private List<FieldInfo> fields;

    private transient Accessor objAccessor;

    private transient Accessor rawAccessor;

    private transient volatile Map<String, RawField> rawFields;

    private transient volatile FieldInfo[] rawInputFields;

    static String[] getFieldNameArray(List<FieldMetadata> list) {
        int index = 0;
        String[] a = new String[list.size()];
        for (FieldMetadata f : list) {
            a[index] = f.getName();
            index += 1;
        }
        return a;
    }

    /**
     * Creates a new composite key format.
     */
    CompositeKeyFormat(Class cls, ClassMetadata metadata, List<FieldMetadata> fieldMeta) {
        this(cls, metadata, getFieldNameArray(fieldMeta));
    }

    /**
     * Reconsistitues a composite key format after a PersistComparator is
     * deserialized.
     */
    CompositeKeyFormat(Class cls, String[] fieldNames) {
        this(cls, null, fieldNames);
    }

    private CompositeKeyFormat(Class cls, ClassMetadata metadata, String[] fieldNames) {
        super(cls);
        this.metadata = metadata;
        Class superCls = cls.getSuperclass();
        if (superCls != Object.class) {
            throw new IllegalArgumentException("Composite key class must be derived from Object: " + cls.getName());
        }
        List<FieldInfo> instanceFields = FieldInfo.getInstanceFields(cls, metadata);
        fields = new ArrayList<FieldInfo>(instanceFields.size());
        for (String fieldName : fieldNames) {
            FieldInfo field = null;
            for (FieldInfo tryField : instanceFields) {
                if (fieldName.equals(tryField.getName())) {
                    field = tryField;
                    break;
                }
            }
            if (field == null) {
                throw new IllegalArgumentException("Composite key field is not an instance field: " + getClassName() + '.' + fieldName);
            }
            fields.add(field);
            instanceFields.remove(field);
            Class fieldCls = field.getFieldClass();
            if (!SimpleCatalog.isSimpleType(fieldCls) && !fieldCls.isEnum()) {
                throw new IllegalArgumentException("Composite key field is not a simple type or enum: " + getClassName() + '.' + fieldName);
            }
        }
        if (instanceFields.size() > 0) {
            throw new IllegalArgumentException("All composite key instance fields must be key fields: " + getClassName() + '.' + instanceFields.get(0).getName());
        }
    }

    List<FieldInfo> getFieldInfo() {
        return fields;
    }

    @Override
    void migrateFromBeta(Map<String, Format> formatMap) {
        super.migrateFromBeta(formatMap);
        for (FieldInfo field : fields) {
            field.migrateFromBeta(formatMap);
        }
    }

    @Override
    boolean isModelClass() {
        return true;
    }

    @Override
    public ClassMetadata getClassMetadata() {
        if (metadata == null) {
            throw DbCompat.unexpectedState(getClassName());
        }
        return metadata;
    }

    @Override
    public Map<String, RawField> getFields() {
        if (rawFields == null) {
            Map<String, RawField> map = new HashMap<String, RawField>();
            for (RawField field : fields) {
                map.put(field.getName(), field);
            }
            rawFields = map;
        }
        return rawFields;
    }

    @Override
    void collectRelatedFormats(Catalog catalog, Map<String, Format> newFormats) {
        for (FieldInfo field : fields) {
            field.collectRelatedFormats(catalog, newFormats);
        }
    }

    @Override
    void initialize(Catalog catalog, EntityModel model, int initVersion) {
        for (FieldInfo field : fields) {
            field.initialize(catalog, model, initVersion);
        }
        Class type = getType();
        if (type != null) {
            if (EnhancedAccessor.isEnhanced(type)) {
                objAccessor = new EnhancedAccessor(catalog, type, fields);
            } else {
                objAccessor = new ReflectionAccessor(catalog, type, fields);
            }
        }
        rawAccessor = new RawAccessor(this, fields);
    }

    @Override
    Object newArray(int len) {
        return objAccessor.newArray(len);
    }

    @Override
    public Object newInstance(EntityInput input, boolean rawAccess) {
        Accessor accessor = rawAccess ? rawAccessor : objAccessor;
        return accessor.newInstance();
    }

    @Override
    public Object readObject(Object o, EntityInput input, boolean rawAccess) {
        Accessor accessor = rawAccess ? rawAccessor : objAccessor;
        accessor.readCompositeKeyFields(o, input);
        return o;
    }

    @Override
    void writeObject(Object o, EntityOutput output, boolean rawAccess) {
        Accessor accessor = rawAccess ? rawAccessor : objAccessor;
        accessor.writeCompositeKeyFields(o, output);
    }

    @Override
    Object convertRawObject(Catalog catalog, boolean rawAccess, RawObject rawObject, IdentityHashMap converted) {
        FieldInfo[] myFields = rawInputFields;
        if (myFields == null) {
            myFields = new FieldInfo[fields.size()];
            fields.toArray(myFields);
            rawInputFields = myFields;
        }
        if (rawObject.getSuper() != null) {
            throw new IllegalArgumentException("RawObject has too many superclasses: " + rawObject.getType().getClassName());
        }
        RawObject[] objects = new RawObject[myFields.length];
        Arrays.fill(objects, rawObject);
        EntityInput in = new RawComplexInput(catalog, rawAccess, converted, myFields, objects);
        Object o = newInstance(in, rawAccess);
        converted.put(rawObject, o);
        return readObject(o, in, rawAccess);
    }

    @Override
    void skipContents(RecordInput input) {
        int maxNum = fields.size();
        for (int i = 0; i < maxNum; i += 1) {
            fields.get(i).getType().skipContents(input);
        }
    }

    @Override
    void copySecKey(RecordInput input, RecordOutput output) {
        int maxNum = fields.size();
        for (int i = 0; i < maxNum; i += 1) {
            fields.get(i).getType().copySecKey(input, output);
        }
    }

    @Override
    Format getSequenceKeyFormat() {
        if (fields.size() != 1) {
            throw new IllegalArgumentException("A composite key class used with a sequence may contain " + "only a single integer key field: " + getClassName());
        }
        return fields.get(0).getType().getSequenceKeyFormat();
    }

    @Override
    boolean evolve(Format newFormatParam, Evolver evolver) {
        if (!(newFormatParam instanceof CompositeKeyFormat)) {
            evolver.addEvolveError(this, newFormatParam, null, "A composite key class may not be changed to a different " + "type");
            return false;
        }
        CompositeKeyFormat newFormat = (CompositeKeyFormat) newFormatParam;
        if (fields.size() != newFormat.fields.size()) {
            evolver.addEvolveError(this, newFormat, "Composite key class fields were added or removed ", "Old fields: " + fields + " new fields: " + newFormat.fields);
            return false;
        }
        boolean newVersion = false;
        for (int i = 0; i < fields.size(); i += 1) {
            int result = evolver.evolveRequiredKeyField(this, newFormat, fields.get(i), newFormat.fields.get(i));
            if (result == Evolver.EVOLVE_FAILURE) {
                return false;
            }
            if (result == Evolver.EVOLVE_NEEDED) {
                newVersion = true;
            }
        }
        if (newVersion) {
            evolver.useEvolvedFormat(this, newFormat, newFormat);
        } else {
            evolver.useOldFormat(this, newFormat);
        }
        return true;
    }
}
