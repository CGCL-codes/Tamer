package Comunicacion;

import GUI.Pnl_Chat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rickel
 */
public class ClienteChat implements Runnable {

    private int puerto;

    private String host;

    private Socket echoSocket;

    private PrintWriter out;

    private String mensaje2;

    private BufferedReader in;

    private Pnl_Chat pChat;

    public ClienteChat(String host, int puerto, Pnl_Chat pChat) {
        this.host = host;
        this.puerto = puerto;
        this.pChat = pChat;
    }

    public void run() {
        echoSocket = null;
        try {
            echoSocket = new Socket(host, puerto);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            out = new PrintWriter(echoSocket.getOutputStream(), true);
        } catch (UnknownHostException e) {
            System.err.println("Host Inalcanzable");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error del servidor");
            System.exit(1);
        }
        while (true) {
            try {
                mensaje2 = in.readLine();
                System.out.println(mensaje2);
                pChat.anexarMensaje(mensaje2);
            } catch (IOException ex) {
                Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void enviarMensaje(String mensaje) {
        out.println(mensaje);
    }
}
