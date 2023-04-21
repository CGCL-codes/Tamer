package com.debitors.dao;

import java.sql.*;
import java.util.Vector;
import com.core.util.BaseDAO;
import com.util.vo.IConnectionPropertiesVO;
import com.debitors.vo.*;

public class PFTR4DAO extends BaseDAO {

    private String sql = "select * from deb.esr_tr4";

    public PFTR4DAO(IConnectionPropertiesVO cp) {
        super(cp);
    }

    public PFTR4VO[] getData() throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                PFTR4VO[] pftr4vo = fill(rs);
                return pftr4vo;
            } finally {
                release(conn);
            }
        }
        return null;
    }

    private PFTR4VO[] fill(ResultSet rs) throws SQLException {
        if (rs != null) {
            Vector<PFTR4VO> data = new Vector<PFTR4VO>();
            while (rs.next()) {
                PFTR4VO vo = new PFTR4VO();
                vo.setId(rs.getLong("id"));
                vo.setType(rs.getString("type"));
                vo.setTa(rs.getString("ta"));
                vo.setOrigin(rs.getString("origin"));
                vo.setDeliveryKind(rs.getString("delivery_kind"));
                vo.setCustomerNr(rs.getString("customer_nr"));
                vo.setSortKey(rs.getString("sort_key"));
                vo.setCurrencyCode(rs.getString("currency_code"));
                vo.setAmount(rs.getBigDecimal("amount"));
                vo.setTransactionCount(rs.getLong("transaction_count"));
                vo.setCreationDate(rs.getDate("creation_date"));
                vo.setCurrencyCode2(rs.getString("currency_code_2"));
                vo.setCost(rs.getBigDecimal("cost"));
                data.add(vo);
            }
            PFTR4VO[] array = new PFTR4VO[data.size()];
            data.toArray(array);
            return array;
        }
        return null;
    }

    public void setData(PFTR4VO[] pftr4vo) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                PreparedStatement stmt = conn.prepareStatement("insert into deb.esr_tr4 (type,ta,origin,delivery_kind,customer_nr,sort_key,currency_code,amount,transaction_count,creation_date,currency_code_2,cost) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                for (int i = 0; i < pftr4vo.length; i++) {
                    stmt.setString(1, pftr4vo[i].getType());
                    stmt.setString(2, pftr4vo[i].getTa());
                    stmt.setString(3, pftr4vo[i].getOrigin());
                    stmt.setString(4, pftr4vo[i].getDeliveryKind());
                    stmt.setString(5, pftr4vo[i].getCustomerNr());
                    stmt.setString(6, pftr4vo[i].getSortKey());
                    stmt.setString(7, pftr4vo[i].getCurrencyCode());
                    stmt.setBigDecimal(8, pftr4vo[i].getAmount());
                    stmt.setLong(9, pftr4vo[i].getTransactionCount());
                    stmt.setDate(10, pftr4vo[i].getCreationDate());
                    stmt.setString(11, pftr4vo[i].getCurrencyCode2());
                    stmt.setBigDecimal(12, pftr4vo[i].getCost());
                    stmt.executeUpdate();
                }
            } finally {
                release(conn);
            }
        }
    }

    public void updateData(PFTR4VO pftr4vo) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                PreparedStatement stmt = conn.prepareStatement("update deb.esr_tr4 set type= ?,ta= ?,origin= ?,delivery_kind= ?,customer_nr= ?,sort_key= ?,currency_code= ?,amount= ?,transaction_count= ?,creation_date= ?,currency_code_2= ?,cost= ? where id = ?");
                stmt.setString(1, pftr4vo.getType());
                stmt.setString(2, pftr4vo.getTa());
                stmt.setString(3, pftr4vo.getOrigin());
                stmt.setString(4, pftr4vo.getDeliveryKind());
                stmt.setString(5, pftr4vo.getCustomerNr());
                stmt.setString(6, pftr4vo.getSortKey());
                stmt.setString(7, pftr4vo.getCurrencyCode());
                stmt.setBigDecimal(8, pftr4vo.getAmount());
                stmt.setLong(9, pftr4vo.getTransactionCount());
                stmt.setDate(10, pftr4vo.getCreationDate());
                stmt.setString(11, pftr4vo.getCurrencyCode2());
                stmt.setBigDecimal(12, pftr4vo.getCost());
                stmt.setLong(13, pftr4vo.getId());
                stmt.executeUpdate();
            } finally {
                release(conn);
            }
        }
    }

    public void deleteData(PFTR4VO pftr4vo) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("delete from deb.esr_tr4 where id = " + pftr4vo.getId());
            } finally {
                release(conn);
            }
        }
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
