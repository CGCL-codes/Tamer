package com.versant.core.jdbc.metadata;

import com.versant.core.metadata.parser.JdoElement;
import com.versant.core.metadata.parser.JdoExtension;
import com.versant.core.metadata.parser.JdoExtensionKeys;
import com.versant.core.metadata.*;
import com.versant.core.common.OID;
import com.versant.core.common.State;
import com.versant.core.jdbc.*;
import com.versant.core.jdbc.query.JdbcJDOQLCompiler;
import com.versant.core.jdbc.sql.exp.*;
import com.versant.core.server.PersistGraph;
import com.versant.core.server.StateContainer;
import com.versant.core.common.*;
import com.versant.core.util.CharBuf;
import com.versant.core.jdo.query.Node;
import com.versant.core.jdo.query.VarNode;
import com.versant.core.jdo.query.VarNodeIF;
import com.versant.core.common.Debug;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.versant.core.common.BindingSupportImpl;

/**
 * A field that is a Collection or array of a PC class stored using a
 * foreign key in the value class.
 */
public class JdbcFKCollectionField extends JdbcCollectionField {

    /**
     * This is the 'foreign key' field in the value class.
     */
    public JdbcRefField fkField;

    /**
     * This is the JdbcClass for the elements.
     */
    private JdbcClass elementJdbcClass;

    private static final int WINDOW_SIZE = 20;

    private static final float FUDGE_FACTOR = 1.5f;

    private static final int MIN_LEN = 4;

    protected transient int fetchCount;

    protected transient float avgRowCount;

    protected transient int expansionCount;

    public void dump(PrintStream out, String indent) {
        super.dump(out, indent);
        String is = indent + "  ";
        out.println(is + "fkField " + fkField);
    }

    /**
     * Complete the meta data for this collection. This must use info
     * already supplied in the .jdo file and add anything else needed.
     */
    public void processMetaData(JdoElement context, JdbcMetaDataBuilder mdb, boolean quiet) {
        ClassMetaData ecmd = fmd.elementTypeMetaData;
        elementJdbcClass = (JdbcClass) ecmd.storeClass;
        if (elementJdbcClass == null) {
            throw BindingSupportImpl.getInstance().runtime("The inverse extension may only be used for " + "collections of PC instances stored by JDBC\n" + context.getContext());
        }
        ClassMetaData cmd = fmd.classMetaData;
        super.processMetaData(context, mdb, quiet);
        useJoin = JdbcField.USE_JOIN_INNER;
        if (fmd.category != MDStatics.CATEGORY_ARRAY) {
            fmd.managed = mdb.getJdbcConfig().managedOneToMany;
        } else {
            fmd.managed = false;
        }
        JdoExtension[] a;
        if (fmd.category == MDStatics.CATEGORY_ARRAY) {
            a = fmd.jdoArray.extensions;
        } else if (fmd.category == MDStatics.CATEGORY_COLLECTION) {
            a = fmd.jdoCollection.extensions;
        } else {
            throw BindingSupportImpl.getInstance().internal("Category '" + MDStaticUtils.toCategoryString(fmd.category) + "' is not supported for FK Collections");
        }
        int len = a.length;
        for (int i = 0; i < len; i++) {
            JdoExtension e = a[i];
            switch(e.key) {
                case JdoExtensionKeys.MANAGED:
                    if (fmd.category == MDStatics.CATEGORY_ARRAY && e.getBoolean()) {
                        throw BindingSupportImpl.getInstance().invalidOperation("The managed option is not supported for arrays: " + fmd.name);
                    }
                    fmd.managed = e.getBoolean();
                    break;
                case JdoExtensionKeys.INVERSE:
                case JdoExtensionKeys.JDBC_LINK_FOREIGN_KEY:
                    String fname = e.getString();
                    FieldMetaData f = ecmd.getFieldMetaData(fname);
                    if (f == null) {
                        f = createFakeFKBackRef(cmd, ecmd, mdb, e, quiet);
                    }
                    if (f.isEmbeddedRef()) {
                        throw BindingSupportImpl.getInstance().invalidOperation("an Inverse field may not be Embedded");
                    }
                    if (f.storeField == null) {
                        throw BindingSupportImpl.getInstance().runtime("Field '" + fname + "' is not persistent\n" + context.getContext());
                    }
                    if (!(f.storeField instanceof JdbcRefField)) {
                        throw BindingSupportImpl.getInstance().runtime("Field '" + fname + "' is not a reference\n" + context.getContext());
                    }
                    fkField = (JdbcRefField) f.storeField;
                    if (!cmd.isAncestorOrSelf(fkField.targetClass)) {
                        throw BindingSupportImpl.getInstance().runtime("Field '" + fname + "' references " + fkField.targetClass + " and not our class\n" + context.getContext());
                    }
                    fmd.ordered = false;
                    createIndex(mdb, ecmd, e.nested);
                    break;
                default:
                    if (e.isJdbc()) {
                        throw BindingSupportImpl.getInstance().runtime("Unexpected extension: " + e + "\n" + e.getContext());
                    }
            }
        }
        if (fkField == null) {
            throw BindingSupportImpl.getInstance().internal("fkField is null");
        }
        fkField.masterCollectionField = this;
        ourPkColumns = fkField.cols;
    }

