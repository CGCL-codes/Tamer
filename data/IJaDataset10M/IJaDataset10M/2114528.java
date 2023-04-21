package socket;

import java.io.*;
import java.net.Socket;

/**
 * 
 * @author pfister@lgi2p 07/11/28
 * 
 */
class ServerClientThread extends BaseThread implements Protocol {

    public ServerClientThread(Socket connexion, BaseClient client) {
        super(connexion, client);
    }

    public void run() {
        try {
            boolean stop = false;
            while (!stop) {
                BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String readbuf = socketReader.readLine();
                if (readbuf == null || (readbuf != null && readbuf.equalsIgnoreCase(QUIT.trim()))) {
                    Server.getInstance().removeClient(client);
                    break;
                } else if (readbuf != null) Server.getInstance().broadCast(client.getName() + " " + SEND + readbuf);
            }
            socket.close();
        } catch (IOException e) {
            Server.log(ERROR + " 5 " + e.toString(), true);
            try {
                Server.getInstance().broadCast(client.getName() + " " + RESET);
                Server.getInstance().removeClient(client);
            } catch (Exception ex) {
                Server.log(ERROR + " 6 " + ex.toString(), true);
            }
        }
        Server.log(STOP + client.getName(), true);
    }
}
