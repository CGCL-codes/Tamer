package condivisi;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia per le callback
 * @author Bandettini Alberto
 * @author Lottarini Andrea
 * @version BitCreekPeer 1.0
 */
public interface InterfacciaCallback extends Remote {

    /**
     * Metodo esportato dal peer al server :
     * avvisa il peer che ha pubblicato il file con nome nome
     * che l' IP lo ha cercato
     * @param ind IP da notificare
     * @param nome nome del file cercato
     * @throws java.rmi.RemoteException
     */
    public void notifyMe(InetAddress ind, String nome) throws RemoteException;
}
