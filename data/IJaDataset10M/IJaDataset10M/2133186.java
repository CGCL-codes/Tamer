package cookxml.core;

import java.util.*;
import org.w3c.dom.*;
import cookxml.core.exception.*;
import cookxml.core.interfaces.*;

/**
 * This class does the actual internal decoding.
 *
 * @author Heng Yuan
 * @version $Id: DecodeEngine.java 265 2007-06-10 18:47:06Z coconut $
 * @since CookXml 1.0
 */
public class DecodeEngine implements ExceptionHandler {

    /**
	 * the XML namespace for xmlns:... attributes.
	 * We need to ignore these since these are automatically processed.
	 */
    public static final String XML_NS = "http://www.w3.org/2000/xmlns/";

    public static DecodeEngine createDecodeEngine(CookXml cookXml, VarLookup varLookup, StringHook stringHook) {
        return new DecodeEngine(cookXml, varLookup, stringHook);
    }

    public static Object decode(Document document, CookXml cookXml, VarLookup varLookup, StringHook stringHook) throws CookXmlException {
        return decode(null, null, document, null, cookXml, varLookup, stringHook);
    }

    public static Object decode(String parentNS, String parentTag, Document document, Object parentObj, CookXml cookXml, VarLookup varLookup, StringHook stringHook) throws CookXmlException {
        DecodeEngine decodeEngine = createDecodeEngine(cookXml, varLookup, stringHook);
        decodeEngine.setDocument(document);
        return decodeEngine.decodeElement(parentNS, parentTag, document.getDocumentElement(), parentObj);
    }

    public static Object decode(Element elm, CookXml cookXml, VarLookup varLookup, StringHook stringHook) throws CookXmlException {
        return decode(null, null, elm, null, cookXml, varLookup, stringHook);
    }

    public static Object decode(String parentNS, String parentTag, Element elm, Object parentObj, CookXml cookXml, VarLookup varLookup, StringHook stringHook) throws CookXmlException {
        DecodeEngine decodeEngine = createDecodeEngine(cookXml, varLookup, stringHook);
        return decodeEngine.decodeElement(parentNS, parentTag, elm, parentObj);
    }

    /**
	 * the CookXml object which contains the taglibrary and id cache
	 */
    private final CookXml m_cookXml;

    /**
	 * a map of objects being created in binary relationship.
	 */
    private final Map m_parentMap = new HashMap();

    /**
	 * a handler cache.
	 */
    private final Map m_handlerMap = new HashMap();

    /**
	 * the handler for finding variables
	 */
    private final VarLookup m_varLookup;

    /**
	 * the special creator that has precedence in processing the current element.
	 * @since	CookXml 3.0
	 */
    private SpecialCreator m_specialCreator;

    /**
	 * the current element being decoded.
	 */
    private Element m_currentElm;

    /**
	 * the current namespace of the tag of being decoded
	 * @since	CookXml 3.0
	 */
    private String m_currentNS;

    /**
	 * the current object id
	 * @since	CookXml 3.0
	 */
    private String m_currentId;

    /**
	 * the current object variable
	 * @since	CookXml 3.0
	 */
    private String m_currentVar;

    /**
	 * the current tag of being decoded
	 */
    private String m_currentTag;

    /**
	 * the skip list of attributes
	 * @since	CookXml 3.0
	 */
    private List m_currentSkipList = new LinkedList();

    /**
	 * the current add action for this object.
	 * @since	CookXml 3.0
	 */
    private AddAction m_addAction;

    /**
	 * the hook that translate the attribute value
	 */
    private StringHook m_stringHook;

    /**
	 * this is to indicate the document root, which may or may not exist
	 */
    private Document m_document;

    /**
	 * this flag is used to indicate whether or not add the object to the parent after finishing
	 * paring the element
	 */
    private boolean m_doAdd;

    /**
	 * this flag is used to indicate whether or not process child elements of the current node
	 * @since	CookXml 2.0
	 */
    private boolean m_doProcessChildren;

    /**
	 * this stack is used by creators to hold temporary data which must be cleaned up
	 * by themselves after when they were finished.
	 *
	 * @since	CookXml 2.0
	 */
    private final Stack m_dataStack = new Stack();

