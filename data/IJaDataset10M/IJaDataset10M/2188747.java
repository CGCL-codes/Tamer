package net.sf.doolin.app.sc.engine;

import java.io.Serializable;
import java.util.Locale;

public class ClientConnection implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String clientName;

    private final Locale locale;

    public ClientConnection(String clientName, Locale locale) {
        this.clientName = clientName;
        this.locale = locale;
    }

    public String getClientName() {
        return this.clientName;
    }

    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public String toString() {
        return this.clientName;
    }
}
