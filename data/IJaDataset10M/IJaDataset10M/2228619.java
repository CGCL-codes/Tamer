package com.amazon.carbonado.repo.jdbc;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.cojen.classfile.ClassFile;
import org.cojen.classfile.CodeBuilder;
import org.cojen.classfile.Label;
import org.cojen.classfile.LocalVariable;
import org.cojen.classfile.MethodInfo;
import org.cojen.classfile.Modifiers;
import org.cojen.classfile.Opcode;
import org.cojen.classfile.TypeDesc;
import org.cojen.util.ClassInjector;
import org.cojen.util.KeyFactory;
import org.joda.time.ReadableInstant;
import com.amazon.carbonado.FetchException;
import com.amazon.carbonado.OptimisticLockException;
import com.amazon.carbonado.PersistException;
import com.amazon.carbonado.Storable;
import com.amazon.carbonado.SupportException;
import com.amazon.carbonado.lob.Lob;
import com.amazon.carbonado.info.StorablePropertyAdapter;
import com.amazon.carbonado.gen.CodeBuilderUtil;
import com.amazon.carbonado.gen.MasterFeature;
import com.amazon.carbonado.gen.MasterStorableGenerator;
import com.amazon.carbonado.gen.MasterSupport;
import com.amazon.carbonado.gen.StorableGenerator;
import com.amazon.carbonado.gen.TriggerSupport;
import static com.amazon.carbonado.gen.CommonMethodNames.*;
import com.amazon.carbonado.util.SoftValuedCache;

/**
 * Generates concrete implementations of {@link Storable} types for
 * {@link JDBCRepository}.
 *
 * @author Brian S O'Neill
 */
class JDBCStorableGenerator<S extends Storable> {

    private static final String EXTRACT_ALL_METHOD_NAME = "extractAll$";

    private static final String EXTRACT_DATA_METHOD_NAME = "extractData$";

    private static final String LOB_LOADER_FIELD_PREFIX = "lobLoader$";

    private static final int INITIAL_UPDATE_BUFFER_SIZE = 100;

    private static final int NORMAL = 0;

    private static final int NOT_NULL = 1;

    private static final int INITIAL_VERSION = 2;

    private static final int INCREMENT_VERSION = 3;

    private static final SoftValuedCache<Object, Class<? extends Storable>> cCache;

    static {
        cCache = SoftValuedCache.newCache(11);
    }

    static <S extends Storable> Class<? extends S> getGeneratedClass(JDBCStorableInfo<S> info, boolean isMaster, boolean autoVersioning, boolean suppressReload) throws SupportException {
        Object key = KeyFactory.createKey(new Object[] { info, isMaster, autoVersioning, suppressReload });
        synchronized (cCache) {
            Class<? extends S> generatedClass = (Class<? extends S>) cCache.get(key);
            if (generatedClass != null) {
                return generatedClass;
            }
            generatedClass = new JDBCStorableGenerator<S>(info, isMaster, autoVersioning, suppressReload).generateAndInjectClass();
            cCache.put(key, generatedClass);
            return generatedClass;
        }
    }

    private final Class<S> mStorableType;

    private final JDBCStorableInfo<S> mInfo;

    private static enum Versioning {

        NONE, EXTERNAL, AUTO
    }

    private final Versioning mVersioning;

    private final boolean mSuppressReload;

    private final Map<String, ? extends JDBCStorableProperty<S>> mAllProperties;

    private final ClassLoader mParentClassLoader;

    private final ClassInjector mClassInjector;

    private final ClassFile mClassFile;

    private JDBCStorableGenerator(JDBCStorableInfo<S> info, boolean isMaster, boolean autoVersioning, boolean suppressReload) throws SupportException {
        mStorableType = info.getStorableType();
        mInfo = info;
        mAllProperties = mInfo.getAllProperties();
        EnumSet<MasterFeature> features = EnumSet.of(MasterFeature.INSERT_TXN, MasterFeature.UPDATE_TXN);
        if (!isMaster) {
            mVersioning = Versioning.NONE;
        } else {
            features.add(MasterFeature.INSERT_SEQUENCES);
            features.add(MasterFeature.INSERT_CHECK_REQUIRED);
            if (info.getVersionProperty() != null && info.getVersionProperty().isDerived()) {
                features.add(MasterFeature.VERSIONING);
                mVersioning = Versioning.NONE;
            } else {
                mVersioning = autoVersioning ? Versioning.AUTO : Versioning.EXTERNAL;
            }
        }
        if (suppressReload) {
            honorSuppression: {
                Map<String, JDBCStorableProperty<S>> identityProperties = info.getIdentityProperties();
                for (JDBCStorableProperty<S> prop : mAllProperties.values()) {
                    if (!prop.isSelectable()) {
                        continue;
                    }
                    if (prop.isAutomatic() && !identityProperties.containsKey(prop.getName())) {
                        suppressReload = false;
                        break honorSuppression;
                    }
                    if (prop.isVersion() && mVersioning == Versioning.EXTERNAL) {
                        suppressReload = false;
                        break honorSuppression;
                    }
                }
                features.remove(MasterFeature.INSERT_TXN);
                features.remove(MasterFeature.UPDATE_TXN);
            }
        }
        mSuppressReload = suppressReload;
        final Class<? extends S> abstractClass = MasterStorableGenerator.getAbstractClass(mStorableType, features);
        mParentClassLoader = abstractClass.getClassLoader();
        mClassInjector = ClassInjector.create(mStorableType.getName(), mParentClassLoader);
        mClassFile = new ClassFile(mClassInjector.getClassName(), abstractClass);
        mClassFile.markSynthetic();
        mClassFile.setSourceFile(JDBCStorableGenerator.class.getName());
        mClassFile.setTarget("1.5");
    }

