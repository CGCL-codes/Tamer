package com.creditors.dao;

import java.sql.*;
import java.util.Vector;
import com.core.util.BaseDAO;
import com.util.vo.IConnectionPropertiesVO;
import com.creditors.vo.*;

public class PaymentViewDAO extends BaseDAO {

    private String sql = "select * from cred.payment_v";

    public PaymentViewDAO(IConnectionPropertiesVO cp) {
        super(cp);
    }

    public PaymentViewVO[] getData() throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                PaymentViewVO[] paymentviewvo = fill(rs);
                return paymentviewvo;
            } finally {
                release(conn);
            }
        }
        return null;
    }

    private PaymentViewVO[] fill(ResultSet rs) throws SQLException {
        if (rs != null) {
            Vector<PaymentViewVO> data = new Vector<PaymentViewVO>();
            while (rs.next()) {
                PaymentViewVO vo = new PaymentViewVO();
                vo.setId(rs.getLong("id"));
                vo.setType(rs.getLong("type"));
                vo.setPrincipal(rs.getString("principal"));
                vo.setCurrencyBook(rs.getString("currency_book"));
                vo.setAmount(rs.getBigDecimal("amount"));
                vo.setCurrencyPay(rs.getString("currency_pay"));
                vo.setCountry(rs.getString("country"));
                vo.setCreationDate(rs.getDate("creation_date"));
                vo.setDueDate(rs.getDate("due_date"));
                vo.setPostAccount(rs.getString("post_account"));
                vo.setBankAccount(rs.getString("bank_account"));
                vo.setRecipient(rs.getString("recipient"));
                vo.setBeneficiary(rs.getString("beneficiary"));
                vo.setMessage4x35(rs.getString("message4x35"));
                vo.setOrderer(rs.getString("orderer"));
                data.add(vo);
            }
            PaymentViewVO[] array = new PaymentViewVO[data.size()];
            data.toArray(array);
            return array;
        }
        return null;
    }

    public void setData(PaymentViewVO[] paymentviewvo) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                PreparedStatement stmt = conn.prepareStatement("insert into cred.payment_v (type,principal,currency_book,amount,currency_pay,country,creation_date,due_date,post_account,bank_account,recipient,beneficiary,message4x35,orderer) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                for (int i = 0; i < paymentviewvo.length; i++) {
                    stmt.setLong(1, paymentviewvo[i].getType());
                    stmt.setString(2, paymentviewvo[i].getPrincipal());
                    stmt.setString(3, paymentviewvo[i].getCurrencyBook());
                    stmt.setBigDecimal(4, paymentviewvo[i].getAmount());
                    stmt.setString(5, paymentviewvo[i].getCurrencyPay());
                    stmt.setString(6, paymentviewvo[i].getCountry());
                    stmt.setDate(7, paymentviewvo[i].getCreationDate());
                    stmt.setDate(8, paymentviewvo[i].getDueDate());
                    stmt.setString(9, paymentviewvo[i].getPostAccount());
                    stmt.setString(10, paymentviewvo[i].getBankAccount());
                    stmt.setString(11, paymentviewvo[i].getRecipient());
                    stmt.setString(12, paymentviewvo[i].getBeneficiary());
                    stmt.setString(13, paymentviewvo[i].getMessage4x35());
                    stmt.setString(14, paymentviewvo[i].getOrderer());
                    stmt.executeUpdate();
                }
            } finally {
                release(conn);
            }
        }
    }

    public void updateData(PaymentViewVO paymentviewvo) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                PreparedStatement stmt = conn.prepareStatement("update cred.payment_v set type= ?,principal= ?,currency_book= ?,amount= ?,currency_pay= ?,country= ?,creation_date= ?,due_date= ?,post_account= ?,bank_account= ?,recipient= ?,beneficiary= ?,message4x35= ?,orderer= ? where id = ?");
                stmt.setLong(1, paymentviewvo.getType());
                stmt.setString(2, paymentviewvo.getPrincipal());
                stmt.setString(3, paymentviewvo.getCurrencyBook());
                stmt.setBigDecimal(4, paymentviewvo.getAmount());
                stmt.setString(5, paymentviewvo.getCurrencyPay());
                stmt.setString(6, paymentviewvo.getCountry());
                stmt.setDate(7, paymentviewvo.getCreationDate());
                stmt.setDate(8, paymentviewvo.getDueDate());
                stmt.setString(9, paymentviewvo.getPostAccount());
                stmt.setString(10, paymentviewvo.getBankAccount());
                stmt.setString(11, paymentviewvo.getRecipient());
                stmt.setString(12, paymentviewvo.getBeneficiary());
                stmt.setString(13, paymentviewvo.getMessage4x35());
                stmt.setString(14, paymentviewvo.getOrderer());
                stmt.setLong(15, paymentviewvo.getId());
                stmt.executeUpdate();
            } finally {
                release(conn);
            }
        }
    }

    public void deleteData(PaymentViewVO paymentviewvo) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("delete from cred.payment_v where id = " + paymentviewvo.getId());
            } finally {
                release(conn);
            }
        }
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
