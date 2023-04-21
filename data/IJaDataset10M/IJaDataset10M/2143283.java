package example.btl2capecho.server;

public interface ServerConnectionHandlerListener {

    public void handleOpen(ServerConnectionHandler handler);

    public void handleOpenError(ServerConnectionHandler handler, String errorMessage);

    public void handleReceivedMessage(ServerConnectionHandler handler, byte[] messageBytes);

    public void handleQueuedMessageWasSent(ServerConnectionHandler handler, Integer id);

    public void handleClose(ServerConnectionHandler handler);

    public void handleErrorClose(ServerConnectionHandler handler, String errorMessage);
}
