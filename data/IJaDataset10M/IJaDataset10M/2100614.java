package easyJ.database.session;

import java.sql.Connection;
import easyJ.common.EasyJException;

public abstract class Transaction {

    public abstract void commit() throws EasyJException;

    public abstract void rollback() throws EasyJException;

    protected abstract Connection getConnection();

    protected abstract void begin();
}
