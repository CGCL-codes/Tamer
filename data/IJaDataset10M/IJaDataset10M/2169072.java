package org.lottz.fgen.model;

import java.util.ArrayList;
import org.lottz.fgen.model.OperativeModel;
import java.sql.*;
import javax.sql.*;
import java.lang.reflect.*;
import java.util.Date;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.naming.*;

/**
 * Process Operative Models and return the result.
*/
public class JDBCProcessor implements IModelProcessor {

    protected String _driver = "";

    protected String url = "";

    protected HashMap serviceMap;

    protected HashMap remoteMap;

    protected String useSource = "";

    private static Pattern fieldPattern = Pattern.compile("^([0-9]|[a-zA-Z]|_|\\-)+$");

    protected HashMap operConvMap = new HashMap();

    private static final String operSep = "__";

    protected String dbConnectType = "driverManager";

    protected DataSource _ds;

    public JDBCProcessor() {
        operConvMap.put("like", "like");
        operConvMap.put("gt", ">");
        operConvMap.put("lt", "<");
        operConvMap.put("ge", ">=");
        operConvMap.put("le", "<=");
        operConvMap.put("eq", "=");
        operConvMap.put("eqStr", "=");
        operConvMap.put("eqDate", "=");
        operConvMap.put("and", "and");
        operConvMap.put("or", "or");
        operConvMap.put("andNext", "and");
        operConvMap.put("orNext", "or");
        operConvMap.put("hasMany", "=");
        operConvMap.put("belongsTo", "=");
    }

