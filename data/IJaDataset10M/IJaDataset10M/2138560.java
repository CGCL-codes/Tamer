package uk.ac.imperial.ma.metric.metricmlp;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Converts the MetricML nodes in XML DOM documents to something else - typically
 * MathML.  
 *
 * @author <a href="mailto:mail@daniel.may.name">Daniel J. R. May</a>
 * @version 0.1, 8 Dec 2008
 */
public class MetricMLProcessor {

    /** The namespace URI for metricml elements. */
    public static final String METRICML_NAMESPACE_URI = "http://metric.ma.imperial.ac.uk/metricml";

    /** 
	 * Get or create the default logger for the MetricMLProcessor application, 
	 * and tell it where that its messages can be found in the 
	 * <code>i18n/messages.properties</code> file.
	 */
    private static Logger logger = Logger.getLogger("uk.ac.imperial.ma.metric.metricmlp", "i18n/metricmlp");

    /**
	 * The translator for metricml elements which get converted to MathML.
	 * TODO change this to MathematicsTranslator when we have computerese and
	 * OpenMath in the mix.
	 */
    private MathematicsNodeTranslator mathsTranslator;

    /**
	 * The XML DOM document being processed.
	 */
    private Document document;

    /**
	 * Constructs.
	 *
	 * @param document the document to be processed 
	 * (by calling the <code>process</code> method.
	 */
    public MetricMLProcessor(Document document) {
        this.document = document;
        mathsTranslator = new MathematicsNodeTranslator(document);
    }

    /**
	 * Process the document, replacing the metricml nodes.
	 * 
	 * @return the processed document with all its metricml 
	 * nodes replaced.
	 */
    public Document process() {
        Node root = document.getDocumentElement();
        processNode(root);
        return document;
    }

    /**
	 * Checks if a node is a metricml node in which case it 
	 * is replaced; otherwise its children are recursed.
	 *
	 * @param node the node to process.
	 */
    private void processNode(Node node) {
        logger.log(Level.FINE, "PROCESSING_NODE", node);
        if (node.getNamespaceURI() != null && node.getNamespaceURI().equals(METRICML_NAMESPACE_URI)) {
            replaceNode(node);
        } else if (node.hasChildNodes()) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                processNode(children.item(i));
            }
        } else {
        }
    }

    /**
	 * Replace a metricml node within the document.
	 *
	 * @param node the metricml node which is to be replaced.
	 */
    private void replaceNode(Node node) {
        logger.log(Level.INFO, "REPLACING_NODE", node);
        String localTagName = node.getNodeName().substring(node.getNodeName().indexOf(':') + 1);
        logger.log(Level.FINE, "LOCAL_TAG_NAME", localTagName);
        Node replacement;
        if (localTagName.equals("ipmml")) {
            replacement = mathsTranslator.translateIpmml(node);
        } else if (localTagName.equals("bpmml")) {
            replacement = mathsTranslator.translateBpmml(node);
        } else if (localTagName.equals("button")) {
            logger.log(Level.SEVERE, "NO_REPLACEMENT_FOR_NODE", node);
            replacement = node;
        } else {
            logger.log(Level.SEVERE, "NO_REPLACEMENT_FOR_NODE", node);
            replacement = node;
        }
        node.getParentNode().replaceChild(replacement, node);
    }
}
