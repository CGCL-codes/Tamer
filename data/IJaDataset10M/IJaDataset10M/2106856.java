package sparrow.util;

import java.io.Serializable;
import java.net.URL;
import java.util.Map;

public class InitData implements Serializable {

    /**
   * stores the IP in wich the receiver wait the results
   */
    private String IP_;

    /**
   * stores the port in wich the receiver wait the results
   */
    private int port_;

    /**
   * Stores the URLs to be sent to the workers 
   */
    private URL[] URLs_;

    /**
   * Constructor
   * @param urls The urls
   */
    public InitData(URL[] urls) {
        URLs_ = urls;
    }

    /**
   * Returns the URLs
   * @return The URLs
   */
    public URL[] getURLs() {
        return URLs_;
    }

    /**
   * sets the IP
   */
    public void setIP(String IP) {
        IP_ = IP;
    }

    /** 
   * gets the IP
   */
    public String getIP() {
        return IP_;
    }

    /**
   * sets the port
   */
    public void setPort(int port) {
        port_ = port;
    }

    /**
   * gets the port
   */
    public int getPort() {
        return port_;
    }
}
