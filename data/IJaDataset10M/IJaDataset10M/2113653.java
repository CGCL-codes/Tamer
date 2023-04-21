package fr.inria.zuist.app.ue;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class PaperInfo {

    String[] authorIDs;

    String[] keywordIDs;

    PaperInfo(Element e) {
        NodeList nl = ((Element) e.getElementsByTagName("authors").item(0)).getElementsByTagName("author");
        authorIDs = new String[nl.getLength()];
        for (int i = 0; i < nl.getLength(); i++) {
            authorIDs[i] = ((Element) nl.item(i)).getAttribute("idref");
        }
        nl = e.getElementsByTagName("keywords");
        if (nl.getLength() > 0) {
            nl = ((Element) nl.item(0)).getElementsByTagName("kw");
            keywordIDs = new String[nl.getLength()];
            for (int i = 0; i < nl.getLength(); i++) {
                keywordIDs[i] = ((Element) nl.item(i)).getAttribute("idref");
            }
        }
    }
}
