package de.fu_berlin.inf.gmanda.imports;

import gnu.mail.providers.mbox.MboxStore;
import gnu.mail.treeutil.StatusEvent;
import gnu.mail.treeutil.StatusListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import de.fu_berlin.inf.gmanda.gui.TextView;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;
import de.fu_berlin.inf.gmanda.util.progress.IProgress.ProgressStyle;

public class MyMimeUtils {

    private static final Logger log = Logger.getLogger(MyMimeUtils.class);

    public static String getBody(Message message) throws MessagingException {
        StringBuilder sb = new StringBuilder();
        try {
            String subject = getSubject(message);
            String from = getFrom(message);
            String to = getRecipients(message, Message.RecipientType.TO);
            String cc = getRecipients(message, Message.RecipientType.CC);
            String bcc = getRecipients(message, Message.RecipientType.BCC);
            String date = getDate(message);
            if (isSet(from)) {
                sb.append("From: " + from).append("\n");
            }
            if (isSet(to)) {
                sb.append("To  : " + to).append("\n");
            }
            if (isSet(cc)) {
                sb.append("CC  : " + cc).append("\n");
            }
            if (isSet(bcc)) {
                sb.append("BCC : " + bcc).append("\n");
            }
            if (isSet(date)) {
                sb.append("Date: " + date).append("\n");
            }
            sb.append("Subj: " + subject);
            sb = new StringBuilder(TextView.toHTMLBody(sb.toString()) + "<br><br>\n");
            sb.append(toString((Part) message));
        } catch (Exception e) {
            sb.append("Error reading mail content").append("\n");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            sb.append(sw.toString()).append("\n");
            log.error(sb.toString(), e);
        }
        return sb.toString();
    }

    private static boolean isSet(String cc) {
        return cc != null && cc.trim().length() > 0 && !"unknown".equals(cc);
    }

    public static Folder getFolder(File mboxFile) throws MessagingException {
        return getFolder(mboxFile, null);
    }

    public static Folder getFolder(final IProgress progress, File mboxFile) throws MessagingException {
        progress.setScale(100);
        progress.setStyle(ProgressStyle.DOUBLING);
        progress.setNote("Loading Gmane " + mboxFile.getAbsolutePath());
        Folder folder = MyMimeUtils.getFolder(mboxFile, new StatusListener() {

            public void statusOperationEnded(StatusEvent arg0) {
                progress.done();
            }

            public void statusOperationStarted(StatusEvent arg0) {
                progress.start();
            }

            public void statusProgressUpdate(StatusEvent arg0) {
                int status = arg0.getValue();
                progress.setProgress(status);
                if (status % 100 == 0) progress.setNote("Reading message: " + status);
            }
        });
        return folder;
    }

    public static Folder getFolder(File mboxFile, StatusListener listener) throws MessagingException {
        Session session = Session.getDefaultInstance(new Properties());
        MboxStore store = (MboxStore) session.getStore(new URLName("mbox:" + mboxFile.getAbsolutePath()));
        store.connect();
        if (listener != null) store.addStatusListener(listener);
        Folder folder = store.getDefaultFolder();
        folder.open(Folder.READ_ONLY);
        return folder;
    }

    public static String getDate(Message message) throws MessagingException {
        Date sentDate = message.getSentDate();
        String date;
        if (sentDate != null) {
            date = new DateTime(sentDate).toString();
        } else {
            date = MyMimeUtils.getFirst(message.getHeader("Date"));
            if (date == null) {
                log.warn("Message contains no date!");
            } else {
                date = date.trim().replaceAll("\\s+", " ");
                DateTime time = parse("EE MMM d HH:mm:ss yyyy", date);
                if (time == null) {
                    date = date.replaceAll(",", "").trim();
                    time = parse("d MMM yyyy HH:mm:ss Z", date);
                }
                if (time == null) {
                    log.warn("Could not parse date: " + date);
                    date = null;
                } else {
                    date = time.toString();
                }
            }
        }
        return unknown(date);
    }

