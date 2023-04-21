package bluetooth;

/**
 * @author pmp
 */
public interface ServerListener extends BtListener {

    public void clientConnected(String btAdress);
}
