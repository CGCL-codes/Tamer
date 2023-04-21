package net.wastl.webmail.plugins;

import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.exceptions.*;
import java.util.*;
import java.text.*;
import java.io.*;
import javax.mail.*;
import net.wastl.webmail.misc.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This plugin shows the Form for attaching files to a message as well as does
 * the actual attaching to a WebMailSession
 *
 * provides: attach
 * requires: composer
 *
 * @author Sebastian Schaffert
 */
public class FileAttacher implements URLHandler, Plugin {

    private static Log log = LogFactory.getLog(FileAttacher.class);

    public static final String VERSION = "1.00";

    public static final String URL = "/compose/attach";

    Storage store;

    public FileAttacher() {
    }

    public void register(WebMailServer parent) {
        parent.getURLHandler().registerHandler(URL, this);
        this.store = parent.getStorage();
        parent.getConfigScheme().configRegisterStringKey("MAX ATTACH SIZE", "1000000", "Maximum size of attachments in bytes");
    }

    public String getName() {
        return "FileAttacher";
    }

    public String getDescription() {
        return "This URL-Handler handles file attachments for the Composer.";
    }

    public String getVersion() {
        return VERSION;
    }

    public String getURL() {
        return URL;
    }

    public HTMLDocument handleURL(String suburl, HTTPSession sess, HTTPRequestHeader head) throws WebMailException {
        if (sess == null) {
            throw new WebMailException("No session was given. If you feel this is incorrect, please contact your system administrator");
        }
        WebMailSession session = (WebMailSession) sess;
        UserData user = session.getUser();
        if (head.isContentSet("ADD")) {
            try {
                ByteStore bs = (ByteStore) head.getObjContent("FILE");
                String description = "";
                if (head.isContentSet("DESCRIPTION")) {
                    description = new String(((ByteStore) head.getObjContent("DESCRIPTION")).getBytes());
                }
                String fileName = bs.getName();
                if (!((fileName == null) || fileName.equals(""))) {
                    int offset = fileName.lastIndexOf("\\");
                    fileName = fileName.substring(offset + 1);
                    fileName = new String(fileName.getBytes("ISO8859_1"), "UTF-8");
                    bs.setName(fileName);
                }
                if ((description != null) && (!description.equals(""))) description = new String(description.getBytes("ISO8859_1"), "UTF-8");
                if (bs != null && bs.getSize() > 0) {
                    session.addWorkAttachment(bs.getName(), bs, description);
                }
            } catch (Exception e) {
                log.error("Could not attach file", e);
                throw new DocumentNotFoundException("Could not attach file. (Reason: " + e.getMessage() + ")");
            }
        } else if (head.isContentSet("DELETE") && head.isContentSet("ATTACHMENTS")) {
            try {
                String attachmentName = head.getContent("ATTACHMENTS");
                attachmentName = new String(attachmentName.getBytes("ISO8859_1"), "UTF-8");
                log.info("Removing " + attachmentName);
                session.removeWorkAttachment(attachmentName);
            } catch (Exception e) {
                log.error("Could not remove attachment", e);
                throw new DocumentNotFoundException("Could not remove attachment. (Reason: " + e.getMessage() + ")");
            }
        }
        return new XHTMLDocument(session.getModel(), store.getStylesheet("compose_attach.xsl", user.getPreferredLocale(), user.getTheme()));
    }

    public String provides() {
        return "attach";
    }

    public String requires() {
        return "composer";
    }
}
