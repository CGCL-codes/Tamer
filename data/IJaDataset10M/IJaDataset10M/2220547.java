package com.ohua.engine.graphanalysis.cycleDetection;

import java.util.LinkedHashSet;
import java.util.Set;
import com.ohua.engine.flowgraph.elements.operator.OperatorID;
import com.ohua.engine.flowgraph.elements.packets.IStreamPacket;

/**
 * This marker packet is used for the cycle detection algorithm
 * @author sertel
 *
 */
public class CycleDetectionMarkerPacket implements ICycleDetectionMarker {

    private Set<OperatorID> _seenOperators = new LinkedHashSet<OperatorID>();

    private int _readerLevel = -1;

    public boolean hasSeenMe(OperatorID opID) {
        return _seenOperators.contains(opID);
    }

    public void addSeenOperator(OperatorID opID) {
        _seenOperators.add(opID);
    }

    public Set<OperatorID> getSeenOperators() {
        return _seenOperators;
    }

    public void setReaderLevel(int readerLevel) {
        _readerLevel = readerLevel;
    }

    public int getReaderLevel() {
        return _readerLevel;
    }

    public IStreamPacket deepCopy() {
        CycleDetectionMarkerPacket clone = new CycleDetectionMarkerPacket();
        clone.setReaderLevel(_readerLevel);
        clone.getSeenOperators().clear();
        clone.getSeenOperators().addAll(_seenOperators);
        return clone;
    }
}
