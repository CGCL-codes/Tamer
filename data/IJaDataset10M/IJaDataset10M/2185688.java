package nuts.core.oxm.adapter;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import nuts.core.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * AdapterFactory produces Node adapters for Java object types. Adapter classes
 * are generally instantiated dynamically via a no-args constructor and
 * populated with their context information via the AdapterNode interface.
 * 
 * This factory supports proxying of generic DOM Node trees, allowing arbitrary
 * Node types to be mixed together. You may simply return a Document or Node
 * type as an object property and it will appear as a sub-tree in the XML as
 * you'd expect. See #proxyNode().
 * 
 * Customization of the result XML can be accomplished by providing alternate
 * adapters for Java types. Adapters are associated with Java types through the
 * registerAdapterType() method.
 * 
 * The DateAdapter will use Date.getTime() to treat date as a long number.
 * 
 * <pre>
 * <date>2452345623456435</date>
 * </pre>
 * 
 * The StringAdapter (which is normally invoked only to adapt String values) is
 * a useful base for these kinds of customizations and can produce structured
 * XML output as well as plain text by setting its parseStringAsXML() property
 * to true.
 * 
 * See provided examples.
 */
public class AdapterFactory {

    private static Log log = LogFactory.getLog(AdapterFactory.class);

    private Map<Class, Class> adapterTypes = new HashMap<Class, Class>();

    public static final int CYCLE_DETECT_NOPROP = 1;

    public static final int CYCLE_DETECT_LENIENT = 2;

    public static final int CYCLE_DETECT_STRICT = 3;

    protected int cycleDetect = CYCLE_DETECT_NOPROP;

    protected boolean ignoreNullProperty = true;

    protected String dateFormat;

    protected PropertyFilter propertyFilter;

    /**
	 * @return the propertyFilter
	 */
    public PropertyFilter getPropertyFilter() {
        return propertyFilter;
    }

    /**
	 * @param propertyFilter the propertyFilter to set
	 */
    public void setPropertyFilter(PropertyFilter propertyFilter) {
        this.propertyFilter = propertyFilter;
    }

    /**
	 * @return the dateFormat
	 */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
	 * @param dateFormat the dateFormat to set
	 */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
	 * @return the ignoreNullProperty
	 */
    public boolean isIgnoreNullProperty() {
        return ignoreNullProperty;
    }

    /**
	 * @param ignoreNullProperty the ignoreNullProperty to set
	 */
    public void setIgnoreNullProperty(boolean ignoreNullProperty) {
        this.ignoreNullProperty = ignoreNullProperty;
    }

    /**
	 * @return the cycleDetect
	 */
    public int getCycleDetect() {
        return cycleDetect;
    }

    /**
	 * @param cycleDetect the cycleDetect to set
	 */
    public void setCycleDetect(int cycleDetect) {
        this.cycleDetect = cycleDetect;
    }

    /**
	 * Register an adapter type for a Java class type.
	 * 
	 * @param type the Java class type which is to be handled by the adapter.
	 * @param adapterType The adapter class, which implements AdapterNode.
	 */
    public void registerAdapterType(Class type, Class adapterType) {
        adapterTypes.put(type, adapterType);
    }

    /**
	 * Create a top level Document adapter for the specified Java object. The
	 * document will have a root element with the specified property name and
	 * contain the specified Java object content.
	 * 
	 * @param propertyName The name of the root document element
	 * @return Xml document
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
    public Document adaptDocument(String propertyName, Object propertyValue) throws IllegalAccessException, InstantiationException {
        return new SimpleAdapterDocument(this, null, propertyName, propertyValue);
    }

    protected boolean isCycleObject(AdapterNode node, Object value) {
        if (ClassUtils.isPrimitiveOrWrapper(value.getClass())) {
            return false;
        }
        while (node != null) {
            if (node.getNodeType() == Node.DOCUMENT_NODE) {
                break;
            }
            if (node.getPropertyValue() == value) {
                return true;
            }
            node = node.getParent();
        }
        return false;
    }

    protected AdapterNode emptyNode(AdapterNode parent, String propertyName) {
        return new StringAdapter(this, parent, propertyName, "");
    }

    protected String getNodePath(AdapterNode node) {
        StringBuilder sb = new StringBuilder();
        while (node != null) {
            if (node.getNodeType() == Node.DOCUMENT_NODE) {
                break;
            }
            sb.insert(0, node.getNodeName()).insert(0, '/');
            node = node.getParent();
        }
        return sb.toString();
    }

    protected boolean filterProperty(AdapterNode parent, String propertyName, Object value) {
        if (propertyFilter != null) {
            if (propertyFilter.apply(parent.getPropertyValue(), propertyName, value)) {
                if (log.isDebugEnabled()) {
                    log.debug("filter node: " + getNodePath(parent) + "/" + propertyName);
                }
                return true;
            }
        }
        return false;
    }

    /**
	 * Create an Node adapter for a child element. Note that the parent of the
	 * created node must be an AdapterNode, however the child node itself may be
	 * any type of Node.
	 * 
	 * @see #adaptDocument(String, Object )
	 */
    public Node adaptNode(AdapterNode parent, String propertyName, Object value) {
        if (value == null) {
            return ignoreNullProperty ? null : emptyNode(parent, propertyName);
        }
        if (filterProperty(parent, propertyName, value)) {
            return null;
        }
        if (log.isTraceEnabled()) {
            log.trace("adapt node: " + getNodePath(parent) + "/" + propertyName);
        }
        if (isCycleObject(parent, value)) {
            String msg = getNodePath(parent) + "/" + propertyName + " is a cycle in the hierarchy!";
            switch(cycleDetect) {
                case CYCLE_DETECT_STRICT:
                    throw new RuntimeException(msg);
                case CYCLE_DETECT_LENIENT:
                    log.warn(msg);
                    return emptyNode(parent, propertyName);
                case CYCLE_DETECT_NOPROP:
                default:
                    log.warn(msg);
                    return null;
            }
        }
        return getAdapterForValue(parent, propertyName, value);
    }

