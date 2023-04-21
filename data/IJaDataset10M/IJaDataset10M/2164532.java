package com.netx.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.regex.Matcher;
import com.netx.generics.basic.IntegrityException;
import com.netx.generics.time.Date;
import com.netx.generics.time.Moment;
import com.netx.generics.time.Time;
import com.netx.generics.translation.Results;
import com.netx.generics.translation.TranslationStep;
import com.netx.generics.util.Strings;
import com.netx.generics.util.ByteValue;
import com.netx.generics.util.ByteValue.MEASURE;
import com.netx.data.Field.TYPE;
import com.netx.data.Field.CONSTRAINT;

class DatabaseAnalyzer extends TranslationStep {

    private final Pattern _pIdentifier;

    public DatabaseAnalyzer(DatabaseParser parser) {
        super(parser);
        _pIdentifier = Pattern.compile("[a-zA-Z0-9_]*");
    }

    public Object performWork(Results r) {
        final SymbolDatabase sDatabase = (SymbolDatabase) r.getContents();
        final Repository repository = _createRepository(r, sDatabase.pRepository);
        Object[] lTemp = _createDataSinks(r, sDatabase.pDataSinks);
        @SuppressWarnings("unchecked") final List<DataSink> backupSinks = (List) lTemp[0];
        @SuppressWarnings("unchecked") final Map<String, DataSink> replicationSinks = (Map) lTemp[1];
        if (repository == null) {
            r.addError("database repository could not be initialized. Exiting analysis operation...");
            return null;
        }
        final Map<String, MetaData> metadata = new HashMap<String, MetaData>();
        final Map<String, EntityMetaData> entities = new HashMap<String, EntityMetaData>();
        final Database database = new Database(repository, metadata, entities, backupSinks, replicationSinks);
        final Map<String, Object> loadedEntities = new HashMap<String, Object>();
        final Map<String, Object> loadedClasses = new HashMap<String, Object>();
        Iterator<SymbolEntity> itEntities = sDatabase.pEntities.iterator();
        while (itEntities.hasNext()) {
            _loadEntity(r, itEntities.next(), loadedEntities, loadedClasses, replicationSinks, database.getDriver());
        }
        itEntities = sDatabase.pEntities.iterator();
        while (itEntities.hasNext()) {
            SymbolEntity sEntity = itEntities.next();
            EntityMetaData entity = new EntityMetaData(sEntity.aName, sEntity.aClassName, sEntity.aReplication, sEntity.aConstraints, sEntity.aAutoGenerateKeys, database);
            entities.put(entity.getName(), entity);
            metadata.put(entity.getName(), entity);
        }
        itEntities = sDatabase.pEntities.iterator();
        while (itEntities.hasNext()) {
            SymbolEntity sEntity = itEntities.next();
            if (sEntity.pRelation != null) {
                if (entities.containsKey(sEntity.pRelation.aName)) {
                    r.addError("in relation '" + sEntity.pName + "." + sEntity.pRelation.aName + "': name conficts with an entity name");
                }
                EntityMetaData holder = entities.get(sEntity.aName);
                EntityMetaData related = entities.get(sEntity.pRelation.pEntityName);
                if (related == null) {
                    r.addError("in relation '" + sEntity.pName + "." + sEntity.pRelation.pName + "': entity '" + sEntity.pRelation.pEntityName + "' could not be found");
                } else {
                    RelationMetaData relation = new RelationMetaData(sEntity.pRelation.aName, holder, related, sEntity.pRelation.aConstraints, database);
                    holder.setRelation(relation);
                    metadata.put(relation.getName(), relation);
                }
            }
        }
        itEntities = sDatabase.pEntities.iterator();
        while (itEntities.hasNext()) {
            SymbolEntity sEntity = itEntities.next();
            EntityMetaData entity = entities.get(sEntity.aName);
            Map<String, Field> fields = _createFields(r, entities, sEntity.aFields, sEntity.pName, entity);
            Iterator<String> itPrimaryKey = sEntity.aPrimaryKeyFields.iterator();
            List<Field> primaryKey = new ArrayList<Field>();
            while (itPrimaryKey.hasNext()) {
                primaryKey.add(fields.get(itPrimaryKey.next()));
            }
            entity.setFields(fields, primaryKey);
        }
        itEntities = sDatabase.pEntities.iterator();
        while (itEntities.hasNext()) {
            SymbolEntity sEntity = itEntities.next();
            if (sEntity.pRelation != null) {
                EntityMetaData holder = entities.get(sEntity.aName);
                RelationMetaData relation = holder.getRelationMetaData();
                if (relation != null) {
                    Map<String, Field> fields = _createFields(r, entities, sEntity.pRelation.aFields, sEntity.pName + "." + sEntity.pRelation.pName, relation);
                    EntityMetaData related = relation.getRelatedEntity();
                    if (holder.getPrimaryKey().length > 1) {
                        r.addError("in relation '" + sEntity.pName + "." + sEntity.pRelation.pName + "': cannot establish relation with entity \"" + holder.getName() + "\" because it has a composed primary key");
                    }
                    if (related.getPrimaryKey().length > 1) {
                        r.addError("in relation '" + sEntity.pName + "." + sEntity.pRelation.pName + "': cannot establish relation with entity \"" + related.getName() + "\" because it has a composed primary key");
                    }
                    List<Field> primaryKey = new ArrayList<Field>();
                    primaryKey.add(holder.getPrimaryKey()[0]);
                    primaryKey.add(related.getPrimaryKey()[0]);
                    fields.put(holder.getPrimaryKey()[0].getName(), holder.getPrimaryKey()[0]);
                    fields.put(related.getPrimaryKey()[0].getName(), related.getPrimaryKey()[0]);
                    relation.setFields(fields, primaryKey);
                }
            }
        }
        itEntities = sDatabase.pEntities.iterator();
        while (itEntities.hasNext()) {
            SymbolEntity sEntity = itEntities.next();
            _checkForeignKeys(r, sEntity.aFields, sEntity.pName, entities);
            if (sEntity.pRelation != null) {
                _checkForeignKeys(r, sEntity.pRelation.aFields, sEntity.pName + "." + sEntity.pRelation.pName, entities);
            }
        }
        return database;
    }

