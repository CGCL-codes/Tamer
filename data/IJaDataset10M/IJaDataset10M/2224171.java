package sc.fgrid.client;

/**
 * Used to register an event in case a server reports an error, but does not
 * disconnect by its own. The return value of the method in this interface
 * decides whether the server will disconnect.
 */
public interface RecoverableErrorListener {

    /**
     * This method should return immediately. It runs in a server thread which
     * polls all kinds of information from the server and is blocked as long as
     * this method runs. It is intended that long running actions are performed
     * in a separate thread, presumably with a call of
     * java.awt.EventQueue.invokeLater(...) in case the handler belongs to a
     * GUI.
     * 
     * @return if true is returned, then the server will disconnect, if false it
     *         will continue.
     */
    boolean handle(Exception cause);
}
