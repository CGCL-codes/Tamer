package com.apelon.soap.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import com.apelon.common.xml.XmlUtilities;

/**
 * @author jweis
 */
public class SoapMessageParser {

    ArrayList fHandlers = new ArrayList();

    XmlPullParser fParser;

    ArrayList fNamespaces = new ArrayList();

    StringBuffer fInternalBuffer = new StringBuffer();

    public SoapMessageParser() throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
        factory.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        fParser = factory.newPullParser();
    }

    public SoapMessageParser(SoapMessageHandler h) throws Exception {
        this();
        addSoapMessageHandler(h);
    }

    public void addSoapMessageHandler(SoapMessageHandler h) throws Exception {
        if (h != null) fHandlers.add(h);
    }

    public void parseSoapMessage(Reader r) throws Exception {
        fInternalBuffer.setLength(0);
        fNamespaces.clear();
        fParser.setInput(r);
        parse();
    }

    private void parse() throws XmlPullParserException, IOException {
        try {
            int eventType = fParser.getEventType();
            do {
                if (eventType == XmlPullParser.START_TAG) {
                    if (fParser.getName().equalsIgnoreCase("envelope")) {
                        String ns = null;
                        String prefix = null;
                        String ver = "1.1";
                        try {
                            for (int k = 0; (ns = fParser.getNamespaceUri(k)) != null; k++) {
                                prefix = fParser.getNamespacePrefix(k);
                                ns = prefix + "=\"" + ns + "\"";
                                if (ns.indexOf("envelope") == -1) fNamespaces.add(ns);
                                Iterator iter = fHandlers.iterator();
                                while (iter.hasNext()) {
                                    ((SoapMessageHandler) iter.next()).namespace(ns);
                                }
                                if (ns.indexOf("http://www.w3.org/2003/05/soap-envelope") > -1) {
                                    ver = "1.2";
                                }
                            }
                        } catch (Exception e) {
                        }
                        try {
                            Iterator iter = fHandlers.iterator();
                            while (iter.hasNext()) {
                                ((SoapMessageHandler) iter.next()).soapVersion(ver);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        fInternalBuffer.append(fParser.getText());
                    } else if (fParser.getName().equalsIgnoreCase("body")) {
                        fInternalBuffer.append(fParser.getText());
                        try {
                            Iterator iter = fHandlers.iterator();
                            while (iter.hasNext()) {
                                ((SoapMessageHandler) iter.next()).soapMessageStart(fInternalBuffer.toString());
                            }
                            fInternalBuffer.setLength(0);
                        } catch (NullPointerException e) {
                        }
                        eventType = fParser.next();
                        setBody();
                        if (fParser.getEventType() == XmlPullParser.START_TAG && fParser.isEmptyElementTag()) fParser.next(); else if (fParser.getEventType() == XmlPullParser.END_TAG) fInternalBuffer.append(fParser.getText());
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    fInternalBuffer.append(fParser.getText());
                    if (fParser.getName().equalsIgnoreCase("envelope")) {
                        try {
                            Iterator iter = fHandlers.iterator();
                            while (iter.hasNext()) {
                                ((SoapMessageHandler) iter.next()).soapMessageEnd(fInternalBuffer.toString());
                            }
                            break;
                        } catch (NullPointerException e) {
                        }
                    }
                }
                eventType = fParser.next();
            } while (eventType != XmlPullParser.END_DOCUMENT);
        } catch (XmlPullParserException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    public void parseSoapMessage(InputStream is, String encoding) throws IOException, Exception {
        try {
            fInternalBuffer.setLength(0);
            fNamespaces.clear();
            fParser.setInput(new InputStreamReader(is, encoding));
            parse();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBody() {
        StringBuffer buf = new StringBuffer();
        String text = null;
        try {
            int eventType = fParser.getEventType();
            do {
                if (eventType == XmlPullParser.START_TAG && fParser.isEmptyElementTag()) {
                    buf.append(fParser.getText());
                    Iterator iter = fHandlers.iterator();
                    while (iter.hasNext()) {
                        ((SoapMessageHandler) iter.next()).soapBody(addNamespaceToBody(buf.toString()));
                    }
                    return;
                }
                if (eventType == XmlPullParser.END_TAG) {
                    if (fParser.getName().equalsIgnoreCase("body")) {
                        Iterator iter = fHandlers.iterator();
                        while (iter.hasNext()) {
                            ((SoapMessageHandler) iter.next()).soapBody(addNamespaceToBody(buf.toString()));
                        }
                        return;
                    }
                }
                text = fParser.getText();
                if (eventType == XmlPullParser.TEXT) {
                    text = XmlUtilities.escape(text);
                }
                buf.append(text);
                eventType = fParser.next();
            } while (eventType != XmlPullParser.END_DOCUMENT);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String addNamespaceToBody(String body) {
        Iterator iter = fNamespaces.iterator();
        StringBuffer buf = new StringBuffer();
        String rep = ">";
        while (iter.hasNext()) {
            buf.append(" ");
            buf.append("xmlns:");
            buf.append((String) iter.next());
        }
        try {
            if (fParser.getEventType() == XmlPullParser.START_TAG && fParser.isEmptyElementTag()) rep = "/>";
        } catch (Exception e) {
        }
        buf.append(rep);
        return body.replaceFirst(rep, buf.toString());
    }
}
