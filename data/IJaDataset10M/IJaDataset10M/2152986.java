package es.uclm.inf_cr.alarcos.desglosa_web.exception;

public class MarketNotFoundException extends Exception {

    private static final long serialVersionUID = 5364261293913699563L;

    public MarketNotFoundException() {
        super();
    }

    public MarketNotFoundException(String message) {
        super(message);
    }
}
