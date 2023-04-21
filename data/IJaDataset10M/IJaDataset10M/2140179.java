package com.be.dao;

import java.sql.*;
import java.util.Vector;
import com.be.vo.*;
import com.core.util.BaseDAO;

public class GlobalParameterDAO extends BaseDAO {

    private String sql = "select * from bo.obj_global_parameter";

    public GlobalParameterDAO(ConnectionPropertiesVO cp) {
        super(cp);
    }

    public GlobalParameterVO[] getData() throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                GlobalParameterVO[] globalparametervo = fill(rs);
                return globalparametervo;
            } finally {
                release(conn);
            }
        }
        return null;
    }

    private GlobalParameterVO[] fill(ResultSet rs) throws SQLException {
        if (rs != null) {
            Vector<GlobalParameterVO> data = new Vector<GlobalParameterVO>();
            while (rs.next()) {
                GlobalParameterVO vo = new GlobalParameterVO();
                vo.setId(rs.getLong("id"));
                vo.setName(rs.getString("name"));
                vo.setValue(rs.getString("value"));
                data.add(vo);
            }
            GlobalParameterVO[] array = new GlobalParameterVO[data.size()];
            data.toArray(array);
            return array;
        }
        return null;
    }

    public void setData(GlobalParameterVO[] globalparametervo) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                PreparedStatement stmt = conn.prepareStatement("insert into bo.obj_global_parameter (id,name,value) values (?,?, ?)");
                for (int i = 0; i < globalparametervo.length; i++) {
                    stmt.setLong(1, globalparametervo[i].getId());
                    stmt.setString(2, globalparametervo[i].getName());
                    stmt.setString(3, globalparametervo[i].getValue());
                    stmt.executeUpdate();
                }
            } finally {
                release(conn);
            }
        }
    }

    public void updateData(GlobalParameterVO globalparametervo) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                PreparedStatement stmt = conn.prepareStatement("update bo.obj_global_parameter set name= ?,value= ? where id = ?");
                stmt.setString(1, globalparametervo.getName());
                stmt.setString(2, globalparametervo.getValue());
                stmt.setLong(3, globalparametervo.getId());
                stmt.executeUpdate();
            } finally {
                release(conn);
            }
        }
    }

    public void deleteData(GlobalParameterVO globalparametervo) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("delete from bo.obj_global_parameter where id = " + globalparametervo.getId());
            } finally {
                release(conn);
            }
        }
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
