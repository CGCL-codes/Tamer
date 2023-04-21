package org.wikiup.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.wikiup.core.imp.df.DirectoryDocumentFilter;
import org.wikiup.core.imp.df.FileDocumentFilter;
import org.wikiup.core.imp.df.StreamDocumentFilter;
import org.wikiup.core.imp.df.StringDocumentFilter;
import org.wikiup.core.imp.document.DocumentImpl;
import org.wikiup.core.inf.Attribute;
import org.wikiup.core.inf.Document;
import org.wikiup.core.inf.Element;
import org.wikiup.core.inf.ExceptionHandler;
import org.wikiup.core.inf.Gettable;
import org.wikiup.core.inf.Resource;
import org.wikiup.core.util.XPath;

public class DocumentUtil {

    public static final String PATH_DELIMETER = "[/.:\\\\]";

    public static final String PATH_NODE_DELIMETER = "[/\\\\]";

    public static final int CONDITION_HEAD_TOKEN = '[';

    public static final int CONDITION_TAIL_TOKEN = ']';

    private static final String S_TOKEN = "<";

    private static final String E_TOKEN = ">";

    private static final String T_TOKEN = "/>";

    private static final String LB = "\r\n";

    public static Document createDocument(String name) {
        return new DocumentImpl(name);
    }

    public static Document clone(Document doc) {
        Document c = createDocument(doc.getName());
        mergeDocument(c, doc);
        return c;
    }

    public static String getDocumentName(Document doc, String def) {
        return getAttributeValue(doc, "name", def);
    }

    public static String getDocumentName(Document doc) {
        return getDocumentName(doc, null);
    }

    public static String getDocumentValue(Document doc) {
        String value = ValueUtil.getAttributeValue(doc.getAttribute("value"));
        return value != null ? value : ValueUtil.getAttributeValue(doc);
    }

    public static String getDocumentValue(Document doc, String name) {
        return getDocumentValue(doc, name, null);
    }

    public static String getDocumentValue(Document doc, String name, String defValue) {
        String value = getAttributeValue(doc, name, null);
        return value != null ? value : getChildValue(doc, name, defValue);
    }

    public static String getDocumentValueByXPath(Document doc, String path) {
        return getDocumentValueByXPath(doc, path, "");
    }

    public static String getDocumentValueByXPath(Document doc, String path, String defValue) {
        try {
            Attribute vs = getAttributeByXPath(doc, path);
            return vs != null ? ValueUtil.getAttributeValue(vs) : defValue;
        } catch (Exception ex) {
            return defValue;
        }
    }

    public static int getDocumentInteger(Document doc, String name, int defValue) {
        return ValueUtil.toInteger(getDocumentValue(doc, name, null), defValue);
    }

    public static Attribute touchAttribute(Element node, String name) {
        Attribute a = node.getAttribute(name);
        return a != null ? a : node.addAttribute(name);
    }

    public static Document touchChild(Document node, String name) {
        Document t = node.getChild(name);
        return t != null ? t : node.addChild(name);
    }

    public static Document touchDocument(Document doc, String[] paths, int depth) {
        int i;
        for (i = 0; i < depth; i++) if (!StringUtil.isEmpty(paths[i])) doc = touchChild(doc, paths[i]);
        return doc;
    }

    public static Document touchDocument(Document doc, String path) {
        String paths[] = path.split("/");
        return touchDocument(doc, paths, paths.length - 1);
    }

    public static void setAttributeValue(Element node, String name, Object value) {
        Attribute vs = node.getAttribute(name);
        (vs != null ? vs : node.addAttribute(name)).setObject(value);
    }

    public static void setChildValue(Document node, String name, String value) {
        Attribute vs = node.getChild(name);
        (vs != null ? vs : node.addChild(name)).setObject(value);
    }

    public static String getAttributeValue(Element node, String name, String defValue) {
        String value = ValueUtil.getAttributeValue(node.getAttribute(name));
        return value != null ? value : defValue;
    }

    public static String getAttributeValue(Element node, String name) {
        return getAttributeValue(node, name, "");
    }

    public static boolean getAttributeBooleanValue(Element node, String name, boolean defValue) {
        return ValueUtil.toBoolean(getAttributeValue(node, name, null), defValue);
    }

    public static int getAttributeIntegerValue(Element node, String name, int defValue) {
        return ValueUtil.toInteger(getAttributeValue(node, name, null), defValue);
    }

    public static String getChildValue(Document doc, String name) {
        return getChildValue(doc, name, "");
    }

    public static boolean getChildBooleanValue(Document node, String name, boolean defValue) {
        return ValueUtil.toBoolean(getChildValue(node, name, null), defValue);
    }

