package gnu.mail.providers.nntp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;

/**
 * A JavaMail MIME message delegate for an NNTP article.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 2.0
 */
public final class NNTPMessage extends MimeMessage {

    String messageId;

    NNTPMessage(NNTPFolder folder, int msgnum, String messageId) {
        super(folder, msgnum);
        this.messageId = messageId;
        headers = null;
        flags = folder.getPermanentFlags();
        if (folder.isSeen(msgnum)) {
            flags.add(Flags.Flag.SEEN);
        } else {
            flags.remove(Flags.Flag.SEEN);
        }
    }

    public String getMessageId() {
        return messageId;
    }

    void requestHeaders() throws MessagingException {
        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        NNTPMessage[] messages = new NNTPMessage[1];
        messages[0] = this;
        folder.fetch(messages, fp);
    }

    void updateHeaders(InputStream in) throws MessagingException, IOException {
        headers = new InternetHeaders(in);
    }

    void requestContent() throws MessagingException {
        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.CONTENT_INFO);
        NNTPMessage[] messages = new NNTPMessage[1];
        messages[0] = this;
        folder.fetch(messages, fp);
    }

    void updateContent(byte[] content) {
        this.content = content;
    }

    public String[] getHeader(String name) throws MessagingException {
        if (headers == null) {
            requestHeaders();
        }
        return super.getHeader(name);
    }

    public String getHeader(String name, String delimiter) throws MessagingException {
        if (headers == null) {
            requestHeaders();
        }
        return super.getHeader(name, delimiter);
    }

    public Enumeration getAllHeaders() throws MessagingException {
        if (headers == null) {
            requestHeaders();
        }
        return super.getAllHeaders();
    }

    public Enumeration getMatchingHeaders(String[] names) throws MessagingException {
        if (headers == null) {
            requestHeaders();
        }
        return super.getMatchingHeaders(names);
    }

    public Enumeration getNonMatchingHeaders(String[] names) throws MessagingException {
        if (headers == null) {
            requestHeaders();
        }
        return super.getNonMatchingHeaders(names);
    }

    public Enumeration getAllHeaderLines() throws MessagingException {
        if (headers == null) {
            requestHeaders();
        }
        return super.getAllHeaderLines();
    }

    public Enumeration getMatchingHeaderLines(String[] names) throws MessagingException {
        if (headers == null) {
            requestHeaders();
        }
        return super.getMatchingHeaderLines(names);
    }

    public Enumeration getNonMatchingHeaderLines(String[] names) throws MessagingException {
        if (headers == null) {
            requestHeaders();
        }
        return super.getNonMatchingHeaderLines(names);
    }

    public int getSize() throws MessagingException {
        if (content == null) {
            requestContent();
        }
        return super.getSize();
    }

    public int getLineCount() throws MessagingException {
        String value = getHeader("Lines", ",");
        if (value != null) {
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
            }
        }
        return -1;
    }

    public InputStream getContentStream() throws MessagingException {
        if (content == null) {
            requestContent();
        }
        return super.getContentStream();
    }

    public void saveChanges() throws MessagingException {
        if (headers == null) {
            requestHeaders();
        }
        if (content == null) {
            requestContent();
        }
    }

    public void setFlags(Flags flag, boolean set) throws MessagingException {
        if (flag.contains(Flags.Flag.SEEN)) {
            ((NNTPFolder) folder).setSeen(msgnum, set);
        }
        super.setFlags(flag, set);
    }
}
