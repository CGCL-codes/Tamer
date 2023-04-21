package com.google.gwt.sample.contacts.server;

import com.google.gwt.sample.contacts.client.GreetingService;
import com.google.gwt.sample.contacts.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

    public String greetServer(String input) throws IllegalArgumentException {
        if (!FieldVerifier.isValidName(input)) {
            throw new IllegalArgumentException("Name must be at least 4 characters long");
        }
        String serverInfo = getServletContext().getServerInfo();
        String userAgent = getThreadLocalRequest().getHeader("User-Agent");
        return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>" + userAgent;
    }
}
