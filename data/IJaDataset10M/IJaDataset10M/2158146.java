package org.jxstar.dm.compare;

import java.util.List;
import java.util.Map;
import org.jxstar.dao.BaseDao;
import org.jxstar.dao.DaoParam;
import org.jxstar.dm.DmException;
import org.jxstar.dm.DmFactory;
import org.jxstar.dm.MetaData;
import org.jxstar.dm.util.DmUtil;
import org.jxstar.util.factory.FactoryUtil;
import org.jxstar.util.resource.JsMessage;

/**
 * 比较数据库配置信息与数据库系统表中的信息的差异。
 *
 * @author TonyTan
 * @version 1.0, 2010-12-26
 */
public class CompareDB extends CompareData {

    private BaseDao _dao = BaseDao.getInstance();

    private String _dsname = "default";

    private MetaData _metaData = DmFactory.getMetaData(_dsname);

    /**
	 * 比较表配置
	 * @return
	 */
    public List<String> compareTable() throws DmException {
        List<Map<String, String>> lsField = FactoryUtil.newList();
        lsField.add(fieldMap("table_name", "string"));
        lsField.add(fieldMap("table_title", "string"));
        String srcTable = "v_table_info";
        String srcKeyField = "table_name";
        String tagTable = "dm_tablecfg";
        String tagKeyField = "table_name";
        String srcsql = "select * from v_table_info where table_name in " + "(select table_name from dm_table where table_type < '9') order by table_name";
        DaoParam srcParam = _dao.createParam(srcsql);
        List<Map<String, String>> lsSrc = _dao.query(srcParam);
        String tagsql = "select * from dm_tablecfg where table_type < '9' order by table_name";
        DaoParam tagParam = _dao.createParam(tagsql);
        List<Map<String, String>> lsTag = _dao.query(tagParam);
        List<String> lssql = FactoryUtil.newList();
        lssql.add("--" + JsMessage.getValue("comparedb.comptable") + "\r\n");
        List<String> lstable = compareCfg(srcTable, srcKeyField, tagTable, tagKeyField, lsSrc, lsTag, lsField);
        if (lstable.isEmpty()) {
            lssql.add("--" + JsMessage.getValue("comparedata.nodiff") + "\r\n\r\n");
        } else {
            lssql.addAll(lstable);
        }
        lssql.add("--" + JsMessage.getValue("comparedb.compfield") + "\r\n");
        List<String> lsfield = FactoryUtil.newList();
        for (int i = 0, n = lsSrc.size(); i < n; i++) {
            Map<String, String> mpsrc = lsSrc.get(i);
            String tableName = mpsrc.get("table_name");
            List<String> lstmp = compareField(tableName);
            if (!lstmp.isEmpty()) {
                lsfield.add("--" + JsMessage.getValue("comparedb.fielddiff", tableName) + "\r\n");
                lsfield.addAll(lstmp);
            }
        }
        if (lsfield.isEmpty()) {
            lssql.add("--" + JsMessage.getValue("comparedata.nodiff") + "\r\n\r\n");
        } else {
            lssql.addAll(lsfield);
        }
        lssql.add("--" + JsMessage.getValue("comparedb.compindex") + "\r\n");
        List<String> lsindex = FactoryUtil.newList();
        for (int i = 0, n = lsSrc.size(); i < n; i++) {
            Map<String, String> mpsrc = lsSrc.get(i);
            String tableName = mpsrc.get("table_name");
            List<String> lstmp = compareIndex(tableName);
            if (!lstmp.isEmpty()) {
                lsindex.add("--" + JsMessage.getValue("comparedb.indexdiff", tableName) + "\r\n");
                lsindex.addAll(lstmp);
            }
        }
        if (lsindex.isEmpty()) {
            lssql.add("--" + JsMessage.getValue("comparedata.nodiff") + "\r\n\r\n");
        } else {
            lssql.addAll(lsindex);
        }
        lssql.add("--" + JsMessage.getValue("comparedb.compkey") + "\r\n");
        lsTag = _dao.query(tagParam);
        List<String> lskey = FactoryUtil.newList();
        for (int i = 0, n = lsTag.size(); i < n; i++) {
            Map<String, String> mptag = lsTag.get(i);
            String tableName = mptag.get("table_name");
            String keyField = mptag.get("key_field");
            Map<String, String> mpKey = _metaData.getKeyMeta(tableName, _dsname);
            String dbkey = "";
            if (mpKey != null && !mpKey.isEmpty()) {
                dbkey = mpKey.get("key_field");
            }
            if (!keyField.equals(dbkey)) {
                lskey.add("--" + JsMessage.getValue("comparedb.keydiff", tableName) + "\r\n");
                lskey.add("--" + JsMessage.getValue("comparedb.keydb", dbkey, keyField) + "\r\n");
            }
        }
        if (lskey.isEmpty()) {
            lssql.add("--" + JsMessage.getValue("comparedata.nodiff") + "\r\n\r\n");
        } else {
            lssql.addAll(lskey);
        }
        return lssql;
    }

