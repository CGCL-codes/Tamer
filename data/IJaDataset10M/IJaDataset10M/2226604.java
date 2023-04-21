package org.tagbox.soap.jaxm;

import java.io.File;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.tagbox.soap.Message;
import org.tagbox.soap.MessagePart;
import org.tagbox.soap.MessageFactory;
import org.tagbox.soap.SoapException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import org.tagbox.util.Log;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;

public class JaxmMessage implements Message {

    private Map attachments;

    private SOAPMessage message;

    private MessageFactory responseFactory;

    public JaxmMessage(SOAPMessage message) throws SoapException {
        this(new JaxmMessageFactory(), message);
    }

    public JaxmMessage(MessageFactory responseFactory, SOAPMessage message) throws SoapException {
        this.responseFactory = responseFactory;
        this.message = message;
        attachments = new HashMap();
        Iterator it = message.getAttachments();
        while (it.hasNext()) {
            AttachmentPart part = (AttachmentPart) it.next();
            MessagePart attachment = new JaxmAttachmentPart(part);
            String location = attachment.getContentId();
            attachments.put(location, attachment);
        }
    }

    public Collection getAttachmentLocations() {
        return attachments.keySet();
    }

    /**
     * @param location should reference the message part Content-Id.
     * If the Content-Id is not already set, the method will
     * set a content-id to 'location' enclosed in angle brackets.
     * see: http://www.w3.org/TR/SOAP-attachments
     */
    public void setAttachment(String location, MessagePart attachment) throws SoapException {
        JaxmAttachmentPart part = (JaxmAttachmentPart) attachment;
        AttachmentPart attachmentPart = part.getAttachmentPart();
        if (location.startsWith("<") && location.endsWith(">")) location = location.substring(1, location.length() - 1);
        if (attachments.containsKey(location)) {
            try {
                String contentType = attachmentPart.getContentType();
                Object content = attachmentPart.getContent();
                part = (JaxmAttachmentPart) attachments.get(location);
                attachmentPart = part.getAttachmentPart();
                attachmentPart.setContent(content, contentType);
                attachment = part;
            } catch (SOAPException exc) {
                throw new SoapException(exc);
            }
        } else {
            message.addAttachmentPart(attachmentPart);
        }
        attachments.put(location, attachment);
        if (attachmentPart.getContentId() == null || attachmentPart.getContentId().equals("")) {
            if (!(location.startsWith("<") && location.endsWith(">"))) location = '<' + location + '>';
            attachmentPart.setContentId(location);
        }
    }

    /**
     * If 'location' is enclosed in angle brackets, they will then be removed.
     */
    public MessagePart getAttachment(String location) {
        if (location.startsWith("<") && location.endsWith(">")) location = location.substring(1, location.length() - 1);
        return (MessagePart) attachments.get(location);
    }

    public void setHeader(String name, String value) {
        message.getMimeHeaders().setHeader(name, value);
    }

    public String getHeader(String name) {
        String[] values = message.getMimeHeaders().getHeader(name);
        if (values == null || values.length == 0) return null;
        return values[0];
    }

    public Collection getHeaderNames() {
        List names = new ArrayList();
        Iterator it = message.getMimeHeaders().getAllHeaders();
        while (it.hasNext()) {
            MimeHeader mime = (MimeHeader) it.next();
            names.add(mime.getName());
        }
        return names;
    }

    public void setMessageBody(Node body) throws SoapException {
        SOAPPart sp = message.getSOAPPart();
        sp.setContentId("<SOAPPart>");
        try {
            sp.setContent(new DOMSource(body));
        } catch (SOAPException exc) {
            throw new SoapException(exc);
        }
    }

    public void setMessageBody(MessagePart bodypart) throws SoapException {
        Element envelope = (Element) bodypart.getContentAsXML().cloneNode(true);
        SOAPPart sp = message.getSOAPPart();
        sp.setContentId("<SOAPPart>");
        try {
            sp.setContent(new DOMSource(envelope));
        } catch (Exception exc) {
            throw new SoapException(exc);
        }
    }

    public MessagePart getMessageBody() {
        return new JaxmMessagePart(message.getSOAPPart());
    }

    public Message createResponse() throws SoapException {
        return responseFactory.createMessage();
    }

    public MessagePart createAttachment(Object content, String contentType) throws SoapException {
        DataHandler handler = new DataHandler(content, contentType);
        AttachmentPart attachment = message.createAttachmentPart(handler);
        return new JaxmAttachmentPart(attachment);
    }

    public MessagePart createAttachment(File file) throws SoapException {
        DataHandler handler = new DataHandler(new FileDataSource(file));
        AttachmentPart attachment = message.createAttachmentPart(handler);
        String disposition = "attachment; filename=" + file.getName() + "; size=" + file.length();
        attachment.setMimeHeader(MessagePart.MIME_CONTENT_DISPOSITION, disposition);
        return new JaxmAttachmentPart(attachment);
    }

    public SOAPMessage getSOAPMessage() {
        return message;
    }

    public void write(OutputStream out) throws SoapException {
        try {
            message.writeTo(out);
        } catch (SOAPException exc) {
            throw new SoapException(exc);
        } catch (IOException exc) {
            throw new SoapException(exc);
        }
    }
}
