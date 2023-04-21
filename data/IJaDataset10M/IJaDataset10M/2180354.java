package m2s.Scorm;

import java.io.IOException;
import m2s.utils.*;
import oracle.xml.parser.v2.XMLDocument;
import org.w3c.dom.Element;

/**
 *
 * @author v3r5_u5
 */
public class Lom extends XMLFile {

    private String path;

    private String title;

    public Lom(String fileName) {
        super(fileName);
    }

    @Override
    public void createEmptyDoc() {
        XMLDoc = new XMLDocument();
        XMLDoc.setVersion("1.0");
        XMLDoc.setEncoding("UTF-8");
        Element root = XMLDoc.createElement("lom");
        root.setAttribute("xmlns", "http://www.imsglobal.org/xsd/imsmd_rootv1p2p1");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xsi:schemaLocation", "http://www.imsglobal.org/xsd/imsmd_rootv1p2p1 imsmd_rootv1p2p1.xsd");
        Element generalEl = XMLDoc.createElement("general");
        root.appendChild(generalEl);
        Element lifeEl = XMLDoc.createElement("lifecycle");
        root.appendChild(lifeEl);
        Element metaEl = XMLDoc.createElement("metametadata");
        root.appendChild(metaEl);
        Element technicalEl = XMLDoc.createElement("technical");
        root.appendChild(technicalEl);
        Element educationalEl = XMLDoc.createElement("educational");
        root.appendChild(educationalEl);
        Element rightsEl = XMLDoc.createElement("rights");
        root.appendChild(rightsEl);
        Element classificationEl = XMLDoc.createElement("classification");
        root.appendChild(classificationEl);
        XMLDoc.appendChild(root);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
