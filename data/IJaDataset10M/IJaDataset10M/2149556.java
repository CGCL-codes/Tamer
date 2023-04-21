package borknet_services.core;

import borknet_services.core.*;

public class Server {

    private String numeric;

    private String host;

    private String hub;

    private boolean service;

    public Server(String numeric) {
        this.numeric = numeric;
    }

    public String getNumeric() {
        return numeric;
    }

    public void setNumeric(String s) {
        numeric = s;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String s) {
        host = s;
    }

    public String getHub() {
        return hub;
    }

    public void setHub(String s) {
        hub = s;
    }

    public Boolean getService() {
        return service;
    }

    public void setService(Boolean s) {
        service = s;
    }
}
