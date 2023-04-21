package net.sourceforge.nekomud.service;

import java.util.List;
import net.sourceforge.nekomud.nio.Connection;

public interface NetworkService {

    public List<Connection> getConnections();

    public void start();

    public void stop();
}