    /**
	 * this property is used for creators to save cached objects.
	 * @since	CookXml 2.1
	 */
    private HashMap m_properties;

    /**
	 * this map is used to store the namespace prefixes.
	 * @since	CookXml 3.0.1
	 */
    private NameSpaceScope m_nsScope;

    /**
	 * this indicates that there is a namespace setting in the current element.
	 * @since	CookXml 3.0.1
	 */
    private boolean m_hasNSSetting;

    /**
	 * this list contains hooks which will be called when DecodeEngine is finished.
	 */
    private LinkedList m_cleanupList;

    private DecodeEngine(CookXml cookXml, VarLookup varLookup, StringHook stringHook) {
        m_cookXml = cookXml;
        m_varLookup = varLookup;
        m_stringHook = stringHook;
        m_specialCreator = cookXml.getTagLibrary().getSpecialCreator();
    }

    public DecodeEngine newInstance() {
        return new DecodeEngine(m_cookXml, m_varLookup, m_stringHook);
    }

    public Object decodeElement(Element elm) throws CookXmlException {
        return decodeElement(null, null, elm, null);
    }

    public Object decodeElement(String parentNS, String parentTag, Element elm, Object parentObj) throws CookXmlException {
        String ns = elm.getNamespaceURI();
        String tag;
        if (ns == null) tag = elm.getTagName(); else tag = elm.getLocalName();
        m_currentNS = ns;
        m_currentTag = tag;
        m_currentElm = elm;
        m_currentId = null;
        m_currentVar = null;
        m_currentSkipList.clear();
        m_doAdd = true;
        m_doProcessChildren = true;
        m_addAction = null;
        m_hasNSSetting = false;
        CookXml cookXml = m_cookXml;
        TagLibrary tagLibrary = cookXml.getTagLibrary();
        Object obj = null;
        Creator creator = null;
        if (m_specialCreator != null) creator = m_specialCreator.getCreator(this, parentNS, parentTag, elm, parentObj);
        if (creator != null) {
            try {
                obj = creator.create(parentNS, parentTag, elm, parentObj, this);
            } catch (Exception ex) {
                if (!(ex instanceof CreatorException) || ((CreatorException) ex).creator != creator) ex = new CreatorException(this, ex, creator, parentNS, parentTag, elm, parentObj);
                handleException(null, ex);
            }
        } else {
            creator = tagLibrary.getCreator(ns, tag);
            if (creator == null) {
                handleException(null, new NoCreatorException(this, tag, parentNS, parentTag, elm, parentObj));
                return null;
            }
            try {
                obj = creator.create(parentNS, parentTag, elm, parentObj, this);
            } catch (Exception ex) {
                if (!(ex instanceof CreatorException) || ((CreatorException) ex).creator != creator) ex = new CreatorException(this, ex, creator, parentNS, parentTag, elm, parentObj);
                handleException(null, ex);
            }
        }
        if (cookXml.getRootObject() == null) cookXml.setRootObject(obj);
        if (obj == null) return null;
        setParent(parentObj, obj);
        NamedNodeMap attributes = elm.getAttributes();
        int size = attributes.getLength();
        List skipList = m_currentSkipList;
        for (int i = 0; i < size; ++i) {
            try {
                Attr attr = (Attr) attributes.item(i);
                if (skipList.contains(attr)) continue;
                decodeAttribute(m_currentNS, m_currentTag, attr, obj);
            } catch (SetterException ex) {
                handleException(null, ex);
            }
        }
        ns = m_currentNS;
        tag = m_currentTag;
        String id = m_currentId;
        String var = m_currentVar;
        boolean hasNSSetting = m_hasNSSetting;
        if (id != null) m_cookXml.setId(id, ns, tag, obj);
        if (!(obj instanceof NoAdd) && m_doAdd) addChild(parentNS, parentTag, elm, parentObj, obj);
        if (m_doProcessChildren) {
            AddAction addAction = m_addAction;
            NodeList children = elm.getChildNodes();
            size = children.getLength();
            for (int i = 0; i < size; ++i) {
                Node node = children.item(i);
                if (node instanceof Element) decodeElement(ns, tag, (Element) node, obj);
            }
            m_addAction = addAction;
        }
        if (hasNSSetting) m_nsScope.exitScope();
        try {
            Object newObj = creator.editFinished(parentNS, parentTag, elm, parentObj, obj, this);
            if (id != null && newObj != obj) m_cookXml.setId(id, ns, tag, newObj);
            try {
                if (var != null) {
                    if (m_varLookup == null) throw new VarLookupException(this, null, m_varLookup, var, newObj, false);
                    m_varLookup.setVariable(var, newObj, this);
                }
            } catch (Exception ex) {
                if (!(ex instanceof VarLookupException) || ((VarLookupException) ex).varLookup != m_varLookup) ex = new VarLookupException(this, ex, m_varLookup, var, newObj, false);
                handleException(null, ex);
            }
            return newObj;
        } catch (Exception ex) {
            if (!(ex instanceof CookXmlException)) ex = new CookXmlException(null, this, null, ex);
            handleException(null, ex);
        }
        return obj;
    }

