package com.oroad.stxx.transform.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.MessageResources;
import com.oroad.stxx.util.NoDocumentFilter;
import com.oroad.stxx.util.StxxProperties;
import com.oroad.stxx.util.XMLConsumer;
import com.oroad.stxx.xform.DOMForm;
import com.oroad.stxx.xform.JDOMForm;
import com.oroad.stxx.xform.XMLForm;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.SAXOutputter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 *  Serializes objects by firing SAX events
 *
 *@author    Don Brown
 *@author    <a href="mailto:silvester@gnu.kicks-ass.org">Silvester van der Bijl
 *      </a>
 */
public class DefaultSAXSerializer implements SAXSerializer {

    /**   Whether to serialize request parameters as comma-separated values */
    protected static final String USE_CSV_PARAM_FORMAT_KEY = "stxx.useCSVParameterFormat";

    /** The logging instance */
    protected static final Log log = LogFactory.getLog(DefaultSAXSerializer.class);

    /**  Whether to serialize request parameters as comma-separated values */
    protected boolean useCSVParamFormat = false;

    /**  Stores whether Struts 1.1+ is present */
    protected boolean isStrutsPlugin = false;

    /**
     *  Initializes the serializer
     *
     *@param  props  The stxx properties
     */
    public void init(StxxProperties props) {
        useCSVParamFormat = props.getBoolean(USE_CSV_PARAM_FORMAT_KEY, false);
        isStrutsPlugin = props.isStrutsPlugin();
    }

    /**
     *  Serializes the named request parameter as an element.
     *
     *@param  request           The http request
     *@param  name              The name of the request parameter
     *@param  handler           The SAX event consumer
     *@exception  SAXException  If something goes wrong
     */
    public void serializeRequestParameter(HttpServletRequest request, String name, XMLConsumer handler) throws SAXException {
        String[] values = request.getParameterValues(name);
        if (useCSVParamFormat) {
            StringBuffer sb = new StringBuffer();
            sb.append(values[0]);
            for (int x = 1; x < values.length; x++) {
                sb.append(",");
                sb.append(values[x]);
            }
            sendElement("param", sb.toString(), "name", name, handler);
        } else {
            handler.startElement("", "param", "param", getAttributes("name", name));
            for (int x = 0; x < values.length; x++) {
                handler.startElement("", "value", "value", new AttributesImpl());
                handler.characters(values[x].toCharArray(), 0, values[x].length());
                handler.endElement("", "value", "value");
            }
            handler.endElement("", "param", "param");
        }
    }

    /**
     *  Serializes the named request attribute as an element.
     *
     *@param  request           The http request
     *@param  name              The name of the request attribute
     *@param  handler           The SAX event consumer
     *@exception  SAXException  If something goes wrong
     */
    public void serializeRequestAttribute(HttpServletRequest request, String name, XMLConsumer handler) throws SAXException {
        Object o = request.getAttribute(name);
        if (o != null && !(o instanceof ActionForm)) {
            handler.startElement("", "attribute", "attribute", getAttributes("name", name));
            serializeIt(o, name.replace(' ', '_'), handler, new Vector());
            handler.endElement("", "attribute", "attribute");
        }
    }

    /**
     *  Serializes the action error as an element.
     *
     *@param  request           The http request
     *@param  name              The property name
     *@param  error             The action error
     *@param  locale            The current locale
     *@param  res               The current resource bundle
     *@param  handler           The SAX event consumer
     *@exception  SAXException  If something goes wrong
     */
    public void serializeActionError(HttpServletRequest request, String name, ActionError error, Locale locale, MessageResources res, XMLConsumer handler) throws SAXException {
        AttributesImpl attr = new AttributesImpl();
        attr.addAttribute("", "property", "property", "CDATA", name);
        attr.addAttribute("", "name", "name", "CDATA", error.getKey());
        handler.startElement("", "error", "error", attr);
        Object[] values = error.getValues();
        if (values != null) {
            for (int x = 0; x < values.length; x++) {
                if (values[x] != null) {
                    sendElement("value", values[x].toString(), handler);
                }
            }
        }
        if (res != null) {
            sendElement("text", res.getMessage(locale, error.getKey(), values), handler);
        }
        handler.endElement("", "error", "error");
    }

