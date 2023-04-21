package org.xulbooster.eclipse.xb.ui.editors.xbl.tabletree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMWriter;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * This performs the work of taking a DOM tree and converting it to a
 * displayable 'UI' tree.
 * 
 * For example : - white space text nodes are ommited from the 'UI' tree -
 * adjacent Text and EntityReference nodes are combined into a single 'UI'
 * node - Elements with 'text only' children are diplayed without children
 * 
 */
public class TreeContentHelper {

    public static final int HIDE_WHITE_SPACE_TEXT_NODES = 8;

    public static final int COMBINE_ADJACENT_TEXT_AND_ENTITY_REFERENCES = 16;

    public static final int HIDE_ELEMENT_CHILD_TEXT_NODES = 32;

    protected int style = HIDE_WHITE_SPACE_TEXT_NODES | COMBINE_ADJACENT_TEXT_AND_ENTITY_REFERENCES | HIDE_ELEMENT_CHILD_TEXT_NODES;

    /**
	 * 
	 */
    public boolean hasStyleFlag(int flag) {
        return (style & flag) != 0;
    }

    /**
	 * 
	 */
    public Object[] getChildren(Object element) {
        Object[] result = null;
        if (element instanceof Node) {
            Node node = (Node) element;
            List list = new ArrayList();
            boolean textContentOnly = true;
            NamedNodeMap map = node.getAttributes();
            if (map != null) {
                int length = map.getLength();
                for (int i = 0; i < length; i++) {
                    list.add(map.item(i));
                    textContentOnly = false;
                }
            }
            Node prevIncludedNode = null;
            for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
                int childNodeType = childNode.getNodeType();
                boolean includeNode = true;
                if (includeNode && hasStyleFlag(HIDE_WHITE_SPACE_TEXT_NODES)) {
                    if (isIgnorableText(childNode)) {
                        includeNode = false;
                    }
                }
                if (includeNode && hasStyleFlag(COMBINE_ADJACENT_TEXT_AND_ENTITY_REFERENCES)) {
                    if (isTextOrEntityReferenceNode(childNode) && (prevIncludedNode != null) && isTextOrEntityReferenceNode(prevIncludedNode)) {
                        includeNode = false;
                    }
                }
                if (hasStyleFlag(HIDE_ELEMENT_CHILD_TEXT_NODES)) {
                    if ((childNodeType != Node.TEXT_NODE) && (childNodeType != Node.ENTITY_REFERENCE_NODE)) {
                        textContentOnly = false;
                    }
                }
                if (includeNode) {
                    list.add(childNode);
                    prevIncludedNode = childNode;
                }
            }
            if (hasStyleFlag(HIDE_ELEMENT_CHILD_TEXT_NODES) && textContentOnly) {
                result = new Object[0];
            } else {
                result = list.toArray();
            }
        }
        return result;
    }

    /**
	 * 
	 */
    protected boolean isTextOrEntityReferenceNode(Node node) {
        return (node.getNodeType() == Node.TEXT_NODE) || (node.getNodeType() == Node.ENTITY_REFERENCE_NODE);
    }

    /**
	 * 
	 */
    public boolean isIgnorableText(Node node) {
        boolean result = false;
        if (node.getNodeType() == Node.TEXT_NODE) {
            String data = ((Text) node).getData();
            result = ((data == null) || (data.trim().length() == 0));
        }
        return result;
    }

    /**
	 * 
	 */
    public boolean isCombinedTextNode(Node node) {
        boolean result = false;
        if (node.getNodeType() == Node.TEXT_NODE) {
            Node nextNode = node.getNextSibling();
            if (nextNode != null) {
                if (nextNode.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
                    result = true;
                }
            }
        } else if (node.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
            result = true;
        }
        return result;
    }

    /**
	 * 
	 */
    public List getCombinedTextNodeList(Node theNode) {
        List list = new Vector();
        boolean prevIsEntity = false;
        for (Node node = theNode; node != null; node = node.getNextSibling()) {
            int nodeType = node.getNodeType();
            if (nodeType == Node.ENTITY_REFERENCE_NODE) {
                prevIsEntity = true;
                list.add(node);
            } else if ((nodeType == Node.TEXT_NODE) && (prevIsEntity || (node == theNode))) {
                prevIsEntity = false;
                list.add(node);
            } else {
                break;
            }
        }
        return list;
    }

    public String getElementTextValue(Element element) {
        List list = _getElementTextContent(element);
        return list != null ? getValueForTextContent(list) : null;
    }

    public void setElementTextValue(Element element, String value) {
        setElementNodeValue(element, value);
    }

    private List _getElementTextContent(Element element) {
        List result = null;
        for (Node node = element.getFirstChild(); node != null; node = node.getNextSibling()) {
            if ((node.getNodeType() == Node.TEXT_NODE) || (node.getNodeType() == Node.ENTITY_REFERENCE_NODE)) {
                if (result == null) {
                    result = new Vector();
                }
                result.add(node);
            } else {
                result = null;
                break;
            }
        }
        return result;
    }

    /**
	 * If the element is has 'text only' content this method will return the
	 * list of elements that compose the text only content
	 */
    public List getElementTextContent(Element element) {
        List result = null;
        if (!element.hasAttributes()) {
            result = _getElementTextContent(element);
        }
        return result;
    }

    /**
	 * 
	 */
    public String getNodeValue(Node node) {
        String result = null;
        int nodeType = node.getNodeType();
        switch(nodeType) {
            case Node.ATTRIBUTE_NODE:
                {
                    result = ((Attr) node).getValue();
                    break;
                }
            case Node.CDATA_SECTION_NODE:
            case Node.COMMENT_NODE:
                {
                    result = ((CharacterData) node).getData();
                    break;
                }
            case Node.DOCUMENT_TYPE_NODE:
                {
                    result = getDocumentTypeValue((DocumentType) node);
                    break;
                }
            case Node.ELEMENT_NODE:
                {
                    result = getElementNodeValue((Element) node);
                    break;
                }
            case Node.ENTITY_REFERENCE_NODE:
            case Node.TEXT_NODE:
                {
                    result = getTextNodeValue(node);
                    break;
                }
            case Node.PROCESSING_INSTRUCTION_NODE:
                {
                    result = ((ProcessingInstruction) node).getData();
                    break;
                }
        }
        return result;
    }

    /**
	 * 
	 */
    public void setNodeValue(Node node, String value) {
        int nodeType = node.getNodeType();
        try {
            switch(nodeType) {
                case Node.ATTRIBUTE_NODE:
                    {
                        ((Attr) node).setValue(value);
                        break;
                    }
                case Node.CDATA_SECTION_NODE:
                case Node.COMMENT_NODE:
                    {
                        ((CharacterData) node).setData(value);
                        break;
                    }
                case Node.ELEMENT_NODE:
                    {
                        setElementNodeValue((Element) node, value);
                        break;
                    }
                case Node.ENTITY_REFERENCE_NODE:
                case Node.TEXT_NODE:
                    {
                        setTextNodeValue(node, value);
                        break;
                    }
                case Node.PROCESSING_INSTRUCTION_NODE:
                    {
                        ((ProcessingInstruction) node).setData(value);
                        break;
                    }
            }
        } catch (DOMException e) {
            Display d = getDisplay();
            if (d != null) {
                d.beep();
            }
        }
    }

    private Display getDisplay() {
        return PlatformUI.getWorkbench().getDisplay();
    }

    /**
	 * 
	 */
    protected String getDocumentTypeValue(DocumentType documentType) {
        return DOMWriter.getDocumentTypeData(documentType);
    }

    /**
	 * 
	 */
    protected String getElementNodeValue(Element element) {
        String result = null;
        List list = getElementTextContent(element);
        if (list != null) {
            result = getValueForTextContent(list);
        }
        return result;
    }

    /**
	 * 
	 */
    protected void setElementNodeValue(Element element, String value) {
        List list = getElementTextContent(element);
        if (list != null) {
            setValueForTextContent(list, value);
        } else {
            Document document = element.getOwnerDocument();
            Text text = document.createTextNode(value);
            element.appendChild(text);
        }
    }

    /**
	 * 
	 */
    protected String getTextNodeValue(Node node) {
        String result = null;
        List list = null;
        if (isCombinedTextNode(node)) {
            list = getCombinedTextNodeList(node);
        } else {
            list = new Vector();
            list.add(node);
        }
        result = getValueForTextContent(list);
        return result;
    }

    /**
	 * 
	 */
    protected void setTextNodeValue(Node node, String value) {
        List list = null;
        if (isCombinedTextNode(node)) {
            list = getCombinedTextNodeList(node);
        } else {
            list = new Vector();
            list.add(node);
        }
        setValueForTextContent(list, value);
    }

    public Text getEffectiveTextNodeForCombinedNodeList(List list) {
        Text result = null;
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            Node node = (Node) i.next();
            if (node.getNodeType() == Node.TEXT_NODE) {
                result = (Text) node;
                break;
            }
        }
        return result;
    }

    /**
	 * 
	 */
    protected String getValueForTextContent(List list) {
        String result = null;
        if (list.size() > 0) {
            IDOMNode first = (IDOMNode) list.get(0);
            IDOMNode last = (IDOMNode) list.get(list.size() - 1);
            IDOMModel model = first.getModel();
            int start = first.getStartOffset();
            int end = last.getEndOffset();
            try {
                result = model.getStructuredDocument().get(start, end - start);
            } catch (Exception e) {
            }
        }
        if (result != null) {
            result = result.trim();
        }
        return result;
    }

    /**
	 * 
	 */
    protected void setValueForTextContent(List list, String value) {
        if (list.size() > 0) {
            IDOMNode first = (IDOMNode) list.get(0);
            IDOMNode last = (IDOMNode) list.get(list.size() - 1);
            int start = first.getStartOffset();
            int end = last.getEndOffset();
            first.getModel().getStructuredDocument().replaceText(this, start, end - start, value);
        }
    }

    /**
	 * 
	 */
    public boolean isEditable(Node node) {
        int nodeType = node.getNodeType();
        boolean result = false;
        switch(nodeType) {
            case Node.ATTRIBUTE_NODE:
            case Node.CDATA_SECTION_NODE:
            case Node.COMMENT_NODE:
            case Node.ENTITY_REFERENCE_NODE:
            case Node.TEXT_NODE:
            case Node.PROCESSING_INSTRUCTION_NODE:
                {
                    result = true;
                    break;
                }
            case Node.ELEMENT_NODE:
                {
                    result = (getElementTextContent((Element) node) != null) || (node.getChildNodes().getLength() == 0);
                    break;
                }
        }
        return result;
    }
}
