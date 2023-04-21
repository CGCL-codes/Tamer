package org.docflower.xml;

import java.text.MessageFormat;
import java.util.*;
import javax.xml.namespace.QName;
import javax.xml.xpath.*;
import org.apache.ws.commons.schema.*;
import org.docflower.consts.DocFlowerXmlConsts;
import org.docflower.util.*;
import org.docflower.util.value.*;
import org.docflower.xml.typeinfo.TypeInfo;
import org.w3c.dom.*;

/**
 * This is utility class used to build the XML DOM by the XML Schema. There can
 * be used some parameters and schema extensions through the schema annotations
 * to customize building process. See the DOMBuilder constants for parameter
 * details and http://docflower.org/schema/extension namespace for the XML
 * Schema annotation's extensions.
 */
public class DOMBuilder {

    /**
	 * No limit for deep level of creating nodes.
	 */
    public static final int DOMB_DEEP_LEVEL_UNLIMITED = -1;

    /**
	 * Defines how many levels deep generate the nodes. -1 means no limit.
	 */
    public static final String DOMB_DEEP_LEVEL = "#DOMB_DEEP_LEVEL";

    /**
	 * Defines the QName root element name. If schema contains more then one
	 * possible root elements you can specify this parameter to choose one among
	 * them. This parameter is only used when rootNode is null and means that
	 * user wants to create completely new DOM. If this parameter is null the
	 * first root element will be used.
	 */
    public static final String DOMB_ROOT_ELEMENT_NAME = "#DOMB_ROOT_ELEMENT_NAME";

    /**
	 * This parameter is used to override some of the types by its extensions
	 * (derived types). It uses the xsi:type attribute to override the base type
	 * and generate the elements from the derived types also. Any encountered
	 * base type defined as QName key of this map will be substituted by its
	 * derived type derived as QName value.
	 */
    public static final String DOMB_ADDED_NODE_INFO_LIST = "#DOMB_ADDED_NODE_INFO_LIST";

    /**
	 * Can be used in conjunction with the ext:nodeSet XML Schema annotation
	 * extension from the http://docflower.org/schema/extension namespace. If
	 * this list is defined only elements which has such nodeSets will be added
	 * into the generated DOM.
	 */
    public static final String DOMB_REQUESTED_SETS = "#DOMB_REQUESTED_SETS";

    /**
	 * If this parameter is true than only first level's elements and elements
	 * with MaxOccurs == 1 of the added subtree will be generated into the DOM.
	 */
    public static final String DOMB_NO_ARRAY_ELEMENTS = "#DOMB_NO_ARRAY_ELEMENTS";

    /**
	 * Defines the existed siblings element the new element will be added after.
	 */
    public static final String DOMB_BEFORE_SIBLING_NODE = "#DOMB_BEFORE_SIBLING_NODE";

    public static final String DOMB_ALL_NAMESPACES_AT_ROOT = "#DOMB_ALL_NAMESPACES_AT_ROOT";

    public static final String DOMB_META_NODES = "#DOMB_META_NODES";

    private Node rootNode;

    private List<Node> firstLevelOfAddedNodes;

    private Node beforeSiblingNode;

    private SchemasManager schemasManager;

    private SimpleNamespaceContext namespaceContext;

    private int deepLevel = DOMB_DEEP_LEVEL_UNLIMITED;

    private String[] requestedSets;

    private boolean noArrayElements;

    private XPathExpression nodeSetExtractor;

    private Map<String, Object> params;

    public DOMBuilder() {
        params = new HashMap<String, Object>();
    }

    public DOMBuilder(Node rootNode, SchemasManager schemasManager, SimpleNamespaceContext namespaceContext, Map<String, Object> params) {
        this.rootNode = rootNode;
        this.schemasManager = schemasManager;
        this.namespaceContext = namespaceContext;
        this.params = params;
    }

    /**
	 * Builds the XML DOM subtree using the schema.
	 * 
	 * @param rootNode
	 *            - root node. If it is Document it will build new DOM.
	 * @param schemasManager
	 *            - The XML Schema Manager used for building the tree
	 * @param params
	 *            - the parameters for the generation customization.
	 * @return list of nodes added as children of node where attached new
	 *         sub-tree.
	 */
    public static List<Node> buildSubTreeBySchema(Node rootNode, SchemasManager schemasManager, SimpleNamespaceContext namespaceContext, Map<String, Object> params) {
        DOMBuilder builder = new DOMBuilder(rootNode, schemasManager, namespaceContext, params);
        return builder.build();
    }