    private void decodeAttribute(String tagNS, String tag, Node attrib, Object obj) throws SetterException {
        String attrNS = attrib.getNamespaceURI();
        String attr;
        if (attrNS == null) attr = attrib.getNodeName(); else attr = attrib.getLocalName();
        String value = attrib.getNodeValue();
        if (XML_NS.equals(attrNS)) {
            if (attr.length() == 0) return;
            if (!m_hasNSSetting) {
                if (m_nsScope == null) m_nsScope = new NameSpaceScope();
                m_nsScope.newScope();
                m_hasNSSetting = true;
            }
            m_nsScope.addScope(attr, value);
            return;
        }
        if (attrNS == null) attrNS = tagNS;
        callSetter(attrNS, tag, attrNS, attr, obj, value);
    }

    public void callSetter(String ns, String tag, String attrNS, String attr, Object obj, Object value) throws SetterException {
        TagLibrary tagLibrary = m_cookXml.getTagLibrary();
        Setter setter = tagLibrary.getSetter(ns, tag, attrNS, attr);
        if (setter == null) {
            setter = tagLibrary.getSetter(ns, tag, attrNS, null);
            if (setter == null) {
                setter = tagLibrary.getSetter(ns, null, attrNS, attr);
                if (setter == null) {
                    setter = tagLibrary.getSetter(ns, null, attrNS, null);
                    if (setter == null) throw new NoSetterException(this, ns, tag, attrNS, attr, obj, value);
                }
            }
        }
        try {
            if (m_stringHook != null && value instanceof String) value = m_stringHook.getTranslatedString(tag, obj, attr, (String) value, this);
            setter.setAttribute(ns, tag, attrNS, attr, obj, value, this);
        } catch (Exception ex) {
            if (!(ex instanceof SetterException) || ((SetterException) ex).setter != setter) ex = new SetterException(this, ex, setter, ns, tag, attrNS, attr, obj, value);
            throw (SetterException) ex;
        }
    }

    /**
	 * This function calls addChildUnchecked and calls ExceptionHandler to handle any exceptions generated.
	 *
	 * @param   parentNS
	 *          the name space of the parentTag.
	 * @param	parentTag
	 * 			parentTag of the parent element.
	 * @param	elm
	 * 			the current element.
	 * @param	parentObj
	 * 			the parent object.
	 * @param	childObj
	 * 			the current object to be added to the parent.
	 * @throws	CookXmlException
	 *          the exception is thrown when errors occur.
	 */
    public void addChild(String parentNS, String parentTag, Element elm, Object parentObj, Object childObj) throws CookXmlException {
        if (parentObj == null || parentTag == null || parentTag.length() == 0) return;
        if (m_addAction != null) {
            m_addAction.addChild(this, parentNS, parentTag, elm, parentObj, childObj);
            return;
        }
        TagLibrary tagLibrary = m_cookXml.getTagLibrary();
        Adder adder = tagLibrary.getAdder(parentNS, parentTag);
        if (adder == null) {
            adder = tagLibrary.getAdder(parentNS, null);
            if (adder == null) {
                handleException(null, new NoAdderException(this, parentNS, parentTag, parentObj, childObj));
                return;
            }
        }
        Exception newEx;
        try {
            if (adder.add(parentNS, parentTag, parentObj, childObj, this)) return;
            newEx = new AdderException(this, null, adder, parentNS, parentTag, parentObj, childObj);
        } catch (Exception ex) {
            if (!(ex instanceof AdderException) || ((AdderException) ex).adder != adder) newEx = new AdderException(this, ex, adder, parentNS, parentTag, parentObj, childObj); else newEx = ex;
        }
        handleException(null, newEx);
    }

