package networking;

import java.io.Serializable;

/**
 * Constructs all types of messages used in socket communication
 * @author Arvanitis Ioannis
 */
public class SocketMessageFactory implements Serializable {

    private int port;

    private String msgType;

    private int totalNodes;

    private StringBuffer sb = new StringBuffer(0);

    public int getTotalNodes() {
        return totalNodes;
    }

    public void setTotalNodes(int totalNodes) {
        this.totalNodes = totalNodes;
    }

    public StringBuffer getSb() {
        return sb;
    }

    public void setSb(StringBuffer sb) {
        this.sb = sb;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
