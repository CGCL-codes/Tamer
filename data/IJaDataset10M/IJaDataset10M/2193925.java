package sun.net.spi.nameservice;

import java.net.UnknownHostException;

public interface NameService {

    public byte[][] lookupAllHostAddr(String host) throws UnknownHostException;

    public String getHostByAddr(byte[] addr) throws UnknownHostException;
}
