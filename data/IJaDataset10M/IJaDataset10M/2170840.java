package ca.sqlpower.wabit;

/**
 * This interface defines methods that will be called when server information is
 * added or removed from the context.
 */
public interface ServerListListener {

    public void serverAdded(ServerListEvent e);

    public void serverRemoved(ServerListEvent e);
}
