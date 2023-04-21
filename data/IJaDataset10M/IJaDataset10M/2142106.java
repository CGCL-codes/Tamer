package org.devtools.wiki;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.devtools.util.MultiProperties;
import org.devtools.webtrans.AuthorManager;
import org.devtools.webtrans.PersistenceException;
import org.devtools.webtrans.TranslatorServices;
import org.devtools.webtrans.WebContent;

/**
 * @author: Avery Regier
 */
public class WikiAuthorManager implements AuthorManager {

    public static final String REQUESTED_WIKI_NAME_KEY = "wikiName";

    public static final String UNKNOWN_USER = "Unknown";

    public static final String THIS_CLIENT_KEY = "thisClient";

    public static final String USER_KEY = "user";

    public static final String AUTHOR_KEY = "author";

    protected MultiProperties authorMap;

    protected String filename;

    protected String defaultAuthorType;

    protected TranslatorServices srv;

    /**
	 * @see org.devtools.webtrans.AuthorManager#applyAuthorTo(java.util.Properties, org.devtools.webtrans.WebContent)
	 */
    public void applyAuthorTo(Properties options, WebContent page) throws PersistenceException {
        String user = options.getProperty(USER_KEY);
        if (user != null) {
            D.msg("user is " + user);
            user = getAssociatedWikiName(user);
            D.msg("wikiName is " + user);
            page.setProperty(AUTHOR_KEY, user);
        } else {
            String client = options.getProperty(THIS_CLIENT_KEY);
            if (client != null) {
                String ipMap = srv.getProperty(client);
                if (ipMap != null) page.setProperty(AUTHOR_KEY, ipMap); else {
                    try {
                        InetAddress ipa = InetAddress.getByName(client);
                        page.setProperty(AUTHOR_KEY, ipa.getHostName());
                    } catch (UnknownHostException uhe) {
                        page.setProperty(AUTHOR_KEY, client);
                    }
                }
            }
        }
    }

    /**
	 * @see org.devtools.webtrans.AuthorManager#changeAssociatedWikiName(java.lang.String, java.lang.String)
	 */
    public void changeAssociatedWikiName(String userIdentifier, String wikiName) {
        String old = (String) authorMap.get(userIdentifier);
        authorMap.put(userIdentifier, wikiName);
        if (old != null) authorMap.put(old, wikiName);
        D.msg("Changing " + userIdentifier + " from " + old + " to " + wikiName);
        save();
    }

    private void createDebugInterface(TranslatorServices srv) {
        D = new org.devtools.util.debug.Debugger("WikiServices", srv.getDebugOutput());
        D.setDisplayGroups(true);
        D.enable("*");
    }

    /**
	 * Just return the useridentifier as the wiki name.  Subclasses can be a bit
	 * smarter about this.
	 * 
	 * @param userIdentifier
	 * @return String
	 */
    protected String createWikiName(String userIdentifier) {
        return userIdentifier;
    }

    /**
	 * Gets a wiki name from the "wikiAuthorsFile".  If it doesn't exist, call
	 * createWikiName() and add the results of that in.
	 * 
	 * @see org.devtools.webtrans.AuthorManager#getAssociatedWikiName(java.lang.String)
	 */
    public String getAssociatedWikiName(String userIdentifier) {
        if (userIdentifier == null) return UNKNOWN_USER;
        String wikiName = authorMap.getProperty(userIdentifier);
        if (wikiName == null) {
            authorMap.list(D.out);
            wikiName = createWikiName(userIdentifier);
            authorMap.put(userIdentifier, wikiName);
            save();
        }
        D.out.println(wikiName + " getAssociatedWikiName(" + userIdentifier + ")");
        return wikiName;
    }

    /**
	 * First get the remote user id.  If that fails, get the remote address.
	 * Otherwise return "Unknown"
	 * 
	 * @see org.devtools.webtrans.AuthorManager#getCurrentUserIdentifier(javax.servlet.http.HttpServletRequest)
	 */
    public String getCurrentUserIdentifier(HttpServletRequest req) {
        String user = req.getRemoteUser();
        if (user == null) {
            user = req.getRemoteAddr();
        }
        if (user == null) {
            user = UNKNOWN_USER;
        }
        return user;
    }