    private Repository _createRepository(Results r, SymbolRepository sRepository) {
        DatabaseDriver driver = null;
        try {
            Class<?> c = Class.forName(sRepository.pDriver);
            Constructor co = c.getConstructor(new Class<?>[0]);
            driver = (DatabaseDriver) co.newInstance(new Object[0]);
        } catch (Exception ex) {
            r.addError("could not load database driver '" + sRepository.pDriver + "': " + ex);
            return null;
        }
        if (sRepository.pMaxActive != null) {
            sRepository.aMaxActive = _getIntParameter(r, sRepository.pMaxActive, "max-active");
        } else {
            sRepository.aMaxActive = 100;
        }
        if (sRepository.pMaxIdle != null) {
            sRepository.aMaxIdle = _getIntParameter(r, sRepository.pMaxIdle, "max-idle");
        } else {
            sRepository.aMaxIdle = 100;
        }
        if (sRepository.pMaxWait != null) {
            sRepository.aMaxWait = _getIntParameter(r, sRepository.pMaxWait, "max-wait");
        } else {
            sRepository.aMaxWait = 10;
        }
        if (sRepository.pRemoveAbandonedTimeout != null) {
            sRepository.aRemoveAbandonedTimeout = _getIntParameter(r, sRepository.pRemoveAbandonedTimeout, "remove-abandoned-timeout");
        } else {
            sRepository.aRemoveAbandonedTimeout = 60;
        }
        return new Repository(driver, sRepository);
    }

    private int _getIntParameter(Results r, String value, String paramName) {
        try {
            return new Integer(value).intValue();
        } catch (NumberFormatException nfe) {
            r.addError("in repository parameter '" + paramName + "': illegal value '" + value + "'");
            return 0;
        }
    }

    private Object[] _createDataSinks(Results r, List<SymbolDataSink> lDataSinks) {
        List<DataSink> backupSinks = new ArrayList<DataSink>();
        Map<String, DataSink> replicationSinks = new HashMap<String, DataSink>();
        Iterator<SymbolDataSink> it = lDataSinks.iterator();
        while (it.hasNext()) {
            SymbolDataSink sDataSink = it.next();
            DataSink dataSink = _createDataSink(r, sDataSink);
            if (sDataSink.pType.equals("backup")) {
                backupSinks.add(dataSink);
            } else if (sDataSink.pType.equals("replication")) {
                sDataSink.aName = _checkName(r, sDataSink.pName, "replication sink \"" + sDataSink.pName + "\"");
                if (replicationSinks.containsKey(sDataSink.pName)) {
                    r.addError("in replication sink '" + sDataSink.pName + "': name already exists");
                }
                replicationSinks.put(sDataSink.pName, dataSink);
            } else {
                r.addError("invalid data-sink type '" + sDataSink.pType + "'");
            }
        }
        return new Object[] { backupSinks, replicationSinks };
    }

