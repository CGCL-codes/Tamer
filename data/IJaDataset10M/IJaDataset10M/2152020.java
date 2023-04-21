package corujao.monitor.socket;

import corujao.monitor.MonitorTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Eduardo_Rangel
 */
public class SocketTask extends MonitorTask {

    protected Socket socket;

    protected PrintWriter out;

    protected BufferedReader in;

    /** Creates a new instance of SocketTask */
    public SocketTask() {
    }

    public void init() {
    }

    protected boolean inicializaSocket(String ip, String porta) {
        boolean result = true;
        try {
            socket = new Socket(ip, Integer.parseInt(porta));
            socket.setSoTimeout(30000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            log.error("Host desconhecido", e);
            result = false;
        } catch (IOException e) {
            log.error("Erro ao conectar", e);
            result = false;
        }
        return result;
    }

    public void execute() {
    }

    public void terminate() {
        try {
            if (!socket.isClosed()) socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