    /**
     * Called from the Factory class for initialize some fields.
     */
    public void setPropertyMap(HashMap arg_serviceMap, HashMap arg_remoteMap) {
        serviceMap = arg_serviceMap;
        remoteMap = arg_remoteMap;
        useSource = (String) serviceMap.get("use-source");
        if ("jdbc".equals(useSource)) {
            System.out.println("[INFO]using DriverManager for DB connection.");
            dbConnectType = "driverManager";
            _driver = (String) serviceMap.get("jdbc-driver");
            url = (String) serviceMap.get("jdbc-url");
            if (_driver == null || "".equals(_driver)) {
                throw makeReturnException("jdbc-driver", "FGenModelFactory Init Error(jdbc-driver setting)", "ModelFactory_INIT", new Exception("FGenModelFactory Init Error"));
            }
            if (url == null || "".equals(url)) {
                throw makeReturnException("jdbc-url", "FGenModelFactory Init Error(jdbc-url setting)", "ModelFactory_INIT", new Exception("FGenModelFactory Init Error"));
            }
        } else {
            System.out.println("[INFO]using DataSource for DB connection.");
            dbConnectType = "dataSource";
            useSource = (String) serviceMap.get(useSource);
            if (useSource == null || "".equals(useSource)) {
                throw makeReturnException(useSource, "FGenModelFactory Init Error(use-source setting)", "ModelFactory_INIT", new Exception("FGenModelFactory Init Error"));
            }
            try {
                InitialContext ctx = new InitialContext();
                _ds = (DataSource) ctx.lookup(useSource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Process OperativeModels and return the result.
     */
    public ArrayList process(final ArrayList arr) throws Exception {
        ArrayList ret = new ArrayList();
        int len = arr.size();
        OperativeModel fst;
        if (len >= 1 && arr.get(0) instanceof OperativeModel) {
            fst = (OperativeModel) arr.get(0);
            if (fst.operation == null || (fst.operation instanceof String && "read".equals(fst.operation)) || (fst.operation instanceof HashMap)) {
                return execRead(fst);
            }
        }
        for (Object obj : arr) {
            if (obj instanceof OperativeModel) {
                OperativeModel m = (OperativeModel) obj;
                if (m.operation == null || "read".equals(m.operation)) {
                } else if ("create".equals(m.operation)) {
                    ret.add(execCreate(m));
                } else if ("update".equals(m.operation)) {
                    ret.add(execUpdate(m));
                }
                if ("delete".equals(m.operation)) {
                    ret.add(execDelete(m));
                }
            }
        }
        return ret;
    }

    /**
     * Make a query conditon from the passed Model instance.
     */
    private ArrayList getFieldParamData(String modelName, OperativeModel mdl) throws Exception {
        ArrayList queryValType = new ArrayList();
        ArrayList queryValue = new ArrayList();
        String query = "";
        Double updateInt = null;
        String updateStr = null;
        Date updateDate = null;
        Field[] fields = mdl.getClass().getFields();
        for (Field field : fields) {
            if ("operation".equals(field.getName())) {
                continue;
            }
            if (field.getType() == String.class) {
                updateStr = (String) field.get(mdl);
                if (updateStr != null) {
                    query += " " + modelName + "." + field.getName() + "=?,";
                    queryValue.add(field.get(mdl));
                    queryValType.add(field.getType());
                }
            } else if (field.getType() == Double.class) {
                updateInt = (Double) field.get(mdl);
                if (updateInt != null && !Double.isNaN(updateInt)) {
                    if ("id".equals(field.getName())) {
                        continue;
                    }
                    query += " " + modelName + "." + field.getName() + "=?,";
                    queryValue.add(field.get(mdl));
                    queryValType.add(field.getType());
                }
            }
            if (field.getType() == Date.class) {
                updateDate = (Date) field.get(mdl);
                if (updateDate != null) {
                    query += " " + modelName + "." + field.getName() + "=?,";
                    queryValue.add(field.get(mdl));
                    queryValType.add(field.getType());
                }
            }
        }
        if (query.length() > 0) {
            query = query.substring(0, query.length() - 1);
        }
        ArrayList ret = new ArrayList();
        ret.add(queryValType);
        ret.add(queryValue);
        ret.add(query);
        return ret;
    }

    /**
     * Simple facade class to extract the condition array from the model object and pass it to the implemnt function.
     */
    private ArrayList getQueryParamData(String modelName, HashMap ctMap, boolean bFirst) {
        ArrayList ctArr = (ArrayList) ctMap.get("criteria");
        return getQueryParamData(modelName, ctArr, bFirst);
    }

    /**
     * Make the condition part of SQL.
     */
    private ArrayList getQueryParamData(String modelName, ArrayList ctArr, boolean bFirst) {
        ArrayList queryValType = new ArrayList();
        ArrayList queryValue = new ArrayList();
        ArrayList orderByField = new ArrayList();
        boolean bExistCriteria = false;
        boolean bOrderAsc = true;
        String query = "";
        if (ctArr != null) {
            final int len = ctArr.size();
            for (int i = 0; i < len; i++) {
                Object arr[] = (Object[]) ctArr.get(i);
                System.out.println("criteria:key:" + arr[0] + ":value:" + arr[1]);
                String key = (String) arr[0];
                int criteriaIdx = key.lastIndexOf(operSep);
                if (criteriaIdx >= 0) {
                    String operStr = key.substring(criteriaIdx + operSep.length());
                    String operFieldStr = key.substring(0, criteriaIdx);
                    Object value = null;
                    if ("andNext".equals(operStr)) {
                        query += " AND ";
                        continue;
                    } else if ("orNext".equals(operStr)) {
                        query += " OR ";
                        continue;
                    } else if ("orderBy".equals(operStr)) {
                        if (operFieldStr != null && operFieldStr.length() > 0) {
                            System.out.println("ordere by came here!:" + operFieldStr);
                            orderByField.add(operFieldStr);
                        }
                        if (arr[1] instanceof String && "desc".equals(arr[1])) {
                            bOrderAsc = false;
                        }
                        continue;
                    } else if ("and".equals(operStr)) {
                        Object valueArr[] = (Object[]) arr[1];
                        int condCtLen = valueArr.length;
                        for (int j = 0; j < condCtLen; j++) {
                            ArrayList vals = getQueryParamData(modelName, (ArrayList) valueArr[j], false);
                            queryValType.addAll((ArrayList) vals.get(0));
                            queryValue.addAll((ArrayList) vals.get(1));
                            if (j != 0 && j != len - 1) {
                                query += " and ";
                            }
                            query += "(" + (String) vals.get(2) + ")";
                        }
                    } else if ("or".equals(operStr)) {
                        Object valueArr[] = (Object[]) arr[1];
                        int condCtLen = valueArr.length;
                        for (int j = 0; j < condCtLen; j++) {
                            ArrayList vals = getQueryParamData(modelName, (ArrayList) valueArr[j], false);
                            queryValType.addAll((ArrayList) vals.get(0));
                            queryValue.addAll((ArrayList) vals.get(1));
                            if (j != 0 && j != len - 1) {
                                query += " or ";
                            }
                            query += "(" + (String) vals.get(2) + ")";
                        }
                    } else if ("hasMany".equals(operStr) || "belongsTo".equals(operStr)) {
                        continue;
                    } else {
                        if (query.length() != 0) {
                            query += " AND ";
                        }
                        value = arr[1];
                        queryValue.add(value);
                        queryValType.add(value.getClass());
                        System.out.println("operStr:" + operStr + ":" + operConvMap.get(operStr));
                        query += " " + modelName + "." + operFieldStr + " " + operConvMap.get(operStr) + " ? ";
                    }
                }
            }
        }
        int len = orderByField.size();
        if (len > 0) {
            System.out.println("order by added!");
            query += " order by ";
            for (int i = 0; i < len; i++) {
                Matcher matcher = fieldPattern.matcher((String) orderByField.get(i));
                if (matcher.matches()) {
                    query += modelName + "." + orderByField.get(i);
                    if (i < len - 1) {
                        query += ",";
                    }
                } else {
                    throw new Error("[ERROR][FGEN]invalid order by field name:" + orderByField.get(i));
                }
            }
            if (bOrderAsc) {
                query += " asc";
            } else {
                query += " desc";
            }
        }
        ArrayList ret = new ArrayList();
        ret.add(queryValType);
        ret.add(queryValue);
        ret.add(query);
        return ret;
    }

    private boolean isRelationQuery(HashMap ctMap) {
        ArrayList ctArr = (ArrayList) ctMap.get("criteria");
        if (ctArr == null) {
            return false;
        }
        if (ctArr.size() == 0) {
            return false;
        }
        Object arr[] = (Object[]) ctArr.get(0);
        String key = (String) arr[0];
        int criteriaIdx = key.lastIndexOf(operSep);
        if (criteriaIdx < 0) {
            return false;
        }
        String operStr = key.substring(criteriaIdx + operSep.length());
        if ("hasMany".equals(operStr) || "belongsTo".equals(operStr)) {
            return true;
        } else {
            return false;
        }
    }

    private ArrayList makeRelationQueryParamData(OperativeModel mdl) throws Exception {
        final HashMap ctMap = (HashMap) mdl.operation;
        final ArrayList ctArr = (ArrayList) ctMap.get("criteria");
        OperativeModel targetModelInst = null;
        ArrayList fromTableNames = new ArrayList();
        String query = "";
        if (ctArr == null) {
            throw new Exception("[ERROR][FGEN]criteria is null");
        }
        if (ctArr.size() == 0) {
            throw new Exception("[ERROR][FGEN]criteria sizs 0");
        }
        int arrLen = ctArr.size();
        String key = null;
        Object arr[];
        int prevRelationIdx = -1;
        for (int i = 0; i < arrLen; i++) {
            arr = (Object[]) ctArr.get(i);
            key = (String) arr[0];
            int criteriaIdx = key.lastIndexOf(operSep);
            if (criteriaIdx < 0) {
                throw new Exception("[ERROR][FGEN]invalid criteria format");
            }
            String operStr = key.substring(criteriaIdx + operSep.length());
            if ("hasMany".equals(operStr) || "belongsTo".equals(operStr)) {
                String operFieldStr = key.substring(0, criteriaIdx);
                String targetModel = (String) arr[1];
                String targetField = (String) arr[2];
                targetModelInst = (OperativeModel) arr[3];
                if (query.length() == 0) {
                    query += " where ";
                }
                if (prevRelationIdx == -1) {
                    query += mdl.getClass().getSimpleName() + "." + operFieldStr + " = " + targetModel + "." + targetField;
                } else {
                    query += " and " + ((Object[]) ctArr.get(prevRelationIdx))[1] + "." + operFieldStr + " = " + targetModel + "." + targetField;
                }
                fromTableNames.add(arr[1]);
                prevRelationIdx = i;
            }
        }
        if (fromTableNames.size() == 0) {
            return null;
        }
        ArrayList ret = new ArrayList();
        ret.add(fromTableNames);
        ret.add(query);
        ret.add(targetModelInst);
        return ret;
    }

    protected Connection getConnection() throws Exception {
        Connection conn = null;
        if ("driverManager".equals(dbConnectType)) {
            try {
                Class.forName(_driver);
            } catch (java.lang.ClassNotFoundException e) {
                System.err.println(e.getMessage());
                throw e;
            }
            System.out.println("[DEBUG]Connecting database:" + url);
            conn = DriverManager.getConnection(url);
            System.out.println("[DEBUG]Connected");
        } else if ("dataSource".equals(dbConnectType)) {
            conn = _ds.getConnection();
        }
        conn.setAutoCommit(true);
        return conn;
    }

    public ArrayList execRead(final OperativeModel mdl) throws Exception {
        ArrayList ret = new ArrayList();
        Class cls = mdl.getClass();
        String tableName = cls.getSimpleName();
        String relatedToTableName = null;
        ArrayList fromTableNames = new ArrayList();
        String query = "";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        ArrayList queryValType = new ArrayList();
        ArrayList queryValue = new ArrayList();
        ArrayList vals = null;
        String addQuery = null;
        Field[] fields;
        try {
            if (mdl.operation instanceof HashMap) {
                System.out.println("[DEBUG]processing criteria");
                vals = makeRelationQueryParamData(mdl);
                if (vals != null) {
                    fromTableNames = (ArrayList) vals.get(0);
                    relatedToTableName = (String) fromTableNames.get(fromTableNames.size() - 1);
                    query = (String) vals.get(1);
                    cls = vals.get(2).getClass();
                }
                vals = getQueryParamData(tableName, (HashMap) mdl.operation, false);
                queryValType = (ArrayList) vals.get(0);
                queryValue = (ArrayList) vals.get(1);
                addQuery = (String) vals.get(2);
                if (addQuery.length() > 0) {
                    if (queryValue.size() > 0) {
                        if (query.length() > 0) {
                            query += " and ";
                        }
                        query += addQuery;
                    } else {
                        query += addQuery;
                    }
                }
            }
            vals = getFieldParamData(tableName, mdl);
            ArrayList fieldQueryValType = (ArrayList) vals.get(0);
            ArrayList fieldQueryValue = (ArrayList) vals.get(1);
            addQuery = (String) vals.get(2);
            System.out.println("not criteira addQueery:" + addQuery);
            if (addQuery.length() > 0) {
                if (fieldQueryValue.size() > 0) {
                    if (query.length() > 0) {
                        query += " and ";
                    } else {
                        query += " where ";
                    }
                    query += addQuery;
                } else {
                    query += addQuery;
                }
            }
            queryValue.addAll(fieldQueryValue);
            queryValType.addAll(fieldQueryValType);
            fields = cls.getFields();
            int fieldsLen = fields.length;
            String queryFront = "select ";
            for (Field field : fields) {
                if ("operation".equals(field.getName())) {
                    continue;
                }
                if (relatedToTableName != null) {
                    queryFront += relatedToTableName + "." + field.getName() + ",";
                } else {
                    queryFront += tableName + "." + field.getName() + ",";
                }
            }
            queryFront = queryFront.substring(0, queryFront.length() - 1);
            if (relatedToTableName != null) {
                int len = fromTableNames.size();
                String tableNames = "";
                for (int i = 0; i < len; i++) {
                    tableNames += (String) fromTableNames.get(i) + ",";
                }
                if (tableNames.length() > 0) {
                    tableNames = tableNames.substring(0, tableNames.length() - 1);
                }
                query = queryFront + " from " + tableName + "," + tableNames + " " + query;
            } else {
                query = queryFront + " from " + tableName + " " + query;
            }
            System.out.println("[FGEN][DEBUG]query:" + query);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(query);
            final int len = queryValue.size();
            for (int i = 0; i < len; i++) {
                Class type = (Class) queryValType.get(i);
                if (type == String.class) {
                    stmt.setString(i + 1, (String) queryValue.get(i));
                } else if (type == Double.class) {
                    stmt.setDouble(i + 1, ((Double) queryValue.get(i)).doubleValue());
                } else if (type == Integer.class) {
                    stmt.setInt(i + 1, ((Integer) queryValue.get(i)).intValue());
                } else if (type == Date.class) {
                    stmt.setDate(i + 1, new java.sql.Date(((Date) queryValue.get(i)).getTime()));
                }
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                Object retObj = cls.newInstance();
                for (Field field : fields) {
                    System.out.println("type:" + field.getType());
                    System.out.println("name:" + field.getName());
                    if ("operation".equals(field.getName())) {
                        continue;
                    }
                    if (field.getType() == String.class) {
                        field.set(retObj, rs.getString(field.getName()));
                    } else if (field.getType() == Double.class) {
                        field.set(retObj, rs.getDouble(field.getName()));
                    }
                    if (field.getType() == Date.class) {
                        field.set(retObj, rs.getDate(field.getName()));
                    }
                }
                ret.add(retObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("[FGEN:ERROR]close error:" + e.getMessage());
            }
        }
        return ret;
    }

    public OperativeModel execCreate(final OperativeModel mdl) throws Exception {
        final Class cls = mdl.getClass();
        final String tableName = cls.getSimpleName();
        final String fqcn = mdl.getClass().getName();
        String query = "insert into " + tableName + " (";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        String queryField = "";
        ArrayList queryValType = new ArrayList();
        ArrayList queryValue = new ArrayList();
        try {
            conn = getConnection();
            Field[] fields = cls.getFields();
            int fieldsLen = fields.length;
            Double updateInt = null;
            String updateStr = null;
            Date updateDate = null;
            Object retObj = cls.newInstance();
            for (Field field : fields) {
                System.out.println("type:" + field.getType());
                System.out.println("name:" + field.getName());
                if ("operation".equals(field.getName())) {
                    continue;
                }
                if (field.getType() == String.class) {
                    updateStr = (String) field.get(mdl);
                    if (updateStr != null && !"".equals(updateStr)) {
                        queryField += field.getName() + ",";
                        queryValue.add(field.get(mdl));
                        queryValType.add(field.getType());
                    }
                } else if (field.getType() == Double.class) {
                    System.out.println("field: double type");
                    updateInt = (Double) field.get(mdl);
                    if (updateInt != null && (!Double.isNaN((Double) updateInt))) {
                        queryField += field.getName() + ",";
                        queryValue.add(field.get(mdl));
                        queryValType.add(field.getType());
                        System.out.println("field:" + field.getName() + ",data:" + updateInt);
                    } else if (updateInt != null) {
                        System.out.println("field: number is null");
                    } else if (Double.isNaN((Double) updateInt)) {
                        System.out.println("field: number is NaN");
                    }
                }
                if (field.getType() == Date.class) {
                    updateDate = (Date) field.get(mdl);
                    if (updateDate != null) {
                        queryField += field.getName() + ",";
                        queryValue.add(field.get(mdl));
                        queryValType.add(field.getType());
                    }
                }
            }
            if (queryField.length() > 0) {
                queryField = queryField.substring(0, queryField.length() - 1);
            }
            query += queryField + ") values (";
            int fieldNum = queryValue.size();
            for (int i = 0; i < fieldNum; i++) {
                query += "?,";
            }
            query = query.substring(0, query.length() - 1) + ")";
            System.out.println("query:" + query);
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int len = queryValue.size();
            for (int i = 0; i < len; i++) {
                Class type = (Class) queryValType.get(i);
                if (type == String.class) {
                    stmt.setString(i + 1, (String) queryValue.get(i));
                } else if (type == Double.class) {
                    stmt.setDouble(i + 1, ((Double) queryValue.get(i)).doubleValue());
                } else if (type == Date.class) {
                    stmt.setDate(i + 1, new java.sql.Date(((Date) queryValue.get(i)).getTime()));
                }
            }
            int num = stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            try {
                rs.next();
                BigDecimal id = rs.getBigDecimal(1);
                System.out.println(id);
                mdl.id = id.doubleValue();
            } finally {
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("[FGEN:ERROR]close error:" + e.getMessage());
            }
        }
        return mdl;
    }

    public OperativeModel execUpdate(final OperativeModel mdl) throws Exception {
        final Class cls = mdl.getClass();
        final String tableName = cls.getSimpleName();
        final String fqcn = mdl.getClass().getName();
        String query = "update " + tableName + " SET ";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        String queryField = "";
        ArrayList queryValType = new ArrayList();
        ArrayList queryValue = new ArrayList();
        try {
            conn = getConnection();
            Field[] fields = cls.getFields();
            int fieldsLen = fields.length;
            Double updateInt = null;
            String updateStr = null;
            Date updateDate = null;
            Object retObj = cls.newInstance();
            int id = -1;
            for (Field field : fields) {
                System.out.println("type:" + field.getType());
                System.out.println("name:" + field.getName());
                if ("operation".equals(field.getName())) {
                    continue;
                }
                if (field.getType() == String.class) {
                    updateStr = (String) field.get(mdl);
                    if (updateStr != null) {
                        query += " " + field.getName() + "=?,";
                        queryValue.add(field.get(mdl));
                        queryValType.add(field.getType());
                    }
                } else if (field.getType() == Double.class) {
                    updateInt = (Double) field.get(mdl);
                    if (updateInt != null && !Double.isNaN(updateInt)) {
                        if ("id".equals(field.getName())) {
                            id = updateInt.intValue();
                            continue;
                        }
                        query += " " + field.getName() + "=?,";
                        queryValue.add(field.get(mdl));
                        queryValType.add(field.getType());
                    }
                }
                if (field.getType() == Date.class) {
                    updateDate = (Date) field.get(mdl);
                    if (updateDate != null) {
                        query += " " + field.getName() + "=?,";
                        queryValue.add(field.get(mdl));
                        queryValType.add(field.getType());
                    }
                }
            }
            query = query.substring(0, query.length() - 1);
            query += " where id=?";
            System.out.println("query:" + query);
            stmt = conn.prepareStatement(query);
            int len = queryValue.size();
            int i;
            for (i = 0; i < len; i++) {
                Class type = (Class) queryValType.get(i);
                if (type == String.class) {
                    stmt.setString(i + 1, (String) queryValue.get(i));
                } else if (type == Double.class) {
                    stmt.setDouble(i + 1, ((Double) queryValue.get(i)).doubleValue());
                } else if (type == Date.class) {
                    stmt.setDate(i + 1, new java.sql.Date(((Date) queryValue.get(i)).getTime()));
                }
            }
            stmt.setInt(i + 1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("[FGEN:ERROR]close error:" + e.getMessage());
            }
        }
        return mdl;
    }

    public OperativeModel execDelete(final OperativeModel mdl) throws Exception {
        final Class cls = mdl.getClass();
        final String tableName = cls.getSimpleName();
        final String fqcn = mdl.getClass().getName();
        String query = "delete from " + tableName + " where id=?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        String queryField = "";
        ArrayList queryValType = new ArrayList();
        ArrayList queryValue = new ArrayList();
        try {
            conn = getConnection();
            Field[] fields = cls.getFields();
            int fieldsLen = fields.length;
            Double updateInt = null;
            String updateStr = null;
            Date updateDate = null;
            Object retObj = cls.newInstance();
            int id = -1;
            for (Field field : fields) {
                System.out.println("type:" + field.getType());
                System.out.println("name:" + field.getName());
                if ("operation".equals(field.getName())) {
                    continue;
                }
                if (field.getType() == String.class) {
                    updateStr = (String) field.get(mdl);
                    if (updateStr != null) {
                    }
                } else if (field.getType() == Double.class) {
                    updateInt = (Double) field.get(mdl);
                    if (updateInt != null) {
                        if ("id".equals(field.getName())) {
                            id = updateInt.intValue();
                            continue;
                        }
                    }
                }
                if (field.getType() == Date.class) {
                    updateDate = (Date) field.get(mdl);
                    if (updateDate != null) {
                    }
                }
            }
            System.out.println("query:" + query);
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            int num = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("[FGEN:ERROR]close error:" + e.getMessage());
            }
        }
        return mdl;
    }

    private RuntimeException makeReturnException(final String source, final String msg, final String code, final Exception e) {
        RuntimeException ex = new RuntimeException(msg + ":" + source, e);
        return ex;
    }
}
