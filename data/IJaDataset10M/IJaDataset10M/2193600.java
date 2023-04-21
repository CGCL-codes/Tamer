package com.ohua.checkpoint.framework.serialization;

import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import com.ohua.checkpoint.framework.operatorcheckpoints.EndpointMiniInfo;
import com.ohua.checkpoint.framework.operatorcheckpoints.OhuaCheckpoint;
import com.ohua.eai.resources.JMSQueueConnection;
import com.ohua.engine.flowgraph.elements.operator.OhuaOperator;
import com.ohua.engine.operators.categories.TransactionalIOOperator;
import com.ohua.engine.resource.management.CheckpointResourceManager;
import com.ohua.engine.utils.OhuaLoggerFactory;

public class JMSWriterMiniCheckpointSerializer implements CheckpointSerializer, TransactionalEndpointMinisSerializer {

    private static final String DROP_QUEUE = "mini-cp-incomplete-";

    private static final String CP_QUEUE = "mini-cp-";

    private static final String LAST_CP_QUEUE = "mini-last-cp-";

    private Logger _logger = OhuaLoggerFactory.getLogger(getClass());

    private OhuaOperator _operator = null;

    private JMSQueueConnection _cnn = null;

    private MessageProducer _cpProducer = null;

    private MessageProducer _lastCPProducer = null;

    private MessageProducer _inCompleteProducer = null;

    private MessageConsumer _lastCPConsumer = null;

    private MessageConsumer _inCompleteConsumer = null;

    private int _cpID = 0;

    private boolean _cpCallNext = false;

    public JMSWriterMiniCheckpointSerializer(OhuaOperator operator) {
        _operator = operator;
    }

    public void initialize() {
        performInitialization(true);
        drainQueues();
    }

    private void drainQueues() {
    }

    /**
   * BEWARE! This function requires that the connection operators in a distributed deployment
   * context have different unique identifiers!
   * @param enqueueInitialMessages
   */
    private void performInitialization(boolean enqueueInitialMessages) {
        _cnn = (JMSQueueConnection) ((TransactionalIOOperator) _operator.getUserOperator()).getConnection();
        try {
            Queue lastCPQueue = _cnn.createQueue(LAST_CP_QUEUE + _operator.getId());
            System.out.println("last cp queue:" + (LAST_CP_QUEUE + _operator.getId()));
            _lastCPProducer = _cnn.createProducer(lastCPQueue);
            _lastCPConsumer = _cnn.createConsumer(lastCPQueue);
            _cpProducer = _cnn.createProducer(_cnn.createQueue(CP_QUEUE + _operator.getId()));
            System.out.println("cp queue:" + (CP_QUEUE + _operator.getId()));
            Queue dropQueue = _cnn.createQueue(DROP_QUEUE + _operator.getId());
            System.out.println("drop queue:" + (DROP_QUEUE + _operator.getId()));
            _inCompleteProducer = _cnn.createProducer(dropQueue);
            _inCompleteConsumer = _cnn.createConsumer(dropQueue);
            if (enqueueInitialMessages) {
                MapMessage msg = _cnn.createMapMessage();
                msg.setLong("committed", 0);
                msg.setInt("cp-id", -1);
                _lastCPProducer.send(msg);
                msg = _cnn.createMapMessage();
                msg.setLong("committed", 0);
                _inCompleteProducer.send(msg);
            }
            CheckpointResourceManager.getInstance().commitTransaction(_cnn);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void serialize(OhuaCheckpoint cp) {
        if (_cpCallNext) {
            performCheckpointMini(cp);
            _cpCallNext = false;
        } else {
            performAsyncMini(cp);
        }
    }

    private void performCheckpointMini(OhuaCheckpoint cp) {
        EndpointMiniInfo mini = (EndpointMiniInfo) cp;
        try {
            _logger.fine("1 Blocking deqeue");
            MapMessage msg = (MapMessage) _inCompleteConsumer.receive(3000);
            _logger.fine("1 Blocking deqeue done");
            long oldDropped = msg.getLong("committed");
            msg = _cnn.createMapMessage();
            msg.setLong("committed", 0);
            _inCompleteProducer.send(msg);
            msg = _cnn.createMapMessage();
            msg.setLong("committed", oldDropped);
            msg.setInt("cp-id", _cpID);
            _cpProducer.send(msg);
            _lastCPConsumer.receive(3000);
            msg = _cnn.createMapMessage();
            msg.setLong("committed", oldDropped);
            msg.setInt("cp-id", _cpID);
            _lastCPProducer.send(msg);
        } catch (JMSException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        mini._highestIndex = _cpID;
        _cpID++;
    }

    private void performAsyncMini(OhuaCheckpoint cp) {
        EndpointMiniInfo mini = (EndpointMiniInfo) cp;
        try {
            _logger.fine("3 Blocking deqeue");
            MapMessage msg = (MapMessage) _inCompleteConsumer.receive(3000);
            _logger.fine("3 Blocking deqeue done");
            long oldDropped = msg.getLong("committed");
            long newDropped = oldDropped + mini._packetsToDrop;
            msg = _cnn.createMapMessage();
            msg.setLong("committed", newDropped);
            _logger.fine("New drop count: " + newDropped);
            _inCompleteProducer.send(msg);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        mini._highestIndex = _cpID;
    }

    public OhuaCheckpoint deserialize() {
        EndpointMiniInfo mini = new EndpointMiniInfo();
        long dropCount = 0;
        try {
            MapMessage msg = (MapMessage) _lastCPConsumer.receiveNoWait();
            if (msg != null) {
                int cpID = msg.getInt("cp-id");
                if (cpID > _cpID) {
                    throw new UnsupportedOperationException();
                } else if (cpID == _cpID + 1) {
                    long dropped = msg.getLong("committed");
                    dropCount += dropped;
                }
            }
            _logger.fine("2 Blocking deqeue");
            msg = (MapMessage) _inCompleteConsumer.receive(3000);
            _logger.fine("2 Blocking deqeue done");
            long dropped = msg.getLong("committed");
            dropCount += dropped;
            _logger.fine("JMSWriter needs to drop " + dropCount + " packets!");
            CheckpointResourceManager.getInstance().rollbackTransaction(_cnn);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        mini._packetsToDrop = dropCount;
        return mini;
    }

    /**
   * Enqueue into checkpoint queue and erase the one in the drop queue.
   */
    public void checkpoint() {
        _cpCallNext = true;
    }

    public void cleanup() throws Exception {
        _cpProducer.close();
        _inCompleteProducer.close();
        _inCompleteConsumer.close();
    }

    public void restart(long miniCheckpointPointer) {
        performInitialization(false);
        _cpID = (int) miniCheckpointPointer;
    }
}
