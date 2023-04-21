package net.doepner.ws.web;

import org.apache.wicket.protocol.http.WebApplication;

/**
 * WsWebApp ...
 *
 * Design-Pattern-Role: 
 * @version $Id: java,v 1.3 2006/07/17 13:13:58 oliver Exp $
 * @author Oliver Doepner
 */
public class WsWebApp extends WebApplication {

    @Override
    public Class<Home> getHomePage() {
        return Home.class;
    }
}
