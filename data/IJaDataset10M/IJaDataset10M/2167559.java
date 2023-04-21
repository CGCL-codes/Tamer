package com.phloc.commons.microdom.utils;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import org.w3c.dom.Attr;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.impl.MicroCDATA;
import com.phloc.commons.microdom.impl.MicroComment;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.microdom.impl.MicroDocumentType;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.microdom.impl.MicroEntityReference;
import com.phloc.commons.microdom.impl.MicroProcessingInstruction;
import com.phloc.commons.microdom.impl.MicroText;

@Immutable
public final class MicroUtils {

    private static final String BR_TAG = "br";

    @PresentForCodeCoverage
    @SuppressWarnings("unused")
    private static final MicroUtils s_aInstance = new MicroUtils();

    private MicroUtils() {
    }

    @Nullable
    public static List<IMicroNode> nl2br(@Nullable final String sText) {
        if (sText == null) return null;
        final List<IMicroNode> ret = new ArrayList<IMicroNode>();
        int start = 0;
        while (true) {
            final int i = sText.indexOf('\n', start);
            if (i < 0) break;
            if (start != i) ret.add(new MicroText(sText.substring(start, i)));
            ret.add(new MicroElement(BR_TAG));
            start = i + 1;
        }
        if (start < sText.length()) ret.add(new MicroText(sText.substring(start)));
        return ret;
    }

    @Nonnull
    public static IMicroNode append(@Nonnull final IMicroNode aSrcNode, @Nullable final Object aChild) {
        if (aSrcNode == null) throw new NullPointerException("srcNode");
        if (aChild != null) if (aChild instanceof IMicroNode) {
            aSrcNode.appendChild((IMicroNode) aChild);
        } else if (aChild instanceof String) {
            aSrcNode.appendText((String) aChild);
        } else if (aChild instanceof Iterable<?>) {
            for (final Object aSubChild : (Iterable<?>) aChild) append(aSrcNode, aSubChild);
        } else if (ArrayHelper.isArray(aChild)) {
            for (final Object aSubChild : (Object[]) aChild) append(aSrcNode, aSubChild);
        } else {
            throw new IllegalArgumentException("Passed object cannot be appended to an IMicroNode (type=" + aChild.getClass().getName() + ".");
        }
        return aSrcNode;
    }

    /**
   * Get the path of the given node, up to the root element.
   * 
   * @param aNode
   *        The node to get the path from. May be <code>null</code>.
   * @param sSep
   *        The separator to be put between each level. For XPath e.g. use "/"
   * @return A non-<code>null</code> string. If the passed node is
   *         <code>null</code>, the return value is an empty string.
   */
    @Nonnull
    public static String getPath(@Nullable final IMicroNode aNode, @Nonnull final String sSep) {
        if (sSep == null) throw new NullPointerException("separator");
        final StringBuilder aSB = new StringBuilder();
        IMicroNode aCurrentNode = aNode;
        while (aCurrentNode != null) {
            if (aSB.length() > 0) aSB.insert(0, sSep);
            aSB.insert(0, aCurrentNode.getNodeName());
            aCurrentNode = aCurrentNode.getParent();
        }
        return aSB.toString();
    }

    /**
   * Get the tag name of the passed documents root element.
   * 
   * @param aDoc
   *        The document to be evaluated. May be <code>null</code>.
   * @return <code>null</code> if the passed document was <code>null</code> or
   *         if no document element is present. The tag name otherwise.
   */
    @Nullable
    public static String getDocumentRootElementTagName(@Nullable final IMicroDocument aDoc) {
        if (aDoc != null) {
            final IMicroElement eRoot = aDoc.getDocumentElement();
            if (eRoot != null) return eRoot.getTagName();
        }
        return null;
    }

    @Nonnull
    public static IMicroNode convertToMicroNode(@Nonnull final Node aNode) {
        if (aNode == null) throw new NullPointerException("node");
        IMicroNode ret;
        final short nNodeType = aNode.getNodeType();
        switch(nNodeType) {
            case Node.DOCUMENT_NODE:
                {
                    ret = new MicroDocument();
                    break;
                }
            case Node.DOCUMENT_TYPE_NODE:
                {
                    final DocumentType aDT = (DocumentType) aNode;
                    ret = new MicroDocumentType(aDT.getName(), aDT.getPublicId(), aDT.getSystemId());
                    break;
                }
            case Node.ELEMENT_NODE:
                {
                    final IMicroElement aElement = new MicroElement(aNode.getNodeName());
                    final NamedNodeMap aAttrs = aNode.getAttributes();
                    if (aAttrs != null) {
                        final int nAttrCount = aAttrs.getLength();
                        for (int i = 0; i < nAttrCount; ++i) {
                            final Attr aAttr = (Attr) aAttrs.item(i);
                            aElement.setAttribute(aAttr.getName(), aAttr.getValue());
                        }
                    }
                    ret = aElement;
                    break;
                }
            case Node.CDATA_SECTION_NODE:
                ret = new MicroCDATA(aNode.getNodeValue());
                break;
            case Node.TEXT_NODE:
                ret = new MicroText(aNode.getNodeValue());
                break;
            case Node.COMMENT_NODE:
                ret = new MicroComment(aNode.getNodeValue());
                break;
            case Node.ENTITY_REFERENCE_NODE:
                ret = new MicroEntityReference(aNode.getNodeValue());
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                final ProcessingInstruction aPI = (ProcessingInstruction) aNode;
                ret = new MicroProcessingInstruction(aPI.getTarget(), aPI.getData());
                break;
            case Node.ATTRIBUTE_NODE:
                throw new IllegalArgumentException("Unknown/unsupported node type: ATTRIBUTE_NODE");
            case Node.ENTITY_NODE:
                throw new IllegalArgumentException("Unknown/unsupported node type: ENTITY_NODE");
            case Node.DOCUMENT_FRAGMENT_NODE:
                throw new IllegalArgumentException("Unknown/unsupported node type: DOCUMENT_FRAGMENT_NODE");
            case Node.NOTATION_NODE:
                throw new IllegalArgumentException("Unknown/unsupported node type: NOTATION_NODE");
            default:
                throw new IllegalArgumentException("Unknown/unsupported node type: " + nNodeType);
        }
        final NodeList aChildren = aNode.getChildNodes();
        if (aChildren != null) {
            final int nChildCount = aChildren.getLength();
            for (int i = 0; i < nChildCount; ++i) {
                final Node aChildNode = aChildren.item(i);
                ret.appendChild(convertToMicroNode(aChildNode));
            }
        }
        return ret;
    }

