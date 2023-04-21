package de.fraunhofer.ipsi.ipsixq.datamodel.dom;

import de.fraunhofer.ipsi.ipsixq.datamodel.*;
import org.w3c.dom.*;
import de.fraunhofer.ipsi.util.Environment;
import de.fraunhofer.ipsi.xpathDatatypes.XS_String;
import de.fraunhofer.ipsi.xpathDatatypes.XS_UntypedAtomic;
import de.fraunhofer.ipsi.xquery.datamodel.Sequence;
import de.fraunhofer.ipsi.xquery.datamodel.XDMCastException;
import de.fraunhofer.ipsi.xquery.sequencetypes.BuiltinTypeEnum;
import java.util.StringTokenizer;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.apache.xerces.xs.XSSimpleTypeDefinition;

public class DM_ElementNodeDom extends DM_ElementNode implements DM_NodeDom {

    protected Element element;

    /**
	 * Constructor.
	 */
    public DM_ElementNodeDom(Element elem, Environment<String, String> env) {
        super(elem.getBaseURI(), new QName(DOMUtils.lookup(elem.getPrefix(), env = DOMUtils.expandEnv(elem, env), true), (elem.getLocalName() != null ? elem.getLocalName() : elem.getNodeName()), (elem.getPrefix() != null ? elem.getPrefix() : XMLConstants.DEFAULT_NS_PREFIX)), DOMUtils.extractElementType(elem), DOMUtils.isNilled(elem), env);
        this.element = elem;
    }

    /**
	 * Method children
	 *
	 * @return   a DM_Sequence
	 *
	 */
    public Sequence children() {
        if (children == null) {
            children = manager.newSequence();
            NodeList childs = element.getChildNodes();
            for (int i = 0; i < childs.getLength(); i++) {
                Node node = childs.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    DM_ElementNode elementNode = new DM_ElementNodeDom((Element) node, namespaceBindings());
                    try {
                        elementNode.setParent(this);
                    } catch (AlreadySetException e) {
                    }
                    children.add(elementNode);
                } else if (node.getNodeType() == Node.TEXT_NODE) {
                    DM_TextNode textNode = new DM_TextNodeDom((Text) node);
                    try {
                        textNode.setParent(this);
                    } catch (AlreadySetException e) {
                    }
                    children.add(textNode);
                } else if (node.getNodeType() == Node.COMMENT_NODE) {
                    DM_CommentNode textNode = new DM_CommentNodeDom((Comment) node);
                    try {
                        textNode.setParent(this);
                    } catch (AlreadySetException e) {
                    }
                    children.add(textNode);
                } else if (node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
                    DM_ProcessingInstructionNode textNode = new DM_ProcessingInstructionNodeDom((ProcessingInstruction) node);
                    try {
                        textNode.setParent(this);
                    } catch (AlreadySetException e) {
                    }
                    children.add(textNode);
                }
            }
            children.setImmutable();
        }
        return children;
    }

    /**
	 * Method attributes
	 *
	 * @return   a DM_Sequence
	 *
	 */
    public Sequence attributes() {
        if (attributes == null) {
            attributes = manager.newSequence();
            NamedNodeMap attrs = element.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                Attr node = (Attr) attrs.item(i);
                if (!((node.getPrefix() != null && node.getPrefix().equalsIgnoreCase("xmlns")) || node.getName().equalsIgnoreCase("xmlns"))) {
                    DM_AttributeNode attributeNode = new DM_AttributeNodeDom(node, namespaceBindings());
                    try {
                        attributeNode.setParent(this);
                    } catch (AlreadySetException e) {
                    }
                    attributes.add(attributeNode);
                }
            }
            attributes.setImmutable();
        }
        return attributes;
    }

    /**
	 * Method getDomElement
	 *
	 * @return   an Element
	 *
	 */
    public Node getDomNode() {
        return element;
    }

    /**
	 * Method typedValue
	 *
	 * @return   a Sequence
	 *
	 */
    public Sequence typedValue() {
        Sequence result = manager.newSequence();
        if (!(nilled().booleanValue() || children().isEmpty())) {
            String s = stringValue();
            if (typeName().equals(BuiltinTypeEnum.XS_Untyped.getName()) || typeName().equals(BuiltinTypeEnum.XS_AnySimpleType.getName())) {
                result.add(manager.newAtomicValue(XS_UntypedAtomic.valueOf(s)));
            } else {
                XSSimpleTypeDefinition typeDef = DOMUtils.getSimpleType(element);
                if (typeDef.getVariety() == XSSimpleTypeDefinition.VARIETY_LIST) {
                    typeDef = typeDef.getItemType();
                    StringTokenizer tok = new StringTokenizer(s, " ");
                    while (tok.hasMoreTokens()) {
                        try {
                            result.add(manager.castas(XS_String.valueOf(tok.nextToken()), types.newAtomicType(typeName(), typeDef), namespaceBindings()));
                        } catch (XDMCastException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                } else {
                    try {
                        result.add(manager.castas(XS_String.valueOf(s), types.newAtomicType(typeName(), typeDef), namespaceBindings()));
                    } catch (XDMCastException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }
        } else {
            result.add(manager.newAtomicValue(XS_UntypedAtomic.valueOf("")));
        }
        return result;
    }
}
