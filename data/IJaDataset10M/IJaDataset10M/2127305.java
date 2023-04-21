package net.sourceforge.transumanza.writer;

public interface TransactWriter extends BaseWriter {

    public void setAutoCommit(boolean autocommit) throws Exception;

    public boolean getAutoCommit() throws Exception;

    public void commit() throws Exception;

    public void rollback() throws Exception;
}