    /**
   * Helper method to extract the text content of the child element denoted by
   * the parameter sChildElementName of the passed parent element.
   * 
   * @param eParentElement
   *        The parent element to use. May not be <code>null</code>.
   * @param sChildElementName
   *        The name of the child element who's text content is to be extracted.
   * @return <code>null</code> if the child element does not exist or the child
   *         element does not contain any text.
   */
    @Nullable
    public static String getChildTextContent(@Nonnull final IMicroElement eParentElement, @Nonnull final String sChildElementName) {
        final IMicroElement eChildElement = eParentElement.getFirstChildElement(sChildElementName);
        return eChildElement != null ? eChildElement.getTextContent() : null;
    }

    /**
   * Helper method to extract the text content of the child element denoted by
   * the parameter sChildElementName of the passed parent element. The read text
   * content is converted via the
   * {@link com.phloc.commons.typeconvert.TypeConverter} to the desired
   * destination type.
   * 
   * @param eParentElement
   *        The parent element to use. May not be <code>null</code>.
   * @param sChildElementName
   *        The name of the child element who's text content is to be extracted.
   * @param aDstClass
   *        The destination class. May not be <code>null</code>.
   * @return <code>null</code> if the child element does not exist or the child
   *         element does not contain any text.
   */
    @Nullable
    public static <DSTTYPE> DSTTYPE getChildTextContentWithConversion(@Nonnull final IMicroElement eParentElement, @Nonnull final String sChildElementName, @Nonnull final Class<DSTTYPE> aDstClass) {
        final IMicroElement eChildElement = eParentElement.getFirstChildElement(sChildElementName);
        return eChildElement != null ? eChildElement.getTextContentWithConversion(aDstClass) : null;
    }

    /**
   * Helper method to extract the text content of the child element denoted by
   * the parameters sNamespaceURI and sChildElementName of the passed parent
   * element.
   * 
   * @param eParentElement
   *        The parent element to use. May not be <code>null</code>.
   * @param sNamespaceURI
   *        The expected namespace URI of the element.
   * @param sChildElementName
   *        The name of the child element who's text content is to be extracted.
   * @return <code>null</code> if the child element does not exist or the child
   *         element does not contain any text.
   */
    @Nullable
    public static String getChildTextContent(@Nonnull final IMicroElement eParentElement, @Nonnull final String sNamespaceURI, @Nonnull final String sChildElementName) {
        final IMicroElement eChildElement = eParentElement.getFirstChildElement(sNamespaceURI, sChildElementName);
        return eChildElement != null ? eChildElement.getTextContent() : null;
    }

    /**
   * Helper method to extract the text content of the child element denoted by
   * the parameters sNamespaceURI and sChildElementName of the passed parent
   * element. The read text content is converted via the
   * {@link com.phloc.commons.typeconvert.TypeConverter} to the desired
   * destination type.
   * 
   * @param eParentElement
   *        The parent element to use. May not be <code>null</code>.
   * @param sNamespaceURI
   *        The expected namespace URI of the element.
   * @param sChildElementName
   *        The name of the child element who's text content is to be extracted.
   * @param aDstClass
   *        The destination class. May not be <code>null</code>.
   * @return <code>null</code> if the child element does not exist or the child
   *         element does not contain any text.
   */
    @Nullable
    public static <DSTTYPE> DSTTYPE getChildTextContentWithConversion(@Nonnull final IMicroElement eParentElement, @Nonnull final String sNamespaceURI, @Nonnull final String sChildElementName, @Nonnull final Class<DSTTYPE> aDstClass) {
        final IMicroElement eChildElement = eParentElement.getFirstChildElement(sNamespaceURI, sChildElementName);
        return eChildElement != null ? eChildElement.getTextContentWithConversion(aDstClass) : null;
    }
}
