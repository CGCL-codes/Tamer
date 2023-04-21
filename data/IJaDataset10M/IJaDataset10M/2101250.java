package logger.sd.server;

import java.util.Observer;

public interface LoggingServer {

    public void start();

    public void stop();

    public boolean isRunning();

    public void addListener(Observer o);

    public void tokenArrive();

    public boolean hasToken();

    public void passarToken();

    public void addChange();

    public Integer getChanges();

    public void clearChanges();

    public Long getId();

    public ServerConfiguration getConfig();

    public void setConfig(ServerConfiguration config);
}
