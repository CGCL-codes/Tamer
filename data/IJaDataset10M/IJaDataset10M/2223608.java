package wilos.tools.imports.epfcomposer.fillers;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import wilos.model.spem2.workproduct.WorkProductDefinition;
import wilos.tools.imports.epfcomposer.utils.EncodingProcessor;

public class FillerWorkProduct extends FillerElement {

    private static String NodePresentation = "Presentation";

    private static String NodeImpactOfNotHaving = "ImpactOfNotHaving";

    private static String NodePurpose = "Purpose";

    private static String NodeReasonsForNotNeeding = "ReasonsForNotNeeding";

    private static String NodeBriefOutline = "BriefOutline";

    private static String NodeRepresentationOptions = "RepresentationOptions";

    /**
     * Constructor of FillerWorkProduct
     * 
     * @param _e
     * @param _aNode
     */
    public FillerWorkProduct(WorkProductDefinition _e, Node _aNode) {
        super(_e, _aNode);
        fill();
    }

    /**
     * Fill a WorkProduct list
     */
    public void fill() {
        NodeList myNodeList = myNode.getChildNodes();
        NodeList nodePresentationList = null;
        Node nodePresentation = null;
        String impactOfNotHaving = "";
        String purpose = "";
        String reasonsForNotNeeding = "";
        String briefOutline = "";
        String representationOptions = "";
        for (int i = 0; i < myNodeList.getLength() && nodePresentation == null; i++) {
            if (myNodeList.item(i).getNodeName().equals(NodePresentation)) {
                nodePresentation = myNodeList.item(i);
            }
        }
        if (nodePresentation != null) {
            nodePresentationList = nodePresentation.getChildNodes();
            for (int i = 0; i < nodePresentationList.getLength() && (impactOfNotHaving.equals("") || purpose.equals("") || reasonsForNotNeeding.equals("") || briefOutline.equals("") || representationOptions.equals("")); i++) {
                if (nodePresentationList.item(i).getNodeName().equals(NodeImpactOfNotHaving)) {
                    impactOfNotHaving = EncodingProcessor.cleanString(nodePresentationList.item(i).getTextContent());
                }
                if (nodePresentationList.item(i).getNodeName().equals(NodePurpose)) {
                    purpose = EncodingProcessor.cleanString(nodePresentationList.item(i).getTextContent());
                }
                if (nodePresentationList.item(i).getNodeName().equals(NodeReasonsForNotNeeding)) {
                    reasonsForNotNeeding = EncodingProcessor.cleanString(nodePresentationList.item(i).getTextContent());
                }
                if (nodePresentationList.item(i).getNodeName().equals(NodeBriefOutline)) {
                    briefOutline = EncodingProcessor.cleanString(nodePresentationList.item(i).getTextContent());
                }
                if (nodePresentationList.item(i).getNodeName().equals(NodeRepresentationOptions)) {
                    representationOptions = EncodingProcessor.cleanString(nodePresentationList.item(i).getTextContent());
                }
            }
        }
        ((WorkProductDefinition) myElement).setImpactOfNotHaving(impactOfNotHaving);
        ((WorkProductDefinition) myElement).setPurpose(purpose);
        ((WorkProductDefinition) myElement).setReasonsForNotNeeding(reasonsForNotNeeding);
        ((WorkProductDefinition) myElement).setBriefOutline(briefOutline);
        ((WorkProductDefinition) myElement).setRepresentationOptions(representationOptions);
    }
}
