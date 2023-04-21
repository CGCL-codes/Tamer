package org.modss.facilitator.shared.help;

import org.modss.facilitator.shared.singleton.*;
import org.modss.facilitator.shared.resource.*;
import org.modss.facilitator.shared.browser.*;
import org.swzoo.log2.core.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.MissingResourceException;

/**
 * Implementation of the help manager interface.
 */
public class DefaultHelpManager implements HelpManager {

    /**
     * Obtain a help URL.  The help manager implementation knows where the
     * help is located.  Invokers of this method know about the help (user)
     * manual location and so the offsets provided will bear this knowledge.
     * The help manager implementation provides a URL which appends this offset
     * to its knowledge of the root location of the help manual.   As a an
     * example the help manager implementation may switch between local and
     * online versions of the help manual based on user preference settings.
     * <p>
     * The current implementation returns a "null" URL if there are any 
     * problems creating the URL.
     *
     * @param offset the offset into the user manual.
     * @return a URL reference which includes the base location of the help
     * manual with the offset appended.
     */
    public URL getHelpURL(String offset) {
        LogTools.trace(logger, 25, "HelpManagerImpl.getHelpURL(offset=" + offset + ")");
        if (offset == null) offset = "";
        boolean local = resources.getBooleanProperty("dss.help.local", true);
        LogTools.trace(logger, 25, "HelpManagerImpl.getHelpURL() - local=" + local);
        if (local) {
            try {
                String userguideOffset = resources.getProperty("dss.help.local.userguide", true);
                String installdir = resources.getProperty("dss.install.dir", true);
                String fileprotocol = resources.getProperty("dss.url.file.protocol", true);
                URL url = new URL(fileprotocol, null, installdir);
                URL baseurl = createURL(url, userguideOffset);
                URL finalurl = createURL(baseurl, offset);
                LogTools.trace(logger, 25, "HelpManagerImpl.getHelpURL() - Local url is [" + finalurl.toExternalForm() + "]");
                return finalurl;
            } catch (MissingResourceException mrex) {
                LogTools.warn(logger, "HelpManagerImpl.getHelpURL() - Missing resource.  Message: " + mrex.getMessage());
                return null;
            } catch (MalformedURLException mex) {
                LogTools.warn(logger, "HelpManagerImpl.getHelpURL() - URL encoding problem.  Reason: " + mex.getMessage());
                return null;
            }
        } else {
            String baseText = resources.getProperty("dss.help.remote.url");
            if (baseText == null) {
                LogTools.warn(logger, "HelpManagerImpl.getHelpURL() - Property dss.help.remote.url not set.");
                return null;
            }
            try {
                URL base = new URL(baseText);
                LogTools.trace(logger, 25, "HelpManagerImpl.getHelpURL() - Remote URL base is [" + base.toExternalForm() + "]");
                URL finalurl = createURL(base, offset);
                LogTools.trace(logger, 25, "HelpManagerImpl.getHelpURL() - Remote url is [" + finalurl.toExternalForm() + "]");
                return finalurl;
            } catch (MalformedURLException mex) {
                LogTools.warn(logger, "HelpManagerImpl.getHelpURL() - URL encoding problem.  Reason: " + mex.getMessage());
                return null;
            }
        }
    }

    /**
     * Show help.  It is left up to the implementation as to how this is done.
     * One obvious way is to show the help in a browser.
     *
     * @param offset the offset into the user (help) manual.
     */
    public void showHelp(String offset) {
        LogTools.trace(logger, 25, "HelpManagerImpl.showHelp(offset=" + offset + ")");
        URL url = getHelpURL(offset);
        if (url == null) {
            LogTools.warn(logger, "HelpManagerImpl.showHelp() - URL is null; bailing");
            return;
        }
        browser.showURL(url);
    }

    /**
     * Create a URL by appending the offset.
     *
     * @param base the base URL.
     * @param offset the offset to append to the base URL.
     * @return a URL which is a concatenation of the base url and offset.
     */
    URL createURL(URL base, String offset) throws MalformedURLException {
        LogTools.trace(logger, 25, "HelpManagerImpl.createURL(base=" + base.toExternalForm() + ",offset=" + offset + ")");
        String urlSeparator = resources.getProperty("dss.url.separator", "/");
        String file = base.getFile();
        if (file == null) {
            file = "";
        } else {
            if (!file.substring(file.length() - 1, file.length()).equals(urlSeparator)) {
                file = new String(file + urlSeparator);
            }
        }
        LogTools.trace(logger, 25, "HelpManagerImpl.createURL() - File portion of base URL is [" + file + "]");
        String newFile = new String(file + offset);
        LogTools.trace(logger, 25, "HelpManagerImpl.createURL() - New file portion is [" + newFile + "]");
        URL newURL = new URL(base.getProtocol(), base.getHost(), newFile);
        LogTools.trace(logger, 25, "HelpManagerImpl.createURL() - New URL is [" + newURL + "]");
        return newURL;
    }

    /** Logger. */
    private static final Logger logger = LogFactory.getLogger();

    /** Resource provider. */
    private static final ResourceProvider resources = Singleton.Factory.getInstance().getResourceProvider();

    /** Browser manager. */
    private static final BrowserManager browser = Singleton.Factory.getInstance().getBrowserManager();
}