    public static DateTime parse(String format, String date) {
        try {
            return DateTimeFormat.forPattern(format).parseDateTime(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getFrom(Message m) throws MessagingException {
        return unknown(getFromInternal(m));
    }

    public static String unknown(String string) {
        if (string == null || string.trim().length() == 0) return "unknown";
        return string;
    }

    protected static String getFromInternal(Message m) throws MessagingException {
        try {
            return toString(m.getFrom());
        } catch (AddressException e) {
            return toString(m.getHeader("From"));
        }
    }

    public static String getRecipients(Message m, Message.RecipientType type) throws MessagingException {
        try {
            return unknown(toString(m.getRecipients(type)));
        } catch (AddressException e) {
            return unknown(toString(m.getHeader(type.toString())));
        }
    }

    public static String toString(Address[] ads) {
        StringBuilder sb = new StringBuilder();
        if (ads == null) return null;
        boolean first = true;
        for (Address a : ads) {
            if (!first) sb.append(", "); else first = false;
            if (a instanceof InternetAddress) {
                InternetAddress i = (InternetAddress) a;
                sb.append(i.toUnicodeString());
            } else {
                sb.append(a.toString());
            }
        }
        return sb.toString();
    }

    public static String toString(Multipart mp) throws MessagingException, IOException {
        if (mp.getCount() == 0) return "";
        StringBuilder sb = new StringBuilder();
        if (StringUtils.indexOf(mp.getContentType(), "multipart/alternative") == 0) {
            int bestChoice = -1;
            for (int i = 0; i < mp.getCount(); i++) {
                if (StringUtils.indexOf(mp.getBodyPart(i).getContentType(), "text/plain") == 0) {
                    bestChoice = i;
                }
            }
            if (bestChoice == -1) bestChoice = mp.getCount() - 1;
            return sb.append(toString(mp.getBodyPart(bestChoice))).append('\n').toString();
        }
        for (int i = 0; i < mp.getCount(); i++) {
            sb.append(toString(mp.getBodyPart(i))).append('\n');
        }
        return sb.toString();
    }

    public static String toString(Part part) throws MessagingException, IOException {
        StringBuilder sb = new StringBuilder();
        try {
            if (part.getDisposition() != null && !part.getDisposition().equals("inline")) sb.append(TextView.toHTMLBody(">-- " + part.getDisposition() + '\n'));
        } catch (ParseException e) {
            sb.append(TextView.toHTMLBody(">-- Disposition decoding problem\n"));
        }
        Object content;
        try {
            content = part.getContent();
        } catch (UnsupportedEncodingException e) {
            content = "Message could not be decoded: " + e.getMessage() + "\n";
        }
        if (content instanceof String) {
            if (!"text/html".equals(part.getContentType())) sb.append(TextView.toHTMLBody((String) content)); else sb.append(((String) content).replaceFirst("^.*?<body[^>]*>", "").replaceFirst("</body>.*?$", ""));
        } else if (content instanceof Message) {
            sb.append(getBody((Message) content));
        } else if (content instanceof Multipart) {
            sb.append(toString((Multipart) content));
        } else if (content instanceof Part) {
            sb.append(toString((Part) content));
        } else if (content instanceof InputStream) {
            sb.append(TextView.toHTMLBody(StringUtils.abbreviate(IOUtils.toString((InputStream) content), 1000)));
        } else {
            sb.append(TextView.toHTMLBody("UNKNOWN CONTENT: " + content));
        }
        sb.append("<br>\n");
        return sb.toString();
    }

    public static String toString(String[] ss) {
        if (ss == null) return "";
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : ss) {
            if (!first) sb.append("\n"); else first = false;
            try {
                sb.append(MimeUtility.decodeText(s));
            } catch (UnsupportedEncodingException e) {
                sb.append(s);
            }
        }
        return sb.toString();
    }

    public static String getFirst(String[] header) {
        if (header == null || header.length == 0) return null;
        String result = header[0];
        int i = 1;
        while ((result == null || result.trim().length() == 0) && header.length > i) {
            result = header[i++];
        }
        if (result == null || result.trim().length() == 0) return null;
        return result;
    }

    public static String getSubject(Message message) throws MessagingException {
        return unknown(message.getSubject());
    }

    public static String getMessageID(Message message) throws MessagingException {
        String[] mids = message.getHeader("Message-ID");
        if (mids == null || mids.length == 0 || mids[0].trim().length() == 0) {
            log.error("No message ID found: " + Arrays.toString(mids));
            mids = new String[] { String.valueOf((long) (Math.random() * Integer.MAX_VALUE)) + String.valueOf((long) (Math.random() * Integer.MAX_VALUE)) };
            log.error("Generated MID: " + Arrays.toString(mids));
        }
        return mids[0];
    }

    public static String getContentType(Message message) throws MessagingException {
        return MyMimeUtils.getFirst(message.getHeader("Content-type"));
    }
}
