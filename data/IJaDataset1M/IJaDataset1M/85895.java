package lytt.xenu;

import lytt.disco.AppletServer;
import lytt.disco.HTTPServer;
import lytt.disco.Debug;
import java.lang.Runtime;

/**
 * If this xenulayer object is run as a thread, a webserver will start on the tini
 * <p>Example: Webserver w = new Webserver().start();</p>
 * <br />Version 1.1 updates:
 * <ul>
 * 	<li>Extended documentation</li>
 *	<li>100% Debugging</li>
 * </ul>
 *
 * @author Tim Fennis <timmfenis@xs4all.nl>
 * @version 1.1
 * @since 1.0
 * @see lytt.xenu.HTTPServer
 */
public class Webserver extends Thread {

    /**
	 * Holds the instance of the lytt.disco.HTTPServer
	 */
    HTTPServer server = null;

    /**
	 *
	 */
    AppletServer as = null;

    /**
	 *
	 */
    public Webserver() {
        Debug.message("lytt.xenu.Webserver::Webserver() executing applet server");
        try {
            lytt.disco.Registry.appletServer = new AppletServer(4711);
            Thread t = new Thread(lytt.disco.Registry.appletServer);
            t.start();
            Debug.message("lytt.xenu.Webserver::Webserver() Applet server is up and running");
        } catch (Exception e) {
            Debug.message("lytt.xenu.Webserver::Webserver() failed to run applet server, exception passed to debugger");
            Debug.exception(e);
        }
    }

    /**
	 * @return Instance of disco.HTTPServer object
	 */
    public HTTPServer getInstance() {
        return server;
    }

    /**
	 * This method is autocalled when this thread is started, it will call the startServer method of lytt.disco.HTTPServer and will result in a infinite loop handeling web requests.
	 */
    public void run() {
        Debug.message("lytt.xenu.Webserver::run() Running lytt.disco.HTTPServer waiting for response");
        server = new HTTPServer();
        Debug.message("lytt.xenu.Webserver::run() Config succefully loaded starting server now");
        server.startServer();
        Debug.message("lytt.xenu.Webserver::run() Statment unreachable report Tim when this error occures");
    }
}
