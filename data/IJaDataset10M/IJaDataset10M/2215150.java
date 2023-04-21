package nodomain.applewhat.torrentdemonio.protocol;

public interface IncomingConnectionEventProducer {

    void addIncomingConnectionListener(IncomingConnectionListener listener);
}
