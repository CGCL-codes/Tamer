package com.arm.framework.core;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.arm.framework.FieldData;
import com.arm.framework.KeyData;
import com.arm.framework.QueryData;
import com.arm.framework.Type;
import com.arm.framework.annotation.AutoKey;
import com.arm.framework.annotation.ConnectedTo;
import com.arm.framework.annotation.NotNull;
import com.arm.framework.annotation.Optional;
import com.arm.framework.common.Messages;
import com.arm.framework.exception.UnKnownException;
import com.arm.framework.sql.MySql;
import com.arm.framework.utilities.F;
import com.arm.framework.utilities.UUID;
import com.mysql.jdbc.CommunicationsException;

/**
 * @author <a href="mailto:todushyant@gmail.com">todushyant@gmail.com</a>
 * 
 * @param <E>
 * 
 *            Example of ALTER TABLE.
 * 
 *            ALTER TABLE `arm`.`item` ADD CONSTRAINT `newfk` FOREIGN KEY
 *            `newfk` (`MFGId`) REFERENCES `company` (`id`);
 */
public class Core<E> {

    /**
	 * MySql Database Name.
	 */
    public static String database = Messages.getString("STRINGS.0");

    /**
	 * @param <T>
	 * @param className
	 * @return
	 * @throws SecurityException
	 */
    public static <T> Field[] getDeclaredFields(Class<T> className) throws SecurityException {
        return className.getDeclaredFields();
    }

    /**
	 * @param e
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws CommunicationsException
	 */
    protected int update(E e, int id) throws CommunicationsException, ClassNotFoundException, SQLException, UnKnownException {
        Class<? extends Object> className = e.getClass();
        String tableName = className.getSimpleName().toLowerCase();
        String query = "update " + tableName + " set ";
        Field[] declaredFields = getFields(className);
        String values = "";
        ArrayList<FieldData> datas = getFieldDatas(e, declaredFields);
        for (FieldData fieldData : datas) {
            Object data = fieldData.getData();
            values += setQueryBasedOnDataTypeForUpdateQuery(fieldData, data);
        }
        String updatedOnDate = dateToString(new Date());
        values = F.removeFromLast(values);
        String updatedOnDateString = ", updatedOn='" + updatedOnDate + "'";
        String whereString = " where id=" + id;
        query += values + updatedOnDateString + whereString;
        p(query);
        MySql.create().execute(query);
        return id;
    }

    /**
	 * @param e
	 * @param duplicate
	 *            if false. it will not save duplicate entry, if true it will
	 *            save duplicate entry
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws CommunicationsException
	 * @throws UnKnownException
	 * 
	 */
    protected int saveData(E e, boolean duplicate) throws CommunicationsException, ClassNotFoundException, SQLException, UnKnownException {
        if (!duplicate) {
            int existsAlready = exists(e);
            if (existsAlready > 0) {
                return existsAlready;
            } else {
            }
        }
        Class<? extends Object> className = e.getClass();
        String tableName = className.getSimpleName().toLowerCase();
        String query = "insert into " + tableName;
        Field[] declaredFields = getFields(className);
        ArrayList<FieldData> datas = getFieldDatas(e, declaredFields);
        String intoWhat = "";
        String values = "";
        for (FieldData fieldData : datas) {
            intoWhat += fieldData.getName() + ",";
            Object data = fieldData.getData();
            values = setQueryBasedOnDataTypeForInsertQuery(values, fieldData, data);
        }
        intoWhat = F.removeFromLast(intoWhat);
        values = F.removeFromLast(values);
        String createdOnDate = dateToString(new Date());
        query = query + "(" + intoWhat + ",createdOn) values(" + values + ",'" + createdOnDate + "')";
        MySql<?> mySql = MySql.get();
        mySql.execute(query);
        int exists = exists(e);
        return exists;
    }

    /**
	 * @param values
	 * @param fieldData
	 * @param data
	 * @return
	 */
    private String setQueryBasedOnDataTypeForInsertQuery(String values, FieldData fieldData, Object data) {
        if (fieldData.getType().equals(Type.VARCHAR)) {
            values += "'" + data.toString() + "'" + ",";
        } else if (fieldData.getType().equals(Type.INT)) {
            values += data.toString() + ",";
        } else if (fieldData.getType().equals(Type.DATE)) {
            String s = dateToString((Date) data);
            values += "'" + s + "'" + ",";
        } else if (fieldData.getType().equals(Type.BOOLEAN)) {
            values += data.toString() + ",";
        }
        return values;
    }

