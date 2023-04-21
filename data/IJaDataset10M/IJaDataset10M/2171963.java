package com.ohua.engine.operators;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import com.ohua.checkpoint.framework.operatorcheckpoints.AbstractCheckPoint;
import com.ohua.checkpoint.framework.operatorcheckpoints.DatabaseOperatorCheckPoint;
import com.ohua.clustering.testing.Experimenter;

/**
 * Incoming data is submitted to the database in batches. Commit handling is still all done by
 * the framework.
 * @author sertel
 * 
 */
public class DatabaseBatchWriterOperator extends AbstractDatabaseEndpoint {

    public static class DatabaseBatchEndpointProperties extends DatabaseEndpointProperties implements Serializable {

        public int batchSize = 100;
    }

    private int _batchCount = 0;

    private int _rowCount = 0;

    @Override
    public void runProcessRoutine() {
        try {
            while (_inPortControl.next()) {
                getLogger().fine("received data packet:" + _inPortControl.dataToString("XML"));
                for (Map.Entry<String, String> adaptation : getProperties().adaptation.entrySet()) {
                    List<Object> data = _inPortControl.getData(adaptation.getKey());
                    Object val = data.get(0);
                    if (val == null || val.toString().equals("null")) {
                        getInsertStmt().setObject(_stmtMap.get(adaptation.getValue()), "NULL");
                    } else {
                        getInsertStmt().setObject(_stmtMap.get(adaptation.getValue()), val);
                    }
                }
                addBatch();
                _rowCount++;
                if (_rowCount >= ((DatabaseBatchEndpointProperties) getProperties()).batchSize) {
                    Experimenter.getInstance().addStartWriteBatchCall();
                    writeBatch();
                    Experimenter.getInstance().addEndWriteBatchCall();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("unhandled exception", e);
        }
    }

    private void addBatch() throws SQLException {
        Experimenter.getInstance().addStartAddBatchCall();
        try {
            getInsertStmt().addBatch();
        } catch (SQLException e) {
            getLogger().warning("row count: " + _rowCount);
            getLogger().warning("insert statement: " + getInsertStmt().toString());
            getLogger().warning("insert statement meta data: " + getInsertStmt().getMetaData().toString());
            getLogger().throwing(getClass().getCanonicalName(), "runProcessRoutine", e);
            throw e;
        }
        Experimenter.getInstance().addEndAddBatchCall();
    }

    private int[] writeBatch() throws SQLException {
        getLogger().info("Writing batch number " + _batchCount);
        _batchCount++;
        int[] results = getInsertStmt().executeBatch();
        getLogger().info("records written: " + results.length);
        assert results.length == _rowCount;
        _rowCount = 0;
        return results;
    }

    /**
   * In the case of the DatabaseWriter that uses batch functionality for the updates to the
   * database, we need to write out the current batch at this point because otherwise the data
   * in the current batch will not be part of the transaction that gets committed with this
   * checkpoint. On restart this would lead to missing packets! Furthermore this DatabaseWriter
   * tries to write its last batch in the flush() function that is called after the checkpoint
   * framework has handled the EOS marker. Hence the data in the last batch would not be
   * committed to the database. In order to fix both scenarios we need to write out the batch
   * with every checkpoint (which seems reasonable because otherwise we would have to include
   * the data in the current batch into the checkpoint!!!)
   * @see ohua.checkpoint.framework.operators.OperatorCheckpoint#checkpoint()
   */
    @Override
    public DatabaseOperatorCheckPoint checkpoint() {
        DatabaseOperatorCheckPoint dbwChckPt = super.checkpoint();
        dbwChckPt.setBatchCount(_batchCount);
        return dbwChckPt;
    }

    @Override
    public void restart(AbstractCheckPoint checkpoint) {
        DatabaseOperatorCheckPoint dbwChckPt = (DatabaseOperatorCheckPoint) checkpoint;
        _batchCount = dbwChckPt.getBatchCount();
        super.restart(checkpoint);
    }

    public void flush() {
        if (_rowCount < 1) {
            return;
        }
        try {
            writeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Logger.getLogger(getLogger().getName() + "-result").info("total records written (guessed!): " + (_batchCount * ((DatabaseBatchEndpointProperties) getProperties()).batchSize + _rowCount));
    }

    @Override
    protected DatabaseProperties getDefaultProperties() {
        return new DatabaseBatchEndpointProperties();
    }
}