    /**
	 * 比较字段配置
	 * @param tableName -- 表名
	 * @return
	 */
    public List<String> compareField(String tableName) throws DmException {
        List<Map<String, String>> lsField = FactoryUtil.newList();
        lsField.add(fieldMap("field_name", "string"));
        lsField.add(fieldMap("field_title", "string"));
        lsField.add(fieldMap("data_type", "string"));
        lsField.add(fieldMap("data_size", "int"));
        lsField.add(fieldMap("data_scale", "int"));
        lsField.add(fieldMap("nullable", "string"));
        lsField.add(fieldMap("default_value", "string"));
        String srcTable = "v_column_info";
        String srcKeyField = "field_name";
        String tagTable = "dm_fieldcfg";
        String tagKeyField = "field_name";
        String srcsql = "select * from v_column_info where table_name = ? order by field_name";
        DaoParam srcParam = _dao.createParam(srcsql);
        srcParam.addStringValue(tableName);
        List<Map<String, String>> lsSrc = _dao.query(srcParam);
        lsSrc = clearDefaultChar(lsSrc);
        String tableId = TableConfig.getTableId(tableName);
        if (tableId == null || tableId.length() == 0) {
            throw new DmException(JsMessage.getValue("comparedb.cfgidnull"), tableName);
        }
        String tagsql = "select * from dm_fieldcfg where table_id = ? order by field_name";
        DaoParam tagParam = _dao.createParam(tagsql);
        tagParam.addStringValue(tableId);
        List<Map<String, String>> lsTag = _dao.query(tagParam);
        List<String> lssql = compareCfg(srcTable, srcKeyField, tagTable, tagKeyField, lsSrc, lsTag, lsField);
        return addTableWhere(lssql, tableId);
    }

    /**
	 * 比较索引配置
	 * @param tableName -- 表名
	 * @return
	 */
    public List<String> compareIndex(String tableName) throws DmException {
        List<Map<String, String>> lsField = FactoryUtil.newList();
        lsField.add(fieldMap("index_name", "string"));
        lsField.add(fieldMap("index_field", "string"));
        lsField.add(fieldMap("isunique", "string"));
        String srcTable = "";
        String srcKeyField = "index_name";
        String tagTable = "dm_indexcfg";
        String tagKeyField = "index_name";
        List<Map<String, String>> lsSrc = _metaData.getIndexMeta(tableName, _dsname);
        String tableId = TableConfig.getTableId(tableName);
        if (tableId == null || tableId.length() == 0) {
            throw new DmException(JsMessage.getValue("comparedb.cfgidnull"), tableName);
        }
        String tagsql = "select * from dm_indexcfg where table_id = ? order by index_name";
        DaoParam tagParam = _dao.createParam(tagsql);
        tagParam.addStringValue(tableId);
        List<Map<String, String>> lsTag = _dao.query(tagParam);
        List<String> lssql = compareCfg(srcTable, srcKeyField, tagTable, tagKeyField, lsSrc, lsTag, lsField);
        return addTableWhere(lssql, tableId);
    }

