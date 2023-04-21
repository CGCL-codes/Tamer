package sourceforge.pebblesframewor.gwt.server;

import sourceforge.pebblesframewor.gwt.client.DataService;
import sourceforge.pebblesframewor.gwt.server.test.OlapModuleTest;
import sourceforge.pebblesframewor.gwt.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements DataService {

    public String testMessage() throws IllegalArgumentException {
        System.out.println("testMessage() invoked.");
        OlapModuleTest omt = new OlapModuleTest();
        String consoleOut = omt.initServer();
        System.out.println("console out complete with content: " + consoleOut);
        return consoleOut;
    }
}
