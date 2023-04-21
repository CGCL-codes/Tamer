package cx.ath.contribs.standardApp.BasicResponseDocTransformations;

import cx.ath.contribs.internal.xerces.dom.CoreDocumentImpl;
import cx.ath.contribs.internal.xerces.dom.NodeImpl;
import cx.ath.contribs.standardApp.attributedTree.businessLogic.StandardBusinessLogic;
import cx.ath.contribs.standardApp.attributedTree.businessLogic.StandardBusinessLogicEnvironment;
import cx.ath.contribs.standardApp.attributedTree.businessLogic.StandardBusinessLogicTransformer;

public class ResponseDocFrameBuilding<E extends StandardBusinessLogicEnvironment, C extends StandardBusinessLogic<E>> extends StandardBusinessLogicTransformer<E, C> {

    public ResponseDocFrameBuilding(C cat) {
        super(cat);
    }

    public NodeImpl transform(NodeImpl docNode) {
        try {
            CoreDocumentImpl doc = _env.getDocumentBuilder().newDocument();
            doc.addNamespaceDeclaration("app", "http://contribs.ath.cx/wljd/app").addNamespaceDeclaration("exf", "http://contribs.ath.cx/wljd/exf").addNamespaceDeclaration("ev", "http://www.w3.org/2001/xml-events").addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance").addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema").addChild(doc.createDocumentType("app:serverResponse", "", "")).addChild(doc.createElementNS("app:serverResponse").addIdAttribute("xml:id", "serverResponse").addAttribute("processingStatus", "processing").addChild(doc.createElementNS("app:responseProperties").addIdAttribute("xml:id", "responseProperties").addAttribute("actionId", docNode.getElement("requestProperties").getAttribute("actionId")).addAttribute("targetContentType", docNode.getElement("requestProperties").getAttribute("targetContentType")).addAttribute("responseId", docNode.getElement("requestProperties").getAttribute("responseId"))));
            _env.writeLog(doc, "Output of ResponseDocFrameBuilder");
            return doc;
        } catch (Exception ex) {
            return null;
        }
    }
}
