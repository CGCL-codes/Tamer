package org.xito.blx;

import java.io.*;
import java.net.*;
import javax.xml.parsers.*;
import org.xito.boot.util.Base64;
import org.w3c.dom.*;

/**
 *
 * @author  drichan
 * @version
 */
public class SerializeObjectAdapter implements BLXObject {

    public static final String NODE_NAME = "javaobject";

    private boolean dirty_flag = true;

    private Serializable dataObject;

    private static DocumentBuilder docBuilder;

    private BLXElement blxElement;

    private BLXHelper blxHelper;

    /**
    * Creates a BLX object that wraps a Serializable object. This class
    * enables any java Serializable object to be stored in a BLX Document
    */
    public SerializeObjectAdapter() {
        blxHelper = new BLXHelper(this);
    }

    /**
    * Creates a BLX object that wraps a Serializable object. This class
    * enables any java Serializable object to be stored in a BLX Document
    * @param obj Object that should be serialized into the XML
    */
    public SerializeObjectAdapter(Serializable obj) {
        this();
        setObject(obj);
    }

    /**
    * Set the Object that this adapter will serialize
    */
    public void setObject(Serializable obj) {
        dataObject = obj;
        dirty_flag = true;
    }

    /**
    * Get the Object that this adapter contains
    */
    public Object getObject() throws ClassNotFoundException {
        if (dataObject != null) return dataObject;
        if (blxElement != null) return getObjectFromXML();
        return null;
    }

    /**
    * Get the XML Data associated with this Object. The XML Data should is a single element
    * that this object uses to persist its state
    * @return the XML Data Element for this Component
    */
    public Element getDataElement() {
        byte _data[] = null;
        String _encodedData = null;
        try {
            ByteArrayOutputStream _byte_out = new ByteArrayOutputStream();
            ObjectOutputStream _out = new ObjectOutputStream(_byte_out);
            _out.writeObject(dataObject);
            _data = _byte_out.toByteArray();
            _out.close();
            _byte_out.close();
        } catch (IOException _exp) {
            _exp.printStackTrace();
            _data = null;
        }
        _encodedData = Base64.encode(_data);
        Document _doc = docBuilder.newDocument();
        Element _element = _doc.createElement(NODE_NAME);
        _element.setAttribute(BLXElement.BLX_CLASS_NAME_ATTR, dataObject.getClass().getName());
        try {
            BLXExtClassLoader _loader = (BLXExtClassLoader) dataObject.getClass().getClassLoader();
            String _extName = null;
            String _extVersion = null;
            String _extHREF = null;
            if (_extName != null) _element.setAttribute(BLXElement.BLX_EXT_NAME_ATTR, _extName);
            if (_extVersion != null) _element.setAttribute(BLXElement.BLX_EXT_VERSION_ATTR, _extName);
            if (_extHREF != null) _element.setAttribute(BLXElement.BLX_EXT_HREF_ATTR, _extHREF);
        } catch (ClassCastException _exp) {
            Package _package = dataObject.getClass().getPackage();
            String _extName = _package.getSpecificationTitle();
            if (_extName != null) _element.setAttribute(BLXElement.BLX_EXT_NAME_ATTR, _extName);
        }
        CDATASection _cdata = _doc.createCDATASection(_encodedData);
        _element.appendChild(_cdata);
        return _element;
    }

    /**
    * Get the Object from an Element
    * @param Element
    * @return Object
    */
    protected Object getObjectFromXML() throws ClassNotFoundException {
        String _encodedData = null;
        Element e = blxElement.getDataElement();
        if (e.getNodeName() != NODE_NAME) {
            NodeList _childNodes = e.getChildNodes();
            e = null;
            for (int i = 0; i < _childNodes.getLength(); i++) {
                Node _child = _childNodes.item(i);
                if (_child.getNodeName().equals(NODE_NAME)) {
                    e = (Element) _child;
                    break;
                }
            }
        }
        if (e == null) return null;
        String _extHREF = e.getAttribute(BLXElement.BLX_EXT_HREF_ATTR);
        String _clsName = e.getAttribute(BLXElement.BLX_CLASS_NAME_ATTR);
        BLXExtClassLoader _loader = null;
        NodeList _list = e.getChildNodes();
        for (int i = 0; i < _list.getLength(); i++) {
            Node _child = _list.item(i);
            if (_child.getNodeType() == Node.CDATA_SECTION_NODE) {
                String _value = ((CDATASection) _child).getData();
                _encodedData = new String(_value.getBytes());
                break;
            }
        }
        if (_encodedData == null) return null;
        if (_loader != null && _encodedData != null) {
            byte _data[] = Base64.decode(_encodedData);
            try {
                ByteArrayInputStream _in = new ByteArrayInputStream(_data);
                MyObjectInputStream _objIn = new MyObjectInputStream(_in, _loader);
                dataObject = (Serializable) _objIn.readObject();
                return dataObject;
            } catch (IOException _ioExp) {
                _ioExp.printStackTrace();
                throw new ClassNotFoundException(_clsName);
            }
        } else {
            throw new ClassNotFoundException(_clsName);
        }
    }

    /**
    * Get the BLX Element for this Component or Object.
    * This BLXElement will not have the objects Data Element Populated
    * @return the BLXElement object that describes this type of Component.
    */
    public BLXElement getBLXElement() {
        return blxHelper.getBLXObjectElement();
    }

    /**
    * Return true if this components state has changed in a way that
    * Requires the objects container to fetch new XML Data for the Object.
    * @return true if component has changed
    */
    public boolean isDirty() {
        return dirty_flag;
    }

    /**
    * Set the BLX Element for this Component or Object
    * This should only be called when the object is first being created. Which
    * would normally be directly after the default constructor has been called.
    * @param blxElement for this Component
    */
    public void setBLXElement(BLXElement blxElement) {
        blxHelper.setBLXElement(blxElement);
        dataObject = null;
        dirty_flag = false;
    }

    /** Get the BLX Object instance ID for this object or null if the object does not have
    * an Id.
    * @return id
    *
    */
    public String getBLXId() {
        return blxHelper.getBLXId();
    }

    /** Store the BLX Object. This will store the objects entire child state or
    * its nested children could use the optional IBLXStorageHandler
    * to persist each of its children.
    * @param allChildren true causes this object to call getDataElement on all its children false means
    *   only dirty children
    * @param IBLXStorageHandler child objects can optionally have their state stored in seperate
    *  documents using a Storage handler.
    * @return the XML Data Element for this Component
    *
    */
    public void store(boolean allChildren, BLXStorageHandler storageHandler) throws IOException {
    }

    /**
    *
    *
    *
    */
    private class MyObjectInputStream extends ObjectInputStream {

        ClassLoader loader;

        public MyObjectInputStream(InputStream pIn, ClassLoader pLoader) throws IOException {
            super(pIn);
            loader = pLoader;
        }

        protected Class resolveClass(ObjectStreamClass v) throws IOException, ClassNotFoundException {
            return Class.forName(v.getName(), false, loader);
        }
    }
}
