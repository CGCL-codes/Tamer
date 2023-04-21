package com.dynamicobjects.dd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * 
 * @author Hern�n Lorenzini
 */
public class DDAttributeInfoDAO {

    private static final String ATTRIBUTEID = "attributeId";

    private static final String NAMEFIELD = "nameField";

    private static final String DDTYPE = "ddtype";

    private static final String PERSISTENT = "persistent";

    private static final String ALIAS = "alias";

    private static final String KEYORDER = "keyOrder";

    private static final String CLASSNAME = "className";

    private static final String UPDATEABLE = "updateable";

    private static final String LENGTHPRECISION = "lengthPrecision";

    private static final String SCALE = "scale";

    private static final String DEFAULTVALUE = "defaultValue";

    private static final String AUTOINCREMENT = "autoIncrement";

    private static final String REQUIRED = "required";

    private static final String OVERRRIDE = "override";

    private static final String HIDDENMETHOD = "hiddenMethod";

    private static final String INTERNAL = "internal";

    private static final String DDATTRIBUTEINFO_SELECT = "SELECT attributeId, nameField, ddtype, persistent, alias, keyOrder, className, updateable, lengthPrecision, scale, defaultValue, autoIncrement, required, override, hiddenMethod, internal FROM ddattributeinfo WHERE ddObjectId = ? ORDER BY attributeId";

    private SimpleJdbcTemplate jdbcTemplate;

    public SimpleJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DDAttributeInfo> getDDAttributes(String ddObjectId) {
        List<DDAttributeInfo> matches = jdbcTemplate.query(DDATTRIBUTEINFO_SELECT, new RowMapper<DDAttributeInfo>() {

            public DDAttributeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                String nameField = rs.getString(NAMEFIELD);
                String alias = rs.getString(ALIAS);
                DDAttributeType ddtype = DDAttributeType.valueOf(rs.getString(DDTYPE));
                DDAttributeInfo row = new DDAttributeInfo(nameField, alias, ddtype);
                row.setAttributeId(rs.getInt(ATTRIBUTEID));
                row.setPersistent(rs.getBoolean(PERSISTENT));
                row.setKeyOrder(rs.getInt(KEYORDER));
                row.setClassName(rs.getString(CLASSNAME));
                row.setUpdateable(rs.getBoolean(UPDATEABLE));
                row.setLengthPrecision(rs.getInt(LENGTHPRECISION));
                row.setScale(rs.getInt(SCALE));
                row.setDefaultValue(rs.getString(DEFAULTVALUE));
                row.setAutoIncrement(rs.getBoolean(AUTOINCREMENT));
                row.setRequired(rs.getBoolean(REQUIRED));
                row.setOverride(rs.getBoolean(OVERRRIDE));
                row.setHiddenMethod(rs.getBoolean(HIDDENMETHOD));
                row.setInternal(rs.getBoolean(INTERNAL));
                return row;
            }
        }, ddObjectId);
        return matches;
    }
}
