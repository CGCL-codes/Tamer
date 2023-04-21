package com.be.dao;

import java.sql.*;
import java.util.Vector;
import com.util.vo.IConnectionPropertiesVO;
import com.be.vo.*;
import com.core.util.BaseDAO;

public class ContactRelationDAO extends BaseDAO {

    private String sql = "select * from bo.obj_contact_relation";

    public ContactRelationDAO(IConnectionPropertiesVO cp) {
        super(cp);
    }

    public ContactRelationVO[] getData() throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                ContactRelationVO[] contactrelationvo = fill(rs);
                return contactrelationvo;
            } finally {
                release(conn);
            }
        }
        return null;
    }

    private ContactRelationVO[] fill(ResultSet rs) throws SQLException {
        if (rs != null) {
            Vector<ContactRelationVO> data = new Vector<ContactRelationVO>();
            while (rs.next()) {
                ContactRelationVO vo = new ContactRelationVO();
                vo.setId(rs.getLong("id"));
                vo.setTypeID(rs.getLong("type_id"));
                vo.setTypeText(rs.getString("type_text"));
                vo.setParentID(rs.getLong("parent_id"));
                vo.setChildID(rs.getLong("child_id"));
                vo.setModified(rs.getTimestamp("modified"));
                vo.setValidFrom(rs.getDate("valid_from"));
                vo.setValidTo(rs.getDate("valid_to"));
                data.add(vo);
            }
            ContactRelationVO[] array = new ContactRelationVO[data.size()];
            data.toArray(array);
            return array;
        }
        return null;
    }

    public void setData(ContactRelationVO[] contactrelationvo) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                PreparedStatement stmt = conn.prepareStatement("insert into bo.obj_contact_relation (type_id,type_text,parent_id,child_id,modified,valid_from,valid_to) values (?, ?, ?, ?, ?, ?, ?)");
                for (int i = 0; i < contactrelationvo.length; i++) {
                    stmt.setLong(1, contactrelationvo[i].getTypeID());
                    stmt.setString(2, contactrelationvo[i].getTypeText());
                    stmt.setLong(3, contactrelationvo[i].getParentID());
                    stmt.setLong(4, contactrelationvo[i].getChildID());
                    stmt.setTimestamp(5, contactrelationvo[i].getModified());
                    stmt.setDate(6, contactrelationvo[i].getValidFrom());
                    stmt.setDate(7, contactrelationvo[i].getValidTo());
                    stmt.executeUpdate();
                }
            } finally {
                release(conn);
            }
        }
    }

    public void updateData(ContactRelationVO contactrelationvo) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                PreparedStatement stmt = conn.prepareStatement("update bo.obj_contact_relation set type_id= ?,type_text= ?,parent_id= ?,child_id= ?,modified= ?,valid_from= ?,valid_to= ? where id = ?");
                stmt.setLong(1, contactrelationvo.getTypeID());
                stmt.setString(2, contactrelationvo.getTypeText());
                stmt.setLong(3, contactrelationvo.getParentID());
                stmt.setLong(4, contactrelationvo.getChildID());
                stmt.setTimestamp(5, contactrelationvo.getModified());
                stmt.setDate(6, contactrelationvo.getValidFrom());
                stmt.setDate(7, contactrelationvo.getValidTo());
                stmt.setLong(8, contactrelationvo.getId());
                stmt.executeUpdate();
            } finally {
                release(conn);
            }
        }
    }

    public void deleteData(ContactRelationVO contactrelationvo) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("delete from bo.obj_contact_relation where id = " + contactrelationvo.getId());
            } finally {
                release(conn);
            }
        }
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
