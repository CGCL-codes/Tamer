package de.lema.transfer.container;

import de.lema.transfer.RequestContainer;
import de.lema.transfer.ServerRequest;

public class InstanzenZuAnwendungLadenRequest extends RequestContainer {

    private static final long serialVersionUID = 1L;

    private final String anwendung;

    public InstanzenZuAnwendungLadenRequest(String anwendung) {
        super(ServerRequest.InstanzenZuAnwendungLaden);
        this.anwendung = anwendung;
    }

    public String getAnwendung() {
        return anwendung;
    }

    @Override
    public String getParameters() {
        return anwendung;
    }
}
