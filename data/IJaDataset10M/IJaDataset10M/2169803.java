package de.tudresden.inf.rn.mobilis.services.cores;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class MapDrawKMLDocumentBuilder extends DocumentContentBuilder {

    public static final String FOLDER_NAME = "Mobilis MapDraw";

    public MapDrawKMLDocumentBuilder() {
        super();
        fileExtension = ".kml";
    }

    @Override
    public void createInitialDocumentStructure(Document doc) {
        Element root = doc.createElement("kml");
        root.setAttribute("xmlns", "http://www.opengis.net/kml/2.2");
        root.setAttribute("xmlns:gx", "http://www.google.com/kml/ext/2.2");
        root.setAttribute("xmlns:kml", "http://www.opengis.net/kml/2.2");
        root.setAttribute("xmlns:atom", "http://www.w3.org/2005/Atom");
        root.setAttribute("xmlns:mobilis", "http://www.tu-dresden.de/inf/rn/mobilis");
        doc.appendChild(root);
        Element docE = doc.createElement("Document");
        root.appendChild(docE);
        Element nameE = doc.createElement("name");
        Text nameT = doc.createTextNode("DrawingObjects.kml");
        nameE.appendChild(nameT);
        docE.appendChild(nameE);
        Element folderE = doc.createElement("Folder");
        docE.appendChild(folderE);
        Element nameFE = doc.createElement("name");
        Text nameFT = doc.createTextNode(FOLDER_NAME);
        nameFE.appendChild(nameFT);
        folderE.appendChild(nameFE);
        Element openE = doc.createElement("open");
        Text openT = doc.createTextNode("1");
        openE.appendChild(openT);
        folderE.appendChild(openE);
        Element extE = doc.createElement("ExtendedData");
        folderE.appendChild(extE);
    }
}
