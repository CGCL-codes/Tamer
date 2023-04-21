package harima.AGameServer.ServerSide.Exceptions;

public class CannotStartServerException extends ServerException {

    public CannotStartServerException(Exception e) {
        super("Server cannot be started", e);
    }
}
