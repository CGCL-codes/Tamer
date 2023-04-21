package org.jxstar.dm.ddl;

import java.util.List;
import java.util.Map;
import org.jxstar.dao.BaseDao;
import org.jxstar.dao.DaoParam;
import org.jxstar.dm.DdlField;
import org.jxstar.dm.DmException;
import org.jxstar.dm.DmParser;
import org.jxstar.util.MapUtil;
import org.jxstar.util.factory.FactoryUtil;

/**
 * SQLSERVER数据库在处理字段缺省值时与Oracle不同
 *
 * @author TonyTan
 * @version 1.0, 2012-4-9
 */
public class SqlServerDdlField extends DdlField {

    public SqlServerDdlField(DmParser parser) {
        super(parser);
    }

    /**
	 * 获取字段修改内容
	 * @param mpField -- 新字段内容
	 * @param mpFieldOld -- 原字段内容
	 * @return
	 */
    protected List<String> fieldModifySql(Map<String, String> mpField, Map<String, String> mpFieldOld) throws DmException {
        List<String> lssql = FactoryUtil.newList();
        String fieldName = MapUtil.getValue(mpField, "field_name");
        String fieldNameOld = MapUtil.getValue(mpFieldOld, "field_name");
        if (!fieldName.equalsIgnoreCase(fieldNameOld)) {
            mpField.put("old_field_name", fieldNameOld);
            lssql.add(_parser.parseTemplet("alter_column_name", mpField));
        }
        String fieldTitle = MapUtil.getValue(mpField, "field_title");
        String fieldTitleOld = MapUtil.getValue(mpFieldOld, "field_title");
        if (!fieldTitle.equals(fieldTitleOld)) {
            lssql.add(_parser.parseTemplet("alter_column_title", mpField));
        }
        String dataType = MapUtil.getValue(mpField, "data_type");
        String dataTypeOld = MapUtil.getValue(mpFieldOld, "data_type");
        String dataSize = MapUtil.getValue(mpField, "data_size");
        String dataSizeOld = MapUtil.getValue(mpFieldOld, "data_size");
        String dataScale = MapUtil.getValue(mpField, "data_scale");
        String dataScaleOld = MapUtil.getValue(mpFieldOld, "data_scale");
        if (!dataType.equals(dataTypeOld) || !dataSize.equals(dataSizeOld) || !dataScale.equals(dataScaleOld)) {
            lssql.add(_parser.parseTemplet("alter_column_type", mpField));
        }
        String defaultValue = MapUtil.getValue(mpField, "default_value");
        String defaultValueOld = MapUtil.getValue(mpFieldOld, "default_value");
        if (!defaultValue.equals(defaultValueOld)) {
            if (defaultValueOld.length() > 0) {
                String tableName = MapUtil.getValue(mpField, "table_name");
                String defaultName = getDefaultName(tableName, fieldName);
                if (defaultName.length() > 0) {
                    lssql.add("ALTER TABLE [dbo].[" + tableName + "] DROP CONSTRAINT [" + defaultName + "]\r\n");
                }
            }
            if (defaultValue.length() > 0) {
                lssql.add(_parser.parseTemplet("alter_column_default", mpField));
            }
        }
        String nullable = MapUtil.getValue(mpField, "nullable");
        String nullableOld = MapUtil.getValue(mpFieldOld, "nullable");
        if (!nullable.equals(nullableOld)) {
            lssql.add(_parser.parseTemplet("alter_column_nullable", mpField));
        }
        return lssql;
    }

    /**
	 * 取缺省值约束的名称
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
    private String getDefaultName(String tableName, String fieldName) {
        BaseDao dao = BaseDao.getInstance();
        String sql = "select a.id, b.colid from sysobjects a, syscolumns b " + "where a.id = b.id and upper(a.name) = ? and upper(b.name) = ?";
        DaoParam param = dao.createParam(sql);
        param.addStringValue(tableName.toUpperCase());
        param.addStringValue(fieldName.toUpperCase());
        Map<String, String> mp = dao.queryMap(param);
        if (mp.isEmpty()) return "";
        param.clearParam();
        sql = "select a.name from sysobjects a, sysconstraints b " + "where a.xtype = 'D' and a.id = b.constid and b.id = ? and b.colid = ?";
        param.setSql(sql);
        param.addStringValue(mp.get("id"));
        param.addStringValue(mp.get("colid"));
        mp = dao.queryMap(param);
        if (mp.isEmpty()) return "";
        return mp.get("name");
    }
}
