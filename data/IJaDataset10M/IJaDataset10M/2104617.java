package cx.ath.contribs.webFrame.ui.commonHtml;

import cx.ath.contribs.attributedTree.xml.transform.CategoryBase;
import cx.ath.contribs.internal.xerces.dom.CoreDocumentImpl;
import cx.ath.contribs.internal.xerces.dom.DocumentFragmentImpl;
import cx.ath.contribs.internal.xerces.dom.NodeImpl;
import cx.ath.contribs.webFrame.ui.WFUiEnvironment;
import cx.ath.contribs.webFrame.ui.WFUiTransformer;

/**
 * regelt die Verarbeitung von (in der Regel äußeren) param.group Elementen
 */
public class ParamForm<E extends WFUiEnvironment, C extends CategoryBase<E>> extends WFUiTransformer<E, C> {

    public ParamForm(C cat) {
        super(cat);
    }

    public ParamForm() {
        super();
    }

    protected String _pre = "";

    protected NodeImpl do_transform(NodeImpl node) {
        if (node.getAttribute("interchangeMode").equals("ignore")) return null;
        CoreDocumentImpl targetDoc = node.getOwnerDocument();
        _pre = _env.getHtmlNS().equals("") ? "" : _env.getHtmlNS() + ":";
        boolean hasCssClass = !node.getAttribute("cssClass").trim().equals("");
        boolean hasIdPrimitive = !node.getAttribute("idPrimitive").trim().equals("");
        NodeImpl captionNode = null;
        DocumentFragmentImpl childNodes = targetDoc.createDocumentFragment();
        if (!node.getAttribute("text").equals("") && !node.getAttribute("text").equals("adopt")) childNodes.addChild(targetDoc.createTextNode(node.getAttribute("text")));
        NodeImpl nextChild = node.getFirstChild();
        while (nextChild != null) {
            NodeImpl nextSibling = nextChild.getNextSibling();
            if (nextChild.getNodeName().equals("baseCH:info.caption")) {
                captionNode = nextChild;
            } else if (nextChild.getNodeType() == CoreDocumentImpl.ELEMENT_NODE) childNodes.addChild(nextChild);
            nextChild = nextSibling;
        }
        if (captionNode == null && !node.getAttribute("caption").equals("") && !node.getAttribute("caption").equals("adopt")) captionNode = targetDoc.createElementNS("baseCH:info.caption").addAttribute("level", node.getAttribute("level")).addAttribute("text", node.getAttribute("caption")).addAttribute("typeId", "121");
        NodeImpl result = targetDoc.createElementNS(_pre + "form").addAttribute("method", "post").addAttribute("action", _env.getBasePath() + "callWithParams");
        if (captionNode != null) result.addChild(captionNode.addAttribute("cssClass", hasCssClass ? node.getAttribute("cssClass") + "-caption" : "").addAttribute("idPrimitive", hasIdPrimitive ? node.getAttribute("idPrimitive") + "-caption" : ""));
        result.addAttribute("class", hasCssClass ? node.getAttribute("cssClass") : "").addAttribute("id", hasIdPrimitive ? node.getAttribute("idPrimitive") + "-form" : "").addChild(targetDoc.createElementNS(_pre + "div").addAttribute("class", hasCssClass ? node.getAttribute("cssClass") + "-innerSection" : "").addAttribute("id", hasIdPrimitive ? node.getAttribute("idPrimitive") + "-innerSection" : "").addChild(targetDoc.createElementNS(_pre + "input").addAttribute("name", "cpRootIndividualKey").addAttribute("value", _env.getCurrentCpRootIndividualKey()).addAttribute("type", "hidden")).addChild(targetDoc.createElementNS(_pre + "input").addAttribute("name", "paramFormId").addAttribute("value", node.getAttribute("xml:id")).addAttribute("type", "hidden")).addChild(childNodes));
        setNameAttributeOfDependantCalls(result, node.getAttribute("xml:id"));
        return result;
    }

    protected void setNameAttributeOfDependantCalls(NodeImpl node, String enclosingFormElementNameSpecifier) {
        NodeImpl nextChild = node.getFirstChild();
        while (nextChild != null) {
            NodeImpl nextSibling = nextChild.getNextSibling();
            if (nextChild.getNodeName().equals("baseCH:call")) {
                nextChild.addAttribute("enclosingFormElementNameSpecifier", enclosingFormElementNameSpecifier);
            } else if (nextChild.getNodeType() == CoreDocumentImpl.ELEMENT_NODE) {
                setNameAttributeOfDependantCalls(nextChild, enclosingFormElementNameSpecifier);
            }
            nextChild = nextSibling;
        }
    }
}
