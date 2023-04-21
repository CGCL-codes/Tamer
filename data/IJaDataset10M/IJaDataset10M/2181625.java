package com.meidusa.amoeba.net;

import java.nio.channels.SocketChannel;
import org.apache.log4j.Logger;

public abstract class DatabaseConnection extends AuthingableConnection {

    private static Logger logger = Logger.getLogger(DatabaseConnection.class);

    private String schema;

    /**
	 * �Ƿ����Զ��ύ����
	 */
    private boolean autoCommit = true;

    /**
	 * transaction isolation level
	 */
    private int transactionIsolation;

    /**
	 * ��ǰ������ص��ն�ϵͳ(client/dbserver)���ȡ���ַ����
	 */
    private String charset;

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
        if (logger.isDebugEnabled()) {
            logger.debug("set client charset=" + charset);
        }
    }

    public DatabaseConnection(SocketChannel channel, long createStamp) {
        super(channel, createStamp);
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
	 * @see {@link java.sql.Connection#getAutoCommit}
	 */
    public boolean isAutoCommit() {
        return autoCommit;
    }

    /**
	 * @see {@link java.sql.Connection#setAutoCommit}
	 */
    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    /**
	 * @see {@link java.sql.Connection#getTransactionIsolation}
	 */
    public int getTransactionIsolation() {
        return transactionIsolation;
    }

    /**
	 * @see {@link java.sql.Connection#setTransactionIsolation}
	 */
    public void setTransactionIsolation(int transactionIsolation) {
        this.transactionIsolation = transactionIsolation;
    }
}