    private DataSink _createDataSink(Results r, SymbolDataSink sDataSink) {
        Class<?> c = DataSinks.getRegisteredClass(sDataSink.pRegisteredClassId);
        if (c == null) {
            r.addError("data-sink class '" + sDataSink.pRegisteredClassId + "' is not registered");
            return null;
        } else {
            Object[] args = new Object[sDataSink.pParameterNames.size()];
            Class<?>[] cArgs = new Class[sDataSink.pParameterNames.size()];
            int i = 0;
            for (Iterator it = sDataSink.pParameterValues.iterator(); it.hasNext(); i++) {
                args[i] = it.next().toString();
                cArgs[i] = String.class;
            }
            try {
                Constructor co = c.getConstructor(cArgs);
                return (DataSink) co.newInstance(args);
            } catch (Exception ex) {
                r.addError("could not create data sink with class-id '" + sDataSink.pRegisteredClassId + "': " + ex.getMessage());
                return null;
            }
        }
    }

    private Map<String, Field> _createFields(Results r, Map<String, ? extends MetaData> metadata, Map<String, SymbolField> aFields, String where, MetaData holder) {
        Map<String, Field> fields = new HashMap<String, Field>();
        Iterator<SymbolField> it = aFields.values().iterator();
        while (it.hasNext()) {
            SymbolField sField = it.next();
            MetaData foreignEntity = null;
            if (sField.aType == TYPE.FOREIGN_KEY) {
                foreignEntity = metadata.get(sField.pTypeArgument);
                if (foreignEntity == null) {
                    r.addError("in field '" + where + "." + sField.pName + "': cannot find foreign entity '" + sField.pTypeArgument + "'");
                }
            }
            Field field = new Field(sField.aName, sField.aType, sField.aIsRequired, sField.aDefaultValue, sField.aOnDelete, sField.aOnUpdate, sField.aValidation, sField.aTextLength, holder, foreignEntity);
            fields.put(field.getName(), field);
        }
        return fields;
    }

    private void _loadEntity(Results r, SymbolEntity sEntity, Map<String, Object> loadedEntities, Map<String, Object> loadedClasses, Map<String, DataSink> replicationSinks, DatabaseDriver driver) {
        sEntity.aName = _checkName(r, sEntity.pName, "entity '" + sEntity.pName + "'");
        if (loadedEntities.get(sEntity.pName) != null) {
            r.addError("in entity '" + sEntity.pName + "': name already exists");
        } else {
            loadedEntities.put(sEntity.pName, new Object());
        }
        if (loadedClasses.get(sEntity.pClassName) != null) {
            r.addError("in entity '" + sEntity.pName + "': class '" + sEntity.pClassName + "' is already used in another entity");
        } else {
            loadedClasses.put(sEntity.pClassName, new Object());
            try {
                Class.forName(sEntity.pClassName);
                sEntity.aClassName = sEntity.pClassName;
            } catch (Exception e) {
                r.addError("in entity '" + sEntity.pName + "': could not load class '" + sEntity.pClassName + "': " + e);
            }
        }
        final Iterator<SymbolField> itPrimaryKey = sEntity.pPrimaryKeyFields.iterator();
        while (itPrimaryKey.hasNext()) {
            SymbolField sField = itPrimaryKey.next();
            _loadField(r, sField, driver, sEntity.pName);
            _addField(r, sEntity.aFields, sField, sEntity.pName);
            sEntity.aPrimaryKeyFields.add(sField.aName);
        }
        sEntity.aAutoGenerateKeys = false;
        Boolean b = _getBoolean(r, sEntity.pAutoGenerateKeys, sEntity.pName + ".auto-generate-keys");
        if (b != null) {
            sEntity.aAutoGenerateKeys = b.booleanValue();
            if (sEntity.aAutoGenerateKeys && sEntity.aPrimaryKeyFields.size() > 1) {
                r.addError("in entity '" + sEntity.pName + "': cannot auto-generate keys in composed primary key");
                sEntity.aAutoGenerateKeys = false;
            }
            SymbolField sPrimKey = sEntity.pPrimaryKeyFields.get(0);
            if (sPrimKey.aType != TYPE.LONG) {
                r.addError("in entity '" + sEntity.pName + "': cannot auto-generate keys for '" + sPrimKey.pType + "' field");
                sEntity.aAutoGenerateKeys = false;
            }
        }
        final Iterator<SymbolField> itFields = sEntity.pFields.iterator();
        while (itFields.hasNext()) {
            SymbolField sField = itFields.next();
            _loadField(r, sField, driver, sEntity.pName);
            _addField(r, sEntity.aFields, sField, sEntity.pName);
        }
        _loadUniqueConstraints(r, sEntity.pConstraints, sEntity.aConstraints, sEntity.aFields, "entity \"" + sEntity.pName + "\"");
        final Iterator<String> itReplication = sEntity.pReplication.iterator();
        while (itReplication.hasNext()) {
            String rSinkName = itReplication.next();
            DataSink sink = replicationSinks.get(rSinkName);
            if (sink == null) {
                r.addError("in entity '" + sEntity.pName + "': could not find replication sink named '" + rSinkName + "'");
            } else {
                sEntity.aReplication.add(sink);
            }
        }
        if (sEntity.pRelation != null) {
            sEntity.pRelation.aName = _checkName(r, sEntity.pRelation.pName, "relation '" + sEntity.pName + "." + sEntity.pRelation.pName + "'");
            Iterator<SymbolField> itRelationFields = sEntity.pRelation.pFields.iterator();
            while (itRelationFields.hasNext()) {
                SymbolField sField = itRelationFields.next();
                _loadField(r, sField, driver, sEntity.pRelation.pName);
                _addField(r, sEntity.pRelation.aFields, sField, sEntity.pName + "." + sEntity.pRelation.pName);
            }
            _loadUniqueConstraints(r, sEntity.pRelation.pConstraints, sEntity.pRelation.aConstraints, sEntity.pRelation.aFields, "relation \"" + sEntity.pName + "." + sEntity.pRelation.pName + "\"");
        }
    }

