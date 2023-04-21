package net.suberic.pooka;

import java.util.*;
import java.io.IOException;
import javax.mail.internet.*;
import javax.mail.*;

/**
 * This class is here for my convenience so that I can have a single
 * set of attachment parsers that will return any set of information
 * that I want.
 */
class AttachmentBundle {

    private Attachment textPart = null;

    private Vector allAttachments = new Vector();

    Vector attachmentsAndTextPart = null;

    InternetHeaders headers = null;

    Vector headerLines = null;

    /**
   * Creates an AttachmentBundle.
   */
    AttachmentBundle() {
    }

    /**
   * Creates an AttachmentBundle from the given MimePart.
   */
    AttachmentBundle(MimePart m) throws MessagingException {
        setHeaderSource(m);
    }

    /**
   * Adds all of the entries in the <code>subBundle</code> to this
   * AttachmentBundle.  Also will update the <code>textPart</code>,
   * if this Bundle doesn't have one yet.
   */
    synchronized void addAll(AttachmentBundle subBundle) {
        if (subBundle.textPart != null) subBundle.textPart.setHeaderSource(subBundle);
        if (textPart == null) {
            textPart = subBundle.textPart;
        } else if (textPart instanceof net.suberic.pooka.crypto.CryptoAttachment) {
            if (subBundle.textPart != null) {
                textPart = subBundle.textPart;
            }
        } else if (subBundle.textPart != null) {
            allAttachments.add(subBundle.textPart);
        }
        allAttachments.addAll(subBundle.allAttachments);
    }

    /**
   * Adds an Attachment to the AttachmentBundle.  Automagically checks the
   * type of the Attachment and sets it as the text part, if appropriate.
   */
    synchronized void addAttachment(Attachment newAttach) {
        addAttachment(newAttach, newAttach.getMimeType(), true);
    }

    /**
   * Adds an Attachment to the AttachmentBundle.  
   *
   * If allowTextPart is false,  then forces the Attachment to 
   * be an attachment, rather than allowing it to be set as 
   * the content.
   */
    synchronized void addAttachment(Attachment newAttach, boolean allowTextPart) {
        addAttachment(newAttach, newAttach.getMimeType(), allowTextPart);
    }

    /**
   * Removes the given Attachment.
   */
    synchronized void removeAttachment(Attachment delAttach) {
        if (textPart == delAttach) {
            textPart = null;
            Iterator it = allAttachments.iterator();
            while (textPart == null && it.hasNext()) {
                Attachment at = (Attachment) it.next();
                if (at instanceof AlternativeAttachment || at.getMimeType().match("text/")) {
                    textPart = at;
                }
            }
        } else {
            allAttachments.remove(delAttach);
        }
    }

    /**
   * Adds an Attachment using the given ContentType.
   */
    synchronized void addAttachment(Attachment newAttach, ContentType ct) {
        addAttachment(newAttach, ct, true);
    }

    /**
   * Adds an Attachment using the given ContentType.
   * 
   * If allowTextPart is false,  then forces the Attachment to 
   * be an attachment, rather than allowing it to be set as 
   * the content.
   */
    synchronized void addAttachment(Attachment newAttach, ContentType ct, boolean allowTextPart) {
        if (!allowTextPart) {
            allAttachments.add(newAttach);
        } else {
            if ((textPart == null || textPart instanceof net.suberic.pooka.crypto.CryptoAttachment) && (newAttach instanceof AlternativeAttachment || ct.match("text/*"))) {
                textPart = newAttach;
            } else if (textPart == null && newAttach instanceof net.suberic.pooka.crypto.CryptoAttachment) {
                textPart = newAttach;
                allAttachments.add(newAttach);
            } else {
                allAttachments.add(newAttach);
            }
        }
    }

    /**
   * This gets the Text part of a message.  This is useful if you want
   * to display just the 'body' of the message without the attachments.
   */
    public String getTextPart(boolean withHeaders, boolean showFullHeaders, int maxLength, String truncationMessage) throws IOException {
        StringBuffer retVal = new StringBuffer();
        if (withHeaders) retVal.append(getHeaderInformation(showFullHeaders, false));
        String text = null;
        if (textPart != null) {
            text = textPart.getText(withHeaders, showFullHeaders, maxLength, truncationMessage);
        }
        if (text != null) {
            retVal.append(text);
            return retVal.toString();
        } else return null;
    }

