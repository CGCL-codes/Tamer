package issrg.ontology.exporter;

import org.w3c.dom.*;
import issrg.ontology.*;

public class PolicyOntologyExporter {

    protected PolicyOntology ontology;

    public PolicyOntologyExporter(PolicyOntology ontology) {
        this.ontology = ontology;
    }

    public Document getPermisXML() throws PolicyExportException {
        XMLExporter exporter = new issrg.ontology.exporter.permis.PERMISExporter(this.ontology);
        return exporter.getXML();
    }

    public void writeToPERMISFile(String filename) throws PolicyExportException {
        XMLExporter exporter = new issrg.ontology.exporter.permis.PERMISExporter(this.ontology);
        exporter.writeToFile(filename);
    }

    public void writeToOWLFile(String filename) throws PolicyExportException {
        OWLExporter exporter = new OWLExporter(this.ontology);
        exporter.writeToFile(filename);
    }
}