    private void _loadUniqueConstraints(Results r, List<String> pConstraints, List<String[]> aConstraints, Map<String, SymbolField> aFields, String where) {
        Iterator<String> itConstraints = pConstraints.iterator();
        while (itConstraints.hasNext()) {
            String value = itConstraints.next();
            String[] fieldNames = value.split("[,]");
            Map<String, String> mFieldNames = new HashMap<String, String>();
            for (int i = 0; i < fieldNames.length; i++) {
                if (!aFields.containsKey(fieldNames[i])) {
                    r.addError("in " + where + ": could not find constraint field '" + fieldNames[i] + "'");
                }
                if (mFieldNames.containsKey(fieldNames[i])) {
                    r.addError("in " + where + ": duplicated field '" + fieldNames[i] + "' in constraint");
                }
                mFieldNames.put(fieldNames[i], fieldNames[i]);
            }
            aConstraints.add(fieldNames);
        }
    }

    private void _loadField(Results r, SymbolField sField, DatabaseDriver driver, String where) {
        where = "field '" + where + "." + sField.pName + "'";
        sField.aName = _checkName(r, sField.pName, where);
        boolean illegalType = false;
        if (sField.pType.equals("boolean")) {
            sField.aType = TYPE.BOOLEAN;
        } else if (sField.pType.equals("short")) {
            sField.aType = TYPE.SHORT;
        } else if (sField.pType.equals("int")) {
            sField.aType = TYPE.INT;
        } else if (sField.pType.equals("long")) {
            sField.aType = TYPE.LONG;
        } else if (sField.pType.equals("float")) {
            sField.aType = TYPE.FLOAT;
        } else if (sField.pType.equals("double")) {
            sField.aType = TYPE.DOUBLE;
        } else if (sField.pType.equals("text")) {
            sField.aType = TYPE.TEXT;
        } else if (sField.pType.equals("binary")) {
            sField.aType = TYPE.BINARY;
        } else if (sField.pType.equals("date")) {
            sField.aType = TYPE.DATE;
        } else if (sField.pType.equals("time")) {
            sField.aType = TYPE.TIME;
        } else if (sField.pType.equals("moment")) {
            sField.aType = TYPE.MOMENT;
        } else if (sField.pType.equals("foreign-key")) {
            sField.aType = TYPE.FOREIGN_KEY;
        } else {
            illegalType = true;
            r.addError("in " + where + ": illegal type '" + sField.pType + "'");
            sField.aType = null;
        }
        if (sField.pOnDelete != null || sField.pOnUpdate != null) {
            if (sField.pOnDelete != null) {
                sField.aOnDelete = _checkConstraint(r, sField.pOnDelete, where, "on-delete", sField.pDefaultValue);
            }
            if (sField.pOnUpdate != null) {
                sField.aOnUpdate = _checkConstraint(r, sField.pOnUpdate, where, "on-update", sField.pDefaultValue);
            }
            if (!illegalType && sField.aType != TYPE.FOREIGN_KEY) {
                r.addError("in " + where + ": cannot use constraints in " + sField.pType + " field");
            }
        }
        if (!illegalType) {
            sField.aTextLength = 0;
            if (sField.pType.equals("text") || sField.pType.equals("binary")) {
                if (sField.pTypeArgument == null) {
                    r.addError("in " + where + ": argument expected for type '" + sField.pType + "'");
                } else if (sField.pTypeArgument.indexOf("K") != -1) {
                    double arg = _getSizeArgument(r, sField.pTypeArgument, where, "K", sField.aType);
                    sField.aTextLength = Math.round(ByteValue.convert(arg, MEASURE.KILOBYTES, MEASURE.BYTES));
                } else if (sField.pTypeArgument.indexOf("M") != -1) {
                    double arg = _getSizeArgument(r, sField.pTypeArgument, where, "M", sField.aType);
                    sField.aTextLength = Math.round(ByteValue.convert(arg, MEASURE.MEGABYTES, MEASURE.BYTES));
                } else if (sField.pTypeArgument.indexOf("G") != -1) {
                    double arg = _getSizeArgument(r, sField.pTypeArgument, where, "G", sField.aType);
                    sField.aTextLength = Math.round(ByteValue.convert(arg, MEASURE.GIGABYTES, MEASURE.BYTES));
                } else {
                    sField.aTextLength = (long) _getSizeArgument(r, sField.pTypeArgument, where, "", sField.aType);
                }
            } else if (sField.pType.equals("foreign-key")) {
                if (sField.pTypeArgument == null) {
                    r.addError("in " + where + ": argument expected for type '" + sField.pType + "'");
                }
            } else {
                if (sField.pTypeArgument != null) {
                    r.addError("in " + where + ": no arguments expected for type '" + sField.pType + "'");
                }
            }
            if (sField.pValidation != null) {
                if (sField.pType.equals("text") || sField.pType.equals("binary")) {
                    try {
                        sField.aValidation = Pattern.compile(sField.pValidation);
                    } catch (PatternSyntaxException pse) {
                        r.addError("in " + where + ": invalid validation expression '" + sField.pValidation + "'");
                    }
                } else {
                    r.addError("in " + where + ": cannot use validations in a " + sField.pType + " field");
                }
            }
            if (Strings.isEmpty(sField.pDefaultValue)) {
                sField.aDefaultValue = null;
                sField.aIsRequired = true;
            } else if (sField.pDefaultValue.equals("null")) {
                sField.aDefaultValue = null;
                sField.aIsRequired = false;
            } else {
                sField.aIsRequired = false;
                try {
                    sField.aDefaultValue = _loadDefaultValue(sField.pDefaultValue, sField.aType, sField.aTextLength, driver);
                } catch (RuntimeException re) {
                    String typeName = sField.pType;
                    if (sField.aType == TYPE.BINARY || sField.aType == TYPE.TEXT) {
                        typeName = typeName + "(" + sField.aTextLength + ")";
                    }
                    if (re instanceof IllegalArgumentException) {
                        r.addError("in " + where + ": illegal default value '" + sField.pDefaultValue + "' for a " + typeName + " field");
                    } else if (re instanceof IndexOutOfBoundsException) {
                        r.addError("in " + where + ": illegal length in default value for a " + typeName + " field: " + re.getMessage());
                    } else {
                        throw re;
                    }
                }
            }
        }
    }

