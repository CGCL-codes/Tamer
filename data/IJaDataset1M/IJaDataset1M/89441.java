package org.exist.xqj;

import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.xml.xquery.XQCommonHandler;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQItemType;
import org.exist.dom.QName;
import org.exist.xquery.XPathException;
import org.exist.xquery.value.BooleanValue;
import org.exist.xquery.value.DoubleValue;
import org.exist.xquery.value.FloatValue;
import org.exist.xquery.value.IntegerValue;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.NodeValue;
import org.exist.xquery.value.Type;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;

/**
 * @author Adam Retter <adam.retter@devon.gov.uk>
 * 
 */
public class XQItem implements javax.xml.xquery.XQItem {

    Item item;

    /**
	 * 
	 */
    public XQItem() {
        item = null;
    }

    public XQItem(Item item) {
        this.item = item;
    }

    public XQItem(boolean value, XQItemType type) throws XQException {
        item = new BooleanValue(value);
        item = convertTo(type);
    }

    public XQItem(byte value, XQItemType type) throws XQException {
        try {
            item = new IntegerValue(value, org.exist.xquery.value.Type.BYTE);
            item = convertTo(type);
        } catch (XPathException xpe) {
            throw new XQException("Unable to create XQItem from byte: " + xpe.getMessage());
        }
    }

    public XQItem(double value, XQItemType type) throws XQException {
        item = new DoubleValue(value);
        item = convertTo(type);
    }

    public XQItem(float value, XQItemType type) throws XQException {
        item = new FloatValue(value);
        item = convertTo(type);
    }

    public void close() throws XQException {
        item = null;
    }

    public boolean isClosed() {
        return item == null;
    }

    public String getAtomicValue() throws XQException {
        try {
            if (item != null) return item.atomize().getStringValue();
            return null;
        } catch (XPathException xpe) {
            throw new XQException(xpe.getMessage());
        }
    }

    public boolean getBoolean() throws XQException {
        BooleanValue b = (BooleanValue) convertTo(Type.BOOLEAN);
        return b.getValue();
    }

    public byte getByte() throws XQException {
        IntegerValue v = (IntegerValue) convertTo(Type.BYTE);
        return 0;
    }

    public double getDouble() throws XQException {
        DoubleValue d = (DoubleValue) convertTo(Type.DOUBLE);
        return d.getValue();
    }

    public float getFloat() throws XQException {
        FloatValue f = (FloatValue) convertTo(Type.FLOAT);
        return f.getValue();
    }

    public int getInt() throws XQException {
        try {
            IntegerValue i = (IntegerValue) convertTo(Type.INT);
            return i.getInt();
        } catch (XPathException xpe) {
            throw new XQException(xpe.getMessage());
        }
    }

    public String getItemAsString() throws XQException {
        return null;
    }

    public XQItemType getItemType() throws XQException {
        return null;
    }

    public long getLong() throws XQException {
        IntegerValue d = (IntegerValue) convertTo(Type.LONG);
        return d.getValue();
    }

    public Node getNode() throws XQException {
        try {
            NodeValue n = (NodeValue) item.convertTo(Type.NODE);
            return n.getNode();
        } catch (XPathException xpe) {
            throw new XQException(xpe.getMessage());
        }
    }

    public URI getNodeUri() throws XQException {
        try {
            NodeValue n = (NodeValue) item.convertTo(Type.NODE);
            Document doc = n.getOwnerDocument();
            if (doc != null) {
                String documentURI = null;
                try {
                    Method method = Document.class.getMethod("getDocumentURI", null);
                    documentURI = (String) method.invoke(n.getOwnerDocument(), null);
                } catch (NoSuchMethodException ex) {
                } catch (IllegalAccessException ex) {
                } catch (InvocationTargetException ex) {
                }
                if (documentURI != null) {
                    return new URI(documentURI);
                }
            }
            return null;
        } catch (XPathException xpe) {
            throw new XQException(xpe.getMessage());
        } catch (URISyntaxException use) {
            throw new XQException(use.getMessage());
        }
    }

    public Object getObject() throws XQException {
        return null;
    }

    public Object getObject(XQCommonHandler handler) throws XQException {
        return handler.toObject(this);
    }

    public short getShort() throws XQException {
        return 0;
    }

    public boolean instanceOf(XQItemType type) throws XQException {
        String prefix = type.getTypeName().getPrefix();
        String local = type.getTypeName().getLocalPart();
        try {
            if (item.getType() == Type.getType(new QName(local, null, prefix))) {
                return true;
            }
        } catch (XPathException xpe) {
            throw new XQException(xpe.getMessage());
        }
        return false;
    }

    public void writeItem(OutputStream os, Properties props) throws XQException {
    }

    public void writeItem(Writer ow, Properties props) throws XQException {
    }

    public void writeItemToSAX(ContentHandler saxHandler) throws XQException {
    }

    private Item convertTo(XQItemType type) throws XQException {
        try {
            return item.convertTo(Type.getType(new QName(type.getTypeName().getLocalPart(), null, type.getTypeName().getPrefix())));
        } catch (XPathException xpe) {
            throw new XQException("Could not convert value for item to: " + type.getTypeName().toString() + " " + xpe.getMessage());
        }
    }

    private Item convertTo(int type) throws XQException {
        try {
            return item.convertTo(type);
        } catch (XPathException xpe) {
            throw new XQException("Could not convert value for item to: " + Type.getTypeName(type) + " " + xpe.getMessage());
        }
    }
}