    /**
	 * @param values
	 * @param fieldData
	 * @param data
	 * @return
	 */
    private String setQueryBasedOnDataTypeForUpdateQuery(FieldData fieldData, Object data) {
        String values = "";
        if (fieldData.getType().equals(Type.VARCHAR)) {
            values = "'" + data.toString() + "'" + ",";
        } else if (fieldData.getType().equals(Type.INT)) {
            values = data.toString() + ",";
        } else if (fieldData.getType().equals(Type.DATE)) {
            String s = dateToString((Date) data);
            values = "'" + s + "'" + ",";
        } else if (fieldData.getType().equals(Type.BOOLEAN)) {
            values = data.toString() + ",";
        }
        return fieldData.getName() + " = " + values;
    }

    /**
	 * @param className
	 * @return
	 */
    private Field[] getFields(Class<? extends Object> className) {
        Field[] declaredFields = Core.getDeclaredFields(className);
        return declaredFields;
    }

    /**
	 * @param data
	 * @return
	 */
    private String dateToString(Object data) {
        Format formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String s = formatter.format(data);
        return s;
    }

    /**
	 * @param e
	 * @param declaredFields
	 * @return
	 * @throws UnKnownException
	 */
    private ArrayList<FieldData> getFieldDatas(E e, Field[] declaredFields) throws UnKnownException {
        ArrayList<FieldData> datas = new ArrayList<FieldData>();
        boolean ignoreField = false;
        try {
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Optional.class)) {
                    if (!field.getType().equals(Long.class)) {
                        throw new AssertionError("field annotated with @Optional must be Long not long or anything else.");
                    }
                    try {
                        field.get(e).toString();
                    } catch (Exception ex) {
                        ignoreField = true;
                    }
                } else {
                    ignoreField = false;
                }
                if (!ignoreField) {
                    com.arm.framework.annotation.Field fieldValues = field.getAnnotation(com.arm.framework.annotation.Field.class);
                    Type type = fieldValues.type();
                    datas.add(new FieldData(field.getName(), field.get(e), type));
                } else {
                }
            }
        } catch (Exception ex) {
            throw new UnKnownException(ex.toString());
        }
        return datas;
    }

    /**
	 * @param e
	 * @return id if exists. -1 if doesnt exist,-2 if more then 1 exist
	 * @throws UnKnownException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws CommunicationsException
	 * 
	 */
    public int exists(E e) throws UnKnownException, CommunicationsException, ClassNotFoundException, SQLException {
        Class<? extends Object> className = e.getClass();
        String tableName = className.getSimpleName().toLowerCase();
        String query = "select * from " + tableName + " where ";
        Field[] declaredFields = Core.getDeclaredFields(className);
        ArrayList<FieldData> fieds = getFieldDatas(e, declaredFields);
        String where = "";
        for (FieldData fieldData : fieds) {
            where = setWhereBasedOnDataType(where, fieldData);
        }
        where = F.removeFromLast(where, 3).trim();
        query = query + where;
        ResultSet select = MySql.get().select(query);
        int Id = 0;
        int rowCount = 0;
        while (select.next()) {
            Id = select.getInt(1);
            rowCount++;
        }
        if (rowCount == 1) {
            return Id;
        } else if (rowCount > 1) {
            return -2;
        } else if (rowCount < 1) {
            return -1;
        }
        return -1;
    }

    /**
	 * @param where
	 * @param fieldData
	 * @return
	 */
    private String setWhereBasedOnDataType(String where, FieldData fieldData) {
        where += fieldData.getName() + "=";
        Object data = fieldData.getData();
        if (fieldData.getType().equals(Type.VARCHAR)) {
            where += "'" + data.toString() + "'" + " and ";
        } else if (fieldData.getType().equals(Type.INT)) {
            where += data.toString() + " and ";
        } else if (fieldData.getType().equals(Type.DATE)) {
            String s = dateToString(data);
            where += "'" + s + "'" + " and ";
        } else if (fieldData.getType().equals(Type.BOOLEAN)) {
            where += data.toString() + " and ";
        }
        return where;
    }

    /**
	 * 
	 * creates table
	 * 
	 * @param e
	 * @return
	 */
    protected String create(E e) {
        String tableName = e.getClass().getSimpleName().toLowerCase();
        String query = "CREATE TABLE " + tableName;
        Field[] declaredFields = Core.getDeclaredFields(e.getClass());
        query += "(";
        ArrayList<QueryData> queryDatas = extractFieldsFromClass(tableName, declaredFields);
        Field[] declaredFields2 = e.getClass().getSuperclass().getDeclaredFields();
        ArrayList<QueryData> datas = extractFieldsFromClass(tableName, declaredFields2);
        for (QueryData queryData : datas) {
            queryDatas.add(queryData);
        }
        String subQuery = "";
        for (QueryData queryData : queryDatas) {
            subQuery += queryData.getName() + " " + queryData.getType() + ",";
        }
        subQuery = F.removeFromLast(subQuery);
        query += subQuery + ")";
        p(query);
        return query;
    }

    /**
	 * @param tableName
	 * @param declaredFields
	 * @return
	 */
    private ArrayList<QueryData> extractFieldsFromClass(String tableName, Field[] declaredFields) {
        ArrayList<QueryData> queryDatas = new ArrayList<QueryData>();
        for (Field field : declaredFields) {
            if (!field.getName().startsWith("$")) {
                QueryData queryData = new QueryData();
                queryData.setName(field.getName());
                if (field.isAnnotationPresent(com.arm.framework.annotation.Field.class)) {
                    com.arm.framework.annotation.Field fieldValues = field.getAnnotation(com.arm.framework.annotation.Field.class);
                    if (fieldValues.type().equals(Type.VARCHAR)) {
                        queryData.addType(fieldValues.type().name() + "(" + fieldValues.size() + ")");
                    } else if (fieldValues.type().equals(Type.INT)) {
                        queryData.addType(fieldValues.type().name());
                    } else if (fieldValues.type().equals(Type.DATE)) {
                        queryData.addType(fieldValues.type().name());
                    } else if (fieldValues.type().equals(Type.BOOLEAN)) {
                        queryData.addType(fieldValues.type().name());
                    }
                    if (field.isAnnotationPresent(NotNull.class)) {
                        queryData.addType("NOT NULL");
                    }
                } else if (field.isAnnotationPresent(AutoKey.class)) {
                    queryData.addType("INT");
                    if (!queryData.HasType("NOT NULL")) {
                        if (field.isAnnotationPresent(NotNull.class)) {
                            queryData.addType("NOT NULL");
                        }
                    }
                    queryData.addType("AUTO_INCREMENT, PRIMARY KEY(id)");
                }
                generateRelation(tableName, field);
                queryDatas.add(queryData);
            }
        }
        return queryDatas;
    }

    /**
	 * @param tableName
	 * @param field
	 * 
	 *            creates : ALTER TABLE SYNTAX
	 */
    private void generateRelation(String tableName, Field field) {
        if (field.isAnnotationPresent(ConnectedTo.class)) {
            ConnectedTo connectedTo = field.getAnnotation(ConnectedTo.class);
            String simpleName = connectedTo.className().getSimpleName();
            p(simpleName);
            String foreignKeyName = "KEY_" + UUID.uuid(5);
            String alterQuery = "ALTER TABLE `" + tableName.toLowerCase() + "` ADD CONSTRAINT `" + foreignKeyName + "` FOREIGN KEY `" + foreignKeyName + "` (`" + field.getName() + "`) REFERENCES `" + simpleName.toLowerCase() + "` (`id`)";
            KeyData.add(alterQuery);
        }
    }

    /**
	 * @param userNameOrId
	 * @param className
	 * @param kind
	 * @return
	 * 
	 *         check right for the class and kind
	 * @throws UnKnownException
	 */
    protected boolean checkRight(Object userNameOrId, E className, Type.Right.Kind kind) throws UnKnownException {
        boolean iReturn = false;
        String query = "";
        if (userNameOrId.getClass().equals(Integer.class)) {
            query = "select tableName from permissions where userId=" + userNameOrId.toString() + " and type='" + kind.name() + "' and tableName='" + className.getClass().getSimpleName() + "'";
        } else if (userNameOrId.getClass().equals(String.class)) {
            query = "select p.tableName from permissions p,person c where p.type='" + kind.name() + "' and p.tableName='" + className.getClass().getSimpleName() + "' and c.firstName='" + userNameOrId.toString() + "' and c.id=p.userId";
        } else {
            throw new AssertionError("oopsie , userNameOrId should be either String or Integer");
        }
        if (query.equals("")) {
            throw new AssertionError("there is nothing in query");
        }
        p(query);
        try {
            ResultSet select = MySql.create().select(query);
            while (select.next()) {
                iReturn = true;
            }
        } catch (Exception e) {
            throw new UnKnownException(e.toString());
        }
        return iReturn;
    }

    /**
	 * @param simpleName
	 */
    private void p(String simpleName) {
        System.out.println(simpleName);
    }
}
