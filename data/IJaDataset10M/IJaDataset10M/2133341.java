package de.etqw.openranked.domain;

public class Server implements HasId {

    private long id;

    private String ip;

    private int port;

    private int status;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
