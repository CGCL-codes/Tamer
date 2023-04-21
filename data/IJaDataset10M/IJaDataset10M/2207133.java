package com.ohua.checkpoint.framework.operatorcheckpoints;

import java.util.List;

public class DeterministicMergeOperatorCheckpoint extends AbstractCheckPoint {

    List<String> _openPorts;

    int _currentPortIndex;

    int _packetsAlreadyDequeued;

    int _packetChunks;

    public List<String> getOpenPorts() {
        return _openPorts;
    }

    public void setOpenPorts(List<String> openPorts) {
        _openPorts = openPorts;
    }

    public int getCurrentPortIndex() {
        return _currentPortIndex;
    }

    public void setCurrentPortIndex(int currentPortIndex) {
        _currentPortIndex = currentPortIndex;
    }

    public int getPacketsAlreadyDequeued() {
        return _packetsAlreadyDequeued;
    }

    public void setPacketsAlreadyDequeued(int packetsAlreadyDequeued) {
        _packetsAlreadyDequeued = packetsAlreadyDequeued;
    }

    public int getPacketChunks() {
        return _packetChunks;
    }

    public void setPacketChunks(int packetChunks) {
        _packetChunks = packetChunks;
    }
}
