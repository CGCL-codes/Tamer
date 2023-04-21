package net.sourceforge.fsync.message;

public class PortResponse implements Message {

    private static final long serialVersionUID = 8440457479829612541L;

    int port;

    public PortResponse(int port) {
        super();
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String toString() {
        return "PortResponse :: Port " + port;
    }
}
