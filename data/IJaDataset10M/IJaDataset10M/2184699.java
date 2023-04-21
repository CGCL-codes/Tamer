package restlet.description.convert;

import java.util.HashMap;
import java.util.Map;

public class RESTResourceDescription {

    protected String uri;

    boolean deployed;

    protected Handler handler;

    protected Map<String, Handler> getHandlers = new HashMap<String, Handler>();

    protected Map<String, Handler> postHandlers = new HashMap<String, Handler>();

    protected Map<String, Handler> putHandlers = new HashMap<String, Handler>();

    protected Handler deleteHandler;

    protected Handler getHandler;

    protected Handler postHandler;

    protected Handler putHandler;

    public boolean hasMediaTypeHandlers() {
        return deleteHandler != null || getHandlers.size() > 0 || putHandlers.size() > 0 || postHandlers.size() > 0;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler resourceHandler) {
        this.handler = resourceHandler;
    }

    public Map<String, Handler> getGetHandlers() {
        return getHandlers;
    }

    public void setGetHandlers(Map<String, Handler> getHandlers) {
        this.getHandlers = getHandlers;
    }

    public Map<String, Handler> getPostHandlers() {
        return postHandlers;
    }

    public void setPostHandlers(Map<String, Handler> postHandlers) {
        this.postHandlers = postHandlers;
    }

    public Map<String, Handler> getPutHandlers() {
        return putHandlers;
    }

    public void setPutHandlers(Map<String, Handler> putHandlers) {
        this.putHandlers = putHandlers;
    }

    public Handler getDeleteHandler() {
        return deleteHandler;
    }

    public void setDeleteHandler(Handler deleteHandler) {
        this.deleteHandler = deleteHandler;
    }

    public Handler getGetHandler() {
        return getHandler;
    }

    public void setGetHandler(Handler getHandler) {
        this.getHandler = getHandler;
    }

    public Handler getPostHandler() {
        return postHandler;
    }

    public void setPostHandler(Handler postHandler) {
        this.postHandler = postHandler;
    }

    public Handler getPutHandler() {
        return putHandler;
    }

    public void setPutHandler(Handler putHandler) {
        this.putHandler = putHandler;
    }

    public boolean isDeployed() {
        return deployed;
    }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
    }
}