    /**
   * This gets the Html part of a message.  This is useful if you want
   * to display just the 'body' of the message without the attachments.
   */
    public String getHtmlPart(boolean withHeaders, boolean showFullHeaders, int maxLength, String truncationMessage) throws IOException {
        StringBuffer retVal = new StringBuffer();
        retVal.append("<html><body>");
        if (withHeaders) retVal.append(getHeaderInformation(showFullHeaders, true));
        if (textPart != null) retVal.append(textPart.getHtml(withHeaders, showFullHeaders, maxLength, truncationMessage));
        retVal.append("</body></html>");
        return retVal.toString();
    }

    /**
   * This returns the Attachments (basically, all the Parts in a Multipart
   * except for the main body of the message).
   */
    public Vector getAttachments() {
        return new Vector(allAttachments);
    }

    /**
   * This returns the Attachments (basically, all the Parts in a Multipart
   * except for the main body of the message) using the given 
   * messageLength to determine whether or not the main text part is an
   * attachment or not.
   */
    public Vector getAttachments(int maxLength) {
        if (textPart != null && textPart.getSize() >= maxLength) {
            if (attachmentsAndTextPart != null) return attachmentsAndTextPart; else {
                attachmentsAndTextPart = new Vector();
                attachmentsAndTextPart.add(textPart);
                attachmentsAndTextPart.addAll(allAttachments);
                return new Vector(attachmentsAndTextPart);
            }
        } else return new Vector(allAttachments);
    }

    /**
   * Returns all attachments, including the text part.
   */
    public Vector getAttachmentsAndTextPart() {
        if (attachmentsAndTextPart != null) return new Vector(attachmentsAndTextPart); else {
            attachmentsAndTextPart = new Vector();
            attachmentsAndTextPart.add(textPart);
            attachmentsAndTextPart.addAll(allAttachments);
            return new Vector(attachmentsAndTextPart);
        }
    }

    /**
   * This method returns the Message Text plus the text inline attachments.
   * The attachments are separated by the separator flag.
   */
    public String getTextAndTextInlines(String separator, boolean withHeaders, boolean showFullHeaders, int maxLength, String truncationMessage) throws IOException {
        StringBuffer returnValue = new StringBuffer();
        if (withHeaders) returnValue.append(getHeaderInformation(showFullHeaders, false));
        if (textPart != null) returnValue.append(textPart.getText(withHeaders, showFullHeaders, maxLength, truncationMessage));
        if (allAttachments != null && allAttachments.size() > 0) {
            for (int i = 0; i < allAttachments.size(); i++) {
                Attachment attach = (Attachment) allAttachments.elementAt(i);
                if (attach.isPlainText()) {
                    returnValue.append(separator);
                    returnValue.append(attach.getText(withHeaders, showFullHeaders, maxLength, truncationMessage));
                }
            }
        }
        return returnValue.toString();
    }

    /**
   * This method returns the Message HTML plus the text inline attachments.
   * The attachments are separated by the separator flag.
   */
    public String getHtmlAndTextInlines(String separator, boolean withHeaders, boolean showFullHeaders, int maxLength, String truncationMessage) throws IOException {
        StringBuffer returnValue = new StringBuffer();
        returnValue.append("<html><body>");
        if (withHeaders) returnValue.append(getHeaderInformation(showFullHeaders, true));
        if (textPart != null) returnValue.append(textPart.getHtml(withHeaders, showFullHeaders, maxLength, truncationMessage));
        if (allAttachments != null && allAttachments.size() > 0) {
            for (int i = 0; i < allAttachments.size(); i++) {
                Attachment attach = (Attachment) allAttachments.elementAt(i);
                if (attach.isPlainText()) {
                    returnValue.append(separator);
                    returnValue.append(attach.getText(withHeaders, showFullHeaders, maxLength, truncationMessage));
                }
            }
        }
        returnValue.append("</body></html>");
        return returnValue.toString();
    }

