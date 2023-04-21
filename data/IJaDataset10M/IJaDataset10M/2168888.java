package br.unb.cic.ethutil;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;

/**
 * This class offers a timeout feature on socket connections.
 * A maximum length of time allowed for a connection can be
 * specified, along with a host and port.
 */
class TimedSocket {

    /**
	 * Attempts to connect to a service at the specified address
	 * and port, for a specified maximum amount of time.
	 *
	 *	@param	addr	Address of host
	 *	@param	port	Port of service
	 * @param	delay	Delay in milliseconds
	 */
    public static Socket getSocket(String host, int port, int delay) throws Exception {
        Socket socket = null;
        try {
            SocketThread st = new SocketThread(host, port);
            st.start();
            st.join(delay);
            if (st.isConnected()) {
                socket = st.getSocket();
                return socket;
            } else if (st.isError()) {
                throw st.getException();
            } else {
                st.setDoneWaiting(new InterruptedIOException("Could not connect for " + delay + " milliseconds"));
                st.interrupt();
                throw st.getException();
            }
        } catch (Exception outerEx) {
            throw outerEx;
        }
    }

    static class SocketThread extends Thread {

        private volatile Socket m_connection = null;

        private volatile boolean doneWaiting = false;

        private String m_host = null;

        private int m_port = 0;

        private IOException m_exception = null;

        public SocketThread(String host, int port) {
            m_host = host;
            m_port = port;
        }

        public void setDoneWaiting(InterruptedIOException ioe) {
            doneWaiting = true;
            m_exception = ioe;
        }

        public void run() {
            m_connection = null;
            try {
                m_connection = new Socket(m_host, m_port);
            } catch (IOException ioe) {
                m_exception = ioe;
            }
            if (doneWaiting || isError()) {
                if (m_connection != null) {
                    try {
                        m_connection.close();
                        m_connection = null;
                    } catch (IOException e) {
                    }
                }
            }
        }

        public boolean isConnected() {
            if (m_connection == null) return false; else return true;
        }

        public boolean isError() {
            if (m_exception == null) return false; else return true;
        }

        public Socket getSocket() {
            return m_connection;
        }

        public IOException getException() {
            return m_exception;
        }
    }
}
