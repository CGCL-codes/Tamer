package com.objectwave.xjr;

import java.io.InputStream;
import java.util.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.SAXParser;
import org.apache.log4j.*;
import org.xml.sax.*;

/**
 * @author  cson
 * @version  $Id: XJRFactory.java,v 1.1.1.1 2002/05/15 16:50:02 trever Exp $
 * @author:  Trever Shick
 */
public class XJRFactory extends DefaultHandler {

    /**
	 */
    public static final Category log = Category.getInstance(XJRFactory.class);

    String defaultPackageName = "";

    XJRBuilder rootBuilder = null;

    XJRConstructor defaultConstructor = null;

    XJRConnector defaultConnector = null;

    XJRError errors = null;

    XJRNamingScheme namingScheme = null;

    Object constructedObject = null;

    /**
	 *Constructor for the XJRFactory object
	 */
    public XJRFactory() {
        this.rootBuilder = new XJRBuilder(null, this);
        this.defaultConstructor = new XJRConstructor(this);
        this.defaultConnector = new XJRConnector(this);
        this.errors = new XJRError();
        this.namingScheme = new XJRNamingScheme();
    }

    /**
	 *Constructor for the XJRFactory object
	 *
	 * @param  substMap
	 */
    public XJRFactory(HashMap substMap) {
        this();
        this.defaultConstructor = new XJRConstructor(this, substMap);
    }

    /**
	 * Constructs an XJRFactory that returns an xjr object
	 *
	 * @param  clz The class of the object that the {@link build(java.net.URL)} method returns.
	 */
    public XJRFactory(Class clz) {
        this();
        if (clz == null) {
            throw new IllegalArgumentException("Class cannot be null.");
        }
        String clzName = clz.getName();
        int i = clzName.lastIndexOf('.');
        if (i > -1) {
            defaultPackageName = clzName.substring(0, i);
        } else {
            defaultPackageName = clzName;
        }
    }

    /**
	 *Sets the XJRNamingScheme attribute of the XJRFactory object
	 *
	 * @param  scheme The new XJRNamingScheme value
	 */
    public void setXJRNamingScheme(XJRNamingScheme scheme) {
        this.namingScheme = scheme;
    }

    /**
	 *Sets the XJRConstructor attribute of the XJRFactory object
	 *
	 * @param  cont The new XJRConstructor value
	 */
    public void setXJRConstructor(XJRConstructor cont) {
        this.defaultConstructor = cont;
    }

    /**
	 *Sets the XJRConnector attribute of the XJRFactory object
	 *
	 * @param  conn The new XJRConnector value
	 */
    public void setXJRConnector(XJRConnector conn) {
        this.defaultConnector = conn;
    }

    /**
	 * @param  pkg The new DefaultPackage value
	 */
    public void setDefaultPackage(String pkg) {
        this.defaultPackageName = pkg;
    }

    /**
	 *Gets the NamingScheme attribute of the XJRFactory object
	 *
	 * @return  The NamingScheme value
	 */
    public XJRNamingScheme getNamingScheme() {
        return this.namingScheme;
    }

    /**
	 * @param  parser
	 * @param  inputSource
	 * @return
	 * @exception  Exception
	 */
    public Object build(SAXParser parser, InputSource inputSource) throws Exception {
        try {
            parser.parse(inputSource, this);
            Object ob = constructedObject;
            constructedObject = null;
            if (ob == null) {
                log.warn("Unable to construct object");
            }
            return ob;
        } catch (Exception e) {
            log.error("build", e);
            throw e;
        }
    }

    /**
	 *  This is a wrapper around {@link #build(SaxParser, InputSource)}
	 *  It creates an XMLReader from SAXParserFactory. This requires JAXP 1.1 or above.
	 *
	 * @param  xmlFile Fully qualified url to the xml file.
	 * @return  XJR object representing root node.
	 * @exception  Exception
	 */
    public Object build(java.net.URL xmlFile) throws Exception {
        if (xmlFile == null) {
            throw new IllegalArgumentException("A valid xml file is required.");
        }
        SAXParser parser = null;
        try {
            parser = javax.xml.parsers.SAXParserFactory.newInstance().newSAXParser();
            InputSource inputSource = new InputSource(xmlFile.toString());
            return build(parser, inputSource);
        } catch (SAXException e) {
            log.error("build(java.net.URL)", e);
            throw e;
        } finally {
            parser = null;
        }
    }

    /**
	 *	This method returns a builder for XML objects.  If none is found,
	 *  then a null is returned.
	 *
	 * @param  builder
	 * @param  path
	 * @return  A builder for the xml element specified in path, null if none exists
	 */
    public XJRBuilder builder(XJRBuilder builder, String path) {
        return new XJRBuilder(builder, this);
    }

    /**
	 *  Pass this on to the root builder.
	 *
	 * @param  chars
	 * @param  a
	 * @param  b
	 */
    public void characters(char[] chars, int a, int b) {
        rootBuilder.characters(chars, a, b);
    }

    /**
	 * @param  parentPath
	 * @param  childName
	 * @return  a connector that knows how to connect a child to
	 *	it's parent.
	 */
    public XJRConnector connector(String parentPath, String childName) {
        return defaultConnector;
    }

    /**
	 *  Returns an XJRConstructor that can be used to construct instances
	 *  of objects to be "built" by the XJRBuilder.  This method should be
	 *  overridden by subclasses to provide a constructor that is different
	 *  than the default which uses basic Reflection to perform it's work.
	 *
	 * @param  pathName The XPath name of the element to construct
	 * @return
	 */
    public XJRConstructor constructor(String pathName) {
        return this.defaultConstructor;
    }

    /**
	 * @param  name
	 */
    public void endElement(String uri, String localName, String qName) {
        rootBuilder.endElement(uri, localName, qName);
        if (localName.equals(rootBuilder.handlingElement)) {
            constructedObject = rootBuilder.constructedObject;
            rootBuilder = new XJRBuilder(null, this);
        }
    }

    /**
	 * @param  name
	 * @param  attList
	 */
    public void startElement(String uri, String localName, String qName, Attributes attList) {
        rootBuilder.startElement(uri, localName, qName, attList);
    }

    /**
	 * @return
	 */
    public XJRError errors() {
        return this.errors;
    }

    /**
	 * @return
	 */
    public boolean hasErrors() {
        return errors.hasErrors();
    }

    /**
	 * @return
	 */
    public boolean hasWarnings() {
        return errors.hasWarnings();
    }

    /**
	 * @param  parm1
	 * @exception  org.xml.sax.SAXException
	 */
    public void fatalError(SAXParseException parm1) throws org.xml.sax.SAXException {
        log.fatal(parm1.getMessage());
    }

    /**
	 * @param  parm1
	 * @exception  org.xml.sax.SAXException
	 */
    public void warning(SAXParseException parm1) throws org.xml.sax.SAXException {
        log.warn(parm1.getMessage());
    }

    /**
	 * @param  parm1
	 * @exception  org.xml.sax.SAXException
	 */
    public void error(SAXParseException parm1) throws org.xml.sax.SAXException {
        log.error(parm1.getMessage());
    }
}
