package au.edu.uq.itee.maenad.restletexample;

import org.restlet.Component;
import org.restlet.data.Protocol;

/**
 * The component sets up the server configuration.
 *
 * In the example we attach a HTTP server connector to a hardcoded port, which
 * is the part that will accept connections. The classpath client connector is
 * used to load files directly from the classpath, e.g. images or JavaScript code.
 *
 * The last part is attaching the actual application to the component, the
 * component is then attached to the Servlet engine using the web.xml file.
 */
public class ExampleComponent extends Component {

    public ExampleComponent() {
        getServers().add(Protocol.HTTP, 8181);
        getClients().add(Protocol.CLAP);
        getDefaultHost().attach(new ExampleApplication());
    }
}
