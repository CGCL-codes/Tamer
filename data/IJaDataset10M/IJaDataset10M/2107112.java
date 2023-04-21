package net.woodstock.rockapi.xml.dom;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.woodstock.rockapi.sys.SysLogger;
import net.woodstock.rockapi.utils.Base64Utils;
import net.woodstock.rockapi.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XmlElement extends ElementWrapper {

    private static final long serialVersionUID = 8642703359069757721L;

    private static DateFormat format;

    public static final String CLASS_ATTRIBUTE = "class";

    protected XmlElement() {
        super();
    }

    protected XmlElement(Element e) {
        super();
        this.setElement(e);
    }

    public CDATASection addCDATASection(String data) {
        CDATASection cdata = this.getOwnerDocument().createCDATASection(data);
        this.appendChild(cdata);
        return cdata;
    }

    public Comment addComment(String data) {
        Comment c = this.getOwnerDocument().createComment(data);
        this.appendChild(c);
        return c;
    }

    public XmlElement addElement(Element e) {
        if (e instanceof XmlElement) {
            e = ((XmlElement) e).getElement();
        }
        Document doc = this.getOwnerDocument();
        Element ee = (Element) doc.importNode(e, true);
        this.appendChild(ee);
        return XmlElement.toXmlElement(ee);
    }

    public XmlElement addElement(String name) {
        Document doc = this.getOwnerDocument();
        Element e = doc.createElement(name);
        this.appendChild(e);
        return XmlElement.toXmlElement(e);
    }

    public XmlElement addElement(String name, Map<String, String> attributes) {
        Document doc = this.getOwnerDocument();
        Element e = doc.createElement(name);
        Set<String> keys = attributes.keySet();
        for (String k : keys) {
            e.setAttribute(k, attributes.get(k));
        }
        this.appendChild(e);
        return XmlElement.toXmlElement(e);
    }

    public XmlElement addElement(String name, boolean data) {
        return this.addElement(name, Boolean.toString(data));
    }

    public XmlElement addElement(String name, byte data) {
        return this.addElement(name, Byte.toString(data));
    }

    public XmlElement addElement(String name, char data) {
        return this.addElement(name, Character.toString(data));
    }

    public XmlElement addElement(String name, Date data) {
        return this.addElement(name, XmlElement.format.format(data));
    }

    public XmlElement addElement(String name, double data) {
        return this.addElement(name, Double.toString(data));
    }

    public XmlElement addElement(String name, float data) {
        return this.addElement(name, Float.toString(data));
    }

    public XmlElement addElement(String name, int data) {
        return this.addElement(name, Integer.toString(data));
    }

    public XmlElement addElement(String name, long data) {
        return this.addElement(name, Long.toString(data));
    }

    public XmlElement addElement(String name, short data) {
        return this.addElement(name, Short.toString(data));
    }

    public XmlElement addElement(String name, String data) {
        Document doc = this.getOwnerDocument();
        Element e = doc.createElement(name);
        Text t = doc.createTextNode(data);
        e.appendChild(t);
        this.appendChild(e);
        return XmlElement.toXmlElement(e);
    }

    public XmlElement addElement(String name, boolean data, Map<String, String> attributes) {
        return this.addElement(name, Boolean.toString(data), attributes);
    }

    public XmlElement addElement(String name, byte data, Map<String, String> attributes) {
        return this.addElement(name, Byte.toString(data), attributes);
    }

    public XmlElement addElement(String name, char data, Map<String, String> attributes) {
        return this.addElement(name, Character.toString(data), attributes);
    }

    public XmlElement addElement(String name, Date data, Map<String, String> attributes) {
        return this.addElement(name, XmlElement.format.format(data), attributes);
    }

    public XmlElement addElement(String name, double data, Map<String, String> attributes) {
        return this.addElement(name, Double.toString(data), attributes);
    }

    public XmlElement addElement(String name, float data, Map<String, String> attributes) {
        return this.addElement(name, Float.toString(data), attributes);
    }

    public XmlElement addElement(String name, int data, Map<String, String> attributes) {
        return this.addElement(name, Integer.toString(data), attributes);
    }

    public XmlElement addElement(String name, long data, Map<String, String> attributes) {
        return this.addElement(name, Long.toString(data), attributes);
    }

    public XmlElement addElement(String name, short data, Map<String, String> attributes) {
        return this.addElement(name, Short.toString(data), attributes);
    }

    public XmlElement addElement(String name, String data, Map<String, String> attributes) {
        Document doc = this.getOwnerDocument();
        Element e = doc.createElement(name);
        Set<String> keys = attributes.keySet();
        Text t = doc.createTextNode(data);
        e.appendChild(t);
        for (String k : keys) {
            e.setAttribute(k, attributes.get(k));
        }
        this.appendChild(e);
        return XmlElement.toXmlElement(e);
    }

    public void copy(Document d) {
        NodeList list = d.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node n = list.item(i);
            if (n instanceof Element) {
                this.copy(n);
                break;
            }
        }
    }

    public void copy(Element e) {
        this.copy((Node) e);
    }

    public void copy(Node n) {
        NodeList list = n.getChildNodes();
        Document doc = this.getOwnerDocument();
        for (int i = 0; i < list.getLength(); i++) {
            Node nn = doc.importNode(list.item(i), true);
            this.appendChild(nn);
        }
        NamedNodeMap map = n.getAttributes();
        for (int i = 0; i < map.getLength(); i++) {
            Attr a = (Attr) doc.importNode(map.item(i), true);
            this.setAttributeNode(a);
        }
    }

    public void removeElement(String name) {
        this.removeElement(this.getElement(name));
    }

    public void removeElement(Element e) {
        this.removeChild(e);
    }

    public void removeElement(XmlElement e) {
        this.removeChild(e.getElement());
    }

    public boolean hasElement(String name) {
        return this.getElement(name) != null;
    }

    public XmlElement getElement(String name) {
        NodeList nodes = this.getElementsByTagName(name);
        int length = nodes.getLength();
        for (int i = 0; i < length; i++) {
            if ((nodes.item(i) instanceof Element) && (nodes.item(i).getNodeName().equals(name))) {
                return XmlElement.toXmlElement((Element) nodes.item(i));
            }
        }
        return null;
    }

    public List<XmlElement> getElements() {
        NodeList nodes = this.getChildNodes();
        int length = nodes.getLength();
        List<XmlElement> list = new LinkedList<XmlElement>();
        for (int i = 0; i < length; i++) {
            if (nodes.item(i) instanceof Element) {
                list.add(XmlElement.toXmlElement((Element) nodes.item(i)));
            }
        }
        return list;
    }

    public List<XmlElement> getElements(String name) {
        NodeList nodes = this.getElementsByTagName(name);
        int length = nodes.getLength();
        List<XmlElement> list = new LinkedList<XmlElement>();
        for (int i = 0; i < length; i++) {
            if (nodes.item(i) instanceof Element) {
                list.add(XmlElement.toXmlElement((Element) nodes.item(i)));
            }
        }
        return list;
    }

    public boolean getBoolean() {
        return this.getBooleanNvl(false);
    }

    public boolean getBooleanNvl(boolean nvl) {
        String s = this.getString();
        if (s == null) {
            return nvl;
        }
        return Boolean.parseBoolean(s);
    }

    public boolean getBoolean(String name) {
        return this.getBooleanNvl(name, false);
    }

    public boolean getBooleanNvl(String name, boolean nvl) {
        return Boolean.parseBoolean(this.getStringNvl(name, Boolean.toString(nvl)));
    }

    public byte getByte() {
        return this.getByteNvl((byte) 0);
    }

    public byte getByteNvl(byte nvl) {
        String s = this.getString();
        if (s == null) {
            return nvl;
        }
        return Byte.parseByte(s);
    }

    public byte getByte(String name) {
        return this.getByteNvl(name, (byte) 0);
    }

    public byte getByteNvl(String name, byte nvl) {
        return Byte.parseByte(this.getStringNvl(name, Byte.toString(nvl)));
    }

    public char getChar() {
        return this.getCharNvl('\0');
    }

    public char getCharNvl(char nvl) {
        String s = this.getString();
        if ((s == null) || (s.length() < 1)) {
            return nvl;
        }
        return s.charAt(0);
    }

    public char getChar(String name) {
        return this.getCharNvl(name, '\0');
    }

    public char getCharNvl(String name, char nvl) {
        String s = this.getString(name);
        if ((s == null) || (s.length() < 1)) {
            return nvl;
        }
        return s.charAt(0);
    }

    public Date getDate() {
        return this.getDateNvl((Date) null);
    }

    public Date getDateNvl(Date nvl) {
        String s = this.getString();
        if (s != null) {
            try {
                return XmlElement.format.parse(s);
            } catch (ParseException e) {
                this.getLogger().warn(e.getMessage(), e);
            }
        }
        return nvl;
    }

    public Date getDate(String name) {
        return this.getDateNvl(name, null);
    }

    public Date getDateNvl(String name, Date nvl) {
        String s = this.getString(name);
        if (s != null) {
            try {
                return XmlElement.format.parse(s);
            } catch (ParseException e) {
                this.getLogger().warn(e.getMessage(), e);
            }
        }
        return nvl;
    }

    public double getDouble() {
        return this.getDoubleNvl(0);
    }

    public double getDoubleNvl(double nvl) {
        String s = this.getString();
        if (s == null) {
            return nvl;
        }
        return Double.parseDouble(s);
    }

    public double getDouble(String name) {
        return this.getDoubleNvl(name, 0);
    }

    public double getDoubleNvl(String name, double nvl) {
        return Double.parseDouble(this.getStringNvl(name, Double.toString(nvl)));
    }

    public float getFloat() {
        return this.getFloatNvl(0);
    }

    public float getFloatNvl(float nvl) {
        String s = this.getString();
        if (s == null) {
            return nvl;
        }
        return Float.parseFloat(s);
    }

    public float getFloat(String name) {
        return this.getFloatNvl(name, 0);
    }

    public float getFloatNvl(String name, float nvl) {
        return Float.parseFloat(this.getStringNvl(name, Float.toString(nvl)));
    }

    public int getInt() {
        return this.getIntNvl(0);
    }

    public int getIntNvl(int nvl) {
        String s = this.getString();
        if (s == null) {
            return nvl;
        }
        return Integer.parseInt(s);
    }

    public int getInt(String name) {
        return this.getIntNvl(name, 0);
    }

    public int getIntNvl(String name, int nvl) {
        return Integer.parseInt(this.getStringNvl(name, Integer.toString(nvl)));
    }

    public long getLong() {
        return this.getLongNvl(0);
    }

    public long getLongNvl(long nvl) {
        String s = this.getString();
        if (s == null) {
            return nvl;
        }
        return Long.parseLong(s);
    }

    public long getLong(String name) {
        return this.getLongNvl(name, 0);
    }

    public long getLongNvl(String name, long nvl) {
        return Long.parseLong(this.getStringNvl(name, Long.toString(nvl)));
    }

    public short getShort() {
        return this.getShortNvl((short) 0);
    }

    public short getShortNvl(short nvl) {
        String s = this.getString();
        if (s == null) {
            return nvl;
        }
        return Short.parseShort(s);
    }

    public short getShort(String name) {
        return this.getShortNvl(name, (short) 0);
    }

    public short getShortNvl(String name, short nvl) {
        return Short.parseShort(this.getStringNvl(name, Short.toString(nvl)));
    }

    public String getString() {
        return this.getStringNvl(null);
    }

    public String getStringNvl(String nvl) {
        NodeList list = this.getChildNodes();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if ((node instanceof CDATASection) || (node instanceof CharacterData) || (node instanceof Text)) {
                String nodeValue = node.getNodeValue();
                if (nodeValue != null) {
                    builder.append(nodeValue.trim());
                }
            }
        }
        String value = builder.toString();
        if (StringUtils.isEmpty(value)) {
            return nvl;
        }
        return value;
    }

    public String getString(String name) {
        return this.getStringNvl(name, null);
    }

    public String getStringNvl(String name, String nvl) {
        XmlElement e = this.getElement(name);
        return e.getStringNvl(nvl);
    }

    public XmlElement addObject(String name, Serializable object) throws IOException {
        String encoded = Base64Utils.serialize(object);
        HashMap<String, String> attributes = new HashMap<String, String>();
        String className = null;
        if (object.getClass().isArray()) {
            className = object.getClass().getComponentType().getCanonicalName();
        } else {
            className = object.getClass().getCanonicalName();
        }
        attributes.put(XmlElement.CLASS_ATTRIBUTE, className);
        return this.addElement(name, encoded, attributes);
    }

    public Object getObject() throws IOException, ClassNotFoundException {
        Object o = Base64Utils.unserialize(this.getString());
        return o;
    }

    public Object getObject(String name) throws IOException, ClassNotFoundException {
        Object o = Base64Utils.unserialize(this.getString(name));
        return o;
    }

    public void setAttribute(String name, boolean data) {
        super.setAttribute(name, Boolean.toString(data));
    }

    public void setAttribute(String name, byte data) {
        super.setAttribute(name, Byte.toString(data));
    }

    public void setAttribute(String name, char data) {
        super.setAttribute(name, Character.toString(data));
    }

    public void setAttribute(String name, Date data) {
        super.setAttribute(name, XmlElement.format.format(data));
    }

    public void setAttribute(String name, double data) {
        super.setAttribute(name, Double.toString(data));
    }

    public void setAttribute(String name, float data) {
        super.setAttribute(name, Float.toString(data));
    }

    public void setAttribute(String name, int data) {
        super.setAttribute(name, Integer.toString(data));
    }

    public void setAttribute(String name, long data) {
        super.setAttribute(name, Long.toString(data));
    }

    public void setAttribute(String name, short data) {
        super.setAttribute(name, Short.toString(data));
    }

    public void write(File file) throws IOException {
        this.write(new FileWriter(file));
    }

    public void write(OutputStream out) throws IOException {
        this.write(new OutputStreamWriter(out));
    }

    public void write(Writer writer) throws IOException {
        Document doc = XmlDocument.builder.newDocument();
        Node node = doc.importNode(this.getElement(), true);
        doc.appendChild(node);
        XmlDocument.toXmlDocument(doc).write(writer);
    }

    public static Element toElement(XmlElement e) {
        return e.getElement();
    }

    public static XmlElement toXmlElement(Element e) {
        return new XmlElement(e);
    }

    public static List<Element> toElementList(List<XmlElement> list) {
        List<Element> x = new LinkedList<Element>();
        for (XmlElement e : list) {
            x.add(XmlElement.toElement(e));
        }
        return x;
    }

    public static List<XmlElement> toXmlElementList(List<Element> list) {
        List<XmlElement> x = new LinkedList<XmlElement>();
        for (Element e : list) {
            x.add(XmlElement.toXmlElement(e));
        }
        return x;
    }

    @Override
    public int compareTo(Element o) {
        if (o == null) {
            return 0;
        }
        return this.getNodeName().compareTo(o.getNodeName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof XmlElement) {
            return this.getElement().equals(((XmlElement) obj).getElement());
        }
        if (obj instanceof Element) {
            return this.getElement().equals(obj);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getElement().hashCode();
    }

    @Override
    public String toString() {
        try {
            StringWriter writer = new StringWriter();
            this.write(writer);
            return writer.toString();
        } catch (IOException e) {
            SysLogger.getLogger().warn(e.getMessage(), e);
            return super.toString();
        }
    }

    protected Log getLogger() {
        return SysLogger.getLogger();
    }

    static {
        XmlElement.format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
    }
}