    private Class<? extends S> generateAndInjectClass() throws SupportException {
        final Map<JDBCStorableProperty<S>, Class<?>> lobLoaderMap = generateLobLoaders();
        final TypeDesc jdbcSupportType = TypeDesc.forClass(JDBCSupport.class);
        final TypeDesc resultSetType = TypeDesc.forClass(ResultSet.class);
        final TypeDesc connectionType = TypeDesc.forClass(Connection.class);
        final TypeDesc preparedStatementType = TypeDesc.forClass(PreparedStatement.class);
        final TypeDesc lobArrayType = TypeDesc.forClass(Lob.class).toArrayType();
        final TypeDesc masterSupportType = TypeDesc.forClass(MasterSupport.class);
        final TypeDesc classType = TypeDesc.forClass(Class.class);
        if (lobLoaderMap.size() > 0) {
            MethodInfo mi = mClassFile.addInitializer();
            CodeBuilder b = new CodeBuilder(mi);
            int i = 0;
            for (Class<?> loaderClass : lobLoaderMap.values()) {
                String fieldName = LOB_LOADER_FIELD_PREFIX + i;
                mClassFile.addField(Modifiers.PRIVATE.toStatic(true).toFinal(true), fieldName, classType);
                b.loadConstant(TypeDesc.forClass(loaderClass));
                b.storeStaticField(fieldName, classType);
                i++;
            }
            b.returnVoid();
        }
        {
            TypeDesc[] params = { jdbcSupportType };
            MethodInfo mi = mClassFile.addConstructor(Modifiers.PUBLIC, params);
            CodeBuilder b = new CodeBuilder(mi);
            b.loadThis();
            b.loadLocal(b.getParameter(0));
            b.checkCast(masterSupportType);
            b.invokeSuperConstructor(new TypeDesc[] { masterSupportType });
            b.returnVoid();
        }
        {
            TypeDesc[] params = { jdbcSupportType, resultSetType, TypeDesc.INT };
            MethodInfo mi = mClassFile.addConstructor(Modifiers.PUBLIC, params);
            CodeBuilder b = new CodeBuilder(mi);
            b.loadThis();
            b.loadLocal(b.getParameter(0));
            b.checkCast(masterSupportType);
            b.invokeSuperConstructor(new TypeDesc[] { masterSupportType });
            b.loadThis();
            b.loadLocal(b.getParameter(1));
            b.loadLocal(b.getParameter(2));
            b.invokePrivate(EXTRACT_ALL_METHOD_NAME, null, new TypeDesc[] { resultSetType, TypeDesc.INT });
            b.loadThis();
            b.invokeVirtual(StorableGenerator.LOAD_COMPLETED_METHOD_NAME, null, null);
            b.returnVoid();
        }
        CodeBuilderUtil.definePrepareMethod(mClassFile, mStorableType, jdbcSupportType);
        defineExtractAllMethod(lobLoaderMap);
        defineExtractDataMethod(lobLoaderMap);
        {
            for (JDBCStorableProperty<S> property : mAllProperties.values()) {
                if (property.isDerived() || property.isJoin() || property.isSupported()) {
                    continue;
                }
                CodeBuilder b;
                Class exClass = UnsupportedOperationException.class;
                String message = "Independent property \"" + property.getName() + "\" is not supported by the SQL schema: ";
                if (property.isNullable()) {
                    b = new CodeBuilder(mClassFile.addMethod(property.getReadMethod()));
                    b.loadNull();
                    b.returnValue(TypeDesc.OBJECT);
                    b = new CodeBuilder(mClassFile.addMethod(property.getWriteMethod()));
                    b.loadLocal(b.getParameter(0));
                    Label notNull = b.createLabel();
                    b.ifNullBranch(notNull, false);
                    b.returnVoid();
                    notNull.setLocation();
                    CodeBuilderUtil.throwException(b, exClass, message);
                } else {
                    message += mInfo.getTableName();
                    b = new CodeBuilder(mClassFile.addMethod(property.getReadMethod()));
                    CodeBuilderUtil.throwException(b, exClass, message);
                    b = new CodeBuilder(mClassFile.addMethod(property.getWriteMethod()));
                    CodeBuilderUtil.throwException(b, exClass, message);
                }
            }
        }
        {
            MethodInfo mi = mClassFile.addMethod(Modifiers.PROTECTED, MasterStorableGenerator.DO_TRY_LOAD_MASTER_METHOD_NAME, TypeDesc.BOOLEAN, null);
            mi.addException(TypeDesc.forClass(FetchException.class));
            CodeBuilder b = new CodeBuilder(mi);
            LocalVariable supportVar = getJDBCSupport(b);
            Label tryBeforeCon = b.createLabel().setLocation();
            LocalVariable conVar = getConnection(b, supportVar);
            Label tryAfterCon = b.createLabel().setLocation();
            b.loadThis();
            b.loadLocal(supportVar);
            b.loadLocal(conVar);
            b.loadNull();
            b.invokeVirtual(MasterStorableGenerator.DO_TRY_LOAD_MASTER_METHOD_NAME, TypeDesc.BOOLEAN, new TypeDesc[] { jdbcSupportType, connectionType, lobArrayType });
            LocalVariable resultVar = b.createLocalVariable("result", TypeDesc.BOOLEAN);
            b.storeLocal(resultVar);
            yieldConAndHandleException(b, supportVar, tryBeforeCon, conVar, tryAfterCon, false);
            b.loadLocal(resultVar);
            b.returnValue(TypeDesc.BOOLEAN);
        }
        {
            MethodInfo mi = mClassFile.addMethod(Modifiers.PROTECTED, MasterStorableGenerator.DO_TRY_LOAD_MASTER_METHOD_NAME, TypeDesc.BOOLEAN, new TypeDesc[] { jdbcSupportType, connectionType, lobArrayType });
            mi.addException(TypeDesc.forClass(Exception.class));
            CodeBuilder b = new CodeBuilder(mi);
            StringBuilder selectBuilder = null;
            for (JDBCStorableProperty<S> property : mAllProperties.values()) {
                if (!property.isSelectable() || property.isPrimaryKeyMember()) {
                    continue;
                }
                if (selectBuilder == null) {
                    selectBuilder = new StringBuilder();
                    selectBuilder.append("SELECT ");
                } else {
                    selectBuilder.append(',');
                }
                selectBuilder.append(property.getColumnName());
            }
            if (selectBuilder == null) {
                selectBuilder = new StringBuilder();
                selectBuilder.append("SELECT ");
                selectBuilder.append(mInfo.getPrimaryKeyProperties().values().iterator().next().getColumnName());
            }
            selectBuilder.append(" FROM ");
            selectBuilder.append(mInfo.getQualifiedTableName());
            LocalVariable psVar = b.createLocalVariable("ps", preparedStatementType);
            Label tryAfterPs = buildWhereClauseAndPreparedStatement(b, selectBuilder, b.getParameter(1), psVar, b.getParameter(0), null);
            b.loadLocal(psVar);
            b.invokeInterface(preparedStatementType, "executeQuery", resultSetType, null);
            LocalVariable rsVar = b.createLocalVariable("rs", resultSetType);
            b.storeLocal(rsVar);
            Label tryAfterRs = b.createLabel().setLocation();
            LocalVariable resultVar = b.createLocalVariable("result", TypeDesc.BOOLEAN);
            b.loadLocal(rsVar);
            b.invokeInterface(resultSetType, "next", TypeDesc.BOOLEAN, null);
            b.storeLocal(resultVar);
            b.loadLocal(resultVar);
            Label noResults = b.createLabel();
            b.ifZeroComparisonBranch(noResults, "==");
            b.loadThis();
            b.loadLocal(rsVar);
            b.loadConstant(1);
            b.loadLocal(b.getParameter(2));
            b.invokePrivate(EXTRACT_DATA_METHOD_NAME, null, new TypeDesc[] { resultSetType, TypeDesc.INT, lobArrayType });
            noResults.setLocation();
            closeResultSet(b, rsVar, tryAfterRs);
            closeStatement(b, psVar, tryAfterPs);
            b.loadLocal(resultVar);
            b.returnValue(TypeDesc.BOOLEAN);
        }
        {
            MethodInfo mi = mClassFile.addMethod(Modifiers.PUBLIC, INSERT_METHOD_NAME, null, null);
            mi.addException(TypeDesc.forClass(PersistException.class));
            CodeBuilder b = new CodeBuilder(mi);
            Label tryStart = b.createLabel().setLocation();
            b.loadThis();
            b.invokeSuper(mClassFile.getSuperClassName(), INSERT_METHOD_NAME, null, null);
            Label tryEnd = b.createLabel().setLocation();
            b.returnVoid();
            b.exceptionHandler(tryStart, tryEnd, RuntimeException.class.getName());
            b.throwObject();
            b.exceptionHandler(tryStart, tryEnd, Exception.class.getName());
            pushJDBCSupport(b);
            b.swap();
            TypeDesc[] params = { TypeDesc.forClass(Throwable.class) };
            b.invokeInterface(jdbcSupportType, "toPersistException", TypeDesc.forClass(PersistException.class), params);
            b.throwObject();
        }
        {
            MethodInfo mi = mClassFile.addMethod(Modifiers.PUBLIC, TRY_INSERT_METHOD_NAME, TypeDesc.BOOLEAN, null);
            mi.addException(TypeDesc.forClass(PersistException.class));
            CodeBuilder b = new CodeBuilder(mi);
            Label tryStart = b.createLabel().setLocation();
            b.loadThis();
            b.invokeSuper(mClassFile.getSuperClassName(), TRY_INSERT_METHOD_NAME, TypeDesc.BOOLEAN, null);
            Label innerTryEnd = b.createLabel().setLocation();
            b.returnValue(TypeDesc.BOOLEAN);
            b.exceptionHandler(tryStart, innerTryEnd, SQLException.class.getName());
            b.dup();
            pushJDBCSupport(b);
            b.swap();
            b.invokeInterface(jdbcSupportType, "isUniqueConstraintError", TypeDesc.BOOLEAN, new TypeDesc[] { TypeDesc.forClass(SQLException.class) });
            Label notConstraint = b.createLabel();
            b.ifZeroComparisonBranch(notConstraint, "==");
            b.loadConstant(false);
            b.returnValue(TypeDesc.BOOLEAN);
            notConstraint.setLocation();
            b.throwObject();
            Label outerTryEnd = b.createLabel().setLocation();
            b.exceptionHandler(tryStart, outerTryEnd, RuntimeException.class.getName());
            b.throwObject();
            b.exceptionHandler(tryStart, outerTryEnd, Exception.class.getName());
            pushJDBCSupport(b);
            b.swap();
            TypeDesc[] params = { TypeDesc.forClass(Throwable.class) };
            b.invokeInterface(jdbcSupportType, "toPersistException", TypeDesc.forClass(PersistException.class), params);
            b.throwObject();
        }
        {
            MethodInfo mi = mClassFile.addMethod(Modifiers.PROTECTED, MasterStorableGenerator.DO_TRY_INSERT_MASTER_METHOD_NAME, TypeDesc.BOOLEAN, null);
            mi.addException(TypeDesc.forClass(PersistException.class));
            CodeBuilder b = new CodeBuilder(mi);
            LocalVariable supportVar = getJDBCSupport(b);
            LocalVariable conVar = getConnection(b, supportVar);
            Label tryAfterCon = b.createLabel().setLocation();
            b.loadLocal(conVar);
            String staticInsertStatement;
            {
                StringBuilder sb = new StringBuilder();
                sb.append("INSERT INTO ");
                sb.append(mInfo.getQualifiedTableName());
                sb.append(" ( ");
                int ordinal = 0;
                for (JDBCStorableProperty<?> property : mAllProperties.values()) {
                    if (!property.isSelectable()) {
                        continue;
                    }
                    if (ordinal > 0) {
                        sb.append(',');
                    }
                    sb.append(property.getColumnName());
                    ordinal++;
                }
                sb.append(" ) VALUES (");
                for (int i = 0; i < ordinal; i++) {
                    if (i > 0) {
                        sb.append(',');
                    }
                    sb.append('?');
                }
                sb.append(')');
                staticInsertStatement = sb.toString();
            }
            boolean useStaticInsertStatement = true;
            for (JDBCStorableProperty<?> property : mAllProperties.values()) {
                if (!property.isDerived()) {
                    if (property.isVersion() || property.isAutomatic()) {
                        useStaticInsertStatement = false;
                        break;
                    }
                }
            }
            LocalVariable insertCountVar = null;
            if (useStaticInsertStatement) {
                b.loadConstant(staticInsertStatement);
            } else {
                insertCountVar = b.createLocalVariable(null, TypeDesc.INT);
                int initialCount = 0;
                for (JDBCStorableProperty<?> property : mAllProperties.values()) {
                    if (!property.isSelectable()) {
                        continue;
                    }
                    if (isAlwaysInserted(property)) {
                        initialCount++;
                    }
                }
                b.loadConstant(initialCount);
                b.storeLocal(insertCountVar);
                TypeDesc stringBuilderType = TypeDesc.forClass(StringBuilder.class);
                b.newObject(stringBuilderType);
                b.dup();
                b.loadConstant(staticInsertStatement.length());
                b.invokeConstructor(stringBuilderType, new TypeDesc[] { TypeDesc.INT });
                b.loadConstant("INSERT INTO " + mInfo.getQualifiedTableName() + " ( ");
                CodeBuilderUtil.callStringBuilderAppendString(b);
                int propNumber = -1;
                for (JDBCStorableProperty<?> property : mAllProperties.values()) {
                    propNumber++;
                    if (!property.isSelectable()) {
                        continue;
                    }
                    Label nextProperty = b.createLabel();
                    if (!isAlwaysInserted(property)) {
                        branchIfDirty(b, propNumber, nextProperty, false);
                        b.integerIncrement(insertCountVar, 1);
                    }
                    b.loadConstant(property.getColumnName() + ',');
                    CodeBuilderUtil.callStringBuilderAppendString(b);
                    nextProperty.setLocation();
                }
                LocalVariable sbVar = b.createLocalVariable(null, stringBuilderType);
                b.storeLocal(sbVar);
                b.loadLocal(sbVar);
                b.loadLocal(sbVar);
                CodeBuilderUtil.callStringBuilderLength(b);
                b.loadConstant(1);
                b.math(Opcode.ISUB);
                CodeBuilderUtil.callStringBuilderSetLength(b);
                b.loadLocal(sbVar);
                b.loadConstant(" ) VALUES (");
                CodeBuilderUtil.callStringBuilderAppendString(b);
                b.loadLocal(insertCountVar);
                Label finishStatement = b.createLabel();
                b.ifZeroComparisonBranch(finishStatement, "<=");
                b.loadConstant('?');
                CodeBuilderUtil.callStringBuilderAppendChar(b);
                Label loopStart = b.createLabel().setLocation();
                b.integerIncrement(insertCountVar, -1);
                b.loadLocal(insertCountVar);
                b.ifZeroComparisonBranch(finishStatement, "<=");
                b.loadConstant(",?");
                CodeBuilderUtil.callStringBuilderAppendString(b);
                b.branch(loopStart);
                finishStatement.setLocation();
                b.loadConstant(')');
                CodeBuilderUtil.callStringBuilderAppendChar(b);
                CodeBuilderUtil.callStringBuilderToString(b);
            }
            Collection<JDBCStorableProperty<S>> identityProperties = mInfo.getIdentityProperties().values();
            LocalVariable psVar = b.createLocalVariable("ps", preparedStatementType);
            if (identityProperties.isEmpty()) {
                b.invokeInterface(connectionType, "prepareStatement", preparedStatementType, new TypeDesc[] { TypeDesc.STRING });
            } else {
                b.loadConstant(Statement.RETURN_GENERATED_KEYS);
                b.invokeInterface(connectionType, "prepareStatement", preparedStatementType, new TypeDesc[] { TypeDesc.STRING, TypeDesc.INT });
            }
            b.storeLocal(psVar);
            Label tryAfterPs = b.createLabel().setLocation();
            Map<JDBCStorableProperty<S>, Integer> lobIndexMap = findLobs();
            LocalVariable lobArrayVar = null;
            if (lobIndexMap.size() != 0) {
                lobArrayVar = b.createLocalVariable(null, lobArrayType);
                b.loadConstant(lobIndexMap.size());
                b.newObject(lobArrayType);
                b.storeLocal(lobArrayVar);
            }
            int ordinal = 0;
            LocalVariable ordinalVar = null;
            if (!useStaticInsertStatement) {
                ordinalVar = b.createLocalVariable(null, TypeDesc.INT);
                b.loadConstant(0);
                b.storeLocal(ordinalVar);
            }
            int propNumber = -1;
            for (JDBCStorableProperty<S> property : mAllProperties.values()) {
                propNumber++;
                if (!property.isSelectable()) {
                    continue;
                }
                Label nextProperty = b.createLabel();
                if (!isAlwaysInserted(property)) {
                    branchIfDirty(b, propNumber, nextProperty, false);
                }
                b.loadLocal(psVar);
                if (ordinalVar == null) {
                    b.loadConstant(++ordinal);
                } else {
                    b.integerIncrement(ordinalVar, 1);
                    b.loadLocal(ordinalVar);
                }
                Label setNormally = b.createLabel();
                if (property.isVersion() && mVersioning == Versioning.AUTO) {
                    branchIfDirty(b, propNumber, setNormally, true);
                    setPreparedStatementValue(b, property, INITIAL_VERSION, null, lobArrayVar, lobIndexMap.get(property));
                    b.branch(nextProperty);
                }
                setNormally.setLocation();
                setPreparedStatementValue(b, property, NORMAL, null, lobArrayVar, lobIndexMap.get(property));
                nextProperty.setLocation();
            }
            b.loadLocal(psVar);
            b.invokeInterface(preparedStatementType, "executeUpdate", TypeDesc.INT, null);
            b.pop();
            if (identityProperties.size() > 0) {
                b.loadLocal(psVar);
                b.invokeInterface(preparedStatementType, "getGeneratedKeys", resultSetType, null);
                LocalVariable rsVar = b.createLocalVariable("rs", resultSetType);
                b.storeLocal(rsVar);
                Label tryAfterRs = b.createLabel().setLocation();
                b.loadLocal(rsVar);
                b.invokeInterface(resultSetType, "next", TypeDesc.BOOLEAN, null);
                Label noResults = b.createLabel();
                b.ifZeroComparisonBranch(noResults, "==");
                LocalVariable initialOffsetVar = b.createLocalVariable(null, TypeDesc.INT);
                b.loadConstant(1);
                b.storeLocal(initialOffsetVar);
                defineExtract(b, rsVar, initialOffsetVar, null, mInfo.getIdentityProperties().values(), lobLoaderMap);
                noResults.setLocation();
                closeResultSet(b, rsVar, tryAfterRs);
            }
            closeStatement(b, psVar, tryAfterPs);
            if (!mSuppressReload) {
                b.loadThis();
                b.loadLocal(supportVar);
                b.loadLocal(conVar);
                if (lobArrayVar == null) {
                    b.loadNull();
                } else {
                    b.loadLocal(lobArrayVar);
                }
                b.invokeVirtual(MasterStorableGenerator.DO_TRY_LOAD_MASTER_METHOD_NAME, TypeDesc.BOOLEAN, new TypeDesc[] { jdbcSupportType, connectionType, lobArrayType });
                Label reloaded = b.createLabel();
                b.ifZeroComparisonBranch(reloaded, "!=");
                String message = "Reload after insert failed, " + "possibly because database changed the primary key: ";
                for (JDBCStorableProperty<S> prop : mInfo.getPrimaryKeyProperties().values()) {
                    Class type = prop.getType();
                    if (Date.class.isAssignableFrom(type) || ReadableInstant.class.isAssignableFrom(type)) {
                        message += "Property type of date may have been truncated: " + prop.getName() + ": ";
                    }
                }
                TypeDesc persistExType = TypeDesc.forClass(PersistException.class);
                b.newObject(persistExType);
                b.dup();
                b.loadConstant(message);
                b.loadThis();
                b.invokeVirtual(TO_STRING_KEY_ONLY_METHOD_NAME, TypeDesc.STRING, null);
                b.invokeVirtual(TypeDesc.STRING, "concat", TypeDesc.STRING, new TypeDesc[] { TypeDesc.STRING });
                b.invokeConstructor(persistExType, new TypeDesc[] { TypeDesc.STRING });
                b.throwObject();
                reloaded.setLocation();
            }
            yieldCon(b, supportVar, conVar, tryAfterCon);
            b.loadConstant(true);
            b.returnValue(TypeDesc.BOOLEAN);
        }
        {
            MethodInfo mi = mClassFile.addMethod(Modifiers.PROTECTED, MasterStorableGenerator.DO_TRY_UPDATE_MASTER_METHOD_NAME, TypeDesc.BOOLEAN, null);
            mi.addException(TypeDesc.forClass(PersistException.class));
            CodeBuilder b = new CodeBuilder(mi);
            LocalVariable supportVar = getJDBCSupport(b);
            Label tryBeforeCon = b.createLabel().setLocation();
            LocalVariable conVar = getConnection(b, supportVar);
            Label tryAfterCon = b.createLabel().setLocation();
            b.loadLocal(conVar);
            TypeDesc stringBuilderType = TypeDesc.forClass(StringBuilder.class);
            b.newObject(stringBuilderType);
            b.dup();
            b.loadConstant(INITIAL_UPDATE_BUFFER_SIZE);
            b.invokeConstructor(stringBuilderType, new TypeDesc[] { TypeDesc.INT });
            {
                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("UPDATE ");
                sqlBuilder.append(mInfo.getQualifiedTableName());
                sqlBuilder.append(" SET ");
                b.loadConstant(sqlBuilder.toString());
                CodeBuilderUtil.callStringBuilderAppendString(b);
            }
            LocalVariable countVar = b.createLocalVariable("count", TypeDesc.INT);
            b.loadConstant(0);
            b.storeLocal(countVar);
            int propNumber = -1;
            for (JDBCStorableProperty property : mAllProperties.values()) {
                propNumber++;
                if (property.isSelectable() && !property.isPrimaryKeyMember()) {
                    if (property.isVersion() && mVersioning == Versioning.EXTERNAL) {
                        continue;
                    }
                    Label isNotDirty = null;
                    if (!property.isVersion() || mVersioning != Versioning.AUTO) {
                        isNotDirty = b.createLabel();
                        branchIfDirty(b, propNumber, isNotDirty, false);
                    }
                    b.loadLocal(countVar);
                    Label isZero = b.createLabel();
                    b.ifZeroComparisonBranch(isZero, "==");
                    b.loadConstant(',');
                    CodeBuilderUtil.callStringBuilderAppendChar(b);
                    isZero.setLocation();
                    b.loadConstant(property.getColumnName());
                    CodeBuilderUtil.callStringBuilderAppendString(b);
                    b.loadConstant("=?");
                    CodeBuilderUtil.callStringBuilderAppendString(b);
                    b.integerIncrement(countVar, 1);
                    if (isNotDirty != null) {
                        isNotDirty.setLocation();
                    }
                }
            }
            Collection<JDBCStorableProperty<S>> whereProperties = mInfo.getPrimaryKeyProperties().values();
            JDBCStorableProperty<S> versionProperty = mInfo.getVersionProperty();
            if (versionProperty != null) {
                if (!versionProperty.isSelectable() || mVersioning == Versioning.NONE) {
                    versionProperty = null;
                } else {
                    List<JDBCStorableProperty<S>> list = new ArrayList<JDBCStorableProperty<S>>(whereProperties);
                    list.add(versionProperty);
                    whereProperties = list;
                }
            }
            {
                b.loadLocal(countVar);
                Label notZero = b.createLabel();
                b.ifZeroComparisonBranch(notZero, "!=");
                b.loadConstant(whereProperties.iterator().next().getColumnName());
                CodeBuilderUtil.callStringBuilderAppendString(b);
                b.loadConstant("=?");
                CodeBuilderUtil.callStringBuilderAppendString(b);
                notZero.setLocation();
            }
            b.loadConstant(" WHERE ");
            CodeBuilderUtil.callStringBuilderAppendString(b);
            int ordinal = 0;
            for (JDBCStorableProperty<S> property : whereProperties) {
                if (ordinal > 0) {
                    b.loadConstant(" AND ");
                    CodeBuilderUtil.callStringBuilderAppendString(b);
                }
                b.loadConstant(property.getColumnName());
                CodeBuilderUtil.callStringBuilderAppendString(b);
                Label next = b.createLabel();
                if (property.isNullable()) {
                    b.loadThis();
                    b.loadField(property.getName(), TypeDesc.forClass(property.getType()));
                    Label notNull = b.createLabel();
                    b.ifNullBranch(notNull, false);
                    b.loadConstant(" IS NULL");
                    CodeBuilderUtil.callStringBuilderAppendString(b);
                    b.branch(next);
                    notNull.setLocation();
                }
                b.loadConstant("=?");
                CodeBuilderUtil.callStringBuilderAppendString(b);
                next.setLocation();
                ordinal++;
            }
            CodeBuilderUtil.callStringBuilderToString(b);
            LocalVariable psVar = b.createLocalVariable("ps", preparedStatementType);
            b.invokeInterface(connectionType, "prepareStatement", preparedStatementType, new TypeDesc[] { TypeDesc.STRING });
            b.storeLocal(psVar);
            Label tryAfterPs = b.createLabel().setLocation();
            LocalVariable indexVar = b.createLocalVariable("index", TypeDesc.INT);
            b.loadConstant(1);
            b.storeLocal(indexVar);
            Map<JDBCStorableProperty<S>, Integer> lobIndexMap = findLobs();
            LocalVariable lobArrayVar = null;
            if (lobIndexMap.size() != 0) {
                lobArrayVar = b.createLocalVariable(null, lobArrayType);
                b.loadConstant(lobIndexMap.size());
                b.newObject(lobArrayType);
                b.storeLocal(lobArrayVar);
            }
            {
                b.loadLocal(countVar);
                Label notZero = b.createLabel();
                b.ifZeroComparisonBranch(notZero, "!=");
                JDBCStorableProperty property = whereProperties.iterator().next();
                b.loadLocal(psVar);
                b.loadLocal(indexVar);
                setPreparedStatementValue(b, property, NORMAL, null, lobArrayVar, lobIndexMap.get(property));
                b.integerIncrement(indexVar, 1);
                notZero.setLocation();
            }
            propNumber = -1;
            for (JDBCStorableProperty property : mAllProperties.values()) {
                propNumber++;
                if (property.isSelectable() && !property.isPrimaryKeyMember()) {
                    if (property.isVersion() && mVersioning == Versioning.EXTERNAL) {
                        continue;
                    }
                    Label isNotDirty = null;
                    if (!property.isVersion() || mVersioning != Versioning.AUTO) {
                        isNotDirty = b.createLabel();
                        branchIfDirty(b, propNumber, isNotDirty, false);
                    }
                    b.loadLocal(psVar);
                    b.loadLocal(indexVar);
                    int mode = (property.isVersion() && mVersioning == Versioning.AUTO) ? INCREMENT_VERSION : NORMAL;
                    setPreparedStatementValue(b, property, mode, null, lobArrayVar, lobIndexMap.get(property));
                    b.integerIncrement(indexVar, 1);
                    if (isNotDirty != null) {
                        isNotDirty.setLocation();
                    }
                }
            }
            for (JDBCStorableProperty<S> property : whereProperties) {
                Label nextProperty = b.createLabel();
                if (property.isNullable()) {
                    b.loadThis();
                    b.loadField(property.getName(), TypeDesc.forClass(property.getType()));
                    b.ifNullBranch(nextProperty, true);
                }
                b.loadLocal(psVar);
                b.loadLocal(indexVar);
                setPreparedStatementValue(b, property, NOT_NULL, null, null, null);
                b.integerIncrement(indexVar, 1);
                nextProperty.setLocation();
            }
            b.loadLocal(psVar);
            LocalVariable updateCount = b.createLocalVariable("updateCount", TypeDesc.INT);
            b.invokeInterface(preparedStatementType, "executeUpdate", TypeDesc.INT, null);
            b.storeLocal(updateCount);
            closeStatement(b, psVar, tryAfterPs);
            Label doReload = b.createLabel();
            Label skipReload = b.createLabel();
            if (versionProperty == null) {
                b.loadLocal(updateCount);
                b.ifZeroComparisonBranch(skipReload, "==");
            } else {
                b.loadLocal(updateCount);
                b.ifZeroComparisonBranch(doReload, "!=");
                StringBuilder selectBuilder = new StringBuilder();
                selectBuilder.append("SELECT ");
                selectBuilder.append(versionProperty.getColumnName());
                selectBuilder.append(" FROM ");
                selectBuilder.append(mInfo.getQualifiedTableName());
                LocalVariable countPsVar = b.createLocalVariable("ps", preparedStatementType);
                Label tryAfterCountPs = buildWhereClauseAndPreparedStatement(b, selectBuilder, conVar, countPsVar, null, null);
                b.loadLocal(countPsVar);
                b.invokeInterface(preparedStatementType, "executeQuery", resultSetType, null);
                LocalVariable rsVar = b.createLocalVariable("rs", resultSetType);
                b.storeLocal(rsVar);
                Label tryAfterRs = b.createLabel().setLocation();
                b.loadLocal(rsVar);
                b.invokeInterface(resultSetType, "next", TypeDesc.BOOLEAN, null);
                b.ifZeroComparisonBranch(skipReload, "==");
                b.loadLocal(rsVar);
                b.loadConstant(1);
                b.invokeInterface(resultSetType, "getLong", TypeDesc.LONG, new TypeDesc[] { TypeDesc.INT });
                LocalVariable actualVersion = b.createLocalVariable(null, TypeDesc.LONG);
                b.storeLocal(actualVersion);
                closeResultSet(b, rsVar, tryAfterRs);
                closeStatement(b, countPsVar, tryAfterCountPs);
                {
                    TypeDesc desc = TypeDesc.forClass(OptimisticLockException.class);
                    b.newObject(desc);
                    b.dup();
                    b.loadThis();
                    TypeDesc propertyType = TypeDesc.forClass(versionProperty.getType());
                    b.loadField(versionProperty.getName(), propertyType);
                    b.convert(propertyType, TypeDesc.LONG.toObjectType());
                    b.loadLocal(actualVersion);
                    b.convert(TypeDesc.LONG, TypeDesc.LONG.toObjectType());
                    b.invokeConstructor(desc, new TypeDesc[] { TypeDesc.OBJECT, TypeDesc.OBJECT });
                    b.throwObject();
                }
            }
            doReload.setLocation();
            if (!mSuppressReload) {
                b.loadThis();
                b.loadLocal(supportVar);
                b.loadLocal(conVar);
                if (lobArrayVar == null) {
                    b.loadNull();
                } else {
                    b.loadLocal(lobArrayVar);
                }
                b.invokeVirtual(MasterStorableGenerator.DO_TRY_LOAD_MASTER_METHOD_NAME, TypeDesc.BOOLEAN, new TypeDesc[] { jdbcSupportType, connectionType, lobArrayType });
                b.storeLocal(updateCount);
            }
            skipReload.setLocation();
            yieldConAndHandleException(b, supportVar, tryBeforeCon, conVar, tryAfterCon, true);
            b.loadLocal(updateCount);
            b.returnValue(TypeDesc.BOOLEAN);
        }
        {
            MethodInfo mi = mClassFile.addMethod(Modifiers.PROTECTED, MasterStorableGenerator.DO_TRY_DELETE_MASTER_METHOD_NAME, TypeDesc.BOOLEAN, null);
            mi.addException(TypeDesc.forClass(PersistException.class));
            CodeBuilder b = new CodeBuilder(mi);
            StringBuilder deleteBuilder = new StringBuilder();
            deleteBuilder.append("DELETE FROM ");
            deleteBuilder.append(mInfo.getQualifiedTableName());
            LocalVariable supportVar = getJDBCSupport(b);
            Label tryBeforeCon = b.createLabel().setLocation();
            LocalVariable conVar = getConnection(b, supportVar);
            Label tryAfterCon = b.createLabel().setLocation();
            LocalVariable psVar = b.createLocalVariable("ps", preparedStatementType);
            Label tryAfterPs = buildWhereClauseAndPreparedStatement(b, deleteBuilder, conVar, psVar, null, null);
            b.loadLocal(psVar);
            b.invokeInterface(preparedStatementType, "executeUpdate", TypeDesc.INT, null);
            LocalVariable resultVar = b.createLocalVariable("result", TypeDesc.INT);
            b.storeLocal(resultVar);
            closeStatement(b, psVar, tryAfterPs);
            yieldConAndHandleException(b, supportVar, tryBeforeCon, conVar, tryAfterCon, true);
            b.loadLocal(resultVar);
            b.returnValue(TypeDesc.BOOLEAN);
        }
        Class<? extends S> generatedClass = mClassInjector.defineClass(mClassFile);
        lobLoaderMap.size();
        return generatedClass;
    }