    /**
   * Sets the header source for this AttachmentBundle.  This is used when
   * we print out headers for the Message.
   */
    public void setHeaderSource(MimePart headerSource) throws MessagingException {
        headers = parseHeaders(headerSource.getAllHeaders());
        headerLines = parseHeaderLines(headerSource.getAllHeaderLines());
    }

    /**
   * This returns the formatted header information for a message.
   */
    public StringBuffer getHeaderInformation(boolean showFullHeaders, boolean useHtml) {
        if (headers != null) {
            StringBuffer headerText = new StringBuffer();
            if (showFullHeaders) {
                if (useHtml) {
                    Enumeration allHdrs = headers.getAllHeaders();
                    while (allHdrs.hasMoreElements()) {
                        Header nextHeader = (Header) allHdrs.nextElement();
                        headerText.append("<b>" + nextHeader.getName() + ":</b>&nbsp;&nbsp;");
                        headerText.append(MailUtilities.escapeHtml(nextHeader.getValue()));
                        headerText.append("<br>\n");
                    }
                } else {
                    Enumeration allHdrs = headers.getAllHeaderLines();
                    while (allHdrs.hasMoreElements()) {
                        headerText.append(MailUtilities.decodeText((String) allHdrs.nextElement()));
                        headerText.append('\n');
                    }
                }
            } else {
                StringTokenizer tokens = new StringTokenizer(Pooka.getProperty("MessageWindow.Header.DefaultHeaders", "From:To:CC:Date:Subject"), ":");
                String hdrLabel, currentHeader = null;
                String hdrValue = null;
                while (tokens.hasMoreTokens()) {
                    currentHeader = tokens.nextToken();
                    hdrLabel = Pooka.getProperty("MessageWindow.Header." + currentHeader + ".label", currentHeader);
                    hdrValue = MailUtilities.decodeText((String) headers.getHeader(Pooka.getProperty("MessageWindow.Header." + currentHeader + ".MIMEHeader", currentHeader), ":"));
                    if (hdrValue != null) {
                        if (useHtml) {
                            headerText.append("<b>" + hdrLabel + ":</b>&nbsp;&nbsp;");
                            headerText.append(MailUtilities.escapeHtml(hdrValue));
                            headerText.append("<br>\n");
                        } else {
                            headerText.append(hdrLabel + ":  ");
                            headerText.append(hdrValue);
                            headerText.append("\n");
                        }
                    }
                }
            }
            if (useHtml) {
                String separator = Pooka.getProperty("MessageWindow.htmlSeparator", "<hr><br>");
                headerText.append(separator);
            } else {
                String separator = Pooka.getProperty("MessageWindow.separator", "");
                if (separator.equals("")) headerText.append("\n\n"); else headerText.append(separator);
            }
            return headerText;
        } else {
            return new StringBuffer();
        }
    }

    /**
   * Parses the Enumeration of Header objects into a HashMap.
   */
    private InternetHeaders parseHeaders(Enumeration pHeaders) {
        InternetHeaders retVal = new InternetHeaders();
        while (pHeaders.hasMoreElements()) {
            Header hdr = (Header) pHeaders.nextElement();
            retVal.addHeader(hdr.getName(), hdr.getValue());
        }
        return retVal;
    }

    /**
   * Parses the Enumeration of header lines into a Vector.
   */
    private Vector parseHeaderLines(Enumeration pHeaderLines) {
        Vector retVal = new Vector();
        while (pHeaderLines.hasMoreElements()) retVal.add(pHeaderLines.nextElement());
        return retVal;
    }

    /**
   * Returns whether or not this attachment has an HTML version available.
   */
    public boolean containsHtml() {
        if (textPart != null) {
            if (textPart instanceof AlternativeAttachment) {
                return true;
            } else {
                return textPart.getMimeType().match("text/html");
            }
        } else return false;
    }

    /**
   * Returns true if the main content of this message exists only as
   * HTML.
   */
    public boolean isHtml() {
        if (textPart != null) {
            if (textPart instanceof AlternativeAttachment) return false; else return (textPart.getMimeType().match("text/html"));
        } else return false;
    }
}
