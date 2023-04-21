package se.ucr.openqregdemo.bean;

import java.sql.Connection;
import java.sql.SQLException;
import se.ucr.db.PersistentTransactionBean;
import se.ucr.openqregdemo.bean.base.DischargeBeanBase;

/**
* This is the Bean class for the  discharge table.
*/
public class DischargeBean extends DischargeBeanBase implements PersistentTransactionBean {

    /**
* @see se.ucr.db.PersistentTransactionBean#beforeCreate(java.sql.Connection)
*/
    @Override
    protected void beforeCreate(Connection con) throws SQLException {
    }

    /**
* @see se.ucr.db.PersistentTransactionBean#afterCreate(java.sql.Connection)
*/
    @Override
    protected void afterCreate(Connection con) throws SQLException {
    }

    /**
* @see se.ucr.db.PersistentTransactionBean#beforeStore(java.sql.Connection)
*/
    @Override
    protected void beforeStore(Connection con) throws SQLException {
    }

    /**
* @see se.ucr.db.PersistentTransactionBean#afterStore(java.sql.Connection)
*/
    @Override
    protected void afterStore(Connection con) throws SQLException {
    }

    /**
* @see se.ucr.db.PersistentTransactionBean#beforeRemove(java.sql.Connection)
*/
    @Override
    protected void beforeRemove(Connection con) throws SQLException {
    }

    /**
* @see se.ucr.db.PersistentTransactionBean#afterRemove(java.sql.Connection)
*/
    @Override
    protected void afterRemove(Connection con) throws SQLException {
    }

    @Override
    public void afterPopulate(Connection con) throws SQLException {
    }
}
