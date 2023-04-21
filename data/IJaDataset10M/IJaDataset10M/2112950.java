package br.com.sysmap.crux.core.client.event;

/**
 * Crux event abstraction. 
 * @author Thiago
 */
public class Event {

    private String id;

    private String controller;

    private String method;

    public Event(String id, String controller, String method) {
        this.id = id;
        this.controller = controller;
        this.method = method;
    }

    public String getId() {
        return id;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
