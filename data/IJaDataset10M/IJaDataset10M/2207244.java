package de.pannenleiter.util;

import java.io.*;
import java.util.*;
import java.net.Socket;
import java.net.InetAddress;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * Mailer -- This ContentHandler sends an email
 *
 *
 */
public class Mailer implements ContentHandler, DocumentHandler {

    protected Stack elements;

    protected HeaderElement current;

    protected Vector header;

    protected Exception error;

    public void setDocumentLocator(Locator locator) {
    }

    public void startDocument() throws SAXException {
        elements = new Stack();
        current = null;
        header = new Vector();
        error = null;
    }

    public void endDocument() throws SAXException {
        try {
            MailerThread cl = new MailerThread(header, this);
            cl.start();
            cl.join(20000);
            if (cl.isAlive()) {
                cl.stop(new Exception("Timeout on sending mail"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SAXException("Can't send mail", ex);
        }
        if (error != null) {
            error.printStackTrace();
            throw new SAXException("Can't send mail", error);
        }
    }

    public void startPrefixMapping(java.lang.String prefix, java.lang.String uri) throws SAXException {
    }

    public void endPrefixMapping(java.lang.String prefix) throws SAXException {
    }

    public void startElement(java.lang.String namespaceURI, java.lang.String localName, java.lang.String rawName, Attributes atts) throws SAXException {
        elements.push(current);
        current = new HeaderElement(rawName, atts);
    }

    public void startElement(java.lang.String rawName, AttributeList atts) throws SAXException {
        elements.push(current);
        current = new HeaderElement(rawName, atts);
    }

    public void endElement(java.lang.String namespaceURI, java.lang.String localName, java.lang.String rawName) throws SAXException {
        header.addElement(current);
        current = (HeaderElement) elements.pop();
    }

    public void endElement(java.lang.String rawName) throws SAXException {
        endElement("", "", rawName);
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        String s = new String(ch, start, length);
        if (current == null) {
        } else {
            current.content = current.content + s;
        }
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    public void processingInstruction(java.lang.String target, java.lang.String data) throws SAXException {
    }

    public void skippedEntity(java.lang.String name) throws SAXException {
    }
}

class HeaderElement {

    String name;

    String lower;

    Vector attributes;

    String content;

    HeaderElement(String name, Attributes atts) {
        this.name = name;
        lower = name.toLowerCase();
        if (atts.getLength() > 0) {
            attributes = new Vector();
            for (int i = 0; i < atts.getLength(); i++) {
                attributes.addElement(atts.getQName(i));
                attributes.addElement(atts.getValue(i));
            }
        }
        content = "";
    }

    HeaderElement(String name, AttributeList atts) {
        this.name = name;
        lower = name.toLowerCase();
        if (atts.getLength() > 0) {
            attributes = new Vector();
            for (int i = 0; i < atts.getLength(); i++) {
                attributes.addElement(atts.getName(i));
                attributes.addElement(atts.getValue(i));
            }
        }
        content = "";
    }
}

class MailerThread extends Thread {

    protected Mailer errorHandler;

    protected Vector header;

    public void run() {
        Socket s = null;
        try {
            String body = "";
            String host = "localhost";
            int port = 25;
            for (int i = 0; i < header.size(); i++) {
                String h = ((HeaderElement) header.elementAt(i)).lower;
                if ("mail".equals(h)) {
                    body = ((HeaderElement) header.elementAt(i)).content;
                    Vector atts = ((HeaderElement) header.elementAt(i)).attributes;
                    for (int j = 0; j < atts.size(); j += 2) {
                        String name = (String) atts.elementAt(j);
                        String value = (String) atts.elementAt(j + 1);
                        if ("host".equals(name)) {
                            host = value;
                        }
                        if ("port".equals(name)) {
                            port = Integer.parseInt(value);
                        }
                    }
                }
            }
            s = new Socket(host, port);
            PrintWriter mailer = new PrintWriter(s.getOutputStream());
            BufferedReader status = new BufferedReader(new InputStreamReader(s.getInputStream()));
            check("init", "220", status.readLine());
            InetAddress me = InetAddress.getLocalHost();
            mailer.print("HELO " + me.getHostName() + "\r\n");
            mailer.flush();
            check("HELO", "250", status.readLine());
            boolean ok = false;
            for (int i = 0; i < header.size(); i++) {
                String h = ((HeaderElement) header.elementAt(i)).lower;
                if ("from".equals(h)) {
                    mailer.print("MAIL FROM: " + ((HeaderElement) header.elementAt(i)).content + "\r\n");
                    mailer.flush();
                    check("MAIL FROM", "250", status.readLine());
                    ok = true;
                }
            }
            if (!ok) throw new Exception("SendMail: From element missing");
            ok = false;
            for (int i = 0; i < header.size(); i++) {
                String h = ((HeaderElement) header.elementAt(i)).lower;
                if ("to".equals(h) || "cc".equals(h)) {
                    mailer.print("RCPT TO: " + ((HeaderElement) header.elementAt(i)).content + "\r\n");
                    mailer.flush();
                    check("RCPT TO", "250", status.readLine());
                    ok = true;
                }
            }
            if (!ok) throw new Exception("SendMail: To element missing");
            mailer.print("DATA\r\n");
            mailer.flush();
            check("DATA", "354", status.readLine());
            for (int i = 0; i < header.size(); i++) {
                HeaderElement h = (HeaderElement) header.elementAt(i);
                if (!"mail".equals(h.lower)) {
                    mailer.print(h.name + ": " + h.content + "\r\n");
                }
            }
            mailer.print("\r\n");
            mailer.print(body);
            mailer.print("\r\n.\r\n");
            mailer.flush();
            check("done", "250", status.readLine());
            mailer.print("QUIT\r\n");
            mailer.close();
            status.close();
            s.close();
        } catch (Exception ex) {
            if (s != null) try {
                s.close();
            } catch (Exception ex1) {
            }
            errorHandler.error = ex;
        }
    }

    protected void check(String info, String code, String line) throws Exception {
        if (!line.startsWith(code)) {
            throw new Exception("MailerThread " + info + ": except: " + code + " got: " + line);
        }
    }

    MailerThread(Vector header, Mailer errorHandler) {
        this.header = header;
        this.errorHandler = errorHandler;
    }
}
