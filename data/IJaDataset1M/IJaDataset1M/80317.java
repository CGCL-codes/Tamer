package onepoint.service.server;

public final class XLocalServer extends XServer {

    private static XLocalServer _instance;

    public final void start() {
        if (_instance == null) _instance = this;
    }

    static XLocalServer getInstance() {
        return _instance;
    }
}