    private FieldMetaData createFakeFKBackRef(ClassMetaData cmd, ClassMetaData ecmd, JdbcMetaDataBuilder mdb, JdoExtension e, boolean quiet) {
        fmd.managed = false;
        FieldMetaData f = new FieldMetaData();
        f.fake = true;
        f.typeMetaData = cmd;
        f.name = cmd.getShortName() + "_" + fmd.name;
        f.category = MDStatics.CATEGORY_REF;
        f.ordered = false;
        f.managed = false;
        f.primaryField = true;
        JdbcRefField jdbcRefField = new JdbcRefField();
        jdbcRefField.targetClass = cmd;
        fkField = jdbcRefField;
        f.classMetaData = ecmd;
        jdbcRefField.fmd = f;
        f.storeField = jdbcRefField;
        jdbcRefField.fake = true;
        f.type = cmd.cls;
        f.inverseFieldMetaData = fmd;
        mdb.processRefFieldImpl(elementJdbcClass, jdbcRefField, f, e, e.nested, quiet);
        mdb.getClassInfo(ecmd).elements.add(f.storeField);
        return f;
    }

    /**
     * Create an index on our pkField unless this has been disabled.
     */
    private void createIndex(JdbcMetaDataBuilder mdb, ClassMetaData refCmd, JdoExtension[] nested) {
        JdbcClass refJdbcClass = (JdbcClass) refCmd.storeClass;
        JdbcIndex idx = null;
        boolean doNotCreateIndex = false;
        int n = nested == null ? 0 : nested.length;
        for (int i = 0; i < n; i++) {
            JdoExtension e = nested[i];
            switch(e.key) {
                case JdoExtensionKeys.JDBC_INDEX:
                    if (idx != null) {
                        throw BindingSupportImpl.getInstance().runtime("Only one jdbc-index extension is allowed here\n" + e.getContext());
                    }
                    if (e.isNoValue()) {
                        doNotCreateIndex = true;
                        break;
                    }
                    idx = new JdbcIndex();
                    idx.name = e.value;
                    break;
                case JdoExtensionKeys.JDBC_COLUMN:
                case JdoExtensionKeys.JDBC_USE_JOIN:
                case JdoExtensionKeys.JDBC_CONSTRAINT:
                case JdoExtensionKeys.JDBC_REF:
                    break;
                default:
                    if (e.isJdbc()) {
                        MetaDataBuilder.throwUnexpectedExtension(e);
                    }
            }
        }
        if (doNotCreateIndex) return;
        if (idx == null) idx = new JdbcIndex();
        if (fkField.cols != null) {
            idx.setCols(fkField.cols);
        }
        if (idx.name != null) {
            try {
                mdb.getNameGenerator().addIndexName(refJdbcClass.table.name, idx.name);
            } catch (IllegalArgumentException x) {
                throw BindingSupportImpl.getInstance().runtime(x.getMessage(), x);
            }
        }
        mdb.getClassInfo(refCmd).autoIndexes.add(idx);
    }