    /**
     * Returns true if property value is always part of insert statement.
     */
    private boolean isAlwaysInserted(JDBCStorableProperty<?> property) {
        return property.isVersion() ? (mVersioning == Versioning.AUTO) : !property.isAutomatic();
    }

    /**
     * Finds all Lob properties and maps them to a zero-based index. This
     * information is used to update large Lobs after an insert or update.
     */
    private Map<JDBCStorableProperty<S>, Integer> findLobs() {
        Map<JDBCStorableProperty<S>, Integer> lobIndexMap = new IdentityHashMap<JDBCStorableProperty<S>, Integer>();
        int lobIndex = 0;
        for (JDBCStorableProperty<S> property : mAllProperties.values()) {
            if (!property.isSelectable() || property.isVersion()) {
                continue;
            }
            Class psClass = property.getPreparedStatementSetMethod().getParameterTypes()[1];
            if (Lob.class.isAssignableFrom(property.getType()) || java.sql.Blob.class.isAssignableFrom(psClass) || java.sql.Clob.class.isAssignableFrom(psClass)) {
                lobIndexMap.put(property, lobIndex++);
            }
        }
        return lobIndexMap;
    }

    /**
     * Generates code to get the JDBCSupport instance and store it in a local
     * variable.
     */
    private LocalVariable getJDBCSupport(CodeBuilder b) {
        pushJDBCSupport(b);
        LocalVariable supportVar = b.createLocalVariable("support", TypeDesc.forClass(JDBCSupport.class));
        b.storeLocal(supportVar);
        return supportVar;
    }

