package client;

import logic.Document;
import logic.Template;
import net.retrievers.Retriever;
import net.rmi.List;
import net.rmi.Logon;
import net.rmi.Reporter;

public abstract class Factory {

    public abstract Retriever<Template> getTemplateRetriever();

    public abstract Retriever<Document> getDocumentRetriever();

    public abstract Logon getLogon();

    public abstract Reporter getReporter();

    public abstract List getList();

    private static Factory actual = new LocalFactory();

    public static enum Connection {

        UDP, TCP, RMI, CORBA
    }

    public static void init(Connection type) {
        switch(type) {
            case UDP:
                actual = new DatagrammeFactory();
                break;
            case TCP:
                actual = new TcpFactory();
                break;
            case RMI:
                actual = new RmiFactory();
                break;
            case CORBA:
                actual = new CorbaFactory();
                break;
            default:
                break;
        }
    }

    public static Factory getFactory() {
        if (actual == null) throw new IllegalStateException("actual factory has not been initialized");
        return actual;
    }
}
