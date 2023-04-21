package org.mobicents.protocols.xml.patch;

import java.util.Map;
import org.mobicents.protocols.xcap.diff.BuildPatchException;

/**
 * 
 * Builds XML Patch Ops (RFC 5261) patching instructions.
 * 
 * @author baranowb
 * @author martins
 * @param <D>
 *            the document type, defines what is the concrete type of XML
 *            documents used
 * @param <C>
 *            the patching instruction type
 * @param <E>
 *            the element type
 * @param <N>
 *            the node type
 */
public interface XmlPatchOperationsBuilder<D, C, E, N> {

    public static final String XML_PATCH_OPS_NAMESPACE = "urn:ietf:params:xml:schema:patch-ops";

    /**
	 * possible <add/> "pos" attribute values
	 */
    public enum Pos {

        prepend, before, after;

        /**
		 * @param attribute
		 * @return
		 */
        public static Pos fromString(String attribute) {
            if (attribute == null || attribute.equals("")) {
                return null;
            } else if (attribute.equals("after")) {
                return after;
            } else if (attribute.equals("before")) {
                return before;
            } else if (attribute.equals("prepend")) {
                return prepend;
            } else {
                throw new IllegalArgumentException("Wrong value of 'pos' attribute, present value is '" + attribute + "'.");
            }
        }
    }

    /**
	 * possible <remove/> "ws" attribute values
	 */
    public enum Ws {

        before, after, both;

        /**
		 * @param attribute
		 * @return
		 */
        public static Ws fromString(String attribute) {
            if (attribute.equals("before")) {
                return before;
            } else if (attribute.equals("before")) {
                return before;
            } else if (attribute.equals("both")) {
                return both;
            } else if (attribute == null || attribute.equals("")) {
                return null;
            } else {
                throw new IllegalArgumentException("Wrong value of 'ws' attribute, present value is '" + attribute + "'.");
            }
        }
    }

    /**
	 * Builds the patching instruction for the creation of an attribute.
	 * 
	 * @param sel
	 *            the xpath selector, which points to the added attribute.
	 * @param attrName
	 *            the attribute name
	 * @param attrValue
	 *            the attribute value
	 * @param namespaceBindings
	 *            namespace bindings for undeclared namespaces in type and sel
	 * @return
	 * @throws BuildPatchException
	 *             if an exception occurred while building the patch, the
	 *             concrete exception is the exception's cause.
	 */
    public C addAttribute(String sel, String attrName, String attrValue, Map<String, String> namespaceBindings) throws BuildPatchException;

    /**
	 * Builds the patching instruction for the creation of an element.
	 * 
	 * @param sel
	 *            the xpath selector, which points to the added element.
	 * @param element
	 *            the element
	 * @param namespaceBindings
	 *            namespace bindings for undeclared namespaces in the element
	 *            and sel
	 * @return
	 * @throws BuildPatchException
	 *             if an exception occurred while building the patch, the
	 *             concrete exception is the exception's cause.
	 */
    public C addElement(String sel, E element, Map<String, String> namespaceBindings) throws BuildPatchException;

    /**
	 * Builds the patching instruction for the creation of a node.
	 * 
	 * @param sel
	 *            the xpath selector, which points to the added node.
	 * @param pos
	 *            the pos attribute
	 * @param node
	 *            the node
	 * @param namespaceBindings
	 *            namespace bindings for undeclared namespaces in the node and
	 *            sel
	 * @return
	 * @throws BuildPatchException
	 *             if an exception occurred while building the patch, the
	 *             concrete exception is the exception's cause.
	 */
    public C addNode(String sel, Pos pos, N node, Map<String, String> namespaceBindings) throws BuildPatchException;

    /**
	 * Builds the patching instruction for the creation of a prefix namespace
	 * declaration.
	 * 
	 * @param sel
	 *            the xpath selector, which points to the added prefix namespace
	 *            declaration.
	 * @param namespacePrefix
	 *            the namespace prefix
	 * @param namespaceValue
	 *            the namespace value
	 * @param namespaceBindings
	 *            namespace bindings for undeclared namespaces in sel
	 * @return
	 * @throws BuildPatchException
	 *             if an exception occurred while building the patch, the
	 *             concrete exception is the exception's cause.
	 */
    public C addPrefixNamespaceDeclaration(String sel, String namespacePrefix, String namespaceValue, Map<String, String> namespaceBindings) throws BuildPatchException;

