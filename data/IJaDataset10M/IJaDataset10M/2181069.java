package xlion.maildisk.server.intf;

public interface IConnectionConfig {

    String getHostName();

    String getProtocol();

    int getPort();

    boolean isSSL();
}
