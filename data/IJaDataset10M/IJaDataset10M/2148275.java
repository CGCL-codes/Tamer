package org.riverock.dbrevision.db.definition;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.riverock.dbrevision.db.DatabaseAdapter;
import org.riverock.dbrevision.db.DatabaseManager;
import org.riverock.dbrevision.db.DatabaseStructureManager;
import org.riverock.dbrevision.annotation.schema.db.*;
import org.riverock.dbrevision.utils.Utils;
import org.riverock.dbrevision.utils.DbUtils;

/**
 * User: Admin
 * Date: May 15, 2003
 * Time: 11:15:35 PM
 * <p/>
 * $Id: DefinitionService.java 1141 2006-12-14 14:43:29Z serg_main $
 */
public final class DefinitionService {

    private static Logger log = Logger.getLogger(DefinitionService.class);

    public static final String CUSTOM_SQL_TYPE = "CUSTOM_SQL";

    public static final String CUSTOM_CLASS_ACTION_TYPE = "CUSTOM_CLASS_ACTION";

    public static final String CREATE_SEQUENCE_TYPE = "CREATE_SEQUENCE";

    public static final String CREATE_TABLE_TYPE = "CREATE_TABLE";

    public static final String ADD_TABLE_COLUMN_TYPE = "ADD_TABLE_COLUMN";

    public static final String DROP_TABLE_COLUMN_TYPE = "DROP_TABLE_COLUMN";

    public static final String ADD_PRIMARY_KEY_TYPE = "ADD_PRIMARY_KEY";

    public static final String ADD_FOREIGN_KEY_TYPE = "ADD_FOREIGN_KEY";

    public static final String DROP_PRIMARY_KEY_TYPE = "DROP_PRIMARY_KEY";

    public static final String DROP_FOREIGN_KEY_TYPE = "DROP_FOREIGN_KEY";

    public static final String DROP_TABLE_TYPE = "DROP_TABLE";

    public static final String DROP_SEQUENCE_TYPE = "DROP_SEQUENCE";

    public static final String DELETE_BEFORE_FK_TYPE = "DELETE_BEFORE_FK";

    public static final String COPY_COLUMN_TYPE = "COPY_COLUMN";

    public static final String CLONE_COLUMN_TYPE = "CLONE_COLUMN";

    public static final String COPY_TABLE_TYPE = "COPY_TABLE";

    private static final int UNKNOWN_TYPE_VALUE = 0;

    private static final int CUSTOM_SQL_TYPE_VALUE = 1;

    private static final int CUSTOM_CLASS_ACTION_TYPE_VALUE = 2;

    private static final int CREATE_SEQUENCE_TYPE_VALUE = 3;

    private static final int CREATE_TABLE_TYPE_VALUE = 4;

    private static final int ADD_TABLE_COLUMN_TYPE_VALUE = 5;

    private static final int DROP_TABLE_COLUMN_TYPE_VALUE = 6;

    private static final int ADD_PRIMARY_KEY_TYPE_VALUE = 7;

    private static final int ADD_FOREIGN_KEY_TYPE_VALUE = 8;

    private static final int DROP_PRIMARY_KEY_TYPE_VALUE = 9;

    private static final int DROP_FOREIGN_KEY_TYPE_VALUE = 10;

    private static final int DROP_TABLE_TYPE_VALUE = 11;

    private static final int DROP_SEQUENCE_TYPE_VALUE = 12;

    private static final int DELETE_BEFORE_FK_TYPE_VALUE = 13;

    private static final int COPY_COLUMN_TYPE_VALUE = 14;

    private static final int CLONE_COLUMN_TYPE_VALUE = 15;

    private static final int COPY_TABLE_TYPE_VALUE = 16;

    private static Map<String, Integer> actionTypes = new HashMap<String, Integer>();

