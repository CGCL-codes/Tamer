package org.zoolu.net;

/** Listener for TcpConnection events.
  */
public interface TcpConnectionListener {

    /** When new data is received through the TcpConnection. */
    public void onReceivedData(TcpConnection tcp_conn, byte[] data, int len);

    /** When TcpConnection terminates. */
    public void onConnectionTerminated(TcpConnection tcp_conn, Exception error);
}
