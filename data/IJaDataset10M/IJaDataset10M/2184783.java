package org.eclipse.eodm.examples.owl;

import java.io.IOException;
import org.eclipse.eodm.owl.OWLOntology;
import org.eclipse.eodm.owl.resource.OWLXMLSaver;
import org.eclipse.eodm.owl.resource.parser.OWLParser;
import org.eclipse.eodm.owl.resource.parser.impl.OWLParserImpl;
import org.eclipse.eodm.owl.resource.parser.OWLDocument;
import org.eclipse.eodm.owl.resource.parser.impl.OWLDocumentImpl;

/**
 * This example shows how to load and save owl ontologies from owl files.
 */
public class LoadAndSaveOWLbyOWLFile {

    public static void main(String[] args) {
        OWLParser parser = new OWLParserImpl();
        OWLDocument food = new OWLDocumentImpl("./testcase/food.owl", null, true);
        parser.addOWLDocument(food);
        OWLDocument wine = new OWLDocumentImpl("./testcase/wine.owl", "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine", true);
        parser.addOWLDocument(wine);
        OWLOntology ontofood = parser.parseOWLDocument(food);
        OWLOntology ontowine = parser.parseOWLDocument(wine);
        try {
            OWLXMLSaver.saveToFile(ontofood, "./testcase/food3.owl", "UTF-8");
            OWLXMLSaver.saveToFile(ontowine, "./testcase/wine3.owl", "UTF-8");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        parser.clear();
    }
}
