package com.mentorgen.tools.profile.output;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class JipBundleParser extends DefaultHandler {

    private IJipBundleHandler mBundleHandler;

    CharArrayWriter text = new CharArrayWriter();

    private static final String DEFAULT_FRAMEWORK = "org.apache.felix.framework";

    private static final String DEFAULT_PROFILER = "ca.ubc.magic.osgi.jip.Profiler";

    private JipBundleParser(IJipBundleHandler handler) {
        mBundleHandler = handler;
    }

    public static void jipParseBundle(InputStream is) throws SAXException, IOException {
        IJipBundleHandler handler = new JipBundleHandler();
        JipBundleParser bundleParser = new JipBundleParser(handler);
        handler.skipListAdd(JipBundleParser.DEFAULT_FRAMEWORK);
        handler.skipListAdd(JipBundleParser.DEFAULT_PROFILER);
        XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(bundleParser);
        xr.setErrorHandler(bundleParser);
        xr.parse(new InputSource(is));
    }

    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void warning(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void startElement(String uri, String name, String qName, Attributes atts) {
        try {
            text.reset();
            if (name.equals("bnd")) mBundleHandler.startBundle();
            if (name.equals("service")) mBundleHandler.startService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void endElement(String uri, String name, String qName) {
        try {
            if (name.equals("bnd")) mBundleHandler.endBundle(); else if (name.equals("sn")) mBundleHandler.getBundle(getText()); else if (name.equals("objectClass")) mBundleHandler.getService(getText()); else if (name.equals("service.id")) mBundleHandler.verifyService(getText()); else if (name.equals("avg-data-in")) mBundleHandler.setAvgDataIn(getText()); else if (name.equals("avg-data-out")) mBundleHandler.setAvgDataOut(getText()); else if (name.equals("avg-data-total")) mBundleHandler.setTotalData(getText()); else if (name.equals("service")) mBundleHandler.endService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getText() {
        return text.toString().trim();
    }

    public void characters(char[] ch, int start, int length) {
        text.write(ch, start, length);
    }

    private String getAttrString(Attributes atts, String name) {
        String value = atts.getValue(name);
        if (value == null) {
            throw new RuntimeException("no value for '" + name + "'");
        }
        return value;
    }

    private long getAttrLong(Attributes atts, String name) {
        String value = getAttrString(atts, name);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException(name + " (" + value + ") isn't a long");
        }
    }
}
