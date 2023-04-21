package org.jxstar.dao;

import java.util.List;
import java.util.Map;
import org.jxstar.util.factory.FactoryUtil;

/**
 * 读取数据模型配置信息，用于DmDao类。
 *
 * @author TonyTan
 * @version 1.0, 2011-1-26
 */
public class DmDaoUtil {

    private static BaseDao _dao = BaseDao.getInstance();

    /**
	 * 取指定表的主键字段名
	 * @param tableName -- 表名
	 * @return
	 */
    public static String getKeyField(String tableName) {
        String sql = "select key_field from dm_table where table_name = ?";
        DaoParam param = _dao.createParam(sql);
        param.addStringValue(tableName);
        Map<String, String> mpTable = _dao.queryMap(param);
        return mpTable.get("key_field");
    }

    /**
	 * 取指定表的字段名与数据类型的MAP对象
	 * @param tableName -- 表名
	 * @return
	 */
    public static Map<String, String> getParamType(String tableName) {
        Map<String, String> mpType = FactoryUtil.newMap();
        List<Map<String, String>> lsField = queryField(tableName);
        for (int i = 0, n = lsField.size(); i < n; i++) {
            Map<String, String> mpField = lsField.get(i);
            String fieldName = mpField.get("field_name");
            String dataType = mpField.get("data_type");
            String paramType = cvtDataType(dataType);
            mpType.put(fieldName, paramType);
        }
        return mpType;
    }

    /**
	 * 取所有字段构建的SQL，格式如：field1, field2, field3 ...
	 * @param tableName -- 表名
	 * @return
	 */
    public static String getFieldSql(String tableName) {
        StringBuilder sbField = new StringBuilder();
        List<Map<String, String>> lsField = queryField(tableName);
        for (int i = 0, n = lsField.size(); i < n; i++) {
            Map<String, String> mpField = lsField.get(i);
            String fieldName = mpField.get("field_name");
            sbField.append(fieldName).append(", ");
        }
        String sql = "";
        if (sbField.length() > 0) {
            sql = sbField.substring(0, sbField.length() - 2);
        }
        return sql;
    }

    /**
	 * 从数据模型中查询表的字段列表
	 * @param tableName -- 表名
	 * @return
	 */
    private static List<Map<String, String>> queryField(String tableName) {
        String sql = "select field_name, data_type from dm_field where table_id in " + "(select table_id from dm_table where table_name = ?) order by field_index";
        DaoParam param = _dao.createParam(sql);
        param.addStringValue(tableName);
        return _dao.query(param);
    }

    /**
	 * 把数据模型中的数据类型转换为DAO操作中的参数类型。
	 * @param dataType -- 数据类型
	 * @return
	 */
    private static String cvtDataType(String dataType) {
        if (dataType.indexOf("char") >= 0) {
            return "string";
        } else if (dataType.indexOf("date") >= 0) {
            return "date";
        } else if (dataType.indexOf("int") >= 0) {
            return "int";
        } else if (dataType.indexOf("number") >= 0) {
            return "double";
        }
        return "string";
    }
}
