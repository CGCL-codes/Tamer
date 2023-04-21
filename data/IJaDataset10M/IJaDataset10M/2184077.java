package logic.mail;

import javax.mail.internet.*;
import java.util.*;
import java.io.*;
import java.util.Date;
import javax.mail.*;
import javax.mail.MessagingException;
import java.util.Properties;
import javax.mail.Session;
import com.sun.mail.smtp.SMTPMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.digester.SetRootRule;

/**
 * This class represents a single e-mail message.
 * @author jarek
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Mail {

    /**main class, storing almost everything about the message*/
    public SMTPMessage message;

    /**Message id*/
    public long id;

    /**Id of mail folder this message belongs to*/
    public long folderId;

    /**states whether this message is sent*/
    public boolean isSent;

    /**states whether this message is received*/
    public boolean isReceived;

    /**states whether this message is undone*/
    public boolean isUndone;

    /**states whether this e-mail is new and unread*/
    public boolean isNew;

    /**Mail body cached as String*/
    private String messageAsString;

    /**List of attachments*/
    private Part[] attachments;

    /**
	 * checks whether this mail part is MimeMultipart type
	 * @param obj
	 * @return
	 */
    private static boolean isMultipart(Object obj) {
        try {
            if (((obj.getClass().getName()).toLowerCase().indexOf("mimemultipart")) != -1) return true;
            if ((((Part) obj).getContent() != null) && ((((((Part) obj).getContent()).getClass().getName()).toLowerCase().indexOf("mimemultipart")) != -1)) return true;
        } catch (MessagingException e) {
        } catch (IOException e) {
        }
        return false;
    }

    /**
	 * checks whether this mail part is text type
	 * @param obj
	 * @return
	 */
    private static boolean isText(Object obj) {
        try {
            String contentType = ((Part) obj).getContentType();
            if ((contentType.toLowerCase().startsWith("text"))) return true;
        } catch (MessagingException e) {
        }
        return false;
    }

    /**
	 * checks whether this mail part is text/plain type
	 * @param obj
	 * @return
	 */
    private static boolean isPlainText(Object obj) {
        try {
            String contentType = ((Part) obj).getContentType();
            if ((contentType.toLowerCase().startsWith("text/plain"))) return true;
        } catch (MessagingException e) {
        }
        return false;
    }

    /**
	 * checks whether this mail part is String
	 */
    private static boolean isString(Object obj) {
        if (((obj.getClass().getName()).indexOf("String")) != -1) return true;
        return false;
    }

    private static boolean isStream(Object obj) {
        if (((obj.getClass().getName()).indexOf("Stream")) != -1) return true;
        return false;
    }

    /**
	 * checks whether this mail part is text/html type
	 * @param obj
	 * @return
	 */
    private static boolean isHtmlText(Object obj) {
        try {
            String contentType = ((Part) obj).getContentType();
            if ((contentType.toLowerCase().startsWith("text/html"))) return true;
        } catch (MessagingException e) {
        }
        return false;
    }

    /**
	 * checks whether this mail part is an attachment
	 */
    private static boolean isAttachment(Object obj) {
        try {
            if (((Part) obj).getFileName() != null) return true;
            return false;
        } catch (MessagingException e) {
        }
        return false;
    }

    /**
     * Changes e-mail body to quotation (with '<' at the beginning of each line
     * @param text mail body
     * @return quoted message
     */
    public static String replyText(String text) {
        String reply = "", line;
        int poz = 0, prevPoz = 0;
        do {
            poz = text.indexOf("\n", prevPoz);
            if (poz == -1) line = text.substring(prevPoz); else {
                poz = poz + 1;
                line = text.substring(prevPoz, poz);
            }
            if (line.length() < 70) reply = reply + "> " + line; else {
                reply = reply + "> " + line.substring(0, 70) + "\n> " + line.substring(70);
            }
            prevPoz = poz;
        } while (poz != -1);
        return reply;
    }

    /**
	 * Prepares a reply message.
	 * @param replyToAll do we reply to all
	 * @param sender Recepients address.
	 * @return reply message with To: fields filled and qouted mail body. Attachments are removed.
	 */
    public static SMTPMessage reply(boolean replyToAll, InternetAddress sender, Mail mail) {
        try {
            SMTPMessage message = mail.message;
            MimeMessage reply = (MimeMessage) message.reply(false);
            reply.setRecipients(Message.RecipientType.TO, message.getFrom());
            reply.setFrom(sender);
            if (replyToAll) {
                Address[] to = message.getRecipients(Message.RecipientType.TO);
                Address[] cc = message.getRecipients(Message.RecipientType.CC);
                int i, count = 0, index = 0;
                if (to != null) for (i = 0; i < to.length; ++i) {
                    if (Mail.equal(sender, to[i])) count++;
                }
                if (cc != null) for (i = 0; i < cc.length; ++i) {
                    if (Mail.equal(sender, cc[i])) count++;
                }
                if (count > 0) {
                    Address[] array = new Address[count];
                    if (to != null) for (i = 0; i < to.length; ++i) {
                        if (Mail.equal(sender, to[i])) array[index] = to[i];
                        index++;
                    }
                    if (cc != null) for (i = 0; i < cc.length; ++i) {
                        if (Mail.equal(sender, cc[i])) array[index] = cc[i];
                        index++;
                    }
                    reply.setRecipients(Message.RecipientType.CC, array);
                }
            }
            Multipart multipart = new MimeMultipart();
            String text = mail.getPlainTextWithoutAttachments();
            if (text != null) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(replyText(text));
                multipart.addBodyPart(messageBodyPart);
            }
            reply.setContent(multipart);
            return new SMTPMessage(reply);
        } catch (MessagingException e) {
            System.out.println(e + "problem: reply");
        }
        return null;
    }

    /**
	 * Prepares a forward message.
	 * @param sender to who we send the message
	 * @return quoted message with attachments
	 */
    public static SMTPMessage forward(InternetAddress sender, Mail mail) {
        try {
            SMTPMessage message = mail.message;
            SMTPMessage forward;
            Properties props = new Properties();
            Session session = Session.getInstance(props, null);
            forward = new SMTPMessage(session);
            forward.setFrom(sender);
            forward.setSubject(mail.getSubject());
            Multipart multipart = new MimeMultipart();
            String text = mail.getPlainTextWithoutAttachments();
            if (text != null) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(replyText(text));
                multipart.addBodyPart(messageBodyPart);
            }
            forward.setContent(multipart);
            ArrayList arrayList = mail.getAttachmentsAsArrayList();
            int i, n;
            for (i = 0, n = arrayList.size(); i < n; ++i) {
                AttachmentDetails att = new AttachmentDetails();
                Part part = (Part) arrayList.get(i);
                multipart.addBodyPart((BodyPart) part);
            }
            return forward;
        } catch (MessagingException e) {
            System.out.println(e + "problem: reply");
        }
        return null;
    }

    /**Checks whether message is in mime format.
	 */
    private boolean isMimeType() {
        try {
            String contentType = message.getContentType();
            if (contentType.toLowerCase().startsWith("text") && isString(message.getContent())) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                if (((String) message.getContent()) == null) {
                    return false;
                }
                messageBodyPart.setText(((String) message.getContent()).trim());
                getReady(messageBodyPart);
                return true;
            }
            if (contentType.toLowerCase().startsWith("text") && isStream(message.getContent())) {
                InputStream stream = (InputStream) message.getContent();
                if (stream == null) {
                    return false;
                }
                byte[] b = new byte[stream.available()];
                stream.read(b);
                return true;
            }
            Object obj = message.getContent();
            if ((isMultipart(message))) {
                MimeMultipart multipart = (MimeMultipart) obj;
                boolean bool = isMimeType(multipart);
                if (bool == false) {
                    return false;
                }
                return true;
            }
        } catch (IOException e) {
            System.out.println(e + "problem:  isMimeType");
        } catch (MessagingException e) {
            System.out.println(e + " \n**************\nproblem:  isMimeType");
        }
        return false;
    }

    /** Checks is multipart object is in correct format.
	 */
    private static boolean isMimeType(MimeMultipart multipart) {
        try {
            boolean ret = true;
            boolean ok;
            String contentType = multipart.getContentType();
            for (int i = 0, n = multipart.getCount(); i < n; i++) {
                ok = false;
                Part part = multipart.getBodyPart(i);
                String partContentType = part.getContentType();
                if (partContentType == null) {
                    return false;
                }
                if (isAttachment(part) && (ok == false)) {
                    ok = true;
                    String partFilename = part.getFileName();
                    if (partFilename == null) {
                        return false;
                    }
                    InputStream stream = part.getInputStream();
                    if (stream == null) {
                        return false;
                    }
                    byte[] b = new byte[stream.available()];
                    stream.read(b);
                }
                if (isText(part) && (ok == false) && isString(((MimeBodyPart) part).getContent())) {
                    ok = true;
                    String text = (String) ((MimeBodyPart) part).getContent();
                    if (text == null) {
                        return false;
                    }
                }
                if (isText(part) && (ok == false) && isStream(((MimeBodyPart) part).getContent())) {
                    ok = true;
                    InputStream stream = (InputStream) (((MimeBodyPart) part).getContent());
                    if (stream == null) {
                        return false;
                    }
                    int ile = stream.available();
                    byte[] b = new byte[ile];
                    stream.read(b);
                    String text = new String(b);
                    if (ile == 0) {
                        text = "";
                    }
                    if ((text == null)) {
                        return false;
                    }
                }
                String disposition = part.getDisposition();
                if ((isMultipart(part)) && (ok == false)) {
                    ok = true;
                    ret = ret && isMimeType((MimeMultipart) part.getContent());
                }
                if (ok == false) {
                    return false;
                }
            }
            return ret;
        } catch (MessagingException e) {
            System.out.println(e + "\n----------------------\nproblem: isMimeType");
        } catch (IOException e) {
            System.out.println(e + "\n 1 problem: isMimeType");
        }
        return false;
    }

    /**
	 * Returns list of attachments from a message. 
	 */
    private ArrayList getAttachmentsAsArrayList() {
        try {
            String contentType = message.getContentType();
            Object obj = message.getContent();
            if ((isMultipart(message))) {
                MimeMultipart multipart = (MimeMultipart) obj;
                return getAttachmentsAsArray(multipart);
            }
        } catch (IOException e) {
            System.out.println(e + "problem: getAttachment");
        } catch (MessagingException e) {
            System.out.println(e + "problem: getAttachment");
        }
        return new ArrayList();
    }

    /**
	 * Returns a number of attachments.
	 */
    public int getAttachmentCount() {
        ArrayList array = getAttachmentsAsArrayList();
        if (array == null) return 0;
        return array.size();
    }

    /**
	 * returns an array of attachments stored as AttachmentDetails objects.
	 */
    public AttachmentDetails[] getAttachmentsDetails() {
        try {
            ArrayList arrayList = getAttachmentsAsArrayList();
            AttachmentDetails[] ret = new AttachmentDetails[arrayList.size()];
            int i, n;
            for (i = 0, n = arrayList.size(); i < n; ++i) {
                AttachmentDetails att = new AttachmentDetails();
                Part part = (Part) arrayList.get(i);
                att.filename = part.getFileName();
                if (att.filename == null) att.filename = "NoName";
                att.contentType = part.getContentType();
                InputStream stream = part.getInputStream();
                if ((stream.available() != 0) && ((((part.getContent()).getClass()).getName().indexOf("String")) == -1)) att.length = stream.available(); else att.length = ((String) part.getContent()).length();
                att.id = i;
                ret[i] = att;
            }
            Object[] att = arrayList.toArray();
            attachments = new Part[att.length];
            for (i = 0; i < att.length; ++i) {
                attachments[i] = (Part) att[i];
            }
            return ret;
        } catch (MessagingException e) {
            System.out.println(e + "problem: getAttachmentDetails");
        } catch (IOException e) {
            System.out.println(e + "problem: getAttachmentDetails");
        }
        return new AttachmentDetails[0];
    }

    /**
	 * Gets attachment list from multipart object
	 */
    private static ArrayList getAttachmentsAsArray(MimeMultipart multipart) {
        try {
            String contentType = multipart.getContentType();
            ArrayList array = new ArrayList();
            for (int i = 0, n = multipart.getCount(); i < n; i++) {
                Part part = multipart.getBodyPart(i);
                if (isAttachment(part)) {
                    array.add(part);
                }
                String partContentType = part.getContentType();
                if ((isMultipart(part))) {
                    array.addAll(getAttachmentsAsArray((MimeMultipart) part.getContent()));
                }
            }
            return array;
        } catch (MessagingException e) {
            System.out.println(e + "problem: getAttachment");
        } catch (IOException e) {
            System.out.println(e + "problem: getAttachment");
        }
        return new ArrayList();
    }

    /**
	 * Gets charset from contentType
	 */
    private static String getCharset(String contentType) {
        int poz1, poz2, poz3;
        poz1 = contentType.indexOf('\"');
        poz2 = contentType.indexOf('\"', poz1 + 1);
        if ((poz1 == -1) || (poz2 == -1)) {
            poz3 = contentType.indexOf("charset=");
            if (poz3 == -1) System.out.println("problem z charset"); else return contentType.substring(poz3 + ("charset=").length());
        }
        return contentType.substring(poz1 + 1, poz2);
    }

    /**dzieki temu porpawnie sie ustawia jedno z pol
	 * @param part
	 */
    private static void getReady(MimeBodyPart part) {
        try {
            Properties props = new Properties();
            Session session = Session.getInstance(props, null);
            SMTPMessage message = new SMTPMessage(session);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(part);
            message.setContent(multipart);
            ByteArrayOutputStream e = new ByteArrayOutputStream();
            message.writeTo(e);
            multipart.removeBodyPart(part);
        } catch (IOException e) {
            System.out.println(e + "problem: getReady");
        } catch (MessagingException e) {
            System.out.println(e + "problem: getRready");
        }
    }

    /**
	 * Returns an array of objects with plain/text content type.
	 */
    public ArrayList getPlainTextAsArray() {
        try {
            ArrayList array = new ArrayList();
            String contentType = message.getContentType();
            if (contentType.toLowerCase().startsWith("text/plain") && isString(message.getContent())) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(((String) message.getContent()).trim());
                getReady(messageBodyPart);
                array.add(messageBodyPart);
                return array;
            }
            if (contentType.toLowerCase().startsWith("text/plain") && isStream(message.getContent())) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                InputStream stream = (InputStream) message.getContent();
                byte[] b = new byte[stream.available()];
                stream.read(b);
                messageBodyPart.setText((new String(b)).trim());
                getReady(messageBodyPart);
                array.add(messageBodyPart);
                return array;
            }
            Object obj = message.getContent();
            if ((isMultipart(message))) {
                MimeMultipart multipart = (MimeMultipart) obj;
                return getPlainTextAsArray(multipart);
            }
        } catch (IOException e) {
            System.out.println(e + "problem: getPlainTextAsArray");
        } catch (MessagingException e) {
            System.out.println(e + "problem: getPlainTextAsArray");
        }
        return new ArrayList();
    }

    /**
	 * Returns an array of plain/text objects from multipart object.
	 */
    private static ArrayList getPlainTextAsArray(MimeMultipart multipart) {
        try {
            ArrayList array = new ArrayList();
            String contentType = multipart.getContentType();
            for (int i = 0, n = multipart.getCount(); i < n; i++) {
                Part part = multipart.getBodyPart(i);
                String partContentType = part.getContentType();
                if ((partContentType == null) || (partContentType.toLowerCase().startsWith("text/plain"))) {
                    array.add(part);
                }
                if ((isMultipart(part))) {
                    array.addAll(getPlainTextAsArray((MimeMultipart) part.getContent()));
                }
            }
            return array;
        } catch (MessagingException e) {
            System.out.println(e + "problem: getPlainText");
        } catch (IOException e) {
            System.out.println(e + "problem: getPlainText");
        }
        return new ArrayList();
    }

    /**
	 * returns message body without attachments.
	 */
    private String getPlainTextWithoutAttachments() {
        try {
            String ret = null;
            ArrayList array = getPlainTextAsArray();
            if (array.size() > 0) ret = "";
            for (int i = 0, n = array.size(); i < n; ++i) {
                MimeBodyPart part = (MimeBodyPart) array.get(i);
                if (part.getFileName() == null) if (isString(((MimeBodyPart) array.get(i)).getContent())) ret = ret + (String) ((MimeBodyPart) array.get(i)).getContent() + "\n"; else {
                    InputStream stream = (InputStream) ((MimeBodyPart) array.get(i)).getContent();
                    byte[] b = new byte[stream.available()];
                    stream.read(b);
                    ret = ret + (new String(b)) + "\n";
                }
            }
            return ret;
        } catch (MessagingException e) {
            System.out.println(e + "problem: getPlainText");
        } catch (IOException e) {
            System.out.println(e + "problem: getPlainText");
        }
        return null;
    }

    /**
	 * Returns message body and all parts with plain/text content type or getHtml()
	 */
    public String getPlainText() {
        try {
            String ret = null;
            ArrayList array = getPlainTextAsArray();
            if (array.size() > 0) ret = "";
            for (int i = 0, n = array.size(); i < n; ++i) {
                if (isString(((MimeBodyPart) array.get(i)).getContent())) ret = ret + (String) ((MimeBodyPart) array.get(i)).getContent() + "\n"; else {
                    InputStream stream = (InputStream) ((MimeBodyPart) array.get(i)).getContent();
                    byte[] b = new byte[stream.available()];
                    stream.read(b);
                    ret = ret + (new String(b)) + "\n";
                }
            }
            return ret;
        } catch (MessagingException e) {
            System.out.println(e + "problem: getPlainText");
        } catch (IOException e) {
            System.out.println(e + "problem: getPlainText");
        }
        return null;
    }

    /**
	 * Returns safe message body (to be displayed via http interface).
	 */
    public String getPlainTextSafe() {
        try {
            String ret = "";
            int i, n;
            ArrayList array = getPlainTextAsArray();
            if (array.size() > 0) ret = "";
            for (i = 0, n = array.size(); i < n; ++i) {
                MimeBodyPart part = (MimeBodyPart) array.get(i);
                ret = ret + "<hr>";
                if (isString(((MimeBodyPart) array.get(i)).getContent())) ret = ret + deleteMarks((String) ((MimeBodyPart) array.get(i)).getContent()) + "\n"; else {
                    InputStream stream = (InputStream) ((MimeBodyPart) array.get(i)).getContent();
                    byte[] b = new byte[stream.available()];
                    stream.read(b);
                    ret = ret + deleteMarks(new String(b)) + "\n";
                }
            }
            ArrayList arrayHtml = getHtmlTextAsArray();
            if (arrayHtml.size() > 0) for (i = 0, n = arrayHtml.size(); i < n; ++i) {
                ret = ret + "<hr>";
                if (isString(((MimeBodyPart) array.get(i)).getContent())) ret = ret + deleteMarks((String) ((MimeBodyPart) array.get(i)).getContent()) + "\n"; else {
                    InputStream stream = (InputStream) ((MimeBodyPart) array.get(i)).getContent();
                    byte[] b = new byte[stream.available()];
                    stream.read(b);
                    ret = ret + deleteMarks(new String(b)) + "\n";
                }
            }
            for (i = 0, n = array.size(); i < n; ++i) {
                MimeBodyPart part = (MimeBodyPart) array.get(i);
                if (part.getFileName() != null) {
                    ret = ret + "<hr>";
                    ret = ret + deleteMarks(part.getFileName() + "\n");
                    ret = ret + "<hr>";
                    Part att = part;
                    InputStream stream = att.getInputStream();
                    int length = stream.available();
                    byte[] b = new byte[length];
                    stream.read(b);
                    if ((length != 0) && ((((part.getContent()).getClass()).getName().indexOf("String")) == -1)) ret = ret + deleteMarks((new String(b)) + "\n"); else ret = ret + deleteMarks((String) att.getContent() + "\n");
                }
            }
            return ret;
        } catch (MessagingException e) {
            System.out.println(e + "problem: getPlainText");
        } catch (IOException e) {
            System.out.println(e + "problem: getPlainText");
        }
        return "";
    }

    /**
	 * Changes message body written in html, so that '/>' and '/<' characters are correctly viewed.
	 */
    public String getHtmlText() {
        try {
            ArrayList array = getHtmlTextAsArray();
            String ret = null;
            if (array.size() > 0) ret = "";
            for (int i = 0, n = array.size(); i < n; ++i) {
                if (isString(((MimeBodyPart) array.get(i)).getContent())) ret = ret + (String) ((MimeBodyPart) array.get(i)).getContent() + "\n"; else {
                    InputStream stream = (InputStream) ((MimeBodyPart) array.get(i)).getContent();
                    byte[] b = new byte[stream.available()];
                    stream.read(b);
                    ret = ret + (new String(b)) + "\n";
                }
            }
            if (ret != null) {
                return deleteMarks(ret);
            }
            return null;
        } catch (MessagingException e) {
            System.out.println(e + "problem: getHtmlText");
        } catch (IOException e) {
            System.out.println(e + "problem: getHtmlText");
        }
        return null;
    }

    /**
	 * @param html text
	 * @return text with "/" before '>' and '<'
	 */
    private String deleteMarks(String html) {
        if (html == null) {
            return "";
        }
        String ret;
        ret = html.replaceAll("<", "&lt;");
        ret = ret.replaceAll(">", "&gt;");
        ret = ret.replaceAll("\n", "<br>");
        return ret;
    }

    /**
	 * Returns an array of parts of message in HTML format. 
	 */
    private ArrayList getHtmlTextAsArray() {
        try {
            ArrayList array = new ArrayList();
            String contentType = message.getContentType();
            if (contentType.toLowerCase().startsWith("text/html") && isString(message.getContent())) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(((String) message.getContent()).trim());
                getReady(messageBodyPart);
                array.add(messageBodyPart);
                return array;
            }
            if (contentType.toLowerCase().startsWith("text/html") && isStream(message.getContent())) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                InputStream stream = (InputStream) message.getContent();
                byte[] b = new byte[stream.available()];
                stream.read(b);
                messageBodyPart.setText((new String(b)).trim());
                getReady(messageBodyPart);
                array.add(messageBodyPart);
                return array;
            }
            Object obj = message.getContent();
            if ((isMultipart(message))) {
                MimeMultipart multipart = (MimeMultipart) obj;
                return getHtmlTextAsArray(multipart);
            }
        } catch (IOException e) {
            System.out.println(e + "problem: getHtmlText");
        } catch (MessagingException e) {
            System.out.println(e + "problem: getHtmlText");
        }
        return new ArrayList();
    }

    /**
	 * Returns an array of parts of message in HTML format and belong to Multipart. 
	 */
    private static ArrayList getHtmlTextAsArray(MimeMultipart multipart) {
        try {
            ArrayList array = new ArrayList();
            String contentType = multipart.getContentType();
            for (int i = 0, n = multipart.getCount(); i < n; i++) {
                Part part = multipart.getBodyPart(i);
                String partContentType = part.getContentType();
                if ((partContentType == null) || (partContentType.toLowerCase().startsWith("text/html"))) {
                    array.add(part);
                }
                if ((isMultipart(part))) {
                    array.addAll(getHtmlTextAsArray((MimeMultipart) part.getContent()));
                }
            }
            return array;
        } catch (MessagingException e) {
            System.out.println(e + "problem: getHtmlText");
        } catch (IOException e) {
            System.out.println(e + "problem: getHtmlText");
        }
        return new ArrayList();
    }

    public static boolean equal(InternetAddress i, Address ad) {
        return i.equals(ad);
    }

    /**
	 * Sets message body.
	 * @param text
	 */
    public void setContent(String text) {
        try {
            if (!isUndone) {
                System.out.println("problem: Changing this mail is forbiden");
            } else {
                attachments = null;
                messageAsString = null;
                Properties props = new Properties();
                Session session = Session.getInstance(props, null);
                SMTPMessage newmessage = new SMTPMessage(session);
                MimeMultipart newmultipart = new MimeMultipart();
                newmessage.setContent(newmultipart);
                MimeMultipart multipart = (MimeMultipart) message.getContent();
                for (int n = multipart.getCount(), i = n - 1; i >= 0; i--) {
                    Part part = multipart.getBodyPart(i);
                    if (!(isText(part))) newmultipart.addBodyPart((BodyPart) part);
                }
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(text);
                newmultipart.addBodyPart(messageBodyPart);
                if (message.getSubject() != null) newmessage.setSubject(message.getSubject());
                if (message.getSentDate() != null) newmessage.setSentDate(message.getSentDate());
                Address[] rec = message.getRecipients(Message.RecipientType.BCC);
                if ((rec != null) && (rec.length != 0)) newmessage.setRecipients(Message.RecipientType.BCC, rec);
                rec = message.getRecipients(Message.RecipientType.CC);
                if ((rec != null) && (rec.length != 0)) newmessage.setRecipients(Message.RecipientType.CC, rec);
                rec = message.getRecipients(Message.RecipientType.TO);
                if ((rec != null) && (rec.length != 0)) newmessage.setRecipients(Message.RecipientType.TO, rec);
                rec = message.getFrom();
                if ((rec != null) && (rec.length != 0)) newmessage.setFrom(rec[0]);
                String[] pr = message.getHeader("X-Priority");
                if ((pr != null) && (pr.length != 0)) message.setHeader("X-Priority", pr[0]);
                message = newmessage;
                message.setContent(newmultipart);
                messageAsString = null;
            }
        } catch (MessagingException e) {
            System.out.println(e + "problem setContent");
        } catch (IOException e) {
            System.out.println(e + "problem setContent");
        }
    }

    /**
	 * Sets adds at the end of message body.
	 */
    public void addAdvertisment(String add) {
        try {
            if (!isUndone) {
                System.out.println("problem: Changing this mail is forbiden");
            } else {
                messageAsString = null;
                MimeMultipart multipart = (MimeMultipart) message.getContent();
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(add);
                multipart.addBodyPart(messageBodyPart);
            }
        } catch (MessagingException e) {
            System.out.println(e + "problem addAdvertisment");
        } catch (IOException e) {
            System.out.println(e + "problem addAdvertisment");
        }
    }

    /**
	 * Adds an attachment.
	 */
    public void addAttachment(byte[] content, String filename, String contentType) {
        try {
            if (!isUndone) {
                System.out.println("problem: Changing this mail is forbiden");
            } else {
                attachments = null;
                messageAsString = null;
                Properties props = new Properties();
                Session session = Session.getInstance(props, null);
                SMTPMessage newmessage = new SMTPMessage(session);
                MimeMultipart newmultipart = new MimeMultipart();
                newmessage.setContent(newmultipart);
                MimeMultipart multipart = (MimeMultipart) message.getContent();
                for (int n = multipart.getCount(), i = n - 1; i >= 0; i--) {
                    Part part = multipart.getBodyPart(i);
                    newmultipart.addBodyPart((BodyPart) part);
                }
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setFileName(filename);
                messageBodyPart.setText((new String(content)).trim());
                messageBodyPart.setHeader("Content-Type", contentType);
                newmultipart.addBodyPart(messageBodyPart);
                if (message.getSubject() != null) newmessage.setSubject(message.getSubject());
                if (message.getSentDate() != null) newmessage.setSentDate(message.getSentDate());
                Address[] rec = message.getRecipients(Message.RecipientType.BCC);
                if ((rec != null) && (rec.length != 0)) newmessage.setRecipients(Message.RecipientType.BCC, rec);
                rec = message.getRecipients(Message.RecipientType.CC);
                if ((rec != null) && (rec.length != 0)) newmessage.setRecipients(Message.RecipientType.CC, rec);
                rec = message.getRecipients(Message.RecipientType.TO);
                if ((rec != null) && (rec.length != 0)) newmessage.setRecipients(Message.RecipientType.TO, rec);
                rec = message.getFrom();
                if ((rec != null) && (rec.length != 0)) newmessage.setFrom(rec[0]);
                String[] pr = message.getHeader("X-Priority");
                if ((pr != null) && (pr.length != 0)) message.setHeader("X-Priority", pr[0]);
                message = newmessage;
                message.setContent(newmultipart);
                messageAsString = null;
            }
        } catch (MessagingException e) {
            System.out.println(e + "problem addAttachment");
        } catch (IOException e) {
            System.out.println(e + "problem addAttachment");
        }
        messageAsString = null;
    }

    /**
	 * Adds attachment as String.
	 * @param index attachments index number.
	 */
    public String getAttachmentAsString(int index) {
        try {
            if ((index < 0) || (attachments == null) || (attachments.length <= index)) {
                attachments = null;
                System.out.println("problem: No such attachment, all attachments were deleted");
                return "";
            }
            Part att = attachments[index];
            InputStream stream = att.getInputStream();
            int length = stream.available();
            byte[] b = new byte[length];
            stream.read(b);
            if ((length != 0) && ((((att.getContent()).getClass()).getName().indexOf("String")) == -1)) return new String(b);
            return (String) att.getContent();
        } catch (IOException e) {
            System.out.println(e + "problem: getAttachmentAsString");
        } catch (MessagingException e) {
            System.out.println(e + "problem: getAttachmentAsString");
        }
        return "";
    }

    /**
	 * Returns an attachment.
	 * @param index which attachment
	 */
    public Attachment getAttachment(int index) {
        Attachment ret = new Attachment();
        try {
            ret.contentType = "";
            ret.content = new byte[0];
            ret.filename = "";
            id = index;
            if ((index < 0) || (attachments == null) || (attachments.length <= index)) {
                attachments = null;
                System.out.println("problem: No such attachment, all attachments were deleted");
                return ret;
            }
            Part part = attachments[index];
            ret.filename = part.getFileName();
            ret.contentType = part.getContentType();
            InputStream stream = part.getInputStream();
            int length = stream.available();
            byte[] b = new byte[length];
            stream.read(b);
            if ((length != 0) && ((((part.getContent()).getClass()).getName().indexOf("String")) == -1)) ret.content = b; else ret.content = ((String) part.getContent()).getBytes();
            ret.length = ret.content.length;
            return ret;
        } catch (IOException e) {
            System.out.println(e + "problem: removeAllAttachments");
        } catch (MessagingException e) {
            System.out.println(e + "problem: removeAllAttachments");
        }
        return ret;
    }

    /**
	 * Removes an attachment.
	 * @param index which attachment
	 */
    public void removeAttachment(int index) {
        try {
            if (!isUndone) {
                System.out.println("problem: Changing this mail is forbiden");
                return;
            }
            if ((index < 0) || (attachments == null) || (attachments.length <= index)) {
                attachments = null;
                messageAsString = null;
                System.out.println("problem: No such attachment, all attachments were deleted");
                return;
            }
            Properties props = new Properties();
            Session session = Session.getInstance(props, null);
            SMTPMessage newmessage = new SMTPMessage(session);
            MimeMultipart newmultipart = new MimeMultipart();
            newmessage.setContent(newmultipart);
            MimeMultipart multipart = (MimeMultipart) message.getContent();
            for (int n = multipart.getCount(), i = n - 1; i >= 0; i--) {
                Part part = multipart.getBodyPart(i);
                if ((((BodyPart) part).getFileName() == null) || (((BodyPart) attachments[index]).getFileName().hashCode() != ((BodyPart) part).getFileName().hashCode())) newmultipart.addBodyPart((BodyPart) part);
            }
            if (message.getSubject() != null) newmessage.setSubject(message.getSubject());
            if (message.getSentDate() != null) newmessage.setSentDate(message.getSentDate());
            Address[] rec = message.getRecipients(Message.RecipientType.BCC);
            if ((rec != null) && (rec.length != 0)) newmessage.setRecipients(Message.RecipientType.BCC, rec);
            rec = message.getRecipients(Message.RecipientType.CC);
            if ((rec != null) && (rec.length != 0)) newmessage.setRecipients(Message.RecipientType.CC, rec);
            rec = message.getRecipients(Message.RecipientType.TO);
            if ((rec != null) && (rec.length != 0)) newmessage.setRecipients(Message.RecipientType.TO, rec);
            rec = message.getFrom();
            if ((rec != null) && (rec.length != 0)) newmessage.setFrom(rec[0]);
            String[] pr = message.getHeader("X-Priority");
            if ((pr != null) && (pr.length != 0)) message.setHeader("X-Priority", pr[0]);
            message = newmessage;
            message.setContent(newmultipart);
            attachments = null;
            messageAsString = null;
        } catch (MessagingException e) {
            System.out.println(e + "problem: removeAllAttachments");
        } catch (IOException e) {
            System.out.println(e + "problem: removeAllAttachments");
        }
    }

    /** Returns a heading for SMTP server.
	 * @param name mail recepient
	 */
    private String getString(String name) {
        try {
            ByteArrayOutputStream e = new ByteArrayOutputStream();
            message.writeTo(e);
            InternetAddress[] from = (InternetAddress[]) message.getFrom();
            if ((from == null) || (from.length == 0)) {
                System.out.println("Nie ma od kogo");
                return null;
            }
            byte[] b = new byte[1];
            b[0] = 13;
            String cr = new String(b);
            String ret = "MAIL FROM: " + from[0].getAddress() + cr + "\n";
            ret = ret + "RCPT TO: " + name + cr + "\n";
            ret = ret + "DATA" + cr + "\n";
            ret = ret + e.toString();
            ret = ret + cr + "\n";
            ret = ret + "." + cr + "\n";
            return ret;
        } catch (IOException e) {
            System.out.println(e + "problem: toString");
        } catch (MessagingException e) {
            System.out.println(e + "problem: toString");
        }
        return null;
    }

    /**Returns a collection of Strings for SMTP server.
	 */
    public ArrayList getStrings() {
        try {
            ArrayList array = new ArrayList();
            InternetAddress[] adrbcc = (InternetAddress[]) message.getRecipients(Message.RecipientType.BCC);
            InternetAddress[] adrcc = (InternetAddress[]) message.getRecipients(Message.RecipientType.CC);
            InternetAddress[] adrto = (InternetAddress[]) message.getRecipients(Message.RecipientType.TO);
            Address[] addr = null;
            message.setRecipients(Message.RecipientType.TO, addr);
            Date date = message.getSentDate();
            if (date == null) {
                Calendar calendar = new GregorianCalendar();
                date = calendar.getTime();
                message.setSentDate(date);
            }
            int i;
            for (i = 0; i < adrto.length; ++i) {
                array.add(getString(adrto[i].getAddress()));
            }
            for (i = 0; i < adrcc.length; ++i) {
                array.add(getString(adrcc[i].getAddress()));
            }
            for (i = 0; i < adrbcc.length; ++i) {
                array.add(getString(adrbcc[i].getAddress()));
            }
            return array;
        } catch (MessagingException e) {
            System.out.println(e + "problem: toStrings");
        }
        return null;
    }

    /**
	 * Returns a message as string to be stored in database.
	 */
    public String toStringInDatabase() {
        try {
            if (messageAsString == null) {
                ByteArrayOutputStream e = new ByteArrayOutputStream();
                message.writeTo(e);
                messageAsString = e.toString();
            }
            return messageAsString;
        } catch (MessagingException e) {
            System.out.println(e + "problem: toStringInDatabase");
        } catch (IOException e) {
            System.out.println(e + "problem: toStringInDatabase");
        }
        return null;
    }

    /**
	 * Constructor. Generates Mail object from message returned by mail server.
	 * @param mail - message from e-mail server received as String
	 * @param isUndone - is mail a working copy
	 * 
	 */
    public Mail(String mail, boolean isUndon) throws UnknownMailFormat {
        boolean error = false, isMime = false;
        messageAsString = mail;
        isUndone = true;
        InputStream is = new StringBufferInputStream(mail);
        Properties props = new Properties();
        Session session = Session.getInstance(props, null);
        try {
            message = new SMTPMessage(session, is);
            isMime = isMimeType();
        } catch (MessagingException e) {
            error = true;
            System.out.println(e + "problem: Mail: UnknownMailFormat, jest messaging exception");
        } catch (Exception e) {
            error = true;
            System.out.println(e + "problem: Mail: UnknownMailFormat, jest exception");
        }
        isUndone = isUndon;
        if ((!(isMime)) || (error)) {
            if (!(isMime)) {
                System.out.println("isMime jest false");
            } else {
                System.out.println("Nastopil error");
            }
            System.out.println("zrodlo");
            System.out.println(mail);
            throw new UnknownMailFormat();
        }
    }

    /**Contructor. Creates an empty mail.
	 * 
	 */
    public Mail() {
        try {
            isUndone = true;
            Properties props = new Properties();
            Session session = Session.getInstance(props, null);
            message = new SMTPMessage(session);
            MimeMultipart multipart = new MimeMultipart();
            message.setContent(multipart);
            setContent("  ");
            toStringInDatabase();
        } catch (MessagingException e) {
            System.out.println(e + "problem: Mail");
        }
    }

    /**
	 * Changes a list of e-mail addresses to String.
	 * @param adr Array of e-mail addresses
	 * @return String with addresses seperated by commas.
	 */
    public static String write(Address[] adr) {
        if ((adr == null) || (adr.length == 0)) return "";
        return InternetAddress.toString((InternetAddress[]) adr);
    }

    /**
	 * Generates a list of e-mail addresses from String.
	 * @param addr a list of adrresses seperated by commas
	 * @return array of e-mail addresses
	 * @throws AddressException
	 */
    public BAddressImpl[] addressessFromString(String addr) throws AddressException {
        InternetAddress[] inad = InternetAddress.parse(addr);
        BAddressImpl[] ad = new BAddressImpl[inad.length];
        for (int i = 0; i < inad.length; ++i) {
            ad[i] = new BAddressImpl(inad[i]);
        }
        return ad;
    }

    /** Method to change all message attributes at once.
	 * @param from sender
	 * @param to recepient
	 * @param cc recepient of a copy
	 * @param bcc hidden copy
	 * @param subject mail subject
	 * @param content mail content
	 * @throws BrokenField
	 */
    public void setAll(String from, String to, String cc, String bcc, String subject, String content) throws BrokenField {
        try {
            BAddressImpl[] addr;
            setSubject(subject);
            setContent(content);
            try {
                addr = addressessFromString(to);
                if ((addr != null) && (addr.length != 0)) setToRecipients(addr);
            } catch (AddressException e) {
                throw new BrokenField("Bad ToAddress");
            }
            try {
                addr = addressessFromString(bcc);
                if ((addr != null) && (addr.length != 0)) setBccRecipients(addr);
            } catch (AddressException e) {
                throw new BrokenField("Bad BccAddress");
            }
            try {
                addr = addressessFromString(cc);
                if ((addr != null) && (addr.length != 0)) setCcRecipients(addr);
            } catch (AddressException e) {
                throw new BrokenField("Bad CcAddress");
            }
            try {
                addr = addressessFromString(from);
                if ((addr != null) && (addr.length != 0)) setFrom(addr[0]);
            } catch (AddressException e) {
                throw new BrokenField("Bad FromAddress");
            }
            Date date = message.getSentDate();
            if (date == null) {
                Calendar calendar = new GregorianCalendar();
                date = calendar.getTime();
                message.setSentDate(date);
            }
        } catch (MessagingException e) {
            System.out.println(e + "problem: getAll");
        }
    }

    /**
	 * Returns Send date.
	 */
    public Date getSentDate() {
        try {
            return message.getSentDate();
        } catch (MessagingException e) {
            System.out.println(e + "problem: getSentDate");
        }
        return null;
    }

    /**
	 * Sets Send date.
	 * @param d date of messeage sending.
	 */
    public void setSentDate(Date d) {
        try {
            if (!isUndone) {
                System.out.println("problem: Changing this mail is forbiden");
            } else {
                messageAsString = null;
                message.setSentDate(d);
            }
        } catch (MessagingException e) {
            System.out.println(e + "problem: setSentDate");
        }
    }

    /**
	 * returns message subject.
	 */
    public String getSubject() {
        try {
            return message.getSubject();
        } catch (MessagingException e) {
            System.out.println(e + "problem: getSubject");
        }
        return null;
    }

    /**
	 * returns message's subject.
	 */
    public String getSubjectSafe() {
        try {
            return deleteMarks(message.getSubject());
        } catch (MessagingException e) {
            System.out.println(e + "problem: getSubject");
        }
        return null;
    }

    /**
	 * Sets message subject.
	 */
    public void setSubject(String sub) {
        try {
            if (!isUndone) {
                System.out.println("problem: Changing this mail is forbiden");
            } else {
                messageAsString = null;
                message.setSubject(sub);
            }
        } catch (MessagingException e) {
            System.out.println(e + "problem: setSubcest");
        }
    }

    /**
	 * Returns To attribute.
	 */
    public String getToRecipients() {
        try {
            return write(message.getRecipients(Message.RecipientType.TO));
        } catch (MessagingException e) {
            System.out.println(e + "problem: gettoRecipents");
        }
        return null;
    }

    /**
	 * Returns To attribute.
	 */
    public String getToRecipientsSafe() {
        try {
            return deleteMarks(write(message.getRecipients(Message.RecipientType.TO)));
        } catch (MessagingException e) {
            System.out.println(e + "problem: gettoRecipents");
        }
        return null;
    }

    /**
	 * Sets To attribute.
	 */
    public void setToRecipients(BAddressImpl[] ad) {
        try {
            if (!isUndone) {
                System.out.println("problem: Changing this mail is forbiden");
            } else {
                messageAsString = null;
                message.setRecipients(Message.RecipientType.TO, (InternetAddress[]) ad);
            }
        } catch (MessagingException e) {
            System.out.println(e + "problem: setToRecipients");
        }
    }

    /**
	 * Sets To attribute.
	 */
    public void setToRecipients(String to) throws AddressException {
        if (!isUndone) {
            System.out.println("problem: Changing this mail is forbiden");
        } else {
            BAddressImpl[] addr = addressessFromString(to);
            if ((addr != null) && (addr.length != 0)) setToRecipients(addr);
        }
    }

    /**
	 * Inserts CC atribute.
	 */
    public String getCcRecipients() {
        try {
            return write(message.getRecipients(Message.RecipientType.CC));
        } catch (MessagingException e) {
            System.out.println(e + "problem: getCcRecipients");
        }
        return null;
    }

    /**
	 * Returns CC attribute.
	 */
    public String getCcRecipientsSafe() {
        try {
            return deleteMarks(write(message.getRecipients(Message.RecipientType.CC)));
        } catch (MessagingException e) {
            System.out.println(e + "problem: getCcRecipients");
        }
        return null;
    }

    /**
	 * Inserts To attribute.
	 */
    public void setCcRecipients(BAddressImpl[] ad) {
        try {
            if (!isUndone) {
                System.out.println("problem: Changing this mail is forbiden");
            } else {
                messageAsString = null;
                message.setRecipients(Message.RecipientType.CC, (InternetAddress[]) ad);
            }
        } catch (MessagingException e) {
            System.out.println(e + "problem: getCcRecipients");
        }
    }

    /**
	 * Returns HCC attribute.
	 */
    public String getBccRecipients() {
        try {
            return write(message.getRecipients(Message.RecipientType.BCC));
        } catch (MessagingException e) {
            System.out.println(e + "problem: getBccRecipients");
        }
        return null;
    }

    /**
	 * Inserts HCC attribute.
	 */
    public void setBccRecipients(BAddressImpl[] ad) {
        try {
            if (!isUndone) {
                System.out.println("problem: Changing this mail is forbiden");
            } else {
                messageAsString = null;
                message.setRecipients(Message.RecipientType.BCC, (InternetAddress[]) ad);
            }
        } catch (MessagingException e) {
            System.out.println(e + "problem: getBccRecipients");
        }
    }

    /**
	 * Returns From attribute.
	 */
    public String getFrom() {
        try {
            return write(message.getFrom());
        } catch (MessagingException e) {
            System.out.println(e + "problem: getFrom");
        }
        return null;
    }

    /**
	 * Returns From attribute.
	 */
    public String getFromSafe() {
        try {
            return deleteMarks(write(message.getFrom()));
        } catch (MessagingException e) {
            System.out.println(e + "problem: getFrom");
        }
        return null;
    }

    /**
	 * Inserts From attribute. 
	 */
    public void setFrom(BAddressImpl ad) {
        try {
            if (!isUndone) {
                System.out.println("problem: Changing this mail is forbiden");
            } else {
                messageAsString = null;
                message.setFrom((InternetAddress) ad);
            }
        } catch (MessagingException e) {
            System.out.println(e + "problem: getFrom");
        }
    }

    /**
	 * Returns message priority. If null then priority is set to 3.
	 */
    public String getPriority() {
        try {
            String s = message.getHeader("X-Priority", ",");
            if (s == null) return "3";
            return s;
        } catch (MessagingException e) {
            System.out.println(e + "problem: getPriority");
        }
        return null;
    }

    /**
	 * Retrieves message priority. If message has no priority, priority is set to 3.
	 */
    public String getPrioritySafe() {
        try {
            String s = deleteMarks(message.getHeader("X-Priority", ","));
            if ((s == null) || (s == "")) return "3";
            return s;
        } catch (MessagingException e) {
            System.out.println(e + "problem: getPriority");
        }
        return null;
    }

    /**
	 * Sets message priority
	 */
    public void setPriority(String pr) {
        try {
            if (!isUndone) {
                System.out.println("problem: Changing this mail is forbiden");
            } else {
                messageAsString = null;
                message.setHeader("X-Priority", pr);
            }
        } catch (MessagingException e) {
            System.out.println(e + "problem: setPriority");
        }
    }

    /**creates SimpleMail object from Mail object
	 * 
	 */
    public SimpleMail toSimpleMail() {
        SimpleMail sim = new SimpleMail();
        sim.id = id;
        sim.folderId = folderId;
        sim.isSent = isSent;
        sim.isReceived = isReceived;
        sim.isUnDone = isUndone;
        sim.isNew = isNew;
        sim.from = getFrom();
        sim.sentDate = getSentDate();
        sim.subject = getSubject();
        sim.priority = getPriority();
        return sim;
    }
}
