package p2;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

public class ServidorFecha extends UnicastRemoteObject implements FechaInterface {

    private static final long serialVersionUID = -2121596542529869876L;

    public ServidorFecha() throws RemoteException {
        super();
    }

    public Date getDate() throws RemoteException {
        System.out.println("llamada de un cliente");
        return new Date();
    }

    public static void main(String args[]) {
        try {
            ServidorFecha servidor = new ServidorFecha();
            Naming.rebind("rmi://localhost/servidor_fecha", servidor);
            System.out.println("Servidor de Fecha ...");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
