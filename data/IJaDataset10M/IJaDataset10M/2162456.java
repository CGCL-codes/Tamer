package com.be.dao;

import java.sql.*;
import java.util.Vector;
import com.util.vo.IConnectionPropertiesVO;
import com.be.vo.*;
import com.core.util.BaseDAO;

public class LedgerSaldoDAO extends BaseDAO {

    private String sql = "select * from bo.ledger_saldo";

    public LedgerSaldoDAO(IConnectionPropertiesVO cp) {
        super(cp);
    }

    public LedgerSaldoVO[] getData() throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                LedgerSaldoVO[] ledgersaldovo = fill(rs);
                return ledgersaldovo;
            } finally {
                release(conn);
            }
        }
        return null;
    }

    private LedgerSaldoVO[] fill(ResultSet rs) throws SQLException {
        if (rs != null) {
            Vector<LedgerSaldoVO> data = new Vector<LedgerSaldoVO>();
            while (rs.next()) {
                LedgerSaldoVO vo = new LedgerSaldoVO();
                vo.setId(rs.getLong("id"));
                vo.setTstamp(rs.getTimestamp("tstamp"));
                vo.setListType(rs.getLong("list_type"));
                vo.setListSubType(rs.getLong("list_sub_type"));
                vo.setEvalStart(rs.getDate("eval_start"));
                vo.setEvalDate(rs.getDate("eval_date"));
                vo.setLedgerID(rs.getLong("ledger_id"));
                vo.setAccountID(rs.getString("account_id"));
                vo.setLedgerName(rs.getString("ledger_name"));
                vo.setLedgerType(rs.getString("ledger_type"));
                vo.setAmountStart(rs.getBigDecimal("amount_start"));
                vo.setAmountPrevious(rs.getBigDecimal("amount_previous"));
                vo.setAmountDebit(rs.getBigDecimal("amount_debit"));
                vo.setAmountCredit(rs.getBigDecimal("amount_credit"));
                vo.setAmount(rs.getBigDecimal("amount"));
                vo.setCurrencyID(rs.getShort("currency_id"));
                vo.setExchangeRate(rs.getDouble("exchange_rate"));
                vo.setAmountRef(rs.getBigDecimal("amount_real"));
                vo.setValidFrom(rs.getDate("valid_from"));
                vo.setValidTo(rs.getDate("valid_to"));
                data.add(vo);
            }
            LedgerSaldoVO[] array = new LedgerSaldoVO[data.size()];
            data.toArray(array);
            return array;
        }
        return null;
    }

    public void setData(LedgerSaldoVO[] ledgersaldovo) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                PreparedStatement stmt = conn.prepareStatement("insert into bo.ledger_saldo (tstamp,list_type,list_sub_type,eval_start,eval_date,ledger_id,account_id,ledger_name,ledger_type,amount_start,amount_previous,amount_debit,amount_credit,amount,currency_id,exchange_rate,amount_ref,valid_from,valid_to) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                for (int i = 0; i < ledgersaldovo.length; i++) {
                    stmt.setTimestamp(1, ledgersaldovo[i].getTstamp());
                    stmt.setLong(2, ledgersaldovo[i].getListType());
                    stmt.setLong(3, ledgersaldovo[i].getListSubType());
                    stmt.setDate(4, ledgersaldovo[i].getEvalStart());
                    stmt.setDate(5, ledgersaldovo[i].getEvalDate());
                    stmt.setLong(6, ledgersaldovo[i].getLedgerID());
                    stmt.setString(7, ledgersaldovo[i].getAccountID());
                    stmt.setString(8, ledgersaldovo[i].getLedgerName());
                    stmt.setString(9, ledgersaldovo[i].getLedgerType());
                    stmt.setBigDecimal(10, ledgersaldovo[i].getAmountStart());
                    stmt.setBigDecimal(11, ledgersaldovo[i].getAmountPrevious());
                    stmt.setBigDecimal(12, ledgersaldovo[i].getAmountDebit());
                    stmt.setBigDecimal(13, ledgersaldovo[i].getAmountCredit());
                    stmt.setBigDecimal(14, ledgersaldovo[i].getAmount());
                    stmt.setShort(15, ledgersaldovo[i].getCurrencyID());
                    stmt.setDouble(16, ledgersaldovo[i].getExchangeRate());
                    stmt.setBigDecimal(17, ledgersaldovo[i].getAmountRef());
                    stmt.setDate(18, ledgersaldovo[i].getValidFrom());
                    stmt.setDate(19, ledgersaldovo[i].getValidTo());
                    stmt.executeUpdate();
                }
            } finally {
                release(conn);
            }
        }
    }

    public void updateData(LedgerSaldoVO ledgersaldovo) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                PreparedStatement stmt = conn.prepareStatement("update bo.ledger_saldo set tstamp= ?,list_type= ?,list_sub_type= ?,eval_start= ?,eval_date= ?,ledger_id= ?,account_id= ?,ledger_name= ?,ledger_type= ?,amount_start= ?,amount_previous= ?,amount_debit= ?,amount_credit= ?,amount= ?,currency_id= ?,exchange_rate= ?,amount_ref= ?,valid_from= ?,valid_to= ? where id = ?");
                stmt.setTimestamp(1, ledgersaldovo.getTstamp());
                stmt.setLong(2, ledgersaldovo.getListType());
                stmt.setLong(3, ledgersaldovo.getListSubType());
                stmt.setDate(4, ledgersaldovo.getEvalStart());
                stmt.setDate(5, ledgersaldovo.getEvalDate());
                stmt.setLong(6, ledgersaldovo.getLedgerID());
                stmt.setString(7, ledgersaldovo.getAccountID());
                stmt.setString(8, ledgersaldovo.getLedgerName());
                stmt.setString(9, ledgersaldovo.getLedgerType());
                stmt.setBigDecimal(10, ledgersaldovo.getAmountStart());
                stmt.setBigDecimal(11, ledgersaldovo.getAmountPrevious());
                stmt.setBigDecimal(12, ledgersaldovo.getAmountDebit());
                stmt.setBigDecimal(13, ledgersaldovo.getAmountCredit());
                stmt.setBigDecimal(14, ledgersaldovo.getAmount());
                stmt.setShort(15, ledgersaldovo.getCurrencyID());
                stmt.setDouble(16, ledgersaldovo.getExchangeRate());
                stmt.setBigDecimal(17, ledgersaldovo.getAmountRef());
                stmt.setDate(18, ledgersaldovo.getValidFrom());
                stmt.setDate(19, ledgersaldovo.getValidTo());
                stmt.setLong(20, ledgersaldovo.getId());
                stmt.executeUpdate();
            } finally {
                release(conn);
            }
        }
    }

    public void deleteData(LedgerSaldoVO ledgersaldovo) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("delete from bo.ledger_saldo where id = " + ledgersaldovo.getId());
            } finally {
                release(conn);
            }
        }
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
