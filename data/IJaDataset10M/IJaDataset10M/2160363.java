package org.one.stone.soup.wiki.access.control;

import java.io.IOException;
import java.net.Socket;
import org.one.stone.soup.authentication.server.Login;
import org.one.stone.soup.server.http.HttpResponseHeader;
import org.one.stone.soup.server.http.authentication.HttpAuthenticator;
import org.one.stone.soup.server.http.helpers.HttpRequestHelper;
import org.one.stone.soup.wiki.file.manager.FileManager;
import org.one.stone.soup.wiki.file.manager.FileManagerInterface;
import org.one.stone.soup.xml.XmlElement;

public class HttpSessionCookieAuthenticator implements HttpAuthenticator {

    private String domain;

    private FileManagerInterface fileManager;

    public HttpSessionCookieAuthenticator(FileManagerInterface fileManager, String domain) throws IOException {
        this.fileManager = fileManager;
        this.domain = domain;
    }

    public HttpSessionCookieAuthenticator(FileManager fileManager, String domain) throws IOException {
        this.domain = domain;
        this.fileManager = fileManager;
    }

    public Login getLogin(XmlElement httpHeader) throws Exception {
        String memberAlias = null;
        try {
            memberAlias = fileManager.getAuthenticator().authenticateUser(getSessionId(httpHeader));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (memberAlias == null) {
            Login login = new Login(domain, "Anon", null);
            return login;
        }
        Login login = new Login(domain, memberAlias, null);
        return login;
    }

    public boolean isAllowed(XmlElement httpHeader) throws Exception {
        Login login = getLogin(httpHeader);
        boolean allowed = fileManager.getAuthorisor().memberCanPerformAction(login.getUser().getName(), WikiAuthorisor.READ, httpHeader.getElementByName("request").getValue());
        if (allowed) {
            httpHeader.addAttribute("user", login.getUser().getName());
            httpHeader.addAttribute("session", login.getPebble());
            System.out.println("Authorisation request Passed. Allowed in " + httpHeader.getElementByName("request").getValue());
        } else {
            System.out.println("Authorisation request Rejected. Not allowed in " + httpHeader.getElementByName("request").getValue());
        }
        return allowed;
    }

    public boolean obtainAuthorisation(XmlElement httpHeader, Socket socket) throws Exception {
        HttpResponseHeader responseHeader = new HttpResponseHeader(httpHeader, "text/html", "302 Redirect");
        responseHeader.addParameter("location", "/SignIn");
        responseHeader.writeHeader(socket.getOutputStream());
        socket.getOutputStream().flush();
        socket.close();
        return false;
    }

    private String getSessionId(XmlElement httpHeader) {
        XmlElement parameters = HttpRequestHelper.parseHttpCookieParameters(httpHeader);
        XmlElement session = parameters.getElementByName("wikiSession");
        if (session == null) {
            return null;
        }
        return session.getValue();
    }
}
