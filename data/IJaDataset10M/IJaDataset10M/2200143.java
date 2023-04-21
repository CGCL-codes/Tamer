package au.com.cahaya.asas.net.mail;

import java.io.File;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMultipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import au.com.cahaya.asas.util.StringUtil;

/**
 * This message extends the parent to add functionality for
 * embedding of images.
 *
 * @author Mathew Pole
 * @since June 2002
 * @version ${Revision}
 */
public class HtmlMessage extends AttachmentMessage {

    /** The private logger for this class */
    private Logger myLog = LoggerFactory.getLogger(HtmlMessage.class);

    File myPath;

    /**
   *
   */
    public HtmlMessage(Session session) {
        this(session, null);
    }

    /**
   *
   * @param path is the base path for images to be attached
   */
    public HtmlMessage(Session session, File path) {
        super(session, new MimeMultipart("related"));
        myPath = path;
    }

    /**
   *
   */
    public void clearContent() throws MessagingException {
        clearContent("related");
    }

    /**
   * This method inserts cid: after all &lt;img src=" references,
   * where the url is relative (i.e. does not specify a protocol
   * and server.
   */
    public StringBuffer embedImages(String description) throws MessagingException {
        myLog.debug("embedImages - myPath = {}", myPath);
        StringBuffer sb = new StringBuffer(description.length() + 100);
        String lower = description.toLowerCase();
        int current = 0;
        int last = 0;
        while ((current = lower.indexOf("<img", last)) >= 0) {
            current = current + 2;
            int s = lower.indexOf("src=\"", current);
            int q = lower.indexOf("\"", s + 5);
            int c = lower.indexOf(">", current);
            if (s < c) {
                sb.append(description.substring(last, s + 5));
                if (lower.substring(s + 5, q).indexOf("://") == -1) {
                    sb.append("cid:");
                    File f = new File(description.substring(s + 5, q));
                    ;
                    if ((!f.canRead()) && (myPath != null)) {
                        f = new File(myPath, f.getPath());
                    }
                    if (f.canRead()) {
                        attachFile(f);
                        sb.append(f.getName());
                        sb.append(description.substring(q, c + 1));
                    } else {
                        myLog.error("embedImages - img: myPath = {}, file = {}", myPath, f);
                    }
                } else {
                    sb.append(description.substring(s + 5, c + 1));
                }
            } else {
                sb.append(description.substring(last, c + 1));
            }
            last = c + 1;
        }
        sb.append(description.substring(last));
        description = sb.toString();
        lower = description.toLowerCase();
        sb = new StringBuffer();
        current = 0;
        last = 0;
        while ((current = lower.indexOf(" background=\"", last)) >= 0) {
            int s = current;
            int q = lower.indexOf("\"", s + 13);
            int c = lower.indexOf(">", current);
            {
                Object[] t = { current, last, s, q, c };
                myLog.debug("embedImages - current = {}, last = {}, s = {}, q = {}, c = {}", t);
            }
            if ((s + 13 < q) && s < c) {
                sb.append(description.substring(last, s + 13));
                if (lower.substring(s + 13, q).indexOf("://") == -1) {
                    sb.append("cid:");
                    File f = new File(description.substring(s + 13, q));
                    if ((!f.canRead()) && (myPath != null)) {
                        f = new File(myPath, f.getPath());
                    }
                    if (f.canRead()) {
                        attachFile(f);
                        sb.append(f.getName());
                        sb.append(description.substring(q, c + 1));
                    } else {
                        myLog.error("embedImages - background: myPath = {}, file = {}", myPath, f);
                    }
                } else {
                    sb.append(description.substring(s + 13, c + 1));
                }
            } else {
                sb.append(description.substring(last, c + 1));
            }
            last = c + 1;
        }
        sb.append(description.substring(last));
        description = sb.toString();
        lower = description.toLowerCase();
        sb = new StringBuffer();
        current = 0;
        last = 0;
        while ((current = lower.indexOf(" url(", last)) >= 0) {
            int s = current;
            int c = lower.indexOf(")", s + 4);
            {
                Object[] t = { current, last, s, c };
                myLog.debug("embedImages - current = {}, last = {}, s = {}, c = {}", t);
            }
            sb.append(description.substring(last, s + 5));
            if (lower.substring(s + 5, c).indexOf("://") == -1) {
                sb.append("cid:");
                File f = new File(description.substring(s + 5, c));
                if ((!f.canRead()) && (myPath != null)) {
                    f = new File(myPath, f.getPath());
                }
                if (f.canRead()) {
                    attachFile(f);
                    sb.append(f.getName());
                } else {
                    myLog.error("embedImages - background: myPath = {}, file = {}", myPath, f);
                }
            } else {
                sb.append(description.substring(s + 5, c));
            }
            last = c;
        }
        sb.append(description.substring(last));
        return StringUtil.ascii128Convert(sb);
    }
}
