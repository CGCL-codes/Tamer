package nanosn.webapplication.core;

import firstSteps.HelloWorldResource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.restlet.Client;
import org.restlet.Component;

/**
 *
 * @author selkhateeb
 */
public class ApplicationComponent extends Component {

    public ApplicationComponent() {
        this.getClients().add(new Client("HTTP"));
        this.getClients().add(new Client("FILE"));
        this.getDefaultHost().attach(".*/htdocs/", new StaticFilesApplication());
        this.getDefaultHost().attachDefault(HelloWorldResource.class);
    }
}
