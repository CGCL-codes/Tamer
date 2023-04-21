package info.reflectionsofmind.connexion.platform.core.transport;

public interface IClientNode {

    IServerToClientTransport getTransport();

    void send(String contents) throws TransportException;
}
