package rtm4java.impl.util;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Defines what a http client must do in the context of this library
 * 
 * @author <a href="mailto:nerab@gmx.at">Andreas E. Rabenau</a>
 */
public interface HttpClient {

    public String get(URL url) throws UnknownHostException, IOException;
}