    /**
	 * Builds the XML DOM subtree.
	 * 
	 * @return The list of just added first level nodes.
	 */
    public List<Node> build() {
        Document doc;
        boolean findElementRightPosition;
        Node node;
        init();
        if (getRootNode() != null) {
            if (getRootNode().getNodeType() == Node.DOCUMENT_NODE) {
                doc = (Document) getRootNode();
                XMLUtils.clearDocument(doc);
                getNamespaceContext().update(doc);
                node = createRootNode(doc, namespaceContext, (QName) getParams().get(DOMB_ROOT_ELEMENT_NAME));
                deepLevel--;
                findElementRightPosition = false;
            } else {
                node = getRootNode();
                doc = node.getOwnerDocument();
                findElementRightPosition = true;
            }
            XmlSchemaElement somElement = DOMUtils.getSOMElement(node, getSchemasManager());
            doBuildSubTreeBySchema(doc, node, somElement, getNamespaceContext(), deepLevel, findElementRightPosition);
        }
        checkNamespaces(getNamespaceContext());
        return getFirstLevelOfAddedNodes();
    }

    private void checkNamespaces(SimpleNamespaceContext simpleNamespaceContext) {
        if (Boolean.TRUE.equals(getParams().get(DOMB_ALL_NAMESPACES_AT_ROOT))) {
            if ((getRootNode().getNodeType() == Node.DOCUMENT_NODE)) {
                simpleNamespaceContext.regenerateNamespacesInRoot((Document) getRootNode());
            } else {
                simpleNamespaceContext.regenerateNamespacesInRoot(getRootNode().getOwnerDocument());
            }
        }
    }

    private void init() {
        firstLevelOfAddedNodes = new ArrayList<Node>();
        requestedSets = ((String[]) (getParams().get(DOMB_REQUESTED_SETS)));
        noArrayElements = Boolean.TRUE.equals(getParams().get(DOMB_NO_ARRAY_ELEMENTS));
        beforeSiblingNode = (Node) getParams().get(DOMB_BEFORE_SIBLING_NODE);
        Integer deepLevelInt = ((Integer) (getParams().get(DOMB_DEEP_LEVEL)));
        this.deepLevel = (deepLevelInt != null) ? deepLevelInt.intValue() : DOMB_DEEP_LEVEL_UNLIMITED;
    }

