package org.carp.engine.exec;

import java.sql.SQLException;
import org.carp.impl.AbstractCarpQuery;

/**
 * ����ִ������
 * @author zhou
 * @since 0.2
 */
public class BatchExecutor extends Executor {

    public BatchExecutor(AbstractCarpQuery query) throws Exception {
        super(query);
    }

    @Override
    protected void executeStatement() throws Exception {
    }

    /**
	 * ������Ӱ�������
	 * @return
	 * @throws SQLException 
	 */
    public void addBatch() throws SQLException {
        this.getQuery().getPreparedStatement().addBatch();
        this.getQuery().clearParameters();
    }
}
