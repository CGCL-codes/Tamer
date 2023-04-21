package ramon.http;

import java.io.InputStream;
import java.net.URL;

public interface StaticConnector {

    URL getResource(String name);

    InputStream getResourceAsStream(String name);
}