    private void doBuildSubTreeBySchema(Document doc, Node node, XmlSchemaElement somElement, SimpleNamespaceContext namespaceContext, int deepLevel, boolean findElementRightPosition) {
        XmlSchemaType nodeType = installRealNodeType((Element) node, somElement, namespaceContext);
        putDefaultValue(somElement, nodeType, (Element) node);
        if (deepLevel != 0) {
            List<XmlSchemaElement> somChildren = DOMUtils.getSOMElementChildren(nodeType, getSchemasManager());
            for (XmlSchemaElement somChildElement : somChildren) {
                Element childNode = createElement(doc, node, namespaceContext, somChildElement, findElementRightPosition);
                if (childNode != null) {
                    doBuildSubTreeBySchema(doc, childNode, somChildElement, namespaceContext, deepLevel - 1, false);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private XmlSchemaType installRealNodeType(Element element, XmlSchemaElement somElement, SimpleNamespaceContext namespaceContext) {
        XmlSchemaType result;
        QName overridedType = null;
        Map<QName, QName> addedNodeInfoList = (Map<QName, QName>) getParams().get(DOMB_ADDED_NODE_INFO_LIST);
        if (addedNodeInfoList != null) {
            overridedType = addedNodeInfoList.get(somElement.getQName());
        }
        if (overridedType != null) {
            String prefix = namespaceContext.findPrefix(overridedType.getNamespaceURI(), element.getOwnerDocument());
            namespaceContext.checkPrefix(DocFlowerXmlConsts.NS_XML_INSTANCE_ATTR_TYPE_PREFIX, DocFlowerXmlConsts.NS_XML_INSTANCE, element.getOwnerDocument());
            element.setAttributeNS(DocFlowerXmlConsts.NS_XML_INSTANCE, DocFlowerXmlConsts.NS_XML_INSTANCE_ATTR_TYPE, prefix + ":" + overridedType.getLocalPart());
            Attr typeAttr = element.getAttributeNodeNS(DocFlowerXmlConsts.NS_XML_INSTANCE, DocFlowerXmlConsts.NS_XML_INSTANCE_ATTR_TYPE);
            typeAttr.setPrefix(DocFlowerXmlConsts.NS_XML_INSTANCE_ATTR_TYPE_PREFIX);
            result = DOMUtils.getRealXmlSchemaType(element, getSchemasManager(), namespaceContext);
            if (result == null) {
                throw new LowLevelException(MessageFormat.format(XmlMessages.getString("DOMBuilder.UnableFindDef"), overridedType.getNamespaceURI(), overridedType.getLocalPart(), element.getNamespaceURI(), element.getLocalName()));
            }
        } else {
            result = DOMUtils.getXmlSchemaType(somElement, getSchemasManager().getSchemaCollection());
        }
        return result;
    }

    private Node createRootNode(Document doc, SimpleNamespaceContext namespaceContext, QName rootElementName) {
        if (rootElementName != null) {
            XmlSchemaElement rootSchemaElem = getSchemasManager().getListOfSOMElements().get(rootElementName);
            if (rootSchemaElem != null) {
                return createElement(doc, doc, namespaceContext, rootSchemaElem, false);
            } else {
                throw new LowLevelException(MessageFormat.format(XmlMessages.getString("DOMBuilder.UnableFindSchema"), rootElementName));
            }
        }
        throw new LowLevelException(XmlMessages.getString("DOMBuilder.RootSchemaCantBeDetermined"));
    }

    private Element createElement(Document owner, Node node, SimpleNamespaceContext namespaceContext, XmlSchemaElement schemaElem, boolean findElementRightPosition) {
        boolean isFirstLevel = (findElementRightPosition) || (owner == node);
        if (isElementAllowed(schemaElem, isFirstLevel)) {
            String prefix = namespaceContext.findPrefix(schemaElem.getQName().getNamespaceURI(), owner);
            Element elem = owner.createElementNS(schemaElem.getQName().getNamespaceURI(), schemaElem.getQName().getLocalPart());
            elem.setPrefix(prefix);
            if (findElementRightPosition) {
                if (beforeSiblingNode != null) {
                    node.insertBefore(elem, beforeSiblingNode);
                } else {
                    node.insertBefore(elem, DOMUtils.findNextSibling(node, schemaElem, schemasManager));
                }
            } else {
                node.appendChild(elem);
            }
            if (isFirstLevel) {
                getFirstLevelOfAddedNodes().add(elem);
            }
            return elem;
        }
        return null;
    }

    private void putDefaultValue(XmlSchemaElement somElement, XmlSchemaType nodeType, Element element) {
        String defaultStringValue = SchemaExtensionsUtils.getExtPropertyByName(somElement, "defaultvalue");
        if (defaultStringValue == null) {
            TypeInfo typeInfo = getSchemasManager().getTypeRestrictions(nodeType);
            if ((typeInfo != null) && (typeInfo.getSimpleTypeName() != null) && (!"anyType".equals(typeInfo.getSimpleTypeName()))) {
                AbstractValueManager mgr = ValueFactory.getSingleton().getValueManager(typeInfo.getSimpleTypeName());
                defaultStringValue = mgr.getDefaultValueAsString(typeInfo);
            }
            if (defaultStringValue != null) {
                element.setTextContent(defaultStringValue);
            }
        }
    }

    /**
	 * Allow create node if there is empty list of requested set. Otherewise
	 * check in the schema info whether this node is allowed for creation. in
	 * the requested sets
	 * 
	 * @param schemaElem
	 *            - checked node element
	 * @return true if node is allowed according to its node set.
	 */
    private boolean isElementAllowed(XmlSchemaElement schemaElem, boolean firstLevel) {
        if (isNoArrayElements() && !firstLevel && (schemaElem.getMaxOccurs() != 1)) {
            return false;
        } else if ((getRequestedSets() == null) || (getRequestedSets().length < 1)) {
            return true;
        } else {
            XmlSchemaAppInfo appInfo = DOMUtils.getAppInfo(schemaElem);
            if ((appInfo != null) && (appInfo.getMarkup() != null)) {
                try {
                    NodeList allowedSets = (NodeList) getNodeSetExtractor().evaluate(appInfo.getMarkup(), XPathConstants.NODESET);
                    if (allowedSets.getLength() > 0) {
                        return xmlNodeValuesEqualsAtLeastOneOfTheStrings(allowedSets, getRequestedSets());
                    }
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private void initNodeXPath() {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xp = xPathFactory.newXPath();
        xp.setNamespaceContext(getSchemasManager().getSchemaNamespace());
        try {
            nodeSetExtractor = xp.compile(DocFlowerXmlConsts.SCHEMA_EXTENSION_PREFIX + ":nodeSets//" + DocFlowerXmlConsts.SCHEMA_EXTENSION_PREFIX + ":nodeSet");
        } catch (XPathExpressionException e) {
            throw new LowLevelException(XmlMessages.getString("DOMBuilder.UnableCompileNodeSetXPath"));
        }
    }

    private boolean xmlNodeValuesEqualsAtLeastOneOfTheStrings(NodeList allowedSets, String[] requestedSets) {
        for (int i = 0; i < allowedSets.getLength(); i++) {
            for (String requestedSet : requestedSets) {
                if (requestedSet.equals(allowedSets.item(i).getTextContent().trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    public SchemasManager getSchemasManager() {
        return schemasManager;
    }

    public void setSchemasManager(SchemasManager schemasManager) {
        if (this.schemasManager != schemasManager) {
            this.schemasManager = schemasManager;
            initNodeXPath();
        }
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public List<Node> getFirstLevelOfAddedNodes() {
        return firstLevelOfAddedNodes;
    }

    public boolean isNoArrayElements() {
        return noArrayElements;
    }

    public String[] getRequestedSets() {
        return requestedSets;
    }

    public XPathExpression getNodeSetExtractor() {
        return nodeSetExtractor;
    }

    public SimpleNamespaceContext getNamespaceContext() {
        return namespaceContext;
    }
}
