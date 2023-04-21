package evs.testiap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import evs.brp.RemotingErrorException;
import evs.iap.ServerRequestHandler;

public class TestServer {

    private Log log = LogFactory.getLog(TestServer.class);

    public void printSyntax() {
        System.err.println("Syntax: TestServer PORT NameServerURI");
        System.exit(-1);
    }

    public static void main(String[] args) {
        TestServer testServer = new TestServer();
        int port = -1;
        if (args.length != 2) {
            testServer.printSyntax();
        } else {
            try {
                port = new Integer(args[0]).intValue();
            } catch (Exception exc) {
                testServer.printSyntax();
            }
        }
        testServer.startServerRequestHandler(port, args[1]);
    }

    public void startServerRequestHandler(int port, String nameServerUri) {
        try {
            ServerRequestHandler serverRequestHandler = new ServerRequestHandler(port, nameServerUri);
        } catch (RemotingErrorException exc) {
            log.error(exc);
        }
    }
}
