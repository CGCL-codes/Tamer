package wtanaka.praya.msn;

/**
 * ConnectionReaderListener, interface for listening on a reader
 *            
 */
public interface ConnectionReaderListener {

    void readerClosed();

    void readerCommandRecived(Response response);

    void readerMessageRecived(Message message);

    void readerErrorRecived(ErrorCode error);
}