    private CONSTRAINT _checkConstraint(Results r, String constraint, String where, String constraintType, String defaultValue) {
        if (constraint.equals("no-action")) {
            return CONSTRAINT.NO_ACTION;
        } else if (constraint.equals("restrict")) {
            return CONSTRAINT.RESTRICT;
        } else if (constraint.equals("cascade")) {
            return CONSTRAINT.CASCADE;
        } else if (constraint.equals("set null")) {
            return CONSTRAINT.SET_NULL;
        } else if (constraint.equals("set default")) {
            if (Strings.isEmpty(defaultValue)) {
                r.addError("in " + where + ": 'set default' constraint setting cannot be used in field with no default value");
            }
            return CONSTRAINT.SET_DEFAULT;
        } else {
            r.addError("in " + where + ": illegal " + constraintType + " constraint '" + constraint + "'");
            return null;
        }
    }

    private void _checkForeignKeys(Results r, Map<String, SymbolField> aFields, String where, Map<String, ? extends MetaData> metadata) {
        Iterator<SymbolField> itFields = aFields.values().iterator();
        while (itFields.hasNext()) {
            SymbolField sField = itFields.next();
            if (sField.aType == TYPE.FOREIGN_KEY) {
                MetaData entity = metadata.get(sField.pTypeArgument);
                if (entity != null) {
                    if (entity.getPrimaryKey().length > 1) {
                        r.addError("in field '" + where + "." + sField.pName + "': cannot establish relation with entity '" + sField.pTypeArgument + "' because it has a composed primary key", null);
                    }
                }
            }
        }
    }

