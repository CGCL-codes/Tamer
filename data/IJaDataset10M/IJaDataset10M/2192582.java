package es.eucm.eadventure.editor.control.writer.domwriters;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import es.eucm.eadventure.common.auxiliar.ReportDialog;
import es.eucm.eadventure.common.data.chapter.elements.Atrezzo;
import es.eucm.eadventure.common.data.chapter.resources.Resources;

public class AtrezzoDOMWriter {

    /**
     * Private constructor.
     */
    private AtrezzoDOMWriter() {
    }

    public static Node buildDOM(Atrezzo atrezzo) {
        Element atrezzoElement = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            atrezzoElement = doc.createElement("atrezzoobject");
            atrezzoElement.setAttribute("id", atrezzo.getId());
            if (atrezzo.getDocumentation() != null) {
                Node atrezzoDocumentationNode = doc.createElement("documentation");
                atrezzoDocumentationNode.appendChild(doc.createTextNode(atrezzo.getDocumentation()));
                atrezzoElement.appendChild(atrezzoDocumentationNode);
            }
            for (Resources resources : atrezzo.getResources()) {
                Node resourcesNode = ResourcesDOMWriter.buildDOM(resources, ResourcesDOMWriter.RESOURCES_ITEM);
                doc.adoptNode(resourcesNode);
                atrezzoElement.appendChild(resourcesNode);
            }
            Node descriptionNode = doc.createElement("description");
            Element nameNode = doc.createElement("name");
            if (atrezzo.getDescription(0).getNameSoundPath() != null && !atrezzo.getDescription(0).getNameSoundPath().equals("")) {
                nameNode.setAttribute("soundPath", atrezzo.getDescription(0).getNameSoundPath());
            }
            nameNode.appendChild(doc.createTextNode(atrezzo.getDescription(0).getName()));
            descriptionNode.appendChild(nameNode);
            Element briefNode = doc.createElement("brief");
            briefNode.appendChild(doc.createTextNode(""));
            descriptionNode.appendChild(briefNode);
            Element detailedNode = doc.createElement("detailed");
            detailedNode.appendChild(doc.createTextNode(""));
            descriptionNode.appendChild(detailedNode);
            atrezzoElement.appendChild(descriptionNode);
        } catch (ParserConfigurationException e) {
            ReportDialog.GenerateErrorReport(e, true, "UNKNOWERROR");
        }
        return atrezzoElement;
    }
}
