package fi.foyt.cs.facebook;

import static com.google.appengine.api.urlfetch.FetchOptions.Builder.doNotValidateCertificate;
import java.io.IOException;
import java.net.URL;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.restfb.DefaultWebRequestor;

public class GAEWebRequestor extends DefaultWebRequestor {

    @Override
    public Response executeGet(String url) throws IOException {
        URL aUrl = new URL(url);
        HTTPMethod httpMethod = HTTPMethod.GET;
        HTTPRequest httpRequest = new HTTPRequest(aUrl, httpMethod, doNotValidateCertificate());
        URLFetchService service = URLFetchServiceFactory.getURLFetchService();
        HTTPResponse response = service.fetch(httpRequest);
        System.out.println(new String(response.getContent(), "UTF-8"));
        return new Response(response.getResponseCode(), new String(response.getContent(), "UTF-8"));
    }
}
