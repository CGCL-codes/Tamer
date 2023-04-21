package edu.url.lasalle.campus.scorm2004rte.server.validator.concreteParsers;

import java.util.Iterator;
import java.util.Vector;
import org.w3c.dom.Node;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.Files;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.Resource;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.server.validator.DOMTreeUtility;
import edu.url.lasalle.campus.scorm2004rte.server.validator.concreteParsers.ParseFile;

public class ParseResource {

    private Node resourceNode;

    private String identifier;

    private String scormType;

    private String href;

    private Resource resource = new Resource();

    private boolean error;

    private String error_message;

    private boolean searchHref(String href) {
        for (Iterator iteratorSearcher = resource.getFileIterator(); iteratorSearcher.hasNext(); ) {
            if (href.equals(((Files) iteratorSearcher.next()).href)) return true;
        }
        return false;
    }

    public boolean getError() {
        return error;
    }

    public String getErrorMessage() {
        return error_message;
    }

    public Resource getResource() {
        return resource;
    }

    public ParseResource(Node newResource, String path) {
        resourceNode = newResource;
        identifier = DOMTreeUtility.getAttributeValue(resourceNode, Constants.IDENTIFIER);
        if (identifier.length() == 0) {
            if (Constants.DEBUG_ERRORS || Constants.DEBUG_RESOURCES) System.out.println("[ERROR] a l'agafar l'identificador dels recursos!!");
        }
        resource.setIdentifier(identifier);
        scormType = DOMTreeUtility.getAttributeValue(resourceNode, Constants.RES_SCORMTYPE);
        if (scormType.length() == 0) {
            if (Constants.DEBUG_ERRORS || Constants.DEBUG_RESOURCES) System.out.println("ERROR a l'agafar l'scormType dels recursos!!");
        }
        resource.setScormType(scormType);
        href = DOMTreeUtility.getAttributeValue(resourceNode, Constants.HREF);
        if (href.length() == 0) {
            if (Constants.DEBUG_ERRORS || Constants.DEBUG_RESOURCES) System.out.println("ERROR a l'agafar l'href dels recursos!!");
        }
        resource.setHref(href);
        Vector filesInResources = DOMTreeUtility.getNodes(resourceNode, Constants.FILE);
        if (Constants.DEBUG_INFO || Constants.DEBUG_RESOURCES) System.out.println("[Resource-INICI]\t" + identifier + "\t" + scormType + "\t" + filesInResources.size());
        for (Iterator filesIterator = filesInResources.iterator(); filesIterator.hasNext(); ) {
            Node nouFile = (Node) filesIterator.next();
            if (nouFile == null) {
                if (Constants.DEBUG_ERRORS || Constants.DEBUG_RESOURCES) System.out.println("[ERROR] a l'agafar nouFile d'un resource!!");
            } else {
                ParseFile nouParseFile = new ParseFile(nouFile, path);
                resource.addFiles(nouParseFile.getFile());
            }
        }
        if (!searchHref(href)) {
            if (Constants.DEBUG_ERRORS || Constants.DEBUG_RESOURCES) System.out.println("[ERROR] dintre del resource '" + identifier + "'!!\n\tNo he trobat cap file que tingui '" + href + "'");
        }
        if (Constants.DEBUG_INFO || Constants.DEBUG_RESOURCES) System.out.println("[Resource-FINAL]");
    }
}
