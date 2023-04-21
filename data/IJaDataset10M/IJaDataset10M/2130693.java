package consumidor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import red.Servidor;

public class InterfaceUsuario extends Thread {

    private Elemento elem;

    private Buffer b;

    private InputStream entrada;

    private ObjectInputStream objin;

    private BufferedInputStream buffer;

    private Socket socket;

    public InterfaceUsuario(Socket socket, Buffer b) {
        this.socket = socket;
        this.b = b;
    }

    @Override
    public void run() {
        try {
            entrada = socket.getInputStream();
            buffer = new BufferedInputStream(entrada);
            objin = new ObjectInputStream(buffer);
            buffer.mark(10);
            while (buffer.read() != -1) {
                try {
                    buffer.reset();
                } catch (IOException ex) {
                    Logger.getLogger(InterfaceUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                elem = recibir();
                buffer.mark(10);
                System.out.println("recibido un elemento " + elem.numero + "de cliente " + elem.cliente);
                b.poner(elem);
                System.out.println("elemento encolado");
            }
        } catch (IOException ex) {
            Logger.getLogger(InterfaceUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Elemento recibir() {
        try {
            elem = (Elemento) objin.readObject();
        } catch (IOException ex) {
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return elem;
    }

    public void enviar(Elemento elem) {
    }
}
