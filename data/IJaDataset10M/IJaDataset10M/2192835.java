package pedrociarlini.sintoniadoamor.thread;

import java.io.IOException;
import java.net.Socket;

public class Cliente extends Conector {

    public Cliente() {
        setName("Conector CLIENTE");
    }

    protected Socket conecta() {
        System.out.println("Iniciando conex�o...");
        Socket myServer = null;
        try {
            myServer = new Socket(configuracao.getIp(), configuracao.getPorta());
        } catch (IOException e) {
            System.out.println(e);
        }
        return myServer;
    }
}