    /**
     *  Serializes the action message as an element.
     *
     *@param  request           The http request
     *@param  name              The property name
     *@param  message           The action message
     *@param  locale            The current locale
     *@param  res               The current resource bundle
     *@param  handler           The SAX event consumer
     *@exception  SAXException  If something goes wrong
     */
    public void serializeActionMessage(HttpServletRequest request, String name, ActionMessage message, Locale locale, MessageResources res, XMLConsumer handler) throws SAXException {
        AttributesImpl attr = new AttributesImpl();
        attr.addAttribute("", "property", "property", "CDATA", name);
        attr.addAttribute("", "name", "name", "CDATA", message.getKey());
        handler.startElement("", "message", "message", attr);
        Object[] values = message.getValues();
        if (values != null) {
            for (int x = 0; x < values.length; x++) {
                if (values[x] != null) {
                    sendElement("value", values[x].toString(), handler);
                }
            }
        }
        if (res != null) {
            sendElement("text", res.getMessage(locale, message.getKey(), values), handler);
        }
        handler.endElement("", "message", "message");
    }

    /**
     *  Serializes the action form as an element.
     *
     *@param  request           The http request
     *@param  form              The action form
     *@param  handler           The SAX event consumer
     *@exception  SAXException  If something goes wrong
     */
    public void serializeActionForm(HttpServletRequest request, String name, ActionForm form, XMLConsumer handler) throws SAXException {
        String token = (String) request.getSession().getAttribute(Action.TRANSACTION_TOKEN_KEY);
        if (token != null) {
            sendElement("token", token, handler);
        }
        if (isStrutsPlugin && form instanceof DynaActionForm) {
            serializeIt(((DynaActionForm) form).getMap(), name, handler, new Vector());
        } else if (form instanceof XMLForm) {
            XMLForm frm = (XMLForm) form;
            sendElement("phase", frm.getPhase(), handler);
            if (frm instanceof DOMForm) {
                serializeIt(((DOMForm) frm).getXML(), frm.getName(), handler, new Vector());
            } else if (frm instanceof JDOMForm) {
                serializeIt(((JDOMForm) frm).getXML(), frm.getName(), handler, new Vector());
            }
        } else {
            serializeIt(form, name, handler, new Vector());
        }
    }

    /**
     *  Serializes the resource as an element.
     *
     *@param  request           The http request
     *@param  resources         The resources
     *@param  key               The resource key
     *@param  handler           The SAX event consumer
     *@exception  SAXException  If something goes wrong
     */
    public void serializeResource(HttpServletRequest request, Map resources, String key, XMLConsumer handler) throws SAXException {
        String value = (String) resources.get(key);
        if (key.startsWith(".")) {
            key = key.substring(1, key.length());
        }
        sendElement("key", value, "name", key, handler);
    }

    /**
     *  Gets an Attributes object that has the given attribute
     *
     *@param  name              The attribute name
     *@param  value             The attribute value
     *@return                   The attributes object
     *@exception  SAXException  If something goes wrong
     */
    protected Attributes getAttributes(String name, String value) throws SAXException {
        AttributesImpl attr = new AttributesImpl();
        attr.addAttribute("", name, name, "CDATA", value);
        return attr;
    }

    /**
     *  Sends an element that has the given attribute and text
     *
     *@param  eName             The name of the element
     *@param  eText             The text value 
     *@param  aName             The name of the attribute
     *@param  aValue            The value of the attribute
     *@param  handler           The SAX event consumer
     *@exception  SAXException  If something goes wrong
     */
    protected void sendElement(String eName, String eText, String aName, String aValue, XMLConsumer handler) throws SAXException {
        handler.startElement("", eName, eName, getAttributes(aName, aValue));
        if (eText != null) {
            handler.characters(eText.toCharArray(), 0, eText.length());
        }
        handler.endElement("", eName, eName);
    }

    /**
     *  Sends an element that has the given text
     *
     *@param  eName             The name of the element
     *@param  eText             The text value
     *@param  handler           The SAX event consumer
     *@exception  SAXException  If something goes wrong
     */
    protected void sendElement(String eName, String eText, XMLConsumer handler) throws SAXException {
        handler.startElement("", eName, eName, new AttributesImpl());
        if (eText != null) {
            handler.characters(eText.toCharArray(), 0, eText.length());
        }
        handler.endElement("", eName, eName);
    }