    public CookXml getCookXml() {
        return m_cookXml;
    }

    public Object getParent(Object child) {
        return m_parentMap.get(child);
    }

    /**
	 * returns the current DOM element being decoded.
	 *
	 * @return	the current DOM element being decoded.
	 */
    public Element getCurrentElement() {
        return m_currentElm;
    }

    /**
	 * Set the parentObj childObj relationship by using childObj as the key for
	 * lookup its parentObj.
	 *
	 * @param	parentObj
	 *			the parentObj object.
	 * @param	childObj
	 *			the childObj object.
	 */
    public void setParent(Object parentObj, Object childObj) {
        m_parentMap.put(childObj, parentObj);
    }

    /**
	 * returns the variable lookup handler.
	 *
	 * @return	the variable lookup handler
	 */
    public VarLookup getVarLookup() {
        return m_varLookup;
    }

    public Object getVariable(String name) throws VarLookupException {
        if (m_varLookup == null) return null;
        return m_varLookup.getVariable(name, this);
    }

    public void setVariable(String name, Object value) throws VarLookupException {
        if (m_varLookup == null) return;
        m_varLookup.setVariable(name, value, this);
    }

    /**
	 * retrieve a handler from cache using the key pair.  It is usually the
	 * case where the handler for the same name and attribute use the same
	 * handler function.
	 *
	 * @param   ns
	 *          the namespace of the tag.
	 * @param	tag
	 *          the tag name of the key.
	 * @param	type
	 * 			the type of the handler
	 * @param	attr
	 *          the attribute name of the key.
	 * @param	cl
	 * 			the class name for the value.
	 * @return	a handler from cache.
	 */
    public Handler getHandler(String ns, String tag, char type, String attr, Class cl) {
        return (Handler) m_handlerMap.get(ns + ':' + tag + '/' + type + attr + '/' + cl);
    }

    /**
	 * save a handler into cache using the key pair.
	 *
	 * @param   ns
	 *          the namespace of the tag.
	 * @param	tag
	 *          the tag name of the key.
	 * @param	attr
	 *          the attribute name of the key.
	 * @param	type
	 * 			the type of the handler
	 * @param	cl
	 * 			the class name for the value.
	 * @param	handler
	 *          the handler to be put in the cache.
	 */
    public void setHandler(String ns, String tag, char type, String attr, Class cl, Handler handler) {
        m_handlerMap.put(ns + ':' + tag + '/' + type + attr + '/' + cl, handler);
    }

    public String getCurrentTag() {
        return m_currentTag;
    }

    public void setCurrentTag(String tag) {
        m_currentTag = tag;
    }

    public void setCurrentTag(String ns, String tag) {
        m_currentNS = ns;
        m_currentTag = tag;
    }

    public void setCurrentId(String id) {
        m_currentId = id;
    }

    public void setCurrentVar(String var) {
        m_currentVar = var;
    }

    public void addCurrentSkipList(Attr attr) {
        m_currentSkipList.add(attr);
    }

    public StringHook getStringHook() {
        return m_stringHook;
    }

    public void setStringHook(StringHook stringHook) {
        m_stringHook = stringHook;
    }

    public Document getDocument() {
        return m_document;
    }

    public void setDocument(Document document) {
        m_document = document;
    }

    /**
	 * Push data onto stack.
	 *
	 * @since	CookXml 2.0
	 * @param	data
	 *			data to be pushed onto stack.
	 */
    public void pushData(Object data) {
        m_dataStack.push(data);
    }

    /**
	 * Pop the data off stack.
	 *
	 * @since	CookXml 2.0
	 * @return	data popped.
	 */
    public Object popData() {
        if (m_dataStack.size() == 0) return null;
        return m_dataStack.pop();
    }

    /**
	 * Peek the data on stack.
	 *
	 * @since	CookXml 2.0
	 * @return	data peeked.
	 */
    public Object peekData() {
        if (m_dataStack.size() == 0) return null;
        return m_dataStack.peek();
    }

