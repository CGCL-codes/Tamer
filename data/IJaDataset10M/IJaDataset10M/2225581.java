package com.ohua.checkpoint.framework.handlers.checkpoint;

import java.util.LinkedList;
import com.ohua.checkpoint.framework.CheckpointEvents;
import com.ohua.checkpoint.framework.markerpackets.CheckpointMarkerPacket;
import com.ohua.checkpoint.framework.operatorcheckpoints.AbstractCheckPoint;
import com.ohua.engine.exceptions.Assertion;
import com.ohua.engine.extension.points.InputPortEvents;
import com.ohua.engine.flowgraph.elements.operator.InputPort;
import com.ohua.engine.flowgraph.elements.operator.OhuaOperator;
import com.ohua.engine.flowgraph.elements.operator.InputPort.VisitorReturnType;
import com.ohua.engine.flowgraph.elements.packets.DataPollOnBlockedInputPortEvent;
import com.ohua.engine.flowgraph.elements.packets.EndOfStreamPacket;
import com.ohua.engine.flowgraph.elements.packets.functionality.handers.DataPacketLoggerHandler;

/**
 * This is an optimization that can only be used in combination with the Fast Traveler support.
 * It allows deterministic operators with more than one incoming arc to continue processing
 * although the marker has not been detected on all the input ports yet.<br>
 * The state of the operator will be taken as soon as the first checkpoint marker was detected
 * on one of the input ports. Afterwards data packets arriving at the other input ports are
 * being logged to the checkpoint until the marker arrives. Once the marker was seen on all the
 * input ports, the checkpoint is saved to disk. As a result: No blocking is necessary and the
 * processing latency is reduced.
 * <p>
 * Note that this optimization also involves a slight change in the restart algorithm. Normally
 * we only check the endpoints to understand what the latest complete system-wide checkpoint
 * was. Every deterministic operator that uses this handler will have to be included in this set
 * because it might very well be that a crash happens after all endpoints have saved checkpoint
 * X and an operator of this category is still logging packets on one of its input ports.
 * <p>
 * In such a case we would restart from checkpoint X-1 (or the last complete system-wide
 * checkpoint) and the Fast Traveler concept will just treat every checkpoint taken in the
 * future of the restart checkpoint as mini-checkpoint and preserve exactly-once delivery
 * semantics.
 * @author sertel
 * 
 */
public class DMergeFTSupportCPMarkerHandler extends AbstractDeterministicOperatorCPMarkerHandler {

    private LinkedList<DataPacketLoggerHandler> _unfinished = new LinkedList<DataPacketLoggerHandler>();

    private boolean _propagatedMarker = false;

    public DMergeFTSupportCPMarkerHandler(OhuaOperator operator) {
        super(operator);
    }

    /**
   * We still let the algorithm allow to block the input ports that have seen a marker. This is
   * just a little try for an optimization. It could be that the operator actually was going to
   * dequeue from an input port that has not seen a marker yet. Hence it would reduce the data
   * to be saved with the checkpoint.<br>
   * As soon as the deterministic algorithm inside the operator is trying to poll on a blocked
   * input port we start logging packets on all input ports that have not seen the marker yet. <br>
   * Note that this will also be the point where we will take the checkpoint; that is right
   * before we start logging data packets among input ports.
   */
    public void notifyMarkerArrived(InputPort port, DataPollOnBlockedInputPortEvent event) {
        _logger.fine("Poll on blocked port: " + port);
        AbstractCheckPoint cp = takeOperatorCheckpoint();
        DataPacketLoggerHandler loggerHandler = new DataPacketLoggerHandler(cp);
        _unfinished.addLast(loggerHandler);
        int searchedMarkerIndex = _servedOpenInputPorts.get(port).size();
        for (InputPort inPort : getPendingInputPorts(searchedMarkerIndex)) {
            inPort.registerForEvent(InputPortEvents.DATA_PACKET_ARRIVAL, loggerHandler);
            inPort.registerForEvent(CheckpointEvents.CHECKPOINT_PACKET_ARRIVAL, loggerHandler);
        }
        CheckpointMarkerPacket toBePropagated = (CheckpointMarkerPacket) _servedOpenInputPorts.get(port).get(0);
        Assertion.invariant(toBePropagated != null, "There was no checkpoint marker for the blocked input port available.");
        super.propagateMarker(toBePropagated);
        _propagatedMarker = true;
        unblockAllInputPorts();
        port.setVisitorReturnStatus(VisitorReturnType.DEQUEUE_NEXT_PACKET);
    }

    /**
   * Either it is the first checkpoint, then do nothing and wait for the "blocking-port-poll"
   * callback. Or it is a marker on an input port that we are currently logging on in which case
   * we stop the logging. If this is the last input port we were logging on then save the
   * checkpoint to disk.
   */
    @Override
    public void notifyMarkerArrived(InputPort port, CheckpointMarkerPacket packet) {
        super.notifyMarkerArrived(port, packet);
    }

    /**
   * This function will be called when we received all checkpoint markers among all the input
   * ports. Hence we either save the checkpoint "the normal way" or we are finished with logging
   * and wrap up the current checkpoint.
   */
    @Override
    protected AbstractCheckPoint takeOperatorCheckpoint() {
        if (_unfinished.isEmpty()) {
            return super.takeOperatorCheckpoint();
        } else {
            DataPacketLoggerHandler loggerHandler = _unfinished.poll();
            AbstractCheckPoint cp = loggerHandler.getAssociatedCheckpoint();
            cp.setArcData(loggerHandler.getLoggedData());
            return cp;
        }
    }

    @Override
    protected void propagateMarker(CheckpointMarkerPacket toBePropagated) {
        if (_propagatedMarker) {
            _propagatedMarker = false;
            return;
        } else {
            super.propagateMarker(toBePropagated);
        }
    }

    @Override
    public void notifyMarkerArrived(InputPort port, EndOfStreamPacket packet) {
        super.notifyMarkerArrived(port, packet);
    }
}
