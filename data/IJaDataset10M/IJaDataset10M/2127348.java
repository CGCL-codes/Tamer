package gnu.saw.client.filetransfer;

import gnu.saw.client.connection.SAWClientConnection;
import gnu.saw.client.session.SAWClientSession;

public class SAWFileTransferClient implements Runnable {

    private SAWClientConnection connection;

    private SAWClientSession session;

    private SAWFileTransferClientSessionHandler handler;

    public SAWFileTransferClient(SAWClientSession session) {
        this.connection = session.getConnection();
        this.session = session;
        this.handler = new SAWFileTransferClientSessionHandler(this, new SAWFileTransferClientSession(this));
    }

    public SAWClientConnection getConnection() {
        return connection;
    }

    public SAWClientSession getSession() {
        return session;
    }

    public SAWFileTransferClientSessionHandler getHandler() {
        return handler;
    }

    public void setHandler(SAWFileTransferClientSessionHandler handler) {
        this.handler = handler;
    }

    public void run() {
        handler.run();
    }
}
