package server.entidades;

import comum.IMsgSource;
import comum.Message;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Observable;
import proxy.ISocketPeer;
import server.EscritorSocket;
import server.Server;

/**
 *
 * @author root
 */
public abstract class Peer extends Observable implements Runnable, ISocketPeer, IMsgSource {

    protected Socket socket;

    protected BufferedWriter out;

    protected BufferedReader in;

    protected EscritorSocket escritor;

    protected boolean run;

    protected Server parent;

    public Peer(Server parent, Socket socket) {
        this.socket = socket;
        this.parent = parent;
        escritor = new EscritorSocket(this);
    }

    public void run() {
        if (socket != null && socket.isConnected()) {
            run = true;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                Thread t = new Thread(escritor);
                t.start();
                while (run && socket.isConnected()) {
                    String leitura = in.readLine();
                    if (leitura != null && !leitura.isEmpty()) {
                        this.tratarMessage(new Message(leitura));
                    }
                }
                finalizar();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public abstract void transmitirMensagem(Message msg);

    public abstract void tratarMessage(Message m);

    public abstract void init();

    private void escreverLinha(String linha) {
        escritor.escreverLinha(linha);
    }

    public abstract void stop();

    public BufferedWriter getBuffWriter() {
        return this.out;
    }

    public BufferedReader getBuffReader() {
        return this.in;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public abstract void finalizar();
}