    /**
     * Generates code to push the JDBCSupport instance on the stack.
     */
    private void pushJDBCSupport(CodeBuilder b) {
        b.loadThis();
        b.loadField(StorableGenerator.SUPPORT_FIELD_NAME, TypeDesc.forClass(TriggerSupport.class));
        b.checkCast(TypeDesc.forClass(JDBCSupport.class));
    }

    /**
     * Generates code to get connection from JDBCConnectionCapability and store
     * it in a local variable.
     *
     * @param capVar reference to JDBCConnectionCapability
     */
    private LocalVariable getConnection(CodeBuilder b, LocalVariable capVar) {
        b.loadLocal(capVar);
        b.invokeInterface(TypeDesc.forClass(JDBCConnectionCapability.class), "getConnection", TypeDesc.forClass(Connection.class), null);
        LocalVariable conVar = b.createLocalVariable("con", TypeDesc.forClass(Connection.class));
        b.storeLocal(conVar);
        return conVar;
    }

    /**
     * Generates code which emulates this:
     *
     *     // May throw FetchException
     *     JDBCConnectionCapability.yieldConnection(con);
     *
     * @param capVar required reference to JDBCConnectionCapability
     * @param conVar optional connection to yield
     */
    private void yieldConnection(CodeBuilder b, LocalVariable capVar, LocalVariable conVar) {
        if (conVar != null) {
            b.loadLocal(capVar);
            b.loadLocal(conVar);
            b.invokeInterface(TypeDesc.forClass(JDBCConnectionCapability.class), "yieldConnection", null, new TypeDesc[] { TypeDesc.forClass(Connection.class) });
        }
    }

