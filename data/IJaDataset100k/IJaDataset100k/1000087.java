package org.columba.mail.spam.spamassassin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.columba.core.base.OSInfo;
import org.columba.core.gui.externaltools.AbstractExternalToolsPlugin;

/**
 * @author fdietz
 */
public class SALearnExternalToolPlugin extends AbstractExternalToolsPlugin {

    protected static URL websiteURL;

    static {
        try {
            websiteURL = new URL("http://www.spamassassin.org/");
        } catch (MalformedURLException mue) {
        }
    }

    File defaultLinux = new File("/usr/bin/sa-learn");

    File defaultLocalLinux = new File("/usr/local/bin/sa-learn");

    public SALearnExternalToolPlugin() {
        super();
    }

    public String getDescription() {
        return "<html><body><p>sa-learn - train SpamAssassin's Bayesian classifier</p></body></html>";
    }

    public URL getWebsite() {
        return websiteURL;
    }

    public File locate() {
        if (OSInfo.isLinux() || OSInfo.isSolaris()) {
            if (defaultLinux.exists()) {
                return defaultLinux;
            } else if (defaultLocalLinux.exists()) {
                return defaultLocalLinux;
            }
        }
        return null;
    }
}
