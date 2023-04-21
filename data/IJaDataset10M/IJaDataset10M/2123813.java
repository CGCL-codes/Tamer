package org.gudy.azureus2.core3.peer;

public interface PEPeerManagerStats {

    public void discarded(PEPeer peer, int length);

    public void hashFailed(int length);

    public void dataBytesReceived(PEPeer peer, int length);

    public void protocolBytesReceived(PEPeer peer, int length);

    public void dataBytesSent(PEPeer peer, int length);

    public void protocolBytesSent(PEPeer peer, int length);

    public void haveNewPiece(int pieceLength);

    public void haveNewConnection(boolean incoming);

    public long getDataReceiveRate();

    public long getProtocolReceiveRate();

    public long getDataSendRate();

    public long getProtocolSendRate();

    public long getTotalDataBytesSent();

    public long getTotalProtocolBytesSent();

    public long getTotalDataBytesReceived();

    public long getTotalProtocolBytesReceived();

    public long getTotalDataBytesSentNoLan();

    public long getTotalProtocolBytesSentNoLan();

    public long getTotalDataBytesReceivedNoLan();

    public long getTotalProtocolBytesReceivedNoLan();

    public long getTotalAverage();

    public long getTotalHashFailBytes();

    public long getTotalDiscarded();

    public int getTimeSinceLastDataReceivedInSeconds();

    public int getTimeSinceLastDataSentInSeconds();

    public int getTotalIncomingConnections();

    public int getTotalOutgoingConnections();

    public int getPermittedBytesToReceive();

    public void permittedReceiveBytesUsed(int bytes);

    public int getPermittedBytesToSend();

    public void permittedSendBytesUsed(int bytes);
}
