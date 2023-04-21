package socket;

import java.net.Socket;

/**
 * 
 * @author pfister@lgi2p 07/11/28
 *
 */
public class BaseThread extends Thread {

    protected Socket socket;

    protected BaseClient client;

    public BaseThread(Socket connexion, BaseClient client) {
        this.socket = connexion;
        this.client = client;
    }
}
