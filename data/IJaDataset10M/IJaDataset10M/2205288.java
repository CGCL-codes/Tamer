package com.session;

import java.net.SocketException;
import java.util.ArrayList;
import com.util.Strings;

public class ShipmentXmail implements Shipment {

    private Message message;

    private Domain domain;

    public ShipmentXmail(Message message, Domain domain) throws Exception {
        this.message = message;
        this.domain = domain;
    }

    public void send() throws Exception, SocketException {
        Session session = new Session(domain);
        session.connect(message.getSourceUser());
        ArrayList<String> users = new ArrayList<String>();
        for (int i = 0; i < message.getDestains().length; i++) {
            if (Strings.getMailDomain(message.getDestains()[i]).compareTo(domain.getDomain()) == 0) {
                users.add(message.getDestains()[i]);
            }
        }
        session.sendFile(message.getSourceUser(), message.getSubject(), users.toArray(), message.getFile());
    }
}
