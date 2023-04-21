package net.lukemurphey.nsia.scan.scriptenvironment;

import java.net.MalformedURLException;
import java.net.URL;

public class GetMethod extends WebClient {

    public GetMethod(String url) throws MalformedURLException {
        super(HttpMethod.GET, url);
    }

    public GetMethod(URL url) {
        super(HttpMethod.GET, url);
    }
}