    public static String getChildValue(Document doc, String name, String def) {
        String value = ValueUtil.getAttributeValue(doc.getChild(name));
        return value != null ? value : def;
    }

    public static Attribute getAttributeByXPath(Document doc, String xpath) {
        return new XPath(xpath).getAttribute(doc);
    }

    public static Document getDocumentByXPath(Document doc, String xpath) {
        return (Document) new XPath(xpath).getAttribute(doc);
    }

    public static Document findMatchesChild(Document doc, String attribute, String value) {
        return findMatchesChild(doc.getChildren(), attribute, value);
    }

    public static Document findMatchesChild(Document doc, String childName, String attribute, String value) {
        return findMatchesChild(doc.getChildren(childName), attribute, value);
    }

    private static Document findMatchesChild(Iterator<Document> iterator, String attribute, String value) {
        while (iterator.hasNext()) {
            Document node = iterator.next();
            if (value.matches(getAttributeValue(node, attribute))) return node;
        }
        return null;
    }

    public static Document loadXMLFromURL(String url) {
        try {
            return loadXMLFromURL(new URL(url));
        } catch (IOException ex) {
            return null;
        }
    }

    public static Document loadXMLFromURL(URL url) {
        try {
            return loadXMLFromStream(url.openStream(), true);
        } catch (IOException ex) {
            Assert.fail(ex);
        }
        return null;
    }

    public static Document loadXMLFromStream(InputStream input, boolean close) {
        Document xml;
        try {
            xml = new StreamDocumentFilter().filter(input);
        } finally {
            if (close) StreamUtil.close(input);
        }
        return xml;
    }

    public static Document loadXMLFromStream(InputStream input) {
        return loadXMLFromStream(input, false);
    }

    public static Document loadXMLFromString(String str) {
        return new StringDocumentFilter().filter(str);
    }

    public static Document loadXMLFromResourceLoader(Resource rl) {
        return loadXMLFromStream(rl.open(), true);
    }

    public static Document loadXMLFromFile(String fileName) {
        return loadXMLFromFile(FileUtil.getFile(fileName));
    }

    public static Document loadXMLFromFile(File file) {
        return new FileDocumentFilter().filter(file);
    }

    public static Document loadXMLFromDirectory(String dirName) {
        return new DirectoryDocumentFilter().filter(FileUtil.getFile(dirName));
    }

    public static Document loadXMLFromClassResource(Class clazz) {
        String name = clazz.getName();
        int idx = name.lastIndexOf('.');
        String resourceName = (idx != -1 ? name.substring(idx + 1) : name) + ".xml";
        return loadXMLFromStream(clazz.getResourceAsStream(resourceName));
    }

    public static void clearDocument(Document doc) {
        clearAttribute(doc);
        clearChild(doc);
    }

    public static void clearChild(Document doc) {
        Iterator iterator = doc.getChildren();
        while (iterator.hasNext()) iterator.remove();
    }

    public static void clearAttribute(Element node) {
        Iterator iterator = node.getAttributes();
        while (iterator.hasNext()) iterator.remove();
    }

    public static Document mergeDocument(Document target, Document source) {
        Iterator iterator = source.getChildren();
        target.setObject(source.getObject());
        mergeAttribute(target, source);
        while (iterator.hasNext()) {
            Document ts = (Document) iterator.next();
            mergeDocument(target.addChild(ts.getName()), ts);
        }
        return target;
    }

    public static Document mergeChildren(DocumentImpl target, Iterator<Document> iterator) {
        while (iterator.hasNext()) {
            Document doc = iterator.next();
            target.addChild(doc.getName(), doc);
        }
        return target;
    }

    public static void mergeAttribute(Document target, Document source) {
        Iterator iterator = source.getAttributes();
        while (iterator.hasNext()) {
            Attribute vs = (Attribute) iterator.next();
            touchAttribute(target, vs.getName()).setObject(vs.getObject());
        }
    }

    public static void copyDocument(Document target, Document source) {
        clearDocument(target);
        mergeDocument(target, source);
    }

    public static void printToStream(Document doc, OutputStream stream) {
        Writer writer = new OutputStreamWriter(stream);
        printToWriter(doc, writer, 0);
        StreamUtil.flush(writer);
    }

    public static void printToStream(Document doc, OutputStream stream, int indent) {
        Writer writer = new OutputStreamWriter(stream);
        printToWriter(doc, writer, indent);
        StreamUtil.flush(writer);
    }

    public static void printToWriter(Document doc, Writer writer) {
        printToWriter(doc, writer, 0, true);
    }

    public static void printToWriter(Document doc, Writer writer, int indent) {
        printToWriter(doc, writer, indent, true);
    }

