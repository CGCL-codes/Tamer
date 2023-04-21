package mrstalk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabriel
 */
public class Chat {

    public Socket chatSocket = null;

    public DataInputStream is = null;

    public DataOutputStream os = null;

    public String user;

    public boolean disconnected = false;

    private static final String DISCONNECT_MSG = "/disconnect";

    private static final String ACK_MSG = "/acknoledgment";

    public Chat(Socket t_chatSocket, String t_user) {
        chatSocket = t_chatSocket;
        user = t_user;
        try {
            os = new DataOutputStream(chatSocket.getOutputStream());
            is = new DataInputStream(chatSocket.getInputStream());
            os.writeUTF("Início da conversa com " + user);
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String sendMsg(String msg) {
        try {
            if (!disconnected) {
                msg = user + ": " + msg;
                os.writeUTF(msg);
            } else {
                msg = "A mensagem '" + msg + "' não pôde ser enviada";
            }
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return msg;
    }

    public String receiveMsg() {
        String msg = null;
        try {
            if (!disconnected) {
                msg = is.readUTF();
                os.writeUTF(ACK_MSG);
                if (msg.equals(DISCONNECT_MSG)) {
                    disconnected = true;
                    msg = "Desconectado";
                } else if (msg.equals(ACK_MSG)) {
                    msg = null;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return msg;
    }

    public void closeConnection() {
        try {
            if (!disconnected) {
                os.writeUTF(DISCONNECT_MSG);
            }
            os.close();
            is.close();
            chatSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