    /**
     *  Recursive function to serialize objects to XML. Currently it will
     *  serialize Collections, maps, org.jdom.Document, org.jdom.Element, Array
     *  org.w3c.dom.Node, and javabeans. It maintains a stack of objects serialized already in the
     *  current functioncall. This is used to avoid looping (stack overflow) of
     *  circular linked objects.
     *
     *@param  bean              The object you want serialized.
     *@param  name              The name of the object, used for element
     *      &lt;name/&gt;
     *@param  handler           XMLConsumer
     *@param  stack             Vector of objects we're serializing since the
     *      first calling of this function (to prevent looping on circular
     *      references).
     *@exception  SAXException  If something goes wrong
     */
    protected void serializeIt(Object bean, String name, XMLConsumer handler, Vector stack) throws SAXException {
        if ((bean != null) && (stack.contains(bean))) {
            if (log.isInfoEnabled()) {
                log.info("Circular reference detected, not serializing object: " + name);
            }
            return;
        } else if (bean != null) {
            stack.add(bean);
        }
        if (bean == null) {
            return;
        }
        String clsName = bean.getClass().getName();
        if (!"org.jdom.Element".equals(clsName)) {
            handler.startElement("", name, name, new AttributesImpl());
        }
        if (bean instanceof Collection) {
            Collection col = (Collection) bean;
            for (Iterator i = col.iterator(); i.hasNext(); ) {
                serializeIt(i.next(), "value", handler, stack);
            }
        } else if (bean instanceof Map) {
            Map map = (Map) bean;
            for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
                Object key = i.next();
                Object Objvalue = map.get(key);
                if (Objvalue instanceof DynaActionForm) {
                    serializeIt(((DynaActionForm) Objvalue).getMap(), (String) key, handler, new Vector());
                } else {
                    serializeIt(Objvalue, key.toString(), handler, stack);
                }
            }
        } else if (bean.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(bean); i++) {
                serializeIt(Array.get(bean, i), "arrayitem", handler, stack);
            }
        } else if (bean instanceof org.w3c.dom.Node) {
            outputNode((org.w3c.dom.Node) bean, handler);
        } else {
            if ("org.jdom.Document".equals(clsName)) {
                outputJDOM((Document) bean, handler);
            } else if ("org.jdom.Element".equals(clsName)) {
                outputJDOM((Element) bean, handler);
            } else if (clsName != null && clsName.startsWith("org.apache.struts")) {
            } else if (clsName != null && clsName.startsWith("com.oroad.stxx.action")) {
            } else if (clsName != null && clsName.startsWith("com.oroad.stxx.util")) {
            } else if (clsName != null && clsName.startsWith("com.oroad.stxx.transform")) {
            } else if (clsName.startsWith("java.lang")) {
                handler.characters(bean.toString().toCharArray(), 0, bean.toString().length());
            } else {
                try {
                    BeanInfo info = Introspector.getBeanInfo(bean.getClass());
                    PropertyDescriptor[] props = info.getPropertyDescriptors();
                    for (int i = 0; i < props.length; i++) {
                        Class t = props[i].getPropertyType();
                        String n = props[i].getName();
                        Method m = props[i].getReadMethod();
                        if (m != null) {
                            serializeIt(m.invoke(bean, null), n, handler, stack);
                        }
                    }
                } catch (Exception e) {
                    log.error(e, e);
                    throw new SAXException(e.getMessage());
                }
            }
        }
        if (!"org.jdom.Element".equals(clsName)) {
            handler.endElement("", name, name);
        }
        stack.remove(bean);
    }

    /**
     *  Serializes a JDOM document
     *
     *@param  e        The element
     *@param  handler  The SAX event consumer
     */
    protected void outputJDOM(Document d, XMLConsumer handler) {
        NoDocumentFilter filter = new NoDocumentFilter(handler);
        SAXOutputter out = new SAXOutputter(filter);
        out.setReportNamespaceDeclarations(true);
        try {
            out.output(d);
        } catch (Exception ex) {
            log.error("Unable to serialize JDOM document", ex);
        }
    }

    /**
     *  Serializes a JDOM element
     *
     *@param  e        The element
     *@param  handler  The SAX event consumer
     */
    protected void outputJDOM(Element e, XMLConsumer handler) {
        NoDocumentFilter filter = new NoDocumentFilter(handler);
        SAXOutputter out = new SAXOutputter(filter);
        out.setReportNamespaceDeclarations(true);
        ArrayList lst = new ArrayList();
        lst.add(e);
        try {
            out.output(lst);
        } catch (Exception ex) {
            log.error("Unable to serialize JDOM element", ex);
        }
    }

    /**
     *  Serializes a w3c DOM node
     *
     *@param n         The node
     *@param handler   The SAX event consumer
     */
    protected void outputNode(Node n, XMLConsumer handler) {
        NoDocumentFilter docFilter = new NoDocumentFilter(handler);
        Transformer trans = null;
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            trans = factory.newTransformer();
        } catch (TransformerException ex) {
            log.error(ex, ex);
        }
        try {
            trans.transform(new DOMSource(n), new SAXResult(docFilter));
        } catch (TransformerException ex) {
            log.error(ex, ex);
        }
    }
}
