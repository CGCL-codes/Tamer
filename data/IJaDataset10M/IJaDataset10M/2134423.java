package com.pioneer.app.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;

public class Dom4jUtil {

    public static void writeDocToOut(Document doc, String sCharSet, OutputStream out) {
        OutputFormat fmt = new OutputFormat("  ", true, sCharSet);
        try {
            org.dom4j.io.XMLWriter xmlWriter = new org.dom4j.io.XMLWriter(out, fmt);
            doc.setXMLEncoding(sCharSet);
            xmlWriter.write(doc);
            xmlWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeDocToFile(Document doc, String sCharSet, String sFN) {
        try {
            FileOutputStream fout = new FileOutputStream(sFN);
            writeDocToOut(doc, sCharSet, fout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String writeDocToStr(Document doc, String sCharSet) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        writeDocToOut(doc, sCharSet, bout);
        try {
            return new String(bout.toByteArray(), sCharSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Document getDocFromStream(InputStream in) {
        Document doc = null;
        try {
            doc = new SAXReader().read(in);
        } catch (Exception e) {
        }
        return doc;
    }

    public static Document getDocFromStr(String s) {
        return getDocFromStr(s, "utf-8");
    }

    public static Document getDocFromStr(String s, String sCharSet) {
        try {
            ByteArrayInputStream bin = new ByteArrayInputStream(s.getBytes(sCharSet));
            return new SAXReader().read(bin);
        } catch (Exception e) {
        }
        return null;
    }

    public static Document getDocFromStream(InputStream in, String sCharSet) {
        try {
            InputStreamReader irer = new InputStreamReader(in, sCharSet);
            return new SAXReader().read(irer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Document getDocFromFile(String sFn) throws Exception {
        try {
            return new SAXReader().read(sFn);
        } catch (Exception e) {
            throw e;
        }
    }
}
