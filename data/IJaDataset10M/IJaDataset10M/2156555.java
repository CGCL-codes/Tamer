package servidor;

import java.rmi.RemoteException;
import java.util.Hashtable;
import libreriaChat.IfzCliente;
import libreriaChat.ManejadorMensaje;

public class ManejadorMsg implements ManejadorMensaje {

    private String chat = "";

    @Override
    public void enviarMensaje(String mensaje) throws RemoteException {
        chat = chat + mensaje + System.getProperty("line.separator");
    }

    @Override
    public String recibirMensajes() throws RemoteException {
        return chat;
    }
}