    /**
     * Generates code that finishes the given SQL statement by appending a
     * WHERE clause. Prepared statement is then created and all parameters are
     * filled in.
     *
     * @param sqlBuilder contains SQL statement right before the WHERE clause
     * @param conVar local variable referencing connection
     * @param psVar declared local variable which will receive PreparedStatement
     * @param capVar when non-null, check transaction if SELECT should be FOR UPDATE
     * @param instanceVar when null, assume properties are contained in
     * "this". Otherwise, invoke property access methods on storable referenced
     * in var.
     * @return label right after prepared statement was created, which is to be
     * used as the start of a try block that ensures the prepared statement is
     * closed.
     */
    private Label buildWhereClauseAndPreparedStatement(CodeBuilder b, StringBuilder sqlBuilder, LocalVariable conVar, LocalVariable psVar, LocalVariable capVar, LocalVariable instanceVar) throws SupportException {
        final TypeDesc superType = TypeDesc.forClass(mClassFile.getSuperClassName());
        final Iterable<? extends JDBCStorableProperty<?>> properties = mInfo.getPrimaryKeyProperties().values();
        sqlBuilder.append(" WHERE ");
        List<JDBCStorableProperty> nullableProperties = new ArrayList<JDBCStorableProperty>();
        int ordinal = 0;
        for (JDBCStorableProperty property : properties) {
            if (!property.isSelectable()) {
                continue;
            }
            if (property.isNullable()) {
                nullableProperties.add(property);
                continue;
            }
            if (ordinal > 0) {
                sqlBuilder.append(" AND ");
            }
            sqlBuilder.append(property.getColumnName());
            sqlBuilder.append("=?");
            ordinal++;
        }
        b.loadLocal(conVar);
        if (nullableProperties.isEmpty()) {
            b.loadConstant(sqlBuilder.toString());
            if (capVar != null) {
                b.loadLocal(capVar);
                b.invokeInterface(TypeDesc.forClass(JDBCConnectionCapability.class), "isTransactionForUpdate", TypeDesc.BOOLEAN, null);
                Label notForUpdate = b.createLabel();
                b.ifZeroComparisonBranch(notForUpdate, "==");
                b.loadConstant(" FOR UPDATE");
                b.invokeVirtual(TypeDesc.STRING, "concat", TypeDesc.STRING, new TypeDesc[] { TypeDesc.STRING });
                notForUpdate.setLocation();
            }
        } else {
            if (ordinal > 0) {
                sqlBuilder.append(" AND ");
            }
            int capacity = sqlBuilder.length() + 7 * nullableProperties.size();
            if (nullableProperties.size() > 1) {
                capacity += 5 * (nullableProperties.size() - 1);
            }
            for (JDBCStorableProperty property : nullableProperties) {
                capacity += property.getColumnName().length();
            }
            TypeDesc stringBuilderType = TypeDesc.forClass(StringBuilder.class);
            b.newObject(stringBuilderType);
            b.dup();
            b.loadConstant(capacity);
            b.invokeConstructor(stringBuilderType, new TypeDesc[] { TypeDesc.INT });
            b.loadConstant(sqlBuilder.toString());
            CodeBuilderUtil.callStringBuilderAppendString(b);
            ordinal = 0;
            for (JDBCStorableProperty property : nullableProperties) {
                if (ordinal > 0) {
                    b.loadConstant(" AND ");
                    CodeBuilderUtil.callStringBuilderAppendString(b);
                }
                b.loadConstant(property.getColumnName());
                CodeBuilderUtil.callStringBuilderAppendString(b);
                b.loadThis();
                final TypeDesc propertyType = TypeDesc.forClass(property.getType());
                b.loadField(superType, property.getName(), propertyType);
                Label notNull = b.createLabel();
                b.ifNullBranch(notNull, false);
                b.loadConstant(" IS NULL");
                CodeBuilderUtil.callStringBuilderAppendString(b);
                Label next = b.createLabel();
                b.branch(next);
                notNull.setLocation();
                b.loadConstant("=?");
                CodeBuilderUtil.callStringBuilderAppendString(b);
                next.setLocation();
                ordinal++;
            }
            if (capVar != null) {
                b.loadLocal(capVar);
                b.invokeInterface(TypeDesc.forClass(JDBCConnectionCapability.class), "isTransactionForUpdate", TypeDesc.BOOLEAN, null);
                Label notForUpdate = b.createLabel();
                b.ifZeroComparisonBranch(notForUpdate, "==");
                b.loadConstant(" FOR UPDATE");
                CodeBuilderUtil.callStringBuilderAppendString(b);
                notForUpdate.setLocation();
            }
            CodeBuilderUtil.callStringBuilderToString(b);
        }
        final TypeDesc connectionType = TypeDesc.forClass(Connection.class);
        final TypeDesc preparedStatementType = TypeDesc.forClass(PreparedStatement.class);
        b.invokeInterface(connectionType, "prepareStatement", preparedStatementType, new TypeDesc[] { TypeDesc.STRING });
        b.storeLocal(psVar);
        Label tryAfterPs = b.createLabel().setLocation();
        ordinal = 0;
        for (JDBCStorableProperty property : properties) {
            if (!property.isSelectable() || property.isNullable()) {
                continue;
            }
            ordinal++;
            b.loadLocal(psVar);
            b.loadConstant(ordinal);
            setPreparedStatementValue(b, property, NOT_NULL, instanceVar, null, null);
        }
        if (!nullableProperties.isEmpty()) {
            ordinal++;
            LocalVariable indexVar;
            if (nullableProperties.size() == 1) {
                indexVar = null;
            } else {
                indexVar = b.createLocalVariable(null, TypeDesc.INT);
                b.loadConstant(ordinal);
                b.storeLocal(indexVar);
            }
            Iterator<JDBCStorableProperty> it = nullableProperties.iterator();
            while (true) {
                JDBCStorableProperty property = it.next();
                TypeDesc propertyType = TypeDesc.forClass(property.getType());
                b.loadThis();
                b.loadField(superType, property.getName(), propertyType);
                Label nextProperty = b.createLabel();
                b.ifNullBranch(nextProperty, true);
                b.loadLocal(psVar);
                if (indexVar == null) {
                    b.loadConstant(ordinal);
                } else {
                    b.loadLocal(indexVar);
                }
                setPreparedStatementValue(b, property, NOT_NULL, instanceVar, null, null);
                if (it.hasNext()) {
                    if (indexVar != null) {
                        b.integerIncrement(indexVar, 1);
                    }
                    nextProperty.setLocation();
                } else {
                    nextProperty.setLocation();
                    break;
                }
            }
        }
        return tryAfterPs;
    }