    /**
	 * Construct a proxy adapter for a value that is an existing DOM Node. This
	 * allows arbitrary DOM Node trees to be mixed in with our results. The
	 * proxied nodes are read-only and currently support only limited types of
	 * Nodes including Element, Text, and Attributes. (Other Node types may be
	 * ignored by the proxy and not appear in the result tree).
	 * <p/>
	 * // TODO: NameSpaces are not yet supported.
	 * <p/>
	 * This method is primarily for use by the adapter node classes.
	 */
    public AdapterNode proxyNode(AdapterNode parent, Node node) {
        if (node instanceof Document) node = ((Document) node).getDocumentElement();
        if (node == null) return null;
        if (node.getNodeType() == Node.ELEMENT_NODE) return new ProxyElementAdapter(this, parent, (Element) node);
        if (node.getNodeType() == Node.TEXT_NODE) return new ProxyTextNodeAdapter(this, parent, (Text) node);
        if (node.getNodeType() == Node.ATTRIBUTE_NODE) return new ProxyAttrAdapter(this, parent, (Attr) node);
        return null;
    }

    public NamedNodeMap proxyNamedNodeMap(AdapterNode parent, NamedNodeMap nnm) {
        return new ProxyNamedNodeMap(this, parent, nnm);
    }

    protected AdapterNode getAdapterForValue(AdapterNode parent, String propertyName, Object value) {
        Class valueType = value.getClass();
        Class adapterClass = adapterTypes.get(valueType);
        if (adapterClass == null) {
            if (value instanceof Document) {
                value = ((Document) value).getDocumentElement();
            }
            if (value instanceof Node) {
                return proxyNode(parent, (Node) value);
            }
            if (value instanceof String || value instanceof Number || value instanceof Class || ClassUtils.isPrimitiveOrWrapper(valueType)) {
                adapterClass = StringAdapter.class;
            } else if (value instanceof Date || value instanceof Calendar) {
                adapterClass = DateAdapter.class;
            } else if (valueType.isArray()) {
                adapterClass = ArrayAdapter.class;
            } else if (value instanceof Collection) {
                adapterClass = CollectionAdapter.class;
            } else if (value instanceof Map) {
                adapterClass = MapAdapter.class;
            } else {
                adapterClass = BeanAdapter.class;
            }
        }
        return constructAdapterInstance(adapterClass, parent, propertyName, value);
    }

    /**
	 * Create an instance of an adapter dynamically and set its context via the
	 * AdapterNode interface.
	 */
    protected AdapterNode constructAdapterInstance(Class adapterClass, AdapterNode parent, String propertyName, Object propertyValue) {
        try {
            AdapterNode adapterNode = (AdapterNode) adapterClass.newInstance();
            adapterNode.setAdapterFactory(this);
            adapterNode.setParent(parent);
            adapterNode.setPropertyName(propertyName);
            adapterNode.setPropertyValue(propertyValue);
            if (adapterNode instanceof DateAdapter) {
                ((DateAdapter) adapterNode).setDateFormat(dateFormat);
            }
            return adapterNode;
        } catch (Exception e) {
            throw new RuntimeException("Cannot adapt " + getNodePath(parent) + "/" + propertyName, e);
        }
    }
}
