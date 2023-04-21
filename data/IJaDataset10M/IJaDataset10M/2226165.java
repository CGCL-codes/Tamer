package ws;

public interface DaoJuegoService extends javax.xml.rpc.Service {

    public java.lang.String getDaoJuegoAddress();

    public ws.DaoJuego getDaoJuego() throws javax.xml.rpc.ServiceException;

    public ws.DaoJuego getDaoJuego(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
