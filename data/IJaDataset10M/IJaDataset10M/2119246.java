package edu.ucdavis.genomics.metabolomics.util.config.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import edu.ucdavis.genomics.metabolomics.util.io.source.Source;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Max Planck Institute</p>
 * @author Gert Wohlgemuth
 * @version 1.0
 */
public final class XmlHandling {

    /**
     * DOCUMENT ME!
     */
    private static Document _Document = new Document();

    /**
     * Creates a new XmlHandling object.
     */
    XmlHandling() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static Document readDocument(File file) throws Exception {
        return new SAXBuilder().build(file);
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static Element readXml(File file) throws Exception {
        return readXml(new FileInputStream(file));
    }

    public static Element readXml(Source source) throws Exception {
        return readXml(source.getStream());
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static Element readXml(InputStream in) throws Exception {
        return readXml(new InputStreamReader(in));
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static Element readXml(Reader in) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        _Document = builder.build(in);
        in.close();
        return _Document.getRootElement();
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @param document DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void writeDocument(File file, Document document) throws Exception {
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        outputter.output(document, new FileOutputStream(file));
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @param root DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void writeXml(File file, Element root) throws Exception {
        XMLOutputter putter = new XMLOutputter();
        putter.output(root, new FileOutputStream(file));
    }

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     * @param root DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void writeXml(OutputStream out, Element root) throws Exception {
        XMLOutputter putter = new XMLOutputter();
        _Document.setRootElement(root);
        putter.output(_Document, out);
    }

    public static void logXML(Logger logger, Element element) throws Exception {
        StringWriter writer = new StringWriter();
        XMLOutputter putter = new XMLOutputter();
        putter.output(element, writer);
        writer.flush();
        logger.info("\n" + writer.getBuffer().toString() + "\n");
        writer.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     * @param root DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void writeXml(Writer out, Element root) throws Exception {
        XMLOutputter putter = new XMLOutputter();
        putter.output(root, out);
    }
}
