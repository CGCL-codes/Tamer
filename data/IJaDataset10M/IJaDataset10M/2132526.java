package org.exmaralda.tagging;

import java.io.IOException;
import java.util.List;
import org.exmaralda.common.jdomutilities.IOUtilities;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

public class SextantTokenHandler implements org.annolab.tt4j.ProbabilityHandler<String> {

    Document sextantDocument;

    int count = 0;

    List<String> idList;

    org.jdom.Namespace xlinkNameSpace = org.jdom.Namespace.getNamespace("xlink", "http://www.w3.org/1999/xlink");

    public SextantTokenHandler() {
        try {
            sextantDocument = new IOUtilities().readDocumentFromResource("/org/exmaralda/tagging/DummySextantAnnotation.exa");
        } catch (JDOMException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void token(String token, String pos, String lemma) {
    }

    void setIDList(List<String> ids) {
        idList = ids;
    }

    public void probability(String pos, String lemma, double probability) {
        Element ann = new Element("ann");
        ann.setAttribute("id", "ann_" + Integer.toString(count));
        ann.setAttribute("href", "#" + idList.get(count), xlinkNameSpace);
        Element fs = new Element("fs");
        ann.addContent(fs);
        Element posElement = new Element("f");
        posElement.setAttribute("name", "pos");
        fs.addContent(posElement);
        Element posValue = new Element("symbol");
        posValue.setAttribute("value", pos);
        posElement.addContent(posValue);
        Element lemElement = new Element("f");
        lemElement.setAttribute("name", "lemma");
        fs.addContent(lemElement);
        Element lemValue = new Element("symbol");
        lemValue.setAttribute("value", lemma);
        lemElement.addContent(lemValue);
        Element probElement = new Element("f");
        probElement.setAttribute("name", "p-pos");
        fs.addContent(probElement);
        Element probValue = new Element("symbol");
        probValue.setAttribute("value", Double.toString(probability));
        probElement.addContent(probValue);
        sextantDocument.getRootElement().addContent(ann);
        count++;
    }
}
