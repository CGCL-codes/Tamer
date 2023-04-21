package pruebaasincrona;

import java.util.LinkedList;

public class Puerto {

    private int numeroMensajes = 0;

    private LinkedList mensajes = new LinkedList();

    public synchronized void send(Object o) {
        numeroMensajes++;
        mensajes.add(o);
        if (numeroMensajes <= 0) notify();
    }

    public synchronized Object recieve() {
        Object mensajeRecibido = null;
        numeroMensajes--;
        if (numeroMensajes < 0) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        mensajeRecibido = mensajes.pollFirst();
        return mensajeRecibido;
    }
}