    /**
     * Persist pass 2 field for a block of graph entries all with
     * the same class. The same ps'es can be used for all entries in the block.
     */
    public void persistPass2Block(PersistGraph graph, int blockStart, int blockEnd, CharBuf s, Connection con, boolean batchInserts, boolean batchUpdates) throws SQLException {
    }

    /**
     * Fetch the values for this field.
     */
    public int fetch(JdbcStorageManager sm, OID oid, State state, FetchGroupField field, boolean forUpdate, StateContainer container, boolean fetchPass2Fields, ColFieldHolder colFHolder) throws SQLException {
        String sql = forUpdate ? field.jdbcSelectSqlForUpdate : field.jdbcSelectSql;
        final boolean joined = field.jdbcUseJoin != JdbcField.USE_JOIN_NO;
        FetchGroup nextFetchGroup = field.nextFetchGroup;
        FgDs[] fgDses = new FgDs[1];
        if (sql == null) {
            SelectExp se = getSelectExp(field, sm, fgDses);
            CharBuf s = sm.generateSql(se);
            sql = s.toString();
            if (forUpdate) {
                field.jdbcSelectSqlForUpdate = sql;
            } else {
                field.jdbcSelectSql = sql;
            }
        } else {
            fgDses[0] = ((JdbcFetchGroup) nextFetchGroup.storeFetchGroup).getExistingFgDs(true, false);
        }
        if (colFHolder != null && fgDses[0] != null) {
            colFHolder.valueJs = fgDses[0].getJoinStruct();
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        Struct s = new Struct();
        try {
            ps = sm.con().prepareStatement(sql);
            ((JdbcOID) oid).setParams(ps, 1);
            try {
                rs = ps.executeQuery();
            } catch (Exception e) {
                throw mapException(e, "Fetch inverse foreign key collection failed: " + JdbcUtils.toString(e) + "\n" + "Field: " + fmd.getTypeQName() + "\n" + "Instance: " + oid.toSString() + "\n" + JdbcUtils.getPreparedStatementInfo(sql, ps));
            }
            s.init();
            ClassMetaData valueCmd = fmd.elementTypeMetaData;
            int valuePkLen = 0;
            if (joined) valuePkLen = elementJdbcClass.table.pkSimpleColumnCount;
            for (; rs.next(); ) {
                OID valueOid = valueCmd.createOID(false);
                boolean isNull = !((JdbcOID) valueOid).copyKeyFields(rs, 1);
                if (isNull) {
                    s.add(null);
                } else {
                    s.add(valueOid);
                }
                if (!isNull && joined && container.isStateRequired(valueOid, nextFetchGroup)) {
                    State valueState = sm.createStateImp(rs, valueOid, nextFetchGroup, forUpdate, 1 + valuePkLen, null, true, container, fgDses[0], fetchPass2Fields, false, null);
                    container.addState(valueOid, valueState);
                }
            }
            updateState(s, state);
            updateStats(s.size);
        } finally {
            cleanup(rs);
            cleanup(ps);
        }
        return s.size;
    }

    public int fetchFrom(ResultSet rs, OID oid, State state, FetchGroupField field, boolean forUpdate, StateContainer container, boolean fetchPass2Fields, int colIndex, FetchInfo fetchInfo, JdbcStorageManager sm) throws SQLException {
        Struct s = new Struct();
        final boolean joined = field.jdbcUseJoin != JdbcField.USE_JOIN_NO;
        boolean first = true;
        s.init();
        ClassMetaData valueCmd = fmd.elementTypeMetaData;
        FetchGroup nextFetchGroup = field.nextFetchGroup;
        int valuePkLen = 0;
        if (joined) valuePkLen = elementJdbcClass.table.pkSimpleColumnCount;
        for (; ; ) {
            boolean mustBreak = false;
            if (first) {
                first = false;
                mustBreak = updateForFirstRow(fetchInfo, mustBreak, rs, colIndex, oid);
            } else {
                if (rs.next()) {
                    mustBreak = checkKeyOid(rs, colIndex, fetchInfo, mustBreak, oid);
                    fetchInfo.onNextRow = true;
                } else {
                    fetchInfo.onNextRow = false;
                    fetchInfo.finished = true;
                    mustBreak = true;
                }
            }
            if (mustBreak) break;
            OID valueOid = valueCmd.createOID(false);
            boolean isNull = !((JdbcOID) valueOid).copyKeyFields(rs, colIndex + ourPkColumns.length);
            if (!isNull) {
                s.add(valueOid);
                if (joined && container.isStateRequired(valueOid, nextFetchGroup)) {
                    State valueState = sm.createStateImp(rs, valueOid, nextFetchGroup, forUpdate, colIndex + ourPkColumns.length + valuePkLen, null, true, container, ((JdbcFetchGroup) nextFetchGroup.storeFetchGroup).getExistingFgDs(true, true), fetchPass2Fields, false, null);
                    container.addState(valueOid, valueState);
                }
            } else {
                break;
            }
        }
        updateState(s, state);
        updateStats(s.size);
        return s.size;
    }

    private void updateStats(int size) {
        int fc = ++fetchCount;
        if (fc > WINDOW_SIZE) {
            fc = WINDOW_SIZE;
        } else if (fc == 1) {
            avgRowCount = size;
        } else if (fc < 0) {
            fc = fetchCount = WINDOW_SIZE;
        } else {
            avgRowCount = (avgRowCount * (fc - 1) + size) / fc;
        }
    }

    public int fetchWithFilter(JdbcStorageManager sm, StateContainer oidStates, FetchGroupField field, ResultSet rs, boolean forUpdate, OID oidToCheckOn, OID[] lastReadStateOID, ClassMetaData cmd, ColFieldHolder colFHolder) throws SQLException {
        ClassMetaData valueCmd = fmd.elementTypeMetaData;
        final boolean joined = field.jdbcUseJoin != JdbcField.USE_JOIN_NO;
        FetchGroup nextFetchGroup = field.nextFetchGroup;
        if (colFHolder != null) {
            colFHolder.valueJs = new JoinStructure(nextFetchGroup);
        }
        int valuePkLen = 0;
        if (joined) valuePkLen = elementJdbcClass.table.pkSimpleColumnCount;
        int rootOIDLenght = ((JdbcClass) cmd.storeClass).table.pkSimpleColumnCount;
        int stateOIDPKLen = ((JdbcClass) fmd.classMetaData.storeClass).table.pkSimpleColumnCount;
        OID rootOid = cmd.createOID(false);
        OID prevRootOid = cmd.createOID(false);
        OID tmpOID = null;
        OID stateOID = fmd.classMetaData.createOID(false);
        OID prevStateOID = fmd.classMetaData.createOID(false);
        OID tmpStateOID = null;
        Struct s = new Struct();
        s.init();
        boolean currentRowValid = false;
        boolean prevRowValid = false;
        int returnState = 0;
        FgDs fgDs = ((JdbcFetchGroup) nextFetchGroup.storeFetchGroup).getExistingFgDs(true, field.jdbcUseJoin == JdbcField.USE_JOIN_OUTER);
        if (colFHolder != null) {
            colFHolder.valueJs = fgDs.getJoinStruct();
        }
        if (lastReadStateOID[0] != null) {
            int index = 1;
            prevRootOid = lastReadStateOID[0];
            index += rootOIDLenght;
            stateOID = lastReadStateOID[1];
            index += stateOIDPKLen;
            currentRowValid = oidStates.containsKey(stateOID);
            if (currentRowValid) {
                returnState |= STATUS_VALID_ROWS;
                OID valueOid = valueCmd.createOID(false);
                boolean isNull = !((JdbcOID) valueOid).copyKeyFields(rs, index);
                index += valuePkLen;
                if (!isNull) {
                    s.add(valueOid);
                } else {
                    s.add(null);
                }
                if (!isNull && joined && oidStates.isStateRequired(valueOid, nextFetchGroup)) {
                    State valueState = sm.createStateImp(rs, valueOid, nextFetchGroup, forUpdate, index, null, true, oidStates, fgDs, false, false, null);
                    oidStates.addState(valueOid, valueState);
                }
            }
            tmpOID = rootOid;
            rootOid = prevRootOid;
            prevRootOid = tmpOID;
            tmpStateOID = stateOID;
            stateOID = prevStateOID;
            prevStateOID = tmpStateOID;
            prevRowValid = currentRowValid;
        }
        for (; rs.next(); ) {
            int index = 1;
            ((JdbcOID) rootOid).copyKeyFields(rs, index);
            index += rootOIDLenght;
            ((JdbcOID) stateOID).copyKeyFields(rs, index);
            index += stateOIDPKLen;
            currentRowValid = oidStates.containsKey(stateOID);
            if (!stateOID.equals(prevStateOID) && prevRowValid) {
                if (updateStateFilter(s, oidStates.get(prevStateOID))) {
                    returnState |= STATUS_DATA_ADDED;
                }
                updateStatistics(s.size);
                if (oidToCheckOn.equals(prevRootOid) && !oidToCheckOn.equals(rootOid)) {
                    lastReadStateOID[0] = rootOid;
                    lastReadStateOID[1] = stateOID;
                    returnState |= STATUS_VALID_ROWS;
                    return returnState;
                }
                s.init();
            }
            if (currentRowValid) {
                returnState |= STATUS_VALID_ROWS;
                OID valueOid = valueCmd.createOID(false);
                boolean isNull = !((JdbcOID) valueOid).copyKeyFields(rs, index);
                if (!isNull) {
                    s.add(valueOid);
                } else {
                    s.add(null);
                }
                index += valuePkLen;
                if (!isNull && joined && oidStates.isStateRequired(valueOid, nextFetchGroup)) {
                    State valueState = sm.createStateImp(rs, valueOid, nextFetchGroup, forUpdate, index, null, true, oidStates, ((JdbcFetchGroup) nextFetchGroup.storeFetchGroup).getFgDs(true, field.jdbcUseJoin == JdbcField.USE_JOIN_OUTER), false, false, null);
                    oidStates.addState(valueOid, valueState);
                }
            }
            tmpOID = rootOid;
            rootOid = prevRootOid;
            prevRootOid = tmpOID;
            tmpStateOID = stateOID;
            stateOID = prevStateOID;
            prevStateOID = tmpStateOID;
            prevRowValid = currentRowValid;
        }
        rs.close();
        if ((returnState & STATUS_VALID_ROWS) == STATUS_VALID_ROWS) {
            if (updateStateFilter(s, oidStates.get(prevStateOID))) {
                returnState |= STATUS_DATA_ADDED;
            }
        }
        returnState |= STATUS_CLOSED;
        return returnState;
    }

    public void fillStateWithEmpty(FetchGroupField field, State state) {
        if (!state.containsField(fmd.stateFieldNo)) {
            state.setInternalObjectField(fmd.stateFieldNo, PRE_GEN_EMPTY_OBJECT_ARRAY);
        }
    }

    private boolean updateState(Struct s, State state) {
        if (state == null) return false;
        if (s.values == null) s.values = EMPTY_OID_ARRAY;
        s.trim();
        state.setInternalObjectField(fmd.stateFieldNo, s.values);
        return true;
    }

    private boolean updateStateFilter(Struct s, State state) {
        if (state == null) return false;
        if (s.values == null) s.values = EMPTY_OID_ARRAY;
        if (state.getInternalObjectField(fmd.stateFieldNo) == PRE_GEN_EMPTY_OBJECT_ARRAY) {
            s.trim();
            state.setInternalObjectField(fmd.stateFieldNo, s.values);
            return true;
        }
        return false;
    }

    public void appendOrderExpForFilterExp(SelectExp se, SelectExp root) {
        if (fmd.elementTypeMetaData != null) {
            root.appendOrderByForColumns(((JdbcClass) fmd.elementTypeMetaData.storeClass).table.pk, se);
        }
    }

    private class Struct {

        public int len;

        public OID[] values;

        public int size;

        public OID prevRootOID;

        public OID currentRootOID;

        public OID prevStateOID;

        public OID currentStateOID;

        public void init() {
            len = (int) (avgRowCount * FUDGE_FACTOR);
            if (len < MIN_LEN) len = 0;
            values = len == 0 ? null : new OID[len];
            if (Debug.DEBUG) {
                if (((fetchCount + 1) % 10) == 0) {
                    System.out.println("JdbcFkCollectionField.fetch" + " avgRowCount = " + avgRowCount + " " + " len = " + len + " " + " expansionCount = " + expansionCount + " " + " fetchCount = " + fetchCount);
                }
            }
            size = 0;
        }

        private void add(OID value) {
            if (size == len) {
                if (len == 0) {
                    values = new OID[len = MIN_LEN];
                } else {
                    len = len * 3 / 2 + 1;
                    OID[] a = new OID[len];
                    System.arraycopy(values, 0, a, 0, size);
                    values = a;
                    expansionCount++;
                }
            }
            values[size++] = value;
        }

        /**
         * Trim values down to size elements.
         */
        public void trim() {
            if (values.length == size) return;
            OID[] a = new OID[size];
            System.arraycopy(values, 0, a, 0, size);
            values = a;
        }
    }

    /**
     * Update statistics. This is not thread safe but it is not a
     * problem if the avgRowCount is a bit out sometimes.
     */
    private void updateStatistics(int size) {
        int fc = ++fetchCount;
        if (fc > WINDOW_SIZE) {
            fc = WINDOW_SIZE;
        } else if (fc == 1) {
            avgRowCount = size;
        } else if (fc < 0) {
            fc = fetchCount = WINDOW_SIZE;
        } else {
            avgRowCount = (avgRowCount * (fc - 1) + size) / fc;
        }
    }

    /**
     * Get a SelectExp to select all the rows in this collection using the
     * supplied fetch group field to control joins and so on.
     */
    private SelectExp getSelectExp(FetchGroupField field, JdbcStorageManager sm, FgDs[] fgDses) {
        SelectExp root = new SelectExp();
        root.table = elementJdbcClass.table;
        root.selectList = JdbcColumn.toSqlExp(root.table.pk, root);
        if (field.jdbcUseJoin != JdbcField.USE_JOIN_NO) {
            sm.addSelectFetchGroup(root, field.nextFetchGroup, true, fgDses[0] = ((JdbcFetchGroup) field.nextFetchGroup.storeFetchGroup).getFgDs(true, false), root, root.table.pk, this);
        }
        SelectExp se = root.findTable(ourPkColumns[0].table);
        if (se == null) {
            se = new SelectExp();
            se.table = ourPkColumns[0].table;
            root.addJoin(root.table.pk, se.table.pk, se);
        }
        root.whereExp = JdbcColumn.createEqualsParamExp(ourPkColumns, se);
        if (((JdbcClass) fmd.elementTypeMetaData.storeClass).classIdCol != null) {
            root.whereExp.next = ((JdbcClass) fmd.elementTypeMetaData.storeClass).getCheckClassIdExp(root);
            AndExp andExp = new AndExp(root.whereExp);
            root.whereExp = andExp;
        }
        if (fmd.ordering != null) {
            root.addOrderBy(fmd.ordering, false);
        }
        if (Debug.DEBUG) {
            System.out.println("%%% JdbcFKCollectionField.getSelectExp: " + fmd.getQName());
            root.dump("  ");
            System.out.println("%%%");
        }
        return root;
    }

    /**
     * Get a SelectExp to select all the rows in this collection using the
     * supplied fetch group field to control joins and so on.
     */
    public SelectExp getSelectExpFrom(JdbcStorageManager sm, SelectExp joinToExp, FetchGroupField field, FgDs owningFgDs) {
        SelectExp root = new SelectExp();
        root.outer = true;
        root.table = elementJdbcClass.table;
        root.selectList = JdbcColumn.toSqlExp(ourPkColumns, root, JdbcColumn.toSqlExp(root.table.pk, root));
        if (field.jdbcUseJoin != JdbcField.USE_JOIN_NO) {
            FgDs fgDs = ((JdbcFetchGroup) field.nextFetchGroup.storeFetchGroup).getFgDs(true, true);
            sm.addSelectFetchGroup(root, field.nextFetchGroup, true, fgDs, root, root.table.pk, this);
            owningFgDs.valueJs = fgDs.getJoinStruct();
        }
        if (fmd.ordering != null) {
            root.addOrderBy(fmd.ordering, false);
        }
        if (Debug.DEBUG) {
            System.out.println("%%% JdbcFKCollectionField.getSelectExp: " + fmd.getQName());
            root.dump("  ");
            System.out.println("%%%");
        }
        joinToExp.addJoin(joinToExp.table.pk, ourPkColumns, root);
        joinToExp.appendOrderByExp(root.orderByList);
        root.orderByList = null;
        return root;
    }

    public SelectExp getSelectFilterExp(JdbcStorageManager sm, FetchGroupField field, ColFieldHolder colFHolder) {
        SelectExp root = new SelectExp();
        root.table = elementJdbcClass.table;
        root.selectList = JdbcColumn.toSqlExp(root.table.pk, root);
        root.whereExp = ((JdbcClass) fmd.elementTypeMetaData.storeClass).getCheckClassIdExp(root);
        SqlExp e = JdbcColumn.toSqlExp(ourPkColumns, root, root.selectList);
        root.selectList = e;
        root.appendOrderByForColumns(ourPkColumns);
        if (field.jdbcUseJoin != JdbcField.USE_JOIN_NO) {
            FgDs fgDs = ((JdbcFetchGroup) field.nextFetchGroup.storeFetchGroup).getFgDs(true, field.jdbcUseJoin == JdbcField.USE_JOIN_OUTER);
            colFHolder.valueJs = fgDs.getJoinStruct();
            sm.addSelectFetchGroup(root, field.nextFetchGroup, true, fgDs, false);
        }
        if (fmd.ordering != null) {
            root.addOrderBy(fmd.ordering, true);
        }
        return root;
    }

    public SelectExp getSelectFilterJoinExp(boolean value, SelectExp lhSe, SelectExp rootSe, boolean addRootJoin) {
        SelectExp root = new SelectExp();
        root.table = elementJdbcClass.table;
        lhSe.addJoin(lhSe.table.pk, ourPkColumns, root);
        return root;
    }

    /**
     * Convert this field into an isEmpty expression.
     */
    public SqlExp toIsEmptySqlExp(JdbcJDOQLCompiler comp, SelectExp root) {
        SelectExp se = new SelectExp();
        se.table = elementJdbcClass.table;
        se.jdbcField = this;
        se.subSelectJoinExp = root.createJoinExp(root.table.pk, ourPkColumns, se);
        return new UnaryOpExp(new ExistsExp(se, true), UnaryOpExp.OP_NOT);
    }

    /**
     * Convert this field into a contains expression.
     */
    public SqlExp toContainsSqlExp(JdbcJDOQLCompiler comp, SelectExp root, Node args) {
        if (args instanceof VarNodeIF) {
            VarNode v = ((VarNodeIF) args).getVarNode();
            SelectExp vse = (SelectExp) v.getStoreExtent();
            if (vse.table == ourPkColumns[0].table) {
                vse.subSelectJoinExp = root.createJoinExp(root.table.pk, ourPkColumns, vse);
            } else {
                SelectExp se = new SelectExp();
                se.table = ourPkColumns[0].table;
                se.outer = vse.outer;
                vse.addJoin(vse.table.pk, ourPkColumns[0].table.pk, se);
            }
            if (v.getCmd() != fmd.elementTypeMetaData) {
                vse.whereExp = SelectExp.appendWithAnd(vse.whereExp, ((JdbcClass) fmd.elementTypeMetaData.storeClass).getCheckClassIdExp(vse));
            }
            return new ExistsExp(vse, true, v);
        } else {
            SelectExp se = new SelectExp();
            se.table = elementJdbcClass.table;
            se.jdbcField = this;
            se.subSelectJoinExp = root.createJoinExp(root.table.pk, ourPkColumns, se);
            SqlExp left = JdbcColumn.toSqlExp(se.table.pkSimpleCols, se);
            for (SqlExp e = left; e != null; e = e.next) {
                ((ColumnExp) e).cmd = fmd.elementTypeMetaData;
            }
            SqlExp right = comp.getVisitor().toSqlExp(args, root, left, 0, null);
            if (left.next == null && right.next == null) {
                BinaryOpExp ans = new BinaryOpExp(left, BinaryOpExp.EQUAL, right);
                if (right instanceof ParamExp) {
                    ParamExp p = (ParamExp) right;
                    p.usage.expList = ans;
                    p.usage.expCount = 1;
                }
                se.whereExp = ans;
            } else {
                throw BindingSupportImpl.getInstance().internal("not implemented");
            }
            return new ExistsExp(se, false);
        }
    }
}