    /**
	 * 比较一个表中的数据差异，并生成更新SQL
	 * @param srcTable -- 来源表名
	 * @param srcKeyField -- 来源主键名
	 * @param tagTable -- 目标表名
	 * @param tagKeyField -- 目标主键名
	 * @param lsField -- 需要比较的字段
	 * @return
	 * @throws DmException
	 */
    public List<String> compareCfg(String srcTable, String srcKeyField, String tagTable, String tagKeyField, List<Map<String, String>> lsSrcData, List<Map<String, String>> lsTagData, List<Map<String, String>> lsField) throws DmException {
        boolean isexist = false;
        List<String> lssql = FactoryUtil.newList();
        for (int i = 0, n = lsSrcData.size(); i < n; i++) {
            Map<String, String> mpSrc = lsSrcData.get(i);
            String srcKey = mpSrc.get(srcKeyField);
            for (int j = 0, m = lsTagData.size(); j < m; j++) {
                Map<String, String> mpTag = lsTagData.get(j);
                String tagKey = mpTag.get(tagKeyField);
                if (srcKey.equals(tagKey)) {
                    lssql.addAll(updateSql(lsField, mpSrc, mpTag, tagTable, tagKeyField, tagKey));
                    lsTagData.remove(j);
                    isexist = true;
                    break;
                }
            }
            if (!isexist) {
                lssql.add(insertSql(lsField, mpSrc, tagTable));
            }
            isexist = false;
        }
        for (int i = 0, n = lsTagData.size(); i < n; i++) {
            Map<String, String> mpTag = lsTagData.get(i);
            String tagKey = mpTag.get(tagKeyField);
            lssql.add(deleteSql(tagTable, tagKeyField, tagKey));
        }
        return lssql;
    }

    /**
     * 来源值与目标值比较的方法，返回true表示相同，子类可以继承，修改比较算法
     * @param srcValue -- 来源值
     * @param tagValue -- 目标值
     * @param mpField -- 字段信息
     * @return
     */
    protected boolean compareValue(String srcValue, String tagValue, Map<String, String> mpField) throws DmException {
        if (mpField == null) {
            throw new DmException(JsMessage.getValue("comparedata.paramnull"));
        }
        if (srcValue == null) srcValue = "";
        if (tagValue == null) tagValue = "";
        String fieldName = mpField.get("field_name");
        if (fieldName.equals("data_type") && _metaData != null) {
            srcValue = _metaData.getDataType(srcValue);
            if (srcValue.equals("number") || tagValue.equals("int")) {
                return true;
            }
        } else if (fieldName.equals("data_scale")) {
            if (srcValue.length() == 0) srcValue = "0";
            if (tagValue.length() == 0) tagValue = "0";
        }
        if (fieldName.equals("default_value")) {
        }
        return srcValue.length() == tagValue.length() && srcValue.equals(tagValue);
    }

    /**
     * 构建字段信息对象
     * @param fieldName -- 字段名
     * @param dataType -- 数据类型
     * @return
     */
    private Map<String, String> fieldMap(String fieldName, String dataType) {
        Map<String, String> mpField = FactoryUtil.newMap();
        mpField.put("field_name", fieldName);
        mpField.put("data_type", dataType);
        return mpField;
    }

    /**
     * 给SQL语句添加表ID的过滤语句
     * @param lssql
     * @param tableId
     * return
     */
    private List<String> addTableWhere(List<String> lssql, String tableId) {
        if (lssql == null || lssql.isEmpty()) return lssql;
        List<String> lsnew = FactoryUtil.newList();
        for (int i = 0; i < lssql.size(); i++) {
            String sql = lssql.get(i);
            if (sql.trim().charAt(0) == '-') {
                lsnew.add(sql);
                continue;
            }
            int wi = sql.indexOf(" where ");
            if (wi >= 0) {
                sql = sql.substring(0, wi) + " where table_id = '" + tableId + "' and " + sql.substring(wi + 7, sql.length());
            }
            lsnew.add(sql);
        }
        return lsnew;
    }

    /**
     * Oracle数据库用，取到的缺省值后面有换行符号
     * @param lsData
     * @return
     */
    private List<Map<String, String>> clearDefaultChar(List<Map<String, String>> lsData) {
        if (lsData == null || lsData.isEmpty()) return lsData;
        for (Map<String, String> mpData : lsData) {
            String key = "default_value";
            String value = mpData.get(key);
            if (value != null) {
                value = value.replaceAll("\n", "");
            } else {
                value = "";
            }
            if (value.length() > 0) {
                if (DmUtil.hasYinHao(value)) {
                    value = value.substring(1, value.length() - 1);
                } else if (value.equals("null")) {
                    value = "";
                }
            }
            mpData.put(key, value);
        }
        return lsData;
    }
}