    /**
	 * Builds the patching instruction for the replacement of an attribute.
	 * 
	 * @param sel
	 *            the xpath selector, which points to the replaced attribute.
	 * @param attrValue
	 *            the attribute value
	 * @param namespaceBindings
	 *            namespace bindings for undeclared namespaces in sel
	 * @return
	 * @throws BuildPatchException
	 *             if an exception occurred while building the patch, the
	 *             concrete exception is the exception's cause.
	 */
    public C replaceAttribute(String sel, String attributeValue, Map<String, String> namespaceBindings) throws BuildPatchException;

    /**
	 * Builds the patching instruction for the replacement of an element.
	 * 
	 * @param sel
	 *            the xpath selector, which points to the replaced element.
	 * @param element
	 *            the element
	 * @param namespaceBindings
	 *            namespace bindings for undeclared namespaces in the element
	 *            and sel
	 * @return
	 * @throws BuildPatchException
	 *             if an exception occurred while building the patch, the
	 *             concrete exception is the exception's cause.
	 */
    public C replaceElement(String sel, E element, Map<String, String> namespaceBindings) throws BuildPatchException;

    /**
	 * Builds the patching instruction for the replacement of a node.
	 * 
	 * @param sel
	 *            the xpath selector, which points to the replaced node.
	 * @param node
	 *            the node
	 * @param namespaceBindings
	 *            namespace bindings for undeclared namespaces in the node and
	 *            sel
	 * @return
	 * @throws BuildPatchException
	 *             if an exception occurred while building the patch, the
	 *             concrete exception is the exception's cause.
	 */
    public C replaceNode(String sel, N node, Map<String, String> namespaceBindings) throws BuildPatchException;

    /**
	 * Builds the patching instruction for the replacement of a prefix namespace
	 * declaration.
	 * 
	 * @param sel
	 *            the xpath selector, which points to the replaced prefix
	 *            namespace declaration.
	 * @param namespaceValue
	 *            the namespace value
	 * @param namespaceBindings
	 *            namespace bindings for undeclared namespaces in sel
	 * @return
	 * @throws BuildPatchException
	 *             if an exception occurred while building the patch, the
	 *             concrete exception is the exception's cause.
	 */
    public C replacePrefixNamespaceDeclaration(String sel, String namespaceValue, Map<String, String> namespaceBindings) throws BuildPatchException;

    /**
	 * Builds the patching instruction for the removal of an attribute.
	 * 
	 * @param sel
	 *            the xpath selector, which points to the removed attribute.
	 * @param namespaceBindings
	 *            namespace bindings for undeclared namespaces in sel
	 * @return
	 * @throws BuildPatchException
	 *             if an exception occurred while building the patch, the
	 *             concrete exception is the exception's cause.
	 */
    public C removeAttribute(String sel, Map<String, String> namespaceBindings) throws BuildPatchException;

    /**
	 * Builds the patching instruction for the removal of an element.
	 * 
	 * @param sel
	 *            the xpath selector, which points to the removed element.
	 * @param ws
	 *            the ws.
	 * @param namespaceBindings
	 *            namespace bindings for undeclared namespaces in sel
	 * @return
	 * @throws BuildPatchException
	 *             if an exception occurred while building the patch, the
	 *             concrete exception is the exception's cause.
	 */
    public C removeElement(String sel, Ws ws, Map<String, String> namespaceBindings) throws BuildPatchException;

    /**
	 * Builds the patching instruction for the removal of node.
	 * 
	 * @param sel
	 *            the xpath selector, which points to the removed node.
	 * @param namespaceBindings
	 *            namespace bindings for undeclared namespaces in sel
	 * @return
	 * @throws BuildPatchException
	 *             if an exception occurred while building the patch, the
	 *             concrete exception is the exception's cause.
	 */
    public C removeNode(String sel, Map<String, String> namespaceBindings) throws BuildPatchException;

    /**
	 * Builds the patching instruction for the removal of a prefix namespace
	 * declaration.
	 * 
	 * @param sel
	 *            the xpath selector, which points to the removed prefix
	 *            namespace declaration.
	 * @param namespaceBindings
	 *            namespace bindings for undeclared namespaces in sel
	 * @return
	 * @throws BuildPatchException
	 *             if an exception occurred while building the patch, the
	 *             concrete exception is the exception's cause.
	 */
    public C removePrefixNamespaceDeclaration(String sel, Map<String, String> namespaceBindings) throws BuildPatchException;

    /**
	 * Builds a document patching instructions, by comparing two versions of a
	 * document. It requires documents to exist, that is this method should not
	 * be called for operations which add/remove document.

	 * @param originalDocument
	 * @param patchedDocument
	 * @return
	 * @throws BuildPatchException
	 *             if an exception occurred while building the patch, the
	 *             concrete exception is the exception's cause.
	 */
    public C[] buildPatchInstructions(D originalDocument, D patchedDocument) throws BuildPatchException;
}