    static {
        actionTypes.put(CUSTOM_SQL_TYPE, CUSTOM_SQL_TYPE_VALUE);
        actionTypes.put(CUSTOM_CLASS_ACTION_TYPE, CUSTOM_CLASS_ACTION_TYPE_VALUE);
        actionTypes.put(CREATE_SEQUENCE_TYPE, CREATE_SEQUENCE_TYPE_VALUE);
        actionTypes.put(CREATE_TABLE_TYPE, CREATE_TABLE_TYPE_VALUE);
        actionTypes.put(ADD_TABLE_COLUMN_TYPE, ADD_TABLE_COLUMN_TYPE_VALUE);
        actionTypes.put(DROP_TABLE_COLUMN_TYPE, DROP_TABLE_COLUMN_TYPE_VALUE);
        actionTypes.put(ADD_PRIMARY_KEY_TYPE, ADD_PRIMARY_KEY_TYPE_VALUE);
        actionTypes.put(ADD_FOREIGN_KEY_TYPE, ADD_FOREIGN_KEY_TYPE_VALUE);
        actionTypes.put(DROP_PRIMARY_KEY_TYPE, DROP_PRIMARY_KEY_TYPE_VALUE);
        actionTypes.put(DROP_FOREIGN_KEY_TYPE, DROP_FOREIGN_KEY_TYPE_VALUE);
        actionTypes.put(DROP_TABLE_TYPE, DROP_TABLE_TYPE_VALUE);
        actionTypes.put(DROP_SEQUENCE_TYPE, DROP_SEQUENCE_TYPE_VALUE);
        actionTypes.put(DELETE_BEFORE_FK_TYPE, DELETE_BEFORE_FK_TYPE_VALUE);
        actionTypes.put(COPY_COLUMN_TYPE, COPY_COLUMN_TYPE_VALUE);
        actionTypes.put(CLONE_COLUMN_TYPE, CLONE_COLUMN_TYPE_VALUE);
        actionTypes.put(COPY_TABLE_TYPE, COPY_TABLE_TYPE_VALUE);
    }

    private static boolean isDefinitionProcessed = false;

    private static Map<String, Object> definitionRelateHash = null;

    private static Map<String, Definition> definitionHash = null;

    private static Map<String, String> dbHash = null;

    private static DefinitionList definitionList = new DefinitionList();

    public static synchronized void registerRelateDefinitionDown(String definitionMain, String definitionTarget) {
        Utils.putKey(definitionRelateHash, definitionMain, definitionTarget);
    }

    private static final Object syncDebug = new Object();

