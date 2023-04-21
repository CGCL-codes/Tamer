package com.ohua.checkpoint.framework.markerpackets;

import com.ohua.engine.flowgraph.elements.packets.IStreamPacket;

public interface IMarkerPacketVisitor {

    public void visit(IStreamPacket packet);
}
