package gnu.javax.net.ssl.provider;

import javax.net.ssl.SSLException;

class AlertException extends SSLException {

    private final Alert alert;

    private final boolean isLocal;

    AlertException(Alert alert, boolean isLocal) {
        super(alert.getDescription().toString());
        this.alert = alert;
        this.isLocal = isLocal;
    }

    public String getMessage() {
        return alert.getDescription() + ": " + (isLocal ? "locally generated; " : "remotely generated; ") + alert.getLevel();
    }

    public Alert getAlert() {
        return alert;
    }
}
