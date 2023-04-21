package fr.jade.examples.fraclite.orb.helloworld;

import fr.jade.fraclite.api.control.GenericAttributeController;
import fr.jade.fraclite.api.control.NoSuchAttributeException;

public class ServerImpl implements Service, GenericAttributeController {

    protected static final String headerAttName = "header";

    protected String header = "";

    protected static final String countAttName = "count";

    protected int count = 0;

    protected static final String[] listAtt = new String[] { headerAttName, countAttName };

    public void print(final String msg) {
        System.out.println("Server: begin printing...");
        for (int i = 0; i < count; ++i) {
            System.out.println(header + msg);
        }
        System.out.println("Server: print done.");
    }

    public String getAttribute(String name) throws NoSuchAttributeException {
        if (name.equals(headerAttName)) return header;
        if (name.equals(countAttName)) return String.valueOf(count);
        throw new NoSuchAttributeException(name);
    }

    public String[] listFcAtt() {
        return listAtt;
    }

    public void setAttribute(String name, String value) throws NoSuchAttributeException {
        if (name.equals(headerAttName)) {
            header = value;
        } else if (name.equals(countAttName)) {
            count = Integer.parseInt(value);
        } else {
            throw new NoSuchAttributeException(name);
        }
    }
}
