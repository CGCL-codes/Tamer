package com.michaelbelyakov1967.xml;

import java.io.*;
import java.sql.*;
import java.util.Properties;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import org.w3c.dom.Node;
import javax.xml.transform.dom.DOMSource;
import com.michaelbelyakov1967.util.*;

public class XSLHolder {

    private Transformer transformer;

    protected static TransformerFactory tFactory;

    private Source source;

    public XSLHolder() throws TransformerConfigurationException {
        if (tFactory == null) tFactory = TransformerFactory.newInstance();
    }

    public XSLHolder(Object o1, Object o2) throws TransformerConfigurationException {
        this();
        setInput(o1, o2);
    }

    public XSLHolder(Object o1) throws TransformerConfigurationException {
        this();
        setInput(o1, null);
    }

    public void setSource(Source s) {
        source = s;
    }

    private void setInput(Object o1, Object o2) throws TransformerConfigurationException {
        InputStream is = o2 == null ? StreamPreparator.getInputStream(o1) : StreamPreparator.getInputStream(o1, o2);
        transformer = tFactory.newTransformer(new StreamSource(is));
    }

    public void setParameters(String[][] sa) {
        if (sa != null) for (int i = 0; i < sa.length; i++) setParameter(sa[i]);
    }

    public void setParameter(String[] sa) {
        transformer.setParameter(sa[0], sa[1]);
    }

    public void setParameter(String name, String value) {
        transformer.setParameter(name, value);
    }

    private void transform(Source in, Result out, String[][] parameters) throws TransformerException {
        if (parameters != null) setParameters(parameters);
        if (source == null) source = in == null ? new StreamSource(new ByteArrayInputStream("<R/>".getBytes())) : in;
        transformer.transform(source, out);
        source = null;
    }

    private void transform(Object o1, Object o2, Result out, String[][] params) throws TransformerException {
        if (o1 instanceof Source) {
            transform((Source) o1, out, params);
            return;
        }
        Source src = null;
        InputStream is = o2 == null ? StreamPreparator.getInputStream(o1) : StreamPreparator.getInputStream(o1, o2);
        if (is != null) src = new StreamSource(is);
        transform(src, out, params);
    }

    private void transform(Object o1, Result out, String[][] params) throws TransformerException {
        transform(o1, null, out, params);
    }

    public void transform(Object o1, Object o2, Writer out, String[][] params) throws TransformerException {
        transform(o1, o2, new StreamResult(out), params);
    }

    public void transform(Object o1, Object o2, OutputStream out, String[][] params) throws TransformerException {
        transform(o1, o2, new StreamResult(out), params);
    }

    public void transform(Object o1, Object o2, Writer out) throws TransformerException {
        transform(o1, o2, new StreamResult(out), null);
    }

    public void transform(Object o1, Object o2, OutputStream out) throws TransformerException {
        transform(o1, o2, new StreamResult(out), null);
    }

    public void transform(Object o1, Writer out) throws TransformerException {
        transform(o1, null, new StreamResult(out), null);
    }

    public void transform(Object o1, OutputStream out) throws TransformerException {
        transform(o1, null, new StreamResult(out), null);
    }

    public void transform(Writer out) throws TransformerException {
        transform(null, null, new StreamResult(out), null);
    }

    public void transform(OutputStream out) throws TransformerException {
        transform(null, null, new StreamResult(out), null);
    }

    public void transform(Node n, Writer out, String[][] sa) throws TransformerException, IOException, SQLException {
        transform(new DOMSource(n), new StreamResult(out), sa);
    }

    public void transform(Node n, OutputStream out, String[][] sa) throws TransformerException, IOException, SQLException {
        transform(new DOMSource(n), new StreamResult(out), sa);
    }

    public void transform(Node n, Writer out) throws TransformerException, IOException, SQLException {
        transform(new DOMSource(n), null, new StreamResult(out), null);
    }

    public void transform(Node n, OutputStream out) throws TransformerException, IOException, SQLException {
        transform(new DOMSource(n), null, new StreamResult(out), null);
    }
}
