package com.ohua.checkpoint.framework.handlers.checkpoint;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ohua.checkpoint.framework.operatorcheckpoints.AbstractCheckPoint;
import com.ohua.checkpoint.framework.operators.BatchEndpoint;
import com.ohua.engine.exceptions.Assertion;
import com.ohua.engine.flowgraph.elements.operator.OhuaOperator;
import com.ohua.engine.flowgraph.elements.operator.OperatorID;
import com.ohua.engine.operators.categories.TransactionalIOOperator;
import com.ohua.engine.resource.management.AbstractCPResource;
import com.ohua.engine.resource.management.AbstractResource;
import com.ohua.engine.resource.management.CheckpointResourceAccess;
import com.ohua.engine.resource.management.CheckpointResourceManager;
import com.ohua.engine.resource.management.ResourceConnection;
import com.ohua.engine.resource.management.ResourceManager;
import com.ohua.engine.utils.OhuaLoggerFactory;

public class TransactionalEndpointCPMarkerHandler extends AbstractDeterministicOperatorCPMarkerHandler {

    private static final String CHCKPT_TABLE_NAME = "checkpoints_op_";

    private ResourceConnection _cnn = null;

    private CheckpointResourceAccess _resourceAccess = null;

    private String _opChckPtArtifactName = null;

    private Logger _commitLogger = null;

    public TransactionalEndpointCPMarkerHandler(OhuaOperator operator) {
        super(operator);
        doCreationInit(operator);
    }

    protected void doCreationInit(OhuaOperator operator) {
        Assertion.invariant(operator.getUserOperator() instanceof TransactionalIOOperator);
        TransactionalIOOperator dbOutputOp = (TransactionalIOOperator) _operator.getUserOperator();
        AbstractResource resource = ResourceManager.getInstance().getResource(dbOutputOp.getResourceID());
        Assertion.invariant(resource instanceof AbstractCPResource);
        _commitLogger = OhuaLoggerFactory.getLogger(OhuaLoggerFactory.getLogIDForOperator(operator.getUserOperator().getClass(), operator.getUserOperator().getOperatorName(), Integer.toString(operator.getId().getIDInt())) + "-commit");
    }

    @Override
    public void init() {
        super.init();
        doPostInit();
    }

    private void doPostInit() {
        localFieldsInit();
        createCheckpointArtifact();
        _logger.log(Level.ALL, "table with name " + _opChckPtArtifactName + " successfully created!");
        prepareInsertStatement();
    }

    private void localFieldsInit() {
        _cnn = retrieveConnection();
        _opChckPtArtifactName = createChckPtTableName(_operator);
        TransactionalIOOperator dbOutputOp = (TransactionalIOOperator) _operator.getUserOperator();
        AbstractResource resource = ResourceManager.getInstance().getResource(dbOutputOp.getResourceID());
        _resourceAccess = ((AbstractCPResource) resource).getCheckpointResourceAccess(_operator.getId(), _opChckPtArtifactName);
        _resourceAccess.setMetaData(Collections.singletonMap("id", "INT"), Collections.singletonList("id"));
        _resourceAccess.initialize();
    }

    @Override
    public final void restart(AbstractCheckPoint checkpoint) {
        super.restart(checkpoint);
        localFieldsInit();
        prepareInsertStatement();
    }

    public static String createChckPtTableName(OhuaOperator operator) {
        return createChckPtTableName(operator.getId());
    }

    public static String createChckPtTableName(OperatorID operatorID) {
        return CHCKPT_TABLE_NAME + operatorID.getIDInt();
    }

    /**
   * Gets the database connection from the database operator whose checkpoints it is handling in
   * order to also acquire commit management over the transactions of this operator.
   * @return
   */
    private ResourceConnection retrieveConnection() {
        TransactionalIOOperator TAOutputOp = (TransactionalIOOperator) _operator.getUserOperator();
        ResourceConnection cnn = TAOutputOp.getConnection();
        return cnn;
    }

    /**
   * Creates a table to keep the checkpoint IDs of successfully created checkpoints. (= a one
   * column table)
   * <p>
   * The problem is that we need to have an atomic operation to retrieve the correct
   * mini-checkpoint information to the checkpoint we recover from. Hence if saving the minis
   * fails then we need to also recover from the previous checkpoint!
   */
    private void createCheckpointArtifact() {
        try {
            _resourceAccess.deleteArtifact();
            _resourceAccess.createArtifact();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("unhandled exception", e);
        }
    }

    /**
   * Prepares the insert statement for the checkpoint table.
   */
    private void prepareInsertStatement() {
        try {
            _resourceAccess.prepareInsert();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("unhandled exception", e);
        }
    }

    @Override
    public void saveOperatorCheckpoint(AbstractCheckPoint checkpoint) {
        super.saveOperatorCheckpoint(checkpoint);
        if (_operator.getUserOperator() instanceof BatchEndpoint) {
            ((BatchEndpoint) _operator.getUserOperator()).flush();
        }
        try {
            Assertion.invariant(ResourceManager.getInstance().isOpenConnection(_cnn.getConnectionID()));
            _resourceAccess.insert(Collections.<String, Object>singletonMap("id", getNextCheckpointID() - 1));
            CheckpointResourceManager.getInstance().commitTransaction(_cnn);
            _commitLogger.info("commit of checkpoint " + (getNextCheckpointID() - 1) + " successfull.");
            _commitLogger.info("---------------------------------------------------");
        } catch (Throwable e) {
            System.out.println("CP id:" + (getNextCheckpointID() - 1));
            e.printStackTrace();
            throw new RuntimeException("unhandled exception", e);
        }
    }

    public static String getDeleteChckPtsQuery(OhuaOperator operator, int from) {
        return "delete from " + createChckPtTableName(operator) + " where id > " + from;
    }
}
