package com.cred.industries.platform.server;

import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import com.sun.jersey.api.client.WebResource;

public class ServerInterface implements IServerResource {

    private static String url = "http://localhost:8080/webportal/resource";

    private WebResource resource;

    @Override
    public WebResource getResource() {
        return resource;
    }

    public void setResource(WebResource resource) {
        this.resource = resource;
    }

    public ServerInterface(WebResource resource) {
        this.resource = resource;
    }

    public static URI getBaseURI() {
        return UriBuilder.fromUri(url).build();
    }

    public static String getURL() {
        return url;
    }
}
