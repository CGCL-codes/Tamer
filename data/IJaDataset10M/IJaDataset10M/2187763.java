package net.wastl.webmail.plugins;

import java.util.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.HTTPRequestHeader;
import net.wastl.webmail.misc.*;
import net.wastl.webmail.exceptions.*;

/**
 * The content bar on the left.
 *
 * provides: content bar
 * requires:
 *
 * @author Sebastian Schaffert
 */
public class NavBar implements Plugin, URLHandler {

    public static final String VERSION = "2.0";

    public static final String URL = "/content";

    String template;

    String bar;

    Storage store;

    public NavBar() {
    }

    public void register(WebMailServer parent) {
        this.store = parent.getStorage();
        parent.getURLHandler().registerHandler(URL, this);
    }

    public String getVersion() {
        return VERSION;
    }

    public String getName() {
        return "ContentBar";
    }

    public String getDescription() {
        return "This is the content-bar on the left frame in the mailbox window. " + "ContentProviders register with this content-bar to have a link and an icon added.";
    }

    public String getURL() {
        return URL;
    }

    public HTMLDocument handleURL(String suburl, HTTPSession session, HTTPRequestHeader header) throws WebMailException {
        if (session == null) {
            throw new WebMailException("No session was given. If you feel this is incorrect, please contact your system administrator");
        }
        WebMailSession sess = (WebMailSession) session;
        UserData user = sess.getUser();
        return new XHTMLDocument(session.getModel(), store.getStylesheet("navbar.xsl", user.getPreferredLocale(), user.getTheme()));
    }

    public String provides() {
        return "content bar";
    }

    public String requires() {
        return "";
    }
}
