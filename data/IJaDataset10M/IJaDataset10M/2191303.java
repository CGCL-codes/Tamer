package com.ohua.checkpoint.framework.visitors;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ohua.checkpoint.framework.CheckpointEvents;
import com.ohua.checkpoint.framework.handlers.IFastTravelerPacketHandler;
import com.ohua.checkpoint.framework.markerpackets.FastTravelerMarkerPacket;
import com.ohua.engine.extension.points.IInputPortEvent;
import com.ohua.engine.flowgraph.elements.operator.InputPort;
import com.ohua.engine.flowgraph.elements.operator.InputPort.VisitorReturnType;
import com.ohua.engine.flowgraph.elements.packets.functionality.VisitorMixin;

public class FastTravelerVisitorMixin extends VisitorMixin<FastTravelerMarkerPacket, IFastTravelerPacketHandler> {

    public FastTravelerVisitorMixin(InputPort in) {
        super(in);
    }

    @Override
    public void handlePacket(FastTravelerMarkerPacket packet) {
        Logger.getLogger(getClass().getCanonicalName()).log(Level.ALL, "fast traveler packet encountered on operator " + _inputPort.getOwner().getOperatorName() + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        notifyHandlers(packet);
        _inputPort.setVisitorReturnStatus(VisitorReturnType.DEQUEUE_NEXT_PACKET);
    }

    public void notifyHandlers(FastTravelerMarkerPacket packet) {
        Set<IFastTravelerPacketHandler> allHandlers = getAllHandlers();
        for (IFastTravelerPacketHandler handler : allHandlers) {
            handler.notifyMarkerArrived(_inputPort, packet);
        }
    }

    public IInputPortEvent getEventResponsibility() {
        return CheckpointEvents.FAST_TRAVELER_PACKET_ARRIVAL;
    }
}