    public static void printToWriter(Document doc, Writer writer, int indent, boolean encode) {
        String data, name = doc.getName() != null ? doc.getName() : "";
        Iterator<Document> iterator = doc.getChildren();
        boolean isLeave = !iterator.hasNext(), hasData;
        try {
            printIndent(writer, indent);
            writer.write(S_TOKEN);
            writer.write(name);
            printAttributes(writer, doc);
            data = encode ? encode(ValueUtil.getAttributeValue(doc)) : ValueUtil.getAttributeValue(doc);
            hasData = data != null;
            if (isLeave && !hasData) {
                writer.write(T_TOKEN);
                writer.write(LB);
            } else {
                writer.write(E_TOKEN);
                if (hasData && data.length() > 0) {
                    if (isLeave) writer.write(data); else {
                        writer.write(LB);
                        printIndent(writer, indent + 1);
                        writer.write(data);
                        writer.write(LB);
                    }
                }
                if (!isLeave) {
                    writer.write(LB);
                    while (iterator.hasNext()) printToWriter(iterator.next(), writer, indent + 1, encode);
                }
                if (!isLeave || !hasData) printIndent(writer, indent);
                writer.write(S_TOKEN + "/" + name + E_TOKEN + LB);
            }
        } catch (IOException ex) {
            Assert.fail(ex);
        }
    }

    public static org.w3c.dom.Document loadFromXML(File file) {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        } catch (Exception ex) {
            Assert.fail(ex);
        }
        return null;
    }

    public static void saveToXML(org.w3c.dom.Document doc, File file) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(file));
        } catch (Exception ex) {
            Assert.fail(ex);
        }
    }

    public static void saveToXML(Document doc, File file, String charset) {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file), charset);
            writeXMLHeader(writer, charset);
            DocumentUtil.printToWriter(doc, writer);
            StreamUtil.close(writer);
        } catch (FileNotFoundException e) {
            Assert.fail(e);
        } catch (UnsupportedEncodingException e) {
            Assert.fail(e);
        } finally {
            StreamUtil.close(writer);
        }
    }

    public static Document findRoutingEntry(Document doc, Gettable getter) {
        Iterator iterator = doc.getChildren();
        if (!iterator.hasNext()) return isRoutingMatch(doc, getter) ? doc : null;
        while (iterator.hasNext()) {
            Document node = (Document) iterator.next();
            if (isRoutingMatch(node, getter)) return findRoutingEntry(node, getter);
        }
        return null;
    }

    public static String getNamespace(Document doc, String name) {
        Iterator iterator = doc.getAttributes();
        while (iterator.hasNext()) {
            Attribute value = (Attribute) iterator.next();
            String attrName = value.getName();
            if (attrName.startsWith("xmlns") && value.getObject().equals(name)) return attrName.length() > 6 ? attrName.substring(6) : "";
        }
        return name;
    }

    private static boolean isRoutingMatch(Document doc, Gettable<?> getter) {
        Iterator<Attribute> iterator = doc.getAttributes();
        while (iterator.hasNext()) {
            Attribute attr = iterator.next();
            Object obj = getter.get(attr.getName());
            if (obj != null && !obj.toString().matches(ValueUtil.getAttributeValue(attr))) return false;
        }
        return true;
    }

    private static void printAttributes(Writer writer, Document node) throws IOException {
        Iterator<Attribute> iterator = node.getAttributes();
        while (iterator.hasNext()) {
            Attribute vs = iterator.next();
            String value = ValueUtil.getAttributeValue(vs);
            writer.write(" " + vs.getName() + "=\"" + (value == null ? "" : encode(value)) + "\"");
        }
    }

    public static void printIndent(Writer writer, int indent) throws IOException {
        int i;
        for (i = 0; i < indent; i++) writer.write("  ");
    }

    public static String encode(String xml) {
        return xml != null ? xml.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("'", "&apos;").replace("\"", "&quot;") : xml;
    }

    public static Document getDocumentByPath(Document node, String route[], int length) {
        return getDocumentByPath(node, route, length, false);
    }

    public static Document getDocumentByPath(Document node, String route[], int length, boolean create) {
        int i;
        for (i = 0; i < length && route != null; i++) if (!StringUtil.isEmpty(route[i])) {
            node = create ? touchChild(node, route[i]) : node.getChild(route[i]);
            if (node == null) break;
        }
        return node;
    }

    public static void writeXMLHeader(Writer writer, String cs) {
        writeXMLHeader(writer, cs, null);
    }

    public static void writeXMLHeader(Writer writer, String cs, ExceptionHandler eh) {
        try {
            writer.write("<?xml version=\"1.0\" encoding=\"" + cs + "\"?>\n");
        } catch (IOException e) {
            if (!InterfaceUtil.handleException(eh, e)) Assert.fail(e);
        }
    }
}
