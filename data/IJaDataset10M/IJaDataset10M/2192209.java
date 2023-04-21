package wilos.tools.imports.epfcomposer.fillers;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import wilos.model.spem2.guide.Guidance;
import wilos.tools.imports.epfcomposer.parser.XMLParser;
import wilos.tools.imports.epfcomposer.utils.EncodingProcessor;

public class FillerGuidance extends FillerElement {

    private static String presentationName = "presentationName";

    private static String attachment_tag = "Attachment";

    /**
     * Constructor of FillerGuidance
     * 
     * @param _e
     * @param _aNode
     */
    public FillerGuidance(Guidance _e, Node _aNode) {
        super(_e, _aNode);
        fill();
    }

    /**
     * Fill a guidance list
     */
    private void fill() {
        String type = myNode.getAttributes().getNamedItem("xsi:type").getTextContent();
        type = type.substring(4, type.length());
        ((Guidance) myElement).setType(type);
        ((Guidance) myElement).setPresentationName(myNode.getAttributes().getNamedItem(presentationName).getNodeValue());
        NodeList l = myNode.getChildNodes();
        String mainDescription = "";
        String attachment = "";
        for (int i = 0; i < l.getLength(); i++) {
            if (l.item(i).getNodeName().equals(XMLParser.presentation)) {
                NodeList listDesc = l.item(i).getChildNodes();
                for (int j = 0; j < listDesc.getLength(); j++) {
                    if (listDesc.item(j).getNodeName().equals(XMLParser.maindescription)) {
                        mainDescription = listDesc.item(j).getTextContent();
                        if (type.equals(Guidance.example)) {
                            int posDebut = mainDescription.indexOf("href");
                            posDebut = mainDescription.indexOf('"', posDebut);
                            int posFin = mainDescription.indexOf('"', posDebut + 1);
                            if (posFin >= 0 && posDebut >= 0) {
                                attachment = EncodingProcessor.cleanString(mainDescription.substring(posDebut + 1, posFin));
                                mainDescription = "";
                            }
                        }
                    }
                    if (listDesc.item(j).getNodeName().equals(FillerGuidance.attachment_tag)) {
                        attachment = EncodingProcessor.cleanString(listDesc.item(j).getTextContent());
                        attachment = attachment.trim();
                    }
                }
            }
        }
        ((Guidance) myElement).setDescription(EncodingProcessor.cleanString(mainDescription));
        ((Guidance) myElement).setAttachment(attachment);
    }
}
