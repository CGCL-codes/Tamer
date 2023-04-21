package org.openscience.cdk.applications.taverna.scuflworkers.cdk;

import org.embl.ebi.escience.scufl.DuplicateProcessorNameException;
import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scufl.ProcessorCreationException;
import org.embl.ebi.escience.scufl.ScuflModel;
import org.embl.ebi.escience.scufl.XScufl;
import org.embl.ebi.escience.scufl.parser.XScuflFormatException;
import org.embl.ebi.escience.scuflworkers.ProcessorFactory;
import org.embl.ebi.escience.scuflworkers.XMLHandler;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Handles XML store and load for the cdk local process processor
 * 
 * @author Tom Oinn
 * @author Thomas Kuhn
 */
public class CDKXMLHandler implements XMLHandler {

    private static final Namespace xscuflNamespace = XScufl.XScuflNS;

    public Element elementForProcessor(Processor p) {
        CDKProcessor lsp = (CDKProcessor) p;
        Element spec = new Element("cdk", xscuflNamespace);
        spec.setText(lsp.getWorkerClassName());
        if (lsp.getWorker() instanceof XMLExtensible) {
            spec.addContent(((XMLExtensible) lsp.getWorker()).provideXML());
        }
        return spec;
    }

    public Element elementForFactory(ProcessorFactory pf) {
        CDKProcessorFactory lspf = (CDKProcessorFactory) pf;
        Element spec = new Element("cdk", xscuflNamespace);
        spec.setText(lspf.getWorkerClassName());
        return spec;
    }

    public ProcessorFactory getFactory(Element specElement) {
        String workerClass = specElement.getTextTrim();
        String[] parts = workerClass.split("\\.");
        String descriptiveName = parts[parts.length - 1];
        return new CDKProcessorFactory(workerClass, descriptiveName);
    }

    public Processor loadProcessorFromXML(Element processorNode, ScuflModel model, String name) throws ProcessorCreationException, DuplicateProcessorNameException, XScuflFormatException {
        Element local = processorNode.getChild("cdk", xscuflNamespace);
        Element additionalInfo = local.getChild("extensions", xscuflNamespace);
        String workerClass = local.getTextTrim();
        if (additionalInfo == null) {
            return new CDKProcessor(model, name, workerClass);
        } else {
            return new CDKProcessor(model, name, workerClass, additionalInfo);
        }
    }
}
