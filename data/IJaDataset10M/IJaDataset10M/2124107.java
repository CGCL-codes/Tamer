package coyousoft.jiuhuabook.entity;

import coyousoft.mvc.util.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * 出版社信息表
 *
 * @author SCM
 *
 */
public class PressInfo {

    private Long pressId;

    private String pressName;

    private String pressFirstChar;

    private String pressRemark;

    public PressInfo() {
    }

    public PressInfo(Long pressId) {
        this.pressId = pressId;
    }

    public void setPressId(Long pressId) {
        this.pressId = pressId;
    }

    public Long getPressId() {
        return pressId;
    }

    public void setPressName(String pressName) {
        this.pressName = pressName;
    }

    public String getPressName() {
        return pressName;
    }

    public void setPressFirstChar(String pressFirstChar) {
        this.pressFirstChar = pressFirstChar;
    }

    public String getPressFirstChar() {
        return pressFirstChar;
    }

    public void setPressRemark(String pressRemark) {
        this.pressRemark = pressRemark;
    }

    public String getPressRemark() {
        return pressRemark;
    }

    public PressInfo fill(Map<String, Object> eachRow) {
        Object obj = null;
        if ((obj = eachRow.get("PRESS_ID")) != null) {
            pressId = ((Number) obj).longValue();
        }
        if ((obj = eachRow.get("PRESS_NAME")) != null) {
            pressName = (String) obj;
        }
        if ((obj = eachRow.get("PRESS_FIRST_CHAR")) != null) {
            pressFirstChar = (String) obj;
        }
        if ((obj = eachRow.get("PRESS_REMARK")) != null) {
            pressRemark = (String) obj;
        }
        return this;
    }

    public PressInfo fill(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int numberOfColumns = rsmd.getColumnCount();
        Map<String, Object> map = new HashMap<String, Object>();
        Object obj = null;
        int sqlType = 0;
        for (int i = 1; i <= numberOfColumns; i++) {
            sqlType = rsmd.getColumnType(i);
            if (sqlType == java.sql.Types.DATE) {
                obj = rs.getTimestamp(i);
            } else if (sqlType == java.sql.Types.CLOB) {
                Clob clob = rs.getClob(i);
                obj = (clob != null ? clob.getSubString(1, (int) clob.length()) : null);
            } else {
                obj = rs.getObject(i);
            }
            map.put(rsmd.getColumnName(i).toUpperCase(), obj);
        }
        return fill(map);
    }

    public PressInfo fill(RequestWrapper wrapper) throws Exception {
        pressId = wrapper.getLong("pressId");
        pressName = wrapper.getString("pressName");
        pressFirstChar = wrapper.getString("pressFirstChar");
        pressRemark = wrapper.getString("pressRemark");
        return this;
    }
}
