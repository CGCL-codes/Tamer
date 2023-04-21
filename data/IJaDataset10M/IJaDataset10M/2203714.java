package xdocument;

import net.GroupClient;
import event.*;

public interface GDocumentClient<D extends GDocument, E> extends MessageListener, ConnectionListener, EditBuffering {

    public void connect() throws Exception;

    public void disconnect();

    public void messageReceived(MessageEvent e);

    public void connectionClosed(ConnectionEvent ce);

    public void setDocument(D doc);

    public D getDocument();

    /**
    * Returns true if client has a connection with the group's server and
    * either has received the current state of the document or is the only
    * person in the group.
    *
    * @return false if client does not have a connection with the group's
    *         server or does not have the current state of the document.
    *         Returns true otherwise.
    **/
    public boolean isConnected();

    /**
    * Handles a local user insertion.
    *
    * @param e  Document event information.
    **/
    public void insertUpdate(E e);

    /**
    * Handles a local user removal.
    *
    * @param e  Document event information.
    **/
    public void removeUpdate(E e);

    /**
    * Handles a local user change.
    *
    * @param e  Document event information.
    **/
    public void changedUpdate(E e);
}