    private Boolean _getBoolean(Results r, String value, String where) {
        if (value == null) {
            return null;
        } else if (value.equals("true")) {
            return new Boolean(true);
        } else if (value.equals("false")) {
            return new Boolean(false);
        } else {
            r.addError("in property '" + where + "': expected boolean value, found '" + value + "'");
            return null;
        }
    }

    private void _addField(Results r, Map<String, SymbolField> fields, SymbolField sField, String where) {
        where = "field '" + where + "." + sField.pName + "'";
        if (sField != null) {
            if (fields.containsKey(sField.aName)) {
                r.addError("in " + where + ": name already exists");
            } else {
                fields.put(sField.aName, sField);
            }
        }
    }

    private Object _loadDefaultValue(String value, TYPE type, long length, DatabaseDriver driver) {
        if (type == TYPE.BOOLEAN) {
            if (value.equals("true")) {
                return new Boolean(true);
            } else if (value.equals("false")) {
                return new Boolean(false);
            } else {
                throw new IllegalArgumentException(value);
            }
        } else if (type == TYPE.SHORT) {
            return new Short(value);
        } else if (type == TYPE.INT) {
            return new Integer(value);
        } else if (type == TYPE.LONG) {
            return new Long(value);
        } else if (type == TYPE.FLOAT) {
            return new Float(value);
        } else if (type == TYPE.DOUBLE) {
            return new Double(value);
        } else if (type == TYPE.TEXT || type == TYPE.BINARY) {
            if (value.length() > length) {
                throw new IndexOutOfBoundsException(length + "");
            }
            return value;
        } else if (type == TYPE.DATE) {
            if (value.equals("now")) {
                return value;
            } else {
                return Date.valueOf(value, driver.getDateFormat());
            }
        } else if (type == TYPE.TIME) {
            if (value.equals("now")) {
                return value;
            } else {
                return Time.valueOf(value, driver.getTimeFormat());
            }
        } else if (type == TYPE.MOMENT) {
            if (value.equals("now")) {
                return value;
            } else {
                return Moment.valueOf(value, driver.getDateTimeFormat());
            }
        } else if (type == TYPE.FOREIGN_KEY) {
            throw new IllegalArgumentException(value);
        } else {
            throw new IntegrityException("unknown type: " + type);
        }
    }

    private String _checkName(Results r, String name, String where) {
        Matcher m = _pIdentifier.matcher(name);
        if (!m.matches()) {
            String illegalChars = m.replaceAll("_");
            illegalChars = illegalChars.replaceAll("_", "");
            r.addError("in " + where + ": name '" + name + "' has illegal characters '" + illegalChars + "'");
        }
        if (Character.isDigit(name.charAt(0))) {
            r.addError("in " + where + ": illegal name '" + name + "': identifiers cannot start with a digit");
        }
        if (!Strings.isLowerCase(name)) {
            r.addWarning("in " + where + ": name '" + name + "' should be lower case");
        }
        return name.toLowerCase();
    }

    private double _getSizeArgument(Results r, String argument, String where, String argType, TYPE type) {
        if (!argument.equals("") && !argument.endsWith(argType)) {
            r.addError("in " + where + ": wrong argument format for " + type + " type: '" + argument + "'");
            return 0.0;
        } else {
            argument = argument.replaceAll(argType, "");
            try {
                Double d = new Double(argument);
                return d.doubleValue();
            } catch (NumberFormatException nfe) {
                r.addError("in " + where + ": wrong argument format for " + type + " type: '" + argument + "'");
                return 0.0;
            }
        }
    }
}