    /**
     * Generates code to call a PreparedStatement.setXxx(int, Xxx) method, with
     * the value of the given property. Assumes that PreparedStatement and int
     * index are on the stack.
     *
     * If the property is a Lob, then pass in the optional lobTooLargeVar to
     * track if it was too large to insert/update. The type of lobTooLargeVar
     * must be the carbonado lob type. At runtime, if the variable's value is
     * not null, then lob was too large to insert. The value of the variable is
     * the original lob. An update statement needs to be issued after the load
     * to insert/update the large value.
     *
     * @param mode one of NORMAL, NOT_NULL, INITIAL_VERSION or INCREMENT_VERSION
     * @param instanceVar when null, assume properties are contained in
     * "this". Otherwise, invoke property access methods on storable referenced
     * in var.
     * @param lobArrayVar optional, used for lob properties
     * @param lobIndex optional, used for lob properties
     */
    private void setPreparedStatementValue(CodeBuilder b, JDBCStorableProperty<?> property, int mode, LocalVariable instanceVar, LocalVariable lobArrayVar, Integer lobIndex) throws SupportException {
        Class psClass = property.getPreparedStatementSetMethod().getParameterTypes()[1];
        TypeDesc psType = TypeDesc.forClass(psClass);
        TypeDesc propertyType = TypeDesc.forClass(property.getType());
        StorablePropertyAdapter adapter = property.getAppliedAdapter();
        if (mode != INITIAL_VERSION) {
            if (instanceVar == null) {
                b.loadThis();
            } else {
                b.loadLocal(instanceVar);
            }
        }
        TypeDesc fromType;
        if (adapter == null) {
            if (mode != INITIAL_VERSION) {
                if (instanceVar == null) {
                    b.loadField(property.getName(), propertyType);
                } else {
                    b.loadField(instanceVar.getType(), property.getName(), propertyType);
                }
            }
            fromType = propertyType;
        } else {
            Class toClass = psClass;
            if (java.sql.Blob.class.isAssignableFrom(toClass)) {
                toClass = com.amazon.carbonado.lob.Blob.class;
            } else if (java.sql.Clob.class.isAssignableFrom(toClass)) {
                toClass = com.amazon.carbonado.lob.Clob.class;
            }
            Method adaptMethod = adapter.findAdaptMethod(property.getType(), toClass);
            if (adaptMethod == null) {
                if (toClass == String.class) {
                    adaptMethod = adapter.findAdaptMethod(property.getType(), char.class);
                    if (adaptMethod == null) {
                        adaptMethod = adapter.findAdaptMethod(property.getType(), Character.class);
                    }
                }
                if (adaptMethod == null) {
                    throw new SupportException("Unable to adapt " + property.getType() + " to " + toClass.getName());
                }
            }
            TypeDesc adaptType = TypeDesc.forClass(adaptMethod.getReturnType());
            if (mode != INITIAL_VERSION) {
                String methodName = property.getReadMethodName() + '$';
                if (instanceVar == null) {
                    b.invokeVirtual(methodName, adaptType, null);
                } else {
                    b.invokeVirtual(instanceVar.getType(), methodName, adaptType, null);
                }
            }
            fromType = adaptType;
        }
        Label done = b.createLabel();
        if (mode == INITIAL_VERSION) {
            CodeBuilderUtil.initialVersion(b, fromType, 1);
        } else if (mode == INCREMENT_VERSION) {
            CodeBuilderUtil.incrementVersion(b, fromType);
        } else if (!fromType.isPrimitive() && mode != NOT_NULL) {
            b.dup();
            Label notNull = b.createLabel();
            b.ifNullBranch(notNull, false);
            b.pop();
            b.loadConstant(property.getDataType());
            b.invokeInterface(TypeDesc.forClass(PreparedStatement.class), "setNull", null, new TypeDesc[] { TypeDesc.INT, TypeDesc.INT });
            b.branch(done);
            notNull.setLocation();
        }
        if (Lob.class.isAssignableFrom(fromType.toClass())) {
            LocalVariable lobVar = b.createLocalVariable(null, fromType);
            b.storeLocal(lobVar);
            LocalVariable columnVar = b.createLocalVariable(null, TypeDesc.INT);
            b.storeLocal(columnVar);
            LocalVariable psVar = b.createLocalVariable("ps", TypeDesc.forClass(PreparedStatement.class));
            b.storeLocal(psVar);
            if (lobArrayVar != null && lobIndex != null) {
                b.loadLocal(lobArrayVar);
                b.loadConstant(lobIndex);
            }
            pushJDBCSupport(b);
            b.loadLocal(psVar);
            b.loadLocal(columnVar);
            b.loadLocal(lobVar);
            Method setValueMethod;
            try {
                String name = fromType.toClass().getName();
                name = "set" + name.substring(name.lastIndexOf('.') + 1) + "Value";
                setValueMethod = JDBCSupport.class.getMethod(name, PreparedStatement.class, int.class, fromType.toClass());
            } catch (NoSuchMethodException e) {
                throw new UndeclaredThrowableException(e);
            }
            b.invoke(setValueMethod);
            if (lobArrayVar == null || lobIndex == null) {
                b.pop();
            } else {
                b.storeToArray(TypeDesc.OBJECT);
            }
        } else {
            if (psType == TypeDesc.STRING && fromType.toPrimitiveType() == TypeDesc.CHAR) {
                b.convert(fromType, fromType.toPrimitiveType());
                b.invokeStatic(String.class.getName(), "valueOf", TypeDesc.STRING, new TypeDesc[] { TypeDesc.CHAR });
            } else {
                b.convert(fromType, psType);
            }
            b.invoke(property.getPreparedStatementSetMethod());
        }
        done.setLocation();
    }

    /**
     * Generates code which emulates this:
     *
     * ...
     * } finally {
     *     JDBCConnectionCapability.yieldConnection(con);
     * }
     *
     * @param capVar required reference to JDBCConnectionCapability
     * @param conVar optional connection variable
     * @param tryAfterCon label right after connection acquisition
     */
    private void yieldCon(CodeBuilder b, LocalVariable capVar, LocalVariable conVar, Label tryAfterCon) {
        Label endFinallyLabel = b.createLabel().setLocation();
        Label contLabel = b.createLabel();
        yieldConnection(b, capVar, conVar);
        b.branch(contLabel);
        b.exceptionHandler(tryAfterCon, endFinallyLabel, null);
        yieldConnection(b, capVar, conVar);
        b.throwObject();
        contLabel.setLocation();
    }