    /**
	 * this function is mainly for the creators to tell the DecodeEngine not to add the object
	 * to the parent while it is being created.
	 *
	 * @since	CookXml 2.0
	 * @param	b	whether or not the add the object to the parent.
	 */
    public void setDoAdd(boolean b) {
        m_doAdd = b;
    }

    /**
	 * This function set the current AddAction for addChild call.
	 *
	 * @since	CookXml 3.0
	 * @param	addAction
	 *			the current AddAction should be.
	 */
    public void setAddAction(AddAction addAction) {
        m_addAction = addAction;
    }

    /**
	 * this function is mainly for the creators to tell the DecodeEngine not to process child
	 * nodes of the current one.
	 *
	 * @since	CookXml 2.0
	 * @param	b	whether or not the process further down the DOM hierachy.
	 */
    public void setDoProcessChildren(boolean b) {
        m_doProcessChildren = b;
    }

    /**
	 * A short hand for calling CookXml.getExceptionHandler and then call it to handle
	 * the exception.
	 * @since	CookXml 2.0
	 * @param	msg
	 *			the message for the exception
	 * @param	ex
	 *			the exception to be handled
	 * @throws	CookXmlException
	 *			if an error cannot be handled
	 */
    public void handleException(String msg, Exception ex) throws CookXmlException {
        getCookXml().getExceptionHandler().handleException(msg, ex);
    }

    /**
	 * Set data for a given key.  This feature is to allow creators to cache certain data.
	 *
	 * @since	CookXml 2.1
	 * @param	key
	 *			key of the property entry
	 * @param	value
	 *			value of the property entry
	 * @return	old value for the given key
	 */
    public Object setProperty(String key, Object value) {
        HashMap properties = m_properties;
        if (properties == null) {
            properties = new HashMap();
            m_properties = properties;
        }
        Object oldValue = properties.get(key);
        properties.put(key, value);
        return oldValue;
    }

    /**
	 * Get data for a given key.
	 *
	 * @since	CookXml 2.1
	 * @param	key
	 *			key of the property entry
	 * @return	the value for the given key
	 */
    public Object getProperty(String key) {
        HashMap properties = m_properties;
        if (properties == null) return null;
        return properties.get(key);
    }

    /**
	 * Add a CleanupHook for cleaning up work after DecodeEngine finishes,
	 * if the hook was not yet in the hook list.
	 * @param   hook
	 *          the CleanupHook to be added.
	 * @since CookXml 2.5
	 */
    public void addCleanupHook(CleanupHook hook) {
        if (hook == null) return;
        if (m_cleanupList == null) m_cleanupList = new LinkedList();
        if (!m_cleanupList.contains(hook)) m_cleanupList.add(hook);
    }

    /**
	 * Removes a CleanupHook for cleaning up work after DecodeEngine finishes.
	 * @param   hook
	 *          the CleanupHook to be remove.
	 * @since CookXml 2.5
	 */
    public void removeCleanupHook(CleanupHook hook) {
        if (m_cleanupList != null) m_cleanupList.remove(hook);
    }

    /**
	 * This function is to be called by CookXml after DecodeEngine finishes decoding
	 * XML.
	 *
	 * @throws  CleanupException
	 *          This exception is thrown when one of the CleanupHook encountered an error.
	 * @since CookXml 2.5
	 */
    public void cleanup() throws CleanupException {
        if (m_cleanupList != null) {
            for (Iterator iter = m_cleanupList.iterator(); iter.hasNext(); ) {
                CleanupHook hook = ((CleanupHook) iter.next());
                try {
                    hook.cleanup(this);
                } catch (CleanupException ex) {
                    handleException(null, ex);
                } catch (Exception ex) {
                    handleException(null, new CleanupException(this, ex, hook));
                }
            }
        }
    }

    /**
	 * Get the namespace associated with the namespace.
	 *
	 * @param   prefix
	 *          the namespace prefix.
	 * @return  the namespace associated with the prefix.  null if not found.
	 * @since CookXml 3.0.1
	 */
    public String getPrefixNameSpace(String prefix) {
        if (m_nsScope == null) return null;
        return m_nsScope.getPrefixNameSpace(prefix);
    }
}
