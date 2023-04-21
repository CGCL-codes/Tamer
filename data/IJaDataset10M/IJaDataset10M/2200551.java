package net.sourceforge.fsync.portmanager;

public interface PortManager {

    public Integer requestPort();

    public void releasePort(Integer port);
}