    /**
     * Generates code which emulates this:
     *
     * ...
     *     } finally {
     *         JDBCConnectionCapability.yieldConnection(con);
     *     }
     * } catch (RuntimeException e) {
     *     throw e;
     * } catch (Exception e) {
     *     throw JDBCConnectionCapability.toFetchException(e);
     * }
     *
     * @param capVar required reference to JDBCConnectionCapability
     * @param txnVar optional transaction variable to commit/exit
     * @param tryBeforeCon label right before connection acquisition
     * @param conVar optional connection variable
     * @param tryAfterCon label right after connection acquisition
     */
    private void yieldConAndHandleException(CodeBuilder b, LocalVariable capVar, Label tryBeforeCon, LocalVariable conVar, Label tryAfterCon, boolean forPersist) {
        Label endFinallyLabel = b.createLabel().setLocation();
        Label contLabel = b.createLabel();
        yieldConnection(b, capVar, conVar);
        b.branch(contLabel);
        b.exceptionHandler(tryAfterCon, endFinallyLabel, null);
        yieldConnection(b, capVar, conVar);
        b.throwObject();
        b.exceptionHandler(tryBeforeCon, b.createLabel().setLocation(), RuntimeException.class.getName());
        b.throwObject();
        b.exceptionHandler(tryBeforeCon, b.createLabel().setLocation(), Exception.class.getName());
        b.loadLocal(capVar);
        b.swap();
        TypeDesc[] params = { TypeDesc.forClass(Throwable.class) };
        if (forPersist) {
            b.invokeInterface(TypeDesc.forClass(JDBCConnectionCapability.class), "toPersistException", TypeDesc.forClass(PersistException.class), params);
        } else {
            b.invokeInterface(TypeDesc.forClass(JDBCConnectionCapability.class), "toFetchException", TypeDesc.forClass(FetchException.class), params);
        }
        b.throwObject();
        contLabel.setLocation();
    }

    /**
     * Generates code which emulates this:
     *
     * ...
     * } finally {
     *     statement.close();
     * }
     *
     * @param statementVar Statement variable
     * @param tryAfterStatement label right after Statement acquisition
     */
    private void closeStatement(CodeBuilder b, LocalVariable statementVar, Label tryAfterStatement) {
        Label contLabel = b.createLabel();
        Label endFinallyLabel = b.createLabel().setLocation();
        b.loadLocal(statementVar);
        b.invokeInterface(TypeDesc.forClass(Statement.class), "close", null, null);
        b.branch(contLabel);
        b.exceptionHandler(tryAfterStatement, endFinallyLabel, null);
        b.loadLocal(statementVar);
        b.invokeInterface(TypeDesc.forClass(Statement.class), "close", null, null);
        b.throwObject();
        contLabel.setLocation();
    }

    /**
     * Generates code which emulates this:
     *
     * ...
     * } finally {
     *     rs.close();
     * }
     *
     * @param rsVar ResultSet variable
     * @param tryAfterRs label right after ResultSet acquisition
     */
    private void closeResultSet(CodeBuilder b, LocalVariable rsVar, Label tryAfterRs) {
        Label contLabel = b.createLabel();
        Label endFinallyLabel = b.createLabel().setLocation();
        b.loadLocal(rsVar);
        b.invokeInterface(TypeDesc.forClass(ResultSet.class), "close", null, null);
        b.branch(contLabel);
        b.exceptionHandler(tryAfterRs, endFinallyLabel, null);
        b.loadLocal(rsVar);
        b.invokeInterface(TypeDesc.forClass(ResultSet.class), "close", null, null);
        b.throwObject();
        contLabel.setLocation();
    }

    /**
     * Generates code to branch if a property is dirty.
     *
     * @param propNumber property number from all properties map
     * @param target branch target
     * @param when true, branch if dirty; when false, branch when not dirty
     */
    private void branchIfDirty(CodeBuilder b, int propNumber, Label target, boolean branchIfDirty) {
        String stateFieldName = StorableGenerator.PROPERTY_STATE_FIELD_NAME + (propNumber >> 4);
        b.loadThis();
        b.loadField(stateFieldName, TypeDesc.INT);
        int shift = (propNumber & 0xf) * 2;
        b.loadConstant(StorableGenerator.PROPERTY_STATE_MASK << shift);
        b.math(Opcode.IAND);
        b.loadConstant(StorableGenerator.PROPERTY_STATE_DIRTY << shift);
        b.ifComparisonBranch(target, branchIfDirty ? "==" : "!=");
    }

    private void defineExtractAllMethod(Map<JDBCStorableProperty<S>, Class<?>> lobLoaderMap) throws SupportException {
        MethodInfo mi = mClassFile.addMethod(Modifiers.PRIVATE, EXTRACT_ALL_METHOD_NAME, null, new TypeDesc[] { TypeDesc.forClass(ResultSet.class), TypeDesc.INT });
        CodeBuilder b = new CodeBuilder(mi);
        defineExtract(b, b.getParameter(0), b.getParameter(1), null, mInfo.getPrimaryKeyProperties().values(), lobLoaderMap);
        b.loadThis();
        b.loadLocal(b.getParameter(0));
        b.loadLocal(b.getParameter(1));
        b.loadConstant(1);
        b.math(Opcode.IADD);
        b.loadNull();
        b.invokePrivate(EXTRACT_DATA_METHOD_NAME, null, new TypeDesc[] { TypeDesc.forClass(ResultSet.class), TypeDesc.INT, TypeDesc.forClass(Lob.class).toArrayType() });
        b.returnVoid();
    }

    private void defineExtractDataMethod(Map<JDBCStorableProperty<S>, Class<?>> lobLoaderMap) throws SupportException {
        MethodInfo mi = mClassFile.addMethod(Modifiers.PRIVATE, EXTRACT_DATA_METHOD_NAME, null, new TypeDesc[] { TypeDesc.forClass(ResultSet.class), TypeDesc.INT, TypeDesc.forClass(Lob.class).toArrayType() });
        CodeBuilder b = new CodeBuilder(mi);
        defineExtract(b, b.getParameter(0), b.getParameter(1), b.getParameter(2), mInfo.getDataProperties().values(), lobLoaderMap);
        b.returnVoid();
    }