    /**
	 * @see org.devtools.webtrans.AuthorManager#getDefaultAuthorType()
	 */
    public String getDefaultAuthorType() {
        return defaultAuthorType;
    }

    /**
	 * @see org.devtools.webtrans.AuthorManager#init(org.devtools.webtrans.TranslatorServices)
	 */
    public void init(TranslatorServices srv) {
        createDebugInterface(srv);
        this.srv = srv;
        defaultAuthorType = srv.getProperty("OnsiteLinkTypes");
        if (defaultAuthorType == null) {
            defaultAuthorType = srv.getType();
        } else {
            int pos = defaultAuthorType.indexOf(":");
            if (pos > 0) {
                defaultAuthorType = defaultAuthorType.substring(0, pos);
            }
        }
        filename = srv.getProperty("wikiAuthorsFile");
        if (filename == null) filename = "wikiAuthors.properties";
        authorMap = new MultiProperties();
        File f = new File(filename);
        if (f.exists()) {
            try {
                FileInputStream in = new FileInputStream(f);
                authorMap.load(in);
            } catch (IOException e) {
                D.msg("IOException loading " + f + ": " + e.getMessage());
            }
        }
        D.msg("Author Map successfully created");
        authorMap.list(D.out);
    }

    protected void save() {
        new Thread() {

            public void run() {
                try {
                    authorMap.store(new FileOutputStream(filename), "author=WikiName");
                } catch (IOException e) {
                    D.msg("Cannot save map of authors to WikiNames: " + e.getMessage());
                }
            }
        }.start();
    }

    /**
	 * @see org.devtools.webtrans.AuthorManager#setAuthor(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.util.Properties)
	 */
    public void setAuthor(HttpServletRequest req, HttpServletResponse res, Properties query) {
        String ips = req.getRemoteAddr();
        if (ips != null) {
            query.put(THIS_CLIENT_KEY, ips);
        }
        String user = getCurrentUserIdentifier(req);
        if (user != null) {
            query.put(USER_KEY, user);
        }
        String wikiName = getRequestedWikiName(req);
        if (wikiName != null) {
            if (user != null) {
                changeAssociatedWikiName(user, wikiName);
            } else if (ips != null) {
                changeAssociatedWikiName(ips, wikiName);
            }
        }
    }

    public String getRequestedWikiName(HttpServletRequest req) {
        String wikiName = req.getParameter(REQUESTED_WIKI_NAME_KEY);
        if (wikiName != null) {
            wikiName = wikiIze(wikiName, true);
        }
        return wikiName;
    }

    /**
	 * @see org.devtools.webtrans.AuthorManager#transferAuthor(java.util.Properties, java.util.Properties)
	 */
    public void transferAuthor(Properties from, Properties to) {
        to.put(THIS_CLIENT_KEY, from.getProperty(THIS_CLIENT_KEY));
        to.put(USER_KEY, from.getProperty(USER_KEY));
    }

    /**
	 * Capitalize the first letter of the string and leave the rest as is
	 * 
	 * @param s
	 * @return String
	 */
    public String capitalizeFirst(String s, boolean userRequested) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + (userRequested ? s.substring(1) : s.substring(1).toLowerCase());
    }

    /**
	 * Capitalizes the first letter of each word and squashes the words
	 * together.
	 * 
	 * @param string
	 * @return String
	 */
    public String wikiIze(String string, boolean userRequested) {
        String find = " ";
        if (string == null) return null;
        StringBuffer buffer = new StringBuffer();
        int previousLoc = 0;
        int loc = string.indexOf(find, previousLoc);
        while ((loc != -1) && (previousLoc < string.length())) {
            buffer.append(capitalizeFirst(string.substring(previousLoc, loc), userRequested));
            previousLoc = (loc + find.length());
            loc = string.indexOf(find, previousLoc);
        }
        buffer.append(capitalizeFirst(string.substring(previousLoc, string.length()), userRequested));
        return (buffer.toString());
    }

    protected org.devtools.util.debug.Debugger D;

    private static org.devtools.util.debug.Debugger SD = new org.devtools.util.debug.Debugger("WikiServices.static");

    {
        SD.setDisplayGroups(true);
        SD.enable("*");
    }
}
