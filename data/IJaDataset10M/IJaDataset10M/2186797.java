package org.openqreg.openqregdemo.bean;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.openqreg.openqregdemo.bean.base.MceBeanBase;
import org.openqreg.db.PersistentTransactionBean;

/**
* This is the Bean class for the  mce table.
*/
public class MceBean extends MceBeanBase implements PersistentTransactionBean {

    /**
* @see org.openqreg.db.PersistentTransactionBean#beforeCreate(java.sql.Connection)
*/
    @Override
    protected void beforeCreate(Connection con) throws SQLException {
    }

    /**
* @see org.openqreg.db.PersistentTransactionBean#afterCreate(java.sql.Connection)
*/
    @Override
    protected void afterCreate(Connection con) throws SQLException {
        if (null == this.getMceid()) {
            PreparedStatement pStmt = null;
            ResultSet rs = null;
            try {
                pStmt = con.prepareStatement("SELECT LAST_INSERT_ID()");
                rs = pStmt.executeQuery();
                while (rs.next()) {
                    this.setMceid(new Long(rs.getLong(1)));
                }
            } finally {
                if (null != rs) {
                    rs.close();
                    rs = null;
                }
                if (null != pStmt) {
                    pStmt.close();
                    pStmt = null;
                }
            }
        }
    }

    /**
* @see org.openqreg.db.PersistentTransactionBean#beforeStore(java.sql.Connection)
*/
    @Override
    protected void beforeStore(Connection con) throws SQLException {
    }

    /**
* @see org.openqreg.db.PersistentTransactionBean#afterStore(java.sql.Connection)
*/
    @Override
    protected void afterStore(Connection con) throws SQLException {
    }

    /**
* @see org.openqreg.db.PersistentTransactionBean#beforeRemove(java.sql.Connection)
*/
    @Override
    protected void beforeRemove(Connection con) throws SQLException {
    }

    /**
* @see org.openqreg.db.PersistentTransactionBean#afterRemove(java.sql.Connection)
*/
    @Override
    protected void afterRemove(Connection con) throws SQLException {
    }

    @Override
    public void afterPopulate(Connection con) throws SQLException {
    }
}
