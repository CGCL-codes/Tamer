package ar.com.arkios.kfcajitas;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;

/**
 * <p>
 * Esta clase encapsula todo dispositivo físico con el que se comunica la 
 * aplicación.
 * </p>
 * 
 * <p>
 * Existen en principio dos tipos de dispositivos:
 * </p>
 * <ul>
 *      <li>Cliente</li>
 *      <li>Servidor</li>
 * </ul>
 * 
 * <p>
 * <b>Cliente:</b> se le crea un thread para atender los mensajes que son 
 * enviados por el dispositivo.
 * </p>
 * 
 * <p>
 * <b>Servidor:</b> por el momento solo se tiene el socket en el que se deben
 * realizar las peticiones de datos al dispositivo. En principio, no es 
 * necesario un thread dedicado a la comunicación. Las clases que hereden de 
 * esta clase siendo su tipo de dispositivo <i>Servidor</i>, deberían tener
 * métodos que permitan enviar datos al dispositivos y estos métodos enviar los
 * mismos haciendo uso del Socket asociado al dispositivo.
 * </p>
 * 
 * <p>
 * TODO: lo ideal sería que la comunicación del dispositivo a través del socket
 * quede encapsulada en esta clase. Existe una interfaz Protocolo la cual debería
 * ser indicada para cualquier tipo de Dispositivo donde se define como recibir
 * o enviar un dato a través del socket.
 * </p>
 * 
 * <p>
 * En caso de ser necesario, si la comunicación involucra tener un manejo del 
 * Socket mas profundo del genérico definido en esta clase, se puede sobreescribir
 * el método en las clases que hereden de esta para manipular a gusto la 
 * comunicación. Pero en principio la idea sería que el socket lo administre esta
 * clase y la lectura de los datos recibidos o el envío de datos se lleve a cabo
 * a trabés del Protocolo definido.
 * </p>
 */
public abstract class Dispositivo extends Observable {

    private Socket miSocket;

    private SocketListener miSocketListener;

    /**
     * Constructor utilizado para dispositivos que hacen de servidores. Es decir
     * que se debe conectar al mismo para solicitar información.
     * 
     * @param elSocket socket a través del cual se va a llevar a cabo la 
     *          comunicación.
     */
    public Dispositivo(Socket elSocket) {
        miSocket = elSocket;
    }

    /**
     * Constructor utilizados cuando el dispositivo es un cliente y necesita 
     * de un thread para escuchar los mensajes del mismo.
     * 
     * @param elSocket socket por el cual se comunica con el dispositivo físico.
     * @param elProtocolo protocolo de comunicacion con el dispositivo físico.
     */
    public Dispositivo(Socket elSocket, Protocolo elProtocolo) {
        miSocket = elSocket;
        miSocketListener = new SocketListener(this, elProtocolo);
        miSocketListener.start();
    }

    public Socket getMiSocket() {
        return miSocket;
    }

    public void setMiSocket(Socket miSocket) {
        this.miSocket = miSocket;
    }

    /**
     * <p>
     * Thread que se encarga de escuchar los mensajes envíados por el 
     * dispositivo cliente.
     * </p>
     */
    private class SocketListener extends Thread {

        /**  */
        private Dispositivo miDispositivo;

        /** Protocolo a ser utilizado para la recepción de datos. */
        private Protocolo miProtocolo;

        public SocketListener(Dispositivo elDispositivo, Protocolo elProtocolo) {
            this.miDispositivo = elDispositivo;
            this.miProtocolo = elProtocolo;
        }

        @Override
        public void run() {
            Socket socket = miDispositivo.getMiSocket();
            while (socket != null && socket.isConnected()) {
                try {
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    miProtocolo.receive(input);
                } catch (IOException e) {
                }
            }
        }
    }
}
