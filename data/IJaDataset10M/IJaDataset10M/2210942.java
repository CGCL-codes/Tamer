package ee.cafe;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.Page;

public class Application extends WebApplication {

    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }
}