    private void defineExtract(CodeBuilder b, LocalVariable rsVar, LocalVariable initialOffsetVar, LocalVariable lobArrayVar, Iterable<JDBCStorableProperty<S>> properties, Map<JDBCStorableProperty<S>, Class<?>> lobLoaderMap) throws SupportException {
        LocalVariable offsetVar = null;
        int lobIndex = 0;
        for (JDBCStorableProperty<S> property : properties) {
            if (!property.isSelectable()) {
                continue;
            }
            b.loadThis();
            b.loadLocal(rsVar);
            if (offsetVar == null) {
                offsetVar = initialOffsetVar;
            } else {
                b.integerIncrement(offsetVar, 1);
            }
            b.loadLocal(offsetVar);
            Method resultSetGetMethod = property.getResultSetGetMethod();
            b.invoke(resultSetGetMethod);
            TypeDesc resultSetType = TypeDesc.forClass(resultSetGetMethod.getReturnType());
            Label wasNull = b.createLabel();
            if (resultSetType.isPrimitive() && property.isColumnNullable()) {
                b.loadLocal(rsVar);
                b.invokeInterface(TypeDesc.forClass(ResultSet.class), "wasNull", TypeDesc.BOOLEAN, null);
                Label wasNotNull = b.createLabel();
                b.ifZeroComparisonBranch(wasNotNull, "==");
                if (resultSetType.isDoubleWord()) {
                    b.pop2();
                } else {
                    b.pop();
                }
                b.loadNull();
                b.branch(wasNull);
                wasNotNull.setLocation();
            }
            if (Lob.class.isAssignableFrom(property.getType()) || java.sql.Blob.class.isAssignableFrom(resultSetType.toClass()) || java.sql.Clob.class.isAssignableFrom(resultSetType.toClass())) {
                boolean isClob = com.amazon.carbonado.lob.Clob.class.isAssignableFrom(property.getType()) || java.sql.Clob.class.isAssignableFrom(resultSetType.toClass());
                String lobTypeName = isClob ? "Clob" : "Blob";
                Method convertMethod;
                try {
                    String loaderName = "com.amazon.carbonado.repo.jdbc.JDBC" + lobTypeName + "Loader";
                    convertMethod = JDBCSupport.class.getMethod("convert".concat(lobTypeName), resultSetType.toClass(), Class.forName(loaderName));
                } catch (ClassNotFoundException e) {
                    throw new UndeclaredThrowableException(e);
                } catch (NoSuchMethodException e) {
                    throw new UndeclaredThrowableException(e);
                }
                pushJDBCSupport(b);
                b.swap();
                TypeDesc lobLoaderType = TypeDesc.forClass(lobLoaderMap.get(property));
                b.newObject(lobLoaderType);
                b.dup();
                b.loadThis();
                b.invokeConstructor(lobLoaderType, new TypeDesc[] { mClassFile.getType() });
                b.invoke(convertMethod);
                resultSetType = TypeDesc.forClass(convertMethod.getReturnType());
                if (lobArrayVar != null) {
                    b.loadLocal(lobArrayVar);
                    Label noUpdateLob = b.createLabel();
                    b.ifNullBranch(noUpdateLob, true);
                    b.loadLocal(lobArrayVar);
                    b.loadConstant(lobIndex);
                    b.loadFromArray(TypeDesc.OBJECT);
                    b.ifNullBranch(noUpdateLob, true);
                    TypeDesc lobType = TypeDesc.forClass(convertMethod.getReturnType());
                    LocalVariable lob = b.createLocalVariable(null, lobType);
                    b.storeLocal(lob);
                    pushJDBCSupport(b);
                    b.loadLocal(lob);
                    b.loadLocal(lobArrayVar);
                    b.loadConstant(lobIndex);
                    b.loadFromArray(TypeDesc.OBJECT);
                    b.checkCast(lobType);
                    TypeDesc[] params = { lobType, lobType };
                    b.invokeInterface(TypeDesc.forClass(JDBCSupport.class), "update".concat(lobTypeName), null, params);
                    b.loadLocal(lob);
                    noUpdateLob.setLocation();
                    lobIndex++;
                }
            }
            TypeDesc superType = TypeDesc.forClass(mClassFile.getSuperClassName());
            StorablePropertyAdapter adapter = property.getAppliedAdapter();
            if (adapter == null) {
                TypeDesc propertyType = TypeDesc.forClass(property.getType());
                convertFromResultSet(b, property, resultSetType, propertyType);
                wasNull.setLocation();
                b.storeField(superType, property.getName(), propertyType);
            } else {
                Method adaptMethod = adapter.findAdaptMethod(resultSetType.toClass(), property.getType());
                if (adaptMethod == null) {
                    if (resultSetType == TypeDesc.STRING) {
                        adaptMethod = adapter.findAdaptMethod(char.class, property.getType());
                        if (adaptMethod == null) {
                            adaptMethod = adapter.findAdaptMethod(Character.class, property.getType());
                        }
                    }
                    if (adaptMethod == null) {
                        throw new SupportException("Unable to adapt " + resultSetType.toClass().getName() + " to " + property.getType());
                    }
                }
                TypeDesc adaptType = TypeDesc.forClass(adaptMethod.getParameterTypes()[0]);
                convertFromResultSet(b, property, resultSetType, adaptType);
                wasNull.setLocation();
                b.invokeVirtual(superType, property.getWriteMethodName() + '$', null, new TypeDesc[] { adaptType });
            }
        }
    }

    private void convertFromResultSet(CodeBuilder b, JDBCStorableProperty<S> property, TypeDesc resultSetType, TypeDesc toType) {
        if (resultSetType == TypeDesc.STRING && toType.toPrimitiveType() == TypeDesc.CHAR) {
            Label charWasNull = null;
            if (property.isNullable()) {
                charWasNull = b.createLabel();
                LocalVariable temp = b.createLocalVariable(null, resultSetType);
                b.storeLocal(temp);
                b.loadLocal(temp);
                b.ifNullBranch(charWasNull, true);
                b.loadLocal(temp);
            }
            b.loadConstant(0);
            b.invokeVirtual(String.class.getName(), "charAt", TypeDesc.CHAR, new TypeDesc[] { TypeDesc.INT });
            b.convert(TypeDesc.CHAR, toType);
            if (charWasNull != null) {
                Label skipNull = b.createLabel();
                b.branch(skipNull);
                charWasNull.setLocation();
                b.loadNull();
                skipNull.setLocation();
            }
        } else {
            b.convert(resultSetType, toType);
        }
    }

    private Map<JDBCStorableProperty<S>, Class<?>> generateLobLoaders() throws SupportException {
        Map<JDBCStorableProperty<S>, Class<?>> lobLoaderMap = new IdentityHashMap<JDBCStorableProperty<S>, Class<?>>();
        for (JDBCStorableProperty<S> property : mAllProperties.values()) {
            if (!property.isSelectable() || property.isVersion()) {
                continue;
            }
            Class psClass = property.getPreparedStatementSetMethod().getParameterTypes()[1];
            Class<?> lobLoader;
            if (com.amazon.carbonado.lob.Blob.class.isAssignableFrom(property.getType()) || java.sql.Blob.class.isAssignableFrom(psClass)) {
                lobLoader = generateLobLoader(property, JDBCBlobLoader.class);
            } else if (com.amazon.carbonado.lob.Clob.class.isAssignableFrom(property.getType()) || java.sql.Clob.class.isAssignableFrom(psClass)) {
                lobLoader = generateLobLoader(property, JDBCClobLoader.class);
            } else {
                continue;
            }
            lobLoaderMap.put(property, lobLoader);
        }
        return lobLoaderMap;
    }

    /**
     * Generates an inner class conforming to JDBCBlobLoader or JDBCClobLoader.
     *
     * @param loaderType either JDBCBlobLoader or JDBCClobLoader
     */
    private Class<?> generateLobLoader(JDBCStorableProperty<S> property, Class<?> loaderType) throws SupportException {
        ClassInjector ci = ClassInjector.create(property.getEnclosingType().getName(), mParentClassLoader);
        ClassFile cf = new ClassFile(ci.getClassName());
        cf.markSynthetic();
        cf.setSourceFile(JDBCStorableGenerator.class.getName());
        cf.setTarget("1.5");
        cf.addInterface(loaderType);
        boolean isClob = loaderType == JDBCClobLoader.class;
        final TypeDesc capType = TypeDesc.forClass(JDBCConnectionCapability.class);
        final TypeDesc resultSetType = TypeDesc.forClass(ResultSet.class);
        final TypeDesc preparedStatementType = TypeDesc.forClass(PreparedStatement.class);
        final TypeDesc sqlLobType = TypeDesc.forClass(isClob ? java.sql.Clob.class : java.sql.Blob.class);
        final String enclosingFieldName = "enclosing";
        final TypeDesc enclosingType = mClassFile.getType();
        cf.addField(Modifiers.PRIVATE, enclosingFieldName, enclosingType);
        {
            MethodInfo mi = cf.addConstructor(Modifiers.PUBLIC, new TypeDesc[] { enclosingType });
            CodeBuilder b = new CodeBuilder(mi);
            b.loadThis();
            b.invokeSuperConstructor(null);
            b.loadThis();
            b.loadLocal(b.getParameter(0));
            b.storeField(enclosingFieldName, enclosingType);
            b.returnVoid();
        }
        MethodInfo mi = cf.addMethod(Modifiers.PUBLIC, "load", sqlLobType, new TypeDesc[] { capType });
        mi.addException(TypeDesc.forClass(FetchException.class));
        CodeBuilder b = new CodeBuilder(mi);
        LocalVariable capVar = b.getParameter(0);
        Label tryBeforeCon = b.createLabel().setLocation();
        LocalVariable conVar = getConnection(b, capVar);
        Label tryAfterCon = b.createLabel().setLocation();
        StringBuilder selectBuilder = new StringBuilder();
        selectBuilder.append("SELECT ");
        selectBuilder.append(property.getColumnName());
        selectBuilder.append(" FROM ");
        selectBuilder.append(mInfo.getQualifiedTableName());
        LocalVariable psVar = b.createLocalVariable("ps", preparedStatementType);
        LocalVariable instanceVar = b.createLocalVariable(null, enclosingType);
        b.loadThis();
        b.loadField(enclosingFieldName, enclosingType);
        b.storeLocal(instanceVar);
        Label tryAfterPs = buildWhereClauseAndPreparedStatement(b, selectBuilder, conVar, psVar, capVar, instanceVar);
        b.loadLocal(psVar);
        b.invokeInterface(preparedStatementType, "executeQuery", resultSetType, null);
        LocalVariable rsVar = b.createLocalVariable("rs", resultSetType);
        b.storeLocal(rsVar);
        Label tryAfterRs = b.createLabel().setLocation();
        LocalVariable resultVar = b.createLocalVariable(null, sqlLobType);
        b.loadNull();
        b.storeLocal(resultVar);
        b.loadLocal(rsVar);
        b.invokeInterface(resultSetType, "next", TypeDesc.BOOLEAN, null);
        Label noResults = b.createLabel();
        b.ifZeroComparisonBranch(noResults, "==");
        b.loadLocal(rsVar);
        b.loadConstant(1);
        b.invokeInterface(resultSetType, isClob ? "getClob" : "getBlob", sqlLobType, new TypeDesc[] { TypeDesc.INT });
        b.storeLocal(resultVar);
        noResults.setLocation();
        closeResultSet(b, rsVar, tryAfterRs);
        closeStatement(b, psVar, tryAfterPs);
        yieldConAndHandleException(b, capVar, tryBeforeCon, conVar, tryAfterCon, false);
        b.loadLocal(resultVar);
        b.returnValue(sqlLobType);
        return ci.defineClass(cf);
    }
}