    public static void processDefinitionList(DatabaseAdapter db_, DefinitionList definitionList) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("definitionList " + definitionList);
            if (definitionList != null) log.debug("definitionList.getDefinitionCount() " + definitionList.getDefinition().size());
        }
        for (Definition defItem : definitionList.getDefinition()) {
            try {
                if (log.isInfoEnabled()) {
                    log.info("process definition " + defItem.getNameDef());
                }
                log.debug("processTable ");
                processTable(db_, defItem);
                log.debug("processPrimaryKey ");
                processPrimaryKey(db_, defItem);
                log.debug("processImportedKeys ");
                processImportedKeys(db_, defItem);
                log.debug("processSequences ");
                processSequences(db_, defItem);
                log.debug("processAction ");
                processAction(db_, defItem);
                log.debug("store info about processed definition");
                CustomSequence seq = new CustomSequence();
                seq.setSequenceName("SEQ_WM_DB_DEFINITION");
                seq.setTableName("WM_DB_DEFINITION");
                seq.setColumnName("ID_DB_DEFINITION");
                MainDbDefinitionItem item = new MainDbDefinitionItem();
                item.setIdDbDefinition(db_.getSequenceNextValue(seq));
                item.setNameDefinition(defItem.getNameDef());
                item.setApplayDate(new Timestamp(System.currentTimeMillis()));
                String sql_ = "insert into WM_DB_DEFINITION " + "(ID_DB_DEFINITION, NAME_DEFINITION, APLAY_DATE)" + "values" + "( ?,  ?,  ?)";
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = db_.getConnection().prepareStatement(sql_);
                    ps.setLong(1, item.getIdDbDefinition());
                    ps.setString(2, item.getNameDefinition());
                    ps.setTimestamp(3, new java.sql.Timestamp(item.getApplayDate().getTime()));
                    int countInsertRecord = ps.executeUpdate();
                    if (log.isDebugEnabled()) log.debug("Count of inserted records - " + countInsertRecord);
                } catch (Exception e) {
                    log.error("Item getIdDbDefinition(), value - " + item.getIdDbDefinition());
                    log.error("Item getNameDefinition(), value - " + item.getNameDefinition());
                    log.error("Item getAplayDate(), value - " + item.getApplayDate());
                    log.error("Error update db", e);
                    throw e;
                } finally {
                    DatabaseManager.close(rs, ps);
                    rs = null;
                    ps = null;
                }
            } catch (Exception e) {
                log.error("Error process definition '" + defItem.getNameDef() + "' ", e);
                return;
            }
        }
    }

    public static String getString(DefinitionActionDataList actionList, String nameParam, String defValue) throws IllegalArgumentException {
        String value = getString(actionList, nameParam);
        if (value == null) return defValue;
        return value;
    }

    public static synchronized String getString(DefinitionActionDataList actionList, String nameParam) {
        if (actionList == null || nameParam == null || nameParam.length() == 0) return null;
        for (DefinitionActionData action : actionList.getParameter()) {
            if (action.getName().equals(nameParam)) return action.getData();
        }
        return null;
    }

    public static Double getDouble(DefinitionActionDataList actionList, String nameParam, double defValue) throws IllegalArgumentException {
        Double value = getDouble(actionList, nameParam);
        if (value == null) return defValue;
        return value;
    }

    public static synchronized Double getDouble(DefinitionActionDataList actionList, String nameParam) throws IllegalArgumentException {
        if (actionList == null || nameParam == null || nameParam.length() == 0) return null;
        for (DefinitionActionData action : actionList.getParameter()) {
            if (action.getName().equals(nameParam)) {
                String value = action.getData();
                Double doubleValue = null;
                try {
                    doubleValue = new Double(value);
                } catch (Exception e) {
                    String errorString = "Error convert String to Double from data - " + action.getData();
                    log.error(errorString, e);
                    throw new IllegalArgumentException(errorString);
                }
                return doubleValue;
            }
        }
        return null;
    }

    public static Long getLong(DefinitionActionDataList actionList, String nameParam, long defValue) throws IllegalArgumentException {
        Long value = getLong(actionList, nameParam);
        if (value == null) return defValue;
        return value;
    }

    public static synchronized Long getLong(DefinitionActionDataList actionList, String nameParam) throws IllegalArgumentException {
        if (actionList == null || nameParam == null || nameParam.length() == 0) return null;
        for (DefinitionActionData action : actionList.getParameter()) {
            if (action.getName().equals(nameParam)) {
                String value = action.getData();
                Long longValue = null;
                try {
                    longValue = new Long(value);
                } catch (Exception e) {
                    String errorString = "Error convert String to Long from data - " + action.getData();
                    log.error(errorString, e);
                    throw new IllegalArgumentException(errorString);
                }
                return longValue;
            }
        }
        return null;
    }

    public static Integer getInteger(DefinitionActionDataList actionList, String nameParam, int defValue) throws IllegalArgumentException {
        Integer value = getInteger(actionList, nameParam);
        if (value == null) return defValue;
        return value;
    }

    public static Integer getInteger(DefinitionActionDataList actionList, String nameParam) throws IllegalArgumentException {
        if (actionList == null || nameParam == null || nameParam.length() == 0) return null;
        for (DefinitionActionData action : actionList.getParameter()) {
            if (action.getName().equals(nameParam)) {
                String value = action.getData();
                Integer intValue = null;
                try {
                    intValue = new Integer(value);
                } catch (Exception e) {
                    String errorString = "Error convert String to Integer from data - " + action.getData();
                    log.error(errorString, e);
                    throw new IllegalArgumentException(errorString);
                }
                return intValue;
            }
        }
        return null;
    }

    public static Boolean getBoolean(DefinitionActionDataList actionList, String nameParam, boolean defValue) throws IllegalArgumentException {
        Boolean value = getBoolean(actionList, nameParam);
        if (value == null) return defValue;
        return value;
    }

    public static synchronized Boolean getBoolean(DefinitionActionDataList actionList, String nameParam) throws IllegalArgumentException {
        if (actionList == null || nameParam == null || nameParam.length() == 0) return null;
        for (DefinitionActionData action : actionList.getParameter()) {
            if (action.getName().equals(nameParam)) {
                String value = action.getData();
                if (value == null) value = "false";
                if (value.equals("1")) value = "true";
                Boolean booleanValue = null;
                try {
                    booleanValue = Boolean.valueOf(value);
                } catch (Exception e) {
                    String errorString = "Error convert String to Boolean from data - " + action.getData();
                    log.error(errorString, e);
                    throw new IllegalArgumentException(errorString);
                }
                return booleanValue;
            }
        }
        return null;
    }

    private static synchronized void processTable(DatabaseAdapter db_, Definition defItem) throws Exception {
        if (defItem == null) return;
        for (Object o : defItem.getTableListOrActionListOrData()) {
            if (o instanceof DefinitionTableList) {
                DefinitionTableList tableList = (DefinitionTableList) o;
                try {
                    for (DbTable table : tableList.getTable()) {
                        db_.createTable(table);
                    }
                } catch (Exception e) {
                    log.error("IsSilensAction - " + defItem.isIsSilensAction() + ", Definition - " + defItem.getNameDef(), e);
                    if (Boolean.FALSE.equals(defItem.isIsSilensAction())) {
                        throw e;
                    }
                }
            }
        }
    }

    private static synchronized void processPrimaryKey(DatabaseAdapter db_, Definition defItem) throws Exception {
        if (defItem == null) return;
        for (Object o : defItem.getTableListOrActionListOrData()) {
            if (o instanceof DbPrimaryKey) {
                DbPrimaryKey pk = (DbPrimaryKey) o;
                try {
                    if (!pk.getColumns().isEmpty()) {
                        DbSchema schema = DatabaseManager.getDbStructure(db_);
                        DbTable table = DatabaseManager.getTableFromStructure(schema, pk.getColumns().get(0).getTableName());
                        DatabaseManager.addPrimaryKey(db_, table, pk);
                    }
                } catch (Exception e) {
                    log.error("IsSilensAction - " + defItem.isIsSilensAction() + ", Definition - " + defItem.getNameDef(), e);
                    if (Boolean.FALSE.equals(defItem.isIsSilensAction())) throw e;
                }
            }
        }
    }

    private static synchronized void processImportedKeys(DatabaseAdapter db_, Definition defItem) throws Exception {
        if (defItem == null) return;
        for (Object o : defItem.getTableListOrActionListOrData()) {
            if (o instanceof DbImportedKeyList) {
                DbImportedKeyList fkList = (DbImportedKeyList) o;
                try {
                    if (!fkList.getKeys().isEmpty()) {
                        DatabaseStructureManager.createForeignKey(db_, fkList);
                    }
                } catch (Exception e) {
                    log.error("IsSilensAction - " + defItem.isIsSilensAction() + ", Definition - " + defItem.getNameDef(), e);
                    if (Boolean.FALSE.equals(defItem.isIsSilensAction())) throw e;
                }
            }
        }
    }

    private static synchronized void processSequences(DatabaseAdapter db_, Definition defItem) throws Exception {
        if (defItem == null) return;
        for (Object o : defItem.getTableListOrActionListOrData()) {
            if (o instanceof DbSequenceList) {
                DbSequenceList seqList = (DbSequenceList) o;
                try {
                    for (DbSequence seq : seqList.getSequences()) {
                        db_.createSequence(seq);
                    }
                } catch (Exception e) {
                    log.error("IsSilensAction - " + defItem.isIsSilensAction() + ", Definition - " + defItem.getNameDef(), e);
                    if (Boolean.FALSE.equals(defItem.isIsSilensAction())) {
                        throw e;
                    }
                }
            }
        }
    }

    private static synchronized void processAction(DatabaseAdapter db_, Definition defItem) throws Exception {
        if (defItem == null) return;
        for (Object o : defItem.getTableListOrActionListOrData()) {
            if (o instanceof DefinitionTableList) {
                DefinitionActionList actionList = (DefinitionActionList) o;
                if (actionList != null) {
                    if (log.isDebugEnabled()) log.debug("actionList.getActionCount() " + actionList.getAction().size());
                    for (DefinitionAction action : actionList.getAction()) {
                        try {
                            Integer type = actionTypes.get(action.getActionType());
                            if (type == null) type = UNKNOWN_TYPE_VALUE;
                            switch(type) {
                                case ADD_FOREIGN_KEY_TYPE_VALUE:
                                    {
                                    }
                                    break;
                                case ADD_PRIMARY_KEY_TYPE_VALUE:
                                    {
                                    }
                                    break;
                                case ADD_TABLE_COLUMN_TYPE_VALUE:
                                    {
                                        if (log.isDebugEnabled()) log.debug("process action ADD_TABLE_COLUMN_TYPE");
                                        DbField field = new DbField();
                                        field.setName(getString(action.getActionParameters(), "column_name"));
                                        field.setJavaType(DatabaseManager.sqlTypesMapping(getString(action.getActionParameters(), "column_type")));
                                        field.setSize(getInteger(action.getActionParameters(), "column_size", 0));
                                        field.setDecimalDigit(getInteger(action.getActionParameters(), "column_decimal_digit", 0));
                                        field.setDefaultValue(getString(action.getActionParameters(), "column_default_value"));
                                        field.setNullable(getInteger(action.getActionParameters(), "column_nullable", 0));
                                        DatabaseStructureManager.addColumn(db_, getString(action.getActionParameters(), "table_name"), field);
                                    }
                                    break;
                                case CLONE_COLUMN_TYPE_VALUE:
                                    {
                                    }
                                    break;
                                case COPY_COLUMN_TYPE_VALUE:
                                    {
                                    }
                                    break;
                                case CREATE_SEQUENCE_TYPE_VALUE:
                                    {
                                        DbSequence seq = new DbSequence();
                                        seq.setCacheSize(getInteger(action.getActionParameters(), "sequence_cache_size", 0));
                                        seq.setIncrementBy(getInteger(action.getActionParameters(), "sequence_increment", 1));
                                        seq.setIsCycle(getBoolean(action.getActionParameters(), "sequence_is_cycle", false));
                                        seq.setIsOrder(getBoolean(action.getActionParameters(), "sequence_is_order", false));
                                        seq.setLastNumber(getLong(action.getActionParameters(), "sequence_last_number", 0));
                                        seq.setMaxValue(getString(action.getActionParameters(), "sequence_max_value", "0"));
                                        seq.setMinValue(getInteger(action.getActionParameters(), "sequence_min_value", 0));
                                        seq.setName(getString(action.getActionParameters(), "sequence_name"));
                                        db_.createSequence(seq);
                                    }
                                    break;
                                case CREATE_TABLE_TYPE_VALUE:
                                    {
                                    }
                                    break;
                                case CUSTOM_CLASS_ACTION_TYPE_VALUE:
                                    {
                                        String className = getString(action.getActionParameters(), "class_name");
                                        if (className == null) throw new Exception("Definition - " + defItem.getNameDef() + ", action '" + CUSTOM_CLASS_ACTION_TYPE + "' must have parameter 'class_name'");
                                        Object obj = Utils.createCustomObject(className);
                                        if (obj == null) throw new Exception("Definition - " + defItem.getNameDef() + ", action '" + CUSTOM_CLASS_ACTION_TYPE + "', obj is null");
                                        ((DefinitionProcessingInterface) obj).processAction(db_, action.getActionParameters());
                                    }
                                    break;
                                case CUSTOM_SQL_TYPE_VALUE:
                                    {
                                        String sql = getString(action.getActionParameters(), "sql");
                                        if (log.isDebugEnabled()) {
                                            log.debug("Action type " + action.getActionType());
                                            log.debug("Custom sql " + sql);
                                        }
                                        Statement st = null;
                                        try {
                                            st = db_.getConnection().createStatement();
                                            st.execute(sql);
                                        } catch (Exception e) {
                                            log.error("Exception exceute statement " + sql, e);
                                            throw e;
                                        } catch (Error e) {
                                            log.error("Error exceute statement " + sql, e);
                                            throw e;
                                        } finally {
                                            DatabaseManager.close(st);
                                            st = null;
                                        }
                                    }
                                    break;
                                case DELETE_BEFORE_FK_TYPE_VALUE:
                                    {
                                    }
                                    break;
                                case DROP_FOREIGN_KEY_TYPE_VALUE:
                                    {
                                    }
                                    break;
                                case DROP_PRIMARY_KEY_TYPE_VALUE:
                                    {
                                    }
                                    break;
                                case DROP_TABLE_TYPE_VALUE:
                                    {
                                        String nameTable = getString(action.getActionParameters(), "name_table");
                                        if (nameTable != null) {
                                            db_.dropTable(nameTable);
                                            db_.getConnection().commit();
                                        } else log.error("Definition - " + defItem.getNameDef() + ", action '" + DROP_TABLE_TYPE + "' must have parameter 'name_table'");
                                    }
                                    break;
                                case DROP_TABLE_COLUMN_TYPE_VALUE:
                                    {
                                    }
                                    break;
                                case DROP_SEQUENCE_TYPE_VALUE:
                                    {
                                        String nameSeq = getString(action.getActionParameters(), "name_sequence");
                                        if (nameSeq != null) {
                                            db_.dropSequence(nameSeq);
                                        } else log.error("Definition - " + defItem.getNameDef() + ", action '" + DROP_TABLE_TYPE + "' must have parameter 'name_sequence'");
                                    }
                                    break;
                                default:
                                    String errorString = "Unknown type of action - " + action.getActionType();
                                    log.error(errorString);
                                    throw new Exception(errorString);
                            }
                        } catch (Exception e) {
                            log.error("IsSilensAction - " + defItem.isIsSilensAction() + ", Definition - " + defItem.getNameDef() + ", action '" + action.getActionType() + "'", e);
                            if (Boolean.FALSE.equals(defItem.isIsSilensAction())) throw e;
                        }
                    }
                }
            }
        }
    }

    private static synchronized void walk(String key) {
        Object obj = definitionRelateHash.get(key);
        if (obj != null) {
            if (obj instanceof String) {
                String nameDef = (String) obj;
                walk(nameDef);
            } else if (obj instanceof List) {
                List v = (List) obj;
                walkList(v);
            } else {
                throw new IllegalStateException("Wrong type of element in list, valid String and List, current: " + obj.getClass().getName());
            }
        }
        Object value = dbHash.get(key);
        boolean flag = isInQueue(key);
        if (value == null && !flag) definitionList.getDefinition().add(definitionHash.get(key));
    }

    private static synchronized void walkList(List v) {
        for (Object obj : v) {
            if (obj == null) {
                throw new IllegalStateException("Element in list is null");
            }
            if (obj instanceof String) {
                String nameDef = (String) obj;
                walk(nameDef);
            } else if (obj instanceof List) {
                walkList((List) obj);
            } else {
                throw new IllegalStateException("Wrong type of element in list, valid String and List, current: " + obj.getClass().getName());
            }
        }
    }

    private static synchronized boolean isInQueue(String nameDef) {
        for (Definition def : definitionList.getDefinition()) {
            if (def.getNameDef().equals(nameDef)) return true;
        }
        return false;
    }

    private static synchronized void getProcessedDefinition(DatabaseAdapter db_) throws Exception {
        if (dbHash != null) {
            dbHash.clear();
            dbHash = null;
        }
        dbHash = new HashMap<String, String>();
        MainDbDefinitionList mainDef = new MainDbDefinitionList();
        try {
            String sql_ = "select ID_DB_DEFINITION from WM_DB_DEFINITION ";
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = db_.getConnection().prepareStatement(sql_);
                rs = ps.executeQuery();
                while (rs.next()) {
                    GetMainDbDefinitionItem tempItem = new GetMainDbDefinitionItem(db_, DbUtils.getLong(rs, "ID_DB_DEFINITION"));
                    mainDef.getMainDbDefinitionList().add(tempItem.item);
                }
            } catch (Exception e) {
                if (db_.testExceptionTableNotFound(e)) {
                    log.warn("WM_DB_DEFINITION table not found. Assumed this first connect to empty DB schema. Start create new structure");
                    return;
                }
                if (e instanceof SQLException) log.error("Error get data from WM_DB_DEFINITION, sql code " + ((SQLException) e).getErrorCode(), e); else log.error("Error get data from WM_DB_DEFINITION", e);
                throw e;
            } finally {
                DatabaseManager.close(rs, ps);
                rs = null;
                ps = null;
            }
        } catch (Exception e) {
            log.error("Error get new instance for GetMainDbDefinitionFullList", e);
            throw e;
        }
        for (MainDbDefinitionItem tempDbItem : mainDef.getMainDbDefinitionList()) {
            dbHash.put(tempDbItem.getNameDefinition(), tempDbItem.getNameDefinition());
        }
    }
}
