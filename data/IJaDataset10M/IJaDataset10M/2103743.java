package org.das2.jythoncompletion;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import org.das2.jythoncompletion.support.CompletionDocumentation;

/**
 *
 * @author jbf
 */
public class DefaultDocumentationItem implements CompletionDocumentation {

    String link;

    String text;

    /** Creates a new instance of UserDocumentationItem */
    public DefaultDocumentationItem(String link) {
        this(link, null);
    }

    public DefaultDocumentationItem(String link, String text) {
        this.link = link;
        this.text = text;
    }

    public String getText() {
        if (text != null) {
            return text;
        }
        URL url = getURL();
        if (url == null) {
            return "<html>unable to resolve link: <br>" + link + "</html>";
        } else {
            return null;
        }
    }

    public URL getURL() {
        URL result = resolveURL(this.link);
        System.err.println("getURL=" + result);
        return result;
    }

    private static URL resolveURL(String link) {
        if (link == null) {
            return null;
        }
        if (link.contains("://")) {
            try {
                return new URL(link);
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            URL url;
            int i = link.indexOf("#");
            if (i == -1) {
                url = DefaultDocumentationItem.class.getResource(link);
            } else {
                url = DefaultDocumentationItem.class.getResource(link.substring(0, i));
                try {
                    url = new URL(url, link.substring(i));
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                }
            }
            return url;
        }
    }

    public CompletionDocumentation resolveLink(String string) {
        try {
            URL url = new URL(new URL(link), string);
            return new DefaultDocumentationItem(url.toString());
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    public Action getGotoSourceAction() {
        return null;
    }
}
