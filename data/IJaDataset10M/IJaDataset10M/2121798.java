package bufferings.kotori.server.application;

import org.slim3.util.ApplicationMessage;
import org.slim3.util.RequestLocator;
import org.slim3.util.ServletContextLocator;
import bufferings.kotori.client.application.CalendarService;

/**
 * The server side implementation of the RPC service.
 */
public class CalendarServiceImpl implements CalendarService {

    public String greetServer(String input) {
        String serverInfo = ServletContextLocator.get().getServerInfo();
        String userAgent = RequestLocator.get().getHeader("User-Agent");
        return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>" + userAgent + "<br><br>" + ApplicationMessage.get("validator.range", 2, 1, 3);
    }
}
